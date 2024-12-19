/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.flujos.basicos;

import net.qoopo.engine.core.renderer.post.flujos.QRenderEfectos;
import net.qoopo.engine.core.texture.QTextura;

/**
 * No realiza ningun efecto post procesamiento
 *
 * @author alberto
 */
public class QEfectosDefault extends QRenderEfectos {

    public QEfectosDefault() {
    }

    @Override
    public QTextura ejecutar(QTextura buffer) {
        return buffer;
    }

}
