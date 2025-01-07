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
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
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

        // agrego una esfera para cargar un mapa como entorno
        Entity entorno = new Entity("Entorno");
        Material matEntorno = new Material("Entorno");
        matEntorno.setFactorEmision(1.0f);
        matEntorno.setMapaColor(AssetManager.get().loadTexture("entornoDifuso",
                new File("assets/textures/hdri/autumn_field_puresky_1k.png")));
        entorno.addComponent(
                MaterialUtil.applyMaterial(NormalUtil.invertirNormales(new Sphere(50)), matEntorno));
        mundo.addEntity(entorno);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
