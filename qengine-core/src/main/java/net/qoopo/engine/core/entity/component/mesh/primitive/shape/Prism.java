/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QVector3;

/**
 * Un prisma, ​ en geometría, es un poliedro que consta de dos caras iguales y
 * paralelas llamadas bases, y de caras laterales que son paralelogramos. Los
 * prismas se nombran por la forma de su base, por lo que un prisma de base
 * pentagonal se llama prisma pentagonal.​
 * 
 * @author alberto
 */
@Getter
@Setter
public class Prism extends Shape {

    protected float radioSuperior;
    protected float radioInferior;
    protected float alto;
    protected int secciones;
    protected int seccionesVerticales = 1;

    public Prism() {
        material = new QMaterialBas("Prisma");
        nombre = "Prisma";
        radioSuperior = 1;
        radioInferior = 1;
        alto = 1;
        secciones = 36;
        build();
    }

    public Prism(float alto, float radio) {
        material = new QMaterialBas("Prisma");
        nombre = "Prisma";
        radioSuperior = radio;
        radioInferior = radio;
        this.alto = alto;
        secciones = 36;
        build();
    }

    public Prism(float alto, float radio, int secciones) {
        material = new QMaterialBas("Prisma");
        nombre = "Cilindro";
        radioSuperior = radio;
        radioInferior = radio;
        this.alto = alto;
        this.secciones = secciones;
        build();
    }

    public Prism(float alto, float radio, int secciones, int seccionesVerticales) {
        material = new QMaterialBas("Prisma");
        nombre = "Prisma";
        radioSuperior = radio;
        radioInferior = radio;
        this.alto = alto;
        this.secciones = secciones;
        this.seccionesVerticales = seccionesVerticales;
        build();
    }

    public Prism(float alto, float radio, float radioInferior, int secciones, int seccionesVerticales) {
        material = new QMaterialBas("Prisma");
        nombre = "Prisma";
        radioSuperior = radio;
        this.radioInferior = radioInferior;
        this.alto = alto;
        this.secciones = secciones;
        this.seccionesVerticales = seccionesVerticales;
        build();
    }

    private boolean validaVertice(String nombre, int vertice) {
        if (this.vertexList.length < vertice) {
            System.out.println(nombre + " fuera de rango " + vertice + " limite " + this.vertexList.length);
            return false;
        }
        return true;
    }

    @Override
    public void build() {
        deleteData();

        // paso 1 generar los vertices superiores, inferiores e intermedios dependiendo
        // del numero de secciones verticales
        // superior
        // QVector3 inicial = QVector3.of(0,alto, radioSuperior);
        int indiceOffset = 0;
        int indiceOffsetAux = 0;

        // para centrarlo
        float ajusteAltura = alto / 2;

        float angulo = 360.0f / secciones;
        QVector3 vUV = QVector3.of(0.5f, 0.f, 0);
        QVector3 vector = QVector3.of(0, alto, radioSuperior);

        // cara superior - vertices
        for (int i = 0; i <= secciones; i++) {

            this.addVertex(vector.x, vector.y, vector.z);
            this.addUV(vUV.x, vUV.y);
            vector.rotateY((float) Math.toRadians(angulo));
            vUV.rotateZ((float) Math.toRadians(angulo));
            indiceOffsetAux++;

        }
        // cara superior - triangulos
        for (int i = 1; i < this.vertexList.length - 1; i++) {
            try {
                this.addPoly(new int[] { 0, i, i + 1 }, new int[] {}, new int[] { 0, i, i + 1 });
            } catch (Exception ex) {
                Logger.getLogger(Prism.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        indiceOffset = indiceOffsetAux;

        // ---------------------- CUERPO -----------------------------------
        // cuerpo, para separar las caras superiores de las laterales (para el
        // suavizado)
        // vuelvo a crear los vertices de los niveles superior, inferior y los
        // intermedios
        for (int nivel = 0; nivel <= seccionesVerticales; nivel++) {
            vector.set(0, alto - alto / seccionesVerticales * nivel, radioSuperior);
            for (int i = 0; i <= secciones; i++) {
                this.addVertex(vector.x, vector.y, vector.z);
                this.addUV(1.0f / secciones * i, 1.0f - 1.0f / seccionesVerticales * nivel);
                vector.rotateY((float) Math.toRadians(angulo));
                indiceOffsetAux++;
            }
        }

        // cuerpo crea los triangulos
        // triangulos
        // k1--k1+1
        // | / |
        // | / |
        // k2--k2+1
        int k1, k2;
        for (int i = 0; i < seccionesVerticales - 1; i++) {
            k1 = i * (secciones + 1); // beginning of current stack
            k2 = k1 + secciones + 1; // beginning of next stack
            for (int j = 0; j < secciones; j++, k1++, k2++) {

                try {
                    validaVertice("k1 + indiceOffset", k1 + indiceOffset);
                    validaVertice(" k2 + indiceOffset", k2 + indiceOffset);
                    validaVertice(" k1 + 1 + indiceOffset", k1 + 1 + indiceOffset);

                    addPoly(new int[] { k1 + indiceOffset, k2 + indiceOffset, k1 + 1 + indiceOffset },
                            new int[] {},
                            new int[] { k1 + indiceOffset, k2 + indiceOffset, k1 + 1 + indiceOffset }).setSmooth(true);
                    // addPoly(k1 + 1 + indiceOffset, k2 + indiceOffset, k1 +
                    // indiceOffset).smooth = true;

                    validaVertice("k1 + 1 + indiceOffset", k1 + 1 + indiceOffset);
                    validaVertice(" k2 + indiceOffset", k2 + indiceOffset);
                    validaVertice(" k2 + 1 + indiceOffset", k2 + 1 + indiceOffset);

                    addPoly(new int[] { k1 + 1 + indiceOffset, k2 + indiceOffset, k2 + 1 + indiceOffset },
                            new int[] {},
                            new int[] { k1 + 1 + indiceOffset, k2 + indiceOffset, k2 + 1 + indiceOffset })
                            .setSmooth(true);
                    // addPoly(k2 + 1 + indiceOffset, k2 + indiceOffset, k1 + 1 +
                    // indiceOffset).smooth = true;
                } catch (Exception ex) {
                    Logger.getLogger(Prism.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        indiceOffset = indiceOffsetAux;
        // cara inferior - vertices
        vector.set(0, 0, radioSuperior);
        vUV.set(0.5f, 0.f, 0);
        for (int i = 0; i <= secciones; i++) {
            this.addVertex(vector.x, vector.y, vector.z);
            this.addUV(vUV.x, vUV.y);
            vector.rotateY((float) Math.toRadians(angulo));
            vUV.rotateZ((float) Math.toRadians(angulo));
            indiceOffsetAux++;

        }
        // cara inferior - triangulos
        for (int i = indiceOffset + 1; i < this.vertexList.length - 1; i++) {
            try {
                this.addPoly(new int[] { i + 1, i, indiceOffset },
                        new int[] {},
                        new int[] { i + 1, i, indiceOffset });
            } catch (Exception ex) {
                Logger.getLogger(Prism.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        indiceOffset = indiceOffsetAux;

        // ajustamos la altura
        for (Vertex v : this.vertexList) {
            v.location.y -= ajusteAltura;
        }

        computeNormals();
        applyMaterial(material);
    }

    public float getAlto() {
        return alto;
    }

    public void setAlto(float alto) {
        this.alto = alto;
    }

    public float getRadioSuperior() {
        return radioSuperior;
    }

    public void setRadioSuperior(float radioSuperior) {
        this.radioSuperior = radioSuperior;
    }

    public float getRadioInferior() {
        return radioInferior;
    }

    public void setRadioInferior(float radioInferior) {
        this.radioInferior = radioInferior;
    }

    public int getSecciones() {
        return secciones;
    }

    public void setSecciones(int secciones) {
        this.secciones = secciones;
    }

    public int getSeccionesVerticales() {
        return seccionesVerticales;
    }

    public void setSeccionesVerticales(int seccionesVerticales) {
        this.seccionesVerticales = seccionesVerticales;
    }

}
