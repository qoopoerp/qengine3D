/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.pixelshader.basico.parciales;

import net.qoopo.engine.core.entity.component.ligth.QIluminacion;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPixel;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.renderer.shader.pixelshader.QShader;

/**
 * Shader con sombrado de phong. sin Textura
 *
 * @author alberto
 */
public class QPhongShaderBAS extends QShader {

    public QPhongShaderBAS(RenderEngine render) {
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

        // No procesa textura , usa el color del material
        color.set(((QMaterialBas) pixel.material).getColorBase());
        calcularIluminacion(iluminacion, pixel);
        color.scale(iluminacion.getColorAmbiente());
        color.addLocal(iluminacion.getColorLuz());
        return color;
    }

    protected void calcularIluminacion(QIluminacion illumination, QPixel pixel) {
        pixel.normal.normalize();
        // toma en cuenta la luz ambiente
        iluminacion.setColorAmbiente(render.getEscena().getAmbientColor().clone());
        iluminacion.setColorLuz(QColor.BLACK.clone());
        tmpPixelPos.set(pixel.ubicacion.getVector3());
        tmpPixelPos.normalize();
        // Iluminacion default no toma en cuenta las luces del entorno
        iluminacion.getColorAmbiente().add(-tmpPixelPos.dot(pixel.normal));
    }

}
