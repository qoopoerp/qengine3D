package net.qoopo.engine.core.entity.component.modifier.generate;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.IcoSphere;
import net.qoopo.engine.core.entity.component.modifier.ModifierComponent;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.util.array.IntArray;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubdivisionModifier implements ModifierComponent {

    private static Logger log = Logger.getLogger("subdivison-modifier");

    public static final int TYPE_SIMPLE = 0;
    public static final int TYPE_CATMULL_CLARK = 1;
    private Entity entity;

    private int type = TYPE_SIMPLE;
    private int times = 1;

    private Long timeMark = -1L;

    private boolean changed;

    public SubdivisionModifier(int type) {
        this.type = type;
    }

    public SubdivisionModifier(int type, int times) {
        this.type = type;
        this.times = times;
    }

    public void changed() {
        this.changed = true;
    }

    @Override
    public void destruir() {

    }

    @Override
    public void apply(Mesh mesh) {
        if (mesh != null && timeMark != mesh.getTimeMark()) {
            subdivisionSurface(mesh, times);
            mesh.computeNormals();
            timeMark = mesh.getTimeMark();
            changed=false;
        }
    }

    /**
     * Realiza una division de la superficie.La superficie debe estar formada
     * por triangulos o cuadrilateros
     *
     * @param veces Veces a dividir (debe ser mayor a 1)
     * @return
     */

    public Mesh subdivisionSurface(Mesh mesh, int times) {
        for (int i = 0; i < times; i++) {
            switch (type) {
                case TYPE_SIMPLE:
                    subdivisionSurfaceSimple(mesh);
                    break;
                case TYPE_CATMULL_CLARK:
                    subdivisionSurfaceCatmullClark(mesh);
                    break;
            }
            cleanDuplicateVertex(mesh);
        }
        log.info("[+] subdivision finished");
        return mesh;
    }

    /**
     * Realiza una division de la superficie.La superficie debe estar formada
     * por triangulos
     *
     * @return
     */
    public Mesh subdivisionSurfaceSimple(Mesh mesh) {
        log.info("[+] Apply subdivision modifier - simple [" + (entity != null ? entity.getName() : "") + "]");
        Vertex[] vertexListPrevious = Arrays.copyOf(mesh.vertexList, mesh.vertexList.length);
        Primitive[] primitiveListPrevious = Arrays.copyOf(mesh.primitiveList, mesh.primitiveList.length);
        QVector3[] normalListPrevious = Arrays.copyOf(mesh.normalList, mesh.normalList.length);
        QVector2[] uvListPrevious = Arrays.copyOf(mesh.uvList, mesh.uvList.length);

        mesh.destruir();
        int c = 0;
        try {
            for (Primitive primitive : primitiveListPrevious) {
                switch (primitive.vertexIndexList.length) {
                    case 3:
                        // primero agrego los vertices originales del triangulo
                        mesh.addVertex(vertexListPrevious[primitive.vertexIndexList[0]]);// 0
                        mesh.addVertex(vertexListPrevious[primitive.vertexIndexList[1]]);// 1
                        mesh.addVertex(vertexListPrevious[primitive.vertexIndexList[2]]);// 2
                        try {
                            mesh.addNormal(normalListPrevious[primitive.normalIndexList[0]]);
                            mesh.addNormal(normalListPrevious[primitive.normalIndexList[1]]);
                            mesh.addNormal(normalListPrevious[primitive.normalIndexList[2]]);
                        } catch (Exception e) {

                        }
                        try {
                            mesh.addUV(uvListPrevious[primitive.uvIndexList[0]]);
                            mesh.addUV(uvListPrevious[primitive.uvIndexList[1]]);
                            mesh.addUV(uvListPrevious[primitive.uvIndexList[2]]);
                        } catch (Exception e) {

                        }

                        // luego agrego los nuevos vertices
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[0]],
                                vertexListPrevious[primitive.vertexIndexList[1]])); // 3
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[1]],
                                vertexListPrevious[primitive.vertexIndexList[2]])); // 4
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[2]],
                                vertexListPrevious[primitive.vertexIndexList[0]])); // 5

                        // ahora agrego las caras
                        mesh.addPoly(primitive.material, IntArray.of(c + 0, c + 3, c + 5),
                                IntArray.of(c + 0, c + 3, c + 5),
                                IntArray.of(c + 0, c + 3, c + 5));
                        mesh.addPoly(primitive.material, IntArray.of(c + 1, c + 4, c + 3),
                                IntArray.of(c + 1, c + 4, c + 3),
                                IntArray.of(c + 1, c + 4, c + 3));
                        mesh.addPoly(primitive.material, IntArray.of(c + 2, c + 5, c + 4),
                                IntArray.of(c + 2, c + 5, c + 4),
                                IntArray.of(c + 2, c + 5, c + 4));
                        mesh.addPoly(primitive.material, IntArray.of(c + 3, c + 4, c + 5),
                                IntArray.of(c + 3, c + 4, c + 5),
                                IntArray.of(c + 3, c + 4, c + 5));
                        // recorro los vertices agregados
                        c += 6;
                        break;
                    case 4:
                        // primero agrego los vertices originales del triangulo
                        mesh.addVertex(vertexListPrevious[primitive.vertexIndexList[0]]);// 0
                        mesh.addVertex(vertexListPrevious[primitive.vertexIndexList[1]]);// 1
                        mesh.addVertex(vertexListPrevious[primitive.vertexIndexList[2]]);// 2
                        mesh.addVertex(vertexListPrevious[primitive.vertexIndexList[3]]);// 3
                        try {
                            mesh.addNormal(normalListPrevious[primitive.normalIndexList[0]]);
                            mesh.addNormal(normalListPrevious[primitive.normalIndexList[1]]);
                            mesh.addNormal(normalListPrevious[primitive.normalIndexList[2]]);
                            mesh.addNormal(normalListPrevious[primitive.normalIndexList[3]]);
                        } catch (Exception e) {

                        }
                        try {
                            mesh.addUV(uvListPrevious[primitive.uvIndexList[0]]);
                            mesh.addUV(uvListPrevious[primitive.uvIndexList[1]]);
                            mesh.addUV(uvListPrevious[primitive.uvIndexList[2]]);
                            mesh.addUV(uvListPrevious[primitive.uvIndexList[3]]);
                        } catch (Exception e) {

                        }
                        // vertice en el centro
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[0]],
                                vertexListPrevious[primitive.vertexIndexList[1]],
                                vertexListPrevious[primitive.vertexIndexList[2]],
                                vertexListPrevious[primitive.vertexIndexList[3]]));// 3

                        // luego agrego los nuevos vertices en los lados
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[0]],
                                vertexListPrevious[primitive.vertexIndexList[1]])); // 4
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[1]],
                                vertexListPrevious[primitive.vertexIndexList[2]])); // 5
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[2]],
                                vertexListPrevious[primitive.vertexIndexList[3]])); // 6
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[3]],
                                vertexListPrevious[primitive.vertexIndexList[0]])); // 7

                        // ahora agrego las caras
                        mesh.addPoly(primitive.material, IntArray.of(c + 0, c + 5, c + 4, c + 8),
                                IntArray.of(c + 0, c + 5, c + 4, c + 8), IntArray.of(c + 0, c + 5, c + 4, c + 8));
                        mesh.addPoly(primitive.material, IntArray.of(c + 5, c + 1, c + 6, c + 4),
                                IntArray.of(c + 5, c + 1, c + 6, c + 4), IntArray.of(c + 5, c + 1, c + 6, c + 4));
                        mesh.addPoly(primitive.material, IntArray.of(c + 4, c + 6, c + 2, c + 7),
                                IntArray.of(c + 4, c + 6, c + 2, c + 7), IntArray.of(c + 4, c + 6, c + 2, c + 7));
                        mesh.addPoly(primitive.material, IntArray.of(c + 8, c + 4, c + 7, c + 3),
                                IntArray.of(c + 8, c + 4, c + 7, c + 3), IntArray.of(c + 8, c + 4, c + 7, c + 3));
                        // recorro los vertices agregados
                        c += 9;
                        break;
                    default:

                        // agrega los mismos vertices sin variar nada
                        int[] antVert = Arrays.copyOf(primitive.vertexIndexList, primitive.vertexIndexList.length);
                        int[] antNormal = Arrays.copyOf(primitive.normalIndexList, primitive.normalIndexList.length);
                        int[] antUV = Arrays.copyOf(primitive.uvIndexList, primitive.uvIndexList.length);
                        int ii = 0;
                        for (int i : primitive.vertexIndexList) {
                            mesh.addVertex(vertexListPrevious[i]);
                            antVert[ii] = c;
                            ii++;
                            c++;
                        }
                        for (int i : antNormal) {
                            mesh.addNormal(normalListPrevious[i]);
                        }
                        for (int i : antUV) {
                            mesh.addUV(uvListPrevious[i]);
                        }
                        mesh.addPoly(primitive.material, antVert, antNormal, antUV);
                        break;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(IcoSphere.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mesh;
    }

    /**
     * Busca el otro triangulo que tiene compartido los vertices v1 y v2
     *
     * @param v1           vertice 1
     * @param v2           vertice 2
     * @param iCaraActual, la cara actual, busca una cara diferente a esta
     * @return
     */
    private Vertex buscarVerticeOpuestoLoop(Vertex v1, Vertex v2, Primitive iCaraActual, Vertex[] vertices,
            Primitive[] primitivas) {
        Vertex v = new Vertex();
        boolean encontrado = false;
        for (Primitive p : primitivas) {
            if (p != iCaraActual) {
                encontrado = false;
                for (int i : p.vertexIndexList) {
                    if (vertices[i].equals(v1) || vertices[i].equals(v2)) {
                        encontrado = true;
                    } else if (encontrado) {
                        v = vertices[i];
                        break;
                    }
                }
            }
        }
        // if (v == null) {
        // System.out.println("ERROR, NO SE ENCONTRO EL VERTICE OPUESTO");
        // }
        return v;
    }

    /**
     * Los puntos de vértice se construyen para cada vértice antiguo. Un vértice
     * dado tiene n vértices vecinos. El nuevo punto de vértice es uno menos n
     * veces s por el vértice anterior, más s veces la suma de los vértices
     * vecinos, donde s es un factor de escala. Para n igual a tres, s es tres
     * dieciseisavos. Para n mayor que tres, s es 1 / n (5/8 - (3/8 + 1/4 cos
     * (2π / n )) 2 )
     *
     * @param v1
     * @param v2
     * @param iCaraActual
     * @param vertices
     * @param primitivas
     * @return
     */
    private Vertex calcularVerticeLoop(Vertex v1, Primitive iCaraActual, Vertex[] vertices,
            Primitive[] primitivas) {

        int n = 0; // numero de vertices vecinos
        // paso 1 , calculamos cuantos vertices son vecinos de este vertice. El numero
        // de vertices vecinos es igual al nuemro de planos que tienen este vertice mas
        // 1.
        Vertex[] vecinos = new Vertex[0];
        for (Primitive p : primitivas) {
            if (p != iCaraActual) {
                for (int i : p.vertexIndexList) {
                    if (vertices[i].equals(v1)) {
                        vecinos = Arrays.copyOf(vecinos, vecinos.length + 1);
                        vecinos[vecinos.length - 1] = vertices[i];
                        n++;
                    }
                }
            }
        }
        n++; // se agrega 1.
        // -------------------------------------
        float s = 0.0f;
        // if (n == 3) {
        // s = 3.0f / 16.0f;
        // } else if (n > 3) {
        float f = (float) (3.0f / 8.0f + 0.25f * Math.cos(QMath.TWO_PI / n));
        // s = 3.0f / 8.0f * (f * f);
        s = 1.0f / n * (5.0f / 8.0f - (f * f));
        // }
        // System.out.println("nuevo punto de vertice ");
        // System.out.println("n=" + n);
        // System.out.println("s=" + s);

        // --------------
        Vertex v = Vertex.add(v1.multiply(1.0f - n * s), Vertex.add(vecinos).multiply(s));
        return v;
    }

    /**
     * Realiza una division de la superficie.La superficie debe estar formada
     * por triangulos
     *
     *
     * http://www.holmes3d.net/graphics/subdivision/
     *
     * http://graphics.stanford.edu/courses/cs468-10-fall/LectureSlides/10_Subdivision.pdf
     *
     * @return
     */
    public Mesh subdivisionSurfaceCatmullClark(Mesh mesh) {

        log.info("[+] Apply subdivision modifier - catmull-clark [" + (entity != null ? entity.getName() : "") + "]");
        Vertex[] vertexListPrevious = Arrays.copyOf(mesh.vertexList, mesh.vertexList.length);
        Primitive[] primitiveListPrevious = Arrays.copyOf(mesh.primitiveList, mesh.primitiveList.length);
        QVector3[] normalListPrevious = Arrays.copyOf(mesh.normalList, mesh.normalList.length);
        QVector2[] uvListPrevious = Arrays.copyOf(mesh.uvList, mesh.uvList.length);
        mesh.destruir();
        int descartados = 0;
        int c = 0;
        try {

            // float factor = (3.0f / 8.0f) + f1 * f1;
            for (Primitive primitive : primitiveListPrevious) {
                switch (primitive.vertexIndexList.length) {
                    case 3:

                        Vertex v1 = vertexListPrevious[primitive.vertexIndexList[0]];
                        Vertex v2 = vertexListPrevious[primitive.vertexIndexList[1]];
                        Vertex v3 = vertexListPrevious[primitive.vertexIndexList[2]];

                        /*
                         * El esquema de bucle se define solo para mallas triangulares, no para mallas
                         * poligonales generales. En cada paso del esquema, cada triángulo se divide en
                         * cuatro triángulos más pequeños.
                         * 
                         * Los puntos de borde se construyen en cada borde. Estos puntos son tres
                         * octavos de la suma de los dos puntos finales del borde más un octavo de la
                         * suma de los otros dos puntos que forman
                         * los dos triángulos que comparten el borde en cuestión.
                         * 
                         * Los puntos de vértice se construyen para cada vértice antiguo. Un vértice
                         * dado tiene n vértices vecinos. El nuevo punto de vértice es uno menos n veces
                         * s por el vértice anterior,
                         * más s veces la suma de los vértices vecinos, donde s es un factor de escala.
                         * Para n igual a tres, s es tres dieciseisavos. Para n mayor que tres, s es 1 /
                         * n (5/8 - (3/8 + 1/4 cos (2π / n )) 2 )
                         * Cada triángulo antiguo tendrá tres puntos de borde, uno para cada borde y
                         * tres puntos de vértice, uno para cada vértice.
                         * 
                         * Para formar los nuevos triángulos, estos puntos se conectan,
                         * vértice-borde-borde,
                         * creando cuatro triángulos.
                         * 
                         * Un nuevo triángulo toca cada vértice anterior, y el último triángulo nuevo se
                         * encuentra en el centro, conectando los tres puntos del borde.
                         * Debido a que las superficies de bucle deben comenzar con una malla
                         * triangular, la superficie resultante no se puede comparar directamente con
                         * los dos esquemas anteriores, que funcionan con polígonos arbitrarios.
                         * La misma secuencia se demuestra aquí en una versión teselada de la malla
                         * poligonal utilizada anteriormente.
                         */
                        // primero agrego los vertices originales del triangulo
                        // addVertex(calcularVerticeLoop(v1, t, v, p));//0
                        // addVertex(calcularVerticeLoop(v2, t, v, p));//1
                        // addVertex(calcularVerticeLoop(v3, t, v, p));//2
                        mesh.addVertex(v1);// 0
                        mesh.addVertex(v2);// 1
                        mesh.addVertex(v3);// 2

                        try {
                            mesh.addNormal(normalListPrevious[primitive.normalIndexList[0]]);
                            mesh.addNormal(normalListPrevious[primitive.normalIndexList[1]]);
                            mesh.addNormal(normalListPrevious[primitive.normalIndexList[2]]);
                        } catch (Exception e) {
                        }
                        try {
                            mesh.addUV(uvListPrevious[primitive.uvIndexList[0]]);
                            mesh.addUV(uvListPrevious[primitive.uvIndexList[1]]);
                            mesh.addUV(uvListPrevious[primitive.uvIndexList[2]]);
                        } catch (Exception e) {
                        }

                        // luego agrego los nuevos vertices (puntos de borde)
                        mesh.addVertex(Vertex.add(Vertex.add(v1, v2).multiply(3.0f / 8.0f),
                                Vertex.add(v3, buscarVerticeOpuestoLoop(v1, v2, primitive, vertexListPrevious,
                                        primitiveListPrevious)).multiply(1.0f / 8.0f))); // 3
                        mesh.addVertex(Vertex.add(Vertex.add(v2, v3).multiply(3.0f / 8.0f),
                                Vertex.add(v1, buscarVerticeOpuestoLoop(v2, v3, primitive, vertexListPrevious,
                                        primitiveListPrevious)).multiply(1.0f / 8.0f))); // 4
                        mesh.addVertex(Vertex.add(Vertex.add(v3, v1).multiply(3.0f / 8.0f),
                                Vertex.add(v2, buscarVerticeOpuestoLoop(v3, v1, primitive, vertexListPrevious,
                                        primitiveListPrevious)).multiply(1.0f / 8.0f))); // 5

                        // normales

                        // uvs

                        // ahora agrego las caras
                        mesh.addPoly(primitive.material, IntArray.of(c + 0, c + 3, c + 5),
                                IntArray.of(c + 0, c + 3, c + 5),
                                IntArray.of(c + 0, c + 3, c + 5));
                        mesh.addPoly(primitive.material, IntArray.of(c + 1, c + 4, c + 3),
                                IntArray.of(c + 1, c + 4, c + 3),
                                IntArray.of(c + 1, c + 4, c + 3));
                        mesh.addPoly(primitive.material, IntArray.of(c + 2, c + 5, c + 4),
                                IntArray.of(c + 2, c + 5, c + 4),
                                IntArray.of(c + 2, c + 5, c + 4));
                        mesh.addPoly(primitive.material, IntArray.of(c + 3, c + 4, c + 5),
                                IntArray.of(c + 3, c + 4, c + 5),
                                IntArray.of(c + 3, c + 4, c + 5));
                        // recorro los vertices agregados
                        c += 6;
                        break;
                    case 4:

                        descartados++;
                        /*
                         * El esquema Los nuevos polígonos se construyen a partir de la malla anterior
                         * de la siguiente manera.
                         * Se crea un punto frontal para cada polígono antiguo, definido como el
                         * promedio de cada punto del polígono. Se crea un punto de borde para cada
                         * borde antiguo,
                         * definido como el promedio del punto medio del borde original y el punto medio
                         * de los dos nuevos puntos de la cara para los polígonos que lindan con el
                         * borde original.
                         * Y finalmente, se definen nuevos puntos de vértice .
                         * Por cada vértice antiguo, hay n polígonos que lo comparten. El nuevo vértice
                         * es ( n - 3) / n ) multiplicado por el antiguo vértice + (1 / n ) multiplicado
                         * por el promedio de los puntos
                         * frontales de los polígonos contiguos + (2 / n) multiplicado por el promedio
                         * de los puntos medios de las aristas que tocan el vértice antiguo.
                         * Esto da un punto cercano, pero generalmente no precisamente en, el vértice
                         * anterior.
                         * Entonces se conectan los nuevos puntos. Esto es sencillo: cada punto de cara
                         * se conecta a un punto de borde, que se conecta a un nuevo punto de vértice,
                         * que se conecta al punto de borde del borde contiguo, que vuelve al punto de
                         * cara.
                         * Esto se hace para cada uno de esos cuádruples, desplegando cuadriláteros
                         * alrededor de las caras. El esquema solo produce cuadriláteros, aunque no son
                         * necesariamente planos.
                         */
                        // primero agrego los vertices originales del cuadrilatero
                        mesh.addVertex(vertexListPrevious[primitive.vertexIndexList[0]]);// 0
                        mesh.addVertex(vertexListPrevious[primitive.vertexIndexList[1]]);// 1
                        mesh.addVertex(vertexListPrevious[primitive.vertexIndexList[2]]);// 2
                        mesh.addVertex(vertexListPrevious[primitive.vertexIndexList[3]]);// 3

                        // vertice en el centro
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[0]],
                                vertexListPrevious[primitive.vertexIndexList[1]],
                                vertexListPrevious[primitive.vertexIndexList[2]],
                                vertexListPrevious[primitive.vertexIndexList[3]]));// 3

                        // luego agrego los nuevos vertices en los lados
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[0]],
                                vertexListPrevious[primitive.vertexIndexList[1]])); // 4
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[1]],
                                vertexListPrevious[primitive.vertexIndexList[2]])); // 5
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[2]],
                                vertexListPrevious[primitive.vertexIndexList[3]])); // 6
                        mesh.addVertex(Vertex.promediar(vertexListPrevious[primitive.vertexIndexList[3]],
                                vertexListPrevious[primitive.vertexIndexList[0]])); // 7
                        // ahora agrego las caras
                        mesh.addPoly(primitive.material, IntArray.of(c + 0, c + 5, c + 4, c + 8));
                        mesh.addPoly(primitive.material, IntArray.of(c + 5, c + 1, c + 6, c + 4));
                        mesh.addPoly(primitive.material, IntArray.of(c + 4, c + 6, c + 2, c + 7));
                        mesh.addPoly(primitive.material, IntArray.of(c + 8, c + 4, c + 7, c + 3));
                        // recorro los vertices agregados
                        c += 9;
                        break;
                    default:
                        descartados++;
                        // agrega los mismos vertices sin variar nada
                        int[] antVert = Arrays.copyOf(primitive.vertexIndexList, primitive.vertexIndexList.length);
                        int[] antNormal = Arrays.copyOf(primitive.normalIndexList, primitive.normalIndexList.length);
                        int[] antUV = Arrays.copyOf(primitive.uvIndexList, primitive.uvIndexList.length);
                        int ii = 0;
                        for (int i : primitive.vertexIndexList) {
                            mesh.addVertex(vertexListPrevious[i]);
                            antVert[ii] = c;
                            ii++;
                            c++;
                        }
                        for (int i : antNormal) {
                            mesh.addNormal(normalListPrevious[i]);
                        }
                        for (int i : antUV) {
                            mesh.addUV(uvListPrevious[i]);
                        }
                        mesh.addPoly(primitive.material, antVert, antNormal, antUV);
                        break;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(IcoSphere.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Division terminada, se descartan " + descartados + " poligonos");
        return mesh;
    }

    /**
     * Se busca los vertices duplicados (que tienen la misma ubicacion), y los
     * elimina dejando solo uno de ellos. Modifica los poligonos para apunten al
     * vertice que queda
     *
     * @return
     */
    public Mesh cleanDuplicateVertex(Mesh mesh) {
        boolean repetir = false;
        log.info("[+] cleanDuplicateVertex");
        // Recorro los vertices

        int i = 0;
        int j = 0;
        for (i = 0; i < mesh.vertexList.length - 1 && !repetir; i++) {
            // pregunto por todos los demas vertices
            for (j = i + 1; j < mesh.vertexList.length && !repetir; j++) {
                if (i != j) {
                    if (mesh.vertexList[i].location.equals(mesh.vertexList[j].location)) {
                        // eliminamos el vertice j, los vertices que estan despues lo bajamos una
                        // posicion
                        for (int k = j; k < mesh.vertexList.length - 1; k++) {
                            mesh.vertexList[k] = mesh.vertexList[k + 1];
                        }
                        // en las caras buscamos los que tengan el vertice j para cambiarlo por i, y los
                        // que son superiores a j le restamos 1.
                        for (Primitive p : mesh.primitiveList) {
                            for (int l = 0; l < p.vertexIndexList.length; l++) {
                                // si es (j), lo cambiamos por el vertice que se va a quedar (i)
                                if (p.vertexIndexList[l] == j) {
                                    p.vertexIndexList[l] = i;
                                } else if (p.vertexIndexList[l] > j) {
                                    // si es superior a j, le restamos 1
                                    p.vertexIndexList[l]--;
                                }
                            }
                        }
                        repetir = true;
                    }
                }
            }
        }
        // if (repetir) {
        // // cambiamos la dimension de los vertices para eliminar la ultima posicion
        // // System.out.println("Eliminando vertices.. actual:" + i + " quedan " +
        // // (vertices.length - 1));
        // mesh.vertexList = Arrays.copyOf(mesh.vertexList, mesh.vertexList.length - 1);
        // cleanDuplicateVertex(mesh);
        // }
        return mesh;
    }

}
