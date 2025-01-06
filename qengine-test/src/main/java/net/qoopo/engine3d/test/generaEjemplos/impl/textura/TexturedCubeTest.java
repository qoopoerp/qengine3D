/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.textura;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class TexturedCubeTest extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;
        Entity cubo = new Entity("cubo");
        QMaterialBas material = new QMaterialBas();
        material.setMapaColor(new QProcesadorSimple(
                AssetManager.get().loadTexture("difusa", new File("assets/textures/uv.png"))));

        // material.setMapaColor(new QProcesadorSimple(
        // AssetManager.get().loadTexture("difusa", new
        // File("assets/textures/rock.png"))));
        // material.setMapaNormal(new QProcesadorSimple(
        // AssetManager.get().loadTexture("normal", new
        // File("assets/textures/rock_normals.png"))));

        cubo.addComponent(MaterialUtil.applyMaterial(new Box(2), material));
        cubo.move(-5, 0, 0);
        mundo.addEntity(cubo);

        Entity box2 = new Entity("box2");
        box2.addComponent(MaterialUtil.applyMaterial(new Box(2, true), material));
        box2.move(5, 0, 0);
        mundo.addEntity(box2);
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
