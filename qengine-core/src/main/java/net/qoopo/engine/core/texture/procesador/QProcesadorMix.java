/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.texture.procesador;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.texture.Texture;

/**
 * Mezcla 2 textures de acuerdo a la razon dada
 *
 * @author alberto
 */
public class QProcesadorMix extends Texture {

    private Texture texturaA;
    private Texture texturaB;
    private Texture textura;

    private float razon = 0;

    public QProcesadorMix(Texture texturaA, Texture texturaB) {
        this.texturaA = texturaA;
        this.texturaB = texturaB;
        this.textura = new Texture();
    }

    public Texture getTexturaA() {
        return texturaA;
    }

    public void setTexturaA(Texture texturaA) {
        this.texturaA = texturaA;
    }

    public Texture getTexturaB() {
        return texturaB;
    }

    public void setTexturaB(Texture texturaB) {
        this.texturaB = texturaB;
    }

    public float getRazon() {
        return razon;
    }

    public void setRazon(float razon) {
        this.razon = razon;
    }

    @Override
    public int getARGB(float x, float y) {
        return textura.getARGB(x, y);
    }

    @Override
    public QColor getQColor(float x, float y) {
        return QMath.mix(texturaA.getQColor(x, y), texturaB.getQColor(x, y), razon);
    }

    // @Override
    // public float getNormalX(float x, float y) {
    // return QMath.mix(texturaA.getNormalX(x, y), texturaB.getNormalX(x, y),
    // razon);
    // }

    // @Override
    // public float getNormalY(float x, float y) {
    // return QMath.mix(texturaA.getNormalY(x, y), texturaB.getNormalY(x, y),
    // razon);
    // }

    // @Override
    // public float getNormalZ(float x, float y) {
    // return QMath.mix(texturaA.getNormalZ(x, y), texturaB.getNormalZ(x, y),
    // razon);
    // }

    @Override
    public BufferedImage getImagen(Dimension size) {
        return textura.getImagen(size);
    }

    @Override
    public BufferedImage getImagen() {
        return textura.getImagen();
    }

    @Override
    public void destruir() {

        if (textura != null) {
            textura.destruir();
            textura = null;
        }

        if (texturaA != null) {
            texturaA.destruir();
            texturaA = null;
        }

        if (texturaB != null) {
            texturaB.destruir();
            texturaB = null;
        }
    }

    @Override
    public void setMuestrasU(float muestras) {
    }

    @Override
    public void setMuestrasV(float muestras) {
    }

    public Texture getTextura() {
        return textura;
    }

    public void setTextura(Texture textura) {
        this.textura = textura;
    }

}
