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
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
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
        QMaterialBas material = new QMaterialBas();
        QTextura albedo = AssetManager.get().loadTexture("difusa", new File("assets/"
                + "textures/pbr/metal/used-stainless-steel/albedo.png"));
        QTextura normal = AssetManager.get().loadTexture("normal", new File("assets/"
                + "textures/pbr/metal/used-stainless-steel/normal.png"));
        QTextura rugoso = AssetManager.get().loadTexture("rugoso", new File("assets/"
                + "textures/pbr/metal/used-stainless-steel/roughness.png"));
        QTextura metalico = AssetManager.get().loadTexture("metalico", new File("assets/"
                + "textures/pbr/metal/used-stainless-steel/metallic.png"));
        QTextura sombras = AssetManager.get().loadTexture("ao", new File("assets/"+
                "textures/pbr/metal/used-stainless-steel/ao.png"));

        material.setMapaColor(new QProcesadorSimple(albedo));
        material.setMapaNormal(new QProcesadorSimple(normal));
        material.setMapaRugosidad(new QProcesadorSimple(rugoso));
        material.setMapaMetalico(new QProcesadorSimple(metalico));
        material.setMapaSAO(new QProcesadorSimple(sombras));

        Entity objeto = new Entity("tetera");
        // objeto.agregarComponente(QMaterialUtil.aplicarMaterial(malla, material));
        objeto.addComponent(MaterialUtil.applyMaterial(new Teapot(), material));
        mundo.addEntity(objeto);
        // -------------------------------------
        CubeMap mapa = new CubeMap(QGlobal.MAPA_CUPO_RESOLUCION);
        material.setMapaEntorno(new QProcesadorSimple(mapa.getTexturaEntorno()));
        material.setTipoMapaEntorno(CubeMap.FORMATO_MAPA_CUBO);
        objeto.addComponent(mapa);
        mapa.aplicar(CubeMap.FORMATO_MAPA_CUBO, 0.8f, 1.45f);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
