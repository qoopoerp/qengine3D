/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.textura;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QEsfera;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjmTexturaEsfera extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;

        QMaterialBas mat1 = new QMaterialBas();
        mat1.setFactorNormal(0.5f);
        mat1.setMapaColor(new QProcesadorSimple(AssetManager.get().loadTexture("difusa", new File("assets/textures/solar_system/2k_earth_daymap.jpg"))));
        mat1.setMapaNormal(new QProcesadorSimple(AssetManager.get().loadTexture("normal", new File("assets/textures/solar_system/2k_earth_normal_map.png"))));

        Entity esfera = new Entity("Esfera");
        Mesh tierra = new QEsfera(2.5f, 36);
        tierra.nombre = "Tierra";
        QMaterialUtil.aplicarMaterial(tierra, mat1);
        esfera.addComponent(tierra);
        mundo.addEntity(esfera);
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
