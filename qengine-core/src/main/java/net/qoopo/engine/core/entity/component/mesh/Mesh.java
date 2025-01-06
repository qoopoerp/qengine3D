package net.qoopo.engine.core.entity.component.mesh;

import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.primitive.Line;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.math.QVector4;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.mesh.NormalUtil;

@Getter
@Setter
public class Mesh implements EntityComponent {

    @Getter
    @Setter
    private Entity entity;

    private long timeMark = 0L;

    private Mesh cacheMesh = null;

    public static final int GEOMETRY_TYPE_MESH = 0;// malla
    public static final int GEOMETRY_TYPE_MATH = 1;// funcion matematica
    public static final int GEOMETRY_TYPE_PATH = 2;// ruta
    public static final int GEOMETRY_TYPE_WIRE = 3; // alambre (formado por triangulos)
    public static final int GEOMETRY_TYPE_SEGMENT = 4; // alambre por lineas

    public int tipo = GEOMETRY_TYPE_MESH;
    public String nombre = "";// usada para identificar los objetos cargados , luego la eliminamos
    public Vertex[] vertexList = new Vertex[0];
    public QVector3[] normalList = new QVector3[0];
    public QVector2[] uvList = new QVector2[0];
    public Primitive[] primitiveList = new Primitive[0];

    public Mesh() {
        tipo = GEOMETRY_TYPE_MESH;
    }

    public Mesh(int type) {
        this.tipo = type;
    }

    public void clearNormals() {
        normalList = new QVector3[0];
    }

    public void removeVertex(int indice) {
        System.arraycopy(vertexList, indice + 1, vertexList, indice, vertexList.length - 1 - indice);
    }

    public void removePoly(int indice) {
        System.arraycopy(primitiveList, indice + 1, primitiveList, indice, primitiveList.length - 1 - indice);
    }

    public Vertex addVertex() {
        Vertex nuevo = new Vertex();
        vertexList = Arrays.copyOf(vertexList, vertexList.length + 1);
        vertexList[vertexList.length - 1] = nuevo;
        return nuevo;
    }

    public Vertex addVertex(Vertex vertice) {
        Vertex nuevo = vertice.clone();
        vertexList = Arrays.copyOf(vertexList, vertexList.length + 1);
        vertexList[vertexList.length - 1] = nuevo;
        return nuevo;
    }

    public Vertex addVertex(QVector3 posicion) {
        Vertex nuevo = new Vertex(posicion.x, posicion.y, posicion.z);
        vertexList = Arrays.copyOf(vertexList, vertexList.length + 1);
        vertexList[vertexList.length - 1] = nuevo;
        return nuevo;
    }

    public Vertex addVertex(QVector4 posicion) {
        Vertex nuevo = new Vertex(posicion.x, posicion.y, posicion.z, posicion.w);
        vertexList = Arrays.copyOf(vertexList, vertexList.length + 1);
        vertexList[vertexList.length - 1] = nuevo;
        return nuevo;
    }

    public Vertex addVertex(float x, float y, float z) {
        Vertex nuevo = new Vertex(x, y, z, 1);
        vertexList = Arrays.copyOf(vertexList, vertexList.length + 1);
        vertexList[vertexList.length - 1] = nuevo;
        return nuevo;
    }

    // public Vertex addVertex(float x, float y, float z, float u, float v) {
    // Vertex nuevo = new Vertex(x, y, z, 1, u, v);
    // vertices = Arrays.copyOf(vertices, vertices.length + 1);
    // vertices[vertices.length - 1] = nuevo;
    // return nuevo;
    // }

    // public Vertex addVertex(QVector3 posicion, float u, float v) {
    // Vertex nuevo = new Vertex(posicion.x, posicion.y, posicion.z, 1, u, v);
    // vertices = Arrays.copyOf(vertices, vertices.length + 1);
    // vertices[vertices.length - 1] = nuevo;
    // return nuevo;
    // }

    // public Vertex addVertex(QVector4 posicion, float u, float v) {
    // Vertex nuevo = new Vertex(posicion.x, posicion.y, posicion.z, posicion.w, u,
    // v);
    // vertices = Arrays.copyOf(vertices, vertices.length + 1);
    // vertices[vertices.length - 1] = nuevo;
    // return nuevo;
    // }

    public QVector3 addNormal(QVector3 normal) {
        normalList = Arrays.copyOf(normalList, normalList.length + 1);
        normalList[normalList.length - 1] = normal;
        return normal;
    }

    public QVector3 addNormal(float x, float y, float z) {
        QVector3 nuevo = new QVector3(x, y, z);
        normalList = Arrays.copyOf(normalList, normalList.length + 1);
        normalList[normalList.length - 1] = nuevo;
        return nuevo;
    }

    public QVector2 addUV(QVector2 newUV) {
        uvList = Arrays.copyOf(uvList, uvList.length + 1);
        uvList[uvList.length - 1] = newUV;
        return newUV;
    }

    public QVector2 addUV(float u, float v) {
        QVector2 nuevo = new QVector2(u, v);
        uvList = Arrays.copyOf(uvList, uvList.length + 1);
        uvList[uvList.length - 1] = nuevo;
        return nuevo;
    }

    public Line addLine(int... vertices) throws Exception {
        validarVertices(vertices);
        Line nuevo = new Line(this, vertices);
        primitiveList = Arrays.copyOf(primitiveList, primitiveList.length + 1);
        primitiveList[primitiveList.length - 1] = nuevo;
        return nuevo;
    }

    public Line addLine(AbstractMaterial material, int... vertices) throws Exception {
        validarVertices(vertices);
        Line nuevo = new Line(this, vertices);
        nuevo.material = material;
        primitiveList = Arrays.copyOf(primitiveList, primitiveList.length + 1);
        primitiveList[primitiveList.length - 1] = nuevo;
        return nuevo;
    }

    public Poly addPoly() {
        Poly nuevo = new Poly(this);
        primitiveList = Arrays.copyOf(primitiveList, primitiveList.length + 1);
        primitiveList[primitiveList.length - 1] = nuevo;
        return nuevo;
    }

    public Poly addPoly(int[] vertices) throws Exception {
        validarVertices(vertices);
        Poly nuevo = new Poly(this, vertices, vertices, vertices);
        primitiveList = Arrays.copyOf(primitiveList, primitiveList.length + 1);
        primitiveList[primitiveList.length - 1] = nuevo;
        return nuevo;
    }

    public Poly addPoly(int[] vertices, int[] normalList, int[] uvList) throws Exception {
        validarVertices(vertices);
        Poly nuevo = new Poly(this, vertices, normalList, uvList);
        primitiveList = Arrays.copyOf(primitiveList, primitiveList.length + 1);
        primitiveList[primitiveList.length - 1] = nuevo;
        return nuevo;
    }

    public Poly addPoly(AbstractMaterial material, int[] vertices) throws Exception {
        validarVertices(vertices);
        Poly nuevo = new Poly(this, vertices);
        nuevo.material = material;
        primitiveList = Arrays.copyOf(primitiveList, primitiveList.length + 1);
        primitiveList[primitiveList.length - 1] = nuevo;
        return nuevo;
    }

    public Poly addPoly(AbstractMaterial material, int[] vertices, int[] normalList, int[] uvList) throws Exception {
        validarVertices(vertices);
        Poly nuevo = new Poly(this, vertices, normalList, uvList);
        nuevo.material = material;
        primitiveList = Arrays.copyOf(primitiveList, primitiveList.length + 1);
        primitiveList[primitiveList.length - 1] = nuevo;
        return nuevo;
    }

    public Poly addPoly(AbstractMaterial material, boolean smooth, int[] vertices) throws Exception {
        validarVertices(vertices);
        Poly nuevo = new Poly(this, vertices);
        nuevo.setSmooth(smooth);
        nuevo.material = material;
        primitiveList = Arrays.copyOf(primitiveList, primitiveList.length + 1);
        primitiveList[primitiveList.length - 1] = nuevo;
        return nuevo;
    }

    public Poly addPoly(AbstractMaterial material, boolean smooth, int[] vertices, int[] normalList, int[] uvList)
            throws Exception {
        validarVertices(vertices);
        Poly nuevo = new Poly(this, vertices, normalList, uvList);
        nuevo.setSmooth(smooth);
        nuevo.material = material;
        primitiveList = Arrays.copyOf(primitiveList, primitiveList.length + 1);
        primitiveList[primitiveList.length - 1] = nuevo;
        return nuevo;
    }

    @Override
    public Mesh clone() {
        Mesh nuevo = new Mesh(this.tipo);
        nuevo.timeMark = timeMark;
        // nuevo.vertexList = Arrays.copyOf(vertexList, vertexList.length);
        // nuevo.primitiveList = Arrays.copyOf(primitiveList, primitiveList.length);
        // nuevo.normalList = Arrays.copyOf(normalList, normalList.length);
        // nuevo.uvList = Arrays.copyOf(uvList, uvList.length);
        for (Vertex current : vertexList) {
            // nuevo.addVertex(current.location.x, current.location.y, current.location.z);
            nuevo.addVertex(current);
        }
        for (QVector3 normal : this.normalList) {
            nuevo.addNormal(normal.x, normal.y, normal.z);
        }
        for (QVector2 uv : this.uvList) {
            nuevo.addUV(uv.x, uv.y);
        }
        for (Primitive face : primitiveList) {
            if (face instanceof Poly) {
                Poly poly = nuevo.addPoly();
                poly.setVertexIndexList(Arrays.copyOf(face.vertexIndexList, face.vertexIndexList.length));
                poly.setNormalIndexList(Arrays.copyOf(face.normalIndexList, face.normalIndexList.length));
                poly.setUVList(Arrays.copyOf(face.uvIndexList, face.uvIndexList.length));
                poly.material = face.material;
                poly.setNormal(((Poly) face).getNormal());
                poly.setSmooth(((Poly) face).isSmooth());
                poly.setNormalInversa(((Poly) face).isNormalInversa());
            }
        }
        return nuevo;
    }

    public void destroy() {
        // destruir();
    }

    @Override
    public void destruir() {
        deleteData();
        // try {
        // for (Primitive face : primitiveList) {
        // face.mesh = null;
        // }
        // } catch (Exception e) {

        // }
        // vertexList = null;
        // primitiveList = null;
    }

    private void validarVertices(int... vertices) throws Exception {
        for (int i : vertices) {
            if (i > this.vertexList.length) {
                throw new Exception("Se esta agregando indices que no existen ");
            }
        }
    }

    protected void deleteData() {
        this.vertexList = new Vertex[0];
        this.primitiveList = new Primitive[0];
        this.normalList = new QVector3[0];
    }

    public void invertNormals() {
        NormalUtil.invertirNormales(this);
        // updateTimeMark();
    }

    public void computeNormals() {
        NormalUtil.computeNormals(this);
        // updateTimeMark();
    }

    public Mesh smooth() {
        MaterialUtil.smooth(this, true);
        updateTimeMark();
        return this;
    }

    public Mesh unSmooth() {
        MaterialUtil.smooth(this, false);
        updateTimeMark();
        return this;
    }

    public void applyMaterial(AbstractMaterial material) {
        MaterialUtil.applyMaterial(this, material);
    }

    public void updateTimeMark() {
        timeMark = System.nanoTime();
    }
}
