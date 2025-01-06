/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.procesors;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;

/**
 *
 * @author alberto
 */
public abstract class QProcesador implements EntityComponent {

    @Getter
    @Setter
    private Entity entity;

    public abstract void preProceso();

    public abstract void procesar(RenderEngine render, Scene universo);

    public abstract void postProceso();

}
