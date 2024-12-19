/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.restricciones;

import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;

/**
 *
 * @author alberto
 */
public class QRestriccionBisagra extends QRestriccion {

    public QRestriccionBisagra() {
    }

    public QRestriccionBisagra(QObjetoRigido a) {
        this.a = a;
    }

    public QRestriccionBisagra(QObjetoRigido a, QObjetoRigido b) {
        this.a = a;
        this.b = b;
    }

}
