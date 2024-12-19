/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.gui;

import net.qoopo.engine.core.entity.component.EntityComponent;

/**
 *
 * @author alberto
 */
public abstract class QTecladoReceptor extends EntityComponent {

    public abstract void keyPressed(java.awt.event.KeyEvent evt);

    public abstract void keyReleased(java.awt.event.KeyEvent evt);

    @Override
    public void destruir() {

    }

}
