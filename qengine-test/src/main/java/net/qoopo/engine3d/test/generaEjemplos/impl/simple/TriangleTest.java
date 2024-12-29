/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.j3d.Material;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.mesh.NormalUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class TriangleTest extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;

            QMaterialBas material = new QMaterialBas();
            material.setColorBase(QColor.RED);

            Entity triangle = new Entity("Triangle");

            Mesh mesh = new Mesh();
            mesh.addVertex(1, -1, 0);
            mesh.addVertex(0, 1, 0);
            mesh.addVertex(-1, -1, 0);
            mesh.addPoly(0, 1, 2);
            mesh = NormalUtil.calcularNormales(mesh);

            MaterialUtil.applyMaterial(mesh, material);

            triangle.addComponent(mesh);

            mundo.addEntity(triangle);

        } catch (Exception ex) {
            Logger.getLogger(TriangleTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
