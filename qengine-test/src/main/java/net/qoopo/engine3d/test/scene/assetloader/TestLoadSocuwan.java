/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.assetloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.assets.model.waveobject.LoadModelObj;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.ComponentUtil;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 *
 * @author alberto
 */
public class TestLoadSocuwan extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;

            ModelLoader loadModel = new LoadModelObj();

            // Cargador propio
            List<File> archivos = new ArrayList<>();

            String[] values = "Terrain;Statue;Houses;Trees;Path Edge;Rocks;Bushes;Bridge;Fences;Windmill;Mushrooms;Herb Stall;Barrels;Cute Trees;Bench;Lanterns;Lilly Pads;Foxes;Turtles"
                    .split(";");
            for (String value : values) {
                archivos.add(new File("assets/models/obj/socuwan/" + value + "/model.obj"));
            }

            for (File file:archivos) {
                try {                    
                    Entity entidad = loadModel.loadModel(file);                
                    Texture textura= AssetManager.get().loadTexture(file.getParentFile().getName(),new File(file.getParentFile(),"diffuse.png"));
                    Material material= new Material(textura);
                    MaterialUtil.applyMaterial(ComponentUtil.getMesh(entidad), material);
                    mundo.addEntity(entidad);
                } catch (Exception e) {
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(TestLoadMd5.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
