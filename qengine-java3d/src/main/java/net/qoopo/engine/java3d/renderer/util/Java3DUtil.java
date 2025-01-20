/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.java3d.renderer.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PointLight;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.SpotLight;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.image.TextureLoader;

import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.Rotation;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.util.QVectMathUtil;

/**
 *
 * @author alberto
 */
public class Java3DUtil {

    private static Logger logger = Logger.getLogger("java3d-util");

    public static Node crearPiramideEjemplo() {
        Point3f e = new Point3f(1.0f, 0.0f, 0.0f); // east
        Point3f s = new Point3f(0.0f, 0.0f, 1.0f); // south
        Point3f w = new Point3f(-1.0f, 0.0f, 0.0f); // west
        Point3f n = new Point3f(0.0f, 0.0f, -1.0f); // north
        Point3f t = new Point3f(0.0f, 0.721f, 0.0f); // top

        TriangleArray pyramidGeometry = new TriangleArray(18, TriangleArray.COORDINATES);
        pyramidGeometry.setCoordinate(0, e);
        pyramidGeometry.setCoordinate(1, t);
        pyramidGeometry.setCoordinate(2, s);

        pyramidGeometry.setCoordinate(3, s);
        pyramidGeometry.setCoordinate(4, t);
        pyramidGeometry.setCoordinate(5, w);

        pyramidGeometry.setCoordinate(6, w);
        pyramidGeometry.setCoordinate(7, t);
        pyramidGeometry.setCoordinate(8, n);

        pyramidGeometry.setCoordinate(9, n);
        pyramidGeometry.setCoordinate(10, t);
        pyramidGeometry.setCoordinate(11, e);

        pyramidGeometry.setCoordinate(12, e);
        pyramidGeometry.setCoordinate(13, s);
        pyramidGeometry.setCoordinate(14, w);

        pyramidGeometry.setCoordinate(15, w);
        pyramidGeometry.setCoordinate(16, n);
        pyramidGeometry.setCoordinate(17, e);
        GeometryInfo geometryInfo = new GeometryInfo(pyramidGeometry);
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(geometryInfo);

        GeometryArray result = geometryInfo.getGeometryArray();

        // yellow appearance
        Appearance appearance = new Appearance();
        Color3f color = new Color3f(Color.yellow);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Texture texture = new Texture2D();
        TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
        Material mat = new Material(color, black, color, white, 70f);
        appearance.setTextureAttributes(texAttr);
        appearance.setMaterial(mat);
        appearance.setTexture(texture);
        Shape3D shape = new Shape3D(result, appearance);
        return shape;
    }

    public static Node crearNodo(EntityComponent objeto) {
        TransformGroup GT = new TransformGroup();
        Transform3D transform = new Transform3D();
        GT.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        // quito textures para pruebas
        // ((QMaterialBas) objeto.primitivas[0].material).setMapaDifusa(null);

        try {
            Node nodo;
            if (objeto instanceof QLigth) {
                nodo = crearNodoLuz((QLigth) objeto);
            } else if (objeto instanceof Mesh) {
                nodo = crearNodoGeometria((Mesh) objeto);
            } else {
                return null;
            }

            // SETEO DE LAS TRANSFORMACIONES
            long t = System.currentTimeMillis();
            transform.setScale(QVectMathUtil.convertirVector3d(objeto.getEntity().getTransform().getScale()));
            transform.setTranslation(
                    QVectMathUtil
                            .convertirVector3f(objeto.getEntity().getMatrizTransformacion(t).toTranslationVector()));
            Rotation rot = new Rotation();
            rot.setCuaternion(objeto.getEntity().getMatrizTransformacion(t).toRotationQuat());
            rot.updateEuler();

            Transform3D t1 = new Transform3D();
            Transform3D t2 = new Transform3D();
            Transform3D t3 = new Transform3D();

            t1.rotX(rot.getEulerAngles().x);
            t2.rotY(rot.getEulerAngles().y);
            t3.rotZ(rot.getEulerAngles().z);
            t2.mul(t1);
            t3.mul(t2);
            transform.mul(t3);
            GT.setTransform(transform);
            GT.addChild(nodo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return GT;
    }

    public static Node crearNodoLuz(QLigth luz) {

        long time = System.currentTimeMillis();
        try {
            if (luz instanceof QDirectionalLigth) {
                BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                        // ((QLuzDireccional) luz).radio
                        1000000000);
                Color3f color = new Color3f(((QDirectionalLigth) luz).color.r, ((QDirectionalLigth) luz).color.g,
                        ((QDirectionalLigth) luz).color.b);
                Vector3f direccion = QVectMathUtil.convertirVector3f(((QDirectionalLigth) luz).getDirection());
                DirectionalLight luzJava3D = new DirectionalLight(color, direccion);

                luzJava3D.setCapability(PointLight.ALLOW_STATE_WRITE);
                luzJava3D.setCapability(PointLight.ALLOW_COLOR_WRITE);
                luzJava3D.setCapability(PointLight.ALLOW_POSITION_WRITE);
                luzJava3D.setCapability(PointLight.ALLOW_ATTENUATION_WRITE);
                luzJava3D.setInfluencingBounds(bounds);
                // luzJava3D.setScope(group, 0);
                return luzJava3D;
            } else if (luz instanceof QPointLigth) {
                BoundingSphere bounds = new BoundingSphere(
                        QVectMathUtil.convertirPoint3d(
                                ((QPointLigth) luz).getEntity().getMatrizTransformacion(time).toTranslationVector()),
                        ((QPointLigth) luz).radio
                // 100000
                );
                Color3f color = new Color3f(((QPointLigth) luz).color.r, ((QPointLigth) luz).color.g,
                        ((QPointLigth) luz).color.b);
                // factor de atenuacion = 1/(c+l*d+q*q*d)
                // donde c es el primer parametro del punto (x), d es al factor lineal, segundo
                // aprametro (y) , y q es el cuadratico, tercer parametro (z)

                PointLight luzJava3D = new PointLight(color,
                        QVectMathUtil.convertirPoint3f(
                                ((QPointLigth) luz).getEntity().getMatrizTransformacion(time).toTranslationVector()),
                        new Point3f(0, 1.0f / (((QPointLigth) luz).energy * 1f), 0));
                luzJava3D.setCapability(PointLight.ALLOW_STATE_WRITE);
                luzJava3D.setCapability(PointLight.ALLOW_COLOR_WRITE);
                luzJava3D.setCapability(PointLight.ALLOW_POSITION_WRITE);
                luzJava3D.setCapability(PointLight.ALLOW_ATTENUATION_WRITE);

                luzJava3D.setInfluencingBounds(bounds);
                return luzJava3D;

            } else if (luz instanceof QSpotLigth) {
                BoundingSphere bounds = new BoundingSphere(
                        QVectMathUtil.convertirPoint3d(
                                ((QSpotLigth) luz).getEntity().getMatrizTransformacion(time).toTranslationVector()),
                        ((QSpotLigth) luz).radio
                // 100000
                );
                Color3f color = new Color3f(((QSpotLigth) luz).color.r, ((QSpotLigth) luz).color.g,
                        ((QSpotLigth) luz).color.b);

                Vector3f direccion = QVectMathUtil.convertirVector3f(((QSpotLigth) luz).getDirection());

                // factor de atenuacion =
                // AAtt(ang) = cos(ang) elevado a la factorNormal . factorNormal es el factor de
                // concentracion
                SpotLight luzJava3D = new SpotLight(
                        color,
                        QVectMathUtil.convertirPoint3f(
                                ((QSpotLigth) luz).getEntity().getMatrizTransformacion(time).toTranslationVector()),
                        new Point3f(0, 1.0f / (((QSpotLigth) luz).energy * 1f), 0),
                        direccion,
                        // (float) Math.toRadians(Math.toDegrees(((QLuzSpot) luz).getAngulo()) / 3),
                        ((QSpotLigth) luz).getAnguloExterno(),
                        0 // 0 distribucion uniforme de la luz, 128 mas esta concentrada la luz en el
                          // centro. 96 vendria a ser mas o menos un 75%
                );

                luzJava3D.setCapability(PointLight.ALLOW_STATE_WRITE);
                luzJava3D.setCapability(PointLight.ALLOW_COLOR_WRITE);
                luzJava3D.setCapability(PointLight.ALLOW_POSITION_WRITE);
                luzJava3D.setCapability(PointLight.ALLOW_ATTENUATION_WRITE);
                luzJava3D.setInfluencingBounds(bounds);

                return luzJava3D;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    public static Node crearNodoGeometria(Mesh objeto) {
        Shape3D nodo = null;
        try {
            List<Point3f> listaPuntos = new ArrayList<>();
            List<TexCoord2f> listaUV = new ArrayList<>();
            List<Vector3f> listaNormales = new ArrayList<>();// mapeo las normales que ya calcule antes, en caso de
                                                             // tener las normales invertidas
            for (Vertex vertice : objeto.vertexList) {
                listaPuntos.add(new Point3f(vertice.location.x, vertice.location.y, vertice.location.z));
            }
            for (Vector3 normal : objeto.normalList) {
                listaNormales.add(QVectMathUtil.convertirVector3f(normal));
            }

            for (Vector2 uv : objeto.uvList)
                listaUV.add(new TexCoord2f(uv.x, uv.y));

            // ------------------------CONSTRUCCION GEOMETRIA
            GeometryArray listaGeometria;
            switch (objeto.primitiveList[0].vertexIndexList.length) {
                case 4:
                    // si son cuadrados
                    if (((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                            .getColorMap() != null) {
                        listaGeometria = new QuadArray(objeto.primitiveList.length * 4,
                                QuadArray.COORDINATES | QuadArray.NORMALS | QuadArray.TEXTURE_COORDINATE_2);
                    } else {
                        listaGeometria = new QuadArray(objeto.primitiveList.length * 4,
                                QuadArray.COORDINATES | QuadArray.NORMALS);
                    }
                    break;
                case 3:
                    // si son triangulos
                    if (((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                            .getColorMap() != null) {
                        listaGeometria = new TriangleArray(objeto.primitiveList.length * 3,
                                TriangleArray.COORDINATES | TriangleArray.NORMALS | TriangleArray.TEXTURE_COORDINATE_2);
                    } else {
                        listaGeometria = new TriangleArray(objeto.primitiveList.length * 3,
                                TriangleArray.COORDINATES | TriangleArray.NORMALS);
                    }
                    break;
                default:
                    logger.info("Objeto " + objeto.name + ". La geometría no es de triangulos ni cuadrados. Tiene "
                            + objeto.primitiveList[0].vertexIndexList.length + " lados");
                    return null;// no construimos nada
            }
            int contVertices = 0;
            for (Primitive primitiva : objeto.primitiveList) {
                if (primitiva instanceof Poly) {
                    Poly poligono = (Poly) primitiva;
                    for (int indiceVertice : poligono.vertexIndexList) {
                        if (contVertices < listaGeometria.getVertexCount()) {
                            listaGeometria.setCoordinate(contVertices, listaPuntos.get(indiceVertice));
                            listaGeometria.setTextureCoordinate(0, contVertices, listaUV.get(contVertices));
                            listaGeometria.setNormal(contVertices, listaNormales.get(indiceVertice));
                            contVertices++;
                        } else {
                            System.out.println("Objeto " + objeto.name
                                    + " se intenta agrera un vertice de poligono indice=" + contVertices);
                            System.out.println("solo hay " + listaGeometria.getVertexCount());
                        }
                    }
                }
            }

            // coordendas de textura
            // if (((QMaterialBas) objeto.primitivas[0].material).getMapaDifusa() != null) {
            // contVertices = 0;
            // for (QPrimitiva primitiva : objeto.primitivas) {
            // if (primitiva instanceof QPoligono) {
            // QPoligono poligono = (QPoligono) primitiva;
            // for (QPoligono.UVCoordinate uv : poligono.uv) {
            // if (poligono.uv != null && poligono.uv.length > 0 && uv != null) {
            // try {
            // listaGeometria.setTextureCoordinate(0, contVertices, new TexCoord2f(uv.u, 1 -
            // uv.v));//las coordenadas verticales estan inversas
            // contVertices++;
            // } catch (Exception e) {
            // e.printStackTrace();
            // }
            // }
            // }
            // }
            // }
            // }
            GeometryInfo geometryInfo = new GeometryInfo(listaGeometria);
            // cone ste codigo genero normales automaticamente, en este caso yo ya las
            // agregue de nuestro modelo que ya las tiene calculadas
            // NormalGenerator ng = new NormalGenerator();
            // ng.generateNormals(geometryInfo);

            GeometryArray geometriaArreglo = geometryInfo.getGeometryArray();

            // ----------------------------------APARIENCIA
            Appearance apariencia = new Appearance();
            apariencia.setCapability(Texture.ALLOW_IMAGE_WRITE);
            apariencia.setCapability(Texture.ALLOW_ENABLE_WRITE);
            apariencia.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
            apariencia.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
            apariencia.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);

            Color3f color = new Color3f(
                    ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material).getColor().r,
                    ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material).getColor().g,
                    ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material).getColor().b);

            Color3f colorEmisivo = new Color3f(
                    ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material).getColor().r
                            * ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                                    .getEmissionIntensity(),
                    ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material).getColor().g
                            * ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                                    .getEmissionIntensity(),
                    ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material).getColor().b
                            * ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                                    .getEmissionIntensity());

            // Color3f colorEspecular = new Color3f(
            // ((QMaterialBas) objeto.primitivas[0].material).getColorEspecular().r,
            // ((QMaterialBas) objeto.primitivas[0].material).getColorEspecular().g,
            // ((QMaterialBas) objeto.primitivas[0].material).getColorEspecular().b);
            Color3f colorEspecular = new Color3f(
                    1.0f,
                    1.0f,
                    1.0f);

            // hasta ahora se asume que toda la geometria tiene una sola textura
            if (((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                    .getColorMap() != null) {
                // si hay textura
                // TextureLoader loader = new TextureLoader("img/text1.jpg", "INTENSITY", new
                // Container());
                try {
                    Texture texture;
                    if (((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                            .getColorMap().transientObject == null) {
                        TextureLoader loader = new TextureLoader(
                                ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                                        .getColorMap().getImagen());
                        texture = loader.getTexture();
                        ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                                .getColorMap().transientObject = texture;
                    } else {
                        texture = (Texture) ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                                .getColorMap().transientObject;
                    }
                    // estos atributos permite que la textura sea iluminada
                    TextureAttributes texAttr = new TextureAttributes();
                    texAttr.setTextureMode(TextureAttributes.MODULATE);// permite la iluminacion
                    // texAttr.setTextureMode(TextureAttributes.REPLACE);
                    // texAttr.setTextureMode(TextureAttributes.COMBINE);
                    texture.setBoundaryModeS(Texture.WRAP);
                    texture.setBoundaryModeT(Texture.WRAP);
                    // texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
                    apariencia.setTextureAttributes(texAttr);
                    apariencia.setTexture(texture);
                } catch (Exception e) {
                    System.out.println(
                            "error al cargar textura de " + objeto.getEntity().getName() + " error:" + e.getMessage());
                    e.printStackTrace();
                    // se configura como si no hay textura
                    Texture texture = new Texture2D();
                    TextureAttributes texAttr = new TextureAttributes();
                    texAttr.setTextureMode(TextureAttributes.MODULATE);
                    texture.setBoundaryModeS(Texture.WRAP);
                    texture.setBoundaryModeT(Texture.WRAP);
                    // texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
                    Material material = new Material(color, colorEmisivo, color, colorEspecular,
                            ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                                    .getSpecularExponent());

                    apariencia.setTextureAttributes(texAttr);
                    apariencia.setMaterial(material);
                    apariencia.setTexture(texture);
                }
                Material material = new Material(color, colorEmisivo, color, colorEspecular,
                        ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                                .getSpecularExponent());
                apariencia.setMaterial(material);
            } else {
                // si no hay textura
                Texture texture = new Texture2D();
                TextureAttributes texAttr = new TextureAttributes();
                texAttr.setTextureMode(TextureAttributes.MODULATE);// permite la ilumnicacion
                texture.setBoundaryModeS(Texture.WRAP);
                texture.setBoundaryModeT(Texture.WRAP);
                // texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
                Material material = new Material(color, colorEmisivo, color, colorEspecular,
                        ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                                .getSpecularExponent());

                apariencia.setTextureAttributes(texAttr);
                apariencia.setMaterial(material);
                apariencia.setTexture(texture);
            }

            // transparencia
            TransparencyAttributes t_attr = new TransparencyAttributes();
            t_attr.setTransparencyMode(TransparencyAttributes.BLENDED);
            t_attr.setTransparency(
                    1.0f - ((net.qoopo.engine.core.material.Material) objeto.primitiveList[0].material)
                            .getTransAlfa());

            apariencia.setTransparencyAttributes(t_attr);

            // CREACION DE LA FORMA 3D
            nodo = new Shape3D(geometriaArreglo, apariencia);
            return nodo;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // public static Node convertirAnodo(QGeometria objeto) {
    // TransformGroup GT = new TransformGroup();
    // Transform3D transform = new Transform3D();
    // GT.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    //
    // //quito textures para pruebas
    //// ((QMaterialBas) objeto.primitivas[0].material).setMapaDifusa(null);
    // Shape3D nodo = null;
    // try {
    // List<Point3f> listaPuntos = new ArrayList<>();
    // List<Vector3f> listaNormales = new ArrayList<>();//mapeo las normales que ya
    // calcule antes, en caso de tener las normales invertidas
    // for (QVertice vertice : objeto.vertices) {
    // listaPuntos.add(new Point3f(vertice.x, vertice.y, vertice.z));
    // listaNormales.add(QVectMathUtil.convertirVector3f(vertice.normal));
    // }
    //
    // //------------------------CONSTRUCCION GEOMETRIA
    // GeometryArray listaGeometria;
    // switch (objeto.primitivas[0].vertices.length) {
    // case 4:
    // //si son cuadrados
    // if (((QMaterialBas) objeto.primitivas[0].material).getMapaDifusa() != null) {
    // listaGeometria = new QuadArray(objeto.primitivas.length * 4,
    // QuadArray.COORDINATES | QuadArray.NORMALS | QuadArray.TEXTURE_COORDINATE_2);
    // } else {
    // listaGeometria = new QuadArray(objeto.primitivas.length * 4,
    // QuadArray.COORDINATES | QuadArray.NORMALS);
    // }
    // break;
    // case 3:
    // //si son triangulos
    // if (((QMaterialBas) objeto.primitivas[0].material).getMapaDifusa() != null) {
    // listaGeometria = new TriangleArray(objeto.primitivas.length * 3,
    // TriangleArray.COORDINATES | TriangleArray.NORMALS |
    // TriangleArray.TEXTURE_COORDINATE_2);
    // } else {
    // listaGeometria = new TriangleArray(objeto.primitivas.length * 3,
    // TriangleArray.COORDINATES | TriangleArray.NORMALS);
    // }
    // break;
    // default:
    // logger.info("Objeto " + objeto.nombre + ". La geometría no es de triangulos
    // ni cuadrados. Tiene " + objeto.primitivas[0].vertices.length + " lados");
    // return null;//no construimos nada
    // }
    // int contVertices = 0;
    // for (QPoligono poligono : objeto.primitivas) {
    // for (int indiceVertice : poligono.vertices) {
    // if (contVertices < listaGeometria.getVertexCount()) {
    // listaGeometria.setCoordinate(contVertices, listaPuntos.get(indiceVertice));
    // listaGeometria.setNormal(contVertices, listaNormales.get(indiceVertice));
    // contVertices++;
    // } else {
    // System.out.println("Objeto " + objeto.nombre + " se intenta agrera un vertice
    // de poligono indice=" + contVertices);
    // System.out.println("solo hay " + listaGeometria.getVertexCount());
    // }
    // }
    // }
    //
    // //coordendas de textura
    // if (((QMaterialBas) objeto.primitivas[0].material).getMapaDifusa() != null) {
    // contVertices = 0;
    // for (QPoligono poligono : objeto.primitivas) {
    // for (QPoligono.UVCoordinate uv : poligono.uv) {
    // if (poligono.uv != null && poligono.uv.length > 0 && uv != null) {
    // try {
    // listaGeometria.setTextureCoordinate(0, contVertices, new TexCoord2f(uv.u, 1 -
    // uv.v));//las coordenadas verticales estan inversas
    // contVertices++;
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // }
    // }
    // GeometryInfo geometryInfo = new GeometryInfo(listaGeometria);
    // //cone ste codigo genero normales automaticamente, en este caso yo ya las
    // agregue de nuestro modelo que ya las tiene calculadas
    //// NormalGenerator ng = new NormalGenerator();
    //// ng.generateNormals(geometryInfo);
    //
    // GeometryArray geometriaArreglo = geometryInfo.getGeometryArray();
    //
    // //----------------------------------APARIENCIA
    // Appearance apariencia = new Appearance();
    // apariencia.setCapability(Texture.ALLOW_IMAGE_WRITE);
    // apariencia.setCapability(Texture.ALLOW_ENABLE_WRITE);
    // apariencia.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
    // apariencia.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
    // apariencia.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
    //
    // Color3f color = new Color3f(
    // ((QMaterialBas) objeto.primitivas[0].material).colorDifusa.r,
    // ((QMaterialBas) objeto.primitivas[0].material).colorDifusa.g,
    // ((QMaterialBas) objeto.primitivas[0].material).colorDifusa.b);
    //
    // Color3f colorEmisivo = new Color3f(
    // ((QMaterialBas) objeto.primitivas[0].material).colorDifusa.r *
    // ((QMaterialBas) objeto.primitivas[0].material).luzEmitida,
    // ((QMaterialBas) objeto.primitivas[0].material).colorDifusa.g *
    // ((QMaterialBas) objeto.primitivas[0].material).luzEmitida,
    // ((QMaterialBas) objeto.primitivas[0].material).colorDifusa.b *
    // ((QMaterialBas) objeto.primitivas[0].material).luzEmitida);
    //
    // Color3f colorEspecular = new Color3f(
    // ((QMaterialBas) objeto.primitivas[0].material).colorEspecular.r,
    // ((QMaterialBas) objeto.primitivas[0].material).colorEspecular.g,
    // ((QMaterialBas) objeto.primitivas[0].material).colorEspecular.b);
    //
    // //hasta ahora se asume que toda la geometria tiene una sola textura
    // if (((QMaterialBas) objeto.primitivas[0].material).getMapaDifusa() != null) {
    // //si hay textura
    //// TextureLoader loader = new TextureLoader("img/text1.jpg", "INTENSITY", new
    // Container());
    // try {
    // Texture texture;
    // if (((QMaterialBas)
    // objeto.primitivas[0].material).getMapaDifusa().objetoJava3D == null) {
    // TextureLoader loader = new TextureLoader(((QMaterialBas)
    // objeto.primitivas[0].material).getMapaDifusa().getTexture());
    // texture = loader.getTexture();
    // ((QMaterialBas) objeto.primitivas[0].material).getMapaDifusa().objetoJava3D =
    // texture;
    // } else {
    // texture = (Texture) ((QMaterialBas)
    // objeto.primitivas[0].material).getMapaDifusa().objetoJava3D;
    // }
    // //estos atributos permite que la textura sea iluminada
    // TextureAttributes texAttr = new TextureAttributes();
    // texAttr.setTextureMode(TextureAttributes.MODULATE);//permite la iluminacion
    //// texAttr.setTextureMode(TextureAttributes.REPLACE);
    //// texAttr.setTextureMode(TextureAttributes.COMBINE);
    // texture.setBoundaryModeS(Texture.WRAP);
    // texture.setBoundaryModeT(Texture.WRAP);
    //// texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
    // apariencia.setTextureAttributes(texAttr);
    // apariencia.setTexture(texture);
    // } catch (Exception e) {
    // System.out.println("error al cargar textura de " + objeto.entity.nombre + "
    // error:" + e.getMessage());
    // e.printStackTrace();
    // //se configura como si no hay textura
    // Texture texture = new Texture2D();
    // TextureAttributes texAttr = new TextureAttributes();
    // texAttr.setTextureMode(TextureAttributes.MODULATE);
    // texture.setBoundaryModeS(Texture.WRAP);
    // texture.setBoundaryModeT(Texture.WRAP);
    //// texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
    // Material material = new Material(color, colorEmisivo, color, colorEspecular,
    // ((QMaterialBas) objeto.primitivas[0].material).specularExponent);
    //
    // apariencia.setTextureAttributes(texAttr);
    // apariencia.setMaterial(material);
    // apariencia.setTexture(texture);
    // }
    // Material material = new Material(color, colorEmisivo, color, colorEspecular,
    // ((QMaterialBas) objeto.primitivas[0].material).specularExponent);
    // apariencia.setMaterial(material);
    // } else {
    // //si no hay textura
    // Texture texture = new Texture2D();
    // TextureAttributes texAttr = new TextureAttributes();
    // texAttr.setTextureMode(TextureAttributes.MODULATE);//permite la ilumnicacion
    // texture.setBoundaryModeS(Texture.WRAP);
    // texture.setBoundaryModeT(Texture.WRAP);
    //// texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
    // Material material = new Material(color, colorEmisivo, color, colorEspecular,
    // ((QMaterialBas) objeto.primitivas[0].material).specularExponent);
    //
    // apariencia.setTextureAttributes(texAttr);
    // apariencia.setMaterial(material);
    // apariencia.setTexture(texture);
    // }
    //
    // //transparencia
    // TransparencyAttributes t_attr = new TransparencyAttributes();
    // t_attr.setTransparencyMode(TransparencyAttributes.BLENDED);
    // t_attr.setTransparency(1.0f - ((QMaterialBas)
    // objeto.primitivas[0].material).transAlfa);
    //
    // apariencia.setTransparencyAttributes(t_attr);
    //
    // //CREACION DE LA FORMA 3D
    // nodo = new Shape3D(geometriaArreglo, apariencia);
    //
    // //SETEO DE LAS TRASNFORMACIONES
    // long t = System.currentTimeMillis();
    //
    // transform.setScale(QVectMathUtil.convertirVector3d(objeto.entity.transformacion.getEscala()));
    //
    // transform.setTranslation(QVectMathUtil.convertirVector3f(objeto.entity.getMatrizTransformacion(t).toTranslationVector()));
    //
    // QRotacion rot = new QRotacion();
    //
    // rot.setCuaternion(objeto.entity.getMatrizTransformacion(t).toRotationQuat());
    // rot.actualizarAngulos();
    //
    // Transform3D t1 = new Transform3D();
    // Transform3D t2 = new Transform3D();
    // Transform3D t3 = new Transform3D();
    //
    // t1.rotX(rot.getAngulos().x);
    // t2.rotY(rot.getAngulos().y);
    // t3.rotZ(rot.getAngulos().z);
    // t2.mul(t1);
    // t3.mul(t2);
    // transform.mul(t3);
    // GT.setTransform(transform);
    // GT.addChild(nodo);
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return GT;
    // }
}
