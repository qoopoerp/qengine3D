package net.qoopo.engine.renderer.raster;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClippedData {
    private List<Vertex> vertexList = new ArrayList<>();
    private List<Vector3> normalList = new ArrayList<>();
    private List<Vector2> uvList = new ArrayList<>();

    public void addVertex(Vertex vertex) {
        vertexList.add(vertex);
    }

    public void addNormal(Vector3 normal) {
        normalList.add(normal);
    }

    public void addUV(Vector2 uv) {
        uvList.add(uv);
    }
}
