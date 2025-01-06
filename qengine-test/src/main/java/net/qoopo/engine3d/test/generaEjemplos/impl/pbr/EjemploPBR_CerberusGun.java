/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.pbr;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.assets.model.waveobject.LoadModelObj;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.cubemap.CubeMap;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjemploPBR_CerberusGun extends MakeTestScene {

    public EjemploPBR_CerberusGun() {

    }

    public void make(Scene escena) {
        this.scene = escena;
        try {
            CubeMap mapa = new CubeMap(QGlobal.MAPA_CUPO_RESOLUCION);

            // Entity objeto = new Entity("PBR");
            // Entity objeto = AssimpLoader.cargarAssimpItems(new File("assets/"+
            // "models/fbx/Cerberus_by_Andrew_Maximov/Cerberus_LP.FBX")).get(0);
            Entity objeto = AssetManager.get()
                    .loadModel(new File("assets/models/fbx/cerberus.fbx"));
            objeto.scale(10);
            objeto.addComponent(mapa);
            objeto.rotate(0, Math.toRadians(180), 0);
            // objeto.agregarComponente(QMaterialUtil.aplicarMaterial(new Sphere(1.0f),
            // material));
            mapa.aplicar(CubeMap.FORMATO_MAPA_CUBO, 1.0f, 0);
            escena.addEntity(objeto);
        } catch (Exception ex) {
            Logger.getLogger(EjemploPBR_CerberusGun.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
