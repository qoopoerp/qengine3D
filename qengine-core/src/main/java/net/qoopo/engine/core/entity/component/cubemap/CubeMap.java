/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.cubemap;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.UpdatableComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.renderer.post.procesos.blur.QProcesadorBlur;
import net.qoopo.engine.core.renderer.post.procesos.color.QProcesadorBloom;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.procesador.QProcesadorTextura;
import net.qoopo.engine.core.util.QGlobal;

/**
 * Genera y gestiona un mapa cúbico * Permite la creación de un mapa cúbico
 * dinámico * Permite la generación de una imagen HDRI a partir de textures para
 * cada lado
 *
 * @author alberto
 */
@Getter
@Setter
public class CubeMap extends EntityComponent implements UpdatableComponent {

    private static Logger logger = Logger.getLogger("cube-map");

    public static final int FORMATO_MAPA_CUBO = 1;
    public static final int FORMATO_MAPA_HDRI = 2;

    /**
     * Se realiza un Mapeo cubico, 6 textures uno por cada lado del cubo
     */
    transient private RenderEngine render;
    private QVector3[] direcciones;
    private QTextura[] textures;
    private QVector3[] direccionesArriba;

    private int tipoSalida = FORMATO_MAPA_CUBO;
    // private int tipoSalida = FORMATO_MAPA_HDRI;
    private QProcesadorTextura procEntorno;
    private QProcesadorSimple procIrradiacion;
    private QTextura texturaEntorno;// esta textura es la union de las 6 textures renderizadas en el formato de
                                    // cubemap o HDRI
    private QTextura texturaIrradiacion;// esta textura es la textura de salida despues de un proceso de blur, se usa
                                        // como textur ade irradiacion

    public String[] nombres = { "Arriba", "Abajo", "Frente", "Atras", "Izquierda", "Derecha" };

    private int size;
    private boolean actualizar = true;
    private Dimension dimensionLado;
    private boolean dinamico;

    // variables para el panel que lo contruye
    private float factorReflexion = 1.0f;
    private float indiceRefraccion = 1.52f;

    private boolean generarIrradiacion = false;

    public CubeMap() {
        this(QGlobal.MAPA_CUPO_RESOLUCION);
    }

    public CubeMap(int resolution) {
        direcciones = new QVector3[6];

        // -------------------------------------
        direcciones[1] = QVector3.of(0, 1, 0); // arriba
        direcciones[0] = QVector3.of(0, -1, 0); // abajo
        direcciones[3] = QVector3.of(0, 0, 1); // adelante
        direcciones[2] = QVector3.of(0, 0, -1); // atras
        direcciones[5] = QVector3.of(-1, 0, 0); // izquierda
        direcciones[4] = QVector3.of(1, 0, 0); // derecha
        // -------------------------------------
        direccionesArriba = new QVector3[6];
        direccionesArriba[0] = QVector3.of(0, 0, -1); // arriba
        direccionesArriba[1] = QVector3.of(0, 0, 1); // abajo
        direccionesArriba[2] = QVector3.of(0, 1, 0); // adelante
        direccionesArriba[3] = QVector3.of(0, 1, 0); // atras
        direccionesArriba[4] = QVector3.of(0, 1, 0); // izquierda
        direccionesArriba[5] = QVector3.of(0, 1, 0); // derecha

        textures = new QTextura[6];
        for (int i = 0; i < 6; i++) {
            textures[i] = new QTextura();
            textures[i].setSignoX(-1);// es reflejo
        }

        // render = new QRender(null, null, resolucion, resolucion);
        render = AssetManager.get().getRendererFactory().createRenderEngine(null, null, null, resolution, resolution);
        render.setEfectosPostProceso(null);
        render.setShowStats(false);
        render.setRenderReal(false);
        render.setCamara(new Camera("CubeMap"));
        render.getCamara().setFOV((float) Math.toRadians(90.0f));// angulo de visión de 90 grados
        // render.cambiarShader(3);//el shader de textura, un shader simple
        // render.cambiarShader(4);//el shader de textura con iluminacion
        // render.cambiarShader(5);//el shader con sombras
        // render.cambiarShader(6);//el shader full
        texturaEntorno = new QTextura();
        texturaIrradiacion = new QTextura();
        // procEntorno = new QProcesadorMipMap(texturaEntorno, 5,
        // QProcesadorMipMap.TIPO_BLUR);
        procEntorno = new QProcesadorSimple(texturaEntorno);
        procIrradiacion = new QProcesadorSimple(texturaIrradiacion);
        build(resolution);
    }

    /**
     * Construye un mapa cubico estatico
     *
     * @param tipo
     * @param positivoY
     * @param positivoX
     * @param positivoZ
     * @param negativoY
     * @param negativoX
     * @param negativoZ
     */
    public CubeMap(int tipo, QTextura positivoX, QTextura positivoY, QTextura positivoZ, QTextura negativoX,
            QTextura negativoY, QTextura negativoZ) {
        this.size = positivoX.getAncho();
        direcciones = new QVector3[6];
        textures = new QTextura[6];
        textures[0] = positivoY;
        textures[1] = negativoY;
        textures[2] = positivoZ;
        textures[3] = negativoZ;
        textures[4] = negativoX;
        textures[5] = positivoX;
        texturaEntorno = new QTextura();
        texturaIrradiacion = new QTextura();
        // procEntorno = new QProcesadorMipMap(texturaEntorno, 5,
        // QProcesadorMipMap.TIPO_BLUR);
        procEntorno = new QProcesadorSimple(texturaEntorno);
        procIrradiacion = new QProcesadorSimple(texturaIrradiacion);
        dimensionLado = null;
        dinamico = false;
        tipoSalida = tipo;
        updateTexture();
        direccionesArriba = new QVector3[6];
    }

    public void build(int size) {
        this.size = size;
        render.setRenderReal(false);
        render.opciones.setForzarResolucion(true);
        render.opciones.setAncho(size);
        render.opciones.setAlto(size);
        render.opciones.setNormalMapping(false);
        render.opciones.setDibujarCarasTraseras(false);
        render.opciones.setSombras(false);
        render.opciones.setDibujarLuces(false);
        render.opciones.setNormalMapping(false);
        render.opciones.setDefferedShadding(false);
        // render.opciones.setDefferedShadding(false);
        render.resize();
        dimensionLado = new Dimension(size, size);
        dinamico = false;
        actualizar = true;// obliga a actualizar el mapa
    }

    public void aplicar(int tipo, float factorMetalico, float indiceRefraccion) {
        setTipoSalida(tipo);
        setFactorReflexion(factorMetalico);
        setIndiceRefraccion(indiceRefraccion);
        List<QMaterialBas> lst = new ArrayList<>();
        // ahora recorro todos los materiales del objeto y le agrego la textura de
        // reflexion
        if (entity.getComponents() != null && !entity.getComponents().isEmpty()) {
            for (EntityComponent componente : entity.getComponents()) {
                if (componente instanceof Mesh) {
                    for (QPrimitiva poligono : ((Mesh) componente).primitivas) {
                        if (poligono.material instanceof QMaterialBas) {
                            if (!lst.contains((QMaterialBas) poligono.material)) {
                                lst.add((QMaterialBas) poligono.material);
                            }
                        }
                    }
                }
            }
        }
        if (!lst.isEmpty()) {
            for (QMaterialBas mat : lst) {
                mat.setMapaEntorno(procEntorno);
                mat.setMapaIrradiacion(procIrradiacion);
                mat.setMetalico(factorMetalico);
                mat.setIndiceRefraccion(indiceRefraccion);
                mat.setReflexion(factorMetalico > 0.0f);
                mat.setRefraccion(indiceRefraccion > 0.0f);
                mat.setTipoMapaEntorno(getTipoSalida());// mapa cubico o HDRI
            }
        }
        actualizar = true;// obliga a actualizar el mapa
    }

    /**
     * Actualiza los mapas de acuerdo a la posicion del cubo
     *
     * @param posicion
     */
    public void actualizarMapa(QVector3 posicion) {
        logger.info("[>] Actualizando mapa de entorno");
        for (int i = 0; i < 6; i++) {
            render.getCamara().lookAt(posicion, direcciones[i], direccionesArriba[i]);
            render.update();
            // render.shadeFragments();
            // render.postRender();
            textures[i].loadTexture(render.getFrameBuffer().getRendered());
            logger.info("[>] Generado " + (i + 1) + "/" + 6);
        }
        updateTexture();
        logger.info("[>] Mapa de entorno generado");
    }

    /**
     * Actualiza el mapa. La actualizacion es controlada con las variables
     * <<Actualizar>> o <<Dinamico>>
     *
     * @param mainRender
     */
    public void actualizarMapa(RenderEngine mainRender) {
        if (!mainRender.opciones.isMaterial()) {
            return;
        }
        if (mainRender.getFrameBuffer() == null) {
            return;
        }

        if (dinamico || actualizar) {
            boolean dibujar = entity.isToRender();
            try {
                render.setEscena(mainRender.getEscena());
                // seteo para q no se dibuje a la entity
                entity.setToRender(false);
                actualizarMapa(entity.getMatrizTransformacion(QGlobal.tiempo).toTranslationVector());
                actualizar = false;
            } finally {
                // seteo para q se dibuje la entity en los demas renderes
                entity.setToRender(dibujar);
            }
        }
    }

    /**
     * actualiza la textura de salida con loas 6 textures renderizadas
     */
    private void updateTexture() {
        logger.info("[>] Actualizando textura");
        switch (tipoSalida) {
            case FORMATO_MAPA_CUBO:
            default:
                texturaEntorno.loadTexture(buildCubeMap());
                break;
            case FORMATO_MAPA_HDRI:
                texturaEntorno.loadTexture(buildHDRI(buildCubeMap()));
                break;
        }

        // logger.info("[>] Generando mipmap");
        // re-genera los mimpmaps
        // procEntorno.generarMipMap(texturaEntorno);
        // procEntorno.setNivel(1);

        if (generarIrradiacion) {
            logger.info("[>] Generando irradiacion");
            QProcesadorBloom bloom = new QProcesadorBloom(texturaEntorno.getAncho() / 2, texturaEntorno.getAlto() / 2,
                    0.6f);
            QProcesadorBlur blur = new QProcesadorBlur(texturaEntorno.getAncho() / 2, texturaEntorno.getAlto() / 2, 20);
            bloom.procesar(texturaEntorno);
            blur.procesar(bloom.getBufferSalida());
            texturaIrradiacion.loadTexture(blur.getBufferSalida().getImagen());
        }
        // texturaIrradiacion.loadTexture(proc.getBufferSalida().getImagen());
    }

    /**
     * Crea una imagen de salida en formato mapa cubico
     *
     * @return
     */
    private BufferedImage buildCubeMap() {
        BufferedImage img = new BufferedImage(size * 4, size * 3, BufferedImage.TYPE_INT_ARGB);
        // //https://en.wikipedia.org/wiki/Cube_mapping
        // el orden de pintura es importante porq evita las lineas negras que tengo en
        // los frames sobreescribiendolas, debo corregir esas lineas negras

        img.getGraphics().drawImage(textures[4].getImagen(dimensionLado), 0, size - 2, null); // izquierda 4
        img.getGraphics().drawImage(textures[0].getImagen(dimensionLado), size, 0, null); // arriba 0

        img.getGraphics().drawImage(textures[1].getImagen(dimensionLado), size, size * 2 - 4, null);// abajo
        img.getGraphics().drawImage(textures[3].getImagen(dimensionLado), size * 3 - 5, size - 1, null);// atras 3
        img.getGraphics().drawImage(textures[5].getImagen(dimensionLado), size * 2 - 2, size - 1, null);// derecha
                                                                                                        // 5

        img.getGraphics().drawImage(textures[2].getImagen(dimensionLado), size, size - 1, null);// frente

        return img;
    }

    /**
     * Crea una imagen HDRI desde la imagen del mapa de cubo
     * https://stackoverflow.com/questions/34250742/converting-a-cubemap-into-equirectangular-panorama
     *
     * @param cubeMap
     * @return
     */
    private BufferedImage buildHDRI(BufferedImage cubeMap) {
        BufferedImage salida = new BufferedImage(cubeMap.getWidth(), cubeMap.getHeight(), BufferedImage.TYPE_INT_ARGB);
        // BufferedImage salida = new BufferedImage(tamanio * 4, tamanio * 3,
        // BufferedImage.TYPE_INT_ARGB);
        float u, v; // Normalised texture coordinates, from 0 to 1, starting at lower left corner
        float phi, theta; // Polar coordinates
        int anchoCara, altoCara;

        // anchoCara = cubeMap.getWidth() / 4; //4 horizontal faces
        // altoCara = cubeMap.getHeight() / 3; //3 vertical faces
        anchoCara = size; // 4 horizontal faces
        altoCara = size; // 3 vertical faces

        for (int j = 0; j < salida.getHeight(); j++) {
            // Rows start from the bottom
            v = 1 - ((float) j / (float) salida.getHeight());
            theta = v * QMath.PI;

            for (int i = 0; i < salida.getWidth(); i++) {
                // Columns start from the left
                u = ((float) i / (float) salida.getWidth());
                phi = u * 2 * QMath.PI;

                float x, y, z; // Unit

                x = QMath.sin(phi) * QMath.sin(theta) * -1;
                y = QMath.cos(theta);
                z = QMath.cos(phi) * QMath.sin(theta) * -1;

                // x = QMath.cos(phi) * QMath.cos(theta);
                // y = QMath.sin(phi);
                // z = QMath.cos(phi) * QMath.sin(theta);
                float xa, ya, za;
                float a;

                a = Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(z));

                // Vector Parallel to the unit vector that lies on one of the cube faces
                xa = x / a;
                ya = y / a;
                za = z / a;

                int color;
                int xPixel, yPixel;
                int xOffset, yOffset;

                if (xa == 1) {
                    // Right
                    xPixel = (int) ((((za + 1f) / 2f) - 1f) * anchoCara);
                    xOffset = 2 * anchoCara; // Offset
                    yPixel = (int) ((((ya + 1f) / 2f)) * altoCara);
                    yOffset = altoCara; // Offset
                } else if (xa == -1) {
                    // Left
                    xPixel = (int) ((((za + 1f) / 2f)) * anchoCara);
                    xOffset = 0;
                    yPixel = (int) ((((ya + 1f) / 2f)) * altoCara);
                    yOffset = altoCara;
                } else if (ya == 1) {
                    // Up
                    xPixel = (int) ((((xa + 1f) / 2f)) * anchoCara);
                    xOffset = anchoCara;
                    yPixel = (int) ((((za + 1f) / 2f) - 1f) * altoCara);
                    yOffset = 2 * altoCara;
                } else if (ya == -1) {
                    // Down
                    xPixel = (int) ((((xa + 1f) / 2f)) * anchoCara);
                    xOffset = anchoCara;
                    yPixel = (int) ((((za + 1f) / 2f)) * altoCara);
                    yOffset = 0;
                } else if (za == 1) {
                    // Front
                    xPixel = (int) ((((xa + 1f) / 2f)) * anchoCara);
                    xOffset = anchoCara;
                    yPixel = (int) ((((ya + 1f) / 2f)) * altoCara);
                    yOffset = altoCara;
                } else if (za == -1) {
                    // Back
                    xPixel = (int) ((((xa + 1f) / 2f) - 1f) * anchoCara);
                    xOffset = 3 * anchoCara;
                    yPixel = (int) ((((ya + 1f) / 2f)) * altoCara);
                    yOffset = altoCara;
                } else {
                    xPixel = 0;
                    yPixel = 0;
                    xOffset = 0;
                    yOffset = 0;
                }

                xPixel = Math.abs(xPixel);
                yPixel = Math.abs(yPixel);

                xPixel += xOffset;
                yPixel += yOffset;

                // desde 1 hasta el ancho -1
                xPixel = QMath.clamp(xPixel, 0, cubeMap.getWidth() - 1);
                yPixel = QMath.clamp(yPixel, 0, cubeMap.getHeight() - 1);

                try {
                    color = cubeMap.getRGB(xPixel, yPixel);
                    // salida.setRGB(i, j, color);
                    // invierto la coordenada X d
                    salida.setRGB(salida.getWidth() - i, j, color);
                } catch (Exception e) {

                }
            }
        }
        return salida;
    }

    public QTextura[] getTexturas() {
        return textures;
    }

    public QTextura getTextura(int i) {
        return textures[i];
    }

    public QTextura getTexturaEntorno() {
        return texturaEntorno;
    }

    public int getTipoSalida() {
        return tipoSalida;
    }

    public void setTipoSalida(int tipoSalida) {
        this.tipoSalida = tipoSalida;
    }

    @Override
    public void destruir() {
        texturaEntorno = null;
        render = null;
        textures = null;
    }

    public boolean isActualizar() {
        return actualizar;
    }

    public void setActualizar(boolean actualizar) {
        this.actualizar = actualizar;
    }

    public float getFactorReflexion() {
        return factorReflexion;
    }

    public void setFactorReflexion(float factorReflexion) {
        this.factorReflexion = factorReflexion;
    }

    public float getIndiceRefraccion() {
        return indiceRefraccion;
    }

    public void setIndiceRefraccion(float indiceRefraccion) {
        this.indiceRefraccion = indiceRefraccion;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int tamanio) {
        this.size = tamanio;
    }

    public QTextura getTexturaIrradiacion() {
        return texturaIrradiacion;
    }

    public void setTexturaIrradiacion(QTextura texturaIrradiacion) {
        this.texturaIrradiacion = texturaIrradiacion;
    }

    // public QProcesadorMipMap getProcEntorno() {
    // return procEntorno;
    // }

    // public void setProcEntorno(QProcesadorMipMap procEntorno) {
    // this.procEntorno = procEntorno;
    // }
    public QProcesadorSimple getProcIrradiacion() {
        return procIrradiacion;
    }

    // public void setProcIrradiacion(QProcesadorSimple procIrradiacion) {
    // this.procIrradiacion = procIrradiacion;
    // }
    public boolean isGenerarIrradiacion() {
        return generarIrradiacion;
    }

    public void setGenerarIrradiacion(boolean generarIrradiacion) {
        this.generarIrradiacion = generarIrradiacion;
    }

    @Override
    public boolean isRequierepdate() {
        return actualizar;
    }

    @Override
    public void update(RenderEngine renderEngine, Scene scene) {
        actualizarMapa(renderEngine);
    }
}
