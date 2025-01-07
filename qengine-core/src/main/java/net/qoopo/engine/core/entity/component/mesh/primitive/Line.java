package net.qoopo.engine.core.entity.component.mesh.primitive;

import java.util.Arrays;

import net.qoopo.engine.core.entity.component.mesh.Mesh;

public class Line extends Primitive implements Comparable<Line> {

    public Line(Mesh parent) {
        super(parent);
    }

    public Line(Mesh parent, int[] vertexList) {
        this(parent);
        setVertexIndexList(vertexList);
    }

    public Line(Mesh parent, int[] vertexList, int[] normalList, int[] uvList) {
        this(parent);
        setVertexIndexList(vertexList);
        setNormalIndexList(normalList);
        setUVList(uvList);
    }

    @Override
    public String toString() {
        return "QLinea{" + "listaVertices=" + Arrays.toString(vertexIndexList) + '}';
    }

    @Override
    public int compareTo(Line other) {
        // return Float.valueOf(vFin.z).compareTo(other.vFin.z);
        return Float.valueOf(mesh.vertexList[vertexIndexList[0]].location.z)
                .compareTo(other.mesh.vertexList[vertexIndexList[0]].location.z);
        // return Float.valueOf(other.vFin.z).compareTo(vFin.z);
    }

}
