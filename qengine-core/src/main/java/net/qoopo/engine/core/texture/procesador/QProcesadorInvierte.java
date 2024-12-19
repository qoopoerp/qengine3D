/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.texture.procesador;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.QTextura;

/**
 * Este procesador es un proxy simple sin aplicar ningun proceso
 *
 * @author alberto
 */
public class QProcesadorInvierte extends QProcesadorTextura {

    private QTextura textura;

    public QProcesadorInvierte(QTextura textura) {
        this.textura = textura;
    }

    public QTextura getTextura() {
        return textura;
    }

    public void setTextura(QTextura textura) {
        this.textura = textura;
    }

    @Override
    public int get_ARGB(float x, float y) {
        // return textura.getARGB(x, y);
        return QColor.toQARGB(textura.getARGB(x, y)).invert().toARGB();
    }

    @Override
    public QColor get_QARGB(float x, float y) {
        return textura.getQColor(x, y).invert();
    }

    @Override
    public float getNormalX(float x, float y) {
        return 1.0f - textura.getNormalX(x, y);
    }

    @Override
    public float getNormalY(float x, float y) {
        return 1.0f - textura.getNormalY(x, y);
    }

    @Override
    public float getNormalZ(float x, float y) {
        return 1.0f - textura.getNormalZ(x, y);
    }

    @Override
    public BufferedImage getTexture(Dimension size) {
        return textura.getImagen(size);
    }

    @Override
    public void procesar() {
    }

    @Override
    public BufferedImage getTexture() {
        return textura.getImagen();
    }

    @Override
    public void destruir() {
        if (textura != null) {
            textura.destruir();
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
