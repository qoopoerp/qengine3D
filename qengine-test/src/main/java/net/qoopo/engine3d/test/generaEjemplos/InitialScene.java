/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.impl.assedloader.TestLoadModel;
import net.qoopo.engine3d.test.generaEjemplos.impl.ligths.SunTestScene;
import net.qoopo.engine3d.test.generaEjemplos.impl.simple.HdriTest;

/**
 *
 * @author alberto
 */
public class InitialScene extends MakeTestScene {

        public void make(Scene scene) {
                try {
                        this.scene = scene;
                        Material material = new Material();
                        // material.setColorMap(AssetManager.get().loadTexture("difusa",
                        // new File("assets/textures/uv.png")));
                        material.setColor(QColor.CYAN);
                        material.setRoughness(0.5f);
                        material.setMetallic(0.5f);

                        // Entity box = new Entity("Box");
                        // // Mesh mesh = new Box(1.0f);
                        // Mesh mesh = new Suzane();
                        // mesh.applyMaterial(material);
                        // box.addComponent(mesh);
                        // // box.addComponent(new QColisionCaja(1, 1, 1));
                        // // box.move(0, 0, 3);
                        // scene.addEntity(box);

                        // Entity e2 = new Entity("suzane");
                        // Mesh mesh2 = new Suzane();
                        // mesh2.applyMaterial(material);
                        // e2.addComponent(mesh2);
                        // e2.move(-2, 0, 0);
                        // scene.addEntity(e2);

                        // Entity e3 = new Entity("teapot");
                        // Mesh mesh3 = new Teapot();
                        // // mesh3.applyMaterial(material);
                        // e3.addComponent(mesh3);
                        // e3.move(2, 0, 0);
                        // scene.addEntity(e3);

                        // Entity e4 = new Entity("sphere");
                        // Mesh mesh4 = new Sphere();
                        // mesh4.applyMaterial(material);
                        // e4.addComponent(mesh4);
                        // e4.move(0, 4, 0);
                        // scene.addEntity(e4);

                        // Entity e5 = new Entity("torus");
                        // Mesh mesh5 = new Torus();
                        // mesh5.applyMaterial(material);
                        // e5.addComponent(mesh5);
                        // // e5.addComponent(new RotationModifier((float) Math.toRadians(90.0f), 0,
                        // 0));
                        // e5.move(-2, 4, 0);
                        // scene.addEntity(e5);

                        List<MakeTestScene> ejemplo = new ArrayList<>();
                        // ejemplo.add(new TriangleTest());
                        // ejemplo.add(new TexturedCubeTest());
                        // ejemplo.add(new TexturedSphereTest());
                        // ejemplo.add(new TexturedCubesUniverseTest());
                        // ejemplo.add(new TexturedSphereUniverseTest());

                        // ejemplo.add(new PhysicsTest1());
                        // ejemplo.add(new EjemploFisica2());
                        // ejemplo.add(new EjemploSponza());
                        // ejemplo.add(new FisicaDisparar());

                        // ejemplo.add(new SubdivisionModifierTest());

                        // ejemplo.add(new EjmTexturaSistemaSolar());
                        // ejemplo.add(new Nieve());
                        // ejemplo.add(new Fuego());
                        // ejemplo.add(new Humo());
                        // ejemplo.add(new WaterDuDvTest());
                        // ejemplo.add(new LowPolyWaterTest());
                        // ejemplo.add(new RadialWaterTest());
                        // ejemplo.add(new WaterLakeTestScene());

                        // ejemplo.add(new TestLoadObj());
                        // ejemplo.add(new TestLoadMd5());
                        // ejemplo.add(new TestLoadSocuwan());
                        // ejemplo.add(new TestLoadDae());
                        // ejemplo.add(new TestLoadAssimp());
                        ejemplo.add(new TestLoadModel());
                        // ejemplo.add(new TestExportObj());

                        // ejemplo.add(new EjemploVehiculo());
                        // ejemplo.add(new EjemploVehiculoModelo());
                        // ejemplo.add(new EjmTexturaEsferaShaders());
                        // materiales Nodos
                        // ejemplo.add(new NodosSimple());
                        // ejemplo.add(new NodosSimple2());// textures
                        // ejemplo.add(new NodosSimple3());//reflejos
                        // ejemplo.add(new NodosSimple4());//refraccion
                        // ejemplo.add(new NodosSimple5());//vidrio (reflexion y refraccion) y mix de
                        // reflexion y refraccion
                        // ejemplo.add(new NodosUniversoBoxs());
                        // ejemplo.add(new NodosVarios());//Entorno, difuso, emisivo, reflexion
                        // materiales PBR
                        // ejemplo.add(new PbrTest());
                        // ejemplo.add(new PbrEnviromentTest());
                        // ejemplo.add(new PbrSphereTest());
                        // ejemplo.add(new PbrCubeTest());
                        // ejemplo.add(new PbrTeapotTest());
                        // -----------------------------------------
                        // ejemplo.add(new RotateItemsTest());
                        // ejemplo.add(new FloorTestScene());
                        ejemplo.add(new HdriTest());
                        // ejemplo.add(new EnviromentTest());
                        // ejemplo.add(new TestSimpleTerrain());
                        // ejemplo.add(new TestTerrainHeightMap());
                        // ejemplo.add(new TestTerrainProcedural());
                        // ejemplo.add(new SkyBoxTest());
                        ejemplo.add(new SunTestScene());
                        // ejemplo.add(new PointLigthsTest());
                        for (MakeTestScene ejem : ejemplo) {
                                ejem.make(scene);
                        }
                } catch (Exception ex) {
                        Logger.getLogger(InitialScene.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
