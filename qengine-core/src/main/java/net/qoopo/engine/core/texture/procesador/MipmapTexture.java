/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.texture.procesador;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.post.FilterTexture;
import net.qoopo.engine.core.renderer.post.filters.antialiasing.MsaaFilter;
import net.qoopo.engine.core.renderer.post.filters.blur.BlurFilter;
import net.qoopo.engine.core.texture.Texture;

/**
 * Este procesador permite generar y almacenar texturas de diferente nivel de
 * detalle y resoluciones
 *
 * @author alberto
 */
public class MipmapTexture extends Texture {

    private static Logger logger = Logger.getLogger("mip-map");

    public static final int TIPO_BLUR = 1;
    public static final int TIPO_ANTIALIASING = 2;
    private Texture texturaAtlas;
    private Texture[] textureArray; // permite meyor velocidad
    private Texture currentTexture;
    private int maxLevel;
    private int level;
    private int type = TIPO_ANTIALIASING; // 1 antialiasing, 2 blur

    public MipmapTexture(Texture textura, int niveles, int tipo) {
        this.currentTexture = new Texture();
        this.maxLevel = niveles;
        this.type = tipo;
        textureArray = new Texture[niveles];
        if (textura != null && textura.getWidth() != 0 && textura.getHeight() != 0) {
            generarMipMap(textura, niveles, tipo);
            setLevel(1);
        }
    }

    public MipmapTexture(Texture textura, int niveles) {
        this.currentTexture = new Texture();
        this.maxLevel = niveles;
        textureArray = new Texture[niveles];
        if (textura != null && textura.getWidth() != 0 && textura.getHeight() != 0) {
            generarMipMap(textura, niveles, type);
            setLevel(1);
        }
    }

    public Texture generarMipMap(Texture textura) {
        return generarMipMap(textura, maxLevel, type);
    }

    public void loadTexture(BufferedImage img) {
        Texture texture = new Texture();
        texture.loadTexture(img);

        generarMipMap(texture);
    }

    /**
     * Genera un mapa de niveles
     *
     * El mimamp generado es una textura con el alto original pero el ancho
     * varia dependiendo el numero de copias
     *
     * @param textura
     * @param maxLevel
     * @param tipo     el tipo generado ( QProcesadorMipMap.TIPO_BLUR,
     *                 QProcesadorMipMap.TIPO_ANTIALIASING)
     * @return La textura con los mipmap generados
     */
    public Texture generarMipMap(Texture textura, int maxLevel, int tipo) {
        logger.info("[+] Generando mipmap");
        // calculo el ancho necesario
        textureArray = new Texture[maxLevel];
        this.maxLevel = maxLevel;
        this.type = tipo;
        int totalAncho = textura.getWidth();
        for (int iNivel = 2; iNivel <= maxLevel; iNivel++) {
            totalAncho += textura.getWidth() / iNivel;
        }
        Texture mipmap = new Texture(totalAncho, textura.getHeight());

        int xGlobal = 0;
        for (int iNivel = 1; iNivel <= maxLevel; iNivel++) {
            Texture tmp = new Texture();

            if (iNivel > 1) {
                FilterTexture filter = null;
                // textura.getWidth() / iNivel, textura.getHeight() / iNivel
                switch (tipo) {
                    case TIPO_BLUR:
                        filter = new BlurFilter(iNivel * 2);
                        break;
                    default:
                    case TIPO_ANTIALIASING:
                        filter = new MsaaFilter();
                        break;
                }
                tmp.loadTexture(filter.apply(textura).getImagen());
            } else {
                tmp = textura;// si es el primer nivel guarda la textura sin procesar
            }

            // leo la textura y la guardo en la textura de destino
            for (int y = 0; y < tmp.getHeight(); y++) {
                for (int x = 0; x < tmp.getWidth(); x++) {
                    mipmap.setQColor(x + xGlobal, y, (tmp.getColor(x, y)));
                }
            }
            textureArray[iNivel - 1] = tmp;
            xGlobal += tmp.getWidth();
        }
        texturaAtlas = mipmap;

        procesar();
        logger.info("[+] Mipmap generado");
        return mipmap;
    }

    public Texture getTexturaAtlas() {
        return texturaAtlas;
    }

    public void setTexturaAtlas(Texture texturaAtlas) {
        this.texturaAtlas = texturaAtlas;
    }

    public Texture[] getTextureArray() {
        return textureArray;
    }

    public void setTextureArray(Texture[] texturarreglo) {
        this.textureArray = texturarreglo;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int niveles) {
        this.maxLevel = niveles;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int nivel) {
        if (nivel != this.level) {
            this.level = nivel;
            procesar();
        }
    }

    @Override
    public void procesar() {
        level = Math.min(level, maxLevel);
        level = Math.max(1, level);
        currentTexture = textureArray[level - 1];
    }

    @Override
    public int getARGB(float x, float y) {
        return currentTexture.getARGB(x, y);
    }

    @Override
    public QColor getQColor(float x, float y) {
        return currentTexture.getQColor(x, y);
    }

    @Override
    public BufferedImage getImagen(Dimension size) {
        return currentTexture.getImagen(size);
    }

    @Override
    public BufferedImage getImagen() {
        return currentTexture.getImagen();
    }

    @Override
    public void destroy() {
        if (texturaAtlas != null) {
            texturaAtlas.destroy();
            texturaAtlas = null;
        }
    }

    @Override
    public void setMuestrasU(float muestras) {
    }

    @Override
    public void setMuestrasV(float muestras) {
    }

    public int getWidth() {
        return currentTexture.getWidth();
    }

    public int getHeight() {
        return currentTexture.getHeight();
    }

}
