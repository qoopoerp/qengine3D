package net.qoopo.engine.core.entity.component.mesh;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.primitive.Line;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.IcoSphere;
import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.math.QVector4;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.mesh.NormalUtil;

public class Mesh extends EntityComponent {

    public static final int GEOMETRY_TYPE_MESH = 0;// malla
    public static final int GEOMETRY_TYPE_MATH = 1;// funcion matematica
    public static final int GEOMETRY_TYPE_PATH = 2;// ruta
    public static final int GEOMETRY_TYPE_WIRE = 3; // alambre (formado por triangulos)
    public static final int GEOMETRY_TYPE_SEGMENT = 4; // alambre por lineas

    public int tipo = GEOMETRY_TYPE_MESH;
    public String nombre = "";// usada para identificar los objetos cargados , luego la eliminamos
    public Vertex[] vertices = new Vertex[0];
    public QPrimitiva[] primitivas = new QPrimitiva[0];

    public Mesh() {
        tipo = GEOMETRY_TYPE_MESH;
    }

    public Mesh(int type) {
        this.tipo = type;
    }

    public void removeVertex(int indice) {
        System.arraycopy(vertices, indice + 1, vertices, indice, vertices.length - 1 - indice);
    }

    public void removePoly(int indice) {
        System.arraycopy(primitivas, indice + 1, primitivas, indice, primitivas.length - 1 - indice);
    }

    public Vertex addVertex() {
        Vertex nuevo = new Vertex();
        vertices = Arrays.copyOf(vertices, vertices.length + 1);
        vertices[vertices.length - 1] = nuevo;
        return nuevo;
    }

    public Vertex addVertex(Vertex vertice) {
        Vertex nuevo = vertice.clone();
        vertices = Arrays.copyOf(vertices, vertices.length + 1);
        vertices[vertices.length - 1] = nuevo;
        return nuevo;
    }

    public Vertex addVertex(QVector3 posicion) {
        Vertex nuevo = new Vertex(posicion.x, posicion.y, posicion.z);
        vertices = Arrays.copyOf(vertices, vertices.length + 1);
        vertices[vertices.length - 1] = nuevo;
        return nuevo;
    }

    public Vertex addVertex(QVector4 posicion) {
        Vertex nuevo = new Vertex(posicion.x, posicion.y, posicion.z, posicion.w);
        vertices = Arrays.copyOf(vertices, vertices.length + 1);
        vertices[vertices.length - 1] = nuevo;
        return nuevo;
    }

    public Vertex addVertex(QVector3 posicion, float u, float v) {
        Vertex nuevo = new Vertex(posicion.x, posicion.y, posicion.z, 1, u, v);
        vertices = Arrays.copyOf(vertices, vertices.length + 1);
        vertices[vertices.length - 1] = nuevo;
        return nuevo;
    }

    public Vertex addVertex(QVector4 posicion, float u, float v) {
        Vertex nuevo = new Vertex(posicion.x, posicion.y, posicion.z, posicion.w, u, v);
        vertices = Arrays.copyOf(vertices, vertices.length + 1);
        vertices[vertices.length - 1] = nuevo;
        return nuevo;
    }

    public Vertex addVertex(float x, float y, float z) {
        Vertex nuevo = new Vertex(x, y, z, 1);
        vertices = Arrays.copyOf(vertices, vertices.length + 1);
        vertices[vertices.length - 1] = nuevo;
        return nuevo;
    }

    public Vertex addVertex(float x, float y, float z, float u, float v) {
        Vertex nuevo = new Vertex(x, y, z, 1, u, v);
        vertices = Arrays.copyOf(vertices, vertices.length + 1);
        vertices[vertices.length - 1] = nuevo;
        return nuevo;
    }

    public Poly addPoly() {
        Poly nuevo = new Poly(this);
        primitivas = Arrays.copyOf(primitivas, primitivas.length + 1);
        primitivas[primitivas.length - 1] = nuevo;
        return nuevo;
    }

    public Line agregarLinea(int... vertices) throws Exception {
        validarVertices(vertices);
        Line nuevo = new Line(this, vertices);
        primitivas = Arrays.copyOf(primitivas, primitivas.length + 1);
        primitivas[primitivas.length - 1] = nuevo;
        return nuevo;
    }

    public Line agregarLinea(AbstractMaterial material, int... vertices) throws Exception {
        validarVertices(vertices);
        Line nuevo = new Line(this, vertices);
        nuevo.material = material;
        primitivas = Arrays.copyOf(primitivas, primitivas.length + 1);
        primitivas[primitivas.length - 1] = nuevo;
        return nuevo;
    }

    public Poly addPoly(int... vertices) throws Exception {
        validarVertices(vertices);
        Poly nuevo = new Poly(this, vertices);
        primitivas = Arrays.copyOf(primitivas, primitivas.length + 1);
        primitivas[primitivas.length - 1] = nuevo;
        return nuevo;
    }

    public Poly addPoly(AbstractMaterial material, int... vertices) throws Exception {
        validarVertices(vertices);
        Poly nuevo = new Poly(this, vertices);
        nuevo.material = material;
        primitivas = Arrays.copyOf(primitivas, primitivas.length + 1);
        primitivas[primitivas.length - 1] = nuevo;
        return nuevo;
    }

    public Poly addPoly(AbstractMaterial material, boolean smooth, int... vertices) throws Exception {
        validarVertices(vertices);
        Poly nuevo = new Poly(this, vertices);
        nuevo.setSmooth(smooth);
        nuevo.material = material;
        primitivas = Arrays.copyOf(primitivas, primitivas.length + 1);
        primitivas[primitivas.length - 1] = nuevo;
        return nuevo;
    }

    @Override
    public Mesh clone() {
        Mesh nuevo = new Mesh(this.tipo);
        for (Vertex current : vertices) {
            nuevo.addVertex(current.location.x, current.location.y, current.location.z).normal = current.normal.clone();
        }
        for (QPrimitiva face : primitivas) {
            if (face instanceof Poly) {
                Poly poligono = nuevo.addPoly();
                poligono.setVertices(Arrays.copyOf(face.listaVertices, face.listaVertices.length));
                poligono.material = face.material;
                poligono.setNormal(((Poly) face).getNormal());
                poligono.setSmooth(((Poly) face).isSmooth());
            }
        }
        return nuevo;
    }

    public void destroy() {
        try {
            for (QPrimitiva face : primitivas) {
                face.geometria = null;
            }
        } catch (Exception e) {

        }
        primitivas = null;
    }

    @Override
    public void destruir() {
        vertices = null;
        primitivas = null;
    }

    private void validarVertices(int... vertices) throws Exception {
        for (int i : vertices) {
            if (i > this.vertices.length) {
                throw new Exception("Se esta agregando indices que no existen ");
            }
        }
    }

    protected void deleteData() {
        this.destroy();
        this.vertices = new Vertex[0];
        this.primitivas = new QPrimitiva[0];
    }

    public void calculateNormals() {
        NormalUtil.calcularNormales(this);
    }

    public Mesh smooth() {
        MaterialUtil.smooth(this, true);
        return this;
    }

    public Mesh unSmooth() {
        MaterialUtil.smooth(this, false);
        return this;
    }

    public void applyMaterial(AbstractMaterial material) {
        MaterialUtil.applyMaterial(this, material);
    }

    /**
     * Infla los vertices de forma esferica
     *
     * @param radio
     * @return
     */
    public Mesh inflate(float radio) {
        for (Vertex ve : this.vertices) {
            double escala = radio / (Math.sqrt(
                    ve.location.x * ve.location.x + ve.location.y * ve.location.y + ve.location.z * ve.location.z));
            ve.location.set(ve.location.getVector3().multiply((float) escala), 1);
        }
        return this;
    }

    /**
     * Realiza una division de la superficie.La superficie debe estar formada
     * por triangulos o cuadrilateros
     *
     * @param veces Veces a dividir (debe ser mayor a 1)
     * @return
     */
    public Mesh dividir(int veces) {
        for (int i = 1; i <= veces - 1; i++) {
            this.dividir();
            cleanDuplicateVertex();
        }
        return this;
    }

    /**
     * Realiza una division de la superficie.La superficie debe estar formada
     * por triangulos
     *
     * @return
     */
    public Mesh dividir() {
        Vertex[] v = Arrays.copyOf(vertices, vertices.length);
        QPrimitiva[] p = Arrays.copyOf(primitivas, primitivas.length);
        deleteData();
        int c = 0;
        try {
            for (QPrimitiva t : p) {
                switch (t.listaVertices.length) {
                    case 3:
                        // primero agrego los vertices originales del triangulo
                        addVertex(v[t.listaVertices[0]]);// 0
                        addVertex(v[t.listaVertices[1]]);// 1
                        addVertex(v[t.listaVertices[2]]);// 2
                        // luego agrego los nuevos vertices
                        addVertex(Vertex.promediar(v[t.listaVertices[0]], v[t.listaVertices[1]])); // 3
                        addVertex(Vertex.promediar(v[t.listaVertices[1]], v[t.listaVertices[2]])); // 4
                        addVertex(Vertex.promediar(v[t.listaVertices[2]], v[t.listaVertices[0]])); // 5
                        // ahora agrego las caras
                        addPoly(t.material, c + 0, c + 3, c + 5);
                        addPoly(t.material, c + 1, c + 4, c + 3);
                        addPoly(t.material, c + 2, c + 5, c + 4);
                        addPoly(t.material, c + 3, c + 4, c + 5);
                        // recorro los vertices agregados
                        c += 6;
                        break;
                    case 4:
                        // primero agrego los vertices originales del triangulo
                        addVertex(v[t.listaVertices[0]]);// 0
                        addVertex(v[t.listaVertices[1]]);// 1
                        addVertex(v[t.listaVertices[2]]);// 2
                        addVertex(v[t.listaVertices[3]]);// 3

                        // vertice en el centro
                        addVertex(Vertex.promediar(v[t.listaVertices[0]], v[t.listaVertices[1]], v[t.listaVertices[2]],
                                v[t.listaVertices[3]]));// 3

                        // luego agrego los nuevos vertices en los lados
                        addVertex(Vertex.promediar(v[t.listaVertices[0]], v[t.listaVertices[1]])); // 4
                        addVertex(Vertex.promediar(v[t.listaVertices[1]], v[t.listaVertices[2]])); // 5
                        addVertex(Vertex.promediar(v[t.listaVertices[2]], v[t.listaVertices[3]])); // 6
                        addVertex(Vertex.promediar(v[t.listaVertices[3]], v[t.listaVertices[0]])); // 7
                        // ahora agrego las caras
                        addPoly(t.material, c + 0, c + 5, c + 4, c + 8);
                        addPoly(t.material, c + 5, c + 1, c + 6, c + 4);
                        addPoly(t.material, c + 4, c + 6, c + 2, c + 7);
                        addPoly(t.material, c + 8, c + 4, c + 7, c + 3);
                        // recorro los vertices agregados
                        c += 9;
                        break;
                    default:
                        // agrega los mismos vertices sin variar nada
                        int[] antVert = Arrays.copyOf(t.listaVertices, t.listaVertices.length);
                        int ii = 0;
                        for (int i : t.listaVertices) {
                            addVertex(v[i]);
                            antVert[ii] = c;
                            ii++;
                            c++;
                        }
                        addPoly(t.material, antVert);
                        break;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(IcoSphere.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    /**
     * Realiza una division de la superficie.La superficie debe estar formada
     * por triangulos
     *
     * @param veces Veces a dividir (debe ser mayor a 1)
     * @return
     */
    public Mesh dividirCatmullClark(int veces) {
        for (int i = 1; i <= veces - 1; i++) {
            this.dividirCatmullClark();
        }
        return this;
    }

    /**
     * Busca el otro triangulo que tiene compartido los vertices v1 y v2
     *
     * @param v1           vertice 1
     * @param v2           vertice 2
     * @param iCaraActual, la cara actual, busca una cara diferente a esta
     * @return
     */
    private Vertex buscarVerticeOpuestoLoop(Vertex v1, Vertex v2, QPrimitiva iCaraActual, Vertex[] vertices,
            QPrimitiva[] primitivas) {
        Vertex v = new Vertex();
        boolean encontrado = false;
        for (QPrimitiva p : primitivas) {
            if (p != iCaraActual) {
                encontrado = false;
                for (int i : p.listaVertices) {
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
    private Vertex calcularVerticeLoop(Vertex v1, QPrimitiva iCaraActual, Vertex[] vertices,
            QPrimitiva[] primitivas) {

        int n = 0; // numero de vertices vecinos
        // paso 1 , calculamos cuantos vertices son vecinos de este vertice. El numero
        // de vertices vecinos es igual al nuemro de planos que tienen este vertice mas
        // 1.
        Vertex[] vecinos = new Vertex[0];
        for (QPrimitiva p : primitivas) {
            if (p != iCaraActual) {
                for (int i : p.listaVertices) {
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
        Vertex v = Vertex.sumar(v1.multiply(1.0f - n * s), Vertex.sumar(vecinos).multiply(s));
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
    public Mesh dividirCatmullClark() {
        Vertex[] v = Arrays.copyOf(vertices, vertices.length);
        QPrimitiva[] p = Arrays.copyOf(primitivas, primitivas.length);

        deleteData();

        int descartados = 0;
        int c = 0;
        try {

            // float factor = (3.0f / 8.0f) + f1 * f1;
            for (QPrimitiva t : p) {
                switch (t.listaVertices.length) {
                    case 3:

                        Vertex v1,
                                v2,
                                v3;
                        v1 = v[t.listaVertices[0]];
                        v2 = v[t.listaVertices[1]];
                        v3 = v[t.listaVertices[2]];

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
                        addVertex(v1);// 0
                        addVertex(v2);// 1
                        addVertex(v3);// 2

                        // luego agrego los nuevos vertices (puntos de borde)
                        addVertex(Vertex.sumar(Vertex.sumar(v1, v2).multiply(3.0f / 8.0f),
                                Vertex.sumar(v3, buscarVerticeOpuestoLoop(v1, v2, t, v, p)).multiply(1.0f / 8.0f))); // 3
                        addVertex(Vertex.sumar(Vertex.sumar(v2, v3).multiply(3.0f / 8.0f),
                                Vertex.sumar(v1, buscarVerticeOpuestoLoop(v2, v3, t, v, p)).multiply(1.0f / 8.0f))); // 4
                        addVertex(Vertex.sumar(Vertex.sumar(v3, v1).multiply(3.0f / 8.0f),
                                Vertex.sumar(v2, buscarVerticeOpuestoLoop(v3, v1, t, v, p)).multiply(1.0f / 8.0f))); // 5
                        // addVertex(QVertice.promediar(v1, v2)); //3
                        // addVertex(QVertice.promediar(v2, v3)); //4
                        // addVertex(QVertice.promediar(v3, v1)); //5

                        // ahora agrego las caras
                        addPoly(t.material, c + 0, c + 3, c + 5);
                        addPoly(t.material, c + 1, c + 4, c + 3);
                        addPoly(t.material, c + 2, c + 5, c + 4);
                        addPoly(t.material, c + 3, c + 4, c + 5);
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
                        addVertex(v[t.listaVertices[0]]);// 0
                        addVertex(v[t.listaVertices[1]]);// 1
                        addVertex(v[t.listaVertices[2]]);// 2
                        addVertex(v[t.listaVertices[3]]);// 3

                        // vertice en el centro
                        addVertex(Vertex.promediar(v[t.listaVertices[0]], v[t.listaVertices[1]], v[t.listaVertices[2]],
                                v[t.listaVertices[3]]));// 3

                        // luego agrego los nuevos vertices en los lados
                        addVertex(Vertex.promediar(v[t.listaVertices[0]], v[t.listaVertices[1]])); // 4
                        addVertex(Vertex.promediar(v[t.listaVertices[1]], v[t.listaVertices[2]])); // 5
                        addVertex(Vertex.promediar(v[t.listaVertices[2]], v[t.listaVertices[3]])); // 6
                        addVertex(Vertex.promediar(v[t.listaVertices[3]], v[t.listaVertices[0]])); // 7
                        // ahora agrego las caras
                        addPoly(t.material, c + 0, c + 5, c + 4, c + 8);
                        addPoly(t.material, c + 5, c + 1, c + 6, c + 4);
                        addPoly(t.material, c + 4, c + 6, c + 2, c + 7);
                        addPoly(t.material, c + 8, c + 4, c + 7, c + 3);
                        // recorro los vertices agregados
                        c += 9;
                        break;
                    default:
                        descartados++;
                        // agrega los mismos vertices sin variar nada
                        int[] antVert = Arrays.copyOf(t.listaVertices, t.listaVertices.length);
                        int ii = 0;
                        for (int i : t.listaVertices) {
                            addVertex(v[i]);
                            antVert[ii] = c;
                            ii++;
                            c++;
                        }
                        addPoly(t.material, antVert);
                        break;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(IcoSphere.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Division terminada, se descartan " + descartados + " poligonos");
        return this;
    }

    /**
     * Se busca los vertices duplicados (que tienen la misma ubicacion), y los
     * elimina dejando solo uno de ellos. Modifica los poligonos para apunten al
     * vertice que queda
     *
     * @return
     */
    public Mesh cleanDuplicateVertex() {
        boolean repetir = false;
        // Recorro los vertices

        int i = 0;
        int j = 0;
        for (i = 0; i < vertices.length - 1 && !repetir; i++) {
            // pregunto por todos los demas vertices
            for (j = i + 1; j < vertices.length && !repetir; j++) {
                if (i != j) {
                    if (vertices[i].location.equals(vertices[j].location)) {
                        // eliminamos el vertice j, los vertices que estan despues lo bajamos una
                        // posicion
                        for (int k = j; k < vertices.length - 1; k++) {
                            vertices[k] = vertices[k + 1];
                        }
                        // en las caras buscamos los que tengan el vertice j para cambiarlo por i, y los
                        // que son superiores a j le restamos 1.
                        for (QPrimitiva p : primitivas) {
                            for (int l = 0; l < p.listaVertices.length; l++) {
                                // si es (j), lo cambiamos por el vertice que se va a quedar (i)
                                if (p.listaVertices[l] == j) {
                                    p.listaVertices[l] = i;
                                } else if (p.listaVertices[l] > j) {
                                    // si es superior a j, le restamos 1
                                    p.listaVertices[l]--;
                                }
                            }
                        }
                        repetir = true;
                    }
                }
            }
        }
        if (repetir) {
            // cambiamos la dimension de los vertices para eliminar la ultima posicion
            // System.out.println("Eliminando vertices.. actual:" + i + " quedan " +
            // (vertices.length - 1));
            vertices = Arrays.copyOf(vertices, vertices.length - 1);
            cleanDuplicateVertex();
        }
        return this;
    }

}
