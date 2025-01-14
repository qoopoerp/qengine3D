/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import java.io.File;
import java.util.Random;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 *
 * @author alberto
 */
public class TexturedSphereUniverseTest extends MakeTestScene {

    private Material material = null;

    private void loadMaterial() {
        material = null;
        try {
            // int colorTransparencia = -1;
            material = new Material();
            material.setColorMap(AssetManager.get().loadTexture("difusa",
                    new File("assets/textures/solar_system/2k_earth_daymap.jpg")));
            material.setNormalMap(AssetManager.get().loadTexture("normal",
                    new File("assets/textures/solar_system/2k_earth_normal_map.png")));
            // material.texturaColorTransparente = colorTransparencia;
            // if (colorTransparencia != -1) {
            // material.transAlfa = 0.99f;// el objeto tiene una trasnparencia
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void make(Scene mundo) {
        this.scene = mundo;
        loadMaterial();
        Random rnd = new Random();
        float tamUniverso = 100;

        Mesh geometria = new Sphere(1);
        MaterialUtil.applyMaterial(geometria, material);
        for (int i = 0; i < 100; i++) {
            Entity esfera = new Entity("Esfera [" + i + "]");
            esfera.move(rnd.nextFloat() * tamUniverso * 2 - tamUniverso,
                    rnd.nextFloat() * tamUniverso * 2 - tamUniverso, rnd.nextFloat() * tamUniverso * 2 - tamUniverso);
            esfera.addComponent(geometria);
            mundo.addEntity(esfera);
        }
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
