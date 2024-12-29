/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas;

import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.terrain.Terrain;

/**
 * Define una esfera contenedora del objeto
 *
 * @author alberto
 */
public class QColisionTerreno extends CollisionShape {

    private Terrain terreno;

    public QColisionTerreno(Terrain terreno) {
        this.terreno = terreno;
    }

    @Override
    public boolean verificarColision(CollisionShape otro) {
        boolean b = false;
        // validar contra otro estera
        if (otro instanceof QColisionTerreno) {
        }

        return b;
    }

    @Override
    public void destruir() {
    }

    public Terrain getTerreno() {
        return terreno;
    }

    public void setTerreno(Terrain terreno) {
        this.terreno = terreno;
    }

}
