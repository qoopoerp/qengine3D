/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.core.util;

import java.util.ArrayList;
import java.util.List;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.QVector3;

/**
 *
 * @author alberto
 */
public class QDefinirCentro {

    /**
     * Traslada la ubicación de la entity al centro del origen y sus vertices
     * acorde al traslado
     *
     * @param entity
     */
    public static void definirCentroGeometriaAlOrigen(Entity entity) {

    }

    /**
     * Traslada la ubicación de la entity al centro de la geometria
     *
     * @param entity
     */
    public static void definirCentroOrigenAGeometria(Entity entity) {
        // paso 1 . Obtener el centro de todos los vertices pues sera la nueva ubicación
        // de la trasnformación
        List<Vertex> vertices = new ArrayList<>();
        for (EntityComponent com : entity.getComponents()) {
            if (com instanceof Mesh) {
                for (Vertex ver : ((Mesh) com).vertexList) {
                    vertices.add(ver);
                }
            }
        }

        // recorrer los vertice sy sacar punto medio
        QVector3 centro = QVector3.empty();
        int c = 0;
        for (Vertex vertice : vertices) {
            // centro.add(QVector3.of(vertice.x, vertice.y, vertice.z));
            centro.add(vertice.location.getVector3());
            c++;
        }
        centro.multiply(1.0f / c);// divido para obtener promedio
        // Paso 2. A todos los vertices restar el centro
        for (Vertex vertice : vertices) {
            vertice.location.x -= centro.x;
            vertice.location.y -= centro.y;
            vertice.location.z -= centro.z;
        }

        // Paso 3 . a LA trasnformación dar la ubicación del centro
        entity.getTransform().getLocation().set(centro);
    }
}
