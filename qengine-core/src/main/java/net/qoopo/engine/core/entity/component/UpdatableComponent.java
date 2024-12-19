package net.qoopo.engine.core.entity.component;

import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;

/**
 * REpresentaun compoenete que requiere ser actualizado en cada frame
 */
public interface UpdatableComponent {
    public default boolean isRequierepdate() {
        return true;
    }

    public void update(RenderEngine renderEngine, Scene scene);
}

