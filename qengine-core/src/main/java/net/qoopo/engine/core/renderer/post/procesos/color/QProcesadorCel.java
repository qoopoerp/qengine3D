/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.procesos.color;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.post.procesos.QPostProceso;
import net.qoopo.engine.core.texture.QTextura;

/**
 * Realiza una modificación del color final de la imagen convirtiendo en cel
 * shading o tipo acuarela
 *
 * @author alberto
 */
public class QProcesadorCel extends QPostProceso {

    // divido a 4 rangos del brillo
    private int niveles = 4;

    public QProcesadorCel(int ancho, int alto) {
        bufferSalida = new QTextura(ancho, alto);
    }

    @Override
    public void procesar(QTextura... buffer) {
        QColor color;
        float brillo;
        float f = 0;
        float fb = 1.0f / niveles;
        try {
            for (int x = 0; x < buffer[0].getAncho(); x++) {
                for (int y = 0; y < buffer[0].getAlto(); y++) {
                    color = buffer[0].getColor(x, y);
                    brillo = color.r * 0.2126f + color.g * 0.7152f + color.b * 0.0722f;
                    // f= (float) ((Math.floor(brillo/fb))*fb);
                    f = (float) ((Math.floor(brillo * niveles)) / niveles);
                    color = color.scale(f);
                    // if (brillo < 0.7f) {
                    // color = QColor.BLACK;
                    // }
                    bufferSalida.setQColor(x, y, color);
                    // bufferSalida.setQColorNormalizado((float) x / buffer[0].getAncho(), (float) y
                    // / buffer[0].getAlto(), color);
                }
            }
        } catch (Exception e) {

        }
        // bufferSalida.actualizarTextura();
    }

}
