/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.ia;

import net.qoopo.engine.core.engine.Engine;
import net.qoopo.engine.core.scene.Scene;

/**
 * Motor para ejecutar la inteligencia de los personajes del universo para
 * modificar sus propiedades
 *
 * @author alberto
 */
public abstract class IAEngine extends Engine {

    protected Scene universo;

    // protected long stopTime;
    public IAEngine(Scene universo) {
        this.universo = universo;
    }

    @Override
    public void start() {
        ejecutando = true;
    }

    @Override
    public void stop() {
        ejecutando = false;
        try {
            Thread.sleep(300);
        } catch (Exception e) {

        }
    }

    @Override
    public abstract long update();

}
