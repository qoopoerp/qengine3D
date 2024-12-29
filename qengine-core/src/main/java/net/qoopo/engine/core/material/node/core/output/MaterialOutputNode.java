/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.material.node.core.output;

import java.util.ArrayList;

import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.material.node.core.ShaderNode;
import net.qoopo.engine.core.material.node.core.perifericos.QPerColor;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;

/**
 * Este nodo debe ser la salida o nodo raiz del material
 *
 * @author alberto
 */
public class MaterialOutputNode extends ShaderNode {

    private QPerColor saColor;
    private QPerColor enColor;

    public MaterialOutputNode() {
        enColor = new QPerColor(QColor.WHITE);
        enColor.setNodo(this);
        saColor = new QPerColor(QColor.WHITE);
        saColor.setNodo(this);
        entradas = new ArrayList<>();
        entradas.add(enColor);
        salidas = new ArrayList<>();
        salidas.add(saColor);
    }

    @Override
    public void procesar(RenderEngine render, Fragment pixel) {
        // esta activada la opción de material
        if (render.opciones.isMaterial()) {
            enColor.procesarEnlaces(render, pixel);
            saColor.setColor(enColor.getColor());
        }
    }

    public QPerColor getSaColor() {
        return saColor;
    }

    public void setSaColor(QPerColor saColor) {
        this.saColor = saColor;
    }

    public QPerColor getEnColor() {
        return enColor;
    }

    public void setEnColor(QPerColor enColor) {
        this.enColor = enColor;
    }

}
