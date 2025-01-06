package net.qoopo.engine.core.export.obj;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Clase para representar un vértice
class Vertex {
    float x, y, z;

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return String.format("v %.6f %.6f %.6f", x, y, z);
    }
}

// Clase para representar una normal
class Normal {
    float nx, ny, nz;

    public Normal(float nx, float ny, float nz) {
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
    }

    @Override
    public String toString() {
        return String.format("vn %.6f %.6f %.6f", nx, ny, nz);
    }
}

// Clase para representar una coordenada UV
class UV {
    float u, v;

    public UV(float u, float v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public String toString() {
        return String.format("vt %.6f %.6f", u, v);
    }
}

// Clase para representar una cara
class Face {
    List<Integer> vertexIndices;
    List<Integer> normalIndices;
    List<Integer> uvIndices;

    public Face(List<Integer> vertexIndices, List<Integer> normalIndices, List<Integer> uvIndices) {
        this.vertexIndices = vertexIndices;
        this.normalIndices = normalIndices;
        this.uvIndices = uvIndices;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("f");
        for (int i = 0; i < vertexIndices.size(); i++) {
            sb.append(" ")
                    .append(vertexIndices.get(i));
            if (uvIndices != null && i < uvIndices.size()) {
                sb.append("/").append(uvIndices.get(i));
            }
            if (normalIndices != null && i < normalIndices.size()) {
                sb.append("/").append(normalIndices.get(i));
            }
        }
        return sb.toString();
    }
}

// Clase que genera los datos de la tetera de Utah proceduralmente
class UtahTeapot {
    private static final float[][][] PATCHES = {
            // Parche de ejemplo, se deben añadir todos los parches para completar la tetera
            { { -1.5f, 0.0f, 2.0f }, { -0.5f, 0.0f, 2.0f }, { 0.5f, 0.0f, 2.0f }, { 1.5f, 0.0f, 2.0f } },
            { { -1.5f, 0.5f, 1.5f }, { -0.5f, 0.5f, 1.5f }, { 0.5f, 0.5f, 1.5f }, { 1.5f, 0.5f, 1.5f } },
            { { -1.5f, 1.0f, 1.0f }, { -0.5f, 1.0f, 1.0f }, { 0.5f, 1.0f, 1.0f }, { 1.5f, 1.0f, 1.0f } },
            { { -1.5f, 1.5f, 0.5f }, { -0.5f, 1.5f, 0.5f }, { 0.5f, 1.5f, 0.5f }, { 1.5f, 1.5f, 0.5f } }
    };

    public static void generateTeapot(List<Vertex> vertices, List<Normal> normals, List<UV> uvs, List<Face> faces) {
        int subdivisions = 10; // Control de subdivisión

        for (float[][] patch : PATCHES) {
            generatePatch(patch, subdivisions, vertices, normals, uvs, faces);
        }
    }

    private static void generatePatch(float[][] controlPoints, int subdivisions, List<Vertex> vertices,
            List<Normal> normals, List<UV> uvs, List<Face> faces) {
        int startIndex = vertices.size() + 1;

        for (int i = 0; i < subdivisions; i++) {
            for (int j = 0; j < subdivisions; j++) {
                float u0 = (float) i / subdivisions;
                float v0 = (float) j / subdivisions;
                float u1 = (float) (i + 1) / subdivisions;
                float v1 = (float) (j + 1) / subdivisions;

                Vertex p0 = bezierSurface(controlPoints, u0, v0);
                Vertex p1 = bezierSurface(controlPoints, u1, v0);
                Vertex p2 = bezierSurface(controlPoints, u1, v1);
                Vertex p3 = bezierSurface(controlPoints, u0, v1);

                vertices.add(p0);
                vertices.add(p1);
                vertices.add(p2);
                vertices.add(p3);

                faces.add(new Face(
                        List.of(startIndex, startIndex + 1, startIndex + 2),
                        null, null));
                faces.add(new Face(
                        List.of(startIndex, startIndex + 2, startIndex + 3),
                        null, null));

                startIndex += 4;
            }
        }
    }

    private static Vertex bezierSurface(float[][] controlPoints, float u, float v) {
        float x = 0, y = 0, z = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float bernsteinU = bernstein(i, u);
                float bernsteinV = bernstein(j, v);
                x += controlPoints[i][j * 3] * bernsteinU * bernsteinV;
                y += controlPoints[i][j * 3 + 1] * bernsteinU * bernsteinV;
                z += controlPoints[i][j * 3 + 2] * bernsteinU * bernsteinV;
            }
        }
        return new Vertex(x, y, z);
    }

    private static float bernstein(int i, float t) {
        switch (i) {
            case 0:
                return (1 - t) * (1 - t) * (1 - t);
            case 1:
                return 3 * t * (1 - t) * (1 - t);
            case 2:
                return 3 * t * t * (1 - t);
            case 3:
                return t * t * t;
            default:
                throw new IllegalArgumentException("Índice inválido para Bernstein");
        }
    }
}

// Clase que genera una esfera proceduralmente
class SphereGenerator {

    public static void generateSphere(float radius, int slices, int stacks, List<Vertex> vertices, List<Normal> normals,
            List<UV> uvs, List<Face> faces) {
        for (int stack = 0; stack <= stacks; stack++) {
            float phi = (float) (Math.PI * stack / stacks);
            for (int slice = 0; slice <= slices; slice++) {
                float theta = (float) (2 * Math.PI * slice / slices);

                float x = (float) (radius * Math.sin(phi) * Math.cos(theta));
                float y = (float) (radius * Math.sin(phi) * Math.sin(theta));
                float z = (float) (radius * Math.cos(phi));

                vertices.add(new Vertex(x, y, z));
                normals.add(new Normal(x / radius, y / radius, z / radius));
                uvs.add(new UV((float) slice / slices, (float) stack / stacks));
            }
        }

        for (int stack = 0; stack < stacks; stack++) {
            for (int slice = 0; slice < slices; slice++) {
                int first = (stack * (slices + 1)) + slice;
                int second = first + slices + 1;

                faces.add(new Face(
                        List.of(first + 1, second + 1, first + 2),
                        List.of(first + 1, second + 1, first + 2),
                        List.of(first + 1, second + 1, first + 2)));

                faces.add(new Face(
                        List.of(second + 1, second + 2, first + 2),
                        List.of(second + 1, second + 2, first + 2),
                        List.of(second + 1, second + 2, first + 2)));
            }
        }
    }
}

// Exportador OBJ
public class OBJExporter {

    public static void exportToOBJ(String filePath, List<Vertex> vertices, List<Normal> normals, List<UV> uvs,
            List<Face> faces) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Escribir los vértices
            for (Vertex vertex : vertices) {
                writer.write(vertex.toString());
                writer.newLine();
            }

            // Escribir las coordenadas UV
            for (UV uv : uvs) {
                writer.write(uv.toString());
                writer.newLine();
            }

            // Escribir las normales
            for (Normal normal : normals) {
                writer.write(normal.toString());
                writer.newLine();
            }

            // Escribir las caras
            for (Face face : faces) {
                writer.write(face.toString());
                writer.newLine();
            }

            System.out.println("Archivo OBJ exportado exitosamente a " + filePath);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo OBJ: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Listas para almacenar los datos
        List<Vertex> vertices = new ArrayList<>();
        List<Normal> normals = new ArrayList<>();
        List<UV> uvs = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        // // Generar la tetera de Utah
        // UtahTeapot.generateTeapot(vertices, normals, uvs, faces);

        // Generar una esfera procedural
        SphereGenerator.generateSphere(1.0f, 20, 20, vertices, normals, uvs, faces);

        // Exportar el archivo
        exportToOBJ("test-sphere.obj", vertices, normals, uvs, faces);
    }
}
