/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas;

import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;

/**
 * Define una cilindro contenedora del objeto
 *
 * @author alberto
 */
public class QColisionCilindroX extends CollisionShape {

    private float altura;
    private float radio;

    public QColisionCilindroX() {
    }

    public QColisionCilindroX(float altura, float radio) {
        this.altura = altura;
        this.radio = radio;
    }

    @Override
    public boolean verificarColision(CollisionShape otro) {

        boolean b = false;

//validar contra otro estera
        if (otro instanceof QColisionCilindroX) {
        }

        return b;

    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    @Override
    public void destruir() {

    }
}
