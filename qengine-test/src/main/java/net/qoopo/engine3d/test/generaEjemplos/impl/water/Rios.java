/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.water;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QEsfera;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlano;
import net.qoopo.engine.core.entity.component.mesh.util.QUnidadMedida;
import net.qoopo.engine.core.entity.component.terrain.HeightMapTerrain;
import net.qoopo.engine.core.entity.component.terrain.Terrain;
import net.qoopo.engine.core.entity.component.water.Water;
import net.qoopo.engine.core.entity.component.water.WaterDuDv;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QUtilComponentes;
import net.qoopo.engine.core.util.mesh.QUtilNormales;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;
import net.qoopo.engine3d.test.juegotest.generadores.GenMonitores;

/**
 *
 * @author alberto
 */
public class Rios extends MakeTestScene {

    public void make(Scene mundo) {
        try {
            this.scene = mundo;

            int anchoReflejo = 800;
            int altoReflejo = 600;

            Entity sol = new Entity("Sol");
            sol.addComponent(new QDirectionalLigth(1.5f, QColor.WHITE, 1000, QVector3.of(0, -1, 0), true, true));
            mundo.addEntity(sol);
            // //cielo
            Entity cielo = new Entity("Cielo");
            Mesh cieloG = new QEsfera(mundo.UM.convertirPixel(500, QUnidadMedida.METRO));
            QMaterialBas mat = new QMaterialBas(
                    new QTextura(ImageIO.read(new File("assets/textures/cielo/esfericos/cielo.jpg"))));
            QMaterialUtil.aplicarMaterial(cieloG, mat);
            QUtilNormales.invertirNormales(cieloG);
            cielo.addComponent(cieloG);

            mundo.addEntity(cielo);

            // //un planeta tierra
            // Entity planeta = new Entity("planeta");
            // QGeometria planteG = new QEsfera(mundo.UM.convertirPixel(5,
            // QUnidadMedida.KILOMETRO));
            // QMaterialUtil.aplicarTexturaEsfera(planteG, new
            // File("assets/"+"textures/planetas/tierra/text4/tierra.jpg"));
            //// QUtilNormales.invertirNormales(cieloG);
            // planeta.agregarComponente(planteG);
            // planeta.mover(-10000, 0, 10000);
            // mundo.addEntity(planeta);
            // CREACION DEL LAGO
            System.out.println("Cargando Lago");
            // Lago
            QMaterialBas material = new QMaterialBas("Lago");
            // material.setTransparencia(true);
            // material.setTransAlfa(0.4f);//40% ( transparencia del 60%)
            material.setColorBase(new QColor(1, 0, 0, 0.7f));

            Entity agua = new Entity("Agua");
            agua.addComponent(QMaterialUtil.aplicarMaterial(new QPlano(350, 350), material));
            WaterDuDv procesador = new WaterDuDv(mundo, anchoReflejo, altoReflejo);
            agua.addComponent(procesador);
            material.setMapaColor(procesador.getOutputTexture());
            material.setMapaNormal(new QProcesadorSimple(procesador.getTextNormal()));
            agua.move(0, 0.1f, 6);

            mundo.addEntity(agua);

            // un monitor para ver el mapa de reflexion
            // texEspejo1
            Entity monitor = GenMonitores.crearMonitorTipo1(procesador.getTextReflexion());
            monitor.move(-48, 48, -45);
            monitor.rotate((float) Math.toRadians(-45), (float) Math.toRadians(45), 0);

            mundo.addEntity(monitor);

            // el terreno generado con mapas de altura
            System.out.println("cargando Terreno");
            Entity entidadTerreno = new Entity("Terreno");

            QTextura terrainTexture = AssetManager.get().loadTexture("terreno",
                    "assets/textures/terrain/rocky_terrain/rocky_terrain_02_diff_4k.jpg");
            QMaterialBas materialTerrain = new QMaterialBas(terrainTexture);
            materialTerrain.setMapaNormal(AssetManager.get().loadTexture("terreno_normal",
                    "assets/textures/terrain/rocky_terrain/rocky_terrain_02_nor_gl_4k.png"));
            materialTerrain.setMapaEspecular(AssetManager.get().loadTexture("terreno_normal",
                    "assets/textures/terrain/rocky_terrain/rocky_terrain_02_spec_4k.png"));
            materialTerrain.setMapaRugosidad(AssetManager.get().loadTexture("terreno_normal",
                    "assets/textures/terrain/rocky_terrain/rocky_terrain_02_rough_4k.png"));

            Terrain terreno = new HeightMapTerrain(new File("assets/heightmaps/wm4.jpg"), 1, -5, 15f,
                    1,
                    materialTerrain, true);
            entidadTerreno.addComponent(terreno);

            mundo.addEntity(entidadTerreno);

            System.out.println("cargando arboles");
            // arboles
            Mesh pinoG = QUtilComponentes
                    .getMesh(AssetManager.get().loadModel(new File("assets/"
                            + "models/obj/VEGETACION/EXTERIOR/baja_calidad/pino/lowpolytree.obj")));
            Entity pino1 = new Entity();
            pino1.addComponent(pinoG.clone());
            pino1.move(78, 20, 0);
            pino1.scale(4, 4, 4);
            mundo.addEntity(pino1);

            Entity pino2 = new Entity();
            pino2.addComponent(pinoG.clone());
            pino2.move(78, 20, 25);
            pino2.scale(4, 4, 4);
            mundo.addEntity(pino2);

            Entity pino3 = new Entity();
            pino3.addComponent(pinoG.clone());
            pino3.move(90, 25, 15);
            pino3.scale(4, 4, 4);
            mundo.addEntity(pino3);

            Entity pino4 = new Entity();
            pino4.addComponent(pinoG.clone());
            pino4.move(60, 25, 60);
            pino4.scale(4, 4, 4);
            mundo.addEntity(pino4);

            Entity pino5 = new Entity();
            pino5.addComponent(pinoG.clone());
            pino5.move(45, 25, 68);
            pino5.scale(4, 4, 4);
            mundo.addEntity(pino5);
            System.out.println("Cargado");
        } catch (IOException ex) {
            Logger.getLogger(Rios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
