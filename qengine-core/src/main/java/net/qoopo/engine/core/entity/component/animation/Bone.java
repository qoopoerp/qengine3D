/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.animation;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.math.QMatriz4;

/**
 * Esta entity representa un hueso para la animación Esquelética. NO debe tener
 * componentes
 *
 * @author alberto
 */
public class Bone extends Entity {

    public int indice = -1;

    // inversa de la transformacion Pose, es para volver el vertice a su estado
    // normal y aplicar la transformacion de la animacion
    public QMatriz4 transformacionInversa = new QMatriz4();
    // private long cached_time_inversa = 0;
    // private QMatriz4 cachedMatrizInversa;
    protected QMatriz4 cachedMatrizConInversa = new QMatriz4();
    private long cached_time_inversa = 0;

    public Bone() {
        agregarGeometria();
    }

    public Bone(String name) {
        super(name, false);
        agregarGeometria();
    }

    public Bone(int indice, String name) {
        super(name, false);
        this.indice = indice;
        agregarGeometria();
    }

    public Bone(int indice) {
        this.indice = indice;
        agregarGeometria();
    }

    /**
     * Esta geometria es para pruebas
     */
    private void agregarGeometria() {

        // LoadModel loadModel = new LoadModelObj();
        // QEntity ent =
        // loadModel.loadModel(QGizmoTraslacion.class.getResourceAsStream("/models/bone/bone.obj"));
        // for (QEntity child : ent.getChilds()) {
        // addComponent(QUtilComponentes.getMesh(child));
        // }

        addComponent(new Box(1, 1, 1));
        addComponent(new Box(0.1f, 0.1f, 0.1f));
        addComponent(new Box(0.5f, 0.1f, 0.1f));

        // public static final Mesh GEOMETRIA_BONE_CUERPO = QUtilComponentes.getMesh(
        // CargaWaveObject.cargarWaveObject(QBone.class.getResourceAsStream("/modelos/bone/bone.obj")).get(0));
        // public static final Mesh GEOMETRIA_BONE_B1 = QUtilComponentes.getMesh(
        // CargaWaveObject.cargarWaveObject(QBone.class.getResourceAsStream("/modelos/bone/bone.obj")).get(1));
        // public static final Mesh GEOMETRIA_BONE_B2 = QUtilComponentes.getMesh(
        // CargaWaveObject.cargarWaveObject(QBone.class.getResourceAsStream("/modelos/bone/bone.obj")).get(2));
        // addComponent(GEOMETRIA_BONE_CUERPO);
        // addComponent(GEOMETRIA_BONE_B1);
        // addComponent(GEOMETRIA_BONE_B2);

    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public QMatriz4 getTransformacionInversa() {
        return transformacionInversa;
    }

    public void setTransformacionInversa(QMatriz4 transformacionInversa) {
        this.transformacionInversa = transformacionInversa;
    }

    /**
     * Realiza el calculo de la inversa de la transformacion de este hueso
     *
     * @param parentBindTransform
     */
    public void calcularTransformacionInversa(QMatriz4 parentBindTransform) {
        try {
            QMatriz4 mat = parentBindTransform.mult(transformacion.toTransformMatriz());
            try {
                transformacionInversa = mat.invert();
            } catch (Exception e) {
                transformacionInversa = new QMatriz4();
            }
            for (Entity hijo : this.getChilds()) {
                if (hijo instanceof Bone) {
                    ((Bone) hijo).calcularTransformacionInversa(mat);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Matrix4f bindTransform = Matrix4f.mul(parentBindTransform,
        // localBindTransform, null);
        // Matrix4f.invert(bindTransform, inverseBindTransform);
        // for (Joint child : children) {
        // child.calcularTransformacionInversa(bindTransform);
        // }
    }

    public Bone clone() {
        Bone nuevo = new Bone(indice, name);
        nuevo.transformacion = this.transformacion.clone();
        nuevo.transformacionInversa = this.transformacionInversa.clone();
        // hijos
        if (this.getChilds() != null && !this.getChilds().isEmpty()) {
            for (Entity hijo : this.getChilds()) {
                nuevo.addChild(((Bone) hijo).clone());
            }
        }

        return nuevo;
    }

    /**
     * Metodo sobrecargado para agregar a la multiplicacion la matriz inversa y
     * almacenarla en el cache
     *
     * @param time
     * @return
     */
    // @Override
    public QMatriz4 getMatrizTransformacionHueso(long time) {
        try {
            if (time != cached_time_inversa || cachedMatrizConInversa == null) {
                cached_time_inversa = time;
                cachedMatrizConInversa = transformacion.toTransformMatriz();
                if (super.getParent() != null) {
                    // toma la transformacion anidada de los padres y multiplicamos por la
                    // transformacion local
                    cachedMatrizConInversa = super.getParent().getMatrizTransformacion(time)
                            .mult(cachedMatrizConInversa);
                }
                // multiplica por la inversa para quitar la transformacion original del vertice
                // y aplicar la de la animacion
                cachedMatrizConInversa = cachedMatrizConInversa.mult(transformacionInversa);
            }
        } catch (Exception e) {
            System.out.println("Error en la entity " + name);
            e.printStackTrace();
        }
        return cachedMatrizConInversa;
    }
}
