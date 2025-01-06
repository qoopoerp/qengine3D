package net.qoopo.engine.core.entity.component.mesh.primitive;

import java.io.Serializable;
import java.util.Arrays;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.material.AbstractMaterial;

public class Primitive implements Serializable {

    public Mesh mesh;
    // la lista de vertices deben ser en el orden contrario a las agujas del reloj

    public int[] vertexIndexList = new int[0]; // lista de indices vertices
    public int[] normalIndexList = new int[0]; // lista de indices de normales
    public int[] uvIndexList = new int[0]; // lista de indices de normales

    public AbstractMaterial material = new AbstractMaterial();

    public Primitive() {
    }

    public Primitive(Mesh mesh) {
        this.mesh = mesh;
    }

    public Primitive(Mesh parent, int[] vertices) {
        this(parent);
        setVertexIndexList(vertices);
    }

    public Primitive(Mesh parent, int[] vertices, int[] normales, int[] uv) {
        this(parent);
        setVertexIndexList(vertices);
        setNormalIndexList(normales);
        setUVList(uv);
    }

    public void addVertex(int vertex) {
        vertexIndexList = Arrays.copyOf(vertexIndexList, vertexIndexList.length + 1);
        vertexIndexList[vertexIndexList.length - 1] = vertex;
    }

    public void setVertexIndexList(int[] vertices) {
        vertexIndexList = Arrays.copyOf(vertices, vertices.length);
    }

    public void addNormal(int normal) {
        normalIndexList = Arrays.copyOf(normalIndexList, normalIndexList.length + 1);
        normalIndexList[normalIndexList.length - 1] = normal;
    }

    public void setNormalIndexList(int[] normales) {
        normalIndexList = Arrays.copyOf(normales, normales.length);
    }

    public void addUV(int uv) {
        uvIndexList = Arrays.copyOf(uvIndexList, uvIndexList.length + 1);
        uvIndexList[uvIndexList.length - 1] = uv;
    }

    public void setUVList(int[] uv) {
        uvIndexList = Arrays.copyOf(uv, uv.length);
    }

    @Override
    public String toString() {
        return "Primitive {" + "listaVertices=" + Arrays.toString(vertexIndexList) + '}';
    }

}
