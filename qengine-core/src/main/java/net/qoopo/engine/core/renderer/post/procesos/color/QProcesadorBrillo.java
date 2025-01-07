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
public class QProcesadorBrillo extends QPostProceso {

    private float umbral = 0.7f;

    public QProcesadorBrillo(int ancho, int alto) {
        bufferSalida = new Texture(ancho, alto);
    }

    public QProcesadorBrillo(int ancho, int alto, float umbral) {
        bufferSalida = new Texture(ancho, alto);
        this.umbral = umbral;
    }

    @Override
    public void procesar(Texture... buffer) {
        QColor color;
        float brillo;
        Texture textura = buffer[0];
        try {
            for (int x = 0; x < textura.getAncho(); x++) {
                for (int y = 0; y < textura.getAlto(); y++) {
                    color = textura.getColor(x, y);
                    brillo = color.r * 0.2126f + color.g * 0.7152f + color.b * 0.0722f;
                    color = color.scale(brillo);
                    if (brillo < umbral) {
                        color = QColor.BLACK;
                    }
                    bufferSalida.setQColor(bufferSalida.getAncho() * x / textura.getAncho(),
                            bufferSalida.getAlto() * y / textura.getAlto(), color);
                }
            }
        } catch (Exception e) {

        }
        // bufferSalida.actualizarTextura();
    }

    public float getUmbral() {
        return umbral;
    }

    public void setUmbral(float umbral) {
        this.umbral = umbral;
    }

}
