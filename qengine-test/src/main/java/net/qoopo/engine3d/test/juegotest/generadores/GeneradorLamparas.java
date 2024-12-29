/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.juegotest.generadores;

import java.io.File;
import java.io.FileNotFoundException;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Cylinder;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Cone;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;

/**
 * Clase para crear personajes del juego
 *
 * @author alberto
 */
public class GeneradorLamparas {

        // private Entity lampara;
        // private Entity cono;
        // private Entity tronco;
        // private Entity base;
        private static int secciones = 10;

        public boolean shift = false;

        public static Entity crearLamparaVelador() {

                Entity lampara = new Entity("Lampara");

                Entity cono = new Entity("cono");
                cono.move(0, 0.35f, 0);// a metro y medio de altura
                cono.addComponent(MaterialUtil.applyColor(new Cone(0.15f, 0.15f, secciones), 0.7f, 1f, 1f, 0, 1, 1,
                                1, 1, 64));
                cono.addComponent(new QPointLigth(.5f, QColor.YELLOW, 5f, false, false));
                // cono.agregarComponente(new QLuz(QLuz.LUZ_PUNTUAL, .5f, 255, 255, 255, true));

                Entity tronco = new Entity("tronco");
                tronco.move(0, 0.15f, 0);
                tronco.addComponent(new Cylinder(0.35f, 0.01f, secciones));

                Entity base = new Entity("Base");
                base.addComponent(new Cylinder(0.02f, 0.15f, secciones));

                lampara.addChild(cono);
                lampara.addChild(tronco);
                lampara.addChild(base);

                return lampara;
        }

        public static Entity crearLinterna() {

                Entity lampara = new Entity("Linterna");

                Entity cono = new Entity("li-cono");
                cono.move(0, 0.35f, 0);
                // cono.agregarComponente(QMaterialUtil.aplicarColor(new QCono(0.15f, 0.05f,
                // secciones), 1f, QColor.YELLOW, QColor.WHITE, 1, 64));
                cono.addComponent(MaterialUtil.applyColor(new Sphere(0.03f, 8), 1f, QColor.YELLOW, QColor.WHITE, 1,
                                64));
                cono.addComponent(new QSpotLigth(2.5f, QColor.YELLOW, 20f, QVector3.of(0, 0, -1),
                                (float) Math.toRadians(30f), (float) Math.toRadians(25f), false, false));
                // cono.agregarComponente(new QLuzPuntual(2.5f, QColor.YELLOW, true, 20));

                // cono.agregarComponente(new QLuzPuntual(2.5f, QColor.YELLOW, true, 20f));
                Entity tronco = new Entity("li-tronco");
                tronco.move(0, 0.15f, 0);
                tronco.addComponent(new Cylinder(0.35f, 0.01f, 8));

                lampara.addChild(cono);
                lampara.addChild(tronco);

                return lampara;
        }

        public static Entity crearLamparaPiso2() {
                Entity lampara = new Entity("Lampara");
                Entity cono = new Entity("cono");
                cono.move(0, 3f, 0);

                cono.addComponent(new QPointLigth(1f, QColor.YELLOW, 10, false, false));

                lampara.addChild(cono);
                Entity cuerpo;
                try {
                        cuerpo = AssetManager.get().loadModel(new File("assets/"
                                        + "models/obj/ARQUITECTURA/baja_calidad/lampara/lamp.obj"));

                        cuerpo.scale(0.3f, 0.3f, 0.3f);

                        lampara.addChild(cuerpo);
                        // se agrga la animacino a la entidad
                        // personaje.agregarComponente(animacion);
                } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                return lampara;
        }

        public static Entity crearLamparaPiso() {
                Entity lampara = new Entity("Lampara");

                Entity cono = new Entity("l-foco");
                cono.move(0, 2.25f, 0);

                // cono.agregarComponente(QNormales.invertirNormales(QMaterialUtil.aplicarColor(new
                // Sphere(0.25f, 8), 0.7f, 1f, 1f, 0, 1, 1, 1, 64)));
                // cono.agregarComponente(QMaterialUtil.aplicarColor(new Sphere(0.5f, 8),
                // 0.15f, QColor.YELLOW, QColor.YELLOW, 1, 1000000));//el efecto aura
                cono.addComponent(MaterialUtil.applyColor(new Sphere(0.25f, 8), 1f, QColor.YELLOW, QColor.WHITE, 1,
                                64));// el real
                cono.addComponent(new QPointLigth(2.5f, QColor.YELLOW, 30, false, false));
                // cono.agregarComponente(new QLuz(QLuz.LUZ_PUNTUAL, .5f, 255, 255, 255, true));

                Entity tronco = new Entity("l-faro");
                tronco.move(0, 1f, 0);
                tronco.addComponent(new Cylinder(2f, 0.01f, 8));

                Entity base = new Entity("l-base");
                base.addComponent(new Cylinder(0.02f, 0.15f, secciones));

                lampara.addChild(cono);
                lampara.addChild(tronco);
                lampara.addChild(base);

                // se agrga la animacino a la entidad
                // personaje.agregarComponente(animacion);
                return lampara;
        }

        public static Entity crearLamparaPisoMonstruos() {
                Entity lampara = new Entity("Lampara");

                Entity cono = new Entity("l-foco");
                cono.move(0, 2.25f, 0);

                // cono.agregarComponente(QNormales.invertirNormales(QMaterialUtil.aplicarColor(new
                // Sphere(0.25f, 8), 0.7f, 1f, 1f, 0, 1, 1, 1, 64)));
                // cono.agregarComponente(QMaterialUtil.aplicarColor(new Sphere(0.5f, 8),
                // 0.15f, QColor.YELLOW, QColor.YELLOW, 1, 1000000));//el efecto aura
                cono.addComponent(MaterialUtil.applyColor(new Sphere(0.25f, 8), 1f, QColor.YELLOW, QColor.WHITE, 1,
                                64));// el real
                cono.addComponent(new QPointLigth(100f, QColor.YELLOW, 1000, false, false));
                // cono.agregarComponente(new QLuz(QLuz.LUZ_PUNTUAL, .5f, 255, 255, 255, true));

                Entity tronco = new Entity("l-faro");
                tronco.move(0, 1f, 0);
                tronco.addComponent(new Cylinder(2f, 0.01f, 8));

                Entity base = new Entity("l-base");
                base.addComponent(new Cylinder(0.02f, 0.15f, secciones));

                lampara.addChild(cono);
                lampara.addChild(tronco);
                lampara.addChild(base);

                // se agrga la animacino a la entidad
                // personaje.agregarComponente(animacion);
                return lampara;
        }

}
