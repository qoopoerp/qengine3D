/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.ligths;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 *
 *
 * @author alberto
 */
public class SpotLigthsTest extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;
        int nLigthsPerLine = 2;
        float d = 10;
        int n = nLigthsPerLine - 1;
        d = d * 2 / n;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                Entity objeto = new Entity("Luz");
                objeto.addComponent(
                        new QSpotLigth(new Vector3(0, 0, 1), (float) Math.toRadians(45), (float) Math.toRadians(30)));
                objeto.move(i * d - (n * d / 2), j * d - (n * d / 2), 5);
                mundo.addEntity(objeto);
            }
        }
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
