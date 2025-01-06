/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.detector;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;

/**
 * Envoltorio que Detecta una colisión
 *
 * @author alberto
 */
//CollisionShape
public abstract class CollisionShape implements EntityComponent {

    @Getter
    @Setter
    private Entity entity;

    public abstract boolean verificarColision(CollisionShape otro);
}
