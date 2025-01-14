/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.filters.color;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.post.FilterTexture;
import net.qoopo.engine.core.texture.Texture;

/**
 * Realiza una modificación del color final de la imagen realzando el contraste
 *
 * @author alberto
 */
public class MixFilter implements FilterTexture {

    public MixFilter() {

    }

    @Override
    public Texture apply(Texture... buffer) {
        QColor color;
        QColor color2;
        QColor color3;
        Texture textura1 = buffer[0];
        Texture textura2 = buffer[1];
        Texture output = new Texture(textura1.getWidth(), textura1.getHeight());
        try {
            for (int x = 0; x < textura1.getWidth(); x++) {
                for (int y = 0; y < textura1.getHeight(); y++) {
                    color = textura1.getColor(x, y);
                    color2 = textura2.getColor(textura2.getWidth() * x / textura1.getWidth(),
                            textura2.getHeight() * y / textura1.getHeight());
                    color3 = color.add(color2);
                    output.setQColor(output.getWidth() * x / textura1.getWidth(),
                            output.getHeight() * y / textura1.getHeight(), color3);
                }
            }
        } catch (Exception e) {

        }
        return output;

    }

}
