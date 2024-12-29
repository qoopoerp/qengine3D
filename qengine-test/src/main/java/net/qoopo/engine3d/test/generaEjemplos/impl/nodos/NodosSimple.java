/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.nodos;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.material.node.MaterialNode;
import net.qoopo.engine.core.material.node.core.QNodoEnlace;
import net.qoopo.engine.core.material.node.core.output.MaterialOutputNode;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorIluminacion;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class NodosSimple extends MakeTestScene {

    public NodosSimple() {

    }

    public void make(Scene mundo) {
        this.scene = mundo;

        QNodoColorIluminacion ilum1 = new QNodoColorIluminacion(QColor.RED);
        MaterialOutputNode nodosalida = new MaterialOutputNode();
        QNodoEnlace enlace = new QNodoEnlace(ilum1.getSaColor(), nodosalida.getEnColor());
//
        MaterialNode material = new MaterialNode("Nodo_Esfera");
        material.setNodo(nodosalida);

        Entity esfera = new Entity("esfera");
        esfera.addComponent(MaterialUtil.applyMaterial(new Sphere(2), material));
        esfera.move(-5, 5, 0);
        mundo.addEntity(esfera);
//-----------------------

        QNodoColorIluminacion ilum2 = new QNodoColorIluminacion(QColor.BLUE);
        MaterialOutputNode nodosalida2 = new MaterialOutputNode();
        QNodoEnlace enlace2 = new QNodoEnlace(ilum2.getSaColor(), nodosalida2.getEnColor());

        MaterialNode materialCubo = new MaterialNode("Nodo_Cubo");
        materialCubo.setNodo(nodosalida2);

        Entity cubo = new Entity("Cubo");
        cubo.addComponent(MaterialUtil.applyMaterial(new Box(2), materialCubo));
        cubo.move(5, 5, 0);
        mundo.addEntity(cubo);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
