/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.animaciones.esqueletica;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.ComponentUtil;
import net.qoopo.engine3d.QEngine3D;
import net.qoopo.engine3d.core.util.SerializarUtil;

/**
 *
 * @author alberto
 */
public class MonstruosDoom {

    public static void main(String[] args) {
        QEngine3D motor = new QEngine3D();

        Camera cam = new Camera();
        cam.frustrumLejos = 1000;
        cam.lookAtTarget(QVector3.of(250, 250, 250), QVector3.zero, QVector3.unitario_y.clone());
        motor.getScene().addEntity(cam);
        motor.configRenderer(800, 600, cam);
        motor.getRenderEngine().opciones.setDibujarLuces(false);
        motor.getRenderEngine().setCargando(true);
        motor.setIniciarAudio(false);
        motor.setIniciarFisica(false);
        motor.setIniciarDiaNoche(false);
        motor.exitonEsc();
        motor.start();

        // carga las animaciones
        Entity doomMonster = (Entity) SerializarUtil.leerObjeto("assets/models/qengine/doom/hellknight.qengine", 0, true);
        doomMonster.move(-100, 0, 0);
        motor.getScene().addEntity(doomMonster);

        Entity quake1 = (Entity) SerializarUtil.leerObjeto("assets/models/qengine/quake/qdemon.qengine", 0, true);
        quake1.move(100, 0, 0);
        quake1.addComponent(ComponentUtil.getAlmacenAnimaciones(quake1).getAnimacion("idle1"));
        motor.getScene().addEntity(quake1);

        Entity quake2 = (Entity) SerializarUtil.leerObjeto("assets/models/qengine/quake/qshambler.qengine", 0, true);
        quake2.move(0, 0, -100);
        quake2.addComponent(ComponentUtil.getAlmacenAnimaciones(quake2).getAnimacion("idle02"));
        motor.getScene().addEntity(quake2);

        Entity quake3 = (Entity) SerializarUtil.leerObjeto("assets/models/qengine/quake/qwizard.qengine", 0, true);
        quake3.move(0, 40, 100);
        quake3.addComponent(ComponentUtil.getAlmacenAnimaciones(quake3).getAnimacion("idle"));
        motor.getScene().addEntity(quake3);

        QLigth sol = new QDirectionalLigth(1f, QColor.WHITE, 200, QVector3.of(-0.5f, -1f, 0.5f), true, true);
        sol.setProyectarSombras(true);
        Entity luzEntidad = new Entity("Sol");
        luzEntidad.addComponent(sol);
        motor.getScene().addEntity(luzEntidad);

        Entity piso = new Entity("piso");
//        piso.rotar(Math.toRadians(5), Math.toRadians(5), 0);
        piso.addComponent(new Box(0.f, 500, 500));
        motor.getScene().addEntity(piso);

        motor.getRenderEngine().setCargando(false);

    }
}
