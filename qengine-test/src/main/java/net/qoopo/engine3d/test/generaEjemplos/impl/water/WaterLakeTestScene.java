/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.water;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.terrain.Terrain;
import net.qoopo.engine.core.entity.component.water.WaterDuDv;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.terrain.HeightMapTerrain;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class WaterLakeTestScene extends MakeTestScene {

        @Override
        public void make(Scene scene) {
                try {
                        this.scene = scene;

                        // el terreno generado con mapas de altura
                        Entity terrainEntity = new Entity("Terreno");

                        QTextura terrainTexture = AssetManager.get().loadTexture("terreno",
                                        "assets/textures/terrain/rocky_terrain/rocky_terrain_02_diff_4k.jpg");
                        QMaterialBas materialTerrain = new QMaterialBas(terrainTexture);
                        materialTerrain.setMapaNormal(AssetManager.get().loadTexture("terreno_normal",
                                        "assets/textures/terrain/rocky_terrain/rocky_terrain_02_nor_gl_4k.png"));
                        materialTerrain.setMapaEspecular(AssetManager.get().loadTexture("terreno_normal",
                                        "assets/textures/terrain/rocky_terrain/rocky_terrain_02_spec_4k.png"));
                        materialTerrain.setMapaRugosidad(AssetManager.get().loadTexture("terreno_normal",
                                        "assets/textures/terrain/rocky_terrain/rocky_terrain_02_rough_4k.png"));

                        Terrain terrain = new HeightMapTerrain(new File("assets/heightmaps/lake3.jpg"), 1, -5, 15f,
                                        2,
                                        materialTerrain, true);
                        terrainEntity.addComponent(terrain);
                        terrain.build();
                        terrainEntity.scale(0.1f);
                        scene.addEntity(terrainEntity);
                        Entity entityWater = new Entity("Agua");
                        // Water water = new Ocean(scene, (int) terrain.getWidth(), (int)
                        // terrain.getHeight(),
                        // (int) (terrain.getWidth() / 2.0f));
                        WaterDuDv water = new WaterDuDv(scene, (int) terrain.getWidth(), (int) terrain.getHeight());
                        water.setTerrain(terrain);
                        entityWater.addComponent(water);
                        entityWater.scale(0.1f);
                        entityWater.move(0, -2, 0);
                        water.build();
                        scene.addEntity(entityWater);
                        // Arbol

                        Entity tree = AssetManager.get().loadModel(new File("assets/models/fbx/lowpoly/tree.fbx"));
                        tree.scale(0.001f);
                        tree.move(6, -2, -6);
                        scene.addEntity(tree);


                } catch (Exception ex) {
                        Logger.getLogger(WaterLakeTestScene.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
