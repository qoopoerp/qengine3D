/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.juegotest.generadores;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Plane;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.util.MaterialUtil;

/**
 *
 * @author alberto
 */
public class GenMonitores {

    public static Entity crearMonitorTipo1(Texture textura) {
        Entity monitor = new Entity("Monitor");
        Texture textMonitor = null;
        Material materialCarcasa = new Material();
        // materialCarcasa.setColor(new QColor(1, 0.5f, 0.5f, 0.5f));
        materialCarcasa.setColor(QColor.YELLOW);
        // try {
        // textMonitor = new QTextura(
        // ImageIO.read(new
        // File("assets/textures/basicas/varias/Monitor-300x191.gif")));
        // materialCarcasa.setMapaColor(new QProcesadorSimple(textMonitor));
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        monitor.addComponent(
                MaterialUtil.applyMaterial(new Plane(3 * 600 / 800 + 0.2f, 3 + 0.1f), materialCarcasa));

        Entity pantalla = new Entity("pantalla");
        Material pantallMat = new Material();
        pantallMat.setMapaColor(textura);
        // pantallMat.setColor(new QColor(1, 0.2f, 0.2f, 0.2f));
        pantallMat.setColor(QColor.BROWN);
        pantalla.addComponent(MaterialUtil.applyMaterial(new Plane(3 * 600 / 800, 3), pantallMat));
        pantalla.move(0, 0.02f, 0);
        monitor.addChild(pantalla);

        return monitor;
    }
}
