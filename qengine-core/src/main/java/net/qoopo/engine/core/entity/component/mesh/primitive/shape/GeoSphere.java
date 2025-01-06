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
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.util.array.IntArray;

/**
 * Geoesfera o Icoesfera
 *
 * @author alberto
 */
public class GeoSphere extends Shape {

    private float radio;
    private int divisiones = 3;

    private static final float H_ANGLE = QMath.PI / 180 * 72; // 72 degree = 360 / 5
    private static final float V_ANGLE = QMath.atan(1.0f / 2); // elevation = 26.565 degree

    public GeoSphere() {
        nombre = "Esfera";
        material = new QMaterialBas("Esfera");
        radio = 1;
        build();
    }

    public GeoSphere(float radio) {
        nombre = "Esfera";
        material = new QMaterialBas("Esfera");
        this.radio = radio;
        build();
    }

    public GeoSphere(float radio, int divisiones) {
        nombre = "Esfera";
        material = new QMaterialBas("Esfera");
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
        crearIcosaedro();
        // paso 2
        // armarTriangulos();
        // paso 3. - realizar la division del icosaedro
        // dividirIcosaedro(divisiones);
        computeNormals();
        new SubdivisionModifier(SubdivisionModifier.TYPE_SIMPLE, divisiones).apply(this);
        new InflateModifier(radio).apply(this);
        // inflate(radio);
        computeNormals();
        smooth();
        applyMaterial(material);
    }

    /**
     * Crea un Icosaedro
     *
     * http://www.songho.ca/opengl/gl_sphere.html
     *
     */
    private void crearIcosaedro() {
        try {
            int i1, i2; // indices
            float z, radio_Cos; // coords
            float hAngle1 = -QMath.PI / 2 - H_ANGLE / 2; // start from -126 deg at 1st row
            float hAngle2 = -QMath.PI / 2; // start from -90 deg at 2nd row

            float U = 186.0f / 2048.0f;
            float V = 186.0f / 2048.0f;

            addVertex(0, 0, radio);
            addUV(U, 0);
            // inicializamos con 12 vertices (menos el primero ya agregado), luego
            // calculamos sus posiciones
            for (int i = 1; i <= 11; i++) {
                addVertex(0, 0, 0);
                addUV(0, 0);
            }
            z = radio * QMath.sin(V_ANGLE); // elevaton
            // calcula los 10 vertices, at 1st and 2nd rows
            for (int i = 1; i <= 5; ++i) {
                i1 = i; // index for 1st row
                i2 = (i + 5); // index for 2nd row
                radio_Cos = radio * QMath.cos(V_ANGLE); // length on xy plane
                vertexList[i1].location.set(radio_Cos * QMath.cos(hAngle1), radio_Cos * QMath.sin(hAngle1), z, 1);
                vertexList[i2].location.set(radio_Cos * QMath.cos(hAngle2), radio_Cos * QMath.sin(hAngle2), -z, 1);
                // next horizontal angles
                hAngle1 += H_ANGLE;
                hAngle2 += H_ANGLE;
            } // the last bottom vertex at (0, 0, -r)
              /// El ultimo vertice
            vertexList[11].location.set(0, 0, -radio, 1);
            uvList[11].setXY(10 * U, 3 * V);
            // las caras superiores
            addPoly(IntArray.of(0, 1, 2));
            addPoly(IntArray.of(0, 2, 3));
            addPoly(IntArray.of(0, 3, 4));
            addPoly(IntArray.of(0, 4, 5));
            addPoly(IntArray.of(0, 5, 1));
            // las caras del centro
            addPoly(IntArray.of(1, 6, 2));
            addPoly(IntArray.of(2, 6, 7));
            //
            addPoly(IntArray.of(2, 7, 3));
            addPoly(IntArray.of(3, 7, 8));
            //
            addPoly(IntArray.of(3, 8, 4));
            addPoly(IntArray.of(4, 8, 9));
            //
            addPoly(IntArray.of(4, 9, 5));
            addPoly(IntArray.of(5, 9, 10));
            //
            addPoly(IntArray.of(5, 10, 1));
            addPoly(IntArray.of(1, 10, 6));
            //
            // las caras inferiores
            addPoly(IntArray.of(11, 7, 6));
            addPoly(IntArray.of(11, 8, 7));
            addPoly(IntArray.of(11, 9, 8));
            addPoly(IntArray.of(11, 10, 9));
            addPoly(IntArray.of(11, 6, 10));
        } catch (Exception ex) {
            Logger.getLogger(GeoSphere.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // private void armarTriangulos() {
    //
    // // float S_STEP = 1 / 11.0f; // horizontal texture step
    // // float T_STEP = 1 / 3.0f; // vertical texture step
    // float S_STEP = 186 / 2048.0f; // horizontal texture step
    // float T_STEP = 322 / 1024.0f; // vertical texture step
    //
    // QVector3 vertice = QVector3.empty(); // vertex
    // QVector3 normal = QVector3.empty(); // normal
    // float scale; // scale factor for normalization
    //
    // // smooth icosahedron has 14 non-shared (0 to 13) and
    // // 8 shared vertices (14 to 21) (total 22 vertices)
    // // 00 01 02 03 04 //
    // // /\ /\ /\ /\ /\ //
    // // / \/ \/ \/ \/ \ //
    // //10--14--15--16--17--11 //
    // // \ /\ /\ /\ /\ /\ //
    // // \/ \/ \/ \/ \/ \ //
    // // 12--18--19--20--21--13 //
    // // \ /\ /\ /\ /\ / //
    // // \/ \/ \/ \/ \/ //
    // // 05 06 07 08 09 //
    // // add 14 non-shared vertices first (index from 0 to 13)
    //// addVertex(tmpVertices[0], tmpVertices[1], tmpVertices[2]); // v0 (top)
    //// addNormal(0, 0, 1);
    //// addTexCoord(S_STEP, 0);
    // addVertex(vertices[0].ubicacion, S_STEP, 0);
    //
    //// addVertex(tmpVertices[0], tmpVertices[1], tmpVertices[2]); // v1
    //// addNormal(0, 0, 1);
    //// addTexCoord(S_STEP * 3, 0);
    // addVertex(vertices[0].ubicacion, S_STEP * 3, 0);
    //
    //// addVertex(tmpVertices[0], tmpVertices[1], tmpVertices[2]); // v2
    //// addNormal(0, 0, 1);
    //// addTexCoord(S_STEP * 5, 0);
    // addVertex(vertices[0].ubicacion, S_STEP * 5, 0);
    //
    //// addVertex(tmpVertices[0], tmpVertices[1], tmpVertices[2]); // v3
    //// addNormal(0, 0, 1);
    //// addTexCoord(S_STEP * 7, 0);
    // addVertex(vertices[0].ubicacion, S_STEP * 7, 0);
    //
    //// addVertex(tmpVertices[0], tmpVertices[1], tmpVertices[2]); // v4
    //// addNormal(0, 0, 1);
    //// addTexCoord(S_STEP * 9, 0);
    // addVertex(vertices[0].ubicacion, S_STEP * 9, 0);
    //
    //// addVertex(tmpVertices[33], tmpVertices[34], tmpVertices[35]); // v5
    // (bottom)
    //// addNormal(0, 0, -1);
    //// addTexCoord(S_STEP * 2, T_STEP * 3);
    // addVertex(vertices[11].ubicacion, S_STEP * 2, T_STEP * 3);
    //
    //// addVertex(tmpVertices[33], tmpVertices[34], tmpVertices[35]); // v6
    //// addNormal(0, 0, -1);
    //// addTexCoord(S_STEP * 4, T_STEP * 3);
    // addVertex(vertices[11].ubicacion, S_STEP * 4, T_STEP * 3);
    //
    //// addVertex(tmpVertices[33], tmpVertices[34], tmpVertices[35]); // v7
    //// addNormal(0, 0, -1);
    //// addTexCoord(S_STEP * 6, T_STEP * 3);
    // addVertex(vertices[11].ubicacion, S_STEP * 6, T_STEP * 3);
    //
    //// addVertex(tmpVertices[33], tmpVertices[34], tmpVertices[35]); // v8
    //// addNormal(0, 0, -1);
    //// addTexCoord(S_STEP * 8, T_STEP * 3);
    // addVertex(vertices[11].ubicacion, S_STEP * 8, T_STEP * 3);
    //
    //// addVertex(tmpVertices[33], tmpVertices[34], tmpVertices[35]); // v9
    //// addNormal(0, 0, -1);
    //// addTexCoord(S_STEP * 10, T_STEP * 3);
    // addVertex(vertices[11].ubicacion, S_STEP * 10, T_STEP * 3);
    // }
    /**
     * Divide el icosaedro original
     *
     * @param divisiones
     */
    // private void dividirIcosaedro(int subdivision) {
    //// std::vector < float > tmpVertices;
    //// std::vector < float > tmpIndices;
    //// const float *v1, *v2, *v3; // ptr to original vertices of a triangle
    // QVertice nuevo;
    // QVector3 v1,v2,v3;
    // QVector3 newV1=QVector3.empty(), newV2=QVector3.empty(),
    // newV3=QVector3.empty(); // new vertex positions
    // int index;
    //
    // QPrimitiva[] tmpIndices;
    // QVertice[] tmpVertices;
    //
    //// iterate all subdivision levels
    // for (int i = 1; i <= subdivision; ++i) {
    // // copy prev vertex/index arrays and clear
    //// tmpVertices = vertices;
    //// tmpIndices = indices;
    //// vertices.clear();
    //// indices.clear();
    //
    // tmpIndices = this.primitivas;
    // index = 0;
    //
    // // perform subdivision for each triangle
    // for (int j = 0; j < tmpIndices.length; j++ /* j += 3*/) {
    // // get 3 vertices of a triangle
    // v1 = & tmpVertices[tmpIndices[j] * 3];
    // v2 = & tmpVertices[tmpIndices[j + 1] * 3];
    // v3 = & tmpVertices[tmpIndices[j + 2] * 3];
    //
    // // compute 3 new vertices by spliting half on each edge
    // // v1
    // // / \
    // // newV1 *---* newV3
    // // / \ / \
    // // v2---*---v3
    // // newV2
    // computeHalfVertex(v1, v2, newV1);
    // computeHalfVertex(v2, v3, newV2);
    // computeHalfVertex(v1, v3, newV3);
    //
    // // add 4 new triangles to vertex array
    // addVertices(v1, newV1, newV3);
    // addVertices(newV1, v2, newV2);
    // addVertices(newV1, newV2, newV3);
    // addVertices(newV3, newV2, v3);
    //
    // // add indices of 4 new triangles
    // addIndices(index, index + 1, index + 2);
    // addIndices(index + 3, index + 4, index + 5);
    // addIndices(index + 6, index + 7, index + 8);
    // addIndices(index + 9, index + 10, index + 11);
    // index += 12; // next index
    // }
    // }
    // }
    ///////////////////////////////////////////////////////////////////////////////
    // find middle point of 2 vertices
    // NOTE: new vertex must be resized, so the length is equal to the radius
    ///////////////////////////////////////////////////////////////////////////////
    private void computeHalfVertex(QVector3 v1, QVector3 v2, QVector3 newV) {
        // newV[0] = v1[0] + v2[0]; // x
        // newV[1] = v1[1] + v2[1]; // y
        // newV[2] = v1[2] + v2[2]; // z
        newV.x = v1.x + v2.x;
        newV.y = v1.y + v2.y;
        newV.z = v1.z + v2.z;

        float scale = radio / QMath.sqrt(newV.x * newV.x + newV.y * newV.y + newV.z * newV.z);
        newV.multiply(scale);
        // newV[0] *= scale;
        // newV[1] *= scale;
        // newV[2] *= scale;
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
