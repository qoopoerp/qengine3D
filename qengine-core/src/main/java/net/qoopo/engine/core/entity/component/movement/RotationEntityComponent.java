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
import net.qoopo.engine.core.math.QVector3;
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

    private float radiansX = 0.0f;
    private float radiansY = (float) Math.toRadians(0.01f);
    private float radiansZ = 0.0f;

    private float totalX = 0;
    private float totalY = 0;
    private float totalZ = 0;

    public RotationEntityComponent(Cuaternion cuaternion) {
        QVector3 angles = QVector3.empty();
        this.radiansX = cuaternion.toAngleAxis(angles);
        this.radiansX = angles.x;
        this.radiansY = angles.y;
        this.radiansZ = angles.z;
    }

    public RotationEntityComponent(QVector3 angles) {
        this.radiansX = angles.x;
        this.radiansY = angles.y;
        this.radiansZ = angles.z;

    }

    public RotationEntityComponent(float radiansX, float radiansY, float radiansZ) {
        this.radiansX = radiansX;
        this.radiansY = radiansY;
        this.radiansZ = radiansZ;
    }

    @Override
    public void update(RenderEngine renderEngine, Scene scene) {

        totalX += radiansX * EngineTime.deltaMS;
        totalY += radiansY * EngineTime.deltaMS;
        totalZ += radiansZ * EngineTime.deltaMS;
        entity.rotate(totalX, totalY, totalZ);
    }

    @Override
    public void destroy() {

    }

}
