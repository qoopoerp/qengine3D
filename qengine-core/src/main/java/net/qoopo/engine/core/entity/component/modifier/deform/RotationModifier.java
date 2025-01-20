package net.qoopo.engine.core.entity.component.modifier.deform;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.modifier.ModifierComponent;
import net.qoopo.engine.core.math.Cuaternion;
import net.qoopo.engine.core.math.Vector3;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Componente que se encarga de rotar los vértices de una malla
 */
public class RotationModifier implements ModifierComponent {

    private Entity entity;

    private float radiansX = 0.0f;
    private float radiansY = (float) Math.toRadians(0.05f);
    private float radiansZ = 0.0f;

    private Long timeMark = -1L;

    private boolean changed;

    public RotationModifier(Cuaternion cuaternion) {
        Vector3 angles = Vector3.empty();
        this.radiansX = cuaternion.toAngleAxis(angles);
        this.radiansX = angles.x;
        this.radiansY = angles.y;
        this.radiansZ = angles.z;
    }

    public RotationModifier(Vector3 angles) {
        this.radiansX = angles.x;
        this.radiansY = angles.y;
        this.radiansZ = angles.z;
    }

    public RotationModifier(float radiansX, float radiansY, float radiansZ) {
        this.radiansX = radiansX;
        this.radiansY = radiansY;
        this.radiansZ = radiansZ;
    }

    @Override
    public void apply(Mesh mesh) {
        if (mesh != null && timeMark != mesh.getTimeMark()) {
            // inflate(radio, mesh);
            for (Vertex vertex : mesh.vertexList) {
                vertex.location.rotateX(radiansX);
                vertex.location.rotateY(radiansY);
                vertex.location.rotateZ(radiansZ);
            }
            mesh.computeNormals();
            timeMark = mesh.getTimeMark();
            changed=false;
        }
    }

    public void changed() {
        this.changed = true;
    }

    @Override
    public void destroy() {

    }

}
