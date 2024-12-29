/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.raster;

import java.util.List;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Line;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.transform.QVertexBuffer;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.material.node.MaterialNode;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.math.QVector4;
import net.qoopo.engine.core.renderer.QOpcionesRenderer;
import net.qoopo.engine.core.util.TempVars;
import net.qoopo.engine.renderer.SoftwareRenderer;
import net.qoopo.engine.renderer.util.TransformationVectorUtil;

/**
 * Realiza la rasterización de los polígonos version 2. Aun tiene fallas en el
 * mapeo de textures por la perspectiva
 *
 * @author alberto
 */
public class ParallelRaster implements AbstractRaster {
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
    public void rasterLine(QPrimitiva primitiva, Vertex... vertex) {
        int[] indices = new int[vertex.length];
        for (int i = 0; i < vertex.length; i++)
            indices[i] = i;
        List<Vertex> clippedVertex = AbstractRaster.clipping(render.getCamara(), indices, vertex);
        // List<Vertex> clippedVertex =List.of(vertex);
        for (int i = 0; i < clippedVertex.size() - 1; i++) {
            QVector2[] screenPojected = projectVertices(clippedVertex.get(i), clippedVertex.get(i + 1));
            lineaBresenham(QVector3.empty(), QVector3.empty(), primitiva, (int) screenPojected[0].x,
                    (int) screenPojected[0].y, (int) screenPojected[1].x,
                    (int) screenPojected[1].y,
                    // vertex
                    new Vertex[] { clippedVertex.get(i), clippedVertex.get(i + 1) });
        }
    }

    /**
     * Realiza la rasterización de un polígono
     *
     * @param bufferVertices
     * @param primitiva
     * @param wire
     *
     */
    @Override
    public void raster(QVertexBuffer bufferVertices, QPrimitiva primitiva, boolean wire) {
        if (primitiva instanceof Poly) {

            if (wire) {
                rasterWirePolygon(bufferVertices, (Poly) primitiva);
            } else {
                rasterFullPolygon(bufferVertices, (Poly) primitiva);
            }
        } else if (primitiva instanceof Line) {
            Vertex p1 = bufferVertices.getVerticesTransformados()[primitiva.listaVertices[0]];
            Vertex p2 = bufferVertices.getVerticesTransformados()[primitiva.listaVertices[1]];
            rasterLine(primitiva, p1, p2);
        }
    }

    /**
     * Realiza un proceso de un polígono donde solo dibuja las aristas
     *
     * @param vertexBuffer
     * @param poligono
     *
     */
    protected void rasterWirePolygon(QVertexBuffer vertexBuffer, Poly poligono) {
        try {
            Vertex v1;
            Vertex v2;
            Vertex v3;
            // synchronized (this) {
            if (poligono.listaVertices.length >= 3) {
                // QVector3 toCenter=poligono.getCenterCopy().location.getVector3();

                // // Rasterizacion (dibujo de los puntos del plano)
                // for (int i = 1; i < vertexBuffer.getVerticesTransformados().length - 1; i++)
                // {
                // v1 = vertexBuffer.getVerticesTransformados()[0];
                // v2 = vertexBuffer.getVerticesTransformados()[i];
                // v3 = vertexBuffer.getVerticesTransformados()[i + 1];

                // rasterLine(poligono, v1, v2);
                // rasterLine(poligono, v1, v3);
                // rasterLine(poligono, v2, v3);
                // }

                List<Vertex> clippedVertex = AbstractRaster.clipping(render.getCamara(),
                        poligono.listaVertices,
                        vertexBuffer.getVerticesTransformados());

                // Rasterizacion (dibujo de los puntos del plano)
                // Separo en triangulos sin importar cuantos puntos tenga
                for (int i = 1; i < clippedVertex.size() - 1; i++) {
                    v1 = clippedVertex.get(0);
                    v2 = clippedVertex.get(i);
                    v3 = clippedVertex.get(i + 1);
                    // si el triangulo no esta en el campo de vision, pasamos y no dibujamos
                    if (!render.getCamara().isVisible(v1) &&
                            !render.getCamara().isVisible(v2)
                            && !render.getCamara().isVisible(v3)) {
                        continue;
                    }

                    rasterLine(poligono, v1, v2);
                    rasterLine(poligono, v1, v3);
                    rasterLine(poligono, v2, v3);
                }
            }
            // }
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
    private void rasterFullPolygon(QVertexBuffer bufferVertices, Poly poly) {
        try {
            if (poly.listaVertices.length >= 3) {
                QVector3 toCenter = poly.getCenterCopy().location.getVector3();
                // validación caras traseras
                // si el objeto es tipo wire se dibuja igual sus caras traseras
                // si el objeto tiene transparencia (con material básico) igual dibuja sus caras
                // traseras
                if ((!(poly.material instanceof QMaterialBas) || ((poly.material instanceof QMaterialBas)
                        && !((QMaterialBas) poly.material).isTransparencia()))
                        && !render.opciones.isDibujarCarasTraseras() && toCenter.dot(poly.getNormalCopy()) > 0) {
                    render.poligonosDibujadosTemp--;
                    return; // salta el dibujo de caras traseras
                }

                List<Vertex> clippedVertex = AbstractRaster.clipping(render.getCamara(), poly.listaVertices,
                        bufferVertices.getVerticesTransformados());

                // Rasterizacion (dibujo de los puntos del plano)
                // Separo en triangulos sin importar cuantos puntos tenga
                for (int i = 1; i < clippedVertex.size() - 1; i++) {
                    Vertex v1 = clippedVertex.get(0);
                    Vertex v2 = clippedVertex.get(i);
                    Vertex v3 = clippedVertex.get(i + 1);
                    // si el triangulo no esta en el campo de vision, pasamos y no dibujamos
                    if (!render.getCamara().isVisible(v1) && !render.getCamara().isVisible(v2)
                            && !render.getCamara().isVisible(v3)) {
                        continue;
                    }
                    rasterTriangle(v1, v2, v3, poly);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rasterTriangle(Vertex v1, Vertex v2, Vertex v3, Poly poly) {
        // obtenemos los puntos proyectados en la pantalla
        QVector2[] screenPojected = projectVertices(v1, v2, v3, v3); // se coloca el v3 en cuarto lugar para tener 4
                                                                     // posiciones en caso de agregar un nuevo vertice

        Vertex[] vt = { v1, v2, v3 };

        QVector3 up = QVector3.empty();
        QVector3 right = QVector3.empty();

        // valido si el punto proyectado esta dentro del rango de vision de la Camara
        if ((screenPojected[0].x < 0 && screenPojected[1].x < 0 && screenPojected[2].x < 0)
                || (screenPojected[0].y < 0 && screenPojected[1].y < 0 && screenPojected[2].y < 0)
                || (screenPojected[0].x > render.getFrameBuffer().getAncho()
                        && screenPojected[1].x > render.getFrameBuffer().getAncho()
                        && screenPojected[2].x > render.getFrameBuffer().getAncho())
                || (screenPojected[0].y > render.getFrameBuffer().getAlto()
                        && screenPojected[1].y > render.getFrameBuffer().getAlto()
                        && screenPojected[2].y > render.getFrameBuffer().getAlto())) {
            render.poligonosDibujados--;
            return;
        }

        // mapeo normal para materiales básicos
        if ((poly.material instanceof QMaterialBas
                && ((QMaterialBas) poly.material).getMapaNormal() != null) // || (primitiva.material
                                                                           // instanceof QMaterialBas &&
                                                                           // ((QMaterialBas)
                                                                           // primitiva.material).getMapaDesplazamiento()
                                                                           // != null)
        ) {
            calcularArriba(up, v1, v2, v3);
            calcularDerecha(right, v1, v2, v3);
            up.normalize();
            right.normalize();
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
            scanBottomFlatTriangle(up, right, poly, order[0], order[1], order[2], screenPojected, vt);
        } else if (screenPojected[order[0]].y == screenPojected[order[1]].y) {
            // this->scanTopFlatTriangle(v1, v2, v3);
            scanTopFlatTriangle(up, right, poly, order[0], order[1], order[2], screenPojected, vt);
        } else {
            // En este caso vamos a dividir el triángulo en dos
            // para tener uno que cumpla 'bottomFlat' y el otro 'TopFlat' y necesitamos un
            // punto extra para separar ambos
            TempVars tvars = TempVars.get();

            try {

                // el nuevo vertice a crear
                Vertex v4 = tvars.vertex1;

                QVector2 screenProjectedV4 = new QVector2();
                screenProjectedV4.setXY(
                        (screenPojected[order[0]].x + ((screenPojected[order[1]].y - screenPojected[order[0]].y)
                                / (screenPojected[order[2]].y - screenPojected[order[0]].y))
                                * (screenPojected[order[2]].x - screenPojected[order[0]].x)),
                        screenPojected[order[1]].y);

                QVector3 bar = tvars.vector3f1;
                // Hallamos las coordenadas baricéntricas del punto v4 respecto al triángulo pa,
                // pb, pc
                QMath.getBarycentricCoordinates(bar, screenProjectedV4.x, screenProjectedV4.y, screenPojected[order[0]],
                        screenPojected[order[1]], screenPojected[order[2]]);

                // correccion de proyectar
                // float zUV = 1.0f / (bar.x * 1.0f / vt[order[0]].ubicacion.z + bar.y * 1.0f /
                // vt[order[1]].ubicacion.z + bar.z * 1.0f / vt[order[2]].ubicacion.z);
                float zUV = 1.0f / (bar.x / vt[order[0]].location.z + bar.y / vt[order[1]].location.z
                        + bar.z / vt[order[2]].location.z);

                // calculamos el vertice nuevo
                v4.location.x = bar.x * vt[order[0]].location.x + bar.y * vt[order[1]].location.x
                        + bar.z * vt[order[2]].location.x;
                v4.location.y = bar.x * vt[order[0]].location.y + bar.y * vt[order[1]].location.y
                        + bar.z * vt[order[2]].location.y;
                // correccion de proyectar
                v4.location.z = zUV * (bar.x + bar.y + bar.z);

                // coordenadas de textura
                // v4.u = bar.x * vt[order[0]].u + bar.y * vt[order[1]].u + bar.z *
                // vt[order[2]].u;
                // v4.v = bar.x * vt[order[0]].v + bar.y * vt[order[1]].v + bar.z *
                // vt[order[2]].v;
                // correccion de proyectar
                v4.u = zUV * (bar.x * (vt[order[0]].u / vt[order[0]].location.z)
                        + bar.y * (vt[order[1]].u / vt[order[1]].location.z)
                        + bar.z * (vt[order[2]].u / vt[order[2]].location.z));
                v4.v = zUV * (bar.x * (vt[order[0]].v / vt[order[0]].location.z)
                        + bar.y * (vt[order[1]].v / vt[order[1]].location.z)
                        + bar.z * (vt[order[2]].v / vt[order[2]].location.z));

                // la normal
                v4.normal.x = bar.x * vt[order[0]].normal.x + bar.y * vt[order[1]].normal.x
                        + bar.z * vt[order[2]].normal.x;
                v4.normal.y = bar.x * vt[order[0]].normal.y + bar.y * vt[order[1]].normal.y
                        + bar.z * vt[order[2]].normal.y;
                v4.normal.z = bar.x * vt[order[0]].normal.z + bar.y * vt[order[1]].normal.z
                        + bar.z * vt[order[2]].normal.z;

                screenPojected[3] = screenProjectedV4;
                scanBottomFlatTriangle(up, right, poly, order[0], order[1], 3, screenPojected,
                        new Vertex[] { v1, v2, v3, v4 });
                // this->scanBottomFlatTriangle(v1, v2, v4);
                scanTopFlatTriangle(up, right, poly, order[1], 3, order[2], screenPojected,
                        new Vertex[] { v1, v2, v3, v4 });
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
    private void scanBottomFlatTriangle(QVector3 up, QVector3 right, Poly poligono, int i1, int i2, int i3,
            QVector2[] screenPojected,
            Vertex[] vt) {
        QVector2 pa = screenPojected[i1];
        QVector2 pb = screenPojected[i2];
        QVector2 pc = screenPojected[i3];

        float invslope1 = (pb.x - pa.x) / (pb.y - pa.y);
        float invslope2 = (pc.x - pa.x) / (pc.y - pa.y);

        float curx1 = pa.x;
        float curx2 = pa.x;

        for (int scanlineY = (int) pa.y; scanlineY <= pb.y; scanlineY++) {
            scanLine(up, right, poligono, curx1, curx2, scanlineY, i1, i2, i3, screenPojected, vt);
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
    private void scanTopFlatTriangle(QVector3 up, QVector3 right, Poly poligono, int i1, int i2, int i3,
            QVector2[] screenPojected,
            Vertex[] vt) {
        QVector2 pa = screenPojected[i1];
        QVector2 pb = screenPojected[i2];
        QVector2 pc = screenPojected[i3];

        float invslope1 = (pc.x - pa.x) / (pc.y - pa.y);
        float invslope2 = (pc.x - pb.x) / (pc.y - pb.y);

        float curx1 = pc.x;
        float curx2 = pc.x;

        for (int scanlineY = (int) pc.y; scanlineY > pa.y; scanlineY--) {
            scanLine(up, right, poligono, curx1, curx2, scanlineY, i1, i2, i3, screenPojected, vt);
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
    private void scanLine(QVector3 up, QVector3 right, Poly poligono, float startX, float endX, int y, int i1, int i2,
            int i3,
            QVector2[] screenPojected, Vertex[] vt) {
        if (startX == endX) {
            return;
        }

        QVector2 pa = screenPojected[i1];
        QVector2 pb = screenPojected[i2];
        QVector2 pc = screenPojected[i3];

        Vertex VA = vt[i1];
        Vertex VB = vt[i2];
        Vertex VC = vt[i3];

        // left to right
        if (startX > endX) {
            int tmp = (int) startX;
            startX = endX;
            endX = tmp;
        }
        TempVars t = TempVars.get();
        Vertex currentVertex = new Vertex();
        try {
            for (int x = (int) startX; x < endX; x++) {
                if (x >= 0 && y >= 0 && render.getFrameBuffer().getAncho() > x
                        && render.getFrameBuffer().getAlto() > y) {
                    QVector3 bar = t.vector3f1;
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
                    /// --------------------------------- VERTICE
                    // INTERPOLADO-------------------------
                    currentVertex.location.x = bar.x * VA.location.x + bar.y * VB.location.x + bar.z * VC.location.x;
                    currentVertex.location.y = bar.x * VA.location.y + bar.y * VB.location.y + bar.z * VC.location.y;
                    // verticeActual.ubicacion.z = bar.x * VA.ubicacion.z + bar.y * VB.ubicacion.z +
                    // bar.z * VC.ubicacion.z;
                    // verticeActual.ubicacion.z = zUV * (bar.x * (VA.ubicacion.z / VA.ubicacion.z)
                    // + bar.y * (VB.ubicacion.z / VB.ubicacion.z) + bar.z * (VC.ubicacion.z /
                    // VC.ubicacion.z));
                    // correccion de proyectar
                    currentVertex.location.z = zUV * (bar.x + bar.y + bar.z);

                    // --------------------------------- COORDENADAS
                    // TEXTURAS-------------------------
                    // verticeActual.u = bar.x * VA.u + bar.y * VB.u + bar.z * VC.u;
                    // verticeActual.v = bar.x * VA.v + bar.y * VB.v + bar.z * VC.v;
                    // correccion de proyectar
                    currentVertex.u = zUV * (bar.x * (VA.u / VA.location.z) + bar.y * (VB.u / VB.location.z)
                            + bar.z * (VC.u / VC.location.z));
                    currentVertex.v = zUV * (bar.x * (VA.v / VA.location.z) + bar.y * (VB.v / VB.location.z)
                            + bar.z * (VC.v / VC.location.z));
                    //
                    // float zUV = 1.0f / (bar.x * VA.ubicacion.z + bar.y * VB.ubicacion.z + bar.z *
                    // VC.ubicacion.z);
                    // verticeActual.u/= verticeActual.ubicacion.z;
                    // verticeActual.v/= verticeActual.ubicacion.z;
                    // verticeActual.u *= zUV;
                    // verticeActual.v *= zUV;

                    // --------------------------------- NORMAL-------------------------
                    currentVertex.normal.x = bar.x * VA.normal.x + bar.y * VB.normal.x + bar.z * VC.normal.x;
                    currentVertex.normal.y = bar.x * VA.normal.y + bar.y * VB.normal.y + bar.z * VC.normal.y;
                    currentVertex.normal.z = bar.x * VA.normal.z + bar.y * VB.normal.z + bar.z * VC.normal.z;
                    // verticeActual.normal.normalize();
                    prepareFragment(up, right, poligono, currentVertex, x, y);
                }
            }
        } catch (Exception e) {

        } finally {
            t.release();
        }
    }

    protected void computeCurrentVertex(QVector3 up, QVector3 right, Vertex v1, Vertex v2, float alpha, QPrimitiva poly,
            int x, int y) {
        // solo interpolamos z para validar profundidad
        float zVertex = QMath.interpolateLinear(alpha, v1.location.z, v2.location.z);
        try {
            if (validateZBuffer(x, y, zVertex)) {
                Vertex currentVertex = new Vertex();
                QMath.interpolateLinear(currentVertex, alpha, v1, v2);
                prepareFragment(up, right, poly, currentVertex, x, y);
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
    protected void prepareFragment(QVector3 up, QVector3 right, QPrimitiva poly, Vertex currentVertex, int x, int y) {
        if (x > 0 && x < render.getFrameBuffer().getAncho() && y > 0 && y < render.getFrameBuffer().getAlto()) {
            if (validateZBuffer(x, y, currentVertex.location.z)) {
                // si no es suavizado se copia la normal de la cara para dibujar con Flat
                // Shadded
                // igualmente si es tipo wire toma la normal de la cara porq no hay normal
                // interpolada

                if (poly instanceof Poly) {
                    if (poly.geometria.tipo == Mesh.GEOMETRY_TYPE_WIRE
                            || !(((Poly) poly).isSmooth()
                                    && (render.opciones.getTipoVista() >= QOpcionesRenderer.VISTA_PHONG)
                                    || render.opciones.isForzarSuavizado())) {
                        currentVertex.normal.set(((Poly) poly).getNormalCopy());
                    }
                }

                if (render.opciones.isMaterial()) {
                    // Mapa de normales
                    if (poly.material != null
                            && (poly.material instanceof QMaterialBas
                                    && ((QMaterialBas) poly.material).getMapaNormal() != null) // si tiene material
                                                                                               // basico y tiene mapa
                                                                                               // normal
                            && render.opciones.isNormalMapping()) {
                        QMaterialBas material = (QMaterialBas) poly.material;

                        QVector3 currentUp = QVector3.empty();
                        QVector3 currentRight = QVector3.empty();

                        currentUp.set(up);
                        currentRight.set(right);
                        // usando el metodo arriba e izquierda
                        currentUp.multiply(
                                (material.getMapaNormal().getNormalY(currentVertex.u, currentVertex.v) * 2 - 1)
                                        * material.getFactorNormal());
                        currentRight
                                .multiply(
                                        (material.getMapaNormal().getNormalX(currentVertex.u, currentVertex.v) * 2 - 1)
                                                * material.getFactorNormal());
                        // continua ejecucion normal
                        currentVertex.normal
                                .multiply(
                                        material.getMapaNormal().getNormalZ(currentVertex.u, currentVertex.v) * 2 - 1);
                        currentVertex.normal.add(currentUp, currentRight);
                        currentVertex.normal.normalize();
                    }

                    // si tiene material y tiene mapa normal
                    if (poly.material != null && (poly.material instanceof MaterialNode)
                            && render.opciones.isNormalMapping()) {
                        currentVertex.up.set(up);
                        currentVertex.right.set(right);
                    }
                }

                // panelclip
                try {
                    if (render.getPanelClip() != null) {
                        if (!render.getPanelClip()
                                .esVisible(TransformationVectorUtil.transformarVector(
                                        TransformationVectorUtil.transformarVectorInversa(currentVertex.location,
                                                poly.geometria.entity, render.getCamara()),
                                        poly.geometria.entity))) {
                            return;
                        }
                    }
                } catch (Exception e) {
                }

                if (render.getFrameBuffer().getPixel(x, y) != null) {
                    render.getFrameBuffer().getPixel(x, y).setDibujar(true);
                    render.getFrameBuffer().getPixel(x, y).ubicacion.set(currentVertex.location);
                    render.getFrameBuffer().getPixel(x, y).normal.set(currentVertex.normal);
                    render.getFrameBuffer().getPixel(x, y).material = poly.material;
                    render.getFrameBuffer().getPixel(x, y).primitiva = poly;
                    render.getFrameBuffer().getPixel(x, y).u = currentVertex.u;
                    render.getFrameBuffer().getPixel(x, y).v = currentVertex.v;
                    render.getFrameBuffer().getPixel(x, y).entity = poly.geometria.entity;
                    render.getFrameBuffer().getPixel(x, y).up.set(currentVertex.up);
                    render.getFrameBuffer().getPixel(x, y).right.set(currentVertex.right);
                    // synchronized (this) {
                    if (!render.opciones.isDefferedShadding())
                        render.getFrameBuffer().setQColor(x, y,
                                render.getShader().shadeFragment(render.getFrameBuffer().getPixel(x, y), x, y));
                    // }
                }

                // actualiza el zBuffer
                updateZBuffer(x, y, currentVertex.location.z);
            }
        }
    }

    protected void calcularArriba(QVector3 upVector, Vertex v1, Vertex v2, Vertex v3) {
        float coefficient1, coefficient2;
        if (v1.u == v2.u) {
            coefficient1 = 0;
            coefficient2 = 1.0f / (v1.v - v2.v);
        } else if (v1.u == v3.u) {
            coefficient1 = 1.0f / (v1.v - v3.v);
            coefficient2 = 0;
        } else {
            coefficient1 = 1.0f / ((v1.v - v3.v) - (v1.u - v3.u) * (v1.v - v2.v) / (v1.u - v2.u));
            coefficient2 = (-coefficient1 * (v1.u - v3.u)) / (v1.u - v2.u);
        }
        upVector.x = coefficient1 * (v1.location.x - v3.location.x) + coefficient2 * (v1.location.x - v2.location.x);
        upVector.y = coefficient1 * (v1.location.y - v3.location.y) + coefficient2 * (v1.location.y - v2.location.y);
        upVector.z = coefficient1 * (v1.location.z - v3.location.z) + coefficient2 * (v1.location.z - v2.location.z);
    }

    protected void calcularDerecha(QVector3 rightVector, Vertex v1, Vertex v2, Vertex v3) {
        float coefficient1, coefficient2;
        if (v1.v == v2.v) {
            coefficient1 = 0;
            coefficient2 = 1.0f / (v1.u - v2.u);
        } else if (v1.v == v3.v) {
            coefficient1 = 1.0f / (v1.u - v3.u);
            coefficient2 = 0;
        } else {
            coefficient1 = 1.0f / ((v1.u - v3.u) - (v1.v - v3.v) * (v1.u - v2.u) / (v1.v - v2.v));
            coefficient2 = (-coefficient1 * (v1.v - v3.v)) / (v1.v - v2.v);
        }
        rightVector.x = coefficient1 * (v1.location.x - v3.location.x) + coefficient2 * (v1.location.x - v2.location.x);
        rightVector.y = coefficient1 * (v1.location.y - v3.location.y) + coefficient2 * (v1.location.y - v2.location.y);
        rightVector.z = coefficient1 * (v1.location.z - v3.location.z) + coefficient2 * (v1.location.z - v2.location.z);
    }

    /**
     * Algoritmo para pintar una linea
     *
     * @param primitiva
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     */
    public void lineaBresenham(QVector3 up, QVector3 right, QPrimitiva primitiva, int x0, int y0, int x1, int y1,
            Vertex[] vt) {
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

        computeCurrentVertex(up, right, vt[0], vt[1], 0, primitiva, x, y);

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
                computeCurrentVertex(up, right, vt[0], vt[1], (float) x / (float) dx, primitiva, x, y);
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
                computeCurrentVertex(up, right, vt[0], vt[1], (float) x / (float) dx, primitiva, x, y);
            }
        }
    }

    private QVector2[] projectVertices(Vertex... v) {
        // obtenemos los puntos proyectados en la pantalla
        QVector4[] locations = new QVector4[v.length];
        for (int i = 0; i < v.length; i++) {
            locations[i] = v[i].location;
        }
        return projectVectors(locations);
    }

    private QVector2[] projectVectors(QVector4... v) {
        QVector2[] screenPojected = new QVector2[v.length];

        // obtenemos los puntos proyectados en la pantalla

        for (int i = 0; i < v.length; i++) {
            screenPojected[i] = new QVector2();
        }
        for (int i = 0; i < v.length; i++) {
            render.getCamara().getCoordenadasPantalla(screenPojected[i], v[i],
                    render.getFrameBuffer().getAncho(), render.getFrameBuffer().getAlto());
        }
        return screenPojected;
    }

}
