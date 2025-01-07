/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.flujos.basicos;

import net.qoopo.engine.core.renderer.post.flujos.QRenderEfectos;
import net.qoopo.engine.core.renderer.post.procesos.color.QProcesadorContraste;
import net.qoopo.engine.core.texture.Texture;

/**
 * Realiza un efecto de realce de contraste
 *
 * @author alberto
 */
public class QEfectoContraste extends QRenderEfectos {

    private QProcesadorContraste filtro = null;

    public QEfectoContraste() {
    }

    @Override
    public Texture ejecutar(Texture buffer) {
        try {
            if (filtro == null || (filtro.getBufferSalida().getAncho() != buffer.getAncho() && filtro.getBufferSalida().getAlto() != buffer.getAlto())) {
                filtro = new QProcesadorContraste(buffer.getAncho(), buffer.getAlto(), 0.2f);
            }
          filtro.procesar(buffer);
            return filtro.getBufferSalida();
        } catch (Exception e) {
            return null;
        }
    }
}
