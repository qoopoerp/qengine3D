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
import net.qoopo.engine.core.load.md5.LoadModelMd5;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class TestLoadMd5 extends MakeTestScene {

        public void make(Scene mundo) {
                try {
                        this.scene = mundo;
                        ModelLoader loadModel = new LoadModelMd5();
                        List<File> archivos = new ArrayList<>();
                        // archivos.add(new File("assets/models/md5/doom3/md5/characters/player/mocap/player.md5mesh"));
                        // monstuos
                        archivos.add(new File("assets/models/md5/doom3_monsters/hellknight/monster.md5mesh"));
                        archivos.add(new File("assets/models/md5/bob/boblamp.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/mancubus/mancubus.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/imp/imp.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/hellknight/hellknight.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/guardian/guardian.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/gseeker/gseeker.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/archvile/archvile.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/cacodemon/cacodemon.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/lostsoul/lostsoul.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/maggot3/maggot3.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/pinky/pinky.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/revenant/revenant.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/skeleton/skeleton.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/trite/trite.md5mesh"));
                        // archivos.add(new File("assets/models/md5/doom3/md5/monsters/sabaoth/sabaoth.md5mesh"));

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
