/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.basico.parciales;

import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;

/**
 * 1.
 * 
 * Shader simple, sin calculo de iluminacion ni textura
 * 
 * Util en el modo wireframe
 * 
 *
 * @author alberto
 */
public class OnlyColorFragmentShader extends FragmentShader {

    public OnlyColorFragmentShader(RenderEngine render) {
        super(render);
    }

    @Override
    public QColor shadeFragment(Fragment pixel, int x, int y) {
        if (pixel == null) {
            return null;
        }
        if (!pixel.isDraw()) {
            return null;
        }
        // solo usa el color del material
        return pixel.material.getColor();
    }

}
