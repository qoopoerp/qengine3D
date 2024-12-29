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
public class Box extends Shape {

    // variables usadas para tener los valores anterior y solo construir en caso de
    // que cambie
    // private float anchoAnt;
    // private float altoAnt;
    // private float largoAnt;
    private float ancho;
    private float alto;
    private float largo;

    private boolean mapUVtoFace = false;

    public Box() {
        material = new QMaterialBas("Caja");
        nombre = "Caja";
        ancho = 1;
        alto = 1;
        largo = 1;
        build();
    }

    public Box(float lado) {
        material = new QMaterialBas("Caja");
        nombre = "Caja";
        this.ancho = lado;
        this.alto = lado;
        this.largo = lado;
        build();
    }

    public Box(float lado, boolean mapUVtoFace) {
        material = new QMaterialBas("Caja");
        nombre = "Caja";
        this.ancho = lado;
        this.alto = lado;
        this.largo = lado;
        this.mapUVtoFace = mapUVtoFace;
        build();
    }

    public Box(float alto, float ancho, float largo) {
        material = new QMaterialBas("Caja");
        nombre = "Caja";
        this.ancho = ancho;
        this.alto = alto;
        this.largo = largo;
        build();
    }

    public Box(float alto, float ancho, float largo, boolean mapUVtoFace) {
        material = new QMaterialBas("Caja");
        nombre = "Caja";
        this.ancho = ancho;
        this.alto = alto;
        this.largo = largo;
        this.mapUVtoFace = mapUVtoFace;
        build();
    }

    @Override
    public void build() {
        try {
            if (mapUVtoFace) {
                buildUVFaceMap();
            } else {
                buildSkyboxMap();
            }
        } catch (Exception e) {

        }
    }

    /**
     * Construye la caja con un mapa uv para cada cara
     */
    public void buildUVFaceMap() {
        try {

            deleteData();

            // Cubo con mapa UV para cada plano

            // vertices
            // cara frontal

            this.addVertex(-ancho / 2, alto / 2, largo / 2, 0, 1); // 0
            this.addVertex(ancho / 2, alto / 2, largo / 2, 1, 1); // 1
            this.addVertex(ancho / 2, -alto / 2, largo / 2, 1, 0); // 2
            this.addVertex(-ancho / 2, -alto / 2, largo / 2, 0, 0); // 3
            // cara trasera
            this.addVertex(-ancho / 2, alto / 2, -largo / 2, 1, 0); // 4
            this.addVertex(ancho / 2, alto / 2, -largo / 2, 0, 0); // 5
            this.addVertex(ancho / 2, -alto / 2, -largo / 2, 0, 1); // 6
            this.addVertex(-ancho / 2, -alto / 2, -largo / 2, 1, 1); // 7

            // caras
            // cara frontal
            this.addPoly(3, 1, 0);
            this.addPoly(3, 2, 1);

            // cara trasera
            this.addPoly(5, 7, 4);
            this.addPoly(5, 6, 7);

            // cara lateral
            this.addPoly(2, 5, 1);
            this.addPoly(2, 6, 5);

            // cara lateral
            this.addPoly(4, 3, 0);
            this.addPoly(4, 7, 3);

            // cara superior
            this.addPoly(0, 5, 4);
            this.addPoly(0, 1, 5);

            // cara inferior
            this.addPoly(6, 2, 3);
            this.addPoly(6, 3, 7);
            applyMaterial(material);
            calculateNormals();
        } catch (Exception ex) {
            Logger.getLogger(Box.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * construye una caja con el mapa uv de skybox
     */
    public void buildSkyboxMap() {
        try {

            deleteData();

            // Cubo con mapa UV en forma de skybox (14 vertices)
            // ----------
            // | |
            // | UP |
            // | |
            // ----------------------------------------
            // | | | | |
            // | BACK | LEFT | FRONT | RIGHT |
            // | | | | |
            // ----------------------------------------
            // | |
            // | DOWN |
            // | |
            // ----------
            //
            //
            /// 0---------1
            // | |
            // | UP |
            // | |
            // 2---------3---------4---------5--------6
            // | | | | |
            // | BACK | LEFT | FRONT | RIGHT |
            // | | | | |
            // 7---------8---------9---------10-------11
            // | |
            // | DOWN |
            // | |
            // 12-------13
            //
            // primer paso generar vertices
            // primera linea
            this.addVertex(ancho / 2, alto / 2, -largo / 2, 0.25f, 1.0f);
            this.addVertex(ancho / 2, alto / 2, largo / 2, 0.5f, 1.0f);
            // segunda linea
            this.addVertex(ancho / 2, alto / 2, -largo / 2, 0.f, 0.666f);
            this.addVertex(-ancho / 2, alto / 2, -largo / 2, 0.25f, 0.666f);
            this.addVertex(-ancho / 2, alto / 2, largo / 2, 0.50f, 0.666f);
            this.addVertex(ancho / 2, alto / 2, largo / 2, 0.75f, 0.666f);
            this.addVertex(ancho / 2, alto / 2, -largo / 2, 1f, 0.666f);
            // tercera linea
            this.addVertex(ancho / 2, -alto / 2, -largo / 2, 0.f, 0.333f);
            this.addVertex(-ancho / 2, -alto / 2, -largo / 2, 0.25f, 0.333f);
            this.addVertex(-ancho / 2, -alto / 2, largo / 2, 0.50f, 0.333f);
            this.addVertex(ancho / 2, -alto / 2, largo / 2, 0.75f, 0.333f);
            this.addVertex(ancho / 2, -alto / 2, -largo / 2, 1f, 0.333f);
            // cuarta linea
            this.addVertex(ancho / 2, -alto / 2, -largo / 2, 0.25f, 0);
            this.addVertex(ancho / 2, -alto / 2, largo / 2, 0.5f, 0);

            // segundo paso generar caras
            // cara superior
            this.addPoly(material, 1, 0, 3);
            this.addPoly(material, 3, 4, 1);
            // trasera
            this.addPoly(material, 3, 2, 7);
            this.addPoly(material, 7, 8, 3);
            // izquierda
            this.addPoly(material, 4, 3, 8);
            this.addPoly(material, 8, 9, 4);
            // frente
            this.addPoly(material, 5, 4, 9);
            this.addPoly(material, 9, 10, 5);
            // derecha
            this.addPoly(material, 6, 5, 10);
            this.addPoly(material, 10, 11, 6);
            // abajo
            this.addPoly(material, 9, 8, 12);
            this.addPoly(material, 12, 13, 9);
            calculateNormals();
        } catch (Exception ex) {
            Logger.getLogger(Box.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public float getAncho() {
        return ancho;
    }

    public void setAncho(float ancho) {
        this.ancho = ancho;
    }

    public float getAlto() {
        return alto;
    }

    public void setAlto(float alto) {
        this.alto = alto;
    }

    public float getLargo() {
        return largo;
    }

    public void setLargo(float largo) {
        this.largo = largo;
    }

}