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
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.impl.ligths.SunTestScene;
import net.qoopo.engine3d.test.generaEjemplos.impl.simple.RotateItemsTest;
import net.qoopo.engine3d.test.generaEjemplos.impl.simple.TexturedCubesUniverseTest;
import net.qoopo.engine3d.test.generaEjemplos.impl.simple.TexturedSphereUniverseTest;

/**
 *
 * @author alberto
 */
public class InitialScene extends MakeTestScene {

        public void make(Scene scene) {
                try {
                        this.scene = scene;
                        Material material = new Material();
                        material.setMapaColor(AssetManager.get().loadTexture("difusa",
                                        new File("assets/textures/uv.png")));

                        //// Agrega un objeto inicial
                        // Entity box = new Entity("Box");
                        // box.addComponent(new Box(1));
                        // box.addComponent(new QColisionCaja(1, 1, 1));
                        // scene.addEntity(box);

                        // Entity torus = new Entity("torus");
                        // Torus torusMesh = new Torus();
                        // torusMesh.applyMaterial(material);
                        // torus.addComponent(torusMesh);
                        // torus.addComponent(new RotationModifier((float) Math.toRadians(90.0f), 0,
                        // 0));
                        // torus.addComponent(new RotationEntityComponent());
                        // // torus.rotate(Math.toRadians(90), 0, 0);
                        // torus.move(-2,0,0);
                        // scene.addEntity(torus);

                        // Entity torus2 = new Entity("torus2");
                        // Torus2 torus2Mesh = new Torus2();
                        // torus2Mesh.applyMaterial(material);
                        // torus2.addComponent(torus2Mesh);
                        // torus2.move(2, 0, 0);
                        // torus2.addComponent(new RotationEntityComponent());
                        // scene.addEntity(torus2);

                        // cargar ejemplos

                        List<MakeTestScene> ejemplo = new ArrayList<>();
                        // ejemplo.add(new TriangleTest());
                        // ejemplo.add(new TexturedCubeTest());
                        // ejemplo.add(new TexturedSphereTest());
                        ejemplo.add(new TexturedCubesUniverseTest());
                        ejemplo.add(new TexturedSphereUniverseTest());
                        // ejemplo.add(new PrimitivesTest());
                        // ejemplo.add(new EjemploFisica1());
                        // ejemplo.add(new EjemploFisica2());
                        // ejemplo.add(new EjemploSponza());
                        // ejemplo.add(new FisicaDisparar());

                        // ejemplo.add(new SubdivisionModifierTest());
                        // ejemplo.add(new EjmTexturaTransparente());

                        // ejemplo.add(new EjmTexturaSistemaSolar());
                        // ejemplo.add(new EsferaAnimada());
                        // ejemplo.add(new Nieve());
                        // ejemplo.add(new Fuego());
                        // ejemplo.add(new Humo());
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
                        ejemplo.add(new RotateItemsTest());
                        // ejemplo.add(new TestSimpleTerrain());
                        // ejemplo.add(new TestTerrainHeightMap());
                        // ejemplo.add(new TestTerrainProcedural());
                        // ejemplo.add(new SkyBoxTest());
                        // ejemplo.add(new FloorTestScene());
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
