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
public class QPixel implements Serializable {

    private boolean dibujar = false;
    public float u, v;
    public QVector4 ubicacion = new QVector4();
    public QVector3 normal = QVector3.empty();
    public QVector3 arriba = QVector3.empty();//usada para el mapa normal con los somreadores nodo y no en el raster
    public QVector3 derecha = QVector3.empty();//usada para el mapa normal con los somreadores nodo y no en el raster
    public AbstractMaterial material;
    public QPrimitiva primitiva;
    public Entity entity;

    public QPixel() {
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

    public QVector3 getArriba() {
        return arriba;
    }

    public void setArriba(QVector3 arriba) {
        this.arriba = arriba;
    }

    public QVector3 getDerecha() {
        return derecha;
    }

    public void setDerecha(QVector3 derecha) {
        this.derecha = derecha;
    }

    public AbstractMaterial getMaterial() {
        return material;
    }

    public void setMaterial(AbstractMaterial material) {
        this.material = material;
    }

    public QPrimitiva getPrimitiva() {
        return primitiva;
    }

    public void setPoligono(QPoligono poligono) {
        this.primitiva = poligono;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

}
