/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.core.input.control.gizmo.transformation.scale;

import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.assets.model.waveobject.LoadModelObj;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.ComponentUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine3d.core.input.control.gizmo.Gizmo;

/**
 *
 * @author alberto
 */
public class ScaleGizmo extends Gizmo {

    // private static final List<QEntity> ent =
    // LoadModelObj.cargarWaveObject(QGizmoTraslacion.class.getResourceAsStream("/gizmos/Giz_esc.obj"));
    private static final Mesh formaY;// = QUtilComponentes.getMesh(ent.get(0));
    private static final Mesh formaX;// = QUtilComponentes.getMesh(ent.get(1));
    private static final Mesh formaZ;// = QUtilComponentes.getMesh(ent.get(2));
    private static final Material matX;
    private static final Material matY;
    private static final Material matZ;

    static {

        ModelLoader loadModel = new LoadModelObj();
        Entity ent = loadModel.loadModel(ScaleGizmo.class.getResourceAsStream("/gizmos/Giz_esc.obj"));

        formaY = ComponentUtil.getMesh(ent.getChilds().get(0));
        formaX = ComponentUtil.getMesh(ent.getChilds().get(1));
        formaZ = ComponentUtil.getMesh(ent.getChilds().get(2));

        matX = new Material("x");
        matX.setColor(QColor.RED);
        matX.setEmision(0.85f);
        // matX.setTransparencia(true);
        // matX.setTransAlfa(0.9f);
        MaterialUtil.applyMaterial(formaX, matX);

        matY = new Material("y");
        matY.setColor(QColor.GREEN);
        matY.setEmision(0.85f);
        // matY.setTransparencia(true);
        // matY.setTransAlfa(0.9f);
        MaterialUtil.applyMaterial(formaY, matY);

        matZ = new Material("z");
        matZ.setColor(QColor.BLUE);
        matZ.setEmision(0.85f);
        // matZ.setTransparencia(true);
        // matZ.setTransAlfa(0.9f);
        MaterialUtil.applyMaterial(formaZ, matZ);
    }

    public ScaleGizmo() {
        addChild(crearControladorX());
        addChild(crearControladorY());
        addChild(crearControladorZ());
        addChild(crearControladorTodos());
    }

    private Gizmo crearControladorTodos() {
        Gizmo todos = new Gizmo() {
            @Override
            public void mouseMove(float deltaX, float deltaY) {
                ScaleGizmo.this.entity.aumentarEscalaX(getDelta(deltaX, deltaY));
                ScaleGizmo.this.entity.aumentarEscalaY(getDelta(deltaX, deltaY));
                ScaleGizmo.this.entity.aumentarEscalaZ(getDelta(deltaX, deltaY));
                updateLocationGizmo();
            }
        };

        Material matTodos = new Material("x");
        matTodos.setColor(QColor.WHITE);
        // matTodos.setTransparencia(true);
        // matTodos.setTransAlfa(0.75f);
        matTodos.setEmision(.85f);
        todos.addComponent(MaterialUtil.applyMaterial(new Box(0.85f), matTodos));

        return todos;
    }

    private Gizmo crearControladorX() {
        Gizmo conX = new Gizmo() {
            @Override
            public void mouseMove(float deltaX, float deltaY) {
                ScaleGizmo.this.entity.aumentarEscalaX(deltaX / 10);
                updateLocationGizmo();
            }
        };

        conX.addComponent(formaX);

        return conX;
    }

    private Gizmo crearControladorY() {
        Gizmo conY = new Gizmo() {
            @Override
            public void mouseMove(float deltaX, float deltaY) {
                ScaleGizmo.this.entity.aumentarEscalaY(getDelta(deltaX, deltaY));
                updateLocationGizmo();
            }
        };
        conY.addComponent(formaY);
        return conY;
    }

    private Gizmo crearControladorZ() {
        Gizmo conZ = new Gizmo() {
            @Override
            public void mouseMove(float deltaX, float deltaY) {
                ScaleGizmo.this.entity.aumentarEscalaZ(getDelta(deltaX, deltaY));
                updateLocationGizmo();
            }
        };

        conZ.addComponent(formaZ);
        return conZ;
    }

    @Override
    public void updateLocationGizmo() {
        try {
            if (entity != null) {
                // actualizo posicion y rotacion
                this.transformacion.getTraslacion()
                        .set(entity.getMatrizTransformacion(QGlobal.time).toTranslationVector());
                this.transformacion.getRotacion()
                        .setCuaternion(entity.getMatrizTransformacion(QGlobal.time).toRotationQuat());
                this.transformacion.getRotacion().actualizarAngulos();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void mouseMove(float deltaX, float deltaY) {

    }
}
