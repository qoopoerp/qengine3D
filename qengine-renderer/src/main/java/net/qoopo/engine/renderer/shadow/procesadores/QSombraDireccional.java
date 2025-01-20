/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shadow.procesadores;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.shadow.QProcesadorSombra;
import net.qoopo.engine.renderer.shadow.SoftwareShadowRender;

/**
 * Procesador de sombras para luces Direccionales
 *
 * @author alberto
 */
public class QSombraDireccional extends QProcesadorSombra {

    // un solo render de sombras pues solo hay una direccion
    private SoftwareShadowRender renderSombra;

    public QSombraDireccional(Scene mundo, QDirectionalLigth luz, Camera camaraRender, int ancho, int alto) {
        super(mundo, luz, ancho, alto);
        try {
            renderSombra = new SoftwareShadowRender(SoftwareShadowRender.DIRECIONALES, mundo, luz, camaraRender, ancho, alto);
            renderSombra.resize();
            renderSombra.clean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public float factorSombra(Vector3 vector, Entity entity) {
        return renderSombra.factorSombra(vector, entity);
    }

    @Override
    public void generarSombras() {
        if (renderSombra != null && (actualizar || dinamico)) {
            renderSombra.update();
        }
        actualizar = false;
    }

    public void actualizarDireccion(Vector3 direccion) {
        if (renderSombra != null) {
            renderSombra.setDireccion(direccion);
        }
    }

    @Override
    public void destruir() {
        renderSombra = null;
    }

}
