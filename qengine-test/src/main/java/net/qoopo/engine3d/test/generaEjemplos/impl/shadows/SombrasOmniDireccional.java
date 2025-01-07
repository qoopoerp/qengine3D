/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.shadows;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlanarMesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Teapot;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Torus;
import net.qoopo.engine.core.entity.component.mesh.util.QUnidadMedida;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.mesh.NormalUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 *
 * @author alberto
 */
public class SombrasOmniDireccional extends MakeTestScene {

        public void make(Scene mundo) {
                this.scene = mundo;

                // luces
                Entity luzCentral = new Entity("Luz_central");
                // luzEntidad.mover(0, 0.5f, 10f);
                QPointLigth luz = new QPointLigth(1f, QColor.WHITE, 30, true, true);
                luzCentral.addComponent(luz);
                mundo.addEntity(luzCentral);

                //
                // Entity luz2 = new Entity("luz_2");
                // luz2.mover(-2, 0.5f, 2f);
                // luz2.agregarComponente(new QLuzPuntual(1f, QColor.WHITE, true, 30));
                // mundo.addEntity(luz2);
                //
                // Entity luz3 = new Entity("luz_3");
                // luz3.mover(-2, 1.5f, -2f);
                // luz3.agregarComponente(new QLuzPuntual(1f, QColor.WHITE, true, 30));
                // mundo.addEntity(luz3);
                //
                // Entity luz4 = new Entity("luz_4");
                // luz4.mover(2, -1f, -2f);
                // luz4.agregarComponente(new QLuzPuntual(1f, QColor.WHITE, true, 30));
                // mundo.addEntity(luz4);
                //
                //
                Entity toro = new Entity("toro");
                toro.addComponent(new Torus(1, 0.4f));
                toro.move(0, -2.5f, 0);
                mundo.addEntity(toro);

                Entity cuboEntidad = new Entity("cubo");
                cuboEntidad.move(-3, 1, 0);
                Material mat1 = new Material();
                mat1.setMapaColor(AssetManager.get().loadTexture("difusa",
                                new File("assets/textures/testNormal/cajaColor.jpg")));
                mat1.setMapaNormal(AssetManager.get().loadTexture("normal",
                                new File("assets/textures/testNormal/cajaNormal.jpg")));

                cuboEntidad.addComponent(MaterialUtil.applyMaterial(new Box(2), mat1));
                mundo.addEntity(cuboEntidad);

                Entity cuboEntidad2 = new Entity("cubo 2");
                cuboEntidad2.move(0, 2, -2);
                Material mat2 = new Material();
                mat2.setMapaColor(AssetManager.get().loadTexture("difusa2",
                                new File("assets/textures/fuego/fuego4.png")));
                cuboEntidad2.addComponent(MaterialUtil.applyMaterial(new Box(2), mat2));

                mundo.addEntity(cuboEntidad2);

                Entity esfera = new Entity("esfera");
                esfera.getTransformacion().trasladar(2f, 0, 0f);
                esfera.addComponent(new Sphere(mundo.UM.convertirPixel(25, QUnidadMedida.CENTIMETRO)));
                mundo.addEntity(esfera);

                Entity tetera = new Entity("tetera");
                tetera.addComponent(new Teapot());
                tetera.move(2f, -2f, -2f);
                tetera.rotate(0, Math.toRadians(80), 0);
                mundo.addEntity(tetera);

                Entity cubo = new Entity("cubo");
                cubo.addComponent(new Box(0.5f));
                cubo.move(0, 0, 1);
                mundo.addEntity(cubo);

                Entity cubo2 = new Entity("cubo2");
                cubo2.addComponent(new Box(0.5f));
                cubo2.move(-1.5f, 0.5f, 0);
                mundo.addEntity(cubo2);

                Entity malla = new Entity("malla");
                malla.addComponent(new PlanarMesh(true, 5, 5, 5f, 5));
                mundo.addEntity(malla);

                Entity pared1 = new Entity("Caja");
                // pared1.mover(0, -1, 0);
                pared1.addComponent(NormalUtil.invertirNormales(new Box(30f, 30f, 30f)));
                mundo.addEntity(pared1);

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
