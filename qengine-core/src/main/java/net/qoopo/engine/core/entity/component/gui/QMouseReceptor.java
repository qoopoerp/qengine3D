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
public abstract class QMouseReceptor extends EntityComponent {

    public abstract void mouseEntered(java.awt.event.MouseEvent evt);

    public abstract void mousePressed(java.awt.event.MouseEvent evt);

    public abstract void mouseReleased(java.awt.event.MouseEvent evt);

    public abstract void mouseDragged(java.awt.event.MouseEvent evt);

    public abstract void mouseWheelMoved(java.awt.event.MouseWheelEvent evt);

    public abstract void mouseMoved(java.awt.event.MouseEvent evt);

}
