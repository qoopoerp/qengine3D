/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.material.node.core.perifericos;

import net.qoopo.engine.core.entity.component.mesh.primitive.QPixel;
import net.qoopo.engine.core.material.node.core.QNodoPeriferico;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.texture.procesador.QProcesadorTextura;

/**
 *
 * @author alberto
 */
public class QPerImagen extends QNodoPeriferico {

    private QProcesadorTextura textura;

    public QPerImagen(QProcesadorTextura textura) {
        this.textura = textura;

    }

    public QProcesadorTextura getTextura() {
        return textura;
    }

    public void setTextura(QProcesadorTextura textura) {
        this.textura = textura;
    }

    @Override
    public void procesarEnlaces(RenderEngine render, QPixel pixel) {
        super.procesarEnlaces(render, pixel);

    }

}
