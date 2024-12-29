/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.material.basico.QMaterialBas;

/**
 * Crea un Plano
 *
 * @author alberto
 */
public class Plane extends Shape {

    private float alto;
    private float ancho;

    public Plane(float alto, float ancho) {
        nombre = "Plano";
        this.alto = alto;
        this.ancho = ancho;
        material = new QMaterialBas("Plano");
        build();
    }

    @Override
    public void build() {
        try {
            deleteData();

            // primer paso generar vertices
            this.addVertex(-ancho / 2, 0, -alto / 2, 0, 1); // primer vertice superior
            this.addVertex(ancho / 2, 0, -alto / 2, 1, 1); // tercer vertice superior
            this.addVertex(ancho / 2, 0, alto / 2, 1, 0); // cuarto vertice superio
            this.addVertex(-ancho / 2, 0, alto / 2, 0, 0); // segundo vertice superior

            // segundo paso generar caras
            this.addPoly(material, 3, 2, 1, 0);// superior
            applyMaterial(material);
            calculateNormals();
        } catch (Exception ex) {
            Logger.getLogger(Plane.class.getName()).log(Level.SEVERE, null, ex);
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
