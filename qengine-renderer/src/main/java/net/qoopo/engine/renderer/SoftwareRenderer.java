package net.qoopo.engine.renderer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.engine.EngineTime;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.UpdatableComponent;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.entity.component.mesh.primitive.QVertexBuffer;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.modifier.ModifierComponent;
import net.qoopo.engine.core.entity.component.particles.Particle;
import net.qoopo.engine.core.entity.component.particles.ParticleEmissor;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.math.QVector4;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.renderer.RenderOptions;
import net.qoopo.engine.core.renderer.superficie.Superficie;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.shadow.QProcesadorSombra;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.util.ComponentUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.renderer.raster.AbstractRaster;
import net.qoopo.engine.renderer.raster.ParallelRaster;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;
import net.qoopo.engine.renderer.shader.fragment.basico.StandardFragmentShader;
import net.qoopo.engine.renderer.shader.vertex.DefaultVertexShader;
import net.qoopo.engine.renderer.shader.vertex.VertexShader;
import net.qoopo.engine.renderer.shader.vertex.VertexShaderComponent;
import net.qoopo.engine.renderer.shadow.procesadores.QSombraCono;
import net.qoopo.engine.renderer.shadow.procesadores.QSombraDireccional;
import net.qoopo.engine.renderer.shadow.procesadores.QSombraDireccionalCascada;
import net.qoopo.engine.renderer.shadow.procesadores.QSombraOmnidireccional;
import net.qoopo.engine.renderer.util.TransformationVectorUtil;

/**
 * Renderizador interno. No utiliza aceleracion por Hardware
 *
 * @author alberto
 */
@Getter
@Setter
public class SoftwareRenderer extends RenderEngine {

    private static Logger logger = Logger.getLogger("SoftwareRenderer");

    // El que crea los triangulos y llama al shader en cada pixel
    protected AbstractRaster raster;
    // El sombreador (el que calcula el color de cada pixel)
    protected FragmentShader shader;

    protected DefaultVertexShader vertexShader;

    public SoftwareRenderer(Scene escena, Superficie superficie, int ancho, int alto) {
        super(escena, superficie, ancho, alto);
        raster = new ParallelRaster(this);
        shader = new StandardFragmentShader(this);
        vertexShader = new DefaultVertexShader();
    }

    public SoftwareRenderer(Scene escena, String nombre, Superficie superficie, int ancho, int alto) {
        super(escena, nombre, superficie, ancho, alto);
        raster = new ParallelRaster(this);
        shader = new StandardFragmentShader(this);
        vertexShader = new DefaultVertexShader();
    }

    @Override
    public long update() {
        try {
            if (!isLoading()) {
                if (frameBuffer != null) {
                    clean();
                    updateLigths();
                    render();
                    postProcess();
                }
            } else {
                showSplash();
            }
        } catch (Exception e) {
            logger.severe("Error en el render=" + name);
            e.printStackTrace();
        }
        poligonosDibujados = poligonosDibujadosTemp;
        beforeTime = System.currentTimeMillis();
        return beforeTime;
    }

    /**
     * Realiza la limpieza de pantalla
     */
    public void clean() {
        frameBuffer.clean();
        frameBuffer.fill(backColor);// llena el buffer de color con el color indicado, dura 1ms
    }

    /**
     * Realiza el proceso de renderizado
     *
     * @throws Exception
     */
    private void render() throws Exception {
        poligonosDibujadosTemp = 0;
        try {
            // La Matriz de vista es la inversa de la matriz de la camara.
            // Esto es porque la camara siempre estara en el centro y movemos el mundo
            // en direccion contraria a la camara.
            QMatriz4 matrizVista = camera.getMatrizTransformacion(QGlobal.time).invert();
            QMatriz4 matrizVistaInvertidaBillboard = camera.getMatrizTransformacion(QGlobal.time);
            // caras solidas
            scene.getEntities().stream()
                    .filter(entity -> entity.isToRender())
                    .parallel()
                    .forEach(entity -> renderEntity(entity, matrizVista, matrizVistaInvertidaBillboard, false));

            shadeFragments();
            // caras transperentes
            scene.getEntities().stream()
                    .filter(entity -> entity.isToRender())
                    .parallel()
                    .forEach(entity -> renderEntity(entity, matrizVista, matrizVistaInvertidaBillboard, true));

            shadeFragments();
        } catch (Exception e) {
            System.out.println("Error render:" + name);
            e.printStackTrace();
        }
    }

    /**
     * Se encarga de pasar el fragment shader en caso de ser diferido
     */
    public void shadeFragments() {
        if (!isLoading()) {
            if (opciones.isDefferedShadding()) {
                if (getFrameBuffer() != null) {
                    // toda la pantalla
                    // shadeFragments(0, getFrameBuffer().getAncho(), 0,
                    // getFrameBuffer().getAlto());
                    // divide la pantalla en secciones
                    int sections = 4;
                    int widthSection = getFrameBuffer().getWidth() / sections;
                    int heigthSection = getFrameBuffer().getHeight() / sections;
                    // // // procesa cada seccion en paralelo
                    IntStream.range(0, sections)
                            .parallel()
                            .forEach(i -> IntStream.range(0, sections)
                                    .parallel()
                                    .forEach(j -> {
                                        shadeFragments(i * widthSection, widthSection, j * heigthSection,
                                                heigthSection);
                                    }));
                }
            }
        }
    }

    /**
     * Renderiza una seccion del frmeBuffer
     * 
     * @param xFrom
     * @param width
     * @param yFrom
     * @param height
     */
    private void shadeFragments(int xFrom, int width, int yFrom, int height) {
        if (!isLoading()) {
            if (getFrameBuffer() != null && getShader() != null) {
                for (int x = xFrom; x < xFrom + width; x++) {
                    for (int y = yFrom; y < yFrom + height; y++) {
                        if (getFrameBuffer().getFragment(x, y) != null && getFrameBuffer().getFragment(x, y).isDraw()) {
                            try { // busca un shader para esta entidad
                                FragmentShader shader = getShader();

                                // if (getFrameBuffer().getPixel(x, y).entity != null) {
                                // FragmentShaderComponent qshader = (FragmentShaderComponent)
                                // ComponentUtil.getComponent(
                                // getFrameBuffer().getPixel(x, y).entity,
                                // FragmentShaderComponent.class);

                                // if (qshader != null && qshader.getShader() != null) {
                                // shader = qshader.getShader();
                                // }
                                // shader.setRender(this);
                                // }

                                getFrameBuffer().setQColor(x, y,
                                        shader.shadeFragment(
                                                getFrameBuffer().getFragment(x, y), x, y));
                                if (QGlobal.ENABLE_GAMMA_FIX)
                                    getFrameBuffer().getColor(x, y).fixGamma();

                                getFrameBuffer().getFragment(x, y).setDraw(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    // Dibuja sobre la superficie
    public void draw() {
        if (!isLoading()) {
            if (frameBuffer != null) {
                if (renderReal) {
                    showStats(frameBuffer.getBufferColor());
                }

                if (renderReal
                        && (this.getSuperficie() != null
                                && this.getSuperficie().getComponente() != null
                                && this.getSuperficie().getComponente().getGraphics() != null)) {
                    this.getSuperficie().getComponente().setImagen(frameBuffer.getRendered());
                    this.getSuperficie().getComponente().repaint();
                }
            }
        }
    }

    /**
     * Realiza el renderizado de un entity
     * 
     * @param entity
     * @param matrizVista
     * @param matrizVistaInvertidaBillboard
     */
    public void renderEntity(Entity entity, QMatriz4 matrizVista, QMatriz4 matrizVistaInvertidaBillboard,
            boolean transparentes) {

        // logger.info("[+] Renderizando entidad : " + entity.getName());
        // La matriz vistaModelo es el resultado de multiplicar la matriz de vista por
        // la matriz del modelo
        // De esta forma es la matriz que se usa para transformar el modelo a las
        // coordenadas del mundo
        // luego de estas coordenadas se transforma a las coordenadas de la camara
        QMatriz4 matVistaModelo;

        // La matriz modelo contiene la información del modelo
        // Traslación, rotacion (en su propio eje ) y escala
        QMatriz4 matrizModelo;

        // salta las camaras si no esta activo el dibujo de las camaras
        if (entity instanceof Camera && !opciones.isDibujarLuces()) {
            return;
        }
        // salta el dibujo de la camara que esta usando el render
        if (entity instanceof Camera && entity.equals(this.camera)) {
            return;
        }

        // Matriz de modelo
        // obtiene la matriz de informacion concatenada con los padres
        matrizModelo = entity.getMatrizTransformacion(QGlobal.time);

        // ------------------------------------------------------------
        // Matriz VistaModelo
        // obtiene la matriz de transformacion del objeto combinada con la matriz de
        // vision de la camara
        matVistaModelo = matrizVista.mult(matrizModelo);

        // // busca un shader personalizado
        // FragmentShaderComponent qshader = (FragmentShaderComponent)
        // ComponentUtil.getComponent(entity,
        // FragmentShaderComponent.class);

        // FragmentShader shader = this.shader;
        // if (qshader != null && qshader.getShader() != null) {
        // shader = qshader.getShader();
        // }
        // shader.setRender(this);

        // busca un vertexShader personalizado
        VertexShaderComponent vertexShaderComponent = (VertexShaderComponent) ComponentUtil.getComponent(entity,
                VertexShaderComponent.class);
        VertexShader tmpVertexShader = this.vertexShader;
        if (vertexShaderComponent != null && vertexShaderComponent.getShader() != null) {
            tmpVertexShader = vertexShaderComponent.getShader();
        }

        final VertexShader vertexShader = tmpVertexShader;

        entity.actualizarRotacionBillboard(matrizVistaInvertidaBillboard);

        // componentes - como particulas o CubeMaps
        ParticleEmissor emisor = null;
        for (EntityComponent component : entity.getComponents()) {
            if (component instanceof UpdatableComponent) {
                if (((UpdatableComponent) component).isRequiereUpdate())
                    ((UpdatableComponent) component).update(this, scene);
                // particulas
            } else if (component instanceof ParticleEmissor) {
                emisor = (ParticleEmissor) component;
                emisor.emitir(EngineTime.deltaNano);
                for (Particle particula : emisor.getParticulasNuevas()) {
                    scene.addEntity(particula.objeto);
                }
            }
        }

        // primero ejecuta los componentes y modificadores
        entity.getComponents(Mesh.class)
                .stream()
                .parallel()
                .forEach(comp -> {
                    Mesh mesh = (Mesh) comp;

                    if (mesh != null) {
                        // actualizo la marca de tiempo, solo se usa para saber si se debe volver a
                        // calcular los modificadores
                        if (mesh.getTimeMark() == 0L) {
                            mesh.updateTimeMark();
                        }

                        // Recorre los modificadores para preguntar si hay algun cambio
                        for (EntityComponent component : entity.getComponents(ModifierComponent.class)) {
                            if (((ModifierComponent) component).isChanged()) {
                                mesh.updateTimeMark();
                                break;
                            }
                        }

                        Mesh tmpMesh = mesh.getCacheMesh();
                        if (tmpMesh == null || tmpMesh.getTimeMark() != mesh.getTimeMark()) {
                            logger.info("Actualizando malla por modificadores " + entity.getName());
                            tmpMesh = mesh.clone();// no destructivo, las modificaciones se realizan sobre la copia
                            tmpMesh.setEntity(entity);
                        }
                        // aplica modificadores
                        for (EntityComponent component : entity.getComponents()) {
                            if (component instanceof ModifierComponent) {
                                ((ModifierComponent) component).apply(tmpMesh);
                            }
                        }

                        mesh.setCacheMesh(tmpMesh);
                        final Mesh modifiedMesh = tmpMesh;

                        // Mesh modifiedMesh = (Mesh) entity.getComponent(Mesh.class);

                        // rasterizacion caras
                        List.of(modifiedMesh.primitiveList).stream()
                                .parallel()
                                .forEach(primitive -> {
                                    if (primitive.material == null
                                            // q no tengra transparencia cuando tiene el tipo de material basico
                                            || (primitive.material instanceof Material
                                                    && transparentes == ((Material) primitive.material)
                                                            .isTransparencia())) {

                                        // transformamos los vertices por cada poligono

                                        try {
                                            QVertexBuffer bufferVertices = new QVertexBuffer();
                                            bufferVertices.init(
                                                    modifiedMesh.vertexList.length,
                                                    modifiedMesh.normalList.length,
                                                    modifiedMesh.uvList.length);

                                            // calcula las transformaciones de los vértices
                                            // de 0 a n donde n es el numero de vertices del poligono o primitva (linea)
                                            for (int polyVertexIndex = 0; polyVertexIndex < primitive.vertexIndexList.length; polyVertexIndex++) {

                                                // indice del vértice de la malla
                                                int vertexIndex = primitive.vertexIndexList[polyVertexIndex];
                                                int normalIndex = 0;
                                                int uvIndex = 0;

                                                if (primitive.normalIndexList.length > polyVertexIndex)
                                                    normalIndex = primitive.normalIndexList[polyVertexIndex];

                                                if (primitive.uvIndexList.length > polyVertexIndex)
                                                    uvIndex = primitive.uvIndexList[polyVertexIndex];

                                                Vertex vertex = new Vertex();
                                                QVector3 vertexNormal = new QVector3();
                                                QVector2 vertexUV = new QVector2();

                                                // el vertice que corresponde de la malla
                                                vertex.set(modifiedMesh.vertexList[vertexIndex]);

                                                // la normal que corresponde de la malla
                                                if (normalIndex >= 0 && modifiedMesh.normalList.length > normalIndex)
                                                    vertexNormal.set(modifiedMesh.normalList[normalIndex]);

                                                // la coordenada uv que corresponde de la malla
                                                if (uvIndex >= 0 && modifiedMesh.uvList.length > uvIndex)
                                                    vertexUV.set(modifiedMesh.uvList[uvIndex]);

                                                bufferVertices.setVertex(
                                                        vertexShader.apply(vertex, vertexNormal, vertexUV,
                                                                QColor.WHITE,
                                                                matVistaModelo),
                                                        vertexIndex);
                                                if (normalIndex >= 0 && modifiedMesh.normalList.length > normalIndex)
                                                    bufferVertices.setNormal(vertexNormal, normalIndex);
                                                if (uvIndex >= 0 && modifiedMesh.uvList.length > uvIndex)
                                                    bufferVertices.setUV(vertexUV, uvIndex);

                                            }

                                            if (primitive instanceof Poly) {
                                                Poly poly = (Poly) primitive;
                                                // vuelve a calcular la normal y el centro en función de los vértices
                                                // transformados, funciona para las
                                                // animaciones, donde la transformacion de la normal no da los
                                                // resultados esperados, porq no tenemos la matriz del hueso
                                                poly.computeNormalCenter(bufferVertices.getVertexList());
                                                if (poly.isNormalInversa()) {
                                                    // invierto la normal en caso detener la marca de normal inversa
                                                    poly.getNormal().flip();
                                                    poly.getNormalCopy().flip();
                                                }
                                            }

                                            poligonosDibujadosTemp++;
                                            raster.raster(matVistaModelo, bufferVertices, primitive, opciones
                                                    .getTipoVista() == RenderOptions.VISTA_WIRE
                                                    || primitive.mesh.type == Mesh.GEOMETRY_TYPE_WIRE);
                                        } catch (Exception ex) {
                                            logger.severe("[X] Error al dibujar entidad : " + entity.getName()
                                                    + "   -> " + ex.getLocalizedMessage());
                                            ex.printStackTrace();
                                        }
                                    }
                                });
                    }
                });

    }

    /**
     * Ejecuta los efectosPotProcesamiento
     */
    private void postProcess() {
        if (filterQueue != null) {
            Texture inputTexture = frameBuffer.getBufferColor();

            AtomicReference<Texture> textureWrapper = new AtomicReference<Texture>(inputTexture);
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
            filterQueue.forEach(filter -> {
                try {
                    ImageIO.write(textureWrapper.get().getImagen(), "png",
                            new File("/tmp/filter_0_" + sf.format(new Date()) + ".png"));
                } catch (IOException ex) {

                }
                textureWrapper.set(filter.apply(textureWrapper.get()));
                try {
                    ImageIO.write(textureWrapper.get().getImagen(), "png",
                            new File("/tmp/filter_1_" + sf.format(new Date()) + ".png"));
                } catch (IOException ex) {

                }
            });

            frameBuffer.setBufferColor(textureWrapper.get());
            frameBuffer.updateOuputTexture();
        }
    }

    /**
     * Actualiza la información de las luces y de las sombras, procesadores de
     * sombras
     */
    private void updateLigths() {
        try {
            litgths.clear();
            // for (Entity entity : escena.getListaEntidades()) {
            scene.getEntities().stream().filter(entity -> entity.isToRender()).parallel()
                    .forEach(entity -> {
                        entity.getComponents().stream().parallel().forEach(componente -> {
                            if (componente instanceof QLigth) {
                                QLigth luz = ((QLigth) componente);
                                if (!litgths.contains(luz)) {
                                    litgths.add(luz);
                                }

                                // la direccion de la luz en el espacio de la camara (Se usa para el calculo de
                                // la iluminacion)
                                QVector3 direccionLuzEspacioCamara = QVector3.zero;
                                // la direccion de la luz en el espacio mundial(no se aplica la transformacion
                                // de la camara) (Se usa en el calculo de la sombra)
                                QVector3 direccionLuzMapaSombra = QVector3.zero;

                                if (luz instanceof QDirectionalLigth) {
                                    direccionLuzEspacioCamara = ((QDirectionalLigth) componente).getDirection();
                                    direccionLuzMapaSombra = ((QDirectionalLigth) componente).getDirection();
                                } else if (luz instanceof QSpotLigth) {
                                    direccionLuzEspacioCamara = ((QSpotLigth) componente).getDirection();
                                    direccionLuzMapaSombra = ((QSpotLigth) componente).getDirection();
                                }

                                // Al igual que con el buffer de vertices se aplica la transformacion, a esta
                                // copia de la luz
                                // se le aplica la transformacion para luego calcular la ilumnicacion.
                                // La ilumnicacion se calcula usando lso vertices ya transformados
                                // luz.entity.getTransformacion().getLocation().set(QTransformar.transformarVector(QVector3.zero,
                                // entity, camara));
                                // actualiza la dirección de la luz
                                direccionLuzEspacioCamara = TransformationVectorUtil
                                        .transformarVectorNormal(direccionLuzEspacioCamara, entity, camera);
                                direccionLuzMapaSombra = TransformationVectorUtil.transformarVectorNormal(
                                        direccionLuzMapaSombra,
                                        entity.getMatrizTransformacion(QGlobal.time));

                                if (luz instanceof QDirectionalLigth) {
                                    ((QDirectionalLigth) luz).setDirectionTransformada(direccionLuzEspacioCamara);
                                } else if (luz instanceof QSpotLigth) {
                                    ((QSpotLigth) luz).setDirectionTransformada(direccionLuzEspacioCamara);
                                }

                                // Actualiza procesadores de sombras
                                if (opciones.isSombras() && opciones.isMaterial() && luz.isProyectarSombras()) {
                                    // si no existe crea uno nuevo
                                    QProcesadorSombra proc = null;
                                    if (luz.getSombras() == null) {
                                        if (luz instanceof QDirectionalLigth) {
                                            if (QGlobal.SOMBRAS_DIRECCIONALES_CASCADA) {
                                                proc = new QSombraDireccionalCascada(QGlobal.SOMBRAS_CASCADAS_TAMANIO,
                                                        scene, (QDirectionalLigth) componente, camera,
                                                        luz.getResolucionMapaSombra(), luz.getResolucionMapaSombra());
                                            } else {
                                                proc = new QSombraDireccional(scene, (QDirectionalLigth) componente,
                                                        camera, luz.getResolucionMapaSombra(),
                                                        luz.getResolucionMapaSombra());
                                            }
                                            // logger.info("Creado pocesador de sombra Direccional con clave "
                                            // + entity.getName());
                                        } else if (luz instanceof QPointLigth) {
                                            proc = new QSombraOmnidireccional(scene, (QPointLigth) componente, camera,
                                                    luz.getResolucionMapaSombra(), luz.getResolucionMapaSombra());
                                            // logger.info("Creado pocesador de sombra Omnidireccional con clave "
                                            // + entity.getName());
                                        } else if (luz instanceof QSpotLigth) {
                                            proc = new QSombraCono(scene, (QSpotLigth) componente, camera,
                                                    luz.getResolucionMapaSombra(), luz.getResolucionMapaSombra());
                                            // logger.info("Creado pocesador de sombra Cónica con clave "
                                            // + entity.getName());
                                        }
                                        if (proc != null) {
                                            proc.setDinamico(luz.isSombraDinamica());
                                            luz.setSombras(proc);
                                        }
                                    } else {
                                        // si ya existe un procesador de sombras
                                        // si el direccional actualizo la direccion de la luz del procesador de sombra
                                        // de acuerdo a la entity
                                        if (luz instanceof QDirectionalLigth) {
                                            proc = (QSombraDireccional) luz.getSombras();
                                            ((QSombraDireccional) proc).actualizarDireccion(direccionLuzMapaSombra);
                                            if (forzarActualizacionMapaSombras) {
                                                proc.setActualizar(true);
                                            }
                                            proc.generarSombras();
                                        } else if (luz instanceof QSpotLigth) {
                                            proc = (QSombraCono) luz.getSombras();
                                            ((QSombraCono) proc).actualizarDireccion(direccionLuzMapaSombra);
                                        }
                                    }
                                    if (proc != null) {
                                        if (forzarActualizacionMapaSombras) {
                                            proc.setActualizar(true);
                                        }
                                        proc.generarSombras();
                                    }
                                } else {
                                    luz.setSombras(null);
                                }
                            }
                        });
                    });
        } catch (Exception e) {
        } finally {
            forzarActualizacionMapaSombras = false;
        }
    }

    /**
     * Permite la selección de un objeto con el mouse
     *
     * @param mouseLocation
     * @return
     */
    public Entity selectEntity(QVector2 mouseLocation) {
        try {
            if (opciones.isForzarResolucion() && this.getSuperficie() != null) {
                mouseLocation.x = mouseLocation.x * opciones.getAncho()
                        / this.getSuperficie().getComponente().getWidth();
                mouseLocation.y = mouseLocation.y * opciones.getAlto()
                        / this.getSuperficie().getComponente().getHeight();
            }

            QVector2 ubicacionLuz = new QVector2();
            QVector3 tmp;
            // Selección de luces
            for (Entity entity : scene.getEntities()) {
                if (entity.isToRender()) {
                    for (EntityComponent componente : entity.getComponents()) {
                        if (componente instanceof QLigth) {
                            tmp = TransformationVectorUtil.transformarVector(QVector3.zero, entity, camera);
                            camera.getCoordenadasPantalla(ubicacionLuz, new QVector4(tmp, 1), frameBuffer.getWidth(),
                                    frameBuffer.getHeight());
                            if ((ubicacionLuz.x - mouseLocation.x)
                                    * (ubicacionLuz.x - mouseLocation.x)
                                    + (ubicacionLuz.y - mouseLocation.y)
                                            * (ubicacionLuz.y - mouseLocation.y) <= lightOnScreenSize
                                                    * lightOnScreenSize / 4) {
                                return entity;
                            }
                        }
                    }
                }
            }

            // metodo donde ordeno las caras y veo si lo que selecciona esta dentro de las
            // coordenadas de pantalla de la cara
            // Arrays.sort(carasAdibujarOrdenadas);
            //// for (QPoligono primitiva : carasAdibujarOrdenadas) {
            //// for (int ii=0;ii< carasAdibujarOrdenadas.length;ii++) {
            // for (int ii = carasAdibujarOrdenadas.length - 1; ii > 0; ii--) {
            //
            // QPoligono primitiva = carasAdibujarOrdenadas[ii];
            // Point[] facePoints = new Point[primitiva.listaVertices.length];
            // int j = 0;
            // for (int i : primitiva.listaVertices) {
            // facePoints[j] = new Point();
            // camara.proyectar(facePoints[j++], bufferVerticesTransformados.getVertice(i));
            // }
            // if (pointBelongsToPlane(mouseLocation, facePoints)) {
            // return primitiva.geometria.entity;
            // }
            // }
            // metodo donde tomo las coordenadas de pantalla del cursor y veo en buffer el
            // pixel
            Fragment pixel = frameBuffer.getFragment((int) mouseLocation.x, (int) mouseLocation.y);
            if (pixel != null) {
                return pixel.entity;
            }

        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void renderLine(QMatriz4 matViewModel, Primitive primitiva, Vertex... vertex) {
        raster.rasterLine(matViewModel, primitiva, vertex);
    }

    @Override
    public void render(QMatriz4 matViewModel, QVertexBuffer bufferVertices, Primitive primitiva, boolean wire) {
        raster.raster(matViewModel, bufferVertices, primitiva, wire);
    }

    public void setShader(Object shader) {
        if (shader instanceof FragmentShader) {
            this.shader = (FragmentShader) shader;
            this.shader.setRender(this);
        }
    }

}
