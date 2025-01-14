/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.buffer;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.Texture;

/**
 * Framebuffer Utiliza una textura como buffer de color
 *
 * @author alberto
 */

@Getter
@Setter
public class FrameBuffer {

    // este buffer adicional tiene información de material, entity (transformación y
    // demas) por cada pixel
    // no tiene información de color
    protected final Fragment[][] pixelBuffer;
    // este buffer es el de color que se llena despues de procesar los pixeles
    private Texture bufferColor;
    // buffer de profundidad
    protected float[][] zBuffer;
    private float minimo = 0, maximo = 0;
    private int width, height;
    // esta textura si es diferente de nulo, dibujamos sobre ella tambien
    protected Texture textura;

    public FrameBuffer(int ancho, int alto, Texture texturaSalida) {
        this.width = ancho;
        this.height = alto;
        zBuffer = new float[ancho][alto];
        bufferColor = new Texture(ancho, alto);
        pixelBuffer = new Fragment[ancho][alto];
        for (Fragment[] row : pixelBuffer) {
            for (int i = 0; i < row.length; i++) {
                row[i] = new Fragment();
            }
        }
        this.textura = texturaSalida;
    }

    /**
     * Actualiza el bufferedimage y carga a la textura de salida
     */
    public void updateOuputTexture() {
        if (textura != null) {
            textura.loadTexture(bufferColor.getQImagen());
        }
    }

    public Fragment[][] getPixelBuffer() {
        return pixelBuffer;
    }

    public BufferedImage getRendered() {
        return bufferColor.getImagen();
    }

    public float[][] getzBuffer() {
        return zBuffer;
    }

    public void setzBuffer(float[][] zBuffer) {
        this.zBuffer = zBuffer;
    }

    /**
     * Limpia el buffer de profundidad
     */
    public void cleanZBuffer() {
        for (float[] row : zBuffer) {
            Arrays.fill(row, Float.NEGATIVE_INFINITY);
        }
    }

    public void clean() {
        cleanZBuffer();
        for (Fragment[] row : pixelBuffer) {
            for (int i = 0; i < row.length; i++) {
                row[i].setDraw(false);
            }
        }
    }

    /**
     * Llena el buffer de color con el color solicitado
     *
     * @param color
     */
    public void fill(QColor color) {
        bufferColor.fill(color);
    }

    public Fragment getFragment(int x, int y) {
        try {
            return pixelBuffer[x][y];
        } catch (Exception e) {
            return null;
        }
    }

    public float getZBuffer(int x, int y) {
        if (x < zBuffer.length && y < zBuffer[0].length) {
            return zBuffer[x][y];
        } else {
            return Float.POSITIVE_INFINITY;
        }
    }

    public void setZBuffer(int x, int y, float valor) {
        zBuffer[x][y] = valor;
    }

    public void setRGB(int x, int y, float r, float g, float b) {
        setQColor(x, y, new QColor(r, g, b));
    }

    public QColor getColor(int x, int y) {
        return bufferColor.getColor(x, y);
    }

    public void setQColor(int x, int y, QColor color) {
        bufferColor.setQColor(x, y, color);
    }

    public QColor getColorNormalizado(float x, float y) {
        return bufferColor.getQColor(x, -y);
    }

    public void setQColorNormalizado(float x, float y, QColor color) {
        bufferColor.setQColor(x, -y, color);
    }

    public void computeMaxMinZbuffer() {
        minimo = Float.POSITIVE_INFINITY;
        maximo = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < zBuffer.length; i++) {
            for (int j = 0; j < zBuffer[0].length; j++) {
                if (zBuffer[i][j] > maximo && zBuffer[i][j] != Float.POSITIVE_INFINITY) {
                    maximo = zBuffer[i][j];
                }
                if (zBuffer[i][j] < minimo && zBuffer[i][j] != Float.NEGATIVE_INFINITY) {
                    minimo = zBuffer[i][j];
                }
            }
        }
    }

    /**
     * Pinta el mapa de profundidad en lugar de los colores rgb.Lo pinta en
     * escala de grises
     *
     * @param farPlane diastancia maxima de la camara
     */
    public void paintZBuffer(float farPlane) {
        try {
            float r = 0, g = 0, b = 0;
            for (int x = 0; x < zBuffer.length; x++) {
                for (int y = 0; y < zBuffer[0].length; y++) {
                    // b = g = r = ((zBuffer[y][x] - minimo) / maximo);
                    b = g = r = (zBuffer[x][y] / farPlane);
                    setRGB(x, y, r, g, b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza una copia de un buffer a otro con dimensiones diferentes
     *
     * @param buffer
     * @param ancho
     * @param alto
     * @return
     */
    public static FrameBuffer copy(FrameBuffer buffer, int ancho, int alto) {
        // QFrameBuffer nuevo = new QFrameBuffer(ancho, alto, buffer.getTextura());
        FrameBuffer nuevo = new FrameBuffer(ancho, alto, null);
        QColor color;
        try {
            // si es expandir
            if (buffer.getWidth() < ancho) {
                for (int x = 0; x < nuevo.getWidth(); x++) {
                    for (int y = 0; y < nuevo.getHeight(); y++) {
                        color = buffer.getColorNormalizado((float) x / nuevo.getWidth(), (float) y / nuevo.getHeight());
                        nuevo.setQColor(x, y, color);
                    }
                }
            } else {
                // si es encojer
                for (int x = 0; x < buffer.getWidth(); x++) {
                    for (int y = 0; y < buffer.getHeight(); y++) {
                        color = buffer.getColor(x, y);
                        nuevo.setQColorNormalizado((float) x / buffer.getWidth(), (float) y / buffer.getHeight(),
                                color);
                    }
                }
            }
        } catch (Exception e) {

        }

        return nuevo;
    }

}
