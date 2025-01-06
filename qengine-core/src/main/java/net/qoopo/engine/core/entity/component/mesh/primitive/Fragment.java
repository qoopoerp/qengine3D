package net.qoopo.engine.core.entity.component.mesh.primitive;

import java.io.Serializable;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.math.QVector4;

/**
 * Pixel que pertenece al buffer de pixel
 *
 * @author alberto
 */
public class Fragment implements Serializable {

    private boolean dibujar = false;
    public float u, v;
    public QVector4 ubicacion = new QVector4();
    public QVector3 normal = QVector3.empty();
    public QVector3 up = QVector3.empty();//usada para el mapa normal con los somreadores nodo y no en el raster
    public QVector3 right = QVector3.empty();//usada para el mapa normal con los somreadores nodo y no en el raster
    public AbstractMaterial material;
    public Primitive primitiva;
    public Entity entity;

    public Fragment() {
    }

    public boolean isDibujar() {
        return dibujar;
    }

    public void setDibujar(boolean dibujar) {
        this.dibujar = dibujar;
    }

    public float getX() {
        return ubicacion.x;
    }

    public void setX(float x) {
        this.ubicacion.x = x;
    }

    public float getY() {
        return ubicacion.y;
    }

    public void setY(float y) {
        this.ubicacion.y = y;
    }

    public float getZ() {
        return ubicacion.z;
    }

    public void setZ(float z) {
        this.ubicacion.z = z;
    }

    public float getU() {
        return u;
    }

    public void setU(float u) {
        this.u = u;
    }

    public float getV() {
        return v;
    }

    public void setV(float v) {
        this.v = v;
    }

    public QVector3 getNormal() {
        return normal;
    }

    public void setNormal(QVector3 normal) {
        this.normal = normal;
    }

    public QVector3 getUp() {
        return up;
    }

    public void setUp(QVector3 arriba) {
        this.up = arriba;
    }

    public QVector3 getRight() {
        return right;
    }

    public void setRight(QVector3 derecha) {
        this.right = derecha;
    }

    public AbstractMaterial getMaterial() {
        return material;
    }

    public void setMaterial(AbstractMaterial material) {
        this.material = material;
    }

    public Primitive getPrimitiva() {
        return primitiva;
    }

    public void setPoligono(Poly poligono) {
        this.primitiva = poligono;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

}
