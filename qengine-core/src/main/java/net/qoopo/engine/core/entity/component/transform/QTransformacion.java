/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.transform;

import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.QRotacion;
import net.qoopo.engine.core.math.QVector3;

/**
 * Representa una transformacion (Traslacion, Escala y Rotacion)
 *
 * @author alberto
 */
public class QTransformacion extends EntityComponent {

    private final QMatriz4 matriz = new QMatriz4();
    private QVector3 traslacion = QVector3.zero.clone();
    private QVector3 escala = QVector3.unitario_xyz.clone();
    private QRotacion rotacion = new QRotacion(QRotacion.ANGULOS_EULER);

    public QTransformacion() {
    }

    public QTransformacion(int tipoRotacion) {
//        rotacion = new QRotacion(tipoRotacion);
        rotacion.setTipo(tipoRotacion);
    }

    public QVector3 getTraslacion() {
        return traslacion;
    }

    public QVector3 getEscala() {
        return escala;
    }

    public QRotacion getRotacion() {
        return rotacion;
    }

//    public void setRotacion(QRotacion rotacion) {
//        this.rotacion = rotacion;
//    }
    public void desdeMatrix(QMatriz4 matriz) {
        traslacion.set(matriz.toTranslationVector());
        escala.set(matriz.toScaleVector());
        rotacion.setCuaternion(matriz.toRotationQuat());
        rotacion.actualizarAngulos();
    }

    public QMatriz4 toTransformMatriz() {
        matriz.loadIdentity();
        matriz.setTranslation(traslacion);
        matriz.setRotationCuaternion(rotacion.getCuaternion());
        matriz.setScale(escala);
        return matriz;
    }

    @Override
    public QTransformacion clone() {
        QTransformacion nuevo = new QTransformacion();
        try {
            nuevo.getEscala().set(escala);
            nuevo.getTraslacion().set(traslacion);
            nuevo.getRotacion().setAngulos(rotacion.getAngulos().clone());
            nuevo.getRotacion().setCuaternion(rotacion.getCuaternion().clone());
//            nuevo.getRotacion().actualizarAngulos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nuevo;
    }

    public void trasladar(float x, float y, float z) {
        traslacion.set(x, y, z);
    }

    public void trasladar(QVector3 nuevaPosicion) {
        traslacion.set(nuevaPosicion);
    }

    public void escalar(float x, float y, float z) {
        escala.set(x, y, z);
    }

    public void escalar(QVector3 valor) {
        escala.set(valor);
    }

    @Override
    public void destruir() {
        traslacion = null;
        escala = null;
        rotacion = null;
    }

    @Override
    public String toString() {
        return "QTRans {" + "t=" + traslacion + ", e=" + escala + ", r=" + rotacion.toString() + '}';
    }

    /**
     * Realiza la interporlacion entre dos trasnforamcion
     *
     * @param origen
     * @param destino
     * @param progresion
     * @return
     */
    public static QTransformacion interpolar(QTransformacion origen, QTransformacion destino, float progresion) {
        QTransformacion nueva = new QTransformacion(QRotacion.CUATERNION);
        QMath.interpolateLinear(nueva.traslacion, progresion, origen.traslacion, destino.traslacion);
        QMath.interpolateLinear(nueva.escala, progresion, origen.escala, destino.escala);
        //interpolacion espefira (SLERP)
//        nueva.getRotacion().setCuaternion(new Cuaternion(origen.getRotacion().getCuaternion(), destino.getRotacion().getCuaternion(), progresion));
        //interpolacion normalizada (NLERP) , m[as rapida
        nueva.getRotacion().getCuaternion().set(origen.getRotacion().getCuaternion());
        nueva.getRotacion().getCuaternion().nlerp(destino.getRotacion().getCuaternion(), progresion);
        return nueva;
    }
}
