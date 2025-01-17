/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.pbr;

import java.io.File;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
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
public class PbrSphereTest extends MakeTestScene {

        public void make(Scene mundo) {
                this.scene = mundo;

                Material material = PbrUtil
                                .loadPbrMaterial(new File("assets/textures/pbr/floor/Shades Tile/shades-tile-ue/"),
                                                true);

                Material material2 = PbrUtil
                                .loadPbrMaterial(new File("assets/textures/pbr/floor/Shades Tile/shades-tile-ue/"));

                Material material3 = PbrUtil
                                .loadPbrMaterial(new File("assets/textures/pbr/floor/Shades Tile/shades-tile-unity/"));

                Entity objeto = new Entity("Esfera PBR");
                objeto.addComponent(MaterialUtil.applyMaterial(new Sphere(1f), material));
                objeto.move(-3, 3, 0);
                mundo.addEntity(objeto);

                Entity objeto2 = new Entity("Esfera PBR 2");
                objeto2.addComponent(MaterialUtil.applyMaterial(new Sphere(1f), material2));
                objeto2.move(0, 3, 0);
                mundo.addEntity(objeto2);

                Entity objeto3 = new Entity("Esfera PBR 3");
                objeto3.addComponent(MaterialUtil.applyMaterial(new Sphere(1f), material3));
                objeto3.move(3, 3, 0);
                mundo.addEntity(objeto3);

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
