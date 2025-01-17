/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.cinematic;

import net.qoopo.engine.core.animation.AnimationFrame;
import net.qoopo.engine.core.animation.AnimationPair;
import net.qoopo.engine.core.entity.component.animation.AnimationComponent;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 *
 * @author alberto
 */
public class CinematicTest extends MakeTestScene {

    public CinematicTest() {

    }

    public void make(Scene scene) {
        this.scene = scene;
        Camera camera = new Camera("cinematic_camera");

        float duration = 5.0f;
        int frames = 9;
        float timeDeltaPerFrame = duration / frames;

        AnimationComponent animation = new AnimationComponent(duration);

        float timeMark = 0;

        QVector3[] locations = new QVector3[] {
                QVector3.of(-20, 20, -20), QVector3.of(-20, 20, 0), QVector3.of(-20, 20, 20),
                QVector3.of(0, 20, 20), QVector3.of(20, 20, 20), QVector3.of(20, 20, 0),
                QVector3.of(20, 20, -20), QVector3.of(0, 20, -20), QVector3.of(-20, 20, -20)
        };

        for (int keyFrame = 0; keyFrame < frames; keyFrame++) {
            AnimationFrame frame = new AnimationFrame(timeMark);
            camera.lookAtTarget(locations[keyFrame], QVector3.zero, QVector3.unitario_y);
            frame.addPair(new AnimationPair(camera, camera.getTransform().clone()));
            animation.addFrame(frame);

            timeMark += timeDeltaPerFrame;
        }

        camera.addComponent(animation);
        scene.addEntity(camera);
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
