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
public class QObjetoRigido extends QObjetoDinamico {

    public Vector3 velocidadLinear = Vector3.zero.clone();
    public Vector3 velocidadAngular = Vector3.zero.clone();
    public Vector3 fuerzaInercial = Vector3.zero.clone();

    // cada objeto tiene una afectacion diferente de la gravedad
    // este vector peso sera el resultado de multiplicar la gravedad por la masa
    public Vector3 peso = Vector3.zero.clone();

    // la fuerza que se aplica en cada pasada a la velocidad
    public Vector3 fuerzaTotal = Vector3.zero.clone();
    public Vector3 fuerzaTorque = Vector3.zero.clone();

    private float masa = 1.0f;
    private float friccion = 0.5f;
    private float restitucion = 0.f; // elasticidad
    private float amortiguacion_traslacion = 0.04f; // interpolateLinear
    private float amortiguacion_rotacion = 0.1f; // angular

    private boolean usado;

    public QObjetoRigido(int tipo_dinamico) {
        super(tipo_dinamico);
        if (tipo_dinamico == ESTATICO) {
            masa = 0;
        }
    }

    public QObjetoRigido(int tipo_dinamico, float masa) {
        super(tipo_dinamico);
        this.masa = masa;
        if (tipo_dinamico == ESTATICO) {
            masa = 0;
        }
    }

    /**
     * Agrega un impulso
     *
     * @param nuevaFuerza
     */
    public void agregarFuerzas(Vector3... nuevaFuerza) {
        fuerzaTotal.add(nuevaFuerza);
    }

    public void agregarFuerzasTorque(Vector3... nuevaFuerza) {
        fuerzaTorque.add(nuevaFuerza);
    }

    // public void agregarFuerzas(float deltaTime, QVector3... nuevaFuerza) {
    // fuerzaTotal.add(deltaTime, nuevaFuerza);
    // }
    public void aplicarGravedad() {
        if (tipoDinamico == QObjetoDinamico.DINAMICO) {
            agregarFuerzas(peso);
        }
    }

    public void actualizarVelocidad(float deltaTime) {
        if (tipoDinamico == QObjetoDinamico.DINAMICO) {
            velocidadLinear.add(fuerzaTotal.multiply(deltaTime));
        }
    }

    public void actualizarMovimiento(float deltaTime) {
        if (tipoDinamico == QObjetoDinamico.DINAMICO || tipoDinamico == QObjetoDinamico.CINEMATICO) {
            actualizarVelocidad(deltaTime);
            // transformacion.getLocation().add(velocidadLinear);
            entity.move(entity.getTransform().getLocation().clone().add(velocidadLinear));
            // luego llamo al metodo para indicar que su movimeinto fue actualizado
            entity.comprobarMovimiento();
        }
    }

    public void limpiarFuezas() {
        fuerzaTotal.set(0, 0, 0);
        fuerzaTorque.set(0, 0, 0);
    }

    public void addForceAtPosition(Vector3 force, Vector3 position) {
        agregarFuerzasTorque(position.clone().cross(force));
    }

    public void detener() {
        limpiarFuezas();
        velocidadLinear.set(0, 0, 0);
    }

    public void calcularPeso(Vector3 gravedad) {
        if (masa != 0f) {
            this.peso = gravedad.clone().multiply(masa);
        }
    }

    public Vector3 getPeso() {
        return peso.clone();
    }

    public float getMasa() {
        return masa;
    }

    public void setMasa(float masa, Vector3 inercia) {
        if (masa == 0f) {
            // this.tipoDinamico = ESTATICO;
        } else {
            // this.tipoDinamico = DINAMICO;
            // this.masa = 1 / masa;
            this.masa = masa;
        }
        this.fuerzaInercial.set(
                inercia.x != 0f ? 1f / inercia.x : 0f,
                inercia.y != 0f ? 1f / inercia.y : 0f,
                inercia.z != 0f ? 1f / inercia.z : 0f);

    }

    public Vector3 getFuerzaInercial() {
        return fuerzaInercial;
    }

    public void setFuerzaInercial(Vector3 fuerzaInercial) {
        this.fuerzaInercial = fuerzaInercial;
    }

    public float getFriccion() {
        return friccion;
    }

    public void setFriccion(float friccion) {
        this.friccion = friccion;
    }

    public float getRestitucion() {
        return restitucion;
    }

    public void setRestitucion(float restitucion) {
        this.restitucion = restitucion;
    }

    public float getAmortiguacion_traslacion() {
        return amortiguacion_traslacion;
    }

    public void setAmortiguacion_traslacion(float amortiguacion_traslacion) {
        this.amortiguacion_traslacion = amortiguacion_traslacion;
    }

    public float getAmortiguacion_rotacion() {
        return amortiguacion_rotacion;
    }

    public void setAmortiguacion_rotacion(float amortiguacion_rotacion) {
        this.amortiguacion_rotacion = amortiguacion_rotacion;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

}
