package net.qoopo.engine.water.distortion;

import net.qoopo.engine.core.entity.component.mesh.Mesh;

/**
 * Aplica la distorción de los vértices para la superficie y simular el agua
 */
public interface WaveDistortion  {
    
    public void apply(Mesh mesh);
}
