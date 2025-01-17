/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas;

import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Triangle;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.math.Vector3;

/**
 * Define una esfera contenedora del objeto
 *
 * @author alberto
 */
public class QColisionTriangulo extends CollisionShape {

    private Vector3 p1;
    private Vector3 p2;
    private Vector3 p3;

    public QColisionTriangulo() {
    }

    public QColisionTriangulo(Triangle triangulo) {
        p1 = Vector3.of(triangulo.vertexList[0]);
        p2 = Vector3.of(triangulo.vertexList[1]);
        p3 = Vector3.of(triangulo.vertexList[2]);
    }

    public QColisionTriangulo(Vector3 p1, Vector3 p2, Vector3 p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Vector3 getP1() {
        return p1;
    }

    public void setP1(Vector3 p1) {
        this.p1 = p1;
    }

    public Vector3 getP2() {
        return p2;
    }

    public void setP2(Vector3 p2) {
        this.p2 = p2;
    }

    public Vector3 getP3() {
        return p3;
    }

    public void setP3(Vector3 p3) {
        this.p3 = p3;
    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean verificarColision(CollisionShape otro) {
        return false;
    }
}
