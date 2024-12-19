/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.flujos.basicos;

import net.qoopo.engine.core.renderer.post.flujos.QRenderEfectos;
import net.qoopo.engine.core.renderer.post.procesos.color.QProcesadorCel;
import net.qoopo.engine.core.texture.QTextura;

/**
 * Realiza un efecto de realce de contraste
 *
 * @author alberto
 */
public class QEfectoCel extends QRenderEfectos {

    private QProcesadorCel filtro = null;

    public QEfectoCel() {
    }

    @Override
    public QTextura ejecutar(QTextura buffer) {
        try {
            if (filtro == null || (filtro.getBufferSalida().getAncho() != buffer.getAncho() && filtro.getBufferSalida().getAlto() != buffer.getAlto())) {
                filtro = new QProcesadorCel(buffer.getAncho(), buffer.getAlto());
            }
            filtro.procesar(buffer);
            return filtro.getBufferSalida();
        } catch (Exception e) {
            return null;
        }
    }
}
