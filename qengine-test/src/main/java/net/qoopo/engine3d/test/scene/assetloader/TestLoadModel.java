/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.assetloader;

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
import net.qoopo.engine3d.test.scene.MakeTestScene;

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

                        // ----- wavefront obj -------
                        // archivos.add(new File("assets/models/obj/bunny.obj"));
                        // archivos.add(new File("assets/models/obj/teapot.obj"));
                        // archivos.add(new File("assets/models/obj/meta.obj"));
                        // archivos.add(new File("assets/models/obj/cube.obj"));
                        // archivos.add(new File("assets/models/obj/lowPolyTree.obj"));
                        // archivos.add(new File("assets/models/obj/caja_madera/box.obj"));
                        // archivos.add(new File("assets/models/obj/cyborg/cyborg.obj"));

                        // ----- md2 mesh -------
                        // archivos.add(new File("assets/models/MD2/faerie.md2"));

                        // // ------------------------ md5 --------------------------------
                        // archivos.add(new File("assets/models/md5/doom3_monsters/hellknight/monster.md5mesh"));
                        // archivos.add(new File("assets/models/md5/bob/boblamp.md5mesh"));
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
                        // //---------------- collada ---------------------------
                        // archivos.add(new File("assets/models/collada/vaquero_tuto/model.dae"));
                        // archivos.add(new File("assets/models/collada/duck/duck.dae"));
                        // archivos.add(new File("assets/models/others/black_dragon/dae/Dragon 2.5_dae.dae"));
                        // archivos.add(new File("assets/models/collada/star_fox/star_fox_4.dae"));
                        // archivos.add(new File("assets/models/collada/box_nested_animation.dae"));
                        // archivos.add(new File("assets/models/collada/anims_with_full_rotations_between_keys.DAE"));

                        // //------- fbx ------------------
                        // archivos.add(new File("assets/models/fbx/mixamo/Shoved Reaction With Spin.fbx"));
                        // archivos.add(new File("assets/models/fbx/mixamo/Texting While Standing.fbx"));

                        // ---------- blend ----------------------
                        // archivos.add(new File("assets/models/others/black_dragon/blender/Dragon_Baked_Actions.blend"));
                        // -------------------- 3DS -------------------
                        // archivos.add(new File("assets/models/others/black_dragon/3ds/Dragon 2.5_3ds.3ds"));

                        // -------------------- OGRE XML -------------------
                        // archivos.add(new File("assets/models/Ogre/oto/Oto.mesh.xml"));
                        // archivos.add(new File("assets/models/Ogre/TheThing/Mesh.mesh.xml"));
                        // -------------------- GLFT -------------------
                        // archivos.add(new File("assets/models/glTF/samples/Models/Suzanne/glTF/Suzanne.gltf"));
                        // archivos.add(new File("assets/models/glTF/samples/Models/WaterBottle/glTF/WaterBottle.gltf"));
                        // archivos.add(new File("assets/models/glTF/samples/Models/AnimatedCube/glTF/AnimatedCube.gltf"));

                        int space = 8;
                        int sections = 5;
                        for (int i = 0; i < archivos.size(); i++) {
                                Entity entidad = loadModel.loadModel(archivos.get(i));
                                if(entidad!=null){
                                        entidad.move((i % sections) * space, 0, (i / sections) * space);
                                        mundo.addEntity(entidad);
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
