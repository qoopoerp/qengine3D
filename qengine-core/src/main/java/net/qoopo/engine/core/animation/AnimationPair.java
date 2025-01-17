/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.animation;

import java.io.Serializable;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.transform.Transform;

/**
 *
 * @author alberto
 */
public class AnimationPair implements Serializable {

    private Entity entity;
    private Transform transform;

    public AnimationPair() {
    }

    public AnimationPair(Entity entity, Transform transform) {
        this.entity = entity;
        this.transform = transform;
    }

    public Entity getEntidad() {
        return entity;
    }

    public void setEntidad(Entity entity) {
        this.entity = entity;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transformacion) {
        this.transform = transformacion;
    }

}
