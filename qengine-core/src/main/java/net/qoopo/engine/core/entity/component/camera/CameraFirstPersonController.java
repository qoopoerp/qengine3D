/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.camera;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.gui.QMouseReceptor;
import net.qoopo.engine.core.entity.component.gui.QTecladoReceptor;
import net.qoopo.engine.core.input.QInputManager;
import net.qoopo.engine.core.scene.Camera;

/**
 * Componenete que permite controlar la camara con el mouse en modo de primera
 * persona
 * 
 * @author alberto
 */
public class CameraFirstPersonController implements EntityComponent {

    @Getter
    @Setter
    private Entity entity;

    private Camera camara;
    private QMouseReceptor mouseReceptor;
    private QTecladoReceptor tecladoReceptor;

    public CameraFirstPersonController(Camera camara) {
        this.camara = camara;
        configurar();
    }

    public Camera getCamara() {
        return camara;
    }

    public void setCamara(Camera camara) {
        this.camara = camara;
    }

    private void configurar() {
        mouseReceptor = new QMouseReceptor() {

            @Override
            public void mouseEntered(MouseEvent evt) {

            }

            @Override
            public void mousePressed(MouseEvent evt) {
                if (SwingUtilities.isLeftMouseButton(evt)) {

                }
            }

            @Override
            public void mouseReleased(MouseEvent evt) {

            }

            @Override
            public void mouseDragged(MouseEvent evt) {

                if (SwingUtilities.isLeftMouseButton(evt)) {

                }

                if (SwingUtilities.isMiddleMouseButton(evt)) {
                    if (QInputManager.isShitf() && QInputManager.isCtrl() && !QInputManager.isAlt()) {
                        // rota camara en su propio eje
                        camara.aumentarRotY((float) Math.toRadians(-QInputManager.getDeltaX() / 2));
                        camara.aumentarRotX((float) Math.toRadians(-QInputManager.getDeltaY() / 2));
                    } else if (QInputManager.isShitf() && !QInputManager.isCtrl() && !QInputManager.isAlt()) {
                        // mueve la camara
                        camara.moveLeft(-QInputManager.getDeltaX() / 100.0f);
                        camara.moveUp(QInputManager.getDeltaY() / 100.0f);
                    }
                }
                QInputManager.warpMouse(evt.getXOnScreen(), evt.getYOnScreen());
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent evt) {
                if (evt.getWheelRotation() < 0) {
                    if (!QInputManager.isShitf()) {
                        camara.moveForward(0.2f);
                    } else {
                        camara.moveForward(1f);
                    }
                } else {
                    if (!QInputManager.isShitf()) {
                        camara.moveForward(-0.2f);
                    } else {
                        camara.moveForward(-1f);
                    }
                }

            }

            @Override
            public void mouseMoved(MouseEvent evt) {

            }

            @Override
            public void destruir() {

            }
        };

        tecladoReceptor = new QTecladoReceptor() {
            @Override
            public void keyPressed(KeyEvent evt) {

            }

            @Override
            public void keyReleased(KeyEvent evt) {

            }
        };

        QInputManager.addMouseListener(mouseReceptor);
        QInputManager.addKeyboardListener(tecladoReceptor);
    }

    @Override
    public void destruir() {
        QInputManager.removeMouseListener(mouseReceptor);
        QInputManager.removeKeyboardListener(tecladoReceptor);
    }

}
