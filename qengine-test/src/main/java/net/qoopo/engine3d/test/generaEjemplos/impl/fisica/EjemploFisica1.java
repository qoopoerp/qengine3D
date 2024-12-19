/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.fisica;

import java.io.File;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCaja;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QEsfera;
import net.qoopo.engine.core.entity.component.mesh.util.QUnidadMedida;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCaja;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionEsfera;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.util.QMaterialUtil;

/**
 *
 * @author alberto
 */
public class EjemploFisica1 extends FisicaDisparar {

    private float largoLadrillo = 0.48f;
    private float anchoLadrillo = 0.24f;
    private float altoLadrillo = 0.12f;

    private static QMaterialBas materialLadrillo;
    private static QMaterialBas materialBalas;
    private static QMaterialBas materialBombas;

    @Override
    public void make(Scene mundo) {
        super.make(mundo);

        materialBalas = new QMaterialBas("bala");
        materialBalas.setColorBase(QColor.BLUE);
        materialBombas = new QMaterialBas("bomba");
        materialBombas.setColorBase(QColor.YELLOW);

        // //el mundo por default esta con unidades de medida en metro
        // //al non usar el conversor d eunidades de media, se toma como metros
        // //Piso
        // Entity piso = new Entity("piso");
        //// piso.transformacion.getTraslacion().y = 0f;
        // piso.mover(0, 0, 0);
        // piso.escalar(10f, 10f, 10f);
        //
        // piso.agregarComponente(new QCaja(0.1f, mundo.UM.convertirPixel(50),
        // mundo.UM.convertirPixel(50)));
        //
        // QColisionCaja colision = new QColisionCaja(mundo.UM.convertirPixel(50), 0.1f,
        // mundo.UM.convertirPixel(50));
        // piso.agregarComponente(colision);
        //
        // QObjetoRigido pisoRigidez = new QObjetoRigido(QObjetoDinamico.ESTATICO);
        // pisoRigidez.setMasa(0, QVector3.zero.clone());
        // pisoRigidez.setFormaColision(colision);
        //
        // piso.agregarComponente(pisoRigidez);
        // mundo.addEntity(piso);

        // carga.setAccionFinal(accionFinal);
        // // carga.setProgreso(barraProgreso);
        // carga.cargar(new File("assets/"+
        // "models/obj/ESCENARIOS/escenarioQEngine.obj"));

        try {
            mundo.addEntity(AssetManager.get()
                    .loadModel(new File("assets/models/obj/ESCENARIOS/escenarioQEngine.obj")));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        crearEsferas(mundo);
        construirMuro(mundo);
    }

    private void crearEsferas(Scene mundo) {
        // Balon 1
        Entity balon = new Entity("Esfera1");// Entity("Esfera 0.05");
        balon.addComponent(new QEsfera(4));
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
        balon2.addComponent(new QEsfera(1));
        QObjetoRigido balon2Fisica = new QObjetoRigido(QObjetoDinamico.DINAMICO);
        balon2Fisica.setFormaColision(colision2);
        balon2Fisica.setMasa(1f, QVector3.zero.clone());
        balon2.addComponent(balon2Fisica);
        balon2.move(mundo.UM.convertirPixel(3), mundo.UM.convertirPixel(8f), mundo.UM.convertirPixel(-5));
        mundo.addEntity(balon2);

        // Balon 3
        Entity balon3 = new Entity("Esfera2");// Entity("Esfera 0.01");

        balon3.addComponent(new QEsfera(2));
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

        materialLadrillo = new QMaterialBas();

        materialLadrillo = null;
        try {
            materialLadrillo = new QMaterialBas(
                    new QTextura(ImageIO.read(new File("assets/textures/basicas/muro/ladrillo_1.jpg"))),
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
        QCaja geometria = new QCaja(altoLadrillo, anchoLadrillo, largoLadrillo);
        geometria.setMaterial(materialLadrillo);
        QMaterialUtil.aplicarMaterial(geometria, materialLadrillo);
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
