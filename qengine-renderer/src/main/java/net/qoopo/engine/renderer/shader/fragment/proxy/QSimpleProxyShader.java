/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.proxy;

import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.material.node.MaterialNode;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;
import net.qoopo.engine.renderer.shader.fragment.basico.parciales.QSimpleShaderBAS;
import net.qoopo.engine.renderer.shader.fragment.nodos.NodeFragmentShader;

/**
 * Calcula el color e iluminación de cada pixel, dependiendo si es con material
 * básico o pbr redirecciona los shader correspondientes
 *
 * @author alberto
 */
public class QSimpleProxyShader extends FragmentShader {

    private FragmentShader basico;
    private NodeFragmentShader nodos;

    public QSimpleProxyShader(RenderEngine render) {
        super(render);
        basico = new QSimpleShaderBAS(render);
        nodos = new NodeFragmentShader(render);
    }

    @Override
    public QColor shadeFragment(Fragment pixel, int x, int y) {
        try {
            if (pixel.material instanceof QMaterialBas) {
                return basico.shadeFragment(pixel, x, y);
            } else if (pixel.material instanceof MaterialNode) {
                return nodos.shadeFragment(pixel, x, y);
            }
        } catch (Exception e) {

        }
        return null;
    }

}