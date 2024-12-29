/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.pbr;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.cubemap.QCubeMap;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class PBREsfera extends MakeTestScene {

        public void make(Scene mundo) {
                this.scene = mundo;

                // //-------------------------
                QTextura albedo = AssetManager.get().loadTexture("difusa", new File("assets/"
                                + "textures/pbr/metal/used-stainless-steel/albedo.png"));
                QTextura normal = AssetManager.get().loadTexture("normal", new File("assets/"
                                + "textures/pbr/metal/used-stainless-steel/normal.png"));
                QTextura rugoso = AssetManager.get().loadTexture("rugoso", new File("assets/"
                                + "textures/pbr/metal/used-stainless-steel/roughness.png"));
                QTextura metalico = AssetManager.get().loadTexture("metalico", new File("assets/"
                                + "textures/pbr/metal/used-stainless-steel/metallic.png"));
                QTextura sombras = AssetManager.get().loadTexture("ao", new File("assets/" +
                                "textures/pbr/metal/used-stainless-steel/ao.png"));

                QMaterialBas material = new QMaterialBas();

                material.setColorBase(QColor.WHITE);
                material.setMapaColor(albedo);
                material.setMapaNormal(normal);
                material.setMapaRugosidad(rugoso);
                material.setMapaMetalico(metalico);
                material.setMapaSAO(sombras);

                Entity objeto = new Entity("Esfera PBR");
                objeto.addComponent(MaterialUtil.applyMaterial(new Sphere(1f), material));
                // -------------------------------------
                QCubeMap cubeMap = new QCubeMap(QGlobal.MAPA_CUPO_RESOLUCION);
                cubeMap.setGenerarIrradiacion(true);
                material.setMapaEntorno(cubeMap.getTexturaEntorno());
                material.setTipoMapaEntorno(QCubeMap.FORMATO_MAPA_CUBO);
                objeto.addComponent(cubeMap);
                cubeMap.aplicar(QCubeMap.FORMATO_MAPA_CUBO, 0.8f, 0);
                mundo.addEntity(objeto);
        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
