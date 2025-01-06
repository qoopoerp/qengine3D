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
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.scene.Camera;

/**
 * Componenete que permite controlar la camara. Permite el modo de primera
 * persona y el modo de orbitar, ademas que agrega el control de teclado
 *
 * @author alberto
 */
public class CameraController implements EntityComponent {

    @Getter
    @Setter
    private Entity entity;

    /**
     * Our absolute min/max ascent (pitch) angle, in radians. This is set at
     * 89.95 degrees to prevent the camera's direction from becoming parallel to
     * the world up vector.
     */
    public static final float ABSOLUTE_MAXASCENT = (float) Math.toRadians(89.95);

    protected float minAscent = -ABSOLUTE_MAXASCENT;
    protected float maxAscent = ABSOLUTE_MAXASCENT;

    private Camera camara;
    private QMouseReceptor mouseReceptor;
    private QTecladoReceptor tecladoReceptor;
    private final QVector3 coordenadasEsfericas = QVector3.empty();
    private final QVector3 target = QVector3.empty();

    public CameraController(Camera camara) {
        this.camara = camara;
        configurar();
    }

    public Camera getCamara() {
        return camara;
    }

    public void setCamara(Camera camara) {
        this.camara = camara;
    }

    public void moverAdelanteAtras(float valor) {
        camara.moveForward(valor); // la camara
        target.add(camara.getDirection().multiply(-valor));

        // la diferencia la suma a los hijos
    }

    /**
     * Mueve el objeto izquierda -derecha
     *
     * @param valor
     */
    public void moverDerechaIzquierda(float valor) {
        camara.moveLeft(valor);
        target.add(camara.getLeft().multiply(valor));
    }

    /**
     * Mueve el objeto izquierda -derecha
     *
     * @param valor
     */
    public void moverArribaAbajo(float valor) {
        camara.moveUp(valor);
        target.add(camara.getUp().multiply(valor));
    }

    public void updateCamera() {
        float distance = camara.getTransformacion().getTraslacion().clone().subtract(target).length();
        // QVector3 ubicacion = QVector3.empty();
        QVector3 ubicacion = QVector3.of(
                distance * (float) (Math.cos(coordenadasEsfericas.x) * Math.sin(coordenadasEsfericas.y)),
                distance * (float) Math.sin(coordenadasEsfericas.x),
                distance * (float) (Math.cos(coordenadasEsfericas.x) * Math.cos(coordenadasEsfericas.y)));

        // QMath.sphericalToCartesian(coordenadasEsfericas, ubicacion);
        camara.lookAtTarget(ubicacion.add(target), target, QVector3.unitario_y);
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
                if (SwingUtilities.isRightMouseButton(evt)) {

                }

                if (SwingUtilities.isMiddleMouseButton(evt)) {
                    if (QInputManager.isShitf() && QInputManager.isCtrl() && !QInputManager.isAlt()) {
                        // rota camara en su propio eje
                        camara.aumentarRotY((float) Math.toRadians(-QInputManager.getDeltaX() / 2));
                        camara.aumentarRotX((float) Math.toRadians(-QInputManager.getDeltaY() / 2));
                    } else if (!QInputManager.isShitf() && !QInputManager.isCtrl() && !QInputManager.isAlt()) {
                        // rota camara

                        coordenadasEsfericas.y += Math.toRadians(-QInputManager.getDeltaX() / 2);
                        coordenadasEsfericas.x += Math.toRadians(QInputManager.getDeltaY() / 2);

                        // coordenadasEsfericas.y = QMath.clamp(coordenadasEsfericas.y, minAscent,
                        // maxAscent);
                        coordenadasEsfericas.y = QMath.rotateNumber(coordenadasEsfericas.y, QMath.TWO_PI);
                        coordenadasEsfericas.x = QMath.rotateNumber(coordenadasEsfericas.x, QMath.TWO_PI);
                        updateCamera();

                    } else if (QInputManager.isShitf() && !QInputManager.isCtrl() && !QInputManager.isAlt()) {
                        // mueve la camara y el target
                        moverDerechaIzquierda(-QInputManager.getDeltaX() / 100.0f);
                        moverArribaAbajo(QInputManager.getDeltaY() / 100.0f);

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

                switch (evt.getKeyCode()) {
                    case KeyEvent.VK_NUMPAD1:
                        if (QInputManager.isCtrl())
                            camara.lookAtTarget(QVector3.of(0, 0, -10), QVector3.zero, QVector3.unitario_y);
                        else
                            camara.lookAtTarget(QVector3.of(0, 0, 10), QVector3.zero, QVector3.unitario_y);
                        break;
                    case KeyEvent.VK_NUMPAD3:
                        if (QInputManager.isCtrl())
                            camara.lookAtTarget(QVector3.of(-10, 0, 0), QVector3.zero, QVector3.unitario_y);
                        else
                            camara.lookAtTarget(QVector3.of(10, 0, 0), QVector3.zero, QVector3.unitario_y);
                        break;
                    case KeyEvent.VK_NUMPAD7:
                        if (QInputManager.isCtrl())
                            camara.lookAtTarget(QVector3.of(0, -20, 0), QVector3.zero, QVector3.unitario_z);
                        else
                            camara.lookAtTarget(QVector3.of(0, 20, 0), QVector3.zero, QVector3.unitario_z.clone().multiply(-1));
                        break;

                    case KeyEvent.VK_NUMPAD5: {
                        float distance = camara.getTransformacion().getTraslacion().clone().subtract(target).length();
                        camara.setOrtogonal(!camara.isOrtogonal());
                        camara.setEscalaOrtogonal(distance);
                        break;
                    }

                    case KeyEvent.VK_Q:
                        if (!QInputManager.isShitf()) {
                            camara.aumentarY(0.2f);
                        } else {
                            camara.aumentarY(0.8f);
                        }

                        break;
                    case KeyEvent.VK_E:
                        if (!QInputManager.isShitf()) {
                            camara.aumentarY(-0.2f);
                        } else {
                            camara.aumentarY(-0.8f);
                        }
                        break;
                    case KeyEvent.VK_W:
                        // ir hacia adelante
                        if (!QInputManager.isShitf()) {
                            camara.moveForward(0.2f);
                        } else {
                            camara.moveForward(0.8f);
                        }
                        break;
                    case KeyEvent.VK_S:
                        if (!QInputManager.isShitf()) {
                            camara.moveForward(-0.2f);
                        } else {
                            camara.moveForward(-0.8f);
                        }
                        break;
                    case KeyEvent.VK_D:
                        // aumentarZ(1);
                        if (!QInputManager.isShitf()) {
                            camara.moveLeft(0.2f);
                        } else {
                            camara.moveLeft(0.8f);
                        }
                        break;
                    case KeyEvent.VK_A:
                        if (!QInputManager.isShitf()) {
                            camara.moveLeft(-0.2f);
                        } else {
                            camara.moveLeft(-0.8f);
                        }
                        break;
                    case KeyEvent.VK_UP:
                        camara.aumentarRotX((float) Math.toRadians(5));
                        break;
                    case KeyEvent.VK_DOWN:
                        camara.aumentarRotX((float) Math.toRadians(-5));
                        break;
                    case KeyEvent.VK_RIGHT:
                        camara.aumentarRotY((float) Math.toRadians(-5));
                        break;
                    case KeyEvent.VK_LEFT:
                        camara.aumentarRotY((float) Math.toRadians(5));
                        break;

                }
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

    public QVector3 getTarget() {
        return target;
    }

}
