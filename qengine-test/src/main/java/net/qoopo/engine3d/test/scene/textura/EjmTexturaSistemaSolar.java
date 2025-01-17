/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.textura;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.mesh.NormalUtil;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 *
 * @author alberto
 */
public class EjmTexturaSistemaSolar extends MakeTestScene {

        public void make(Scene mundo) {
                this.scene = mundo;

                Entity sky = new Entity("universe");
                Mesh universeGeometry = MaterialUtil.applyMaterial(new Sphere(20, 36), new Material(
                                AssetManager.get().loadTexture("sol", new File(
                                                "assets/textures/solar_system/2k_stars_milky_way.jpg"))));
                ((Material) universeGeometry.primitiveList[0].material).setEmision(1.0f);
                NormalUtil.invertirNormales(universeGeometry);
                sky.addComponent(universeGeometry);
                sky.move(QVector3.zero);

                mundo.addEntity(sky);

                Entity sol = new Entity("Sol");
                Mesh geomeSol = MaterialUtil.applyMaterial(new Sphere(2, 36), new Material(
                                AssetManager.get().loadTexture("sol",
                                                new File("assets/textures/solar_system/2k_sun.jpg"))));
                ((Material) geomeSol.primitiveList[0].material).setEmision(1.0f);
                sol.addComponent(geomeSol);
                sol.move(QVector3.zero);
                mundo.addEntity(sol);

                Entity mercurio = new Entity("Mercurio");
                mercurio.addComponent(MaterialUtil.applyMaterial(new Sphere(0.1f, 36),
                                new Material(AssetManager.get().loadTexture("mercurio",
                                                new File("assets/textures/solar_system/2k_mercury.jpg")))));
                mercurio.move(QVector3.of(4, 0, 0));
                mundo.addEntity(mercurio);

                Entity venus = new Entity("Venus");
                venus.addComponent(MaterialUtil.applyMaterial(new Sphere(0.15f, 36), new Material(AssetManager
                                .get()
                                .loadTexture("venus", new File(
                                                "assets/textures/solar_system/2k_venus_surface.jpg")))));
                venus.move(QVector3.of(5, 0, 0));
                mundo.addEntity(venus);

                Entity tierra = new Entity("Tierra");
                tierra.addComponent(MaterialUtil.applyMaterial(new Sphere(0.2f, 36), new Material(AssetManager
                                .get()
                                .loadTexture("venus", new File(
                                                "assets/textures/solar_system/2k_earth_daymap.jpg")))));
                tierra.move(QVector3.of(6, 0, 0));
                mundo.addEntity(tierra);

                Entity luna = new Entity("Luna");
                luna.addComponent(MaterialUtil.applyMaterial(new Sphere(0.02f, 36), new Material(AssetManager
                                .get()
                                .loadTexture("venus",
                                                new File("assets/textures/solar_system/2k_moon.jpg")))));
                luna.move(QVector3.of(6, 0, 0.35f));
                mundo.addEntity(luna);

                Entity marte = new Entity("Marte");
                marte.addComponent(MaterialUtil.applyMaterial(new Sphere(0.18f, 36), new Material(AssetManager
                                .get()
                                .loadTexture("venus",
                                                new File("assets/textures/solar_system/2k_mars.jpg")))));
                marte.move(QVector3.of(7, 0, 0));
                mundo.addEntity(marte);

                Entity jupiter = new Entity("Júpiter");
                jupiter.addComponent(MaterialUtil.applyMaterial(new Sphere(0.5f, 36), new Material(AssetManager
                                .get()
                                .loadTexture("venus",
                                                new File("assets/textures/solar_system/2k_jupiter.jpg")))));
                jupiter.move(QVector3.of(8.5f, 0, 0));
                mundo.addEntity(jupiter);

                Entity saturno = new Entity("Saturno");
                saturno.addComponent(MaterialUtil.applyMaterial(new Sphere(0.4f, 36), new Material(AssetManager
                                .get()
                                .loadTexture("venus",
                                                new File("assets/textures/solar_system/2k_saturn.jpg")))));
                saturno.move(QVector3.of(10.5f, 0, 0));
                mundo.addEntity(saturno);

                Entity urano = new Entity("Urano");
                urano.addComponent(MaterialUtil.applyMaterial(new Sphere(0.3f, 36), new Material(AssetManager
                                .get()
                                .loadTexture("venus",
                                                new File("assets/textures/solar_system/2k_uranus.jpg")))));
                urano.move(QVector3.of(11.5f, 0, 0));
                mundo.addEntity(urano);

                Entity neptuno = new Entity("Neptuno");
                neptuno.addComponent(
                                MaterialUtil.applyMaterial(new Sphere(0.25f, 36), new Material(AssetManager
                                                .get()
                                                .loadTexture("venus", new File("assets/"
                                                                + "textures/solar_system/2k_neptune.jpg")))));
                neptuno.move(QVector3.of(12.5f, 0, 0));
                mundo.addEntity(neptuno);

                Entity pluton = new Entity("Plutón");
                pluton.addComponent(MaterialUtil.applyMaterial(new Sphere(0.05f, 36), new Material(AssetManager
                                .get()
                                .loadTexture("venus", new File(
                                                "assets/textures/solar_system/2k_stars_milky_way.jpg")))));
                pluton.move(QVector3.of(13.5f, 0, 0));
                mundo.addEntity(pluton);

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
