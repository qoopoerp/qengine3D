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

                        // // ----- ascii -------
                        // archivos.add(new File("assets/models/asc/torus.txt"));
                        // archivos.add(new File("assets/models/asc/ship.txt"));
                        // archivos.add(new File("assets/models/asc/head.txt"));
                        // archivos.add(new File("assets/models/asc/quake.txt"));
                        // ----- md5 mesh -------
                        // archivos.add(new
                        // File("assets/models/md5/doom3/md5/characters/player/mocap/player.md5mesh"));
                        // archivos.add(new
                        // File("assets/models/md5/doom3_monsters/hellknight/monster.md5mesh"));
                        // archivos.add(new File("assets/models/md5/bob/boblamp.md5mesh"));
                        // archivos.add(new
                        // File("assets/models/md5/doom3/md5/monsters/mancubus/mancubus.md5mesh"));
                        // archivos.add(new
                        // File("assets/models/md5/doom3/md5/monsters/imp/imp.md5mesh"));

                        // ----- wavefront obj -------
                        // archivos.add(new File("assets/models/obj/bunny.obj"));
                        archivos.add(new File("assets/models/obj/teapot.obj"));
                        archivos.add(new File("assets/models/obj/meta.obj"));
                        // archivos.add(new File("assets/models/obj/bunny.obj"));
                        // archivos.add(new File("assets/models/obj/cube.obj"));
                        // archivos.add(new File("assets/models/obj/dragon.obj"));
                        // archivos.add(new File("assets/models/obj/lowPolyTree.obj"));
                        // archivos.add(new File("assets/models/obj/caja_madera/box.obj"));
                        // archivos.add(new File("assets/models/obj/cyborg/cyborg.obj"));

                        // ----- collada -------
                        // archivos.add(new File("assets/models/collada/vaquero_tuto/model.dae"));
                        // archivos.add(new File("assets/models/collada/bob_lamp_update.dae"));

                        // ------- fbx ---------
                        // archivos.add(new File("assets/models/fbx/mixamo/Shoved Reaction With
                        // Spin.fbx"));
                        // archivos.add(new File("assets/models/fbx/basic/torus.fbx"));
                        // archivos.add(new File("assets/models/fbx/basic/cube.fbx"));
                        // archivos.add(new File("assets/models/fbx/basic/cone.fbx"));
                        // archivos.add(new File("assets/models/fbx/basic/cilinder.fbx"));
                        // archivos.add(new File("assets/models/fbx/basic/icosphere.fbx"));
                        // archivos.add(new File("assets/models/fbx/basic/piramid.fbx"));
                        // archivos.add(new
                        // File("assets/models/fbx/nathan_animated_walking_fbx/rp_nathan_animated_003_walking.fbx"));

                        int space = 8;
                        int sections = 5;
                        for (int i = 0; i < archivos.size(); i++) {
                                Entity entidad = loadModel.loadModel(archivos.get(i));
                                entidad.move((i % sections) * space, 0, (i / sections) * space);
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
