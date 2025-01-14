/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.modifier.generate.RevolutionModifier;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QVector3;

/**
 * Genera un toroide con el modificador de revolucion
 * 
 * @author alberto
 */
@Getter
@Setter
public class Torus2 extends Shape {

    private float majorRadius;
    private float minorRadius;
    private int sectorCount = 48;
    private int sideCount = 12;

    public Torus2() {
        name = "Toro";
        majorRadius = 1;
        minorRadius = 0.5f;
        material = new Material("Toro");
        build();
    }

    public Torus2(float majorRadius, float minorRadius) {
        name = "Toro";
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        material = new Material("Toro");
        build();
    }

    public Torus2(float majorRadius, float minorRadius, int sectorCount) {
        name = "Toro";
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        this.sectorCount = sectorCount;
        sideCount = sectorCount;
        material = new Material("Toro");
        build();
    }

    public Torus2(float majorRadius, float minorRadius, int sectorCount, int sideCount) {
        // super(QMalla.EJE_Y, false, 2 * QMath.PI, 2 * QMath.PI,sectorCount,
        // sideCount);
        name = "Toro";
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        this.sectorCount = sectorCount;
        this.sideCount = sideCount;
        material = new Material("Toro");
        build();
    }

    public void build() {
        deleteData();

        // primero el circulo
        Vertex inicial = this.addVertex(0, minorRadius, 0); // primer vertice
        addUV(0, 1);

        QVector3 vector = QVector3.of(inicial.location.x, inicial.location.y, inicial.location.z);
        float angulo = 360.0f / sideCount;
        QVector3 tmp = vector;
        // se crean las sectorCount menos la ultima
        for (int i = 1; i < sideCount; i++) {
            tmp = tmp.rotateZ((float) Math.toRadians(angulo));
            addVertex(tmp.x, tmp.y, tmp.z);
            addUV(0, 1.0f - (1.0f / sideCount * (i - 1)));
        }

        // luego movemos los puntos al radio 1
        for (Vertex vertice : this.vertexList) {
            vertice.location.x -= majorRadius;
        }

        // MeshGenerator.generateRevolutionMesh(this, horizontalSectors, true, true,
        // false, false);
        new RevolutionModifier(sectorCount, false, false, true, true).apply(this);
        computeNormals();
        smooth();
        applyMaterial(material);

    }
}
