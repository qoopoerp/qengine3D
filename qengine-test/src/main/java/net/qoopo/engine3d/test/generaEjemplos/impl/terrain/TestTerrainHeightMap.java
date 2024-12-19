/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.terrain;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.terrain.HeightMapTerrain;
import net.qoopo.engine.core.entity.component.terrain.Terrain;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class TestTerrainHeightMap extends MakeTestScene {

        public void make(Scene mundo) {
                this.scene = mundo;
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

                Terrain terreno = new HeightMapTerrain(new File("assets/heightmaps/heightmap.png"), 1, -5, 15f,
                                1,
                                materialTerrain, true);
                terrainEntity.addComponent(terreno);
                terreno.build();
                mundo.addEntity(terrainEntity);
        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
