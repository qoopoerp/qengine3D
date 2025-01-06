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
import net.qoopo.engine.core.math.QMath;

/**
 * Esfera
 *
 * @author alberto
 */
public class Sphere extends Shape {

    private float radio;
    private int secciones = 36;

    public Sphere() {
        nombre = "Esfera";
        material = new QMaterialBas("Esfera");
        radio = 1;
        build();
    }

    public Sphere(float radio) {
        nombre = "Esfera";
        material = new QMaterialBas("Esfera");
        this.radio = radio;
        build();
    }

    public Sphere(float radio, int secciones) {
        nombre = "Esfera";
        material = new QMaterialBas("Esfera");
        this.radio = radio;
        this.secciones = secciones;
        build();
    }

    /**
     * Construye una esfera http://www.songho.ca/opengl/gl_sphere.html
     */
    @Override
    public void build() {
        deleteData();
        int stacks = secciones / 2; // No. de lineas de latitud
        int sectors = secciones; // No. de lineas de longitud

        float x, y, z, radio_Cos; // vertex position
        float nx, ny, nz, lengthInv = 1.0f / radio; // vertex normal
        float s, t; // vertex texCoord

        float sectorStep = (float) (2.0f * Math.PI / sectors);
        float stackStep = (float) (Math.PI / stacks);
        float sectorAngle, stackAngle;

        // del ejemplo cambio las coordenadas X por Z, Y por X, Z por Y
        // int k = 1;
        // vertices on the main body
        for (int i = 0; i <= stacks; i++) {
            stackAngle = (float) (Math.PI / 2 - i * stackStep); // starting from pi/2 to -pi/2
            radio_Cos = radio * QMath.cos(stackAngle); // r * cos(u)
            y = radio * QMath.sin(stackAngle); // r * sin(u)

            // add (sectorCount+1) vertices per stack
            // the first and last vertices have same position and normal, but different tex
            // coords
            for (int j = 0; j <= sectors; j++) {
                sectorAngle = j * sectorStep; // starting from 0 to 2pi
                // vertex position (x, y, z)
                // x = radio_Cos * QMath.cos(sectorAngle); // r * cos(u) * cos(v)
                // z = radio_Cos * QMath.sin(sectorAngle); // r * cos(u) * sin(v)
                x = radio_Cos * QMath.sin(sectorAngle); // r * cos(u) * sin(v)
                z = radio_Cos * QMath.cos(sectorAngle); // r * cos(u) * cos(v)
                // normalized vertex normal (nx, ny, nz)
                nx = x * lengthInv;
                ny = y * lengthInv;
                nz = z * lengthInv;
                // vertex tex coord (s, t) range between [0, 1]
                s = (float) j / sectors;
                t = (float) i / stacks;
                addVertex(x, y, z);
                addUV(s, 1.0f - t);
                addNormal(nx, ny, nz);
            }
        }

        // triangulos
        // k1--k1+1
        // | / |
        // | / |
        // k2--k2+1
        int k1, k2;
        for (int i = 0; i < stacks; i++) {
            k1 = i * (sectors + 1); // beginning of current stack
            k2 = k1 + sectors + 1; // beginning of next stack

            for (int j = 0; j < sectors; j++, k1++, k2++) {
                // 2 triangles per sector excluding first and last stacks
                // k1 => k2 => k1+1
                if (i != 0) {
                    try {
                        addPoly(new int[] { k1, k2, k1 + 1 }, new int[] { k1, k2, k1 + 1 },
                                new int[] { k1, k2, k1 + 1 });
                    } catch (Exception ex) {
                        Logger.getLogger(Sphere.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                // k1+1 => k2 => k2+1
                // if (i != (stacks - 1)) {
                if (i != (stacks - 1)) {
                    try {
                        addPoly(new int[] { k1 + 1, k2, k2 + 1 }, new int[] { k1 + 1, k2, k2 + 1 },
                                new int[] { k1 + 1, k2, k2 + 1 });
                    } catch (Exception ex) {
                        Logger.getLogger(Sphere.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                // // vertical lines for all stacks
                // lineIndices.push_back(k1);
                // lineIndices.push_back(k2);
                // if (i != 0) // horizontal lines except 1st stack
                // {
                // lineIndices.push_back(k1);
                // lineIndices.push_back(k1 + 1);
                // }
            }
        }
        smooth();
        applyMaterial(material);
        // computeNormals();
    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    public int getSecciones() {
        return secciones;
    }

    public void setSecciones(int secciones) {
        this.secciones = secciones;
    }

}
