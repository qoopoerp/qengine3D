package net.qoopo.engine.core.assets.model.waveobject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.qoopo.engine.core.assets.model.ModelExporter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.util.QGlobal;

public class ExportModelObj implements ModelExporter {

    private String fileName = null;
    private File parentFolder = null;
    private boolean materialExported = false;

    public void exportModel(File file, Entity entity) {
        try {
            this.fileName = file.getName();
            this.parentFolder = file.getParentFile();
            file.createNewFile();

            exportMaterial(new FileOutputStream(new File(parentFolder, fileName.replace(".obj", ".mtl"))), entity);
            exportModel(new FileOutputStream(file), entity);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void exportModel(OutputStream output, Entity entity) {

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
        try {
            /**
             * 
             * # Blender v2.90.0 OBJ File: ''
             * # www.blender.org
             * # mtllib teapot.mtl
             */
            writer.write("# QEngine " + QGlobal.version + " OBJ File");
            writer.newLine();
            if (fileName != null && materialExported) {
                writer.write("mtlib " + fileName.replace(".obj", ".mtl"));
                writer.newLine();
            }

            exportEntity(output, QVector3.zero, entity, writer, 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void exportEntity(OutputStream output, QVector3 parentLocation, Entity entity, BufferedWriter writer,
            int vertextCount) {
        try {
            int vertextCountLocal = 0;
            writer.write("o " + entity.getName());
            writer.newLine();
            for (EntityComponent component : entity.getComponents()) {
                // Exporta las mallas
                if (component instanceof Mesh) {
                    Mesh mesh = (Mesh) component;
                    // writer.write("o " + mesh.nombre);
                    // writer.newLine();
                    // escribe los vértices
                    for (Vertex vertex : mesh.vertices) {
                        writer.write(String
                                .format("v %.6f %.6f %.6f",
                                        vertex.location.x + parentLocation.x
                                                + entity.getTransformacion().getTraslacion().x,
                                        vertex.location.y + parentLocation.y
                                                + entity.getTransformacion().getTraslacion().y,
                                        vertex.location.z + parentLocation.z
                                                + entity.getTransformacion().getTraslacion().z)
                                .replace(",", "."));
                        writer.newLine();
                    }

                    // escribe las coordenadas de texturas
                    for (Vertex vertex : mesh.vertices) {
                        writer.write(String.format("vt %.6f %.6f", vertex.u, vertex.v).replace(",", "."));
                        writer.newLine();
                    }

                    // escribe las normales
                    for (Vertex vertex : mesh.vertices) {
                        // vertex.normal.normalize();
                        writer.write(
                                String.format("vn %.4f %.4f %.4f", vertex.normal.x, vertex.normal.y,
                                        vertex.normal.z)
                                        .replace(",", "."));
                        writer.newLine();
                    }

                    // escribe las caras
                    // usemtl Default_OBJ.001
                    // s 1
                    // f 1/1/1 2/2/2 3/3/3
                    String currentMaterialName = "";
                    Boolean currentSmooth = null;
                    for (QPrimitiva poly : mesh.primitivas) {

                        if (poly.material != null && !poly.material.getNombre().equals(currentMaterialName)) {
                            currentMaterialName = poly.material.getNombre();
                            writer.write("usemtl " + currentMaterialName);
                            writer.newLine();
                        }

                        if (poly instanceof Poly) {
                            Poly polygon = (Poly) poly;
                            if (currentSmooth == null || currentSmooth != polygon.isSmooth()) {
                                currentSmooth = polygon.isSmooth();
                                writer.write("s " + currentSmooth);
                                writer.newLine();
                            }
                        }

                        writer.write("f");
                        for (int index : poly.listaVertices) {
                            // indice de vertice/indice de texura /indice de normal
                            writer.write(String.format(" %d/%d/%d", (index + vertextCount + vertextCountLocal + 1),
                                    (index + vertextCount + vertextCountLocal + 1),
                                    (index + vertextCount + vertextCountLocal + 1)));
                        }
                        writer.newLine();
                    }
                    vertextCountLocal += mesh.vertices.length;
                }
            }

            // ahora exporta los hijos como mallas independientes

            for (Entity child : entity.getChilds()) {
                exportEntity(output, entity.getTransformacion().getTraslacion(), child, writer,
                        vertextCount + vertextCountLocal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportMaterial(OutputStream output, Entity entity) {
        materialExported = false;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
        try {

            ArrayList<QMaterialBas> materiales = new ArrayList<>();
            getMaterials(entity, materiales);
            // extraemos los materiales

            /**
             * 
             * # Blender v2.90.0 OBJ File: ''
             * # www.blender.org
             * # mtllib teapot.mtl
             */
            writer.write("# QEngine " + QGlobal.version + " MTL File");
            writer.newLine();

            for (QMaterialBas material : materiales) {
                /**
                 * newmtl Material
                 * Ns 323.999994
                 * Ka 1.000000 1.000000 1.000000
                 * Kd 0.800000 0.800000 0.800000
                 * Ks 0.500000 0.500000 0.500000
                 * Ke 0.000000 0.000000 0.000000
                 * Ni 1.450000
                 * d 1.000000
                 * illum 2
                 */
                writer.write("newmtl " + material.getNombre());
                writer.newLine();
                writer.write("Kd " + String.format("%.3f %.3f %.3f", material.getColorBase().r,
                        material.getColorBase().g, material.getColorBase().b));
                writer.newLine();
                writer.write("Ks " + String.format("%.3f %.3f %.3f", QColor.WHITE.r, QColor.WHITE.g, QColor.WHITE.b));
                writer.newLine();
                writer.write("Ns " + String.format("%d", material.getSpecularExponent()));
                writer.newLine();
                writer.write("d " + String.format("%.6f", material.getTransAlfa()));
                writer.newLine();
                // mapas
                if (material.getMapaColor() != null) {
                    ImageIO.write(material.getMapaColor().getTexture(), "jpg",
                            new File(parentFolder, material.getNombre() + "_kd.jpg"));
                    writer.write("map_kd " + material.getNombre() + "_kd.jpg");
                    writer.newLine();
                }
                if (material.getMapaNormal() != null) {
                    ImageIO.write(material.getMapaNormal().getTexture(), "jpg",
                            new File(parentFolder, material.getNombre() + "_normal.jpg"));
                    writer.write("map_Bump " + material.getNombre() + "_normal.jpg");
                    writer.newLine();
                }
                if (material.getMapaRugosidad() != null) {
                    ImageIO.write(material.getMapaRugosidad().getTexture(), "jpg",
                            new File(parentFolder, material.getNombre() + "_ns.jpg"));
                    writer.write("map_Ns " + material.getNombre() + "_ns.jpg");
                    writer.newLine();
                }
                if (material.getMapaMetalico() != null) {
                    ImageIO.write(material.getMapaMetalico().getTexture(), "jpg",
                            new File(parentFolder, material.getNombre() + "_refl.jpg"));
                    writer.write("refl " + material.getNombre() + "_refl.jpg");
                    writer.newLine();
                }
                if (material.getMapaTransparencia() != null) {
                    ImageIO.write(material.getMapaMetalico().getTexture(), "jpg",
                            new File(parentFolder, material.getNombre() + "_d.jpg"));
                    writer.write("map_d " + material.getNombre() + "_d.jpg");
                    writer.newLine();
                }
            }
            materialExported = true;
        } catch (Exception e) {
            e.printStackTrace();
            materialExported = false;
        } finally {
            try {
                writer.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void getMaterials(Entity entity, ArrayList<QMaterialBas> materialList) {
        for (EntityComponent component : entity.getComponents()) {
            // Exporta las mallas
            if (component instanceof Mesh) {
                Mesh mesh = (Mesh) component;
                for (QPrimitiva poly : mesh.primitivas) {
                    if (poly.material instanceof QMaterialBas) {
                        QMaterialBas material = (QMaterialBas) poly.material;
                        if (!materialList.contains(material))
                            materialList.add(material);
                    }
                }
            }
        }

        for (Entity child : entity.getChilds()) {
            getMaterials(child, materialList);
        }
    }

}