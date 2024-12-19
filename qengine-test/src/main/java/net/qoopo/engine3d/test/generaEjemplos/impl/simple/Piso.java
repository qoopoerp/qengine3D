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
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlano;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class Piso extends MakeTestScene {

        public void make(Scene mundo) {
                try {
                        this.scene = mundo;
                        Entity piso = new Entity("Piso");
                        Mesh pisoGeometria = new QPlano(50, 50);

                        QMaterialBas material = new QMaterialBas();

                        QTextura albedo = AssetManager.get().loadTexture("difusa", new File("assets/"
                                        + "textures/pbr/floor/rectangle-polished-tile-ue/albedo.png"));
                        QTextura normal = AssetManager.get().loadTexture("normal", new File("assets/"
                                        + "textures/pbr/floor/rectangle-polished-tile-ue/normal.png"));
                        QTextura rugoso = AssetManager.get().loadTexture("rugoso", new File("assets/"
                                        + "textures/pbr/floor/rectangle-polished-tile-ue/roughness.png"));
                        QTextura metalico = AssetManager.get().loadTexture("metalico", new File("assets/"
                                        + "textures/pbr/floor/rectangle-polished-tile-ue/metallic.png"));
                        QTextura sombras = AssetManager.get().loadTexture("ao", new File("assets/"+
                                        "textures/pbr/floor/rectangle-polished-tile-ue/ao.png"));

                        material.setMapaColor(new QProcesadorSimple(albedo));
                        material.setMapaNormal(new QProcesadorSimple(normal));
                        material.setMapaRugosidad(new QProcesadorSimple(rugoso));
                        material.setMapaMetalico(new QProcesadorSimple(metalico));
                        material.setMapaSAO(new QProcesadorSimple(sombras));

                        material.getMapaColor().setMuestrasU(10);
                        material.getMapaColor().setMuestrasV(10);
                        QMaterialUtil.aplicarMaterial(pisoGeometria, material);

                        piso.addComponent(pisoGeometria);
                        mundo.addEntity(piso);
                } catch (Exception ex) {
                        Logger.getLogger(Piso.class.getName()).log(Level.SEVERE, null, ex);
                }

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
