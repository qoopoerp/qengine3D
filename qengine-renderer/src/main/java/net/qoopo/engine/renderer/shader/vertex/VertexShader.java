package net.qoopo.engine.renderer.shader.vertex;

import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.Matrix4;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;

/**
 * Vertex Shader. Realiza los calculos de transformacion de cada vertice
 *
 * @author alberto
 */
public interface VertexShader {

    public VertexShaderOutput apply(Vertex vertex, Vector3 normal, Vector2 uv, QColor color, Matrix4 matrizVistaModelo);

}
