/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.pbr;

import java.io.File;
import net.qoopo.engine3d.componentes.QEntidad;
import net.qoopo.engine3d.componentes.geometria.QGeometria;
import net.qoopo.engine3d.core.escena.QEscena;
import net.qoopo.engine3d.test.generaEjemplos.GeneraEjemplo;
import net.qoopo.engine3d.componentes.geometria.primitivas.formas.QEsfera;
import net.qoopo.engine3d.componentes.reflexiones.QMapaCubo;
import net.qoopo.engine3d.core.material.basico.QMaterialBas;
import net.qoopo.engine3d.core.material.nodos.QMaterialNodo;
import net.qoopo.engine3d.core.math.QColor;
import net.qoopo.engine3d.core.recursos.QGestorRecursos;
import net.qoopo.engine3d.core.textura.mapeo.QMaterialUtil;
import net.qoopo.engine3d.core.textura.procesador.QProcesadorSimple;
import net.qoopo.engine3d.core.util.QGlobal;
import net.qoopo.engine3d.core.util.QUtilNormales;
import net.qoopo.engine3d.engines.render.QMotorRender;
import net.qoopo.engine3d.engines.render.interno.shader.pixelshader.nodos.nodos.core.QNodoEnlace;
import net.qoopo.engine3d.engines.render.interno.shader.pixelshader.nodos.nodos.salida.QPBRMaterial;
import net.qoopo.engine3d.engines.render.interno.shader.pixelshader.nodos.nodos.sombreado.QPBRColorIluminacion;
import net.qoopo.engine3d.engines.render.interno.shader.pixelshader.nodos.nodos.sombreado.QPBRColorMix;
import net.qoopo.engine3d.engines.render.interno.shader.pixelshader.nodos.nodos.sombreado.QPBRColorReflexion;
import net.qoopo.engine3d.engines.render.interno.shader.pixelshader.nodos.nodos.sombreado.QPBRColorRefraccion;
import net.qoopo.engine3d.engines.render.interno.shader.pixelshader.nodos.nodos.sombreado.QPBRColorVidrio;

/**
 *
 * @author alberto
 */
public class PBRSimple5 extends GeneraEjemplo {

    public PBRSimple5() {

    }

    public void iniciar(QEscena mundo) {
        this.mundo = mundo;

        //agrego una esfera para cargar un mapa como entorno
        QEntidad entorno = new QEntidad("Entorno");
        QMaterialBas matEntorno = new QMaterialBas("Entorno");
        matEntorno.setMapaDifusa(new QProcesadorSimple(QGestorRecursos.cargarTextura("difusa", new File(QGlobal.RECURSOS + "texturas/entorno/hdri/exteriores/from_cubemap.jpg"))));

        entorno.agregarComponente(QMaterialUtil.aplicarMaterial(QUtilNormales.invertirNormales(new QEsfera(50)), matEntorno));

        mundo.agregarEntidad(entorno);

        //agrego un piso
//        QEntidad plano = new QEntidad("plano");
//        plano.agregarComponente(new QPlano(20, 20));
//        mundo.agregarEntidad(plano);
        //Reflexion estandar
        //a cada entidad le agrego su generador de mapa de reflexion con un mapa cubico
        QEntidad cubo4 = new QEntidad("vidrioBAS");
        QMapaCubo mapa = new QMapaCubo(400);

        QGeometria esfera1 = new QEsfera(1);
        QMaterialBas mat4 = new QMaterialBas("Reflexion real");
        mat4.setColorDifusa(QColor.YELLOW);
        mat4.setFactorEntorno(1);
        mat4.setIndiceRefraccion(1.45f);
        mat4.setMapaEntorno(new QProcesadorSimple(mapa.getTexturaSalida()));
        mat4.setTipoMapaEntorno(1);
        cubo4.agregarComponente(QMaterialUtil.aplicarMaterial(esfera1, mat4));
        cubo4.agregarComponente(mapa);
        mapa.aplicar(QMapaCubo.FORMATO_MAPA_CUBO, 1, 1.45f);
        cubo4.mover(-2, 0.5f, 0);
        mundo.agregarEntidad(cubo4);
//---------------------------------------------------------------------------------------
        // vidrio con pbr
        QEntidad cubo5 = new QEntidad("Vidrio PBR");
        QMapaCubo mapa2 = new QMapaCubo(400);

        QGeometria esfera2 = new QEsfera(1);
//        QGeometria esfera2 = QUtilComponentes.getGeometria(CargaWaveObject.cargarWaveObject(new File(QGlobal.RECURSOS + "objetos/formato_obj/VARIOS/VIDRIO/Glass/Glass.obj")).get(0));
        QMaterialNodo mat5 = new QMaterialNodo("Vidrio real PBR");

        QPBRColorVidrio nodoVidrio = new QPBRColorVidrio(new QProcesadorSimple(mapa2.getTexturaSalida()), 1.45f);
        nodoVidrio.setTipoMapaEntorno(QMapaCubo.FORMATO_MAPA_CUBO);
        QPBRColorIluminacion nodoDifuso = new QPBRColorIluminacion();

        // enlace que une la salida de la textura con con difuso
        QNodoEnlace enlace = new QNodoEnlace(nodoVidrio.getSaColor(), nodoDifuso.getEnColor());

        QPBRMaterial nodosalida = new QPBRMaterial();
        QNodoEnlace enlace2 = new QNodoEnlace(nodoDifuso.getSaColor(), nodosalida.getEnColor());

        mat5.setNodo(nodosalida);
//        mat5.setNodo(nodoRefraccion);

        cubo5.agregarComponente(QMaterialUtil.aplicarMaterial(esfera2, mat5));
        cubo5.agregarComponente(mapa2);
        mapa2.aplicar(QMapaCubo.FORMATO_MAPA_CUBO, 1, 0);
        cubo5.mover(0, 0.5f, 0);
        mundo.agregarEntidad(cubo5);
//---------------------------------------------------------------------------------------
        // vidrio con pbr con mix
        QEntidad cubo6 = new QEntidad("Mix");
        QMapaCubo mapa3 = new QMapaCubo(400);

        QGeometria esfera3 = new QEsfera(1);
        QMaterialNodo mat6 = new QMaterialNodo("Vidrio real PBR");

        QPBRColorReflexion nodoReflejo = new QPBRColorReflexion(new QProcesadorSimple(mapa3.getTexturaSalida()));
        nodoReflejo.setTipoMapaEntorno(QMapaCubo.FORMATO_MAPA_CUBO);
        QPBRColorRefraccion nodoRefraccion = new QPBRColorRefraccion(new QProcesadorSimple(mapa3.getTexturaSalida()), 1.45f);
        nodoRefraccion.setTipoMapaEntorno(QMapaCubo.FORMATO_MAPA_CUBO);

        QPBRColorMix nodoMix = new QPBRColorMix(0.5f);

        QPBRColorIluminacion nodoDifuso6_1 = new QPBRColorIluminacion();

        // enlace que une la salida de la textura con con difuso
        QNodoEnlace enlace6_1 = new QNodoEnlace(nodoReflejo.getSaColor(), nodoMix.getEnColor1());
        QNodoEnlace enlace6_2 = new QNodoEnlace(nodoRefraccion.getSaColor(), nodoMix.getEnColor2());
        QNodoEnlace enlace6_3 = new QNodoEnlace(nodoMix.getSaColor(), nodoDifuso6_1.getEnColor());

        QPBRMaterial nodosalida2 = new QPBRMaterial();
        QNodoEnlace enlace3 = new QNodoEnlace(nodoMix.getSaColor(), nodosalida2.getEnColor());

        mat6.setNodo(nodosalida2);

        cubo6.agregarComponente(QMaterialUtil.aplicarMaterial(esfera3, mat6));
        cubo6.agregarComponente(mapa3);
        mapa3.aplicar(QMapaCubo.FORMATO_MAPA_CUBO, 1, 0);
        cubo6.mover(2, 0.5f, 0);
        mundo.agregarEntidad(cubo6);

    }

    @Override
    public void accion(int numAccion, QMotorRender render) {
    }

}
