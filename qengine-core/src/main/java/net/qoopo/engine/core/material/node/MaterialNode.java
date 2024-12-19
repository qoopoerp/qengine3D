/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.material.node;

import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.material.node.core.output.MaterialOutputNode;

/**
 *
 * @author alberto
 */
public class MaterialNode extends AbstractMaterial {

    private MaterialOutputNode nodo;

    public MaterialNode() {
    }

    public MaterialNode(String nombre) {
        this.nombre = nombre;
    }

    public MaterialOutputNode getNodo() {
        return nodo;
    }

    public void setNodo(MaterialOutputNode nodo) {
        this.nodo = nodo;
    }

}
