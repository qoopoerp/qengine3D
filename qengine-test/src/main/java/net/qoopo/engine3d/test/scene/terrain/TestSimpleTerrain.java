/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.terrain;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.terrain.Terrain;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.terrain.SimpleTerrain;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 *
 * @author alberto
 */
public class TestSimpleTerrain extends MakeTestScene {

        public void make(Scene mundo) {
                this.scene = mundo;
                Entity entidad = new Entity("Terreno");

                Texture terrainTexture = AssetManager.get().loadTexture("terreno",
                                "assets/textures/terrain/rocky_terrain/rocky_terrain_02_diff_4k.jpg");
                Material materialTerrain = new Material(terrainTexture);
                materialTerrain.setNormalMap(AssetManager.get().loadTexture("terreno_normal",
                                "assets/textures/terrain/rocky_terrain/rocky_terrain_02_nor_gl_4k.png"));
                materialTerrain.setMapaEspecular(AssetManager.get().loadTexture("terreno_normal",
                                "assets/textures/terrain/rocky_terrain/rocky_terrain_02_spec_4k.png"));
                materialTerrain.setRoughnessMap(AssetManager.get().loadTexture("terreno_normal",
                                "assets/textures/terrain/rocky_terrain/rocky_terrain_02_rough_4k.png"));

                Terrain terreno = new SimpleTerrain(50, 50, 1f, materialTerrain, true);
                entidad.addComponent(terreno);
                terreno.build();
                mundo.addEntity(entidad);
        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
