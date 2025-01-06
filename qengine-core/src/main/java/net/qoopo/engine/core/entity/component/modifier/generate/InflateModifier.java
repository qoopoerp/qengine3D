package net.qoopo.engine.core.entity.component.modifier.generate;

import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.modifier.ModifierComponent;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InflateModifier implements ModifierComponent {

    private static Logger log = Logger.getLogger("inflate-modifier");

    private Entity entity;

    private Mesh cachedMesh = null;

    private float radio = 1.f;

    public InflateModifier(float radio) {
        this.radio = radio;
    }

    @Override
    public void destruir() {

    }

    @Override
    public void apply(Mesh mesh) {
        if (mesh != null
                && (cachedMesh == null || (cachedMesh != null && cachedMesh.getTimeMark() != mesh.getTimeMark()))) {
            inflate(radio, mesh);
            mesh.computeNormals();
            cachedMesh = mesh;
        }
    }

    /**
     * Infla los vertices de forma esferica
     *
     * @param radio
     * @return
     */
    public Mesh inflate(float radio, Mesh mesh) {
        for (Vertex ve : mesh.vertexList) {
            double escala = radio / (Math.sqrt(
                    ve.location.x * ve.location.x + ve.location.y * ve.location.y + ve.location.z * ve.location.z));
            ve.location.set(ve.location.getVector3().multiply((float) escala), 1);
        }
        return mesh;
    }

}
