/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.fisica;

import net.qoopo.engine.core.entity.Entity;

/**
 *
 * @author alberto
 */
public class EntidadTestFisica extends Entity {

    public EntidadTestFisica() {
        super();
        construir();
    }

    private void construir() {

    }

    @Override
    public void comprobarMovimiento() {
        // if (transformacion.getLocation().y < 0.5f) {
        // transformacion.getLocation().y = 0.5f;
        // }
    }

}