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
import net.qoopo.engine.core.lwjgl.asset.AssimpModelLoader;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class TestLoadAssimp extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;

            List<File> archivos = new ArrayList<>();
            // ------------------------ md2 --------------------------------
            // archivos.add(new File("assets/models/obj/VARIOS/superMario64/renderer3D/mario_idle.md2"));
            // archivos.add(new File("assets/models/obj/VARIOS/superMario64/renderer3D/mario_jump.md2"));
            // archivos.add(new File("assets/models/obj/VARIOS/superMario64/renderer3D/mario_run.md2"));
            // archivos.add(new File("assets/models/obj/VARIOS/superMario64/renderer3D/mario_run2.md2"));

            // ------------------------ md5 --------------------------------
            // archivos.add(new File("assets/models/md5/doom3/md5/monsters/hellknight/hellknight.md5mesh"));
            // archivos.add(new File("assets/models/md5/DOOM_MONSTERS/hellknight/monster.md5mesh"));
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
            archivos.add(new File("assets/models/collada/vaquero_tuto/model.dae"));
            // archivos.add(new File("assets/models/collada/black_dragon/Dragon 2.5_dae.dae"));
            // archivos.add(new File("assets/models/collada/Crysis/Crysis.dae"));
            // archivos.add(new File("assets/models/collada/animacionTest.dae"));
            // archivos.add(new File("assets/models/collada/star_fox/star_fox_4.dae"));
            // archivos.add(new File("assets/models/colladaident_evil_doorident_evil_door_2.dae"));
            // archivos.add(new File("assets/models/collada/bandera/flag.dae"));
            // archivos.add(new File("assets/models/collada/bandera/camera.dae"));
            // archivos.add(new File("assets/models/collada/parappa/parappa.dae"));
            // archivos.add(new File("assets/models/collada/parappa/parappa_bye_animation.dae"));
            // archivos.add(new File("assets/models/blend/Black Dragon NEW/Dragon 2.5_dae.dae"));
            // archivos.add(new File("assets/models/blend/Black Dragon NEW/Dragon_2.5_For_Animations.dae"));
            // archivos.add(new File("assets/models/blend/Suzanne_head_expressions/Suzanne head expressions.dae"));
            // archivos.add(new File("assets/models/collada/space station/Space Station Scene.dae"));
            // archivos.add(new File("assets/models/collada/space station/Space Station Scene 2.dae"));
            // archivos.add(new File("assets/models/collada/space station/Space Station Scene 3.dae"));
            // archivos.add(new File("assets/models/collada/mixamo/crisys/Samba Dancing.dae"));
            // archivos.add(new File("assets/models/collada/mixamo/swat/Taunt/Taund.dae"));
            // archivos.add(new File("assets/models/collada/mixamo/Bot-Y/Idle.dae"));
            // archivos.add(new File("assets/models/collada/mixamo/Bot-Y/Boxing.dae"));
            //// //------- fbx ------------------
            // archivos.add(new File("assets/models/fbx/mixamo/Shoved Reaction With Spin.fbx"));
            // archivos.add(new File("assets/models/fbx/mixamo/Bot-Y/Idle.fbx"));
            // archivos.add(new File("assets/models/fbx/mixamo/Bot-Y/Boxing.fbx"));
            // archivos.add(new File("assets/models/fbx/mixamo/Bot-Y/Mutant Punch.fbx"));
            // archivos.add(new File("assets/models/fbx/mixamo/Bot-Y/Silly Dancing.fbx"));
            // archivos.add(new File("assets/models/fbx/mixamo/Bot-Y/Run.fbx"));
            // archivos.add(new File("assets/models/fbx/mixamo/swat/Boxing.fbx"));
            // archivos.add(new File("assets/models/fbx/mixamo/PandaMale/Taunt.fbx"));
            // archivos.add(new File("assets/models/blend/Low-Poly Spider/Spider.fbx"));
            // archivos.add(new File("assets/models/blend/Low-Poly Spider/Spider_2.fbx"));
            // archivos.add(new File("assets/models/blend/Low-Poly Spider/Spider_3.fbx"));
            // archivos.add(new File("assets/models/blend/Black Dragon NEW/Dragon 2.5_fbx.fbx"));
            // //---------- blend ----------------------
            // archivos.add(new File("assets/models/blend/Low-Poly Spider/Only_Spider_with_Animations.blend"));
            // archivos.add(new File("assets/models/blend/animacionTest.blend"));
            // archivos.add(new File("assets/models/blend/cubo.blend"));
            // archivos.add(new File("assets/models/blend/Suzanne_head_expressions/Suzanne head expressions.blend"));
            // archivos.add(new File("assets/models/blend/living room ibra1991 01 July 2017 11 25 pm/living room.blend"));
            // archivos.add(new File("assets/models/collada/space station/Space Station Scene.blend"));
            // -------------------- 3DS -------------------
            // archivos.add(new File("assets/models/blend/Low-Poly Spider/Only_Spider_with_Animations.3ds"));
            // archivos.add(new File("assets/models/blend/Black Dragon NEW/Dragon 2.5_3ds.3ds"));
            
            // -------------------- OGRE XML -------------------
            // archivos.add(new File("assets/models/ogre_xml/oto/Oto.mesh.xml"));
            // -------------------- GLFT -------------------
            // archivos.add(new File("assets/models/gltf/Box.gltf"));

            int i = 0;
            ModelLoader loader = new AssimpModelLoader();
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 5; x++) {
                    try {
                        if (i < archivos.size()) {
                            // cargardor ASIMP
                            Entity objeto = loader.loadModel(archivos.get(i));
                            objeto.move(x * 10, 0, y * 10);
                            // objeto.scale(0.05f, 0.05f, 0.05f);
                            mundo.addEntity(objeto);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(TestLoadAssimp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
