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
import net.qoopo.engine3d.test.generaEjemplos.impl.assedloader.TestLoadSocuwan;

/**
 *
 * @author alberto
 */
public class InitialScene extends MakeTestScene {

        public void make(Scene scene) {
                try {
                        this.scene = scene;

                        //// Agrega un objeto inicial
                        // Entity objeto = new Entity("Cubo");
                        // objeto.agregarComponente(new QCaja(1));
                        // objeto.agregarComponente(new QColisionCaja(1, 1, 1));
                        // scene.addEntity(objeto);
                        // Entity objeto = new Entity("Esfera");
                        // objeto.agregarComponente(new QEsfera(1));
                        // objeto.agregarComponente(new QColisionEsfera(1));
                        // scene.addEntity(objeto);

                        // QEntity objeto = new QEntity("teapot");
                        // QGeometria teapot = new QTeapot();
                        // objeto.agregarComponente(teapot);
                        // objeto.agregarComponente(new QColisionMallaConvexa(teapot));
                        // scene.addEntity(objeto);

                        // // agrega una luz
                        // QEntity luz = new QEntity("Luz");
                        // luz.move(4, 5, 1);
                        // luz.agregarComponente(new QLuzPuntual());
                        // scene.addEntity(luz);
                        // // segunda luz
                        // QEntity luz2 = new QEntity("Luz");
                        // luz2.move(-4, -5, -1);
                        // luz2.agregarComponente(new QLuzPuntual());
                        // scene.addEntity(luz2);

                        // cargar ejemplos

                        List<MakeTestScene> ejemplo = new ArrayList<>();
                        // ejemplo.add(new UniversoCubos());
                        // ejemplo.add(new UniversoEsferas());
                        // ejemplo.add(new Ejemplo2());
                        // ejemplo.add(new EjemplRotarItems());
                        // ejemplo.add(new EjemploFisica1());
                        // ejemplo.add(new EjemploFisica2());
                        // ejemplo.add(new EjemploSponza());
                        // ejemplo.add(new FisicaDisparar());
                        // ejemplo.add(new EjmDivision());
                        // ejemplo.add(new EjmTexturaTransparente());
                        // ejemplo.add(new EjmTexturaCubo());
                        // ejemplo.add(new EjmTexturaEsfera());
                        // ejemplo.add(new EjmTexturaSistemaSolar());
                        // ejemplo.add(new EsferaAnimada());
                        // ejemplo.add(new Nieve());
                        // ejemplo.add(new Fuego());
                        // ejemplo.add(new Humo());
                        // ejemplo.add(new Espejos());
                        // ejemplo.add(new Agua());
                        // ejemplo.add(new Laguna());
                        // ejemplo.add(new Rios());
                        // ejemplo.add(new SombrasDireccional());
                        // ejemplo.add(new SombrasOmniDireccional());
                        // ejemplo.add(new SombrasOmniDireccional2());
                        // ejemplo.add(new EjemCargaMD5());
                        // ejemplo.add(new TestLoadObj());
                        ejemplo.add(new TestLoadSocuwan());
                        // ejemplo.add(new TestLoadDae());
                        // ejemplo.add(new TestLoadAssimp());
                        // ejemplo.add(new Entorno());//Entorno
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
                        // ejemplo.add(new NodosUniversoCubos());//Universo cubos
                        // ejemplo.add(new NodosVarios());//Entorno, difuso, emisivo, reflexion
                        // materiales PBR
                        // ejemplo.add(new EjemploPBR()); // esferas con diferentes valores de rugosidad
                        // y metalico
                        // ejemplo.add(new EjemploPBR2()); // esferas con diferentes valores de
                        // rugosidad y metalico , y un mapa de reflexiones
                        // ejemplo.add(new EjemploPBRTextura());
                        // ejemplo.add(new PBREsfera());
                        // ejemplo.add(new PBRCubo());
                        // ejemplo.add(new PBRTetera());
                        // ejemplo.add(new EjemploPBR_CerberusGun());

                        // -----------------------------------------
                        // ejemplo.add(new EjemplRotarItems());
                        // ejemplo.add(new TestSimpleTerrain());
                        // ejemplo.add(new TestTerrainHeightMap());
                        // ejemplo.add(new TestTerrainProcedural());
                        // ejemplo.add(new Entorno());
                        // ejemplo.add(new Piso());
                        // ejemplo.add(new SolEjemplo());
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
