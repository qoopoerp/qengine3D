/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.component.mesh.primitive.QShape;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.mesh.QUtilNormales;

/**
 * Crea un Plano Billboard, que está destinado a mirar siempre a la cámara, solo
 * tiene un lado
 *
 * @author alberto
 */
public class QPlanoBillboard extends QShape {

    private float alto;
    private float ancho;

    public QPlanoBillboard(float alto, float ancho) {
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

            this.addVertex(-ancho / 2, -alto / 2, 0, 0, 1); // primer vertice superiro
            this.addVertex(ancho / 2, -alto / 2, 0, 1, 1); // tercer vertice superior
            this.addVertex(ancho / 2, alto / 2, 0, 1, 0); // cuarto vertice superio
            this.addVertex(-ancho / 2, alto / 2, 0, 0, 0); // segundo vertice superior

            // segundo paso generar caras
            this.addPoly(material, 3, 2, 1, 0);// superior

            QMaterialUtil.aplicarMaterial(this, material);
            QUtilNormales.calcularNormales(this);
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
