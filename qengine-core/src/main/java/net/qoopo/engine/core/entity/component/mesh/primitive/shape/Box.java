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
import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.util.array.IntArray;

/**
 *
 * @author alberto
 */
@Getter
@Setter
public class Box extends Shape {

    private float ancho;
    private float alto;
    private float largo;

    private boolean mapUVtoFace = false;
    private boolean triangles = true;

    public Box() {
        material = new Material("Caja");
        name = "Caja";
        ancho = 1;
        alto = 1;
        largo = 1;
        build();
    }

    public Box(float lado) {
        material = new Material("Caja");
        name = "Caja";
        this.ancho = lado;
        this.alto = lado;
        this.largo = lado;
        build();
    }

    public Box(float lado, boolean mapUVtoFace) {
        material = new Material("Caja");
        name = "Caja";
        this.ancho = lado;
        this.alto = lado;
        this.largo = lado;
        this.mapUVtoFace = mapUVtoFace;
        build();
    }

    public Box(float alto, float ancho, float largo) {
        material = new Material("Caja");
        name = "Caja";
        this.ancho = ancho;
        this.alto = alto;
        this.largo = largo;
        build();
    }

    public Box(float alto, float ancho, float largo, boolean mapUVtoFace) {
        material = new Material("Caja");
        name = "Caja";
        this.ancho = ancho;
        this.alto = alto;
        this.largo = largo;
        this.mapUVtoFace = mapUVtoFace;
        build();
    }

    public Box(float alto, float ancho, float largo, boolean mapUVtoFace, boolean triangles) {
        material = new Material("Caja");
        name = "Caja";
        this.ancho = ancho;
        this.alto = alto;
        this.largo = largo;
        this.mapUVtoFace = mapUVtoFace;
        this.triangles = triangles;
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
            this.addVertex(-ancho / 2, alto / 2, largo / 2); // 0
            this.addVertex(ancho / 2, alto / 2, largo / 2); // 1
            this.addVertex(ancho / 2, -alto / 2, largo / 2); // 2
            this.addVertex(-ancho / 2, -alto / 2, largo / 2); // 3
            // cara trasera
            this.addVertex(-ancho / 2, alto / 2, -largo / 2); // 4
            this.addVertex(ancho / 2, alto / 2, -largo / 2); // 5
            this.addVertex(ancho / 2, -alto / 2, -largo / 2); // 6
            this.addVertex(-ancho / 2, -alto / 2, -largo / 2); // 7

            this.addUV(0, 1);
            this.addUV(1, 1);
            this.addUV(0, 0);
            this.addUV(1, 0);

            this.addNormal(0, 1, 0); // up
            this.addNormal(0, -1, 0); // down
            this.addNormal(-1, 0, 0);// left
            this.addNormal(1, 0, 0); // right
            this.addNormal(0, 0, 1); // front
            this.addNormal(0, 0, -1); // back

            // caras
            if (isTriangles()) {
                // cara frontal
                this.addPoly(IntArray.of(3, 1, 0), IntArray.of(4, 4, 4), IntArray.of(2, 1, 0));
                this.addPoly(IntArray.of(3, 2, 1), IntArray.of(4, 4, 4), IntArray.of(2, 3, 1));

                // cara trasera
                this.addPoly(IntArray.of(5, 7, 4), IntArray.of(5, 5, 5), IntArray.of(2, 1, 0));
                this.addPoly(IntArray.of(5, 6, 7), IntArray.of(5, 5, 5), IntArray.of(2, 3, 1));

                // cara lateral right
                this.addPoly(IntArray.of(2, 5, 1), IntArray.of(3, 3, 3), IntArray.of(2, 1, 0));
                this.addPoly(IntArray.of(2, 6, 5), IntArray.of(3, 3, 3), IntArray.of(2, 3, 1));

                // cara lateral left
                this.addPoly(IntArray.of(4, 3, 0), IntArray.of(2, 2, 2), IntArray.of(2, 1, 0));
                this.addPoly(IntArray.of(4, 7, 3), IntArray.of(2, 2, 2), IntArray.of(2, 3, 1));

                // cara superior
                this.addPoly(IntArray.of(0, 5, 4), IntArray.of(0, 0, 0), IntArray.of(2, 1, 0));
                this.addPoly(IntArray.of(0, 1, 5), IntArray.of(0, 0, 0), IntArray.of(2, 3, 1));

                // cara inferior
                this.addPoly(IntArray.of(6, 2, 3), IntArray.of(1, 1, 1), IntArray.of(2, 1, 0));
                this.addPoly(IntArray.of(6, 3, 7), IntArray.of(1, 1, 1), IntArray.of(2, 3, 1));
            } else {
                // cara frontal
                this.addPoly(IntArray.of(0, 3, 2, 1), IntArray.of(4, 4, 4, 4), IntArray.of(0, 2, 3, 1));
                // cara trasera
                this.addPoly(IntArray.of(4, 5, 6, 7), IntArray.of(5, 5, 5, 5), IntArray.of(0, 2, 3, 1));

                // cara lateral right
                this.addPoly(IntArray.of(1, 2, 6, 5), IntArray.of(3, 3, 3, 3), IntArray.of(0, 2, 3, 1));

                // cara lateral left
                this.addPoly(IntArray.of(3, 4, 7, 3), IntArray.of(2, 2, 2, 2), IntArray.of(0, 2, 3, 1));

                // cara superior
                this.addPoly(IntArray.of(4, 0, 1, 5), IntArray.of(0, 0, 0, 0), IntArray.of(0, 2, 3, 1));

                // cara inferior
                this.addPoly(IntArray.of(3, 6, 3, 7), IntArray.of(1, 1, 1, 1), IntArray.of(0, 2, 3, 1));
            }
            applyMaterial(material);
            // computeNormals();
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
            // // primera linea
            // this.addVertex(ancho / 2, alto / 2, -largo / 2, 0.25f, 1.0f);
            // this.addVertex(ancho / 2, alto / 2, largo / 2, 0.5f, 1.0f);
            // // segunda linea
            // this.addVertex(ancho / 2, alto / 2, -largo / 2, 0.f, 0.666f);
            // this.addVertex(-ancho / 2, alto / 2, -largo / 2, 0.25f, 0.666f);
            // this.addVertex(-ancho / 2, alto / 2, largo / 2, 0.50f, 0.666f);
            // this.addVertex(ancho / 2, alto / 2, largo / 2, 0.75f, 0.666f);
            // this.addVertex(ancho / 2, alto / 2, -largo / 2, 1f, 0.666f);
            // // tercera linea
            // this.addVertex(ancho / 2, -alto / 2, -largo / 2, 0.f, 0.333f);
            // this.addVertex(-ancho / 2, -alto / 2, -largo / 2, 0.25f, 0.333f);
            // this.addVertex(-ancho / 2, -alto / 2, largo / 2, 0.50f, 0.333f);
            // this.addVertex(ancho / 2, -alto / 2, largo / 2, 0.75f, 0.333f);
            // this.addVertex(ancho / 2, -alto / 2, -largo / 2, 1f, 0.333f);
            // // cuarta linea
            // this.addVertex(ancho / 2, -alto / 2, -largo / 2, 0.25f, 0);
            // this.addVertex(ancho / 2, -alto / 2, largo / 2, 0.5f, 0);

            // // segundo paso generar caras
            // // cara superior
            // this.addPoly(material, 1, 0, 3);
            // this.addPoly(material, 3, 4, 1);
            // // trasera
            // this.addPoly(material, 3, 2, 7);
            // this.addPoly(material, 7, 8, 3);
            // // izquierda
            // this.addPoly(material, 4, 3, 8);
            // this.addPoly(material, 8, 9, 4);
            // // frente
            // this.addPoly(material, 5, 4, 9);
            // this.addPoly(material, 9, 10, 5);
            // // derecha
            // this.addPoly(material, 6, 5, 10);
            // this.addPoly(material, 10, 11, 6);
            // // abajo
            // this.addPoly(material, 9, 8, 12);
            // this.addPoly(material, 12, 13, 9);

            // vertices
            // cara frontal
            this.addVertex(-ancho / 2, alto / 2, largo / 2); // 0
            this.addVertex(ancho / 2, alto / 2, largo / 2); // 1
            this.addVertex(ancho / 2, -alto / 2, largo / 2); // 2
            this.addVertex(-ancho / 2, -alto / 2, largo / 2); // 3
            // cara trasera
            this.addVertex(-ancho / 2, alto / 2, -largo / 2); // 4
            this.addVertex(ancho / 2, alto / 2, -largo / 2); // 5
            this.addVertex(ancho / 2, -alto / 2, -largo / 2); // 6
            this.addVertex(-ancho / 2, -alto / 2, -largo / 2); // 7

            this.addUV(0, 1);
            this.addUV(1, 1);
            this.addUV(0, 0);
            this.addUV(1, 0);

            this.addNormal(0, 1, 0); // up
            this.addNormal(0, -1, 0); // down
            this.addNormal(-1, 0, 0);// left
            this.addNormal(1, 0, 0); // right
            this.addNormal(0, 0, 1); // front
            this.addNormal(0, 0, -1); // back

            // caras
            if (isTriangles()) {
                // cara frontal
                this.addPoly(IntArray.of(3, 1, 0), IntArray.of(4, 4, 4), IntArray.of(2, 1, 0));
                this.addPoly(IntArray.of(3, 2, 1), IntArray.of(4, 4, 4), IntArray.of(2, 3, 1));

                // cara trasera
                this.addPoly(IntArray.of(5, 7, 4), IntArray.of(5, 5, 5), IntArray.of(2, 1, 0));
                this.addPoly(IntArray.of(5, 6, 7), IntArray.of(5, 5, 5), IntArray.of(2, 3, 1));

                // cara lateral right
                this.addPoly(IntArray.of(2, 5, 1), IntArray.of(3, 3, 3), IntArray.of(2, 1, 0));
                this.addPoly(IntArray.of(2, 6, 5), IntArray.of(3, 3, 3), IntArray.of(2, 3, 1));

                // cara lateral left
                this.addPoly(IntArray.of(4, 3, 0), IntArray.of(2, 2, 2), IntArray.of(2, 1, 0));
                this.addPoly(IntArray.of(4, 7, 3), IntArray.of(2, 2, 2), IntArray.of(2, 3, 1));

                // cara superior
                this.addPoly(IntArray.of(0, 5, 4), IntArray.of(0, 0, 0), IntArray.of(2, 1, 0));
                this.addPoly(IntArray.of(0, 1, 5), IntArray.of(0, 0, 0), IntArray.of(2, 3, 1));

                // cara inferior
                this.addPoly(IntArray.of(6, 2, 3), IntArray.of(1, 1, 1), IntArray.of(2, 1, 0));
                this.addPoly(IntArray.of(6, 3, 7), IntArray.of(1, 1, 1), IntArray.of(2, 3, 1));
            } else {
                // cara frontal
                this.addPoly(IntArray.of(0, 3, 2, 1), IntArray.of(4, 4, 4, 4), IntArray.of(0, 2, 3, 1));
                // cara trasera
                this.addPoly(IntArray.of(4, 5, 6, 7), IntArray.of(5, 5, 5, 5), IntArray.of(0, 2, 3, 1));

                // cara lateral right
                this.addPoly(IntArray.of(1, 2, 6, 5), IntArray.of(3, 3, 3, 3), IntArray.of(0, 2, 3, 1));

                // cara lateral left
                this.addPoly(IntArray.of(3, 4, 7, 3), IntArray.of(2, 2, 2, 2), IntArray.of(0, 2, 3, 1));

                // cara superior
                this.addPoly(IntArray.of(4, 0, 1, 5), IntArray.of(0, 0, 0, 0), IntArray.of(0, 2, 3, 1));

                // cara inferior
                this.addPoly(IntArray.of(3, 6, 3, 7), IntArray.of(1, 1, 1, 1), IntArray.of(0, 2, 3, 1));
            }
            // computeNormals();
            applyMaterial(material);
        } catch (Exception ex) {
            Logger.getLogger(Box.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
