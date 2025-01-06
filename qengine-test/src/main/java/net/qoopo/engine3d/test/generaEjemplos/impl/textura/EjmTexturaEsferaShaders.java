/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.textura;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.renderer.shader.fragment.FragmentShaderComponent;
import net.qoopo.engine.renderer.shader.fragment.basico.parciales.QFlatShaderBAS;
import net.qoopo.engine.renderer.shader.fragment.basico.parciales.QSimpleShaderBAS;
import net.qoopo.engine.renderer.shader.fragment.basico.parciales.QTextureShaderBAS;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjmTexturaEsferaShaders extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;

        QMaterialBas mat1 = new QMaterialBas();
        mat1.setFactorNormal(0.5f);
        mat1.setMapaColor(new QProcesadorSimple(AssetManager.get().loadTexture("difusa",
                new File("assets/textures/solar_system/2k_earth_daymap.jpg"))));

        Entity esfera = new Entity("Esfera1");
        esfera.move(5, 5, 5);
        esfera.addComponent(MaterialUtil.applyMaterial(new Sphere(2.5f, 36), mat1));
        mundo.addEntity(esfera);

        Entity esfera2 = new Entity("Esfera2");
        esfera2.move(-5, 5, 5);
        esfera2.addComponent(MaterialUtil.applyMaterial(new Sphere(2.5f, 36), mat1));
        esfera2.addComponent(new FragmentShaderComponent(new QFlatShaderBAS(null)));
        mundo.addEntity(esfera2);

        Entity esfera3 = new Entity("Esfera3");
        esfera3.move(-5, -5, 5);
        esfera3.addComponent(MaterialUtil.applyMaterial(new Sphere(2.5f, 36), mat1));
        esfera3.addComponent(new FragmentShaderComponent(new QSimpleShaderBAS(null)));
        mundo.addEntity(esfera3);

        Entity esfera4 = new Entity("Esfera4");
        esfera4.move(5, -5, 5);
        esfera4.addComponent(MaterialUtil.applyMaterial(new Sphere(2.5f, 36), mat1));
        esfera4.addComponent(new FragmentShaderComponent(new QTextureShaderBAS(null)));
        mundo.addEntity(esfera4);
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
