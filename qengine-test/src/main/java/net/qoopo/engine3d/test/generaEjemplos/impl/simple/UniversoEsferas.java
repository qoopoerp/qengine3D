/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import java.util.Random;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QEsfera;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 *
 * @author alberto
 */
public class UniversoEsferas extends MakeTestScene {

    private QMaterialBas material = null;

    private void cargarMaterial() {
        material = null;
        try {
            // int colorTransparencia = -1;
            material = new QMaterialBas(
                    AssetManager.get().loadTexture("texEsfera", "assets/textures/basicas/caja.jpg"), 64);
            // material.texturaColorTransparente = colorTransparencia;
            // if (colorTransparencia != -1) {
            // material.transAlfa = 0.99f;// el objeto tiene una trasnparencia
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void make(Scene mundo) {
        this.scene = mundo;
        cargarMaterial();
        Random rnd = new Random();
        float tamUniverso = 100;

        Entity luzEntidad = new Entity("luz");

        // luces
        QLigth luz = new QDirectionalLigth(1.5f, QColor.WHITE, 1, QVector3.of(-1, -1, 1), false, false);

        luzEntidad.addComponent(luz);
        mundo.addEntity(luzEntidad);

        // QLuz luz2 = new QLuz(QLuz.LUZ_PUNTUAL, 1.5f, 0, 255, 0, 3f, 2f, 0, true);
        // mundo.agregarLuz(luz2);
        // Color colores[] = {
        // Color.YELLOW,
        // Color.RED,
        // Color.GREEN,
        // Color.PINK,
        // Color.MAGENTA,
        // Color.ORANGE,
        // Color.BLUE,
        // Color.CYAN,
        // Color.LIGHT_GRAY,
        // // Color.BLACK,
        // Color.WHITE,
        // Color.GRAY,
        // Color.DARK_GRAY
        // };
        // Color actual;
        Mesh geometria = new QEsfera(1);
        QMaterialUtil.aplicarMaterial(geometria, material);
        for (int i = 0; i < 100; i++) {
            Entity esfera = new Entity("Esfera [" + i + "]");
            esfera.move(rnd.nextFloat() * tamUniverso * 2 - tamUniverso,
                    rnd.nextFloat() * tamUniverso * 2 - tamUniverso, rnd.nextFloat() * tamUniverso * 2 - tamUniverso);
            // actual = colores[rnd.nextInt(colores.length)];
            // ((QMaterialBas) geometria.listaPrimitivas[0].material).setColorDifusa(new
            // QColor(actual));
            esfera.addComponent(geometria);
            mundo.addEntity(esfera);
        }
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
