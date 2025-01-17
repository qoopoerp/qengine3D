/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.pbr;

import java.io.File;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Teapot;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.material.util.PbrUtil;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 *
 * @author alberto
 */
public class PbrTeapotTest extends MakeTestScene {

        public void make(Scene mundo) {
                this.scene = mundo;

                Material material = PbrUtil
                                .loadPbrMaterial(new File("assets/textures/pbr/floor/Old Stone Tile/tiledstone-dx/"),
                                                true, 4);

                Material material2 = PbrUtil
                                .loadPbrMaterial(new File("assets/textures/pbr/floor/Old Stone Tile/tiledstone-dx/"),
                                                4);

                Material material3 = PbrUtil
                                .loadPbrMaterial(new File("assets/textures/pbr/floor/Old Stone Tile/tiledstone1-OGL/"),
                                                4);

                Entity objeto = new Entity("PBR");
                objeto.addComponent(MaterialUtil.applyMaterial(new Teapot(), material));

                objeto.move(-3, 0, 0);
                mundo.addEntity(objeto);

                Entity objeto2 = new Entity("PBR 2");
                objeto2.addComponent(MaterialUtil.applyMaterial(new Teapot(), material2));
                objeto2.move(0, 0, 0);
                mundo.addEntity(objeto2);

                Entity objeto3 = new Entity("PBR 3");
                objeto3.addComponent(MaterialUtil.applyMaterial(new Teapot(), material3));
                objeto3.move(3, 0, 0);
                mundo.addEntity(objeto3);

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
