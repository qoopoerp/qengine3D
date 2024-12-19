/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.texture.util;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPoligono;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.mesh.primitive.QShape;
import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;

/**
 *
 * @author alberto
 */
public class QMaterialUtil {

    public static Mesh suavizar(Mesh geometria, boolean suave) {
        for (QPrimitiva face : geometria.primitivas) {
            if (face instanceof QPoligono) {
                ((QPoligono) face).setSmooth(suave);
            }
        }
        return geometria;
    }

    /**
     * Aplica un material a todo el objeto
     *
     * @param objeto
     * @param material
     * @return
     */
    public static Mesh aplicarMaterial(Mesh objeto, AbstractMaterial material) {
        for (QPrimitiva primitiva : objeto.primitivas) {
            primitiva.material = material;
        }
        if (objeto instanceof QShape) {
            ((QShape) objeto).setMaterial(material);
        }
        return objeto;
    }

    public static Mesh aplicarColor(Mesh objeto, float alpha, QColor colorDifuso, QColor colorEspecular,
            float factorEmisionLuz, int specularExponent) {
        QMaterialBas material = null;
        try {
            material = new QMaterialBas();
            material.setTransAlfa(alpha);
            material.setColorBase(colorDifuso);
            // material.setColorEspecular(colorEspecular);
            material.setFactorEmision(factorEmisionLuz);
            material.setSpecularExponent(specularExponent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aplicarMaterial(objeto, material);
    }

    public static Mesh aplicarColor(Mesh objeto, float alpha, float r, float g, float b, float rS, float gS, float bS,
            float factorEmisionLuz, int specularExponent) {
        QMaterialBas material = null;
        try {
            material = new QMaterialBas();
            material.setTransAlfa(alpha);
            material.setColorBase(new QColor(1, r, g, b));
            // material.setColorEspecular(new QColor(1, rS, gS, bS));
            material.setFactorEmision(factorEmisionLuz);
            material.setSpecularExponent(specularExponent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aplicarMaterial(objeto, material);
    }

    public static Mesh aplicarColor(Mesh objeto, float alpha, float r, float g, float b, float rS, float gS, float bS,
            int specularExponent) {
        QMaterialBas material = null;
        try {
            material = new QMaterialBas();
            material.setTransAlfa(alpha);
            material.setTransparencia(alpha < 1.0f);
            material.setColorBase(new QColor(1, r, g, b));
            // material.setColorEspecular(new QColor(1, rS, gS, bS));
            material.setSpecularExponent(specularExponent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aplicarMaterial(objeto, material);
    }

}
