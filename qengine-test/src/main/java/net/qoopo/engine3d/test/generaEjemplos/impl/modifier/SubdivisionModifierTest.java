/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.modifier;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.GeoSphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.IcoSphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.SphereBox;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Torus;
import net.qoopo.engine.core.entity.component.modifier.generate.InflateModifier;
import net.qoopo.engine.core.entity.component.modifier.generate.SubdivisionModifier;
import net.qoopo.engine.core.entity.component.movement.RotationEntityComponent;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class SubdivisionModifierTest extends MakeTestScene {

        public void make(Scene scene) {
                this.scene = scene;
                Texture text = AssetManager.get().loadTexture("uvmpap", new File("assets/textures/uv.png"));
                Material material = new Material(text);

                Mesh[] meshList = new Mesh[] { new Box(), new IcoSphere(1, 0), new GeoSphere(1, 0) };

                float startZ = -meshList.length * 2;
                for (int i = 0; i < meshList.length; i++) {
                        Mesh mesh = meshList[i];
                        addMesh(2, mesh.getName(), mesh, scene, material, startZ);
                        startZ += 2;
                }

        }

        /** Agrega una malla y por cada malla llama a la subdivisión simple y Clark */
        private void addMesh(int times, String name, Mesh mesh, Scene scene, Material material, float z) {
                Entity teapot = new Entity(name + "_");
                teapot.addComponent(MaterialUtil.applyMaterial(mesh.clone(), material));
                teapot.addComponent(new RotationEntityComponent());
                teapot.move(-6, 0, z);
                scene.addEntity(teapot);
                addSubSimple(times, name + "_Simple", mesh.clone(), scene, material, -2, 0, z - 1);
                addSubClark(times, name + "_Clark", mesh.clone(), scene, material, -2, 0, z + 1);
        }

        private void addSubSimple(int times, String name, Mesh mesh, Scene scene, Material material, float x,
                        float y,
                        float z) {
                for (int i = 0; i < times; i++) {
                        Entity item = new Entity(name + "_" + i);
                        item.addComponent(MaterialUtil.applyMaterial(mesh.clone(),
                                        material));
                        item.addComponent(new SubdivisionModifier(SubdivisionModifier.TYPE_SIMPLE, (i + 1)));
                        item.addComponent(new InflateModifier(1));
                        item.move(x + i * 2, y, z);
                        item.addComponent(new RotationEntityComponent());
                        scene.addEntity(item);
                }

        }

        private void addSubClark(int times, String name, Mesh mesh, Scene scene, Material material, float x,
                        float y,
                        float z) {
                for (int i = 0; i < times; i++) {
                        Entity item = new Entity(name + "_" + i);
                        item.addComponent(MaterialUtil.applyMaterial(mesh.clone(),
                                        material));
                        item.addComponent(new SubdivisionModifier(SubdivisionModifier.TYPE_CATMULL_CLARK, (i + 1)));
                        item.addComponent(new InflateModifier(1));
                        item.move(x + i * 2, y, z);
                        item.addComponent(new RotationEntityComponent());
                        scene.addEntity(item);
                }

        }

        private void makeIcos(Scene scene, Material material) {
                // original
                Entity icoSphere0 = new Entity("IcoSphere0");
                icoSphere0.addComponent(MaterialUtil.applyMaterial(new IcoSphere(1, 0),
                                material));
                icoSphere0.move(-6, 0, -2);
                icoSphere0.addComponent(new RotationEntityComponent());
                scene.addEntity(icoSphere0);

                Entity ico1 = new Entity("IcoSphere1");
                ico1.addComponent(MaterialUtil.applyMaterial(new IcoSphere(1, 1),
                                material));
                ico1.move(-2, 0, -4);
                ico1.addComponent(new RotationEntityComponent());
                scene.addEntity(ico1);

                Entity ico2 = new Entity("IcoSphere2");
                ico2.addComponent(MaterialUtil.applyMaterial(new IcoSphere(1, 2),
                                material));
                ico2.move(0, 0, -4);
                ico2.addComponent(new RotationEntityComponent());
                scene.addEntity(ico2);

                Entity ico3 = new Entity("IcoSphere3");
                ico3.addComponent(MaterialUtil.applyMaterial(new IcoSphere(1, 3),
                                material));
                ico3.move(2, 0, -4);
                ico3.addComponent(new RotationEntityComponent());
                scene.addEntity(ico3);

                addSubSimple(3, "IcosphereSimple", new IcoSphere(1, 0), scene, material, -2, 0, -2);
                addSubClark(3, "IcosphereClark", new IcoSphere(1, 0), scene, material, -2, 0, 0);

                // ------- geoesfera

                // original
                Entity geoSphere0 = new Entity("GeoSphere0");
                geoSphere0.addComponent(MaterialUtil.applyMaterial(new GeoSphere(1, 0),
                                material));
                geoSphere0.move(-6, 0, 4);
                geoSphere0.addComponent(new RotationEntityComponent());
                scene.addEntity(geoSphere0);

                Entity geo1 = new Entity("GeoSphere1");
                geo1.addComponent(MaterialUtil.applyMaterial(new GeoSphere(1, 1),
                                material));
                geo1.move(-2, 0, 2);
                geo1.addComponent(new RotationEntityComponent());
                scene.addEntity(geo1);

                Entity geo2 = new Entity("GeoSphere2");
                geo2.addComponent(MaterialUtil.applyMaterial(new GeoSphere(1, 2),
                                material));
                geo2.move(0, 0, 2);
                geo2.addComponent(new RotationEntityComponent());
                scene.addEntity(geo2);

                Entity geo3 = new Entity("GeoSphere3");
                geo3.addComponent(MaterialUtil.applyMaterial(new GeoSphere(1, 3),
                                material));
                geo3.move(2, 0, 2);
                geo3.addComponent(new RotationEntityComponent());
                scene.addEntity(geo3);

                addSubSimple(3, "GeoSphereSimple", new GeoSphere(1, 0), scene, material, -2, 0, 4);
                addSubClark(3, "GeoSphereClark", new GeoSphere(1, 0), scene, material, -2, 0, 6);

                /// spherebox
                ///
                /// // original
                Entity sphereBox0 = new Entity("SphereBox0");
                sphereBox0.addComponent(MaterialUtil.applyMaterial(new SphereBox(1, 0),
                                material));
                sphereBox0.move(-6, 0, 8);
                sphereBox0.addComponent(new RotationEntityComponent());
                scene.addEntity(sphereBox0);

                Entity sb1 = new Entity("SphereBox1");
                sb1.addComponent(MaterialUtil.applyMaterial(new SphereBox(1, 1),
                                material));
                sb1.move(-2, 0, 8);
                sb1.addComponent(new RotationEntityComponent());
                scene.addEntity(sb1);

                Entity sb2 = new Entity("SphereBox2");
                sb2.addComponent(MaterialUtil.applyMaterial(new SphereBox(1, 2),
                                material));
                sb2.move(0, 0, 8);
                sb2.addComponent(new RotationEntityComponent());
                scene.addEntity(sb2);

                Entity sb3 = new Entity("SphereBox3");
                sb3.addComponent(MaterialUtil.applyMaterial(new SphereBox(1, 3),
                                material));
                sb3.move(2, 0, 8);
                sb3.addComponent(new RotationEntityComponent());
                scene.addEntity(sb3);

                addSubSimple(3, "SphereBoxSimple", new SphereBox(1, 0), scene, material, -2, 0, 10);
                addSubClark(3, "SphereBoxClark", new SphereBox(1, 0), scene, material, -2, 0, 12);
        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
