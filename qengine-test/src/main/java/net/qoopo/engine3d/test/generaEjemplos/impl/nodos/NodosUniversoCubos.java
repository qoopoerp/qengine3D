/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.nodos;

import java.io.File;
import java.util.Random;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.material.node.MaterialNode;
import net.qoopo.engine.core.material.node.core.QNodoEnlace;
import net.qoopo.engine.core.material.node.core.output.MaterialOutputNode;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorIluminacion;
import net.qoopo.engine.renderer.shader.fragment.nodos.shader.QNodoColorTextura;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class NodosUniversoCubos extends MakeTestScene {

    public NodosUniversoCubos() {

    }

    private MaterialNode material = null;

    private void cargarMaterial() {
        material = null;
        try {
            // int colorTransparencia = -1;
            material = new MaterialNode();

            QNodoColorTextura nodoTextura = new QNodoColorTextura(new QProcesadorSimple(
                    AssetManager.get().loadTexture("difusa", new File("assets/textures/Skybox_example.png"))));
            QNodoColorIluminacion nodoDifuso = new QNodoColorIluminacion();
            // al instanciar el enlace, este se agrega a los perifericos
            QNodoEnlace enlace = new QNodoEnlace(nodoTextura.getSaColor(), nodoDifuso.getEnColor());

            MaterialOutputNode nodosalida = new MaterialOutputNode();
            QNodoEnlace enlace2 = new QNodoEnlace(nodoDifuso.getSaColor(), nodosalida.getEnColor());

            material.setNodo(nodosalida);
            // material.texturaColorTransparente = colorTransparencia;
            // if (colorTransparencia != -1) {
            // material.transAlfa = 0.99f;// el objeto tiene una transparencia
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void make(Scene mundo) {
        this.scene = mundo;
        cargarMaterial();
        Random rnd = new Random();
        float tamUniverso = 200;

        // Color colores[] = {
        // Color.YELLOW,
        // Color.RED,
        // Color.GREEN,
        // Color.PINK,
        // Color.MAGENTA,
        // Color.ORANGE,
        // Color.BLUE,
        // Color.CYAN,
        // Color.LIGHT_GRAY,
        // // Color.BLACK,
        // Color.WHITE,
        // Color.GRAY,
        // Color.DARK_GRAY
        // };
        // Color actual;

        Mesh geometria = new Box(1);
        MaterialUtil.applyMaterial(geometria, material);

        for (int i = 0; i < 3000; i++) {
            Entity cubo = new Entity("Cubo [" + i + "]");
            cubo.move(rnd.nextFloat() * tamUniverso * 2 - tamUniverso, rnd.nextFloat() * tamUniverso * 2 - tamUniverso,
                    rnd.nextFloat() * tamUniverso * 2 - tamUniverso);
            cubo.addComponent(geometria);
            mundo.addEntity(cubo);
        }

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
