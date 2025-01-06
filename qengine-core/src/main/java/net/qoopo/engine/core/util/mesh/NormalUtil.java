/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.util.mesh;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.QVector3;

/**
 *
 * @author alberto
 */
public class NormalUtil {

    /**
     * *
     * Calcula normales para un objeto generado a mano
     *
     * @param objeto
     * @return
     */
    public static Mesh computeNormals(Mesh objeto) {
        return computeNormals(objeto, true);
    }

    /**
     * Calcula las normales de un objeto generado que no tiene normales
     *
     * @param mesh
     * @param forzar Si ya tiene normales no las vuelve a calcular
     * @return
     */
    public static Mesh computeNormals(Mesh mesh, boolean forzar) {
        try {

            // se va a calcular una normal para cada vértice
            // se asume que el objeto es suave y no facetado
            mesh.clearNormals();

            // Paso 1-> se calcula las normales de cada cara, estas son usadas para calcular
            // la normal de cada vértice
            try {
                for (Primitive face : mesh.primitiveList) {
                    if (face instanceof Poly) {
                        Poly poly = (Poly) face;
                        poly.setNormalIndexList(poly.vertexIndexList); // le indico que los indices de las normales son iguales a
                                                             // de los vertices
                        if (poly.vertexIndexList.length >= 3) {
                            if (poly.getNormal().equals(QVector3.zero) || forzar) {
                                poly.computeNormalCenter();// calcula la normal de la cara
                            }
                            // le da a los vertices la normal de la cara
                            for (int i : poly.vertexIndexList) {
                                // mesh.vertices[i].normal.add(poligono.getNormal());
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }

            // Paso 2 calcula una normal para vértice como promedio de las normales de las
            // caras que
            // comparten el vértice
            try {
                for (int vertexIndex = 0; vertexIndex < mesh.vertexList.length; vertexIndex++) {
                    // vertice.normal.normalize();
                    // vertice.normalInversa = false;
                    // recorre todas las caras que hace uso de este vértice
                    QVector3 vertexNormal = QVector3.empty();
                    for (Primitive face : mesh.primitiveList) {
                        if (face instanceof Poly) {
                            Poly poly = (Poly) face;
                            if (poly.vertexIndexList.length >= 3) {
                                if (poly.getNormal().equals(QVector3.zero) || forzar) {
                                    poly.computeNormalCenter();// calcula la normal de la cara
                                }

                                // le da a los vertices la normal de la cara
                                for (int i : poly.vertexIndexList) {
                                    if (i == vertexIndex)
                                        vertexNormal.add(poly.getNormal());
                                    // mesh.vertices[i].normal.add(poligono.getNormal());
                                }
                            }
                        }
                    }
                    vertexNormal.normalize();
                    mesh.addNormal(vertexNormal);
                }
            } catch (Exception e) {
            }

            // // Paso 3 Para cada polygono agrega los indices de las normales a las que
            // debe
            // // apuntar
            // try {
            // for (Primitive face : mesh.primitivas) {
            // if (face instanceof Poly) {
            // Poly poly = (Poly) face;
            // poly.setNormalList(poly.vertexList);
            // }
            // }
            // } catch (Exception e) {
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mesh;
    }

    /**
     * Invierte las normales de una geometria
     *
     * @param objeto
     * @return
     */
    public static Mesh invertirNormales(Mesh objeto) {
        for (Primitive face : objeto.primitiveList) {
            if (face instanceof Poly) {
                ((Poly) face).getNormal().flip();
                // face.normalInversa = true;
                ((Poly) face).setNormalInversa(!((Poly) face).isNormalInversa());
            }
        }

        for (Vertex vertexList : objeto.vertexList) {
            // vertexList.normal.flip();
            vertexList.normalInversa = !vertexList.normalInversa;
        }

        return objeto;
    }

}
