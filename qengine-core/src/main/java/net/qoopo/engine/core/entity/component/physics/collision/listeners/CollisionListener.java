/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.listeners;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;

/**
 *
 * @author alberto
 */
public abstract class CollisionListener implements EntityComponent {

    @Getter
    @Setter
    private Entity entity;

    public abstract void colision(Entity ob1, Entity ob2);

    @Override
    public void destruir() {
    }

}
