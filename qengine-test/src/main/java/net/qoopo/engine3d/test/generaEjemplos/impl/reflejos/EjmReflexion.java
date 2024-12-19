/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.reflejos;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.cubemap.QCubeMap;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QTeapot;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjmReflexion extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;

        Entity objeto = new Entity("Reflexion");
        objeto.move(0, 3, 0);
        QMaterialBas mat4 = new QMaterialBas("Reflexion");
        mat4.setColorBase(QColor.BLUE);
        mat4.setMetalico(0.8f);
        QCubeMap mapa = new QCubeMap();
        mat4.setMapaEntorno(new QProcesadorSimple(mapa.getTexturaEntorno()));
        mat4.setTipoMapaEntorno(QCubeMap.FORMATO_MAPA_CUBO);
        objeto.addComponent(QMaterialUtil.aplicarMaterial(new QTeapot(), mat4));
        objeto.addComponent(mapa);
        mapa.aplicar(QCubeMap.FORMATO_MAPA_CUBO, 0.9f, 0);
        mundo.addEntity(objeto);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
