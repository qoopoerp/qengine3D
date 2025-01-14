/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.water.texture;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.texture.Texture;

/**
 * Mezcla 2 textures de acuerdo a la razon dada ademas de un mapa de ruido
 *
 * @author alberto
 */
public class WaterTextureProcessor extends Texture {

    private Texture texturaA;
    private Texture texturaB;
    private Texture texturaDistorcion;
    private Texture textura;

    private float razon = 0;
    private float fuerzaOla = 0.02f;
    private float factorTiempo = 0.0f;

    public WaterTextureProcessor(Texture texturaA, Texture texturaB) {
        this.texturaA = texturaA;
        this.texturaB = texturaB;
        this.texturaDistorcion = new Texture();
        textura = new Texture();
        setProyectada(true);// la textura de salira ya esta proyectada
    }

    public WaterTextureProcessor(Texture texturaA, Texture texturaB, Texture distorcion) {
        this.texturaA = texturaA;
        this.texturaB = texturaB;
        this.texturaDistorcion = distorcion;
        textura = new Texture();
        setProyectada(true);// la textura de salira ya esta proyectada
    }

    @Override
    public void procesar() {

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
        QVector2 distor = texturaDistorcion.getQColor(x + factorTiempo, y).rg().multiply(0.1f);
        distor.setXY(x + distor.x, y + distor.y + factorTiempo);
        QVector2 distorTotal = texturaDistorcion.getQColor(distor.x, distor.y).rg().multiply(2).subtract(1)
                .multiply(fuerzaOla);
        x += distorTotal.x;
        y += distorTotal.y;
        x = QMath.clamp(x, 0.001f, 0.999f);
        y = QMath.clamp(y, -0.999f, -0.001f);
        return QMath.mix(texturaA.getQColor(x, y), texturaB.getQColor(x, y), razon);
    }

    // @Override
    // public float getNormalX(float x, float y) {
    // QVector2 distor = texturaDistorcion.getQColor(x + factorTiempo,
    // y).rg().multiply(0.1f);
    // distor.setXY(x + distor.x, y + distor.y + factorTiempo);
    // QVector2 distorTotal = texturaDistorcion.getQColor(distor.x,
    // distor.y).rg().multiply(2).subtract(1)
    // .multiply(fuerzaOla);
    // x += distorTotal.x;
    // y += distorTotal.y;
    // x = QMath.clamp(x, 0.001f, 0.999f);
    // y = QMath.clamp(y, -0.999f, -0.001f);
    // return QMath.mix(texturaA.getNormalX(x, y), texturaB.getNormalX(x, y),
    // razon);
    // }

    // @Override
    // public float getNormalY(float x, float y) {
    // QVector2 distor = texturaDistorcion.getQColor(x + factorTiempo,
    // y).rg().multiply(0.1f);
    // distor.setXY(x + distor.x, y + distor.y + factorTiempo);
    // QVector2 distorTotal = texturaDistorcion.getQColor(distor.x,
    // distor.y).rg().multiply(2).subtract(1)
    // .multiply(fuerzaOla);
    // x += distorTotal.x;
    // y += distorTotal.y;
    // x = QMath.clamp(x, 0.001f, 0.999f);
    // y = QMath.clamp(y, -0.999f, -0.001f);
    // return QMath.mix(texturaA.getNormalY(x, y), texturaB.getNormalY(x, y),
    // razon);
    // }

    // @Override
    // public float getNormalZ(float x, float y) {
    // QVector2 distor = texturaDistorcion.getQColor(x + factorTiempo,
    // y).rg().multiply(0.1f);
    // distor.setXY(x + distor.x, y + distor.y + factorTiempo);
    // QVector2 distorTotal = texturaDistorcion.getQColor(distor.x,
    // distor.y).rg().multiply(2).subtract(1)
    // .multiply(fuerzaOla);
    // x += distorTotal.x;
    // y += distorTotal.y;
    // x = QMath.clamp(x, 0.001f, 0.999f);
    // y = QMath.clamp(y, -0.999f, -0.001f);
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
    public void destroy() {
        if (textura != null) {
            textura.destroy();
            textura = null;
        }

        if (texturaA != null) {
            texturaA.destroy();
            texturaA = null;
        }

        if (texturaB != null) {
            texturaB.destroy();
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

    public Texture getTexturaDistorcion() {
        return texturaDistorcion;
    }

    public void setTexturaDistorcion(Texture texturaDistorcion) {
        this.texturaDistorcion = texturaDistorcion;
    }

    public float getFuerzaOla() {
        return fuerzaOla;
    }

    public void setFuerzaOla(float fuerzaOla) {
        this.fuerzaOla = fuerzaOla;
    }

    public float getFactorTiempo() {
        return factorTiempo;
    }

    public void setFactorTiempo(float factorTiempo) {
        this.factorTiempo = factorTiempo;
    }

}
