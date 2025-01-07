/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.texture.util;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.entity.component.mesh.primitive.Shape;
import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.math.QColor;

/**
 *
 * @author alberto
 */
public class MaterialUtil {

    public static Mesh smooth(Mesh geometria, boolean suave) {
        for (Primitive face : geometria.primitiveList) {
            if (face instanceof Poly) {
                ((Poly) face).setSmooth(suave);
            }
        }
        return geometria;
    }

    /**
     * Aplica un material a todo el objeto
     *
     * @param mesh
     * @param material
     * @return
     */
    public static Mesh applyMaterial(Mesh mesh, AbstractMaterial material) {
        for (Primitive primitiva : mesh.primitiveList) {
            primitiva.material = material;
        }
        if (mesh instanceof Shape) {
            ((Shape) mesh).setMaterial(material);
        }
        return mesh;
    }

    public static Mesh applyColor(Mesh objeto, float alpha, QColor colorDifuso, QColor colorEspecular,
            float factorEmisionLuz, int specularExponent) {
        Material material = null;
        try {
            material = new Material();
            material.setTransAlfa(alpha);
            material.setColor(colorDifuso);
            material.setColorEspecular(colorEspecular);
            material.setFactorEmision(factorEmisionLuz);
            material.setSpecularExponent(specularExponent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return applyMaterial(objeto, material);
    }

    public static Mesh applyColor(Mesh objeto, float alpha, float r, float g, float b, float rS, float gS, float bS,
            float factorEmisionLuz, int specularExponent) {
        Material material = null;
        try {
            material = new Material();
            material.setTransAlfa(alpha);
            material.setColor(new QColor(1, r, g, b));
            material.setColorEspecular(new QColor(1, rS, gS, bS));
            material.setFactorEmision(factorEmisionLuz);
            material.setSpecularExponent(specularExponent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return applyMaterial(objeto, material);
    }

    public static Mesh applyColor(Mesh objeto, float alpha, float r, float g, float b, float rS, float gS, float bS,
            int specularExponent) {
        Material material = null;
        try {
            material = new Material();
            material.setTransAlfa(alpha);
            material.setTransparencia(alpha < 1.0f);
            material.setColor(new QColor(1, r, g, b));
            material.setColorEspecular(new QColor(1, rS, gS, bS));
            material.setSpecularExponent(specularExponent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return applyMaterial(objeto, material);
    }

}
