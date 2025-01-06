package net.qoopo.engine.renderer.shader.vertex;

import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;

/**
 * Vertex Shader. Realiza los calculos de transformacion de cada vertice
 *
 * @author alberto
 */
public interface VertexShader {

    public Vertex apply(Vertex vertex, QVector3 normal, QVector2 uv, QColor color, QMatriz4 matrizVistaModelo);

}
