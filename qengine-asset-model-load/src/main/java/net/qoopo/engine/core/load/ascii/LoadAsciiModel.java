/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.load.ascii;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.material.basico.QMaterialBas;

/**
 *
 * @author alberto
 */
public class LoadAsciiModel implements ModelLoader {

    public LoadAsciiModel() {
    }

    @Override
    public Entity loadModel(InputStream stream) {
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

                QMaterialBas defaultMaterial = new QMaterialBas("Default");
                QMaterialBas currentMaterial = null;

                int cargando = 0;
                boolean vertices = false;
                boolean caras = false;

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
                    } else if (line.contains("Material") || line.contains("Smoothing")) {

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
                                        Float.parseFloat(att[2]), Float.parseFloat(att[3]), Float.parseFloat(att[4]));
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
                            Poly face = readingObject.addPoly(vertices_cara);
                            face.material = defaultMaterial;
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

            for (QPrimitiva face : readingObject.primitivas) {
                if (face.listaVertices.length >= 3) {

                    ((Poly) face).calculaNormalYCentro();
                    if (!vertexNormalSpecified || true) {
                        for (int i : face.listaVertices) {
                            face.geometria.vertices[i].normal.add(((Poly) face).getNormal());
                        }
                    }
                }
            }
            for (int i = 0; i < readingObject.vertices.length; i++) {
                readingObject.vertices[i].normal.normalize();
            }

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

}
