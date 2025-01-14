package net.qoopo.engine.core.entity.component;

import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;

/**
 * Representaun componente que requiere ser actualizado en cada frame
 */
public interface UpdatableComponent {
    public default boolean isRequiereUpdate() {
        return true;
    }

    public void update(RenderEngine renderEngine, Scene scene);
}

