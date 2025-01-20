/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.dinamica;

import net.qoopo.engine.core.math.Vector3;

/**
 *
 * @author alberto
 */
public class QObjetoSuave extends QObjetoDinamico {

    public Vector3 velocidadLinear = Vector3.zero;
    public Vector3 velocidadAngular = Vector3.zero;
    public Vector3 fuerzaInercial = Vector3.zero;
    public Vector3 fuerzaTotal = Vector3.zero;
    public Vector3 fueraTorque = Vector3.zero;

    public float masa;

    public QObjetoSuave(int tipo_dinamico) {
        super(tipo_dinamico);
    }

}
