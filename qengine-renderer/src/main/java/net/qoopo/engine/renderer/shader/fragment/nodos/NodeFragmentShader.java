/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.nodos;

import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.material.node.MaterialNode;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;

/**
 * Calcula el color e iluminación de cada pixel
 *
 * @author alberto
 */
public class NodeFragmentShader extends FragmentShader {

    public NodeFragmentShader(RenderEngine render) {
        super(render);
    }

    @Override
    public QColor shadeFragment(Fragment pixel, int x, int y) {
        if (pixel == null) {
            return null;
        }
        if (!pixel.isDibujar()) {
            return null;
        }
        QColor color = new QColor();// color default, blanco

        if (((MaterialNode) pixel.material).getNodo() != null) {
            MaterialNode material = ((MaterialNode) pixel.material);
            material.getNodo().procesar(render, pixel);
            color = material.getNodo().getSaColor().getColor();
            // for (QNodoPeriferico salida : material.getNodo().getSalidas()) {
            // if (salida instanceof QPerColor) {
            // color = ((QPerColor) salida).getColor();
            // }
            // }
        }
        return color;
    }

}
