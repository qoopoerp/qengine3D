package net.qoopo.engine.core.load.collada.thinmatrix.data;

import java.util.List;

public class SkinningData {

    public final List<String> jointOrder;
    public final List<VertexSkinData> verticesSkinData;

    public SkinningData(List<String> jointOrder, List<VertexSkinData> verticesSkinData) {
        this.jointOrder = jointOrder;
        this.verticesSkinData = verticesSkinData;
    }

}
