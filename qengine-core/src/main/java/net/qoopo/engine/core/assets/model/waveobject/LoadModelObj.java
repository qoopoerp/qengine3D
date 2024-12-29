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
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaConvexa;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
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

    // public static List<QEntity> cargarWaveObject(InputStream stream) {
    // return cargarWaveObject(null, stream, "", 0);
    // }

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
            Mesh geometriaLeyendo = null;
            int vertexIndexOffset = 0;
            BufferedReader reader = null;
            boolean smoothMode = false;
            boolean vertexNormalSpecified = false;

            try {
                // reader = new BufferedReader(new FileReader(archivo));
                reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                List<QVector2> listaUV = new ArrayList<>();
                HashMap<String, QMaterialBas> materialMap = new HashMap<>();
                QMaterialBas defaultMaterial = new QMaterialBas("Default");
                QMaterialBas currentMaterial = null;
                while ((line = reader.readLine()) != null) {
                    

                    String[] tokens = line.split("\\s+");
                    switch (tokens[0]) {
                        case "mtllib":
                            // Lee el archivo del material
                            String materialFileName = line.substring("mtllib ".length());
                            // String materialFileName = tokens[1];
                            File materialFile = new File(directory, materialFileName);
                            try {
                                if (materialFile.exists()) {
                                    BufferedReader materialReader = new BufferedReader(new FileReader(materialFile));
                                    String materialLine = "";
                                    QMaterialBas readingMaterial = null;
                                    while ((materialLine = materialReader.readLine()) != null) {
                                        if (materialLine.startsWith("newmtl ")) {
                                            if (readingMaterial != null) {
                                                materialMap.put(readingMaterial.getNombre(), readingMaterial);
                                            }
                                            String materialName = materialLine.substring("newmtl ".length());
                                            readingMaterial = new QMaterialBas(materialName);
                                        } else if (materialLine.startsWith("Ka ")) {
                                            // String[] att = materialLine.split("\\s+");
                                            // readingMaterial.setColorAmbiente(new QColor(1, Float.parseFloat(att[1]),
                                            // Float.parseFloat(att[2]), Float.parseFloat(att[3])));
                                        } else if (materialLine.startsWith("Kd ")) {
                                            String[] att = materialLine.split("\\s+");
                                            readingMaterial.setColorBase(new QColor(1, Float.parseFloat(att[1]),
                                                    Float.parseFloat(att[2]), Float.parseFloat(att[3])));
                                        } else if (materialLine.startsWith("Ks ")) {
                                            // String[] att = materialLine.split("\\s+");
                                            // readingMaterial.setColorEspecular(new QColor(1, Float.parseFloat(att[1]),
                                            // Float.parseFloat(att[2]), Float.parseFloat(att[3])));
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
                                                    readingMaterial.setMapaColor(new QProcesadorSimple(new QTextura(
                                                            ImgReader.leerImagen(validarFile(directory, texture)))));
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
                                                    readingMaterial.setFactorNormal(Float
                                                            .parseFloat(texture.substring(0, texture.indexOf(" "))));
                                                    texture = texture.substring(texture.indexOf(" ")).trim();
                                                }
                                                texture = texture.replaceAll("\\\\", "/");
                                                try {
                                                    readingMaterial.setMapaNormal(new QProcesadorSimple(new QTextura(
                                                            ImgReader.leerImagen(validarFile(directory, texture)))));
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
                                                    readingMaterial.setMapaRugosidad(new QProcesadorSimple(new QTextura(
                                                            ImgReader.leerImagen(validarFile(directory, texture)))));
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
                                                    readingMaterial.setMapaMetalico(new QProcesadorSimple(new QTextura(
                                                            ImgReader.leerImagen(validarFile(directory, texture)))));
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
                                                    readingMaterial.setFactorNormal(Float
                                                            .parseFloat(texture.substring(0, texture.indexOf(" "))));
                                                    texture = texture.substring(texture.indexOf(" ")).trim();
                                                }
                                                texture = texture.replaceAll("\\\\", "/");
                                                // System.out.println(directory + File.separator + texture);
                                                try {
                                                    readingMaterial.setMapaNormal(new QProcesadorSimple(new QTextura(
                                                            ImgReader.leerImagen(validarFile(directory, texture)))));
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
                            break;
                        case "o":
                            if (geometriaLeyendo != null) {
                                for (QPrimitiva face : geometriaLeyendo.primitivas) {
                                    if (face.listaVertices.length >= 3) {
                                        ((Poly) face).calculaNormalYCentro();
                                        if (!vertexNormalSpecified || true) {
                                            for (int i : face.listaVertices) {
                                                face.geometria.vertices[i].normal.add(((Poly) face).getNormal());
                                            }
                                        }
                                    }
                                }
                                for (int i = 0; i < geometriaLeyendo.vertices.length; i++) {
                                    geometriaLeyendo.vertices[i].normal.normalize();
                                }
                                vertexIndexOffset += geometriaLeyendo.vertices.length;

                                Entity ent = new Entity(geometriaLeyendo.nombre);
                                ent.addComponent(geometriaLeyendo);
                                CollisionShape colision = new QColisionMallaConvexa(geometriaLeyendo);
                                ent.addComponent(colision);
                                QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.ESTATICO, 0);
                                rigido.setFormaColision(colision);
                                ent.addComponent(rigido);
                                lista.add(ent);
                                // QGestorRecursos.agregarRecurso("g" + geometriaLeyendo.nombre,
                                // geometriaLeyendo);
                                // QGestorRecursos.agregarRecurso("e" + ent.nombre, ent);
                            }
                            String name = tokens[1].trim();
                            // System.out.println(name);
                            if (name.isEmpty()) {
                                name = null;
                            }
                            geometriaLeyendo = new Mesh();
                            geometriaLeyendo.nombre = name;
                            break;
                        case "v":
                            if (geometriaLeyendo == null) {
                                geometriaLeyendo = new Mesh();
                            }
                            geometriaLeyendo.addVertex(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]),
                                    Float.parseFloat(tokens[3]));
                            break;

                        case "vt":
                            listaUV.add(new QVector2(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
                            break;
                        case "vn":
                            // String[] att = line.split("\\s+");
                            // readingObject.vertices[currentVertex].normal
                            // .set(Float.parseFloat(att[1]),
                            // Float.parseFloat(att[2]),
                            // Float.parseFloat(att[3]));
                            // vertexNormalSpecified = true;
                            break;
                        case "s":
                            // smoothMode = line.trim().equals("s 1");
                            smoothMode = tokens[1].equals("1");
                            break;
                        case "usemtl":
                            // currentMaterial = materialMap.get(line.substring("usemtl ".length()));
                            currentMaterial = materialMap.get(tokens[1]);
                            break;
                        case "f":
                            if (geometriaLeyendo == null) {
                                geometriaLeyendo = new Mesh();
                            }

                            int[] verticesCara = new int[tokens.length - 1];
                            // QPoligono.UVCoordinate[] newFaceTexture = null;
                            // lee las caras, este for toma los vertices que definen la cara
                            // System.out.println("leyendo cara ");
                            // System.out.println(" tokens=" + tokens.length);
                            // System.out.println(" " + Arrays.toString(tokens));
                            int c = 0;
                            for (String token : tokens) {
                                if (!token.equals("f")) {
                                    String[] partes = token.split("/");
                                    verticesCara[c] = Integer.parseInt(partes[0]) - 1 - vertexIndexOffset;
                                    // si tiene textures
                                    if (partes.length > 1) {
                                        if (!partes[1].isEmpty()) {
                                            geometriaLeyendo.vertices[verticesCara[c]].u = listaUV
                                                    .get(Integer.parseInt(partes[1]) - 1).x;
                                            geometriaLeyendo.vertices[verticesCara[c]].v = listaUV
                                                    .get(Integer.parseInt(partes[1]) - 1).y;
                                        }
                                        // si tiene la normal
                                        if (partes.length > 2) {

                                        }
                                    }
                                    c++;
                                }
                            }
                            Poly nuevaCara = geometriaLeyendo.addPoly(verticesCara);
                            nuevaCara.setSmooth(smoothMode);
                            if (currentMaterial != null) {
                                nuevaCara.material = currentMaterial;
                            } else {
                                nuevaCara.material = defaultMaterial;
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
            if (geometriaLeyendo != null) {
                for (QPrimitiva face : geometriaLeyendo.primitivas) {
                    if (face.listaVertices.length >= 3) {
                        ((Poly) face).calculaNormalYCentro();
                        if (!vertexNormalSpecified || true) {
                            for (int i : face.listaVertices) {
                                face.geometria.vertices[i].normal.add(((Poly) face).getNormal());
                            }
                        }
                    }
                }
                for (int i = 0; i < geometriaLeyendo.vertices.length; i++) {
                    geometriaLeyendo.vertices[i].normal.normalize();
                }

                Entity ent = new Entity(geometriaLeyendo.nombre);
                ent.addComponent(geometriaLeyendo);
                CollisionShape colision = new QColisionMallaConvexa(geometriaLeyendo);
                ent.addComponent(colision);
                QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.ESTATICO, 0);
                rigido.setFormaColision(colision);
                ent.addComponent(rigido);
                lista.add(ent);

            }
            // QGestorRecursos.agregarRecurso("g" + geometriaLeyendo.nombre,
            // geometriaLeyendo);
            // QGestorRecursos.agregarRecurso("e" + ent.nombre, ent);
        } catch (Exception e) {
            e.printStackTrace();
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

    private static File validarFile(String ruta, String textura) {
        File f = new File(textura);
        if (f.exists()) {
            return f;
        } else {
            f = new File(ruta, textura);
        }
        return f;
    }

}
