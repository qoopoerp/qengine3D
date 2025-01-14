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
public class BrigthFilter implements FilterTexture {

    private float umbral = 0.7f;

    public BrigthFilter() {

    }

    public BrigthFilter(float umbral) {

        this.umbral = umbral;
    }

    @Override
    public Texture apply(Texture... buffer) {
        QColor color;
        float brillo;
        Texture textura = buffer[0];
        Texture output = new Texture(textura.getWidth(), textura.getHeight());

        try {
            for (int x = 0; x < textura.getWidth(); x++) {
                for (int y = 0; y < textura.getHeight(); y++) {
                    color = textura.getColor(x, y);
                    brillo = color.r * 0.2126f + color.g * 0.7152f + color.b * 0.0722f;
                    color = color.scale(brillo);
                    if (brillo < umbral) {
                        color = QColor.BLACK;
                    }
                    output.setQColor(output.getWidth() * x / textura.getWidth(),
                            output.getHeight() * y / textura.getHeight(), color);
                }
            }
        } catch (Exception e) {

        }
        return output;

    }

    public float getUmbral() {
        return umbral;
    }

    public void setUmbral(float umbral) {
        this.umbral = umbral;
    }

}
