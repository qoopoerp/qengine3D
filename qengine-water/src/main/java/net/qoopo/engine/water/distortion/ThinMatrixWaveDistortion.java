package net.qoopo.engine.water.distortion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.engine.EngineTime;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.QVector4;
import net.qoopo.engine.core.util.mesh.NormalUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThinMatrixWaveDistortion implements WaveDistortion {

    private float amplitude = 1.0f;
    private float wavelength = 5.0f;
    private float speed = 2.0f;

    @Override
    public void apply(Mesh mesh) {
        // calcula la distorisión de los vérties con la función de ola
        float deltaTime = EngineTime.deltaNano / 1000f;
        float waveTime = deltaTime * speed;
        // actualizo los vértices
        for (Vertex vertex : mesh.vertexList) {
            QVector4 original = vertex.location;

            // de thinmatrix
            float radiansX = (float) ((original.x / wavelength + waveTime) * 2.0f * Math.PI);
            float radiansZ = (float) ((original.z / wavelength + waveTime) * 2.0f * Math.PI);
            float distortion = (float) (amplitude * 0.5f * (Math.sin(radiansZ) + Math.cos(radiansX)));
            vertex.location.add(distortion);
            // vertex.location.y=vertex.location.y+distortion;

        }
        NormalUtil.computeNormals(mesh);
    }

}
