package net.qoopo.engine.core.load.collada.thinmatrix.data;
//package net.qoopo.motor3d.core.carga.impl.collada.dataStructures;

/**
 * Contains the extracted data for an animated model, which includes the mesh
 * data, and skeleton (joints heirarchy) data.
 *
 * @author Karl
 *
 */
public class AnimatedModelData {

    private final SkeletonData joints;
    private final MeshData mesh;

    public AnimatedModelData(MeshData mesh, SkeletonData joints) {
        this.joints = joints;
        this.mesh = mesh;
    }

    public SkeletonData getJointsData() {
        return joints;
    }

    public MeshData getMeshData() {
        return mesh;
    }

}
