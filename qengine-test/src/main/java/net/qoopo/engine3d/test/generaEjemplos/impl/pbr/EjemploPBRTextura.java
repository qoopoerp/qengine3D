/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.pbr;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.cubemap.QCubeMap;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QEsfera;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjemploPBRTextura extends MakeTestScene {

    public EjemploPBRTextura() {

    }

    public void make(Scene escena) {
        this.scene = escena;

        // ------------------------------------
        QTextura albedo = AssetManager.get().loadTexture("difusa",
                new File("assets/textures/PBR/metal/used-stainless-steel/albedo.png"));
        QTextura normal = AssetManager.get().loadTexture("normal",
                new File("assets/textures/PBR/metal/used-stainless-steel/normal.png"));
        QTextura rugoso = AssetManager.get().loadTexture("rugoso",
                new File("assets/textures/PBR/metal/used-stainless-steel/roughness.png"));
        QTextura metalico = AssetManager.get().loadTexture("metalico",
                new File("assets/textures/PBR/metal/used-stainless-steel/metallic.png"));

        int nrRows = 7;
        int nrColumns = 7;
        float spacing = 2.5f;

        // la entidad reflexion se encargara de renderizar el mapa de reflejos
        Entity reflexion = new Entity();
        QCubeMap cubeMap = new QCubeMap(QGlobal.MAPA_CUPO_RESOLUCION);
        // material.setMapaEntorno(new QProcesadorSimple(mapa.getTexturaSalida()));
        // material.setTipoMapaEntorno(QMapaCubo.FORMATO_MAPA_CUBO);
        reflexion.addComponent(cubeMap);
        cubeMap.aplicar(QCubeMap.FORMATO_MAPA_CUBO, 1.0f, 0);
        escena.addEntity(reflexion);

        for (int row = 0; row < nrRows; ++row) {
            for (int col = 0; col < nrColumns; ++col) {
                QMaterialBas material = new QMaterialBas("PBR");
                material.setColorBase(QColor.RED);
                material.setMapaColor(albedo);
                material.setMapaNormal(normal);
                material.setMapaRugosidad(rugoso);
                material.setMapaMetalico(metalico);
                material.setMapaEntorno(cubeMap.getProcEntorno());
                material.setMapaIrradiacion(cubeMap.getProcIrradiacion());
                Entity objeto = new Entity("PBR");
                objeto.move((col - (nrColumns / 2)) * spacing, (row - (nrRows / 2)) * spacing, 0);
                objeto.rotate(0, Math.toRadians(180), 0);
                objeto.addComponent(QMaterialUtil.aplicarMaterial(new QEsfera(1.0f), material));
                escena.addEntity(objeto);
            }
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
