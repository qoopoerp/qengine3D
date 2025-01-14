package net.qoopo.engine.core.material.util;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.procesador.NormalMapDirextTexture;

public class PbrUtil {
    public static Material loadPbrMaterial(File folder) {
        return loadPbrMaterial(folder, false);
    }

    public static Material loadPbrMaterial(File folder, int muestrasUV) {
        return loadPbrMaterial(folder, false, muestrasUV);
    }

    public static Material loadPbrMaterial(File folder, boolean invertNormal) {
        return loadPbrMaterial(folder, invertNormal, 1);
    }

    public static Material loadPbrMaterial(File folder, boolean invertNormal, int muestrasUV) {
        Texture albedo = AssetManager.get().loadTexture("difusa", new File(folder, "albedo.png"));
        Texture normal = AssetManager.get().loadTexture("normal", new File(folder, "normal.png"));
        Texture rugoso = AssetManager.get().loadTexture("rugoso", new File(folder, "roughness.png"));
        Texture metalico = AssetManager.get().loadTexture("metalico", new File(folder, "metallic.png"));
        Texture ao = AssetManager.get().loadTexture("ao", new File(folder, "ao.png"));

        Material material = new Material();

        material.setColor(QColor.WHITE);
        material.setColorMap(albedo);
        if (invertNormal)
            material.setNormalMap(new NormalMapDirextTexture(normal));
        else
            material.setNormalMap(normal);
            
        material.setRoughnessMap(rugoso);
        material.setMetallicMap(metalico);
        material.setAoMap(ao);

        if (material.getColorMap() != null) {
            material.getColorMap().setMuestrasU(muestrasUV);
            material.getColorMap().setMuestrasV(muestrasUV);
        }
        if (material.getNormalMap() != null) {
            material.getNormalMap().setMuestrasU(muestrasUV);
            material.getNormalMap().setMuestrasV(muestrasUV);
        }
        if (material.getRoughnessMap() != null) {
            material.getRoughnessMap().setMuestrasU(muestrasUV);
            material.getRoughnessMap().setMuestrasV(muestrasUV);
        }
        if (material.getMetallicMap() != null) {
            material.getMetallicMap().setMuestrasU(muestrasUV);
            material.getMetallicMap().setMuestrasV(muestrasUV);
        }
        if (material.getAoMap() != null) {
            material.getAoMap().setMuestrasU(muestrasUV);
            material.getAoMap().setMuestrasV(muestrasUV);
        }
        return material;

    }
}
