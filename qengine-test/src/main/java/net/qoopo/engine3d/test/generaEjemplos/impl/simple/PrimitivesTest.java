/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Cylinder;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Cone;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Torus;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.util.mesh.NormalUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class PrimitivesTest extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;

            Entity cube = new Entity("Doodster");

            Mesh cubGeometria = new Mesh();
            cubGeometria.addVertex(1, 1, 1);
            cubGeometria.addVertex(1, 1, -1);
            cubGeometria.addVertex(-1, 1, -1);
            cubGeometria.addVertex(-1, 1, 1);
            cubGeometria.addVertex(1, -1, 1);
            cubGeometria.addVertex(1, -1, -1);
            cubGeometria.addVertex(-1, -1, -1);
            cubGeometria.addVertex(-1, -1, 1);
            cubGeometria.addPoly(0, 1, 2, 3);
            cubGeometria.addPoly(0, 4, 5, 1);
            cubGeometria.addPoly(3, 2, 6, 7);
            cubGeometria.addPoly(0, 3, 7, 4);
            cubGeometria.addPoly(1, 5, 6, 2);
            cubGeometria.addPoly(4, 7, 6, 5);
            cubGeometria = NormalUtil.calcularNormales(cubGeometria);

            cube.addComponent(cubGeometria);

            mundo.addEntity(cube);

            Entity luzSpot = new Entity("luz spot");
            luzSpot.addComponent(new QSpotLigth(0.5f, QColor.YELLOW, 30, QVector3.of(-1, 0, 0),
                    (float) Math.toRadians(60), (float) Math.toRadians(50), true, false));
            luzSpot.move(8, 0, 0);
            mundo.addEntity(luzSpot);

            Entity luz1 = new Entity("luz1");
            luz1.addComponent(new QPointLigth(0.5f, new QColor(0.5f, 1, 0), 10, true, false));
            luz1.move(1.5f, -.8f, 1.5f);
            mundo.addEntity(luz1);

            Entity luz2 = new Entity("luz1");
            luz2.addComponent(new QPointLigth(0.5f, new QColor(1, 0, 0.5f), 10, true, false));
            luz2.move(-1.5f, -.8f, 1.5f);
            mundo.addEntity(luz2);

            Entity sol = new Entity("Sol");
            sol.addComponent(new QDirectionalLigth(.5f, new QColor(0.5f, 0.5f, 1), 10, true, false));
            mundo.addEntity(sol);
            Entity cuboBeto = new Entity("cubo");
            cuboBeto.addComponent(new Box(2));
            cuboBeto.getTransformacion().getTraslacion().x += 2;
            cuboBeto.getTransformacion().getTraslacion().y += 2;
            mundo.addEntity(cuboBeto);

            Entity cilindroBeto = new Entity("cilindro");
            cilindroBeto.addComponent(new Cylinder(2, 1));
            cilindroBeto.getTransformacion().getTraslacion().x += 5;
            cilindroBeto.getTransformacion().getTraslacion().z += 5;
            mundo.addEntity(cilindroBeto);

            Entity esfera = new Entity("esfera");
            esfera.addComponent(new Sphere(2));
            esfera.getTransformacion().getTraslacion().x += 5;
            esfera.getTransformacion().getTraslacion().z -= 5;
            mundo.addEntity(esfera);
            //
            Entity toro = new Entity("toro");
            toro.addComponent(new Torus(4, 2));
            toro.getTransformacion().getTraslacion().x -= 5;
            toro.getTransformacion().getTraslacion().z -= 5;
            mundo.addEntity(toro);

            Entity cono = new Entity("cono");
            cono.addComponent(new Cone(4, 2));
            cono.getTransformacion().getTraslacion().x -= 5;

            cono.getTransformacion().getTraslacion().z += 5;
            mundo.addEntity(cono);
        } catch (Exception ex) {
            Logger.getLogger(PrimitivesTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
