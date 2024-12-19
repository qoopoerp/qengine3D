/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.juegotest.generadores;

import java.io.File;

import javax.imageio.ImageIO;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlano;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.QMaterialUtil;

/**
 *
 * @author alberto
 */
public class GenMonitores {

    public static Entity crearMonitorTipo1(QTextura textura) {
        Entity monitor = new Entity("Monitor");
        QTextura textMonitor = null;
        QMaterialBas materialCarcasa = new QMaterialBas();
        // materialCarcasa.setColorBase(new QColor(1, 0.5f, 0.5f, 0.5f));
        materialCarcasa.setColorBase(QColor.YELLOW);
        // try {
        // textMonitor = new QTextura(
        // ImageIO.read(new
        // File("assets/textures/basicas/varias/Monitor-300x191.gif")));
        // materialCarcasa.setMapaColor(new QProcesadorSimple(textMonitor));
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        monitor.addComponent(
                QMaterialUtil.aplicarMaterial(new QPlano(3 * 600 / 800 + 0.2f, 3 + 0.1f), materialCarcasa));

        Entity pantalla = new Entity("pantalla");
        QMaterialBas pantallMat = new QMaterialBas();
        pantallMat.setMapaColor(new QProcesadorSimple(textura));
        // pantallMat.setColorBase(new QColor(1, 0.2f, 0.2f, 0.2f));
        pantallMat.setColorBase(QColor.BROWN);
        pantalla.addComponent(QMaterialUtil.aplicarMaterial(new QPlano(3 * 600 / 800, 3), pantallMat));
        pantalla.move(0, 0.02f, 0);
        monitor.addChild(pantalla);

        return monitor;
    }
}
