package net.qoopo.engine.core.entity.component.mesh.primitive;

import java.util.Arrays;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.math.QVector3;

/**
 * Un poligono se dibuja agregando vertices en el orden contrario a las agujas
 * del reloj
 *
 * @author alberto
 */
public class Poly extends Primitive implements Comparable<Poly> {

    private QVector3 normal = QVector3.empty();
    // la normal transformada
    private QVector3 normalCopy = QVector3.empty();
    private boolean smooth = false;
    private Vertex center = new Vertex();
    // cel centor transformado
    private Vertex centerCopy = new Vertex();
    private boolean normalInversa = false;

    public Poly() {
    }

    public Poly(Mesh parent) {
        super(parent);
    }

    public Poly(Mesh parent, int[] vertexList) {
        super(parent, vertexList);
    }

    // public Poly(Mesh parent,List<Integer> vertexList){
    //     super(parent, (Integer[])vertexList.toArray());
    // }

    public Poly(Mesh parent, int[] vertexList, int[] normalList, int[] uvList) {
        super(parent, vertexList, normalList, uvList);
    }

    public boolean verificarPuntoEnPlano(QVector3 punto) {
        return verificarPuntoEnPlano(punto, 0);
    }

    public boolean verificarPuntoEnPlano(QVector3 punto, float tolerancia) {
        // primero calculamos la distancia al plano
        float distancia = punto.dot(this.normal);
        if (distancia >= -tolerancia && distancia <= tolerancia) {
            // falta verificar los limites con los puntos hasta ahorita solo sabemos si esta
            // en el plano
            return true;
        }
        return false;
    }

    public void computeNormalCenter() {
        computeNormalCenter(mesh.vertexList);
        normalInversa = false;
    }

    public void computeNormalCenter(Vertex[] vertexList) {
        if (vertexList.length >= 3) {
            try {
                normal.set(vertexList[vertexIndexList[0]], vertexList[vertexIndexList[1]]);
                normal.cross(QVector3.of(vertexList[vertexIndexList[0]], vertexList[vertexIndexList[2]]));
                normal.normalize();
                int count = 0;
                center.location.set(0, 0, 0, 1);
                for (int i : vertexIndexList) {
                    center.location.add(vertexList[i].location);
                    count++;
                }
                center.location.multiply(1.0f / count);
                centerCopy.copyAttribute(center);
                normalCopy.set(normal);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public String toString() {
        return "Poly{" + "listaVertices=" + Arrays.toString(vertexIndexList) + '}';
    }

    @Override
    public int compareTo(Poly other) {
        return Float.valueOf(centerCopy.location.z).compareTo(other.centerCopy.location.z);
        // return Float.valueOf(other.centerCopy.z).compareTo(centerCopy.z);
    }

    public QVector3 getNormal() {
        return normal;
    }

    public void setNormal(QVector3 normal) {
        this.normal = normal;
    }

    public QVector3 getNormalCopy() {
        return normalCopy;
    }

    public void setNormalCopy(QVector3 normalCopy) {
        this.normalCopy = normalCopy;
    }

    public boolean isSmooth() {
        return smooth;
    }

    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
    }

    public Vertex getCenter() {
        return center;
    }

    public void setCenter(Vertex center) {
        this.center = center;
    }

    public Vertex getCenterCopy() {
        return centerCopy;
    }

    public void setCenterCopy(Vertex centerCopy) {
        this.centerCopy = centerCopy;
    }

    public boolean isNormalInversa() {
        return normalInversa;
    }

    public void setNormalInversa(boolean normalInversa) {
        this.normalInversa = normalInversa;
    }

}
