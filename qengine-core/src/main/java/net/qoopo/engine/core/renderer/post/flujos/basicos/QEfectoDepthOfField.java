/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.flujos.basicos;

import net.qoopo.engine.core.renderer.post.flujos.QRenderEfectos;
import net.qoopo.engine.core.renderer.post.procesos.blur.QProcesadorDepthOfField;
import net.qoopo.engine.core.texture.Texture;

/**
 *
 * @author alberto
 */
public class QEfectoDepthOfField extends QRenderEfectos {

    private QProcesadorDepthOfField filtro = null;
    private int tipo;
    private float distanciaFocal = 0.5f;

    public QEfectoDepthOfField(int tipo, float distancia) {
//        contraste = new QProcesadorContraste(0.1f);
        this.tipo = tipo;
        this.distanciaFocal = distancia;
    }

    @Override
    public Texture ejecutar(Texture buffer) {
        try {
            //blur
            if (filtro == null) {
                filtro = new QProcesadorDepthOfField(distanciaFocal, tipo, 1.0f, 1);
            }
            filtro.procesar(buffer);
            return filtro.getBufferSalida();
        } catch (Exception e) {
            return null;
        }
    }

}
