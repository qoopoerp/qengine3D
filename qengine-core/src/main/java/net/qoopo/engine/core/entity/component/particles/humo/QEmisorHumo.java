/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.particles.humo;

import java.util.Random;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlanoBillboard;
import net.qoopo.engine.core.entity.component.particles.Particle;
import net.qoopo.engine.core.entity.component.particles.ParticleEmissor;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.procesador.AtlasSequentialTexture;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;

/**
 *
 * @author alberto
 */
public class QEmisorHumo extends ParticleEmissor {

//    private QMaterial material = null;
    int maximoLuces = 3;
    private Texture textura;
    float intencidadLuz = 0.12f;
    int actualLuz = 0;
    private Mesh geometria = new QPlanoBillboard(0.25f, 0.25f);
    private Random rnd = new Random();

//mantengo una lista de luces fijas y las voy a asignando a las nuevas particulas generadas
    //solo voy a crear las luces que corresponden al numero de particulas nuevas
    private void loadTexture() {
        try {
//            textura = new QTextura(ImgReader.leerImagen(new File("res/textures/humo/smoke_atlas_2.jpeg")), QTextura.TIPO_MAPA_DIFUSA);
//            textura = new QTextura(ImgReader.leerImagen(new File("res/textures/humo/smoke_atlas_1.png")), QTextura.TIPO_MAPA_DIFUSA);
            textura = AssetManager.get().loadTexture("humo", "assets/textures/humo/smoke_atlas_1.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AbstractMaterial crearMaterial() {
        Material material = null;
        try {

            material = new Material();
            AtlasSequentialTexture proc = new AtlasSequentialTexture(textura, 4, 4, 100);
            material.setColorMap(proc);
            material.setAlphaColour(QColor.BLACK);
            material.setTransparencia(true);
            material.setTransAlfa(0.90f);// el objeto tiene una transparencia
        } catch (Exception e) {
            e.printStackTrace();
        }
        return material;
    }

    public QEmisorHumo(AABB ambito, float tiempoVida, int maximoParticulas, int velocidadEmision) {
        super(ambito, tiempoVida, maximoParticulas, velocidadEmision, null);
        loadTexture();
    }

    private void agregarParticulas() {

        particulasNuevas.clear();

//        QLuz luz = null;
        if (actuales < maximoParticulas) {

            for (int i = 1; i < velocidadEmision; i++) {
                actuales++;

                Particle nueva = new Particle();

                Entity particula = new Entity("humo");
                particula.setBillboard(true);

                MaterialUtil.applyMaterial(geometria, crearMaterial());
                particula.addComponent(geometria);

//                QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.DINAMICO);
//                rigido.setFormaColision(new QColisionCaja(0.15f, 0.15f, 0.01f));
//
//                // para que funcione con el motor jbullet
////                rigido.setMasa(0.00000000000000000001f, QVector3.zero.clone());
//                rigido.setMasa(0.00000000000000000001f, QVector3.unitario_y.clone());
////                rigido.agregarFuerzas(QVector3.of(rnd.nextFloat() * 0.5f - 0.5f, rnd.nextFloat() * 0.5f - 0.5f, rnd.nextFloat() * 0.5f - 0.5f));
//                particula.agregarComponente(rigido);
                nueva.setTiempoVida(2 * tiempoVida / 3 + (rnd.nextFloat() * tiempoVida / 3));
                nueva.iniciarVida();

                //ubicacion inicial de la particula
                particula.move(
                        entity.getMatrizTransformacion(System.currentTimeMillis()).toTranslationVector().x + rnd.nextFloat() * (ambito.aabMaximo.location.x - ambito.aabMinimo.location.x) + ambito.aabMinimo.location.x,
                        entity.getMatrizTransformacion(System.currentTimeMillis()).toTranslationVector().y + ambito.aabMinimo.location.y,
                        entity.getMatrizTransformacion(System.currentTimeMillis()).toTranslationVector().z + rnd.nextFloat() * (ambito.aabMaximo.location.z - ambito.aabMinimo.location.z) + ambito.aabMinimo.location.z
                );
//                particula.rotar(0, (float) (rnd.nextFloat() * Math.toRadians(180)), 0);

                nueva.objeto = particula;
                this.particulasNuevas.add(nueva);
            }
            this.particulas.addAll(particulasNuevas);
        }
    }

    private void eliminarParticulas() {
        particulasEliminadas.clear();
        for (Particle particula : this.particulas) {

            //si ya paso su tiempo de vida o ya esta fuera del ambito en la altura
            if ((System.currentTimeMillis() - particula.getTiempoCreacion()) > particula.getTiempoVida()
                    || (particula.objeto.getTransform().getLocation().y > ambito.aabMaximo.location.y)) {
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
        float miniMov = -0.0000009f;
        float maxMov = 0.0000009f;
        QVector3 velocidad = QVector3.of(0, 0.005f, 0);
        for (Particle particula : this.particulas) {

            float delta = deltaTime / 1000.0f;
            float dx = velocidad.x * delta;
            float dy = velocidad.y * delta;
            float dz = velocidad.z * delta;
            QVector3 pos = particula.objeto.getTransform().getLocation();

            particula.objeto.move(pos.x + dx, pos.y + dy, pos.z + dz);

//            particula.objeto.
//            QObjetoRigido rigido = QUtilComponentes.getFisicoRigido(particula.objeto);
//            rigido.agregarFuerzas(QVector3.of(
//                    rnd.nextFloat() * (maxMov - miniMov) + miniMov,
//                    0.00009f, //siempre hacia arriba
//                    rnd.nextFloat() * (maxMov - miniMov) + miniMov
//            ));
            //modifico su tamaño para que vaya disminuyendo con el tiempo
            float d = 1.0f - (System.currentTimeMillis() - particula.getTiempoCreacion()) / particula.getTiempoVida();
            particula.objeto.scale(d);

            //modifico transparencia
            for (EntityComponent componente : particula.objeto.getComponents()) {
                if (componente instanceof Mesh) {
                    ((Material) ((Mesh) componente).primitiveList[0].material).setTransAlfa(d - 0.5f);//para que nunca sea 100 visible, se agrega mas transparencia al ser humo
                }
            }
        }
    }

    @Override
    public void emitir(long deltaTime) {

        //verificar si hay que agregar nuevos
        agregarParticulas();

        //verifica el tiempo de vida de cada particula para eliminarla si ya expiro
        eliminarParticulas();

        //modifica las particulas actuales
        modificarParticulas(deltaTime);
//        System.out.println("particulas actuales =" + actuales);
    }

    @Override
    public void destroy() {
        textura = null;
    }
}
