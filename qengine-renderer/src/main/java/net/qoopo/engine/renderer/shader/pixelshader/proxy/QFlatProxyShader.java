/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.pixelshader.proxy;

import net.qoopo.engine.core.entity.component.mesh.primitive.QPixel;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.material.node.MaterialNode;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.renderer.shader.pixelshader.QShader;
import net.qoopo.engine.renderer.shader.pixelshader.basico.parciales.QFlatShaderBAS;
import net.qoopo.engine.renderer.shader.pixelshader.nodos.QNodeShader;

/**
 * Calcula el color e iluminación de cada pixel, dependiendo si es con material
 * básico o pbr redirecciona los shader correspondientes
 *
 * @author alberto
 */
public class QFlatProxyShader extends QShader {

    private QShader basico;
    private QNodeShader nodos;

    public QFlatProxyShader(RenderEngine render) {
        super(render);
        basico = new QFlatShaderBAS(render);
        nodos = new QNodeShader(render);
    }

    @Override
    public QColor colorearPixel(QPixel pixel, int x, int y) {
        try {
            if (pixel.material instanceof QMaterialBas) {
                return basico.colorearPixel(pixel, x, y);
            } else if (pixel.material instanceof MaterialNode) {
                return nodos.colorearPixel(pixel, x, y);
            }
        } catch (Exception e) {

        }
        return null;
    }

}
