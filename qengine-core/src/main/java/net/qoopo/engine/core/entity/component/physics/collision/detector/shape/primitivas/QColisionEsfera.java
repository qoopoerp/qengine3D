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
public class QColisionEsfera extends CollisionShape {

    private float radio;

    public QColisionEsfera() {
        radio = 1.0f;
    }

    public QColisionEsfera(float radio) {
        this.radio = radio;
    }

    @Override
    public boolean verificarColision(CollisionShape otro) {
        boolean b = false;
//validar contra otro estera
        if (otro instanceof QColisionEsfera) {
        }

        return b;

    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    @Override
    public void destroy() {
    }
}
