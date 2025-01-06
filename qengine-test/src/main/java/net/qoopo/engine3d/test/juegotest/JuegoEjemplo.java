/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.juegotest;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.CylinderX;
import net.qoopo.engine.core.entity.component.mesh.util.QUnidadMedida;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaConvexa;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QRueda;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QVehiculo;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QVehiculoControl;
import net.qoopo.engine.core.lwjgl.audio.component.SoundListenerAL;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.QEscenario;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine3d.QEngine3D;
import net.qoopo.engine3d.core.sky.QCielo;
import net.qoopo.engine3d.core.sky.SphereCielo;
import net.qoopo.engine3d.test.juegotest.mundo.niveles.NivelTest2;

/**
 *
 * @author alberto
 */
public class JuegoEjemplo {

        public static void main(String[] args) {
                QEngine3D motor = new QEngine3D("QMotor 3D Juego Test");

                Camera cam = new Camera();
                cam.frustrumLejos = 1000;
                cam.updateCamera();
                motor.getScene().addEntity(cam);
                motor.exitonEsc();

                // estas lineas es por el java3D que debo cargar el escenario antes del
                // renderizador
                // <Java3D>
                // motor.iniciarAudio();
                // motor.setIniciarAudio(false);
                // cargarNivelPersonaje(motor, cam);
                // Entity sol = new Entity("Sol");
                // sol.agregarComponente(new QLuzDireccional(.5f, QColor.YELLOW, true,
                // 1000000));
                // motor.getUniverso().addEntity(sol);
                // </Java3D>
                // motor.configurarRendererFullScreen(cam);
                // motor.configurarRenderer(800, 600, cam, true);
                // motor.configurarRenderer(800, 600, cam, false);
                // motor.setIniciarFisica(false);
                motor.configRenderer(800, 600, cam);
                motor.getRenderEngine().setCargando(true);
                configurarCielo(motor);
                motor.start();

                cargarNivelPersonaje(motor, cam);

                motor.getRenderEngine().setCargando(false);
                System.out.println("Listo");
        }

        private static Entity crearVehiculo(Scene mundo) {

                float alturaLlantaConexion = 0.25f;
                float px = 0.5f;
                float pz = 0.8f;
                QMaterialBas material = new QMaterialBas("Vehículo");
                material.setColorBase(QColor.BLUE);

                Entity carro = new Entity();
                Box geom = new Box(0.5f, 1, 2);
                MaterialUtil.applyMaterial(geom, material);
                carro.addComponent(geom);

                CollisionShape colision = new QColisionMallaConvexa(geom);
                carro.addComponent(colision);
                // CollisionShape colision = new QColisionCaja(2,1f,3);
                // QColisionCompuesta colisionCom = new QColisionCompuesta();
                // colisionCom.agregarHijo(colision, QVector3.of(0, 1f, 0));
                // carro.agregarComponente(colisionCom);

                QObjetoRigido rigido = new QObjetoRigido(QObjetoDinamico.DINAMICO, 800);
                // rigido.setFormaColision(colisionCom);
                rigido.setFormaColision(colision);
                carro.addComponent(rigido);

                QVehiculo vehiculo = new QVehiculo(rigido);
                carro.addComponent(vehiculo);

                // ruedas
                QMaterialBas materialRueda = new QMaterialBas("Rueda");
                materialRueda.setColorBase(QColor.DARK_GRAY);

                CylinderX forma = new CylinderX(0.1f, 0.25f);
                MaterialUtil.applyMaterial(forma, materialRueda);
                // --

                Entity rueda1E = new Entity("rueda1", false);// la entidad que se actualizara su transformacion con el
                                                             // vehiculo
                rueda1E.addComponent(forma.clone());
                rueda1E.move(-px, alturaLlantaConexion, pz);
                mundo.addEntity(rueda1E);

                Entity rueda2E = new Entity("rueda2", false);// la entidad que se actualizara su transformacion con el
                                                             // vehiculo
                rueda2E.addComponent(forma.clone());
                rueda2E.move(px, alturaLlantaConexion, pz);
                mundo.addEntity(rueda2E);

                Entity rueda3E = new Entity("rueda3", false);// la entidad que se actualizara su transformacion con el
                                                             // vehiculo
                rueda3E.addComponent(forma.clone());
                rueda3E.move(-px, alturaLlantaConexion, -pz);
                mundo.addEntity(rueda3E);
                Entity rueda4E = new Entity("rueda4", false);// la entidad que se actualizara su transformacion con el
                                                             // vehiculo
                rueda4E.addComponent(forma.clone());
                rueda4E.move(px, alturaLlantaConexion, -pz);
                mundo.addEntity(rueda4E);
                // ----------------
                // 1
                QRueda rueda1 = new QRueda();
                rueda1.setEntidadRueda(rueda1E);
                rueda1.setFriccion(1000f);
                rueda1.setFrontal(true);
                rueda1.setAncho(0.1f);
                rueda1.setRadio(0.25f);
                rueda1.setUbicacion(QVector3.of(-px, alturaLlantaConexion, pz));
                vehiculo.agregarRueda(rueda1);
                // 2
                QRueda rueda2 = new QRueda();
                rueda2.setEntidadRueda(rueda2E);
                rueda2.setFriccion(1000f);
                rueda2.setFrontal(true);
                rueda2.setAncho(0.1f);
                rueda2.setRadio(0.25f);
                rueda2.setUbicacion(QVector3.of(px, alturaLlantaConexion, pz));
                vehiculo.agregarRueda(rueda2);
                // 3

                QRueda rueda3 = new QRueda();
                rueda3.setEntidadRueda(rueda3E);
                rueda3.setFriccion(1000f);
                rueda3.setFrontal(false);
                rueda3.setAncho(0.1f);
                rueda3.setRadio(0.25f);
                rueda3.setUbicacion(QVector3.of(-px, alturaLlantaConexion, -pz));
                vehiculo.agregarRueda(rueda3);
                // 4

                QRueda rueda4 = new QRueda();
                rueda4.setEntidadRueda(rueda4E);
                rueda4.setFriccion(1000f);
                rueda4.setFrontal(false);
                rueda4.setAncho(0.1f);
                rueda4.setRadio(0.25f);
                rueda4.setUbicacion(QVector3.of(pz, alturaLlantaConexion, -pz));
                vehiculo.agregarRueda(rueda4);
                // control del vehiculo

                // agrego los componentes
                carro.addComponent(new QVehiculoControl(vehiculo));

                carro.move(0, 25, 0);
                // carro.agregarHijo(rueda1E);
                // carro.agregarHijo(rueda2E);
                // carro.agregarHijo(rueda3E);
                // carro.agregarHijo(rueda4E);
                mundo.addEntity(carro);
                return carro;
        }

        private static void configurarCielo(QEngine3D motor) {
                // sol
                Entity sol = new Entity("Sol");
                QDirectionalLigth solLuz = new QDirectionalLigth(1.1f, QColor.WHITE, 1, QVector3.of(0, 0, 0), true,
                                true);
                solLuz.setResolucionMapaSombra(1024);
                sol.addComponent(solLuz);
                motor.getScene().addEntity(sol);

                // QTextura cieloDia = QGestorRecursos.loadTexture("dia", "assets/"+
                // "textures/cielo/esfericos/cielo_dia.jpg");
                QTextura cieloDia = AssetManager.get().loadTexture("dia",
                                "assets/textures/cielo/esfericos/cielo4.jpg");
                // QTextura cieloNoche = QGestorRecursos.loadTexture("noche",
                // "res/textures/cielo/esfericos/cielo_noche.png");
                QTextura cieloNoche = AssetManager.get().loadTexture("noche",
                                "assets/textures/cielo/esfericos/cielo_noche_2.jpg");
                QCielo cielo = new SphereCielo(cieloDia, cieloNoche,
                                motor.getScene().UM.convertirPixel(500, QUnidadMedida.METRO));
                motor.getScene().addEntity(cielo.getEntidad());

                // motor.configurarDiaNoche(cielo, 60 * 2, solLuz, 9); //el dia dura 2 minutos
                // motor.configurarDiaNoche(cielo, 60, solLuz, 9);
                // 50 minutos
                motor.configDayCycle(cielo, 60 * 50, solLuz, 9);// cada hora es igual a 50 minutos
        }

        private static void cargarNivelPersonaje(QEngine3D motor, Camera cam) {
                // QEscenario nivel = new NivelTest();
                QEscenario nivel = new NivelTest2();
                // QEscenario nivel = new DoomTest();
                nivel.cargar(motor.getScene());
                //
                // Entity sol = new Entity("Sol");
                // QLuzDireccional solLuz = new QLuzDireccional(1.5f, QColor.WHITE, true, 1,
                // QVector3.of(1f, -1, 0));
                // solLuz.setProyectarSombras(true);
                //// solLuz.setRadioSombra(50);
                // solLuz.setSombraDinamica(true);
                // solLuz.setResolucionMapaSombra(1024);
                // sol.agregarComponente(solLuz);
                // motor.getEscena().addEntity(sol);

                try {
                        // Entity personaje = MD5Loader.cargar(new File("assets/"+
                        // "models/md5/bob/boblamp.md5mesh").getAbsolutePath());
                        // personaje.mover(-90, 200, 9);
                        // personaje.escalar(0.05f, 0.05f, 0.05f);
                        // personaje.rotar(Math.toRadians(-90), 0, 0);
                        // -------------------------------------------
                        // Bob personaje = new Bob();
                        // personaje.mover(-90, 200, 9);
                        // personaje.setTerreno(nivel.getTerreno());

                        Entity personaje = crearVehiculo(motor.getScene());
                        personaje.move(-90, 15, 9);
                        // pongo las coordenadas para q este detras del jugador
                        cam.lookAtTarget(
                                        QVector3.of(0, 3f, -8),
                                        QVector3.of(0, 1f, 0),
                                        QVector3.unitario_y.clone());

                        // cam.lookAtTarget(
                        // QVector3.of(0, 2.5f, 6), //detras y arriba de bob
                        // QVector3.of(0, -2.5f, -6),
                        // QVector3.unitario_y.clone());
                        // pongo las coordenadas para q este en la cabeza del jugador a manera ojos
                        // cam.lookAtTarget(
                        // QVector3.of(0, 1.2f, -0.1f), //en la cabeza, en el lugar de los ojos
                        // QVector3.of(0, 1.0f, -0.5f),//mire al frente, un poco inclinado
                        // QVector3.unitario_y.clone());
                        // al agregar la camara como hija del personaje, siempre va a seguir al
                        // personaje
                        personaje.addChild(cam);
                        // personaje.agregarLinterna();
                        // personaje.agregarPistola();
                        // personaje.posicionPistola();
                        // configuro a personaje como listener de audio
                        personaje.addComponent(new SoundListenerAL(personaje.getTransformacion().getTraslacion()));
                        motor.getScene().addEntity(personaje);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

}
