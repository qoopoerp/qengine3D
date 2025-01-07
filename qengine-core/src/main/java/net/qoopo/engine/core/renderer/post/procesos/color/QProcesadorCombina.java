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
public class QProcesadorCombina extends QPostProceso {

    public QProcesadorCombina(int ancho, int alto) {
        bufferSalida = new Texture(ancho, alto);
    }

    @Override
    public void procesar(Texture... buffer) {
        QColor color;
        QColor color2;
        QColor color3;
        Texture textura1 = buffer[0];
        Texture textura2 = buffer[1];
        try {
            for (int x = 0; x < textura1.getAncho(); x++) {
                for (int y = 0; y < textura1.getAlto(); y++) {
                    color = textura1.getColor(x, y);
                    color2 = textura2.getColor(textura2.getAncho() * x / textura1.getAncho(),
                            textura2.getAlto() * y / textura1.getAlto());
                    color3 = color.add(color2);
                    bufferSalida.setQColor(bufferSalida.getAncho() * x / textura1.getAncho(),
                            bufferSalida.getAlto() * y / textura1.getAlto(), color3);
                }
            }
        } catch (Exception e) {

        }
        // bufferSalida.actualizarTextura();
    }

}
