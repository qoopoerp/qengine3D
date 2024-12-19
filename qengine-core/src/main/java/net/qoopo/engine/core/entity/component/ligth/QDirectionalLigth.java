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
public class QDirectionalLigth extends QLigth {

    private QVector3 direction = QVector3.of(0, -1, 0);
    private QVector3 directionTransformada = QVector3.of(0, -1, 0);

    public QDirectionalLigth(QVector3 direccion) {
        this.direction = direccion;
        this.resolucionMapaSombra = QGlobal.SOMBRAS_DIRECCIONAL_MAPA_ANCHO;
        coeficientesAtenuacion.set(1.0f, 0.0f, 0.0f);
    }

    public QDirectionalLigth(float intensidad, QColor color, float radio, boolean proyectarSombras,
            boolean sombraDinamica) {
        super(intensidad, color, radio, proyectarSombras, sombraDinamica);
        this.resolucionMapaSombra = QGlobal.SOMBRAS_DIRECCIONAL_MAPA_ANCHO;
        coeficientesAtenuacion.set(1.0f, 0.0f, 0.0f);
    }

    public QDirectionalLigth(float energia, QColor color, float radio, QVector3 direccion, boolean proyectarSombras,
            boolean sombraDinamica) {
        super(energia, color, radio, proyectarSombras, sombraDinamica);
        this.direction = direccion;
        this.resolucionMapaSombra = QGlobal.SOMBRAS_DIRECCIONAL_MAPA_ANCHO;
        coeficientesAtenuacion.set(1.0f, 0.0f, 0.0f);
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

    @Override
    public QLigth clone() {
        QLigth newLight = new QDirectionalLigth(energia, color, radio, direction.clone(), proyectarSombras, sombraDinamica);
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
