/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.mesh.NormalUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class SkyBoxTest extends MakeTestScene {

    public SkyBoxTest() {

    }

    public void make(Scene mundo) {
        this.scene = mundo;

        Texture texture = AssetManager.get().loadTexture("entornoDifuso",
                new File("assets/hdri/qwantani_dusk_2_2k.png"));

        // agrego una esfera para cargar un mapa como entorno
        Entity entorno = new Entity("Entorno");
        Material matEntorno = new Material("Entorno");
        matEntorno.setEmision(1.0f);
        matEntorno.setColorMap(texture);
        entorno.addComponent(MaterialUtil.applyMaterial(NormalUtil.invertirNormales(new Sphere(50)), matEntorno));
        mundo.addEntity(entorno);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
