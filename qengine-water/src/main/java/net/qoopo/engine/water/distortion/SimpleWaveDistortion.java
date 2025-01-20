package net.qoopo.engine.water.distortion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.engine.EngineTime;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.Vector4;
import net.qoopo.engine.core.util.mesh.NormalUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleWaveDistortion implements WaveDistortion {

    private float amplitude = 3.0f;
    private float wavelength = 20.0f;
    private float speed = 1.0f;

    @Override
    public void apply(Mesh mesh) {
        // calcula la distorisión de los vértices con la función de ola

        float deltaTime = EngineTime.deltaNano / 1000f;
        // actualizo los vértices
        for (Vertex vertex : mesh.vertexList) {
            Vector4 original = vertex.location;
            // float wave = amplitude * Mathf.Sin((original.x + time * speed) * (2 *
            // Mathf.PI / wavelength));
            // vertices[i] = new Vector3(original.x, original.y + wave, original.z);
            float wave = amplitude * (float) Math.sin((original.x + deltaTime *
                    speed) * (2 * Math.PI / wavelength));

            vertex.location.y += wave;

        }
        NormalUtil.computeNormals(mesh);
    }

}
