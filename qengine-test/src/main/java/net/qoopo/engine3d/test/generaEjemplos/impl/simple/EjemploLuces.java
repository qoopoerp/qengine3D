/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 *
 * @author alberto
 */
public class EjemploLuces extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;
        float d = 7;
        int n = 1;
        d = d * 2 / n;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                Entity objeto = new Entity("Luz");
                objeto.addComponent(new QPointLigth(3.0f, QColor.WHITE, 100, false, false));
                // objeto.mover(i * d - ((n - 1) * d / 2), j * d - ((n - 1) * d / 2), 10);
                objeto.move(i * d - (n * d / 2), j * d - (n * d / 2), 10);
                mundo.addEntity(objeto);
            }
        }
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
