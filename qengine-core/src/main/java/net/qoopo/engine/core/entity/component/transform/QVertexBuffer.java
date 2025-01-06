/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.transform;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;

/**
 * Contiene la información despues de realizar la transformación
 *
 * @author alberto
 */
@Getter
@Setter
public class QVertexBuffer {

    private Vertex[] vertexList = null;
    private QVector3[] normalList = null;
    private QVector2[] uvList = null;

    public QVertexBuffer() {

    }

    public void init(int vertexCount, int normalCount, int uvCount) {
        if (vertexList == null) {
            vertexList = new Vertex[vertexCount];
            normalList = new QVector3[normalCount];
            uvList = new QVector2[uvCount];
        } else {
            // si ya tiene instancias compruebo las longitudes para no volver a isntanciar
            if (vertexList.length != vertexCount) {
                vertexList = new Vertex[vertexCount];
            }

            // if (poligonosTransformados.length != caras) {
            // poligonosTransformados = new Primitive[caras];
            // }

            if (normalList.length != normalCount) {
                normalList = new QVector3[normalCount];
            }

            if (uvList.length != uvCount) {
                uvList = new QVector2[uvCount];
            }
        }
    }

    public QVertexBuffer(Vertex[] vertexList) {
        this.vertexList = vertexList;
    }

    public Vertex getVertex(int i) {
        if (vertexList.length > i) {
            return vertexList[i];
        } else {
            return new Vertex();
        }
    }

    public void setVertex(Vertex vertice, int i) {
        if (vertexList.length > i) {
            vertexList[i] = vertice;
        }
    }

    public QVector3 getNormal(int i) {
        // if (normalList.length > i) {
            return normalList[i];
        // } else {
        //     return QVector3.empty();
        // }
    }

    public void setNormal(QVector3 normal, int i) {
        if (normalList.length > i) {
            normalList[i] = normal;
        }
    }

    public QVector2 getUV(int i) {
        // if (uvList.length > i) {
            return uvList[i];
        // } else {
        //     return new QVector2();
        // }
    }

    public void setUV(QVector2 uv, int i) {
        if (uvList.length > i) {
            uvList[i] = uv;
        }
    }

    public QVertexBuffer clone() {
        QVertexBuffer nuevo = new QVertexBuffer();
        nuevo.init(this.vertexList.length, this.normalList.length, this.uvList.length);
        return nuevo;
    }

}
