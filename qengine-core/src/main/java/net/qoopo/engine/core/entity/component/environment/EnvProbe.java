/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.environment;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.UpdatableComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.renderer.post.filters.blur.BlurFilter;
import net.qoopo.engine.core.renderer.post.filters.color.BloomFilter;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.procesador.MipmapTexture;
import net.qoopo.engine.core.util.QGlobal;

/**
 * Realiza la captura de un mapa de entorno
 * Permite la creación de un mapa cúbico
 * dinámico
 * Permite la generación de una imagen HDRI a partir de textures para
 * cada lado
 *
 * @author alberto
 */
@Getter
@Setter
public class EnvProbe implements EntityComponent, UpdatableComponent {

    @Getter
    @Setter
    private Entity entity;

    private static Logger logger = Logger.getLogger("cube-map");

    public static final int FORMATO_MAPA_CUBO = 1;
    public static final int FORMATO_MAPA_HDRI = 2;

    /**
     * Se realiza un Mapeo cubico, 6 textures uno por cada lado del cubo
     */
    transient private RenderEngine render;
    private QVector3[] faceDirections;
    private Texture[] textures;
    private QVector3[] faceUps;

    private int mapType = FORMATO_MAPA_CUBO;

    private int lod = 4;
    private Texture envMap;// esta textura es la union de las 6 textures renderizadas en el formato de
                           // cubemap o HDRI
    private Texture hdrMap;// esta textura es la textura de salida despues de un proceso de blur, se usa
                           // como textura de irradiacion

    public String[] nombres = { "Arriba", "Abajo", "Frente", "Atras", "Izquierda", "Derecha" };

    private int size;
    private boolean actualizar = true;
    private Dimension dimensionLado;
    private boolean dinamico;

    private boolean generarIrradiacion = false;

    public EnvProbe() {
        this(QGlobal.MAPA_CUPO_RESOLUCION);
    }

    public EnvProbe(int resolution) {
        faceDirections = new QVector3[6];

        // -------------------------------------
        faceDirections[0] = QVector3.of(0, -1, 0); // abajo
        faceDirections[1] = QVector3.of(0, 1, 0); // arriba
        faceDirections[2] = QVector3.of(0, 0, -1); // atras
        faceDirections[3] = QVector3.of(0, 0, 1); // adelante
        faceDirections[5] = QVector3.of(-1, 0, 0); // izquierda
        faceDirections[4] = QVector3.of(1, 0, 0); // derecha
        // -------------------------------------
        faceUps = new QVector3[6];
        faceUps[0] = QVector3.of(0, 0, -1); // arriba
        faceUps[1] = QVector3.of(0, 0, 1); // abajo
        faceUps[2] = QVector3.of(0, 1, 0); // adelante
        faceUps[3] = QVector3.of(0, 1, 0); // atras
        faceUps[4] = QVector3.of(0, 1, 0); // izquierda
        faceUps[5] = QVector3.of(0, 1, 0); // derecha

        textures = new Texture[6];
        for (int i = 0; i < 6; i++) {
            textures[i] = new Texture();
            textures[i].setSignoX(-1);// es reflejo
        }

        // render = new QRender(null, null, resolucion, resolucion);
        render = AssetManager.get().getRendererFactory().createRenderEngine(null, null, null, resolution, resolution);
        render.setFilterQueue(null);
        render.setShowStats(false);
        render.setRenderReal(false);
        render.setCamera(new Camera("EnvProbe"));
        if (AssetManager.get().getEnvProbeShader() != null)
            render.setShader(AssetManager.get().getEnvProbeShader());
        hdrMap = new Texture();
        envMap = new MipmapTexture(new Texture(), lod, MipmapTexture.TIPO_BLUR);
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
    public EnvProbe(int tipo, Texture positivoX, Texture positivoY, Texture positivoZ, Texture negativoX,
            Texture negativoY, Texture negativoZ) {
        this.size = positivoX.getWidth();
        faceDirections = new QVector3[6];
        textures = new Texture[6];
        textures[0] = positivoY;
        textures[1] = negativoY;
        textures[2] = positivoZ;
        textures[3] = negativoZ;
        textures[4] = negativoX;
        textures[5] = positivoX;
        hdrMap = new Texture();
        envMap = new MipmapTexture(new Texture(), lod, MipmapTexture.TIPO_BLUR);
        dimensionLado = null;
        dinamico = false;
        mapType = tipo;
        updateTexture();
        faceUps = new QVector3[6];
    }

    public void build(int size) {
        this.size = size;
        render.getCamera().setFOV((float) Math.toRadians(90.0f));// angulo de visión de 90 grados
        render.getCamera().setFrustrumLejos(100.0f);
        render.setRenderReal(false);
        render.setBackColor(QColor.WHITE);
        render.opciones.setForzarResolucion(true);
        render.opciones.setAncho(size);
        render.opciones.setAlto(size);
        render.opciones.setNormalMapping(false);
        render.opciones.setDibujarCarasTraseras(false);
        render.opciones.setSombras(false);
        render.opciones.setDibujarLuces(false);
        render.opciones.setNormalMapping(false);
        render.opciones.setDefferedShadding(true);
        render.resize();
        dimensionLado = new Dimension(size, size);
        dinamico = false;
        actualizar = true;// obliga a actualizar el mapa
    }

    public void aplicar(int tipo) {
        setMapType(tipo);
        List<Material> lst = new ArrayList<>();
        // ahora recorro todos los materiales del objeto y le agrego la textura de
        // reflexion
        if (entity != null && entity.getComponents() != null && !entity.getComponents().isEmpty()) {
            for (EntityComponent componente : entity.getComponents()) {
                if (componente instanceof Mesh) {
                    for (Primitive poligono : ((Mesh) componente).primitiveList) {
                        if (poligono.material instanceof Material) {
                            if (!lst.contains((Material) poligono.material)) {
                                lst.add((Material) poligono.material);
                            }
                        }
                    }
                }
            }
        }
        if (!lst.isEmpty()) {
            for (Material mat : lst) {
                mat.setEnvMap(envMap);
                mat.setHdrMap(hdrMap);
                mat.setEnvMapType(getMapType());// mapa cubico o HDRI
            }
        }
        actualizar = true;// obliga a actualizar el mapa
    }

    /**
     * Actualiza los mapas de acuerdo a la posicion del cubo
     *
     * @param posicion
     */
    public void update(QVector3 posicion) {
        logger.info("[>] Actualizando mapa de entorno");
        for (int i = 0; i < 6; i++) {
            // for (int i = 5; i >= 0; i--) {
            updateFace(i, posicion);
        }
        updateTexture();
        logger.info("[>] Mapa de entorno generado");
    }

    /**
     * Actualiza una cara del cubeMap
     */
    private void updateFace(int face, QVector3 posicion) {
        logger.info("[>] Generado " + (face + 1) + "/" + 6);
        render.getCamera().lookAt(posicion, faceDirections[face], faceUps[face]);
        render.update();
        render.shadeFragments();
        // render.postRender();
        textures[face].loadTexture(render.getFrameBuffer().getRendered());
    }

    /**
     * Actualiza el mapa. La actualizacion es controlada con las variables
     * <<Actualizar>> o <<Dinamico>>
     *
     * @param mainRender
     */
    public void updateEnvMap(RenderEngine mainRender) {
        if (!mainRender.opciones.isMaterial()) {
            return;
        }
        if (mainRender.getFrameBuffer() == null) {
            return;
        }

        if (dinamico || actualizar) {
            boolean dibujar = entity.isToRender();
            try {
                render.setScene(mainRender.getScene());
                // seteo para q no se dibuje a la entity
                entity.setToRender(false);
                update(entity.getMatrizTransformacion(QGlobal.time).toTranslationVector());
                actualizar = false;
            } catch (Exception e) {
                e.printStackTrace();
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
        switch (mapType) {
            case FORMATO_MAPA_CUBO:
            default:
                envMap.loadTexture(buildCubeMap());
                break;
            case FORMATO_MAPA_HDRI:
                envMap.loadTexture(buildHDRI(buildCubeMap()));
                break;
        }

        if (generarIrradiacion) {
            logger.info("[>] Generando irradiacion");
            // envMap.getWidth() / 2, envMap.getHeight() / 2
            BloomFilter bloom = new BloomFilter(0.6f);
            BlurFilter blur = new BlurFilter(20);
            hdrMap.loadTexture(blur.apply(bloom.apply(envMap)).getImagen());
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

    public Texture[] getTexturas() {
        return textures;
    }

    public Texture getTextura(int i) {
        return textures[i];
    }

    @Override
    public void destroy() {
        envMap = null;
        render = null;
        textures = null;
    }

    @Override
    public boolean isRequiereUpdate() {
        return actualizar;
    }

    @Override
    public void update(RenderEngine renderEngine, Scene scene) {
        updateEnvMap(renderEngine);
    }
}
