package net.qoopo.engine.core.lwjgl.asset;

import org.lwjgl.assimp.AIBone;

import net.qoopo.engine.core.math.Matrix4;

public class AssimpBone {

    private final int boneId;
    private int boneParentId;
    private final String boneName;
    private Matrix4 offsetMatrix;
    private AIBone aiBone;
    
    //cadena utilizada para ordenar la lista desde los nodos mas cercanos a la raiz hasta los nodos que estan mas debajo en la jerarquia
    private String rutaJerarquia;

    public AssimpBone(int boneId, String boneName, Matrix4 offsetMatrix, AIBone aiBone) {
        this.boneId = boneId;
        this.boneName = boneName;
        this.offsetMatrix = offsetMatrix;
        this.aiBone = aiBone;
    }

    public int getBoneParentId() {
        return boneParentId;
    }

    public void setBoneParentId(int boneParentId) {
        this.boneParentId = boneParentId;
    }

    public int getBoneId() {
        return boneId;
    }

    public String getBoneName() {
        return boneName;
    }

    public Matrix4 getOffsetMatrix() {
        return offsetMatrix;
    }

    public AIBone getAiBone() {
        return aiBone;
    }

    public void setAiBone(AIBone aiBone) {
        this.aiBone = aiBone;
    }

    public String getRutaJerarquia() {
        return rutaJerarquia;
    }

    public void setRutaJerarquia(String rutaJerarquia) {
        this.rutaJerarquia = rutaJerarquia;
    }
    
    

}
