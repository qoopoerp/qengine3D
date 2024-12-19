package net.qoopo.engine.core.renderer;

import net.qoopo.engine.core.renderer.superficie.Superficie;
import net.qoopo.engine.core.scene.Scene;

/**
 * Crea instancias de rendererer 
 */

public interface RendererFactory {
    
    public RenderEngine createRenderEngine(Scene escena, String nombre, Superficie superficie, int ancho, int alto);
}
