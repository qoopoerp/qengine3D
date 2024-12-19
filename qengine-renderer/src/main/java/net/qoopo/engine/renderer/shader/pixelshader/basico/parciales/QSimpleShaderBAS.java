/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.pixelshader.basico.parciales;

import net.qoopo.engine.core.entity.component.mesh.primitive.QPixel;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.renderer.shader.pixelshader.QShader;

/**
 * Shader simple, sin calculo de iluminacion ni textura
 *
 * @author alberto
 */
public class QSimpleShaderBAS extends QShader {

    public QSimpleShaderBAS(RenderEngine render) {
        super(render);
    }

    @Override
    public QColor colorearPixel(QPixel pixel, int x, int y) {
        if (pixel == null) {
            return null;
        }
        if (!pixel.isDibujar()) {
            return null;
        }

        // solo usa el color del material
        color = ((QMaterialBas) pixel.material).getColorBase();
        return color;
    }

}
