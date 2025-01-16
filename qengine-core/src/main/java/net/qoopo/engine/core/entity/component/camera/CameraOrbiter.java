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
 * Componenete que permite controlar la camara con el mouse, orbitar la camara
 * alrededor de un objetivo
 *
 * @author alberto
 */
public class CameraOrbiter implements EntityComponent {

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

    public CameraOrbiter(Camera camara) {
        this.camara = camara;
        configurar();
    }

    public Camera getCamara() {
        return camara;
    }

    public void setCamara(Camera camara) {
        this.camara = camara;
    }

    public void updateCamera() {
        float distance = camara.getTransform().getLocation().clone().subtract(target).length();
        // QVector3 ubicacion = QVector3.empty();
        QVector3 ubicacion = QVector3.of(
                distance * (float) (Math.cos(coordenadasEsfericas.x) * Math.sin(coordenadasEsfericas.y)),
                distance * (float) Math.sin(coordenadasEsfericas.x),
                distance * (float) (Math.cos(coordenadasEsfericas.x) * Math.cos(coordenadasEsfericas.y)));

        // QMath.sphericalToCartesian(coordenadasEsfericas, ubicacion);
        camara.lookAtTarget(ubicacion.add(target), target, QVector3.unitario_y);
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
                    if (!QInputManager.isShitf() && !QInputManager.isCtrl() && !QInputManager.isAlt()) {
                        // rota camara
                        coordenadasEsfericas.y += Math.toRadians(-QInputManager.getDeltaX() / 2);
                        coordenadasEsfericas.x += Math.toRadians(QInputManager.getDeltaY() / 2);
                        // coordenadasEsfericas.y = QMath.clamp(coordenadasEsfericas.y, minAscent,
                        // maxAscent);
                        coordenadasEsfericas.y = QMath.rotateNumber(coordenadasEsfericas.y, QMath.TWO_PI);
                        coordenadasEsfericas.x = QMath.rotateNumber(coordenadasEsfericas.x, QMath.TWO_PI);
                        updateCamera();

                    } else if (QInputManager.isShitf() && !QInputManager.isCtrl() && !QInputManager.isAlt()) {
                        // mueve la camara
                        moverDerechaIzquierda(-QInputManager.getDeltaX() / 100.0f);
                        moverArribaAbajo(QInputManager.getDeltaY() / 100.0f);
                        // mueve el target
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
            public void destroy() {

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
    public void destroy() {
        QInputManager.removeMouseListener(mouseReceptor);
        QInputManager.removeKeyboardListener(tecladoReceptor);
    }

    public QVector3 getTarget() {
        return target;
    }

}
