/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.nodos;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.environment.EnvProbe;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Teapot;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.material.node.MaterialNode;
import net.qoopo.engine.core.material.node.core.QNodoEnlace;
import net.qoopo.engine.core.material.node.core.output.MaterialOutputNode;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorIluminacion;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorMix;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorReflexion;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorRefraccion;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorVidrio;
import net.qoopo.engine3d.test.scene.MakeTestScene;

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
                EnvProbe mapa = new EnvProbe(QGlobal.MAPA_CUPO_RESOLUCION);
                Material mat4 = new Material("Reflexion real");
                mat4.setColor(QColor.YELLOW);
                mat4.setMetallic(1);
                mat4.setIor(1.45f);
                mat4.setEnvMap(mapa.getEnvMap());
                mat4.setEnvMapType(EnvProbe.FORMATO_MAPA_CUBO);
                cubo4.addComponent(MaterialUtil.applyMaterial(new Teapot(), mat4));
                cubo4.addComponent(mapa);
                mapa.aplicar(EnvProbe.FORMATO_MAPA_CUBO);
                cubo4.move(-6, 0.5f, 0);
                mundo.addEntity(cubo4);
                // ---------------------------------------------------------------------------------------
                // vidrio con pbr
                Entity cubo5 = new Entity("Vidrio Nodo");
                EnvProbe mapa2 = new EnvProbe(QGlobal.MAPA_CUPO_RESOLUCION);
                MaterialNode mat5 = new MaterialNode("Vidrio real Nodo");
                QNodoColorVidrio nodoVidrio = new QNodoColorVidrio(mapa2.getEnvMap(), 1.45f);
                nodoVidrio.setTipoMapaEntorno(EnvProbe.FORMATO_MAPA_CUBO);
                QNodoColorIluminacion nodoDifuso = new QNodoColorIluminacion();
                // enlace que une la salida de la textura con con difuso
                QNodoEnlace enlace = new QNodoEnlace(nodoVidrio.getSaColor(), nodoDifuso.getEnColor());
                MaterialOutputNode nodosalida = new MaterialOutputNode();
                QNodoEnlace enlace2 = new QNodoEnlace(nodoDifuso.getSaColor(), nodosalida.getEnColor());
                mat5.setNodo(nodosalida);
                cubo5.addComponent(MaterialUtil.applyMaterial(new Teapot(), mat5));
                cubo5.addComponent(mapa2);
                mapa2.aplicar(EnvProbe.FORMATO_MAPA_CUBO);
                cubo5.move(0, 0.5f, 0);
                mundo.addEntity(cubo5);
                // ---------------------------------------------------------------------------------------
                // vidrio con pbr con mix
                Entity cubo6 = new Entity("Mix");
                EnvProbe mapa3 = new EnvProbe(QGlobal.MAPA_CUPO_RESOLUCION);
                MaterialNode mat6 = new MaterialNode("Vidrio real Nodo");
                QNodoColorReflexion nodoReflejo = new QNodoColorReflexion(
                                mapa3.getEnvMap());
                nodoReflejo.setTipoMapaEntorno(EnvProbe.FORMATO_MAPA_CUBO);
                QNodoColorRefraccion nodoRefraccion = new QNodoColorRefraccion(
                                mapa3.getEnvMap(), 1.45f);
                nodoRefraccion.setTipoMapaEntorno(EnvProbe.FORMATO_MAPA_CUBO);
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
                mapa3.aplicar(EnvProbe.FORMATO_MAPA_CUBO);
                cubo6.move(6, 0.5f, 0);
                mundo.addEntity(cubo6);

        }

        @Override
        public void accion(int numAccion, RenderEngine render) {
        }

}
