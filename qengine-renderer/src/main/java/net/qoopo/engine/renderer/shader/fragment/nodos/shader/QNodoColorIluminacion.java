/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.nodos.shader;

import java.util.ArrayList;

import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QIluminacion;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.material.node.core.ShaderNode;
import net.qoopo.engine.core.material.node.core.perifericos.QPerColor;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.shadow.QProcesadorSombra;
import net.qoopo.engine.core.util.TempVars;
import net.qoopo.engine.renderer.util.TransformationVectorUtil;

/**
 *
 * @author alberto
 */
public class QNodoColorIluminacion extends ShaderNode {

    private QPerColor enColor;
    private QPerColor enNormal;
    private QPerColor saColor;
    // ------------------ variables internas

    private float distanciaLuz;
    private final QVector3 tmpPixelPos = QVector3.empty();
    private final QVector3 vectorLuz = QVector3.empty();
    private final QVector3 tempVector = QVector3.empty();

    private final QIluminacion iluminacion = new QIluminacion();
    private final QColor colorEspecular = QColor.WHITE.clone();

    private QColor color;

    public QNodoColorIluminacion() {
        enColor = new QPerColor(QColor.WHITE);
        enColor.setNodo(this);
        enNormal = new QPerColor(QColor.BLACK);
        enNormal.setNodo(this);
        saColor = new QPerColor(QColor.WHITE);
        saColor.setNodo(this);
        entradas = new ArrayList<>();

        entradas.add(enColor);
        entradas.add(enNormal);
        salidas = new ArrayList<>();
        salidas.add(saColor);
    }

    public QNodoColorIluminacion(QColor color) {
        enColor = new QPerColor(color);
        enNormal = new QPerColor(QColor.BLACK);
        saColor = new QPerColor(QColor.WHITE);
        enColor.setNodo(this);
        enNormal.setNodo(this);
        saColor.setNodo(this);

        entradas = new ArrayList<>();
        entradas.add(enColor);
        entradas.add(enNormal);
        salidas = new ArrayList<>();
        salidas.add(saColor);
    }

    @Override
    public void procesar(RenderEngine render, Fragment pixel) {
        // toma el color de entrada

        if (render.opciones.isMaterial()) {
            enColor.procesarEnlaces(render, pixel);
            color = enColor.getColor().clone();

            if (render.opciones.isNormalMapping() && enNormal.tieneEnlaces()) {
                enNormal.procesarEnlaces(render, pixel);
                // cambio la direccion de la normal del pixel de acuerdo a la normal de entrada
                // (mapa de normales)
                QColor tmp = enNormal.getColor();
                pixel.up.multiply((tmp.g * 2 - 1));
                pixel.right.multiply((tmp.r * 2 - 1));
                pixel.normal.multiply(tmp.b * 2 - 1);
                pixel.normal.add(pixel.up, pixel.right);
                pixel.normal.normalize();
            }

            // paso 1 calcular iluminación phong
            calcularIluminacion(render, iluminacion, pixel);
            //

            color.scale(iluminacion.getColorAmbiente());

            color.addLocal(iluminacion.getColorLuz());
            saColor.setColor(color);
        }

    }

    private void calcularIluminacion(RenderEngine render, QIluminacion iluminacion, Fragment pixel) {
        pixel.normal.normalize();

        // inicia con la luz ambiente ambiente
        iluminacion.setColorAmbiente(render.getScene().getAmbientColor().clone());

        TempVars tv = TempVars.get();
        try {
            iluminacion.setColorLuz(QColor.BLACK.clone());
            float factorSombra = 1;// 1= no sombra
            float reflectancia = 1.0f;

            // solo si hay luces y si las opciones de la vista tiene activado el material
            if (render.opciones.isMaterial() && !render.getLitgths().isEmpty()) {
                for (QLigth luz : render.getLitgths()) {
                    // si esta encendida
                    if (luz != null && luz.getEntity().isToRender() && luz.isEnable()) {
                        factorSombra = 1;
                        QProcesadorSombra proc = luz.getSombras();
                        if (proc != null && render.opciones.isSombras()) {
                            tv.vector3f1.set(pixel.ubicacion.getVector3());
                            factorSombra = proc.factorSombra(TransformationVectorUtil.transformarVectorInversa(tv.vector3f1,
                                    pixel.entity, render.getCamera()), pixel.entity);
                        }

                        if (luz instanceof QPointLigth || luz instanceof QSpotLigth) {
                            vectorLuz.set(pixel.ubicacion.x - luz.getEntity().getTransform().getLocation().x,
                                    pixel.ubicacion.y - luz.getEntity().getTransform().getLocation().y,
                                    pixel.ubicacion.z - luz.getEntity().getTransform().getLocation().z);
                            // solo toma en cuenta a los puntos q estan en el area de afectacion
                            if (vectorLuz.length() > luz.radio) {
                                continue;
                            }

                            // si es Spot valido que este dentro del cono
                            if (luz instanceof QSpotLigth) {
                                QVector3 coneDirection = ((QSpotLigth) luz).getDirection().clone().normalize();
                                tv.vector3f2.set(vectorLuz);
                                if (coneDirection.angulo(tv.vector3f2.normalize()) > ((QSpotLigth) luz)
                                        .getAnguloExterno()) {
                                    continue;
                                }
                            }
                            distanciaLuz = vectorLuz.x * vectorLuz.x + vectorLuz.y * vectorLuz.y
                                    + vectorLuz.z * vectorLuz.z;
                            QColor colorLuz = QMath.calcularColorLuz(color.clone(), colorEspecular.clone(), luz.color,
                                    luz.energia * factorSombra, pixel.ubicacion.getVector3(),
                                    vectorLuz.normalize().invert(), pixel.normal, 50, reflectancia);
                            colorLuz.scaleLocal(1.0f / distanciaLuz);
                            iluminacion.getColorLuz().addLocal(colorLuz);
                        } else if (luz instanceof QDirectionalLigth) {
                            vectorLuz.set(((QDirectionalLigth) luz).getDirection());
                            iluminacion.getColorLuz()
                                    .addLocal(QMath.calcularColorLuz(color, colorEspecular, luz.color,
                                            luz.energia * factorSombra, pixel.ubicacion.getVector3(),
                                            vectorLuz.normalize().invert(), pixel.normal, 50, reflectancia));
                        }
                    }
                }
            } else {
                // iluminacion default cuando no hay luces se asume una luz central
                tmpPixelPos.set(pixel.ubicacion.getVector3());
                tmpPixelPos.normalize();
                iluminacion.getColorAmbiente().add(-tmpPixelPos.dot(pixel.normal));
            }
        } finally {
            tv.release();
        }
    }

    public QPerColor getEnColor() {
        return enColor;
    }

    public void setEnColor(QPerColor enColor) {
        this.enColor = enColor;
    }

    public QPerColor getSaColor() {
        return saColor;
    }

    public void setSaColor(QPerColor saColor) {
        this.saColor = saColor;
    }

    public QPerColor getEnNormal() {
        return enNormal;
    }

    public void setEnNormal(QPerColor enNormal) {
        this.enNormal = enNormal;
    }

}
