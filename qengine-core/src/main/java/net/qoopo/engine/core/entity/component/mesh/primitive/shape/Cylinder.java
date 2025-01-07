/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import net.qoopo.engine.core.material.basico.Material;

/**
 *
 * @author alberto
 */
public class Cylinder extends Prism {

    private float radio;
    // private float alto;
    private int secciones;

    public Cylinder() {
        material = new Material("Cilindro");
        name = "Cilindro";
        radio = 1;
        alto = 1;
        secciones = 36;
        build();
    }

    public Cylinder(float alto, float radio) {
        material = new Material("Cilindro");
        name = "Cilindro";
        this.radio = radio;
        this.alto = alto;
        secciones = 36;
        build();
    }

    public Cylinder(float alto, float radio, int secciones) {
        material = new Material("Cilindro");
        name = "Cilindro";
        this.radio = radio;
        this.alto = alto;
        this.secciones = secciones;
        build();
    }

    @Override
    public void build() {
        deleteData();
        super.setSecciones(secciones);
        super.setSeccionesVerticales(3);
        super.setRadioSuperior(radio);
        super.setRadioInferior(radio);
        super.setAlto(alto);
        super.build();

        // primer paso generar vertices
        // this.addVertex(0, alto / 2, 0, 0, 1);
        // this.addVertex(radio, alto / 2, 0, 0, 1);
        // this.addVertex(radio, -alto / 2, 0, 0, 0);
        // this.addVertex(0, -alto / 2, 0, 0, 0);
        // QGenerador.generarRevolucion(this, secciones, false, false, true, false);
        // QUtilNormales.calcularNormales(this);
        // QMaterialUtil.aplicarMaterial(this, material);
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
