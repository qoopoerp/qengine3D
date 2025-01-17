/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.simple;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.movement.RotationEntityComponent;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 *
 * @author alberto
 */
public class RotateItemsTest extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;

            for (Entity entidad : mundo.getEntities()) {
                if (!(entidad instanceof Camera)) {
                    // lista.add(entidad);
                    entidad.addComponent(new RotationEntityComponent());
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(RotateItemsTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
