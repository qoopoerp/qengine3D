/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import net.qoopo.engine.core.entity.component.mesh.generator.MeshGenerator;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPoligono;
import net.qoopo.engine.core.entity.component.mesh.primitive.QShape;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.mesh.QUtilNormales;

/**
 *
 * @author alberto
 */
public class QCono extends QShape {

    private float radio;
    private float alto;
    private int secciones;

    public QCono() {
        material = new QMaterialBas("Cono");
        nombre = "Cono";
        radio = 1;
        alto = 1;
        secciones = 36;
        build();
    }

    public QCono(float alto, float radio) {
        nombre = "Cono";
        material = new QMaterialBas("Cono");
        this.radio = radio;
        this.alto = alto;
        secciones = 36;
        build();
    }

    public QCono(float alto, float radio, int secciones) {
        nombre = "Cono";
        material = new QMaterialBas("Cono");
        this.radio = radio;
        this.alto = alto;
        this.secciones = secciones;
        build();
    }

    @Override
    public void build() {
        deleteData();
        // primer paso generar vertices
        this.addVertex(0, alto / 2, 0, 0, 1);
        this.addVertex(radio, -alto / 2, 0, 0, 0);
        this.addVertex(0, -alto / 2, 0, 0, 0);
        MeshGenerator.generateRevolutionMesh(this, secciones);
        for (int i = 0; i < this.primitivas.length; i += 2) {
            ((QPoligono) primitivas[i]).setSmooth(true);
        }
        // QGenerador.generarRevolucion(this, secciones, false, false, true, true);
        QUtilNormales.calcularNormales(this);
        QMaterialUtil.aplicarMaterial(this, material);
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
