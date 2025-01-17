package net.qoopo.engine.core.lwjgl.asset;

import static org.lwjgl.assimp.Assimp.aiImportFile;
import static org.lwjgl.assimp.Assimp.aiTextureType_NONE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AICamera;
import org.lwjgl.assimp.AIColor3D;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AILight;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIQuatKey;
import org.lwjgl.assimp.AIQuaternion;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIString;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVectorKey;
import org.lwjgl.assimp.AIVertexWeight;
import org.lwjgl.assimp.Assimp;

import net.qoopo.engine.core.animation.AnimationFrame;
import net.qoopo.engine.core.animation.AnimationPair;
import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.animation.AnimationComponent;
import net.qoopo.engine.core.entity.component.animation.AnimationStorageComponent;
import net.qoopo.engine.core.entity.component.animation.Bone;
import net.qoopo.engine.core.entity.component.animation.Skeleton;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaConvexa;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.entity.component.transform.Transform;
import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.Cuaternion;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.Matrix4;
import net.qoopo.engine.core.math.Rotation;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.procesador.InvertTexture;

/**
 * Permite la carga de vaiors formatos de modelos 3D usando la librer[ia ASSIMP.
 *
 * @author alberto
 */
public class AssimpModelLoader implements ModelLoader {

    private static Logger logger = Logger.getLogger("Assimp-model-loader");

    @Override
    public Entity loadModel(InputStream stream) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadModel'");
    }

    /**
     * Carga los items dentro del archivo file
     *
     * @param file
     * @return
     * @throws Exception
     */
    public Entity loadModel(File file) throws FileNotFoundException {
        List<Entity> lista = new ArrayList<>();
        try {
            lista = loadAssimpItems(file.getAbsolutePath(), file.getParent());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (lista != null && !lista.isEmpty()) {
            if (lista.size() == 1) {
                return lista.get(0);
            } else {
                Entity entity = new Entity("output");
                lista.forEach(child -> entity.addChild(child));
                return entity;
            }
        }
        return null;
    }

    /**
     * Carga los items dentro del archivo resourcePath
     *
     * @param resourcePath
     * @param texturesDir
     * @return
     * @throws Exception
     */
    private List<Entity> loadAssimpItems(String resourcePath, String texturesDir) throws Exception {
        // return AssimpLoader.cargarAssimpItems(resourcePath, texturesDir,
        // Assimp.aiProcess_GenSmoothNormals | Assimp.aiProcess_JoinIdenticalVertices |
        // Assimp.aiProcess_Triangulate | Assimp.aiProcess_FixInfacingNormals |
        // Assimp.aiProcess_LimitBoneWeights);
        return loadAssimpItems(resourcePath, texturesDir, Assimp.aiProcess_GenSmoothNormals
                | Assimp.aiProcess_JoinIdenticalVertices | Assimp.aiProcess_Triangulate);
    }

    /**
     * Carga un archivo de modelos 3d y obtiene las entidades dentro del archivo
     *
     * @param rutaArchivo
     * @param texturesDir
     * @param flags
     * @return
     * @throws Exception
     */
    private List<Entity> loadAssimpItems(String rutaArchivo, String texturesDir, int flags) throws Exception {
        logger.info("[+] Cargando modelo con ASSIMP [ " + rutaArchivo + " ]");

        File f = new File(rutaArchivo);
        if (!f.exists()) {
            logger.severe(" [-] Archivo no existe");
        }

        AIScene aiScene = aiImportFile(rutaArchivo, flags);

        if (aiScene == null) {
            throw new Exception("Error al cargar escena");
        }

        logger.info("  >  Mallas:" + aiScene.mNumMeshes());
        logger.info("  >  Animaciones:" + aiScene.mNumAnimations());
        logger.info("  >  Camaras:" + aiScene.mNumCameras());
        logger.info("  >  Luces:" + aiScene.mNumLights());
        logger.info("  >  Materiales:" + aiScene.mNumMaterials());
        logger.info("  >  Texturas:" + aiScene.mNumTextures());

        List<Entity> entidades = new ArrayList<>();
        AINode aiNodoRaiz = aiScene.mRootNode();
        Node nodoRaiz = procesarJerarquiaNodos(aiNodoRaiz, null);
        int numMaterials = aiScene.mNumMaterials();
        PointerBuffer aiMaterials = aiScene.mMaterials();
        Map<Integer, AbstractMaterial> mapaMateriales = new HashMap<>();
        Integer indice = 0;
        for (int i = 0; i < numMaterials; i++) {
            // indice = aiMaterials.get(i);
            indice = i;
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            mapaMateriales.put(indice, loadMaterial(aiMaterial, texturesDir));
        }

        entidades.add(procesarNodo(aiScene, aiNodoRaiz, nodoRaiz, mapaMateriales));

        logger.info("[+] Modelo cargado [ " + rutaArchivo + " ]");
        return entidades;
    }

    /**
     * Crea una estructura de nodos simple basada en la estructura de nodos de
     * la escena. Se usa para encontrar los nodos de la escena y almacenar sus
     * transformaciones y relacionar con sus padres e hijos
     *
     * @param aiNode
     * @param parentNode
     * @return
     */
    private Node procesarJerarquiaNodos(AINode aiNode, Node parentNode) {
        String nodeName = aiNode.mName().dataString();
        Node node = new Node(nodeName, parentNode, aiNode);

        int numChildren = aiNode.mNumChildren();
        PointerBuffer aiChildren = aiNode.mChildren();
        for (int i = 0; i < numChildren; i++) {
            AINode aiChildNode = AINode.create(aiChildren.get(i));
            Node childNode = procesarJerarquiaNodos(aiChildNode, node);
            node.addChild(childNode);
        }

        return node;
    }

    /**
     * Crea un material desde un material de Assimp
     *
     * @param aiMaterial
     *
     * @param texturesDir
     * @return
     * @throws Exception
     */
    protected AbstractMaterial loadMaterial(AIMaterial aiMaterial, String texturesDir) throws Exception {
        AIColor4D colour = AIColor4D.create();

        Material material = new Material();

        int result = 0;
        QColor diffuse = QColor.WHITE;
        result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, colour);
        if (result == 0) {
            diffuse = new QColor(colour.a(), colour.r(), colour.g(), colour.b());
            material.setColor(diffuse);
        }

        QColor specular = QColor.WHITE;
        result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, colour);
        if (result == 0) {
            specular = new QColor(colour.a(), colour.r(), colour.g(), colour.b());
            material.setSpecularColour(specular);
        }

        QColor emissiveColour = QColor.WHITE;
        result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_EMISSIVE, aiTextureType_NONE, 0, colour);
        if (result == 0) {
            emissiveColour = new QColor(colour.a(), colour.r(), colour.g(), colour.b());
            material.setEmissionIntensity(emissiveColour.r);
        }

        QColor alphaColour = QColor.WHITE;
        result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_TRANSPARENT, aiTextureType_NONE, 0,
                colour);
        if (result == 0) {
            alphaColour = new QColor(colour.a(), colour.r(), colour.g(), colour.b());
            material.setTransAlfa(alphaColour.r);
            material.setAlphaColour(alphaColour);
        }

        // QColor ambient = QColor.WHITE;
        // result = Assimp.aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT,
        // aiTextureType_NONE, 0, colour);
        // if (result == 0) {
        // ambient = new QColor(colour.a(), colour.r(), colour.g(), colour.b());
        // }

        // Mapas (Texturas)

        Texture texture = null;

        // textura difusa
        texture = loadTexture(aiMaterial, Assimp.aiTextureType_DIFFUSE, texturesDir);
        material.setColorMap(texture);
        // textura normal
        texture = loadTexture(aiMaterial, Assimp.aiTextureType_NORMALS, texturesDir);
        material.setNormalMap(texture);
        // textura emisiva/lightmap
        texture = loadTexture(aiMaterial, Assimp.aiTextureType_EMISSIVE, texturesDir);
        if (texture == null) {
            texture = loadTexture(aiMaterial, Assimp.aiTextureType_LIGHTMAP, texturesDir);
        }
        material.setEmissiveMap(texture);

        // textura transparencia
        texture = loadTexture(aiMaterial, Assimp.aiTextureType_OPACITY, texturesDir);
        if (texture != null) {
            material.setAlphaMap(texture);
            material.setTransparent(true);
        }
        // textura especular
        texture = loadTexture(aiMaterial, Assimp.aiTextureType_SPECULAR, texturesDir);
        material.setMapaEspecular(texture);
        // textura metalness
        texture = loadTexture(aiMaterial, Assimp.aiTextureType_METALNESS, texturesDir);
        if (texture == null)
            texture = loadTexture(aiMaterial, Assimp.aiTextureType_REFLECTION,
                    texturesDir);
        material.setMetallicMap(texture);

        // textura de brillo (lo transofrma a rugosidad invirtiendolo)
        texture = loadTexture(aiMaterial, Assimp.aiTextureType_SHININESS, texturesDir);
        if (texture != null) {
            // el inverso de la textura de brillo
            material.setRoughnessMap(new InvertTexture(texture));
        }

        // // textura de entorno
        // texture = loadTexture(aiMaterial, Assimp.aiTextureType_REFLECTION,
        // texturesDir);
        // material.setEnvMap(texture);

        // textura AO
        texture = loadTexture(aiMaterial, Assimp.aiTextureType_AMBIENT_OCCLUSION, texturesDir);
        material.setAoMap(texture);

        return material;
    }

    /**
     * Procesa cada nodo de la jerarquia de nodos de la escena. Por cada nodo
     * crea un esqueleto. Si en la escena hay un solo esqueleto y es compartido
     * por varios nodos de la escena, se creara un esqueleto para cada nodo
     * 
     * @param aiScene
     * @param aiNodo
     * @param nodoRaiz
     * @param aiMeshes
     * @param aiCameras
     * @param aiLights
     * @param aiAnimations
     * @param aiTextures
     * @param mapaMateriales
     * @return
     */
    private Entity procesarNodo(AIScene aiScene, AINode aiNodo, Node nodoRaiz,
            Map<Integer, AbstractMaterial> mapaMateriales) {

        String nombre = aiNodo.mName().dataString();
        logger.info("Procesando Nodo:" + nombre);
        Entity entity = null;

        // ***************************************************************
        // ***** CAMARAS
        // ***************************************************************
        {
            int numCamaras = aiScene.mNumCameras();
            PointerBuffer aiCamaras = aiScene.mCameras();
            for (int i = 0; i < numCamaras; i++) {
                AICamera aiCamera = AICamera.create(aiCamaras.get(i));
                if (nombre.contains(aiCamera.mName().dataString())) {
                    // crea el componente camara
                    logger.info("Procesando cámara: " + aiCamera.mName().dataString());
                    entity = new Camera(nombre);
                    Camera camara = (Camera) entity;
                    camara.setFOV(aiCamera.mHorizontalFOV());
                    camara.frustrumCerca = aiCamera.mClipPlaneNear();
                    camara.frustrumLejos = aiCamera.mClipPlaneFar();
                    camara.setAspectRatio(aiCamera.mAspect());
                }
            }
        }
        // si no es una camara lo crea como una entity Generica
        if (entity == null) {
            entity = new Entity(nombre);
        }

        // Transformacion
        entity.setTransform(new Transform(Rotation.CUATERNION));
        entity.getTransform().fromMatrix(AssimpModelLoader.toMatrix(aiNodo.mTransformation()));

        Skeleton esqueleto = null;

        // ***************************************************************
        // ***** MALLAS, ESQUELETO Y ANIMACIONES
        // ***************************************************************
        PointerBuffer aiMeshes = aiScene.mMeshes();

        List<AssimpBone> boneList = new ArrayList<>();
        int numMallas = aiNodo.mNumMeshes();
        IntBuffer aiMeshesLocal = aiNodo.mMeshes();
        if (aiMeshesLocal != null) {
            // ****************** MALLAS ********************************
            for (int i = 0; i < numMallas; i++) {
                AIMesh aiMesh = AIMesh.create(aiMeshes.get(aiMeshesLocal.get(i)));
                // ****************** MALLA ********************************
                Mesh malla = loadMesh(aiMesh, mapaMateriales, boneList);
                entity.addComponent(malla);
                // ****************** OBJETOS DE COLISION ********************************
                // agrega componentes fisicos para cada malla
                CollisionShape colision = new QColisionMallaConvexa(malla);
                entity.addComponent(colision);
                QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.ESTATICO, 0);
                rigido.setFormaColision(colision);
                entity.addComponent(rigido);
            }
        }
        // ****************** ESQUELETO ********************************
        if (!boneList.isEmpty()) {
            // logger.info("Lista de huesos encontrados " + boneList.size());
            esqueleto = loadSkeleton(boneList, nodoRaiz);
            entity.addComponent(esqueleto);

            // como ya tengo el esqueleto, recorro los vertices para apuntar los huesos de
            // cada vertice (actualmente solo tengo los ids)
            for (EntityComponent componente : entity.getComponents()) {
                if (componente instanceof Mesh) {
                    Mesh malla = (Mesh) componente;
                    for (Vertex vertice : malla.vertexList) {
                        Bone[] listaHuesos = new Bone[vertice.getListaHuesosIds().length];
                        for (int j = 0; j < vertice.getListaHuesosIds().length; j++) {
                            listaHuesos[j] = esqueleto.getBone(vertice.getListaHuesosIds()[j]);
                        }
                        vertice.setListaHuesos(listaHuesos);
                    }
                }
            }
        }
        // if (esqueleto != null) {
        // ****************** ANIMACIONES ********************************
        AnimationStorageComponent almacen = loadAnimations(aiScene, esqueleto, entity);
        if (almacen != null) {
            entity.addComponent(almacen);
            // le setea la primera animacion para q se ejecute
            Iterator it = almacen.getAnimaciones().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                entity.addComponent((AnimationComponent) pair.getValue());
                break;
            }
        }
        // }

        // ***************************************************************
        // ***** LUCES
        // ***************************************************************
        int numLuces = aiScene.mNumLights();
        PointerBuffer aiLuces = aiScene.mLights();
        for (int i = 0; i < numLuces; i++) {
            AILight aiLuz = AILight.create(aiLuces.get(i));
            if (nombre.contains(aiLuz.mName().dataString())) {
                // crea el componente luz
                QLigth luz = null;

                AIColor3D cDifuso = aiLuz.mColorDiffuse();

                QColor colorDifuso = new QColor(cDifuso.r() / 255.0f, cDifuso.g() / 255.0f, cDifuso.b() / 255.0f);
                AIVector3D dir;
                switch (aiLuz.mType()) {
                    case Assimp.aiLightSource_POINT:
                        luz = new QPointLigth();
                        break;
                    case Assimp.aiLightSource_AMBIENT:
                        break;
                    case Assimp.aiLightSource_AREA:
                        break;
                    case Assimp.aiLightSource_DIRECTIONAL:
                        dir = aiLuz.mDirection();
                        luz = new QDirectionalLigth(Vector3.of(dir.x(), dir.y(), dir.z()));
                        break;
                    case Assimp.aiLightSource_SPOT:
                        dir = aiLuz.mDirection();
                        luz = new QSpotLigth(Vector3.of(dir.x(), dir.y(), dir.z()), aiLuz.mAngleOuterCone(),
                                aiLuz.mAngleInnerCone());
                        break;
                }
                if (luz != null) {
                    luz.setRadio(100.0f);
                    luz.setColor(colorDifuso);
                    entity.addComponent(luz);
                }
            }

        }
        // ***************************************************************
        // ***** HIJOS
        // ***************************************************************
        int hijos = aiNodo.mNumChildren();
        if (hijos > 0) {
            PointerBuffer bufferHijos = aiNodo.mChildren();
            for (int i = 0; i < hijos; i++) {
                AINode nodoHijo = AINode.create(bufferHijos.get(i));
                entity.addChild(procesarNodo(aiScene, nodoHijo, nodoRaiz, mapaMateriales));
            }
        }

        return entity;
    }

    /**
     * Cada aiNodeAnim contiene los estados de la animacion (Frames) de un solo
     * hueso
     *
     * @param aiNodeAnim
     * @param boneList
     * @param nodoRaiz
     * @param rootTransformation
     * @param duracionTotalAnimacion
     * @param esqueleto
     * @return
     */
    private List<AnimationFrame> contruirFrames(AINodeAnim aiNodeAnim, float duracionTotalAnimacion,
            Skeleton esqueleto, Entity objeto) {
        int numFrames = aiNodeAnim.mNumPositionKeys();
        // Cada frame deberia tener configurado el tiempo, sin embargo al no encontrarlo
        // asignamos equitativamente el tiempo para cada frame
        float duracion = (float) duracionTotalAnimacion / numFrames;
        List<AnimationFrame> frameList = new ArrayList<>();

        AIVectorKey.Buffer positionKeys = aiNodeAnim.mPositionKeys();
        AIVectorKey.Buffer scalingKeys = aiNodeAnim.mScalingKeys();
        AIQuatKey.Buffer rotationKeys = aiNodeAnim.mRotationKeys();

        for (int iFrame = 0; iFrame < numFrames; iFrame++) {
            AnimationFrame qFrame = new AnimationFrame(duracion * iFrame);
            Entity hueso = null;

            if (esqueleto != null) {
                hueso = esqueleto.getBone(aiNodeAnim.mNodeName().dataString());
            } else {
                if (objeto.getName().contains(aiNodeAnim.mNodeName().dataString())) {
                    hueso = objeto;
                }
            }

            if (hueso != null) {
                Transform transformacion = new Transform(Rotation.CUATERNION);
                if (positionKeys != null && positionKeys.hasRemaining()) {
                    try {
                        // AIVectorKey aiVecKey = ;
                        AIVector3D vec = positionKeys.get(iFrame).mValue();
                        transformacion.getLocation().set(vec.x(), vec.y(), vec.z());
                        if (iFrame < aiNodeAnim.mNumScalingKeys()) {
                            if (scalingKeys != null && scalingKeys.hasRemaining()) {
                                vec = scalingKeys.get(iFrame).mValue();
                                transformacion.getScale().set(vec.x(), vec.y(), vec.z());
                            }
                        }
                    } catch (Exception e) {
                        logger.severe(
                                "[X] Error cargando posicion y escala, no transformacion para el frame solicitado");
                    }
                }
                if (rotationKeys != null && rotationKeys.hasRemaining()) {
                    try {
                        AIQuatKey quatKey = rotationKeys.get(iFrame);
                        AIQuaternion aiQuat = quatKey.mValue();
                        Cuaternion quat = new Cuaternion(aiQuat.x(), aiQuat.y(), aiQuat.z(), aiQuat.w());
                        transformacion.getRotation().setCuaternion(quat);
                    } catch (Exception e) {
                        logger.severe("[X] Error cargando rotacion, no transformacion para el frame solicitado");
                    }
                }

                qFrame.addPair(new AnimationPair(hueso, transformacion));
                frameList.add(qFrame);
            }
        }

        return frameList;
    }

    /**
     * Crea un hueso para el esqueleto
     *
     * @param id
     * @param nodo
     * @param esqueleto
     * @param maxId
     * @return
     */
    private Bone crearHueso(int id, Node nodo, Skeleton esqueleto, Integer maxId,
            Integer contadorNodosSinPadre) {
        Bone hueso = new Bone(id, nodo.getName());
        Matrix4 matriz = nodo.getTransformacion();
        hueso.setTransform(new Transform(Rotation.CUATERNION));
        hueso.getTransform().fromMatrix(matriz);
        Node nodoPadre = nodo.getParent();
        if (nodoPadre != null) {
            // codigo agregado para corregir carga de los modelos MD5 y collada, se debe
            // buscar la manera de evitar agregar los nodos que no son de los huesos
            // if (!nodoPadre.getName().equals("<MD5_Hierarchy>")
            // && !nodoPadre.getName().equals("Scene")
            // && !nodoPadre.getName().equals("RootNode")) {
            Bone huesoPadre = esqueleto.getBone(nodoPadre.getName());
            if (huesoPadre != null) {
                huesoPadre.addChild(hueso);
            } else {
                // if (contadorNodosSinPadre < 0) {
                // contadorNodosSinPadre++;
                // maxId++;
                // logger.info("NO SE ENCUENTRA EL HUESO PADRE CON EL NOMBRE:{" +
                // nodoPadre.getName()
                // + "} para el Hijo :{" + nodo.getName() + "} " + "[" + contadorNodosSinPadre
                // + "]. Se crea un nuevo hueso para el padre no encontrado");
                // // agrego un hueso , el padre que no se encuentra
                // Bone nuevoHuesoPadre = crearHueso(maxId, nodoPadre, esqueleto, maxId,
                // contadorNodosSinPadre);
                // esqueleto.agregarHueso(nuevoHuesoPadre);
                // nuevoHuesoPadre.addChild(hueso);
                // } else {
                logger.warning("[!] NO SE ENCUENTRA EL HUESO PADRE CON EL NOMBRE:{" + nodoPadre.getName()
                        + "} para el Hijo :{" + nodo.getName() + "} " + "[" + contadorNodosSinPadre
                        + "].");
                // }
            }
            // }
        }
        return hueso;
    }

    /**
     * Crea un esqueleto a partir del esqueleto de la lista de bones cargados
     * previamente al procesar las mallas
     *
     * @param aiMesh
     * @param nodoRaiz
     * @return
     */
    private Skeleton loadSkeleton(List<AssimpBone> boneList, Node nodoRaiz) {
        Skeleton esqueleto = new Skeleton();
        esqueleto.setBones(new ArrayList<>());
        // debemos ordenar la lista para agregar primero los nodos que tienen menos
        // padres
        // paso 1, obtener la ruta de la jerarquia
        for (AssimpBone bone : boneList) {
            AIBone aiBone = bone.getAiBone();
            Node nodo = nodoRaiz.findByName(aiBone.mName().dataString());
            bone.setRutaJerarquia(nodo.getRuta());
        }
        // paso 2 ordeno
        Collections.sort(boneList, new Comparator<AssimpBone>() {
            @Override
            public int compare(AssimpBone t, AssimpBone t1) {
                return t.getRutaJerarquia().compareTo(t1.getRutaJerarquia());
            }
        });

        // logger.info("Lista ordenada");
        // for (Bone bone : boneList) {
        // logger.info(bone.getRutaJerarquia());
        // }
        Integer maxId = boneList.size();
        Integer contadorNodosSinPadre = 0;// para comprobar que no se agreguen nodos vacios hasta la raiz.
        for (AssimpBone bone : boneList) {
            Node nodo = nodoRaiz.findByName(bone.getBoneName());
            esqueleto.addBone(crearHueso(bone.getBoneId(), nodo, esqueleto, maxId, contadorNodosSinPadre));
        }
        esqueleto.calcularMatricesInversas();
        //
        // logger.info("Esqueleto Creado");
        // for (QHueso hueso : esqueleto.getHuesos()) {
        // if (hueso.getPadre() == null) {
        // Entity.imprimirArbolEntidad(hueso, "");
        // }
        // }
        return esqueleto;
    }

    /**
     * Procesa las animaciones de la escena y devuelve un componente Almacen de
     * animaciones que contiene todas las animaciones encontradas
     *
     * @param aiScene
     * @param aiMesh
     * @param esqueleto
     * @return
     */
    private AnimationStorageComponent loadAnimations(AIScene aiScene, Skeleton esqueleto, Entity objeto) {

        AnimationStorageComponent almacen = new AnimationStorageComponent();
        boolean satisfactorio = false;

        // agrega una animacion Pose con las transformaciones originales
        // almacen.agregarAnimacion("Pose",
        // QComponenteAnimacion.crearAnimacionPose(esqueleto));
        // Process all animations
        int numAnimations = aiScene.mNumAnimations();
        PointerBuffer aiAnimations = aiScene.mAnimations();
        for (int i = 0; i < numAnimations; i++) {
            AIAnimation aiAnimation = AIAnimation.create(aiAnimations.get(i));
            String nombreAnimacion = aiAnimation.mName().dataString();

            float duracion = (float) aiAnimation.mDuration();
            if (aiAnimation.mTicksPerSecond() > 0) {
                duracion /= aiAnimation.mTicksPerSecond();
            }
            if (nombreAnimacion == null || nombreAnimacion.isEmpty()) {
                nombreAnimacion = "Anima_Default_" + duracion;
            }
            logger.info("            Animacion :" + nombreAnimacion);
            logger.info("                Duracion (ticks):" + aiAnimation.mDuration());
            logger.info("                Ticks por segundo:" + aiAnimation.mTicksPerSecond());
            logger.info("                Duracion (s):" + duracion);

            // aiAnimation.mDuration()
            // Calculate transformation matrices for each node
            int numChanels = aiAnimation.mNumChannels();
            PointerBuffer aiChannels = aiAnimation.mChannels();

            AnimationComponent animation = new AnimationComponent(duracion);
            animation.setNombre(nombreAnimacion);
            animation.setLoop(true);

            // las animaciones vienen separadas por canales
            // cada canal maneja por separado un hueso
            // se debe armar los frames de cada canal (hueso) y luego debemos unirlos,
            // agregando cada canal como pares (hueso-transformacion) de una sola lista de
            // frames
            int numeroTotalFrames = 0;
            Map<Integer, List<AnimationFrame>> mapa = new HashMap<>();
            for (int j = 0; j < numChanels; j++) {
                AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(j));
                if (esqueleto != null || objeto.getName().contains(aiNodeAnim.mNodeName().dataString())) {
                    List<AnimationFrame> framesCanal = contruirFrames(aiNodeAnim, duracion, esqueleto, objeto);
                    numeroTotalFrames = Math.max(numeroTotalFrames, framesCanal.size());
                    mapa.put(j, framesCanal);
                    satisfactorio = true;
                }
            }

            // recorro cada frame y luego cada hueso
            for (int j = 0; j < numeroTotalFrames; j++) {
                AnimationFrame frame = new AnimationFrame(0.0f);
                float tiempo = 0.0f;
                // recorro todos los frames de cada hueso, y los uno una una lista de frames
                for (int k = 0; k < numChanels; k++) {
                    if (mapa.containsKey(k)) {
                        // si existe un frame con un par animado para este frame
                        if (mapa.get(k).size() > j) {
                            AnimationFrame frameHueso = mapa.get(k).get(j);
                            tiempo = Math.max(tiempo, frameHueso.getTimeMark());
                            for (AnimationPair par : frameHueso.getAnimationPairList()) {
                                frame.addPair(par);
                            }
                        }
                    }
                }
                frame.setTimeMark(tiempo);// actualiza el tiempo que fue inicializado en 0.00f
                animation.addFrame(frame);
            }
            almacen.add(animation.getNombre(), animation);
        }
        if (satisfactorio) {
            return almacen;
        } else {
            return null;
        }
    }

    /**
     * Procesa los huesos de cada malla, extrae la informacion de los huesos y
     * carga la informacion de los pesos a la geometria
     *
     * @param malla
     * @param aiMesh
     * @param boneList
     */
    private void procesarBones(AIMesh aiMesh, Mesh malla, List<AssimpBone> boneList) {
        Map<Integer, List<VertexWeight>> weightSet = new HashMap<>();
        int numBones = aiMesh.mNumBones();
        PointerBuffer aiBones = aiMesh.mBones();
        for (int i = 0; i < numBones; i++) {
            AIBone aiBone = AIBone.create(aiBones.get(i));
            AssimpBone bone = null;
            // si hay diferentes mallas, pueden apuntar a los mismos huesos que otras mallas
            // por lo tanto recorremos la lista comprobando si no hay ya agregado ese hueso

            for (AssimpBone boneTmp : boneList) {
                if (boneTmp.getBoneName().equals(aiBone.mName().dataString())) {
                    bone = boneTmp;
                    break;
                }
            }

            // si no lo encontro, lo agrega
            if (bone == null) {
                int id = boneList.size();
                // logger.info(" -> loading aBone " + aiBone.mName().dataString());
                bone = new AssimpBone(id, aiBone.mName().dataString(),
                        AssimpModelLoader.toMatrix(aiBone.mOffsetMatrix()),
                        aiBone);
                boneList.add(bone);
            }

            int numWeights = aiBone.mNumWeights();
            AIVertexWeight.Buffer aiWeights = aiBone.mWeights();
            for (int j = 0; j < numWeights; j++) {
                AIVertexWeight aiWeight = aiWeights.get(j);
                VertexWeight vw = new VertexWeight(bone.getBoneId(), aiWeight.mVertexId(), aiWeight.mWeight());
                List<VertexWeight> vertexWeightList = weightSet.get(vw.getVertexId());
                if (vertexWeightList == null) {
                    vertexWeightList = new ArrayList<>();
                    weightSet.put(vw.getVertexId(), vertexWeightList);
                }
                vertexWeightList.add(vw);
            }
        }

        int numVertices = aiMesh.mNumVertices();
        for (int i = 0; i < numVertices; i++) {
            List<VertexWeight> vertexWeightList = weightSet.get(i);
            int size = vertexWeightList != null ? vertexWeightList.size() : 0;

            // codigo para el formato QEngine
            float[] pesos = new float[size];
            int[] huesosIds = new int[size];

            for (int j = 0; j < size; j++) {
                if (j < size) {
                    VertexWeight vw = vertexWeightList.get(j);
                    pesos[j] = vw.getWeight();
                    huesosIds[j] = vw.getBoneId();
                } else {
                    pesos[j] = 0.0f;
                    huesosIds[j] = 0;
                }
            }

            // codigo para el formato QEngine
            malla.vertexList[i].setListaHuesosIds(huesosIds);
            malla.vertexList[i].setListaHuesosPesos(pesos);
        }
    }

    /**
     * Crea una geometria desde la malla del nodo.
     *
     * @param aiMesh
     * @param mapaMateriales
     * @param boneList
     * @return
     */
    private Mesh loadMesh(AIMesh aiMesh, Map<Integer, AbstractMaterial> mapaMateriales,
            List<AssimpBone> boneList) {
        logger.info("  Procesando malla:" + aiMesh.mName().dataString());
        Mesh malla = new Mesh();
        malla.name = aiMesh.mName().dataString();
        AbstractMaterial material;
        int materialIdx = aiMesh.mMaterialIndex();
        if (mapaMateriales.containsKey(materialIdx)) {
            material = mapaMateriales.get(materialIdx);
        } else {
            material = new Material();
            logger.warning("[!] No se encontró material con el índice :" + materialIdx);
        }
        loadVertices(aiMesh, malla);
        loadNormals(aiMesh, malla);
        loadUVs(aiMesh, malla);
        loadFaces(aiMesh, malla, material);
        procesarBones(aiMesh, malla, boneList);
        // malla.computeNormals();
        return malla;
    }

    /**
     * Convierte una matriz 4x4 de ASSMIP a la matriz del engine
     *
     * @param aiMatrix4x4
     * @return
     */
    public static Matrix4 toMatrix(AIMatrix4x4 aiMatrix4x4) {

        // return QJOMLUtil.convertirQMatriz4(toMatrix_2(aiMatrix4x4));
        Matrix4 result = new Matrix4(
                aiMatrix4x4.a1(), aiMatrix4x4.a2(), aiMatrix4x4.a3(), aiMatrix4x4.a4(),
                aiMatrix4x4.b1(), aiMatrix4x4.b2(), aiMatrix4x4.b3(), aiMatrix4x4.b4(),
                aiMatrix4x4.c1(), aiMatrix4x4.c2(), aiMatrix4x4.c3(), aiMatrix4x4.c4(),
                aiMatrix4x4.d1(), aiMatrix4x4.d2(), aiMatrix4x4.d3(), aiMatrix4x4.d4());
        // QMatriz4 result = new QMatriz4(
        // aiMatrix4x4.a1(), aiMatrix4x4.b1(), aiMatrix4x4.c1(), aiMatrix4x4.d1(),
        // aiMatrix4x4.a2(), aiMatrix4x4.b2(), aiMatrix4x4.c2(), aiMatrix4x4.d2(),
        // aiMatrix4x4.a3(), aiMatrix4x4.b3(), aiMatrix4x4.c3(), aiMatrix4x4.d3(),
        // aiMatrix4x4.a4(), aiMatrix4x4.b4(), aiMatrix4x4.c4(), aiMatrix4x4.d4()
        // );

        // QMatriz4 result = new QMatriz4(
        // trans.m00, trans.m01, trans.m02, trans.m03,
        // trans.m10, trans.m11, trans.m12, trans.m13,
        // trans.m20, trans.m21, trans.m22, trans.m23,
        // trans.m30, trans.m31, trans.m32, trans.m33
        //
        // );
        // result.m00(aiMatrix4x4.a1());
        // result.m10(aiMatrix4x4.a2());
        // result.m20(aiMatrix4x4.a3());
        // result.m30(aiMatrix4x4.a4());
        // result.m01(aiMatrix4x4.b1());
        // result.m11(aiMatrix4x4.b2());
        // result.m21(aiMatrix4x4.b3());
        // result.m31(aiMatrix4x4.b4());
        // result.m02(aiMatrix4x4.c1());
        // result.m12(aiMatrix4x4.c2());
        // result.m22(aiMatrix4x4.c3());
        // result.m32(aiMatrix4x4.c4());
        // result.m03(aiMatrix4x4.d1());
        // result.m13(aiMatrix4x4.d2());
        // result.m23(aiMatrix4x4.d3());
        // result.m33(aiMatrix4x4.d4());
        return result;
    }

    /**
     * Carga solamenta los vertices de la malla
     * 
     * @param aiMesh
     * @param malla
     */
    protected void loadVertices(AIMesh aiMesh, Mesh malla) {
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        AIVector3D aiVertex = null;
        while (aiVertices.hasRemaining()) {
            aiVertex = aiVertices.get();
            malla.addVertex(aiVertex.x(), aiVertex.y(), aiVertex.z());

        }
    }

    /**
     * Carga solamenta los vertices de la malla
     * 
     * @param aiMesh
     * @param malla
     */
    protected void loadNormals(AIMesh aiMesh, Mesh malla) {
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();
        AIVector3D aiNormal = null;
        while (aiNormals.hasRemaining()) {
            aiNormal = aiNormals.get();
            malla.addNormal(aiNormal.x(), aiNormal.y(), aiNormal.z());
        }
    }

    /**
     * Carga solamenta los coordenadas UV de la malla
     * 
     * @param aiMesh
     * @param malla
     */
    protected void loadUVs(AIMesh aiMesh, Mesh malla) {
        AIVector3D.Buffer aiUVs = aiMesh.mTextureCoords(0);
        AIVector3D aiUV = null;
        if (aiUVs != null) {
            while (aiUVs.hasRemaining()) {
                aiUV = aiUVs.get();
                malla.addUV(aiUV.x(), aiUV.y());
            }
        }
    }

    protected void loadFaces(AIMesh aiMesh, Mesh malla, AbstractMaterial material) {
        int numFaces = aiMesh.mNumFaces();
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        for (int i = 0; i < numFaces; i++) {
            try {
                AIFace aiFace = aiFaces.get(i);
                IntBuffer buffer = aiFace.mIndices();
                int[] tmp = new int[aiFace.mNumIndices()];
                buffer.get(tmp);
                malla.addPoly(material, tmp).setSmooth(true);
            } catch (Exception ex) {
                Logger.getLogger(AssimpModelLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Obtiene la textura solicitada de un material
     *
     * @param aiMaterial
     * @param textureType
     * @param texturesDir
     * @return
     */
    protected Texture loadTexture(AIMaterial aiMaterial, int textureType, String texturesDir) {
        Texture textura = null;
        AIString path = AIString.calloc();
        Assimp.aiGetMaterialTexture(aiMaterial, textureType, 0, path, (IntBuffer) null, null, null, null, null, null);
        String txtPath = path.dataString();
        File file;

        if (txtPath != null && txtPath.length() > 0) {
            try {
                file = new File(txtPath);
                if (file.exists()) {
                    textura = AssetManager.get().loadTexture(file);
                    // textureFile = txtPath;
                    // textureFile = textureFile.replace("//", "/");
                } else {
                    file = new File(texturesDir, txtPath);
                    if (file.exists()) {
                        textura = AssetManager.get().loadTexture(file);
                    }
                    // textureFile = texturesDir + "/" + txtPath;
                    // textureFile = textureFile.replace("//", "/");
                }
            } catch (Exception ex) {
                Logger.getLogger(AssimpModelLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return textura;
    }

}
