package net.qoopo.engine3d.test;

import net.qoopo.engine.core.entity.component.camera.CameraController;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine3d.QEngine3D;
import net.qoopo.engine3d.test.generaEjemplos.InitialScene;

public class Main {
    public static void main(String[] args) {
        // ActualizarMonstruosDoom.main(args);
        // MonstruosDoom.main(args);
        loadExample();
    }

    private static void loadExample() {
        QEngine3D motor = new QEngine3D();
        Camera camera = new Camera();
        camera.addComponent(new CameraController(camera));
        motor.getScene().addEntity(camera);
        camera.lookAtTarget(QVector3.of(30, 30, 30), QVector3.of(0, 0, 0), QVector3.unitario_y);
        motor.configRenderer(800, 600, camera);
        motor.exitonEsc();
        motor.start();
        // motor.getRenderEngine().setShowStats(true);
        // motor.getRenderEngine().setLoading(true);
        new InitialScene().make(motor.getScene());

    }
}
