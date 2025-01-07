package net.qoopo.engine.core.entity.component.mesh.primitive;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.math.QVector4;

/**
 * Pixel que pertenece al buffer de pixel
 *
 * @author alberto
 */
@Getter
@Setter
public class Fragment implements Serializable {

    private boolean dibujar = false;
    public float u, v;
    public QVector4 ubicacion = new QVector4();
    public QVector3 normal = QVector3.empty();
    
    public QVector3 up = QVector3.empty();
    public QVector3 right = QVector3.empty();

    public AbstractMaterial material;
    public Primitive primitiva;
    public Entity entity;

    // public QMatriz4 matViewModel;

    public Fragment() {
    }

}
