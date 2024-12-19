/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.ligth;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.util.QGlobal;

/**
 *
 * @author alberto
 */
public class QPointLigth extends QLigth {

    public QPointLigth() {
        super(1.0f, QColor.WHITE, 20.f, true, false);
        this.resolucionMapaSombra = QGlobal.SOMBRAS_OMNI_DIRECCIONAL_MAPA_ANCHO;
    }

    public QPointLigth(float intensidad, QColor color, float radio, boolean proyectarSombras, boolean sombraDinamica) {
        super(intensidad, color, radio, proyectarSombras, sombraDinamica);
        this.resolucionMapaSombra = QGlobal.SOMBRAS_OMNI_DIRECCIONAL_MAPA_ANCHO;
    }

    @Override
    public QLigth clone() {
        QLigth newLight = new QPointLigth(energia, color, radio, proyectarSombras, sombraDinamica);
        newLight.entity = this.entity.clone();
        newLight.setEnable(this.enable);
        newLight.setResolucionMapaSombra(resolucionMapaSombra);
        return newLight;
    }

}
