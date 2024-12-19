/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.pbr;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QEsfera;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjemploPBR extends MakeTestScene {

    public EjemploPBR() {

    }

    public void make(Scene escena) {
        this.scene = escena;

        int nrRows = 7;
        int nrColumns = 7;
        float spacing = 2.5f;

        for (int row = 0; row <=nrRows; ++row) {
            for (int col = 0; col <=nrColumns; ++col) {
                QMaterialBas material = new QMaterialBas("PBR");
                material.setColorBase(QColor.RED);
                material.setRugosidad(QMath.clamp((float)col / (float) nrColumns, 0.05f, 1.0f));
                material.setMetalico((float) row / (float) nrRows);
                Entity objeto = new Entity("PBR");
                objeto.move((col - (nrColumns / 2)) * spacing, (row - (nrRows / 2)) * spacing, 0);
                objeto.addComponent(QMaterialUtil.aplicarMaterial(new QEsfera(1.0f), material));
//                objeto.agregarComponente(QMaterialUtil.aplicarMaterial(new QTeapot(), material));

//                objeto.escalar(0.8f);
                escena.addEntity(objeto);
            }
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
