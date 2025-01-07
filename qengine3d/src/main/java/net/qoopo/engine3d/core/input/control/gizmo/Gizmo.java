/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.core.input.control.gizmo;

import net.qoopo.engine.core.entity.Entity;

/**
 * Los gizmos son los objetos que permitiran la interacción con el mouse para el
 * editor y permite realizar acciones con esos evento como , trasladar, rotar,
 * escalar entre otros.
 *
 * @author alberto
 */
public abstract class Gizmo extends Entity {

    /**
     * La entity que manipula
     */
    protected Entity entity;

    public abstract void mouseMove(float deltaX, float deltaY);

    public void updateLocationGizmo() {

    }

    public Entity getEntidad() {
        return entity;
    }

    public void setEntidad(Entity entity) {
        this.entity = entity;
    }

    public static float getDelta(float deltaX, float deltaY) {
        // return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY)/100.0f;
        return deltaX / 100.0f;
    }

    public static float getDelta(float delta) {
        // return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY)/100.0f;
        return delta / 100.0f;
    }

}
