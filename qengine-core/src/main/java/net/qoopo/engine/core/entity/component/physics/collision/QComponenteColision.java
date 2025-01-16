/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.physics.collision;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionEsfera;
import net.qoopo.engine.core.entity.component.transform.Transform;
import net.qoopo.engine.core.math.QVector3;

/**
 *
 * @author alberto
 */
public class QComponenteColision implements EntityComponent {

    @Getter
    @Setter
    protected Entity entity;

    public static final int TIPO_CONTENEDOR_ESFERA = 1;
    public static final int TIPO_CONTENEDOR_AABB = 2;
    public static final int TIPO_COMPLEJO_TRIANGULO = 3;

    public int tipoContenedorColision = TIPO_CONTENEDOR_ESFERA;

    public boolean tieneColision;// indica si entro en verificarColision

    // Se divide al objeto en partes mas pequeñas y se calcula un formaColision para
    // esa parte y asi se tiene
    // varios contenedores mas acordes a la silueta del objeto
    public List<CollisionShape> listaContenedores;
    public CollisionShape formaColision = null; // Caja invisible que envuelve al objeto. Se usa para detectar
                                                // verificarColision contra la caja que envuelve a otro objeto

    public QComponenteColision() {

    }

    public void setFormaColision(CollisionShape contenedor) {
        this.formaColision = contenedor;
    }

    public boolean verificarColision(QComponenteColision otro) {
        // PRIMER PASO VERIFICAR COLISION CON LOS CONTENEDORES
        // AYUDA A DESCARTAR RAPIDAMENTE OBJETOS NO CERCANOS
        if (formaColision != null && otro != null) {
            tieneColision = this.formaColision.verificarColision(otro.formaColision);
        }

        return tieneColision;
    }

    public void crearContenedorColision(Mesh geometria, Transform transformacion) {
        switch (tipoContenedorColision) {
            case TIPO_CONTENEDOR_ESFERA:
                crearEsfera(geometria, transformacion);
                break;
            case TIPO_CONTENEDOR_AABB:
                crearAABB(geometria, transformacion);
                break;
        }
    }

    /**
     * Crea una figura contenedora tipo AABB
     *
     * @param geometria
     * @param transformacion
     */
    public void crearAABB(Mesh geometria, Transform transformacion) {
        this.formaColision = new AABB(geometria.vertexList[0].clone(), geometria.vertexList[0].clone());

        AABB tmp = (AABB) formaColision;
        // this.formaColision = new AABB(this.vertices[0], this.vertices[0]);

        // el siguiente metodo funciona para todas las formas
        // pero seria mejro que existan clases que sobrecarguen este metodo de acuerdo a
        // una geometria predefinida
        for (Vertex vertice : geometria.vertexList) {
            if (vertice.location.x < tmp.aabMinimo.location.x) {
                tmp.aabMinimo.location.x = vertice.location.x;
            }
            if (vertice.location.y < tmp.aabMinimo.location.y) {
                tmp.aabMinimo.location.y = vertice.location.y;
            }
            if (vertice.location.z < tmp.aabMinimo.location.z) {
                tmp.aabMinimo.location.z = vertice.location.z;
            }
            if (vertice.location.x > tmp.aabMaximo.location.x) {
                tmp.aabMaximo.location.x = vertice.location.x;
            }
            if (vertice.location.y > tmp.aabMaximo.location.y) {
                tmp.aabMaximo.location.y = vertice.location.y;
            }
            if (vertice.location.z > tmp.aabMaximo.location.z) {
                tmp.aabMaximo.location.z = vertice.location.z;
            }

        }

        // actualizo la coordenads con la posicion del objeto en el espacio tambien
        tmp.aabMinimo.location.x += transformacion.getLocation().x;
        tmp.aabMinimo.location.y += transformacion.getLocation().y;
        tmp.aabMinimo.location.z += transformacion.getLocation().z;

        tmp.aabMaximo.location.x += transformacion.getLocation().x;
        tmp.aabMaximo.location.y += transformacion.getLocation().y;
        tmp.aabMaximo.location.z += transformacion.getLocation().z;

        // System.out.println("AAAB Calculado " + ((AABB) formaColision).aabMinimo + " -
        // " + ((AABB) formaColision).aabMaximo);
    }

    public void crearEsfera(Mesh geometria, Transform transformacion) {
        Vertex centro = null;
        float radio = 0;
        // primero calculamos el rectangulo AABB para obtener el centro y radio
        crearAABB(geometria, transformacion);
        QVector3 tmp = QVector3.of(((AABB) formaColision).aabMinimo, ((AABB) formaColision).aabMaximo);

        radio = tmp.length() * 0.5f;// el radio es igual a la mitad de la diagonal de cubo

        tmp.multiply(0.5f);
        centro = new Vertex(tmp.x, tmp.y, tmp.z, 1);

        // this.formaColision = new SphereContenedor(centro, radio);
        this.formaColision = new QColisionEsfera(radio);
    }

    @Override
    public void destroy() {
        if (listaContenedores != null) {
            listaContenedores.clear();
            listaContenedores = null;
        }
    }

}
