/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.filters.color;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.renderer.post.FilterTexture;
import net.qoopo.engine.core.texture.Texture;

/**
 * Realiza una modificación del color final de la imagen realzando el contraste
 *
 * @author alberto
 */
public class GammaFixFilter implements FilterTexture {

    private float gamma = 2.2f;
    private float gammaExp = 1.0f / gamma;

    public GammaFixFilter() {

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
                        // color.fixGamma();
                        color.set(color.a,
                                QMath.pow(color.r, gammaExp),
                                QMath.pow(color.g, gammaExp),
                                QMath.pow(color.b, gammaExp));
                        output.setQColor(x, y, color);
                    }
                }
            }
        } catch (Exception e) {

        }
        return output;
    }

}
