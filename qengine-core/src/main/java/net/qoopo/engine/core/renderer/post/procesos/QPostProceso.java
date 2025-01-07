/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.procesos;

import net.qoopo.engine.core.texture.Texture;

/**
 * Permite la modificación de la imagen generada por el render
 *
 * @author alberto
 */
public abstract class QPostProceso {

    protected Texture bufferSalida;

    public abstract void procesar(Texture... buffer);

    public Texture getBufferSalida() {
        return bufferSalida;
    }

    public void setBufferSalida(Texture bufferSalida) {
        this.bufferSalida = bufferSalida;
    }

}
