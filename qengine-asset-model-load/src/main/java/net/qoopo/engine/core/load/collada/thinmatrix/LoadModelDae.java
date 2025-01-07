/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.load.collada.thinmatrix;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.animation.AnimationFrame;
import net.qoopo.engine.core.animation.AnimationPair;
import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.animation.AnimationComponent;
import net.qoopo.engine.core.entity.component.animation.Bone;
import net.qoopo.engine.core.entity.component.animation.AnimationStorageComponent;
import net.qoopo.engine.core.entity.component.animation.Skeleton;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.transform.QTransformacion;
import net.qoopo.engine.core.load.collada.thinmatrix.data.AnimatedModelData;
import net.qoopo.engine.core.load.collada.thinmatrix.data.AnimationData;
import net.qoopo.engine.core.load.collada.thinmatrix.data.JointData;
import net.qoopo.engine.core.load.collada.thinmatrix.data.JointTransformData;
import net.qoopo.engine.core.load.collada.thinmatrix.data.KeyFrameData;
import net.qoopo.engine.core.load.collada.thinmatrix.data.MeshData;
import net.qoopo.engine.core.load.collada.thinmatrix.data.SkeletonData;
import net.qoopo.engine.core.load.collada.thinmatrix.loader.ColladaLoader;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.util.QJOMLUtil;

/**
 * Carga un modelo según el algoritmo de ThinMatrix para archivos Collada en
 * texto
 * 
 * @author alberto
 */
public class LoadModelDae implements ModelLoader {

    public LoadModelDae() {
    }

    public Entity loadModel(InputStream stream) {
        return null;
    }

    public Entity loadModel(File file) {
        try {
            Entity entity = new Entity();
            AnimatedModelData modelo = ColladaLoader.loadColladaModel(file, 10);

            Skeleton esqueleto = crearEsqueleto(modelo.getJointsData());
            entity.addComponent(esqueleto);
            // Animacion
            AnimationData dataAnimacion = ColladaLoader.loadColladaAnimation(file);
            AnimationComponent animacionPose = AnimationComponent.crearAnimacionPose(esqueleto);
            AnimationComponent animacion = loadAnimation(dataAnimacion, esqueleto);
            AnimationStorageComponent almacen = new AnimationStorageComponent();
            almacen.add("animation", animacion);
            almacen.add("Pose", animacionPose);
            entity.addComponent(almacen);
            entity.addComponent(animacion);
            // Geometria
            entity.addComponent(loadMesh(modelo.getMeshData(), esqueleto));
            return entity;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    private static AnimationComponent loadAnimation(AnimationData dataAnimacion, Skeleton esqueleto) {
        AnimationComponent animacion = new AnimationComponent(dataAnimacion.lengthSeconds);
        animacion.setNombre("animation");
        animacion.setLoop(true);
        for (KeyFrameData frameData : dataAnimacion.keyFrames) {
            AnimationFrame frame = loadAnimationFrame(frameData, esqueleto);
            animacion.addFrame(frame);
        }
        return animacion;
    }

    /**
     * Crea un fram de animation a partir del keyFrame Data
     *
     * @param frameData
     * @param esqueleto
     * @return
     */
    private static AnimationFrame loadAnimationFrame(KeyFrameData frameData, Skeleton esqueleto) {
        AnimationFrame qFrame = new AnimationFrame(frameData.time);
        Bone hueso;
        QTransformacion transformacion;
        for (JointTransformData joinTransFormData : frameData.jointTransforms) {
            hueso = esqueleto.getBone(joinTransFormData.jointNameId);
            if (hueso != null) {
                QMatriz4 mat4 = QJOMLUtil.convertirQMatriz4(joinTransFormData.jointLocalTransform);
                transformacion = new QTransformacion();
                transformacion.desdeMatrix(mat4);
                qFrame.agregarPar(new AnimationPair(hueso, transformacion));
            } else {
                System.out.println("No se encontro el hueso " + joinTransFormData.jointNameId + " para la animation");
            }
        }
        return qFrame;
    }

    /**
     * Crea un Hueso a partir de la información de joint
     *
     * @param joint
     * @return
     */
    private static Bone crearHueso(JointData joint) {
        Bone hueso = new Bone(joint.index, joint.nameId);
        QMatriz4 mat4 = QJOMLUtil.convertirQMatriz4(joint.bindLocalTransform);
        // hueso.transformacion = new QTransformacion(QRotacion.CUATERNION);
        hueso.setTransformacion(new QTransformacion());
        hueso.getTransformacion().desdeMatrix(mat4);
        return hueso;
    }

    private static void procesarHuesoHijos(JointData joint, List<Bone> lista, Bone padre) {
        for (JointData jointItem : joint.children) {
            Bone hueso = crearHueso(jointItem);
            padre.addChild(hueso);
            lista.add(hueso);
            procesarHuesoHijos(jointItem, lista, hueso);
        }
    }

    private static Skeleton crearEsqueleto(SkeletonData data) {
        Skeleton esqueleto = new Skeleton();
        JointData joinData = data.headJoint;
        // int numJoints = data.jointCount;
        List<Bone> lista = new ArrayList<>();
        Bone hueso = crearHueso(joinData);
        lista.add(hueso);
        procesarHuesoHijos(joinData, lista, hueso);

        // System.out.println("ESQUELETO CARGADO");
        // System.out.println("=======================");
        // Entity.imprimirArbolEntidad(hueso, "");
        esqueleto.setHuesos(lista);
        esqueleto.calcularMatricesInversas();
        return esqueleto;
    }

    public static Mesh loadMesh(MeshData data, Skeleton esqueleto) {
        Material material = new Material("default");
        Mesh mesh = new Mesh();

        int verticesReales = data.getVertexCount();
        int vertices = data.getVertices().length;

        // int caras = data.getIndices().length;
        int huesosTotal = data.getJointIds().length;
        int huesosXVertice = huesosTotal / verticesReales;

        // agrega los vertices, de 3 en tres por q son triangulos
        int indiceTextura = 0;
        int indiceHueso = 0;

        for (int indiceVertice = 0; indiceVertice < vertices; indiceVertice += 3) {
            Bone[] huesos = new Bone[huesosXVertice];
            float[] pesos = new float[huesosXVertice];
            int[] huesosIds = new int[huesosXVertice];
            Vertex vertice = mesh.addVertex(data.getVertices()[indiceVertice],
                    data.getVertices()[indiceVertice + 1], data.getVertices()[indiceVertice + 2]);
            mesh.addUV(data.getTextureCoords()[indiceTextura * 2], data.getTextureCoords()[indiceTextura * 2 + 1]);
            // geometria.addNormal(data.getNormals()[indice], indiceHueso, indiceVertice)
            if (esqueleto != null) {
                for (int j = 0; j < huesosXVertice; j++) {
                    huesos[j] = esqueleto.getBone(data.getJointIds()[indiceHueso + j]);
                    huesosIds[j] = data.getJointIds()[indiceHueso + j];
                    pesos[j] = data.getVertexWeights()[indiceHueso + j];
                }
                vertice.setListaHuesos(huesos);
                vertice.setListaHuesosIds(huesosIds);
                vertice.setListaHuesosPesos(pesos);
            }
            indiceHueso += huesosXVertice;
            indiceTextura++;
            // vertice.setListaPosicionPesos(pesosPosiciones);
        }

        // agrega los polígonos
        int i1, i2, i3;
        // DE 3 EN TRES PARA HACER TRIANGULOS
        for (int i = 0; i < data.getIndices().length; i += 3) {
            i1 = data.getIndices()[i];// primer vertice
            i2 = data.getIndices()[i + 1]; // segundo vertice
            i3 = data.getIndices()[i + 2]; // tercer vertice
            try {
                mesh.addPoly(material, new int[] { i1, i2, i3 }, new int[] { i1, i2, i3 },
                        new int[] { i1, i2, i3 });
            } catch (Exception ex) {
                Logger.getLogger(LoadModelDae.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        mesh.computeNormals();
        // QUtilNormales.invertirNormales(objeto);
        // QMaterialUtil.suavizar(geometria, true);
        return mesh;
    }

}
