/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.scene;

import net.qoopo.engine.core.entity.component.terrain.HeightMapTerrain;

/**
 * Representa un nivel de videojuego
 * @author alberto
 */
public abstract class QEscenario {

    protected HeightMapTerrain terreno;

    public abstract void cargar(Scene universo);

    public HeightMapTerrain getTerreno() {
        return terreno;
    }

    public void setTerreno(HeightMapTerrain terreno) {
        this.terreno = terreno;
    }

}
