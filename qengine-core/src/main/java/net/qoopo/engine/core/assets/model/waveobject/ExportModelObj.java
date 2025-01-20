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
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;
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
            exportEntity(output, Vector3.zero, entity, writer, 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void exportEntity(OutputStream output, Vector3 parentLocation, Entity entity, BufferedWriter writer,
            int vertextCount, int uvCount, int normalCount) {
        try {
            int vertextCountLocal = 0;
            int normalCountLocal = 0;
            int uvCountLocal = 0;
            writer.write("o " + entity.getName());
            writer.newLine();
            for (EntityComponent component : entity.getComponents()) {
                // Exporta las mallas
                if (component instanceof Mesh) {
                    Mesh mesh = (Mesh) component;
                    // writer.write("o " + mesh.nombre);
                    // writer.newLine();
                    // escribe los vértices
                    for (Vertex vertex : mesh.vertexList) {
                        writer.write(String
                                .format("v %.6f %.6f %.6f",
                                        vertex.location.x + parentLocation.x
                                                + entity.getTransform().getLocation().x,
                                        vertex.location.y + parentLocation.y
                                                + entity.getTransform().getLocation().y,
                                        vertex.location.z + parentLocation.z
                                                + entity.getTransform().getLocation().z)
                                .replace(",", "."));
                        writer.newLine();
                    }

                    // escribe las coordenadas de texturas
                    for (Vector2 uv : mesh.uvList) {
                        writer.write(String.format("vt %.6f %.6f", uv.x, uv.y).replace(",", "."));
                        writer.newLine();
                    }

                    // escribe las normales
                    for (Vector3 normal : mesh.normalList) {
                        // vertex.normal.normalize();
                        writer.write(String.format("vn %.4f %.4f %.4f", normal.x, normal.y, normal.z)
                                .replace(",", "."));
                        writer.newLine();
                    }

                    // escribe las caras
                    // usemtl Default_OBJ.001
                    // s 1
                    // f 1/1/1 2/2/2 3/3/3
                    String currentMaterialName = "";
                    Boolean currentSmooth = null;
                    for (Primitive poly : mesh.primitiveList) {
                        if (poly.material != null && !poly.material.getNombre().equals(currentMaterialName)) {
                            currentMaterialName = poly.material.getNombre();
                            writer.write("usemtl " + currentMaterialName);
                            writer.newLine();
                        }

                        if (poly instanceof Poly) {
                            Poly polygon = (Poly) poly;
                            if (currentSmooth == null || currentSmooth != polygon.isSmooth()) {
                                currentSmooth = polygon.isSmooth();
                                writer.write("s " + (currentSmooth ? "on" : "off"));
                                writer.newLine();
                            }
                        }

                        writer.write("f");
                        int curNormalIndex = 0;
                        int curUVIndex = 0;
                        int curIndex = 0;
                        for (int index : poly.vertexIndexList) {

                            curNormalIndex = poly.normalIndexList[curIndex];
                            curUVIndex = poly.uvIndexList[curIndex];

                            // indice de vertice/indice de texura /indice de normal
                            writer.write(String.format(" %d/%d/%d",
                                    (index + vertextCount + vertextCountLocal + 1),
                                    (curUVIndex + vertextCount + uvCountLocal + 1),
                                    (curNormalIndex + vertextCount + normalCountLocal + 1)));
                            curIndex++;
                        }
                        writer.newLine();
                    }
                    vertextCountLocal += mesh.vertexList.length;
                    uvCountLocal += mesh.uvList.length;
                    normalCountLocal += mesh.normalList.length;
                }
            }

            // ahora exporta los hijos como mallas independientes

            for (Entity child : entity.getChilds()) {
                exportEntity(output,
                        entity.getTransform().getLocation(),
                        child,
                        writer,
                        vertextCount + vertextCountLocal,
                        uvCount + uvCountLocal,
                        normalCount + normalCountLocal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportMaterial(OutputStream output, Entity entity) {
        materialExported = false;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
        try {

            ArrayList<Material> materiales = new ArrayList<>();
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

            for (Material material : materiales) {
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
                writer.write("Kd " + String.format("%.3f %.3f %.3f", material.getColor().r,
                        material.getColor().g, material.getColor().b).replace(",", "."));
                writer.newLine();
                // writer.write("Ks " + String.format("%.3f %.3f %.3f", QColor.WHITE.r,
                // QColor.WHITE.g, QColor.WHITE.b));
                writer.write("Kd " + String.format("%.3f %.3f %.3f", material.getSpecularColour().r,
                        material.getSpecularColour().g,
                        material.getSpecularColour().b).replace(",", "."));
                writer.newLine();
                writer.write("Ns " + String.format("%d", material.getSpecularExponent()).replace(",", "."));
                writer.newLine();
                writer.write("d " + String.format("%.6f", material.getTransAlfa()).replace(",", "."));
                writer.newLine();
                // mapas
                if (material.getColorMap() != null) {
                    ImageIO.write(material.getColorMap().getImagen(), "png",
                            new File(parentFolder, material.getNombre() + "_kd.png"));
                    writer.write("map_kd " + material.getNombre() + "_kd.png");
                    writer.newLine();
                }
                if (material.getNormalMap() != null) {
                    ImageIO.write(material.getNormalMap().getImagen(), "png",
                            new File(parentFolder, material.getNombre() + "_normal.png"));
                    writer.write("map_Bump " + material.getNombre() + "_normal.png");
                    writer.newLine();
                }
                if (material.getRoughnessMap() != null) {
                    ImageIO.write(material.getRoughnessMap().getImagen(), "png",
                            new File(parentFolder, material.getNombre() + "_ns.png"));
                    writer.write("map_Ns " + material.getNombre() + "_ns.png");
                    writer.newLine();
                }
                if (material.getMetallicMap() != null) {
                    ImageIO.write(material.getMetallicMap().getImagen(), "png",
                            new File(parentFolder, material.getNombre() + "_refl.png"));
                    writer.write("refl " + material.getNombre() + "_refl.png");
                    writer.newLine();
                }
                if (material.getAlphaMap() != null) {
                    ImageIO.write(material.getMetallicMap().getImagen(), "png",
                            new File(parentFolder, material.getNombre() + "_d.png"));
                    writer.write("map_d " + material.getNombre() + "_d.png");
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

                e.printStackTrace();
            }
        }
    }

    private void getMaterials(Entity entity, ArrayList<Material> materialList) {
        for (EntityComponent component : entity.getComponents()) {
            // Exporta las mallas
            if (component instanceof Mesh) {
                Mesh mesh = (Mesh) component;
                for (Primitive poly : mesh.primitiveList) {
                    if (poly.material instanceof Material) {
                        Material material = (Material) poly.material;
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
