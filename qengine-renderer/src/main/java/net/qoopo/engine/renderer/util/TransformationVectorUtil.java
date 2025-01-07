/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.util;

import java.util.logging.Logger;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.math.QVector4;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.TempVars;

/**
 * Realiza la transformación de los vértices y normales
 *
 * @author alberto
 */
public class TransformationVectorUtil {

    private static Logger logger = Logger.getLogger("qtransformar");

    /**
     * Transforma un vector normal. Este vector no se debe procesar la
     * traslación sino solo la dirección
     *
     * @param vector
     * @param matriz
     * @return
     */
    public static QVector3 transformarVectorNormal(QVector3 vector, QMatriz4 matriz) {
        TempVars tmp = TempVars.get();
        try {
            tmp.vector4f1.set(vector, 0);// sin traslación
            return matriz.mult(tmp.vector4f1).getVector3();
        } finally {
            tmp.release();
        }
    }

    /**
     * Transforma un vector de posición donde si se aplica traslación
     *
     * @param vector
     * @param entity Puede ser nulo
     * @param camara
     * @return
     */
    public static QVector3 transformarVector(QVector3 vector, Entity entity, Camera camara) {
        QMatriz4 matVistaModelo = camara.getMatrizTransformacion(QGlobal.time).invert()
                .mult(entity != null ? entity.getMatrizTransformacion(QGlobal.time) : QMatriz4.IDENTITY);
        return matVistaModelo.mult(vector);
    }

    /**
     * Transforma un vector de posición donde si se aplica traslación
     *
     * @param vector
     * @param entity Puede ser nulo
     * @param camara
     * @return
     */
    public static QVector4 transformarVector(QVector4 vector, Entity entity, Camera camara) {
        QMatriz4 matVistaModelo = camara.getMatrizTransformacion(QGlobal.time).invert()
                .mult(entity != null ? entity.getMatrizTransformacion(QGlobal.time) : QMatriz4.IDENTITY);
        return matVistaModelo.mult(vector);
    }

    /**
     * Transforma un vector de posición donde si se aplica traslación, al
     * espacio de la entity
     *
     * @param vector
     * @param entity
     *
     * @return
     */
    public static QVector3 transformarVector(QVector3 vector, Entity entity) {
        QMatriz4 matModelo = entity != null ? entity.getMatrizTransformacion(QGlobal.time) : QMatriz4.IDENTITY;
        return matModelo.mult(vector);
    }

    /**
     * Transforma un vector de posición donde si se aplica traslación, al
     * espacio de la entity
     *
     * @param vector
     * @param entity
     *
     * @return
     */
    public static QVector4 transformarVector(QVector4 vector, Entity entity) {
        QMatriz4 matModelo = entity != null ? entity.getMatrizTransformacion(QGlobal.time) : QMatriz4.IDENTITY;
        return matModelo.mult(vector);
    }

    /**
     * Quita la transformación de un vector de posición
     *
     * @param vector
     * @param entity
     * @param camara
     * @return
     */
    public static QVector3 transformarVectorInversa(QVector3 vector, Entity entity, Camera camara) {
        QMatriz4 matVistaModelo = camara.getMatrizTransformacion(QGlobal.time).invert()
                .mult(entity != null ? entity.getMatrizTransformacion(QGlobal.time) : QMatriz4.IDENTITY);
        return matVistaModelo.invert().mult(vector);
    }

    /**
     * Quita la transformación de un vector de posición
     *
     * @param vector
     * @param entity
     * @param camara
     * @return
     */
    public static QVector4 transformarVectorInversa(QVector4 vector, Entity entity, Camera camara) {
        QMatriz4 matVistaModelo = camara.getMatrizTransformacion(QGlobal.time).invert()
                .mult(entity != null ? entity.getMatrizTransformacion(QGlobal.time) : QMatriz4.IDENTITY);
        return matVistaModelo.invert().mult(vector);
    }

    /**
     * Transforma un vector normal (de direccion) donde no se aplica traslación
     *
     * @param vector
     * @param entity
     * @param camara
     * @return
     */
    public static QVector3 transformarVectorNormal(QVector3 vector, Entity entity, Camera camara) {
        QMatriz4 matrizVista = camara.getMatrizTransformacion(QGlobal.time).invert();
        QMatriz4 matVistaModelo = matrizVista
                .mult(entity != null ? entity.getMatrizTransformacion(QGlobal.time) : QMatriz4.IDENTITY);
        return TransformationVectorUtil.transformarVectorNormal(vector, matVistaModelo);
    }

    public static QVector3 transformarVectorNormalInversa(QVector3 vector, Entity entity, Camera camara) {
        QMatriz4 matrizVista = camara.getMatrizTransformacion(QGlobal.time).invert();
        QMatriz4 matVistaModelo = matrizVista
                .mult(entity != null ? entity.getMatrizTransformacion(QGlobal.time) : QMatriz4.IDENTITY);
        return TransformationVectorUtil.transformarVectorNormal(vector, matVistaModelo.invert());
    }

    // /**
    // *
    // * Metodo usando matrices.Paso 1.Trasformar el objeto de acuerdo a sus
    // * propiedades de transformacion 1.1 QRotacion 1.2 Traslacion 1.3 Escala
    // * Paso 2 Transformar las coordenadas de acuerdo al angulo de visualización
    // * (cámara) 2.1 Trasladar a las coordenadas de la camara 2.2 Rotación
    // * inversa con los angulos de la camara
    // *
    // * @param entity
    // * @param camara
    // * @param transformado
    // * @return
    // */
    // public static QVertexBuffer transformar(Entity entity, Camera camara,
    // QVertexBuffer transformado, QVertexShader vertexShader) {
    // transformar(entity, camara.getMatrizTransformacion(QGlobal.tiempo).invert(),
    // transformado, vertexShader);
    // return transformado;
    // }

    // /**
    // * Metodo usando matrices.Paso 1.Trasformar el objeto de acuerdo a sus
    // * propiedades de transformacion 1.1 QRotacion 1.2 Traslacion 1.3 Escala
    // * Paso 2 Transformar las coordenadas de acuerdo al angulo de visualización
    // * (cámara) 2.1 Trasladar a las coordenadas de la camara 2.2 Rotación
    // * inversa con los angulos de la camara
    // *
    // * @param objeto
    // * @param matrizVista La Matriz de vista es la inversa de la matriz de la
    // * camara.Esto es porque la camara siempre estara en el
    // * centro y movemos el
    // * mundo en direccion contraria a la camara.
    // * @param transformado
    // * @return
    // */
    // public static QVertexBuffer transformar(Entity objeto, QMatriz4 matrizVista,
    // QVertexBuffer transformado, QVertexShader vertexShader) {
    // // contamos los vertices
    // int nVertices = 0;
    // int nPoligonos = 0;
    // int verticeActual = 0;

    // //logger.info("[Transformar]");

    // if (objeto != null && objeto.isToRender()) {
    // for (EntityComponent componente : objeto.getComponents()) {
    // if (componente instanceof Mesh) {
    // nVertices += ((Mesh) componente).vertices.length;
    // nPoligonos += ((Mesh) componente).primitivas.length;
    // }
    // }
    // }

    // long ti = System.currentTimeMillis();

    // if (transformado == null) {
    // //logger.info(" Transformar-- Se inicializa la variable transformado");
    // transformado = new QVertexBuffer();
    // }

    // transformado.init(nVertices, nPoligonos);

    // //logger.info(" Transformar-- Tiempo init=" + (System.currentTimeMillis() -
    // ti));
    // ti = System.currentTimeMillis();

    // nVertices = 0;
    // nPoligonos = 0;

    // // La matriz vistaModelo es el resultado de multiplicar la matriz de vista
    // por
    // // la matriz del modelo
    // // De esta forma es la matriz que se usa para transformar el modelo a las
    // // coordenadas del mundo
    // // luego de estas coordenadas se transforma a las coordenadas de la camara
    // QMatriz4 matVistaModelo;

    // // La matriz modelo contiene la información del modelo
    // // Traslación, rotacion (en su propio eje ) y escala
    // QMatriz4 matrizModelo;

    // QMatriz4 matrizVistaInvertidaBillboard = matrizVista.invert();

    // if (objeto.isToRender()) {
    // // Matriz de modelo
    // // obtiene la matriz de informacion concatenada con los padres
    // matrizModelo = objeto.getMatrizTransformacion(QGlobal.tiempo);

    // // ------------------------------------------------------------
    // // MAtriz VistaModelo
    // // obtiene la matriz de transformacion del objeto combinada con la matriz de
    // // vision de la camara
    // matVistaModelo = matrizVista.mult(matrizModelo);

    // for (EntityComponent componente : objeto.getComponents()) {
    // if (componente instanceof Mesh) {
    // objeto.actualizarRotacionBillboard(matrizVistaInvertidaBillboard);

    // // vertices
    // for (QVertex vertice : ((Mesh) componente).vertices) {
    // transformado.setVertice(vertexShader.apply(vertice, matVistaModelo),
    // nVertices);
    // nVertices++;
    // }

    // QPoligono tmpPoli;
    // QLinea tmpLinea;
    // // caras
    // for (QPrimitiva primitiva : ((Mesh) componente).primitivas) {
    // if (primitiva instanceof QPoligono) {
    // QPoligono poligono = (QPoligono) primitiva;
    // // copia la informacion de las caras
    // tmpPoli = new QPoligono(((Mesh) componente));
    // tmpPoli.geometria = ((Mesh) componente);
    // tmpPoli.material = poligono.material;
    // tmpPoli.setNormalInversa(poligono.isNormalInversa());
    // tmpPoli.getNormal().set(poligono.getNormal());
    // tmpPoli.getNormalCopy().set(tmpPoli.getNormal());
    // tmpPoli.setSmooth(poligono.isSmooth());
    // tmpPoli.listaVertices = new int[poligono.listaVertices.length];
    // tmpPoli.getCenter().copyAttribute(poligono.getCenter());
    // tmpPoli.getCenterCopy().copyAttribute(poligono.getCenter());
    // for (int j = 0; j < poligono.listaVertices.length; j++) {
    // tmpPoli.listaVertices[j] = poligono.listaVertices[j] + verticeActual;
    // }
    // transformado.setPoligono(tmpPoli, nPoligonos);
    // nPoligonos++;
    // } else if (primitiva instanceof QLinea) {
    // QLinea linea = (QLinea) primitiva;
    // tmpLinea = new QLinea(((Mesh) componente));
    // tmpLinea.geometria = ((Mesh) componente);
    // tmpLinea.material = linea.material;
    // tmpLinea.listaVertices = new int[linea.listaVertices.length];
    // for (int j = 0; j < linea.listaVertices.length; j++) {
    // tmpLinea.listaVertices[j] = linea.listaVertices[j] + verticeActual;
    // }
    // transformado.setPoligono(tmpLinea, nPoligonos);
    // nPoligonos++;
    // }
    // }
    // verticeActual += ((Mesh) componente).vertices.length;
    // }
    // }

    // // ti = System.currentTimeMillis();
    // }

    // // //logger.info(" Transformar-- Objetos =" + cObjetos);
    // // ti = System.currentTimeMillis();
    // // recalculo normal para pruebas (al calcular las normales aqui se corrije en
    // // cierta medida el error de las normales)
    // // en las animaciones, donde se modifican las coordenadas de los vértices
    // recalcularNormales(transformado);
    // return transformado;
    // }

    // /**
    // * Recalcula las normales del universo transformado (solo de las caras).
    // * Este método ayuda a solventar el problema de las normales con las
    // * animaciones donde las coordenadas de lso vértices son alteradas de
    // * acuerdo a la animacion esqueletica
    // *
    // * @param transformado
    // */
    // public static void recalcularNormales(QVertexBuffer transformado) {
    // long ti = System.currentTimeMillis();
    // // se calcula las normales para los vértices, estos son usados para el
    // suavizado
    // for (QPrimitiva primitiva : transformado.getPoligonosTransformados()) {
    // if (primitiva instanceof QPoligono) {
    // QPoligono poligono = (QPoligono) primitiva;
    // poligono.calculaNormalYCentro(transformado.getVerticesTransformados());
    // if (poligono.isNormalInversa()) {
    // poligono.getNormal().flip();// invierto la normal en caso detener la marca de
    // normal inversa
    // poligono.getNormalCopy().flip();
    // } // le da a los vertices la normal de la cara
    // // for (int i : poligono.vertices) {
    // // transformado.getVerticesTransformados()[i].normal.add(poligono.normal);
    // // }
    // }
    // }
    // // normaliza
    // // try {
    // // for (QVertice vertexList : transformado.getVerticesTransformados()) {
    // // vertexList.normal.normalize();
    // // }
    // // } catch (Exception e) {
    // // }
    // //logger.info(" Transformar-- Tiempo recalcular normales=" +
    // (System.currentTimeMillis() - ti));
    // }

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // /**
    // * Metodo usando matrices.Paso 1.Trasformar el objeto de acuerdo a sus
    // * propiedades de transformacion 1.1 QRotacion 1.2 Traslacion 1.3 Escala
    // * Paso 2 Transformar las coordenadas de acuerdo al angulo de visualización
    // * (cámara) 2.1 Trasladar a las coordenadas de la camara 2.2 Rotación
    // * inversa con los angulos de la camara
    // *
    // * @param listaEntidades
    // * @param camara
    // * @param transformado
    // * @return
    // */
    // public static QVerticesBuffer transformar(List<Entity> listaEntidades,
    // QCamara camara, QVerticesBuffer transformado) {
    // //contamos los vertices
    // int nVertices = 0;
    // int nPoligonos = 0;
    // int verticeActual = 0;
    //
    // //logger.info("[Transformar]");
    // for (Entity objeto : listaEntidades) {
    // if (objeto != null && objeto.isRenderizar()) {
    // for (QComponente componente : objeto.getComponentes()) {
    // if (componente instanceof QGeometria) {
    // nVertices += ((QGeometria) componente).vertices.length;
    // nPoligonos += ((QGeometria) componente).primitivas.length;
    // }
    // }
    // }
    // }
    //
    // long ti = System.currentTimeMillis();
    //
    // if (transformado == null) {
    // //logger.info(" Transformar-- Se inicializa la variable transformado");
    // transformado = new QVerticesBuffer();
    // }
    //
    // transformado.init(nVertices, nPoligonos);
    //
    // //logger.info(" Transformar-- Tiempo init=" + (System.currentTimeMillis() -
    // ti));
    // ti = System.currentTimeMillis();
    //
    // nVertices = 0;
    // nPoligonos = 0;
    //
    // //La matriz vistaModelo es el resultado de multiplicar la matriz de vista por
    // la matriz del modelo
    // //De esta forma es la matriz que se usa para transformar el modelo a las
    // coordenadas del mundo
    // // luego de estas coordenadas se transforma a las coordenadas de la camara
    // QMatriz4 matVistaModelo;
    //
    // // La matriz modelo contiene la información del modelo
    // // Traslación, rotacion (en su propio eje ) y escala
    // QMatriz4 matrizModelo;
    //
    // //La Matriz de vista es la inversa de la matriz de la camara.
    // // Esto es porque la camara siempre estara en el centro y movemos el mundo
    // // en direccion contraria a la camara.
    // QMatriz4 matrizVista =
    // camara.getMatrizTransformacion(QGlobal.tiempo).invert();
    // QMatriz4 matrizVistaInvertidaBillboard =
    // camara.getMatrizTransformacion(QGlobal.tiempo);
    //
    // long tPromedioObjeto = 0;
    // long cObjetos = 0;
    //
    // for (Entity objeto : listaEntidades) {
    // if (objeto.isRenderizar()) {
    // //Matriz de modelo
    // //obtiene la matriz de informacion concatenada con los padres
    // matrizModelo = objeto.getMatrizTransformacion(QGlobal.tiempo);
    //
    // //------------------------------------------------------------
    // //MAtriz VistaModelo
    // //obtiene la matriz de transformacion del objeto combinada con la matriz de
    // vision de la camara
    // matVistaModelo = matrizVista.mult(matrizModelo);
    //
    // for (QComponente componente : objeto.getComponentes()) {
    // if (componente instanceof QGeometria) {
    // objeto.actualizarRotacionBillboard(matrizVistaInvertidaBillboard);
    //
    // //vertices
    // for (QVertice vertice : ((QGeometria) componente).vertices) {
    // transformado.setVertice(QVertexShader.procesarVertice(vertice,
    // matVistaModelo), nVertices);
    // nVertices++;
    // }
    //
    // QPoligono tmpPoli;
    // QLinea tmpLinea;
    // //caras
    // for (QPrimitiva primitiva : ((QGeometria) componente).primitivas) {
    // if (primitiva instanceof QPoligono) {
    // QPoligono poligono = (QPoligono) primitiva;
    // //copia la informacion de las caras
    // tmpPoli = new QPoligono(((QGeometria) componente));
    // tmpPoli.geometria = ((QGeometria) componente);
    // tmpPoli.material = poligono.material;
    // tmpPoli.setNormalInversa(poligono.isNormalInversa());
    // tmpPoli.getNormal().set(poligono.getNormal());
    // tmpPoli.getNormalCopy().set(tmpPoli.getNormal());
    // tmpPoli.setSmooth(poligono.isSmooth());
    // tmpPoli.vertices = new int[poligono.vertices.length];
    // tmpPoli.getCenter().copyAttribute(poligono.getCenter());
    // tmpPoli.getCenterCopy().copyAttribute(poligono.getCenter());
    // for (int j = 0; j < poligono.vertices.length; j++) {
    // tmpPoli.vertices[j] = poligono.vertices[j] + verticeActual;
    // }
    // transformado.setPoligono(tmpPoli, nPoligonos);
    // nPoligonos++;
    // } else if (primitiva instanceof QLinea) {
    // QLinea linea = (QLinea) primitiva;
    // tmpLinea = new QLinea(((QGeometria) componente));
    // tmpLinea.geometria = ((QGeometria) componente);
    // tmpLinea.material = linea.material;
    // tmpLinea.vertices = new int[linea.vertices.length];
    // for (int j = 0; j < linea.vertices.length; j++) {
    // tmpLinea.vertices[j] = linea.vertices[j] + verticeActual;
    // }
    // transformado.setPoligono(tmpLinea, nPoligonos);
    // nPoligonos++;
    // }
    // }
    // verticeActual += ((QGeometria) componente).vertices.length;
    // }
    // }
    // cObjetos++;
    // tPromedioObjeto += (System.currentTimeMillis() - ti);
    // ti = System.currentTimeMillis();
    // }
    // }
    // if (cObjetos != 0) {
    // tPromedioObjeto /= cObjetos;
    // }
    //
    // //logger.info(" Transformar-- Tiempo promedio objeto=" + tPromedioObjeto);
    //
    // //recalculo normal para pruebas (al calcular las normales aqui se corrije en
    // cierta medida el error de las normales)
    // // en las animaciones, donde se modifican las coordenadas de los vértices
    // recalcularNormales(transformado);
    // return transformado;
    // }
}
