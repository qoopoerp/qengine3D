/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.material.node.core.perifericos;

import net.qoopo.engine.core.entity.component.mesh.primitive.QPixel;
import net.qoopo.engine.core.material.node.core.QNodoEnlace;
import net.qoopo.engine.core.material.node.core.QNodoPeriferico;
import net.qoopo.engine.core.renderer.RenderEngine;

/**
 *
 * @author alberto
 */
public class QPerFlotante extends QNodoPeriferico {

    private float valor;

    public QPerFlotante(float valor) {
        this.valor = valor;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    @Override
    public void procesarEnlaces(RenderEngine render, QPixel pixel) {
        super.procesarEnlaces(render, pixel);
        if (enlaces != null && !enlaces.isEmpty()) {
            for (QNodoEnlace enlace : enlaces) {
                // si yo soy un periferico de entrada, debería estar en la salida del enlace
                if (enlace.getSalida().equals(this)) {
                    // del nodo de entrada tomo su salida
                    if (enlace.getEntrada() instanceof QPerFlotante) {
                        valor = ((QPerFlotante) enlace.getEntrada()).getValor();
                    }
                }
            }
        }
    }

}
