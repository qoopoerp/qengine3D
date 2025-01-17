/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.particulas;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Plane;
import net.qoopo.engine.core.entity.component.particles.fire.QEmisorVolcan;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 *
 *
 * @author alberto
 */
public class Volcan extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;

        Entity volcan = new Entity("fogata");
        AABB ambitoVolcan = new AABB(new Vertex(-9, 0, -1), new Vertex(-8, 2, 1));//
        QEmisorVolcan emisorVolcan = new QEmisorVolcan(ambitoVolcan, 300, 5000, 25, mundo.gravity.clone().multiply(-1));
        volcan.addComponent(emisorVolcan);
        volcan.getTransform().move(0, 0.5f, 0);

//        fogata.agregarComponente(new QLuz(QLuz.TYPE_POINT, 0.8f, 255, 255, 0, true));
//        fogata.agregarComponente(new QLuz(QLuz.TYPE_POINT, 0.8f, 255, 255, 255, true));
//        fogata.agregarComponente(new QLuz(QLuz.TYPE_POINT, 0.8f, 255, 0, 0, true));
        mundo.addEntity(volcan);

        Entity plano = new Entity("plano");
        plano.addComponent(new Plane(10, 10));

        plano.rotate((float) Math.toRadians(90), 0, 0);
        mundo.addEntity(plano);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
