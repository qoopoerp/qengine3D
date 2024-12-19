/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.shadows;

import java.io.File;
import java.io.FileNotFoundException;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlanarMesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCaja;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QEsfera;
import net.qoopo.engine.core.entity.component.mesh.util.QUnidadMedida;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 *
 * @author alberto
 */
public class SombrasDireccional extends MakeTestScene {

        public void make(Scene mundo) {
                this.scene = mundo;

                QDirectionalLigth sol = new QDirectionalLigth(1.5f, QColor.WHITE, 50, QVector3.of(0, -1f, 0), true,
                                true);
                Entity luzEntidad = new Entity("Sol");
                luzEntidad.addComponent(sol);
                mundo.addEntity(luzEntidad);

                QMaterialBas mat1 = new QMaterialBas();
                mat1.setMapaColor(new QProcesadorSimple(
                                AssetManager.get().loadTexture("difusa",
                                                new File("assets/textures/testNormal/cajaColor.jpg"))));
                mat1.setMapaNormal(new QProcesadorSimple(
                                AssetManager.get().loadTexture("normal",
                                                new File("assets/textures/testNormal/cajaNormal.jpg"))));

                Entity cuboEntidad = new Entity("cubo");
                cuboEntidad.move(0, 5, 0);
                cuboEntidad.addComponent(QMaterialUtil.aplicarMaterial(new QCaja(2), mat1));
                mundo.addEntity(cuboEntidad);

                QMaterialBas mat2 = new QMaterialBas();
                mat2.setMapaColor(new QProcesadorSimple(
                                AssetManager.get().loadTexture("difusa2",
                                                new File("assets/textures/fuego/fuego4.png"))));
                mat2.setTransparencia(true);
                mat2.setColorTransparente(QColor.BLACK);
                Entity cuboEntidad2 = new Entity("cubo 2");
                cuboEntidad2.move(0, 5, -2);
                // cuboEntidad2.agregarComponente(QMaterialUtil.aplicarTexturaCubo(new QCaja(8),
                // new File("res/textures/transparent.png"), null, 1));
                cuboEntidad2.addComponent(QMaterialUtil.aplicarMaterial(new QCaja(4), mat2));

                mundo.addEntity(cuboEntidad2);

                Mesh esfera = new QEsfera(mundo.UM.convertirPixel(25, QUnidadMedida.CENTIMETRO));
                Entity esferaEntidad = new Entity("esfera");
                esferaEntidad.move(-2, 2, 2);
                esferaEntidad.addComponent(esfera);
                mundo.addEntity(esferaEntidad);

                // Entity plano = new Entity("plano");
                // plano.agregarComponente(new QPlano(50, 50));
                // mundo.addEntity(plano);
                Entity malla = new Entity("malla");
                malla.addComponent(new PlanarMesh(true, 5, 5, 5, 5));
                mundo.addEntity(malla);

                Entity pino1;
                try {
                        pino1 = AssetManager.get().loadModel(new File(
                                        "assets/models/obj/VEGETACION/EXTERIOR/baja_calidad/pino/lowpolytree.obj"));
                        pino1.move(3, 0, 0);
                        pino1.scale(2, 2, 2);
                        mundo.addEntity(pino1);
                } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
