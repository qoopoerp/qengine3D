/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.particles.fire;

import java.util.Random;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Plane;
import net.qoopo.engine.core.entity.component.particles.Particle;
import net.qoopo.engine.core.entity.component.particles.ParticleEmissor;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.texture.util.MaterialUtil;

/**
 *
 * @author alberto
 */
public class QEmisorVolcan extends ParticleEmissor {

    private Material material = null;

    private void cargarMaterial() {
        material = null;
        try {
            material = new Material(AssetManager.get().loadTexture("fuego", "res/fuego/fuego1.png"), 64);
            material.setTransAlfa(0.90f);// el objeto tiene una transparencia
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public QEmisorVolcan(AABB ambito, float tiempoVida, int maximoParticulas, int velocidadEmision,
            QVector3 direccion) {
        super(ambito, tiempoVida, maximoParticulas, velocidadEmision, direccion);
        cargarMaterial();
    }

    private void agregarParticulas() {

        particulasNuevas.clear();

        if (actuales < maximoParticulas) {

            Random rnd = new Random();
            for (int i = 1; i < velocidadEmision; i++) {
                actuales++;
                // QParticula nueva = new QParticula("copo");
                Particle nueva = new Particle();

                Entity nuevaentity = new Entity("flama");

                Mesh geometria = new Plane(0.5f, 0.5f);
                MaterialUtil.applyMaterial(geometria, material);
                nuevaentity.addComponent(geometria);

                QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.DINAMICO);

                rigido.velocidadLinear = direccion.clone();
                rigido.setMasa(0.005f, QVector3.zero.clone());
                nuevaentity.addComponent(rigido);

                nueva.setTiempoVida(tiempoVida);
                nueva.iniciarVida();

                // ubicacion inicial de la particula
                nuevaentity.getTransform().move(
                        rnd.nextFloat() * (ambito.aabMaximo.location.x - ambito.aabMinimo.location.x)
                                + ambito.aabMinimo.location.x,
                        ambito.aabMaximo.location.y,
                        rnd.nextFloat() * (ambito.aabMaximo.location.z - ambito.aabMinimo.location.z)
                                + ambito.aabMinimo.location.z);

                nueva.objeto = nuevaentity;

                this.particulasNuevas.add(nueva);
                // nueva
            }
            this.particulas.addAll(particulasNuevas);
        }
    }

    private void eliminarParticulas() {
        particulasEliminadas.clear();
        for (Particle particula : this.particulas) {

            // si ya paso su tiempo de vida o ya esta fuera del ambito en la altura
            if ((System.currentTimeMillis() - particula.getTiempoCreacion()) / 1000 > particula.getTiempoVida()
                    || (particula.objeto.getTransform().getLocation().y < ambito.aabMinimo.location.y)) {
                // particulasEliminadas.add(copo);
                actuales--;
                particula.objeto.setToRender(false);
                particula.objeto.setToDelete(true);
            }
        }
        if (actuales < 0) {
            actuales = 0;
        }
        this.particulas.removeAll(particulasEliminadas);
    }

    private void modificarParticulas() {
        // la modificacion de las particulas de la nieva se basa en un zigzagueo hacia
        // abajo
        Random rnd = new Random();
        float miniMov = -0.005f;
        float maxMov = 0.005f;
        for (Particle copo : this.particulas) {
            // se agrega un impulso
            for (EntityComponent componente : copo.objeto.getComponents()) {
                if (componente instanceof QObjetoRigido) {
                    ((QObjetoRigido) componente).agregarFuerzas(QVector3.of(
                            rnd.nextFloat() * (maxMov - miniMov) + miniMov,
                            0,
                            rnd.nextFloat() * (maxMov - miniMov) + miniMov));
                }
            }
        }
    }

    @Override
    public void emitir(long deltaTime) {

        // verificar si hay que agregar nuevos
        agregarParticulas();

        // verifica el tiempo de vida de cada particula para eliminarla si ya expiro
        eliminarParticulas();

        // modifica las particulas actuales
        modificarParticulas();
        // System.out.println("particulas actuales =" + actuales);
    }

    @Override
    public void destroy() {
        material.destroy();
        material = null;
    }
}
