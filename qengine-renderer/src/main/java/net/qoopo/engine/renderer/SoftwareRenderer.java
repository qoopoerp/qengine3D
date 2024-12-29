package net.qoopo.engine.renderer;

import java.util.List;
import java.util.stream.IntStream;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.transform.QVertexBuffer;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.math.QVector4;
import net.qoopo.engine.core.renderer.QOpcionesRenderer;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.renderer.superficie.Superficie;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.shadow.QProcesadorSombra;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.QUtilComponentes;
import net.qoopo.engine.core.util.TempVars;
import net.qoopo.engine.renderer.raster.AbstractRaster;
import net.qoopo.engine.renderer.raster.ParallelRaster;
import net.qoopo.engine.renderer.raster.Raster;
import net.qoopo.engine.renderer.raster.Raster2;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;
import net.qoopo.engine.renderer.shader.fragment.FragmentShaderComponent;
import net.qoopo.engine.renderer.shader.fragment.proxy.QFlatProxyShader;
import net.qoopo.engine.renderer.shader.fragment.proxy.QFullProxyShader;
import net.qoopo.engine.renderer.shader.fragment.proxy.QLigthProxyShader;
import net.qoopo.engine.renderer.shader.fragment.proxy.QPbrProxyShader;
import net.qoopo.engine.renderer.shader.fragment.proxy.QPhongProxyShader;
import net.qoopo.engine.renderer.shader.fragment.proxy.QShadowProxyShader;
import net.qoopo.engine.renderer.shader.fragment.proxy.QSimpleProxyShader;
import net.qoopo.engine.renderer.shader.fragment.proxy.QTexturaProxyShader;
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

    // private static Logger logger = Logger.getLogger("qrender");
    // El sombreador (el que calcula el color de cada pixel)
    protected FragmentShader shader;
    // El sombreador default (el que calcula el color de cada pixel)
    protected FragmentShader defaultShader;
    // El que crea los triangulos y llama al shader en cada pixel
    protected AbstractRaster raster;

    protected VertexShader vertexShader = new VertexShader();
    protected VertexShader defaultVertexShader = new VertexShader();

    public SoftwareRenderer(Scene escena, Superficie superficie, int ancho, int alto) {
        super(escena, superficie, ancho, alto);
        defaultShader = new QFullProxyShader(this);
        raster = new Raster(this);
        // cambiarShader(7);//pbr
    }

    public SoftwareRenderer(Scene escena, String nombre, Superficie superficie, int ancho, int alto) {
        super(escena, nombre, superficie, ancho, alto);
        defaultShader = new QFullProxyShader(this);
        raster = new Raster(this);
        // cambiarShader(7);//pbr
    }

    @Override
    public long update() {
      
        try {
            if (!isCargando()) {
                if (frameBuffer != null) {
                    // logger.info("");
                    // logger.info("======================= QRENDER =======================");
                    // logger.info("");
                    getSubDelta();
                    setColorFondo(escena.getAmbientColor());// pintamos el fondo con el color ambiente
                    clean();
                    // logger.info("P1. Limpiar pantalla=" + getSubDelta());
                    updateLigths();
                    // logger.info("P2. Actualizar luces y sombras=" + getSubDelta());
                    // logger.info("P5. ----Renderizado-----");
                    render();

                    // shadeFragments();
                    // postRender();

                    // logger.info("MR-->" + DF.format(getFPS()) + " FPS");
                    // logger.info("");
                    // logger.info("");
                    // logger.info("");
                }
            } else {
                mostrarSplash();
            }
        } catch (Exception e) {
            // logger.info("Error en el render=" + nombre);
            e.printStackTrace();

        }
        poligonosDibujados = poligonosDibujadosTemp;
        tiempoPrevio = System.currentTimeMillis();
        return tiempoPrevio;
    }

    
    /**
     * Realiza la limpieza de pantalla
     */
    protected void clean() {
        // frameBuffer.limpiarZBuffer(); // limpia el buffer de profundidad
        frameBuffer.clean();
        frameBuffer.llenarColor(colorFondo);// llena el buffer de color con el color indicado, dura 1ms
    }

    /**
     * Realiza el proceso de renderizado
     *
     * @throws Exception
     */
    public void render() throws Exception {
        // logger.info("");
        // logger.info("[Renderizador]");
        listaCarasTransparente.clear();
        poligonosDibujadosTemp = 0;
        try {
            // La Matriz de vista es la inversa de la matriz de la camara.
            // Esto es porque la camara siempre estara en el centro y movemos el mundo
            // en direccion contraria a la camara.
            QMatriz4 matrizVista = camara.getMatrizTransformacion(QGlobal.tiempo).invert();
            QMatriz4 matrizVistaInvertidaBillboard = camara.getMatrizTransformacion(QGlobal.tiempo);
            // caras solidas
            escena.getEntities().stream()
                    .filter(entity -> entity.isToRender())
                    .parallel()
                    .forEach(entity -> renderEntity(entity, matrizVista, matrizVistaInvertidaBillboard, false));
            // caras transperentes
            escena.getEntities().stream()
                    .filter(entity -> entity.isToRender())
                    .parallel()
                    .forEach(entity -> renderEntity(entity, matrizVista, matrizVistaInvertidaBillboard, true));
        } catch (Exception e) {
            System.out.println("Error render:" + nombre);
            e.printStackTrace();
        }

        // logger.info("P6. Postprocesamiento=" + getSubDelta());
        // if (renderReal && opciones.isRenderArtefactos()) {
        // dibujarLuces(frameBuffer.getRendered().getGraphics());
        // // logger.info("P7. Dibujo de luces=" + getSubDelta());
        // }
    }

    /**
     * Se encarga de pasar el fragment shader en caso de ser diferido
     */
    public void shadeFragments() {
        if (opciones.isDefferedShadding()) {
            if (getFrameBuffer() != null) {
                // toda la pantalla
                // shadeFragments(0, getFrameBuffer().getAncho(), 0,
                // getFrameBuffer().getAlto());
                // divide la pantalla en secciones
                int sections = 4;
                int widthSection = getFrameBuffer().getAncho() / sections;
                int heigthSection = getFrameBuffer().getAlto() / sections;
                // // procesa cada seccion en paralelo
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

    private void shadeFragments(int xFrom, int width, int yFrom, int height) {
        if (getFrameBuffer() != null) {
            for (int x = xFrom; x < xFrom + width; x++) {
                for (int y = yFrom; y < yFrom + height; y++) {
                    if (getFrameBuffer().getPixel(x, y).isDibujar())
                        getFrameBuffer().setQColor(x, y,
                                getShader().shadeFragment(getFrameBuffer().getPixel(x, y), x, y));
                }
            }
        }
    }

    public void postRender() {
        if (frameBuffer != null) {
            efectosPostProcesamiento();
            if (renderReal) {
                showStats(frameBuffer.getRendered().getGraphics());
                // logger.info("P8. Estadísticas=" + getSubDelta());
            }

            // Dibuja sobre la superficie
            if (renderReal
                    && (this.getSuperficie() != null
                            && this.getSuperficie().getComponente() != null
                            && this.getSuperficie().getComponente().getGraphics() != null)) {
                this.getSuperficie().getComponente().setImagen(frameBuffer.getRendered());
                this.getSuperficie().getComponente().repaint();
                // logger.info("P10. Tiempo dibujado sobre superficie=" + getSubDelta());
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
    protected void renderEntity(Entity entity, QMatriz4 matrizVista, QMatriz4 matrizVistaInvertidaBillboard,
            boolean transparentes) {
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
        if (entity instanceof Camera && entity.equals(this.camara)) {
            return;
        }

        // Matriz de modelo
        // obtiene la matriz de informacion concatenada con los padres
        matrizModelo = entity.getMatrizTransformacion(QGlobal.tiempo);

        // ------------------------------------------------------------
        // MAtriz VistaModelo
        // obtiene la matriz de transformacion del objeto combinada con la matriz de
        // vision de la camara
        matVistaModelo = matrizVista.mult(matrizModelo);

        // busca un shader personalizado
        FragmentShaderComponent qshader = (FragmentShaderComponent) QUtilComponentes.getComponent(entity,
                FragmentShaderComponent.class);
        if (qshader != null && qshader.getShader() != null) {
            setShader(qshader.getShader());
        } else {
            setShader(defaultShader);
        }
        getShader().setRender(this);

        // busca un vertexShader personalizado
        VertexShaderComponent vertexShaderComponent = (VertexShaderComponent) QUtilComponentes.getComponent(entity,
                VertexShaderComponent.class);
        if (vertexShaderComponent != null && vertexShaderComponent.getShader() != null) {
            setVertexShader(vertexShaderComponent.getShader());
        } else {
            setVertexShader(defaultVertexShader);
        }
        entity.getComponents()
                .stream()
                .filter(componente -> componente instanceof Mesh)
                .parallel()
                .forEach(componente -> {
                    TempVars t = TempVars.get();
                    try {
                        Mesh mesh = (Mesh) componente;
                        entity.actualizarRotacionBillboard(matrizVistaInvertidaBillboard);
                        // vertices
                        int nVertices = 0;
                        t.bufferVertices1.init(mesh.vertices.length, mesh.primitivas.length);
                        for (Vertex vertice : mesh.vertices) {
                            t.bufferVertices1.setVertice(
                                    vertexShader.apply(vertice, matVistaModelo), nVertices);
                            nVertices++;
                        }
                        // rasterizacion
                        // caras
                        // for (QPrimitiva primitiva : ((Mesh) componente).primitivas) {
                        List.of(((Mesh) componente).primitivas).stream()
                                // .parallel()
                                .forEach(primitiva -> {
                                    if (primitiva.material == null
                                            // q no tengra transparencia cuando tiene el tipo de material basico
                                            || (primitiva.material instanceof QMaterialBas
                                                    && transparentes == ((QMaterialBas) primitiva.material)
                                                            .isTransparencia())) {
                                        if (primitiva instanceof Poly) {
                                            Poly poly = (Poly) primitiva;
                                            // vuelve a calcular la normal y el centro, funciona para las
                                            // animaciones, donde la transformacion de la normal no da los
                                            // resultados esperados, porq no tenemos la matriz del hueso
                                            poly.calculaNormalYCentro(t.bufferVertices1.getVerticesTransformados());
                                            if (poly.isNormalInversa()) {
                                                // invierto la normal en caso detener la marca de normal inversa
                                                poly.getNormal().flip();
                                                poly.getNormalCopy().flip();
                                            }
                                        }
                                        poligonosDibujadosTemp++;
                                        raster.raster(t.bufferVertices1, primitiva, opciones
                                                .getTipoVista() == QOpcionesRenderer.VISTA_WIRE
                                                || primitiva.geometria.tipo == Mesh.GEOMETRY_TYPE_WIRE);
                                    }
                                });

                    } finally {
                        t.release();
                    }
                });
    }

    /**
     * Ejecuta los efectosPotProcesamiento
     */
    private void efectosPostProcesamiento() {
        if (efectosPostProceso != null) {
            // try {
            // ImageIO.write(frameBuffer.getRendered(), "png", new
            // File("/home/alberto/testSalidaAntes_" + sf.format(new Date()) + ".png"));
            // } catch (IOException ex) {
            //
            // }
            frameBuffer.setBufferColor(efectosPostProceso.ejecutar(frameBuffer.getBufferColor()));
            // frameBuffer.actualizarTextura();
            // try {
            // ImageIO.write(frameBuffer.getRendered(), "png", new
            // File("/home/alberto/testSalidaDespues_" + sf.format(new Date()) + ".png"));
            // } catch (IOException ex) {
            //
            // }
        }
    }

    /**
     * Realiza el dibujo de las luces para la vista de trabajo.
     *
     * @param g
     */
    // private void dibujarLuces(Graphics g) {
    // if (opciones.isDibujarLuces()) {
    // // dibuja las luces
    // TempVars t = TempVars.get();

    // QVector2 puntoLuz = t.vector2f1;
    // for (Entity entity : escena.getEntities()) {
    // if (entity.isToRender()) {
    // for (EntityComponent componente : entity.getComponents()) {
    // if (componente instanceof QLigth) {
    // QLigth luz = (QLigth) componente;
    // QMatriz4 matVistaModelo =
    // camara.getMatrizTransformacion(QGlobal.tiempo).invert()
    // .mult(entity.getMatrizTransformacion(QGlobal.tiempo));
    // t.vertice1.set(vertexShader.apply(QVertex.ZERO, matVistaModelo));
    // if (t.vertice1.location.z > 0) {
    // continue;
    // }
    // camara.getCoordenadasPantalla(puntoLuz, t.vertice1.location,
    // frameBuffer.getAncho(),
    // frameBuffer.getAlto());
    // g.setColor(luz.color.getColor());
    // if (entidadActiva == entity) {
    // g.setColor(new Color(255, 128, 0));
    // }
    // g.drawOval((int) puntoLuz.x - lightOnScreenSize / 2,
    // (int) puntoLuz.y - lightOnScreenSize / 2, lightOnScreenSize,
    // lightOnScreenSize);
    // // dibuja la dirección de la luz direccional y cónica
    // if (luz instanceof QDirectionalLigth) {
    // t.vector3f1.set(((QDirectionalLigth) luz).getDirection());
    // t.vector3f1.set(QTransformar.transformarVector(t.vector3f1, entity, camara));
    // QVector2 secondPoint = new QVector2();
    // camara.getCoordenadasPantalla(secondPoint, new QVector4(t.vector3f1, 0),
    // frameBuffer.getAncho(), frameBuffer.getAlto());
    // g.drawLine((int) puntoLuz.x, (int) puntoLuz.y, (int) secondPoint.x,
    // (int) secondPoint.y);
    // } else if (luz instanceof QSpotLigth) {
    // t.vector3f1.set(((QSpotLigth) luz).getDirection());
    // t.vector3f1.set(QTransformar.transformarVector(t.vector3f1, entity, camara));
    // QVector2 secondPoint = new QVector2();
    // camara.getCoordenadasPantalla(secondPoint, new QVector4(t.vector3f1, 0),
    // frameBuffer.getAncho(), frameBuffer.getAlto());
    // g.drawLine((int) puntoLuz.x, (int) puntoLuz.y, (int) secondPoint.x,
    // (int) secondPoint.y);
    // // otro circulo alrededo el segundo punto
    // g.drawOval((int) secondPoint.x - lightOnScreenSize,
    // (int) secondPoint.y - lightOnScreenSize / 2, lightOnScreenSize * 2,
    // lightOnScreenSize);
    // }

    // if (!(luz instanceof QDirectionalLigth)) {
    // // dibuja el radio de la luz
    // t.vector3f1.set(t.vertice1.location.getVector3());
    // t.vector3f1.add(QVector3.of(0, luz.radio, 0));// agrego un vector hacia
    // arriba con
    // // tamanio del radio
    // QVector2 secondPoint = new QVector2();
    // camara.getCoordenadasPantalla(secondPoint, new QVector4(t.vector3f1, 1),
    // frameBuffer.getAncho(), frameBuffer.getAlto());
    // g.setColor(luz.color.getColor());
    // float difx = Math.abs(secondPoint.x - puntoLuz.x);
    // float dify = Math.abs(secondPoint.y - puntoLuz.y);
    // int largo = (int) Math.sqrt(difx * difx + dify * dify);
    // g.drawOval((int) puntoLuz.x - largo, (int) puntoLuz.y - largo, largo * 2,
    // largo * 2);
    // }
    // }
    // }
    // }
    // }
    // t.release();

    // }
    // // dibuja las normales de las caras
    // // if (opciones.showNormal) {
    // // g.setColor(Color.CYAN);
    // // for (QVertice vertex : bufferVertices.getVerticesTransformados()) {
    // // if (vertex.ubicacion.z > 0) {
    // // continue;
    // // }
    // // QVector2 vertexPoint = new QVector2();
    // // camara.getCoordenadasPantalla(vertexPoint, vertex);
    // // QVertice tail = vertex.clone();
    // // tail.ubicacion.x += vertex.normal.x / 3;
    // // tail.ubicacion.y += vertex.normal.y / 3;
    // // tail.ubicacion.z += vertex.normal.z / 3;
    // // QVector2 tailPoint = new QVector2();
    // // camara.getCoordenadasPantalla(tailPoint, tail);
    // // g.drawLine((int) vertexPoint.x, (int) vertexPoint.y, (int) tailPoint.x,
    // (int)
    // // tailPoint.y);
    // // }
    // // }
    // }

    /**
     * Actualiza la información de las luces y de las sombras, procesadores de
     * sombras
     */
    private void updateLigths() {
        try {
            litgths.clear();
            // for (Entity entity : escena.getListaEntidades()) {
            escena.getEntities().stream().filter(entity -> entity.isToRender()).parallel()
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
                                // luz.entity.getTransformacion().getTraslacion().set(QTransformar.transformarVector(QVector3.zero,
                                // entity, camara));
                                // actualiza la dirección de la luz
                                direccionLuzEspacioCamara = TransformationVectorUtil
                                        .transformarVectorNormal(direccionLuzEspacioCamara, entity, camara);
                                direccionLuzMapaSombra = TransformationVectorUtil.transformarVectorNormal(
                                        direccionLuzMapaSombra,
                                        entity.getMatrizTransformacion(QGlobal.tiempo));

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
                                                        escena, (QDirectionalLigth) componente, camara,
                                                        luz.getResolucionMapaSombra(), luz.getResolucionMapaSombra());
                                            } else {
                                                proc = new QSombraDireccional(escena, (QDirectionalLigth) componente,
                                                        camara, luz.getResolucionMapaSombra(),
                                                        luz.getResolucionMapaSombra());
                                            }
                                            // logger.info("Creado pocesador de sombra Direccional con clave "
                                            // + entity.getName());
                                        } else if (luz instanceof QPointLigth) {
                                            proc = new QSombraOmnidireccional(escena, (QPointLigth) componente, camara,
                                                    luz.getResolucionMapaSombra(), luz.getResolucionMapaSombra());
                                            // logger.info("Creado pocesador de sombra Omnidireccional con clave "
                                            // + entity.getName());
                                        } else if (luz instanceof QSpotLigth) {
                                            proc = new QSombraCono(escena, (QSpotLigth) componente, camara,
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

                        // for (QComponente componente : entity.getComponentes()) {
                        // }
                    });

            // }
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
            for (Entity entity : escena.getEntities()) {
                if (entity.isToRender()) {
                    for (EntityComponent componente : entity.getComponents()) {
                        if (componente instanceof QLigth) {
                            tmp = TransformationVectorUtil.transformarVector(QVector3.zero, entity, camara);
                            camara.getCoordenadasPantalla(ubicacionLuz, new QVector4(tmp, 1), frameBuffer.getAncho(),
                                    frameBuffer.getAlto());
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
            Fragment pixel = frameBuffer.getPixel((int) mouseLocation.x, (int) mouseLocation.y);
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
    public void cambiarRaster(int opcion) {
        switch (opcion) {
            case 3:
                raster = new ParallelRaster(this);

                break;
            case 2:
                raster = new Raster2(this);
                break;
            case 1:
            default:
                raster = new Raster(this);
                break;
        }
    }

    /**
     * Cambia el shader
     *
     * @param opcion 0. QSimpleShader, 1 QFlatShader, 2 QPhongShader, 3
     *               QTexturaShader, 4 QIluminadoShader, 5 QShadowShader, 6
     *               QFullShader, 7
     *               QPBRShader
     */
    @Override
    public void cambiarShader(int opcion) {
        super.cambiarShader(opcion); // To change body of generated methods, choose Tools | Templates.

        switch (opcion) {
            case 0:
                defaultShader = new QSimpleProxyShader(this);
                break;
            case 1:
                defaultShader = new QFlatProxyShader(this);
                break;
            case 2:
                defaultShader = new QPhongProxyShader(this);
                break;
            case 3:
                defaultShader = new QTexturaProxyShader(this);
                break;
            case 4:
                defaultShader = new QLigthProxyShader(this);
                break;
            case 5:
                defaultShader = new QShadowProxyShader(this);
                break;
            case 6:
                defaultShader = new QFullProxyShader(this);
                break;
            case 7:
                defaultShader = new QPbrProxyShader(this);
                break;
        }
    }

    public FragmentShader getShader() {
        return shader;
    }

    public void setShader(FragmentShader shader) {
        this.shader = shader;
    }

    public FragmentShader getDefaultShader() {
        return defaultShader;
    }

    public void setDefaultShader(FragmentShader defaultShader) {
        this.defaultShader = defaultShader;
    }

    public AbstractRaster getRaster() {
        return raster;
    }

    public void setRaster(AbstractRaster raster) {
        this.raster = raster;
    }

    @Override
    public void renderLine(QPrimitiva primitiva, Vertex... vertex) {
        raster.rasterLine(primitiva, vertex);
    }

    @Override
    public void render(QVertexBuffer bufferVertices, QPrimitiva primitiva, boolean wire) {
        raster.raster(bufferVertices, primitiva, wire);
    }

}
