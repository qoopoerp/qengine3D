/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.simple;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Plane;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaConvexa;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.material.util.PbrUtil;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 *
 * @author alberto
 */
public class FloorTestScene extends MakeTestScene {

        public void make(Scene mundo) {
                try {
                        this.scene = mundo;
                        Entity piso = new Entity("Piso");
                        Mesh mesh = new Plane(15, 15);

                        Material material = PbrUtil
                                        .loadPbrMaterial(new File(
                                                        "assets/textures/pbr/floor/concrete_yellow_gray/"), 10);

                        mesh.applyMaterial(material);
                        piso.addComponent(mesh);
                        piso.addComponent(new QColisionMallaConvexa(mesh));
                        mundo.addEntity(piso);
                } catch (Exception ex) {
                        Logger.getLogger(FloorTestScene.class.getName()).log(Level.SEVERE, null, ex);
                }

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
