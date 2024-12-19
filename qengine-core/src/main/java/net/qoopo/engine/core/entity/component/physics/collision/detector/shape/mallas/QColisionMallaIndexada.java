/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;

/**
 * Define una esfera contenedora del objeto
 *
 * @author alberto
 */
public class QColisionMallaIndexada extends CollisionShape {

    private Mesh malla;

    public QColisionMallaIndexada(Mesh geometria) {
        this.malla = geometria;
    }

    @Override
    public boolean verificarColision(CollisionShape otro) {

        boolean b = false;

        // validar contra otro estera
        if (otro instanceof QColisionMallaIndexada) {
        }

        return b;

    }

    @Override
    public void destruir() {
    }

    public Mesh getMalla() {
        return malla;
    }

    public void setMalla(Mesh malla) {
        this.malla = malla;
    }

}
