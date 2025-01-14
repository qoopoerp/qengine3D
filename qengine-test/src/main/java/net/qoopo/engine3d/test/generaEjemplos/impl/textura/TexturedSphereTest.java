/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.textura;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class TexturedSphereTest extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;

        Material material = new Material();
        material.setColorMap(
                AssetManager.get().loadTexture("difusa", new File("assets/textures/solar_system/2k_earth_daymap.jpg")));
        material.setNormalMap(AssetManager.get().loadTexture("normal",
                new File("assets/textures/solar_system/2k_earth_normal_map.png")));

        Entity earth = new Entity("Tierra");
        Mesh mesh = new Sphere(4);
        mesh.applyMaterial(material);
        earth.addComponent(mesh);
        mundo.addEntity(earth);
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
