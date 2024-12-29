/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.raster;

import java.util.ArrayList;
import java.util.List;

import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.transform.QVertexBuffer;
import net.qoopo.engine.core.math.QMath;
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
    public void rasterLine(QPrimitiva primitiva, Vertex... vertex);

    /**
     * Realiza la rasterización de un polígono
     *
     * @param bufferVertices
     * @param primitiva
     * @param wire
     */
    public void raster(QVertexBuffer bufferVertices, QPrimitiva primitiva, boolean wire);

    /**
     * Realiza el clipping de los vertices
     *
     * @param camara
     * @param poligono
     * @param vertex
     */
    public static List<Vertex> clipping(Camera camara, int[] vertexIndex, Vertex[] vertex) {
        ArrayList<Vertex> clippedVertex = new ArrayList<>();
        // verticesClipped.clear();
        Vertex[] vt = new Vertex[3];
        float alfa;
        Vertex verticeTmp;
        // recorre los vertices y verifica si estan dentro del campo de vision, si no es
        // asi, encuentra el vertice que corresponderia dentro del campo de vision
        for (int i = 0; i < vertexIndex.length; i++) {
            vt[0] = vertex[vertexIndex[i]];
            vt[1] = vertex[vertexIndex[(i + 1) % vertexIndex.length]];

            // si los 2 vertices a procesar estan en el campo de vision
            if (camara.isVisible(vt[0]) && camara.isVisible(vt[1])) {
                clippedVertex.add(vt[0]);
            } else {
                // cuando uno de los dos no esta en el campo de vision
                if (camara.isVisible(vt[0]) && !camara.isVisible(vt[1])) {
                    clippedVertex.add(vt[0]);
                } else if (!camara.isVisible(vt[0]) && camara.isVisible(vt[1])) {
                    if (-vt[1].location.z == camara.frustrumCerca && (i + 1) % vertexIndex.length != 0) {
                        clippedVertex.add(vt[1]);
                        continue;
                    }
                } else {
                    continue;
                }
                alfa = camara.obtenerClipedVerticeAlfa(vt[0].location.getVector3(), vt[1].location.getVector3());
                verticeTmp = new Vertex();
                QMath.interpolateLinear(verticeTmp, alfa, vt[0], vt[1]);
                clippedVertex.add(verticeTmp);
            }
        }
        return clippedVertex;
    }

}
