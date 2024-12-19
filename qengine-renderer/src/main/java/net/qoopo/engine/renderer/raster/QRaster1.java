/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.raster;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.QLinea;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPoligono;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.mesh.primitive.QVertex;
import net.qoopo.engine.core.entity.component.transform.QVertexBuffer;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.material.node.MaterialNode;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.math.QVector4;
import net.qoopo.engine.core.renderer.QOpcionesRenderer;
import net.qoopo.engine.renderer.SoftwareRenderer;
import net.qoopo.engine.renderer.transformacion.QTransformar;

/**
 * Realiza la rasterización de los polígonos
 *
 * @author alberto
 */
public class QRaster1 extends AbstractRaster {

    protected SoftwareRenderer render;
    protected QVector3 toCenter = QVector3.empty();
    protected QVertex[] vt = new QVertex[3]; //vertices transformados
    protected QVertex verticeTmp = new QVertex();
    protected float alfa;//factor de interpolación

    protected QVector2[] puntoXY = new QVector2[3]; // puntos proyectados
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
    protected QVertex verticeDesde = new QVertex();
    protected QVertex verticeHasta = new QVertex();
    protected QVertex verticeActual = new QVertex();
    protected QVector3 up = QVector3.empty();
    protected QVector3 right = QVector3.empty();
    protected QVector3 currentUp = QVector3.empty();
    protected QVector3 currentRight = QVector3.empty();
    protected float tempFloat, vYLength, vXLength, coefficient1, coefficient2;

    public QRaster1(SoftwareRenderer render) {
        this.render = render;
        for (int i = 0; i < 3; i++) {
            puntoXY[i] = new QVector2();
        }
    }

    /**
     *
     *
     * @param primitiva
     * @param p1
     * @param p2
     */
    /**
     *
     * @param primitiva
     * @param p1
     * @param p2
     */
    @Override
    public void raster(QPrimitiva primitiva, QVector4 p1, QVector4 p2) {
        vt[0] = new QVertex(p1.x, p1.y, p1.z, p1.w);
        vt[1] = new QVertex(p2.x, p2.y, p2.z, p1.w);
        vt[2] = new QVertex(p2.x, p2.y, p2.z, p1.w);

        // si uno de los 2 vertices no esta en el campo de vision, se interpola para obtener un vertice adecuado
        if (render.getCamara().isVisible(vt[0]) && !render.getCamara().isVisible(vt[1])) {
            alfa = render.getCamara().obtenerClipedVerticeAlfa(vt[0].location.getVector3(), vt[1].location.getVector3());
            verticeTmp = new QVertex();
            QMath.linear(verticeTmp, alfa, vt[0], vt[1]);
            p2 = verticeTmp.location;
        } else if (!render.getCamara().isVisible(vt[0]) && render.getCamara().isVisible(vt[1])) {
            alfa = render.getCamara().obtenerClipedVerticeAlfa(vt[1].location.getVector3(), vt[0].location.getVector3());
            verticeTmp = new QVertex();
            QMath.linear(verticeTmp, alfa, vt[1], vt[0]);
            p1 = verticeTmp.location;
        }
        vt[0].set(p1.x, p1.y, p1.z, p1.w);
        vt[1].set(p2.x, p2.y, p2.z, p1.w);
        vt[2].set(p2.x, p2.y, p2.z, p1.w);
        render.getCamara().getCoordenadasPantalla(puntoXY[0], p1, render.getFrameBuffer().getAncho(), render.getFrameBuffer().getAlto());
        render.getCamara().getCoordenadasPantalla(puntoXY[1], p2, render.getFrameBuffer().getAncho(), render.getFrameBuffer().getAlto());
        lineaBresenham(primitiva, (int) puntoXY[0].x, (int) puntoXY[0].y, (int) puntoXY[1].x, (int) puntoXY[1].y);
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
        if (primitiva instanceof QPoligono) {
            if (wire) {
                procesarPoligonoWIRE(bufferVertices, (QPoligono) primitiva);
            } else {
                procesarPoligono(bufferVertices, (QPoligono) primitiva);
            }
        } else if (primitiva instanceof QLinea) {
            QVertex p1 = bufferVertices.getVerticesTransformados()[primitiva.listaVertices[0]];
            QVertex p2 = bufferVertices.getVerticesTransformados()[primitiva.listaVertices[1]];
            raster(primitiva, p1.location, p2.location);
        }
    }

    /**
     * Realiza un proceso de un polígono donde solo dibuja las aristas
     *
     * @param bufferVertices
     * @param poligono
     */
    protected void procesarPoligonoWIRE(QVertexBuffer bufferVertices, QPoligono poligono) {
        if (poligono.listaVertices.length >= 3) {
            toCenter.set(poligono.getCenterCopy().location.getVector3());

            //validación caras traseras
            //si el objeto es tipo wire se dibuja igual sus caras traseras
            // si el objeto tiene transparencia (con material básico) igual dibuja sus caras traseras
            if ((!(poligono.material instanceof QMaterialBas) || ((poligono.material instanceof QMaterialBas) && !((QMaterialBas) poligono.material).isTransparencia()))
                    && poligono.geometria.tipo != Mesh.GEOMETRY_TYPE_WIRE
                    && !render.opciones.isDibujarCarasTraseras() && toCenter.dot(poligono.getNormalCopy()) > 0) {
                render.poligonosDibujadosTemp--;
                return; // salta el dibujo de caras traseras
            }

            clipping(render.getCamara(), poligono, bufferVertices.getVerticesTransformados());

            // Rasterizacion (dibujo de los puntos del plano) 
            for (int i = 1; i < verticesClipped.size() - 1; i++) {
                vt[0] = verticesClipped.get(0);
                vt[1] = verticesClipped.get(i);
                vt[2] = verticesClipped.get(i + 1);

                if (!render.getCamara().isVisible(vt[0]) && !render.getCamara().isVisible(vt[1]) && !render.getCamara().isVisible(vt[2])) {
                    continue;
                }
                render.getCamara().getCoordenadasPantalla(puntoXY[0], vt[0].location, render.getFrameBuffer().getAncho(), render.getFrameBuffer().getAlto());
                render.getCamara().getCoordenadasPantalla(puntoXY[1], vt[1].location, render.getFrameBuffer().getAncho(), render.getFrameBuffer().getAlto());
                render.getCamara().getCoordenadasPantalla(puntoXY[2], vt[2].location, render.getFrameBuffer().getAncho(), render.getFrameBuffer().getAlto());

                if ((puntoXY[0].x < 0 && puntoXY[1].x < 0 && puntoXY[2].x < 0)
                        || (puntoXY[0].y < 0 && puntoXY[1].y < 0 && puntoXY[2].y < 0)
                        || (puntoXY[0].x > render.getFrameBuffer().getAncho() && puntoXY[1].x > render.getFrameBuffer().getAncho()
                        && puntoXY[2].x > render.getFrameBuffer().getAncho())
                        || (puntoXY[0].y > render.getFrameBuffer().getAlto() && puntoXY[1].y > render.getFrameBuffer().getAlto()
                        && puntoXY[2].y > render.getFrameBuffer().getAlto())) {
                    render.poligonosDibujados--;
                    continue;
                }

                //tres lineas
                lineaBresenham(poligono, (int) puntoXY[0].x, (int) puntoXY[0].y, (int) puntoXY[1].x, (int) puntoXY[1].y);
                lineaBresenham(poligono, (int) puntoXY[0].x, (int) puntoXY[0].y, (int) puntoXY[2].x, (int) puntoXY[2].y);
                lineaBresenham(poligono, (int) puntoXY[1].x, (int) puntoXY[1].y, (int) puntoXY[2].x, (int) puntoXY[2].y);
            }
        }
    }

    /**
     * Realiza el proceso de rasterización de un polígono sólido
     *
     * @param poligono
     * @param siempreTop
     * @param dibujar . SI es true, llama al método procesarPixel del shader
     */
    private void procesarPoligono(QVertexBuffer bufferVertices, QPoligono poligono) {
        try {
            synchronized (this) {
                if (poligono.listaVertices.length >= 3) {
                    toCenter.set(poligono.getCenterCopy().location.getVector3());
                    //validación caras traseras
                    //si el objeto es tipo wire se dibuja igual sus caras traseras
                    // si el objeto tiene transparencia (con material básico) igual dibuja sus caras traseras
                    if ((!(poligono.material instanceof QMaterialBas) || ((poligono.material instanceof QMaterialBas) && !((QMaterialBas) poligono.material).isTransparencia()))
                            && !render.opciones.isDibujarCarasTraseras() && toCenter.dot(poligono.getNormalCopy()) > 0) {
                        render.poligonosDibujadosTemp--;
                        return; // salta el dibujo de caras traseras
                    }

                    clipping(render.getCamara(), poligono, bufferVertices.getVerticesTransformados());

                    // Rasterizacion (dibujo de los puntos del plano)
                    //Separo en triangulos sin importar cuantos puntos tenga
                    for (int i = 1; i < verticesClipped.size() - 1; i++) {
                        vt[0] = verticesClipped.get(0);
                        vt[1] = verticesClipped.get(i);
                        vt[2] = verticesClipped.get(i + 1);
                        // si el triangulo no esta en el campo de vision, pasamos y no dibujamos
                        if (!render.getCamara().isVisible(vt[0]) && !render.getCamara().isVisible(vt[1]) && !render.getCamara().isVisible(vt[2])) {
                            continue;
                        }
                        //obtenemos los puntos proyectados en la pantalla
                        render.getCamara().getCoordenadasPantalla(puntoXY[0], vt[0].location, render.getFrameBuffer().getAncho(), render.getFrameBuffer().getAlto());
                        render.getCamara().getCoordenadasPantalla(puntoXY[1], vt[1].location, render.getFrameBuffer().getAncho(), render.getFrameBuffer().getAlto());
                        render.getCamara().getCoordenadasPantalla(puntoXY[2], vt[2].location, render.getFrameBuffer().getAncho(), render.getFrameBuffer().getAlto());

                        //valido si el punto proyectado esta dentro del rango de vision de la Camara
                        if ((puntoXY[0].x < 0 && puntoXY[1].x < 0 && puntoXY[2].x < 0)
                                || (puntoXY[0].y < 0 && puntoXY[1].y < 0 && puntoXY[2].y < 0)
                                || (puntoXY[0].x > render.getFrameBuffer().getAncho() && puntoXY[1].x > render.getFrameBuffer().getAncho() && puntoXY[2].x > render.getFrameBuffer().getAncho())
                                || (puntoXY[0].y > render.getFrameBuffer().getAlto() && puntoXY[1].y > render.getFrameBuffer().getAlto() && puntoXY[2].y > render.getFrameBuffer().getAlto())) {
                            render.poligonosDibujados--;
                            continue;
                        }

                        //mapeo normal para materiales básicos
                        if ((poligono.material instanceof QMaterialBas && ((QMaterialBas) poligono.material).getMapaNormal() != null) //                            || (primitiva.material instanceof QMaterialBas && ((QMaterialBas) primitiva.material).getMapaDesplazamiento() != null)
                                ) {
                            calcularArriba(up, vt[0], vt[1], vt[2]);
                            calcularDerecha(right, vt[0], vt[1], vt[2]);
                            up.normalize();
                            right.normalize();
                        }

                        // If primitiva is closer than clipping distance
                        order[0] = 0;
                        order[1] = 1;
                        order[2] = 2;

                        // Sort the points by ejeY coordinate. (bubble sort...derps)
                        if (puntoXY[order[0]].y > puntoXY[order[1]].y) {
                            temp = order[0];
                            order[0] = order[1];
                            order[1] = temp;
                        }
                        if (puntoXY[order[1]].y > puntoXY[order[2]].y) {
                            temp = order[1];
                            order[1] = order[2];
                            order[2] = temp;
                        }
                        if (puntoXY[order[0]].y > puntoXY[order[1]].y) {
                            temp = order[0];
                            order[0] = order[1];
                            order[1] = temp;
                        }

                        //proceso de dibujo, se deberia separar en una clase/metodo de escaneo
                        for (int y = (int) puntoXY[order[0]].y; y <= puntoXY[order[2]].y; y++) {
                            if (y > render.getFrameBuffer().getAlto()) {
                                break;
                            }
                            if (y < 0) {
                                if (puntoXY[order[2]].y > 0) {
                                    y = 0;
                                }
                            }

                            zHasta = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z, vt[order[2]].location.y, vt[order[2]].location.z, y, (int) render.getFrameBuffer().getAlto(), render.getCamara().camaraAlto);
                            xHastaPantalla = (int) QMath.linear(puntoXY[order[0]].y, puntoXY[order[2]].y, y, puntoXY[order[0]].x, puntoXY[order[2]].x);

                            if (!testDifference(vt[order[0]].location.z, vt[order[2]].location.z)) {
                                alfa = (zHasta - vt[order[0]].location.z) / (vt[order[2]].location.z - vt[order[0]].location.z);
                            } else {
                                alfa = puntoXY[order[0]].y == puntoXY[order[2]].y ? 1 : (float) (y - puntoXY[order[0]].y) / (float) (puntoXY[order[2]].y - puntoXY[order[0]].y);
                            }
                            xHasta = QMath.linear(alfa, vt[order[0]].location.x, vt[order[2]].location.x);
                            QMath.linear(verticeHasta, alfa, vt[order[0]], vt[order[2]]);
                            if (y <= puntoXY[order[1]].y) {
                                // Primera mitad
                                if (vt[order[0]].location.y != vt[order[1]].location.y) {
                                    zDesde = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z, vt[order[1]].location.y, vt[order[1]].location.z, y, (int) render.getFrameBuffer().getAlto(), render.getCamara().camaraAlto);
                                } else {
                                    zDesde = vt[order[1]].location.z;
                                }
                                if (!testDifference(vt[order[0]].location.z, vt[order[1]].location.z)) {
                                    alfa = (zDesde - vt[order[0]].location.z) / (vt[order[1]].location.z - vt[order[0]].location.z);
                                } else {
                                    alfa = puntoXY[order[0]].y == puntoXY[order[1]].y ? 0 : (float) (y - puntoXY[order[0]].y) / (float) (puntoXY[order[1]].y - puntoXY[order[0]].y);
                                }
                                xDesde = QMath.linear(alfa, vt[order[0]].location.x, vt[order[1]].location.x);
                                uDesde = QMath.linear(alfa, vt[order[0]].u, vt[order[1]].u);
                                vDesde = QMath.linear(alfa, vt[order[0]].v, vt[order[1]].v);
                                xDesdePantalla = (int) QMath.linear(puntoXY[order[0]].y, puntoXY[order[1]].y, y, puntoXY[order[0]].x, puntoXY[order[1]].x);
                                QMath.linear(verticeDesde, alfa, vt[order[0]], vt[order[1]]);
                            } else {
                                // Segunda mitad
                                if (vt[order[1]].location.y != vt[order[2]].location.y) {
                                    zDesde = interpolateZbyY(vt[order[1]].location.y, vt[order[1]].location.z, vt[order[2]].location.y, vt[order[2]].location.z, y, (int) render.getFrameBuffer().getAlto(), render.getCamara().camaraAlto);
                                } else {
                                    zDesde = vt[order[1]].location.z;
                                }
                                if (!testDifference(vt[order[1]].location.z, vt[order[2]].location.z)) {
                                    alfa = (zDesde - vt[order[1]].location.z) / (vt[order[2]].location.z - vt[order[1]].location.z);
                                } else {
                                    alfa = puntoXY[order[1]].y == puntoXY[order[2]].y ? 0 : (float) (y - puntoXY[order[1]].y) / (float) (puntoXY[order[2]].y - puntoXY[order[1]].y);
                                }
                                xDesde = QMath.linear(alfa, vt[order[1]].location.x, vt[order[2]].location.x);
                                uDesde = QMath.linear(alfa, vt[order[1]].u, vt[order[2]].u);
                                vDesde = QMath.linear(alfa, vt[order[1]].v, vt[order[2]].v);
                                xDesdePantalla = (int) QMath.linear(puntoXY[order[1]].y, puntoXY[order[2]].y, y, puntoXY[order[1]].x, puntoXY[order[2]].x);
                                QMath.linear(verticeDesde, alfa, vt[order[1]], vt[order[2]]);
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
                                    prepararPixel(poligono, x, y);//Pixeles del interior del primitiva                          
                                }
                            }
                            prepararPixel(poligono, xHastaPantalla, y); //<ag> pixeles del exterior del primitiva
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * Prepara el pixel - Realiza la validacion de profundidad y setea los
     * valores a usar en el shader como material, entity, etc.
     *
     * @param primitiva
     * @param x
     * @param y
     */
    protected void prepararPixel(QPrimitiva primitiva, int x, int y) {
        if (x > 0 && x < render.getFrameBuffer().getAncho() && y > 0 && y < render.getFrameBuffer().getAlto()) {

            zActual = interpolateZbyX(xDesde, zDesde, xHasta, zHasta, x, (int) render.getFrameBuffer().getAncho(), render.getCamara().camaraAncho);
            if (!testDifference(zDesde, zHasta)) {
                alfa = (zActual - zDesde) / (zHasta - zDesde);
            } else {
                alfa = xDesdePantalla == xHastaPantalla ? 0 : (float) (x - xDesdePantalla) / (float) (xHastaPantalla - xDesdePantalla);
            }
            // siempre y cuando sea menor que el zbuffer se debe dibujar. quiere decir que esta delante
            if ((-zActual > 0 && -zActual < render.getFrameBuffer().getZBuffer(x, y))) {
                QMath.linear(verticeActual, alfa, verticeDesde, verticeHasta);
                // si no es suavizado se copia la normal de la cara para dibujar con Flat Shadded
                // igualmente si es tipo wire toma la normal de la cara porq no hay normal interpolada

                if (primitiva instanceof QPoligono) {
                    if (primitiva.geometria.tipo == Mesh.GEOMETRY_TYPE_WIRE
                            || !(((QPoligono) primitiva).isSmooth() && (render.opciones.getTipoVista() >= QOpcionesRenderer.VISTA_PHONG) || render.opciones.isForzarSuavizado())) {
                        verticeActual.normal.set(((QPoligono) primitiva).getNormalCopy());
                    }
                }

                if (render.opciones.isMaterial()) {
                    //mapa de desplazamiento
                    if (primitiva.material != null && (primitiva.material instanceof QMaterialBas && ((QMaterialBas) primitiva.material).getMapaDesplazamiento() != null) //si tiene material basico y tiene mapa por desplazamiento
                            ) {
                        QMaterialBas material = (QMaterialBas) primitiva.material;
                        QColor colorDesplazamiento = material.getMapaDesplazamiento().get_QARGB(verticeActual.u, verticeActual.v);
                        float fac = (colorDesplazamiento.r + colorDesplazamiento.g + colorDesplazamiento.b) / 3.0f;
                        QVector3 tmp = QVector3.of(verticeActual);
                        tmp.add(verticeActual.normal.clone().multiply(fac * 2 - 1));
                        verticeActual.setXYZ(tmp.x, tmp.y, tmp.z);
                    }

                    //Mapa de normales
                    if (render.opciones.isNormalMapping() && primitiva.material != null && (primitiva.material instanceof QMaterialBas && ((QMaterialBas) primitiva.material).getMapaNormal() != null) //si tiene material basico y tiene mapa normal
                            ) {
                        QMaterialBas material = (QMaterialBas) primitiva.material;
                        currentUp.set(up);
                        currentRight.set(right);
                        //usando el metodo arriba e izquierda
                        currentUp.multiply((material.getMapaNormal().getNormalY(verticeActual.u, verticeActual.v) * 2 - 1) * material.getFactorNormal());
                        currentRight.multiply((material.getMapaNormal().getNormalX(verticeActual.u, verticeActual.v) * 2 - 1) * material.getFactorNormal());
                        // continua ejecucion normal
                        verticeActual.normal.multiply(material.getMapaNormal().getNormalZ(verticeActual.u, verticeActual.v) * 2 - 1);
                        verticeActual.normal.add(currentUp, currentRight);
                        verticeActual.normal.normalize();
                    }
                    //si tiene material nodo y tiene mapa normal
                    if (primitiva.material != null && (primitiva.material instanceof MaterialNode) && render.opciones.isNormalMapping()) {
                        verticeActual.up.set(up);
                        verticeActual.right.set(right);
                    }
                }

                //panelclip
                try {
                    if (render.getPanelClip() != null) {
                        if (!render.getPanelClip().esVisible(QTransformar.transformarVector(QTransformar.transformarVectorInversa(verticeActual.location, primitiva.geometria.entity, render.getCamara()), primitiva.geometria.entity))) {
                            return;
                        }
                    }
                } catch (Exception e) {
                }

                //actualiza le buffer 
                if (render.getFrameBuffer().getPixel(x, y) != null) {
                    render.getFrameBuffer().getPixel(x, y).setDibujar(true);
                    render.getFrameBuffer().getPixel(x, y).ubicacion.set(verticeActual.location);
                    render.getFrameBuffer().getPixel(x, y).normal.set(verticeActual.normal);
                    render.getFrameBuffer().getPixel(x, y).material = primitiva.material;
                    render.getFrameBuffer().getPixel(x, y).primitiva = primitiva;
                    render.getFrameBuffer().getPixel(x, y).u = verticeActual.u;
                    render.getFrameBuffer().getPixel(x, y).v = verticeActual.v;
                    render.getFrameBuffer().getPixel(x, y).entity = primitiva.geometria.entity;
                    render.getFrameBuffer().getPixel(x, y).arriba.set(verticeActual.up);
                    render.getFrameBuffer().getPixel(x, y).derecha.set(verticeActual.right);
                    render.getFrameBuffer().setQColor(x, y, render.getShader().colorearPixel(render.getFrameBuffer().getPixel(x, y), x, y));
                }
                //actualiza el zBuffer
                render.getFrameBuffer().setZBuffer(x, y, -zActual);
            }
        }
    }

    protected void calcularArriba(QVector3 upVector, QVertex v1, QVertex v2, QVertex v3) {
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

    protected void calcularDerecha(QVector3 rightVector, QVertex v1, QVertex v2, QVertex v3) {
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

        zHasta = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z, vt[order[2]].location.y, vt[order[2]].location.z, y, (int) render.getFrameBuffer().getAlto(), render.getCamara().camaraAlto);
        xHasta = QMath.linear(dx, vt[order[0]].location.x, vt[order[2]].location.x);
        zDesde = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z, vt[order[1]].location.y, vt[order[1]].location.z, y, (int) render.getFrameBuffer().getAlto(), render.getCamara().camaraAlto);

        prepararPixel(primitiva, x, y);

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

                zHasta = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z, vt[order[2]].location.y, vt[order[2]].location.z, y, (int) render.getFrameBuffer().getAlto(), render.getCamara().camaraAlto);
                xHasta = QMath.linear(dx, vt[order[0]].location.x, vt[order[2]].location.x);
                zDesde = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z, vt[order[1]].location.y, vt[order[1]].location.z, y, (int) render.getFrameBuffer().getAlto(), render.getCamara().camaraAlto);
                prepararPixel(primitiva, x, y);
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

                zHasta = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z, vt[order[2]].location.y, vt[order[2]].location.z, y, (int) render.getFrameBuffer().getAlto(), render.getCamara().camaraAlto);
                xHasta = QMath.linear(dx, vt[order[0]].location.x, vt[order[2]].location.x);
                zDesde = interpolateZbyY(vt[order[0]].location.y, vt[order[0]].location.z, vt[order[1]].location.y, vt[order[1]].location.z, y, (int) render.getFrameBuffer().getAlto(), render.getCamara().camaraAlto);
                prepararPixel(primitiva, x, y);
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

}
