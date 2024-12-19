/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import net.qoopo.engine.core.entity.component.mesh.generator.MeshGenerator;
import net.qoopo.engine.core.entity.component.mesh.primitive.QShape;
import net.qoopo.engine.core.entity.component.mesh.primitive.QVertex;
import net.qoopo.engine.core.entity.component.transform.QTransformacion;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.mesh.QUtilNormales;

/**
 *
 * @author alberto
 */
public class QCilindroZ extends QShape {

    private float radio;
    private float alto;
    private int secciones;

    public QCilindroZ() {
        material = new QMaterialBas("Cilindro");
        nombre = "CilindroZ";
        radio = 1;
        alto = 1;
        secciones = 36;
        build();
    }

    public QCilindroZ(float alto, float radio) {
        material = new QMaterialBas("Cilindro");
        nombre = "CilindroZ";
        this.radio = radio;
        this.alto = alto;
        secciones = 36;
        build();
    }

    public QCilindroZ(float alto, float radio, int secciones) {
        material = new QMaterialBas("Cilindro");
        nombre = "CilindroZ";
        this.radio = radio;
        this.alto = alto;
        this.secciones = secciones;
        build();
    }

    @Override
    public void build() {
        deleteData();

        // primer paso generar vertices
        this.addVertex(0, alto / 2, 0);
        this.addVertex(radio, alto / 2, 0);
        this.addVertex(radio, -alto / 2, 0);
        this.addVertex(0, -alto / 2, 0);
        MeshGenerator.generateRevolutionMesh(this, secciones);
        QUtilNormales.calcularNormales(this);
        QMaterialUtil.aplicarMaterial(this, material);
        // ahora roto los vertices para que se alineen al eje x
        QTransformacion tra = new QTransformacion();
        tra.getRotacion().rotarX((float) Math.toRadians(90));
        tra.getRotacion().actualizarCuaternion();
        QVector3 tmp = QVector3.empty();
        for (QVertex vertice : vertices) {
            tmp.set(vertice.location.x, vertice.location.y, vertice.location.z);
            tmp = tra.toTransformMatriz().mult(tmp);
            vertice.setXYZ(tmp.x, tmp.y, tmp.z);
        }
    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    public float getAlto() {
        return alto;
    }

    public void setAlto(float alto) {
        this.alto = alto;
    }

    public int getSecciones() {
        return secciones;
    }

    public void setSecciones(int secciones) {
        this.secciones = secciones;
    }

}
