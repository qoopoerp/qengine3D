/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.texture.procesador;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.Texture;

/**
 * Ete procesador invierte el canal G.
 * 
 * Es usado para los mapas normales Dirext que tienen invertido el canal G
 * https://www.texturecan.com/post/3/DirectX-vs-OpenGL-Normal-Map/
 *
 * @author alberto
 */
public class NormalMapDirextTexture extends Texture {

    private Texture textura;

    public NormalMapDirextTexture(Texture textura) {
        this.textura = textura;
    }

    public Texture getTextura() {
        return textura;
    }

    public void setTextura(Texture textura) {
        this.textura = textura;
    }

    @Override
    public int getARGB(float x, float y) {
        QColor color = textura.getQColor(x, y);
        return new QColor(color.r, 1.0f - color.g, color.b).toARGB();

    }

    @Override
    public QColor getQColor(float x, float y) {
        QColor color = textura.getQColor(x, y);
        return new QColor(color.r, 1.0f - color.g, color.b);
    }

    @Override
    public BufferedImage getImagen(Dimension size) {
        return textura.getImagen(size);
    }

    @Override
    public void procesar() {
    }

    @Override
    public BufferedImage getImagen() {
        return textura.getImagen();
    }

    @Override
    public void destroy() {
        if (textura != null) {
            textura.destroy();
            textura = null;
        }
    }

    @Override
    public void setMuestrasU(float muestras) {
        textura.setMuestrasU(muestras);
    }

    @Override
    public void setMuestrasV(float muestras) {
        textura.setMuestrasV(muestras);
    }

}
