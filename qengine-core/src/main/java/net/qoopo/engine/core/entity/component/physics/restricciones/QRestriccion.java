/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.restricciones;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;

/**
 *
 * @author alberto
 */
public class QRestriccion implements EntityComponent {

    @Getter
    @Setter
    private Entity entity;

    protected QObjetoRigido a;
    protected QObjetoRigido b;

    @Override
    public void destroy() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    public QObjetoRigido getA() {
        return a;
    }

    public void setA(QObjetoRigido a) {
        this.a = a;
    }

    public QObjetoRigido getB() {
        return b;
    }

    public void setB(QObjetoRigido b) {
        this.b = b;
    }

}
