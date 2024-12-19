/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.animation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alberto
 */
public class AnimationFrame implements Serializable {

    /**
     * Marca de tiempo en segundos
     */
    private float marcaTiempo;
    /**
     * QParAnimacion de paresModificarAnimacion a ser modificados en esta
     * animación
     */
    private List<AnimationPair> paresModificarAnimacion = new ArrayList<>();

    public AnimationFrame() {

    }

    public AnimationFrame(float marcaTiempo) {
        this.marcaTiempo = marcaTiempo;
    }

    public AnimationFrame(float marcaTiempo, List<AnimationPair> objetos) {
        this.marcaTiempo = marcaTiempo;
        this.paresModificarAnimacion = objetos;
    }

    public List<AnimationPair> getParesModificarAnimacion() {
        return paresModificarAnimacion;
    }

    public void setParesModificarAnimacion(List<AnimationPair> paresModificarAnimacion) {
        this.paresModificarAnimacion = paresModificarAnimacion;
    }

    public void agregarPar(AnimationPair par) {
        paresModificarAnimacion.add(par);
    }

    public void eliminarPar(AnimationPair par) {
        paresModificarAnimacion.remove(par);
    }

    /**
     * Valida si se debe pasar al siguiente frame de acuerdo al marcaTiempo de
     * la animacion
     *
     * @param tiempo
     * @return
     */
    public boolean pasarSiguienteFrame(float tiempo) {
        return tiempo >= this.marcaTiempo;
    }

    public float getMarcaTiempo() {
        return marcaTiempo;
    }

    public void setMarcaTiempo(float marcaTiempo) {
        this.marcaTiempo = marcaTiempo;
    }

    public void destruir() {
        paresModificarAnimacion.clear();
        paresModificarAnimacion = null;
    }
  
}
