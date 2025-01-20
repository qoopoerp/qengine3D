/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.basico.parciales;

import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;

/**
 * 2.
 * Shader plano sin textura
 * 
 * Util para el modelado donde no se debe tomar en cuenta la iluminacion ni
 * materiales
 *
 * @author alberto
 */
public class FlatFragmentShader extends FragmentShader {

    public FlatFragmentShader(RenderEngine render) {
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

        // (flat shadding)
        // le dice que use la normal de la cara y no la normal interpolada anteriormente
        if (fragment.primitiva instanceof Poly) {
            fragment.normal.set(((Poly) fragment.primitiva).getNormal());
        }

        color.set(fragment.material.getColor());
        computeLighting(fragment, color);

        return color;
    }

    protected void computeLighting(Fragment fragment, QColor color) {
        fragment.normal.normalize();

        // si tiene emision , lo ilumina con eso
        if (fragment.material instanceof Material && ((Material) fragment.material).getEmissionIntensity() > 0) {
            Material material = (Material) fragment.material;
            color.scale(material.getEmissionIntensity());
        } else {
            Vector3 tmpPixelPos = fragment.location.getVector3();
            tmpPixelPos.normalize();
            // Iluminacion default no toma en cuenta las luces del entorno
            color.scale(render.getScene().getAmbientColor().clone().add(-tmpPixelPos.dot(fragment.normal)));
            // Agrega color de la luz.
            // color.addLocal(iluminacion.getColorLuz());
        }

    }

}
