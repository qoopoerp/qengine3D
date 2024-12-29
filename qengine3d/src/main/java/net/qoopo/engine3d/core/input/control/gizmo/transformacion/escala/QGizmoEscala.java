/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.core.input.control.gizmo.transformacion.escala;

import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.assets.model.waveobject.LoadModelObj;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.QUtilComponentes;
import net.qoopo.engine3d.core.input.control.gizmo.QGizmo;

/**
 *
 * @author alberto
 */
public class QGizmoEscala extends QGizmo {

    // private static final List<QEntity> ent =
    // LoadModelObj.cargarWaveObject(QGizmoTraslacion.class.getResourceAsStream("/gizmos/Giz_esc.obj"));
    private static final Mesh formaY;// = QUtilComponentes.getMesh(ent.get(0));
    private static final Mesh formaX;// = QUtilComponentes.getMesh(ent.get(1));
    private static final Mesh formaZ;// = QUtilComponentes.getMesh(ent.get(2));
    private static final QMaterialBas matX;
    private static final QMaterialBas matY;
    private static final QMaterialBas matZ;

    static {

        ModelLoader loadModel = new LoadModelObj();
        Entity ent = loadModel.loadModel(QGizmoEscala.class.getResourceAsStream("/gizmos/Giz_esc.obj"));

        formaY = QUtilComponentes.getMesh(ent.getChilds().get(0));
        formaX = QUtilComponentes.getMesh(ent.getChilds().get(1));
        formaZ = QUtilComponentes.getMesh(ent.getChilds().get(2));

        matX = new QMaterialBas("x");
        matX.setColorBase(QColor.RED);
        matX.setFactorEmision(0.85f);
        // matX.setTransparencia(true);
        // matX.setTransAlfa(0.9f);
        MaterialUtil.applyMaterial(formaX, matX);

        matY = new QMaterialBas("y");
        matY.setColorBase(QColor.GREEN);
        matY.setFactorEmision(0.85f);
        // matY.setTransparencia(true);
        // matY.setTransAlfa(0.9f);
        MaterialUtil.applyMaterial(formaY, matY);

        matZ = new QMaterialBas("z");
        matZ.setColorBase(QColor.BLUE);
        matZ.setFactorEmision(0.85f);
        // matZ.setTransparencia(true);
        // matZ.setTransAlfa(0.9f);
        MaterialUtil.applyMaterial(formaZ, matZ);
    }

    public QGizmoEscala() {
        addChild(crearControladorX());
        addChild(crearControladorY());
        addChild(crearControladorZ());
        addChild(crearControladorTodos());
    }

    private QGizmo crearControladorTodos() {
        QGizmo todos = new QGizmo() {
            @Override
            public void mouseMove(float deltaX, float deltaY) {
                QGizmoEscala.this.entity.aumentarEscalaX(getDelta(deltaX, deltaY));
                QGizmoEscala.this.entity.aumentarEscalaY(getDelta(deltaX, deltaY));
                QGizmoEscala.this.entity.aumentarEscalaZ(getDelta(deltaX, deltaY));
                actualizarPosicionGizmo();
            }
        };

        QMaterialBas matTodos = new QMaterialBas("x");
        matTodos.setColorBase(QColor.WHITE);
        // matTodos.setTransparencia(true);
        // matTodos.setTransAlfa(0.75f);
        matTodos.setFactorEmision(.85f);
        todos.addComponent(MaterialUtil.applyMaterial(new Box(0.85f), matTodos));

        return todos;
    }

    private QGizmo crearControladorX() {
        QGizmo conX = new QGizmo() {
            @Override
            public void mouseMove(float deltaX, float deltaY) {
                QGizmoEscala.this.entity.aumentarEscalaX(deltaX / 10);
                actualizarPosicionGizmo();
            }
        };

        conX.addComponent(formaX);

        return conX;
    }

    private QGizmo crearControladorY() {
        QGizmo conY = new QGizmo() {
            @Override
            public void mouseMove(float deltaX, float deltaY) {
                QGizmoEscala.this.entity.aumentarEscalaY(getDelta(deltaX, deltaY));
                actualizarPosicionGizmo();
            }
        };
        conY.addComponent(formaY);
        return conY;
    }

    private QGizmo crearControladorZ() {
        QGizmo conZ = new QGizmo() {
            @Override
            public void mouseMove(float deltaX, float deltaY) {
                QGizmoEscala.this.entity.aumentarEscalaZ(getDelta(deltaX, deltaY));
                actualizarPosicionGizmo();
            }
        };

        conZ.addComponent(formaZ);
        return conZ;
    }

    @Override
    public void actualizarPosicionGizmo() {
        try {
            if (entity != null) {
                // actualizo posicion y rotacion
                this.transformacion.getTraslacion()
                        .set(entity.getMatrizTransformacion(QGlobal.tiempo).toTranslationVector());
                this.transformacion.getRotacion()
                        .setCuaternion(entity.getMatrizTransformacion(QGlobal.tiempo).toRotationQuat());
                this.transformacion.getRotacion().actualizarAngulos();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void mouseMove(float deltaX, float deltaY) {

    }
}
