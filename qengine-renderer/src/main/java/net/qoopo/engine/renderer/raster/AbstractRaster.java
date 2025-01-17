/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.raster;

import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.entity.component.mesh.primitive.VertexBuffer;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.Matrix4;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.scene.Camera;

/**
 * Realiza la rasterización de los polígonos
 *
 * @author alberto
 */
public interface AbstractRaster {

    // protected ArrayList<Vertex> verticesClipped = new ArrayList<>();

    // public abstract AbstractRaster(QMotorRender render);
    /**
     *
     * @param primitiva
     * @param p1
     * @param p2
     */
    public void rasterLine(Matrix4 matViewModel, Primitive primitiva, Vertex... vertex);

    /**
     * Realiza la rasterización de un polígono
     *
     * @param bufferVertices
     * @param primitiva
     * @param wire
     */
    public void raster(Matrix4 matViewModel, VertexBuffer bufferVertices, Primitive primitiva, boolean wire);

    /**
     * Realiza el clipping de los vertices
     *
     * @param camara
     * @param poligono
     * @param vertextInput
     */
    public static ClippedData clipping(Camera camara, int[] vertexIndex, int[] normalIndex, int[] uvIndex,
            Vertex[] vertextInput,
            Vector3[] normalInput, Vector2[] uvInput) {
        ClippedData clippedData = new ClippedData();
        Vertex[] vertexTemp = new Vertex[2];
        Vector3[] normalTemp = new Vector3[2];
        Vector2[] uvTemp = new Vector2[2];

        float alfa;
        Vertex interpolatedVertex;
        Vector3 interpolatedNormal;
        Vector2 interporlatedUV;
        // recorre los vertices y verifica si estan dentro del campo de vision, si no es
        // asi, encuentra el vertice que corresponderia dentro del campo de vision
        for (int i = 0; i < vertexIndex.length; i++) {
            vertexTemp[0] = vertextInput[vertexIndex[i]];
            vertexTemp[1] = vertextInput[vertexIndex[(i + 1) % vertexIndex.length]];

            if (

            normalIndex.length > i
                    && normalInput.length > normalIndex[i]
                    && normalIndex[i] >= 0
                    && normalInput.length > (normalIndex[(i + 1) % normalIndex.length])) {
                normalTemp[0] = normalInput[normalIndex[i]];
                normalTemp[1] = normalInput[normalIndex[(i + 1) % normalIndex.length]];
            } else {
                normalTemp[0] = new Vector3();
                normalTemp[1] = new Vector3();
            }
            if (uvIndex.length > i
                    && uvInput.length > uvIndex[i]
                    && uvIndex[i] >= 0
                    && uvInput.length > (uvIndex[(i + 1) % uvIndex.length])) {
                uvTemp[0] = uvInput[uvIndex[i]];
                uvTemp[1] = uvInput[uvIndex[(i + 1) % uvIndex.length]];
            } else {
                uvTemp[0] = new Vector2();
                uvTemp[1] = new Vector2();
            }

            // si los 2 vertices a procesar estan en el campo de vision
            if (camara.isVisible(vertexTemp[0]) && camara.isVisible(vertexTemp[1])) {
                clippedData.addVertex(vertexTemp[0]);
                clippedData.addNormal(normalTemp[0]);
                clippedData.addUV(uvTemp[0]);
            } else {
                // cuando uno de los dos no esta en el campo de vision
                if (camara.isVisible(vertexTemp[0]) && !camara.isVisible(vertexTemp[1])) {
                    clippedData.addVertex(vertexTemp[0]);
                    clippedData.addNormal(normalTemp[0]);
                    clippedData.addUV(uvTemp[0]);
                } else if (!camara.isVisible(vertexTemp[0]) && camara.isVisible(vertexTemp[1])) {
                    if (-vertexTemp[1].location.z == camara.frustrumCerca && (i + 1) % vertexIndex.length != 0) {
                        clippedData.addVertex(vertexTemp[1]);
                        clippedData.addNormal(normalTemp[1]);
                        clippedData.addUV(uvTemp[1]);
                        continue;
                    }
                } else {
                    continue;
                }
                alfa = camara.getClipedVerticeAlfa(vertexTemp[0].location.getVector3(),
                        vertexTemp[1].location.getVector3());

                interpolatedVertex = new Vertex();
                interpolatedNormal = new Vector3();
                interporlatedUV = new Vector2();
                QMath.interpolateLinear(interpolatedVertex, alfa, vertexTemp[0], vertexTemp[1]);
                QMath.interpolateLinear(interpolatedNormal, alfa, normalTemp[0], normalTemp[1]);
                QMath.interpolateLinear(interporlatedUV, alfa, uvTemp[0], uvTemp[1]);
                clippedData.addVertex(interpolatedVertex);
                clippedData.addNormal(interpolatedNormal);
                clippedData.addUV(interporlatedUV);
            }
        }
        return clippedData;
    }

}
