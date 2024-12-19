/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.material.node.core;

import java.util.ArrayList;
import java.util.List;

import net.qoopo.engine.core.entity.component.mesh.primitive.QPixel;
import net.qoopo.engine.core.renderer.RenderEngine;

/**
 *
 * @author alberto
 */
public class QNodoPeriferico {

    protected ShaderNode nodo;// el nodo al que pertenece

    protected List<QNodoEnlace> enlaces = new ArrayList<>();

    public ShaderNode getNodo() {
        return nodo;
    }

    public boolean tieneEnlaces() {
        return getEnlaces() != null && !getEnlaces().isEmpty();
    }

    public void setNodo(ShaderNode nodo) {
        this.nodo = nodo;
    }

    public List<QNodoEnlace> getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(List<QNodoEnlace> enlaces) {
        this.enlaces = enlaces;
    }

    /**
     * Si tiene enlaces (cuando es de entrada) procesa las entradas y toma los
     * valores de salida
     *
     * @param render
     * @param pixel
     */
    public void procesarEnlaces(RenderEngine render, QPixel pixel) {
        if (enlaces != null && !enlaces.isEmpty()) {
            for (QNodoEnlace enlace : enlaces) {
                // si yo soy un periferico de entrada, debería estar en la salida del enlace
                if (enlace.getSalida().equals(this)) {
                    enlace.getEntrada().getNodo().procesar(render, pixel);
                }
            }
        }
    }
}
