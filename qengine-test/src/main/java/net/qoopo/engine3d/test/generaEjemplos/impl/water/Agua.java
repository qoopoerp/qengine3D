/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.water;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlano;
import net.qoopo.engine.core.entity.component.water.Water;
import net.qoopo.engine.core.entity.component.water.WaterDuDv;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class Agua extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;
         
            Entity entity = new Entity("Agua");
            WaterDuDv water = new WaterDuDv(mundo, 350,350);
            entity.addComponent(water);
            water.build();
            entity.scale(0.1f);
            mundo.addEntity(entity);
        } catch (Exception ex) {
            Logger.getLogger(Agua.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
