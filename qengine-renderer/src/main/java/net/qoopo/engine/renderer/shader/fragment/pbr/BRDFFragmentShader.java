/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment.pbr;

import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
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
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.TextureUtil;
import net.qoopo.engine.core.texture.procesador.MipmapTexture;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.TempVars;
import net.qoopo.engine.renderer.shader.fragment.FragmentShader;
import net.qoopo.engine.renderer.util.TransformationVectorUtil;

/**
 * Sombreado PBR
 *
 * https://www.youtube.com/watch?v=5p0e7YNONr8
 *
 * https://learnopengl.com/PBR/Theory
 * https://learnopengl.com/PBR/Lighting
 *
 * @author alberto
 */
public class BRDFFragmentShader extends FragmentShader {

    public BRDFFragmentShader(RenderEngine render) {
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
        computeNormal(fragment);
        computeBRDF(fragment, color, getColorBase(fragment, x, y));

        // ***********************************************************
        // ****** TRANSPARENCIA
        // ***********************************************************

        Material material = (Material) fragment.material;
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
     * https://learnopengl.com/PBR/Theory
     * https://learnopengl.com/PBR/Lighting
     * 
     *
     * @param fragment
     */
    protected void computeBRDF(Fragment fragment, QColor outputColor, QColor colorBase) {

        float roughness = 0;
        float metallic = 0;
        float ao = 1.0f;

        Material material = (Material) fragment.material;

        TempVars tv = TempVars.get();
        try {

            // *********************************************************************************************
            // ****** VECTOR VISION
            // *********************************************************************************************
            QVector3 V = fragment.ubicacion.getVector3();
            V.invert();// Cam-WordPos
            V.normalize();

            // //
            // *********************************************************************************************
            // // ****** VECTOR VISION
            // //
            // *********************************************************************************************
            // // para obtener el vector vision quitamos la transformacion de la ubicacion y
            // // volvemos a calcularla en las coordenadas del mundo
            // // tm.vector3f1.set(currentPixel.ubicacion.getVector3());
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

            // el vector F0 debe tener los valores segun el nivel de metalico
            /*
             * Material F0 (Linear) F0 (sRGB)
             * Water (0.02, 0.02, 0.02) (0.15, 0.15, 0.15)
             * Plastic / Glass (Low) (0.03, 0.03, 0.03) (0.21, 0.21, 0.21)
             * Plastic High (0.05, 0.05, 0.05) (0.24, 0.24, 0.24)
             * Glass (high) / Ruby (0.08, 0.08, 0.08) (0.31, 0.31, 0.31)
             * Diamond (0.17, 0.17, 0.17) (0.45, 0.45, 0.45)
             * Iron (0.56, 0.57, 0.58) (0.77, 0.78, 0.78)
             * Copper (0.95, 0.64, 0.54) (0.98, 0.82, 0.76)
             * Gold (1.00, 0.71, 0.29) (1.00, 0.86, 0.57)
             * Aluminium (0.91, 0.92, 0.92) (0.96, 0.96, 0.97)
             * Silver (0.95, 0.93, 0.88) (0.98, 0.97, 0.95)
             * 
             */
            roughness = material.getRoughness();
            metallic = material.getMetallic();

            if (render.opciones.isMaterial() && material.getMetallicMap() != null) {
                metallic = material.getMetallicMap().getQColor(fragment.u, fragment.v).r;
            }
            if (render.opciones.isMaterial() && material.getRoughnessMap() != null) {
                roughness = material.getRoughnessMap().getQColor(fragment.u, fragment.v).r;
            }
            if (render.opciones.isMaterial() && material.getAoMap() != null) {
                ao = material.getAoMap().getQColor(fragment.u, fragment.v).r;
            }

            QVector3 albedo = colorBase.rgb();
            // calcula la reflectancia como normal incidente; Si es material diaeletrico,
            // como el plastico, usa la refletividad base (0.04f),
            // si es metalico usa el color albeto como reflectividad base, esto quiere decir
            // que el color resultante (del reflejo) se mescla con el color base
            QVector3 F0 = QVector3.of(0.04f, 0.04f, 0.04f);
            F0 = QMath.mix(F0, albedo, metallic);

            float NdotV = Math.max(N.dot(V), 0.00001f);

            QVector3 kS = QMath.fresnelSchlick(NdotV, F0, roughness);
            QVector3 kD = QVector3.of(1.0f)
                    .subtract(kS)
                    .multiply(1.0f - metallic);

            // ecuacion reflectancia
            QVector3 ambient = new QVector3();
            QVector3 Lo = QVector3.zero.clone();

            Texture enviromentMap = material.getEnvMap();
            Texture irradianceMap = material.getHdrMap();

            if (enviromentMap == null && irradianceMap != null) {
                enviromentMap = irradianceMap;
            }

            if (enviromentMap != null && irradianceMap == null) {
                irradianceMap = enviromentMap;
            }

            computeBrdfDirectLighting(fragment, roughness, metallic, albedo, F0, V, N, N2, kS, kD, Lo);

            if (enviromentMap != null) {
                computeBrdfIBL(fragment, roughness, metallic, albedo, F0, V, N, N2, kS, kD,
                        enviromentMap, irradianceMap, ao, ambient);
            } else {
                QVector3 ambientColor = render.getScene().getAmbientColor().rgb();

                if (material.getEmissiveMap() != null && render.opciones.isMaterial()) {
                    QColor colorEmisivo = material.getEmissiveMap().getQColor(fragment.u,
                            fragment.v);
                    ambientColor.set(colorEmisivo.add(render.getScene().getAmbientColor()).rgb());
                } else {
                    // si tiene factor de emision toma ese valor solamente
                    if (material.getEmision() > 0) {
                        QColor colorEmisivo = new QColor(material.getEmision(),
                                material.getEmision(),
                                material.getEmision());
                        ambientColor.set(colorEmisivo.add(render.getScene().getAmbientColor()).rgb());
                    }
                }
                ambient = ambientColor.multiply(albedo).multiply(ao);
            }
            // vec3 color = ambient + Lo;
            QVector3 tmpColor = ambient.add(Lo);

            // HDR tonemapping
            tmpColor.divide(tmpColor.clone().add(QVector3.unitario_xyz));

            outputColor.set(1.0f, tmpColor.x, tmpColor.y, tmpColor.z);

            // // Realiza la correccion de Gamma
            // // https://learnopengl.com/Advanced-Lighting/Gamma-Correction
            // outputColor.fixGamma();
        } finally {
            tv.release();
        }
    }

    /** Calcula la iluminación */
    protected void computeBrdfDirectLighting(Fragment fragment, float roughness, float metallic, QVector3 albedo,
            QVector3 F0,
            QVector3 V, QVector3 N, QVector3 N2, QVector3 kS, QVector3 kD, QVector3 Lo) {

        Material material = (Material) fragment.material;
        QVector3 vectorLuz = QVector3.empty();

        float NdotV = Math.max(N.dot(V), 0.00001f);

        float distanciaLuz;
        float sombra = 1;// 1= no sombra
        // solo si hay luces y si las opciones de la vista tiene activado el material
        if (render.opciones.isMaterial() && !render.getLitgths().isEmpty()) {
            for (QLigth luz : render.getLitgths()) {
                // si esta encendida
                if (luz != null && luz.getEntity().isToRender() && luz.isEnable()) {
                    sombra = 1;
                    float alfa = 0.0f;
                    // QProcesadorSombra proc = luz.getSombras();
                    // if (proc != null && render.opciones.isSombras() &&
                    // material.isSombrasRecibir()) {
                    // sombra = proc.factorSombra(TransformationVectorUtil
                    // .transformarVectorInversa(fragment.ubicacion, fragment.entity,
                    // render.getCamara())
                    // .getVector3(), fragment.entity);
                    // }

                    if (luz instanceof QPointLigth || luz instanceof QSpotLigth) {
                        vectorLuz.set(fragment.ubicacion.getVector3().clone().subtract(
                                TransformationVectorUtil.transformarVector(QVector3.zero, luz.getEntity(),
                                        render.getCamera())));
                        // solo toma en cuenta a los puntos q estan en el area de afectacion
                        if (vectorLuz.length() > luz.radio) {
                            continue;
                        }
                        // si es Spot valido que este dentro del cono
                        if (luz instanceof QSpotLigth) {
                            QVector3 coneDirection = ((QSpotLigth) luz).getDirectionTransformada().normalize();
                            alfa = coneDirection.angulo(vectorLuz.clone().normalize());
                            if (alfa > ((QSpotLigth) luz).getAnguloExterno()) {
                                continue;
                            }
                        }
                    } else if (luz instanceof QDirectionalLigth) {
                        vectorLuz.set(((QDirectionalLigth) luz).getDirectionTransformada());
                    }

                    // calcula la radiacion por cada luz
                    // L=normalize(lightPositions[i] -WorldPos);
                    QVector3 L = vectorLuz.clone().invert().normalize();
                    // H= normalize(V + L);
                    QVector3 H = V.clone().add(L).normalize();
                    float NdotL = Math.max(N.dot(L), 0.00001f);
                    // float HdotV = Math.max(H.dot(V), 0.00f);
                    float NdotH = Math.max(N.dot(H), 0.00f);
                    // float distance = length(lightPositions[i] - WorldPos);
                    // distanciaLuz = vectorLuz.length();
                    distanciaLuz = L.length();
                    float attenuation = 1.0f / (distanciaLuz * distanciaLuz);
                    // float attenuation = 1.0f
                    // / (luz.coeficientesAtenuacion.x + luz.coeficientesAtenuacion.y * distanciaLuz
                    // + luz.coeficientesAtenuacion.z * distanciaLuz * distanciaLuz);

                    // vec3 radiance = lightColors[i] * attenuation;
                    // QVector3 radiacion = luz.color.rgb().multiply(attenuation);
                    QVector3 radiacion = luz.color.rgb().multiply(luz.energia * attenuation);
                    // QVector3 radiacion = luz.color.rgb().multiply(luz.energia *
                    // NdotL).multiply(attenuation);
                    // si la luz es spot, realiza una atenuacion adicional dependiendo del angulo
                    if (luz instanceof QSpotLigth) {
                        // lo siguiente es.. la diferencia entre alfa y el angulo externo divido con la
                        // diferencia entre el angulo interno y el angulo externo (se agrega una
                        // validacion para no permitir la division por cero)
                        radiacion
                                .multiply(QMath.clamp(
                                        (alfa - ((QSpotLigth) luz).getAnguloExterno())
                                                / Math.min(((QSpotLigth) luz).getAnguloInterno()
                                                        - ((QSpotLigth) luz).getAnguloExterno(), -0.0001f),
                                        0.0f, 1.0f));
                    }
                    // cook-torrance brdf (BRDF or bidirectional reflective distribution function )
                    // La ecuacion de BRDF es --> fr=kdflambert+ksfcook−torrance
                    // La parte difusa es color dividido por PI. --> kdflambert=c/π
                    // La parte especular es fCookTorrance=DFG/(4*(ωo⋅n)(ωi⋅n) )
                    // DFG son funciones, D= funcion de distribucion Normal (NDF), F= ecuacion
                    // Fresnel, G= funcion de Geometria

                    // ---- Parte especular fCookTorrance=DFG/(4*(ωo⋅n)(ωi⋅n) )
                    // D=NDF (Función de distribución normal)
                    float D = QMath.DistributionGGX(NdotH, roughness); // N y H
                    // float G = QMath.GeometrySmith(NdotV, NdotL, roughness); // N, V, L
                    float G = QMath.GeometrySmith(N, V, L, roughness); // N, V, L
                    // QVector3 F = QMath.fresnelSchlick(HdotV, F0); // H,V, F0
                    // F - kS (componente especular -> fresnelSchlick)
                    QVector3 numerator = kS.clone().multiply(D * G);
                    float denominator = 4.0f * NdotV * NdotL; // N, V -- N ,L
                    QVector3 specularBrdfCookTorrance = numerator.multiply(1.0f / Math.max(denominator, 0.00001f));

                    // para la conservacion de energia la luz difusa y especular no pueden estar
                    // sobre 1.0f (a menos que la superficie emita luz );
                    // para preservar esta relacion el componenete de luz difusa (kD) deberia ser
                    // igual a 1.0f -kS (componenete especular)
                    // QVector3 kD = QVector3.of(1.0f).subtract(F);
                    // F es igual al componenete especular (kS)
                    // multiplicamos kD por el inverso del factor metalico solo los no metales tiene
                    // luz difusa, o una mezcla lineal si es parcialmente metalico
                    // kD.multiply(1.0f - metallic);

                    // sombra
                    // brdfCookTorrance.multiply(sombra);
                    // kD.multiply(sombra);

                    // add to outgoing radiance Lo
                    // notar que :
                    // 1) el angulo de la luz a la superficie afecta especular, no solo a la luz
                    // difusa
                    // 2) mezclamos albedo con difusa, pero no con expecular
                    // Lo += (kD * albedo / PI + specular) * radiance * NdotL;
                    Lo.add(
                            kD.clone().multiply(albedo.clone().multiply(1.0f / QMath.PI))
                                    .add(specularBrdfCookTorrance)
                                    .multiply(radiacion)
                                    .multiply(NdotL));
                }
            }
        }
    }

    /**
     * (IBL - Image Based Limunation)
     * consigue el ambiente del mapa de entorno (si lo tiene)
     * 
     * @param fragment
     * @param roughness
     * @param metallic
     * @param albedo
     * @param F0
     * @param V
     * @param N
     * @param N2
     * @param kS
     * @param kD
     * @param enviromentMap
     * @param irradianceMap
     * @param ao
     * @param ambient
     */
    private void computeBrdfIBL(Fragment fragment, float roughness, float metallic, QVector3 albedo, QVector3 F0,
            QVector3 V, QVector3 N, QVector3 N2, QVector3 kS, QVector3 kD, Texture enviromentMap, Texture irradianceMap,
            float ao, QVector3 ambient) {

        Material material = (Material) fragment.material;
        float NdotV = Math.max(N.dot(V), 0.00001f);

        /*
         * vec3 F = FresnelSchlickRoughness(max(dot(N, V), 0.0), F0, roughness);
         * vec3 kS = F;
         * vec3 kD = 1.0 - kS;
         * kD *= 1.0 - metallic;
         * vec3 irradiance = texture(irradianceMap, N).rgb;
         * vec3 diffuse = irradiance * albedo;
         * const float MAX_REFLECTION_LOD = 4.0;
         * vec3 prefilteredColor = textureLod(prefilterMap, R, roughness *
         * MAX_REFLECTION_LOD).rgb;
         * vec2 envBRDF = texture(brdfLUT, vec2(max(dot(N, V), 0.0), roughness)).rg;
         * vec3 specular = prefilteredColor * (F * envBRDF.x + envBRDF.y);
         * vec3 ambient = (kD * diffuse + specular) * ao;
         */

        // toma el color del mapa de irradiacion usando el vector nomal (en el espacio
        // mundial) (en el mapa de reflejos se usa el vector reflejado)
        QVector3 irradiacion = TextureUtil
                .getEnviromentMapColor(N2, irradianceMap, material.getEnvMapType()).rgb();

        // irradiacion.multiply(5.0f);// elevo la luminosidad
        QVector3 difuso = albedo.multiply(irradiacion);
        if (enviromentMap instanceof MipmapTexture) {
            float niveles = (float) ((MipmapTexture) enviromentMap).getMaxLevel();
            ((MipmapTexture) enviromentMap).setLevel((int) (niveles * roughness));
        }
        QColor[] enviromentColours = computeEnviroment(fragment, N2, V, enviromentMap);
        QColor colorReflejo = enviromentColours[0];
        QColor colorRefraccion = enviromentColours[1];
        // QVector3 specular = colorReflejo.rgb().multiply(kS);
        // https://www.unrealengine.com/en-US/blog/physically-based-shading-on-mobile
        QVector3 specular = QVector3.unitario_xyz;
        if (colorReflejo != null) {
            specular = colorReflejo.rgb().multiply(QMath.EnvBRDFApprox(kS, roughness, NdotV));
        }
        // en caso de tener refraccion como el vidrio
        if (colorRefraccion != null) {
            // difuso.multiply(colorRefraccion.rgb());
            difuso.add(colorRefraccion.rgb());
        }

        ambient.set(difuso.multiply(kD).add(specular).multiply(ao));
    }

    /**
     * Calcula la Reflexión y Refracción utilizando un mapa de entorno (puede
     * ser generado con un mapa de cubo)
     *
     * @param fragment
     */
    private QColor[] computeEnviroment(Fragment fragment, QVector3 N2, QVector3 V, Texture enviromentMap) {
        QColor colorReflejo = null;
        QColor colorRefraccion = null;
        Material material = (Material) fragment.material;
        if (render.opciones.isMaterial() && enviromentMap != null) {

            try {
                // ************************************************************
                // ****** REFLEXION
                // ************************************************************
                // if (material.isReflexion()) {
                QVector3 reflejo = QMath.reflejarVector(V, N2);
                colorReflejo = TextureUtil.getEnviromentMapColor(reflejo, enviromentMap, material.getEnvMapType());
                // } else {
                // colorReflejo = null;
                // }
                // ***********************************************************
                // ****** REFRACCION
                // ***********************************************************
                if (material.getIor() > 0) {
                    QVector3 refraccion = QMath.refractarVector(V, N2, material.getIor());
                    colorRefraccion = TextureUtil.getEnviromentMapColor(refraccion, enviromentMap,
                            material.getEnvMapType());
                } else {
                    colorRefraccion = null;
                }
            } catch (Exception e) {
                System.out.println("error reflexion " + e.getMessage());
                e.printStackTrace();
            } finally {

            }

        }
        return new QColor[] { colorReflejo, colorRefraccion };
    }

}
