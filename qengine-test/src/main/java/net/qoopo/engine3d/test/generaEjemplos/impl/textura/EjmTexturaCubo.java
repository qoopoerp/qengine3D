/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.textura;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCaja;
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
public class EjmTexturaCubo extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;
        Entity cubo = new Entity("cubo");
        QMaterialBas material = new QMaterialBas();
        material.setMapaColor(new QProcesadorSimple(
                AssetManager.get().loadTexture("difusa", new File("assets/textures/cube_map.png"))));
        cubo.addComponent(QMaterialUtil.aplicarMaterial(new QCaja(2), material));
        mundo.addEntity(cubo);
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
