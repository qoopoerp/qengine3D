package net.qoopo.engine3d.editor.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.animation.Bone;
import net.qoopo.engine.core.entity.component.animation.Skeleton;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.QVector4;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.TempVars;
import net.qoopo.engine.renderer.shader.vertex.VertexShader;
import net.qoopo.engine.renderer.util.TransformationVectorUtil;

@Getter
@Setter
public class EditorRenderer {

        public List<Entity> entidadesSeleccionadas = new ArrayList<>();

        public Entity entidadActiva = null;

        public static final int GIZMO_NINGUNO = 0;
        public static final int GIZMO_TRASLACION = 1;
        public static final int GIZMO_ROTACION = 2;
        public static final int GIZMO_ESCALA = 3;

        protected int tipoGizmoActual = GIZMO_TRASLACION;

        protected Poly polSeleccion = null;
        protected Poly polGrid = null;
        protected Poly polEjeX = null;
        protected Poly polEjeY = null;
        protected Poly polEjeZ = null;
        protected Poly polHueso = null;

        private static final QMaterialBas matX;
        private static final QMaterialBas matY;
        private static final QMaterialBas matZ;
        private static final QMaterialBas matGrid;
        private static final QMaterialBas matSeleccion;

        private RenderEngine renderer;

        private VertexShader vertexShader = new VertexShader();

        static {
                matX = new QMaterialBas("x");
                matX.setColorBase(QColor.RED);
                matX.setFactorEmision(0.85f);

                matY = new QMaterialBas("y");
                matY.setColorBase(QColor.GREEN);
                matY.setFactorEmision(0.85f);

                matZ = new QMaterialBas("z");
                matZ.setColorBase(QColor.BLUE);
                matZ.setFactorEmision(0.85f);

                matGrid = new QMaterialBas("grid");
                matGrid.setColorBase(QColor.LIGHT_GRAY);
                matGrid.setFactorEmision(0.85f);

                matSeleccion = new QMaterialBas("matSeleccion");
                matSeleccion.setColorBase(QColor.YELLOW);
                matSeleccion.setFactorEmision(1.0f);
        }

        public EditorRenderer(RenderEngine renderEngine) {
                this.renderer = renderEngine;
                // creo una entity no existente en el escena con un material para poder tener
                // un primitiva para el dibujo de la seleccion
                // se deberia revisar el raster por esas limitaciones
                polSeleccion = new Poly(new Mesh());
                polSeleccion.material = matSeleccion;
                polHueso = new Poly(new Mesh());
                QMaterialBas matHueso = new QMaterialBas("matHueso");
                matHueso.setColorBase(QColor.GRAY);
                matHueso.setFactorEmision(0.15f);
                polHueso.material = matHueso;
                polGrid = new Poly(new Mesh());
                polGrid.material = matGrid;
                polEjeX = new Poly(new Mesh());
                polEjeX.material = matX;
                polEjeY = new Poly(new Mesh());
                polEjeY.material = matY;
                polEjeZ = new Poly(new Mesh());
                polEjeZ.material = matZ;
        }

        public void render() {
                if (renderer.getFrameBuffer() != null) {
                        renderGrid();
                        // renderSkeleton();
                        // renderArtefactosEditor();
                        // renderer.render();
                }
        }

        private void renderGrid() {

                if (renderer.opciones.isDibujarGrid()) {

                        // La Matriz de vista es la inversa de la matriz de la camara.
                        // Esto es porque la camara siempre estara en el centro y movemos el mundo
                        // en direccion contraria a la camara.
                        QMatriz4 matrizVista = renderer.getCamara().getMatrizTransformacion(QGlobal.tiempo).invert();

                        // La matriz vistaModelo es el resultado de multiplicar la matriz de vista por
                        // la matriz del modelo
                        // De esta forma es la matriz que se usa para transformar el modelo a las
                        // coordenadas del mundo
                        // luego de estas coordenadas se transforma a las coordenadas de la camara
                        QMatriz4 matVistaModelo;

                        // La matriz modelo contiene la información del modelo
                        // Traslación, rotacion (en su propio eje ) y escala
                        QMatriz4 matrizModelo = QMatriz4.IDENTITY;

                        // MAtriz VistaModelo
                        // obtiene la matriz de transformacion del objeto combinada con la matriz de
                        // vision de la camara
                        matVistaModelo = matrizVista.mult(matrizModelo);

                        TempVars t = TempVars.get();
                        try {
                                int secciones = 50;
                                float espacio = 1.0f;
                                float maxCoordenada = espacio * secciones / 2.0f;
                                // lineas paralelas en el eje de X
                                for (int i = 0; i <= secciones; i++) {
                                        t.vertex1.set((-secciones / 2 + i) * espacio, 0, maxCoordenada, 1);
                                        t.vertex2.set((-secciones / 2 + i) * espacio, 0, -maxCoordenada, 1);

                                        renderer.renderLine(polGrid,
                                                        vertexShader.apply(t.vertex1, matVistaModelo),
                                                        vertexShader.apply(t.vertex2, matVistaModelo));
                                }
                                // lineas paralelas en el eje Z
                                for (int i = 0; i <= secciones; i++) {
                                        t.vertex1.set(maxCoordenada, 0, (-secciones / 2 + i) * espacio, 1);
                                        t.vertex2.set(-maxCoordenada, 0, (-secciones / 2 + i) * espacio, 1);

                                        renderer.renderLine(polGrid,
                                                        vertexShader.apply(t.vertex1, matVistaModelo),
                                                        vertexShader.apply(t.vertex2, matVistaModelo));
                                }
                                // el eje de X
                                {
                                        t.vertex1.set(maxCoordenada, 0, 0.001f, 1);
                                        t.vertex2.set(-maxCoordenada, 0, 0.001f, 1);

                                        renderer.renderLine(polSeleccion,
                                                        vertexShader.apply(t.vertex1, matVistaModelo),
                                                        vertexShader.apply(t.vertex2, matVistaModelo));
                                }
                                // el eje de Z
                                {
                                        t.vertex1.set(0, 0.001f, maxCoordenada, 1);
                                        t.vertex2.set(0, 0.001f, -maxCoordenada, 1);

                                        renderer.renderLine(polSeleccion,
                                                        vertexShader.apply(t.vertex1, matVistaModelo),
                                                        vertexShader.apply(t.vertex2, matVistaModelo));
                                }
                        } catch (Exception e) {
                                e.printStackTrace();
                        } finally {
                                t.release();
                        }
                }
        }

        private void renderSkeleton() {
                // ------------------------------------ ESQUELETOS
                // ------------------------------------
                // dibuja los esqueletos
                TempVars t = TempVars.get();
                try {
                        // QMatriz4 matTransformacion;
                        // los demas procesos de dibujo usan el bufferVertices, por lo tanto lo cambio
                        // temporalmente con el de los gizmos
                        // bufferVerticesTMP = bufferVertices;
                        // bufferVertices = bufferVertices2;
                        // una entity que contiene a los huesos para mostrarlos, aplicamos la misma
                        // transofmraicon de la entity original
                        Entity entidadTmp = null;
                        for (Entity entity : renderer.getEscena().getEntities()) {
                                for (EntityComponent componente : entity.getComponents()) {
                                        if (componente instanceof Skeleton) {
                                                Skeleton esqueleto = (Skeleton) componente;
                                                if (esqueleto.isMostrar()) {
                                                        if (esqueleto.isSuperponer()) {
                                                                renderer.getFrameBuffer().limpiarZBuffer();
                                                        }
                                                        List<Entity> lista = new ArrayList<>();
                                                        // entity falsa usada para corregir la transformacion de la
                                                        // entity y mostrar
                                                        // los huesos acordes a esta transformacion
                                                        entidadTmp = new Entity();
                                                        entidadTmp.getTransformacion().desdeMatrix(
                                                                        entity.getMatrizTransformacion(QGlobal.tiempo));
                                                        // lista.add(entidadTmp);
                                                        for (Bone hueso : esqueleto.getHuesos()) {
                                                                // agrega al nodo invisible para usar la transformacion
                                                                // de la entity y mostrar
                                                                // correctamente
                                                                // sin embargo da el error que nou sa la pose sin
                                                                // animacion
                                                                if (hueso.getParent() == null) {
                                                                        Bone hueson = hueso.clone();
                                                                        entidadTmp.addChild(hueson);
                                                                        hueson.toLista(lista);
                                                                }
                                                        }
                                                        // for (Entity ent : lista) {
                                                        // TransformationVectorUtil.transformar(ent,
                                                        // renderer.getCamara(),
                                                        // t.bufferVertices1, vertexShader);
                                                        // for (QPrimitiva poligono :
                                                        // t.bufferVertices1.getPoligonosTransformados()) {
                                                        // renderer.raster(t.bufferVertices1, poligono, false);
                                                        // }
                                                        // }
                                                }
                                        }
                                }
                        }
                } finally {
                        t.release();
                }
        }

        private void renderArtefactosEditor() {
                // -------------------- RENDERIZA OBJETOS FUERA DE LA ESCENA PROPIOS DEL EDITOR

                // ------------------------------------ CAJA DE SELECCION
                // ------------------------------------
                if (!entidadesSeleccionadas.isEmpty()) {

                        // La Matriz de vista es la inversa de la matriz de la camara.
                        // Esto es porque la camara siempre estara en el centro y movemos el mundo
                        // en direccion contraria a la camara.
                        QMatriz4 matrizVista = renderer.getCamara().getMatrizTransformacion(QGlobal.tiempo).invert();

                        // La matriz vistaModelo es el resultado de multiplicar la matriz de vista por
                        // la matriz del modelo
                        // De esta forma es la matriz que se usa para transformar el modelo a las
                        // coordenadas del mundo
                        // luego de estas coordenadas se transforma a las coordenadas de la camara
                        QMatriz4 matVistaModelo;

                        // La matriz modelo contiene la información del modelo
                        // Traslación, rotacion (en su propio eje ) y escala
                        QMatriz4 matrizModelo;

                        for (Entity entidadSeleccionado : entidadesSeleccionadas) {

                                // Matriz de modelo
                                // obtiene la matriz de informacion concatenada con los padres
                                matrizModelo = entidadSeleccionado.getMatrizTransformacion(QGlobal.tiempo);

                                // ------------------------------------------------------------
                                // MAtriz VistaModelo
                                // obtiene la matriz de transformacion del objeto combinada con la matriz de
                                // vision de la camara
                                matVistaModelo = matrizVista.mult(matrizModelo);

                                AABB tmp = null;
                                for (EntityComponent comp : entidadSeleccionado.getComponents()) {
                                        if (comp instanceof Mesh) {
                                                if (tmp == null) {
                                                        tmp = new AABB(((Mesh) comp).vertices[0].clone(),
                                                                        ((Mesh) comp).vertices[0].clone());
                                                }
                                                for (Vertex vertice : ((Mesh) comp).vertices) {
                                                        if (vertice.location.x < tmp.aabMinimo.location.x) {
                                                                tmp.aabMinimo.location.x = vertice.location.x;
                                                        }
                                                        if (vertice.location.y < tmp.aabMinimo.location.y) {
                                                                tmp.aabMinimo.location.y = vertice.location.y;
                                                        }
                                                        if (vertice.location.z < tmp.aabMinimo.location.z) {
                                                                tmp.aabMinimo.location.z = vertice.location.z;
                                                        }
                                                        if (vertice.location.x > tmp.aabMaximo.location.x) {
                                                                tmp.aabMaximo.location.x = vertice.location.x;
                                                        }
                                                        if (vertice.location.y > tmp.aabMaximo.location.y) {
                                                                tmp.aabMaximo.location.y = vertice.location.y;
                                                        }
                                                        if (vertice.location.z > tmp.aabMaximo.location.z) {
                                                                tmp.aabMaximo.location.z = vertice.location.z;
                                                        }
                                                }
                                        }
                                }

                                if (tmp != null) {
                                        // dibujo las esquinas del objeto seleccionado
                                        float dx = 0.2f * Math.abs(tmp.aabMinimo.location.x -
                                                        tmp.aabMaximo.location.x);
                                        float dy = 0.2f * Math.abs(tmp.aabMinimo.location.y -
                                                        tmp.aabMaximo.location.y);
                                        float dz = 0.2f * Math.abs(tmp.aabMinimo.location.z -
                                                        tmp.aabMaximo.location.z);

                                        TempVars t = TempVars.get();
                                        try {

                                                // ((QMaterialBas) polSeleccion.material).setColorDifusa(QColor.YELLOW);
                                                // superiores
                                                // 1
                                                t.vector4f1.set(tmp.aabMaximo.location.x, tmp.aabMaximo.location.y,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                QVector4 ps2 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y,
                                                                                t.vector4f1.z - dz, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                QVector4 ps3 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y - dy,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                QVector4 ps4 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x - dx, t.vector4f1.y,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());

                                                t.vector4f1.set(TransformationVectorUtil.transformarVector(t.vector4f1,
                                                                entidadSeleccionado, renderer.getCamara()));

                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps2);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps3);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps4);
                                                // 2
                                                t.vector4f1.set(tmp.aabMinimo.location.x, tmp.aabMaximo.location.y,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMaximo.location.w);
                                                ps2 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y,
                                                                                t.vector4f1.z - dz, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps3 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y - dy,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps4 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x + dx, t.vector4f1.y,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                t.vector4f1.set(TransformationVectorUtil.transformarVector(t.vector4f1,
                                                                entidadSeleccionado, renderer.getCamara()));
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps2);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps3);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps4);
                                                // 3
                                                t.vector4f1.set(tmp.aabMinimo.location.x, tmp.aabMaximo.location.y,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMaximo.location.w);
                                                ps2 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y,
                                                                                t.vector4f1.z + dz, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps3 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y - dy,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps4 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x + dx, t.vector4f1.y,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                t.vector4f1.set(TransformationVectorUtil.transformarVector(t.vector4f1,
                                                                entidadSeleccionado, renderer.getCamara()));
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps2);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps3);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps4);
                                                // 4
                                                t.vector4f1.set(tmp.aabMaximo.location.x, tmp.aabMaximo.location.y,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMaximo.location.w);
                                                ps2 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y,
                                                                                t.vector4f1.z + dz, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps3 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y - dy,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps4 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x - dx, t.vector4f1.y,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                t.vector4f1.set(TransformationVectorUtil.transformarVector(t.vector4f1,
                                                                entidadSeleccionado, renderer.getCamara()));
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps2);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps3);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps4);

                                                // ((QMaterialBas) polSeleccion.material).setColorDifusa(QColor.CYAN);
                                                // inferiores
                                                // 1
                                                t.vector4f1.set(tmp.aabMinimo.location.x, tmp.aabMinimo.location.y,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMinimo.location.w);
                                                ps2 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y,
                                                                                t.vector4f1.z + dz, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps3 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y + dy,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps4 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x + dx, t.vector4f1.y,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                t.vector4f1.set(TransformationVectorUtil.transformarVector(t.vector4f1,
                                                                entidadSeleccionado, renderer.getCamara()));
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps2);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps3);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps4);
                                                // 2
                                                t.vector4f1.set(tmp.aabMaximo.location.x, tmp.aabMinimo.location.y,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMinimo.location.w);
                                                ps2 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y,
                                                                                t.vector4f1.z + dz, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps3 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y + dy,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps4 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x - dx, t.vector4f1.y,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                t.vector4f1.set(TransformationVectorUtil.transformarVector(t.vector4f1,
                                                                entidadSeleccionado, renderer.getCamara()));
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps2);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps3);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps4);
                                                // 3
                                                t.vector4f1.set(tmp.aabMaximo.location.x, tmp.aabMinimo.location.y,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMinimo.location.w);
                                                ps2 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y,
                                                                                t.vector4f1.z - dz, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps3 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y + dy,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps4 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x - dx, t.vector4f1.y,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                t.vector4f1.set(TransformationVectorUtil.transformarVector(t.vector4f1,
                                                                entidadSeleccionado, renderer.getCamara()));
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps2);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps3);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps4);
                                                // 4
                                                t.vector4f1.set(tmp.aabMinimo.location.x, tmp.aabMinimo.location.y,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMinimo.location.w);
                                                ps2 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y,
                                                                                t.vector4f1.z - dz, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps3 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x, t.vector4f1.y + dy,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                ps4 = TransformationVectorUtil.transformarVector(
                                                                new QVector4(t.vector4f1.x + dx, t.vector4f1.y,
                                                                                t.vector4f1.z, 1),
                                                                entidadSeleccionado,
                                                                renderer.getCamara());
                                                t.vector4f1.set(TransformationVectorUtil.transformarVector(t.vector4f1,
                                                                entidadActiva,
                                                                renderer.getCamara()));
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps2);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps3);
                                                // renderer.renderLine(polSeleccion, t.vector4f1, ps4);
                                        } finally {
                                                t.release();
                                        }
                                }
                        }
                }

                // // ------------------------------------ GIZMOS
                // // ------------------------------------
                // // seteo los gizmos
                // if (entidadActiva != null && tipoGizmoActual != GIZMO_NINGUNO) {
                // t = TempVars.get();
                // try {
                // // ---- LIMPIO EL ZBUFFER PARA SOBREESCRIBIR
                // // limpio el zbuffer
                // frameBuffer.limpiarZBuffer();
                // switch (tipoGizmoActual) {
                // case GIZMO_TRASLACION:
                // default:
                // gizActual = gizTraslacion;
                // break;
                // case GIZMO_ROTACION:
                // gizActual = gizRotacion;
                // break;
                // case GIZMO_ESCALA:
                // gizActual = gizEscala;
                // break;
                // }
                // gizActual.setEntidad(entidadActiva);
                // gizActual.actualizarPosicionGizmo();

                // float gizmoSize = 0.06f;
                // float scale = (float) (gizmoSize *
                // (renderer.getCamara().getMatrizTransformacion(QGlobal.tiempo).toTranslationVector()

                // .add(gizActual.getMatrizTransformacion(QGlobal.tiempo).toTranslationVector().multiply(-1.0f))
                // .length() / Math.tan(renderer.getCamara().getFOV() / 2.0f)));
                // gizActual.scale(scale, scale, scale);
                // TransformationVectorUtil.transformar(gizActual, renderer.getCamara(),
                // t.bufferVertices1);
                // for (QPrimitiva poligono : t.bufferVertices1.getPoligonosTransformados())
                // {
                // renderer.raster(t.bufferVertices1, poligono, false);
                // }

                // for (QEntity hijo : gizActual.getChilds()) {
                // TransformationVectorUtil.transformar(hijo, renderer.getCamara(),
                // t.bufferVertices1);
                // for (QPrimitiva poligono : t.bufferVertices1.getPoligonosTransformados())
                // {
                // renderer.raster(t.bufferVertices1, poligono, false);
                // }
                // }

                // } finally {
                // t.release();
                // }
                // } else {
                // gizActual = null;
                // }

                // ------------------------------------ EJES
                // ------------------------------------
                // if (renderer.opciones.isDibujarGrid()) {
                // t = TempVars.get();
                // try {
                // TransformationVectorUtil.transformar(this.entidadOrigen,
                // renderer.getCamara(),
                // t.bufferVertices1,vertexShader);
                // for (QPrimitiva poligono : t.bufferVertices1.getPoligonosTransformados()) {
                // renderer.raster(t.bufferVertices1, poligono, false);
                // }
                // } catch (Exception e) {
                // e.printStackTrace();
                // } finally {
                // t.release();
                // }
                // }

        }
}
