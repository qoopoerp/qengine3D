/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.particulas;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlano;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;
import net.qoopo.engine3d.test.juegotest.generadores.GenFuego;

/**
 *
 *
 * @author alberto
 */
public class Fuego extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;

//        Entity fogata = GenFuego.crearFogataConLuces();
        Entity fogata = GenFuego.crearFogata1();
        mundo.addEntity(fogata);

        Entity plano = new Entity("plano");
        plano.addComponent(new QPlano(10, 10));
        mundo.addEntity(plano);
//        Entity plano2 = new Entity("plano2");
//        plano2.agregarComponente(new QPlanoBillboard(10, 10));
//        mundo.addEntity(plano2);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
