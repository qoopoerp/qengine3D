package net.qoopo.engine3d.editor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.animation.Bone;
import net.qoopo.engine.core.entity.component.animation.Skeleton;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlanarMesh;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.QOrigen;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.TempVars;
import net.qoopo.engine.renderer.shader.fragment.FragmentShaderComponent;
import net.qoopo.engine.renderer.shader.fragment.basico.parciales.OnlyColorFragmentShader;
import net.qoopo.engine.renderer.shader.vertex.DefaultVertexShader;
import net.qoopo.engine.renderer.shader.vertex.VertexShader;
import net.qoopo.engine3d.core.input.control.gizmo.Gizmo;
import net.qoopo.engine3d.core.input.control.gizmo.transformation.move.MoveGizmo;
import net.qoopo.engine3d.core.input.control.gizmo.transformation.rotation.RotateGizmo;
import net.qoopo.engine3d.core.input.control.gizmo.transformation.scale.ScaleGizmo;

@Getter
@Setter
public class EditorRenderer {

        private static Logger log = Logger.getLogger("editor-renderer");
        private Gizmo currentGizmo;
        private Gizmo moveGizmo;
        private Gizmo scaleGizmo;
        private Gizmo rotateGizmo;

        private QOrigen origen;

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

        private static final Material matX;
        private static final Material matY;
        private static final Material matZ;
        private static final Material matGrid;
        private static final Material matSeleccion;

        private RenderEngine renderer;

        private VertexShader vertexShader = new DefaultVertexShader();

        private Entity gridEntity;

        static {
                matX = new Material("x");
                matX.setColor(QColor.RED);
                matX.setEmision(0.85f);

                matY = new Material("y");
                matY.setColor(QColor.GREEN);
                matY.setEmision(0.85f);

                matZ = new Material("z");
                matZ.setColor(QColor.BLUE);
                matZ.setEmision(0.85f);

                matGrid = new Material("grid");
                matGrid.setColor(QColor.LIGHT_GRAY);
                matGrid.setEmision(0.85f);

                matSeleccion = new Material("matSeleccion");
                matSeleccion.setColor(QColor.YELLOW);
                matSeleccion.setEmision(1.0f);
        }

        public EditorRenderer(RenderEngine renderEngine) {
                this.renderer = renderEngine;
                // creo una entity no existente en el escena con un material para poder tener
                // un primitiva para el dibujo de la seleccion
                // se deberia revisar el raster por esas limitaciones
                polSeleccion = new Poly(new Mesh());
                polSeleccion.material = matSeleccion;
                polHueso = new Poly(new Mesh());
                Material matHueso = new Material("matHueso");
                matHueso.setColor(QColor.GRAY);
                matHueso.setEmision(0.15f);
                polHueso.material = matHueso;
                polGrid = new Poly(new Mesh());
                polGrid.material = matGrid;
                polEjeX = new Poly(new Mesh());
                polEjeX.material = matX;
                polEjeY = new Poly(new Mesh());
                polEjeY.material = matY;
                polEjeZ = new Poly(new Mesh());
                polEjeZ.material = matZ;

                this.moveGizmo = new MoveGizmo();
                this.rotateGizmo = new RotateGizmo();
                this.scaleGizmo = new ScaleGizmo();
                this.origen = new QOrigen();

                createGridEntity();
        }

        public void render() {
                if (renderer.getFrameBuffer() != null) {
                        renderGrid();
                        renderSelectedBox();
                        renderGizmos();
                        // renderAxis();
                        renderSkeleton();
                }
        }

        /**
         * Renderiza el origen
         */
        private void renderAxis() {
                if (renderer.opciones.isDibujarGrid()) {

                        // La Matriz de vista es la inversa de la matriz de la camara.
                        // Esto es porque la camara siempre estara en el centro y movemos el mundo
                        // en direccion contraria a la camara.
                        QMatriz4 matrizVista = renderer.getCamera().getMatrizTransformacion(QGlobal.time).invert();
                        QMatriz4 matrizVistaInvertidaBillboard = renderer.getCamera()
                                        .getMatrizTransformacion(QGlobal.time);

                        renderer.renderEntity(origen, matrizVista, matrizVistaInvertidaBillboard, false);
                        renderer.renderEntity(origen, matrizVista, matrizVistaInvertidaBillboard, true);

                        // // La matriz vistaModelo es el resultado de multiplicar la matriz de vista
                        // por
                        // // la matriz del modelo
                        // // De esta forma es la matriz que se usa para transformar el modelo a las
                        // // coordenadas del mundo
                        // // luego de estas coordenadas se transforma a las coordenadas de la camara
                        // QMatriz4 matVistaModelo;

                        // // La matriz modelo contiene la información del modelo
                        // // Traslación, rotacion (en su propio eje ) y escala
                        // QMatriz4 matrizModelo;

                        // // Matriz de modelo
                        // // obtiene la matriz de informacion concatenada con los padres
                        // matrizModelo = origen.getMatrizTransformacion(QGlobal.time);

                        // // ------------------------------------------------------------
                        // // MAtriz VistaModelo
                        // // obtiene la matriz de transformacion del objeto combinada con la matriz de
                        // // vision de la camara
                        // matVistaModelo = matrizVista.mult(matrizModelo);

                        // QVector3 normal = QVector3.unitario_y.clone();
                        // QVector2 uv = new QVector2();
                        // QColor color = QColor.WHITE;

                        // TempVars t = TempVars.get();
                        // try {

                        // // ejes
                        // t.vertex1.set(origen.getTransformacion().getLocation().x,
                        // origen.getTransformacion().getLocation().y,
                        // origen.getTransformacion().getLocation().z,
                        // 1);

                        // t.vertex2.set(origen.getTransformacion().getLocation().x + 1,
                        // origen.getTransformacion().getLocation().y,
                        // origen.getTransformacion().getLocation().z,
                        // 1);

                        // t.vertex3.set(origen.getTransformacion().getLocation().x,
                        // origen.getTransformacion().getLocation().y + 1,
                        // origen.getTransformacion().getLocation().z,
                        // 1);

                        // t.vertex4.set(origen.getTransformacion().getLocation().x,
                        // origen.getTransformacion().getLocation().y,
                        // origen.getTransformacion().getLocation().z + 1,
                        // 1);

                        // renderer.renderLine(polEjeX,
                        // vertexShader.apply(t.vertex1, normal, uv, color,
                        // matVistaModelo),
                        // vertexShader.apply(t.vertex2, normal, uv, color,
                        // matVistaModelo));

                        // renderer.renderLine(polEjeY,
                        // vertexShader.apply(t.vertex1, normal, uv, color,
                        // matVistaModelo),
                        // vertexShader.apply(t.vertex3, normal, uv, color,
                        // matVistaModelo));

                        // renderer.renderLine(polEjeZ,
                        // vertexShader.apply(t.vertex1, normal, uv, color,
                        // matVistaModelo),
                        // vertexShader.apply(t.vertex4, normal, uv, color,
                        // matVistaModelo));

                        // } catch (Exception e) {
                        // e.printStackTrace();
                        // } finally {
                        // t.release();
                        // }
                }
        }

        /**
         * Dibuja la grid del editor
         */
        private void renderGrid() {
                if (renderer.opciones.isDibujarGrid()) {

                        // La Matriz de vista es la inversa de la matriz de la camara.
                        // Esto es porque la camara siempre estara en el centro y movemos el mundo
                        // en direccion contraria a la camara.
                        QMatriz4 matrizVista = renderer.getCamera().getMatrizTransformacion(QGlobal.time).invert();
                        QMatriz4 matrizVistaInvertidaBillboard = renderer.getCamera()
                                        .getMatrizTransformacion(QGlobal.time);
                        renderer.renderEntity(gridEntity, matrizVista, matrizVistaInvertidaBillboard, false);
                }
        }

        private void renderGridLinebyLine() {
                if (renderer.opciones.isDibujarGrid()) {

                        // La Matriz de vista es la inversa de la matriz de la camara.
                        // Esto es porque la camara siempre estara en el centro y movemos el mundo
                        // en direccion contraria a la camara.
                        QMatriz4 matrizVista = renderer.getCamera().getMatrizTransformacion(QGlobal.time).invert();

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
                        QVector3 normal = QVector3.unitario_y.clone();
                        QVector2 uv = new QVector2();
                        QColor color = QColor.WHITE;
                        try {
                                int secciones = 50;
                                float espacio = 1.0f;
                                float maxCoordenada = espacio * secciones / 2.0f;
                                // lineas paralelas en el eje de X
                                for (int i = 0; i <= secciones; i++) {
                                        t.vertex1.set((-secciones / 2 + i) * espacio, 0, maxCoordenada, 1);
                                        t.vertex2.set((-secciones / 2 + i) * espacio, 0, -maxCoordenada, 1);

                                        renderer.renderLine(matVistaModelo, polGrid,
                                                        vertexShader.apply(t.vertex1, normal, uv, color,
                                                                        matVistaModelo).getVertex(),
                                                        vertexShader.apply(t.vertex2, normal, uv, color,
                                                                        matVistaModelo).getVertex());
                                }
                                // lineas paralelas en el eje Z
                                for (int i = 0; i <= secciones; i++) {
                                        t.vertex1.set(maxCoordenada, 0, (-secciones / 2 + i) * espacio, 1);
                                        t.vertex2.set(-maxCoordenada, 0, (-secciones / 2 + i) * espacio, 1);

                                        renderer.renderLine(matVistaModelo, polGrid,
                                                        vertexShader.apply(t.vertex1, normal, uv, color,
                                                                        matVistaModelo).getVertex(),
                                                        vertexShader.apply(t.vertex2, normal, uv, color,
                                                                        matVistaModelo).getVertex());
                                }
                                // el eje de X
                                {
                                        t.vertex1.set(maxCoordenada, 0, 0.001f, 1);
                                        t.vertex2.set(-maxCoordenada, 0, 0.001f, 1);

                                        renderer.renderLine(matVistaModelo, polSeleccion,
                                                        vertexShader.apply(t.vertex1, normal, uv, color,
                                                                        matVistaModelo).getVertex(),
                                                        vertexShader.apply(t.vertex2, normal, uv, color,
                                                                        matVistaModelo).getVertex());
                                }
                                // el eje de Z
                                {
                                        t.vertex1.set(0, 0.001f, maxCoordenada, 1);
                                        t.vertex2.set(0, 0.001f, -maxCoordenada, 1);

                                        renderer.renderLine(matVistaModelo, polSeleccion,
                                                        vertexShader.apply(t.vertex1, normal, uv, color,
                                                                        matVistaModelo).getVertex(),
                                                        vertexShader.apply(t.vertex2, normal, uv, color,
                                                                        matVistaModelo).getVertex());
                                }
                        } catch (Exception e) {
                                e.printStackTrace();
                        } finally {
                                t.release();
                        }
                }
        }

        /**
         * Dibuja los esqueletos
         */
        private void renderSkeleton() {
                // ------------------------------------ ESQUELETOS
                // ------------------------------------
                // dibuja los esqueletos

                try {

                        QMatriz4 matrizVista = renderer.getCamera()
                                        .getMatrizTransformacion(QGlobal.time)
                                        .invert();
                        QMatriz4 matrizVistaInvertidaBillboard = renderer
                                        .getCamera()
                                        .getMatrizTransformacion(QGlobal.time);

                        Entity entidadTmp = null;
                        for (Entity entity : renderer.getScene().getEntities()) {
                                for (EntityComponent componente : entity.getComponents()) {
                                        if (componente instanceof Skeleton) {
                                                Skeleton esqueleto = (Skeleton) componente;
                                                if (esqueleto.isMostrar()) {
                                                        if (esqueleto.isSuperponer()) {
                                                                renderer.getFrameBuffer().cleanZBuffer();
                                                        }
                                                        List<Entity> boneList = new ArrayList<>();
                                                        // entity falsa usada para corregir la transformacion de la
                                                        // entity y mostrar
                                                        // los huesos acordes a esta transformacion
                                                        entidadTmp = new Entity();
                                                        entidadTmp.getTransform().fromMatrix(
                                                                        entity.getMatrizTransformacion(QGlobal.time));
                                                        // lista.add(entidadTmp);
                                                        for (Bone hueso : esqueleto.getBones()) {
                                                                // agrega al nodo invisible para usar la transformacion
                                                                // de la entity y mostrar
                                                                // correctamente
                                                                // sin embargo da el error que nou sa la pose sin
                                                                // animacion
                                                                if (hueso.getParent() == null) {
                                                                        Bone hueson = hueso.clone();
                                                                        entidadTmp.addChild(hueson);
                                                                        hueson.toLista(boneList);
                                                                }
                                                        }
                                                        for (Entity bone : boneList) {
                                                                renderer.renderEntity(bone, matrizVista,
                                                                                matrizVistaInvertidaBillboard, false);
                                                        }
                                                }
                                        }
                                }
                        }
                } finally {

                }
        }

        /**
         * Dibuja la caja de seleccion sobre los objetos seleccionados
         */
        private void renderSelectedBox() {
                // ------------------------------------ CAJA DE SELECCION
                // ------------------------------------
                if (!entidadesSeleccionadas.isEmpty()) {

                        // La Matriz de vista es la inversa de la matriz de la camara.
                        // Esto es porque la camara siempre estara en el centro y movemos el mundo
                        // en direccion contraria a la camara.
                        QMatriz4 matrizVista = renderer.getCamera().getMatrizTransformacion(QGlobal.time).invert();

                        // La matriz vistaModelo es el resultado de multiplicar la matriz de vista por
                        // la matriz del modelo
                        // De esta forma es la matriz que se usa para transformar el modelo a las
                        // coordenadas del mundo
                        // luego de estas coordenadas se transforma a las coordenadas de la camara
                        QMatriz4 matVistaModelo;

                        // La matriz modelo contiene la información del modelo
                        // Traslación, rotacion (en su propio eje ) y escala
                        QMatriz4 matrizModelo;

                        QVector3 normal = QVector3.unitario_y.clone();
                        QVector2 uv = new QVector2();
                        QColor color = QColor.WHITE;

                        for (Entity entidadActiva : entidadesSeleccionadas) {

                                // Matriz de modelo
                                // obtiene la matriz de informacion concatenada con los padres
                                matrizModelo = entidadActiva.getMatrizTransformacion(QGlobal.time);

                                // ------------------------------------------------------------
                                // MAtriz VistaModelo
                                // obtiene la matriz de transformacion del objeto combinada con la matriz de
                                // vision de la camara
                                matVistaModelo = matrizVista.mult(matrizModelo);

                                AABB tmp = null;
                                for (EntityComponent comp : entidadActiva.getComponents()) {
                                        if (comp instanceof Mesh) {
                                                if (tmp == null) {
                                                        tmp = new AABB(((Mesh) comp).vertexList[0].clone(),
                                                                        ((Mesh) comp).vertexList[0].clone());
                                                }
                                                for (Vertex vertice : ((Mesh) comp).vertexList) {
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

                                                // 1
                                                t.vertex1.set(tmp.aabMaximo.location.x, tmp.aabMaximo.location.y,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                t.vertex2.set(tmp.aabMaximo.location.x, tmp.aabMaximo.location.y,
                                                                tmp.aabMaximo.location.z - dz,
                                                                tmp.aabMaximo.location.w);

                                                t.vertex3.set(tmp.aabMaximo.location.x, tmp.aabMaximo.location.y - dy,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                t.vertex4.set(tmp.aabMaximo.location.x - dx, tmp.aabMaximo.location.y,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                // t.vector4f1.set(TransformationVectorUtil.transformarVector(t.vector4f1,
                                                // entidadSeleccionado, renderer.getCamara()));

                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex2, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex3, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex4, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                // 2

                                                t.vertex1.set(tmp.aabMinimo.location.x, tmp.aabMaximo.location.y,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                t.vertex2.set(tmp.aabMinimo.location.x, tmp.aabMaximo.location.y,
                                                                tmp.aabMaximo.location.z - dz,
                                                                tmp.aabMaximo.location.w);

                                                t.vertex3.set(tmp.aabMinimo.location.x, tmp.aabMaximo.location.y - dy,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                t.vertex4.set(tmp.aabMinimo.location.x + dx, tmp.aabMaximo.location.y,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex2, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex3, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex4, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                // 3
                                                t.vertex1.set(tmp.aabMinimo.location.x, tmp.aabMaximo.location.y,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                t.vertex2.set(tmp.aabMinimo.location.x, tmp.aabMaximo.location.y,
                                                                tmp.aabMinimo.location.z + dz,
                                                                tmp.aabMaximo.location.w);

                                                t.vertex3.set(tmp.aabMinimo.location.x, tmp.aabMaximo.location.y - dy,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                t.vertex4.set(tmp.aabMinimo.location.x + dx, tmp.aabMaximo.location.y,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex2, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex3, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex4, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                // 4

                                                t.vertex1.set(tmp.aabMaximo.location.x, tmp.aabMaximo.location.y,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                t.vertex2.set(tmp.aabMaximo.location.x, tmp.aabMaximo.location.y,
                                                                tmp.aabMinimo.location.z + dz,
                                                                tmp.aabMaximo.location.w);

                                                t.vertex3.set(tmp.aabMaximo.location.x, tmp.aabMaximo.location.y - dy,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                t.vertex4.set(tmp.aabMaximo.location.x - dx, tmp.aabMaximo.location.y,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMaximo.location.w);

                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex2, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex3, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex4, normal, uv, color,
                                                                                matVistaModelo).getVertex());

                                                // ((QMaterialBas) polSeleccion.material).setColorDifusa(QColor.CYAN);
                                                // inferiores
                                                // 1

                                                t.vertex1.set(tmp.aabMinimo.location.x, tmp.aabMinimo.location.y,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMinimo.location.w);

                                                t.vertex2.set(tmp.aabMinimo.location.x, tmp.aabMinimo.location.y,
                                                                tmp.aabMinimo.location.z + dz,
                                                                tmp.aabMinimo.location.w);

                                                t.vertex3.set(tmp.aabMinimo.location.x, tmp.aabMinimo.location.y + dy,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMinimo.location.w);

                                                t.vertex4.set(tmp.aabMinimo.location.x + dx, tmp.aabMinimo.location.y,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMinimo.location.w);

                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex2, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex3, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex4, normal, uv, color,
                                                                                matVistaModelo).getVertex());

                                                // 2

                                                t.vertex1.set(tmp.aabMaximo.location.x, tmp.aabMinimo.location.y,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMinimo.location.w);

                                                t.vertex2.set(tmp.aabMaximo.location.x, tmp.aabMinimo.location.y,
                                                                tmp.aabMinimo.location.z + dz,
                                                                tmp.aabMinimo.location.w);

                                                t.vertex3.set(tmp.aabMaximo.location.x, tmp.aabMinimo.location.y + dy,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMinimo.location.w);

                                                t.vertex4.set(tmp.aabMaximo.location.x - dx, tmp.aabMinimo.location.y,
                                                                tmp.aabMinimo.location.z,
                                                                tmp.aabMinimo.location.w);

                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex2, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex3, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex4, normal, uv, color,
                                                                                matVistaModelo).getVertex());

                                                // 3

                                                t.vertex1.set(tmp.aabMaximo.location.x, tmp.aabMinimo.location.y,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMinimo.location.w);

                                                t.vertex2.set(tmp.aabMaximo.location.x, tmp.aabMinimo.location.y,
                                                                tmp.aabMaximo.location.z - dz,
                                                                tmp.aabMinimo.location.w);

                                                t.vertex3.set(tmp.aabMaximo.location.x, tmp.aabMinimo.location.y + dy,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMinimo.location.w);

                                                t.vertex4.set(tmp.aabMaximo.location.x - dx, tmp.aabMinimo.location.y,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMinimo.location.w);

                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex2, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex3, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex4, normal, uv, color,
                                                                                matVistaModelo).getVertex());

                                                // 4

                                                t.vertex1.set(tmp.aabMinimo.location.x, tmp.aabMinimo.location.y,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMinimo.location.w);

                                                t.vertex2.set(tmp.aabMinimo.location.x, tmp.aabMinimo.location.y,
                                                                tmp.aabMaximo.location.z - dz,
                                                                tmp.aabMinimo.location.w);

                                                t.vertex3.set(tmp.aabMinimo.location.x, tmp.aabMinimo.location.y + dy,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMinimo.location.w);

                                                t.vertex4.set(tmp.aabMinimo.location.x + dx, tmp.aabMinimo.location.y,
                                                                tmp.aabMaximo.location.z,
                                                                tmp.aabMinimo.location.w);

                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex2, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex3, normal, uv, color,
                                                                                matVistaModelo).getVertex());
                                                renderer.renderLine(matVistaModelo, polSeleccion,
                                                                vertexShader.apply(t.vertex1, normal, uv, color,
                                                                                matVistaModelo).getVertex(),
                                                                vertexShader.apply(t.vertex4, normal, uv, color,
                                                                                matVistaModelo).getVertex());

                                        } finally {
                                                t.release();
                                        }
                                }
                        }
                }
        }

        /**
         * Renderiza los gizmos
         */
        private void renderGizmos() {

                if (!entidadesSeleccionadas.isEmpty() && tipoGizmoActual != GIZMO_NINGUNO) {
                        for (Entity entidadActiva : entidadesSeleccionadas) {
                                // if (entidadActiva != null && tipoGizmoActual != GIZMO_NINGUNO) {

                                // La Matriz de vista es la inversa de la matriz de la camara.
                                // Esto es porque la camara siempre estara en el centro y movemos el mundo
                                // en direccion contraria a la camara.
                                QMatriz4 matrizVista = renderer.getCamera().getMatrizTransformacion(QGlobal.time)
                                                .invert();
                                QMatriz4 matrizVistaInvertidaBillboard = renderer.getCamera()
                                                .getMatrizTransformacion(QGlobal.time);

                                // La matriz vistaModelo es el resultado de multiplicar la matriz de vista por
                                // la matriz del modelo
                                // De esta forma es la matriz que se usa para transformar el modelo a las
                                // coordenadas del mundo
                                // luego de estas coordenadas se transforma a las coordenadas de la camara
                                QMatriz4 matVistaModelo;

                                // La matriz modelo contiene la información del modelo
                                // Traslación, rotacion (en su propio eje ) y escala
                                QMatriz4 matrizModelo;

                                // Matriz de modelo
                                // obtiene la matriz de informacion concatenada con los padres
                                matrizModelo = entidadActiva.getMatrizTransformacion(QGlobal.time);

                                // ------------------------------------------------------------
                                // MAtriz VistaModelo
                                // obtiene la matriz de transformacion del objeto combinada con la matriz de
                                // vision de la camara
                                matVistaModelo = matrizVista.mult(matrizModelo);

                                QVector3 normal = QVector3.unitario_y.clone();
                                QVector2 uv = new QVector2();
                                QColor color = QColor.WHITE;

                                // TempVars t = TempVars.get();
                                try {
                                        // ---- LIMPIO EL ZBUFFER PARA SOBREESCRIBIR
                                        // limpio el zbuffer
                                        // renderer.getFrameBuffer().cleanZBuffer();
                                        switch (tipoGizmoActual) {
                                                case GIZMO_TRASLACION:
                                                default:
                                                        currentGizmo = moveGizmo;
                                                        break;
                                                case GIZMO_ROTACION:
                                                        currentGizmo = rotateGizmo;
                                                        break;
                                                case GIZMO_ESCALA:
                                                        currentGizmo = scaleGizmo;
                                                        break;
                                        }
                                        currentGizmo.setEntidad(entidadActiva);
                                        currentGizmo.updateLocationGizmo();

                                        float gizmoSize = 0.06f;
                                        float scale = (float) (gizmoSize *
                                                        (renderer.getCamera().getMatrizTransformacion(QGlobal.time)
                                                                        .toTranslationVector()
                                                                        .add(currentGizmo
                                                                                        .getMatrizTransformacion(
                                                                                                        QGlobal.time)
                                                                                        .toTranslationVector()
                                                                                        .multiply(-1.0f))
                                                                        .length()
                                                                        / Math.tan(renderer.getCamera().getFOV()
                                                                                        / 2.0f)));
                                        currentGizmo.scale(scale, scale, scale);

                                        renderer.renderEntity(currentGizmo, matrizVista, matrizVistaInvertidaBillboard,
                                                        false);
                                        renderer.renderEntity(currentGizmo, matrizVista, matrizVistaInvertidaBillboard,
                                                        true);

                                        // // ejes
                                        // t.vertex1.set(currentGizmo.getTransformacion().getLocation().x,
                                        // currentGizmo.getTransformacion().getLocation().y,
                                        // currentGizmo.getTransformacion().getLocation().z,
                                        // 1);

                                        // t.vertex2.set(currentGizmo.getTransformacion().getLocation().x + 1,
                                        // currentGizmo.getTransformacion().getLocation().y,
                                        // currentGizmo.getTransformacion().getLocation().z,
                                        // 1);

                                        // t.vertex3.set(currentGizmo.getTransformacion().getLocation().x,
                                        // currentGizmo.getTransformacion().getLocation().y + 1,
                                        // currentGizmo.getTransformacion().getLocation().z,
                                        // 1);

                                        // t.vertex4.set(currentGizmo.getTransformacion().getLocation().x,
                                        // currentGizmo.getTransformacion().getLocation().y,
                                        // currentGizmo.getTransformacion().getLocation().z + 1,
                                        // 1);

                                        // renderer.renderLine(matVistaModelo, polEjeX,
                                        // vertexShader.apply(t.vertex1, normal, uv, color,
                                        // matVistaModelo),
                                        // vertexShader.apply(t.vertex2, normal, uv, color,
                                        // matVistaModelo));

                                        // renderer.renderLine(matVistaModelo, polEjeY,
                                        // vertexShader.apply(t.vertex1, normal, uv, color,
                                        // matVistaModelo),
                                        // vertexShader.apply(t.vertex3, normal, uv, color,
                                        // matVistaModelo));

                                        // renderer.renderLine(matVistaModelo, polEjeZ,
                                        // vertexShader.apply(t.vertex1, normal, uv, color,
                                        // matVistaModelo),
                                        // vertexShader.apply(t.vertex4, normal, uv, color,
                                        // matVistaModelo));
                                } catch (Exception e) {
                                        e.printStackTrace();
                                } finally {
                                        // t.release();
                                }
                        }
                } else {
                        currentGizmo = null;
                }
        }

        /**
         * Crea la entidad que representa a la grid
         */
        private void createGridEntity() {
                gridEntity = new Entity("grid");
                Mesh gridMesh = new Mesh();
                try {
                        gridMesh = new PlanarMesh(false, true, 50, 50, 50, 50);
                        gridMesh.applyMaterial(matGrid);
                        // int secciones = 50;
                        // float espacio = 1.0f;
                        // float maxCoordenada = espacio * secciones / 2.0f;
                        // // lineas paralelas en el eje de X
                        // int index = 0;
                        // for (int i = 0; i <= secciones; i++) {
                        // gridMesh.addVertex((-secciones / 2 + i) * espacio, 0.0f, maxCoordenada);
                        // gridMesh.addVertex((-secciones / 2 + i) * espacio, 0.0f, -maxCoordenada);
                        // gridMesh.addLine(index, index + 1);
                        // index++;
                        // }
                        // // lineas paralelas en el eje Z
                        // for (int i = 0; i <= secciones; i++) {
                        // gridMesh.addVertex(maxCoordenada, 0, (-secciones / 2 + i) * espacio);
                        // gridMesh.addVertex(-maxCoordenada, 0, (-secciones / 2 + i) * espacio);
                        // gridMesh.addLine(index, index + 1);
                        // index++;
                        // }

                        // gridMesh.applyMaterial(matGrid);

                        // // el eje de X
                        // {
                        // gridMesh.addVertex(maxCoordenada, 0, 0.001f);
                        // gridMesh.addVertex(-maxCoordenada, 0, 0.001f);
                        // gridMesh.addLine(matX, index, index + 1);
                        // index++;
                        // }
                        // // el eje de Z
                        // {
                        // gridMesh.addVertex(0, 0.001f, maxCoordenada);
                        // gridMesh.addVertex(0, 0.001f, -maxCoordenada);
                        // gridMesh.addLine(matZ, index, index + 1);
                        // index++;
                        // }

                } catch (Exception e) {
                        e.printStackTrace();
                } finally {
                        gridEntity.addComponent(gridMesh);
                        gridEntity.addComponent(new FragmentShaderComponent(new OnlyColorFragmentShader(null)));
                }
        }
}
