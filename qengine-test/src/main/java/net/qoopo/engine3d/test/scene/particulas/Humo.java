/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.particulas;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Plane;
import net.qoopo.engine.core.entity.component.particles.humo.QEmisorHumo;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 *
 *
 * @author alberto
 */
public class Humo extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;

        Entity emisorHumo = new Entity("Emisor");

        AABB ambitoHumo = new AABB(new Vertex(-0.15f, 0, -0.15f, 1), new Vertex(.15f, 3.5f, .15f, 1));//
        QEmisorHumo emisor = new QEmisorHumo(ambitoHumo, 5000, 400, 4);
        emisorHumo.addComponent(emisor);
        emisorHumo.move(0, 1.5f, 0);

        mundo.addEntity(emisorHumo);

        Entity plano = new Entity("plano");
        plano.addComponent(new Plane(10, 10));
        mundo.addEntity(plano);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
