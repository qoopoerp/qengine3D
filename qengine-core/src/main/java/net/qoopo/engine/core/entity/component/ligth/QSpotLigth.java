/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.ligth;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.util.QGlobal;

/**
 *
 * @author alberto
 */
public class QSpotLigth extends QLigth {

    private QVector3 direction = QVector3.of(0, -1, 0);
    private QVector3 directionTransformada = QVector3.of(0, -1, 0);
    private float anguloExterno = (float) Math.toRadians(75.0f);
    private float anguloInterno = (float) Math.toRadians(75.0f);

    public QSpotLigth() {
        this.resolucionMapaSombra = QGlobal.SOMBRAS_FOCOS_MAPA_ANCHO;
    }

    public QSpotLigth(QVector3 direction, float angulo, float anguloInterno) {
        this.direction = direction;
        this.anguloExterno = angulo;
        this.anguloInterno = angulo;
        this.resolucionMapaSombra = QGlobal.SOMBRAS_FOCOS_MAPA_ANCHO;
    }

    public QSpotLigth(float intensidad, QColor color, float radio, QVector3 direction, float angulo, float anguloInterno,
            boolean proyectarSombras, boolean sombraDinamica) {
        super(intensidad, color, radio, proyectarSombras, sombraDinamica);
        this.direction = direction;
        this.anguloExterno = angulo;
        this.anguloInterno = anguloInterno;
        this.resolucionMapaSombra = QGlobal.SOMBRAS_FOCOS_MAPA_ANCHO;
    }

    public QVector3 getDirection() {
        return direction;
    }

    public void setDirection(QVector3 direction) {
        this.direction = direction;
    }

    public void setDirection(float x, float y, float z) {
        this.direction.set(x, y, z);
    }

    public QVector3 getDirectionTransformada() {
        return directionTransformada;
    }

    public void setDirectionTransformada(QVector3 directionTransformada) {
        this.directionTransformada = directionTransformada;
    }

    public float getAnguloExterno() {
        return anguloExterno;
    }

    public void setAnguloExterno(float anguloExterno) {
        this.anguloExterno = anguloExterno;
    }

    public float getAnguloInterno() {
        return anguloInterno;
    }

    public void setAnguloInterno(float anguloInterno) {
        this.anguloInterno = anguloInterno;
    }

    @Override
    public QLigth clone() {
        QSpotLigth newLight = new QSpotLigth(energia, color, radio, direction.clone(), anguloExterno, anguloInterno,
                proyectarSombras, sombraDinamica);
        newLight.entity = this.entity.clone();
        newLight.setEnable(this.enable);
        newLight.setResolucionMapaSombra(resolucionMapaSombra);
        return newLight;
    }

    @Override
    public void destruir() {
        super.destruir();
        direction = null;
    }

}
