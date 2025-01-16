/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.basico;

import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QIluminacion;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.shadow.QProcesadorSombra;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.TextureUtil;
import net.qoopo.engine.core.texture.procesador.MipmapTexture;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.TempVars;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;
import net.qoopo.engine.renderer.util.TransformationVectorUtil;

/**
 * Calcula el color e iluminación de cada pixel, calcula la reflexion y
 * refraccion, iluminacion de entorno, sombras, textura y sombreado de phong
 *
 * @author alberto
 */
public class StandardFragmentShader extends FragmentShader {

    public StandardFragmentShader(RenderEngine render) {
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

        Material material = (Material) fragment.material;

        // *********************************************************************************************
        // ****** VECTOR VISION
        // *********************************************************************************************
        // para obtener el vector vision quitamos la transformacion de la ubicacion y
        // volvemos a calcularla en las coordenadas del mundo
        // como la iluminacion la calculamos en el sistema de coordeandas de la camara,
        // el vector vision corresponde a la coordenada del pixel (invertido)
        QVector3 V = fragment.ubicacion.getVector3();
        V.invert();// Cam-WordPos
        V.normalize();

        // *********************************************************************************************
        // ****** VECTOR VISION
        // *********************************************************************************************
        // para obtener el vector vision quitamos la transformacion de la ubicacion y
        // volvemos a calcularla en las coordenadas del mundo
        // tm.vector3f1.set(currentPixel.ubicacion.getVector3());
        // QVector3 V = TransformationVectorUtil.transformarVector(
        // TransformationVectorUtil.transformarVectorInversa(fragment.ubicacion,
        // fragment.entity,
        // render.getCamera()),
        // fragment.entity).getVector3();
        // // ahora restamos la posicion de la camara a la posicion del mundo
        // V.subtract(render.getCamera().getMatrizTransformacion(QGlobal.time).toTranslationVector());
        // V.normalize();

        // *********************************************************************************************
        // ****** VECTOR NORMAL
        // *********************************************************************************************
        QVector3 N = fragment.normal;
        N.normalize();
        // la normal del pixel en el espacio mundial, se usara para tomar el color del
        // mapa de irradiacion, quitamos la transformacion de la ubicacion y volvemos a
        // calcularla en las coordenadas del mundo *
        QVector3 N2 = (TransformationVectorUtil.transformarVectorNormal(
                TransformationVectorUtil.transformarVectorNormalInversa(fragment.normal, fragment.entity,
                        render.getCamera()),
                fragment.entity != null ? fragment.entity.getMatrizTransformacion(QGlobal.time)
                        : QMatriz4.IDENTITY));
        N2.normalize();

        QColor color = getColorBase(fragment, x, y);
        computeNormal(fragment);
        computeEnviromentColor(fragment, N2, V, material.getEnvMap(), color);
        computeLighting(fragment, color);
        // color.fixGamma();

        // ***********************************************************
        // ****** TRANSPARENCIA
        // ***********************************************************
        // //TOMA EL VALOR DE LA TRANSPARENCIA
        if (material.isTransparencia()) {
            // si tiene un mapa de transparencia
            float alpha = 0;
            if (material.getAlphaMap() != null) {
                // es una imagen en blanco y negro, toma cualquier canal de color
                alpha = material.getAlphaMap().getQColor(fragment.u, fragment.v).r;
            } else {
                // toma el valor de transparencia del material
                alpha = material.getTransAlfa();
            }
            if (alpha < 1) {
                QColor tmp = render.getFrameBuffer().getColor(x, y);// el color actual en el buffer para mezclarlo
                color.r = (1 - alpha) * tmp.r + alpha * color.r;
                color.g = (1 - alpha) * tmp.g + alpha * color.g;
                color.b = (1 - alpha) * tmp.b + alpha * color.b;
                tmp = null;
            }
        }

        //
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
     * ********************************************************************************
     * COLOR DIFUSO /BASE
     * ********************************************************************************
     */
    private QColor getColorBase(Fragment fragment, int x, int y) {

        Material material = (Material) fragment.material;
        QColor colorBase = new QColor();// color default, blanco

        if (material.getColorMap() == null || !render.opciones.isMaterial()) {
            // si no hay textura usa el color del material
            colorBase.set(material.getColor());
        } else {
            if (!material.getColorMap().isProyectada()) {
                // si la textura no es proyectada (lo hace otro renderer) toma las coordenadas
                // ya calculadas
                colorBase = material.getColorMap().getQColor(fragment.u, fragment.v);
                // corrijo canal de material albedo (Gamma)
                if (QGlobal.ENABLE_GAMMA_FIX)
                    colorBase.addGamma();
            } else {
                // si es proyectada se asume que la textura es el resultado de un renderizador
                // por lo tanto coresponde a una pantalla y debemos tomar las mismas coordenadas
                // que llegan en X y Y, sin embargo las coordenadas UV estan normalizadas de 0 a
                // 1
                // por lo tanto convertimos las coordeandas XyY a coordenadas UV
                colorBase = material.getColorMap().getQColor((float) x / (float) render.getFrameBuffer().getWidth(),
                        -(float) y / (float) render.getFrameBuffer().getHeight());
            }
        }

        return colorBase;
    }

    /**
     * Si hay un mapa normal, recalcula la normal
     * 
     * @param fragment
     */
    private void computeNormal(Fragment fragment) {
        Material material = (Material) fragment.material;
        // normal map
        // modifica la normal en funcion del mapa normal
        if (material.getNormalMap() != null && render.opciones.isNormalMapping()) {

            // normales en el espacio de objeto (se necesita la matriz vistaModelo para
            // calcular la normal nuevamnte)
            // QVector3 normal = new QVector3();
            // normal.set(material.getMapaNormal().getQColor(fragment.u, fragment.v).rgb());
            // normal.multiply(2).add(-1).normalize();
            // QVector4 tmpNormal = new QVector4(normal, 0);
            // tmpNormal.set(fragment.matViewModel.mult(tmpNormal));
            // fragment.normal.set(tmpNormal.getVector3().normalize());

            QVector3 normalMap = material.getNormalMap().getQColor(fragment.u, fragment.v).rgb();
            // fragment.up.multiply(normalMap.y * 2 - 1);
            // fragment.right.multiply(normalMap.x * 2 - 1);
            // fragment.normal.multiply(normalMap.z * 2 - 1);
            normalMap.multiply(2).add(-1).normalize();
            fragment.up.multiply(normalMap.y);
            fragment.right.multiply(normalMap.x);
            fragment.normal.multiply(normalMap.z);
            fragment.normal.add(fragment.up, fragment.right);
            fragment.normal.normalize();
        }
    }

    /**
     * 07/02/2018.Se implementa la iluminacion de Bling-Phong que mejora los
     * tiempos y es el default de OpenGL y Directx
     * https://en.wikipedia.org/wiki/Blinn%E2%80%93Phong_shading_model que
     * mejora
     *
     * @param fragment
     */
    protected void computeLighting(Fragment fragment, QColor color) {

        QVector3 vectorLuz = QVector3.empty();
        float distanciaLuz;
        QVector3 tmpPixelPos = QVector3.empty();
        QIluminacion iluminacion = new QIluminacion();
        TempVars tv = TempVars.get();
        try {

            Material material = (Material) fragment.material;

            QColor colorEspecular;

            // tomo el valor del mapa especular, si existe
            // es usado en el calculo de la iluminacion y en el reflejo/refraccion del
            // entorno
            if (material.getMapaEspecular() != null) {
                colorEspecular = material.getMapaEspecular().getQColor(fragment.u, fragment.v);
            } else {
                colorEspecular = material.getColorEspecular();// QColor.WHITE;// equivale a multiplicar por 1
            }

            // La iluminacion se calcula en el sistema de coordenadas de la camara
            fragment.normal.normalize();
            iluminacion.setColorLuz(QColor.BLACK.clone());

            // usa el mapa de iluminacion con el ambiente
            if (material.getEmissiveMap() != null && render.opciones.isMaterial()) {
                QColor colorEmisivo = material.getEmissiveMap().getQColor(fragment.u, fragment.v);
                iluminacion.setColorAmbiente(colorEmisivo.clone().add(render.getScene().getAmbientColor()));
            } else {
                // si tiene factor de emision toma ese valor solamente
                if (material.getEmision() > 0) {
                    float factorEmision = material.getEmision();
                    iluminacion.setColorAmbiente(new QColor(factorEmision, factorEmision,
                            factorEmision));
                    return;// no hago mas calculos
                    // QColor colorEmisivo = new QColor(material.getFactorEmision(),
                    // material.getFactorEmision(),
                    // material.getFactorEmision());
                    // iluminacion.setColorAmbiente(colorEmisivo.clone().add(render.getScene().getAmbientColor()));
                } else {
                    iluminacion.setColorAmbiente(render.getScene().getAmbientColor().clone());
                }
            }

            float factorSombra = 1;// 1= no sombra
            float ao = 1;// factor de oclusion ambiental con el mapa SAO
            float rugosidad = material.getRoughness();
            if (render.opciones.isMaterial() && material.getRoughnessMap() != null) {
                rugosidad = material.getRoughnessMap().getQColor(fragment.u, fragment.v).r;
            }

            float reflectancia = 1.0f - rugosidad;

            if (render.opciones.isMaterial() && material.getAoMap() != null) {
                ao = material.getAoMap().getQColor(fragment.u, fragment.v).r;
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
                            // vectorLuz.set(pixel.ubicacion.x -
                            // luz.entity.getTransformacion().getLocation().x, pixel.ubicacion.y -
                            // luz.entity.getTransformacion().getLocation().y, pixel.ubicacion.z -
                            // luz.entity.getTransformacion().getLocation().z);
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
                                    luz.energia * factorSombra * ao, fragment.ubicacion.getVector3(),
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
                                            luz.energia * factorSombra * ao, fragment.ubicacion.getVector3(),
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

            // Iluminacion ambiente
            color.scale(iluminacion.getColorAmbiente());
            // // Agrega color de la luz
            color.addLocal(iluminacion.getColorLuz());
        }
    }

    /**
     * Calcula la Reflexión y Refracción utilizando un mapa de entorno (puede
     * ser generado con un mapa de cubo)
     *
     * @param fragment
     */
    private void computeEnviromentColor(Fragment fragment, QVector3 N, QVector3 V, Texture enviromentMap,
            QColor color) {
        // Reflexion y refraccion del entorno (en caso de materiales con refraccion
        // (transparentes)
        Material material = (Material) fragment.material;

        float roughness = material.getRoughness();
        float metallic = material.getMetallic();
        if (render.opciones.isMaterial() && material.getRoughnessMap() != null) {
            roughness = material.getRoughnessMap().getQColor(fragment.u, fragment.v).r;
        }
        if (render.opciones.isMaterial() && material.getMetallicMap() != null) {
            metallic = material.getMetallicMap().getQColor(fragment.u, fragment.v).r;
        }

        // verifica que el mimap del mapa de entorno este en nivel 1
        if (material.getEnvMap() instanceof MipmapTexture) {
            int nivel = ((MipmapTexture) material.getEnvMap()).getLevel();
            if (nivel != 1) {
                ((MipmapTexture) material.getEnvMap()).setLevel(1);
            }
        }

        if (render.opciones.isMaterial() && enviromentMap != null
                && (material.isReflexion() || material.isRefraccion())) {
            try {

                // según la rugosidad del material cambia el nivel del mipmap
                if (enviromentMap instanceof MipmapTexture) {
                    float niveles = (float) ((MipmapTexture) enviromentMap).getMaxLevel();
                    ((MipmapTexture) enviromentMap).setLevel((int) (niveles * roughness));
                }

                QColor colorReflejo;
                QColor colorRefraccion;
                QColor colorEntorno = null;

                // ************************************************************
                // ****** REFLEXION
                // ************************************************************
                if (material.isReflexion()) {
                    QVector3 reflejo = QMath.reflejarVector(V, N);
                    colorReflejo = TextureUtil.getEnviromentMapColor(reflejo, enviromentMap, material.getEnvMapType());
                } else {
                    colorReflejo = null;
                }
                // ***********************************************************
                // ****** REFRACCION
                // ***********************************************************
                if (material.isRefraccion() && material.getIor() > 0) {
                    QVector3 refraccion = QMath.refractarVector(V, N, material.getIor());
                    colorRefraccion = TextureUtil.getEnviromentMapColor(refraccion, enviromentMap,
                            material.getEnvMapType());
                } else {
                    colorRefraccion = null;
                }

                // mezclo el color de reflexion con el de refraccion
                if (colorReflejo != null && colorRefraccion != null) {
                    float factorFresnel = QMath.factorFresnel(V, N, material.getIor());
                    colorEntorno = QMath.mix(colorRefraccion, colorReflejo, factorFresnel);
                    // color.set(colorEntorno);
                    color.set(QMath.mix(color, colorEntorno, (float) Math.min(Math.max(metallic, 0.0f), 1.0f)));
                } else if (colorReflejo != null) {
                    // solo reflejo
                    color.set(QMath.mix(color, colorReflejo, (float) Math.min(Math.max(metallic, 0.0f), 1.0f)));
                    // colorEntorno = colorReflejo;
                } else if (colorRefraccion != null) {
                    // solo refraccion
                    color.set(QMath.mix(color, colorRefraccion,
                            (float) Math.min(Math.max(metallic, 0.0f), 1.0f)));
                    // colorEntorno = colorRefraccion;
                }
            } catch (Exception e) {
                System.out.println("error reflexion " + e.getMessage());
            } finally {

            }
        }
    }

}
