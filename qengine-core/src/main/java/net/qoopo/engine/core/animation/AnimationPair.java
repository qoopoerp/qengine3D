/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.animation;

import java.io.Serializable;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.transform.QTransformacion;

/**
 *
 * @author alberto
 */
public class AnimationPair implements Serializable {

    private Entity entity;
    private QTransformacion transformacion;

    public AnimationPair() {
    }

    public AnimationPair(Entity entity, QTransformacion transformacion) {
        this.entity = entity;
        this.transformacion = transformacion;
    }

    public Entity getEntidad() {
        return entity;
    }

    public void setEntidad(Entity entity) {
        this.entity = entity;
    }

    public QTransformacion getTransformacion() {
        return transformacion;
    }

    public void setTransformacion(QTransformacion transformacion) {
        this.transformacion = transformacion;
    }

}
