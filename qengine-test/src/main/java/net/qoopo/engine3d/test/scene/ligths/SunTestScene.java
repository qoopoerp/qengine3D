/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.ligths;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.scene.MakeTestScene;
import net.qoopo.engine3d.world.Sun;

/**
 *
 *
 * @author alberto
 */
public class SunTestScene extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;
        // agrega luz ambiental
        mundo.setAmbientColor(QColor.of(0.10f, 0.10f, 0.10f));
        // agrega luz direccional que es el Sol
        Sun sun = new Sun("Sol");
        sun.rotate(Math.toRadians(45), 0, Math.toRadians(-45));
        mundo.addEntity(sun);
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
