/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.generator;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.modifier.generate.RevolutionModifier;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.mesh.NormalUtil;

/**
 * Permite generar automaticamente gemotrias con fórmulas
 *
 * @author aigarcia
 */
public class MeshGenerator {

    public static Mesh generarSombrero(float alto, float radio, int secciones) {
        Mesh objeto = new Mesh();
        Material material = new Material("Cilindro");
        // primer paso generar vertices
        objeto.addVertex(0, alto / 2, 0);
        objeto.addVertex(radio, -alto / 4, 0);
        objeto.addVertex(radio * 2, -alto / 2, 0);
        objeto.addVertex(0, -alto / 2, 0);
        new RevolutionModifier(secciones).apply(objeto);
        objeto = NormalUtil.computeNormals(objeto);
        objeto = MaterialUtil.applyMaterial(objeto, material);
        return objeto;
    }

    public static Mesh generarDisco(float radio, int secciones) {
        Mesh objeto = new Mesh();
        Material material = new Material("Cilindro");
        // primer paso generar vertices
        objeto.addVertex(radio / 2, 0, 0);
        new RevolutionModifier(secciones).apply(objeto);
        objeto = NormalUtil.computeNormals(objeto);
        objeto = MaterialUtil.applyMaterial(objeto, material);
        return objeto;
    }

    public static Mesh generarMediaEsfera(float radio) {
        int secciones = 36;
        return generarMediaEsfera(radio, secciones);
    }

    public static Mesh generarMediaEsfera(float radio, int secciones) {
        Mesh objeto = new Mesh();
        Material material = new Material("Esfera");
        Vertex inicial = objeto.addVertex(0, radio, 0); // primer vertice
        QVector3 vector = QVector3.of(inicial.location.x, inicial.location.y, inicial.location.z);
        float angulo = 360 / secciones;
        // generamos los vertices el contorno para luego generar el objeto por medio de
        // revolucion
        for (int i = 1; i <= secciones / 4; i++) {// medio circulo
            vector = vector.rotateZ((float) Math.toRadians(angulo));
            objeto.addVertex(vector.x, vector.y, vector.z);
        }

        new RevolutionModifier(secciones).apply(objeto);
        objeto = NormalUtil.computeNormals(objeto);
        // el objeto es suavizado
        for (Primitive face : objeto.primitiveList) {
            ((Poly) face).setSmooth(true);
        }
        objeto = MaterialUtil.applyMaterial(objeto, material);
        return objeto;
    }

}
