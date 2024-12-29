/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.material.AbstractMaterial;

/**
 *
 * @author alberto
 */
@Getter
@Setter
public abstract class Shape extends Mesh {

    protected AbstractMaterial material;

    public abstract void build();

    /**
     * Resetea la informaión de los vértices en caso de haber sido distorcionados
     */
    public void reset() {
        build();
    }

}
