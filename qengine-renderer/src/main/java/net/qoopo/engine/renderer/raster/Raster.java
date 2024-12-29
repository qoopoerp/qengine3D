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
import net.qoopo.engine.renderer.SoftwareRenderer;
import net.qoopo.engine.renderer.util.TransformationVectorUtil;

/**
 * Realiza la rasterización de los polígonos
 *
 * @author alberto
 */
public class Raster implements AbstractRaster {

    // private static Logger log = Logger.getLogger("qraster1");

    protected SoftwareRenderer render;
    protected QVector3 toCenter = QVector3.empty();
    protected Vertex[] vt = new Vertex[3]; // vertices transformados
    protected Vertex verticeTmp = new Vertex();
    protected float alfa;// factor de interpolación

    protected QVector2[] screenPojected = new QVector2[3]; // puntos proyectados
    protected int order[] = new int[3];
    protected int temp;

    protected float zDesde;
    protected float zHasta;
    protected float xDesde;
    protected float xHasta;
    protected float zActual;

    protected float tmpU, tmpV;
    protected float uDesde, vDesde;
    protected float uHasta, vHasta;

    protected int xDesdePantalla;
    protected int xHastaPantalla;
    protected int increment;

    protected final float INTERPOLATION_CLAMP = .0001f;
    protected Vertex verticeDesde = new Vertex();
    protected Vertex verticeHasta = new Vertex();
    protected Vertex verticeActual = new Vertex();
    protected QVector3 up = QVector3.empty();
    protected QVector3 right = QVector3.empty();
    protected QVector3 currentUp = QVector3.empty();
    protected QVector3 currentRight = QVector3.empty();
    protected float tempFloat, vYLength, vXLength, coefficient1, coefficient2;

    public Raster(SoftwareRenderer render) {
        this.render = render;
        for (int i = 0; i < 3; i++) {
            screenPojected[i] = new QVector2();
            // vt[i] = new Vertex();
        }
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
        // order[0] = 0;
        // order[1] = 1;
        // order[2] = 2;

        for (int i = 0; i < vertex.length; i++)
            indices[i] = i;
        List<Vertex> clippedVertex = AbstractRaster.clipping(render.getCamara(), indices, vertex);
        for (int i = 0; i < clippedVertex.size() - 1; i++) {
            vt[0] = clippedVertex.get(i);
            vt[1] = clippedVertex.get(i + 1);
            vt[2] = clippedVertex.get(i + 1);

            projectVertices(clippedVertex.get(i), clippedVertex.get(i + 1), clippedVertex.get(i + 1));
            lineaBresenham(primitiva, (int) screenPojected[0].x, (int) screenPojected[0].y, (int) screenPojected[1].x,
                    (int) screenPojected[1].y);
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
     */
    private void rasterWirePolygon(QVertexBuffer vertexBuffer, Poly poligono) {
        try {
            synchronized (this) {
                if (poligono.listaVertices.length >= 3) {
                    toCenter.set(poligono.getCenterCopy().location.getVector3());

                    // // Rasterizacion (dibujo de los puntos del plano)
                    // for (int i = 1; i < vertexBuffer.getVerticesTransformados().length - 1; i++)
                    // {
                    // vt[0] = vertexBuffer.getVerticesTransformados()[0];
                    // vt[1] = vertexBuffer.getVerticesTransformados()[i];
                    // vt[2] = vertexBuffer.getVerticesTransformados()[i + 1];

                    // rasterLine(poligono, vt[0], vt[1]);
                    // rasterLine(poligono, vt[0], vt[2]);
                    // rasterLine(poligono, vt[1], vt[2]);
                    // }

                    List<Vertex> clippedVertex = AbstractRaster.clipping(render.getCamara(),
                            poligono.listaVertices,
                            vertexBuffer.getVerticesTransformados());

                    // Rasterizacion (dibujo de los puntos del plano)
                    // Separo en triangulos sin importar cuantos puntos tenga
                    for (int i = 1; i < clippedVertex.size() - 1; i++) {
                        vt[0] = clippedVertex.get(0);
                        vt[1] = clippedVertex.get(i);
                        vt[2] = clippedVertex.get(i + 1);
                        // si el triangulo no esta en el campo de vision, pasamos y no dibujamos
                        if (!render.getCamara().isVisible(vt[0]) &&
                                !render.getCamara().isVisible(vt[1])
                                && !render.getCamara().isVisible(vt[2])) {
                            continue;
                        }
                        rasterLine(poligono, vt[0], vt[1]);
                        rasterLine(poligono, vt[0], vt[2]);
                        rasterLine(poligono, vt[1], vt[2]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza el proceso de rasterización de un polígono sólido
     *
     * @param poligono
     * @param siempreTop
     * 
     */
    private void rasterFullPolygon(QVertexBuffer vertexBuffer, Poly poligono) {
        try {
            synchronized (this) {
                if (poligono.listaVertices.length >= 3) {
                    toCenter.set(poligono.getCenterCopy().location.getVector3());
                    // validación caras traseras
                    // si el objeto es tipo wire se dibuja igual sus caras traseras
                    // si el objeto tiene transparencia (con material básico) igual dibuja sus caras
                    // traseras
                    if ((!(poligono.material instanceof QMaterialBas) || ((poligono.material instanceof QMaterialBas)
                            && !((QMaterialBas) poligono.material).isTransparencia()))
                            && !render.opciones.isDibujarCarasTraseras()
                            && toCenter.dot(poligono.getNormalCopy()) > 0) {
                        render.poligonosDibujadosTemp--;
                        return; // salta el dibujo de caras traseras
                    }

                    List<Vertex> clippedVertex = AbstractRaster.clipping(render.getCamara(), poligono.listaVertices,
                            vertexBuffer.getVerticesTransformados());

                    // Rasterizacion (dibujo de los puntos del plano)
                    // Separo en triangulos sin importar cuantos puntos tenga
                    for (int i = 1; i < clippedVertex.size() - 1; i++) {
                        vt[0] = clippedVertex.get(0);
                        vt[1] = clippedVertex.get(i);
                        vt[2] = clippedVertex.get(i + 1);
                        // si el triangulo no esta en el campo de vision, pasamos y no dibujamos
                        if (!render.getCamara().isVisible(vt[0]) && !render.getCamara().isVisible(vt[1])
                                && !render.getCamara().isVisible(vt[2])) {
                            continue;
                        }
                        rasterTriangle(vt[0], vt[1], vt[2], poligono);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rasterTriangle(Vertex v1, Vertex v2, Vertex v3, Poly poligono) {
        // obtenemos los puntos proyectados en la pantalla
        projectVertices(v1, v2, v3);

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
        if ((poligono.material instanceof QMaterialBas
                && ((QMaterialBas) poligono.material).getMapaNormal() != null) // || (primitiva.material
                                                                               // instanceof
                                                                               // QMaterialBas &&
                                                                               // ((QMaterialBas)
                                                                               // primitiva.material).getMapaDesplazamiento()
                                                                               // != null)
        ) {
            calcularArriba(up, v1, v2, v3);
            calcularDerecha(right, v1, v2, v3);
            up.normalize();
            right.normalize();
        }

        order[0] = 0;
        order[1] = 1;
        order[2] = 2;

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

        // proceso de dibujo, se deberia separar en una clase/metodo de escaneo

        // log.info("rasterizando y1=" + screenPojected[order[0]].y + " y2=" +
        // screenPojected[order[2]].y
        // + " x1=" + screenPojected[order[0]].x + " x2=" + screenPojected[order[2]].x);

        for (int y = (int) screenPojected[order[0]].y; y <= screenPojected[order[2]].y; y++) {
            if (y > render.getFrameBuffer().getAlto()) {
                break;
            }
            if (y < 0) {
                if (screenPojected[order[2]].y > 0) {
                    y = 0;
                }
            }

            zHasta = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z,
                    vt[order[2]].location.y, vt[order[2]].location.z, y,
                    (int) render.getFrameBuffer().getAlto(), render.getCamara().camaraAlto);
            xHastaPantalla = (int) QMath.interpolateLinear(screenPojected[order[0]].y, screenPojected[order[2]].y,
                    y,
                    screenPojected[order[0]].x, screenPojected[order[2]].x);

            if (!testDifference(vt[order[0]].location.z, vt[order[2]].location.z)) {
                alfa = (zHasta - vt[order[0]].location.z)
                        / (vt[order[2]].location.z - vt[order[0]].location.z);
            } else {
                alfa = screenPojected[order[0]].y == screenPojected[order[2]].y ? 1
                        : (float) (y - screenPojected[order[0]].y)
                                / (float) (screenPojected[order[2]].y - screenPojected[order[0]].y);
            }
            xHasta = QMath.interpolateLinear(alfa, vt[order[0]].location.x, vt[order[2]].location.x);
            QMath.interpolateLinear(verticeHasta, alfa, vt[order[0]], vt[order[2]]);
            if (y <= screenPojected[order[1]].y) {
                // Primera mitad
                if (vt[order[0]].location.y != vt[order[1]].location.y) {
                    zDesde = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z,
                            vt[order[1]].location.y, vt[order[1]].location.z, y,
                            (int) render.getFrameBuffer().getAlto(), render.getCamara().camaraAlto);
                } else {
                    zDesde = vt[order[1]].location.z;
                }
                if (!testDifference(vt[order[0]].location.z, vt[order[1]].location.z)) {
                    alfa = (zDesde - vt[order[0]].location.z)
                            / (vt[order[1]].location.z - vt[order[0]].location.z);
                } else {
                    alfa = screenPojected[order[0]].y == screenPojected[order[1]].y ? 0
                            : (float) (y - screenPojected[order[0]].y)
                                    / (float) (screenPojected[order[1]].y - screenPojected[order[0]].y);
                }
                xDesde = QMath.interpolateLinear(alfa, vt[order[0]].location.x, vt[order[1]].location.x);
                uDesde = QMath.interpolateLinear(alfa, vt[order[0]].u, vt[order[1]].u);
                vDesde = QMath.interpolateLinear(alfa, vt[order[0]].v, vt[order[1]].v);
                xDesdePantalla = (int) QMath.interpolateLinear(screenPojected[order[0]].y,
                        screenPojected[order[1]].y, y,
                        screenPojected[order[0]].x, screenPojected[order[1]].x);
                QMath.interpolateLinear(verticeDesde, alfa, vt[order[0]], vt[order[1]]);
            } else {
                // Segunda mitad
                if (vt[order[1]].location.y != vt[order[2]].location.y) {
                    zDesde = interpolateZbyY(vt[order[1]].location.y, vt[order[1]].location.z,
                            vt[order[2]].location.y, vt[order[2]].location.z, y,
                            (int) render.getFrameBuffer().getAlto(), render.getCamara().camaraAlto);
                } else {
                    zDesde = vt[order[1]].location.z;
                }
                if (!testDifference(vt[order[1]].location.z, vt[order[2]].location.z)) {
                    alfa = (zDesde - vt[order[1]].location.z)
                            / (vt[order[2]].location.z - vt[order[1]].location.z);
                } else {
                    alfa = screenPojected[order[1]].y == screenPojected[order[2]].y ? 0
                            : (float) (y - screenPojected[order[1]].y)
                                    / (float) (screenPojected[order[2]].y - screenPojected[order[1]].y);
                }
                xDesde = QMath.interpolateLinear(alfa, vt[order[1]].location.x, vt[order[2]].location.x);
                uDesde = QMath.interpolateLinear(alfa, vt[order[1]].u, vt[order[2]].u);
                vDesde = QMath.interpolateLinear(alfa, vt[order[1]].v, vt[order[2]].v);
                xDesdePantalla = (int) QMath.interpolateLinear(screenPojected[order[1]].y,
                        screenPojected[order[2]].y, y,
                        screenPojected[order[1]].x, screenPojected[order[2]].x);
                QMath.interpolateLinear(verticeDesde, alfa, vt[order[1]], vt[order[2]]);
            }
            increment = xDesdePantalla > xHastaPantalla ? -1 : 1;

            if (xDesdePantalla != xHastaPantalla) {
                for (int x = xDesdePantalla; x != xHastaPantalla; x += increment) {
                    if (x >= render.getFrameBuffer().getAncho() && increment > 0) {
                        break;
                    } else if (x < 0 && increment < 0) {
                        break;
                    } else if (x >= render.getFrameBuffer().getAncho() && increment < 0) {
                        if (xHastaPantalla < render.getFrameBuffer().getAncho()) {
                            x = render.getFrameBuffer().getAncho();
                        } else {
                            break;
                        }
                    } else if (x < 0 && increment > 0) {
                        if (xHastaPantalla >= 0) {
                            x = -1;
                        } else {
                            break;
                        }
                    }
                    prepareFragment(poligono, x, y);// Pixeles del interior del primitiva
                }
            }
            prepareFragment(poligono, xHastaPantalla, y); // <ag> pixeles del exterior del primitiva
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
    protected void prepareFragment(QPrimitiva poly, int x, int y) {
        if (x > 0 && x < render.getFrameBuffer().getAncho() && y > 0 && y < render.getFrameBuffer().getAlto()) {

            zActual = interpolateZbyX(xDesde, zDesde, xHasta, zHasta, x, (int) render.getFrameBuffer().getAncho(),
                    render.getCamara().camaraAncho);
            if (!testDifference(zDesde, zHasta)) {
                alfa = (zActual - zDesde) / (zHasta - zDesde);
            } else {
                alfa = xDesdePantalla == xHastaPantalla ? 0
                        : (float) (x - xDesdePantalla) / (float) (xHastaPantalla - xDesdePantalla);
            }
            // siempre y cuando sea menor que el zbuffer se debe dibujar. quiere decir que
            // esta delante
            if ((zActual > render.getFrameBuffer().getZBuffer(x, y))) {
                QMath.interpolateLinear(verticeActual, alfa, verticeDesde, verticeHasta);
                // si no es suavizado se copia la normal de la cara para dibujar con Flat
                // Shadded
                // igualmente si es tipo wire toma la normal de la cara porq no hay normal
                // interpolada

                if (poly instanceof Poly) {
                    if (poly.geometria.tipo == Mesh.GEOMETRY_TYPE_WIRE
                            || !(((Poly) poly).isSmooth()
                                    && (render.opciones.getTipoVista() >= QOpcionesRenderer.VISTA_PHONG)
                                    || render.opciones.isForzarSuavizado())) {
                        verticeActual.normal.set(((Poly) poly).getNormalCopy());
                    }
                }

                if (render.opciones.isMaterial()) {
                    // // mapa de desplazamiento
                    // if (primitiva.material != null && (primitiva.material instanceof QMaterialBas
                    // && ((QMaterialBas) primitiva.material).getMapaDesplazamiento() != null) // si
                    // tiene material
                    // // basico y tiene
                    // // mapa por
                    // // desplazamiento
                    // ) {
                    // QMaterialBas material = (QMaterialBas) primitiva.material;
                    // QColor colorDesplazamiento =
                    // material.getMapaDesplazamiento().get_QARGB(verticeActual.u,
                    // verticeActual.v);
                    // float fac = (colorDesplazamiento.r + colorDesplazamiento.g +
                    // colorDesplazamiento.b) / 3.0f;
                    // QVector3 tmp = QVector3.of(verticeActual);
                    // tmp.add(verticeActual.normal.clone().multiply(fac * 2 - 1));
                    // verticeActual.setXYZ(tmp.x, tmp.y, tmp.z);
                    // }

                    // Mapa de normales
                    if (render.opciones.isNormalMapping() && poly.material != null
                            && (poly.material instanceof QMaterialBas
                                    && ((QMaterialBas) poly.material).getMapaNormal() != null) // si tiene material
                                                                                               // basico y tiene
                                                                                               // mapa normal
                    ) {
                        QMaterialBas material = (QMaterialBas) poly.material;
                        currentUp.set(up);
                        currentRight.set(right);
                        // usando el metodo arriba e izquierda
                        currentUp.multiply(
                                (material.getMapaNormal().getNormalY(verticeActual.u, verticeActual.v) * 2 - 1)
                                        * material.getFactorNormal());
                        currentRight.multiply(
                                (material.getMapaNormal().getNormalX(verticeActual.u, verticeActual.v) * 2 - 1)
                                        * material.getFactorNormal());
                        // continua ejecucion normal
                        verticeActual.normal.multiply(
                                material.getMapaNormal().getNormalZ(verticeActual.u, verticeActual.v) * 2 - 1);
                        verticeActual.normal.add(currentUp, currentRight);
                        verticeActual.normal.normalize();
                    }
                    // si tiene material nodo y tiene mapa normal
                    if (poly.material != null && (poly.material instanceof MaterialNode)
                            && render.opciones.isNormalMapping()) {
                        verticeActual.up.set(up);
                        verticeActual.right.set(right);
                    }
                }

                // panelclip
                try {
                    if (render.getPanelClip() != null) {
                        if (!render.getPanelClip()
                                .esVisible(TransformationVectorUtil.transformarVector(
                                        TransformationVectorUtil.transformarVectorInversa(verticeActual.location,
                                                poly.geometria.entity, render.getCamara()),
                                        poly.geometria.entity))) {
                            return;
                        }
                    }
                } catch (Exception e) {
                }

                // actualiza le buffer
                if (render.getFrameBuffer().getPixel(x, y) != null) {
                    render.getFrameBuffer().getPixel(x, y).setDibujar(true);
                    render.getFrameBuffer().getPixel(x, y).ubicacion.set(verticeActual.location);
                    render.getFrameBuffer().getPixel(x, y).normal.set(verticeActual.normal);
                    render.getFrameBuffer().getPixel(x, y).material = poly.material;
                    render.getFrameBuffer().getPixel(x, y).primitiva = poly;
                    render.getFrameBuffer().getPixel(x, y).u = verticeActual.u;
                    render.getFrameBuffer().getPixel(x, y).v = verticeActual.v;
                    render.getFrameBuffer().getPixel(x, y).entity = poly.geometria.entity;
                    render.getFrameBuffer().getPixel(x, y).up.set(verticeActual.up);
                    render.getFrameBuffer().getPixel(x, y).right.set(verticeActual.right);
                    if (!render.opciones.isDefferedShadding())
                        render.getFrameBuffer().setQColor(x, y,
                                render.getShader().shadeFragment(render.getFrameBuffer().getPixel(x, y), x, y));
                }
                // actualiza el zBuffer
                render.getFrameBuffer().setZBuffer(x, y, zActual);
            }
        }
    }

    protected void calcularArriba(QVector3 upVector, Vertex v1, Vertex v2, Vertex v3) {
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
    public void lineaBresenham(QPrimitiva primitiva, int x0, int y0, int x1, int y1) {
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

        zHasta = interpolateZbyY(
                vt[order[0]].location.y,
                vt[order[0]].location.z,
                vt[order[1]].location.y,
                vt[order[1]].location.z,
                y,
                (int) render.getFrameBuffer().getAlto(),
                render.getCamara().camaraAlto);

        zDesde = interpolateZbyY(vt[order[0]].location.y,
                vt[order[0]].location.z,
                vt[order[1]].location.y,
                vt[order[1]].location.z,
                y,
                (int) render.getFrameBuffer().getAlto(),
                render.getCamara().camaraAlto);
        xHasta = QMath.interpolateLinear(dx, vt[order[0]].location.x, vt[order[1]].location.x);

        prepareFragment(primitiva, x, y);

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

                zHasta = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z,
                        vt[order[1]].location.y,
                        vt[order[1]].location.z, y, (int) render.getFrameBuffer().getAlto(),
                        render.getCamara().camaraAlto);
                zDesde = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z,
                        vt[order[1]].location.y,
                        vt[order[1]].location.z, y, (int) render.getFrameBuffer().getAlto(),
                        render.getCamara().camaraAlto);
                xHasta = QMath.interpolateLinear(dx, vt[order[0]].location.x,
                        vt[order[1]].location.x);
                prepareFragment(primitiva, x, y);
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

                zHasta = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z,
                        vt[order[2]].location.y,
                        vt[order[2]].location.z, y, (int) render.getFrameBuffer().getAlto(),
                        render.getCamara().camaraAlto);
                xHasta = QMath.interpolateLinear(dx, vt[order[0]].location.x,
                        vt[order[2]].location.x);
                zDesde = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z,
                        vt[order[1]].location.y,
                        vt[order[1]].location.z, y, (int) render.getFrameBuffer().getAlto(),
                        render.getCamara().camaraAlto);
                prepareFragment(primitiva, x, y);
            }
        }
    }

    protected float interpolateZbyX(float x1, float z1, float x2, float z2, int xS, int wS, float w) {
        if (testDifference(z1, z2)) {
            return z1;
        }
        tempFloat = (x1 - z1 * (x2 - x1) / (z2 - z1)) / (-(x2 - x1) / (z2 - z1) + (.5f - (float) xS / (float) wS) * w);
        if ((tempFloat - z1) / (z2 - z1) > 1) {
            return z2;
        }
        if ((tempFloat - z1) / (z2 - z1) < 0) {
            return z1;
        }
        return tempFloat;
    }

    protected float interpolateZbyY(float y1, float z1, float y2, float z2, int yS, int hS, float h) {
        if (testDifference(z1, z2)) {
            return z1;
        }
        tempFloat = (z1 * (y2 - y1) / (z2 - z1) - y1) / ((y2 - y1) / (z2 - z1) + ((.5f - (float) yS / (float) hS) * h));
        if ((tempFloat - z1) / (z2 - z1) > 1) {
            return z2;
        }
        if ((tempFloat - z1) / (z2 - z1) < 0) {
            return z1;
        }
        return tempFloat;
    }

    protected boolean testDifference(float n1, float n2) {
        return n1 - n2 > -INTERPOLATION_CLAMP && n1 - n2 < INTERPOLATION_CLAMP;
    }

    private void projectVertices(Vertex v1, Vertex v2, Vertex v3) {
        // obtenemos los puntos proyectados en la pantalla
        projectVectors(v1 != null ? v1.location : null, v2 != null ? v2.location : null,
                v3 != null ? v3.location : null);
    }

    private void projectVectors(QVector4 v1, QVector4 v2, QVector4 v3) {
        // obtenemos los puntos proyectados en la pantalla
        try {
            if (v1 != null) {
                render.getCamara().getCoordenadasPantalla(screenPojected[0], v1,
                        render.getFrameBuffer().getAncho(), render.getFrameBuffer().getAlto());
            }

            if (v2 != null) {
                render.getCamara().getCoordenadasPantalla(screenPojected[1], v2,
                        render.getFrameBuffer().getAncho(), render.getFrameBuffer().getAlto());
            }

            if (v3 != null) {
                render.getCamara().getCoordenadasPantalla(screenPojected[2], v3,
                        render.getFrameBuffer().getAncho(), render.getFrameBuffer().getAlto());
            }
        } catch (Exception e) {

        }
    }

}
