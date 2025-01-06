package net.qoopo.engine.renderer.raster;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClippedData {
    private List<Vertex> vertexList = new ArrayList<>();
    private List<QVector3> normalList = new ArrayList<>();
    private List<QVector2> uvList = new ArrayList<>();

    public void addVertex(Vertex vertex) {
        vertexList.add(vertex);
    }

    public void addNormal(QVector3 normal) {
        normalList.add(normal);
    }

    public void addUV(QVector2 uv) {
        uvList.add(uv);
    }
}
