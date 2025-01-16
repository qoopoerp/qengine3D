package net.qoopo.engine.core.entity.component.movement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.engine.EngineTime;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.UpdatableComponent;
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
public class JumpComponent implements EntityComponent, UpdatableComponent {

    private Entity entity;

    // la distancia que va a saltar
    private float height = 3;
    private float currentHeight = 0.f;
    private QVector3 direction = new QVector3(0, 0.01f, 0);
    private float fDirection = 1.0f;

    public JumpComponent(float height) {
        this.height = height;
    }

    @Override
    public void update(RenderEngine renderEngine, Scene scene) {

        if (currentHeight <= 0) {
            direction.set(0, 0.01f, 0);
            fDirection = 1.0f;
        }

        if (currentHeight >= height) {
            direction.set(0, -0.01f, 0);
            fDirection = -1.0f;
        }

        QVector3 jump = direction.clone();
        jump.multiply(EngineTime.deltaMS);
        currentHeight += fDirection * jump.length();

        QVector3 newPosition = entity.getTransform().getLocation();
        newPosition.add(jump);

        entity.move(newPosition);
    }

    @Override
    public void destroy() {

    }

}
