/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.water;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.UpdatableComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.water.texture.WaterTextureProcessor;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.renderer.buffer.QFrameBuffer;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.QClipPane;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;

/**
 * Procesador para simular agua modificando la textura de un Plano.
 *
 *
 * @author alberto
 */
@Getter
@Setter
public abstract class Water extends EntityComponent implements UpdatableComponent {

    protected final static float VELOCIDAD_AGUA = 0.05f;
    protected final static float FUERZA_OLA = 0.005f;

    transient protected RenderEngine render;
    protected WaterTextureProcessor outputTexture = null;// la mezcla de la textura de reflexion y la textur de
                                                         // refracción
    protected QFrameBuffer frameReflexion;
    protected QFrameBuffer frameRefraccion;
    protected QTextura textReflexion;
    protected QTextura textRefraccion;

    // sera usada para simular movimiento de agua con el tiempo agregando un offset
    // a las textures
    protected long lastTime;
    protected long deltaTime;
    protected float factorX;
    protected float factorY;
    protected final QVector3 arriba = QVector3.unitario_y.clone();
    protected final QVector3 abajo = QVector3.unitario_y.clone().multiply(-1.0f);

    protected int width;
    protected int height;
    protected Scene universo;

    protected Mesh mesh;
    protected QMaterialBas material;

    public Water() {

    }

    public void build() {

        try {

            this.textReflexion = new QTextura();
            this.textReflexion.setSignoY(-1);
            this.textRefraccion = new QTextura();

            frameReflexion = new QFrameBuffer(width, height, textReflexion);
            frameRefraccion = new QFrameBuffer(width, height, textRefraccion);
            // render = new QRender(universo, "Agua", null, ancho, ancho);
            render = AssetManager.get().getRendererFactory().createRenderEngine(universo, "Water", null, width, width);
            render.setEfectosPostProceso(null);
            render.renderReal = false;
            render.opciones.setForzarResolucion(true);
            render.opciones.setNormalMapping(false);
            render.opciones.setSombras(false);
            render.opciones.setDibujarCarasTraseras(false);
            render.setShowStats(false);
            render.setCamara(new Camera("WaterCamera"));
            render.resize();
            render.setRenderReal(false);
            render.opciones.setDibujarCarasTraseras(true);
            lastTime = System.currentTimeMillis();
            render.setPanelClip(new QClipPane(arriba, 0));
            outputTexture = new WaterTextureProcessor(textReflexion, textRefraccion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(RenderEngine mainRender, Scene universo) {
        // evita la ejecucion si no esta activo los materiales

        if (!mainRender.opciones.isMaterial()) {
            return;
        }
        if (mainRender.getFrameBuffer() == null) {
            return;
        }

        entity.setToRender(false);// evita renderizar el agua
        // actualizo la resolución de acuerdo a la cámara
        if (render.opciones.getAncho() != mainRender.getFrameBuffer().getAncho()
                && render.opciones.getAlto() != mainRender.getFrameBuffer().getAlto()) {
            render.opciones.setAncho(mainRender.getFrameBuffer().getAncho());
            render.opciones.setAlto(mainRender.getFrameBuffer().getAlto());
            frameReflexion = new QFrameBuffer(render.opciones.getAncho(), render.opciones.getAlto(), textReflexion);
            frameRefraccion = new QFrameBuffer(render.opciones.getAncho(), render.opciones.getAlto(), textRefraccion);
            render.resize();
        }

        render.setEscena(universo);
        render.setNombre(entity.getName());
        render.getCamara().setName(entity.getName());
        try {

            // REFRACCION
            // -------------------------------------------------------------------
            render.getCamara().setTransformacion(mainRender.getCamara().getTransformacion().clone());// la misma
                                                                                                     // posicion de la
                                                                                                     // cámara actual
            render.getCamara().getTransformacion().getRotacion().actualizarAngulos();
            render.setFrameBuffer(frameRefraccion);
            render.getPanelClip().setNormal(abajo);
            render.getPanelClip().setDistancia(entity.getTransformacion().getTraslacion().y - 1.0f);
            render.update();
            render.getFrameBuffer().actualizarTextura();
            // REFLEXION -------------------------------------------------------------------
            // movemos la cámara debajo de la superficie del agua a una distancia 2 veces de
            // la actual altura
            render.setFrameBuffer(frameReflexion);
            render.getPanelClip().setNormal(arriba);
            render.getPanelClip().setDistancia(entity.getTransformacion().getTraslacion().y + 0.5f);

            float distancia = 2 * (mainRender.getCamara().getTransformacion().getTraslacion().y
                    - entity.getTransformacion().getTraslacion().y);
            QVector3 nuevaPos = mainRender.getCamara().getTransformacion().getTraslacion().clone();
            nuevaPos.y -= distancia;

            // invertir angulo Pitch
            render.getCamara().setTransformacion(mainRender.getCamara().getTransformacion().clone());
            render.getCamara().getTransformacion().getRotacion().actualizarAngulos();
            render.getCamara().getTransformacion().getRotacion().getAngulos()
                    .setAnguloX(render.getCamara().getTransformacion().getRotacion().getAngulos().getAnguloX() * -1);
            render.getCamara().getTransformacion().getRotacion().actualizarCuaternion();
            render.getCamara().getTransformacion().trasladar(nuevaPos);
            render.update();
            render.getFrameBuffer().actualizarTextura();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // MOVIMIENTO de las normales
        deltaTime = System.currentTimeMillis() - lastTime;
        factorX = deltaTime * VELOCIDAD_AGUA;
        factorY = deltaTime * VELOCIDAD_AGUA;

        factorX %= 1;
        factorY %= 1;

        // modifica la textura de refracción para simular el efecto de refracción
        // textRefraccion.setOffsetX(factorX);
        // textRefraccion.setOffsetY(factorY);
        lastTime = System.currentTimeMillis();

        // actualiza la textura de la salida con la información de la reflexión
        outputTexture.setFuerzaOla(FUERZA_OLA);
        outputTexture.setFactorTiempo(factorX);
        // textSalida.setRazon(0.5f); //por iguales
        // textSalida.setRazon(0f); //todo reflexion
        // textSalida.setRazon(1f);//todo refraccion
        // textSalida.setRazon(0.9f);
        // efecto fresnel

        // este calculo se deberia hacer para cada pixel de la superficie, pues el
        // angulo es diferente para cada punto de la superficie del agua
        QVector3 vision = mainRender.getCamara().getTransformacion().getTraslacion().clone()
                .subtract(entity.getTransformacion().getTraslacion().clone());
        // el factor fresnel representa que tanta refraccion se aplica
        float factorFresnel = arriba.dot(vision.normalize());
        factorFresnel = QMath.pow(factorFresnel, 0.5f);// para que sea menos reflectante (si se una un numero positivo
                                                       // en el exponente seria mas reflectivo)
        outputTexture.setRazon(factorFresnel);
        entity.setToRender(true);
    }

    @Override
    public void destruir() {
        if (textReflexion != null) {
            textReflexion.destruir();
            textReflexion = null;
        }
    }

}
