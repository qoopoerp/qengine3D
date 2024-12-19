/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.assedloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.assets.model.waveobject.LoadModelObj;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class TestLoadObj extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;

            ModelLoader loadModel = new LoadModelObj();

            // Cargador propio
            List<File> archivos = new ArrayList<>();
            archivos.add(new File("assets/models/obj/bedroom/iscv2.obj"));


            for (int i = 0; i < archivos.size(); i++) {
                try {
                    Entity entidad = loadModel.loadModel(new File(archivos.get(i).getAbsolutePath()));
                    // entidad.move(i * 10, 0, 2);
                    // entidad.scale(0.05f, 0.05f, 0.05f);
                    // entidad.rotate(Math.toRadians(-90), 0, 0);
                    mundo.addEntity(entidad);
                } catch (Exception e) {
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(TestLoadMd5.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
