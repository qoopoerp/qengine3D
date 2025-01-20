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
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlanarMesh;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.Matrix4;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.math.Vector4;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Axis;
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

        private Axis origen;

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
                matX.setEmissionIntensity(0.85f);

                matY = new Material("y");
                matY.setColor(QColor.GREEN);
                matY.setEmissionIntensity(0.85f);

                matZ = new Material("z");
                matZ.setColor(QColor.BLUE);
                matZ.setEmissionIntensity(0.85f);

                matGrid = new Material("grid");
                matGrid.setColor(QColor.LIGHT_GRAY);
                matGrid.setEmissionIntensity(0.5f);

                matSeleccion = new Material("matSeleccion");
                matSeleccion.setColor(QColor.YELLOW);
                matSeleccion.setEmissionIntensity(0.85f);
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
                matHueso.setEmissionIntensity(0.15f);
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
                this.origen = new Axis();

                createGridEntity();
        }

        public void render() {
                if (renderer.getFrameBuffer() != null) {
                        try {
                                Matrix4 matrizVista = renderer.getCamera().getMatrizTransformacion(QGlobal.time)
                                                .invert();
                                Matrix4 matrizVistaInvertidaBillboard = renderer.getCamera()
                                                .getMatrizTransformacion(QGlobal.time);

                                renderGrid(matrizVista, matrizVistaInvertidaBillboard);
                                // renderAxis(matrizVista, matrizVistaInvertidaBillboard);
                                renderSelectedBox(matrizVista, matrizVistaInvertidaBillboard);
                                renderSkeleton(matrizVista, matrizVistaInvertidaBillboard);
                                // limpio el zbuffer
                                renderer.getFrameBuffer().cleanZBuffer();
                                renderGizmos(matrizVista, matrizVistaInvertidaBillboard);
                        } catch (Exception e) {
                                e.printStackTrace();
                        } finally {
                                // renderer.shadeFragments();
                        }
                }
        }

        /**
         * Renderiza el origen
         */
        private void renderAxis(Matrix4 matrizVista, Matrix4 matrizVistaInvertidaBillboard) {
                if (renderer.opciones.isDibujarGrid()) {
                        render(origen, matrizVista, matrizVistaInvertidaBillboard);
                }
        }

        /**
         * Dibuja la grid del editor
         */
        private void renderGrid(Matrix4 matrizVista, Matrix4 matrizVistaInvertidaBillboard) {
                if (renderer.opciones.isDibujarGrid()) {
                        render(gridEntity, matrizVista, matrizVistaInvertidaBillboard);
                }
        }

        /**
         * Dibuja los esqueletos
         */
        private void renderSkeleton(Matrix4 matrizVista, Matrix4 matrizVistaInvertidaBillboard) {
                // ------------------------------------ ESQUELETOS
                // ------------------------------------
                // dibuja los esqueletos

                try {
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
        private void renderSelectedBox(Matrix4 matrizVista, Matrix4 matrizVistaInvertidaBillboard) {
                if (!entidadesSeleccionadas.isEmpty()) {

                        Matrix4 matVistaModelo;

                        // La matriz modelo contiene la información del modelo
                        // Traslación, rotacion (en su propio eje ) y escala
                        Matrix4 matrizModelo;

                        Vector3 normal = Vector3.unitario_y.clone();
                        Vector2 uv = new Vector2();

                        for (Entity entidadActiva : entidadesSeleccionadas) {
                                // Matriz de modelo
                                // obtiene la matriz de informacion concatenada con los padres
                                matrizModelo = entidadActiva.getMatrizTransformacion(QGlobal.time);

                                // ------------------------------------------------------------
                                // MAtriz VistaModelo
                                // obtiene la matriz de transformacion del objeto combinada con la matriz de
                                // vision de la camara
                                matVistaModelo = matrizVista.mult(matrizModelo);

                                AABB aabb = entidadActiva.getAABB();
                                if (aabb != null) {
                                        TempVars t = TempVars.get();
                                        try {
                                                Vector4[] vertices = aabb.getVertex();
                                                List<int[]> edges = aabb.getEdges();

                                                for (int[] edge : edges) {
                                                        t.vertex1.set(vertices[edge[0]]);
                                                        t.vertex2.set(vertices[edge[1]]);
                                                        renderer.renderLine(matVistaModelo, polSeleccion,
                                                                        vertexShader.apply(t.vertex1, normal, uv,
                                                                                        matSeleccion.getColor(),
                                                                                        matVistaModelo).getVertex(),
                                                                        vertexShader.apply(t.vertex2, normal, uv,
                                                                                        matSeleccion.getColor(),
                                                                                        matVistaModelo).getVertex());
                                                }
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
        private void renderGizmos(Matrix4 matrizVista, Matrix4 matrizVistaInvertidaBillboard) {
                if (!entidadesSeleccionadas.isEmpty() && tipoGizmoActual != GIZMO_NINGUNO) {
                        for (Entity entidadActiva : entidadesSeleccionadas) {
                                if (entidadActiva != null && tipoGizmoActual != GIZMO_NINGUNO) {
                                        try {
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
                                                currentGizmo.setEntity(entidadActiva);
                                                currentGizmo.updateLocationGizmo();

                                                float gizmoSize = 0.06f;
                                                float scale = (float) (gizmoSize *
                                                                (renderer.getCamera()
                                                                                .getMatrizTransformacion(QGlobal.time)
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

                                                render(currentGizmo, matrizVista, matrizVistaInvertidaBillboard);

                                        } catch (Exception e) {
                                                e.printStackTrace();
                                        } finally {

                                        }
                                }
                        }
                } else {
                        currentGizmo = null;
                }
        }

        /**
         * Renderiza un Entity y sus hijos en caso de tenerlos
         * 
         * @param entity
         * @param matrizVista
         * @param matrizVistaInvertidaBillboard
         */
        private void render(Entity entity, Matrix4 matrizVista, Matrix4 matrizVistaInvertidaBillboard) {
                renderer.renderEntity(entity, matrizVista,
                                matrizVistaInvertidaBillboard,
                                false);
                // y los hijos de cada gizmo que son los controladores
                for (Entity child : entity.getChilds()) {
                        render(child, matrizVista, matrizVistaInvertidaBillboard);
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
