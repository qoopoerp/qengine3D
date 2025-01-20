/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.basico;

import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.primitive.Fragment;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.Matrix4;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.renderer.RenderEngine;
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
        // Vector3 V = fragment.location.getVector3();
        // V.invert();// Cam-WordPos
        // V.normalize();

        // *********************************************************************************************
        // ****** VECTOR VISION
        // *********************************************************************************************
        // para obtener el vector vision quitamos la transformacion de la ubicacion y
        // volvemos a calcularla en las coordenadas del mundo
        Vector3 V = TransformationVectorUtil.transformarVector(
                TransformationVectorUtil.transformarVectorInversa(fragment.location,
                        fragment.entity,
                        render.getCamera()),
                fragment.entity).getVector3();
        // ahora restamos la posicion de la camara a la posicion del mundo
        V.subtract(render.getCamera().getMatrizTransformacion(QGlobal.time).toTranslationVector());
        V.normalize();

        // *********************************************************************************************
        // ****** VECTOR NORMAL
        // *********************************************************************************************
        // Vector3 N = fragment.normal;
        // N.normalize();
        // la normal del pixel en el espacio mundial, se usara para tomar el color del
        // mapa de irradiacion, quitamos la transformacion de la ubicacion y volvemos a
        // calcularla en las coordenadas del mundo *
        Vector3 N = (TransformationVectorUtil.transformarVectorNormal(
                TransformationVectorUtil.transformarVectorNormalInversa(fragment.normal,
                        fragment.entity,
                        render.getCamera()),
                fragment.entity != null ? fragment.entity.getMatrizTransformacion(QGlobal.time)
                        : Matrix4.IDENTITY));
        N.normalize();

        QColor color = getColorBase(fragment, x, y);
        computeNormal(fragment);
        computeEnviromentColor(fragment, N, V, material.getEnvMap(), color);
        computeLighting(fragment, color, color);
        // color.fixGamma();

        // ***********************************************************
        // ****** TRANSPARENCIA
        // ***********************************************************
        // //TOMA EL VALOR DE LA TRANSPARENCIA
        if (material.isTransparent()) {
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

            Vector3 normalMap = material.getNormalMap().getQColor(fragment.u, fragment.v).rgb();
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
    protected void computeLighting(Fragment fragment, QColor color, QColor outputColor) {

        Vector3 vectorLuz = Vector3.empty();
        float distanciaLuz;
        Vector3 tmpPixelPos = Vector3.empty();

        Vector3 luzAmbiente = new Vector3();
        Vector3 luzEmision = new Vector3();
        Vector3 luzDirecta = new Vector3();

        TempVars tv = TempVars.get();
        try {

            luzAmbiente = render.getScene().getAmbientColor().rgb().multiply(color.rgb());

            Material material = (Material) fragment.material;

            QColor colorEspecular;

            // tomo el valor del mapa especular, si existe
            // es usado en el calculo de la iluminacion y en el reflejo/refraccion del
            // entorno
            if (material.getMapaEspecular() != null) {
                colorEspecular = material.getMapaEspecular().getQColor(fragment.u, fragment.v);
            } else {
                colorEspecular = material.getSpecularColour();// QColor.WHITE;// equivale a multiplicar por 1
            }

            // usa el mapa de iluminacion con el ambiente
            if (material.getEmissiveMap() != null && render.opciones.isMaterial()) {
                luzEmision = material.getEmissiveMap().getQColor(fragment.u, fragment.v).rgb().multiply(color.rgb());
            } else {
                // si tiene factor de emision toma ese valor solamente
                float factorEmision = material.getEmissionIntensity();
                luzEmision = color.rgb().multiply(factorEmision);
            }

            float ao = 1;// factor de oclusion ambiental con el mapa SAO
            float roughness = material.getRoughness();
            if (render.opciones.isMaterial() && material.getRoughnessMap() != null) {
                roughness = material.getRoughnessMap().getQColor(fragment.u, fragment.v).r;
            }

            float reflectancia = 1.0f - roughness;
            if (render.opciones.isMaterial() && material.getAoMap() != null) {
                ao = material.getAoMap().getQColor(fragment.u, fragment.v).r;
            }

            // solo si hay luces y si las opciones de la vista tiene activado el material
            if (render.opciones.isMaterial() && !render.getLitgths().isEmpty()) {
                for (QLigth luz : render.getLitgths()) {
                    // si esta encendida
                    if (luz != null && luz.getEntity().isToRender() && luz.isEnable()) {
                        float shadow = 1;// 1= no sombra
                        // QProcesadorSombra proc = luz.getSombras();
                        // if (proc != null && render.opciones.isSombras() &&
                        // material.isSombrasRecibir()) {
                        // shadow = proc.factorSombra(TransformationVectorUtil.transformarVectorInversa(
                        // fragment.location.getVector3(), fragment.entity, render.getCamera()),
                        // fragment.entity);
                        // }

                        if (luz instanceof QPointLigth || luz instanceof QSpotLigth) {
                            // vectorLuz.set(pixel.ubicacion.x -
                            // luz.entity.getTransformacion().getLocation().x, pixel.ubicacion.y -
                            // luz.entity.getTransformacion().getLocation().y, pixel.ubicacion.z -
                            // luz.entity.getTransformacion().getLocation().z);
                            vectorLuz.set(fragment.location.getVector3().clone().subtract(
                                    TransformationVectorUtil.transformarVector(Vector3.zero, luz.getEntity(),
                                            render.getCamera())));
                            distanciaLuz = vectorLuz.length();
                            // solo toma en cuenta a los puntos q estan en el area de afectacion
                            if (distanciaLuz > luz.radio) {
                                continue;
                            }

                            float alfa = 0.0f;
                            // si es Spot valido que este dentro del cono
                            if (luz instanceof QSpotLigth) {
                                Vector3 coneDirection = ((QSpotLigth) luz).getDirectionTransformada().normalize();
                                alfa = coneDirection.angulo(vectorLuz.clone().normalize());
                                if (alfa > ((QSpotLigth) luz).getAnguloExterno()) {
                                    continue;
                                }
                            }

                            // atenuacion
                            // float attenuationInv = light.att.constant + light.att.interpolateLinear *
                            // distance +
                            // light.att.exponent * distance * distance;
                            float atenuacion = 1.0f
                                    / (luz.coeficientesAtenuacion.x + luz.coeficientesAtenuacion.y * distanciaLuz
                                            + luz.coeficientesAtenuacion.z * distanciaLuz * distanciaLuz);

                            float spotFactor = 1.0f;
                            // si la luz es spot, realiza una atenuacion adicional dependiendo del angulo
                            if (luz instanceof QSpotLigth) {
                                // lo siguiente es.. la diferencia entre alfa y el angulo externo divido con la
                                // diferencia entre el angulo interno y el angulo externo (se agrega una
                                // validacion para no permitir la division por cero)
                                spotFactor = QMath.clamp(
                                        (alfa - ((QSpotLigth) luz).getAnguloExterno())
                                                / Math.min(((QSpotLigth) luz).getAnguloInterno()
                                                        - ((QSpotLigth) luz).getAnguloExterno(), -0.0001f),
                                        0.0f, 1.0f);
                            }

                            luzDirecta.add(QMath.calcularColorLuz(color, colorEspecular, luz.color,
                                    luz.energy * shadow * ao, fragment.location.getVector3(),
                                    vectorLuz.invert().normalize(), fragment.normal, material.getSpecularExponent(),
                                    reflectancia, atenuacion, spotFactor).rgb());
                        } else if (luz instanceof QDirectionalLigth) {
                            vectorLuz.set(((QDirectionalLigth) luz).getDirectionTransformada());
                            luzDirecta.add(QMath.calcularColorLuz(color, colorEspecular, luz.color,
                                    luz.energy * shadow * ao, fragment.location.getVector3(),
                                    vectorLuz.invert().normalize(), fragment.normal,
                                    material.getSpecularExponent(), reflectancia, 1.0f, 1.0f).rgb());
                        }
                    }
                }
            } else {
                // iluminacion default cuando no hay luces se asume una luz central
                tmpPixelPos.set(fragment.location.getVector3());
                tmpPixelPos.normalize();
                luzAmbiente.add(-tmpPixelPos.dot(fragment.normal)).multiply(color.rgb());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            outputColor.set(1, 0, 0, 0);
            // Iluminacion ambiente
            outputColor.add(QMath.max(luzAmbiente, Vector3.zero));
            // luz emision
            outputColor.add(QMath.max(luzEmision, Vector3.zero));
            // lus recibida
            outputColor.add(QMath.max(luzDirecta, Vector3.zero));
            tv.release();
        }
    }

    /**
     * Calcula la Reflexión y Refracción utilizando un mapa de entorno (puede
     * ser generado con un mapa de cubo)
     *
     * @param fragment
     */
    private void computeEnviromentColor(Fragment fragment, Vector3 N, Vector3 V, Texture enviromentMap,
            QColor color) {
        // Reflexion y refraccion del entorno (en caso de materiales con refraccion
        // (transparentes)
        Material material = (Material) fragment.material;

        float roughness = material.getRoughness();
        float metalness = material.getMetalness();
        if (render.opciones.isMaterial() && material.getRoughnessMap() != null) {
            roughness = material.getRoughnessMap().getQColor(fragment.u, fragment.v).r;
        }
        if (render.opciones.isMaterial() && material.getMetallicMap() != null) {
            metalness = material.getMetallicMap().getQColor(fragment.u, fragment.v).r;
        }

        if (render.opciones.isMaterial() && enviromentMap != null
                && (material.isReflexion() || material.isRefraccion())) {
            try {

                // según la rugosidad del material cambia el nivel del mipmap
                if (enviromentMap instanceof MipmapTexture) {
                    float niveles = (float) ((MipmapTexture) enviromentMap).getMaxLevel();
                    ((MipmapTexture) enviromentMap).setLevel((int) (niveles * roughness));
                }

                QColor colorReflejo = null;
                QColor colorRefraccion = null;
                QColor colorEntorno = null;

                // Reflexion
                if (material.isReflexion()) {
                    Vector3 reflejo = QMath.reflejarVector(V, N);
                    // colorReflejo = enviromentMap.getColor(reflejo);
                    colorReflejo = TextureUtil.getHdriTextureColor(reflejo, enviromentMap);
                } else {
                    colorReflejo = null;
                }
                // Refraccion
                if (material.isRefraccion() && material.getIor() > 0) {
                    Vector3 refraccion = QMath.refractarVector(V, N, material.getIor());
                    colorRefraccion = enviromentMap.getColor(refraccion);
                } else {
                    colorRefraccion = null;
                }

                // mezclo el color de reflexion con el de refraccion
                if (colorReflejo != null && colorRefraccion != null) {
                    float factorFresnel = QMath.factorFresnel(V, N, material.getIor());
                    colorEntorno = QMath.mix(colorRefraccion, colorReflejo, factorFresnel);
                    color.set(QMath.mix(color, colorEntorno, (float) Math.min(Math.max(metalness, 0.0f), 1.0f)));
                } else if (colorReflejo != null) {
                    color.set(QMath.mix(color, colorReflejo, (float) Math.min(Math.max(metalness, 0.0f), 1.0f)));
                } else if (colorRefraccion != null) {
                    color.set(QMath.mix(color, colorRefraccion, (float) Math.min(Math.max(metalness, 0.0f), 1.0f)));
                }
            } catch (Exception e) {
                System.out.println("error reflexion " + e.getMessage());
            } finally {

            }
        }
    }

}
