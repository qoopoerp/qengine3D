/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.impl.modifier.SubdivisionModifierTest;

/**
 *
 * @author alberto
 */
public class InitialScene extends MakeTestScene {

        public void make(Scene scene) {
                try {
                        this.scene = scene;
                        QMaterialBas material = new QMaterialBas();
                        material.setMapaColor(AssetManager.get().loadTexture("difusa",
                                        new File("assets/textures/uv.png")));

                        //// Agrega un objeto inicial

                        // Entity box = new Entity("Box");
                        // box.addComponent(new Box(1));
                        // box.addComponent(new QColisionCaja(1, 1, 1));
                        // scene.addEntity(box);

                        // // varios objetos basicos
                        // Entity box = new Entity("Box");
                        // box.addComponent(MaterialUtil.applyMaterial(new Box(1), material));
                        // box.addComponent(new QColisionCaja(1, 1, 1));
                        // box.move(5, 0, 5);
                        // box.addComponent(new RotationComponent());
                        // box.addComponent(new JumpComponent());
                        // scene.addEntity(box);

                        // Entity sphere = new Entity("Esfera");
                        // sphere.addComponent(MaterialUtil.applyMaterial(new Sphere(1), material));
                        // sphere.addComponent(new QColisionEsfera(1));
                        // sphere.move(0, 0, 5);
                        // sphere.addComponent(new RotationComponent());
                        // sphere.addComponent(new JumpComponent());
                        // scene.addEntity(sphere);

                        // Entity icoSphere = new Entity("IcoShpere");
                        // icoSphere.addComponent(MaterialUtil.applyMaterial(new IcoSphere(1),
                        // material));
                        // icoSphere.addComponent(new QColisionEsfera(1));
                        // icoSphere.move(-5, 0, 5);
                        // icoSphere.addComponent(new RotationComponent());
                        // scene.addEntity(icoSphere);

                        // Entity geoSphere = new Entity("GeoShpere");
                        // geoSphere.addComponent(MaterialUtil.applyMaterial(new GeoSphere(1),
                        // material));
                        // geoSphere.addComponent(new QColisionEsfera(1));
                        // geoSphere.move(-5, 0, 0);
                        // geoSphere.addComponent(new RotationComponent());
                        // scene.addEntity(geoSphere);

                        // Entity teapot = new Entity("teapot");
                        // Mesh teapotMesh = new Teapot();
                        // teapotMesh.applyMaterial(material);
                        // teapot.addComponent(teapotMesh);
                        // teapot.addComponent(new QColisionMallaConvexa(teapotMesh));
                        // teapot.move(0, 0, 0);
                        // teapot.addComponent(new RotationComponent());
                        // teapot.addComponent(new JumpComponent());
                        // scene.addEntity(teapot);

                        // Entity sphereBox = new Entity("sphere Box");
                        // sphereBox.addComponent(MaterialUtil.applyMaterial(new SphereBox(1),
                        // material));
                        // sphereBox.addComponent(new QColisionEsfera(1));
                        // sphereBox.move(5, 0, 0);
                        // sphereBox.addComponent(new RotationComponent());
                        // scene.addEntity(sphereBox);

                        // Entity torus = new Entity("torus");
                        // Torus torusMesh = new Torus(1, 0.25f);
                        // torusMesh.applyMaterial(material);
                        // torus.addComponent(torusMesh);
                        // torus.addComponent(new QColisionMallaConvexa(torusMesh));
                        // torus.move(5, 0, -5);
                        // torus.addComponent(new RotationComponent((float) Math.toRadians(.01f),
                        // (float) Math.toRadians(.01f), (float) Math.toRadians(-0.05f)));
                        // scene.addEntity(torus);

                        // Entity triangle = new Entity("triangle");
                        // Mesh triangleMesh = new Triangle(1.0f);
                        // triangleMesh.applyMaterial(material);
                        // triangle.addComponent(triangleMesh);
                        // triangle.addComponent(new QColisionMallaConvexa(triangleMesh));
                        // triangle.move(0, 0, -5);
                        // triangle.addComponent(new RotationComponent());
                        // scene.addEntity(triangle);

                        // Entity suzane = new Entity("suzane");
                        // Mesh suzaneMesh = new Suzane();
                        // suzaneMesh.applyMaterial(material);
                        // suzane.addComponent(suzaneMesh);
                        // suzane.addComponent(new QColisionMallaConvexa(suzaneMesh));
                        // suzane.move(-5, 0, -5);
                        // suzane.addComponent(new RotationComponent());
                        // suzane.addComponent(new JumpComponent(5.0f));
                        // scene.addEntity(suzane);

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
                        ejemplo.add(new SubdivisionModifierTest());
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
                        // ejemplo.add(new TestLoadObj());
                        // ejemplo.add(new TestLoadMd5());
                        // ejemplo.add(new TestLoadSocuwan());
                        // ejemplo.add(new TestLoadDae());
                        // ejemplo.add(new TestLoadAssimp());
                        // ejemplo.add(new TestLoadModel());
                        // ejemplo.add(new TestExportObj());

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
                        // ejemplo.add(new EjemploPBR2()); // esferas con diferentes valores de
                        // rugosidad y metalico , y un mapa de reflexiones
                        // ejemplo.add(new EjemploPBRTextura());
                        // ejemplo.add(new PBRSphereTest());
                        // ejemplo.add(new PBRBox());
                        // ejemplo.add(new PBRTetera());
                        // ejemplo.add(new EjemploPBR_CerberusGun());

                        // -----------------------------------------
                        // ejemplo.add(new RotateItemsTest());
                        // ejemplo.add(new TestSimpleTerrain());
                        // ejemplo.add(new TestTerrainHeightMap());
                        // ejemplo.add(new TestTerrainProcedural());
                        // ejemplo.add(new SkyBoxTest());
                        // ejemplo.add(new FloorTestScene());
                        // ejemplo.add(new SunTestScene());
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
