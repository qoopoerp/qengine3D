/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.juegotest.mundo.niveles;

import java.io.File;
import java.util.Random;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlano;
import net.qoopo.engine.core.entity.component.mesh.util.QUnidadMedida;
import net.qoopo.engine.core.entity.component.physics.collision.QComponenteColision;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaIndexada;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCaja;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.entity.component.terrain.HeightMapTerrain;
import net.qoopo.engine.core.entity.component.terrain.Terrain;
import net.qoopo.engine.core.entity.component.water.WaterDuDv;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.scene.QEscenario;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QUtilComponentes;
import net.qoopo.engine3d.core.sky.QCielo;
import net.qoopo.engine3d.core.sky.QEsferaCielo;
import net.qoopo.engine3d.test.juegotest.generadores.GenFuego;
import net.qoopo.engine3d.test.juegotest.generadores.GeneradorCasas;
import net.qoopo.engine3d.test.juegotest.generadores.GeneradorLamparas;

/**
 *
 * @author alberto
 */
public class NivelTest2 extends QEscenario {

    private static Logger logger = Logger.getLogger("test");

    private static boolean pinos = true;
    private static boolean qubbie = true;
    private static boolean arbolesMuertos = true;
    private static boolean arboles1 = true;
    private static boolean arboles2 = true;
    private static boolean arboles3 = true;
    private static boolean rocas = true;
    private static boolean lamparas = true;
    private static boolean casas_1p = true;
    private static boolean casas_2p = true;
    private static boolean fogatas = true;

    private int anchoReflejo = 800;
    private int altoReflejo = 600;

    public void cargar(Scene escena) {
        logger.info("Cargando nivel...");
        loadTextures();
        // universo.luzAmbiente = 0.5f;
        crearTerreno(escena);
        // crearLago1(universo);
        // crearLago2(universo);
        crearObjetosAleatorios(terreno, escena);
        logger.info("Configurando neblina");
        // QEscena.INSTANCIA.neblina = new QNeblina(true, QColor.GRAY, 0.015f);// como
        // 100 metros
        // QEscena.INSTANCIA.neblina = new QNeblina(true, QColor.GRAY, 0.01f);// como 2
        // metros

        // crearCielo(universo);
        logger.info("Nivel cargado");
    }

    private void crearObjetosAleatorios(HeightMapTerrain terreno, Scene escena) {

        GeneradorCasas generador = new GeneradorCasas();

        try {
            logger.info("  Cargando geometrías..");
            Mesh pinoG = QUtilComponentes.getMesh(AssetManager.get().loadModel(new File(
                    "assets/models/obj/VEGETACION/EXTERIOR/baja_calidad/pino/lowpolytree.obj")));
            Mesh arbolG = QUtilComponentes.getMesh(AssetManager.get().loadModel(new File(
                    "assets/models/obj/VEGETACION/EXTERIOR/alta_calidad/Tree/Tree.obj")));
            Mesh arbol2G = QUtilComponentes.getMesh(AssetManager.get().loadModel(new File(
                    "assets/models/obj/VEGETACION/EXTERIOR/baja_calidad/tree/tree.obj")));
            Mesh arbolMuerto = QUtilComponentes
                    .getMesh(AssetManager.get().loadModel(new File("assets/"
                            + "models/obj/VEGETACION/EXTERIOR/baja_calidad/dead_tree/DeadTree.obj")));
            Mesh roca1 = QUtilComponentes.getMesh(AssetManager.get().loadModel(
                    new File("assets/models/obj/TERRENO/baja_calidad/Rock1/Rock1.obj")).getChilds()
                    .get(1));
            // List<QGeometria> arbol3 = CargaEstatica.cargarWaveObject(new
            // File("assets/"+
            // "models/obj/VEGETACION/EXTERIOR/baja_calidad/tree2/part.obj"));

            Random rnd = new Random();

            logger.info("  Cargando audios..");
            // QBufferSonido bufFuego = QGestorRecursos.cargarAudio("assets/"+
            // "audio/ogg/fire.ogg", "au_beep");
            // QBufferSonido bufBeep = QGestorRecursos.cargarAudio("assets/"+
            // "audio/ogg/beep.ogg", "au_beep");
            // QBufferSonido bufAves = QGestorRecursos.cargarAudio("assets/"+
            // "audio/ogg/aves.ogg", "au_aves");

            float x = 0, z = 0, y = 0;
            for (int i = 1; i <= 50; i++) {

                // FOGATA
                if (fogatas) {
                    if (i % 200 == 0) {
                        x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() - terreno.getInicioX();
                        z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() - terreno.getInicioZ();
                        y = terreno.getAltura(x, z);
                        Entity fogata = GenFuego.crearFogata1();
                        fogata.move(x, y, z);
                        // try {
                        // QEmisorAudio emisorAudio = new QEmisorAudio(true, false);
                        // emisorAudio.setPosition(QVector3.of(x, y, z));
                        // emisorAudio.setBuffer(bufFuego.getBufferId());
                        // emisorAudio.setReproducirAlInicio(true);
                        // emisorAudio.setGain(0.1f);
                        // fogata.agregarComponente(emisorAudio);
                        // } catch (Exception e) {
                        // e.printStackTrace();
                        // }
                        escena.addEntity(fogata);
                    }
                }
                // CASA 1 piso
                if (casas_1p) {
                    if (i % 20 == 0) {
                        x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() - terreno.getInicioX();
                        z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() - terreno.getInicioZ();
                        y = terreno.getAltura(x, z);
                        Entity casa = generador.casa1();
                        casa.move(x, y + 1.25f, z);
                        escena.addEntity(casa);
                    }
                }
                if (casas_2p) // casa 2 pisos
                {
                    if (i % 30 == 0) {
                        x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() - terreno.getInicioX();
                        z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() - terreno.getInicioZ();
                        y = terreno.getAltura(x, z);
                        Entity casa = generador.casa2Pisos();
                        casa.move(x, y + 1.25f, z);
                        escena.addEntity(casa);
                    }
                }

                // Pino
                if (pinos) {
                    if (i % 5 == 0) {
                        x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() - terreno.getInicioX();
                        z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() - terreno.getInicioZ();
                        y = terreno.getAltura(x, z);

                        Entity pino = new Entity();
                        pino.addComponent(pinoG);
                        pino.move(x, y + 2, z);

                        QObjetoRigido pinoRigido = new QObjetoRigido(QObjetoDinamico.ESTATICO);
                        pinoRigido.setMasa(5, QVector3.zero);
                        pinoRigido.tipoContenedorColision = QComponenteColision.TIPO_CONTENEDOR_AABB;
                        pinoRigido.crearContenedorColision(pinoG, pino.getTransformacion());
                        pino.addComponent(pinoRigido);

                        // try {
                        //// manager.addSoundBuffer(buffBeep);
                        // QEmisorAudio sourceBeep = new QEmisorAudio(true, false);
                        // sourceBeep.setPosition(QVector3.of(x, y + 2, z));
                        // sourceBeep.setBuffer(bufAves.getBufferId());
                        // sourceBeep.setReproducirAlInicio(true);
                        // sourceBeep.setGain(0.02f);
                        //// sourceBeep.play();
                        // pino.agregarComponente(sourceBeep);
                        // } catch (Exception e) {
                        // e.printStackTrace();
                        // }
                        escena.addEntity(pino);
                    }
                }
                //// ARBOL
                if (arboles1) {
                    if (i % 10 == 0) {

                        x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() - terreno.getInicioX();
                        z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() - terreno.getInicioZ();
                        y = terreno.getAltura(x, z);

                        Entity arbol1 = new Entity();
                        arbol1.addComponent(arbolG);
                        arbol1.move(x, y + 2, z);
                        arbol1.scale(2f, 2f, 2f);
                        escena.addEntity(arbol1);
                    }
                }

                // ARBOL 2
                if (arboles2) {
                    if (i % 12 == 0) {

                        x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() - terreno.getInicioX();
                        z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() - terreno.getInicioZ();
                        y = terreno.getAltura(x, z);

                        Entity arbol1 = new Entity();
                        arbol1.addComponent(arbol2G);
                        arbol1.move(x, y, z);
                        arbol1.scale(0.01f, 0.01f, 0.01f);
                        escena.addEntity(arbol1);
                    }
                }

                // ARBOL muerto
                if (arbolesMuertos) {
                    if (i % 8 == 0) {

                        x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() - terreno.getInicioX();
                        z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() - terreno.getInicioZ();
                        y = terreno.getAltura(x, z);

                        Entity arbol1 = new Entity("arbol_muerto");
                        arbol1.addComponent(arbolMuerto);
                        arbol1.move(x, y, z);
                        arbol1.scale(0.3f, 0.3f, 0.3f);
                        escena.addEntity(arbol1);
                    }
                }

                // ARBOL 3
                // if (arboles3) {
                // if (i % 13 == 0) {
                // x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() -
                // terreno.getInicioX();
                // z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() -
                // terreno.getInicioZ();
                // y = terreno.getAltura(x, z);
                //
                // Entity arbol1 = new Entity("arbol_3");
                // arbol1.agregarComponente(QMaterialUtil.aplicarColor(arbol3.get(0), 1,
                // QColor.GREEN, QColor.WHITE, 0, 64));
                // arbol1.agregarComponente(QMaterialUtil.aplicarColor(arbol3.get(1), 1,
                // QColor.BROWN, QColor.WHITE, 0, 64));
                // arbol1.mover(x, y, z);
                // arbol1.escalar(0.4f, 0.4f, 0.4f));
                // universo.addEntity(arbol1);
                // }
                // }
                // Roca 1
                if (rocas) {
                    if (i % 10 == 0) {
                        x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() - terreno.getInicioX();
                        z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() - terreno.getInicioZ();
                        y = terreno.getAltura(x, z);

                        Entity roca = new Entity("roca");
                        roca.addComponent(roca1);
                        roca.move(x, y, z);
                        roca.scale(1f, 1f, 1f);
                        escena.addEntity(roca);
                    }
                }

                // lampara
                if (lamparas) {
                    if (i % 10 == 0) {
                        x = rnd.nextFloat() * terreno.getWidth() - terreno.getWidth() - terreno.getInicioX();
                        z = rnd.nextFloat() * terreno.getHeight() - terreno.getHeight() - terreno.getInicioZ();
                        y = terreno.getAltura(x, z);

                        Entity lampara = GeneradorLamparas.crearLamparaPiso();
                        lampara.move(x, y, z);

                        QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.ESTATICO);
                        rigido.setMasa(5, QVector3.zero);
                        rigido.tipoContenedorColision = QComponenteColision.TIPO_CONTENEDOR_AABB;
                        rigido.setFormaColision(new QColisionCaja(0.25f, 2.25f, 0.25f));
                        lampara.addComponent(rigido);

                        escena.addEntity(lampara);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crearTerreno(Scene universo) {
        // el terreno generado con mapas de altura
        logger.info("Creando terreno...");

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

        Terrain terreno = new HeightMapTerrain(new File("assets/heightmaps/heightmap.png"), 1, -5, 15f,
                1,
                materialTerrain, true);
        entidadTerreno.addComponent(terreno);

        // QColisionTerreno colision = new QColisionTerreno(terreno);
        // entidadTerreno.agregarComponente(colision);
        QColisionMallaIndexada colision = new QColisionMallaIndexada(QUtilComponentes.getMesh(entidadTerreno));
        QObjetoRigido terrenoRigidez = new QObjetoRigido(QObjetoDinamico.ESTATICO);
        terrenoRigidez.setFormaColision(colision);
        entidadTerreno.addComponent(terrenoRigidez);
        universo.addEntity(entidadTerreno);
        logger.info("Terreno cargado...");
    }

    private void crearLago1(Scene universo) {
        // CREACION DEL LAGO
        // Lago
        QMaterialBas material = new QMaterialBas("Lago");
        material.setTransAlfa(0.4f);// 40% ( transparencia del 60%)
        material.setColorBase(new QColor(1, 0.f, 0.f, 0.7f));
        material.setSpecularExponent(64);

        // QTextura mapaNormal = null;
        //
        // try {
        // mapaNormal = QGestorRecursos.getTextura("lagoNormal");
        // material.setMapaNormal(new QProcesadorSimple(mapaNormal));
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        Entity agua = new Entity("Agua");

        // puedo agregar la razon que sea necesaria no afectara a la textura de
        // reflexixon porq esta calcula las coordenadas UV en tiempo de renderizado
        agua.addComponent(QMaterialUtil.aplicarMaterial(new QPlano(150, 150), material));
        WaterDuDv procesador = new WaterDuDv(universo, anchoReflejo, altoReflejo);
        agua.addComponent(procesador);
        material.setMapaNormal(new QProcesadorSimple(procesador.getTextNormal()));
        material.setMapaColor(procesador.getOutputTexture());
        agua.move(120, 0.1f, -120);
        agua.rotate((float) Math.toRadians(90), 0, 0);
        universo.addEntity(agua);
    }

    private void crearLago2(Scene universo) {
        // CREACION DEL LAGO
        // Lago
        QMaterialBas material = new QMaterialBas("Lago");

        material.setTransAlfa(0.7f);// 70% ( transparencia del 60%)
        material.setColorBase(new QColor(1, 0.f, 0.f, 0.7f));
        material.setSpecularExponent(64);

        // QTextura mapaNormal = null;
        //
        // try {
        // mapaNormal = QGestorRecursos.getTextura("lagoNormal");
        // material.setMapaNormal(new QProcesadorSimple(mapaNormal));
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        Entity agua = new Entity("Agua");

        // puedo agregar la razon que sea necesaria no afectara a la textura de
        // reflexixon porq esta calcula las coordenadas UV en tiempo de renderizado
        agua.addComponent(QMaterialUtil.aplicarMaterial(new QPlano(150, 150), material));
        WaterDuDv procesador = new WaterDuDv(universo, anchoReflejo, altoReflejo);
        agua.addComponent(procesador);
        material.setMapaNormal(new QProcesadorSimple(procesador.getTextNormal()));
        material.setMapaColor(procesador.getOutputTexture());
        agua.move(-90, -1, 120);
        agua.scale(2, 1.3f, 1);

        agua.rotate((float) Math.toRadians(90), 0, 0);

        universo.addEntity(agua);
    }

    private void loadTextures() {
        logger.info("Cargando textures...");
        AssetManager.get().loadTexture("terreno", "assets/textures/terreno/text4.jpg");
        AssetManager.get().loadTexture("lagoNormal", "assets/textures/agua/matchingNormalMap.png");
        logger.info("Texturas cargadas...");
    }

    private void crearCielo(Scene universo) {
        // cielo
        // Entity cielo = new Entity("Cielo");
        // QGeometria cieloG = new QEsfera(universo.UM.convertirPixel(500,
        // QUnidadMedida.METRO));
        // QMaterialUtil.aplicarTexturaEsfera(cieloG, new
        // File("res/textures/cielo/esfericos/cielo3.jpg"));
        // QNormales.invertirNormales(cieloG);
        // cielo.agregarComponente(cieloG);
        //
        // universo.addEntity(cielo);
        //
        // //luz ambiente
        // universo.luzAmbiente = 0.5f;
        // QLuz luz = new QLuzDireccional(1.5f, QColor.WHITE, true, 1000,
        // QVector3.of(0.5f, -1, 0));
        //
        // Entity sol = new Entity("Sol");
        // sol.agregarComponente(luz);
        // universo.addEntity(sol);

        // sol
        Entity sol = new Entity("Sol");
        QDirectionalLigth solLuz = new QDirectionalLigth(1.1f, QColor.WHITE, 1, QVector3.of(0, 0, 0), true, true);
        sol.addComponent(solLuz);
        universo.addEntity(sol);

        QTextura cieloDia = AssetManager.get().loadTexture("dia",
                "assets/textures/cielo/esfericos/cielo_dia.jpg");
        QTextura cieloNoche = AssetManager.get().loadTexture("noche",
                "assets/textures/cielo/esfericos/cielo_noche.png");
        // QTextura cieloNoche = QGestorRecursos.loadTexture("noche",
        // "res/textures/cielo/esfericos/cielo_noche_2.jpg");

        universo.setAmbientColor(QColor.LIGHT_GRAY);
        QCielo cielo = new QEsferaCielo(cieloDia, cieloNoche, universo.UM.convertirPixel(500, QUnidadMedida.METRO));
        universo.addEntity(cielo.getEntidad());
    }

    public HeightMapTerrain getTerreno() {
        return terreno;
    }

    public void setTerreno(HeightMapTerrain terreno) {
        this.terreno = terreno;
    }

}
