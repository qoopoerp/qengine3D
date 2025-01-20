/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;

/**
 * Contiene la información despues de realizar la transformación
 *
 * @author alberto
 */
@Getter
@Setter
public class VertexBuffer {

    private Vertex[] vertexList = null;
    private QColor[] colorList = null;
    private Vector3[] normalList = null;
    private Vector2[] uvList = null;

    public VertexBuffer() {

    }

    public void init(int vertexCount, int normalCount, int uvCount) {
        if (vertexList == null) {
            vertexList = new Vertex[vertexCount];
            colorList = new QColor[vertexCount];
            normalList = new Vector3[normalCount];
            uvList = new Vector2[uvCount];
        } else {
            // si ya tiene instancias compruebo las longitudes para no volver a isntanciar
            if (vertexList.length != vertexCount) {
                vertexList = new Vertex[vertexCount];
            }

            if (colorList.length != vertexCount) {
                colorList = new QColor[vertexCount];
            }

            if (normalList.length != normalCount) {
                normalList = new Vector3[normalCount];
            }

            if (uvList.length != uvCount) {
                uvList = new Vector2[uvCount];
            }
        }
    }

    public VertexBuffer(Vertex[] vertexList) {
        this.vertexList = vertexList;
    }

    public Vertex getVertex(int i) {
        // if (vertexList.length > i) {
        return vertexList[i];
        // } else {
        // return new Vertex();
        // }
    }

    public void setVertex(Vertex vertice, int i) {
        if (vertexList.length > i) {
            vertexList[i] = vertice;
        }
    }

    public QColor getColor(int i) {
        if (colorList.length > i) {
            return colorList[i];
        } else {
            return new QColor();
        }
    }

    public void setColor(QColor color, int i) {
        if (colorList.length > i) {
            colorList[i] = color;
        }
    }

    public Vector3 getNormal(int i) {
        // if (normalList.length > i) {
        return normalList[i];
        // } else {
        // return QVector3.empty();
        // }
    }

    public void setNormal(Vector3 normal, int i) {
        if (normalList.length > i) {
            normalList[i] = normal;
        }
    }

    public Vector2 getUV(int i) {
        // if (uvList.length > i) {
        return uvList[i];
        // } else {
        // return new QVector2();
        // }
    }

    public void setUV(Vector2 uv, int i) {
        if (uvList.length > i) {
            uvList[i] = uv;
        }
    }

    public VertexBuffer clone() {
        VertexBuffer nuevo = new VertexBuffer();
        nuevo.init(this.vertexList.length, this.normalList.length, this.uvList.length);
        return nuevo;
    }

}
