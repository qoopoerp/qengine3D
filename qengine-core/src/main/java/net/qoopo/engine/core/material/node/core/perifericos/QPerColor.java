/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.material.node.core.perifericos;

import net.qoopo.engine.core.entity.component.mesh.primitive.QPixel;
import net.qoopo.engine.core.material.node.core.QNodoEnlace;
import net.qoopo.engine.core.material.node.core.QNodoPeriferico;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;

/**
 *
 * @author alberto
 */
public class QPerColor extends QNodoPeriferico {

    private QColor color;

    public QPerColor(QColor color) {
        this.color = color;
    }

    public QColor getColor() {
        return color;
    }

    public void setColor(QColor color) {
        this.color = color;
    }

    @Override
    public void procesarEnlaces(RenderEngine render, QPixel pixel) {
        super.procesarEnlaces(render, pixel);
        if (enlaces != null && !enlaces.isEmpty()) {
            for (QNodoEnlace enlace : enlaces) {
                // si yo soy un periferico de entrada, debería estar en la salida del enlace
                if (enlace.getSalida().equals(this)) {
                    // del nodo de entrada tomo su salida
                    if (enlace.getEntrada() instanceof QPerColor) {
                        color = ((QPerColor) enlace.getEntrada()).getColor();
                        return;
                    }
                }
            }
        }
    }

}
