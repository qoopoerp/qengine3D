package net.qoopo.engine.core.entity.component.mesh.primitive;

import java.io.Serializable;
import java.util.Arrays;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.math.QVector3;

/**
 * Un poligono se dibuja agregando vertices en el orden contrario a las agujas
 * del reloj
 *
 * @author alberto
 */
public class QPoligono extends QPrimitiva implements Comparable<QPoligono> {

    private QVector3 normal = QVector3.empty();
    // la normal transformada
    private QVector3 normalCopy = QVector3.empty();
    private boolean smooth = false;
    private QVertex center = new QVertex();
    // cel centor transformado
    private QVertex centerCopy = new QVertex();
    private boolean normalInversa = false;

    public QPoligono() {
    }

    public QPoligono(Mesh parent) {
        super(parent);
    }

    public QPoligono(Mesh parent, int... vertices) {
        // this(parent);
        super(parent, vertices);
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

    public void calculaNormalYCentro() {
        calculaNormalYCentro(geometria.vertices);
        normalInversa = false;
    }

    public void calculaNormalYCentro(QVertex[] vertices) {
        if (vertices.length >= 3) {
            try {
                // normal = QVector3.of(vertices[vertices[0]], vertices[vertices[1]]);
                normal.set(vertices[listaVertices[0]], vertices[listaVertices[1]]);
                normal.cross(QVector3.of(vertices[listaVertices[0]], vertices[listaVertices[2]]));
                normal.normalize();
                int count = 0;
                center.location.set(0, 0, 0, 1);
                for (int i : listaVertices) {
                    center.location.add(vertices[i].location);
                    count++;
                }
                center.location.multiply(1.0f / count);
                centerCopy.copyAttribute(center);
                normalCopy.set(normal);
            } catch (Exception e) {

            }
        }
    }

    // public void calculaNormalYCentro(QVertice vert1, QVertice vert2, QVertice
    // vert3) {
    // normal = QVector3.of(vert1, vert2);
    // normal.cross(QVector3.of(vert2, vert3));
    // normal.normalize();
    // int count = 0;
    //// center.x = 0;
    //// center.y = 0;
    //// center.z = 0;
    // center.ubicacion.set(0, 0, 0, 0);
    //// for (int i : vertices) {
    //// center.x += geometria.vertices[i].x;
    //// center.y += geometria.vertices[i].y;
    //// center.z += geometria.vertices[i].z;
    //// count++;
    //// }
    //
    // center.ubicacion.x = vert1.ubicacion.x + vert2.ubicacion.x +
    // vert3.ubicacion.x;
    // center.ubicacion.y = vert1.ubicacion.y + vert2.ubicacion.y +
    // vert3.ubicacion.y;
    // center.ubicacion.z = vert1.ubicacion.z + vert2.ubicacion.z +
    // vert3.ubicacion.z;
    //// count = 3;
    //// ------------
    // center.ubicacion.x /= count;
    // center.ubicacion.y /= count;
    // center.ubicacion.z /= count;
    // centerCopy.copyAttribute(center);
    // normalCopy.copyXYZ(normal);
    // }
    @Override
    public String toString() {
        return "QPoligono{" + "listaVertices=" + Arrays.toString(listaVertices) + '}';
    }

    @Override
    public int compareTo(QPoligono other) {
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

    public QVertex getCenter() {
        return center;
    }

    public void setCenter(QVertex center) {
        this.center = center;
    }

    public QVertex getCenterCopy() {
        return centerCopy;
    }

    public void setCenterCopy(QVertex centerCopy) {
        this.centerCopy = centerCopy;
    }

    public boolean isNormalInversa() {
        return normalInversa;
    }

    public void setNormalInversa(boolean normalInversa) {
        this.normalInversa = normalInversa;
    }

}
