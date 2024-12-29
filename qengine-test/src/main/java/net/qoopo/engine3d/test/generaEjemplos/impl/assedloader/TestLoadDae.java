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

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.load.collada.thinmatrix.LoadModelDae;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class TestLoadDae extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;

            ModelLoader loadModel = new LoadModelDae();

            QTextura texture = AssetManager.get().loadTexture("difusa",
                    new File("assets/models/collada/vaquero_tuto/diffuse.png"));
            QMaterialBas material = new QMaterialBas(texture);

            File archivo = new File("assets/models/collada/vaquero_tuto/model.dae");

            Entity entidad = loadModel.loadModel(archivo);

            // cambio la textura de las geometria cargadas
            for (EntityComponent comp : entidad.getComponents()) {
                if (comp instanceof Mesh) {
                    MaterialUtil.applyMaterial((Mesh) comp, material);
                    // for (QPrimitiva face : ((Mesh) comp).primitivas) {
                    // if (face.material instanceof QMaterialBas) {
                    // ((QMaterialBas) face.material).setMapaColor(texture);
                    // }
                    // }
                }
            }
            entidad.rotate(Math.toRadians(-90), 0, 0);
            entidad.move(-5, 0, 0);
            // entidad.mover(i * 6, 0, j * 6);
            mundo.addEntity(entidad);

            List<File> archivos = new ArrayList<>();

            // //---------------- collada ---------------------------
            archivos.add(new File("assets/models/collada/vaquero_tuto/model.dae"));
            // archivos.add(new File("assets/"+
            // "models/collada/animaciones_mixamo/crisys/Samba Dancing.dae"));
            // archivos.add(new File("assets/"+
            // "models/collada/animaciones_mixamo/swat/Taunt/Taund.dae"));
            //// //colada Bot-Y
            // archivos.add(new File("assets/"+
            // "models/collada/animaciones_mixamo/Bot-Y/Idle.dae"));
            // archivos.add(new File("assets/"+
            // "models/collada/animaciones_mixamo/Bot-Y/Boxing.dae"));

            int i = 0;
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 5; x++) {
                    try {
                        if (i < archivos.size()) {
                            Entity objeto = loadModel.loadModel(archivos.get(i));
                            objeto.rotate(Math.toRadians(-90), 0, 0);
                            objeto.move(x * 10, 0, y * 10);
                            // objeto.scale(0.05f, 0.05f, 0.05f);
                            mundo.addEntity(objeto);
                            i++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(TestLoadDae.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
