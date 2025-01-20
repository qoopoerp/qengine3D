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
import net.qoopo.engine.core.math.Matrix4;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.texture.Texture;
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
            Vector3 V = fragment.location.getVector3();
            V.invert();// Cam-WordPos
            V.normalize();

            // V = TransformationVectorUtil.transformarVector(
            //         TransformationVectorUtil.transformarVectorInversa(fragment.location,
            //                 fragment.entity,
            //                 render.getCamera()),
            //         fragment.entity).getVector3();
            // // ahora restamos la posicion de la camara a la posicion del mundo
            // V.subtract(render.getCamera().getMatrizTransformacion(QGlobal.time).toTranslationVector());
            // V.normalize();

            // *********************************************************************************************
            // ****** VECTOR NORMAL
            // *********************************************************************************************
            Vector3 N = fragment.normal;
            N.normalize();

            // N = (TransformationVectorUtil.transformarVectorNormal(
            //         TransformationVectorUtil.transformarVectorNormalInversa(fragment.normal,
            //                 fragment.entity,
            //                 render.getCamera()),
            //         fragment.entity != null ? fragment.entity.getMatrizTransformacion(QGlobal.time)
            //                 : Matrix4.IDENTITY));
            // N.normalize();

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
            metallic = material.getMetalness();

            if (render.opciones.isMaterial() && material.getMetallicMap() != null) {
                metallic = material.getMetallicMap().getQColor(fragment.u, fragment.v).r;
            }
            if (render.opciones.isMaterial() && material.getRoughnessMap() != null) {
                roughness = material.getRoughnessMap().getQColor(fragment.u, fragment.v).r;
            }
            if (render.opciones.isMaterial() && material.getAoMap() != null) {
                ao = material.getAoMap().getQColor(fragment.u, fragment.v).r;
            }

            Vector3 albedo = colorBase.rgb();
            // calcula la reflectancia como normal incidente; Si es material diaeletrico,
            // como el plastico, usa la refletividad base (0.04f),
            // si es metalico usa el color albeto como reflectividad base, esto quiere decir
            // que el color resultante (del reflejo) se mescla con el color base
            Vector3 F0 = Vector3.of(0.04f, 0.04f, 0.04f);
            F0 = QMath.mix(F0, albedo, metallic);

            float NdotV = Math.max(N.dot(V), 0.00001f);

            Vector3 kS = QMath.fresnelSchlick(NdotV, F0, roughness);
            Vector3 kD = Vector3.of(1.0f)
                    .subtract(kS)
                    .multiply(1.0f - metallic);

            // ecuacion reflectancia
            Vector3 ambientLigth = Vector3.empty();
            Vector3 directLigth = Vector3.empty();

            Texture enviromentMap = material.getEnvMap();
            Texture irradianceMap = material.getHdrMap();

            // if (enviromentMap == null && irradianceMap != null) {
            // enviromentMap = irradianceMap;
            // }

            // if (enviromentMap != null && irradianceMap == null) {
            // irradianceMap = enviromentMap;
            // }

            computeBrdfDirectLighting(fragment, roughness, metallic, albedo, F0, V, N, kS, kD, directLigth);

            Vector3 ambientColor = render.getScene().getAmbientColor().rgb();
            Vector3 emissionLigth;
            // usa el mapa de iluminacion con el ambiente
            if (material.getEmissiveMap() != null && render.opciones.isMaterial()) {
                emissionLigth = material.getEmissiveMap().getQColor(fragment.u, fragment.v).rgb().multiply(albedo);
            } else {
                // si tiene factor de emision toma ese valor solamente
                float factorEmision = material.getEmissionIntensity();
                emissionLigth = albedo.clone().multiply(factorEmision);
            }

            // IBL
            if (enviromentMap != null) {
                // ****** VECTOR VISION
                // para obtener el vector vision quitamos la transformacion de la ubicacion y
                // volvemos a calcularla en las coordenadas del mundo
                // V = TransformationVectorUtil.transformarVector(
                // TransformationVectorUtil.transformarVectorInversa(fragment.location,
                // fragment.entity,
                // render.getCamera()),
                // fragment.entity).getVector3();
                // // ahora restamos la posicion de la camara a la posicion del mundo
                // V.subtract(render.getCamera().getMatrizTransformacion(QGlobal.time).toTranslationVector());
                // V.normalize();

                // // ****** VECTOR NORMAL
                // // la normal del pixel en el espacio mundial, se usara para tomar el color
                // del
                // // mapa de irradiacion, quitamos la transformacion de la ubicacion y volvemos
                // a
                // // calcularla en las coordenadas del mundo *
                // N = (TransformationVectorUtil.transformarVectorNormal(
                // TransformationVectorUtil.transformarVectorNormalInversa(fragment.normal,
                // fragment.entity,
                // render.getCamera()),
                // fragment.entity != null ?
                // fragment.entity.getMatrizTransformacion(QGlobal.time)
                // : Matrix4.IDENTITY));
                // N.normalize();

                // NdotV = Math.max(N.dot(V), 0.00001f);

                // kS = QMath.fresnelSchlick(NdotV, F0, roughness);
                // kD = Vector3.of(1.0f)
                // .subtract(kS)
                // .multiply(1.0f - metallic);

                computeBrdfIBL(fragment, roughness, metallic, albedo, F0, V, N, kS, kD,
                        enviromentMap, irradianceMap, ao, ambientLigth);
            } else {
                ambientLigth = ambientColor.multiply(albedo).multiply(ao);
            }
            // vec3 color = ambient + Lo;
            Vector3 tmpColor = ambientLigth.add(directLigth).add(emissionLigth);

            // HDR tonemapping
            tmpColor.divide(tmpColor.clone().add(Vector3.unitario_xyz));

            outputColor.set(1.0f, tmpColor.x, tmpColor.y, tmpColor.z);

            // // Realiza la correccion de Gamma
            // // https://learnopengl.com/Advanced-Lighting/Gamma-Correction
            // outputColor.fixGamma();
        } finally {
            tv.release();
        }
    }

    /** Calcula la iluminación */
    protected void computeBrdfDirectLighting(Fragment fragment, float roughness, float metallic, Vector3 albedo,
            Vector3 F0, Vector3 V, Vector3 N, Vector3 kS, Vector3 kD, Vector3 Lo) {

        Material material = (Material) fragment.material;

        float NdotV = Math.max(N.dot(V), 0.00001f);

        // solo si hay luces y si las opciones de la vista tiene activado el material
        if (render.opciones.isMaterial() && !render.getLitgths().isEmpty()) {
            for (QLigth luz : render.getLitgths()) {
                // si esta encendida
                if (luz != null && luz.getEntity().isToRender() && luz.isEnable()) {
                    float alfa = 0.0f;
                    // float sombra = 1;// 1= no sombra
                    // QProcesadorSombra proc = luz.getSombras();
                    // if (proc != null && render.opciones.isSombras() &&
                    // material.isSombrasRecibir()) {
                    // sombra = proc.factorSombra(TransformationVectorUtil
                    // .transformarVectorInversa(fragment.ubicacion, fragment.entity,
                    // render.getCamara())
                    // .getVector3(), fragment.entity);
                    // }

                    Vector3 vectorLuz = Vector3.empty();
                    float distanciaLuz;
                    float attenuation = 1.0f;
                    if (luz instanceof QPointLigth || luz instanceof QSpotLigth) {
                        vectorLuz.set(fragment.location.getVector3().clone().subtract(TransformationVectorUtil
                                .transformarVector(Vector3.zero, luz.getEntity(), render.getCamera())));
                        // solo toma en cuenta a los puntos q estan en el area de afectacion
                        if (vectorLuz.length() > luz.radio) {
                            continue;
                        }
                        // si es Spot valido que este dentro del cono
                        if (luz instanceof QSpotLigth) {
                            Vector3 coneDirection = ((QSpotLigth) luz).getDirectionTransformada().normalize();
                            alfa = coneDirection.angulo(vectorLuz.clone().normalize());
                            if (alfa > ((QSpotLigth) luz).getAnguloExterno()) {
                                continue;
                            }
                        }

                        // float distance = length(lightPositions[i] - WorldPos);
                        distanciaLuz = vectorLuz.length();
                        // distanciaLuz = L.length();
                        // float attenuation = 1.0f / (distanciaLuz * distanciaLuz);
                        attenuation = 1.0f
                                / (luz.coeficientesAtenuacion.x + luz.coeficientesAtenuacion.y * distanciaLuz
                                        + luz.coeficientesAtenuacion.z * distanciaLuz * distanciaLuz);
                        vectorLuz.invert();
                    } else if (luz instanceof QDirectionalLigth) {
                        vectorLuz.set(((QDirectionalLigth) luz).getDirectionTransformada());
                        // vectorLuz.set(TransformationVectorUtil.transformarVectorNormal(vectorLuz,
                        // luz.getEntity().getMatrizTransformacion(QGlobal.time)));
                        vectorLuz.invert();
                    }

                    // calcula la radiacion por cada luz
                    // L=normalize(lightPositions[i] -WorldPos);
                    Vector3 L = vectorLuz.normalize();
                    // H= normalize(V + L);
                    Vector3 H = V.clone().add(L).normalize();
                    float NdotL = Math.max(N.dot(L), 0.00001f);
                    float HdotV = Math.max(H.dot(V), 0.00f);
                    float NdotH = Math.max(N.dot(H), 0.00f);

                    // vec3 radiance = lightColors[i] * attenuation;
                    // QVector3 radiacion = luz.color.rgb().multiply(attenuation);
                    Vector3 radiacion = luz.color.rgb().multiply(luz.energy * attenuation);
                    // QVector3 radiacion = luz.color.rgb().multiply(luz.energia );
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

                    // QVector3 difusseBRDF = kD.clone().multiply(albedo.clone().multiply(1.0f /
                    // QMath.PI));
                    Vector3 difusseBRDF = kD.clone().multiply(albedo);

                    // ---- Parte especular fCookTorrance=FDG/(4*(ωo⋅n)(ωi⋅n) )
                    Vector3 F = QMath.fresnelSchlick(HdotV, F0); // H,V, F0
                    // D=NDF (Función de distribución normal) // Calculate normal distribution for
                    // specular BRDF.
                    float D = QMath.DistributionGGX(NdotH, roughness); // N y H
                    // Calculate geometric attenuation for specular BRDF.
                    // float G = QMath.GeometrySmith(NdotV, NdotL, roughness); // N, V, L
                    float G = QMath.GeometrySmith(N, V, L, roughness); // N, V, L

                    // F - kS (componente especular -> fresnelSchlick)
                    // QVector3 numerator = kS.clone().multiply(D * G);
                    Vector3 numerator = F.multiply(D * G);
                    float denominator = 4.0f * NdotV * NdotL; // N, V -- N ,L
                    Vector3 specularBrdfCookTorrance = numerator.multiply(1.0f / Math.max(denominator, 0.00001f));

                    // sombra
                    // brdfCookTorrance.multiply(sombra);
                    // kD.multiply(sombra);

                    // add to outgoing radiance Lo
                    // notar que :
                    // 1) el angulo de la luz a la superficie afecta especular, no solo a la luz
                    // difusa
                    // 2) mezclamos albedo con difusa, pero no con expecular
                    // Lo += (kD * albedo / PI + specular) * radiance * NdotL;
                    Lo.add(difusseBRDF.add(specularBrdfCookTorrance)
                            .multiply(radiacion)
                            .multiply(NdotL));
                }
            }
        }

        Lo.set(Math.max(Lo.x, 0.0f), Math.max(Lo.y, 0.0f), Math.max(Lo.z, 0.0f));
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
     * 
     * @param kS
     * @param kD
     * @param enviromentMap
     * @param irradianceMap
     * @param ao
     * @param ambient
     */
    private void computeBrdfIBL(Fragment fragment, float roughness, float metallic, Vector3 albedo, Vector3 F0,
            Vector3 V, Vector3 N, Vector3 kS, Vector3 kD, Texture enviromentMap, Texture irradianceMap,
            float ao, Vector3 ambient) {

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

        Vector3 irradiance;
        if (irradianceMap != null)
            irradiance = irradianceMap.getColor(N).rgb();
        else
            irradiance = Vector3.empty();

        // irradiacion.multiply(5.0f);// elevo la luminosidad
        Vector3 diffuseIBL = albedo.multiply(irradiance);
        if (enviromentMap instanceof MipmapTexture) {
            float niveles = (float) ((MipmapTexture) enviromentMap).getMaxLevel();
            ((MipmapTexture) enviromentMap).setLevel((int) (niveles * roughness));
        }

        QColor specularColor;
        QColor colorRefraccion;

        // ************************************************************
        // ****** REFLEXION
        // ************************************************************
        // if (material.isReflexion()) {
        Vector3 reflejo = QMath.reflejarVector(V, N);
        specularColor = enviromentMap.getColor(reflejo);
        // } else {
        // colorReflejo = null;
        // }
        // ***********************************************************
        // ****** REFRACCION
        // ***********************************************************
        if (material.getIor() > 0) {
            Vector3 refraccion = QMath.refractarVector(V, N, material.getIor());
            colorRefraccion = enviromentMap.getColor(refraccion);
        } else {
            colorRefraccion = null;
        }

        // https://www.unrealengine.com/en-US/blog/physically-based-shading-on-mobile
        Vector3 specularBRDF = Vector3.unitario_xyz;
        if (specularColor != null) {
            specularBRDF = specularColor.rgb().multiply(QMath.EnvBRDFApprox(kS,
                    roughness,
                    NdotV));
            // specularBRDF = specularColor.rgb().multiply(QMath.EnvBRDFApprox(F0,
            // roughness, NdotV));
        }
        // en caso de tener refraccion como el vidrio
        if (colorRefraccion != null) {
            // difuso.multiply(colorRefraccion.rgb());
            diffuseIBL.add(colorRefraccion.rgb());
        }

        ambient.set(diffuseIBL.multiply(kD).add(specularBRDF).multiply(ao));
    }

}
