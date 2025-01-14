/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.material.Material;

/**
 * Crea un Plano
 *
 * @author alberto
 */
public class QPlanoBilateral extends Shape {

    private float alto;
    private float ancho;

    public QPlanoBilateral(float alto, float ancho) {
        name = "Plano";
        this.alto = alto;
        this.ancho = ancho;
        material = new Material("Plano");
        build();
    }

    @Override
    public void build() {
        try {
            deleteData();

            this.addVertex(-ancho / 2, -alto / 2, 0); // primer vertice superiro
            this.addVertex(ancho / 2, -alto / 2, 0); // tercer vertice superior
            this.addVertex(ancho / 2, alto / 2, 0); // cuarto vertice superio
            this.addVertex(-ancho / 2, alto / 2, 0); // segundo vertice superior

            this.addUV(0, 1);
            this.addUV(1, 1);
            this.addUV(1, 0);
            this.addUV(0, 0);

            this.addNormal(0, 0, 1);
            this.addNormal(0, 0, -1);

            this.addPoly(material, new int[] { 3, 2, 1, 0 }, new int[] { 0, 0, 0, 0 }, new int[] { 0, 1, 2, 3 });// superior
            this.addPoly(material, new int[] { 0, 1, 2, 3 }, new int[] { 1, 1, 1, 1 }, new int[] { 0, 1, 2, 3 });// superior

            applyMaterial(material);
            computeNormals();
        } catch (Exception ex) {
            Logger.getLogger(QPlanoBilateral.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public float getAlto() {
        return alto;
    }

    public void setAlto(float alto) {
        this.alto = alto;
    }

    public float getAncho() {
        return ancho;
    }

    public void setAncho(float ancho) {
        this.ancho = ancho;
    }

}
