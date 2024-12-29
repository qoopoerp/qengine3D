/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape.wire;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.material.basico.QMaterialBas;

/**
 *
 * @author alberto
 */
public class QEspiral extends Shape {

    private float radio;// el radio de la espiral
    private float largo; // el largo de la espiral
    private float nSegmentos;// el numero de segmentos

    public QEspiral(float radio, float largo, float nSegmentos) {
        nombre = "Espiral";
        material = new QMaterialBas("Espiral");
        this.radio = radio;
        this.largo = largo;
        this.nSegmentos = nSegmentos;
        tipo = Mesh.GEOMETRY_TYPE_WIRE;
        build();
    }

    @Override
    public void build() {
        deleteData();

        double delta = largo / nSegmentos;
        for (int i = 0; i < nSegmentos + 1; i++) {
            // transformables.add(new Segment(new Vertex(i * delta, 0, 0), new Vertex((i +
            // 1) * delta, 0, 0)));
            this.addVertex((float) (i * delta), 0, 0);
        }
        for (int i = 0; i < nSegmentos - 1; i++) {
            try {
                this.agregarLinea(material, i, i + 1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // for (int i = 0; i < this.vertices.length; i++) {
        // calculateSpirale(vertices[i], radio, largo);
        // }
    }

    private void calculateSpirale(Vertex v, double r, double h) {
        double x;
        // save old x value
        x = v.location.x;

        // calculate
        v.location.x = (float) (h * x / Math.PI);
        v.location.y = (float) (r * Math.cos(x));
        v.location.z = (float) (r * Math.sin(x));
    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    public float getLargo() {
        return largo;
    }

    public void setLargo(float largo) {
        this.largo = largo;
    }

    public float getnSegmentos() {
        return nSegmentos;
    }

    public void setnSegmentos(float nSegmentos) {
        this.nSegmentos = nSegmentos;
    }

}
