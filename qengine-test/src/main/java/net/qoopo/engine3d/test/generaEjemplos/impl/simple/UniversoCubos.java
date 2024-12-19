/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import java.io.File;
import java.util.Random;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
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
 *
 * @author alberto
 */
public class UniversoCubos extends MakeTestScene {

    private QMaterialBas material = null;

    private void cargarMaterial() {
        material = null;
        try {
            material = new QMaterialBas();
            material.setMapaColor(new QProcesadorSimple(
                    AssetManager.get().loadTexture("difusa", new File("assets/textures/rock.png"))));
            material.setMapaNormal(new QProcesadorSimple(
                    AssetManager.get().loadTexture("normal", new File("assets/textures/rock_normals.png"))));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void make(Scene mundo) {
        this.scene = mundo;
        cargarMaterial();
        Random rnd = new Random();
        float tamUniverso = 100;

        Mesh geometria = new QCaja(1);
        QMaterialUtil.aplicarMaterial(geometria, material);

        for (int i = 0; i < 500; i++) {
            Entity cubo = new Entity("Cubo [" + i + "]");
            cubo.move(rnd.nextFloat() * tamUniverso * 2 - tamUniverso, rnd.nextFloat() * tamUniverso * 2 - tamUniverso,
                    rnd.nextFloat() * tamUniverso * 2 - tamUniverso);
            cubo.addComponent(geometria);
            mundo.addEntity(cubo);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
