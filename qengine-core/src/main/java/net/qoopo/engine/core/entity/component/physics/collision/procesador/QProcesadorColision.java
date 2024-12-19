/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.procesador;

import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;

/**
 *
 * @author alberto
 */
public abstract class QProcesadorColision {

    public abstract void procesarColision(QObjetoRigido obj1, QObjetoRigido obj2);
}
