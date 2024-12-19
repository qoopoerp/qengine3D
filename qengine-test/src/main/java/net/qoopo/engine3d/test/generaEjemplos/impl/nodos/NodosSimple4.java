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
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QEsfera;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.material.node.MaterialNode;
import net.qoopo.engine.core.material.node.core.QNodoEnlace;
import net.qoopo.engine.core.material.node.core.output.MaterialOutputNode;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.mesh.QUtilNormales;
import net.qoopo.engine.renderer.shader.pixelshader.nodos.shader.QNodoColorIluminacion;
import net.qoopo.engine.renderer.shader.pixelshader.nodos.shader.QNodoColorRefraccion;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 *
 * @author alberto
 */
public class NodosSimple4 extends MakeTestScene {
    
    public NodosSimple4() {
        
    }
    
    public void make(Scene mundo) {
        this.scene = mundo;

        
        //agrego una esfera para cargar un mapa como entorno
        Entity entorno = new Entity("Entorno");
        QMaterialBas matEntorno = new QMaterialBas("Entorno");
        matEntorno.setMapaColor(new QProcesadorSimple(AssetManager.get().loadTexture("difusa", new File("assets/textures/entorno/hdri/exteriores/from_cubemap.jpg"))));

        entorno.addComponent(QMaterialUtil.aplicarMaterial(QUtilNormales.invertirNormales(new QEsfera(50)), matEntorno));

        mundo.addEntity(entorno);
        //agrego un piso
//        Entity plano = new Entity("plano");
//        plano.agregarComponente(new QPlano(20, 20));
//        mundo.addEntity(plano);
        //Reflexion estandar
        //a cada entidad le agrego su generador de mapa de reflexion con un mapa cubico
        Entity cubo4 = new Entity("esferaR1");
        QCubeMap mapa = new QCubeMap(QGlobal.MAPA_CUPO_RESOLUCION);
        
        Mesh esfera1 = new QEsfera(1);
        QMaterialBas mat4 = new QMaterialBas("Reflexion real");
        mat4.setColorBase(QColor.YELLOW);
        mat4.setMetalico(1);
        mat4.setIndiceRefraccion(1.45f);
        mat4.setMapaEntorno(new QProcesadorSimple(mapa.getTexturaEntorno()));
        mat4.setTipoMapaEntorno(QCubeMap.FORMATO_MAPA_CUBO);
        cubo4.addComponent(QMaterialUtil.aplicarMaterial(esfera1, mat4));
        cubo4.addComponent(mapa);
        mapa.aplicar(QCubeMap.FORMATO_MAPA_CUBO, 1, 1.45f);
        cubo4.move(0, 0.5f, 0);
        mundo.addEntity(cubo4);
//---------------------------------------------------------------------------------------
        // reflejos con nodo
        Entity cubo5 = new Entity("Reflejo Nodo");
        QCubeMap mapa2 = new QCubeMap(QGlobal.MAPA_CUPO_RESOLUCION);
        
        Mesh esfera2 = new QEsfera(1);
        MaterialNode mat5 = new MaterialNode("Reflexion real Nodo");
        
        QNodoColorRefraccion nodoRefraccion = new QNodoColorRefraccion(new QProcesadorSimple(mapa2.getTexturaEntorno()),1.45f);
        nodoRefraccion.setTipoMapaEntorno(QCubeMap.FORMATO_MAPA_CUBO);
        QNodoColorIluminacion nodoDifuso = new QNodoColorIluminacion();

        // enlace que une la salida de la textura con con difuso
        QNodoEnlace enlace = new QNodoEnlace(nodoRefraccion.getSaColor(), nodoDifuso.getEnColor());
        
        MaterialOutputNode nodosalida = new MaterialOutputNode();
        QNodoEnlace enlace2 = new QNodoEnlace(nodoDifuso.getSaColor(), nodosalida.getEnColor());
        mat5.setNodo(nodosalida);
//        mat5.setNodo(nodoRefraccion);

        cubo5.addComponent(QMaterialUtil.aplicarMaterial(esfera2, mat5));
        cubo5.addComponent(mapa2);
        mapa2.aplicar(QCubeMap.FORMATO_MAPA_CUBO, 1, 0);
        cubo5.move(2, 0.5f, 0);
        mundo.addEntity(cubo5);
        
    }
    
    @Override
    public void accion(int numAccion, RenderEngine render) {
    }
    
}
