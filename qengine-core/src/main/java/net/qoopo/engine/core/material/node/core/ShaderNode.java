/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.material.node.core;

import java.util.List;

import net.qoopo.engine.core.entity.component.mesh.primitive.QPixel;
import net.qoopo.engine.core.renderer.RenderEngine;

/**
 *
 * @author alberto
 */
public abstract class ShaderNode {

    protected List<QNodoPeriferico> entradas;
    protected List<QNodoPeriferico> salidas;

    public abstract void procesar(RenderEngine render, QPixel pixel);

    public List<QNodoPeriferico> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<QNodoPeriferico> entradas) {
        this.entradas = entradas;
    }

    public List<QNodoPeriferico> getSalidas() {
        return salidas;
    }

    public void setSalidas(List<QNodoPeriferico> salidas) {
        this.salidas = salidas;
    }

}
