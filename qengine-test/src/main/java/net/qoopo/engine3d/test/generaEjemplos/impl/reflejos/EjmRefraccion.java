/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.reflejos;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.cubemap.CubeMap;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Suzane;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Teapot;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjmRefraccion extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;
        Entity ob1 = crear("Tetera",new Teapot());
        ob1.move(-3, 3, 0);
        mundo.addEntity(ob1);
        Entity ob3 = crear("Mona", new Suzane());
        ob3.move(3, 3, 0);
        mundo.addEntity(ob3);

    }

    private Entity crear(String nombre, Mesh malla) {
        Entity objeto = new Entity(nombre);
        CubeMap mapa = new CubeMap();
        QMaterialBas material = new QMaterialBas(nombre);
        material.setColorBase(QColor.WHITE);
        material.setMetalico(1f);
        material.setMapaEntorno(new QProcesadorSimple(mapa.getTexturaEntorno()));
        material.setTipoMapaEntorno(CubeMap.FORMATO_MAPA_CUBO);
        objeto.addComponent(MaterialUtil.applyMaterial(malla, material));
        objeto.addComponent(mapa);
        mapa.aplicar(CubeMap.FORMATO_MAPA_CUBO, 1f, 1.45f);
        return objeto;
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
