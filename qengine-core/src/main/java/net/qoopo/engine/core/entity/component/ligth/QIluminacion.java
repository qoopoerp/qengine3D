package net.qoopo.engine.core.entity.component.ligth;

import net.qoopo.engine.core.math.QColor;

public class QIluminacion {

    private QColor colorAmbiente;
    private QColor colorLuz;

    public QIluminacion() {
    }

    public QIluminacion(QColor colorAmbiente, QColor colorLuz) {
        this.colorAmbiente = colorAmbiente;
        this.colorLuz = colorLuz;
    }

    public QColor getColorAmbiente() {
        return colorAmbiente;
    }

    public void setColorAmbiente(QColor colorAmbiente) {
        this.colorAmbiente = colorAmbiente;
    }

    public QColor getColorLuz() {
        return colorLuz;
    }

    public void setColorLuz(QColor colorLuz) {
        this.colorLuz = colorLuz;
    }

}
