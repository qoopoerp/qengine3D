/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.reflections;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.UpdatableComponent;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;

/**
 * Genera una reflexion planar.Útil en espejos, pisos etc.
 *
 * @author alberto
 */
public class PlanarReflection implements EntityComponent, UpdatableComponent {

    @Getter
    @Setter
    private Entity entity;

    // es el renderizador que tomara la imagen para reflejar
    private RenderEngine render;

    private int metodo = 1;

    public PlanarReflection(Texture textura, Scene universo, int ancho, int alto) {
        // this.texturaEspejo = textura;
        try {

            // render = new QRender(universo, "Reflexión", null, ancho, ancho);
            render = AssetManager.get().getRendererFactory().createRenderEngine(universo, "mirror", null, ancho,
                    ancho);
            render.setTextura(textura);
            render.setFilterQueue(null);
            render.setCamera(new Camera("mirror"));
            render.opciones.setForzarResolucion(true);
            render.opciones.setNormalMapping(false);
            render.opciones.setSombras(false);
            render.opciones.setDibujarCarasTraseras(false);
            render.setShowStats(false);
            render.resize();
            render.setRenderReal(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(RenderEngine mainRender, Scene universo) {
        entity.setToRender(false);
        // actualizo la resolución de acuerdo a la cámara
        if (render.opciones.getAncho() != mainRender.getFrameBuffer().getWidth()
                && render.opciones.getAlto() != mainRender.getFrameBuffer().getHeight()) {
            render.opciones.setAncho(mainRender.getFrameBuffer().getWidth());
            render.opciones.setAlto(mainRender.getFrameBuffer().getHeight());
            render.resize();
        }

        render.getCamera().setName(entity.getName());
        render.setName(entity.getName());

        // el proceo consiste en el siguiente
        // debo colocar la camara en la posicion contraria a la que viene y en direcicon
        // contraria
        // la posicion sera igual a restar un vector a la posicion
        // este vector es el resultado de restar la camara menos la posicion de este
        // espejo, el centro dle espejo
        if (metodo == 1) {
            // metoo uno con la camara fija en el espejo
            try {
                render.getCamera().lookAt(entity.getTransform().getLocation(), entity.getDirection(),
                        entity.getUp().clone());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (metodo == 2) {
            // metodo 2
            // el vector del rayo de vision
            Vector3 vision = Vector3.empty();
            vision.set(
                    entity.getTransform().getLocation().x
                            - mainRender.getCamera().getTransform().getLocation().x,
                    entity.getTransform().getLocation().y
                            - mainRender.getCamera().getTransform().getLocation().y,
                    entity.getTransform().getLocation().z
                            - mainRender.getCamera().getTransform().getLocation().z);
            // entity.transformacion.getLocation().clone().add(camara.transformacion.getLocation().clone().multiply(-1));
            // http://di002.edv.uniovi.es/~rr/Tema5.pdf
            // QVector3 rayoReflejado = vision.add(
            // entity.getDireccion().clone().multiply(vision.clone().dot(entity.getDireccion().clone())).multiply(-2)
            // );
            Vector3 rayoReflejado = entity.getDirection().clone().multiply(entity.getDirection().clone().dot(vision))
                    .multiply(2).add(vision.clone().multiply(-1));

            try {
                // hasta ahorita tengo el rayo reflejado, pero necesito que este en la posicion
                // contraria
                Vector3 nuevaPos = entity.getTransform().getLocation().clone()
                        .add(rayoReflejado.multiply(0.25f));
                // ya tenemos en la posicion correcta pero hay un problema, esta renderizando a
                // su propia geometia y colapsa la vision (ya se corrigio con la bandera de
                // renderizar)

                render.getCamera().lookAtTarget(nuevaPos, entity.getTransform().getLocation().clone(),
                        entity.getUp().clone());

                // render.setPanelClip(new ClipPane(entity.getDireccion(),
                // -entity.transformacion.getLocation().length()));
                // plano de recorte para no ver el espejo tapando
                // render.getCamara().frustrumCerca = rayoReflejado.length();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        render.update();
        render.shadeFragments();
        render.draw();
        entity.setToRender(true);
    }

    @Override
    public void destroy() {
    }

}
