package net.qoopo.engine.water.distortion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.engine.EngineTime;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.math.Vector4;
import net.qoopo.engine.core.util.mesh.NormalUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RadialWavesDistortion implements WaveDistortion {

    private Vector3 origin = Vector3.of(0f, 0f, 0.f);
    private float amplitude = 3.0f; // amplitud inicial de la ola
    private float wavelength = 20.0f; // longitud de onda
    private float speed = 1.0f; // velocidad de propagacion
    private float decay = 100.0f; // Factor de atenuación (mayor = más lenta)

    @Override
    public void apply(Mesh mesh) {

        // calcula la distorisión de los vérties con la función de ola

        float k = (float) (2 * Math.PI / wavelength); // Número de onda
        float omega = speed * k; // Frecuencia angular

        float deltaTime = EngineTime.deltaNano / 1000f;

        // actualizo los vértices
        for (Vertex vertex : mesh.vertexList) {
            Vector4 original = vertex.location;

            // Calcula la distancia al centro de las olas
            float distance = Vector3.of(original.x, 0, original.z)
                    .add(Vector3.of(origin.x, 0, origin.z).multiply(-1.0f)).length();
            // Atenuación de la amplitud en función de la distancia
            float attenuatedAmplitude = amplitude * (float) Math.exp(-distance / decay);

            // Calcula el desplazamiento de la ola
            // float wave = amplitude * (float)Math.sin(k * distance - omega * deltaTime);
            float wave = attenuatedAmplitude * (float) Math.sin(k * distance - omega * deltaTime);

            vertex.location.y += wave; // desplazaminto en Y
        }
        NormalUtil.computeNormals(mesh);
    }

}
