/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.lwjgl.asset.AssimpModelLoader;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjemploSponza extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;

            ModelLoader modelLoader = new AssimpModelLoader();
            Entity entidad = modelLoader
                    .loadModel(new File("assets/models/obj/escenarios/Sponza/sponza.obj"));
            entidad.scale(0.1f, 0.1f, 0.1f);
            this.scene.addEntity(entidad);

        } catch (Exception ex) {
            Logger.getLogger(EjemploSponza.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
