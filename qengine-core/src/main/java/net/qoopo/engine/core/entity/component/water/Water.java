/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.water;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.engine.EngineTime;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.UpdatableComponent;
import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.entity.component.terrain.Terrain;
import net.qoopo.engine.core.entity.component.water.texture.WaterTextureProcessor;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.renderer.buffer.FrameBuffer;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.ClipPane;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;

/**
 * Procesador para simular agua modificando la textura de un Plano.
 *
 *
 * @author alberto
 */
@Getter
@Setter
public abstract class Water implements EntityComponent, UpdatableComponent {

    @Getter
    @Setter
    protected Entity entity;

    protected final static float VELOCIDAD_AGUA = 0.05f;

    protected Vector3 velocity = Vector3.of(0.05f, 0.05f, 0.05f);

    protected Vector3 distortion = Vector3.zero.clone();

    protected final static float FUERZA_OLA = 0.005f;

    protected boolean enableReflection = true;
    protected boolean enableRefraction = true;

    transient protected RenderEngine render;
    protected WaterTextureProcessor outputTexture = null;// la mezcla de la textura de reflexion y la textur de
                                                         // refracción
    protected FrameBuffer frameReflexion;
    protected FrameBuffer frameRefraccion;
    protected Texture textReflexion;
    protected Texture textRefraccion;

    // sera usada para simular movimiento de agua con el tiempo agregando un offset
    // a las textures

    protected float factorX;
    protected float factorY;
    protected float factorZ;

    protected final Vector3 arriba = Vector3.unitario_y.clone();
    protected final Vector3 abajo = Vector3.unitario_y.clone().multiply(-1.0f);

    protected int width;
    protected int height;
    protected Scene scene;

    protected Shape mesh;
    protected Material material;

    // Si se adjunta un terreno al que el agua pertenece se puede realizar más
    // efectos en función de la profundidad por ejemplo
    protected Terrain terrain;

    public Water() {

    }

    public void build() {

        try {

            this.textReflexion = new Texture();
            this.textReflexion.setSignoY(-1);
            this.textRefraccion = new Texture();

            if (enableReflection || enableRefraction) {
                frameReflexion = new FrameBuffer(width / 4, height / 4, textReflexion);
                frameRefraccion = new FrameBuffer(width / 4, height / 4, textRefraccion);
                render = AssetManager.get().getRendererFactory().createRenderEngine(scene, "Water", null, width / 4,
                        width / 4);
                render.setFilterQueue(null);
                render.opciones.setForzarResolucion(true);
                render.opciones.setNormalMapping(false);
                render.opciones.setSombras(false);
                render.opciones.setDibujarCarasTraseras(false);
                render.setShowStats(false);
                render.setCamera(new Camera("WaterCamera"));
                render.resize();
                render.setRenderReal(false);
                render.opciones.setDibujarCarasTraseras(true);
                render.setPanelClip(new ClipPane(arriba, 0));
                outputTexture = new WaterTextureProcessor(textReflexion, textRefraccion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateReflections(RenderEngine mainRender, Scene scene) {

        if (!mainRender.opciones.isMaterial()) {
            return;
        }
        if (mainRender.getFrameBuffer() == null) {
            return;
        }

        if (render.opciones.getAncho() != mainRender.getFrameBuffer().getWidth()
                && render.opciones.getAlto() != mainRender.getFrameBuffer().getHeight()) {
            render.opciones.setAncho(mainRender.getFrameBuffer().getWidth());
            render.opciones.setAlto(mainRender.getFrameBuffer().getHeight());
            frameReflexion = new FrameBuffer(render.opciones.getAncho(), render.opciones.getAlto(), textReflexion);
            frameRefraccion = new FrameBuffer(render.opciones.getAncho(), render.opciones.getAlto(),
                    textRefraccion);
            render.resize();
        }

        render.setScene(scene);
        render.setName(entity.getName());
        render.getCamera().setName(entity.getName());
        try {

            // REFRACCION
            // -------------------------------------------------------------------
            render.getCamera().setTransform(mainRender.getCamera().getTransform().clone());// la misma
                                                                                           // posicion de
                                                                                           // la
                                                                                           // cámara
                                                                                           // actual
            render.getCamera().getTransform().getRotation().updateEuler();
            render.setFrameBuffer(frameRefraccion);
            render.getPanelClip().setNormal(abajo);
            render.getPanelClip().setDistancia(entity.getTransform().getLocation().y - 1.0f);
            render.update();
            render.getFrameBuffer().updateOuputTexture();
            // REFLEXION -------------------------------------------------------------------
            // movemos la cámara debajo de la superficie del agua a una distancia 2 veces de
            // la actual altura
            render.setFrameBuffer(frameReflexion);
            render.getPanelClip().setNormal(arriba);
            render.getPanelClip().setDistancia(entity.getTransform().getLocation().y + 0.5f);

            float distancia = 2 * (mainRender.getCamera().getTransform().getLocation().y
                    - entity.getTransform().getLocation().y);
            Vector3 nuevaPos = mainRender.getCamera().getTransform().getLocation().clone();
            nuevaPos.y -= distancia;

            // invertir angulo Pitch
            render.getCamera().setTransform(mainRender.getCamera().getTransform().clone());
            render.getCamera().getTransform().getRotation().updateEuler();
            render.getCamera().getTransform().getRotation().getEulerAngles().x *= -1;
            render.getCamera().getTransform().getRotation().updateCuaternion();
            render.getCamera().getTransform().move(nuevaPos);
            render.update();
            render.shadeFragments();
            render.draw();
            render.getFrameBuffer().updateOuputTexture();

            // modifica la textura de refracción para simular el efecto de refracción
            // textRefraccion.setOffsetX(factorX);
            // textRefraccion.setOffsetY(factorY);

            // actualiza la textura de la salida con la información de la reflexión

            outputTexture.setFuerzaOla(FUERZA_OLA);
            outputTexture.setFactorTiempo(factorX);

            // textSalida.setRazon(0.5f); //por iguales
            // textSalida.setRazon(0f); //todo reflexion
            // textSalida.setRazon(1f);//todo refraccion
            // textSalida.setRazon(0.9f);
            // Efecto fresnel
            // -> Este calculo se deberia hacer para cada pixel de la superficie, pues el
            // angulo es diferente para cada punto de la superficie del agua
            Vector3 vision = mainRender.getCamera().getTransform().getLocation().clone()
                    .subtract(entity.getTransform().getLocation().clone());
            // el factor fresnel representa que tanta refraccion se aplica
            float factorFresnel = arriba.dot(vision.normalize());
            factorFresnel = QMath.pow(factorFresnel, 0.5f);// para que sea menos reflectante (si se una un numero
                                                           // positivo
                                                           // en el exponente seria mas reflectivo)
            outputTexture.setRazon(factorFresnel);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(RenderEngine mainRender, Scene scene) {
        // evita la ejecucion si no esta activo los materiales

        entity.setToRender(false);// evita renderizar el agua

        // calcula el tiempo delta para la distorsión de la textura de salida

        // DELTA para cada eje
        // factorX = deltaTime * VELOCIDAD_AGUA;
        factorX = EngineTime.deltaNano * velocity.x;
        factorY = EngineTime.deltaNano * velocity.y;
        factorZ = EngineTime.deltaNano * velocity.z;

        factorX %= 1;
        factorY %= 1;
        factorZ %= 1;

        // actualizo la resolución de acuerdo a la cámara
        if (enableReflection || enableRefraction) {
            updateReflections(mainRender, scene);
        }

        entity.setToRender(true);
    }

    @Override
    public void destroy() {
        if (textReflexion != null) {
            textReflexion.destroy();
            textReflexion = null;
        }

        if (textRefraccion != null) {
            textRefraccion.destroy();
            textRefraccion = null;
        }
    }

}
