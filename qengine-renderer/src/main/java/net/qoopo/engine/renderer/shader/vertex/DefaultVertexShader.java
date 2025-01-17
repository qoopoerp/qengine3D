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
import net.qoopo.engine.core.math.QVector4;
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
    public VertexShaderOutput apply(
            Vertex vertex,
            QVector3 normal,
            QVector2 uv,
            QColor color,
            QMatriz4 matrizVistaModelo) {
        Vertex shadedVertex = new Vertex();
        shadedVertex.setColor(color);
        QVector3 shadedNormal = new QVector3();
        QVector4 tmpNormal = new QVector4();
        QVector4 tmpNormalBone = new QVector4();
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
                shadedVertex.location.set(0, 0, 0, 1);// posicion final
                tmpNormal.set(0, 0, 0, 0);// normal final
                float peso = 0;
                // recorre los huesos que afectan el vertice
                for (int i = 0; i < vertex.getListaHuesos().length; i++) {
                    peso = vertex.getListaHuesosPesos()[i];
                    hueso = vertex.getListaHuesos()[i];
                    matrizTransformacionHueso = hueso.getMatrizTransformacionHueso(QGlobal.time);
                    shadedVertex.location.add(matrizTransformacionHueso.mult(vertex.location).multiply(peso));
                    // la normal
                    tmpNormalBone.set(normal, 0);
                    tmpNormal.add(matrizTransformacionHueso.mult(tmpNormalBone).multiply(peso));
                }
            } else {
                // si no hay esqueleto o no esta activo el motor de animacion, usa la coordenada
                // del vertice
                shadedVertex.location.set(vertex.location);
                tmpNormal.set(normal, 0);// un vector normal, sin traslacion
            }
            // ********* PASO 2 ************************
            // multiplica la matriz vista modelo por la ubicacion
            shadedVertex.location.set(matrizVistaModelo.mult(shadedVertex.location));
            // multipla la matriz vista modelo por la normal
            tmpNormal.set((matrizVistaModelo.mult(tmpNormal)));
            // ------
            shadedVertex.location.set(shadedVertex.location);
            shadedNormal.set(tmpNormal.getVector3());
            shadedNormal.normalize();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return VertexShaderOutput.builder().vertex(shadedVertex).normal(shadedNormal).uv(uv).color(color).build();
    }

}
