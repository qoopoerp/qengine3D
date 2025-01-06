/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.vertex;

import net.qoopo.engine.core.entity.component.animation.Bone;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.TempVars;

/**
 * Vertex Shader. Realiza los calculos de transformacion de cada vertice
 *
 * @author alberto
 */
public class DefaultVertexShader implements VertexShader {

    /**
     * Transforma un vertice y devuelve un nuevo vertice con la transformación
     * aplicada
     *
     * //Viene a ser el Vertex Shader del OpenGL
     *
     * @param vertex
     * @param matrizVistaModelo Matriz Vista Modelo
     * @return
     */
    public Vertex apply(Vertex vertex, QVector3 normal, QVector2 uv, QColor color, QMatriz4 matrizVistaModelo) {
        Vertex nuevo = new Vertex();
        // nuevo.u = vertex.u;
        // nuevo.v = vertex.v;
        TempVars tmp = TempVars.get();
        // tmp.vector4f1-- posicion
        // tmp.vector4f2 --normal
        // tmp.vector4f3 --normal (entrada para multiplicar)
        try {
            // Pasos
            // 1.En caso de existir un esqueleto, Modificar la posición del vértice de
            // acuerdo a las matrices de transformación de los huesos
            // 2. Modificar el vertice con la matriz de tranformación enviada modifico de
            // acuerdo a las matrices de transformacion de los huesos
            // ******* PASO 1 ******************************
            if (vertex.getListaHuesos() != null && vertex.getListaHuesos().length > 0) {
                Bone hueso;
                QMatriz4 matrizTransformacionHueso;
                tmp.vector4f1.set(0, 0, 0, 1);// posicion final
                tmp.vector4f2.set(0, 0, 0, 0);// normal final
                float peso = 0;
                // recorre los huesos que afectan el vertice
                for (int i = 0; i < vertex.getListaHuesos().length; i++) {
                    peso = vertex.getListaHuesosPesos()[i];
                    hueso = vertex.getListaHuesos()[i];
                    matrizTransformacionHueso = hueso.getMatrizTransformacionHueso(QGlobal.tiempo);
                    tmp.vector4f1.add(matrizTransformacionHueso.mult(vertex.location).multiply(peso));
                    // la normal
                    tmp.vector4f3.set(normal, 0);
                    tmp.vector4f2.add(matrizTransformacionHueso.mult(tmp.vector4f3).multiply(peso));
                }
            } else {
                // si no hay esqueleto o no esta activo el motor de animacion, usa la coordenada
                // del vertice
                tmp.vector4f1.set(vertex.location);
                tmp.vector4f2.set(normal, 0);// un vector normal, sin traslacion
            }
            // ********* PASO 2 ************************
            // multiplica la matriz vista modelo por la ubicacion
            tmp.vector4f1.set(matrizVistaModelo.mult(tmp.vector4f1));
            // multipla la matriz vista modelo por la normal
            tmp.vector4f2.set((matrizVistaModelo.mult(tmp.vector4f2)));
            // ------
            nuevo.location.set(tmp.vector4f1);
            normal.set(tmp.vector4f2.getVector3());
            normal.normalize();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tmp.release();
        }
        return nuevo;
    }

}
