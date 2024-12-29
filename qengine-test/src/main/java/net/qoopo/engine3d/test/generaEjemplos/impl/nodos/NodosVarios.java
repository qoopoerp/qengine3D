/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.nodos;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.cubemap.QCubeMap;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Teapot;
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
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorReflexion;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorTextura;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoEmision;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class NodosVarios extends MakeTestScene {

    public NodosVarios() {

    }

    public void make(Scene mundo) {
        this.scene = mundo;

        // materiales con difuso
        MaterialNode material = new MaterialNode("Nodo_Esfera");

        QNodoColorIluminacion ilum1 = new QNodoColorIluminacion(QColor.RED);
        MaterialOutputNode nodosalida = new MaterialOutputNode();
        QNodoEnlace enlace = new QNodoEnlace(ilum1.getSaColor(), nodosalida.getEnColor());

        material.setNodo(nodosalida);

        Entity esfera = new Entity("esfera");
        esfera.addComponent(MaterialUtil.applyMaterial(new Sphere(2), material));
        esfera.move(-5, 5, 0);
        mundo.addEntity(esfera);

        // con textura e iluminacion (Nodo) con mapa de normales
        MaterialNode materialC6 = new MaterialNode();

        QNodoColorTextura nodoTexturaC6_1 = new QNodoColorTextura(new QProcesadorSimple(AssetManager.get().loadTexture(
                "difusa",
                new File("assets/textures/poliigon/bricks/RockGrey016/1K/RockGrey016_COL_VAR1_1K.jpg"))));
        QNodoColorTextura nodoTexturaC6_2 = new QNodoColorTextura(new QProcesadorSimple(AssetManager.get().loadTexture(
                "normal",
                new File("assets/textures/poliigon/bricks/RockGrey016/1K/RockGrey016_NRM_1K.jpg"))));
        QNodoColorIluminacion nodoDifusoC6_1 = new QNodoColorIluminacion();

        // enlace que une la salida de la textura con con difuso
        QNodoEnlace enlaceC6_1 = new QNodoEnlace(nodoTexturaC6_1.getSaColor(), nodoDifusoC6_1.getEnColor());
        // enlace que une la salida de DifusoC4 con la entrada1 de mix
        QNodoEnlace enlaceC6_2 = new QNodoEnlace(nodoTexturaC6_2.getSaColor(), nodoDifusoC6_1.getEnNormal());
        // enlace que une la salida del primer difuso con la entrada1 de mix

        MaterialOutputNode nodosalida2 = new MaterialOutputNode();
        QNodoEnlace enlace2_c6 = new QNodoEnlace(nodoDifusoC6_1.getSaColor(), nodosalida2.getEnColor());

        materialC6.setNodo(nodosalida2);
        Entity cubo6 = new Entity("Caja");
        cubo6.addComponent(MaterialUtil.applyMaterial(new Box(2), materialC6));
        cubo6.move(0, 0, 0);

        mundo.addEntity(cubo6);

        // reflejos
        // reflejos con pbr
        Entity tetera = new Entity("Tetera");
        QCubeMap mapa2 = new QCubeMap(QGlobal.MAPA_CUPO_RESOLUCION);

        // QGeometria esfera2 = new Sphere(1);
        MaterialNode mat5 = new MaterialNode("Reflexion");

        QNodoColorReflexion nodoreflexion = new QNodoColorReflexion(new QProcesadorSimple(mapa2.getTexturaEntorno()));
        nodoreflexion.setTipoMapaEntorno(QCubeMap.FORMATO_MAPA_CUBO);
        QNodoColorIluminacion nodoDifuso = new QNodoColorIluminacion();

        // enlace que une la salida de la textura con con difuso
        QNodoEnlace enlace3 = new QNodoEnlace(nodoreflexion.getSaColor(), nodoDifuso.getEnColor());

        MaterialOutputNode nodosalida3 = new MaterialOutputNode();
        QNodoEnlace enlace4 = new QNodoEnlace(nodoDifuso.getSaColor(), nodosalida3.getEnColor());

        mat5.setNodo(nodosalida3);
        // mat5.setNodo(nodoreflexion);

        tetera.addComponent(MaterialUtil.applyMaterial(new Teapot(), mat5));
        tetera.addComponent(mapa2);
        tetera.move(2, 0.5f, 0);
        mapa2.aplicar(QCubeMap.FORMATO_MAPA_CUBO, 1, 0);

        mundo.addEntity(tetera);

        // emisivo
        Entity entEmisivo = new Entity("emision");

        MaterialNode matEmisivo = new MaterialNode("Emisión");
        QNodoEmision nodoEmision = new QNodoEmision(QColor.YELLOW, 1.0f);

        MaterialOutputNode nodosalida5 = new MaterialOutputNode();
        QNodoEnlace enlace5 = new QNodoEnlace(nodoEmision.getSaColor(), nodosalida5.getEnColor());

        matEmisivo.setNodo(nodosalida5);
        entEmisivo.addComponent(MaterialUtil.applyMaterial(new Sphere(0.25f), matEmisivo));
        entEmisivo.move(4, 4, 4);
        mundo.addEntity(entEmisivo);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
