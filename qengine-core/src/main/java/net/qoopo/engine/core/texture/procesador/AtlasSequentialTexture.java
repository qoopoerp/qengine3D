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
 * Este procesador permite cargar una textura atlas que tiene varias textures en
 * su interiory permite ir cambiando cada una de ellas depues de un tiempo
 * determinado (para hacer animaciones por ejemplo la textura de una fogata)
 *
 * @author alberto
 */
public class AtlasSequentialTexture extends Texture {
    private Texture textura;
    private float filas = 1;
    private float columnas = 1;

    // la fila y columna de la textura que se ira a tomar
    private float fila = 0;
    private float col = 0;

    // variables para el control del tiempo
    private long tiempo_ms;// tiempo de vida de cada textura en milisegundos
    private long t_anterior = -1;
    float anchoCelda;
    float altoCelda;

    public AtlasSequentialTexture(Texture textura, int filas, int columnas, long tiempo_ms) {
        this.textura = textura;
        this.filas = filas;
        this.columnas = columnas;
        this.tiempo_ms = tiempo_ms;
        t_anterior = System.currentTimeMillis();
        anchoCelda = 1.0f / (float) columnas;
        altoCelda = 1.0f / (float) filas;
    }

    public float getFilas() {
        return filas;
    }

    public void setFilas(float filas) {
        this.filas = filas;
    }

    public float getColumnas() {
        return columnas;
    }

    public void setColumnas(float columnas) {
        this.columnas = columnas;
    }

    public float getFila() {
        return fila;
    }

    public void setFila(float fila) {
        this.fila = fila;
    }

    public float getCol() {
        return col;
    }

    public void setCol(float col) {
        this.col = col;
    }

    @Override
    public void procesar() {
        //
        if (this.t_anterior == -1) {
            t_anterior = System.currentTimeMillis();
        }
        long d = System.currentTimeMillis() - t_anterior;

        // System.out.println("tiempo ms=" + tiempo_ms);
        // System.out.println("d=" + d);
        if (d >= this.tiempo_ms) {
            col++;
            // if (col > 1) {
            // System.out.println("col=" + col);
            // }
            // System.out.println("col nueva=" + col);
            if (col >= columnas) {
                col = 0;
                fila++;
                // System.out.println("fila nuev=" + fila);
                if (fila >= filas) {
                    fila = 0;
                }
            }
            t_anterior = System.currentTimeMillis();
        }
    }

    public Texture getTextura() {
        procesar();
        return textura;
    }

    public void setTextura(Texture textura) {
        this.textura = textura;
    }

    @Override
    public int getARGB(float x, float y) {
        procesar();
        x = col * anchoCelda + (x / columnas);
        y = fila * altoCelda + (y / filas);
        return textura.getARGB(x, y);
    }

    @Override
    public QColor getQColor(float x, float y) {
        procesar();
        x = col * anchoCelda + (x / columnas);
        y = fila * altoCelda + (y / filas);
        return textura.getQColor(x, y);
    }

    // @Override
    // public float getNormalX(float x, float y) {
    // procesar();
    // x = col * anchoCelda + (x / columnas);
    // y = fila * altoCelda + (y / filas);
    // return textura.getNormalX(x, y);
    // }

    // @Override
    // public float getNormalY(float x, float y) {
    // procesar();
    // x = col * anchoCelda + (x / columnas);
    // y = fila * altoCelda + (y / filas);
    // return textura.getNormalY(x, y);
    // }

    // @Override
    // public float getNormalZ(float x, float y) {
    // procesar();
    // x = col * anchoCelda + (x / columnas);
    // y = fila * altoCelda + (y / filas);
    // return textura.getNormalZ(x, y);
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
    }

    @Override
    public void setMuestrasU(float muestras) {
    }

    @Override
    public void setMuestrasV(float muestras) {
    }

}
