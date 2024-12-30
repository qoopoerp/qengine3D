package net.qoopo.engine.core.load.md5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import net.qoopo.engine.core.animation.AnimationFrame;
import net.qoopo.engine.core.animation.AnimationPair;
import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.animation.AnimationComponent;
import net.qoopo.engine.core.entity.component.animation.Bone;
import net.qoopo.engine.core.entity.component.animation.QCompAlmacenAnimaciones;
import net.qoopo.engine.core.entity.component.animation.Skeleton;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaConvexa;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.entity.component.transform.QTransformacion;
import net.qoopo.engine.core.load.md5.util.MD5AnimModel;
import net.qoopo.engine.core.load.md5.util.MD5BaseFrame;
import net.qoopo.engine.core.load.md5.util.MD5Frame;
import net.qoopo.engine.core.load.md5.util.MD5Hierarchy;
import net.qoopo.engine.core.load.md5.util.MD5JointInfo;
import net.qoopo.engine.core.load.md5.util.MD5Mesh;
import net.qoopo.engine.core.load.md5.util.MD5Model;
import net.qoopo.engine.core.load.md5.util.MD5Utils;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.Cuaternion;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QRotacion;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QJOMLUtil;
import net.qoopo.engine.core.util.Utils;
import net.qoopo.engine.core.util.mesh.NormalUtil;

public class LoadModelMd5 implements ModelLoader {

    // Especificaciones tecnicas del formato del archivo
    // http://tfc.duke.free.fr/coding/md5-specs-en.html
    private static final String NORMAL_FILE_SUFFIX_2 = "_normal";
    private static final String NORMAL_FILE_SUFFIX = "_local";
    private static final String SPECULAR_FILE_SUFFIX = "_s";

    @Override
    public Entity loadModel(InputStream stream) {
        return null;
    }

    @Override
    public Entity loadModel(File file) throws FileNotFoundException {
        try {
            List<MD5AnimModel> animaciones = new ArrayList<>();

            MD5Model md5Meshodel = MD5Model.parse(file.getAbsolutePath());

            // animaciones
            File carpeta = file.getParentFile();
            for (File f : carpeta.listFiles()) {
                if (f.getName().toLowerCase().endsWith(".md5anim")) {
                    animaciones.add(MD5AnimModel.parse(f.getAbsolutePath()));
                }
            }

            return LoadModelMd5.load(md5Meshodel, animaciones, QColor.WHITE, file.getParentFile());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crea una entity a partir de un MD5Model. Agrega a la entity los
     * componenetes del esqueleto, Geometr[ia y componentes de colision y
     * f[isicas estatiscas para cada Malla
     *
     * @param md5Model
     * @param animModels
     * @param defaultColour
     * @param rutaTrabajo
     * @return
     * @throws Exception
     */
    private static Entity load(MD5Model md5Model, List<MD5AnimModel> animModels, QColor defaultColour,
            File rutaTrabajo) throws Exception {
        Entity entity = new Entity(md5Model.getNombre());
        Skeleton esqueleto = crearEsqueleto(md5Model);
        entity.addComponent(esqueleto);

        // geometria
        for (MD5Mesh md5Mesh : md5Model.getMeshes()) {
            Mesh geometria = generarGeometria(md5Mesh, esqueleto);
            loadTextures(geometria, md5Mesh, defaultColour, rutaTrabajo);
            entity.addComponent(geometria);
            // ****************** OBJETOS DE COLISION ********************************
            // agrega componentes fisicos para cada malla
            CollisionShape colision = new QColisionMallaConvexa(geometria);
            entity.addComponent(colision);
            QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.ESTATICO, 0);
            rigido.setFormaColision(colision);
            entity.addComponent(rigido);
        }

        // animaciones
        QCompAlmacenAnimaciones almacen = new QCompAlmacenAnimaciones();
        // agrego a la entity el almacen de animaciones
        entity.addComponent(almacen);
        almacen.agregarAnimacion("Pose_Inicial", AnimationComponent.crearAnimacionPose(esqueleto));
        if (animModels != null && !animModels.isEmpty()) {
            for (MD5AnimModel animModel : animModels) {
                AnimationComponent animacion = crearAnimacion(md5Model, animModel, esqueleto);
                almacen.agregarAnimacion(animModel.getNombre(), animacion);
            }

            // entity.agregarComponente(almacen.getAnimacion("Pose"));
            if (almacen.getAnimacion(md5Model.getNombre()) != null) {
                // le setea la animacion con el mismo nombre del archivo para que se ejecute
                entity.addComponent(almacen.getAnimacion(md5Model.getNombre()));
            } else {
                // le setea la primera animacion para q se ejecute
                entity.addComponent(almacen.getAnimacion(animModels.get(0).getNombre()));
            }

        }

        return entity;
    }

    /**
     * Crea un componenete de Esqueleto a partir de MD5Model
     *
     * @param md5Model
     * @return
     */
    private static Skeleton crearEsqueleto(MD5Model md5Model) {
        Skeleton esqueleto = new Skeleton();
        List<MD5JointInfo.MD5JointData> joints = md5Model.getJointInfo().getJoints();
        int numJoints = joints.size();
        esqueleto.setHuesos(new ArrayList<>());
        Bone hueso;
        MD5JointInfo.MD5JointData joint;
        for (int i = 0; i < numJoints; i++) {
            joint = joints.get(i);
            hueso = new Bone(i, joint.getName());
            hueso.setTransformacion(new QTransformacion(QRotacion.CUATERNION));
            hueso.getTransformacion().getTraslacion().set(QJOMLUtil.convertirQVector3(joint.getPosition()));
            hueso.getTransformacion().getRotacion().setCuaternion(joint.getOrientation().clone());
            // la inversa de la trasnformacion, la calculamos manualmente sin tomar en
            // cuenta la jerarquia
            hueso.transformacionInversa = hueso.getTransformacion().toTransformMatriz().invert();
            if (joint.getParentIndex() > -1) {
                esqueleto.getBone(joint.getParentIndex()).addChild(hueso);
            }
            esqueleto.agregarHueso(hueso);
        }
        // esqueleto.calcularMatricesInversas();
        return esqueleto;
    }

    /**
     * Construye un componente de Geometria a partir de un MD5Model que contiene
     * la informaci[on de la malla
     *
     * @param md5Model
     * @param md5Mesh
     * @param esqueleto
     * @return
     * @throws Exception
     */
    private static Mesh generarGeometria(MD5Mesh md5Mesh, Skeleton esqueleto) throws Exception {
        List<MD5Mesh.MD5Vertex> vertices = md5Mesh.getVertices();
        List<MD5Mesh.MD5Weight> weights = md5Mesh.getWeights();

        Mesh geometria = new Mesh();

        for (MD5Mesh.MD5Vertex vertex : vertices) {
            QVector3 posicionVertice = QVector3.empty();
            int pesoInicial = vertex.getStartWeight();
            int numeroPesos = vertex.getWeightCount();
            Bone[] huesos = new Bone[numeroPesos];
            // QVector3[] pesosPosiciones = new QVector3[numeroPesos];
            float[] pesos = new float[numeroPesos];
            int[] huesosIds = new int[numeroPesos];
            int c = 0;

            // -------------------------------
            /**
             * La posición del vértice se calcula usando todos los pesos
             * relacionados. Cada peso tiene una posición y un sesgo. La suma de
             * todos los sesgos de los pesos asociados a cada vértice debe ser
             * igual a 1.0. Cada peso también tiene una posición que se define
             * en el espacio local de la articulación, por lo que debemos
             * transformarlo en coordenadas de espacio modelo utilizando la
             * orientación y la posición de la articulación (como si se tratara
             * de una matriz de transformación) a la que se refiere.
             */
            for (int indicePeso = pesoInicial; indicePeso < pesoInicial + numeroPesos; indicePeso++) {
                MD5Mesh.MD5Weight weight = weights.get(indicePeso);
                huesos[c] = esqueleto.getBone(weight.getJointIndex());
                huesosIds[c] = weight.getJointIndex();
                pesos[c] = weight.getBias();
                // pesosPosiciones[c] = QJOMLUtil.convertirQVector3(weight.getPosition());

                // ------------------------------------------------------------------------------
                // de acuerdo a lo q entendi en el documento de donde tome este codigo para
                // cargar modelos MD5
                // https://lwjglgamedev.gitbooks.io/3d-game-development-with-lwjgl/content/chapter19/chapter19.html
                // toma la transformacion del hueso que ha calculado antes en la creacion del
                // hueso
                // usa la transfomación sin tomar en cuenta a los padres porq el peso tiene la
                // posición en el espacio de coordenadas de la articulación
                posicionVertice.add(huesos[c].getTransformacion().toTransformMatriz()
                        .mult(QJOMLUtil.convertirQVector3(weight.getPosition())).multiply(weight.getBias()));
                c++;
            }
            Vertex vertice = geometria.addVertex(posicionVertice.x, posicionVertice.y, posicionVertice.z,
                    vertex.getTextCoords().x, 1.0f - vertex.getTextCoords().y);
            vertice.setListaHuesos(huesos);
            vertice.setListaHuesosPesos(pesos);
            vertice.setListaHuesosIds(huesosIds);
            // vertice.setListaPosicionPesos(pesosPosiciones);
        }

        for (MD5Mesh.MD5Triangle tri : md5Mesh.getTriangles()) {
            geometria.addPoly(tri.getVertex0(), tri.getVertex2(), tri.getVertex1());
        }

        geometria = NormalUtil.calcularNormales(geometria);
        geometria = MaterialUtil.smooth(geometria, true);
        // geometria = QUtilNormales.invertirNormales(geometria);
        return geometria;
    }

    private static String getTexturePath(String texturePath, File rutaTrabajo) {
        if (texturePath == null) {
            return null;
        }
        // si es ruta completa
        if (new File(texturePath).exists()) {
            // todo ok
        } else {
            // compruebo si es una ruta relativa
            if (new File(rutaTrabajo, texturePath).exists()) {
                texturePath = new File(rutaTrabajo, texturePath).getAbsolutePath();
            }
        }

        // si no existe puede que no esté indicado una extensión, pruebo agregando una
        // extension
        if (!(new File(texturePath).exists())) {
            if (!texturePath.contains(".")) {
                String validPath = texturePath;
                String[] options = { ".png", "_d.png", ".jpg", "_d.jpg", ".tga", "_d.tga" };
                for (String option : options) {
                    validPath = getTexturePath(texturePath + option, rutaTrabajo);
                    if (new File(validPath).exists())
                        break;
                }
                texturePath = validPath;
                // texturePath = getTexturePath(texturePath + ".tga", rutaTrabajo);
            }
        }

        return texturePath;
    }

    private static void loadTextures(Mesh mesh, MD5Mesh md5Mesh, QColor defaultColour, File rutaTrabajo)
            throws Exception {

        String texturePath = getTexturePath(md5Mesh.getTexture(), rutaTrabajo);
        if (texturePath == null) {
            return;
        }

        try {
            if (texturePath != null && texturePath.length() > 0) {
                if (new File(texturePath).exists()) {
                    QTextura textura = AssetManager.get().loadTexture(texturePath, texturePath);
                    // QProcesadorTextura textura = new QProcesadorSimple(text);
                    QMaterialBas material = new QMaterialBas(textura);

                    // Handle other Maps;
                    int pos = texturePath.lastIndexOf(".");
                    if (pos > 0) {
                        String basePath = texturePath.substring(0, pos);
                        String extension = texturePath.substring(pos, texturePath.length());

                        // normal
                        String normalMapFileName = basePath + NORMAL_FILE_SUFFIX + extension;
                        // System.out.println("el archivo de normal deberia ser:" + normalMapFileName);
                        if (Utils.existsResourceFile(normalMapFileName)) {
                            QTextura normalMap = AssetManager.get().loadTexture(normalMapFileName, normalMapFileName);
                            material.setMapaNormal(normalMap);
                        } else {
                            normalMapFileName = basePath + NORMAL_FILE_SUFFIX_2 + extension;
                            // System.out.println("el archivo de normal deberia ser:" + normalMapFileName);
                            if (Utils.existsResourceFile(normalMapFileName)) {
                                QTextura normalMap = AssetManager.get().loadTexture(normalMapFileName, normalMapFileName);
                                material.setMapaNormal(normalMap);
                            }
                        }
                        String specularMapFileName = basePath + SPECULAR_FILE_SUFFIX + extension;
                        // System.out.println("el archivo de normal deberia ser:" + normalMapFileName);
                        if (Utils.existsResourceFile(specularMapFileName)) {
                            QTextura specularMap = AssetManager.get().loadTexture(specularMapFileName,
                                    specularMapFileName);
                            material.setMapaEspecular(specularMap);
                        }
                    }
                    MaterialUtil.applyMaterial(mesh, material);

                } else {
                    // mesh.setMaterial(new Material(defaultColour, 1));
                    MaterialUtil.applyColor(mesh, 1, defaultColour, QColor.WHITE, 0, 64);
                }
            }
        } catch (Exception e) {

        }
    }

    private static AnimationComponent crearAnimacion(MD5Model md5Model, MD5AnimModel animModel,
            Skeleton esqueleto) {

        float duracionFrames = 1.0f / animModel.getHeader().getFrameRate();
        // float duracionAnimacion = duracionFrames *
        // animModel.getHeader().getNumFrames();
        float duracionAnimacion = (float) animModel.getHeader().getNumFrames()
                / (float) animModel.getHeader().getFrameRate();

        // calcula la duracion de cada frame de acuerdo a la duracion de la animacion /
        // el numero de frames
        // duracionFrames = duracionAnimacion / animModel.getHeader().getNumFrames();
        // System.out.println("Animación:" + animModel.getNombre());
        // System.out.println("FrameRate=" + animModel.getHeader().getFrameRate());
        // System.out.println("Duracion de la animacion=" + duracionAnimacion);
        // QComponenteAnimacion animacion = new QComponenteAnimacion();
        AnimationComponent animacion = new AnimationComponent(duracionAnimacion);
        animacion.setNombre(animModel.getNombre());
        animacion.setLoop(true);
        // animacion.setLoop(false);

        List<MD5Frame> frames = animModel.getFrames();

        float segundos = 0.0f;
        for (MD5Frame md5Frame : frames) {
            AnimationFrame frame = processAnimationFrame(md5Model, animModel, md5Frame, esqueleto, segundos);
            segundos += duracionFrames;
            animacion.addFrame(frame);
        }
        return animacion;
    }

    /**
     *
     * @param md5Model
     * @param animModel
     * @param frame
     * @param esqueleto
     * @param tiempo
     * @return
     */
    private static AnimationFrame processAnimationFrame(MD5Model md5Model, MD5AnimModel animModel, MD5Frame frame,
            Skeleton esqueleto, float tiempo) {
        // crea un frame para el tipo de animacion por frames
        // QAnimacionFrame qFrame = new QAnimacionFrame(QAnimacionFrame.TIPO_FINITA, 1);
        AnimationFrame qFrame = new AnimationFrame(tiempo);

        MD5BaseFrame baseFrame = animModel.getBaseFrame();
        List<MD5Hierarchy.MD5HierarchyData> hierarchyList = animModel.getHierarchy().getHierarchyDataList();
        List<MD5JointInfo.MD5JointData> joints = md5Model.getJointInfo().getJoints();
        int numJoints = joints.size();
        float[] frameData = frame.getFrameData();

        Entity hueso;
        QTransformacion transformacion;
        for (int i = 0; i < numJoints; i++) {
            hueso = esqueleto.getBone(i);
            MD5BaseFrame.MD5BaseFrameData baseFrameData = baseFrame.getFrameDataList().get(i);
            Vector3f position = baseFrameData.getPosition();
            Cuaternion orientation = baseFrameData.getOrientation().clone();

            int flags = hierarchyList.get(i).getFlags();
            int startIndex = hierarchyList.get(i).getStartIndex();

            if ((flags & 1) > 0) {
                position.x = frameData[startIndex++];
            }
            if ((flags & 2) > 0) {
                position.y = frameData[startIndex++];
            }
            if ((flags & 4) > 0) {
                position.z = frameData[startIndex++];
            }
            if ((flags & 8) > 0) {
                orientation.x = frameData[startIndex++];
            }
            if ((flags & 16) > 0) {
                orientation.y = frameData[startIndex++];
            }
            if ((flags & 32) > 0) {
                orientation.z = frameData[startIndex++];
            }
            // Update Quaternion's w component
            orientation = MD5Utils.calculateQuaternion(new Vector3f(orientation.x, orientation.y, orientation.z));
            transformacion = new QTransformacion(QRotacion.CUATERNION);
            transformacion.getTraslacion().set(position.x, position.y, position.z);
            transformacion.getRotacion().setCuaternion(orientation.clone());

            // para probar, la multiplico por la inversa de la transformacion del hueso
            // QMatriz4 mat = transformacion.toTransformMatriz();
            // QMatriz4 matInversa = hueso.transformacion.toTransformMatriz();
            // matInversa = matInversa.invert();
            // mat.multLocal(matInversa);
            // transformacion.desdeMatrix(mat);
            qFrame.agregarPar(new AnimationPair(hueso, transformacion));
        }
        return qFrame;
    }

}
