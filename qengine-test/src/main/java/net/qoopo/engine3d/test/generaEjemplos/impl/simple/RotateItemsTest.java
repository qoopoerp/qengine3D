/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.movement.RotationComponent;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class RotateItemsTest extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;

            // uso esta lista en lugar del mundo par arotar.. para rotar solo aquellas
            // entidades creadas antes de llamar a esta clase.. y asi evitar rotar las
            // posteriores como terrenos o pisos que no desean ser rotados
            // List<Entity> lista = new ArrayList<>();

            for (Entity entidad : mundo.getEntities()) {
                if (!(entidad instanceof Camera)) {
                    // lista.add(entidad);
                    entidad.addComponent(new RotationComponent());
                }
            }

            // Thread hilo = new Thread(new Runnable() {
            // @Override
            // public void run() {

            // float angulo = 0;

            // while (true) {
            // try {
            // Thread.sleep(100);
            // } catch (InterruptedException ex) {
            // }
            // for (Entity entidad : lista) {
            // entidad.rotate(angulo, angulo, angulo);
            // }
            // angulo += (float) Math.toRadians(10);
            // }
            // }
            // });
            // hilo.start();

        } catch (Exception ex) {
            Logger.getLogger(RotateItemsTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
