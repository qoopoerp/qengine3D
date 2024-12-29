/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.water;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.water.Water;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.water.Ocean;
import net.qoopo.engine.water.distortion.RadialWavesDistortion;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class RadialWaterTest extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;
            Entity entity = new Entity("Agua");
            Water water = new Ocean(mundo, 500, 500, 80, new RadialWavesDistortion(QVector3.zero,3.0f, 20.0f, 1.0f,50.0f));
            entity.addComponent(water);
            water.build();
            entity.scale(0.1f);
            mundo.addEntity(entity);
        } catch (Exception ex) {
            Logger.getLogger(WaterDuDvTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
