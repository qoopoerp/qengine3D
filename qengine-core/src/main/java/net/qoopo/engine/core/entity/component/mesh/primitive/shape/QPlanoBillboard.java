/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.mesh.NormalUtil;

/**
 * Crea un Plano Billboard, que está destinado a mirar siempre a la cámara, solo
 * tiene un lado
 *
 * @author alberto
 */
public class QPlanoBillboard extends Shape {

    private float alto;
    private float ancho;

    public QPlanoBillboard(float alto, float ancho) {
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

            // segundo paso generar caras
            this.addPoly(material, new int[] { 3, 2, 1, 0 }, new int[] { 0, 0, 0, 0 }, new int[] { 0, 1, 2, 3 });// superior
            applyMaterial(material);
            computeNormals();
        } catch (Exception ex) {
            Logger.getLogger(QPlanoBillboard.class.getName()).log(Level.SEVERE, null, ex);
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
