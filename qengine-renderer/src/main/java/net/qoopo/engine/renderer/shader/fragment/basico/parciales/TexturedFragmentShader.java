/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.basico.parciales;

import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;

/**
 * 3.
 * Este shader toma en cuenta la textura con sombreado phong. Sin iluminacion
 * del entorno
 *
 * @author alberto
 */
public class TexturedFragmentShader extends FragmentShader {

    private QColor colorDifuso;
    private float transparencia;

    public TexturedFragmentShader(RenderEngine render) {
        super(render);
    }

    @Override
    public QColor shadeFragment(Fragment fragment, int x, int y) {
        if (fragment == null) {
            return null;
        }
        if (!fragment.isDibujar()) {
            return null;
        }

        QColor color = new QColor();// color default, blanco

        // TOMA EL VALOR DE LA TRANSPARENCIA
        if (((Material) fragment.material).isTransparencia()) {
            // si tiene un mapa de transparencia
            if (((Material) fragment.material).getMapaTransparencia() != null) {
                // es una imagen en blanco y negro, toma cualquier canal de color
                transparencia = ((Material) fragment.material).getMapaTransparencia().getQColor(fragment.u, fragment.v).r;
            } else {
                // toma el valor de transparencia del material
                transparencia = ((Material) fragment.material).getTransAlfa();
            }
        } else {
            transparencia = 1;
        }

        if (((Material) fragment.material).getMapaColor() == null || !render.opciones.isMaterial()) {
            // si no hay textura usa el color del material
            color.set(fragment.material.getColor());
        } else {

            // si la textura no es proyectada (lo hace otro renderer) toma las coordenadas
            // ya calculadas
            if (!((Material) fragment.material).getMapaColor().isProyectada()) {
                colorDifuso = ((Material) fragment.material).getMapaColor().getQColor(fragment.u, fragment.v);
            } else {
                colorDifuso = ((Material) fragment.material).getMapaColor().getQColor(
                        (float) x / (float) render.getFrameBuffer().getAncho(),
                        -(float) y / (float) render.getFrameBuffer().getAlto());
            }
            color.set(colorDifuso);

            if (colorDifuso.a < 1 || (((Material) fragment.material).isTransparencia()
                    && ((Material) fragment.material).getColorTransparente() != null
                    && colorDifuso.toRGB() == ((Material) fragment.material).getColorTransparente().toRGB())) {
                return null;
            }
        }

        // ***********************************************************
        // ****** TRANSPARENCIA
        // ***********************************************************
        if (((Material) fragment.material).isTransparencia() && transparencia < 1) {
            QColor tmp = render.getFrameBuffer().getColor(x, y);// el color actual en el buffer para mezclarlo
            color.r = (1 - transparencia) * tmp.r + transparencia * color.r;
            color.g = (1 - transparencia) * tmp.g + transparencia * color.g;
            color.b = (1 - transparencia) * tmp.b + transparencia * color.b;
            tmp = null;
        }
        return color;
    }

}
