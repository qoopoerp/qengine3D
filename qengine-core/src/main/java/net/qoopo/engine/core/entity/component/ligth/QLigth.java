package net.qoopo.engine.core.entity.component.ligth;

import java.awt.Color;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.shadow.QProcesadorSombra;

public abstract class QLigth implements EntityComponent {

    @Getter
    @Setter
    protected Entity entity;

    public final Vector3 coeficientesAtenuacion = Vector3.of(1, 0.045f, 0.0075f); // PARA UNA DISTANCIA DE 100

    /*
     * VALORES PARA LOS COEFICIENTES DE ATENUACION EN FUNCION DE LA DISTANCIA QUE SE
     * REQUIERA QUE CUBRa la luz
     * Distance Constant Linear Quadratic
     * 7 1.0 0.7 1.8
     * 13 1.0 0.35 0.44
     * 20 1.0 0.22 0.20
     * 32 1.0 0.14 0.07
     * 50 1.0 0.09 0.032
     * 65 1.0 0.07 0.017
     * 100 1.0 0.045 0.0075
     * 160 1.0 0.027 0.0028
     * 200 1.0 0.022 0.0019
     * 325 1.0 0.014 0.0007
     * 600 1.0 0.007 0.0002
     * 3250 1.0 0.0014 0.000007
     * 
     * 
     * 
     */
    public float energy = 1f;
    public QColor color = new QColor(Color.WHITE);
    protected boolean enable = true;

    // es el radio de afectacion de la luz, esto permite no comprobar pixeles que no
    // estan cerca de este radio
    public float radio = 15f;

    // -------------------- ATRIBUTOS PARA LA GENERACIÓN DE LA SOMBRA
    protected boolean proyectarSombras = false;
    protected int resolucionMapaSombra;
    protected boolean sombraDinamica = false;

    // ---------------PROCESADOR DE SOMBRAS----------------------
    protected QProcesadorSombra sombras = null;

    // es la distancia que tendra la cámara del mapa de sombras con la cámara del
    // renderer
    // a mayor distancia, mayor cantidad de objetos que pueden entrar en la escena
    // Si es 0 se tomará de la diferencia entre el frustrum lejos y cerca de la
    // cámara
    // protected float radioSombra = 0;
    public QLigth() {

    }

    public QLigth(float intensidad, QColor color, float radio, boolean proyectarSombras, boolean sombraDinamica) {
        setAtributos(intensidad, color, radio, proyectarSombras, sombraDinamica);
    }

    public void setAtributos(float intensidad, QColor color, float radio, boolean proyectarSombras,
            boolean sombraDinamica) {
        this.energy = intensidad;
        this.color = color;
        this.radio = radio;
        this.proyectarSombras = proyectarSombras;
        this.sombraDinamica = sombraDinamica;

        if (this.radio < 0) {
            this.radio = 0;
        }

    }

    public void copyAttribute(QLigth other) {
        color = other.color;
        enable = other.enable;
        energy = other.energy;
    }

    @Override
    public abstract QLigth clone();

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public void destroy() {
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

    // public float getRadioSombra() {
    // return radioSombra;
    // }
    //
    // public void setRadioSombra(float radioSombra) {
    // this.radioSombra = radioSombra;
    // }
    public QProcesadorSombra getSombras() {
        return sombras;
    }

    public void setSombras(QProcesadorSombra sombras) {
        this.sombras = sombras;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energia) {
        this.energy = energia;
    }

    public QColor getColor() {
        return color;
    }

    public void setColor(QColor color) {
        this.color = color;
    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

}
