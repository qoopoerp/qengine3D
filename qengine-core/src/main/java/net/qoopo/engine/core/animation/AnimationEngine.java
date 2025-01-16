/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.animation;

import net.qoopo.engine.core.engine.Engine;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.animation.AnimationComponent;
import net.qoopo.engine.core.scene.Scene;

/**
 * Este motor se encarga de procesar los frames de animacion
 *
 * @author alberto
 */
public class AnimationEngine extends Engine {

    protected Scene scene;
    private float tiempoInicio = 0;
    private float tiempo;
    private float tiempoFin = 10;
    private float velocidad = 1.0f;
    private float direccion = 1.0f;

    public AnimationEngine(Scene universo) {
        this.scene = universo;
    }

    @Override
    public void start() {
        ejecutando = true;
        beforeTime = System.currentTimeMillis();
    }

    @Override
    public void stop() {
        ejecutando = false;
        tiempo = 0;
        actualizarPoses(0);
        try {
            Thread.sleep(300);
        } catch (Exception e) {

        }
    }

    public void velocidad1x() {
        velocidad = 1.0f;
    }

    public void velocidad2x() {
        velocidad = 2.0f;
    }

    public void velocidad4x() {
        velocidad = 4.0f;
    }

    public void velocidad05x() {
        velocidad = 0.5f;
    }

    public void velocidad04x() {
        velocidad = 0.4f;
    }

    /**
     *
     * @param marcaTiempo
     */
    public void actualizarPoses(float marcaTiempo) {
        try {
            AnimationComponent actual;
            for (Entity entity : scene.getEntities()) {
                if (entity.isToRender()) {
                    for (EntityComponent componente : entity.getComponents()) {
                        if (componente instanceof AnimationComponent) {
                            actual = (AnimationComponent) componente;
                            actual.updateAnim(marcaTiempo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long update() {
        tiempo += (getDelta() * velocidad / (1000.0f)) * direccion;
        if (tiempo > tiempoFin) {
            tiempo %= tiempoFin + tiempoInicio;
        }
        if (tiempo < 0.00) {
            tiempo = tiempoFin - tiempo;
        }
        actualizarPoses(tiempo);
        beforeTime = System.currentTimeMillis();
        return beforeTime;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene escena) {
        this.scene = escena;
    }

    public float getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(float tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public float getTiempo() {
        return tiempo;
    }

    public void setTiempo(float tiempo) {
        this.tiempo = tiempo;
    }

    public float getTiempoFin() {
        return tiempoFin;
    }

    public void setTiempoFin(float tiempoFin) {
        this.tiempoFin = tiempoFin;
    }

    public float getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }

    public float getDireccion() {
        return direccion;
    }

    public void setDireccion(float direccion) {
        this.direccion = direccion;
    }

    public void invertir() {
        direccion *= -1.0f;
    }

}
