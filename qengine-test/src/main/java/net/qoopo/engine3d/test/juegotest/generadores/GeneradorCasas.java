/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.juegotest.generadores;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Cylinder;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Cone;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Plane;
import net.qoopo.engine.core.entity.component.particles.humo.QEmisorHumo;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;

/**
 * Clase para crear personajes del juego
 *
 * @author alberto
 */
public class GeneradorCasas {

    private static final QTextura TEXT_MURO = AssetManager.get().loadTexture("muro",
            "assets/textures/muro/muro2.jpg");

    // private static QGeometria
    private static QMaterialBas materialPisos = new QMaterialBas(TEXT_MURO, 0);
    private static Mesh pisoG = MaterialUtil.applyMaterial(new Box(2.5f, 5f, 5f), materialPisos);
    private static Mesh ventanaG = MaterialUtil.applyColor(new Plane(0.5f, 0.75f), 0.8f, 1, 1, 0, 1, 1, 1, 1, 64);
    private static Mesh puertaG = MaterialUtil.applyColor(new Plane(1f, 0.5f), 1f, QColor.GREEN, QColor.WHITE, 0,
            64);
    private static Mesh techoG = MaterialUtil.applyColor(new Cone(1, 5f, 4), 1,
            new QColor(1, 139f / 255f, 99f / 255f, 55f / 255f), QColor.GREEN, 0, 0);
    private static Mesh chimeneaG = MaterialUtil.applyColor(new Cylinder(0.6f, 0.25f, 4), 1, QColor.GRAY,
            QColor.GRAY, 0, 0);

    public Entity casa1() {
        Entity objeto = new Entity("Casa 1P");
        Entity piso1 = piso1();
        Entity techo = techo();
        techo.move(0, 1.25f + 0.5f, 0);
        objeto.addChild(piso1);
        objeto.addChild(techo);
        return objeto;
    }

    public Entity casa2Pisos() {
        Entity objeto = new Entity("Casa 2P");
        Entity piso1 = piso1();
        Entity piso2 = piso();
        piso2.move(0, 1.25f + 1.25f, 0);
        Entity techo = techo();
        techo.move(0, 1.25f + 0.5f + 2.5f, 0);
        objeto.addChild(piso1);
        objeto.addChild(piso2);
        objeto.addChild(techo);
        return objeto;
    }

    private Entity piso() {
        Entity piso = new Entity();

        piso.addComponent(pisoG);

        Entity ventana1 = new Entity("ventana");
        ventana1.addComponent(ventanaG);
        ventana1.addComponent(new QPointLigth(0.5f, QColor.YELLOW, 10, false, false));
        ventana1.move(-1.5f, 0.5f, 2.51f);
        piso.addChild(ventana1);

        Entity ventana2 = new Entity("ventana");
        ventana2.addComponent(ventanaG);
        ventana2.addComponent(new QPointLigth(0.5f, QColor.YELLOW, 10, false, false));
        ventana2.move(1.5f, 0.5f, 2.51f);
        piso.addChild(ventana2);

        return piso;
    }

    private Entity piso1() {
        Entity piso1 = piso();
        Entity puerta = new Entity("puerta");
        puerta.addComponent(puertaG);
        puerta.move(0, -0.75f, 2.51f);
        piso1.addChild(puerta);
        return piso1;
    }

    private Entity techo() {
        Entity techo = new Entity("techo");
        techo.addComponent(techoG);
        techo.rotate(0, Math.toRadians(45), 0);
        Entity chimenea = chimenea();
        chimenea.move(-1f, 0f, 0.75f);
        techo.addChild(chimenea);
        return techo;
    }

    private Entity chimenea() {
        Entity chimenea = new Entity("chimenea");
        chimenea.addComponent(chimeneaG);
        AABB ambitoHumo = new AABB(new Vertex(-0.15f, 0, -0.15f), new Vertex(.15f, 3.5f, .15f));//
        QEmisorHumo emisor = new QEmisorHumo(ambitoHumo, 5000, 400, 4);
        Entity emisorEn = new Entity();
        emisorEn.addComponent(emisor);
        emisorEn.move(0, 0.3f, 0);
        chimenea.addChild(emisorEn);
        return chimenea;
    }

}
