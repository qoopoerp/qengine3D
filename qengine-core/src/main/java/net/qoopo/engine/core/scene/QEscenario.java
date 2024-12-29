/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.scene;

import net.qoopo.engine.core.entity.component.terrain.Terrain;

/**
 * Representa un nivel de videojuego
 * 
 * @author alberto
 */
public abstract class QEscenario {

    protected Terrain terreno;

    public abstract void cargar(Scene universo);

    public Terrain getTerreno() {
        return terreno;
    }

    public void setTerreno(Terrain terreno) {
        this.terreno = terreno;
    }

}
