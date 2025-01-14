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
public class HightContrastFilter implements FilterTexture {

    private float factor = 0.3f;

    public HightContrastFilter() {

    }

    public HightContrastFilter(float factor) {
        this.factor = factor;
    }

    @Override
    public Texture apply(Texture... buffer) {
        QColor color;
        Texture output = new Texture(buffer[0].getWidth(), buffer[0].getHeight());
        try {
            for (Texture fBuffer : buffer) {
                for (int x = 0; x < fBuffer.getWidth(); x++) {
                    for (int y = 0; y < fBuffer.getHeight(); y++) {
                        color = fBuffer.getColor(x, y);
                        color.set(color.a,
                                (color.r - 0.5f) * (1.0f + factor) + 0.5f,
                                (color.g - 0.5f) * (1.0f + factor) + 0.5f,
                                (color.b - 0.5f) * (1.0f + factor) + 0.5f);
                        output.setQColor(x, y, color);
                    }
                }
            }
        } catch (Exception e) {

        }

        return output;
    }

}
