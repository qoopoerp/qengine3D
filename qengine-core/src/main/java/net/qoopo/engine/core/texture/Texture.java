/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.texture;

import static net.qoopo.engine.core.math.QMath.rotateNumber;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.Vector3;

/**
 *
 * @author alberto
 */
@Getter
@Setter
public class Texture implements Serializable {

    /**
     * Puntero a un objeto de otra plataforma que lo necesite, como opengl o java3d
     */
    transient public Object transientObject;

    private QImage imagen;

    //
    // esta bandera la uso para identificar si es un reflejo y debo trabajar con su
    // proyeccion
    // en lugar del mapeo normal
    // el calculo lo hace el render
    // solo tenemos la bandera por logistica
    protected boolean proyectada = false;

    private int width = 0;
    private int height = 0;

    // sera usado para obtener coordenadas diferentes de las solicitadas por el
    // renderer
    // y estos valores seran alterados por modificadores como
    // el procesador de Agua que alterara estos valores para simular movimiento del
    // agua
    // en una superficie plana
    private float offsetX = 0;
    private float offsetY = 0;

    // para poder reflejar la textura
    private int signoX = 1;
    private int signoY = 1;
    // indica cuantas muestras tendra la textura. Si se requiere que la textura se
    // repita 10 veces en el eje x se coloca muestraU=10.0f
    private float muestrasU = 1.0f;
    private float muestrasV = 1.0f;

    /*
     * 
     * Coordenadas UV, son las coordenadas para mapear la textura sobre un poligono,
     * van de 0 a 1
     * 
     * (0,1) ___________________________ (1,1)
     * \ \
     * \ \
     * \ \
     * \ \
     * \ \
     * \ \
     * \ \
     * (0,0) ___________________________ (1,0)
     */
    public Texture() {
    }

    /**
     * Prepara una textura con las dimensiones especificadas y con el tipo de
     * imagen ARGB
     *
     * @param ancho
     * @param alto
     */
    public Texture(int ancho, int alto) {
        loadTexture(new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB));
    }

    /**
     *
     * @param img
     *
     */
    public Texture(BufferedImage img) {
        loadTexture(img);
    }

    public void loadTexture(QImage img) {
        width = img.getAncho();
        height = img.getAlto();
        this.imagen = img;
    }

    public void loadTexture(BufferedImage img) {
        try {
            width = img.getWidth();
            height = img.getHeight();
            if (imagen == null) {
                imagen = new QImage(img);
            } else {
                imagen.setImage(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTexture(Texture texture) {
        try {
            width = texture.getWidth();
            height = texture.getHeight();
            if (imagen == null) {
                imagen = new QImage(texture.getImagen());
            } else {
                imagen.setImage(texture.getImagen());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Coordenadas normalizadas
     *
     * @param x
     * @param y
     * @return
     */
    public int getARGB(float x, float y) {
        try {
            x *= signoX;
            y *= signoY;
            x += offsetX;
            y += offsetY;
            x *= muestrasU;
            y *= muestrasV;
            return imagen.getPixel(rotateNumber((int) (x * width), width),
                    rotateNumber((int) ((1 - y) * height), height));
        } catch (Exception e) {
            return (0 << 24 + 0 << 16 + 0 << 8 + 0);
        }
    }

    /**
     * Coordenadas normalizadas, solo se toma X, y
     * 
     * @param vector
     * @return
     */
    public QColor getColor(Vector3 vector) {
        return getQColor(vector.x, vector.y);
    }

    /**
     * Coordenadas normalizadas
     *
     * @param x
     * @param y
     * @return
     */
    public QColor getQColor(float x, float y) {
        try {
            x *= signoX;
            y *= signoY;
            x += offsetX;
            y += offsetY;
            x *= muestrasU;
            y *= muestrasV;
            return imagen.getPixelQARGB(rotateNumber((int) (x * width), width),
                    rotateNumber((int) ((1 - y) * height), height));
        } catch (Exception e) {
            return new QColor((0 << 24) / 255.0f, (0 << 16) / 255.0f, (0 << 8) / 255.0f, 0);
        }
    }

    /**
     * Coordenadas normalizadas
     *
     * @param x
     * @param y
     * @param argb
     */
    public void setARGB(float x, float y, int argb) {
        try {
            x *= signoX;
            y *= signoY;
            x += offsetX;
            y += offsetY;
            x *= muestrasU;
            y *= muestrasV;
            imagen.setPixel(rotateNumber((int) (x * width), width), rotateNumber((int) ((1 - y) * height), height),
                    argb);
        } catch (Exception e) {

        }
    }

    /**
     * Coordenadas normalizadas
     *
     * @param x
     * @param y
     * @param color
     */
    public void setQColor(float x, float y, QColor color) {
        try {
            x *= signoX;
            y *= signoY;
            x += offsetX;
            y += offsetY;
            x *= muestrasU;
            y *= muestrasV;
            imagen.setPixel(rotateNumber((int) (x * width), width), rotateNumber((int) ((1 - y) * height), height),
                    color);
        } catch (Exception e) {

        }
    }

    /**
     * Coordenadas reales
     *
     * @param x
     * @param y
     * @param color
     */
    public void setQColor(int x, int y, QColor color) {
        try {
            imagen.setPixel(x, y, color);
        } catch (Exception e) {

        }
    }

    /**
     * Coordenadas reales
     *
     * @param x
     * @param y
     * @return
     */
    public QColor getColor(int x, int y) {
        try {
            return imagen.getPixelQARGB(x, y);
        } catch (Exception e) {
            return new QColor((0 << 24) / 255.0f, (0 << 16) / 255.0f, (0 << 8) / 255.0f, 0);
        }
    }

    public float getA(float x, float y) {
        return getQColor(x, y).a;
    }

    public float getR(float x, float y) {
        return getQColor(x, y).r;
    }

    public float getG(float x, float y) {
        return getQColor(x, y).g;
    }

    public float getB(float x, float y) {
        return getQColor(x, y).b;
    }

    public void setQImagen(QImage imagen) {
        this.imagen = imagen;
    }

    public QImage getQImagen() {
        return imagen;
    }

    public BufferedImage getImagen() {
        if (imagen == null) {
            return null;
        }
        return imagen.getBi();
    }

    public BufferedImage getImagen(Dimension size) {
        if (imagen == null) {
            return null;
        }
        if (size == null) {
            return getImagen();// devuelve la imagen original y sin escalar
        }
        BufferedImage newImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        QColor pix = null;
        for (int y = 0; y < newImage.getHeight(); y++) {
            for (int x = 0; x < newImage.getWidth(); x++) {
                float newX = (float) x / size.width;
                float newY = (float) y / size.height;
                // pix = getColor(newX, newY);
                pix = getQColor(newX, 1.0f - newY);// la imagen estaba saliendo invertida por el orden inverso de las
                                                   // coordenadas uv en el eje y
                newImage.setRGB(x, y, pix.toARGB());
            }
        }
        return newImage;
    }

    public void destroy() {
        imagen.destruir();
        imagen = null;
    }

    public void fill(QColor color) {
        imagen.fill(color);
    }

    public void procesar() {

    }

}
