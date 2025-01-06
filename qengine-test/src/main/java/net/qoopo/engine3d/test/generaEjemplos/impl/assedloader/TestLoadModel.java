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
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.core.asset.model.DefaultModelLoader;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class TestLoadModel extends MakeTestScene {

        public void make(Scene mundo) {
                try {
                        this.scene = mundo;
                        ModelLoader loadModel = new DefaultModelLoader();
                        // Cargador propio
                        List<File> archivos = new ArrayList<>();

                        // ascii
                        archivos.add(new File("assets/models/asc/torus.txt"));
                        archivos.add(new File("assets/models/asc/ship.txt"));
                        archivos.add(new File("assets/models/asc/head.txt"));
                        archivos.add(new File("assets/models/asc/quake.txt"));
                        // md5
                        archivos.add(new File("assets/models/md5/doom3/md5/characters/player/mocap/player.md5mesh"));
                        archivos.add(new File("assets/models/md5/DOOM_MONSTERS/hellknight/monster.md5mesh"));
                        archivos.add(new File("assets/models/md5/bob/boblamp.md5mesh"));
                        archivos.add(new File("assets/models/md5/doom3/md5/monsters/mancubus/mancubus.md5mesh"));
                        archivos.add(new File("assets/models/md5/doom3/md5/monsters/imp/imp.md5mesh"));

                        // wavefront obj
                        archivos.add(new File("assets/models/obj/standford/bunny.obj"));
                        archivos.add(new File("assets/models/obj/elephant.obj"));
                        // archivos.add(new File("assets/models/obj/lion.obj"));
                        archivos.add(new File("assets/models/obj/cyborg/cyborg.obj"));
                        archivos.add(new File("assets/models/obj/caja_madera/box.obj"));

                        // collada
                        archivos.add(new File("assets/models/collada/vaquero_tuto/model.dae"));

                        // //// //------- fbx ------------------
                        // archivos.add(new File("assets/models/fbx/mixamo/Shoved Reaction With
                        // Spin.fbx"));

                        int space = 8;
                        int sections = 5;
                        for (int i = 0; i < archivos.size(); i++) {
                                Entity entidad = loadModel.loadModel(archivos.get(i));
                                entidad.move((i % sections) * space, 0, (i / sections) * space);
                                // entidad.scale(0.05f, 0.05f, 0.05f);
                                // entidad.rotate(Math.toRadians(-90), 0, 0);
                                mundo.addEntity(entidad);
                        }

                } catch (Exception ex) {
                        Logger.getLogger(TestLoadMd5.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
