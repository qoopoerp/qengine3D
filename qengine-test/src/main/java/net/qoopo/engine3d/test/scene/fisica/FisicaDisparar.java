/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.fisica;

import java.awt.Color;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionEsfera;
import net.qoopo.engine.core.entity.component.physics.collision.listeners.CollisionListener;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine3d.test.scene.MakeTestScene;
import net.qoopo.engine.core.util.ComponentUtil;

/**
 *
 * @author alberto
 */
public class FisicaDisparar extends MakeTestScene {

    private static Material materialBalas;
    private static Material materialBombas;

    @Override
    public void make(Scene mundo) {
        this.scene = mundo;
        materialBalas = new Material("bala");
        materialBalas.setColor(new QColor(Color.BLUE));
        materialBombas = new Material("bomba");
        materialBombas.setColor(QColor.YELLOW);
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
        Entity nuevoBalon;
        QVector3 nuevaFuerza;
        QObjetoRigido balon3Fisica;
        CollisionShape colision;
        switch (numAccion) {
            case 1:

                // se agrega una pelota al mundo la cual esta impulsada en direccion de la
                // camara
                // Balon 3
                nuevoBalon = new Entity();
                nuevoBalon.addComponent(MaterialUtil.applyMaterial(new Sphere(0.25f), materialBalas));
                colision = new QColisionEsfera(0.25f);
                nuevoBalon.addComponent(colision);
                balon3Fisica = new QObjetoRigido(QObjetoDinamico.DINAMICO);
                balon3Fisica.setFormaColision(colision);
                balon3Fisica.setMasa(2f, QVector3.zero.clone());

                // nuevaFuerza = QUnidadMedida.velocidad(QVector3.of(0, 10, 0));
                nuevaFuerza = render.getCamera().getDirection().clone().multiply(-1).normalize().multiply(50);

                balon3Fisica.agregarFuerzas(nuevaFuerza);

                nuevoBalon.addComponent(balon3Fisica);
                nuevoBalon.move(render.getCamera().getTransform().getLocation().x,
                        render.getCamera().getTransform().getLocation().y,
                        render.getCamera().getTransform().getLocation().z);
                scene.addEntity(nuevoBalon);

                break;
            case 2:

                // se agrega una pelota al mundo la cual esta impulsada en direccion de la
                // camara, peero ademas tiene un
                // efecto de bomba
                // Balon 3
                nuevoBalon = new Entity();
                nuevoBalon.addComponent(MaterialUtil.applyMaterial(new Sphere(0.25f), materialBombas));
                colision = new QColisionEsfera(0.25f);
                nuevoBalon.addComponent(colision);
                balon3Fisica = new QObjetoRigido(QObjetoDinamico.DINAMICO);
                balon3Fisica.setFormaColision(colision);
                balon3Fisica.setMasa(1f, QVector3.zero.clone());

                // nuevaFuerza = QUnidadMedida.velocidad(QVector3.of(0, 10, 0));
                nuevaFuerza = render.getCamera().getDirection().clone().multiply(-1).normalize().multiply(50);

                balon3Fisica.agregarFuerzas(nuevaFuerza);

                nuevoBalon.addComponent(balon3Fisica);
                nuevoBalon.move(render.getCamera().getTransform().getLocation().x,
                        render.getCamera().getTransform().getLocation().y,
                        render.getCamera().getTransform().getLocation().z);

                nuevoBalon.addComponent(new CollisionListener() {
                    @Override
                    public void colision(Entity ob1, Entity ob2) {
                        System.out.println("HAY COLISION !!!!  se hará una explosión");
                        // agrega una fuerza de empuje simulando una explosión al objeto ocn el que
                        // golpea

                        // obtengo la posicion de las 2 entidades y luego agrego un impulso con un
                        // vector igual a la diferencia del 2 con el primero
                        QVector3 posA = ob1.getMatrizTransformacion(QGlobal.time).toTranslationVector();
                        QVector3 posB = ob2.getMatrizTransformacion(QGlobal.time).toTranslationVector();
                        QVector3 direccion = posB.add(posA.multiply(-1));
                        direccion.normalize();
                        QObjetoRigido rig = (QObjetoRigido) ComponentUtil.getComponent(ob2, QObjetoRigido.class);
                        rig.agregarFuerzas(direccion.multiply(30));

                        // busca todas las entidades cercanas para aplicarle la fuerza de colision
                        // QVector3 posA =
                        // ob1.getMatrizTransformacion(QGlobal.tiempo).toTranslationVector();
                        // for (Entity ent : QEscena.INSTANCIA.getListaEntidades()) {
                        // if (!ent.getNombre().equals(ob1.getNombre())) {
                        // QVector3 posB =
                        // ent.getMatrizTransformacion(QGlobal.tiempo).toTranslationVector();
                        // QVector3 direccion = posB.add(posA.multiply(-1));/ta
                        // if (direccion.length() < 5)// en un radio definido
                        // {
                        // QObjetoRigido rig = QUtilComponentes.getFisicoRigido(ent);
                        // if (rig != null) {
                        //// direccion.normalize();
                        // rig.agregarFuerzas(direccion.normalize().multiply(20));
                        // }
                        // }
                        // }
                        // }

                        // elimino a la entidad bala
                        ob1.setToDelete(true);
                    }
                });

                scene.addEntity(nuevoBalon);

                break;

        }
    }

}
