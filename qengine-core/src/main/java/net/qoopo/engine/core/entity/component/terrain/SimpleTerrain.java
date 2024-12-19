/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.terrain;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlanarMesh;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.mesh.QUtilNormales;

/**
 * Genera un terreno a partir de un mapa de altura
 * 
 * @author alberto
 */
@Getter
@Setter
public class SimpleTerrain extends Terrain {

    protected float tileSize;

    protected int offset;
    protected QMaterialBas materialTerreno;

    public SimpleTerrain() {

    }

    public SimpleTerrain(int widthTiles, int heightTiles, float tileSize, QMaterialBas material, boolean smooth) {
        this.tileSize = tileSize;
        this.widthTiles = widthTiles;
        this.heightTiles = heightTiles;
        this.materialTerreno = material;
        this.smooth = smooth;
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

            width = widthTiles * tileSize;
            height = heightTiles * tileSize;

            inicioX = -width / 2;
            inicioZ = -height / 2;

            altura = new float[widthTiles][heightTiles];
            mesh = new PlanarMesh(width, height, widthTiles, heightTiles);
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

    @Override
    public void destruir() {
    }

}
