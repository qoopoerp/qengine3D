/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.material.AbstractMaterial;

/**
 *
 * @author alberto
 */
public abstract class QShape extends Mesh {

    protected AbstractMaterial material;

    public abstract void build();

    public AbstractMaterial getMaterial() {
        return material;
    }

    public void setMaterial(AbstractMaterial material) {
        this.material = material;
    }

}
