/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shadow.raster;

import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.renderer.QOpcionesRenderer;
import net.qoopo.engine.renderer.SoftwareRenderer;
import net.qoopo.engine.renderer.raster.Raster;
import net.qoopo.engine.renderer.util.TransformationVectorUtil;

/**
 * Realiza la rasterización de los polígonos para el mapeo de sombras.
 * Sobrecarga el medotod prepareFragment donde no se valida textures ni colores
 *
 * @author alberto
 */
public class ShadowRaster extends Raster {

    public ShadowRaster(SoftwareRenderer render) {
        super(render);
    }

    @Override
    protected void prepareFragment(QPrimitiva face, int x, int y) {
        if (x > 0 && x < render.getFrameBuffer().getAncho() && y > 0 && y < render.getFrameBuffer().getAlto()) {
            zActual = interpolateZbyX(xDesde, zDesde, xHasta, zHasta, x, (int) render.getFrameBuffer().getAncho(),
                    render.getCamara().camaraAncho);

            // este método solo trabaja con materiales Básicos
            QMaterialBas material = null;
            if (face.material instanceof QMaterialBas) {
                material = (QMaterialBas) face.material;
            } else {
                material = new QMaterialBas();
            }

            if (!testDifference(zDesde, zHasta)) {
                alfa = (zActual - zDesde) / (zHasta - zDesde);
            } else {
                alfa = xDesdePantalla == xHastaPantalla ? 0
                        : (float) (x - xDesdePantalla) / (float) (xHastaPantalla - xDesdePantalla);
            }
            // siempre y cuando sea menor que el depthbuffer se debe dibujar. quiere decir
            // qu esta delante
            if (-zActual > 0 && -zActual < render.getFrameBuffer().getZBuffer(x, y)) {
                QMath.interpolateLinear(verticeActual, alfa, verticeDesde, verticeHasta);
                // si no es suavizado se copia la normal de la cara para dibujar con Flat
                // Shadded
                if (face instanceof Poly) {
                    if (!(((Poly) face).isSmooth()
                            && (render.opciones.getTipoVista() >= QOpcionesRenderer.VISTA_PHONG)
                            || render.opciones.isForzarSuavizado())) {
                        verticeActual.normal.set(((Poly) face).getNormalCopy());
                    }
                }

                // Mapeo de normales
                if (render.opciones.isMaterial()) {
                    if (material.getMapaNormal() != null && render.opciones.isNormalMapping()) {
                        int rgb = material.getMapaNormal().get_ARGB(verticeActual.u, verticeActual.v);
                        currentUp.set(up);
                        currentRight.set(right);
                        currentUp.multiply((((rgb >> 8) & 0xFF) / 255.0f - .5f) * material.getFactorNormal());
                        currentRight.multiply((((rgb >> 16) & 0xFF) / 255.0f - .5f) * material.getFactorNormal());
                        verticeActual.normal
                                .multiply(material.getMapaNormal().getNormalZ(verticeActual.u, verticeActual.v) - .5f);
                        verticeActual.normal.add(currentUp, currentRight);
                        verticeActual.normal.normalize();
                    }
                }

                // panelclip
                try {
                    if (render.getPanelClip() != null) {
                        if (!render.getPanelClip().esVisible(TransformationVectorUtil.transformarVectorInversa(
                                verticeActual.location.getVector3(), face.geometria.entity, render.getCamara()))) {
                            return;
                        }
                    }
                } catch (Exception e) {
                }
                try {
                    float alpha = 1;

                    // TOMA EL VALOR DE LA TRANSPARENCIA
                    if (material.isTransparencia()) {
                        // si tiene un mapa de transparencia
                        if (material.getMapaTransparencia() != null) {
                            // es una imagen en blanco y negro, toma cualquier canal de color
                            alpha = material.getMapaTransparencia().get_QARGB(verticeActual.u, verticeActual.v).r;
                        } else {
                            // toma el valor de transparencia del material
                            alpha = material.getTransAlfa();
                        }
                    } else {
                        alpha = 1;
                    }

                    // proceso a validar si la textura es transparente para no guardar su mapa de
                    // profundidad
                    boolean pixelTransparente = false;
                    boolean pixelTransparente2 = false;
                    QColor pixelColor;
                    if (!material.getMapaColor().isProyectada()) {
                        pixelColor = material.getMapaColor().get_QARGB(verticeActual.u, verticeActual.v);
                    } else {
                        pixelColor = material.getMapaColor().get_QARGB(
                                (float) x / (float) render.getFrameBuffer().getAncho(),
                                -(float) y / (float) render.getFrameBuffer().getAlto());
                    }

                    pixelTransparente2 = material.isTransparencia() && material.getColorTransparente() != null
                            && pixelColor.toRGB() == material.getColorTransparente().toRGB();
                    // solo activa la transparencia si tiene el canal alfa y el color es negro (el
                    // negro es el color transparente)
                    pixelTransparente = alpha < 1.0f || pixelColor.a < 1 || pixelTransparente2;// transparencia imagenes
                                                                                               // png
                    if (pixelTransparente) {
                        return;
                    }
                } catch (Exception e) {
                }
                if (render.getFrameBuffer().getPixel(x, y) != null) {
                    render.getFrameBuffer().getPixel(x, y).setDibujar(true);
                    render.getFrameBuffer().getPixel(x, y).ubicacion.set(verticeActual.location);
                    render.getFrameBuffer().getPixel(x, y).normal.set(verticeActual.normal);
                    render.getFrameBuffer().getPixel(x, y).material = face.material;
                    render.getFrameBuffer().getPixel(x, y).primitiva = face;
                    render.getFrameBuffer().getPixel(x, y).u = verticeActual.u;
                    render.getFrameBuffer().getPixel(x, y).v = verticeActual.v;
                    render.getFrameBuffer().getPixel(x, y).entity = face.geometria.entity;
                }
                render.getFrameBuffer().setZBuffer(x, y, -zActual);
            }
        }
    }
}
