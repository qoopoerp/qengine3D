/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.basico.parciales;

import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;

/**
 * 3.
 * Shader con sombrado de phong. sin Textura
 * 
 * Util para el modelado donde no se debe tomar en cuenta la iluminacion ni
 * materiales
 *
 * @author alberto
 */
public class PhongFramentShader extends FragmentShader {

    public PhongFramentShader(RenderEngine render) {
        super(render);
    }

    @Override
    public QColor shadeFragment(Fragment fragment, int x, int y) {
        if (fragment == null) {
            return null;
        }
        if (!fragment.isDraw()) {
            return null;
        }
        QColor color = new QColor();// color default, blanco
        // No procesa textura , usa el color del material
        color.set(fragment.material.getColor());
        computeLighting(fragment, color);
        return color;
    }

    protected void computeLighting(Fragment fragment, QColor color) {
        fragment.normal.normalize();

        // si tiene emision , lo ilumina con eso
        if (fragment.material instanceof Material && ((Material) fragment.material).getEmision() > 0) {
            Material material = (Material) fragment.material;
            color.scale(material.getEmision());
        } else {
            QVector3 tmpPixelPos = fragment.ubicacion.getVector3();
            tmpPixelPos.normalize();
            // Iluminacion default no toma en cuenta las luces del entorno
            color.scale(render.getScene().getAmbientColor().clone().add(-tmpPixelPos.dot(fragment.normal)));
            // Agrega color de la luz.
            // color.addLocal(iluminacion.getColorLuz());
        }

    }

}
