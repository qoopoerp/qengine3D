package net.qoopo.engine.core.entity.component.mesh.primitive;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.math.Vector4;

/**
 * Pixel que pertenece al buffer de pixel
 *
 * @author alberto
 */
@Getter
@Setter
public class Fragment implements Serializable {

    private boolean draw = false;
    public float u, v;
    public Vector4 location = new Vector4();
    public Vector3 normal = Vector3.empty();
    
    public Vector3 up = Vector3.empty();
    public Vector3 right = Vector3.empty();

    public AbstractMaterial material;
    public Primitive primitiva;
    public Entity entity;

    // public QMatriz4 matViewModel;

    public Fragment() {
    }

}
