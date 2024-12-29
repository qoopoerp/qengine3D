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

import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.impl.pbr.EjemploPBR2;
import net.qoopo.engine3d.test.generaEjemplos.impl.simple.SkyBoxTest;
import net.qoopo.engine3d.test.generaEjemplos.impl.simple.SunTestScene;

/**
 *
 * @author alberto
 */
public class InitialScene extends MakeTestScene {

        public void make(Scene scene) {
                try {
                        this.scene = scene;

                        //// Agrega un objeto inicial

                        // Entity box = new Entity("Box");
                        // box.addComponent(new Box(1));
                        // box.addComponent(new QColisionCaja(1, 1, 1));
                        // scene.addEntity(box);

                        // Entity torus = new Entity("torus");
                        // Torus torusMesh = new Torus(1, 0.5f);
                        // torus.addComponent(torusMesh);
                        // torus.addComponent(new QColisionMallaConvexa(torusMesh));
                        // scene.addEntity(torus);

                        // Entity box = new Entity("Box");
                        // box.addComponent(new Box(1));
                        // box.addComponent(new QColisionCaja(1, 1, 1));
                        // box.move(5, 0, 5);
                        // scene.addEntity(box);

                        // Entity sphere = new Entity("Esfera");
                        // sphere.addComponent(new Sphere(1));
                        // sphere.addComponent(new QColisionEsfera(1));
                        // sphere.move(0, 0, 5);
                        // scene.addEntity(sphere);

                        // Entity icoSphere = new Entity("IcoShpere");
                        // icoSphere.addComponent(new IcoSphere(1));
                        // icoSphere.addComponent(new QColisionEsfera(1));
                        // icoSphere.move(-5, 0, 5);
                        // scene.addEntity(icoSphere);

                        // Entity geoSphere = new Entity("GeoShpere");
                        // geoSphere.addComponent(new GeoSphere(1));
                        // geoSphere.addComponent(new QColisionEsfera(1));
                        // geoSphere.move(-5, 0, 0);
                        // scene.addEntity(geoSphere);

                        // Entity teapot = new Entity("teapot");
                        // Mesh teapotMesh = new Teapot();
                        // teapot.addComponent(teapotMesh);
                        // teapot.addComponent(new QColisionMallaConvexa(teapotMesh));
                        // teapot.move(0, 0, 0);
                        // scene.addEntity(teapot);

                        // Entity sphereBox = new Entity("sphere Box");
                        // sphereBox.addComponent(new SphereBox(1));
                        // sphereBox.addComponent(new QColisionEsfera(1));
                        // sphereBox.move(5, 0, 0);
                        // scene.addEntity(sphereBox);

                        // Entity torus = new Entity("torus");
                        // Torus torusMesh = new Torus(1, 0.25f);
                        // torus.addComponent(torusMesh);
                        // torus.addComponent(new QColisionMallaConvexa(torusMesh));
                        // torus.move(5, 0, -5);
                        // scene.addEntity(torus);

                        // // agrega una luz
                        // QEntity luz = new QEntity("Luz");
                        // luz.move(4, 5, 1);
                        // luz.addComponent(new QLuzPuntual());
                        // scene.addEntity(luz);
                        // // segunda luz
                        // QEntity luz2 = new QEntity("Luz");
                        // luz2.move(-4, -5, -1);
                        // luz2.addComponent(new QLuzPuntual());
                        // scene.addEntity(luz2);

                        // cargar ejemplos

                        List<MakeTestScene> ejemplo = new ArrayList<>();
                        // ejemplo.add(new TriangleTest());
                        // ejemplo.add(new TexturedCubeTest());
                        // ejemplo.add(new TexturedSphereTest());
                        // ejemplo.add(new TexturedCubesUniverseTest());
                        // ejemplo.add(new TexturedSphereUniverseTest());
                        // ejemplo.add(new PrimitivesTest());
                        // ejemplo.add(new EjemplRotarItems());
                        // ejemplo.add(new EjemploFisica1());
                        // ejemplo.add(new EjemploFisica2());
                        // ejemplo.add(new EjemploSponza());
                        // ejemplo.add(new FisicaDisparar());
                        // ejemplo.add(new EjmDivision());
                        // ejemplo.add(new EjmTexturaTransparente());

                        // ejemplo.add(new EjmTexturaSistemaSolar());
                        // ejemplo.add(new EsferaAnimada());
                        // ejemplo.add(new Nieve());
                        // ejemplo.add(new Fuego());
                        // ejemplo.add(new Humo());
                        // ejemplo.add(new Espejos());
                        // ejemplo.add(new WaterDuDvTest());
                        // ejemplo.add(new LowPolyWaterTest());
                        // ejemplo.add(new RadialWaterTest());
                        // ejemplo.add(new WaterLakeTestScene());
                        // ejemplo.add(new DirectionalShadowsTest());
                        // ejemplo.add(new SombrasOmniDireccional());
                        // ejemplo.add(new SombrasOmniDireccional2());
                        // ejemplo.add(new TestLoadMd5());
                        // ejemplo.add(new TestLoadObj());
                        // ejemplo.add(new TestLoadSocuwan());
                        // ejemplo.add(new TestLoadDae());
                        // ejemplo.add(new TestLoadAssimp());
                        // ejemplo.add(new EjemploVehiculo());
                        // ejemplo.add(new EjemploVehiculoModelo());
                        // ejemplo.add(new EjmTexturaEsferaShaders());
                        // -------------------------------
                        // ejemplo.add(new EjmRefraccion());
                        // ejemplo.add(new EjmReflexion());
                        // materiales Nodos
                        // ejemplo.add(new NodosSimple());
                        // ejemplo.add(new NodosSimple2());// textures
                        // ejemplo.add(new NodosSimple3());//reflejos
                        // ejemplo.add(new NodosSimple4());//refraccion
                        // ejemplo.add(new NodosSimple5());//vidrio (reflexion y refraccion) y mix de
                        // reflexion y refraccion
                        // ejemplo.add(new NodosUniversoBoxs());//Universo Boxs
                        // ejemplo.add(new NodosVarios());//Entorno, difuso, emisivo, reflexion
                        // materiales PBR
                        // ejemplo.add(new EjemploPBR()); // esferas con diferentes valores de rugosidad
                        // y metalico
                        ejemplo.add(new EjemploPBR2()); // esferas con diferentes valores de
                        // rugosidad y metalico , y un mapa de reflexiones
                        // ejemplo.add(new EjemploPBRTextura());
                        // ejemplo.add(new PBREsfera());
                        // ejemplo.add(new PBRBox());
                        // ejemplo.add(new PBRTetera());
                        // ejemplo.add(new EjemploPBR_CerberusGun());

                        // -----------------------------------------
                        // ejemplo.add(new RotateItemsTest());
                        // ejemplo.add(new TestSimpleTerrain());
                        // ejemplo.add(new TestTerrainHeightMap());
                        // ejemplo.add(new TestTerrainProcedural());
                        ejemplo.add(new SkyBoxTest());
                        // ejemplo.add(new FloorTestScene());
                        ejemplo.add(new SunTestScene());
                        // ejemplo.add(new EjemploLuces());
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
