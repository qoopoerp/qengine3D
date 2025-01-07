/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.juegotest.generadores;

import java.awt.event.KeyEvent;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.animation.AnimationComponent;
import net.qoopo.engine.core.entity.component.animation.AnimationStorageComponent;
import net.qoopo.engine.core.entity.component.gui.QTecladoReceptor;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Cylinder;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Torus;
import net.qoopo.engine.core.util.ComponentUtil;

/**
 * Clase para crear personajes del juego
 *
 * @author alberto
 */
public class CreadorVehiculos {

    private Entity vehiculo;

    private Entity ruedaDD;
    private Entity ruedaDI;
    private Entity ruedaTD;
    private Entity ruedaTI;

    private int secciones = 10;

    public boolean shift = false;

    public Entity crearPersonaje() {

        vehiculo = new Entity("Vehículo");

        vehiculo.addComponent(new Box(0.5f, 1, 1.5f));

        ruedaDD = new Entity("ruedaDD");
        ruedaDD.move(0, 1.25f, 0);
        ruedaDD.addComponent(new Torus(0.25f, 0.05f, secciones));

        ruedaDI = new Entity("ruedaDI");
        ruedaDI.move(0, 0.85f, 0);
        ruedaDI.addComponent(new Torus(0.25f, 0.05f, secciones));

        ruedaTD = new Entity("brazoI");
        ruedaTD.move(0.15f, 0.75f, 0);
        ruedaTD.addComponent(new Cylinder(0.6f, 0.03f, secciones));

        ruedaTI = new Entity("brazoD");
        ruedaTI.move(-0.15f, 0.75f, 0);
        ruedaTI.addComponent(new Cylinder(0.6f, 0.03f, secciones));

        Entity lampara = GeneradorLamparas.crearLamparaVelador();
        // comov a a estar agregado al brazo se mueve de acuerdo a las coordenadas del
        // brazo
        lampara.move(0, -0.3f, 0);
        lampara.rotate((float) -Math.toRadians(45), 0, 0);
        ruedaTI.addChild(lampara);

        vehiculo.addChild(ruedaDD);
        vehiculo.addChild(ruedaDI);
        vehiculo.addChild(ruedaTI);
        vehiculo.addChild(ruedaTD);

        AnimationStorageComponent almacen = new AnimationStorageComponent();
        // almacen.agregarAnimacion("caminar", animacionCaminar(10));
        // almacen.agregarAnimacion("correr", animacionCaminar(2));
        vehiculo.addComponent(almacen);

        // agrego los listener de teclado
        vehiculo.addComponent(crearReceptorTeclado());

        // se agrga la animacino a la entity
        // personaje.agregarComponente(animacion);
        return vehiculo;
    }

    private QTecladoReceptor crearReceptorTeclado() {
        // voy a agregar los listeners de teclado
        QTecladoReceptor teclado = new QTecladoReceptor() {
            @Override
            public void keyPressed(KeyEvent evt) {

                try {
                    switch (evt.getKeyCode()) {

                        case KeyEvent.VK_SHIFT:
                            shift = true;
                            break;

                        case KeyEvent.VK_Q:
                            vehiculo.aumentarY(0.05f);
                            break;
                        case KeyEvent.VK_E:
                            vehiculo.aumentarY(-0.05f);
                            break;
                        case KeyEvent.VK_W:
                            if (shift) {
                                // corre
                                ComponentUtil.removeComponents(entity, AnimationComponent.class);
                                vehiculo.addComponent(
                                        ((AnimationStorageComponent) ComponentUtil.getComponent(entity,
                                                AnimationStorageComponent.class)).get("correr"));
                                vehiculo.moveForward(0.15f);
                            } else {
                                // camina
                                ComponentUtil.removeComponents(entity, AnimationComponent.class);
                                vehiculo.addComponent(
                                        ((AnimationStorageComponent) ComponentUtil.getComponent(entity,
                                                AnimationStorageComponent.class)).get("caminar"));
                                vehiculo.moveForward(0.05f);
                            }

                            break;
                        case KeyEvent.VK_S:
                            if (shift) {
                                // corre
                                ComponentUtil.removeComponents(entity, AnimationComponent.class);
                                vehiculo.addComponent(
                                        ((AnimationStorageComponent) ComponentUtil.getComponent(entity,
                                                AnimationStorageComponent.class)).get("correr"));
                                vehiculo.moveForward(-0.15f);
                            } else {
                                // camina
                                ComponentUtil.removeComponents(entity, AnimationComponent.class);
                                vehiculo.addComponent(
                                        ((AnimationStorageComponent) ComponentUtil.getComponent(entity,
                                                AnimationStorageComponent.class)).get("caminar"));
                                vehiculo.moveForward(-0.05f);
                            }
                            break;
                        case KeyEvent.VK_D:
                            // personaje.moverDerechaIzquierda(0.05f);
                            vehiculo.aumentarRotY(-(float) Math.toRadians(5));
                            break;
                        case KeyEvent.VK_A:
                            vehiculo.aumentarRotY((float) Math.toRadians(5));
                            // personaje.moverDerechaIzquierda(-0.05f);
                            break;
                        case KeyEvent.VK_SPACE:
                            // personaje.moverDerechaIzquierda(-0.05f);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void keyReleased(KeyEvent evt) {
                // elimino cualquier animacion
                ComponentUtil.removeComponents(entity, AnimationComponent.class);
                switch (evt.getKeyCode()) {

                    case KeyEvent.VK_SHIFT:
                        shift = false;
                        break;

                }

            }
        };
        return teclado;
    }

    /**
     * Crea una animacion simple para caminarF
     *
     * @return
     */
    // private QComponenteAnimacion animacionCaminar(int duracionFrames) {
    // QComponenteAnimacion animacion = new QComponenteAnimacion();
    //// **********************************************************************************************
    //// Frame 1
    //// **********************************************************************************************
    //
    //// int duracionFrames = 10;
    // //frame 1
    // QAnimacionFrame frame1 = new QAnimacionFrame(QAnimacionFrame.TIPO_FINITA,
    // duracionFrames);
    // frame1.setTipoTransformacion(QAnimacionFrame.TIPO_TRANSFORMACION_ADICIONAR);
    //
    // QTransformacion tFrame1_piernaD = new QTransformacion();
    // tFrame1_piernaD.getRotacion().rotarX((float) Math.toRadians(10));
    //
    // QTransformacion tFrame1_piernaI = new QTransformacion();
    // tFrame1_piernaI.getRotacion().rotarX(-(float) Math.toRadians(10));
    //
    // frame1.agregarPar(new QParAnimacion(piernaD, tFrame1_piernaD));
    // frame1.agregarPar(new QParAnimacion(piernaI, tFrame1_piernaI));
    // frame1.agregarPar(new QParAnimacion(ruedaTD, tFrame1_piernaD));
    // frame1.agregarPar(new QParAnimacion(ruedaTI, tFrame1_piernaI));
    //
    // animacion.agregarAnimacionFrame(frame1);
    //
    //// **********************************************************************************************
    //// Frame 2
    //// **********************************************************************************************
    // //frame 1
    // QAnimacionFrame frame2 = new QAnimacionFrame(QAnimacionFrame.TIPO_FINITA,
    // duracionFrames);
    // frame2.setTipoTransformacion(QAnimacionFrame.TIPO_TRANSFORMACION_ADICIONAR);
    // // Pierna D hacia atras 45 grados
    // QTransformacion tFrame2_piernaD = new QTransformacion();
    // tFrame2_piernaD.getRotacion().rotarX((float) Math.toRadians(10));
    //
    // QTransformacion tFrame2_piernaI = new QTransformacion();
    // tFrame2_piernaI.getRotacion().rotarX(-(float) Math.toRadians(10));
    //
    // frame2.agregarPar(new QParAnimacion(piernaD, tFrame2_piernaD));
    // frame2.agregarPar(new QParAnimacion(piernaI, tFrame2_piernaI));
    // frame2.agregarPar(new QParAnimacion(ruedaTD, tFrame2_piernaD));
    // frame2.agregarPar(new QParAnimacion(ruedaTI, tFrame2_piernaI));
    //
    // animacion.agregarAnimacionFrame(frame2);
    //
    // //**********************************************************************************************
    //// Frame 3
    //// **********************************************************************************************
    // //frame 1
    // QAnimacionFrame frame3 = new QAnimacionFrame(QAnimacionFrame.TIPO_FINITA,
    // duracionFrames);
    // frame3.setTipoTransformacion(QAnimacionFrame.TIPO_TRANSFORMACION_ADICIONAR);
    // // Pierna D hacia atras 45 grados
    // QTransformacion tFrame3_piernaD = new QTransformacion();
    // tFrame3_piernaD.getRotacion().rotarX((float) Math.toRadians(10));
    //
    // QTransformacion tFrame3_piernaI = new QTransformacion();
    // tFrame3_piernaI.getRotacion().rotarX(-(float) Math.toRadians(10));
    //
    // frame3.agregarPar(new QParAnimacion(piernaD, tFrame3_piernaD));
    // frame3.agregarPar(new QParAnimacion(piernaI, tFrame3_piernaI));
    // frame3.agregarPar(new QParAnimacion(ruedaTD, tFrame3_piernaD));
    // frame3.agregarPar(new QParAnimacion(ruedaTI, tFrame3_piernaI));
    //
    // animacion.agregarAnimacionFrame(frame3);
    //
    // //**********************************************************************************************
    //// Frame 4
    //// **********************************************************************************************
    // //frame 1
    // QAnimacionFrame frame4 = new QAnimacionFrame(QAnimacionFrame.TIPO_FINITA,
    // duracionFrames);
    // frame4.setTipoTransformacion(QAnimacionFrame.TIPO_TRANSFORMACION_ADICIONAR);
    // // Pierna D hacia atras 45 grados
    // QTransformacion tFrame4_piernaD = new QTransformacion();
    // tFrame4_piernaD.getRotacion().rotarX(-(float) Math.toRadians(10));
    //
    // QTransformacion tFrame4_piernaI = new QTransformacion();
    // tFrame4_piernaI.getRotacion().rotarX((float) Math.toRadians(10));
    //
    // frame4.agregarPar(new QParAnimacion(piernaD, tFrame4_piernaD));
    // frame4.agregarPar(new QParAnimacion(piernaI, tFrame4_piernaI));
    // frame4.agregarPar(new QParAnimacion(ruedaTD, tFrame4_piernaD));
    // frame4.agregarPar(new QParAnimacion(ruedaTI, tFrame4_piernaI));
    //
    // animacion.agregarAnimacionFrame(frame4);
    // //**********************************************************************************************
    //// Frame 5
    //// **********************************************************************************************
    // //frame 1
    // QAnimacionFrame frame5 = new QAnimacionFrame(QAnimacionFrame.TIPO_FINITA,
    // duracionFrames);
    // frame5.setTipoTransformacion(QAnimacionFrame.TIPO_TRANSFORMACION_ADICIONAR);
    //
    // // Pierna D hacia atras 45 grados
    // QTransformacion tFrame5_piernaD = new QTransformacion();
    // tFrame5_piernaD.getRotacion().rotarX(-(float) Math.toRadians(20));//para que
    // lleguen a 0
    //
    // QTransformacion tFrame5_piernaI = new QTransformacion();
    // tFrame5_piernaI.getRotacion().rotarX((float) Math.toRadians(20));//para que
    // lleguen a 0
    //
    // frame5.agregarPar(new QParAnimacion(piernaD, tFrame5_piernaD));
    // frame5.agregarPar(new QParAnimacion(piernaI, tFrame5_piernaI));
    // frame5.agregarPar(new QParAnimacion(ruedaTD, tFrame5_piernaD));
    // frame5.agregarPar(new QParAnimacion(ruedaTI, tFrame5_piernaI));
    //
    // animacion.agregarAnimacionFrame(frame5);
    //
    // //**********************************************************************************************
    //// Frame 6
    //// **********************************************************************************************
    // //frame 1
    // QAnimacionFrame frame6 = new QAnimacionFrame(QAnimacionFrame.TIPO_FINITA,
    // duracionFrames);
    // frame6.setTipoTransformacion(QAnimacionFrame.TIPO_TRANSFORMACION_ADICIONAR);
    //
    // // Pierna D hacia atras 45 grados
    // QTransformacion tFrame6_piernaD = new QTransformacion();
    // tFrame6_piernaD.getRotacion().rotarX(-(float) Math.toRadians(10));
    //
    // QTransformacion tFrame6_piernaI = new QTransformacion();
    // tFrame6_piernaI.getRotacion().rotarX((float) Math.toRadians(10));
    //
    // frame6.agregarPar(new QParAnimacion(piernaD, tFrame6_piernaD));
    // frame6.agregarPar(new QParAnimacion(piernaI, tFrame6_piernaI));
    // frame6.agregarPar(new QParAnimacion(ruedaTD, tFrame6_piernaD));
    // frame6.agregarPar(new QParAnimacion(ruedaTI, tFrame6_piernaI));
    //
    // animacion.agregarAnimacionFrame(frame6);
    //
    //// **********************************************************************************************
    //// Frame 7
    //// **********************************************************************************************
    // //frame 1
    // QAnimacionFrame frame7 = new QAnimacionFrame(QAnimacionFrame.TIPO_FINITA,
    // duracionFrames);
    // frame7.setTipoTransformacion(QAnimacionFrame.TIPO_TRANSFORMACION_ADICIONAR);
    //
    // // Pierna D hacia atras 45 grados
    // QTransformacion tFrame7_piernaD = new QTransformacion();
    // tFrame7_piernaD.getRotacion().rotarX(-(float) Math.toRadians(10));
    //
    // QTransformacion tFrame7_piernaI = new QTransformacion();
    // tFrame7_piernaI.getRotacion().rotarX((float) Math.toRadians(10));
    //
    // frame7.agregarPar(new QParAnimacion(piernaD, tFrame7_piernaD));
    // frame7.agregarPar(new QParAnimacion(piernaI, tFrame7_piernaI));
    // frame7.agregarPar(new QParAnimacion(ruedaTD, tFrame7_piernaD));
    // frame7.agregarPar(new QParAnimacion(ruedaTI, tFrame7_piernaI));
    //
    // animacion.agregarAnimacionFrame(frame7);
    //
    // //**********************************************************************************************
    //// Frame 8
    //// **********************************************************************************************
    // //frame 1
    // QAnimacionFrame frame8 = new QAnimacionFrame(QAnimacionFrame.TIPO_FINITA,
    // duracionFrames);
    // frame8.setTipoTransformacion(QAnimacionFrame.TIPO_TRANSFORMACION_ADICIONAR);
    // // Pierna D hacia atras 45 grados
    // QTransformacion tFrame8_piernaD = new QTransformacion();
    // tFrame8_piernaD.getRotacion().rotarX(-(float) Math.toRadians(10));
    //
    // QTransformacion tFrame8_piernaI = new QTransformacion();
    // tFrame8_piernaI.getRotacion().rotarX((float) Math.toRadians(10));
    //
    // frame8.agregarPar(new QParAnimacion(piernaD, tFrame8_piernaD));
    // frame8.agregarPar(new QParAnimacion(piernaI, tFrame8_piernaI));
    // frame8.agregarPar(new QParAnimacion(ruedaTD, tFrame8_piernaD));
    // frame8.agregarPar(new QParAnimacion(ruedaTI, tFrame8_piernaI));
    //
    // animacion.agregarAnimacionFrame(frame8);
    //
    // //**********************************************************************************************
    //// Frame 9
    //// **********************************************************************************************
    // //frame 1
    // QAnimacionFrame frame9 = new QAnimacionFrame(QAnimacionFrame.TIPO_FINITA,
    // duracionFrames);
    // frame9.setTipoTransformacion(QAnimacionFrame.TIPO_TRANSFORMACION_ADICIONAR);
    // // Pierna D hacia atras 45 grados
    // QTransformacion tFrame9_piernaD = new QTransformacion();
    // tFrame9_piernaD.getRotacion().rotarX((float) Math.toRadians(10));
    //
    // QTransformacion tFrame9_piernaI = new QTransformacion();
    // tFrame9_piernaI.getRotacion().rotarX(-(float) Math.toRadians(10));
    //
    // frame9.agregarPar(new QParAnimacion(piernaD, tFrame9_piernaD));
    // frame9.agregarPar(new QParAnimacion(piernaI, tFrame9_piernaI));
    // frame9.agregarPar(new QParAnimacion(ruedaTD, tFrame9_piernaD));
    // frame9.agregarPar(new QParAnimacion(ruedaTI, tFrame9_piernaI));
    //
    // animacion.agregarAnimacionFrame(frame9);
    // //**********************************************************************************************
    //// Frame 10
    //// **********************************************************************************************
    // //frame 1
    // QAnimacionFrame frame10 = new QAnimacionFrame(QAnimacionFrame.TIPO_FINITA,
    // true, duracionFrames);
    // frame10.setTipoTransformacion(QAnimacionFrame.TIPO_TRANSFORMACION_ADICIONAR);
    //
    // // Pierna D hacia atras 45 grados
    // QTransformacion tFrame10_piernaD = new QTransformacion();
    // tFrame10_piernaD.getRotacion().rotarX((float) Math.toRadians(20));//para que
    // lleguen a 0
    //
    // QTransformacion tFrame10_piernaI = new QTransformacion();
    // tFrame10_piernaI.getRotacion().rotarX(-(float) Math.toRadians(20));//para que
    // lleguen a 0
    //
    // frame10.agregarPar(new QParAnimacion(piernaD, tFrame10_piernaD));
    // frame10.agregarPar(new QParAnimacion(piernaI, tFrame10_piernaI));
    // frame10.agregarPar(new QParAnimacion(ruedaTD, tFrame10_piernaD));
    // frame10.agregarPar(new QParAnimacion(ruedaTI, tFrame10_piernaI));
    // animacion.agregarAnimacionFrame(frame10);
    //
    // return animacion;
    // }
}
