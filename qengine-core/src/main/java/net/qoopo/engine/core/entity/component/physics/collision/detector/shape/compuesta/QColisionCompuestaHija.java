/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.detector.shape.compuesta;

import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.math.QVector3;

/**
 * Define una esfera contenedora del objeto
 *
 * @author alberto
 */
public class QColisionCompuestaHija extends CollisionShape {

    private CollisionShape forma;
    private QVector3 ubicacion;

    public QColisionCompuestaHija(CollisionShape formaColision, QVector3 ubicacion) {
        this.forma = formaColision;
        this.ubicacion = ubicacion;
    }

    public CollisionShape getForma() {
        return forma;
    }

    public void setForma(CollisionShape forma) {
        this.forma = forma;
    }

    public QVector3 getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(QVector3 ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public boolean verificarColision(CollisionShape otro) {

        boolean b = false;

        // validar contra otro estera
        if (otro instanceof QColisionCompuestaHija) {
        }

        return b;

    }

    @Override
    public void destruir() {
    }

}