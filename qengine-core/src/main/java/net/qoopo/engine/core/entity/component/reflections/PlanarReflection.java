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
import net.qoopo.engine.core.math.QVector3;
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
            render.setEfectosPostProceso(null);
            render.setCamara(new Camera("mirror"));
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
        if (render.opciones.getAncho() != mainRender.getFrameBuffer().getAncho()
                && render.opciones.getAlto() != mainRender.getFrameBuffer().getAlto()) {
            render.opciones.setAncho(mainRender.getFrameBuffer().getAncho());
            render.opciones.setAlto(mainRender.getFrameBuffer().getAlto());
            render.resize();
        }

        render.getCamara().setName(entity.getName());
        render.setNombre(entity.getName());

        // el proceo consiste en el siguiente
        // debo colocar la camara en la posicion contraria a la que viene y en direcicon
        // contraria
        // la posicion sera igual a restar un vector a la posicion
        // este vector es el resultado de restar la camara menos la posicion de este
        // espejo, el centro dle espejo
        if (metodo == 1) {
            // metoo uno con la camara fija en el espejo
            try {
                render.getCamara().lookAt(entity.getTransformacion().getTraslacion(), entity.getDirection(),
                        entity.getUp().clone());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (metodo == 2) {
            // metodo 2
            // el vector del rayo de vision
            QVector3 vision = QVector3.empty();
            vision.set(
                    entity.getTransformacion().getTraslacion().x
                            - mainRender.getCamara().getTransformacion().getTraslacion().x,
                    entity.getTransformacion().getTraslacion().y
                            - mainRender.getCamara().getTransformacion().getTraslacion().y,
                    entity.getTransformacion().getTraslacion().z
                            - mainRender.getCamara().getTransformacion().getTraslacion().z);
            // entity.transformacion.getTraslacion().clone().add(camara.transformacion.getTraslacion().clone().multiply(-1));
            // http://di002.edv.uniovi.es/~rr/Tema5.pdf
            // QVector3 rayoReflejado = vision.add(
            // entity.getDireccion().clone().multiply(vision.clone().dot(entity.getDireccion().clone())).multiply(-2)
            // );
            QVector3 rayoReflejado = entity.getDirection().clone().multiply(entity.getDirection().clone().dot(vision))
                    .multiply(2).add(vision.clone().multiply(-1));

            try {
                // hasta ahorita tengo el rayo reflejado, pero necesito que este en la posicion
                // contraria
                QVector3 nuevaPos = entity.getTransformacion().getTraslacion().clone()
                        .add(rayoReflejado.multiply(0.25f));
                // ya tenemos en la posicion correcta pero hay un problema, esta renderizando a
                // su propia geometia y colapsa la vision (ya se corrigio con la bandera de
                // renderizar)

                render.getCamara().lookAtTarget(nuevaPos, entity.getTransformacion().getTraslacion().clone(),
                        entity.getUp().clone());

                // render.setPanelClip(new ClipPane(entity.getDireccion(),
                // -entity.transformacion.getTraslacion().length()));
                // plano de recorte para no ver el espejo tapando
                // render.getCamara().frustrumCerca = rayoReflejado.length();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        render.update();
        render.shadeFragments();
        render.postRender();
        entity.setToRender(true);
    }

    @Override
    public void destruir() {
    }

}
