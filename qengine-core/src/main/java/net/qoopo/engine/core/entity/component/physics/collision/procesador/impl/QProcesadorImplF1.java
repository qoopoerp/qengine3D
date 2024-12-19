/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.procesador.impl;

import net.qoopo.engine.core.entity.component.physics.collision.procesador.QProcesadorColision;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;

/**
 * Detiene el movimiento de los objetos
 *
 * @author alberto
 */
public class QProcesadorImplF1 extends QProcesadorColision {

    @Override
    public void procesarColision(QObjetoRigido obj1, QObjetoRigido obj2) {

        // metodo 1 dejar de mover no hay rebote ni se aplican fuerzas
        obj1.detener();// le digo que ya no se mueva si tenia una fuerza aplicandolo como la peso
        obj2.detener();
        obj1.velocidadLinear.set(0, 0, 0);
        obj2.velocidadLinear.set(0, 0, 0);

    }
}
