/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.generator.MeshGenerator;
import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QVector3;

/**
 * http://www.songho.ca/opengl/gl_torus.html
 * 
 * @author alberto
 */
@Getter
@Setter
public class Torus extends Shape {

    private float radio1;
    private float radio2;
    private int horizontalSectors = 36;
    private int verticalSectors = 18;

    public Torus() {
        nombre = "Toro";
        radio1 = 1;
        radio2 = 0.5f;
        horizontalSectors = 36;
        verticalSectors = 18;
        material = new QMaterialBas("Toro");
        build();
    }

    public Torus(float radio1, float radio2) {
        nombre = "Toro";
        this.radio1 = radio1;
        this.radio2 = radio2;
        horizontalSectors = 36;
        verticalSectors = 18;
        material = new QMaterialBas("Toro");
        build();
    }

    public Torus(float radio1, float radio2, int secciones) {
        nombre = "Toro";
        this.radio1 = radio1;
        this.radio2 = radio2;
        this.horizontalSectors = secciones;
        verticalSectors = secciones;
        material = new QMaterialBas("Toro");
        build();
    }

    public Torus(float radio1, float radio2, int secciones, int secciones2) {
        // super(QMalla.EJE_Y, false, 2 * QMath.PI, 2 * QMath.PI,secciones, secciones2);
        nombre = "Toro";
        this.radio1 = radio1;
        this.radio2 = radio2;
        this.horizontalSectors = secciones;
        this.verticalSectors = secciones2;
        material = new QMaterialBas("Toro");
        build();
    }

    public void build() {
        deleteData();

        // primero el circulo
        Vertex inicial = this.addVertex(0, radio2, 0); // primer vertice
        addUV(0, 1);

        QVector3 vector = QVector3.of(inicial.location.x, inicial.location.y, inicial.location.z);
        float angulo = 360.0f / verticalSectors;
        QVector3 tmp = vector;
        // se crean las secciones menos la ultima
        for (int i = 1; i < verticalSectors; i++) {
            tmp = tmp.rotateZ((float) Math.toRadians(angulo));
            this.addVertex(tmp.x, tmp.y, tmp.z);
            addUV(0, 1.0f - (1.0f / verticalSectors * (i - 1)));
        }

        // para la ultima seccion se debe unir con los primeros puntos
        // this.addVertex(inicial.x, inicial.y, inicial.z);
        // luego movemos los puntos al radio 1
        for (Vertex vertice : this.vertexList) {
            vertice.location.x -= radio1;
        }

        MeshGenerator.generateRevolutionMesh(this, horizontalSectors, true, true, false, false);
        computeNormals();
        smooth();
        applyMaterial(material);

    }
    // @Override
    // public void construir() {
    // eliminarDatos();
    //
    //// QMaterialBas material = new QMaterialBas("Toro_material ");
    //// int numVertices = (secciones + 1) * (secciones + 1);
    //// int numIndices = 2 * secciones * secciones*3;
    //// float[] vertices = new float[numVertices * 3];
    //// float[] normals = new float[numVertices * 3];
    //// int[] indices = new int[numIndices];
    // for (int j = 0; j <= secciones; ++j) {
    // float largeRadiusAngle = (float) (2.0f * Math.PI * j / secciones);
    //
    // for (int i = 0; i <= secciones; ++i) {
    // float smallRadiusAngle = (float) (2.0f * Math.PI * i / secciones);
    // float xNorm = (radio2 * (float) Math.sin(smallRadiusAngle)) * (float)
    // Math.sin(largeRadiusAngle);
    // float x = (radio1 + radio2 * (float) Math.sin(smallRadiusAngle)) * (float)
    // Math.sin(largeRadiusAngle);
    // float yNorm = (radio2 * (float) Math.sin(smallRadiusAngle)) * (float)
    // Math.cos(largeRadiusAngle);
    // float y = (radio1 + radio2 * (float) Math.sin(smallRadiusAngle)) * (float)
    // Math.cos(largeRadiusAngle);
    // float zNorm = radio2 * (float) Math.cos(smallRadiusAngle);
    // float z = zNorm;
    //// normals[vertIndex] = xNorm * normLen;
    //// vertices[vertIndex++] = x;
    //// normals[vertIndex] = yNorm * normLen;
    //// vertices[vertIndex++] = y;
    //// normals[vertIndex] = zNorm * normLen;
    //// vertices[vertIndex++] = z;
    // this.addVertex(x, y, z);
    //
    // if (i > 0 && j > 0) {
    // int a = (secciones + 1) * j + i;
    // int b = (secciones + 1) * j + i - 1;
    // int c = (secciones + 1) * (j - 1) + i - 1;
    // int d = (secciones + 1) * (j - 1) + i;
    //
    //// indices[index++] = a;
    //// indices[index++] = c;
    //// indices[index++] = b;
    // this.addPoly(a, c, b);
    //// indices[index++] = a;
    //// indices[index++] = d;
    //// indices[index++] = c;
    // this.addPoly(a, d, c);
    // }
    // }
    // }
    //
    // QUtilNormales.calcularNormales(this);
    // //el objeto es suavizado
    //
    // QMaterialUtil.suavizar(this, true);
    // QMaterialUtil.aplicarMaterial(this, material);
    //
    // }
    //
    // @Override
    // public void construir() {
    // eliminarDatos();
    //
    // super.setAncho(secciones);
    // super.setLargo(secciones2);
    // super.construir();
    //
    // //transforms the grid domain into a tube
    // for (QVertice vertice : vertices) {
    // calculateTube(vertice, radio2);
    // }
    //// for (int i = 0; i < transformables.size(); i++) {
    //// Triangle tri = (Triangle) transformables.get(i);
    //// calculateTube(tri.v1, radio2);
    //// calculateTube(tri.v2, radio2);
    //// calculateTube(tri.v3, radio2);
    //// }
    //
    // //translates the tube domain by r1
    //// this.translate(new Vertex(radio1, 0, 0));
    // for (QVertice vertice : vertices) {
    // vertice.ubicacion.x += radio1;
    // }
    //
    // //transforms the tube into a torus
    //// for (int j = 0; j < transformables.size(); j++) {
    //// Triangle tri = (Triangle) transformables.get(j);
    //// calculateTorus(tri.v1);
    //// calculateTorus(tri.v2);
    //// calculateTorus(tri.v3);
    //// }
    // for (QVertice vertice : vertices) {
    // calculateTorus(vertice);
    // }
    //
    // //translates the tube domain by r1
    //// this.translate(new Vertex(radio1, 0, 0));
    // for (QVertice vertice : vertices) {
    // vertice.ubicacion.x -= radio1;
    // }
    //
    // QUtilNormales.calcularNormales(this);
    // //el objeto es suavizado
    //
    // QMaterialUtil.suavizar(this, true);
    // QMaterialUtil.aplicarMaterial(this, material);
    //
    // }

    // /**
    // * Calculates the firts phase of torus creation, that of a tube It
    // * transforms only the x and z coordinates with the circle equation creating
    // * a tube in the y direction
    // *
    // * @param v vertex to be transformed
    // * @param r2 secondary radius
    // */
    // private void calculateTube(QVertice v, float r2) {
    // v.ubicacion.z = r2 * QMath.sin(v.ubicacion.x);
    // v.ubicacion.x = r2 * QMath.cos(v.ubicacion.x);
    // }
    //
    // /**
    // * transforms the tube vertexes into a torus by applying this time the
    // * circle equation only to the x and y coordinates
    // *
    // * @param v vertex to be transformed
    // */
    // private void calculateTorus(QVertice v) {
    // float x;
    // x = v.ubicacion.x;
    // v.ubicacion.x = x * QMath.cos(v.ubicacion.y);
    // v.ubicacion.y = x * QMath.sin(v.ubicacion.y);
    // }

}
