/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Plane;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class FloorTestScene extends MakeTestScene {

        public void make(Scene mundo) {
                try {
                        this.scene = mundo;
                        Entity piso = new Entity("Piso");
                        Mesh pisoGeometria = new Plane(50, 50);

                        Material material = new Material();

                        Texture albedo = AssetManager.get().loadTexture("difusa", new File("assets/"
                                        + "textures/pbr/floor/rectangle-polished-tile-ue/albedo.png"));
                        Texture normal = AssetManager.get().loadTexture("normal", new File("assets/"
                                        + "textures/pbr/floor/rectangle-polished-tile-ue/normal.png"));
                        Texture rugoso = AssetManager.get().loadTexture("rugoso", new File("assets/"
                                        + "textures/pbr/floor/rectangle-polished-tile-ue/roughness.png"));
                        Texture metalico = AssetManager.get().loadTexture("metalico", new File("assets/"
                                        + "textures/pbr/floor/rectangle-polished-tile-ue/metallic.png"));
                        Texture sombras = AssetManager.get().loadTexture("ao", new File("assets/" +
                                        "textures/pbr/floor/rectangle-polished-tile-ue/ao.png"));

                        material.setMapaColor(albedo);
                        material.setMapaNormal(normal);
                        material.setMapaRugosidad(rugoso);
                        material.setMapaMetalico(metalico);
                        material.setMapaSAO(sombras);

                        material.getMapaColor().setMuestrasU(10);
                        material.getMapaColor().setMuestrasV(10);
                        material.getMapaNormal().setMuestrasU(10);
                        material.getMapaNormal().setMuestrasV(10);
                        material.getMapaRugosidad().setMuestrasU(10);
                        material.getMapaRugosidad().setMuestrasV(10);
                        material.getMapaMetalico().setMuestrasU(10);
                        material.getMapaMetalico().setMuestrasV(10);
                        material.getMapaSAO().setMuestrasU(10);
                        material.getMapaSAO().setMuestrasV(10);
                        MaterialUtil.applyMaterial(pisoGeometria, material);

                        piso.addComponent(pisoGeometria);
                        mundo.addEntity(piso);
                } catch (Exception ex) {
                        Logger.getLogger(FloorTestScene.class.getName()).log(Level.SEVERE, null, ex);
                }

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
