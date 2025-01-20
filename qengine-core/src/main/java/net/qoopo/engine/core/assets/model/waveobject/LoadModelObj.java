/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.assets.model.waveobject;

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
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaConvexa;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.util.image.ImgReader;

/**
 * Implementación para cargar un modelo .obj, formato desarrollado por Wavefront
 * Technologies el cual es ampliamente utilizado
 * 
 * @author alberto
 */
public class LoadModelObj implements ModelLoader {

    public LoadModelObj() {
    }

    @Override
    public Entity loadModel(File file) throws FileNotFoundException {
        return loadModel(new FileInputStream(file), file.getParent());
    }

    @Override
    public Entity loadModel(InputStream stream) {
        return loadModel(stream, "");
    }

    private void readMaterial(File materialFile, HashMap<String, Material> materialMap, String directory) {
        try {
            if (materialFile.exists()) {
                BufferedReader materialReader = new BufferedReader(new FileReader(materialFile));
                String materialLine = "";
                Material readingMaterial = null;
                while ((materialLine = materialReader.readLine()) != null) {
                    if (materialLine.startsWith("newmtl ")) {
                        if (readingMaterial != null) {
                            materialMap.put(readingMaterial.getNombre(), readingMaterial);
                        }
                        String materialName = materialLine.substring("newmtl ".length());
                        readingMaterial = new Material(materialName);
                    } else if (materialLine.startsWith("Ka ")) {
                        // String[] att = materialLine.split("\\s+");
                        // readingMaterial.setColorAmbiente(new QColor(1, Float.parseFloat(att[1]),
                        // Float.parseFloat(att[2]), Float.parseFloat(att[3])));
                    } else if (materialLine.startsWith("Kd ")) {
                        String[] att = materialLine.split("\\s+");
                        readingMaterial.setColor(new QColor(1, Float.parseFloat(att[1]),
                                Float.parseFloat(att[2]), Float.parseFloat(att[3])));
                    } else if (materialLine.startsWith("Ks ")) {
                        String[] att = materialLine.split("\\s+");
                        readingMaterial.setSpecularColour(new QColor(1, Float.parseFloat(att[1]),
                                Float.parseFloat(att[2]), Float.parseFloat(att[3])));
                    } else if (materialLine.startsWith("d ")) {
                        String[] att = materialLine.split("\\s+");
                        readingMaterial.setTransAlfa(Float.parseFloat(att[1]));
                    } else if (materialLine.startsWith("Tr ")) {
                        String[] att = materialLine.split("\\s+");
                        readingMaterial.setTransAlfa(1 - Float.parseFloat(att[1]));
                    } else if (materialLine.startsWith("Ns ")) {
                        String[] att = materialLine.split("\\s+");
                        readingMaterial.setSpecularExponent((int) Float.parseFloat(att[1]));
                    } else if (materialLine.toLowerCase().startsWith("map_kd ")) {
                        String texture = materialLine.substring("map_Kd ".length()).trim();
                        if (!texture.isEmpty()) {
                            texture = texture.replaceAll("\\\\", "/");
                            try {
                                readingMaterial.setColorMap(new Texture(
                                        ImgReader.read(validarFile(directory, texture))));
                            } catch (Exception e) {
                                System.out.println(
                                        "Error al cargar " + directory + File.separator + texture);
                                e.printStackTrace();
                            }
                        }
                    } else if (materialLine.toLowerCase().startsWith("map_d ")) {
                        String texture = materialLine.substring("map_d ".length()).trim();
                        if (!texture.isEmpty()) {
                            texture = texture.replaceAll("\\\\", "/");
                            try {
                                readingMaterial.setAlphaMap(new Texture(
                                        ImgReader.read(validarFile(directory, texture))));
                            } catch (Exception e) {
                                System.out.println(
                                        "Error al cargar " + directory + File.separator + texture);
                                e.printStackTrace();
                            }
                        }
                    } else if (materialLine.toLowerCase().startsWith("map_bump ")) {
                        String texture = materialLine.substring("map_Bump ".length()).trim();
                        if (!texture.isEmpty()) {
                            if (texture.startsWith("-bm ")) {
                                texture = texture.substring("-bm ".length()).trim();
                                // readingMaterial.setFactorNormal(Float
                                //         .parseFloat(texture.substring(0, texture.indexOf(" "))));
                                texture = texture.substring(texture.indexOf(" ")).trim();
                            }
                            texture = texture.replaceAll("\\\\", "/");
                            try {
                                readingMaterial.setNormalMap(new Texture(
                                        ImgReader.read(validarFile(directory, texture))));
                            } catch (Exception e) {
                                System.out.println(
                                        "Error al cargar " + directory + File.separator + texture);
                                e.printStackTrace();
                            }
                        }
                    } else if (materialLine.toLowerCase().startsWith("map_ns ")) {
                        String texture = materialLine.substring("map_Ns ".length()).trim();
                        if (!texture.isEmpty()) {
                            texture = texture.replaceAll("\\\\", "/");
                            try {
                                readingMaterial.setRoughnessMap(new Texture(
                                        ImgReader.read(validarFile(directory, texture))));
                            } catch (Exception e) {
                                System.out.println(
                                        "Error al cargar " + directory + File.separator + texture);
                                e.printStackTrace();
                            }
                        }
                    } else if (materialLine.toLowerCase().startsWith("refl ")) {
                        String texture = materialLine.substring("refl ".length()).trim();
                        if (!texture.isEmpty()) {
                            texture = texture.replaceAll("\\\\", "/");
                            try {
                                readingMaterial.setMetallicMap(new Texture(
                                        ImgReader.read(validarFile(directory, texture))));
                            } catch (Exception e) {
                                System.out.println(
                                        "Error al cargar " + directory + File.separator + texture);
                                e.printStackTrace();
                            }
                        }
                    } else if (materialLine.toLowerCase().startsWith("bump ")) {
                        String texture = materialLine.substring("bump ".length()).trim();
                        if (!texture.isEmpty()) {
                            if (texture.startsWith("-bm ")) {
                                texture = texture.substring("-bm ".length()).trim();
                                // readingMaterial.setFactorNormal(Float
                                //         .parseFloat(texture.substring(0, texture.indexOf(" "))));
                                texture = texture.substring(texture.indexOf(" ")).trim();
                            }
                            texture = texture.replaceAll("\\\\", "/");
                            // System.out.println(directory + File.separator + texture);
                            try {
                                readingMaterial.setNormalMap(new Texture(
                                        ImgReader.read(validarFile(directory, texture))));
                            } catch (Exception e) {
                                System.out.println(
                                        "Error al cargar " + directory + File.separator + texture);
                                e.printStackTrace();
                            }
                        }
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

    public Entity loadModel(InputStream stream, String directory) {

        List<Entity> lista = new ArrayList<>();
        try {
            Mesh readingMesh = new Mesh();
            int vertexIndexOffset = 0;
            BufferedReader reader = null;
            boolean smoothMode = false;
            boolean vertexNormalSpecified = false;

            try {
                // reader = new BufferedReader(new FileReader(archivo));
                reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                HashMap<String, Material> materialMap = new HashMap<>();
                Material defaultMaterial = new Material("Default");
                Material currentMaterial = null;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split("\\s+");
                    switch (tokens[0]) {
                        case "mtllib":
                            // Lee el archivo del material
                            String materialFileName = line.substring("mtllib ".length());
                            File materialFile = new File(directory, materialFileName);
                            readMaterial(materialFile, materialMap, directory);
                            break;
                        case "o":
                            if (readingMesh != null && readingMesh.vertexList.length > 0) {
                                for (Primitive face : readingMesh.primitiveList) {
                                    if (face.vertexIndexList.length >= 3) {
                                        ((Poly) face).computeNormalCenter();
                                        // if (!vertexNormalSpecified || true) {
                                        // for (int i : face.vertexList) {
                                        // face.mesh.vertices[i].normal.add(((Poly) face).getNormal());
                                        // }
                                        // }
                                    }
                                }
                                // for (int i = 0; i < geometriaLeyendo.vertices.length; i++) {
                                // geometriaLeyendo.vertices[i].normal.normalize();
                                // }
                                vertexIndexOffset += readingMesh.vertexList.length;

                                Entity ent = new Entity(readingMesh.name);
                                ent.addComponent(readingMesh);
                                CollisionShape colision = new QColisionMallaConvexa(readingMesh);
                                ent.addComponent(colision);
                                QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.ESTATICO, 0);
                                rigido.setFormaColision(colision);
                                ent.addComponent(rigido);
                                lista.add(ent);
                            }
                            String name = tokens[1].trim();
                            // System.out.println(name);
                            if (name.isEmpty()) {
                                name = null;
                            }
                            readingMesh = new Mesh();
                            readingMesh.name = name;
                            break;
                        case "v":
                            if (readingMesh == null) {
                                readingMesh = new Mesh();
                            }
                            readingMesh.addVertex(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3]));
                            break;

                        case "vt":
                            readingMesh.addUV(new Vector2(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                            break;
                        case "vn":
                            // String[] att = line.split("\\s+");
                            readingMesh.addNormal(new Vector3(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3])));
                            vertexNormalSpecified = true;
                            break;
                        case "s":
                            // smoothMode = line.trim().equals("s 1");
                            smoothMode = tokens[1].equals("1");
                            break;
                        case "usemtl":
                            currentMaterial = materialMap.get(tokens[1]);
                            break;
                        case "f":
                            if (readingMesh == null) {
                                readingMesh = new Mesh();
                            }
                            int[] vertexFace = new int[tokens.length - 1];
                            int[] uvFace = new int[tokens.length - 1];
                            int[] normalsFace = new int[tokens.length - 1];
                            int c = 0;
                            for (String token : tokens) {
                                if (!token.equals("f")) {
                                    String[] partes = token.split("/");
                                    vertexFace[c] = Integer.parseInt(partes[0]) - 1 - vertexIndexOffset;
                                    // si tiene textures
                                    if (partes.length > 1) {
                                        if (!partes[1].isEmpty()) {
                                            uvFace[c] = Integer.parseInt(partes[1]) - 1 - vertexIndexOffset;
                                            // geometriaLeyendo.vertices[verticesCara[c]].u = listaUV
                                            // .get(Integer.parseInt(partes[1]) - 1).x;
                                            // geometriaLeyendo.vertices[verticesCara[c]].v = listaUV
                                            // .get(Integer.parseInt(partes[1]) - 1).y;
                                        }
                                        // si tiene la normal
                                        if (partes.length > 2) {
                                            if (!partes[2].isEmpty()) {
                                                normalsFace[c] = Integer.parseInt(partes[2]) - 1 - vertexIndexOffset;
                                            }
                                        }
                                    }
                                    c++;
                                }
                            }
                            Poly newFace = readingMesh.addPoly(vertexFace, normalsFace, uvFace);
                            newFace.setSmooth(smoothMode);
                            if (currentMaterial != null) {
                                newFace.material = currentMaterial;
                            } else {
                                newFace.material = defaultMaterial;
                            }
                            break;

                        default:
                            break;
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
            if (readingMesh != null) {
                for (Primitive face : readingMesh.primitiveList) {
                    if (face.vertexIndexList.length >= 3) {
                        ((Poly) face).computeNormalCenter();
                        if (!vertexNormalSpecified || true) {
                            // for (int i : face.vertexList) {
                            // face.mesh.vertices[i].normal.add(((Poly) face).getNormal());
                            // }
                        }
                    }
                }
                // for (int i = 0; i < geometriaLeyendo.vertices.length; i++) {
                // geometriaLeyendo.vertices[i].normal.normalize();
                // }

                Entity ent = new Entity(readingMesh.name);
                ent.addComponent(readingMesh);
                CollisionShape colision = new QColisionMallaConvexa(readingMesh);
                ent.addComponent(colision);
                QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.ESTATICO, 0);
                rigido.setFormaColision(colision);
                ent.addComponent(rigido);
                lista.add(ent);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lista != null && !lista.isEmpty()) {
            if (lista.size() == 1) {
                return lista.get(0);
            } else {
                Entity entity = new Entity("loaded_" + lista.get(0).getName());
                lista.forEach(child -> entity.addChild(child));
                return entity;
            }
        }
        return null;

    }

    private static File validarFile(String ruta, String textura) {
        File f = new File(textura);
        if (f.exists()) {
            return f;
        } else {
            f = new File(ruta, textura);
            if (f.exists()) {
                return f;
            } else {
                f = new File(ruta, new File(textura).getName());
                if (f.exists())
                    return f;

            }
        }
        return null;
    }

}
