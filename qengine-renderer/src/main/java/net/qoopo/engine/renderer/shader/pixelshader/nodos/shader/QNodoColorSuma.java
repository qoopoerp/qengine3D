/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.pixelshader.nodos.shader;

import java.util.ArrayList;

import net.qoopo.engine.core.entity.component.mesh.primitive.QPixel;
import net.qoopo.engine.core.material.node.core.ShaderNode;
import net.qoopo.engine.core.material.node.core.perifericos.QPerColor;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;

/**
 *
 * @author alberto
 */
public class QNodoColorSuma extends ShaderNode {

    private QPerColor enColor1;
    private QPerColor enColor2;
    private QPerColor saColor;

    public QNodoColorSuma() {
        enColor1 = new QPerColor(QColor.WHITE);
        enColor1.setNodo(this);
        enColor2 = new QPerColor(QColor.WHITE);
        enColor2.setNodo(this);
        saColor = new QPerColor(QColor.WHITE);
        saColor.setNodo(this);
        entradas = new ArrayList<>();
        entradas.add(enColor1);
        entradas.add(enColor2);
        salidas = new ArrayList<>();
        salidas.add(saColor);
    }

    public QNodoColorSuma(QColor color1, QColor color2) {
        enColor1 = new QPerColor(color1);
        enColor2 = new QPerColor(color2);

        saColor = new QPerColor(QColor.WHITE);
        enColor1.setNodo(this);
        saColor.setNodo(this);

        entradas = new ArrayList<>();

        entradas.add(enColor1);
        entradas.add(enColor2);
        salidas = new ArrayList<>();
        salidas.add(saColor);
    }

    @Override
    public void procesar(RenderEngine render, QPixel pixel) {
        if (render.opciones.isMaterial()) {
            //toma el color de entrada        
            enColor1.procesarEnlaces(render, pixel);
            enColor2.procesarEnlaces(render, pixel);
            saColor.setColor(enColor1.getColor().add(enColor2.getColor()));
        }
    }

    public QPerColor getEnColor1() {
        return enColor1;
    }

    public void setEnColor1(QPerColor enColor1) {
        this.enColor1 = enColor1;
    }

    public QPerColor getEnColor2() {
        return enColor2;
    }

    public void setEnColor2(QPerColor enColor2) {
        this.enColor2 = enColor2;
    }

    public QPerColor getSaColor() {
        return saColor;
    }

    public void setSaColor(QPerColor saColor) {
        this.saColor = saColor;
    }

}
