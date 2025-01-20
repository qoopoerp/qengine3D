/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.math;

import java.io.Serializable;

/**
 *
 * @author alberto
 */
public class Rotation implements Serializable {

    public static final int ANGULOS_EULER = 1;
    public static final int CUATERNION = 2;
    private int tipo = ANGULOS_EULER;
    private Cuaternion cuaternion = new Cuaternion();
    private Vector3 eulerAngles = new Vector3();

    public Rotation() {
        tipo = ANGULOS_EULER;
        updateCuaternion();
    }

    public void reset() {
        eulerAngles.set(0, 0, 0);
        updateCuaternion();
    }

    public Rotation(int tipo) {
        this.tipo = tipo;
        updateCuaternion();
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
        updateCuaternion();
    }

    public Cuaternion getCuaternion() {
        if (tipo == ANGULOS_EULER) {
            updateCuaternion();
        }
        return cuaternion;
    }

    public void setCuaternion(Cuaternion cuaternion) {
        this.cuaternion = cuaternion;
    }

    public Vector3 getEulerAngles() {
        if (tipo == CUATERNION) {
            updateEuler();
        }
        return eulerAngles;
    }

    public void updateEuler() {
        float[] ang = cuaternion.toAngles(null);
        eulerAngles.set(ang[0], ang[1], ang[2]);
    }

    public void updateCuaternion() {
        cuaternion.fromAngles(eulerAngles.x, eulerAngles.y, eulerAngles.z);
    }

    public void setEulerAngles(Vector3 angulos) {
        this.eulerAngles = angulos;
        updateCuaternion();
    }

    public void rotarX(float angulo) {
        eulerAngles.x = angulo;
        updateCuaternion();
    }

    public void rotarY(float angulo) {
        eulerAngles.y = angulo;
        updateCuaternion();
    }

    public void rotarZ(float angulo) {
        eulerAngles.z = angulo;
        updateCuaternion();
    }

    public void aumentarRotacion(float x, float y, float z) {
        aumentarRotX(x);
        aumentarRotY(y);
        aumentarRotZ(z);
    }

    public void aumentarRotX(float angulo) {
        rotarX(eulerAngles.x + angulo);
    }

    public void aumentarRotY(float angulo) {
        rotarY(eulerAngles.y + angulo);
    }

    public void aumentarRotZ(float angulo) {
        rotarZ(eulerAngles.z + angulo);
    }

    @Override
    public Rotation clone() {
        Rotation nuevo = new Rotation(tipo);
        nuevo.setEulerAngles(eulerAngles.clone());
        nuevo.setCuaternion(cuaternion.clone());
        return nuevo;
    }

    @Override
    public String toString() {
        return "Rotation {" + "t=" + tipo + ", c=" + cuaternion + ", a=" + eulerAngles + '}';
    }

}
