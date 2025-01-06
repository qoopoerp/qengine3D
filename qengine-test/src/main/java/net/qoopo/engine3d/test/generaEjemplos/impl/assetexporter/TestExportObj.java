package net.qoopo.engine3d.test.generaEjemplos.impl.assetexporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.model.ModelExporter;
import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.assets.model.waveobject.ExportModelObj;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.core.asset.model.DefaultModelLoader;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;
import net.qoopo.engine3d.test.generaEjemplos.impl.assedloader.TestLoadMd5;

public class TestExportObj extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;

            ModelLoader loadModel = new DefaultModelLoader();
            ModelExporter exportModel = new ExportModelObj();

            File folder = new File("assets/models/exports");
            folder.mkdirs();

            List<File> archivos = new ArrayList<>();

            // archivos.add(new File("assets/models/obj/casa/Casa_F3DM/Casa.obj"));
            // archivos.add(new File("assets/models/obj/casa_abandonada/cottage_obj.obj"));
            // archivos.add(new File("assets/models/obj/teapot.obj"));
            // archivos.add(new File("assets/models/obj/bunny.obj"));
            archivos.add(new File("assets/models/obj/cube.obj"));
            archivos.add(new File("assets/models/obj/caja_madera/box.obj"));
            // archivos.add(new File("assets/models/obj/camara/camara.obj"));
            // archivos.add(new File("assets/models/collada/vaquero_tuto/model.dae"));
            // archivos.add(new File("assets/models/obj/chestnut/AL05a.obj"));

            // archivos.add(new File("assets/models/fbx/mixamo/Shoved Reaction With
            // Spin.fbx"));
            // archivos.add(new File("assets/models/fbx/cerberus/cerberus.fbx"));

            for (int i = 0; i < archivos.size(); i++) {
                try {
                    Entity entidad = loadModel.loadModel(new File(archivos.get(i).getAbsolutePath()));
                    // exportamos
                    exportModel.exportModel(new File(folder,
                            archivos.get(i).getName().substring(0, archivos.get(i).getName().lastIndexOf("."))
                                    + ".obj"),
                            entidad);
                    mundo.addEntity(entidad);
                } catch (Exception e) {
                    e.printStackTrace();
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
