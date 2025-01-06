/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.load.ascii;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;

/**
 *
 * @author alberto
 */
public class LoadAsciiModel implements ModelLoader {

    public LoadAsciiModel() {
    }

    @Override
    public Entity loadModel(File file) throws FileNotFoundException {
        return loadModel(new FileInputStream(file), file.getParent());
    }

    @Override
    public Entity loadModel(InputStream stream) {
        return loadModel(stream, "");
    }

    public Entity loadModel(InputStream stream, String directory) {
        List<Entity> lista = new ArrayList<>();

        try {
            Mesh readingObject = null;
            int vertexIndexOffset = 0;
            BufferedReader reader = null;
            boolean smoothMode = false;
            boolean vertexNormalSpecified = false;

            try {
                reader = new BufferedReader(new InputStreamReader(stream));

                String line;
                // ArrayList<QPoligono.UVCoordinate> uvList = new ArrayList<>();

                HashMap<String, QMaterialBas> materialMap = new HashMap<>();

                QMaterialBas defaultMaterial = new QMaterialBas("Default");
                QMaterialBas currentMaterial = null;

                int cargando = 0;
                boolean vertices = false;
                boolean caras = false;

                readMaterial(new File(directory, "material.mat"), materialMap, directory);

                while ((line = reader.readLine()) != null) {

                    if (!line.contains(" ") || line.contains("Vertices") || line.contains("Faces")) {
                        // si no contiene espacios es el numero que identifica los vertices o caras
                        cargando++;
                        // si es vertices
                        if (cargando == 1) {
                            vertices = true;
                            caras = false;
                        } else {
                            // si es caras
                            vertices = false;
                            caras = true;
                        }
                    } else if (line.contains("MaterialID")) {
                        currentMaterial = materialMap.get(line.split(" ")[1]);
                    } else if (line.contains("Smoothing")) {
                        smoothMode = Boolean.valueOf(line.split(" ")[1]);
                    } else {
                        // si es un vertice o una cara
                        if (vertices) {
                            if (readingObject == null) {
                                readingObject = new Mesh();
                            }
                            String[] att = line.split(" ");
                            if (att.length == 3) {
                                readingObject.addVertex(Float.parseFloat(att[0]), Float.parseFloat(att[1]),
                                        Float.parseFloat(att[2]));
                            } else if (att.length == 5) {
                                readingObject.addVertex(Float.parseFloat(att[0]), Float.parseFloat(att[1]),
                                        Float.parseFloat(att[2]));
                                readingObject.addUV(Float.parseFloat(att[3]), Float.parseFloat(att[4]));
                            }

                        } else if (caras) {
                            if (readingObject == null) {
                                readingObject = new Mesh();
                            }
                            String[] attr = line.split(" ");
                            int[] vertices_cara = new int[attr.length];
                            for (int i = 0; i < attr.length; i++) {
                                vertices_cara[i] = Integer.valueOf(attr[i]);
                            }
                            Poly newFace = readingObject.addPoly(vertices_cara);
                            newFace.setSmooth(smoothMode);
                            if (currentMaterial != null) {
                                newFace.material = currentMaterial;
                            } else {
                                newFace.material = defaultMaterial;
                            }
                        }
                    }
                }
                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                    }
                }
            }

            for (Primitive face : readingObject.primitiveList) {
                if (face.vertexIndexList.length >= 3) {
                    ((Poly) face).computeNormalCenter();
                    // if (!vertexNormalSpecified || true) {
                    // for (int i : face.vertexList) {
                    // face.mesh.vertices[i].normal.add(((Poly) face).getNormal());
                    // }
                    // }
                }
            }
            // for (int i = 0; i < readingObject.vertices.length; i++) {
            // readingObject.vertices[i].normal.normalize();
            // }

            // lista.add(readingObject);

            Entity ent = new Entity(readingObject.nombre);
            ent.addComponent(readingObject);
            lista.add(ent);
            // refreshObjectList();
            // System.out.println(readingObject.nombre);
        } catch (Exception e) {

        }

        if (lista != null && !lista.isEmpty()) {
            if (lista.size() == 1) {
                return lista.get(0);
            } else {
                Entity entity = new Entity("output");
                lista.forEach(child -> entity.addChild(child));
                return entity;
            }
        }
        return null;
    }

    private void readMaterial(File materialFile, HashMap<String, QMaterialBas> materialMap, String directory) {
        try {
            if (materialFile.exists()) {
                BufferedReader materialReader = new BufferedReader(new FileReader(materialFile));
                String materialLine = "";
                QMaterialBas readingMaterial = null;

                int countMaterial = 0;
                while ((materialLine = materialReader.readLine()) != null) {
                    if (materialLine.startsWith("ambient ")) {

                        String materialName = String.valueOf(countMaterial);
                        readingMaterial = new QMaterialBas(materialName);
                        materialMap.put(readingMaterial.getNombre(), readingMaterial);
                        countMaterial++;

                        // String[] att = materialLine.split("\\s+");
                        // readingMaterial.setColorAmbiente(new QColor(1, Float.parseFloat(att[1]),
                        // Float.parseFloat(att[2]), Float.parseFloat(att[3])));
                    } else if (materialLine.startsWith("diffuse ")) {
                        String[] att = materialLine.split("\\s+");
                        readingMaterial.setColorBase(new QColor(1, Float.parseFloat(att[1]),
                                Float.parseFloat(att[2]), Float.parseFloat(att[3])));
                    } else if (materialLine.startsWith("specular ")) {
                        // String[] att = materialLine.split("\\s+");
                        // readingMaterial.setColorEspecular(new QColor(1, Float.parseFloat(att[1]),
                        // Float.parseFloat(att[2]), Float.parseFloat(att[3])));
                    } else if (materialLine.startsWith("shininess ")) {
                        String[] att = materialLine.split("\\s+");
                        readingMaterial.setSpecularExponent((int) Float.parseFloat(att[1]));
                    }
                }
                if (readingMaterial != null) {
                    materialMap.put(readingMaterial.getNombre(), readingMaterial);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
