/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.pbr;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.cubemap.CubeMap;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Teapot;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class PBRTetera extends MakeTestScene {

        public void make(Scene mundo) {
                this.scene = mundo;
                Material material = new Material();
                Texture albedo = AssetManager.get().loadTexture("difusa", new File("assets/"
                                + "textures/pbr/metal/used-stainless-steel/albedo.png"));
                Texture normal = AssetManager.get().loadTexture("normal", new File("assets/"
                                + "textures/pbr/metal/used-stainless-steel/normal.png"));
                Texture rugoso = AssetManager.get().loadTexture("rugoso", new File("assets/"
                                + "textures/pbr/metal/used-stainless-steel/roughness.png"));
                Texture metalico = AssetManager.get().loadTexture("metalico", new File("assets/"
                                + "textures/pbr/metal/used-stainless-steel/metallic.png"));
                Texture sombras = AssetManager.get().loadTexture("ao", new File("assets/" +
                                "textures/pbr/metal/used-stainless-steel/ao.png"));

                material.setMapaColor(albedo);
                material.setMapaNormal(normal);
                material.setMapaRugosidad(rugoso);
                material.setMapaMetalico(metalico);
                material.setMapaSAO(sombras);

                Entity objeto = new Entity("tetera");
                // objeto.agregarComponente(QMaterialUtil.aplicarMaterial(malla, material));
                objeto.addComponent(MaterialUtil.applyMaterial(new Teapot(), material));
                mundo.addEntity(objeto);
                // -------------------------------------
                CubeMap mapa = new CubeMap(QGlobal.MAPA_CUPO_RESOLUCION);
                material.setMapaEntorno(mapa.getTexturaEntorno());
                material.setTipoMapaEntorno(CubeMap.FORMATO_MAPA_CUBO);
                objeto.addComponent(mapa);
                mapa.aplicar(CubeMap.FORMATO_MAPA_CUBO, 0.8f, 1.45f);

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
