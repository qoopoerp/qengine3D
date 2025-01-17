/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.scene;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.assets.model.waveobject.LoadModelObj;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.math.QVector4;
import net.qoopo.engine.core.util.ComponentUtil;
import net.qoopo.engine.core.util.TempVars;
import net.qoopo.engine.core.util.array.IntArray;

/**
 * La cámara
 *
 * @author aigarcia
 */
@Getter
@Setter
public class Camera extends Entity {

    private static final Mesh GEOMETRIA_FRUSTUM = new Mesh();
    private static final Material MATERIAL;

    static {
        MATERIAL = new Material();
        MATERIAL.setColor(new QColor(1, 1, 204.0f / 255.0f));
        MATERIAL.setTransAlfa(0.3f);
        MATERIAL.setTransparent(true);
    }
    private float escalaOrtogonal = 0.0f;
    private boolean ortogonal = false;
    private float aspectRatio = 800.0f / 600.0f;

    /**
     * Angulo de Vision de la Camara , Los humanos tenemos un angulo de vision
     * de 60 grados
     */
    private float FOV = (float) Math.toRadians(60.0f);

    // ---------------------- datos para el procesos con matrices
    // ----------------------------
    public float frustrumCerca;
    /**
     * Distance from camera to far frustum plane.
     */
    public float frustrumLejos;
    /**
     * Distance from camera to left frustum plane.
     */
    public float frustumIzquierda;
    /**
     * Distance from camera to right frustum plane.
     */
    public float frustumDerecha;
    /**
     * Distance from camera to top frustum plane.
     */
    public float frustumArriba;
    /**
     * Distance from camera to bottom frustum plane.
     */
    public float frustumAbajo;

    /**
     * Los planos de recorte del frustum de la camara
     */
    private final ClipPane[] clipPane = new ClipPane[6];

    /**
     * Matriz para aplicar la proyeccion
     */
    private final QMatriz4 matrizProyeccion = new QMatriz4();

    public Camera() {
        super("Cam ", false);
        iniciar();
        addGeometry();
        addComponent(GEOMETRIA_FRUSTUM);
    }

    public Camera(String name) {
        super(name, false);
        iniciar();
        addGeometry();
        addComponent(GEOMETRIA_FRUSTUM);
    }

    private void addGeometry() {

        ModelLoader loadModel = new LoadModelObj();
        Entity ent = loadModel.loadModel(Camera.class.getResourceAsStream("/models/camera/camera.obj"));
        for (Entity child : ent.getChilds()) {
            addComponent(ComponentUtil.getMesh(child));
        }

        // public static final Mesh GEOMETRIA_CAM = QUtilComponentes.getMesh(
        // LoadModelObj.cargarWaveObject(QCamera.class.getResourceAsStream("/modelos/camara/camara.obj")).get(0));
        // public static final Mesh GEOMETRIA_CAM_1 = QUtilComponentes.getMesh(
        // LoadModelObj.cargarWaveObject(QCamera.class.getResourceAsStream("/modelos/camara/camara.obj")).get(1));
        // addComponent(GEOMETRIA_CAM);
        // addComponent(GEOMETRIA_CAM_1);
    }

    public void iniciar() {
        transform.getLocation().set(QVector3.zero);
        transform.getRotation().reset();
        escalaOrtogonal = 1.0f;
        frustrumCerca = 1.0f;
        frustrumLejos = 100f;
        updateCamera();
    }

    /**
     * Comprueba que el punto ubicado en x,y,z, se encuentre en el campo de
     * vision de la camara
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean isVisible(float x, float y, float z) {
        boolean visible = true;
        TempVars t = TempVars.get();
        t.vector3f1.set(x, y, z);
        // visible = (planosRecorte[0].distancia(t.vector3f1) > 0.005f);
        // visible = (planosRecorte[1].distancia(t.vector3f1) > 0.005f);
        // for (int i = 0; i < 2; i++) { //solo far plane y near plane
        for (int i = 0; i < 6; i++) {
            if (!this.clipPane[i].isVisible(t.vector3f1)) {
                // if (!(this.planosRecorte[i].distancia(t.vector3f1) > 0.005f)) {
                visible = false;
            }
        }
        t.release();
        return visible;
    }

    /**
     * Obtiene el alfa (factor de interpolacion) para obtener el vertice
     * recortado
     *
     * @param v1
     * @param v2
     * @return
     */
    public float getClipedVerticeAlfa(QVector3 v1, QVector3 v2) {
        float alfa = 0.0f;
        // for (int i = 0; i < 2; i++) { //solo far plane y near plane
        for (int i = 0; i < 6; i++) {
            // if (!this.planosRecorte[i].esVisible(v1) ||
            // !this.planosRecorte[i].esVisible(v2)) {
            if (!this.clipPane[i].isVisible(v1) && this.clipPane[i].isVisible(v2)
                    || this.clipPane[i].isVisible(v1) && !this.clipPane[i].isVisible(v2)) {
                float da = this.clipPane[i].distancia(v1); // distance plane -> point a
                float db = this.clipPane[i].distancia(v2); // distance plane -> point b
                return da / (da - db); // intersection factor (between 0 and 1)
            }
        }
        return alfa;
    }

    public boolean isVisible(Vertex vertice) {
        try {
            return isVisible(vertice.location);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @param vertice
     * @return
     */
    public boolean isVisible(QVector3 vertice) {
        return isVisible(vertice.x, vertice.y, vertice.z);
    }

    /**
     *
     * @param vertice
     * @return
     */
    public boolean isVisible(QVector4 vertice) {
        return isVisible(vertice.x, vertice.y, vertice.z);
    }

    public void setRadioAspecto(int pantallaAncho, int pantallaAlto) {
        aspectRatio = (float) pantallaAncho / (float) pantallaAlto;
        updateCamera();
    }

    public void setAspectRatio(float radioAspecto) {
        this.aspectRatio = radioAspecto;
        updateCamera();
    }

    /**
     * Actualiza los valores de frustrum de la cámara de acuerdo al ángulo FOV y
     * actualiza la matriz de proyeccion
     */
    public void updateCamera() {
        // http://www.songho.ca/opengl/gl_transform.html#projection

        float height = frustrumCerca * (float) Math.tan(FOV / 2);
        float width = height * aspectRatio;
        frustumIzquierda = -width;
        frustumDerecha = width;
        frustumArriba = -height;
        frustumAbajo = height;

        // camaraAlto = 2 * frustrumCerca * (float) Math.tan(FOV / 2);
        // camaraAncho = camaraAlto * radioAspecto;
        // frustumIzquierda = -camaraAncho / 2;
        // frustumDerecha = camaraAncho / 2;
        // frustumArriba = -camaraAlto / 2;
        // frustumAbajo = camaraAlto / 2;

        if (ortogonal) {
            frustumArriba *= escalaOrtogonal;
            frustumDerecha *= escalaOrtogonal;
            frustumIzquierda *= escalaOrtogonal;
            frustumAbajo *= escalaOrtogonal;
        }

        /**
         * Matriz de proyeccion igua a la de OpenGL
         * http://www.songho.ca/opengl/gl_projectionmatrix.html
         *
         */
        matrizProyeccion.fromFrustum(frustrumCerca, frustrumLejos, frustumIzquierda, frustumDerecha, frustumArriba,
                frustumAbajo, ortogonal);

        QVector3[] esquinas = getEsquinasFrustum();
        construirPlanosRecorte(esquinas);
        // construirGeometria(esquinas);
        cached_time = -1L;
    }

    /**
     * Calcula las esquinas del tronco frutum con coordenadas en el espacio de
     * la camara (matriz de trasnformacion inversa)
     *
     * @return
     *         https://stackoverflow.com/questions/13665932/calculating-the-viewing-frustum-in-a-3d-space
     *
     */
    public QVector3[] getEsquinasFrustum() {
        QVector3[] esquinas = new QVector3[8];

        // en coordenadas de la camara
        QVector3 centro = QVector3.empty();
        QVector3 camaraDireccion = QVector3.of(0, 0, -1.0f);
        QVector3 arriba = QVector3.of(0, 1.0f, 0);
        QVector3 izquierda = QVector3.of(-1.0f, 0, 0);
        // -----------------
        QVector3 centroCerca = centro.clone().add(camaraDireccion.clone().normalize().multiply(frustrumCerca));
        QVector3 centroLejos = centro.clone().add(camaraDireccion.clone().normalize().multiply(frustrumLejos));

        float cercaAlto = 2 * (float) Math.tan(FOV / 2) * frustrumCerca;
        float cercaAncho = cercaAlto * aspectRatio;

        float lejosAlto = 2 * (float) Math.tan(FOV / 2) * frustrumLejos;
        float lejosAncho = lejosAlto * aspectRatio;

        if (ortogonal) {
            lejosAncho = cercaAncho;
            lejosAlto = cercaAlto;
        }

        // lejanas
        // superior izquierda
        esquinas[0] = centroLejos.clone().add(arriba.clone().multiply(lejosAlto * (-frustumArriba))
                .add(izquierda.clone().multiply(lejosAncho * (-frustumIzquierda))));
        // superior derecha
        esquinas[1] = centroLejos.clone().add(arriba.clone().multiply(lejosAlto * (-frustumArriba))
                .add(izquierda.clone().multiply(lejosAncho * (-frustumDerecha))));
        // inferiro izquierda
        esquinas[2] = centroLejos.clone().add(arriba.clone().multiply(-lejosAlto * (frustumAbajo))
                .add(izquierda.clone().multiply(lejosAncho * (-frustumIzquierda))));
        // inferior derecha
        esquinas[3] = centroLejos.clone().add(arriba.clone().multiply(-lejosAlto * (frustumAbajo))
                .add(izquierda.clone().multiply(lejosAncho * (-frustumDerecha))));

        // ----------cercanas
        // superior izquierda
        esquinas[4] = centroCerca.clone().add(arriba.clone().multiply(cercaAlto * (-frustumArriba))
                .add(izquierda.clone().multiply(cercaAncho * (-frustumIzquierda))));
        // superior derecha
        esquinas[5] = centroCerca.clone().add(arriba.clone().multiply(cercaAlto * (-frustumArriba))
                .add(izquierda.clone().multiply(cercaAncho * (-frustumDerecha))));
        // inferiro izquierda
        esquinas[6] = centroCerca.clone().add(arriba.clone().multiply(-cercaAlto * (frustumAbajo))
                .add(izquierda.clone().multiply(cercaAncho * (-frustumIzquierda))));
        // inferior derecha
        esquinas[7] = centroCerca.clone().add(arriba.clone().multiply(-cercaAlto * (frustumAbajo))
                .add(izquierda.clone().multiply(cercaAncho * (-frustumDerecha))));

        return esquinas;
    }

    private void construirPlanosRecorte(QVector3[] esquinas) {
        // Plano cercano
        clipPane[0] = new ClipPane(esquinas[6], esquinas[4], esquinas[5]);
        // Plano lejano
        clipPane[1] = new ClipPane(esquinas[2], esquinas[1], esquinas[0]);
        // Plano superior
        clipPane[2] = new ClipPane(esquinas[0], esquinas[5], esquinas[4]);
        // Plano inferior
        clipPane[3] = new ClipPane(esquinas[6], esquinas[7], esquinas[2]);
        // Plano derecha
        clipPane[4] = new ClipPane(esquinas[7], esquinas[5], esquinas[1]);
        // Plano izquierda
        clipPane[5] = new ClipPane(esquinas[6], esquinas[2], esquinas[4]);
    }

    /**
     * Construye una geometria para ver la camara
     */
    private void construirGeometria(QVector3[] esquinas) {
        try {
            GEOMETRIA_FRUSTUM.destroy();
            for (QVector3 vector : esquinas) {
                GEOMETRIA_FRUSTUM.addVertex(vector);
            }

            // cercano
            GEOMETRIA_FRUSTUM.addPoly(MATERIAL, IntArray.of(4, 6, 7, 5));
            // lejano
            GEOMETRIA_FRUSTUM.addPoly(MATERIAL, IntArray.of(1, 3, 2, 0));
            // superior
            GEOMETRIA_FRUSTUM.addPoly(MATERIAL, IntArray.of(0, 4, 5, 1));
            // inferior
            GEOMETRIA_FRUSTUM.addPoly(MATERIAL, IntArray.of(2, 3, 7, 6));
            // derecha
            GEOMETRIA_FRUSTUM.addPoly(MATERIAL, IntArray.of(7, 3, 1, 5));
            // izquierda
            GEOMETRIA_FRUSTUM.addPoly(MATERIAL, IntArray.of(0, 2, 6, 4));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Aplica la proyeccion, al multiplicar por la matriz de proyeccion y
     * realiza la division para la perspectiva
     *
     *
     * @param vector
     * @return vector en coordenadas normalizadas (-1:1)
     */
    public QVector3 proyectar(QVector4 vector) {
        // metodo matricial
        // pasos
        // http://www.songho.ca/opengl/gl_transform.html
        // Los datos ya vienen en coordenads del Ojo porq se multiplicaron por la
        // inversa de la transformación de la cámara
        // 1.1 Pasar a coordenadas del Clip usando la matriz de proyeccion (Usa la
        // matriz proyectar u ortogonal y realiza el clipping )
        // Eso devuelve coordenadas en el espacio homogeneo

        TempVars tmp = TempVars.get();
        try {
            // Primero proyeccion. Al proyectar la deja en coordenadas homogeneas (de-1 a 1)
            QVector4 p = getMatrizProyeccion().mult(vector);
            // 1.2 Ahora vamos a normalizar las coordendas dividiendo para el componente W
            // (Perspectiva)
            tmp.vector3f2.set(p.x / p.w, p.y / p.w, p.z / p.w); // division de perspertiva
        } finally {
            tmp.release();
        }
        return tmp.vector3f2;
    }

    /**
     * Devuelve las coordenadas de la pantalla de coordenadas ya normalizadas
     *
     * @param vector de coordenadas normalizadas
     * @return
     */
    private QVector3 coordenadasPantalla(QVector3 vector, int pantallaAncho, int pantallaAlto) {
        // http://www.songho.ca/opengl/gl_transform.html
        TempVars tmp = TempVars.get();
        try {
            tmp.vector3f1.set(
                    (vector.x * pantallaAncho + pantallaAncho) / 2,
                    (vector.y * pantallaAlto + pantallaAlto) / 2,
                    // vector.z * (frustrumLejos - frustrumCerca) / 2 + (frustrumCerca +
                    // frustrumLejos) / 2 //este valor ya no se utiliza, ya no lo calculo
                    0);
        } finally {
            tmp.release();
        }
        return tmp.vector3f1;
    }

    /**
     * Convierte las coordenadas del espacio de la Camara a coordenadas de la
     * pantalla
     *
     * @param onScreen
     * @param vector
     * @param pantallaAncho
     * @param pantallaAlto
     */
    public void getCoordenadasPantalla(QVector2 onScreen, QVector4 vector, int pantallaAncho, int pantallaAlto) {
        QVector3 tmp = coordenadasPantalla(proyectar(vector), pantallaAncho, pantallaAlto);
        if (tmp != null) {
            onScreen.x = (int) tmp.x;
            onScreen.y = (int) tmp.y;
        }
    }

    @Override
    public Camera clone() {
        Camera newCamara = new Camera();
        newCamara.setFOV(FOV);
        newCamara.setAspectRatio(aspectRatio);
        newCamara.setOrtogonal(ortogonal);
        newCamara.updateCamera();
        newCamara.setTransform(transform.clone());
        return newCamara;
    }

    /**
     * Actualiza la posicion de la camara para que apunte a un objetivo
     *
     * @param posicion
     * @param objetivo
     * @param up
     */
    public void lookAtTarget(QVector3 posicion, QVector3 objetivo, QVector3 up) {
        lookAt(posicion, posicion.clone().subtract(objetivo), up);
    }

    /**
     * Actualiza la posicion y direccion de la camara
     *
     * @param location
     * @param direction
     * @param up
     */
    public void lookAt(QVector3 location, QVector3 direction, QVector3 up) {
        transform.getLocation().set(location);
        transform.getRotation().getCuaternion().lookAt(direction, up);
        transform.getRotation().updateEuler();
        updateCamera();
    }

    public boolean isOrtogonal() {
        return ortogonal;
    }

    public void setOrtogonal(boolean ortogonal) {
        if (ortogonal != this.ortogonal) {
            this.ortogonal = ortogonal;
            updateCamera();
        }
    }

    public float getFOV() {
        return FOV;
    }

    public void setFOV(float FOV) {
        if (this.FOV != FOV) {
            this.FOV = FOV;
            updateCamera();
        }
    }

    public float getEscalaOrtogonal() {
        return escalaOrtogonal;
    }

    public void setEscalaOrtogonal(float escalaOrtogonal) {
        if (this.escalaOrtogonal != escalaOrtogonal) {
            this.escalaOrtogonal = escalaOrtogonal;
            updateCamera();
        }
    }

    @Override
    public String toString() {
        return "QCamara{" + "radioAspecto=" + aspectRatio + ", FOV=" + FOV + ", frustrumCerca=" + frustrumCerca
                + ", frustrumLejos=" + frustrumLejos + ", frustumIzquierda=" + frustumIzquierda + ", frustumDerecha="
                + frustumDerecha + ", frustumArriba=" + frustumArriba + ", frustumAbajo=" + frustumAbajo + '}';
    }

}
