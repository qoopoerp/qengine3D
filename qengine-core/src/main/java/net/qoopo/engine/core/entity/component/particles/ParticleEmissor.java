/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.particles;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.math.QVector3;

/**
 *
 * @author alberto
 */
public abstract class ParticleEmissor implements EntityComponent {

    @Getter
    @Setter
    protected Entity entity;

    // define el area sobre el que se va a emitir las particulas
    protected AABB ambito;

    protected List<Particle> particulas = new ArrayList<>();
    protected List<Particle> particulasEliminadas = new ArrayList<>();
    protected List<Particle> particulasNuevas = new ArrayList<>();

    protected long actuales = 0;

    /**
     * Tiempo de vida de las particulas en segundos
     */
    protected float tiempoVida;

    /**
     * Máximo número de particulas a emitir al mismo tiempo
     */
    protected int maximoParticulas;
    /**
     * Número de particulas a emitir cada segundo
     */
    protected int velocidadEmision;

    /**
     * Direccion de emision de las particulas
     */
    protected QVector3 direccion;

    /**
     * Emite las particulas. Se llama en cada pasada de renderizado
     */
    public abstract void emitir(long deltaTime);

    public ParticleEmissor(AABB ambito, float tiempoVida, int maximoParticulas, int velocidadEmision,
            QVector3 direccion) {
        this.ambito = ambito;
        this.tiempoVida = tiempoVida;
        this.maximoParticulas = maximoParticulas;
        this.velocidadEmision = velocidadEmision;
        this.direccion = direccion;
    }

    public List<Particle> getParticulas() {
        return particulas;
    }

    public void setParticulas(List<Particle> particulas) {
        this.particulas = particulas;
    }

    public List<Particle> getParticulasEliminadas() {
        return particulasEliminadas;
    }

    public void setParticulasEliminadas(List<Particle> particulasEliminadas) {
        this.particulasEliminadas = particulasEliminadas;
    }

    public List<Particle> getParticulasNuevas() {
        return particulasNuevas;
    }

    public void setParticulasNuevas(List<Particle> particulasNuevas) {
        this.particulasNuevas = particulasNuevas;
    }

    @Override
    public void destruir() {
        ambito = null;
        particulas.clear();
        particulas = null;
        particulasEliminadas.clear();
        particulasEliminadas = null;
        particulasNuevas.clear();
        particulasNuevas = null;
    }

}
