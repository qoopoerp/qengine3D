/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.particles.nieve;

import java.io.File;
import java.util.Random;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlanoBillboard;
import net.qoopo.engine.core.entity.component.particles.Particle;
import net.qoopo.engine.core.entity.component.particles.ParticleEmissor;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaConvexa;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.QUtilComponentes;
import net.qoopo.engine.core.util.image.ImgReader;

/**
 *
 * @author alberto
 */
public class QEmisorNieve extends ParticleEmissor {

    private QMaterialBas material = null;

    private void cargarMaterial() {
        material = null;
        try {
            material = new QMaterialBas(
                    new QTextura(ImgReader.leerImagen(new File("assets/textures/nieve/copo0.png"))), 64);
            // material.setTransAlfa(0.90f);// el objeto tiene una transparencia
            material.setColorTransparente(QColor.BLACK);
            material.setTransparencia(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public QEmisorNieve(AABB ambito, float tiempoVida, int maximoParticulas, int velocidadEmision, QVector3 direccion) {
        super(ambito, tiempoVida, maximoParticulas, velocidadEmision, direccion);
        cargarMaterial();
    }

    private void agregarParticulas() {

        particulasNuevas.clear();

        if (actuales < maximoParticulas) {

            Random rnd = new Random();
            for (int i = 1; i < velocidadEmision; i++) {
                actuales++;
                Particle nueva = new Particle();
                Entity entParticula = new Entity("copo");
                entParticula.setBillboard(true);
                Mesh geometria = new QPlanoBillboard(0.05f, 0.05f);
                QMaterialUtil.aplicarMaterial(geometria, material);
                entParticula.addComponent(geometria);

                CollisionShape colision = new QColisionMallaConvexa(geometria);
                entParticula.addComponent(colision);

                QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.DINAMICO);
                rigido.setMasa(0.00005f, QVector3.zero.clone());
                // rigido.setMasa(0.0005f, QVector3.zero.clone());
                rigido.setFormaColision(colision);
                entParticula.addComponent(rigido);

                nueva.setTiempoVida(tiempoVida);
                nueva.iniciarVida();

                // ubicacion inicial de la particula
                entParticula.move(
                        entity.getMatrizTransformacion(System.currentTimeMillis()).toTranslationVector().x
                                + rnd.nextFloat() * (ambito.aabMaximo.location.x - ambito.aabMinimo.location.x)
                                + ambito.aabMinimo.location.x,
                        entity.getMatrizTransformacion(System.currentTimeMillis()).toTranslationVector().y
                                + ambito.aabMaximo.location.y,
                        entity.getMatrizTransformacion(System.currentTimeMillis()).toTranslationVector().z
                                + rnd.nextFloat() * (ambito.aabMaximo.location.z - ambito.aabMinimo.location.z)
                                + ambito.aabMinimo.location.z);
                entParticula.rotate(0, (float) (rnd.nextFloat() * Math.toRadians(180)), 0);
                nueva.objeto = entParticula;
                // nueva.objeto.setPadre(this.entity);
                // this.entity.agregarHijo(nueva.objeto);
                this.particulasNuevas.add(nueva);
                // nueva
            }
            this.particulas.addAll(particulasNuevas);
        }
    }

    private void eliminarParticulas() {
        particulasEliminadas.clear();
        for (Particle particula : this.particulas) {
            // copo.setTiempoVida(copo.getTiempoVida() - 0.1f);
            // si ya paso su tiempo de vida o ya esta fuera del ambito en la altura
            if ((System.currentTimeMillis() - particula.getTiempoCreacion()) / 1000 > particula.getTiempoVida()
                    || (particula.objeto.getTransformacion().getTraslacion().y < ambito.aabMinimo.location.y)) {
                particulasEliminadas.add(particula);
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

    private void modificarParticulas(long deltaTime) {
        // la modificacion de las particulas de la nieva se basa en un zigzagueo hacia
        // abajo
        Random rnd = new Random();
        float miniMov = -0.00009f;
        float maxMov = 0.00009f;

        for (Particle copo : this.particulas) {
            QObjetoRigido rigido = QUtilComponentes.getFisicoRigido(copo.objeto);
            rigido.agregarFuerzas(QVector3.of(
                    rnd.nextFloat() * (maxMov - miniMov) + miniMov,
                    rnd.nextFloat() * (maxMov - miniMov) + miniMov,
                    rnd.nextFloat() * (maxMov - miniMov) + miniMov));
        }
    }

    @Override
    public void emitir(long deltaTime) {

        // verificar si hay que agregar nuevos
        agregarParticulas();

        // verifica el tiempo de vida de cada particula para eliminarla si ya expiro
        eliminarParticulas();

        // modifica las particulas actuales
        modificarParticulas(deltaTime);
    }

    @Override
    public void destruir() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

}