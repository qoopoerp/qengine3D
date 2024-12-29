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
import net.qoopo.engine.core.util.mesh.NormalUtil;

/**
 *
 * @author alberto
 */
public class Triangle extends Shape {

    private float lado;

    public Triangle(float lado) {
        nombre = "Triangulo";
        material = new QMaterialBas("Triángulo");
        this.lado = lado;
        build();
    }

    @Override
    public void build() {
        try {
            deleteData();
            /*
             * P1
             * / \
             * / \
             * / \
             * P2/_______\P3
             */
            // primer paso generar vertices
            this.addVertex(0, lado, 0, 0.5f, 1);
            this.addVertex(-lado / 2, 0, 0, 0, 0);
            this.addVertex(lado / 2, 0, 0, 1, 0);
            // caras
            this.addPoly(material, 0, 1, 2);
            this.addPoly(material, 0, 2, 1);

            NormalUtil.calcularNormales(this);
        } catch (Exception ex) {
            Logger.getLogger(Triangle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public float getLado() {
        return lado;
    }

    public void setLado(float lado) {
        this.lado = lado;
    }

}
