/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.textura;

import java.io.File;

import javax.imageio.ImageIO;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Plane;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlanoBillboard;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.procesador.QProcesadorAtlasSecuencial;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjmTexturaTransparente extends MakeTestScene {

    private Material crearMaterial() {
        Material material = null;
        try {
            QColor colorTransparencia = QColor.BLACK;
            material = new Material();
            QProcesadorAtlasSecuencial proc = new QProcesadorAtlasSecuencial(
                    // new QTextura(ImageIO.read(new
                    // File("assets/"+"textures/humo/smoke_atlas_1.png"))),
                    new Texture(ImageIO.read(new File("assets/textures/fuego/fire-texture-atlas_2.png"))),
                    8, 4, 10);
            material.setMapaColor(proc);
            material.setColorTransparente(colorTransparencia);

            material.setTransparencia(true);
            material.setTransAlfa(0.90f);// el objeto tiene una trasnparencia
        } catch (Exception e) {
            e.printStackTrace();
        }
        return material;
    }

    public void make(Scene mundo) {
        this.scene = mundo;

        Material mat = crearMaterial();

        Entity plano1 = new Entity("Plano");
        plano1.setBillboard(true);
        plano1.addComponent(MaterialUtil.applyMaterial(new QPlanoBillboard(1f, 1f), mat));
        plano1.move(0, 0.5f, 0);
        mundo.addEntity(plano1);

        Entity plano2 = new Entity("Plano");
        plano2.setBillboard(true);
        plano2.addComponent(MaterialUtil.applyMaterial(new QPlanoBillboard(1f, 1f), mat));
        plano2.move(0, 0.5f, 0.25f);
        mundo.addEntity(plano2);

        Entity plano3 = new Entity("Plano");
        plano3.setBillboard(true);
        plano3.addComponent(MaterialUtil.applyMaterial(new QPlanoBillboard(1f, 1f), mat));
        plano3.move(0, 0.5f, 0.5f);
        mundo.addEntity(plano3);

        // Entity plano4 = new Entity("Plano");
        // plano4.agregarComponente(QMaterialUtil.aplicarTexturaPlano(new QPlano(1f,
        // 1f),mat, 1));
        // plano4.mover(0.25f, 0.5f, 0.3f);
        // mundo.addEntity(plano4);
        //
        // Entity plano5 = new Entity("Plano");
        // plano5.agregarComponente(QMaterialUtil.aplicarTexturaPlano(new QPlano(1f,
        // 1f),mat, 1));
        // plano5.mover(-0.25f, 0.5f, 0.22f);
        // mundo.addEntity(plano5);
        Entity plano = new Entity("Piso");
        plano.addComponent(MaterialUtil.applyColor(new Plane(10, 10), 1, 1, 1, 0, 0, 1, 1, 61));

        mundo.addEntity(plano);
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
