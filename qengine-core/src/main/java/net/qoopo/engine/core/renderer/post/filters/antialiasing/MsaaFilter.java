/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.filters.antialiasing;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.post.FilterTexture;
import net.qoopo.engine.core.texture.Texture;

/**
 * Realiza un proceso de antialiasing
 *
 * @author alberto
 */
public class MsaaFilter implements FilterTexture {

    private int muestras = 4;

    private float scale = 1.0f;

    public MsaaFilter() {

    }

    public MsaaFilter(float scale) {
        this.scale = scale;
    }

    public MsaaFilter(int muestras) {
        this.muestras = muestras;
    }

    public MsaaFilter(float scale, int muestras) {
        this.muestras = muestras;
        this.scale = scale;
    }

    @Override
    public Texture apply(Texture... buffer) {
        QColor color;
        Texture output = new Texture((int) (buffer[0].getWidth() * scale), (int) (buffer[0].getHeight() * scale));
        try {
            for (int x = 0; x < buffer[0].getWidth(); x++) {
                for (int y = 0; y < buffer[0].getHeight(); y++) {
                    color = buffer[0].getColor(x, y);
                    for (int row = -1; row <= 1; ++row) {
                        for (int col = -1; col <= 1; ++col) {
                            try {
                                color.addLocal(buffer[0].getColor(x + col, y + row));
                            } catch (Exception e) {
                            }
                        }
                    }
                    color.scaleLocal(1.0f / 9.0f);
                    output.setQColor(
                            output.getWidth() * x / buffer[0].getWidth(),
                            output.getHeight() * y / buffer[0].getHeight(),
                            color);
                }
            }
        } catch (Exception e) {

        }
        return output;

    }

}
