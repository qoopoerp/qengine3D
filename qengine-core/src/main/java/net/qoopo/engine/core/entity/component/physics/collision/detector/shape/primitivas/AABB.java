/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas;

import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;

/**
 * Define una AABB (Axis aligned bounding boxes) Es una caja virtual que
 * envuelve un objeto y es utilizada para el detector de colisiones
 *
 * @author alberto
 */
public class AABB extends CollisionShape {

    public Vertex aabMinimo;
    public Vertex aabMaximo;

    public AABB() {
        // aabMinimo= new QVertice();
        // aabMaximo= new QVertice();
    }

    public AABB(Vertex aabMinimo, Vertex aabMaximo) {
        this.aabMinimo = aabMinimo;
        this.aabMaximo = aabMaximo;
    }

    @Override
    public boolean verificarColision(CollisionShape otro) {

        boolean b = false;

        // System.out.println("Preguntando verificarColision AABB_1");
        // System.out.println(" minimo " + aabMinimo.toString());
        // System.out.println(" maximo " + aabMaximo.toString());
        //
        // System.out.println(" AABB_2");
        // System.out.println(" minimo " + otro.aabMinimo.toString());
        // System.out.println(" maximo " + otro.aabMaximo.toString());
        // System.out.println("");
        // System.out.println("");
        // validar contra otro AABB
        if (otro instanceof AABB) {
            b = this.aabMaximo.location.x > ((AABB) otro).aabMinimo.location.x
                    && this.aabMinimo.location.x < ((AABB) otro).aabMaximo.location.x
                    && this.aabMaximo.location.y > ((AABB) otro).aabMinimo.location.y
                    && this.aabMinimo.location.y < ((AABB) otro).aabMaximo.location.y
                    && this.aabMaximo.location.z > ((AABB) otro).aabMinimo.location.z
                    && this.aabMinimo.location.z < ((AABB) otro).aabMaximo.location.z;
        }

        return b;

    }

    @Override
    public void destruir() {

    }
}
