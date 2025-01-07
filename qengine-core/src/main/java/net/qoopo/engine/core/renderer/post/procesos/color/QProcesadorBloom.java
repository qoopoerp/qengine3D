/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.procesos.color;

import net.qoopo.engine.core.renderer.post.procesos.QPostProceso;
import net.qoopo.engine.core.renderer.post.procesos.blur.QProcesadorBlur;
import net.qoopo.engine.core.texture.Texture;

/**
 * Realiza una modificación del color final de la imagen realzando el contraste
 *
 * @author alberto
 */
public class QProcesadorBloom extends QPostProceso {

    private QProcesadorBlur blur = null;
    private QProcesadorBrillo brillo = null;
    private QProcesadorCombina combina = null;
    private QProcesadorContraste contraste = null;

    public QProcesadorBloom(int ancho, int alto) {
        this.bufferSalida = new Texture(ancho, alto);
        this.blur = new QProcesadorBlur(ancho, alto, 10);
        this.brillo = new QProcesadorBrillo(ancho, alto, 0.85f);
        this.combina = new QProcesadorCombina(ancho, alto);
        this.contraste = new QProcesadorContraste(ancho, alto, 0.3f);
    }

    public QProcesadorBloom(int ancho, int alto, float brillo) {
        this.bufferSalida = new Texture(ancho, alto);
        this.blur = new QProcesadorBlur(ancho, alto, 10);
        this.brillo = new QProcesadorBrillo(ancho, alto, brillo);
        this.combina = new QProcesadorCombina(ancho, alto);
        this.contraste = new QProcesadorContraste(ancho, alto, 0.3f);
    }

    @Override
    public void procesar(Texture... buffer) {
        try {
            Texture textura = buffer[0];
            brillo.procesar(textura);
            blur.procesar(brillo.getBufferSalida());
            combina.procesar(textura, blur.getBufferSalida());
            contraste.procesar(combina.getBufferSalida());
            bufferSalida.loadTexture(contraste.getBufferSalida().getImagen());
            // bufferSalida.loadTexture(combina.getBufferSalida().getImagen());
            // bufferSalida.loadTexture(blur.getBufferSalida().getImagen());
        } catch (Exception e) {

        }
        // bufferSalida.actualizarTextura();
    }

}
