/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.particles.fire;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlanoBillboard;
import net.qoopo.engine.core.entity.component.particles.Particle;
import net.qoopo.engine.core.entity.component.particles.ParticleEmissor;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCaja;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.procesador.AtlasSequentialTexture;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;

/**
 *
 * @author alberto
 */
public class FireEmissor extends ParticleEmissor {

//    private QMaterialBas material = null;
    int maximoLuces = 3;
    private Texture textura;
    float intencidadLuz = 0.12f;
    int actualLuz = 0;
    private final List<QLigth> luces = new ArrayList<>();
//    private QGeometria geometria = new QPlanoBillboard(0.05f, 0.05f);
    private final Mesh geometria = new QPlanoBillboard(0.15f, 0.15f);
    private final Random rnd = new Random();
    private boolean agregarLuces = false;
//mantengo una lista de luces fijas y las voy a asignando a las nuevas particulas generadas
    //solo voy a crear las luces que corresponden al numero de particulas nuevas

    private void loadTexture() {
        try {
            //new QMaterialBas(new QTextura(ImgReader.leerImagen(new File("assets/"+ "textures/fuego/fuego_esfera1.png")), QTextura.TIPO_MAPA_DIFUSA)
//                    new QTextura(ImgReader.leerImagen(new File("assets/"+ "textures/fuego/texture_atlas.png")), QTextura.TIPO_MAPA_DIFUSA),
//            textura = new QTextura(ImgReader.leerImagen(new File("assets/"+ "textures/fuego/fire-texture-atlas.png")), QTextura.TIPO_MAPA_DIFUSA);
            textura = AssetManager.get().loadTexture("texFuego", "assets/textures/fuego/fire-texture-atlas_2.png");
//            textura = new QTextura(ImgReader.leerImagen(new File("assets/"+ "textures/fuego/texture_atlas.png")), QTextura.TIPO_MAPA_DIFUSA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Material crearMaterial() {
        Material material = null;
        try {

            material = new Material();
            AtlasSequentialTexture proc = new AtlasSequentialTexture(textura, 8, 8, 10);
            material.setColorMap(proc);
            material.setAlphaColour(QColor.BLACK);
            material.setTransparent(true);
//            material.setTransAlfa(0.90f);// el objeto tiene una transparencia 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return material;
    }

    public FireEmissor(AABB ambito, float tiempoVida, int maximoParticulas, int velocidadEmision, Vector3 direccion, boolean agregarLuces) {
        super(ambito, tiempoVida, maximoParticulas, velocidadEmision, direccion);
        this.agregarLuces = agregarLuces;
//        crearMaterial();
        loadTexture();
    }

    private void agregarParticulas() {

        particulasNuevas.clear();

//        QLuz luz = null;
        if (actuales < maximoParticulas) {

            for (int i = 1; i < velocidadEmision; i++) {
                actuales++;

                Particle nueva = new Particle();

                Entity particula = new Entity("flama");
                particula.setBillboard(true);
                MaterialUtil.applyMaterial(geometria, crearMaterial());
                particula.addComponent(geometria);

                QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.DINAMICO);
                rigido.setFormaColision(new QColisionCaja(0.15f, 0.15f, 0.01f));
                rigido.setMasa(-0.001f, Vector3.zero.clone());
                particula.addComponent(rigido);

                nueva.setTiempoVida(2 * tiempoVida / 3 + (rnd.nextFloat() * tiempoVida / 3));
                nueva.iniciarVida();

                //ubicacion inicial de la particula
                particula.move(
                        entity.getMatrizTransformacion(System.currentTimeMillis()).toTranslationVector().x + rnd.nextFloat() * (ambito.max.x - ambito.min.x) + ambito.min.x,
                        entity.getMatrizTransformacion(System.currentTimeMillis()).toTranslationVector().y + ambito.min.y,
                        entity.getMatrizTransformacion(System.currentTimeMillis()).toTranslationVector().z + rnd.nextFloat() * (ambito.max.z - ambito.min.z) + ambito.min.z
                );
//                particula.rotar(0, (float) (rnd.nextFloat() * Math.toRadians(180)), 0);

                if (agregarLuces) {
                    if (maximoLuces > 0) {
                        if (luces.size() < maximoLuces) {
                            QLigth luz = new QPointLigth(intencidadLuz, QColor.YELLOW, 20, false, false);
                            particula.addComponent(luz);
                            luces.add(luz);
                        } else {
                            particula.addComponent(luces.get(actualLuz));
                            actualLuz++;
                            if (actualLuz >= maximoLuces) {
                                actualLuz = 0;
                            }
                        }
                    }
                }
                nueva.objeto = particula;
//                this.entity.agregarHijo(particula);
                this.particulasNuevas.add(nueva);
//            nueva
            }
            this.particulas.addAll(particulasNuevas);
        }
    }

    private void eliminarParticulas() {
        particulasEliminadas.clear();
        for (Particle particula : this.particulas) {
            //si ya paso su tiempo de vida o ya esta fuera del ambito en la altura
            if ((System.currentTimeMillis() - particula.getTiempoCreacion()) > particula.getTiempoVida()
                    || (particula.objeto.getTransform().getLocation().y > ambito.max.y)) {
                particulasEliminadas.add(particula);
                actuales--;
                particula.objeto.setToRender(false);
                particula.objeto.setToDelete(true);
                //recorro los hijos para apagar las luces de los eliminados
//                for (Qentity hijo : copo.objeto.hijos) {
//                    if (hijo instanceof QLuz) {
//                        ((QLuz) hijo).setEnable(false);
//                    }
//                }
            }
        }
        if (actuales < 0) {
            actuales = 0;
        }
        this.particulas.removeAll(particulasEliminadas);
    }

    private void modificarParticulas() {
        //la modificacion de las particulas de la nieva se basa en un zigzagueo hacia abajo
        float miniMov = -0.009f;
        float maxMov = 0.009f;
        float dx = 1;
        float dz = 1;
        for (Particle particula : this.particulas) {
            //se agrega un impulso
            for (EntityComponent componente : particula.objeto.getComponents()) {
                if (componente instanceof QObjetoRigido) {
                    //el deltax y delta z es para acercarlo al centro, multiplico por -1 para ir en el sentido contrario
                    dx = particula.objeto.getTransform().getLocation().x / Math.abs(particula.objeto.getTransform().getLocation().x) * -1;
                    dz = particula.objeto.getTransform().getLocation().z / Math.abs(particula.objeto.getTransform().getLocation().z) * -1;

                    ((QObjetoRigido) componente).agregarFuerzas(Vector3.of(
                            dx * rnd.nextFloat() * (maxMov - miniMov) + miniMov,
                            0,
                            dz * rnd.nextFloat() * (maxMov - miniMov) + miniMov));
                }
            }
            //modifico su tamaño para que vaya disminuyendo con el tiempo
            float d = 1.0f - (System.currentTimeMillis() - particula.getTiempoCreacion()) / particula.getTiempoVida();
            particula.objeto.scale(d);
        }
    }

    @Override
    public void emitir(long deltaTime) {
        //verificar si hay que agregar nuevos
        agregarParticulas();
        //verifica el tiempo de vida de cada particula para eliminarla si ya expiro
        eliminarParticulas();
        //modifica las particulas actuales
        modificarParticulas();
//        System.out.println("particulas actuales =" + actuales);
    }

    @Override
    public void destroy() {
        super.destroy();
        luces.clear();
    }
}
