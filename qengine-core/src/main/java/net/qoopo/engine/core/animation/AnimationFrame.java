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
    private float timeMark;
    /**
     * QParAnimacion de paresModificarAnimacion a ser modificados en esta
     * animación
     */
    private List<AnimationPair> animationPairList = new ArrayList<>();

    public AnimationFrame() {

    }

    public AnimationFrame(float timeMark) {
        this.timeMark = timeMark;
    }

    public AnimationFrame(float marcaTiempo, List<AnimationPair> objetos) {
        this.timeMark = marcaTiempo;
        this.animationPairList = objetos;
    }

    public List<AnimationPair> getAnimationPairList() {
        return animationPairList;
    }

    public void setAnimationPairList(List<AnimationPair> paresModificarAnimacion) {
        this.animationPairList = paresModificarAnimacion;
    }

    public void addPair(AnimationPair par) {
        animationPairList.add(par);
    }

    public void removePair(AnimationPair par) {
        animationPairList.remove(par);
    }

    /**
     * Valida si se debe pasar al siguiente frame de acuerdo al marcaTiempo de
     * la animacion
     *
     * @param tiempo
     * @return
     */
    public boolean pasarSiguienteFrame(float tiempo) {
        return tiempo >= this.timeMark;
    }

    public float getTimeMark() {
        return timeMark;
    }

    public void setTimeMark(float marcaTiempo) {
        this.timeMark = marcaTiempo;
    }

    public void destruir() {
        animationPairList.clear();
        animationPairList = null;
    }
  
}
