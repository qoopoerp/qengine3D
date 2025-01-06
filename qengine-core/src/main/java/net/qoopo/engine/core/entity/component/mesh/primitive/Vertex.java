package net.qoopo.engine.core.entity.component.mesh.primitive;

import java.io.Serializable;
import java.util.Arrays;

import net.qoopo.engine.core.entity.component.animation.Bone;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.math.QVector4;

/**
 * Representa un vértice de los modelos 3D. Un vértice contiene coordenadas en
 * el espacio 3D, La normal de vértice, si el modelo tiene un esqueleto, el
 * vértice tiene una lista de los huesos y sus pesos
 *
 * @author alberto
 */
public class Vertex implements Serializable {

    public static final Vertex ZERO = new Vertex(0, 0, 0, 1);

    public QVector4 location = new QVector4();

    // public QVector3 normal = QVector3.unitario_y.clone();

    // public float u, v;
    public QVector3 up = QVector3.unitario_y.clone();
    public QVector3 right = QVector3.unitario_x.clone();
    public boolean normalInversa = false;

    /**
     * lista de huesos que afectan a este vértice
     */
    private Bone listaHuesos[] = new Bone[0];
    /**
     * lista de los pesos de cada hueso
     */
    private float listaHuesosPesos[] = new float[0];
    /**
     * lista de los ids de los huesos, se usa en el procesa de carga cuando aun
     * no hay un esqueleto pero si se tiene el id del hueso (ASSIMP)
     */
    private int listaHuesosIds[] = new int[0];

    public Vertex() {
        location.x = 0;
        location.y = 0;
        location.z = 0;
        location.w = 1; // un vertice es de posicion siempre
    }

    public Vertex(QVector4 vector4) {
        location.x = vector4.x;
        location.y = vector4.y;
        location.z = vector4.z;
        location.w = vector4.w;
    }

    public Vertex(float x, float y, float z, float w) {
        location.x = x;
        location.y = y;
        location.z = z;
        location.w = w;
    }

    // public Vertex(float x, float y, float z, float w, float u, float v) {
    // location.x = x;
    // location.y = y;
    // location.z = z;
    // location.w = w;
    // this.u = u;
    // this.v = v;
    // }

    public Vertex(float x, float y, float z) {
        location.x = x;
        location.y = y;
        location.z = z;
        location.w = 1;
    }

    // public Vertex(float x, float y, float z, float u, float v) {
    // location.x = x;
    // location.y = y;
    // location.z = z;
    // location.w = 1;
    // this.u = u;
    // this.v = v;
    // }

    public void set(float x, float y, float z, float w) {
        location.x = x;
        location.y = y;
        location.z = z;
        location.w = w;
    }

    public void setXYZ(float x, float y, float z) {
        location.x = x;
        location.y = y;
        location.z = z;
    }

    public void set(Vertex vertice) {
        location.x = vertice.location.x;
        location.y = vertice.location.y;
        location.z = vertice.location.z;
        location.w = vertice.location.w;
        this.listaHuesos = Arrays.copyOf(vertice.listaHuesos, vertice.listaHuesos.length);
        this.listaHuesosIds = Arrays.copyOf(vertice.listaHuesosIds, vertice.listaHuesosIds.length);
        this.listaHuesosPesos = Arrays.copyOf(vertice.listaHuesosPesos, vertice.listaHuesosPesos.length);
        // this.normal.set(vertice.normal);
    }

    public void copyAttribute(Vertex other) {
        location.x = other.location.x;
        location.y = other.location.y;
        location.z = other.location.z;
        location.w = other.location.w;
        // this.normal.set(other.normal);
    }

    public Vertex clone() {
        Vertex result = new Vertex(location.x, location.y, location.z, location.w);
        result.listaHuesos = Arrays.copyOf(listaHuesos, listaHuesos.length);
        result.listaHuesosIds = Arrays.copyOf(listaHuesosIds, listaHuesosIds.length);
        result.listaHuesosPesos = Arrays.copyOf(listaHuesosPesos, listaHuesosPesos.length);
        return result;
    }

    @Override
    public String toString() {
        return "(" + location.x + ", " + location.y + ", " + location.z + ")";
        // return ubicacion.toString();
    }

    public Bone[] getListaHuesos() {
        return listaHuesos;
    }

    public void setListaHuesos(Bone[] listaHuesos) {
        this.listaHuesos = listaHuesos;
    }

    public float[] getListaHuesosPesos() {
        return listaHuesosPesos;
    }

    public void setListaHuesosPesos(float[] listaHuesosPesos) {
        this.listaHuesosPesos = listaHuesosPesos;
    }

    public int[] getListaHuesosIds() {
        return listaHuesosIds;
    }

    public void setListaHuesosIds(int[] listaHuesosIds) {
        this.listaHuesosIds = listaHuesosIds;
    }

    public Vertex multiply(float valor) {
        this.location.multiply(valor);
        // normal.multiply(valor);
        return this;
    }

    public Vertex add(float valor) {
        this.location.add(valor);
        // normal.add(valor);
        return this;
    }

    public static Vertex promediar(Vertex... vert) {
        Vertex ve = new Vertex();
        for (Vertex v : vert) {
            ve.location.x += v.location.x;
            ve.location.y += v.location.y;
            ve.location.z += v.location.z;
            // ve.u += v.u;
            // ve.v += v.v;
            // ve.normal.add(v.normal);
        }

        // promedia
        ve.location.x /= vert.length;
        ve.location.y /= vert.length;
        ve.location.z /= vert.length;
        // ve.u /= vert.length;
        // ve.v /= vert.length;
        // ve.normal.multiply(1.0f / vert.length);
        return ve;
    }

    public static Vertex add(Vertex... vert) {
        Vertex ve = new Vertex();
        for (Vertex v : vert) {
            ve.location.x += v.location.x;
            ve.location.y += v.location.y;
            ve.location.z += v.location.z;
            // ve.u += v.u;
            // ve.v += v.v;
            // ve.normal.add(v.normal);
        }

        // promedia las coordenadas UV y la normal
        // ve.u /= vert.length;
        // ve.v /= vert.length;
        // // ve.normal.multiply(1.0f / vert.length);
        return ve;
    }

    // public static QVertice promediarCatmullClark(QVertice... vert) {
    // QVertice ve = new QVertice();
    // for (QVertice v : vert) {
    // ve.ubicacion.x += v.ubicacion.x;
    // ve.ubicacion.y += v.ubicacion.y;
    // ve.ubicacion.z += v.ubicacion.z;
    // ve.u += v.u;
    // ve.v += v.v;
    // ve.normal.add(v.normal);
    // }
    //
    // //promedia
    // ve.ubicacion.x /= vert.length;
    // ve.ubicacion.y /= vert.length;
    // ve.ubicacion.z /= vert.length;
    // ve.u /= vert.length;
    // ve.v /= vert.length;
    // ve.normal.multiply(1.0f / vert.length);
    //
    // //modifica el nuevo vertice para que se eleve y suavice la supervicie
    // double escala = 1.0f / (Math.sqrt(ve.ubicacion.x * ve.ubicacion.x +
    // ve.ubicacion.y * ve.ubicacion.y + ve.ubicacion.z * ve.ubicacion.z));
    // ve.ubicacion.set(ve.ubicacion.getVector3().multiply((float) escala), 1);
    // return ve;
    // }
}
