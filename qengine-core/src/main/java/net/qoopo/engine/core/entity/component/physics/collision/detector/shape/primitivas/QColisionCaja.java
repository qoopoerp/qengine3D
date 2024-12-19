/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas;

import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;

/**
 * Define una esfera contenedora del objeto
 *
 * @author alberto
 */
public class QColisionCaja extends CollisionShape {

    private float ancho;
    private float alto;
    private float largo;

    public QColisionCaja(float ancho, float alto, float largo) {
        this.ancho = ancho;
        this.alto = alto;
        this.largo = largo;
    }

    public float getAncho() {
        return ancho;
    }

    public void setAncho(float ancho) {
        this.ancho = ancho;
    }

    public float getAlto() {
        return alto;
    }

    public void setAlto(float alto) {
        this.alto = alto;
    }

    public float getLargo() {
        return largo;
    }

    public void setLargo(float largo) {
        this.largo = largo;
    }

    @Override
    public void destruir() {

    }

    @Override
    public boolean verificarColision(CollisionShape otro) {
        return false;
    }
}
