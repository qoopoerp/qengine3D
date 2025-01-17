/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.raster;

import java.util.logging.Logger;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.entity.component.mesh.primitive.Line;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.entity.component.mesh.primitive.VertexBuffer;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.Matrix4;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.math.Vector4;
import net.qoopo.engine.core.renderer.RenderOptions;
import net.qoopo.engine.core.util.TempVars;
import net.qoopo.engine.core.util.array.Vector2Array;
import net.qoopo.engine.core.util.array.Vector3Array;
import net.qoopo.engine.core.util.array.VertexArray;
import net.qoopo.engine.renderer.SoftwareRenderer;
import net.qoopo.engine.renderer.util.TransformationVectorUtil;

/**
 * Realiza la rasterización de los polígonos version
 *
 * @author alberto
 */
public class ParallelRaster implements AbstractRaster {

    private static Logger logger = Logger.getLogger("raster");
    protected SoftwareRenderer render;

    protected final float INTERPOLATION_CLAMP = .0001f;

    public ParallelRaster(SoftwareRenderer render) {
        this.render = render;
    }

    /**
     *
     * @param primitiva
     * @param p1
     * @param p2
     */
    @Override
    public void rasterLine(Matrix4 matViewModel, Primitive primitiva, Vertex... vertex) {
        int[] indices = new int[vertex.length];
        Vector3[] normales = new Vector3[vertex.length];
        Vector2[] uvList = new Vector2[vertex.length];

        for (int i = 0; i < vertex.length; i++) {
            indices[i] = i;
            normales[i] = Vector3.unitario_y.clone();
            uvList[i] = new Vector2();
        }

        ClippedData clippedData = AbstractRaster.clipping(render.getCamera(), indices, indices, indices, vertex,
                normales, uvList);
        for (int i = 0; i < clippedData.getVertexList().size() - 1; i++) {
            Vector2[] screenPojected = projectVertices(clippedData.getVertexList().get(i),
                    clippedData.getVertexList().get(i + 1));
            drawLine(Vector3.empty(), Vector3.empty(), matViewModel, primitiva,
                    (int) screenPojected[0].x,
                    (int) screenPojected[0].y,
                    (int) screenPojected[1].x,
                    (int) screenPojected[1].y,
                    VertexArray.of(clippedData.getVertexList().get(i), clippedData.getVertexList().get(i + 1)),
                    Vector3Array.of(Vector3.unitario_y.clone(), Vector3.unitario_y.clone()),
                    Vector2Array.of(new Vector2(), new Vector2()));
        }
    }

    /**
     * Realiza la rasterización de un polígono
     *
     * @param vertexBuffer
     * @param primitiva
     * @param wire
     *
     */
    @Override
    public void raster(Matrix4 matViewModel, VertexBuffer vertexBuffer, Primitive primitiva, boolean wire) {
        if (primitiva instanceof Poly) {
            if (wire) {
                rasterWirePolygon(matViewModel, vertexBuffer, (Poly) primitiva);
            } else {
                rasterFullPolygon(matViewModel, vertexBuffer, (Poly) primitiva);
            }
        } else if (primitiva instanceof Line) {
            Vertex p1 = vertexBuffer.getVertexList()[primitiva.vertexIndexList[0]];
            Vertex p2 = vertexBuffer.getVertexList()[primitiva.vertexIndexList[1]];
            rasterLine(matViewModel, primitiva, p1, p2);
        }
    }

    /**
     * Realiza un proceso de un polígono donde solo dibuja las aristas
     *
     * @param vertexBuffer
     * @param poligono
     *
     */
    protected void rasterWirePolygon(Matrix4 matViewModel, VertexBuffer vertexBuffer, Poly poligono) {
        try {
            Vertex v1;
            Vertex v2;
            if (poligono.vertexIndexList.length >= 3) {
                ClippedData clippedData = AbstractRaster.clipping(render.getCamera(),
                        poligono.vertexIndexList,
                        poligono.normalIndexList,
                        poligono.uvIndexList,
                        vertexBuffer.getVertexList(), vertexBuffer.getNormalList(),
                        vertexBuffer.getUvList());
                for (int i = 0; i < clippedData.getVertexList().size() - 1; i++) {
                    v1 = clippedData.getVertexList().get(i);
                    v2 = clippedData.getVertexList().get(i + 1);
                    // si el triangulo no esta en el campo de vision, pasamos y no dibujamos
                    if (!render.getCamera().isVisible(v1) &&
                            !render.getCamera().isVisible(v2)) {
                        continue;
                    }
                    rasterLine(matViewModel, poligono, v1, v2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza el proceso de rasterización de un polígono sólido
     *
     * @param poly
     * @param siempreTop
     * @param dibujar    . SI es true, llama al método procesarPixel del shader
     */
    private void rasterFullPolygon(Matrix4 matViewModel, VertexBuffer vertexBuffer, Poly poly) {
        try {
            if (poly.vertexIndexList.length >= 3) {
                Vector3 toCenter = poly.getCenterCopy().location.getVector3();
                // validación caras traseras
                // si el objeto es tipo wire se dibuja igual sus caras traseras
                // si el objeto tiene transparencia (con material básico) igual dibuja sus caras
                // traseras
                if ((!(poly.material instanceof Material) || ((poly.material instanceof Material)
                        && !((Material) poly.material).isTransparent()))
                        && !render.opciones.isDibujarCarasTraseras() && toCenter.dot(poly.getNormalCopy()) > 0) {
                    render.poligonosDibujadosTemp--;
                    return; // salta el dibujo de caras traseras
                }

                ClippedData clippedData = AbstractRaster.clipping(render.getCamera(),
                        poly.vertexIndexList,
                        poly.normalIndexList,
                        poly.uvIndexList,
                        vertexBuffer.getVertexList(),
                        vertexBuffer.getNormalList(),
                        vertexBuffer.getUvList());

                // Rasterizacion (dibujo de los puntos del plano)
                // Separo en triangulos sin importar cuantos puntos tenga
                // int indexNormal = 0;
                for (int i = 1; i < clippedData.getVertexList().size() - 1; i++) {
                    Vertex v1 = clippedData.getVertexList().get(0);
                    Vertex v2 = clippedData.getVertexList().get(i);
                    Vertex v3 = clippedData.getVertexList().get(i + 1);

                    Vector3 v1Normal = clippedData.getNormalList().get(0);
                    Vector3 v2Normal = clippedData.getNormalList().get(i);
                    Vector3 v3Normal = clippedData.getNormalList().get(i + 1);

                    Vector2 v1uv = clippedData.getUvList().get(0);
                    Vector2 v2uv = clippedData.getUvList().get(i);
                    Vector2 v3uv = clippedData.getUvList().get(i + 1);

                    // si el triangulo no esta en el campo de vision, pasamos y no dibujamos
                    if (!render.getCamera().isVisible(v1) && !render.getCamera().isVisible(v2)
                            && !render.getCamera().isVisible(v3)) {
                        continue;
                    }
                    rasterTriangle(matViewModel, v1, v2, v3, v1Normal, v2Normal, v3Normal, v1uv, v2uv, v3uv, poly);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza la rasterización de un triángulo
     * 
     * @param matViewModel
     * @param v1
     * @param v2
     * @param v3
     * @param v1Normal
     * @param v2Normal
     * @param v3Normal
     * @param v1uv
     * @param v2uv
     * @param v3uv
     * @param poly
     */
    private void rasterTriangle(Matrix4 matViewModel, Vertex v1, Vertex v2, Vertex v3, Vector3 v1Normal,
            Vector3 v2Normal,
            Vector3 v3Normal,
            Vector2 v1uv,
            Vector2 v2uv,
            Vector2 v3uv,
            Poly poly) {

        // Calcula los vectores arriba y derecha (tangente y bitangente ) del tirnagulo
        // para su uso en el noralmapping
        // en el fragmentShader
        Vector3 up = Vector3.empty();
        Vector3 rigth = Vector3.empty();

        calculateUpRight(up, rigth, v1, v2, v3, v1uv, v2uv, v3uv);

        // obtenemos los puntos proyectados en la pantalla
        Vector2[] screenPojected = projectVertices(v1, v2, v3, v3); // se coloca el v3 en cuarto lugar para tener 4
                                                                     // posiciones en caso de agregar un nuevo vertice

        Vertex[] vertexList = { v1, v2, v3 };
        Vector3[] normalList = { v1Normal, v2Normal, v3Normal };
        Vector2[] uvList = { v1uv, v2uv, v3uv };

        // valido si el punto proyectado esta dentro del rango de vision de la Camara
        if ((screenPojected[0].x < 0 && screenPojected[1].x < 0 && screenPojected[2].x < 0)
                || (screenPojected[0].y < 0 && screenPojected[1].y < 0 && screenPojected[2].y < 0)
                || (screenPojected[0].x > render.getFrameBuffer().getWidth()
                        && screenPojected[1].x > render.getFrameBuffer().getWidth()
                        && screenPojected[2].x > render.getFrameBuffer().getWidth())
                || (screenPojected[0].y > render.getFrameBuffer().getHeight()
                        && screenPojected[1].y > render.getFrameBuffer().getHeight()
                        && screenPojected[2].y > render.getFrameBuffer().getHeight())) {
            render.poligonosDibujados--;
            return;
        }

        int temp;
        int order[] = new int[4];
        order[0] = 0;
        order[1] = 1;
        order[2] = 2;
        order[3] = 3;// este no se mueve, se usa en caso de dividir del triangulo, el nuevo punto

        // Sort the points by ejeY coordinate. (bubble sort...derps)
        if (screenPojected[order[0]].y > screenPojected[order[1]].y) {
            temp = order[0];
            order[0] = order[1];
            order[1] = temp;
        }
        if (screenPojected[order[1]].y > screenPojected[order[2]].y) {
            temp = order[1];
            order[1] = order[2];
            order[2] = temp;
        }
        if (screenPojected[order[0]].y > screenPojected[order[1]].y) {
            temp = order[0];
            order[0] = order[1];
            order[1] = temp;
        }

        if (screenPojected[order[1]].y == screenPojected[order[2]].y) {
            // this->scanBottomFlatTriangle(v1, v2, v3);
            scanBottomFlatTriangle(up, rigth, matViewModel, poly, order[0], order[1], order[2], screenPojected,
                    vertexList,
                    normalList, uvList);
        } else if (screenPojected[order[0]].y == screenPojected[order[1]].y) {
            // this->scanTopFlatTriangle(v1, v2, v3);
            scanTopFlatTriangle(up, rigth, matViewModel, poly, order[0], order[1], order[2], screenPojected, vertexList,
                    normalList,
                    uvList);
        } else {
            // En este caso vamos a dividir el triángulo en dos
            // para tener uno que cumpla 'bottomFlat' y el otro 'TopFlat' y necesitamos un
            // punto extra para separar ambos
            TempVars tvars = TempVars.get();

            try {

                // el nuevo vertice a crear
                Vertex v4 = new Vertex();// tvars.vertex1;
                Vector3 v4Normal = new Vector3(); // tvars.vector3f1;
                Vector2 v4UV = new Vector2();// tvars.vector2f1;

                Vector2 screenProjectedV4 = new Vector2();
                screenProjectedV4.setXY(
                        (screenPojected[order[0]].x + ((screenPojected[order[1]].y - screenPojected[order[0]].y)
                                / (screenPojected[order[2]].y - screenPojected[order[0]].y))
                                * (screenPojected[order[2]].x - screenPojected[order[0]].x)),
                        screenPojected[order[1]].y);

                Vector3 bar = tvars.vector3f9;
                // Hallamos las coordenadas baricéntricas del punto v4 respecto al triángulo pa,
                // pb, pc
                QMath.getBarycentricCoordinates(bar, screenProjectedV4.x, screenProjectedV4.y, screenPojected[order[0]],
                        screenPojected[order[1]], screenPojected[order[2]]);

                // correccion de proyectar
                // float zUV = 1.0f / (bar.x * 1.0f / vt[order[0]].ubicacion.z + bar.y * 1.0f /
                // vt[order[1]].ubicacion.z + bar.z * 1.0f / vt[order[2]].ubicacion.z);
                float zUV = 1.0f / (bar.x / vertexList[order[0]].location.z + bar.y / vertexList[order[1]].location.z
                        + bar.z / vertexList[order[2]].location.z);

                // calculamos el vertice nuevo
                v4.location.x = bar.x * vertexList[order[0]].location.x + bar.y * vertexList[order[1]].location.x
                        + bar.z * vertexList[order[2]].location.x;
                v4.location.y = bar.x * vertexList[order[0]].location.y + bar.y * vertexList[order[1]].location.y
                        + bar.z * vertexList[order[2]].location.y;
                // correccion de proyectar
                v4.location.z = zUV * (bar.x + bar.y + bar.z);

                // coordenadas de textura
                // v4.u = bar.x * vt[order[0]].u + bar.y * vt[order[1]].u + bar.z *
                // vt[order[2]].u;
                // v4.v = bar.x * vt[order[0]].v + bar.y * vt[order[1]].v + bar.z *
                // vt[order[2]].v;
                // correccion de proyectar

                v4UV.x = zUV * (bar.x * (uvList[order[0]].x / vertexList[order[0]].location.z)
                        + bar.y * (uvList[order[1]].x / vertexList[order[1]].location.z)
                        + bar.z * (uvList[order[2]].x / vertexList[order[2]].location.z));
                v4UV.y = zUV * (bar.x * (uvList[order[0]].y / vertexList[order[0]].location.z)
                        + bar.y * (uvList[order[1]].y / vertexList[order[1]].location.z)
                        + bar.z * (uvList[order[2]].y / vertexList[order[2]].location.z));

                // la normal
                v4Normal.x = bar.x * normalList[order[0]].x + bar.y * normalList[order[1]].x
                        + bar.z * normalList[order[2]].x;
                v4Normal.y = bar.x * normalList[order[0]].y + bar.y * normalList[order[1]].y
                        + bar.z * normalList[order[2]].y;
                v4Normal.z = bar.x * normalList[order[0]].z + bar.y * normalList[order[1]].z
                        + bar.z * normalList[order[2]].z;

                screenPojected[3] = screenProjectedV4;
                scanBottomFlatTriangle(up, rigth, matViewModel, poly, order[0], order[1], 3, screenPojected,
                        VertexArray.of(v1, v2, v3, v4), Vector3Array.of(v1Normal, v2Normal, v3Normal, v4Normal),
                        Vector2Array.of(v1uv, v2uv, v3uv, v4UV));
                // this->scanBottomFlatTriangle(v1, v2, v4);
                scanTopFlatTriangle(up, rigth, matViewModel, poly, order[1], 3, order[2], screenPojected,
                        VertexArray.of(v1, v2, v3, v4), Vector3Array.of(v1Normal, v2Normal, v3Normal, v4Normal),
                        Vector2Array.of(v1uv, v2uv, v3uv, v4UV));
                // this->scanTopFlatTriangle(v4, v2, v3);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                tvars.release();
            }
        }

    }

    /**
     * Dibuja el triangulo hacia arriba
     *
     * @param poligono
     * @param i1       Indice del punto 1
     * @param i2       Indice del punto 2
     * @param i3       Indice del punto 3
     */
    private void scanBottomFlatTriangle(Vector3 up, Vector3 rigth, Matrix4 matViewModel, Poly poligono, int i1,
            int i2, int i3,
            Vector2[] screenPojected,
            Vertex[] vertexList, Vector3[] normalList, Vector2[] uvList) {
        Vector2 pa = screenPojected[i1];
        Vector2 pb = screenPojected[i2];
        Vector2 pc = screenPojected[i3];

        float invslope1 = (pb.x - pa.x) / (pb.y - pa.y);
        float invslope2 = (pc.x - pa.x) / (pc.y - pa.y);

        float curx1 = pa.x;
        float curx2 = pa.x;

        for (int scanlineY = (int) pa.y; scanlineY <= pb.y; scanlineY++) {
            scanLine(up, rigth, matViewModel, poligono, curx1, curx2, scanlineY, i1, i2, i3, screenPojected, vertexList,
                    normalList, uvList);
            curx1 += invslope1;
            curx2 += invslope2;
        }

    }

    /**
     * Dibuja el triangulo hacia abajo
     *
     * @param poligono
     * @param i1       Indice del punto 1
     * @param i2       Indice del punto 2
     * @param i3       Indice del punto 3
     */
    private void scanTopFlatTriangle(Vector3 up, Vector3 rigth, Matrix4 matViewModel, Poly poligono, int i1, int i2,
            int i3,
            Vector2[] screenPojected,
            Vertex[] vertexList, Vector3[] normalList, Vector2[] uvList) {
        Vector2 pa = screenPojected[i1];
        Vector2 pb = screenPojected[i2];
        Vector2 pc = screenPojected[i3];

        float invslope1 = (pc.x - pa.x) / (pc.y - pa.y);
        float invslope2 = (pc.x - pb.x) / (pc.y - pb.y);

        float curx1 = pc.x;
        float curx2 = pc.x;

        for (int scanlineY = (int) pc.y; scanlineY > pa.y; scanlineY--) {
            scanLine(up, rigth, matViewModel, poligono, curx1, curx2, scanlineY, i1, i2, i3, screenPojected, vertexList,
                    normalList, uvList);
            curx1 -= invslope1;
            curx2 -= invslope2;
        }
    }

    /**
     * Realiza un recorrido por cada linea del triangulo a dibujar
     *
     * @param poligono
     * @param startX
     * @param endX
     * @param y
     * @param i1
     * @param i2
     * @param i3
     */
    private void scanLine(Vector3 up, Vector3 rigth, Matrix4 matViewModel, Poly poligono, float startX, float endX,
            int y, int i1, int i2, int i3,
            Vector2[] screenPojected, Vertex[] vertexList, Vector3[] normalList, Vector2[] uvList) {
        if (startX == endX) {
            return;
        }

        Vector2 pa = screenPojected[i1];
        Vector2 pb = screenPojected[i2];
        Vector2 pc = screenPojected[i3];

        Vertex VA = vertexList[i1];
        Vertex VB = vertexList[i2];
        Vertex VC = vertexList[i3];

        Vector3 normalVA = normalList[i1];
        Vector3 normalVB = normalList[i2];
        Vector3 normalVC = normalList[i3];

        Vector2 uvVA = uvList[i1];
        Vector2 uvVB = uvList[i2];
        Vector2 uvVC = uvList[i3];

        // left to right
        if (startX > endX) {
            int tmp = (int) startX;
            startX = endX;
            endX = tmp;
        }
        TempVars tempVar = TempVars.get();
        Vertex currentVertex = new Vertex();// t.vertex3;
        Vector2 currentUV = new Vector2();// t.vector2f3;
        Vector3 currentNormal = new Vector3();// t.vector3f3;
        try {
            for (int x = (int) startX; x < endX; x++) {
                if (x >= 0 && y >= 0 && render.getFrameBuffer().getWidth() > x
                        && render.getFrameBuffer().getHeight() > y) {
                    Vector3 bar = tempVar.vector3f8;
                    // Hallamos las coordenadas baricéntricas del punto v4 respecto al triángulo pa,
                    // pb, pc
                    QMath.getBarycentricCoordinates(bar, x, y, pa, pb, pc);

                    // correccion de proyectar
                    float zUV = 1.0f / (bar.x / VA.location.z + bar.y / VB.location.z + bar.z / VC.location.z);
                    // float zUV = 1.0f / (bar.x * 1.0f / VA.ubicacion.z + bar.y * 1.0f /
                    // VB.ubicacion.z + bar.z * 1.0f / VC.ubicacion.z);

                    // z coordenadas homogeneas
                    // float z = bar.x * VA.ubicacion.z + bar.y * VB.ubicacion.z + bar.z *
                    // VC.ubicacion.z; // Homogeneous clipspace
                    /// ------------- VERTICE INTERPOLADO-------------------------
                    currentVertex.location.x = bar.x * VA.location.x + bar.y * VB.location.x + bar.z * VC.location.x;
                    currentVertex.location.y = bar.x * VA.location.y + bar.y * VB.location.y + bar.z * VC.location.y;
                    // verticeActual.ubicacion.z = bar.x * VA.ubicacion.z + bar.y * VB.ubicacion.z +
                    // bar.z * VC.ubicacion.z;
                    // verticeActual.ubicacion.z = zUV * (bar.x * (VA.ubicacion.z / VA.ubicacion.z)
                    // + bar.y * (VB.ubicacion.z / VB.ubicacion.z) + bar.z * (VC.ubicacion.z /
                    // VC.ubicacion.z));
                    // correccion de proyectar
                    currentVertex.location.z = zUV * (bar.x + bar.y + bar.z);

                    // ------------------- COORDENADAS TEXTURAS-------------------------
                    // currentUV.x = bar.x * uvVA.x + bar.y * uvVB.x + bar.z * uvVC.x;
                    // currentUV.y = bar.x * uvVA.y + bar.y * uvVB.y + bar.z * uvVC.y;
                    // correccion de proyectar
                    currentUV.x = zUV * (bar.x * (uvVA.x / VA.location.z) + bar.y * (uvVB.x / VB.location.z)
                            + bar.z * (uvVC.x / VC.location.z));
                    currentUV.y = zUV * (bar.x * (uvVA.y / VA.location.z) + bar.y * (uvVB.y / VB.location.z)
                            + bar.z * (uvVC.y / VC.location.z));
                    //
                    // float zUV = 1.0f / (bar.x * VA.ubicacion.z + bar.y * VB.ubicacion.z + bar.z *
                    // VC.ubicacion.z);
                    // verticeActual.u/= verticeActual.ubicacion.z;
                    // verticeActual.v/= verticeActual.ubicacion.z;
                    // verticeActual.u *= zUV;
                    // verticeActual.v *= zUV;

                    // --------------------- NORMAL-------------------------
                    currentNormal.x = bar.x * normalVA.x + bar.y * normalVB.x + bar.z * normalVC.x;
                    currentNormal.y = bar.x * normalVA.y + bar.y * normalVB.y + bar.z * normalVC.y;
                    currentNormal.z = bar.x * normalVA.z + bar.y * normalVB.z + bar.z * normalVC.z;
                    currentNormal.normalize();
                    prepareFragment(up, rigth, matViewModel, poligono, currentVertex, currentNormal, currentUV, x, y);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tempVar.release();
        }
    }

    /**
     * Algoritmo para pintar una linea Bresenham
     *
     * @param primitiva
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     */
    private void drawLine(Vector3 up, Vector3 rigth, Matrix4 matViewModel, Primitive primitiva, int x0, int y0,
            int x1, int y1,
            Vertex[] vt, Vector3[] normals, Vector2[] uv) {
        int x, y, dx, dy, p, incE, incNE, stepx, stepy;
        dx = (x1 - x0);
        dy = (y1 - y0);
        /* determinar que punto usar para empezar, cual para terminar */
        if (dy < 0) {
            dy = -dy;
            stepy = -1;
        } else {
            stepy = 1;
        }
        if (dx < 0) {
            dx = -dx;
            stepx = -1;
        } else {
            stepx = 1;
        }
        x = x0;
        y = y0;

        computeCurrentVertex(up, rigth, matViewModel, vt[0], vt[1], normals[0], normals[1], uv[0], uv[1], 0, primitiva,
                x, y);

        /* se cicla hasta llegar al extremo de la línea */
        if (dx > dy) {
            p = 2 * dy - dx;
            incE = 2 * dy;
            incNE = 2 * (dy - dx);
            while (x != x1) {
                x = x + stepx;
                if (p < 0) {
                    p = p + incE;
                } else {
                    y = y + stepy;
                    p = p + incNE;
                }
                computeCurrentVertex(up, rigth, matViewModel, vt[0], vt[1], normals[0], normals[1], uv[0], uv[1],
                        (float) x / (float) dx,
                        primitiva, x, y);
            }
        } else {
            p = 2 * dx - dy;
            incE = 2 * dx;
            incNE = 2 * (dx - dy);
            while (y != y1) {
                y = y + stepy;
                if (p < 0) {
                    p = p + incE;
                } else {
                    x = x + stepx;
                    p = p + incNE;
                }
                computeCurrentVertex(up, rigth, matViewModel, vt[0], vt[1], normals[0], normals[1], uv[0], uv[1],
                        (float) x / (float) dx, primitiva, x, y);
            }
        }
    }

    protected void computeCurrentVertex(Vector3 up, Vector3 rigth, Matrix4 matViewModel, Vertex v1, Vertex v2,
            Vector3 v1Normal,
            Vector3 v2Normal, Vector2 v1uv, Vector2 v2uv, float alpha, Primitive poly,
            int x, int y) {
        // solo interpolamos z para validar profundidad
        float zVertex = QMath.interpolateLinear(alpha, v1.location.z, v2.location.z);
        try {
            if (validateZBuffer(x, y, zVertex)) {
                Vertex currentVertex = new Vertex();
                Vector3 currentNormal = Vector3.empty();
                Vector2 currentUV = new Vector2();
                QMath.interpolateLinear(currentVertex, alpha, v1, v2);
                QMath.interpolateLinear(currentNormal, alpha, v1Normal, v2Normal);
                QMath.interpolateLinear(currentUV, alpha, v1uv, v2uv);
                prepareFragment(up, rigth, matViewModel, poly, currentVertex, currentNormal, currentUV, x, y);
            }
        } catch (Exception e) {

        }
    }

    public boolean validateZBuffer(int x, int y, float z) {
        synchronized (this) {
            return z > render.getFrameBuffer().getZBuffer(x, y);
        }
    }

    public void updateZBuffer(int x, int y, float z) {
        synchronized (this) {
            render.getFrameBuffer().setZBuffer(x, y, z);
        }
    }

    /**
     * Prepara el pixel - Realiza la validacion de profundidad y setea los
     * valores a usar en el shader como material, entity, etc.
     *
     * @param poly
     * @param x
     * @param y
     */
    protected void prepareFragment(Vector3 up, Vector3 rigth, Matrix4 matViewModel, Primitive poly,
            Vertex currentVertex,
            Vector3 currentNormal, Vector2 currentUV, int x, int y) {
        if (x > 0 && x < render.getFrameBuffer().getWidth() && y > 0 && y < render.getFrameBuffer().getHeight()) {
            if (validateZBuffer(x, y, currentVertex.location.z)) {
                // si no es suavizado se copia la normal de la cara para dibujar con Flat
                // Shadded
                // igualmente si es tipo wire toma la normal de la cara porq no hay normal
                // interpolada

                if (poly instanceof Poly) {
                    if (poly.mesh.type == Mesh.GEOMETRY_TYPE_WIRE
                            || !(((Poly) poly).isSmooth()
                                    && (render.opciones.getTipoVista() >= RenderOptions.VISTA_PHONG)
                                    || render.opciones.isForzarSuavizado())) {
                        currentNormal.set(((Poly) poly).getNormalCopy());
                    }
                }

                // panelclip
                try {
                    if (render.getPanelClip() != null) {
                        if (!render.getPanelClip()
                                .isVisible(TransformationVectorUtil.transformarVector(
                                        TransformationVectorUtil.transformarVectorInversa(currentVertex.location,
                                                poly.mesh.getEntity(), render.getCamera()),
                                        poly.mesh.getEntity()))) {
                            return;
                        }
                    }
                } catch (Exception e) {
                }

                Fragment fragment = render.getFrameBuffer().getFragment(x, y);
                if (fragment != null) {

                    fragment.setDraw(true);
                    fragment.location.set(currentVertex.location);
                    fragment.normal.set(currentNormal);
                    fragment.material = poly.material;
                    fragment.primitiva = poly;
                    fragment.u = currentUV.x;
                    fragment.v = currentUV.y;
                    fragment.entity = poly.mesh.getEntity();
                    // fragment.matViewModel = matViewModel;
                    fragment.up.set(up);
                    fragment.right.set(rigth);

                    if (!render.opciones.isDefferedShadding())
                        render.getFrameBuffer().setQColor(x, y,
                                render.getShader().shadeFragment(fragment, x, y));
                }

                // actualiza el zBuffer
                updateZBuffer(x, y, currentVertex.location.z);
            }
        }
    }

    protected void calculateUpRight(Vector3 Tangent, Vector3 Bitangent, Vertex v0, Vertex v1, Vertex v2,
            Vector2 v0uv, Vector2 v1uv,
            Vector2 v2uv) {

        Vector3 Edge1 = v1.location.getVector3().subtract(v0.location.getVector3()); // v1.m_pos - v0.m_pos;
        Vector3 Edge2 = v2.location.getVector3().subtract(v0.location.getVector3()); // v2.m_pos - v0.m_pos;

        float DeltaU1 = v1uv.x - v0uv.x;
        float DeltaV1 = v1uv.y - v0uv.y;
        float DeltaU2 = v2uv.x - v0uv.x;
        float DeltaV2 = v2uv.y - v0uv.y;

        float f = 1.0f / (DeltaU1 * DeltaV2 - DeltaU2 * DeltaV1);

        Tangent.x = f * (DeltaV2 * Edge1.x - DeltaV1 * Edge2.x);
        Tangent.y = f * (DeltaV2 * Edge1.y - DeltaV1 * Edge2.y);
        Tangent.z = f * (DeltaV2 * Edge1.z - DeltaV1 * Edge2.z);

        Bitangent.x = f * (-DeltaU2 * Edge1.x + DeltaU1 * Edge2.x);
        Bitangent.y = f * (-DeltaU2 * Edge1.y + DeltaU1 * Edge2.y);
        Bitangent.z = f * (-DeltaU2 * Edge1.z + DeltaU1 * Edge2.z);

        // v0.m_tangent += Tangent;
        // v1.m_tangent += Tangent;
        // v2.m_tangent += Tangent;

    }

    private Vector2[] projectVertices(Vertex... v) {
        // obtenemos los puntos proyectados en la pantalla
        Vector4[] locations = new Vector4[v.length];
        for (int i = 0; i < v.length; i++) {
            locations[i] = v[i].location;
        }
        return projectVectors(locations);
    }

    private Vector2[] projectVectors(Vector4... v) {
        Vector2[] screenPojected = new Vector2[v.length];

        // obtenemos los puntos proyectados en la pantalla

        for (int i = 0; i < v.length; i++) {
            screenPojected[i] = new Vector2();
        }
        for (int i = 0; i < v.length; i++) {
            render.getCamera().getCoordenadasPantalla(screenPojected[i], v[i],
                    render.getFrameBuffer().getWidth(), render.getFrameBuffer().getHeight());
        }
        return screenPojected;
    }

}
