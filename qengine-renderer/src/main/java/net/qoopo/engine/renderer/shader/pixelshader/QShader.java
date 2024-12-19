/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.pixelshader;

import net.qoopo.engine.core.entity.component.ligth.QIluminacion;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPixel;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Fog;

/**
 * Las implementaciones de esta clase permiten el cálculo de color y luz de cada
 * pixel
 *
 * @author alberto
 */
/*
 * Implementaciones: 
 * 0. QSimpleShader. Sombreado simple donde no calcula iluminacion. Solo usa los colores sin textura
 * 1. QFlatShader. Sombreado Flat con caras con aristas visibles.
 * 2. QPhongShader. Sombreado de Phong con caras suaves sin aristas visibles.
 * 3. QTexturaShader. Permite el sombreado de Phong con textures
 * 4. QIluminadoShader. Permite sl sombreado de Phong con textures y cáculo de iluminción con las luces de la escena.
 * 5. QShadowShader. Permite los mismo que las atenriores más el cálculo de sombras.
 * 6. QFullShader. Permite todo lo anterior.
 */
public abstract class QShader {

    protected RenderEngine render;
    protected QIluminacion iluminacion = new QIluminacion();

    //usada en los anteriores, esta pendiente quitar
//    protected float r, g, b;
    protected QColor color = new QColor();//color default, blanco

//    protected float iluminacionDifusa;
//    protected float iluminacionEspecular;
    protected float distanciaLuz;
    protected QVector3 tmpPixelPos = QVector3.empty();
    protected QVector3 vectorLuz = QVector3.empty();
    protected QVector3 tempVector = QVector3.empty();

    protected static final float exponenteGamma = 1.0f / 2.2f;
    protected static final float exposicion = 1.0f;

    public QShader(RenderEngine render) {
        this.render = render;
    }

    public RenderEngine getRender() {
        return render;
    }

    public void setRender(RenderEngine render) {
        this.render = render;
    }

    /**
     * Realiza la correccion de Gamma
     *
     * https://learnopengl.com/Advanced-Lighting/Gamma-Correction
     *
     * @param color
     * @return
     */
    public static QColor corregirGamma(QColor color) {
        QVector3 tmpColor = color.rgb();
        //HDR tonemapping 
        tmpColor.divide(tmpColor.clone().add(QVector3.unitario_xyz));
        // Con ajuste de exposicion 
//        tmpColor = QVector3.unitario_xyz.clone().subtract(QVector3.of((float) Math.exp(-tmpColor.x * exposicion), (float) Math.exp(-tmpColor.y * exposicion), (float) Math.exp(-tmpColor.z * exposicion)));
        //correccion de gamma        
        tmpColor.set(QMath.pow(tmpColor.x, exponenteGamma), QMath.pow(tmpColor.y, exponenteGamma), QMath.pow(tmpColor.z, exponenteGamma));
        //cambia el color
        color.set(color.a, tmpColor.x, tmpColor.y, tmpColor.z);
        return color;
    }

    /**
     *
     * @param pixel
     * @param x
     * @param y
     * @return
     */
    public abstract QColor colorearPixel(QPixel pixel, int x, int y);

    public static QColor calcularNeblina(QColor color, QPixel pixel, Fog neblina) {

        if (!neblina.isActive()) {
            return color;
        }
//        float distance = length(pos);
        float distance = pixel.ubicacion.z;
        float fogFactor = (float) (1.0 / Math.exp((distance * neblina.getDensity()) * (distance * neblina.getDensity())));
        fogFactor = QMath.clamp(fogFactor, 0.0f, 1.0f);

        return new QColor(
                QMath.mix(neblina.getColour().r, color.r, fogFactor) / 2,
                QMath.mix(neblina.getColour().g, color.g, fogFactor) / 2,
                QMath.mix(neblina.getColour().b, color.b, fogFactor) / 2
        );
    }

}
