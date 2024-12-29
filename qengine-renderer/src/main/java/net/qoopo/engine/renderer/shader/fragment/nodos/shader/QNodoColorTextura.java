/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.nodos.shader;

import java.util.ArrayList;

import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.material.node.core.ShaderNode;
import net.qoopo.engine.core.material.node.core.perifericos.QPerColor;
import net.qoopo.engine.core.material.node.core.perifericos.QPerImagen;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.texture.procesador.QProcesadorTextura;

/**
 *
 * @author alberto
 */
public class QNodoColorTextura extends ShaderNode {

    private QPerColor saColor;
    private QPerImagen enImagen;

    public QNodoColorTextura(QProcesadorTextura textura) {
        enImagen = new QPerImagen(textura);
        enImagen.setNodo(this);
        saColor = new QPerColor(QColor.WHITE);
        saColor.setNodo(this);
        entradas = new ArrayList<>();
        entradas.add(enImagen);
        salidas = new ArrayList<>();
        salidas.add(saColor);
    }

    @Override
    public void procesar(RenderEngine render, Fragment pixel) {
        if (render.opciones.isMaterial()) {
            saColor.setColor(enImagen.getTextura().get_QARGB(pixel.u, pixel.v));
        }
    }

    public QPerColor getSaColor() {
        return saColor;
    }

    public void setSaColor(QPerColor saColor) {
        this.saColor = saColor;
    }

    public QPerImagen getEnImagen() {
        return enImagen;
    }

    public void setEnImagen(QPerImagen enImagen) {
        this.enImagen = enImagen;
    }

}
