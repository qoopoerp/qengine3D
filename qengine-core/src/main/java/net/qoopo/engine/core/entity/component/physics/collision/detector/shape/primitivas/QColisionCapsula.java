/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas;

import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.math.QVector3;

/**
 * Define una esfera contenedora del objeto
 *
 * @author alberto
 */
public class QColisionCapsula extends CollisionShape {

    private QVector3 centro;
    private float altura;
    private float radio;

    public QColisionCapsula() {
    }

    public QColisionCapsula(QVector3 centro, float altura, float radio) {
        this.centro = centro;
        this.altura = altura;
        this.radio = radio;
    }

    public QColisionCapsula(float altura, float radio) {
        this.altura = altura;
        this.radio = radio;
    }

    @Override
    public boolean verificarColision(CollisionShape otro) {

        boolean b = false;

//validar contra otro estera
        if (otro instanceof QColisionCapsula) {
        }

        return b;

    }

    public QVector3 getCentro() {
        return centro;
    }

    public void setCentro(QVector3 centro) {
        this.centro = centro;
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
