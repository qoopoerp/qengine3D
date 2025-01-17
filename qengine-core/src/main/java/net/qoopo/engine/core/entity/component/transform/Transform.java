/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.transform;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.Rotation;
import net.qoopo.engine.core.math.QVector3;

/**
 * Representa una transformacion (Traslacion, Escala y Rotacion)
 *
 * @author alberto
 */
@Getter
@Setter
public class Transform implements EntityComponent {

    private Entity entity;

    private final QMatriz4 matriz = new QMatriz4();
    private QVector3 location = QVector3.zero.clone();
    private QVector3 scale = QVector3.unitario_xyz.clone();
    private Rotation rotation = new Rotation(Rotation.ANGULOS_EULER);

    public Transform() {
    }

    public Transform(int tipoRotacion) {
        // rotacion = new QRotacion(tipoRotacion);
        rotation.setTipo(tipoRotacion);
    }

    public void fromMatrix(QMatriz4 matriz) {
        location.set(matriz.toTranslationVector());
        scale.set(matriz.toScaleVector());
        rotation.setCuaternion(matriz.toRotationQuat());
        rotation.updateEuler();
    }

    public QMatriz4 toMatrix() {
        matriz.loadIdentity();
        matriz.setTranslation(location);
        matriz.setRotationCuaternion(rotation.getCuaternion());
        matriz.setScale(scale);
        return matriz;
    }

    @Override
    public Transform clone() {
        Transform nuevo = new Transform();
        try {
            nuevo.getScale().set(scale);
            nuevo.getLocation().set(location);
            // nuevo.getRotation().setEulerAngles(rotation.getEulerAngles().clone());
            nuevo.getRotation().setCuaternion(rotation.getCuaternion().clone());
            nuevo.getRotation().updateEuler();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nuevo;
    }

    public void move(float x, float y, float z) {
        location.set(x, y, z);
    }

    public void move(QVector3 nuevaPosicion) {
        location.set(nuevaPosicion);
    }

    public void scale(float x, float y, float z) {
        scale.set(x, y, z);
    }

    public void scale(QVector3 valor) {
        scale.set(valor);
    }

    @Override
    public void destroy() {
        location = null;
        scale = null;
        rotation = null;
    }

    @Override
    public String toString() {
        return "QTRans {" + "t=" + location + ", e=" + scale + ", r=" + rotation.toString() + '}';
    }

    /**
     * Realiza la interporlacion entre dos trasnforamcion
     *
     * @param origen
     * @param destino
     * @param progresion
     * @return
     */
    public static Transform interpolate(Transform origen, Transform destino, float progresion) {
        Transform nueva = new Transform(Rotation.CUATERNION);
        QMath.interpolateLinear(nueva.location, progresion, origen.location, destino.location);
        QMath.interpolateLinear(nueva.scale, progresion, origen.scale, destino.scale);
        // interpolacion espefira (SLERP)
        // nueva.getRotacion().setCuaternion(new
        // Cuaternion(origen.getRotacion().getCuaternion(),
        // destino.getRotacion().getCuaternion(), progresion));
        // interpolacion normalizada (NLERP) , m[as rapida
        nueva.getRotation().getCuaternion().set(origen.getRotation().getCuaternion());
        nueva.getRotation().getCuaternion().nlerp(destino.getRotation().getCuaternion(), progresion);
        // QVector3 angles = new QVector3();
        // QMath.interpolateLinear(angles, progresion, origen.getRotation().getAngulos().getAngles(), destino.getRotation().getAngulos().getAngles());
        // nueva.getRotation().getAngulos().set(angles);
        return nueva;
    }
}
