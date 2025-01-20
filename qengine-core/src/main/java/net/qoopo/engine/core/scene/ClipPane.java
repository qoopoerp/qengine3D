/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.scene;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.math.Vector4;

/**
 * Esta clase define un plano para realizar clipping de escenas y de esta manera
 * no dibujar lo que este fuera de este plano
 *
 * @author alberto
 */
@Getter
@Setter
public class ClipPane {

    /**
     * La normal de plano Los puntos que esten de este lado seran los visibles
     */
    private Vector3 normal;
    /**
     * La distancia que existe desde el origen al plano
     */
    private float distancia;

    /**
     * Construye un plano a partir de una direccion y la distancia del plano al
     * origen
     *
     * @param direccion
     * @param distancia
     */
    public ClipPane(Vector3 direccion, float distancia) {
        this.normal = direccion;
        normal.normalize();
        this.distancia = distancia;
    }

    /**
     * Construye un plano a partir de 3 vectores de posicion
     *
     * @param pos1
     * @param pos2
     * @param pos3
     */
    public ClipPane(Vector3 pos1, Vector3 pos2, Vector3 pos3) {
        normal = Vector3.of(pos1, pos2);
        normal.cross(Vector3.of(pos2, pos3));
        normal.normalize();
        // QVector3 center = QVector3.empty();
        // center.add(pos1, pos2, pos3);
        // center.multiply(1.0f / 3.0f);
        // this.distancia = center.length();
        // segun https://www.cubic.org/docs/3dclip.htm
        this.distancia = normal.dot(pos1);

        // this.pos1 = pos1;
        // this.pos2 = pos2;
        // this.pos3 = pos3;

    }

    public boolean isVisible(Vector3 posicion) {
        return (posicion.dot(normal) - distancia) > 0;
        // return (posicion.dot(normal) - distancia) < 0;
    }

    public boolean isVisible(Vector4 posicion) {
        return (posicion.getVector3().dot(normal) - distancia) > 0;
        // return (posicion.dot(normal) - distancia) < 0;
    }

    public float distancia(Vector3 posicion) {
        return (posicion.dot(normal) - distancia);
    }

}
