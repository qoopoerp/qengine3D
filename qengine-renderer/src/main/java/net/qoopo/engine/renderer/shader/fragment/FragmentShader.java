/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Fog;

/**
 * Las implementaciones de esta clase permiten el cálculo de color y luz de cada
 * pixel
 *
 * @author alberto
 */
/*
 * Implementaciones:
 * 0. QSimpleShader. Sombreado simple donde no calcula iluminacion. Solo usa los
 * colores sin textura
 * 1. QFlatShader. Sombreado Flat con caras con aristas visibles.
 * 2. QPhongShader. Sombreado de Phong con caras suaves sin aristas visibles.
 * 3. QTexturaShader. Permite el sombreado de Phong con textures
 * 4. QIluminadoShader. Permite sl sombreado de Phong con textures y cáculo de
 * iluminción con las luces de la escena.
 * 5. QShadowShader. Permite los mismo que las atenriores más el cálculo de
 * sombras.
 * 6. QFullShader. Permite todo lo anterior.
 */
@Getter
@Setter
public abstract class FragmentShader {

    protected RenderEngine render;

    protected static final float exposicion = 1.0f;

    public FragmentShader(RenderEngine render) {
        this.render = render;
    }

    /**
     *
     * @param fragment
     * @param x
     * @param y
     * @return
     */
    public abstract QColor shadeFragment(Fragment fragment, int x, int y);

    public static QColor calcularNeblina(QColor color, Fragment pixel, Fog neblina) {

        if (!neblina.isActive()) {
            return color;
        }
        // float distance = length(pos);
        float distance = pixel.location.z;
        float fogFactor = (float) (1.0
                / Math.exp((distance * neblina.getDensity()) * (distance * neblina.getDensity())));
        fogFactor = QMath.clamp(fogFactor, 0.0f, 1.0f);

        return new QColor(
                QMath.mix(neblina.getColour().r, color.r, fogFactor) / 2,
                QMath.mix(neblina.getColour().g, color.g, fogFactor) / 2,
                QMath.mix(neblina.getColour().b, color.b, fogFactor) / 2);
    }

}
