/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.nodos;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.cubemap.CubeMap;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Teapot;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.material.node.MaterialNode;
import net.qoopo.engine.core.material.node.core.QNodoEnlace;
import net.qoopo.engine.core.material.node.core.output.MaterialOutputNode;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorIluminacion;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorMix;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorReflexion;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorRefraccion;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorVidrio;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class NodosSimple5 extends MakeTestScene {

        public NodosSimple5() {

        }

        public void make(Scene mundo) {
                this.scene = mundo;

                // //agrego una esfera para cargar un mapa como entorno
                // Entity entorno = new Entity("Entorno");
                // QMaterialBas matEntorno = new QMaterialBas("Entorno");
                // matEntorno.setMapaColor(new
                // QProcesadorSimple(QGestorRecursos.loadTexture("difusa", new
                // File("assets/"+
                // "textures/entorno/hdri/exteriores/from_cubemap.jpg"))));
                // entorno.agregarComponente(QMaterialUtil.aplicarMaterial(QUtilNormales.invertirNormales(new
                // Sphere(50)), matEntorno));
                // mundo.addEntity(entorno);
                // Reflexion estandar
                // a cada entidad le agrego su generador de mapa de reflexion con un mapa cubico
                Entity cubo4 = new Entity("vidrioBAS");
                CubeMap mapa = new CubeMap(QGlobal.MAPA_CUPO_RESOLUCION);
                QMaterialBas mat4 = new QMaterialBas("Reflexion real");
                mat4.setColorBase(QColor.YELLOW);
                mat4.setMetalico(1);
                mat4.setIndiceRefraccion(1.45f);
                mat4.setMapaEntorno(new QProcesadorSimple(mapa.getTexturaEntorno()));
                mat4.setTipoMapaEntorno(CubeMap.FORMATO_MAPA_CUBO);
                cubo4.addComponent(MaterialUtil.applyMaterial(new Teapot(), mat4));
                cubo4.addComponent(mapa);
                mapa.aplicar(CubeMap.FORMATO_MAPA_CUBO, 1, 1.45f);
                cubo4.move(-6, 0.5f, 0);
                mundo.addEntity(cubo4);
                // ---------------------------------------------------------------------------------------
                // vidrio con pbr
                Entity cubo5 = new Entity("Vidrio Nodo");
                CubeMap mapa2 = new CubeMap(QGlobal.MAPA_CUPO_RESOLUCION);
                MaterialNode mat5 = new MaterialNode("Vidrio real Nodo");
                QNodoColorVidrio nodoVidrio = new QNodoColorVidrio(new QProcesadorSimple(mapa2.getTexturaEntorno()),
                                1.45f);
                nodoVidrio.setTipoMapaEntorno(CubeMap.FORMATO_MAPA_CUBO);
                QNodoColorIluminacion nodoDifuso = new QNodoColorIluminacion();
                // enlace que une la salida de la textura con con difuso
                QNodoEnlace enlace = new QNodoEnlace(nodoVidrio.getSaColor(), nodoDifuso.getEnColor());
                MaterialOutputNode nodosalida = new MaterialOutputNode();
                QNodoEnlace enlace2 = new QNodoEnlace(nodoDifuso.getSaColor(), nodosalida.getEnColor());
                mat5.setNodo(nodosalida);
                cubo5.addComponent(MaterialUtil.applyMaterial(new Teapot(), mat5));
                cubo5.addComponent(mapa2);
                mapa2.aplicar(CubeMap.FORMATO_MAPA_CUBO, 1, 0);
                cubo5.move(0, 0.5f, 0);
                mundo.addEntity(cubo5);
                // ---------------------------------------------------------------------------------------
                // vidrio con pbr con mix
                Entity cubo6 = new Entity("Mix");
                CubeMap mapa3 = new CubeMap(QGlobal.MAPA_CUPO_RESOLUCION);
                MaterialNode mat6 = new MaterialNode("Vidrio real Nodo");
                QNodoColorReflexion nodoReflejo = new QNodoColorReflexion(
                                new QProcesadorSimple(mapa3.getTexturaEntorno()));
                nodoReflejo.setTipoMapaEntorno(CubeMap.FORMATO_MAPA_CUBO);
                QNodoColorRefraccion nodoRefraccion = new QNodoColorRefraccion(
                                new QProcesadorSimple(mapa3.getTexturaEntorno()), 1.45f);
                nodoRefraccion.setTipoMapaEntorno(CubeMap.FORMATO_MAPA_CUBO);
                QNodoColorMix nodoMix = new QNodoColorMix(0.5f);
                QNodoColorIluminacion nodoDifuso6_1 = new QNodoColorIluminacion();

                // enlace que une la salida de la textura con con difuso
                QNodoEnlace enlace6_1 = new QNodoEnlace(nodoReflejo.getSaColor(), nodoMix.getEnColor1());
                QNodoEnlace enlace6_2 = new QNodoEnlace(nodoRefraccion.getSaColor(), nodoMix.getEnColor2());
                QNodoEnlace enlace6_3 = new QNodoEnlace(nodoMix.getSaColor(), nodoDifuso6_1.getEnColor());
                MaterialOutputNode nodosalida2 = new MaterialOutputNode();
                QNodoEnlace enlace3 = new QNodoEnlace(nodoMix.getSaColor(), nodosalida2.getEnColor());
                mat6.setNodo(nodosalida2);
                cubo6.addComponent(MaterialUtil.applyMaterial(new Teapot(), mat6));
                cubo6.addComponent(mapa3);
                mapa3.aplicar(CubeMap.FORMATO_MAPA_CUBO, 1, 0);
                cubo6.move(6, 0.5f, 0);
                mundo.addEntity(cubo6);

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
