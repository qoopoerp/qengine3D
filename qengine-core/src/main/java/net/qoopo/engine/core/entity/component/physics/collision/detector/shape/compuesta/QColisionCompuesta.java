/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.detector.shape.compuesta;

import java.util.ArrayList;
import java.util.List;

import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.math.QVector3;

/**
 * Define una esfera contenedora del objeto
 *
 * @author alberto
 */
public class QColisionCompuesta extends CollisionShape {

    private List<QColisionCompuestaHija> hijos = new ArrayList<QColisionCompuestaHija>();

    public QColisionCompuesta() {
    }

    public void agregarHijo(QColisionCompuestaHija hijo) {
        hijos.add(hijo);
    }

    public void agregarHijo(CollisionShape forma, QVector3 ubicacion) {
        hijos.add(new QColisionCompuestaHija(forma, ubicacion));
    }

    @Override
    public boolean verificarColision(CollisionShape otro) {

        boolean b = false;

        // validar contra otro estera
        if (otro instanceof QColisionCompuesta) {
        }

        return b;

    }

    @Override
    public void destruir() {
    }

    public List<QColisionCompuestaHija> getHijos() {
        return hijos;
    }

    public void setHijos(List<QColisionCompuestaHija> hijos) {
        this.hijos = hijos;
    }

}
