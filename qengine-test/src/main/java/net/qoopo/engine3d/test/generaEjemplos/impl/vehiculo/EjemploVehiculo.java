/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.vehiculo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCaja;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCilindroX;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlanarMesh;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaConvexa;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaIndexada;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QRueda;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QVehiculo;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QVehiculoControl;
import net.qoopo.engine.core.entity.component.terrain.HeightMapTerrain;
import net.qoopo.engine.core.entity.component.terrain.Terrain;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QUtilComponentes;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjemploVehiculo extends MakeTestScene {

    private static Logger logger = Logger.getLogger("test");

    @Override
    public void make(Scene mundo) {
        this.scene = mundo;
        // crearPiso(mundo);
        crearTerreno(mundo);
        crearVehiculo(mundo);
    }

    private void crearPiso(Scene mundo) {
        // piso
        Entity piso = new Entity("Piso", false);
        PlanarMesh geom = new PlanarMesh(20, 20, 20, 20);
        QColisionMallaConvexa colision = new QColisionMallaConvexa(geom);
        QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.ESTATICO, 0);
        rigido.setFormaColision(colision);

        piso.addComponent(geom);
        piso.addComponent(colision);
        piso.addComponent(rigido);
        piso.scale(10, 10, 10);
        mundo.addEntity(piso);
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


    private void crearVehiculo(Scene mundo) {

        float alturaLlantaConexion = 0.25f;
        float px = 0.5f;
        float pz = 0.8f;
        QMaterialBas material = new QMaterialBas("Vehículo");
        material.setColorBase(QColor.BLUE);

        Entity carro = new Entity();
        QCaja geom = new QCaja(0.5f, 1, 2);
        QMaterialUtil.aplicarMaterial(geom, material);
        carro.addComponent(geom);

        CollisionShape colision = new QColisionMallaConvexa(geom);
        carro.addComponent(colision);
        // CollisionShape colision = new QColisionCaja(2,1f,3);
        // QColisionCompuesta colisionCom = new QColisionCompuesta();
        // colisionCom.agregarHijo(colision, QVector3.of(0, 1f, 0));
        // carro.agregarComponente(colisionCom);

        QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.DINAMICO, 800);
        // rigido.setFormaColision(colisionCom);
        rigido.setFormaColision(colision);
        carro.addComponent(rigido);

        QVehiculo vehiculo = new QVehiculo(rigido);
        carro.addComponent(vehiculo);

        // ruedas
        QMaterialBas materialRueda = new QMaterialBas("Rueda");
        materialRueda.setColorBase(QColor.DARK_GRAY);

        QCilindroX forma = new QCilindroX(0.1f, 0.25f);
        QMaterialUtil.aplicarMaterial(forma, materialRueda);
        // --

        Entity rueda1E = new Entity("rueda1", false);// la entidad que se actualizara su transformacion con el
                                                     // vehiculo
        rueda1E.addComponent(forma.clone());
        rueda1E.move(-px, alturaLlantaConexion, pz);
        mundo.addEntity(rueda1E);

        Entity rueda2E = new Entity("rueda2", false);// la entidad que se actualizara su transformacion con el
                                                     // vehiculo
        rueda2E.addComponent(forma.clone());
        rueda2E.move(px, alturaLlantaConexion, pz);
        mundo.addEntity(rueda2E);

        Entity rueda3E = new Entity("rueda3", false);// la entidad que se actualizara su transformacion con el
                                                     // vehiculo
        rueda3E.addComponent(forma.clone());
        rueda3E.move(-px, alturaLlantaConexion, -pz);
        mundo.addEntity(rueda3E);
        Entity rueda4E = new Entity("rueda4", false);// la entidad que se actualizara su transformacion con el
                                                     // vehiculo
        rueda4E.addComponent(forma.clone());
        rueda4E.move(px, alturaLlantaConexion, -pz);
        mundo.addEntity(rueda4E);
        // ----------------
        // 1
        QRueda rueda1 = new QRueda();
        rueda1.setEntidadRueda(rueda1E);
        rueda1.setFriccion(1000f);
        rueda1.setFrontal(true);
        rueda1.setAncho(0.1f);
        rueda1.setRadio(0.25f);
        rueda1.setUbicacion(QVector3.of(-px, alturaLlantaConexion, pz));
        vehiculo.agregarRueda(rueda1);
        // 2
        QRueda rueda2 = new QRueda();
        rueda2.setEntidadRueda(rueda2E);
        rueda2.setFriccion(1000f);
        rueda2.setFrontal(true);
        rueda2.setAncho(0.1f);
        rueda2.setRadio(0.25f);
        rueda2.setUbicacion(QVector3.of(px, alturaLlantaConexion, pz));
        vehiculo.agregarRueda(rueda2);
        // 3

        QRueda rueda3 = new QRueda();
        rueda3.setEntidadRueda(rueda3E);
        rueda3.setFriccion(1000f);
        rueda3.setFrontal(false);
        rueda3.setAncho(0.1f);
        rueda3.setRadio(0.25f);
        rueda3.setUbicacion(QVector3.of(-px, alturaLlantaConexion, -pz));
        vehiculo.agregarRueda(rueda3);
        // 4

        QRueda rueda4 = new QRueda();
        rueda4.setEntidadRueda(rueda4E);
        rueda4.setFriccion(1000f);
        rueda4.setFrontal(false);
        rueda4.setAncho(0.1f);
        rueda4.setRadio(0.25f);
        rueda4.setUbicacion(QVector3.of(pz, alturaLlantaConexion, -pz));
        vehiculo.agregarRueda(rueda4);
        // control del vehiculo

        QVehiculoControl control = new QVehiculoControl(vehiculo);

        // agrego los componentes
        carro.addComponent(control);

        carro.move(0, 25, 0);
        // carro.agregarHijo(rueda1E);
        // carro.agregarHijo(rueda2E);
        // carro.agregarHijo(rueda3E);
        // carro.agregarHijo(rueda4E);
        mundo.addEntity(carro);
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {

    }

}
