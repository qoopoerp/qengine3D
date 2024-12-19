/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.engines.ia;

import net.qoopo.engine.core.ia.IAEngine;
import net.qoopo.engine.core.scene.Scene;

/**
 * Este moror será el encargado de procesar los scripts de inteligencia y
 * comportamiento
 *
 * @author alberto
 */
public class DefaultIA extends IAEngine {

    public DefaultIA(Scene universo) {
        super(universo);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public long update() {
        tiempoPrevio = System.currentTimeMillis();
        return tiempoPrevio;
    }

}
