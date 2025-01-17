/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.fisica;

import java.io.File;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.entity.component.mesh.util.QUnidadMedida;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCaja;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionEsfera;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.util.MaterialUtil;

/**
 *
 * @author alberto
 */
public class PhysicsTest1 extends FisicaDisparar {

    private float largoLadrillo = 0.48f;
    private float anchoLadrillo = 0.24f;
    private float altoLadrillo = 0.12f;

    private static Material materialLadrillo;
    private static Material materialBalas;
    private static Material materialBombas;

    @Override
    public void make(Scene mundo) {
        super.make(mundo);

        materialBalas = new Material("bala");
        materialBalas.setColor(QColor.BLUE);
        materialBombas = new Material("bomba");
        materialBombas.setColor(QColor.YELLOW);

        try {
            mundo.addEntity(AssetManager.get()
                    .loadModel(new File("assets/models/obj/escenarios/testLevel1.obj")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // crearEsferas(mundo);
        // construirMuro(mundo);
    }

    private void crearEsferas(Scene mundo) {
        // Balon 1
        Entity balon = new Entity("Esfera1");// Entity("Esfera 0.05");
        balon.addComponent(new Sphere(4));
        QColisionEsfera colision = new QColisionEsfera(4);
        balon.addComponent(colision);
        QObjetoRigido fisicaBalon = new QObjetoRigido(QObjetoDinamico.DINAMICO);
        fisicaBalon.setMasa(16f, QVector3.zero.clone());
        fisicaBalon.setFormaColision(colision);
        balon.addComponent(fisicaBalon);
        balon.move(mundo.UM.convertirPixel(0), mundo.UM.convertirPixel(15f), mundo.UM.convertirPixel(-5));

        mundo.addEntity(balon);

        // Balon 2
        Entity balon2 = new Entity("Esfera2");// Entity("Esfera 0.08");
        QColisionEsfera colision2 = new QColisionEsfera(1);
        balon2.addComponent(colision2);
        balon2.addComponent(new Sphere(1));
        QObjetoRigido balon2Fisica = new QObjetoRigido(QObjetoDinamico.DINAMICO);
        balon2Fisica.setFormaColision(colision2);
        balon2Fisica.setMasa(1f, QVector3.zero.clone());
        balon2.addComponent(balon2Fisica);
        balon2.move(mundo.UM.convertirPixel(3), mundo.UM.convertirPixel(8f), mundo.UM.convertirPixel(-5));
        mundo.addEntity(balon2);

        // Balon 3
        Entity balon3 = new Entity("Esfera2");// Entity("Esfera 0.01");

        balon3.addComponent(new Sphere(2));
        QColisionEsfera colision3 = new QColisionEsfera(2);
        balon3.addComponent(colision3);
        QObjetoRigido balon3Fisica = new QObjetoRigido(QObjetoDinamico.DINAMICO);
        balon3Fisica.setFormaColision(colision3);
        balon3Fisica.setMasa(2f, QVector3.zero.clone());
        balon3.addComponent(balon3Fisica);
        balon3.move(mundo.UM.convertirPixel(1.5f), mundo.UM.convertirPixel(12f), mundo.UM.convertirPixel(0));
        mundo.addEntity(balon3);
    }

    private void construirMuro(Scene mundo) {
        largoLadrillo = mundo.UM.convertirPixel(50, QUnidadMedida.CENTIMETRO);
        anchoLadrillo = mundo.UM.convertirPixel(50, QUnidadMedida.CENTIMETRO);
        altoLadrillo = mundo.UM.convertirPixel(50, QUnidadMedida.CENTIMETRO);

        materialLadrillo = null;
        try {
            materialLadrillo = new Material(
                    new Texture(ImageIO.read(new File("assets/textures/basicas/muro/ladrillo_1.jpg"))),
                    64);
            // materialLadrillo.alpha = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        float inicio = anchoLadrillo / 4;
        float alto = -altoLadrillo / 2;
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 16; i++) {
                QVector3 vt = QVector3.of(i * anchoLadrillo + inicio - 7, altoLadrillo + alto, 0);
                hacerLadrillo(vt, mundo);
            }
            inicio = -inicio;
            alto += altoLadrillo;
        }
    }

    private void hacerLadrillo(QVector3 loc, Scene mundo) {
        Entity bloque = new Entity("Bloque");
        bloque.move(loc);
        Box geometria = new Box(altoLadrillo, anchoLadrillo, largoLadrillo);
        geometria.setMaterial(materialLadrillo);
        MaterialUtil.applyMaterial(geometria, materialLadrillo);
        QColisionCaja formaColision = new QColisionCaja(anchoLadrillo, altoLadrillo, largoLadrillo);
        bloque.addComponent(formaColision);
        QObjetoRigido bloquefisica = new QObjetoRigido(QObjetoDinamico.DINAMICO);
        bloquefisica.setMasa(1f, QVector3.zero.clone());
        bloquefisica.setFormaColision(formaColision);
        bloque.addComponent(bloquefisica);
        bloque.addComponent(geometria);
        mundo.addEntity(bloque);
    }

}
