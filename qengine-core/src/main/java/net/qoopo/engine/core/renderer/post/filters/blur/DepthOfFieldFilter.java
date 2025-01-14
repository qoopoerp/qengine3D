/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.filters.blur;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.post.FilterTexture;
import net.qoopo.engine.core.texture.Texture;

/**
 * Realiza un efecto blur solo a ciertas partes d ela imagen cuando cumple una
 * distancia
 *
 * @author alberto
 */
public class DepthOfFieldFilter implements FilterTexture {

    private static float[] pesos = { 0.084264f, 0.088139f, 0.091276f, 0.093585f, 0.094998f, 0.095474f, 0.094998f,
            0.093585f, 0.091276f, 0.088139f, 0.084264f };
    private static int kernel_size = 11;
    private static int mitad = 5;
    private float escala = 0.25f;
    private int repeticiones = 1;
    private float distanciaFocal = 0.5f;// de 0 a 1;
    public static int DESENFOQUE_LEJOS = 1;// desenfoca lo que esta mas lejos de la distancia
    public static int DESENFOQUE_CERCA = 2; // desenfoa lo que esta mas ceca de la distancia
    public static int DESENFOQUE_EXCLUYENTE = 3;// desenfoca lo que no este en la distancia
    private int tipo = DESENFOQUE_LEJOS;
    private static float margen = 0.05f;


    public DepthOfFieldFilter( int tipoDesenfoque,float distanciaFocal) {
        this.distanciaFocal = distanciaFocal;
        this.tipo = tipoDesenfoque;
    }

    public DepthOfFieldFilter( int tipoDesenfoque,float distanciaFocal, float escala, int repeticiones) {
        this.distanciaFocal = distanciaFocal;
        this.tipo = tipoDesenfoque;
        this.escala = escala;
        this.repeticiones = repeticiones;
    }

    @Override
    public Texture apply(Texture... buffer) {
        Texture output = buffer[0];
        for (int i = 1; i <= repeticiones; i++) {
            output = transposedHBlur(transposedHBlur(output));
        }
        return output;
    }

    private Texture transposedHBlur(Texture buffer) {
        int height = buffer.getHeight();
        int width = buffer.getWidth();

        int nuevoAncho = (int) (height * escala);
        int nuevoAlto = (int) (width * escala);
        if (nuevoAncho <= 0) {
            nuevoAncho = 1;
        }
        if (nuevoAlto <= 0) {
            nuevoAlto = 1;
        }

        // result is transposed, so the width/height are swapped
        // System.out.println("ancho=" + nuevoAncho + " alto=" + nuevoAlto);
        Texture bufferReturn = new Texture(nuevoAncho, nuevoAlto);
        QColor pixel = new QColor();

        // buffer.calcularMaximosMinimosZBuffer();
        // float mitadBuffer = (buffer.getMaximo()+ buffer.getMinimo()) / 2.0f;
        // horizontal blur, transpose result

        float maxBuffer = Float.MIN_VALUE;
        float minBuffer = Float.MAX_VALUE;
        for (int x = 0; x < buffer.getWidth(); x++) {
            for (int y = 0; y < buffer.getHeight(); y++) {
                maxBuffer = Math.max(maxBuffer, buffer.getB(x, y));
                minBuffer = Math.max(minBuffer, buffer.getB(x, y));
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = mitad; x < width - mitad; x++) {
                if ((tipo == DESENFOQUE_LEJOS && buffer.getB(x, y) / maxBuffer >= distanciaFocal)
                        || (tipo == DESENFOQUE_CERCA && buffer.getB(x, y) / maxBuffer <= distanciaFocal)
                        || (tipo == DESENFOQUE_EXCLUYENTE && Math.abs(buffer.getB(x, y) /
                                maxBuffer - distanciaFocal) > margen)) {
                    pixel.set(1, 0, 0, 0);
                    for (int i = 0; i < kernel_size; i++) {
                        pixel = pixel.add(buffer.getColor(x + i - mitad, y).scale(pesos[i]));
                    }
                } else {
                    pixel = buffer.getColor(x, y);// no realiza el efecto blur
                }
                // transpose result!
                bufferReturn.setQColor((int) (x * escala), (int) (y * escala), pixel);
                // setea la profundidad
                // bufferReturn.setZBuffer(x, y, buffer.getZBuffer(x, y));
            }
        }

        return bufferReturn;
    }

}
