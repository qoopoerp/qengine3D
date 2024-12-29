/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.util.mesh;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
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
    public static Mesh calcularNormales(Mesh objeto) {
        return calcularNormales(objeto, true);
    }

    /**
     * Calcula las normales de un objeto generado que no tiene normales
     *
     * @param objeto
     * @param forzar Si ya tiene normales no las vuelve a calcular
     * @return
     */
    public static Mesh calcularNormales(Mesh objeto, boolean forzar) {
        try {
            // se calcula las normales para los vértices, estos son usados para el suavizado
            for (QPrimitiva face : objeto.primitivas) {
                if (face instanceof Poly) {
                    Poly poligono = (Poly) face;
                    if (poligono.listaVertices.length >= 3) {
                        if (poligono.getNormal().equals(QVector3.zero) || forzar) {
                            poligono.calculaNormalYCentro();// calcula la normal de la cara
                        }
                        // le da a los vertices la normal de la cara
                        for (int i : poligono.listaVertices) {
                            objeto.vertices[i].normal.add(poligono.getNormal());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // normaliza
        try {
            for (Vertex vertice : objeto.vertices) {
                vertice.normal.normalize();
                vertice.normalInversa = false;
            }
        } catch (Exception e) {
        }

        return objeto;
    }

    /**
     * Invierte las normales de una geometria
     *
     * @param objeto
     * @return
     */
    public static Mesh invertirNormales(Mesh objeto) {
        for (QPrimitiva face : objeto.primitivas) {
            if (face instanceof Poly) {
                ((Poly) face).getNormal().flip();
                // face.normalInversa = true;
                ((Poly) face).setNormalInversa(!((Poly) face).isNormalInversa());
            }
        }

        for (Vertex vertexList : objeto.vertices) {
            vertexList.normal.flip();
            // vertices.normalInversa = true;
            vertexList.normalInversa = !vertexList.normalInversa;
        }

        return objeto;
    }

}
