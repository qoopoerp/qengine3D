/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.nodos;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.material.node.MaterialNode;
import net.qoopo.engine.core.material.node.core.QNodoEnlace;
import net.qoopo.engine.core.material.node.core.output.MaterialOutputNode;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorIluminacion;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorMix;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorTextura;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class NodosSimple2 extends MakeTestScene {

    public NodosSimple2() {

    }

    public void make(Scene mundo) {
        this.scene = mundo;

        // con textura (BASICO)
        Material mat1 = new Material();
        mat1.setColorMap(
                AssetManager.get().loadTexture("difusa", new File("assets/textures/testNormal/test1-color.jpg")));
        mat1.setNormalMap(
                AssetManager.get().loadTexture("normal", new File("assets/textures/testNormal/test1-normal.jpg")));

        Entity cubo1 = new Entity("text BAS");
        cubo1.addComponent(MaterialUtil.applyMaterial(new Box(2), mat1));
        cubo1.move(-5, 5, 5);

        mundo.addEntity(cubo1);
        // con textura (Nodo)
        MaterialNode matSoloTextura = new MaterialNode();

        QNodoColorTextura text1 = new QNodoColorTextura(
                AssetManager.get().loadTexture("difusa2", new File("assets/textures/testNormal/test1-color.jpg")));

        MaterialOutputNode nodosalida1 = new MaterialOutputNode();
        QNodoEnlace enlace = new QNodoEnlace(text1.getSaColor(), nodosalida1.getEnColor());

        matSoloTextura.setNodo(nodosalida1);

        Entity cubo2 = new Entity("SoloText");
        cubo2.addComponent(MaterialUtil.applyMaterial(new Box(2), matSoloTextura));
        cubo2.move(5, 5, 5);

        mundo.addEntity(cubo2);
        // --------------------------------------------------------------------------------------------------
        // con textura e iluminacion (Nodo)
        MaterialNode matNodoTextIlum = new MaterialNode();

        QNodoColorTextura nodoTextura = new QNodoColorTextura(
                AssetManager.get().loadTexture("difusa3", new File("assets/textures/testNormal/test1-color.jpg")));
        QNodoColorIluminacion nodoDifuso = new QNodoColorIluminacion();
        // al instanciar el enlace, este se agrega a los perifericos
        QNodoEnlace enCubo3_1 = new QNodoEnlace(nodoTextura.getSaColor(), nodoDifuso.getEnColor());

        MaterialOutputNode nodosalida2 = new MaterialOutputNode();
        QNodoEnlace enlace2 = new QNodoEnlace(nodoDifuso.getSaColor(), nodosalida2.getEnColor());

        matNodoTextIlum.setNodo(nodosalida2);

        Entity cubo3 = new Entity("nodo.textura.iluminacion");
        cubo3.addComponent(MaterialUtil.applyMaterial(new Box(2), matNodoTextIlum));
        cubo3.move(5, 5, -5);

        mundo.addEntity(cubo3);
        // --------------------------------------------------------------------------------------------------
        // con textura e iluminacion (Nodo), tambien mezcla la textura con un color
        MaterialNode materialC4 = new MaterialNode();

        QNodoColorTextura nodoTexturaC4 = new QNodoColorTextura(
                AssetManager.get().loadTexture("difusa4", new File("assets/textures/testNormal/test1-color.jpg")));
        QNodoColorIluminacion nodoDifusoC4 = new QNodoColorIluminacion();
        QNodoColorIluminacion nodDifusoAzul = new QNodoColorIluminacion(QColor.BLUE);
        QNodoColorMix nodMix = new QNodoColorMix();
        // enlace que une la salida de la textura con con DifuoC4
        QNodoEnlace enlaceC4_1 = new QNodoEnlace(nodoTexturaC4.getSaColor(), nodoDifusoC4.getEnColor());
        // enlace que une la salida de DifusoC4 con la entrada1 de mix
        QNodoEnlace enlaceC4_2 = new QNodoEnlace(nodoDifusoC4.getSaColor(), nodMix.getEnColor1());
        // enlace que une la salida de nodDifusoAzul con la entrada1 de mix
        QNodoEnlace enlaceC4_3 = new QNodoEnlace(nodDifusoAzul.getSaColor(), nodMix.getEnColor2());

        QNodoColorIluminacion ilum1 = new QNodoColorIluminacion(QColor.RED);
        MaterialOutputNode nodosalida3 = new MaterialOutputNode();
        QNodoEnlace enlace3 = new QNodoEnlace(nodMix.getSaColor(), nodosalida3.getEnColor());

        materialC4.setNodo(nodosalida3);

        Entity cubo4 = new Entity("nodo.text.ilum.mezcla");
        cubo4.addComponent(MaterialUtil.applyMaterial(new Box(2), materialC4));
        cubo4.move(-5, 5, -5);

        mundo.addEntity(cubo4);
        // --------------------------------------------------------------------------------------------------
        // con textura e iluminacion (Nodo),mexcla 2 textures
        MaterialNode materialC5 = new MaterialNode();

        QNodoColorTextura nodoTexturaC5_1 = new QNodoColorTextura(
                AssetManager.get().loadTexture("difusa5", new File("assets/textures/testNormal/test1-color.jpg")));
        QNodoColorTextura nodoTexturaC5_2 = new QNodoColorTextura(
                AssetManager.get().loadTexture("difusa6", new File("assets/textures/textura3.jpg")));
        QNodoColorIluminacion nodoDifusoC5_1 = new QNodoColorIluminacion();
        QNodoColorIluminacion nodoDifusoC5_2 = new QNodoColorIluminacion();

        QNodoColorMix nodMixC5 = new QNodoColorMix();
        // enlace que une la salida de la textura con con difuso
        QNodoEnlace enlaceC5_1 = new QNodoEnlace(nodoTexturaC5_1.getSaColor(), nodoDifusoC5_1.getEnColor());
        // enlace que une la salida de DifusoC4 con la entrada1 de mix
        QNodoEnlace enlaceC5_2 = new QNodoEnlace(nodoTexturaC5_2.getSaColor(), nodoDifusoC5_2.getEnColor());
        // enlace que une la salida del primer difuso con la entrada1 de mix
        QNodoEnlace enlaceC5_3 = new QNodoEnlace(nodoDifusoC5_1.getSaColor(), nodMixC5.getEnColor1());
        // enlace que une la salida del segundo difuso con la entrada1 de mix
        QNodoEnlace enlaceC5_4 = new QNodoEnlace(nodoDifusoC5_2.getSaColor(), nodMixC5.getEnColor2());

        MaterialOutputNode nodosalida5 = new MaterialOutputNode();
        QNodoEnlace enlace5 = new QNodoEnlace(nodMixC5.getSaColor(), nodosalida5.getEnColor());

        materialC5.setNodo(nodosalida5);

        Entity cubo5 = new Entity("nodo.mexcla.textures");
        cubo5.addComponent(MaterialUtil.applyMaterial(new Box(2), materialC5));
        cubo5.move(-5, -5, -5);

        mundo.addEntity(cubo5);

        // --------------------------------------------------------------------------------------------------
        // con textura e iluminacion (Nodo) con mapa de normales
        MaterialNode materialC6 = new MaterialNode();

        QNodoColorTextura nodoTexturaC6_1 = new QNodoColorTextura(
                AssetManager.get().loadTexture("difusa7", new File("assets/textures/testNormal/test1-color.jpg")));
        QNodoColorTextura nodoTexturaC6_2 = new QNodoColorTextura(
                AssetManager.get().loadTexture("difusa8", new File("assets/textures/testNormal/test1-normal.jpg")));
        QNodoColorIluminacion nodoDifusoC6_1 = new QNodoColorIluminacion();

        // enlace que une la salida de la textura con con difuso
        QNodoEnlace enlaceC6_1 = new QNodoEnlace(nodoTexturaC6_1.getSaColor(), nodoDifusoC6_1.getEnColor());
        // enlace que une la salida de DifusoC4 con la entrada1 de mix
        QNodoEnlace enlaceC6_2 = new QNodoEnlace(nodoTexturaC6_2.getSaColor(), nodoDifusoC6_1.getEnNormal());
        // enlace que une la salida del primer difuso con la entrada1 de mix

        MaterialOutputNode nodosalida6 = new MaterialOutputNode();
        QNodoEnlace enlace6 = new QNodoEnlace(nodoDifusoC6_1.getSaColor(), nodosalida6.getEnColor());

        materialC6.setNodo(nodosalida6);

        Entity cubo6 = new Entity("nodo.text.normales");
        cubo6.addComponent(MaterialUtil.applyMaterial(new Box(2), materialC6));
        cubo6.move(0, 0, 0);

        mundo.addEntity(cubo6);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
