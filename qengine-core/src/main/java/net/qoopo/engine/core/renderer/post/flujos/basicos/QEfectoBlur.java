/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.flujos.basicos;

import net.qoopo.engine.core.renderer.post.flujos.QRenderEfectos;
import net.qoopo.engine.core.renderer.post.procesos.blur.QProcesadorBlur;
import net.qoopo.engine.core.texture.Texture;

/**
 *
 * @author alberto
 */
public class QEfectoBlur extends QRenderEfectos {

    private QProcesadorBlur filtro = null;

    public QEfectoBlur(int ancho, int alto) {
        filtro = new QProcesadorBlur(ancho, alto, 10);
    }

    public QEfectoBlur() {
        filtro = new QProcesadorBlur(1.0f, 10);
    }

    @Override
    public Texture ejecutar(Texture buffer) {
        try {
            filtro.procesar(buffer);
            return filtro.getBufferSalida();
        } catch (Exception e) {
            return null;
        }
    }

}
