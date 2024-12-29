package net.qoopo.engine.core.entity.component.mesh.primitive;

import java.util.Arrays;

import net.qoopo.engine.core.entity.component.mesh.Mesh;

public class Line extends QPrimitiva implements Comparable<Line> {

    public Line(Mesh parent) {
        super(parent);
    }

    public Line(Mesh parent, int... vertices) {
        this(parent);
        setVertices(vertices);
    }

    @Override
    public String toString() {
        return "QLinea{" + "listaVertices=" + Arrays.toString(listaVertices) + '}';
    }

    @Override
    public int compareTo(Line other) {
//        return Float.valueOf(vFin.z).compareTo(other.vFin.z);
        return Float.valueOf(geometria.vertices[listaVertices[0]].location.z).compareTo(other.geometria.vertices[listaVertices[0]].location.z);
//        return Float.valueOf(other.vFin.z).compareTo(vFin.z);
    }

}
