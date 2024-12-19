/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.physic;

import net.qoopo.engine.core.engine.Engine;
import net.qoopo.engine.core.scene.Scene;

/**
 * Motor de simulacion física. Se encarga de aplicar las fuerzas a los objetos
 * del universo para modificar sus propiedades
 *
 * @author alberto
 */
public abstract class PhysicsEngine extends Engine {

    protected Scene universo;

    public static final int FISICA_INTERNO = 1;
    public static final int FISICA_JBULLET = 2;

    public PhysicsEngine(Scene universo) {
        this.universo = universo;
    }

    @Override
    public void start() {
        ejecutando = true;
    }

    @Override
    public void stop() {
        ejecutando = false;
//        try {
//            Thread.sleep(10);
//        } catch (Exception e) {
//
//        }
    }

    @Override
    public abstract long update();

}
