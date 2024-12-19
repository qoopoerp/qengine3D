/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.texture.procesador;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.QTextura;

/**
 * Este procesador carga un arreglo de textures que iran cambiando la textura a
 * mostrar con el pasar del tiempo
 *
 * @author alberto
 */
public class QProcesadorSecuencial extends QProcesadorTextura {

    private QTextura textura;// es la textura actual, se usa una aparte porq en las transiciones puede ser el
                             // resultado de 2 textures mezcladas
    private List<QTextura> textures;
    private long tiempo_ms;// tiempo de vida de cada textura en milisegundos
    private int contador = 0;
    private long t_anterior = -1;

    public QProcesadorSecuencial(long tiempo_ms) {
        this.tiempo_ms = tiempo_ms;
        textures = new ArrayList<>();
        t_anterior = -1;
    }

    public void agregarTextura(QTextura textura) {
        textures.add(textura);
    }

    public void eliminarTextura(QTextura textura) {
        textures.remove(textura);
    }

    public List<QTextura> getTexturas() {
        return textures;
    }

    public void setTexturas(List<QTextura> textures) {
        this.textures = textures;
    }

    public long getTiempo_ms() {
        return tiempo_ms;
    }

    public void setTiempo_ms(long tiempo_ms) {
        this.tiempo_ms = tiempo_ms;
    }

    public QTextura getTextura() {
        return textura;
    }

    public void setTextura(QTextura textura) {
        this.textura = textura;
    }

    @Override
    public void procesar() {
        if (this.t_anterior == -1) {
            t_anterior = System.currentTimeMillis();
        }
        long d = System.currentTimeMillis() - t_anterior;

        if (d > this.tiempo_ms) {
            contador++;
            t_anterior = System.currentTimeMillis();
        }

        if (contador > textures.size()) {
            contador = 0;
        }
        textura = textures.get(contador);
    }

    @Override
    public int get_ARGB(float x, float y) {
        procesar();
        return textura.getARGB(x, y);
    }

    @Override
    public QColor get_QARGB(float x, float y) {
        procesar();
        return textura.getQColor(x, y);
    }

    @Override
    public float getNormalX(float x, float y) {
        procesar();
        return textura.getNormalX(x, y);
    }

    @Override
    public float getNormalY(float x, float y) {
        procesar();
        return textura.getNormalY(x, y);
    }

    @Override
    public float getNormalZ(float x, float y) {
        procesar();
        return textura.getNormalZ(x, y);
    }

    @Override
    public BufferedImage getTexture(Dimension size) {
        procesar();
        return textura.getImagen(size);
    }

    @Override
    public BufferedImage getTexture() {
        return textura.getImagen();
    }

    @Override
    public void destruir() {
        for (QTextura tex : textures) {
            tex.destruir();
        }
        textures.clear();
        textures = null;

        if (textura != null) {
            textura.destruir();
            textura = null;
        }
    }

    @Override
    public void setMuestrasU(float muestras) {
    }

    @Override
    public void setMuestrasV(float muestras) {
    }

}
