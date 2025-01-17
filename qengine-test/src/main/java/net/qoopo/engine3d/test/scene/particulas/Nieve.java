/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.particulas;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.particles.nieve.QEmisorNieve;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 *
 *
 * @author alberto
 */
public class Nieve extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;
        // mundo.agregarLuz(new QLuz(0, 2, 128, 255, 0, 1.5f, -.8f, 1.5f, true));
        // mundo.agregarLuz(new QLuz(QLuz.TYPE_POINT, 2.5f, 255, 255, 255, 0, 0.5f, 5f,
        // true));

        // ** Crea un emisor de nieve, se define el ambito
        Entity emisor = new Entity("Emisor");

        AABB ambito = new AABB(new Vertex(-10, 0, -10), new Vertex(10, 10, 10));//
        QEmisorNieve emisorNieve = new QEmisorNieve(ambito, 300, 1000, 5, QVector3.unitario_y.clone().multiply(-1));
        emisor.addComponent(emisorNieve);
        mundo.addEntity(emisor);

        // Entity plano = new Entity("plano");
        // plano.addComponent(new Plane(10, 10));
        // mundo.addEntity(plano);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
