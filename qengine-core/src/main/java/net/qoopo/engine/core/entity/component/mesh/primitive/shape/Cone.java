/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import net.qoopo.engine.core.entity.component.mesh.generator.MeshGenerator;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.entity.component.modifier.generate.RevolutionModifier;
import net.qoopo.engine.core.material.Material;

/**
 *
 * @author alberto
 */
public class Cone extends Shape {

    private float radio;
    private float alto;
    private int secciones;

    public Cone() {
        material = new Material("Cono");
        name = "Cono";
        radio = 1;
        alto = 1;
        secciones = 36;
        build();
    }

    public Cone(float alto, float radio) {
        name = "Cono";
        material = new Material("Cono");
        this.radio = radio;
        this.alto = alto;
        secciones = 36;
        build();
    }

    public Cone(float alto, float radio, int secciones) {
        name = "Cono";
        material = new Material("Cono");
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
        this.addVertex(radio, -alto / 2, 0);
        this.addVertex(0, -alto / 2, 0);
        // MeshGenerator.generateRevolutionMesh(this, secciones);
        new RevolutionModifier(secciones).apply(this);
        for (int i = 0; i < this.primitiveList.length; i += 2) {
            ((Poly) primitiveList[i]).setSmooth(true);
        }
        computeNormals();
        applyMaterial(material);
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
