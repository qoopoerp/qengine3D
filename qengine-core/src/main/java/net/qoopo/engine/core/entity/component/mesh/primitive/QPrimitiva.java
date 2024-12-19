package net.qoopo.engine.core.entity.component.mesh.primitive;

import java.io.Serializable;
import java.util.Arrays;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.material.AbstractMaterial;

public class QPrimitiva implements Serializable {

    public Mesh geometria;
    // la lista de vertices deben ser en el orden contrario a las agujas del reloj
    public int[] listaVertices = new int[0]; // lista de indices vertices

    public AbstractMaterial material = new AbstractMaterial();

    public QPrimitiva() {
    }

    public QPrimitiva(Mesh parent) {
        this.geometria = parent;
    }

    public QPrimitiva(Mesh parent, int... vertices) {
        this(parent);
        setVertices(vertices);
    }

    public void addVertex(int vertex) {
        listaVertices = Arrays.copyOf(listaVertices, listaVertices.length + 1);
        listaVertices[listaVertices.length - 1] = vertex;
    }

    public void setVertices(int[] vertices) {
        listaVertices = Arrays.copyOf(vertices, vertices.length);
    }

    @Override
    public String toString() {
        return "QPrimititva{" + "listaVertices=" + Arrays.toString(listaVertices) + '}';
    }

}
