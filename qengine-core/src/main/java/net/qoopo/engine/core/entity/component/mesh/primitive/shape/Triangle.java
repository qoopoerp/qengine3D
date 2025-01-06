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
            addVertex(0, lado, 0);
            addUV(0.5f, 1);
            addVertex(-lado / 2, 0, 0);
            addUV(0, 0);
            addVertex(lado / 2, 0, 0);
            addUV(1, 0);
            addNormal(0, 0, 1);
            addNormal(0, 0, -1);
            // caras
            this.addPoly(material, new int[] { 0, 1, 2 }, new int[] { 0, 0, 0 }, new int[] { 0, 1, 2 });
            this.addPoly(material, new int[] { 0, 2, 1 }, new int[] { 1, 1, 1 }, new int[] { 0, 2, 1 });

            computeNormals();
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
