/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.entity.component.modifier.generate.InflateModifier;
import net.qoopo.engine.core.entity.component.modifier.generate.SubdivisionModifier;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.util.array.IntArray;

/**
 * Geoesfera o Icoesfera
 *
 * @author alberto
 */
public class IcoSphere extends Shape {

    private float radio;
    private int divisiones = 3;

    public IcoSphere() {
        name = "IcoSphere";
        material = new Material("IcoSphere");
        radio = 1;
        build();
    }

    public IcoSphere(float radio) {
        name = "IcoSphere";
        material = new Material("IcoSphere");
        this.radio = radio;
        build();
    }

    public IcoSphere(float radio, int divisiones) {
        name = "IcoSphere";
        material = new Material("IcoSphere");
        this.radio = radio;
        this.divisiones = divisiones;
        build();
    }

    /**
     * Construye una esfera http://www.songho.ca/opengl/gl_sphere.html
     */
    @Override
    public void build() {
        deleteData();
        // paso 1.- generar el icosaedro origen
        crearOriginal();
        computeNormals();
        new SubdivisionModifier(SubdivisionModifier.TYPE_SIMPLE, divisiones).apply(this);
        new InflateModifier(radio).apply(this);
        computeNormals();
        smooth();
        applyMaterial(material);
    }

    /**
     * Crea la forma original
     */
    private void crearOriginal() {
        try {
            Material blanco = new Material("blanco");
            blanco.setColor(QColor.WHITE);
            Material rojo = new Material("rojo");
            rojo.setColor(QColor.RED);
            Material azul = new Material("azul");
            azul.setColor(QColor.BLUE);
            Material amarillo = new Material("amarillo");
            amarillo.setColor(QColor.YELLOW);

            addVertex(radio / 2, radio / 2, radio / 2);// 0
            addVertex(-radio / 2, -radio / 2, radio / 2);// 1
            addVertex(-radio / 2, radio / 2, -radio / 2);// 2
            addVertex(radio / 2, -radio / 2, -radio / 2);// 3
            addVertex(-radio / 2, radio / 2, -radio / 2);// 4

            addUV(0, 0);
            addUV(0, 0);
            addUV(0, 0);
            addUV(0, 0);
            addUV(0, 0);

            addPoly(blanco, IntArray.of(1, 0, 2));
            addPoly(rojo, IntArray.of(0, 1, 3));
            addPoly(amarillo, IntArray.of(3, 4, 0));
            addPoly(azul, IntArray.of(2, 3, 1));
        } catch (Exception ex) {
            Logger.getLogger(IcoSphere.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    public int getDivisiones() {
        return divisiones;
    }

    public void setDivisiones(int divisiones) {
        this.divisiones = divisiones;
    }

}
