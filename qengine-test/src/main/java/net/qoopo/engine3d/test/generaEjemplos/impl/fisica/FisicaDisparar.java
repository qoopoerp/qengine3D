/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.fisica;

import java.awt.Color;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QEsfera;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionEsfera;
import net.qoopo.engine.core.entity.component.physics.collision.listeners.QListenerColision;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.QUtilComponentes;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class FisicaDisparar extends MakeTestScene {

    private static QMaterialBas materialBalas;
    private static QMaterialBas materialBombas;

    @Override
    public void make(Scene mundo) {
        this.scene = mundo;
        materialBalas = new QMaterialBas("bala");
        materialBalas.setColorBase(new QColor(Color.BLUE));
        materialBombas = new QMaterialBas("bomba");
        materialBombas.setColorBase(QColor.YELLOW);
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
                nuevoBalon.addComponent(QMaterialUtil.aplicarMaterial(new QEsfera(0.25f), materialBalas));
                colision = new QColisionEsfera(0.25f);
                nuevoBalon.addComponent(colision);
                balon3Fisica = new QObjetoRigido(QObjetoDinamico.DINAMICO);
                balon3Fisica.setFormaColision(colision);
                balon3Fisica.setMasa(2f, QVector3.zero.clone());

                // nuevaFuerza = QUnidadMedida.velocidad(QVector3.of(0, 10, 0));
                nuevaFuerza = render.getCamara().getDirection().clone().multiply(-1).normalize().multiply(50);

                balon3Fisica.agregarFuerzas(nuevaFuerza);

                nuevoBalon.addComponent(balon3Fisica);
                nuevoBalon.move(render.getCamara().getTransformacion().getTraslacion().x,
                        render.getCamara().getTransformacion().getTraslacion().y,
                        render.getCamara().getTransformacion().getTraslacion().z);
                scene.addEntity(nuevoBalon);

                break;
            case 2:

                // se agrega una pelota al mundo la cual esta impulsada en direccion de la
                // camara, peero ademas tiene un
                // efecto de bomba
                // Balon 3
                nuevoBalon = new Entity();
                nuevoBalon.addComponent(QMaterialUtil.aplicarMaterial(new QEsfera(0.25f), materialBombas));
                colision = new QColisionEsfera(0.25f);
                nuevoBalon.addComponent(colision);
                balon3Fisica = new QObjetoRigido(QObjetoDinamico.DINAMICO);
                balon3Fisica.setFormaColision(colision);
                balon3Fisica.setMasa(1f, QVector3.zero.clone());

                // nuevaFuerza = QUnidadMedida.velocidad(QVector3.of(0, 10, 0));
                nuevaFuerza = render.getCamara().getDirection().clone().multiply(-1).normalize().multiply(50);

                balon3Fisica.agregarFuerzas(nuevaFuerza);

                nuevoBalon.addComponent(balon3Fisica);
                nuevoBalon.move(render.getCamara().getTransformacion().getTraslacion().x,
                        render.getCamara().getTransformacion().getTraslacion().y,
                        render.getCamara().getTransformacion().getTraslacion().z);

                nuevoBalon.addComponent(new QListenerColision() {
                    @Override
                    public void colision(Entity ob1, Entity ob2) {
                        System.out.println("HAY COLISION !!!!  se hará una explosión");
                        // agrega una fuerza de empuje simulando una explosión al objeto ocn el que
                        // golpea

                        // obtengo la posicion de las 2 entidades y luego agrego un impulso con un
                        // vector igual a la diferencia del 2 con el primero
                        QVector3 posA = ob1.getMatrizTransformacion(QGlobal.tiempo).toTranslationVector();
                        QVector3 posB = ob2.getMatrizTransformacion(QGlobal.tiempo).toTranslationVector();
                        QVector3 direccion = posB.add(posA.multiply(-1));
                        direccion.normalize();
                        QObjetoRigido rig = QUtilComponentes.getFisicoRigido(ob2);
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
