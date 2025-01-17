package net.qoopo.engine.core.load.collada.thinmatrix.loader;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import net.qoopo.engine.core.load.collada.thinmatrix.data.MeshData;
import net.qoopo.engine.core.load.collada.thinmatrix.data.Vertex;
import net.qoopo.engine.core.load.collada.thinmatrix.data.VertexSkinData;
import net.qoopo.engine.core.load.collada.thinmatrix.xmlParser.XmlNode;
import net.qoopo.engine.core.math.QColor;

/**
 * Loads the mesh data for a model from a collada XML file.
 *
 * @author Karl
 *
 */
public class GeometryLoader {

//    private static final Matrix4f CORRECTION = new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0, 0));
//    private static final Matrix4f CORRECTION = new Matrix4f().rotX((float) Math.toRadians(-90));
    private final XmlNode meshData;

    private Map<String, BufferedImage> texturas;
    private final List<VertexSkinData> vertexWeights;

    private float[] verticesArray;
    private float[] normalsArray;
    private float[] texturesArray;
    private int[] indicesArray;
    private int[] jointIdsArray;
    private float[] weightsArray;

    List<Vertex> vertices = new ArrayList<Vertex>();
    List<Vector2f> textures = new ArrayList<Vector2f>();
    List<Vector3f> normals = new ArrayList<Vector3f>();
    List<Integer> indices = new ArrayList<Integer>();
    List<QColor> colores = new ArrayList<QColor>();

    public GeometryLoader(XmlNode geometryNode, List<VertexSkinData> vertexWeights) {
        this.vertexWeights = vertexWeights;
        this.meshData = geometryNode.getChild("geometry").getChild("mesh");

    }

    public GeometryLoader(XmlNode geometryNode, List<VertexSkinData> vertexWeights, Map<String, BufferedImage> texturas) {
        this.vertexWeights = vertexWeights;
        this.meshData = geometryNode.getChild("geometry").getChild("mesh");
        this.texturas = texturas;
    }

    public MeshData extractModelData() {
        readRawData();
        assembleVertices();
        removeUnusedVertices();
        initArrays();
        convertDataToArrays();
        convertIndicesListToArray();
        return new MeshData(verticesArray, texturesArray, normalsArray, indicesArray, jointIdsArray, weightsArray);
    }

    private void readRawData() {
        readPositions();
        readNormals();
        readTextureCoords();
        readColor();
    }

    private void readPositions() {
        String positionsId = meshData.getChild("vertices").getChild("input").getAttribute("source").substring(1);
        XmlNode positionsData = meshData.getChildWithAttribute("source", "id", positionsId).getChild("float_array");
        int count = Integer.parseInt(positionsData.getAttribute("count"));
        String[] posData = positionsData.getData().split(" ");
        for (int i = 0; i < count / 3; i++) {
            float x = Float.parseFloat(posData[i * 3]);
            float y = Float.parseFloat(posData[i * 3 + 1]);
            float z = Float.parseFloat(posData[i * 3 + 2]);
            Vector4f position = new Vector4f(x, y, z, 1);
//            Matrix4f.transform(CORRECTION, position, position);
            if (vertexWeights != null) {
                vertices.add(new Vertex(vertices.size(), new Vector3f(position.x, position.y, position.z), vertexWeights.get(vertices.size())));
            } else {
                vertices.add(new Vertex(vertices.size(), new Vector3f(position.x, position.y, position.z), null));
            }
        }
    }

    private void readNormals() {
        try {
            String normalsId = meshData.getChild("polylist").getChildWithAttribute("input", "semantic", "NORMAL").getAttribute("source").substring(1);
            XmlNode normalsData = meshData.getChildWithAttribute("source", "id", normalsId).getChild("float_array");
            int count = Integer.parseInt(normalsData.getAttribute("count"));
            String[] normData = normalsData.getData().split(" ");
            for (int i = 0; i < count / 3; i++) {
                float x = Float.parseFloat(normData[i * 3]);
                float y = Float.parseFloat(normData[i * 3 + 1]);
                float z = Float.parseFloat(normData[i * 3 + 2]);
                Vector4f norm = new Vector4f(x, y, z, 0f);
//            Matrix4f.transform(CORRECTION, norm, norm);
                normals.add(new Vector3f(norm.x, norm.y, norm.z));
            }
        } catch (Exception e) {
            System.out.println("Error al leer normales");
            e.printStackTrace();
        }
    }

    private void readTextureCoords() {
        try {
            String texCoordsId = meshData.getChild("polylist").getChildWithAttribute("input", "semantic", "TEXCOORD").getAttribute("source").substring(1);
            XmlNode texCoordsData = meshData.getChildWithAttribute("source", "id", texCoordsId).getChild("float_array");
            int count = Integer.parseInt(texCoordsData.getAttribute("count"));
            String[] texData = texCoordsData.getData().split(" ");
            for (int i = 0; i < count / 2; i++) {
                float s = Float.parseFloat(texData[i * 2]);
                float t = Float.parseFloat(texData[i * 2 + 1]);
                textures.add(new Vector2f(s, t));
            }
        } catch (Exception e) {
            System.out.println("Error al leer coordenadas de texturas");
            e.printStackTrace();
        }
    }

    private void readColor() {
        try {
            String texCoordsId = meshData.getChild("polylist").getChildWithAttribute("input", "semantic", "COLOR").getAttribute("source").substring(1);
            XmlNode texCoordsData = meshData.getChildWithAttribute("source", "id", texCoordsId).getChild("float_array");
            int count = Integer.parseInt(texCoordsData.getAttribute("count"));
            String[] texData = texCoordsData.getData().split(" ");
            for (int i = 0; i < count / 3; i++) {
                float r = Float.parseFloat(texData[i * 3]);
                float g = Float.parseFloat(texData[i * 3 + 1]);
                float b = Float.parseFloat(texData[i * 3 + 2]);
                colores.add(new QColor(r, g, b));
            }
        } catch (Exception e) {
            System.out.println("Error al leer colores");
            e.printStackTrace();
        }
    }

    private void assembleVertices() {
        XmlNode poly = meshData.getChild("polylist");
        if (poly != null) {
            //poligonos en general
            int typeCount = poly.getChildren("input").size();
            String[] indexData = poly.getChild("p").getData().split(" ");
            for (int i = 0; i < indexData.length / typeCount; i++) {
                int positionIndex = Integer.parseInt(indexData[i * typeCount]);
                int normalIndex = Integer.parseInt(indexData[i * typeCount + 1]);
                int texCoordIndex = Integer.parseInt(indexData[i * typeCount + 2]);
                processVertex(positionIndex, normalIndex, texCoordIndex);
            }
        } else {
            //triangulos
            poly = meshData.getChild("triangles");
            if (poly != null) {
                int typeCount = poly.getChildren("input").size();
                String[] indexData = poly.getChild("p").getData().split(" ");
                for (int i = 0; i < indexData.length / typeCount; i++) {
                    int positionIndex = Integer.parseInt(indexData[i * typeCount]);
                    int normalIndex = Integer.parseInt(indexData[i * typeCount + 1]);
                    int texCoordIndex = Integer.parseInt(indexData[i * typeCount + 2]);
                    processVertex(positionIndex, normalIndex, texCoordIndex);
                }
            }
        }

    }

    private Vertex processVertex(int posIndex, int normIndex, int texIndex) {
        Vertex currentVertex = vertices.get(posIndex);
        if (!currentVertex.isSet()) {
            currentVertex.setTextureIndex(texIndex);
            currentVertex.setNormalIndex(normIndex);
            indices.add(posIndex);
            return currentVertex;
        } else {
            return dealWithAlreadyProcessedVertex(currentVertex, texIndex, normIndex);
        }
    }

    private int[] convertIndicesListToArray() {
        this.indicesArray = new int[indices.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = indices.get(i);
        }
        return indicesArray;
    }

    private float convertDataToArrays() {
        float furthestPoint = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Vertex currentVertex = vertices.get(i);
            if (currentVertex.getLength() > furthestPoint) {
                furthestPoint = currentVertex.getLength();
            }
            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = textureCoord.x;
//            texturesArray[i * 2 + 1] = 1 - textureCoord.y;
            texturesArray[i * 2 + 1] = textureCoord.y;//<ag>
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
            VertexSkinData weights = currentVertex.getWeightsData();
            jointIdsArray[i * 3] = weights.jointIds.get(0);
            jointIdsArray[i * 3 + 1] = weights.jointIds.get(1);
            jointIdsArray[i * 3 + 2] = weights.jointIds.get(2);
            weightsArray[i * 3] = weights.weights.get(0);
            weightsArray[i * 3 + 1] = weights.weights.get(1);
            weightsArray[i * 3 + 2] = weights.weights.get(2);
        }
        return furthestPoint;
    }

    private Vertex dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex, int newNormalIndex) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
            return previousVertex;
        } else {
            Vertex anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex);
            } else {
                Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition(), previousVertex.getWeightsData());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
                return duplicateVertex;
            }

        }
    }

    private void initArrays() {
        this.verticesArray = new float[vertices.size() * 3];
        this.texturesArray = new float[vertices.size() * 2];
        this.normalsArray = new float[vertices.size() * 3];
        this.jointIdsArray = new int[vertices.size() * 3];
        this.weightsArray = new float[vertices.size() * 3];
    }

    private void removeUnusedVertices() {
        for (Vertex vertex : vertices) {
            vertex.averageTangents();
            if (!vertex.isSet()) {
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }

}
