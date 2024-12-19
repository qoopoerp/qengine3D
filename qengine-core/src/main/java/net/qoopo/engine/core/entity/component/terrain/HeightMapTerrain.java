/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.generator.height.HeightMapGenerator;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlanarMesh;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.image.ImgReader;
import net.qoopo.engine.core.util.mesh.QUtilNormales;

/**
 * Genera un terreno a partir de un mapa de altura
 * 
 * @author alberto
 */
@Getter
@Setter
public class HeightMapTerrain extends Terrain {

    protected float tileSize;
    protected int offset;
    private HeightMapGenerator heightsGenerator;
    protected QMaterialBas materialTerreno;
    private BufferedImage imagen;
    protected float minY;
    protected float maxY;

    public HeightMapTerrain() {

    }

    public HeightMapTerrain(BufferedImage imagen, float tileSize, float minY, float maxY,
            int offset, QMaterialBas material, boolean smooth) {
        this.imagen = imagen;
        heightsGenerator = new HeightMapGenerator(imagen, minY, maxY);
        this.tileSize = tileSize;

        this.offset = offset;
        this.materialTerreno = material;
        this.smooth = smooth;
        this.minY = minY;
        this.maxY = maxY;
    }

    public HeightMapTerrain(File heigthMap, float tileSize, float minY, float maxY,
            int offset, QMaterialBas material, boolean smooth) {
        try {
            imagen = ImgReader.leerImagen(heigthMap);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        heightsGenerator = new HeightMapGenerator(heigthMap, minY, maxY);
        this.tileSize = tileSize;
        this.offset = offset;
        this.materialTerreno = material;
        this.smooth = smooth;
        this.minY = minY;
        this.maxY = maxY;
    }

    public void build() {
        // Geometria
        if (offset <= 0) {
            offset = 1;
        }

        Mesh mesh = new Mesh();
        mesh.nombre = "Terreno";

        if (materialTerreno != null) {
            this.material = materialTerreno;
        } else {
            material.setColorBase(new QColor(1, 139f / 255f, 99f / 255f, 55f / 255f));
            material.setSpecularExponent(10000);
        }

        try {

            width = (imagen.getWidth() - 1) * tileSize;
            height = (imagen.getHeight() - 1) * tileSize;

            widthTiles = (int) ((imagen.getWidth() - 1)) / offset;
            heightTiles = (int) ((imagen.getHeight() - 1)) / offset;

            inicioX = -((float) imagen.getWidth() * tileSize) / 2;
            inicioZ = -((float) imagen.getHeight() * tileSize) / 2;

            altura = new float[widthTiles][heightTiles];
            mesh = new PlanarMesh(width, height, widthTiles, heightTiles, heightsGenerator);
            //actualizo las alturas 

            QUtilNormales.calcularNormales(mesh);
            QMaterialUtil.suavizar(mesh, smooth);
            QMaterialUtil.aplicarMaterial(mesh, material);

        } catch (Exception ex) {
            logger.info("error al generar");
            ex.printStackTrace();
        }
        logger.info("fin de generacion");
        entity.addComponent(mesh);

    }

    // public void build_old() {
    // // Geometria
    // if (offset <= 0) {
    // offset = 1;
    // }

    // Mesh mesh = new Mesh();
    // mesh.nombre = "Terreno";

    // if (materialTerreno != null) {
    // this.material = materialTerreno;
    // } else {
    // material.setColorBase(new QColor(1, 139f / 255f, 99f / 255f, 55f / 255f));
    // material.setSpecularExponent(10000);
    // }

    // try {

    // width = (imagen.getWidth() - 1) * tileSize;
    // height = (imagen.getHeight() - 1) * tileSize;

    // planosAncho = (int) ((imagen.getWidth() - 1)) / offset;
    // planosLargo = (int) ((imagen.getHeight() - 1)) / offset;

    // inicioX = -((float) imagen.getWidth() * tileSize) / 2;
    // inicioZ = -((float) imagen.getHeight() * tileSize) / 2;

    // logger.info("Iniciando mapa de altura " + planosAncho + "x" + planosLargo);

    // altura = new float[planosAncho][planosLargo];

    // float vertexHeight = 0;
    // int j = 0, i = 0;

    // for (int z = 0; z < imagen.getHeight() - 1; z += offset) {
    // i = 0;
    // for (int x = 0; x < imagen.getWidth() - 1; x += offset) {
    // if (x > imagen.getWidth()) {
    // x = imagen.getWidth();
    // }

    // if (z > imagen.getHeight()) {
    // z = imagen.getHeight();
    // }

    // vertexHeight = heightsGenerator.getHeight(((float)x/imagen.getWidth()),
    // ((float)z/imagen.getHeight()));
    // try {
    // altura[i][j] = vertexHeight;
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // mesh.addVertex(inicioX + x * tileSize,
    // vertexHeight,
    // inicioZ + z * tileSize,
    // // Coordenada de textures
    // (float) x / (float) imagen.getWidth(), (float) z / (float)
    // imagen.getHeight());

    // i++;
    // }
    // j++;
    // }

    // logger.info("..coordenadas generadas=" + mesh.vertices.length);

    // for (j = 0; j < planosLargo - 1; j++) {
    // for (i = 0; i < planosAncho - 1; i++) {
    // mesh.addPoly(material,
    // j * planosAncho + i,
    // j * planosAncho + planosAncho + i,
    // j * planosAncho + i + 1);

    // mesh.addPoly(material,
    // j * planosAncho + i + 1,
    // j * planosAncho + planosAncho + i,
    // j * planosAncho + planosAncho + i + 1);
    // }
    // }
    // logger.info("..Triángulos generados =" + mesh.primitivas.length);
    // mesh = QUtilNormales.calcularNormales(mesh);
    // mesh=QMaterialUtil.suavizar(mesh, smooth);

    // } catch (Exception ex) {
    // logger.info("error al generar");
    // ex.printStackTrace();
    // }
    // logger.info("fin de generacion");
    // entity.addComponent(mesh);
    // }

    @Override
    public void destruir() {
    }

}
