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
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.shadow.QProcesadorSombra;
import net.qoopo.engine.core.util.TempVars;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;
import net.qoopo.engine.renderer.util.TransformationVectorUtil;

/**
 * 5.
 * Shader con textura que procesa sombras, iluminacion de escena, textures y
 * sombreado phong
 *
 * @author alberto
 */
public class ShadowFragmentShader extends FragmentShader {

    private QColor colorDifuso;
    private QColor colorEspecular = QColor.WHITE.clone();
    private float transparencia;

    public ShadowFragmentShader(RenderEngine render) {
        super(render);
    }

    @Override
    public QColor shadeFragment(Fragment fragment, int x, int y) {
        if (fragment == null) {
            return null;
        }
        if (!fragment.isDraw()) {
            return null;
        }

        QColor color = new QColor();// color default, blanco
        QIluminacion iluminacion = new QIluminacion();

        // TOMA EL VALOR DE LA TRANSPARENCIA
        if (((Material) fragment.material).isTransparencia()) {
            // si tiene un mapa de transparencia
            if (((Material) fragment.material).getAlphaMap() != null) {
                // es una imagen en blanco y negro, toma cualquier canal de color
                transparencia = ((Material) fragment.material).getAlphaMap().getQColor(fragment.u,
                        fragment.v).r;
            } else {
                // toma el valor de transparencia del material
                transparencia = ((Material) fragment.material).getTransAlfa();
            }
        } else {
            transparencia = 1;
        }
        if (((Material) fragment.material).getColorMap() == null || !render.opciones.isMaterial()) {
            // si no hay textura usa el color del material
            color.set(fragment.material.getColor());
        } else {

            // si la textura no es proyectada (lo hace otro renderer) toma las coordenadas
            // ya calculadas
            if (!((Material) fragment.material).getColorMap().isProyectada()) {
                colorDifuso = ((Material) fragment.material).getColorMap().getQColor(fragment.u, fragment.v);
            } else {
                colorDifuso = ((Material) fragment.material).getColorMap().getQColor(
                        (float) x / (float) render.getFrameBuffer().getWidth(),
                        -(float) y / (float) render.getFrameBuffer().getHeight());
            }
            color.set(colorDifuso);
            if (colorDifuso.a < 1 || (((Material) fragment.material).isTransparencia()
                    && ((Material) fragment.material).getAlphaColour() != null
                    && colorDifuso.toRGB() == ((Material) fragment.material).getAlphaColour().toRGB())) {
                return null;
            }
        }

        calcularIluminacion(fragment, iluminacion, color);
        color.scale(iluminacion.getColorAmbiente());
        color.addLocal(iluminacion.getColorLuz());

        // ***********************************************************
        // ****** TRANSPARENCIA
        // ***********************************************************
        if (((Material) fragment.material).isTransparencia() && transparencia < 1) {
            QColor tmp = render.getFrameBuffer().getColor(x, y);// el color actual en el buffer para mezclarlo
            color.r = (1 - transparencia) * tmp.r + transparencia * color.r;
            color.g = (1 - transparencia) * tmp.g + transparencia * color.g;
            color.b = (1 - transparencia) * tmp.b + transparencia * color.b;
            tmp = null;
        }

        return color;
    }

    /**
     * Realiza el cálculo de la iluminacion tomando en cuenta las luces de la escena
     * 
     * @param fragment
     * @param iluminacion
     * @param color
     */
    protected void calcularIluminacion(Fragment fragment, QIluminacion iluminacion, QColor color) {

        QVector3 vectorLuz = QVector3.empty();
        float distanciaLuz;
        QVector3 tmpPixelPos = QVector3.empty();

        // La iluminacion se calcula en el sistema de coordenadas de la camara
        fragment.normal.normalize();
        iluminacion.setColorLuz(QColor.BLACK.clone());
        Material material = (Material) fragment.material;
        // usa el mapa de iluminacion con el ambiente
        if (material.getEmissiveMap() != null && render.opciones.isMaterial()) {
            QColor colorEmisivo = material.getEmissiveMap().getQColor(fragment.u, fragment.v);
            iluminacion.setColorAmbiente(colorEmisivo.clone().add(render.getScene().getAmbientColor()));
        } else {
            // si tiene factor de emision toma ese valor solamente
            if (material.getEmision() > 0) {
                // illumination.dR = material.getFactorEmision();
                float factorEmision = material.getEmision();
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
            float rugosidad = material.getRoughness();
            if (render.opciones.isMaterial() && material.getRoughnessMap() != null) {
                rugosidad = material.getRoughnessMap().getQColor(fragment.u, fragment.v).r;
            }

            float reflectancia = 1.0f - rugosidad;

            if (render.opciones.isMaterial() && material.getAoMap() != null) {
                factorSombraSAO = material.getAoMap().getQColor(fragment.u, fragment.v).r;
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
                                    fragment.ubicacion.getVector3(), fragment.entity, render.getCamera()),
                                    fragment.entity);
                        }

                        if (luz instanceof QPointLigth || luz instanceof QSpotLigth) {

                            vectorLuz.set(fragment.ubicacion.getVector3().clone().subtract(
                                    TransformationVectorUtil.transformarVector(QVector3.zero, luz.getEntity(),
                                            render.getCamera())));
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

}
