/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.shadows;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.assets.model.waveobject.LoadModelObj;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCaja;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QEsfera;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlano;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.QUtilComponentes;
import net.qoopo.engine.core.util.mesh.QUtilNormales;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 *
 * @author alberto
 */
public class SombrasOmniDireccional2 extends MakeTestScene {

        public void make(Scene mundo) {
                this.scene = mundo;

                // luces
                Entity luzCentral = new Entity("Luz_central");
                luzCentral.move(0, 2f, 0);
                QPointLigth luz = new QPointLigth(50f, QColor.YELLOW, Float.POSITIVE_INFINITY, true, true);
                luzCentral.addComponent(luz);
                mundo.addEntity(luzCentral);

                try {
                        Entity calabaza = new Entity("Jack");
                        calabaza.addComponent(
                                        QUtilComponentes.getMesh(AssetManager.get().loadModel(new File("assets/"
                                                        + "models/obj/VARIOS/CabezaHallowenCalabaza/Cabeza2.obj"))));
                        calabaza.scale(2, 2, 2);
                        // calabaza.mover(2f, -2f, -2f);
                        // calabaza.rotar(0, Math.toRadians(180), 0);
                        mundo.addEntity(calabaza);
                } catch (Exception e) {
                }

                Entity ojo1 = new Entity("esfera");
                ojo1.move(-7, 15, -25);
                ojo1.addComponent(new QEsfera());
                mundo.addEntity(ojo1);

                Entity ojo2 = new Entity("esfera");
                ojo2.move(7, 15, -25);
                ojo2.addComponent(new QEsfera());
                mundo.addEntity(ojo2);

                Entity piso = new Entity("piso");
                // piso.mover(0, -1, 0);
                piso.rotate(Math.toRadians(90), 0, 0);
                // piso.agregarComponente(new QCaja(0.1f, 10f, 10f));
                piso.addComponent(new QPlano(150, 150));
                mundo.addEntity(piso);

                Entity pared1 = new Entity("Caja");
                // pared1.mover(0, -1, 0);
                pared1.addComponent(QUtilNormales.invertirNormales(new QCaja(100f, 100f, 100f)));
                mundo.addEntity(pared1);

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
