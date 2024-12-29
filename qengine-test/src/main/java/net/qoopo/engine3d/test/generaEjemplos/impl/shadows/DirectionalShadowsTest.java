/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.shadows;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlanarMesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.entity.component.mesh.util.QUnidadMedida;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 *
 * @author alberto
 */
public class DirectionalShadowsTest extends MakeTestScene {

        public void make(Scene scene) {
                this.scene = scene;

                try {

                        // QMaterialBas mat1 = new QMaterialBas();
                        // mat1.setMapaColor(new QProcesadorSimple(
                        // AssetManager.get().loadTexture("difusa",
                        // new File("assets/textures/testNormal/cajaColor.jpg"))));
                        // mat1.setMapaNormal(new QProcesadorSimple(
                        // AssetManager.get().loadTexture("normal",
                        // new File("assets/textures/testNormal/cajaNormal.jpg"))));

                        Entity cuboEntidad = new Entity("cubo");
                        cuboEntidad.move(0, 5, 0);
                        // cuboEntidad.addComponent(QMaterialUtil.aplicarMaterial(new Box(2), mat1));
                        cuboEntidad.addComponent(new Box(2));
                        scene.addEntity(cuboEntidad);

                        QMaterialBas mat2 = new QMaterialBas();
                        mat2.setMapaColor(new QProcesadorSimple(
                                        AssetManager.get().loadTexture("difusa2",
                                                        new File("assets/textures/fuego/fuego4.png"))));
                        mat2.setTransparencia(true);
                        mat2.setColorTransparente(QColor.BLACK);
                        Entity cuboEntidad2 = new Entity("cubo 2");
                        cuboEntidad2.move(0, 5, -2);
                        // cuboEntidad2.agregarComponente(QMaterialUtil.aplicarTexturaCubo(new Box(8),
                        // new File("res/textures/transparent.png"), null, 1));
                        cuboEntidad2.addComponent(MaterialUtil.applyMaterial(new Box(4), mat2));

                        scene.addEntity(cuboEntidad2);

                        Mesh esfera = new Sphere(scene.UM.convertirPixel(25, QUnidadMedida.CENTIMETRO));
                        Entity esferaEntidad = new Entity("esfera");
                        esferaEntidad.move(-2, 2, 2);
                        esferaEntidad.addComponent(esfera);
                        scene.addEntity(esferaEntidad);

                        // Entity plano = new Entity("plano");
                        // plano.agregarComponente(new QPlano(50, 50));
                        // mundo.addEntity(plano);
                        Entity malla = new Entity("malla");
                        malla.addComponent(new PlanarMesh(true, 5, 5, 5, 5));
                        scene.addEntity(malla);

                        Entity tree = AssetManager.get().loadModel(new File("assets/models/fbx/lowpoly/tree.fbx"));
                        tree.scale(0.001f);
                        tree.move(6, -2, -6);
                        scene.addEntity(tree);
                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
