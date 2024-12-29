/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.basico.parciales;

import net.qoopo.engine.core.entity.component.ligth.QIluminacion;
import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;

/**
 * Shader plano sin textura
 *
 * @author alberto
 */
public class QFlatShaderBAS extends FragmentShader {

    public QFlatShaderBAS(RenderEngine render) {
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
        QIluminacion iluminacion = new QIluminacion();

        // (flat shadding)
        // le dice que use la normal de la cara y no la normal interpolada anteriormente
        if (pixel.primitiva instanceof Poly) {
            pixel.normal.set(((Poly) pixel.primitiva).getNormal());
        }

        // No procesa textura , usa el color del material
        color.set(((QMaterialBas) pixel.material).getColorBase());
        calcularIluminacion(iluminacion, pixel);
        // Iluminacion ambiente
        color.scale(iluminacion.getColorAmbiente());
        // Agrega color de la luz.
        color.addLocal(iluminacion.getColorLuz());
        return color;
    }

    // @Override
    protected void calcularIluminacion(QIluminacion iluminacion, Fragment fragment) {
        fragment.normal.normalize();

        QVector3 tmpPixelPos = QVector3.empty();

        // toma en cuenta la luz ambiente
        iluminacion.setColorAmbiente(render.getEscena().getAmbientColor().clone());
        iluminacion.setColorLuz(QColor.BLACK.clone());
        tmpPixelPos.set(fragment.ubicacion.getVector3());
        tmpPixelPos.normalize();
        // Iluminacion default no toma en cuenta las luces del entorno
        iluminacion.getColorAmbiente().add(-tmpPixelPos.dot(fragment.normal));

    }

}
