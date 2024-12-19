/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.particles;

import net.qoopo.engine.core.entity.Entity;

/**
 * Paríicula a ser emitida por el Emisor de Partículas
 *
 * @author alberto
 */
public class Particle {

//    public QObjetoRigido objeto;
    public Entity objeto;

    private long tiempoCreacion;
    private float tiempoVida;

    public Particle() {

    }

    public void iniciarVida() {
        tiempoCreacion = System.currentTimeMillis();
    }

    public float getTiempoVida() {
        return tiempoVida;
    }

    public void setTiempoVida(float tiempoVida) {
        this.tiempoVida = tiempoVida;
    }

    public long getTiempoCreacion() {
        return tiempoCreacion;
    }

    public void setTiempoCreacion(long tiempoCreacion) {
        this.tiempoCreacion = tiempoCreacion;
    }

}
