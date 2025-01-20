package net.qoopo.engine.renderer.shader.vertex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VertexShaderOutput {
    private Vertex vertex;
    private Vector3 normal;
    private Vector2 uv;
    private QColor color;
}
