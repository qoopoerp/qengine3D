/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.gui;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;

/**
 *
 * @author alberto
 */
public abstract class QTecladoReceptor implements EntityComponent {

    @Getter
    @Setter
    protected Entity entity;

    public abstract void keyPressed(java.awt.event.KeyEvent evt);

    public abstract void keyReleased(java.awt.event.KeyEvent evt);

    @Override
    public void destruir() {

    }

}
