/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.engine;

import java.text.DecimalFormat;

/**
 *
 * @author alberto
 */
public abstract class Engine {

    protected static final DecimalFormat DF = new DecimalFormat("0.00");

    // usada para medir subprocesos
    protected long ti;
    protected long ti2;
    protected boolean ejecutando = false;

    protected long beforeTime;

    public long getSubDelta() {
        ti2 = System.currentTimeMillis() - ti;
        ti = System.currentTimeMillis();
        return ti2;
    }

    public abstract void start();

    public abstract void stop();

    public abstract long update();

    public long getDelta() {
        return System.currentTimeMillis() - beforeTime;
    }

    public float getFPS() {
        return 1000.0f / getDelta();
    }

    public boolean isEjecutando() {
        return ejecutando;
    }

    public void setEjecutando(boolean ejecutando) {
        this.ejecutando = ejecutando;
    }

}
