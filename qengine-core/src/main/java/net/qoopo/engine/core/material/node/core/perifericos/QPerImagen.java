/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.material.node.core.perifericos;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.material.node.core.QNodoPeriferico;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.texture.Texture;

/**
 *
 * @author alberto
 */
@Getter
@Setter
public class QPerImagen extends QNodoPeriferico {

    private Texture textura;

    public QPerImagen(Texture textura) {
        this.textura = textura;
    }

    @Override
    public void procesarEnlaces(RenderEngine render, Fragment pixel) {
        super.procesarEnlaces(render, pixel);

    }

}
