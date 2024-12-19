/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.raster;

import java.util.ArrayList;

import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.mesh.primitive.QVertex;
import net.qoopo.engine.core.entity.component.transform.QVertexBuffer;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector4;
import net.qoopo.engine.core.scene.Camera;

/**
 * Realiza la rasterización de los polígonos
 *
 * @author alberto
 */
public abstract class AbstractRaster {

    protected ArrayList<QVertex> verticesClipped = new ArrayList<>();

    // public abstract AbstractRaster(QMotorRender render);
    /**
     *
     * @param primitiva
     * @param p1
     * @param p2
     */
    public abstract void raster(QPrimitiva primitiva, QVector4 p1, QVector4 p2);

    /**
     * Realiza la rasterización de un polígono
     *
     * @param bufferVertices
     * @param primitiva
     * @param wire
     */
    public abstract void raster(QVertexBuffer bufferVertices, QPrimitiva primitiva, boolean wire);

    /**
     * Realiza el clipping de los vertices
     *
     * @param camara
     * @param poligono
     * @param vertices
     */
    protected void clipping(Camera camara, QPrimitiva poligono, QVertex[] vertices) {
        verticesClipped.clear();
        QVertex[] vt = new QVertex[3];
        float alfa;
        QVertex verticeTmp;
        // recorre los vertices y verifica si estan dentro del campo de vision, si no es
        // asi, encuentra el vertice que corresponderia dentro del campo de vision
        for (int i = 0; i < poligono.listaVertices.length; i++) {
            vt[0] = vertices[poligono.listaVertices[i]];
            vt[1] = vertices[poligono.listaVertices[(i + 1) % poligono.listaVertices.length]];

            // si los 2 vertices a procesar estan en el campo de vision
            if (camara.isVisible(vt[0]) && camara.isVisible(vt[1])) {
                verticesClipped.add(vt[0]);
            } else {
                // cuando uno de los dos no esta en el campo de vision
                if (camara.isVisible(vt[0]) && !camara.isVisible(vt[1])) {
                    verticesClipped.add(vt[0]);
                    // if (-vt[0].ubicacion.z == camara.frustrumCerca) {
                    // continue;
                    // }
                    // alfa = (-camara.frustrumCerca - vt[0].ubicacion.z) / (vt[1].ubicacion.z -
                    // vt[0].ubicacion.z);
                } else if (!camara.isVisible(vt[0]) && camara.isVisible(vt[1])) {
                    if (-vt[1].location.z == camara.frustrumCerca && (i + 1) % poligono.listaVertices.length != 0) {
                        verticesClipped.add(vt[1]);
                        continue;
                    }
                } else {
                    continue;
                }
                alfa = camara.obtenerClipedVerticeAlfa(vt[0].location.getVector3(), vt[1].location.getVector3());
                verticeTmp = new QVertex();
                QMath.linear(verticeTmp, alfa, vt[0], vt[1]);
                verticesClipped.add(verticeTmp);
            }
        }
    }

}
