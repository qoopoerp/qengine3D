/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.core.sky;

import net.qoopo.engine.core.entity.Entity;

/**
 *
 * @author alberto
 */
public abstract class QCielo {

    protected Entity entity;

    public abstract void setRazon(float razon);

//    protected List<Q
    public Entity getEntidad() {
        return entity;
    }

    public void setEntidad(Entity entity) {
        this.entity = entity;
    }
}
