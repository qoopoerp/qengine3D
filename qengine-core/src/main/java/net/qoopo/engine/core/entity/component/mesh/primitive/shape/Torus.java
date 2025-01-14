/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.util.array.IntArray;

/**
 * http://www.songho.ca/opengl/gl_torus.html
 * 
 * @author alberto
 */
@Getter
@Setter
public class Torus extends Shape {

    private float majorRadius;
    private float minorRadius;
    private int sectorCount = 48;
    private int sideCount = 12;

    public Torus() {
        name = "Toro";
        majorRadius = 1;
        minorRadius = 0.5f;

        material = new Material("Toro");
        build();
    }

    public Torus(float majorRadius, float minorRadius) {
        name = "Toro";
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        material = new Material("Toro");
        build();
    }

    public Torus(float majorRadius, float minorRadius, int sectorCount) {
        name = "Toro";
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        this.sideCount = sectorCount;
        this.sectorCount = sectorCount;
        material = new Material("Toro");
        build();
    }

    public Torus(float majorRadius, float minorRadius, int sectorCount, int sideCount) {
        // super(QMalla.EJE_Y, false, 2 * QMath.PI, 2 * QMath.PI,sideCount,
        // sectorCount);
        name = "Toro";
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        this.sideCount = sideCount;
        this.sectorCount = sectorCount;
        material = new Material("Toro");
        build();
    }

    @Override
    public void build() {
        deleteData();
        try {

            float x, y, z, xy; // vertex position
            float nx, ny, nz; // normal
            float lengthInv = 1.0f / minorRadius; // to normalize normals
            float s, t;

            float sectorStep = 2.0f * (float) Math.PI / sectorCount;
            float sideStep = 2.0f * (float) Math.PI / sideCount;
            float sectorAngle, sideAngle;

            for (int i = 0; i <= sideCount; ++i) {
                // start the tube side from the inside where sideAngle = pi
                sideAngle = (float) Math.PI - i * sideStep; // starting from pi to -pi
                xy = minorRadius * (float) Math.cos(sideAngle); // r * cos(u)
                z = minorRadius * (float) Math.sin(sideAngle); // r * sin(u)

                // add (sectorCount+1) vertices per side
                // the first and last vertices have same position and normal,
                // but different tex coords
                for (int j = 0; j <= sectorCount; ++j) {
                    sectorAngle = j * sectorStep; // starting from 0 to 2pi

                    // tmp x and y to compute normal vector
                    x = xy * (float) Math.cos(sectorAngle);
                    y = xy * (float) Math.sin(sectorAngle);

                    // add normalized vertex normal first
                    nx = x * lengthInv;
                    ny = y * lengthInv;
                    nz = z * lengthInv;
                    addNormal(nx, ny, nz);

                    // shift x & y, and vertex position
                    x += majorRadius * (float) Math.cos(sectorAngle); // (R + r * cos(u)) * cos(v)
                    y += majorRadius * (float) Math.sin(sectorAngle); // (R + r * cos(u)) * sin(v)
                    addVertex(x, y, z);

                    // vertex tex coord between [0, 1]
                    s = (float) j / sectorCount;
                    t = (float) i / sideCount;
                    addUV(s, t);
                }
            }

            // faces

            // indices
            // k1--k1+1
            // | / |
            // | / |
            // k2--k2+1
            int k1, k2;
            for (int i = 0; i < sideCount; ++i) {
                k1 = i * (sectorCount + 1); // beginning of current side
                k2 = k1 + sectorCount + 1; // beginning of next side

                for (int j = 0; j < sectorCount; ++j, ++k1, ++k2) {
                    // 2 triangles per sector
                    addPoly(IntArray.of(k1, k2, k1 + 1)); // k1---k2---k1+1
                    addPoly(IntArray.of(k1 + 1, k2, k2 + 1)); // k1+1---k2---k2+1
                }
            }

            computeNormals();
            smooth();
            applyMaterial(material);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
