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
import net.qoopo.engine.core.util.mesh.QUtilNormales;

/**
 *
 * @author alberto
 */
public class QCaja extends QShape {

    //variables usadas para tener los valores anterior y solo construir en caso de que cambie
//    private float anchoAnt;
//    private float altoAnt;
//    private float largoAnt;
    private float ancho;
    private float alto;
    private float largo;

    public QCaja() {
        material = new QMaterialBas("Caja");
        nombre = "Caja";
        ancho = 1;
        alto = 1;
        largo = 1;
        build();
    }

    public QCaja(float lado) {
        material = new QMaterialBas("Caja");
        nombre = "Caja";
        this.ancho = lado;
        this.alto = lado;
        this.largo = lado;
        build();
    }

    public QCaja(float alto, float ancho, float largo) {
        material = new QMaterialBas("Caja");
        nombre = "Caja";
        this.ancho = ancho;
        this.alto = alto;
        this.largo = largo;
        build();
    }

    @Override
    public void build() {
        try {
//            if (anchoAnt == ancho && altoAnt == alto && largoAnt == largoAnt) {
//                return;
//            }
            deleteData();

            //Cubo con mapa UV en forma de skybox (14 vertices)
//                  ----------
//                  |         |
//                  |   UP    |
//                  |         |
//        ----------------------------------------
//        |         |         |         |        |
//        | BACK    | LEFT    | FRONT   | RIGHT  |
//        |         |         |         |        |
//        ----------------------------------------
//                  |         |
//                  | DOWN    |
//                  |         |
//                  ---------- 
//
//
///                 0---------1        
//                  |         |
//                  |   UP    |
//                  |         |
//        2---------3---------4---------5--------6
//        |         |         |         |        |
//        | BACK    | LEFT    | FRONT   | RIGHT  |
//        |         |         |         |        |
//        7---------8---------9---------10-------11
//                  |         |
//                  | DOWN    |
//                  |         |
//                  12-------13
//
//primer paso generar vertices
//primera linea
            this.addVertex(ancho / 2, alto / 2, -largo / 2, 0.25f, 1.0f);
            this.addVertex(ancho / 2, alto / 2, largo / 2, 0.5f, 1.0f);
//segunda linea
            this.addVertex(ancho / 2, alto / 2, -largo / 2, 0.f, 0.666f);
            this.addVertex(-ancho / 2, alto / 2, -largo / 2, 0.25f, 0.666f);
            this.addVertex(-ancho / 2, alto / 2, largo / 2, 0.50f, 0.666f);
            this.addVertex(ancho / 2, alto / 2, largo / 2, 0.75f, 0.666f);
            this.addVertex(ancho / 2, alto / 2, -largo / 2, 1f, 0.666f);
//tercera linea
            this.addVertex(ancho / 2, -alto / 2, -largo / 2, 0.f, 0.333f);
            this.addVertex(-ancho / 2, -alto / 2, -largo / 2, 0.25f, 0.333f);
            this.addVertex(-ancho / 2, -alto / 2, largo / 2, 0.50f, 0.333f);
            this.addVertex(ancho / 2, -alto / 2, largo / 2, 0.75f, 0.333f);
            this.addVertex(ancho / 2, -alto / 2, -largo / 2, 1f, 0.333f);
//cuarta linea
            this.addVertex(ancho / 2, -alto / 2, -largo / 2, 0.25f, 0);
            this.addVertex(ancho / 2, -alto / 2, largo / 2, 0.5f, 0);

//segundo paso generar caras
//cara superior
            this.addPoly(material, 1, 0, 3);
            this.addPoly(material, 3, 4, 1);
//trasera
            this.addPoly(material, 3, 2, 7);
            this.addPoly(material, 7, 8, 3);
//izquierda
            this.addPoly(material, 4, 3, 8);
            this.addPoly(material, 8, 9, 4);
//frente
            this.addPoly(material, 5, 4, 9);
            this.addPoly(material, 9, 10, 5);
//derecha
            this.addPoly(material, 6, 5, 10);
            this.addPoly(material, 10, 11, 6);
//abajo
            this.addPoly(material, 9, 8, 12);
            this.addPoly(material, 12, 13, 9);
//            anchoAnt = ancho;
//            largoAnt = largo;
//            altoAnt = alto;
            QUtilNormales.calcularNormales(this);
        } catch (Exception ex) {
            Logger.getLogger(QCaja.class.getName()).log(Level.SEVERE, null, ex);
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
