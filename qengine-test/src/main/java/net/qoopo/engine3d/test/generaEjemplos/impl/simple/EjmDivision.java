/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QTeapot;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjmDivision extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;

        QTextura text = AssetManager.get().loadTexture("asd1",
                new File("assets/textures/basicas/planetas/tierra/text3/earthmap1k.jpg"));

        Entity obj1 = new Entity("esfera1");
        obj1.addComponent(QMaterialUtil.aplicarMaterial(new QTeapot(), new QMaterialBas(text)));
        obj1.move(QVector3.of(0, 0, 0));
        mundo.addEntity(obj1);

        Entity obj2 = new Entity("esfera2");
        Mesh g2 = new QTeapot();
        g2.dividir().smooth();
        obj2.addComponent(QMaterialUtil.aplicarMaterial(g2, new QMaterialBas(text)));
        obj2.move(QVector3.of(-3, 0, 0));
        mundo.addEntity(obj2);

        Entity obj3 = new Entity("esfera3");
        Mesh g3 = new QTeapot();
        g3.dividirCatmullClark().smooth();
        obj3.addComponent(QMaterialUtil.aplicarMaterial(g3, new QMaterialBas(text)));
        obj3.move(QVector3.of(3, 0, 0));
        mundo.addEntity(obj3);

        // Entity obj4 = new Entity("esfera4");
        // QGeometria esfera4 = new QTeapot();
        // esfera4.dividirCatmullClark().eliminarVerticesDuplicados().suavizar();
        // obj4.agregarComponente(QMaterialUtil.aplicarMaterial(esfera4, new
        // QMaterialBas(text)));
        // obj4.mover(QVector3.of(6, 0, 0));
        // mundo.addEntity(obj4);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
