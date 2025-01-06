/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.basico.parciales;

import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QIluminacion;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.shadow.QProcesadorSombra;
import net.qoopo.engine.core.texture.QTexturaUtil;
import net.qoopo.engine.core.texture.procesador.QProcesadorMipMap;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.TempVars;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;
import net.qoopo.engine.renderer.util.TransformationVectorUtil;

/**
 * Calcula el color e iluminación de cada pixel, calcula la reflexion y
 * refraccion, iluminacion de entorno sombras, textura y sombreado de phong
 *
 * @author alberto
 */
public class QEntornoShaderBAS extends FragmentShader {

    private QColor colorEspecular = QColor.WHITE.clone();
    private QColor colorReflejo;
    private QColor colorRefraccion;
    private QColor colorEntorno = QColor.WHITE.clone();
    // private QColor colorDesplazamiento;
    private float transparencia;
    private float factorMetalico = 0;
    private float factorFresnel = 0;

    public QEntornoShaderBAS(RenderEngine render) {
        super(render);
    }

    @Override
    public QColor shadeFragment(Fragment pixel, int x, int y) {
        if (pixel == null) {
            return null;
        }
        if (!pixel.isDibujar()) {
            return null;
        }

        QColor color = new QColor();// color default, blanco
        QIluminacion iluminacion = new QIluminacion();

        QMaterialBas material = (QMaterialBas) pixel.material;

        // modifica los valores xyz del pixel de acuerdo al mapa de desplazamiento
        // if (material.getMapaDesplazamiento() != null) {
        // colorDesplazamiento =
        // material.getMapaDesplazamiento().get_QARGB(currentPixel.u, currentPixel.v);
        // // el mapa de desplazamiento es uno a escalas de grises, sin emabargo aun
        // hare el promedio de los 3 canales
        // float fac = (colorDesplazamiento.r + colorDesplazamiento.g +
        // colorDesplazamiento.b) / 3.0f * material.getFactorNormal();
        // QVector3 vec = QVector3.of(currentPixel.x, currentPixel.y, currentPixel.z);
        // vec.add(currentPixel.normal.clone().multiply(fac));
        // currentPixel.x = vec.x;
        // currentPixel.y = vec.y;
        // currentPixel.z = vec.z;
        // }
        // //TOMA EL VALOR DE LA TRANSPARENCIA
        // if (material.isTransparencia()) {
        // //si tiene un mapa de transparencia
        // if (material.getMapaTransparencia() != null) {
        // // es una imagen en blanco y negro, toma cualquier canal de color
        // transparencia = material.getMapaTransparencia().get_QARGB(pixel.u,
        // pixel.v).r;
        // } else {
        // //toma el valor de transparencia del material
        // transparencia = material.getTransAlfa();
        // }
        // } else {
        // transparencia = 1;
        // }
        /**
         * ********************************************************************************
         * COLOR DIFUSO /BASE
         * ********************************************************************************
         */
        if (material.getMapaColor() == null || !render.opciones.isMaterial()) {
            // si no hay textura usa el color del material
            color.set(material.getColorBase());
        } else {
            if (!material.getMapaColor().isProyectada()) {
                // si la textura no es proyectada (lo hace otro renderer) toma las coordenadas
                // ya calculadas
                color = material.getMapaColor().get_QARGB(pixel.u, pixel.v);
            } else {
                // si es proyectada se asume que la textura es el resultado de un renderizador
                // por lo tanto coresponde a una pantalla y debemos tomar las mismas coordenadas
                // que llegan en X y Y, sin embargo las coordenadas UV estan normalizadas de 0 a
                // 1
                // por lo tanto convertimos las coordeandas XyY a coordenadas UV
                color = material.getMapaColor().get_QARGB((float) x / (float) render.getFrameBuffer().getAncho(),
                        -(float) y / (float) render.getFrameBuffer().getAlto());
            }

            // si se configuro un color transparente para la textura
            // solo activa la transparencia si tiene el canal alfa
            if (color.a < 1 || (material.isTransparencia() && material.getColorTransparente() != null
                    && color.toRGB() == material.getColorTransparente().toRGB())) {
                return null;
            }
        }

        // tomo el valor del mapa especular, si existe
        // es usado en el calculo de la iluminacion y en el reflejo/refraccion del
        // entorno
        if (material.getMapaEspecular() != null) {
            colorEspecular = material.getMapaEspecular().get_QARGB(pixel.u, pixel.v);
        } else {
            colorEspecular = QColor.WHITE;// equivale a multiplicar por 1
        }

        if (material.getMapaMetalico() != null) {
            factorMetalico = material.getMapaMetalico().get_QARGB(pixel.u, pixel.v).r;
        } else {
            factorMetalico = material.getMetalico();
        }

        calcularEntorno(pixel, color);
        calcularIluminacion(pixel, iluminacion, color);

        // Iluminacion ambiente
        color.scale(iluminacion.getColorAmbiente());
        // // Agrega color de la luz
        color.addLocal(iluminacion.getColorLuz());

        // ***********************************************************
        // ****** TRANSPARENCIA
        // ***********************************************************
        if (material.isTransparencia() && transparencia < 1) {
            QColor tmp = render.getFrameBuffer().getColor(x, y);// el color actual en el buffer para mezclarlo
            color.r = (1 - transparencia) * tmp.r + transparencia * color.r;
            color.g = (1 - transparencia) * tmp.g + transparencia * color.g;
            color.b = (1 - transparencia) * tmp.b + transparencia * color.b;
            tmp = null;
        }
        //
        // //lugar de la reflexion despues de la iluminacion
        // //calculo la niebla al final del calculo de iluminacion
        // try {
        // if (QEscena.INSTANCIA != null) {
        // QColor resul = calcularNeblina(color, pixel, QEscena.INSTANCIA.neblina);
        // if (resul != null) {
        // color.set(resul);
        // }
        // }
        // } catch (Exception e) {
        // }

        return color;
    }

    /**
     * 07/02/2018.Se implementa la iluminacion de Bling-Phong que mejora los
     * tiempos y es el default de OpenGL y Directx
     * https://en.wikipedia.org/wiki/Blinn%E2%80%93Phong_shading_model que
     * mejora
     *
     * @param fragment
     */
    protected void calcularIluminacion(Fragment fragment, QIluminacion iluminacion, QColor color) {

        QVector3 vectorLuz = QVector3.empty();
        float distanciaLuz;
        QVector3 tmpPixelPos = QVector3.empty();

        // La iluminacion se calcula en el sistema de coordenadas de la camara
        fragment.normal.normalize();
        iluminacion.setColorLuz(QColor.BLACK.clone());
        QMaterialBas material = (QMaterialBas) fragment.material;
        // usa el mapa de iluminacion con el ambiente
        if (material.getMapaEmisivo() != null && render.opciones.isMaterial()) {
            QColor colorEmisivo = material.getMapaEmisivo().get_QARGB(fragment.u, fragment.v);
            iluminacion.setColorAmbiente(colorEmisivo.clone().add(render.getScene().getAmbientColor()));
        } else {
            // si tiene factor de emision toma ese valor solamente
            if (material.getFactorEmision() > 0) {
                // illumination.dR = material.getFactorEmision();
                float factorEmision = material.getFactorEmision();
                iluminacion.setColorAmbiente(new QColor(factorEmision, factorEmision, factorEmision));
                return;// no hago mas calculos
            } else {
                iluminacion.setColorAmbiente(render.getScene().getAmbientColor().clone());
            }
        }

        TempVars tv = TempVars.get();
        try {
            float factorSombra = 1;// 1= no sombra
            float factorSombraSAO = 1;// factor de oclusion ambiental con el mapa SAO
            float rugosidad = material.getRugosidad();
            if (render.opciones.isMaterial() && material.getMapaRugosidad() != null) {
                rugosidad = material.getMapaRugosidad().get_QARGB(fragment.u, fragment.v).r;
            }

            float reflectancia = 1.0f - rugosidad;

            if (render.opciones.isMaterial() && material.getMapaSAO() != null) {
                factorSombraSAO = material.getMapaSAO().get_QARGB(fragment.u, fragment.v).r;
            }

            // solo si hay luces y si las opciones de la vista tiene activado el material
            if (render.opciones.isMaterial() && !render.getLitgths().isEmpty()) {
                for (QLigth luz : render.getLitgths()) {
                    // si esta encendida
                    if (luz != null && luz.getEntity().isToRender() && luz.isEnable()) {
                        factorSombra = 1;
                        QProcesadorSombra proc = luz.getSombras();
                        if (proc != null && render.opciones.isSombras() && material.isSombrasRecibir()) {
                            factorSombra = proc.factorSombra(TransformationVectorUtil.transformarVectorInversa(
                                    fragment.ubicacion.getVector3(), fragment.entity, render.getCamara()),
                                    fragment.entity);
                        }

                        if (luz instanceof QPointLigth || luz instanceof QSpotLigth) {
                            // vectorLuz.set(pixel.ubicacion.x -
                            // luz.entity.getTransformacion().getTraslacion().x, pixel.ubicacion.y -
                            // luz.entity.getTransformacion().getTraslacion().y, pixel.ubicacion.z -
                            // luz.entity.getTransformacion().getTraslacion().z);
                            vectorLuz.set(fragment.ubicacion.getVector3().clone().subtract(
                                    TransformationVectorUtil.transformarVector(QVector3.zero, luz.getEntity(),
                                            render.getCamara())));
                            distanciaLuz = vectorLuz.length();
                            // solo toma en cuenta a los puntos q estan en el area de afectacion
                            if (distanciaLuz > luz.radio) {
                                continue;
                            }

                            float alfa = 0.0f;
                            // si es Spot valido que este dentro del cono
                            if (luz instanceof QSpotLigth) {
                                QVector3 coneDirection = ((QSpotLigth) luz).getDirectionTransformada().normalize();
                                alfa = coneDirection.angulo(vectorLuz.clone().normalize());
                                if (alfa > ((QSpotLigth) luz).getAnguloExterno()) {
                                    continue;
                                }
                            }

                            QColor colorLuz = QMath.calcularColorLuz(color, colorEspecular, luz.color,
                                    luz.energia * factorSombra * factorSombraSAO, fragment.ubicacion.getVector3(),
                                    vectorLuz.invert().normalize(), fragment.normal, material.getSpecularExponent(),
                                    reflectancia);
                            // atenuacion
                            // float attenuationInv = light.att.constant + light.att.interpolateLinear *
                            // distance +
                            // light.att.exponent * distance * distance;
                            colorLuz.scaleLocal(
                                    1.0f / (luz.coeficientesAtenuacion.x + luz.coeficientesAtenuacion.y * distanciaLuz
                                            + luz.coeficientesAtenuacion.z * distanciaLuz * distanciaLuz));

                            // si la luz es spot, realiza una atenuacion adicional dependiendo del angulo
                            if (luz instanceof QSpotLigth) {
                                // lo siguiente es.. la diferencia entre alfa y el angulo externo divido con la
                                // diferencia entre el angulo interno y el angulo externo (se agrega una
                                // validacion para no permitir la division por cero)
                                colorLuz.scaleLocal(QMath.clamp(
                                        (alfa - ((QSpotLigth) luz).getAnguloExterno())
                                                / Math.min(((QSpotLigth) luz).getAnguloInterno()
                                                        - ((QSpotLigth) luz).getAnguloExterno(), -0.0001f),
                                        0.0f, 1.0f));
                            }

                            iluminacion.getColorLuz().addLocal(colorLuz);
                        } else if (luz instanceof QDirectionalLigth) {
                            vectorLuz.set(((QDirectionalLigth) luz).getDirectionTransformada());
                            iluminacion.getColorLuz()
                                    .addLocal(QMath.calcularColorLuz(color, colorEspecular, luz.color,
                                            luz.energia * factorSombra * factorSombraSAO,
                                            fragment.ubicacion.getVector3(),
                                            vectorLuz.invert().normalize(), fragment.normal,
                                            material.getSpecularExponent(), reflectancia));
                        }
                    }
                }
            } else {
                // iluminacion default cuando no hay luces se asume una luz central
                tmpPixelPos.set(fragment.ubicacion.getVector3());
                tmpPixelPos.normalize();
                iluminacion.getColorAmbiente().add(-tmpPixelPos.dot(fragment.normal));
            }
        } finally {
            tv.release();
        }
    }

    /**
     * Calcula la Reflexión y Refracción utilizando un mapa de entorno (puede
     * ser generado con un mapa de cubo)
     *
     * @param fragment
     */
    private void calcularEntorno(Fragment fragment, QColor color) {
        // Reflexion y refraccion del entorno (en caso de materiales con refraccion
        // (transparentes)
        QMaterialBas material = (QMaterialBas) fragment.material;
        // verifica que el mimap del mapa de entorno este en nivel 1
        if (material.getMapaEntorno() instanceof QProcesadorMipMap) {
            int nivel = ((QProcesadorMipMap) material.getMapaEntorno()).getNivel();
            if (nivel != 1) {
                ((QProcesadorMipMap) material.getMapaEntorno()).setNivel(1);
            }
        }

        if (render.opciones.isMaterial()
                && // esta activada la opción de material
                material.getMapaEntorno() != null // tiene un mapa de entorno
                && (material.isReflexion() || material.isRefraccion()) // tien habilitada la reflexión y/o la refración
        ) {
            TempVars tm = TempVars.get();
            try {

                // *********************************************************************************************
                // ****** VECTOR NORMAL
                // *********************************************************************************************
                // la normal del pixel, quitamos la transformacion de la ubicacion y volvemos a
                // calcularla en las coordenadas del mundo
                // tm.vector3f2.set(pixel.normal);
                tm.vector3f2.set(TransformationVectorUtil.transformarVectorNormal(
                        TransformationVectorUtil.transformarVectorNormalInversa(fragment.normal, fragment.entity,
                                render.getCamara()),
                        fragment.entity.getMatrizTransformacion(QGlobal.tiempo)));
                tm.vector3f2.normalize();

                // *********************************************************************************************
                // ****** VECTOR VISION
                // *********************************************************************************************
                // para obtener el vector vision quitamos la transformacion de la ubicacion y
                // volvemos a calcularla en las coordenadas del mundo
                // tm.vector3f1.set(currentPixel.ubicacion.getVector3());
                tm.vector3f1.set(TransformationVectorUtil.transformarVector(
                        TransformationVectorUtil.transformarVectorInversa(fragment.ubicacion, fragment.entity,
                                render.getCamara()),
                        fragment.entity).getVector3());
                // ahora restamos la posicion de la camara a la posicion del mundo
                tm.vector3f1.subtract(render.getCamara().getMatrizTransformacion(QGlobal.tiempo).toTranslationVector());
                tm.vector3f1.normalize();

                // ************************************************************
                // ****** REFLEXION
                // ************************************************************
                if (material.isReflexion()) {
                    tm.vector3f3.set(QMath.reflejarVector(tm.vector3f1, tm.vector3f2));
                    colorReflejo = QTexturaUtil.getColorMapaEntorno(tm.vector3f3, material.getMapaEntorno(),
                            material.getTipoMapaEntorno());
                    // colorReflejo = QTexturaUtil.getColorMapaEntorno(tm.vector3f2,
                    // material.getMapaEntorno(), material.getTipoMapaEntorno());
                } else {
                    colorReflejo = null;
                }
                // ***********************************************************
                // ****** REFRACCION
                // ***********************************************************
                if (material.isRefraccion() && material.getIndiceRefraccion() > 0) {
                    tm.vector3f4.set(QMath.refractarVector(tm.vector3f1, tm.vector3f2, material.getIndiceRefraccion()));
                    colorRefraccion = QTexturaUtil.getColorMapaEntorno(tm.vector3f4, material.getMapaEntorno(),
                            material.getTipoMapaEntorno());
                } else {
                    colorRefraccion = null;
                }
                // APLICACION DEL COLOR DEL ENTORNO

                // mezclo el color de reflexion con el de refraccion
                if (colorReflejo != null && colorRefraccion != null) {
                    factorFresnel = QMath.factorFresnel(tm.vector3f1, tm.vector3f2, 0);
                    // factorFresnel = QMath.factorFresnel(tm.vector3f2, tm.vector3f1, 0);
                    colorEntorno = QMath.mix(colorRefraccion, colorReflejo, factorFresnel);
                    // colorEntorno.r = QMath.mix(colorRefraccion.r, colorReflejo.r, factorFresnel);
                    // colorEntorno.g = QMath.mix(colorRefraccion.g, colorReflejo.g, factorFresnel);
                    // colorEntorno.b = QMath.mix(colorRefraccion.b, colorReflejo.b, factorFresnel);
                } else if (colorReflejo != null) {
                    colorEntorno = colorReflejo.clone();
                } else if (colorRefraccion != null) {
                    colorEntorno = colorRefraccion.clone();
                }

                // mezcla el color del entorno
                // color.r = QMath.mix(color.r, colorEntorno.r, Math.min(factorMetalico,0.9f));
                // color.g = QMath.mix(color.g, colorEntorno.g, Math.min(factorMetalico,0.9f));
                // color.b = QMath.mix(color.b, colorEntorno.b, Math.min(factorMetalico,0.9f));
                color = QMath.mix(color, colorEntorno, Math.min(factorMetalico, 0.9f));
            } catch (Exception e) {
                // System.out.println("error reflexion " + e.getMessage());
            } finally {
                tm.release();
            }
        }
    }

}
