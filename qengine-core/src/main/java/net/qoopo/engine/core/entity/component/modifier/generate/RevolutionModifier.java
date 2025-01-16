package net.qoopo.engine.core.entity.component.modifier.generate;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.generator.MeshGenerator;
import net.qoopo.engine.core.entity.component.modifier.ModifierComponent;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.util.array.IntArray;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * Genera objetos de revolución El objeto pasado como parametro solo
 * contiene vértices que corresponden a media silueta del objeto
 */
public class RevolutionModifier implements ModifierComponent {

    private static Logger log = Logger.getLogger("revolution-modifier");

    private Entity entity;

    private long timeMark = -1L;

    private int sides = 16;
    private boolean smooth = false;
    private boolean smoothTops = false;

    private boolean close = false;
    private boolean likeTorus = false;

    private boolean changed;

    public RevolutionModifier(int sides) {
        this.sides = sides;
    }

    public RevolutionModifier(int sides, boolean smooth, boolean smoothTops, boolean close, boolean likeTorus) {
        this.sides = sides;
        this.smooth = smooth;
        this.smoothTops = smoothTops;
        this.close = close;
        this.likeTorus = likeTorus;
    }

    @Override
    public void destroy() {

    }

    public void changed() {
        this.changed = true;
    }

    @Override
    public void apply(Mesh mesh) {
        if (mesh != null && timeMark != mesh.getTimeMark()) {
            generateRevolutionMesh(mesh, sides, close, likeTorus, smooth, smoothTops);
            mesh.computeNormals();
            timeMark = mesh.getTimeMark();
            changed = false;
        }
    }

    /**
     * Genera objetos de revolución El objeto pasado como parametro solo
     * contiene vértices que corresponden a media silueta del objeto
     *
     * @param mesh        Objeto con los vertices
     * @param lados
     * @param close       agrega una cara mas que corresponde a cerrar el punto
     *                    inicial con el final de los iniciales
     * @param tipotoro
     * @param smooth      marca las caras como suaves
     * @param smoothTops, marca como suave las caras superiores e inferiores,
     *                    se puede usar para los cilindros y conos
     * @return
     */
    public Mesh generateRevolutionMesh(Mesh mesh, int lados, boolean close, boolean tipotoro,
            boolean smooth, boolean smoothTops) {
        float angulo = (float) Math.toRadians((float) 360 / lados);
        int puntos_iniciales = mesh.vertexList.length;

        // generamos los siguientes puntos, comienza en 1 porque ya existe un lado (con
        // el que se manda a crear objetos de revolucion)
        for (int i = 1; i < lados; i++) {
            // recorremos los puntos iniciales solamente
            for (int j = 0; j < puntos_iniciales; j++) {
                QVector3 tmp = QVector3.of(mesh.vertexList[j].location.x, mesh.vertexList[j].location.y,
                        mesh.vertexList[j].location.z);
                // se rota en el ejey de las Y cada punto
                tmp.rotateY(angulo * i);// se rota solo en Eje de Y
                // agrega el vertice rotado
                mesh.addVertex(tmp);
                // pendiente la coordenada uv
                mesh.addUV((float) 1.0f / lados * i, mesh.uvList[j].y);
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
                            mesh.addPoly(IntArray.of(0, v2, v3)).setSmooth(smoothTops);
                        } catch (Exception ex) {
                            Logger.getLogger(MeshGenerator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (j == poligonos_x_lado - 1) {
                        try {
                            // objeto.addPoly(v3, v4, v1);
                            mesh.addPoly(IntArray.of(j + 1, v4, v1)).setSmooth(smoothTops);
                        } catch (Exception ex) {
                            Logger.getLogger(MeshGenerator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            // agrega 2 triangulos
                            mesh.addPoly(IntArray.of(v1, v2, v3)).setSmooth(smooth);
                            mesh.addPoly(IntArray.of(v3, v4, v1)).setSmooth(smooth);
                        } catch (Exception ex) {
                            Logger.getLogger(MeshGenerator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    try {
                        // agrega 2 triangulos
                        mesh.addPoly(IntArray.of(v1, v2, v3)).setSmooth(smooth);
                        mesh.addPoly(IntArray.of(v3, v4, v1)).setSmooth(smooth);
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
            if (close) {
                try {
                    /// 203
                    // 352
                    // agrega 2 triangulos
                    mesh.addPoly(IntArray.of(p1, p2, v2));
                    // objeto.addPoly(p1, v3, v2);
                    mesh.addPoly(IntArray.of(p2, v3, v2));
                } catch (Exception ex) {
                    Logger.getLogger(MeshGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        // ultimo lado
        return mesh;
    }

}
