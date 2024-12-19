/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.audio;

import net.qoopo.engine.core.engine.Engine;
import net.qoopo.engine.core.scene.Scene;

/**
 * Motor de audio. Se encarga de gestionar los componentes de audio. Actualizar
 * las coordenadas de acuerdo a las coordenadas de la entity
 *
 *
 * @author alberto
 */
public abstract class AudioEngine extends Engine {

    protected Scene scene;

    public AudioEngine(Scene escena) {
        this.scene = escena;
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
