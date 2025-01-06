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
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.GeoSphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.IcoSphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.SphereBox;
import net.qoopo.engine.core.entity.component.modifier.generate.InflateModifier;
import net.qoopo.engine.core.entity.component.modifier.generate.SubdivisionModifier;
import net.qoopo.engine.core.entity.component.movement.RotationComponent;
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
public class SubdivisionModifierTest extends MakeTestScene {

        public void make(Scene scene) {
                this.scene = scene;
                QTextura text = AssetManager.get().loadTexture("uvmpap", new File("assets/textures/uv.png"));
                QMaterialBas material = new QMaterialBas(text);

                // original
                Entity icoSphere0 = new Entity("IcoSphere0");
                icoSphere0.addComponent(MaterialUtil.applyMaterial(new IcoSphere(1, 0),
                                material));
                icoSphere0.move(-6, 0, -2);
                icoSphere0.addComponent(new RotationComponent());
                scene.addEntity(icoSphere0);

                Entity ico1 = new Entity("IcoSphere1");
                ico1.addComponent(MaterialUtil.applyMaterial(new IcoSphere(1, 1),
                                material));
                ico1.move(-2, 0, -4);
                ico1.addComponent(new RotationComponent());
                scene.addEntity(ico1);

                Entity ico2 = new Entity("IcoSphere2");
                ico2.addComponent(MaterialUtil.applyMaterial(new IcoSphere(1, 2),
                                material));
                ico2.move(0, 0, -4);
                ico2.addComponent(new RotationComponent());
                scene.addEntity(ico2);

                Entity ico3 = new Entity("IcoSphere3");
                ico3.addComponent(MaterialUtil.applyMaterial(new IcoSphere(1, 3),
                                material));
                ico3.move(2, 0, -4);
                ico3.addComponent(new RotationComponent());
                scene.addEntity(ico3);

                addSubSimple(3, "IcosphereSimple", new IcoSphere(1, 0), scene, material, -2, 0, -2);
                addSubClark(3, "IcosphereClark", new IcoSphere(1, 0), scene, material, -2, 0, 0);

                // ------- geoesfera

                // original
                Entity geoSphere0 = new Entity("GeoSphere0");
                geoSphere0.addComponent(MaterialUtil.applyMaterial(new GeoSphere(1, 0),
                                material));
                geoSphere0.move(-6, 0, 4);
                geoSphere0.addComponent(new RotationComponent());
                scene.addEntity(geoSphere0);

                Entity geo1 = new Entity("GeoSphere1");
                geo1.addComponent(MaterialUtil.applyMaterial(new GeoSphere(1, 1),
                                material));
                geo1.move(-2, 0, 2);
                geo1.addComponent(new RotationComponent());
                scene.addEntity(geo1);

                Entity geo2 = new Entity("GeoSphere2");
                geo2.addComponent(MaterialUtil.applyMaterial(new GeoSphere(1, 2),
                                material));
                geo2.move(0, 0, 2);
                geo2.addComponent(new RotationComponent());
                scene.addEntity(geo2);

                Entity geo3 = new Entity("GeoSphere3");
                geo3.addComponent(MaterialUtil.applyMaterial(new GeoSphere(1, 3),
                                material));
                geo3.move(2, 0, 2);
                geo3.addComponent(new RotationComponent());
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
                sphereBox0.addComponent(new RotationComponent());
                scene.addEntity(sphereBox0);

                Entity sb1 = new Entity("SphereBox1");
                sb1.addComponent(MaterialUtil.applyMaterial(new SphereBox(1, 1),
                                material));
                sb1.move(-2, 0, 8);
                sb1.addComponent(new RotationComponent());
                scene.addEntity(sb1);

                Entity sb2 = new Entity("SphereBox2");
                sb2.addComponent(MaterialUtil.applyMaterial(new SphereBox(1, 2),
                                material));
                sb2.move(0, 0, 8);
                sb2.addComponent(new RotationComponent());
                scene.addEntity(sb2);

                Entity sb3 = new Entity("SphereBox3");
                sb3.addComponent(MaterialUtil.applyMaterial(new SphereBox(1, 3),
                                material));
                sb3.move(2, 0, 8);
                sb3.addComponent(new RotationComponent());
                scene.addEntity(sb3);

                addSubSimple(3, "SphereBoxSimple", new SphereBox(1, 0), scene, material, -2, 0, 10);
                addSubClark(3, "SphereBoxClark", new SphereBox(1, 0), scene, material, -2, 0, 12);

                // Entity box = new Entity("box");
                // box.addComponent(MaterialUtil.applyMaterial(new Box(), material));
                // box.move(-6, 0, 15);
                // scene.addEntity(box);

                // addSubSimple(2, "boxSimple", new Box(), scene, material, -2, 0, 14);
                // addSubClark(2, "boxClark", new Box(), scene, material, -2, 0, 16);

                // Entity suzane = new Entity("Suzane");
                // suzane.addComponent(MaterialUtil.applyMaterial(new Suzane(), material));
                // suzane.move(-6, 0, 21);
                // scene.addEntity(suzane);

                // addSubSimple(2, "SuzaneSimple", new Suzane(), scene, material, -2, 0, 20);
                // addSubClark(2, "SuzaneClark", new Suzane(), scene, material, -2, 0, 22);

                // Entity teapot = new Entity("teapot");
                // teapot.addComponent(MaterialUtil.applyMaterial(new Teapot(), material));
                // teapot.move(-6, 0, 25);
                // scene.addEntity(teapot);

                // addSubSimple(2, "teapotSimple", new Teapot(), scene, material, -2, 0, 24);
                // addSubClark(2, "teapotClark", new Teapot(), scene, material, -2, 0, 26);

        }

        private void addSubSimple(int times, String name, Mesh mesh, Scene scene, QMaterialBas material, float x,
                        float y,
                        float z) {
                for (int i = 0; i < times; i++) {
                        Entity item = new Entity(name + "_" + i);
                        item.addComponent(MaterialUtil.applyMaterial(mesh.clone(),
                                        material));
                        item.addComponent(new SubdivisionModifier(SubdivisionModifier.TYPE_SIMPLE, (i + 1)));
                        item.addComponent(new InflateModifier(1));
                        item.move(x + i * 2, y, z);
                        item.addComponent(new RotationComponent());
                        scene.addEntity(item);
                }

        }

        private void addSubClark(int times, String name, Mesh mesh, Scene scene, QMaterialBas material, float x,
                        float y,
                        float z) {
                for (int i = 0; i < times; i++) {
                        Entity item = new Entity(name + "_" + i);
                        item.addComponent(MaterialUtil.applyMaterial(mesh.clone(),
                                        material));
                        item.addComponent(new SubdivisionModifier(SubdivisionModifier.TYPE_CATMULL_CLARK, (i + 1)));
                        item.addComponent(new InflateModifier(1));
                        item.move(x + i * 2, y, z);
                        item.addComponent(new RotationComponent());
                        scene.addEntity(item);
                }

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
