/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.game.mundo.niveles;

import java.io.File;
import java.util.Random;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaIndexada;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.entity.component.terrain.Terrain;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.scene.QEscenario;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.util.ComponentUtil;
import net.qoopo.engine.terrain.HeightMapTerrain;
import net.qoopo.engine3d.test.game.monstruos.QDoomMonster;

/**
 *
 * @author alberto
 */

public class DoomTest extends QEscenario {

    private static Logger logger = Logger.getLogger("test");

    private static boolean monstruoDoom1 = true;
    private static boolean monstruoQuake1 = true;
    private static boolean monstruoQuake2 = true;

    public void cargar(Scene escena) {
        logger.info("Cargando nivel...");
        loadTextures();
        // universo.luzAmbiente = 0.5f;
        crearTerreno(escena);
        // crearLago1(universo);
        // crearLago2(universo);
        crearObjetosAleatorios(terreno, escena);
        // logger.info("Configurando neblina");
        // QEscena.INSTANCIA.neblina = new QNeblina(true, QColor.GRAY, 0.015f);// como
        // 100 metros
        // QEscena.INSTANCIA.neblina = new QNeblina(true, QColor.GRAY, 0.01f);// como 2
        // metros

        // crearCielo(escena);
        logger.info("Nivel cargado");
    }

    // private void crearCielo(QEscena escena) {
    //
    // //sol
    // Entity sol = new Entity("Sol");
    // QLuzDireccional solLuz = new QLuzDireccional(1.1f, QColor.WHITE, 1,
    // QVector3.of(0, 0, 0), true, true);
    // sol.agregarComponente(solLuz);
    // escena.addEntity(sol);
    //
    // QTextura cieloDia = QGestorRecursos.loadTexture("dia", "assets/"+
    // "textures/cielo/esfericos/cielo_dia.jpg");
    // QTextura cieloNoche = QGestorRecursos.loadTexture("noche", "assets/"+
    // "textures/cielo/esfericos/cielo_noche.png");
    //// QTextura cieloNoche = QGestorRecursos.loadTexture("noche",
    // "res/textures/cielo/esfericos/cielo_noche_2.jpg");
    //
    // escena.setLuzAmbiente(0.5f);
    // QCielo cielo = new SphereCielo(cieloDia, cieloNoche,
    // escena.UM.convertirPixel(500, QUnidadMedida.METRO));
    // escena.addEntity(cielo.getEntidad());
    // }

    private void crearObjetosAleatorios(Terrain terreno, Scene escena) {

        Random rnd = new Random();

        float x = 0, z = 0, y = 0;
        for (int i = 1; i <= 25; i++) {

            if (monstruoDoom1) {
                if (i % 10 == 0) {
                    if (i % 5 == 0) {
                        x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() - terreno.getInicioX();
                        z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() - terreno.getInicioZ();
                        y = terreno.getHeight(x, z);
                        Entity doomMonster = QDoomMonster.quakeMonster();
                        doomMonster.move(x, y, z);
                        // doomMonster.mover(-100, y + 0.8f, 10);
                        doomMonster.scale(0.04f, 0.04f, 0.04f);
                        escena.addEntity(doomMonster);
                    }
                }
            }
            if (monstruoQuake1) {
                if (i % 5 == 0) {
                    x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() - terreno.getInicioX();
                    z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() - terreno.getInicioZ();
                    y = terreno.getHeight(x, z);
                    Entity doomMonster = QDoomMonster.quakeMonster();
                    doomMonster.move(x, y, z);
                    // doomMonster.mover(-100, y + 0.8f, 10);
                    doomMonster.scale(0.04f, 0.04f, 0.04f);
                    escena.addEntity(doomMonster);
                }

            }

            if (monstruoQuake2) {
                if (i % 5 == 0) {
                    x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() - terreno.getInicioX();
                    z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() - terreno.getInicioZ();
                    y = terreno.getHeight(x, z);
                    Entity doomMonster = QDoomMonster.quakeMonster2();
                    doomMonster.move(x, y, z);
                    // doomMonster.mover(-100, y + 0.8f, 10);
                    doomMonster.scale(0.04f, 0.04f, 0.04f);
                    escena.addEntity(doomMonster);
                }

            }

        }
    }

    private void crearTerreno(Scene universo) {
        // el terreno generado con mapas de altura
        logger.info("Creando terreno...");

        Entity entidadTerreno = new Entity("Terreno");

        Texture terrainTexture = AssetManager.get().loadTexture("terreno",
                "assets/textures/terrain/rocky_terrain/rocky_terrain_02_diff_4k.jpg");
        Material materialTerrain = new Material(terrainTexture);
        materialTerrain.setNormalMap(AssetManager.get().loadTexture("terreno_normal",
                "assets/textures/terrain/rocky_terrain/rocky_terrain_02_nor_gl_4k.png"));
        materialTerrain.setMapaEspecular(AssetManager.get().loadTexture("terreno_normal",
                "assets/textures/terrain/rocky_terrain/rocky_terrain_02_spec_4k.png"));
        materialTerrain.setRoughnessMap(AssetManager.get().loadTexture("terreno_normal",
                "assets/textures/terrain/rocky_terrain/rocky_terrain_02_rough_4k.png"));

        Terrain terreno = new HeightMapTerrain(new File("assets/heightmaps/heightmap.png"), 1, -5, 15f,
                1,
                materialTerrain, true);
        entidadTerreno.addComponent(terreno);

        // QColisionTerreno colision = new QColisionTerreno(terreno);
        // entidadTerreno.agregarComponente(colision);
        QColisionMallaIndexada colision = new QColisionMallaIndexada(ComponentUtil.getMesh(entidadTerreno));
        QObjetoRigido terrenoRigidez = new QObjetoRigido(QObjetoDinamico.ESTATICO);
        terrenoRigidez.setFormaColision(colision);
        entidadTerreno.addComponent(terrenoRigidez);
        universo.addEntity(entidadTerreno);
        logger.info("Terreno cargado...");
    }

    private void loadTextures() {
        logger.info("Cargando textures...");
        AssetManager.get().loadTexture("terreno", "assets/textures/terreno/text4.jpg");
        AssetManager.get().loadTexture("lagoNormal", "assets/textures/agua/matchingNormalMap.png");
        logger.info("Texturas cargadas...");
    }

}
