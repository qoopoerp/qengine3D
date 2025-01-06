/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.pbr;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.cubemap.CubeMap;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjemploPBR2 extends MakeTestScene {

    public EjemploPBR2() {

    }

    public void make(Scene escena) {
        this.scene = escena;

        int nrRows = 7;
        int nrColumns = 7;
        float spacing = 2.5f;

        // la entidad reflexion se encargara de renderizar el mapa de reflejos
        Entity reflexion = new Entity("cubemap");
        CubeMap mapa = new CubeMap(QGlobal.MAPA_CUPO_RESOLUCION);
        // mapa.setGenerarIrradiacion(true);
        reflexion.addComponent(mapa);
        mapa.aplicar(CubeMap.FORMATO_MAPA_CUBO, 1.0f, 0);
        escena.addEntity(reflexion);

        for (int row = 0; row <= nrRows; ++row) {
            for (int col = 0; col <= nrColumns; ++col) {
                QMaterialBas material = new QMaterialBas("PBR");
                material.setColorBase(QColor.RED);
                material.setRugosidad(QMath.clamp((float) col / (float) nrColumns, 0.05f, 1.0f));
                material.setMetalico((float) row / (float) nrRows);
                material.setMapaEntorno(mapa.getProcEntorno());
                // material.setMapaIrradiacion(mapa.getProcIrradiacion());
                material.setTipoMapaEntorno(CubeMap.FORMATO_MAPA_CUBO);
                Entity objeto = new Entity("PBR");
                objeto.move((col - (nrColumns / 2)) * spacing, (row - (nrRows / 2)) * spacing, 0);
                objeto.addComponent(MaterialUtil.applyMaterial(new Sphere(1.0f), material));
                // objeto.addComponent(MaterialUtil.aplicarMaterial(new Teapot(),
                // material));
                // objeto.escalar(0.8f);

                escena.addEntity(objeto);
            }
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
