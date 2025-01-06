/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.core.asset.model.studiomax;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine3d.core.asset.model.studiomax.util.Model3DS;
import net.qoopo.engine3d.core.asset.model.studiomax.util.ModelLoader;
import net.qoopo.engine3d.core.asset.model.studiomax.util.ModelObject;

/**
 * Carga una malla simple de unarchivo de 3DStudioMax
 *
 * @author alberto
 */
public class LoadModel3DSMax implements net.qoopo.engine.core.assets.model.ModelLoader {

    public LoadModel3DSMax() {
    }

    @Override
    public Entity loadModel(InputStream stream) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadModel'");
    }

    @Override
    public Entity loadModel(File file) throws FileNotFoundException {
        try {

            List<Entity> lista = new ArrayList<>();

            Model3DS modelo = ModelLoader.load3dModel(file);
            QMaterialBas material = new QMaterialBas("default");
            // try{
            // material = new QMaterialBas(ImgReader.leerImagen(new
            // File(archivo.getParentFile(),archivo.getName() + ".png")), 64, 64);
            // material.nombre="Default";
            // }catch(Exception e)
            // {
            // }
            Mesh objetoActual = null;

            for (ModelObject modeloOb : modelo.objects) {
                objetoActual = new Mesh();
                objetoActual.nombre = modeloOb.getName();
                // vertices
                int vertices = modeloOb.vertices.length;
                for (int i = 0; i < vertices; i += 3) {
                    // objetoActual.addVertex(objeto.vertices[i], objeto.vertices[i + 1],
                    // objeto.vertices[i + 2]);
                    objetoActual.addVertex(modeloOb.vertices[i], modeloOb.vertices[i + 2],
                            modeloOb.vertices[i + 1]);
                }
                // caras
                int caras = modeloOb.polygons.length;
                for (int i = 0; i < caras; i += 3) {
                    if (modeloOb.polygons[i] < vertices && modeloOb.polygons[i + 1] < vertices
                            && modeloOb.polygons[i + 2] < vertices) {
                        // objetoActual.addPoly(material, objeto.polygons[i], objeto.polygons[i
                        // + 1], objeto.polygons[i + 2]);
                        Poly face = objetoActual.addPoly(material, new int[] { modeloOb.polygons[i],
                                modeloOb.polygons[i + 2], modeloOb.polygons[i + 1] });

                    } else {
                        System.out.println("esta llamando a un vertice que no existe");
                    }
                }
                // objeto.textureCoordinates

                // System.out.println("vertices=" + vertices);
                // System.out.println("caras=" + caras);
                // System.out.println("objeto actual ");
                // System.out.println(" vertices=" + objetoActual.vertices.length);
                // System.out.println(" caras = " + objetoActual.primitivas.length);
                objetoActual.computeNormals();

                Entity ent = new Entity(objetoActual.nombre);
                ent.addComponent(objetoActual);
                lista.add(ent);
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

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

}
