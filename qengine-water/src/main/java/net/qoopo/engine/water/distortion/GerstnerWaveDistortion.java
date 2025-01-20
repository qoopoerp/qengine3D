package net.qoopo.engine.water.distortion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.engine.EngineTime;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector4;
import net.qoopo.engine.core.util.mesh.NormalUtil;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GerstnerWaveDistortion implements WaveDistortion {

    private Vector2 direction = new Vector2(1.8f, 0.4f);

    private float amplitude = 2.0f;
    private float wavelength = 10.0f;
    private float speed = 1.0f;

    @Override
    public void apply(Mesh mesh) {

        // calcula la distorisión de los vérties con la función de ola

        float k = (float) (2 * Math.PI / wavelength); // Número de onda
        float omega = speed * k; // Frecuencia angular

        float deltaTime = EngineTime.deltaNano / 1000f;

        // actualizo los vértices
        for (Vertex vertex : mesh.vertexList) {
            Vector4 original = vertex.location;

            // chat gtp
            float wave = k * original.x - omega * deltaTime;

            float cos = (float) Math.cos(wave);
            float sin = (float) Math.sin(wave);
            vertex.location.x = (float) (amplitude * cos); // desplazmiento en X
            vertex.location.y = (float) (amplitude * sin); // desplazaminto en Y
            vertex.location.z = (float) (amplitude * cos);// desplazamiento en Z

            // https://web.engr.oregonstate.edu/~mjb/cs557/Handouts/WaveMotion.6pp.pdf

            // https://jorgellorcagonzalvo.wordpress.com/portfolio/modelo-de-olas-mapa-de-alturas/

            // x=posicion.x+Q*A*0.5*sin((2*3.14159*(s-(L/T*t)))/L);
            // y=A*cos((2*Pi*(s-(L/T*t)))/L);
            // z=posicion.z+Q*A*0.5*sin((2*Pi*(s-(L/T*t)))/L);
            // s ditancia al punto
            // A amplitu
            // L longitud de onda
            // T el periodo
            // ---impl
            // float Q=1.0f;
            // float wave=k*(original.x-(wavelength/EngineTime.delta*speed));
            // vertex.location.x=original.x+Q*amplitude*0.5f*(float)Math.sin(wave);
            // vertex.location.y=amplitude*(float)Math.cos(wave);
            // vertex.location.z=original.z+Q*amplitude*0.5f*(float)Math.sin(wave);

        }
        NormalUtil.computeNormals(mesh);
    }

}
