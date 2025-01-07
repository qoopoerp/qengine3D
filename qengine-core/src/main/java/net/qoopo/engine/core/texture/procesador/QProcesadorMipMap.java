/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.texture.procesador;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.post.procesos.QPostProceso;
import net.qoopo.engine.core.renderer.post.procesos.antialiasing.QMSAA;
import net.qoopo.engine.core.renderer.post.procesos.blur.QProcesadorBlur;
import net.qoopo.engine.core.texture.Texture;

/**
 * Este procesador permite cargar una textura atlas (una textura que tiene
 * muchas textures en ella)
 *
 * @author alberto
 */
public class QProcesadorMipMap extends Texture {

    public static final int TIPO_BLUR = 1;
    public static final int TIPO_ANTIALIASING = 2;
    private Texture texturaAtlas;
    private Texture[] texturarreglo; // permite meyor velocidad
    private Texture textura;
    private int niveles;
    private int nivel;
    private int tipo = TIPO_ANTIALIASING; // 1 antialiasing, 2 blur
    private int anchoOriginal = 0;

    public QProcesadorMipMap(Texture textura, int niveles, int tipo) {
        this.textura = new Texture();
        this.niveles = niveles;
        this.tipo = tipo;
        if (textura.getAncho() != 0 && textura.getAlto() != 0) {
            generarMipMap(textura, niveles, tipo);
            setNivel(1);
        }
    }

    public QProcesadorMipMap(Texture textura, int niveles) {
        this.textura = new Texture();
        this.niveles = niveles;
        if (textura.getAncho() != 0 && textura.getAlto() != 0) {
            generarMipMap(textura, niveles, tipo);
            setNivel(1);
        }

    }

    public Texture generarMipMap(Texture textura) {
        return generarMipMap(textura, niveles, tipo);
    }

    /**
     * Genera un mapa de niveles
     *
     * El mimamp generado es una textura con el alto original pero el ancho
     * varia dependiendo el numero de copias
     *
     * @param textura
     * @param niveles
     * @param tipo    el tipo generado ( QProcesadorMipMap.TIPO_BLUR,
     *                QProcesadorMipMap.TIPO_ANTIALIASING)
     * @return La textura con los mipmap generados
     */
    public Texture generarMipMap(Texture textura, int niveles, int tipo) {
        // calculo el ancho necesario
        texturarreglo = new Texture[niveles];
        this.niveles = niveles;
        this.tipo = tipo;
        this.anchoOriginal = textura.getAncho();
        int totalAncho = textura.getAncho();
        for (int iNivel = 2; iNivel <= niveles; iNivel++) {
            totalAncho += textura.getAncho() / iNivel;
        }
        Texture mipmap = new Texture(totalAncho, textura.getAlto());

        int xGlobal = 0;
        for (int iNivel = 1; iNivel <= niveles; iNivel++) {
            Texture tmp = new Texture();

            if (iNivel > 1) {
                QPostProceso proc = null;
                switch (tipo) {
                    case TIPO_BLUR:
                        proc = new QProcesadorBlur(textura.getAncho() / iNivel, textura.getAlto() / iNivel, iNivel * 2);
                        // proc = new QProcesadorBlur(1.0f / (float) nivel, nivel * 2);
                        break;
                    default:
                    case TIPO_ANTIALIASING:
                        proc = new QMSAA(textura.getAncho() / iNivel, textura.getAlto() / iNivel, 1);
                        break;
                }
                proc.procesar(textura);
                tmp.loadTexture(proc.getBufferSalida().getImagen());
            } else {
                tmp = textura;// si es el primer nivel guarda la textura sin procesar
            }

            // leo la textura y la guardo en la textura de destino
            for (int y = 0; y < tmp.getAlto(); y++) {
                for (int x = 0; x < tmp.getAncho(); x++) {
                    mipmap.setQColor(x + xGlobal, y, (tmp.getColor(x, y)));
                }
            }
            texturarreglo[iNivel - 1] = tmp;
            xGlobal += tmp.getAncho();
        }
        texturaAtlas = mipmap;
        procesar();
        return mipmap;
    }

    public Texture getTexturaAtlas() {
        return texturaAtlas;
    }

    public void setTexturaAtlas(Texture texturaAtlas) {
        this.texturaAtlas = texturaAtlas;
    }

    public Texture[] getTexturarreglo() {
        return texturarreglo;
    }

    public void setTexturarreglo(Texture[] texturarreglo) {
        this.texturarreglo = texturarreglo;
    }

    public int getNiveles() {
        return niveles;
    }

    public void setNiveles(int niveles) {
        this.niveles = niveles;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        if (nivel != this.nivel) {
            this.nivel = nivel;
            procesar();
        }
    }

    @Override
    public void procesar() {
        nivel = Math.min(nivel, niveles);
        nivel = Math.max(1, nivel);
        // int anchoPrevio = 0;
        // for (int niv = 1; niv < nivel; niv++) {
        // anchoPrevio += anchoOriginal / niv;
        // }
        //
        // int anchoCelda = Math.max(anchoOriginal / nivel, 1);
        // int altoCelda = Math.max(texturaAtlas.getAlto() / nivel, 1);
        // int x = anchoPrevio;
        // int y = 0;
        // textura.loadTexture(texturaAtlas.getImagen().getSubimage(x, y, anchoCelda,
        // altoCelda));
        textura = texturarreglo[nivel - 1];
    }

    public Texture getTextura() {
        return textura;
    }

    public void setTextura(Texture textura) {
        this.textura = textura;
    }

    @Override
    public int getARGB(float x, float y) {
        return textura.getARGB(x, y);
    }

    @Override
    public QColor getQColor(float x, float y) {
        return textura.getQColor(x, y);
    }

    // @Override
    // public float getNormalX(float x, float y) {
    // return textura.getNormalX(x, y);
    // }

    // @Override
    // public float getNormalY(float x, float y) {
    // return textura.getNormalY(x, y);
    // }

    // @Override
    // public float getNormalZ(float x, float y) {
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
    public void destruir() {
        if (texturaAtlas != null) {
            texturaAtlas.destruir();
            texturaAtlas = null;
        }
    }

    @Override
    public void setMuestrasU(float muestras) {
    }

    @Override
    public void setMuestrasV(float muestras) {
    }

}
