/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.procesos.color;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.post.procesos.QPostProceso;
import net.qoopo.engine.core.texture.Texture;

/**
 * Realiza una modificación del color final de la imagen realzando el contraste
 *
 * @author alberto
 */
public class QProcesadorContraste extends QPostProceso {

    private float factor = 0.3f;

    public QProcesadorContraste(int ancho, int alto, float factor) {
        this.factor = factor;
        bufferSalida = new Texture(ancho, alto);
    }

    @Override
    public void procesar(Texture... buffer) {
        QColor color;
        try {
            for (Texture fBuffer : buffer) {
                for (int x = 0; x < fBuffer.getAncho(); x++) {
                    for (int y = 0; y < fBuffer.getAlto(); y++) {
                        color = fBuffer.getColor(x, y);
                        color.set(color.a,
                                (color.r - 0.5f) * (1.0f + factor) + 0.5f,
                                (color.g - 0.5f) * (1.0f + factor) + 0.5f,
                                (color.b - 0.5f) * (1.0f + factor) + 0.5f);
                        bufferSalida.setQColor(x, y, color);
                    }
                }
            }
        } catch (Exception e) {

        }
        // bufferSalida.actualizarTextura();
    }

}
