/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.transform;

import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;

/**
 * Contiene la información despues de realizar la transformación
 *
 * @author alberto
 */
public class QVertexBuffer {

    private Vertex[] verticesTransformados = null;
    private QPrimitiva[] poligonosTransformados = null;

    public QVertexBuffer() {

    }

    public void init(int vertices, int caras) {
        if (verticesTransformados == null) {
            verticesTransformados = new Vertex[vertices];
            poligonosTransformados = new QPrimitiva[caras];
            // System.out.println("se instancio vertices y poligonos (" + vertices + "," +
            // caras + ")");
        } else {
            // si ya tiene instancias compruebo las longitudes para no volver a isntanciar
            if (verticesTransformados.length != vertices) {
                verticesTransformados = new Vertex[vertices];
            }

            if (poligonosTransformados.length != caras) {
                poligonosTransformados = new QPrimitiva[caras];
            }
        }
    }

    public QVertexBuffer(Vertex[] verticesTransformados) {
        this.verticesTransformados = verticesTransformados;
    }

    public Vertex[] getVerticesTransformados() {
        return verticesTransformados;
    }

    public void setVerticesTransformados(Vertex[] verticesTransformados) {
        this.verticesTransformados = verticesTransformados;
    }

    public Vertex getVertice(int i) {
        if (verticesTransformados.length > i) {
            return verticesTransformados[i];
        } else {
            return new Vertex();
        }
    }

    public void setVertice(Vertex vertice, int i) {
        if (verticesTransformados.length > i) {
            verticesTransformados[i] = vertice;
        }
    }

    public QPrimitiva getPoligono(int i) {
        if (poligonosTransformados.length > i) {
            if (poligonosTransformados[i] != null) {
                return poligonosTransformados[i];
            } else {
                return new QPrimitiva(null);
            }
        } else {
            return null;
            // return new QPrimitiva(null);
        }
    }

    public QPrimitiva getLinea(int i) {
        if (poligonosTransformados.length > i) {
            if (poligonosTransformados[i] != null) {
                return poligonosTransformados[i];
            } else {
                return new QPrimitiva(null);
            }
        } else {
            return null;
            // return new QPrimitiva(null);
        }
    }

    public void setPoligono(QPrimitiva poligono, int i) {
        if (poligonosTransformados.length > i) {
            poligonosTransformados[i] = poligono;
        }
    }

    public QPrimitiva[] getPoligonosTransformados() {
        return poligonosTransformados;
    }

    public void setPoligonosTransformados(QPrimitiva[] poligonosTransformados) {
        this.poligonosTransformados = poligonosTransformados;
    }

    public QVertexBuffer clone() {
        QVertexBuffer nuevo = new QVertexBuffer();
        nuevo.init(this.verticesTransformados.length, this.poligonosTransformados.length);
        return nuevo;
    }

}
