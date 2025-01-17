/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.core.input.control.gizmo.transformation.move;

import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.assets.model.waveobject.LoadModelObj;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlaneTwoSided;
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
public class MoveGizmo extends Gizmo {

    private static final Mesh formaY;// = QUtilComponentes.getMesh(ent.get(0));
    private static final Mesh formaX;// = QUtilComponentes.getMesh(ent.get(1));
    private static final Mesh formaZ;// =QUtilComponentes.getMesh(ent.get(2));
    private static final Material matX;
    private static final Material matY;
    private static final Material matZ;

    static {
        ModelLoader loadModel = new LoadModelObj();
        Entity ent = loadModel.loadModel(MoveGizmo.class.getResourceAsStream("/gizmos/Giz_mov.obj"));

        formaY = ComponentUtil.getMesh(ent.getChilds().get(0));
        formaX = ComponentUtil.getMesh(ent.getChilds().get(1));
        formaZ = ComponentUtil.getMesh(ent.getChilds().get(2));

        matX = new Material("x");
        matX.setColor(QColor.RED);
        matX.setEmissionIntensity(0.5f);
        MaterialUtil.applyMaterial(formaX, matX);
        matY = new Material("y");
        matY.setColor(QColor.GREEN);
        matY.setEmissionIntensity(0.5f);
        MaterialUtil.applyMaterial(formaY, matY);
        matZ = new Material("z");
        matZ.setColor(QColor.BLUE);
        matZ.setEmissionIntensity(0.5f);
        MaterialUtil.applyMaterial(formaZ, matZ);
    }

    public MoveGizmo() {
        addChild(crearControladorX());
        addChild(crearControladorY());
        addChild(crearControladorZ());
        // planos
        addChild(crearControladorPlanoXZ());
        addChild(crearControladorPlanoXY());
        addChild(crearControladorPlanoZY());
    }

    private Gizmo crearControladorX() {
        Gizmo conX = new Gizmo() {
            @Override
            public void mouseMove(float deltaX, float deltaY) {
                MoveGizmo.this.entity.aumentarX(getDelta(deltaX, deltaY));
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
                MoveGizmo.this.entity.aumentarY(getDelta(deltaX, deltaY));
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
                MoveGizmo.this.entity.aumentarZ(getDelta(deltaX, deltaY));
                updateLocationGizmo();
            }
        };
        conZ.addComponent(formaZ);
        return conZ;
    }

    private Gizmo crearControladorPlanoXZ() {
        Gizmo conXZ = new Gizmo() {
            @Override
            public void mouseMove(float deltaX, float deltaY) {
                MoveGizmo.this.entity.aumentarX(getDelta(deltaX));
                MoveGizmo.this.entity.aumentarZ(getDelta(deltaY));
                updateLocationGizmo();
            }
        };
        conXZ.move(0.5f, 0, 0.5f);
        conXZ.addComponent(MaterialUtil.applyMaterial(new PlaneTwoSided(0.5f, 0.5f), matY));
        return conXZ;
    }

    private Gizmo crearControladorPlanoXY() {
        Gizmo conXY = new Gizmo() {
            @Override
            public void mouseMove(float deltaX, float deltaY) {
                MoveGizmo.this.entity.aumentarX(getDelta(deltaX));
                MoveGizmo.this.entity.aumentarY(getDelta(deltaY));
                updateLocationGizmo();
            }
        };
        conXY.move(0.5f, 0.5f, 0);
        conXY.rotate(Math.toRadians(90), 0, 0);
        conXY.addComponent(MaterialUtil.applyMaterial(new PlaneTwoSided(0.5f, 0.5f), matZ));
        return conXY;
    }

    private Gizmo crearControladorPlanoZY() {
        Gizmo conZY = new Gizmo() {
            @Override
            public void mouseMove(float deltaX, float deltaY) {
                MoveGizmo.this.entity.aumentarZ(getDelta(deltaX));
                MoveGizmo.this.entity.aumentarY(getDelta(deltaY));
                updateLocationGizmo();
            }
        };
        conZY.move(0, 0.5f, 0.5f);
        conZY.rotate(0, 0, Math.toRadians(90));
        conZY.addComponent(MaterialUtil.applyMaterial(new PlaneTwoSided(0.5f, 0.5f), matX));
        return conZY;
    }

    @Override
    public void mouseMove(float deltaX, float deltaY) {

    }

    @Override
    public void updateLocationGizmo() {
        try {
            if (entity != null) {
                this.transform.getLocation()
                        .set(entity.getMatrizTransformacion(QGlobal.time).toTranslationVector());
            }
        } catch (Exception e) {
        }
    }

}
