/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas;

import java.util.ArrayList;
import java.util.List;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.math.Vector4;

/**
 * Define una AABB (Axis aligned bounding boxes) Es una caja virtual que
 * envuelve un objeto y es utilizada para el detector de colisiones
 *
 * @author alberto
 */
public class AABB extends CollisionShape {

    public Vector4 min;
    public Vector4 max;

    public AABB() {

    }

    public AABB(Vertex aabMinimo, Vertex aabMaximo) {
        this.min = aabMinimo.location.clone();
        this.max = aabMaximo.location.clone();
    }

    public AABB(Mesh... meshs) {
        this.min = meshs[0].vertexList[0].location.clone();
        this.max = meshs[0].vertexList[0].location.clone();
        for (Mesh mesh : meshs)
            for (Vertex vertex : mesh.vertexList) {
                if (vertex.location.x < this.min.x) {
                    this.min.x = vertex.location.x;
                }
                if (vertex.location.y < this.min.y) {
                    this.min.y = vertex.location.y;
                }
                if (vertex.location.z < this.min.z) {
                    this.min.z = vertex.location.z;
                }
                if (vertex.location.x > this.max.x) {
                    this.max.x = vertex.location.x;
                }
                if (vertex.location.y > this.max.y) {
                    this.max.y = vertex.location.y;
                }
                if (vertex.location.z > this.max.z) {
                    this.max.z = vertex.location.z;
                }
            }
    }

    @Override
    public boolean verificarColision(CollisionShape otro) {
        boolean b = false;
        if (otro instanceof AABB) {
            b = this.max.x > ((AABB) otro).min.x
                    && this.min.x < ((AABB) otro).max.x
                    && this.max.y > ((AABB) otro).min.y
                    && this.min.y < ((AABB) otro).max.y
                    && this.max.z > ((AABB) otro).min.z
                    && this.min.z < ((AABB) otro).max.z;
        }

        return b;

    }

    @Override
    public void destroy() {

    }

    @Override
    public String toString() {
        return "AABB [min=" + min + ", max=" + max + "]";
    }

    public Vector4[] getVertex() {
        Vector4[] returnValue = new Vector4[8];
        // Combinaciones de min y max para los 8 vértices
        returnValue[0] = new Vector4(min.x, min.y, min.z, 1);
        returnValue[1] = new Vector4(max.x, min.y, min.z, 1);
        returnValue[2] = new Vector4(min.x, max.y, min.z, 1);
        returnValue[3] = new Vector4(max.x, max.y, min.z, 1);
        returnValue[4] = new Vector4(min.x, min.y, max.z, 1);
        returnValue[5] = new Vector4(max.x, min.y, max.z, 1);
        returnValue[6] = new Vector4(min.x, max.y, max.z, 1);
        returnValue[7] = new Vector4(max.x, max.y, max.z, 1);

        return returnValue;
    }

    public List<int[]> getEdges() {
        List<int[]> edges = new ArrayList<>();

        // Conexiones entre vértices
        edges.add(new int[] { 0, 1 }); // Línea inferior 1
        edges.add(new int[] { 1, 3 }); // Línea inferior 2
        edges.add(new int[] { 3, 2 }); // Línea inferior 3
        edges.add(new int[] { 2, 0 }); // Línea inferior 4

        edges.add(new int[] { 4, 5 }); // Línea superior 1
        edges.add(new int[] { 5, 7 }); // Línea superior 2
        edges.add(new int[] { 7, 6 }); // Línea superior 3
        edges.add(new int[] { 6, 4 }); // Línea superior 4

        edges.add(new int[] { 0, 4 }); // Pilar 1
        edges.add(new int[] { 1, 5 }); // Pilar 2
        edges.add(new int[] { 3, 7 }); // Pilar 3
        edges.add(new int[] { 2, 6 }); // Pilar 4

        return edges;
    }

}
