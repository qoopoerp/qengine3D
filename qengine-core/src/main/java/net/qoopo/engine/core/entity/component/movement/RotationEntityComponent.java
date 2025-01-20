package net.qoopo.engine.core.entity.component.movement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.engine.EngineTime;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.UpdatableComponent;
import net.qoopo.engine.core.math.Cuaternion;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Componente que se encarga de rotar un objeto para animarlo
 */
public class RotationEntityComponent implements EntityComponent, UpdatableComponent {

    private Entity entity;

    private Vector3 rotation = new Vector3(0, (float) Math.toRadians(5f), 0);

    private Vector3 totalRotation = new Vector3(Float.MIN_VALUE);

    public RotationEntityComponent(Cuaternion cuaternion) {
        cuaternion.toAngleAxis(rotation);
    }

    public RotationEntityComponent(Vector3 angles) {
        this.rotation = angles;
    }

    public RotationEntityComponent(float radiansX, float radiansY, float radiansZ) {
        rotation.set(radiansX, radiansY, radiansZ);
    }

    @Override
    public void update(RenderEngine renderEngine, Scene scene) {
        if (totalRotation.x == Float.MIN_VALUE) {
            totalRotation.set(entity.getTransform().getRotation().getEulerAngles());
        }
        // totalRotation.add(rotation.clone().multiply(EngineTime.deltaS));
        totalRotation.add(EngineTime.deltaS, rotation);
        entity.rotate(totalRotation);
    }

    @Override
    public void destroy() {

    }

}
