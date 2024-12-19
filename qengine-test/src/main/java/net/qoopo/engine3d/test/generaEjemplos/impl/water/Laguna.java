/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.water;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.terrain.HeightMapTerrain;
import net.qoopo.engine.core.entity.component.terrain.Terrain;
import net.qoopo.engine.core.entity.component.water.WaterDuDv;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;
import net.qoopo.engine3d.test.juegotest.generadores.GenMonitores;

/**
 *
 * @author alberto
 */
public class Laguna extends MakeTestScene {

        @Override
        public void make(Scene mundo) {
                try {
                        this.scene = mundo;

                        // //un planeta tierra
                        // Entity planeta = new Entity("planeta");
                        // QGeometria planteG = new QEsfera(mundo.UM.convertirPixel(5,
                        // QUnidadMedida.KILOMETRO));
                        // QMaterialUtil.aplicarTexturaEsfera(planteG, new File("assets/"+
                        // "textures/planetas/tierra/text4/tierra.jpg"));
                        //// QUtilNormales.invertirNormales(cieloG);
                        // planeta.agregarComponente(planteG);
                        // planeta.mover(-10000, 0, 10000);
                        // mundo.addEntity(planeta);
                        // CREACION DEL LAGO
                        // Lago

                        Entity entity = new Entity("Agua");
                        WaterDuDv water = new WaterDuDv(mundo, 800, 800);
                        entity.addComponent(water);
                        water.build();
                        entity.scale(0.1f);
                        mundo.addEntity(entity);

                        // un monitor para ver el mapa de reflexion y refracción
                        // texEspejo1
                        Entity monitor = GenMonitores.crearMonitorTipo1(water.getTextReflexion());
                        monitor.setName("Reflexion");
                        monitor.move(-48, 48, -45);
                        monitor.rotate((float) Math.toRadians(-45), (float) Math.toRadians(45), 0);
                        mundo.addEntity(monitor);
                        Entity monitor2 = GenMonitores.crearMonitorTipo1(water.getTextRefraccion());
                        // Entity monitor2 =
                        // GenMonitores.crearMonitorTipo1(procesador.getTextNormal());
                        monitor2.setName("Refraccion");
                        monitor2.move(-45, 48, -48);
                        monitor2.rotate((float) Math.toRadians(-45), (float) Math.toRadians(45), 0);
                        mundo.addEntity(monitor2);

                        // el terreno generado con mapas de altura
                        Entity terrainEntity = new Entity("Terreno");

                        QTextura terrainTexture = AssetManager.get().loadTexture("terreno",
                                        "assets/textures/terrain/rocky_terrain/rocky_terrain_02_diff_4k.jpg");
                        QMaterialBas materialTerrain = new QMaterialBas(terrainTexture);
                        materialTerrain.setMapaNormal(AssetManager.get().loadTexture("terreno_normal",
                                        "assets/textures/terrain/rocky_terrain/rocky_terrain_02_nor_gl_4k.png"));
                        materialTerrain.setMapaEspecular(AssetManager.get().loadTexture("terreno_normal",
                                        "assets/textures/terrain/rocky_terrain/rocky_terrain_02_spec_4k.png"));
                        materialTerrain.setMapaRugosidad(AssetManager.get().loadTexture("terreno_normal",
                                        "assets/textures/terrain/rocky_terrain/rocky_terrain_02_rough_4k.png"));

                        Terrain terreno = new HeightMapTerrain(new File("assets/heightmaps/lake.png"), 1, -5, 15f,
                                        1,
                                        materialTerrain, true);
                        terrainEntity.addComponent(terreno);
                        terreno.build();
                        mundo.addEntity(terrainEntity);

                        // Terrain terreno = new ProceduralTerrain(50, 50, 1f, materialTerrain, true);
                        // terrainEntity.addComponent(terreno);
                        // terreno.build();
                        // mundo.addEntity(terrainEntity);

                        // // arboles
                        // Mesh pinoG = QUtilComponentes
                        // .getMesh(AssetManager.get().loadModel(new File("assets/"
                        // + "models/obj/VEGETACION/EXTERIOR/baja_calidad/pino/lowpolytree.obj")));

                        // Entity pino1 = new Entity();
                        // pino1.addComponent(pinoG);
                        // pino1.move(78, 20, 0);
                        // pino1.scale(4, 4, 4);
                        // mundo.addEntity(pino1);

                        // Entity pino2 = new Entity();
                        // pino2.addComponent(pinoG);
                        // pino2.move(78, 20, 25);
                        // pino2.scale(4, 4, 4);
                        // mundo.addEntity(pino2);

                        // Entity pino3 = new Entity();
                        // pino3.addComponent(pinoG);
                        // pino3.move(90, 25, 15);
                        // pino3.scale(4, 4, 4);
                        // mundo.addEntity(pino3);

                        // Entity pino4 = new Entity();
                        // pino4.addComponent(pinoG);
                        // pino4.move(60, 25, 60);
                        // pino4.scale(4, 4, 4);
                        // mundo.addEntity(pino4);

                        // Entity pino5 = new Entity();
                        // pino5.addComponent(pinoG);
                        // pino5.move(45, 25, 68);
                        // pino5.scale(4, 4, 4);
                        // mundo.addEntity(pino5);

                        // // mueve la luz del sol
                        // Thread hilo = new Thread(new Runnable() {
                        // @Override
                        // public void run() {
                        //
                        // float angulo = (float) Math.toRadians(1);
                        //// float angz = 0;
                        //
                        // while (true) {
                        // try {
                        // Thread.sleep(500);
                        // } catch (InterruptedException ex) {
                        // }
                        // luz.direction.rotateZ(angulo);
                        //// sol.direction.rotateZ(angulo);
                        // }
                        // }
                        // });
                        // hilo.start();
                } catch (Exception ex) {
                        Logger.getLogger(Laguna.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
