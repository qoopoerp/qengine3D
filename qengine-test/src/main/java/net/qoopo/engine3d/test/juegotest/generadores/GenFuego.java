/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.juegotest.generadores;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.particles.fire.FireEmissor;
import net.qoopo.engine.core.entity.component.particles.humo.QEmisorHumo;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.util.QGlobal;

/**
 *
 * @author alberto
 */
public class GenFuego {

    public static Entity crearFogata1() {
        Entity fogata = new Entity();

        AABB ambito = new AABB(new Vertex(-0.15f, 0, -0.15f), new Vertex(.15f, 1.5f, .15f));//
        FireEmissor emisorFuego = new FireEmissor(ambito, 2000, 50, 5, QGlobal.gravedad.clone().multiply(-1f), false);
        fogata.addComponent(emisorFuego);
        fogata.getTransformacion().trasladar(0, 0.5f, 0);

        Entity enLuz = new Entity();
        enLuz.move(0, 0.25f, 0);
        enLuz.addComponent(new QPointLigth(0.8f, QColor.YELLOW, 10f, true, true));
        fogata.addChild(enLuz);

        Entity emisorHu = new Entity();
        emisorHu.move(0, 0.5f, 0);
        AABB ambitoHumo = new AABB(new Vertex(-0.15f, 0, -0.15f), new Vertex(.15f, 3.5f, .15f));//
        QEmisorHumo emisorHumo = new QEmisorHumo(ambitoHumo, 5000, 400, 4);
        emisorHu.addComponent(emisorHumo);
        fogata.addChild(emisorHu);

        return fogata;
    }

    public static Entity crearFogataConLuces() {
        Entity fogata = new Entity();

        AABB ambito = new AABB(new Vertex(-0.15f, 0, -0.15f), new Vertex(.15f, 1.5f, .15f));//
        FireEmissor emisorFuego = new FireEmissor(ambito, 2000, 50, 5, QGlobal.gravedad.clone().multiply(-1f), true);
        fogata.addComponent(emisorFuego);
        fogata.getTransformacion().trasladar(0, 0.5f, 0);

        Entity enLuz = new Entity();
        enLuz.move(0, 0.25f, 0);
        enLuz.addComponent(new QPointLigth(0.8f, QColor.YELLOW, 10f, true, true));
        fogata.addChild(enLuz);

        Entity emisorHu = new Entity();
        emisorHu.move(0, 0.5f, 0);
        AABB ambitoHumo = new AABB(new Vertex(-0.15f, 0, -0.15f), new Vertex(.15f, 3.5f, .15f));//
        QEmisorHumo emisorHumo = new QEmisorHumo(ambitoHumo, 5000, 400, 4);
        emisorHu.addComponent(emisorHumo);
        fogata.addChild(emisorHu);

        return fogata;
    }
}
