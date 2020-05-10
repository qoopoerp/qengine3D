package net.qoopo.engine3d.componentes.iluminacion;

import java.awt.Color;
import net.qoopo.engine3d.componentes.QComponente;
import net.qoopo.engine3d.core.math.QColor;

public abstract class QLuz extends QComponente {

    public float energia = 1.5f;
    public QColor color = new QColor(Color.WHITE);
    protected boolean enable = true;

    //es el radio de afectacion de la luz, esto permite no comprobar pixeles que no estan cerca de este radio
    public float radio = 15f;

    //-------------------- ATRIBUTOS PARA LA GENERACIÓN DE LA SOMBRA
    protected boolean proyectarSombras = false;
    protected int resolucionMapaSombra;
    protected boolean sombraDinamica = false;

    // es la distancia que tendra la cámara del mapa de sombras con la cámara del renderer
    // a mayor distancia, mayor cantidad de objetos que pueden entrar en la escena
    //Si es 0 se tomará de la diferencia entre el frustrum lejos y cerca de la cámara
//    protected float radioSombra = 0;

    public QLuz() {

    }

    public QLuz(float energia, QColor color, boolean getNewId, float radio) {
        setAttribute(energia, color, radio);
    }

    public void setAttribute(float energia, QColor color, float radio) {
        this.energia = energia;
        this.color = color;
        this.radio = radio;

        if (this.radio < 0) {
            this.radio = 0;
        }

    }

    public void copyAttribute(QLuz other) {
        color = other.color;
        enable = other.enable;
        energia = other.energia;
    }

    @Override
    public abstract QLuz clone();

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public void destruir() {
        color = null;
    }

    public boolean isProyectarSombras() {
        return proyectarSombras;
    }

    public void setProyectarSombras(boolean proyectarSombras) {
        this.proyectarSombras = proyectarSombras;
    }

    public int getResolucionMapaSombra() {
        return resolucionMapaSombra;
    }

    public void setResolucionMapaSombra(int resolucionMapaSombra) {
        this.resolucionMapaSombra = resolucionMapaSombra;
    }

    public boolean isSombraDinamica() {
        return sombraDinamica;
    }

    public void setSombraDinamica(boolean sombraDinamica) {
        this.sombraDinamica = sombraDinamica;
    }

//    public float getRadioSombra() {
//        return radioSombra;
//    }
//
//    public void setRadioSombra(float radioSombra) {
//        this.radioSombra = radioSombra;
//    }

}
