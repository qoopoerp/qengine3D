/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.generator;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.mesh.NormalUtil;

/**
 * Permite generar automaticamente gemotrias con fórmulas
 *
 * @author aigarcia
 */
public class MeshGenerator {

    /**
     * Genera objetos de revolución El objeto pasado como parametro solo
     * contiene vértices que corresponden a media silueta del objeto
     *
     * @param objeto Objeto con los vertices
     * @param lados
     * @return
     */
    public static Mesh generateRevolutionMesh(Mesh objeto, int lados) {
        return generateRevolutionMesh(objeto, lados, false, false, false, false);
    }

    /**
     * Genera objetos de revolución El objeto pasado como parametro solo
     * contiene vértices que corresponden a media silueta del objeto
     *
     * @param objeto         Objeto con los vertices
     * @param lados
     * @param cerrarFigura
     * @param tipotoro
     * @param suavizar       marca las caras como suaves
     * @param suavizarTopes, marca como suave las caras superiores e inferiores,
     *                       se puede usar para los cilindros y conos
     * @return
     */
    public static Mesh generateRevolutionMesh(Mesh objeto, int lados, boolean cerrarFigura, boolean tipotoro,
            boolean suavizar, boolean suavizarTopes) {
        float angulo = (float) Math.toRadians((float) 360 / lados);
        int puntos_iniciales = objeto.vertices.length;

        // generamos los siguientes puntos, comienza en 1 porque ya existe un lado (con
        // el que se manda a crear objetos de revolucion)
        for (int i = 1; i < lados; i++) {
            // recorremos los puntos iniciales solamente
            for (int j = 0; j < puntos_iniciales; j++) {
                QVector3 tmp = QVector3.of(objeto.vertices[j].location.x, objeto.vertices[j].location.y,
                        objeto.vertices[j].location.z);
                // se rota en el ejey de las Y cada punto
                tmp.rotateY(angulo * i);// se rota solo en Eje de Y
                objeto.addVertex(tmp.x, tmp.y, tmp.z, (float) 1.0f / lados * i, objeto.vertices[j].v);// agrega el
                                                                                                           // vertice
                                                                                                           // rotado
            }
        }
        // ahora unimos las caras con los nuevos vertices

        // se recorre por cada lado
        int poligonos_x_lado = puntos_iniciales - 1;
        // los vertices
        int v1 = 0, v2 = 0, v3 = 0, v4 = 0;
        int t = 0;
        int p1 = 0, p2 = 0;// puntos iniciales de cada lado para cerrar la figura
        for (int lado = 0; lado < lados; lado++) {
            // los poligonos por lado
            for (int j = 0; j < poligonos_x_lado; j++) {
                v1 = lado * puntos_iniciales + j;
                // if (!tipotoro) {
                // if (j == 0) {
                // v1 = 0;
                // }
                // }
                v2 = v1 + 1;
                if (lado < lados - 1) {
                    t = v1 + puntos_iniciales;
                } else// si es el ultimo lado
                {
                    t = j;// aca une con los puntos originales para cerrar la figura
                }
                v3 = t + 1;
                v4 = t;

                // si es el primero plano o el ultimo (de cada lado)
                if (!tipotoro) {
                    if (j == 0 /* || j == poligonos_x_lado - 1 */) {
                        try {
                            // agrega solo un triangulo
                            // objeto.addPoly(v1, v2, v3);
                            objeto.addPoly(0, v2, v3).setSmooth(suavizarTopes);
                        } catch (Exception ex) {
                            Logger.getLogger(MeshGenerator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (j == poligonos_x_lado - 1) {
                        try {
                            // objeto.addPoly(v3, v4, v1);
                            objeto.addPoly(j + 1, v4, v1).setSmooth(suavizarTopes);
                        } catch (Exception ex) {
                            Logger.getLogger(MeshGenerator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            // agrega 2 triangulos
                            objeto.addPoly(v1, v2, v3).setSmooth(suavizar);
                            objeto.addPoly(v3, v4, v1).setSmooth(suavizar);
                        } catch (Exception ex) {
                            Logger.getLogger(MeshGenerator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    try {
                        // agrega 2 triangulos
                        objeto.addPoly(v1, v2, v3).setSmooth(suavizar);
                        objeto.addPoly(v3, v4, v1).setSmooth(suavizar);
                    } catch (Exception ex) {
                        Logger.getLogger(MeshGenerator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                // conserva puntos iniciales
                if (j == 0) {
                    p1 = v1;
                    p2 = v4;
                }
            }
            // agrega una cara mas que corresponde a cerrar el punto inicial con el final de
            // los iniciales
            if (cerrarFigura) {
                try {
                    /// 203
                    // 352
                    // agrega 2 triangulos
                    objeto.addPoly(p1, p2, v2);
                    // objeto.addPoly(p1, v3, v2);
                    objeto.addPoly(p2, v3, v2);
                } catch (Exception ex) {
                    Logger.getLogger(MeshGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        // ultimo lado
        return objeto;
    }

    public static Mesh generarSombrero(float alto, float radio, int secciones) {
        Mesh objeto = new Mesh();
        QMaterialBas material = new QMaterialBas("Cilindro");
        // primer paso generar vertices
        objeto.addVertex(0, alto / 2, 0);
        objeto.addVertex(radio, -alto / 4, 0);
        objeto.addVertex(radio * 2, -alto / 2, 0);
        objeto.addVertex(0, -alto / 2, 0);
        objeto = generateRevolutionMesh(objeto, secciones);
        objeto = NormalUtil.calcularNormales(objeto);
        objeto = MaterialUtil.applyMaterial(objeto, material);
        return objeto;
    }

    public static Mesh generarDisco(float radio, int secciones) {
        Mesh objeto = new Mesh();
        QMaterialBas material = new QMaterialBas("Cilindro");
        // primer paso generar vertices
        objeto.addVertex(radio / 2, 0, 0);
        objeto = generateRevolutionMesh(objeto, secciones);
        objeto = NormalUtil.calcularNormales(objeto);
        objeto = MaterialUtil.applyMaterial(objeto, material);
        return objeto;
    }

    public static Mesh generarMediaEsfera(float radio) {
        int secciones = 36;
        return generarMediaEsfera(radio, secciones);
    }

    public static Mesh generarMediaEsfera(float radio, int secciones) {
        Mesh objeto = new Mesh();
        QMaterialBas material = new QMaterialBas("Esfera");
        Vertex inicial = objeto.addVertex(0, radio, 0); // primer vertice
        QVector3 vector = QVector3.of(inicial.location.x, inicial.location.y, inicial.location.z);
        float angulo = 360 / secciones;
        // generamos los vertices el contorno para luego generar el objeto por medio de
        // revolucion
        for (int i = 1; i <= secciones / 4; i++) {// medio circulo
            vector = vector.rotateZ((float) Math.toRadians(angulo));
            objeto.addVertex(vector.x, vector.y, vector.z);
        }

        objeto = generateRevolutionMesh(objeto, secciones, false, false, false, false);
        objeto = NormalUtil.calcularNormales(objeto);
        // el objeto es suavizado
        for (QPrimitiva face : objeto.primitivas) {
            ((Poly) face).setSmooth(true);
        }
        objeto = MaterialUtil.applyMaterial(objeto, material);
        return objeto;
    }

}
