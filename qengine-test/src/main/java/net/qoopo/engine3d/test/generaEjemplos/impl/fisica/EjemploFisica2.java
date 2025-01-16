/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.fisica;

import java.io.File;

import javax.imageio.ImageIO;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.entity.component.mesh.util.QUnidadMedida;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCaja;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;

/**
 *
 * @author alberto
 */
public class EjemploFisica2 extends FisicaDisparar {

    private float largoLadrillo = 0.48f;
    private float anchoLadrillo = 0.24f;
    private float altoLadrillo = 0.12f;

    private static Material materialLadrillo;

    @Override
    public void make(Scene mundo) {
        super.make(mundo);
        // el mundo por default esta con unidades de medida en metro
        // al non usar el conversor d eunidades de media, se toma como metros
        // Piso
        Entity piso = new Entity("piso");
        piso.getTransform().getLocation().y = 0f;
        piso.move(0, 0, 0);

        piso.addComponent(new Box(0.1f, mundo.UM.convertirPixel(50), mundo.UM.convertirPixel(50)));

        QColisionCaja colision = new QColisionCaja(mundo.UM.convertirPixel(50), 0.1f, mundo.UM.convertirPixel(50));
        piso.addComponent(colision);

        QObjetoRigido pisoRigidez = new QObjetoRigido(QObjetoDinamico.ESTATICO);
        pisoRigidez.setMasa(0, QVector3.zero.clone());
        pisoRigidez.setFormaColision(colision);

        piso.addComponent(pisoRigidez);

        mundo.addEntity(piso);

        // crearEsferas(mundo);
        construirMuro(mundo);
    }

    private void construirMuro(Scene mundo) {
        largoLadrillo = mundo.UM.convertirPixel(50, QUnidadMedida.CENTIMETRO);
        anchoLadrillo = mundo.UM.convertirPixel(50, QUnidadMedida.CENTIMETRO);
        altoLadrillo = mundo.UM.convertirPixel(50, QUnidadMedida.CENTIMETRO);

        materialLadrillo = new Material();

        materialLadrillo = null;
        try {
            materialLadrillo = new Material(
                    new Texture(ImageIO.read(new File("assets/textures/muro/ladrillo_1.jpg"))), 64);
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
        Entity bloque = new Entity();
        bloque.move(loc);
        Mesh geometria = new Box(altoLadrillo, anchoLadrillo, largoLadrillo);
        MaterialUtil.applyMaterial(geometria, materialLadrillo);
        QColisionCaja formaColision = new QColisionCaja(anchoLadrillo, altoLadrillo, largoLadrillo);
        QObjetoRigido bloquefisica = new QObjetoRigido(QObjetoDinamico.DINAMICO);
        bloquefisica.setMasa(4f, QVector3.zero.clone());
        bloquefisica.setFormaColision(formaColision);
        bloque.addComponent(bloquefisica);
        bloque.addComponent(formaColision);
        bloque.addComponent(geometria);
        mundo.addEntity(bloque);
    }

}
