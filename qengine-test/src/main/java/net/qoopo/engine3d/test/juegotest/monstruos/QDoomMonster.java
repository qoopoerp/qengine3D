/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.juegotest.monstruos;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.assets.audio.AudioBuffer;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.animation.AnimationComponent;
import net.qoopo.engine.core.entity.component.animation.AnimationStorageComponent;
import net.qoopo.engine.core.entity.component.ia.acciones.QAccion;
import net.qoopo.engine.core.lwjgl.audio.component.SoundEmissorAL;
import net.qoopo.engine.core.util.ComponentUtil;

/**
 *
 * @author alberto
 */
public class QDoomMonster {

    /**
     * Carga un modelo con animaciones y les agrega sonidos y comportamiento
     *
     * @return
     */
    public static Entity quakeMonster() {

        try {
            Entity monstruo = (Entity) AssetManager.get()
                    .loadModel("assets/models/md5/DOOM_MONSTERS/hellknight/monster.md5mesh");

            // Carga de audios
            AudioBuffer qdemon_atacar = AssetManager.get()
                    .loadAudio("assets/audio/ogg/quake/obihb_qdemon_growl.ogg", "qdemon_atacar");
            AudioBuffer qdemon_walk = AssetManager.get()
                    .loadAudio("assets/audio/ogg/quake/obihb_qwizard_walk.ogg", "qdemon_walk");
            AudioBuffer bufAves = AssetManager.get().loadAudio("assets/audio/ogg/aves.ogg", "au_aves");

            SoundEmissorAL emisorAudio = new SoundEmissorAL(false, false);
            emisorAudio.setPosition(monstruo.getMatrizTransformacion(System.currentTimeMillis()).toTranslationVector());
            emisorAudio.setBuffer(bufAves.getBufferId());
            emisorAudio.setReproducirAlInicio(false);
            emisorAudio.setGain(1.0f);
            monstruo.addComponent(emisorAudio);

            // animacion idle
            monstruo.addComponent(
                    ((AnimationStorageComponent) ComponentUtil.getComponent(monstruo, AnimationStorageComponent.class))
                            .get("idle1"));

            QAccion accionIdle = new QAccion("Idle") {
                @Override
                public void ejecutar(Object... parametros) {

                    // configura la animacion de caminar
                    ComponentUtil.removeComponents(monstruo, AnimationComponent.class);
                    AnimationComponent anim = ((AnimationStorageComponent) ComponentUtil.getComponent(monstruo,
                            AnimationStorageComponent.class)).get("idle1");
                    monstruo.addComponent(anim);

                    try {
                        emisorAudio.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        emisorAudio.setBuffer(qdemon_walk.getBufferId());
                        emisorAudio.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            QAccion caminar = new QAccion("caminar") {
                @Override
                public void ejecutar(Object... parametros) {

                    // configura la animacion de caminar
                    ComponentUtil.removeComponents(monstruo, AnimationComponent.class);
                    AnimationComponent anim = ((AnimationStorageComponent) ComponentUtil.getComponent(monstruo,
                            AnimationStorageComponent.class)).get("walk1");
                    // le quito el loop
                    anim.reiniciar();
                    anim.setLoop(false);
                    monstruo.addComponent(anim);

                    try {
                        emisorAudio.stop();
                    } catch (Exception e) {
                    }
                    emisorAudio.setBuffer(qdemon_walk.getBufferId());
                    emisorAudio.play();

                    try {
                        // espero unos 15 segundos para empezar
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(QDoomMonster.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    accionIdle.ejecutar();
                }
            };

            QAccion atacar = new QAccion("atacar") {
                @Override
                public void ejecutar(Object... parametros) {
                    // configura la animacion de caminar
                    ComponentUtil.removeComponents(monstruo, AnimationComponent.class);

                    AnimationComponent anim = ((AnimationStorageComponent) ComponentUtil.getComponent(monstruo,
                            AnimationStorageComponent.class)).get("melee1");
                    // le quito el loop
                    anim.reiniciar();
                    anim.setLoop(false);
                    monstruo.addComponent(anim);
                    try {
                        emisorAudio.stop();
                    } catch (Exception e) {
                    }
                    emisorAudio.setBuffer(qdemon_atacar.getBufferId());
                    emisorAudio.play();

                    try {

                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(QDoomMonster.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    accionIdle.ejecutar();
                }
            };

            // este hilo va a iterar sobre las acciones, deberia existir una clase de
            // comportamientos que defina la ejecucion o no de las acciones
            Thread hiloTest = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // espero unos 15 segundos para empezar
                        Thread.sleep(15000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(QDoomMonster.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    long tInicio = System.currentTimeMillis();

                    // or 2minutos
                    // while (System.currentTimeMillis() - tInicio < 60000 * 2) {
                    while (true) {
                        try {
                            caminar.ejecutar();
                            Thread.sleep(10000);// espero 10 segundos antes de cambiar de accion
                            atacar.ejecutar();
                            Thread.sleep(10000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(QDoomMonster.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            hiloTest.start();

            return monstruo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Entity quakeMonster2() {
        try {
            Entity monstruo = (Entity) AssetManager.get()
                    .loadModel("assets/models/md5/doom3/md5/monsters/cacodemon/cacodemon.md5mesh");

            // Carga de audios
            AudioBuffer bucAtacar = AssetManager.get().loadAudio(
                    "assets/audio/ogg/quake/obihb_qdemon_growl.ogg",
                    "qdemon_atacar");

            AudioBuffer bufAves = AssetManager.get().loadAudio("assets/audio/ogg/aves.ogg", "au_aves");

            SoundEmissorAL emisorAudio = new SoundEmissorAL(false, false);
            emisorAudio.setPosition(monstruo.getMatrizTransformacion(System.currentTimeMillis()).toTranslationVector());
            emisorAudio.setBuffer(bufAves.getBufferId());
            emisorAudio.setReproducirAlInicio(false);
            emisorAudio.setGain(1.0f);
            monstruo.addComponent(emisorAudio);

            // animacion idle
            monstruo.addComponent(((AnimationStorageComponent) ComponentUtil.getComponent(monstruo,
                    AnimationStorageComponent.class)).get("idle02"));

            QAccion accionIdle = new QAccion("caminar") {
                @Override
                public void ejecutar(Object... parametros) {

                    // configura la animacion de caminar
                    ComponentUtil.removeComponents(monstruo, AnimationComponent.class);
                    AnimationComponent anim = ((AnimationStorageComponent) ComponentUtil.getComponent(monstruo,
                            AnimationStorageComponent.class)).get("idle02");
                    monstruo.addComponent(anim);

                    try {
                        emisorAudio.stop();
                    } catch (Exception e) {
                    }
                    // emisorAudio.setBuffer(qdemon_walk.getBufferId());
                    // emisorAudio.play();
                }
            };

            QAccion atacar = new QAccion("atacar") {
                @Override
                public void ejecutar(Object... parametros) {
                    // configura la animacion de caminar
                    ComponentUtil.removeComponents(monstruo, AnimationComponent.class);

                    AnimationComponent anim = ((AnimationStorageComponent) ComponentUtil.getComponent(monstruo,
                            AnimationStorageComponent.class)).get("attack02");
                    // le quito el loop
                    anim.reiniciar();
                    anim.setLoop(false);
                    monstruo.addComponent(anim);
                    try {
                        emisorAudio.stop();
                    } catch (Exception e) {
                    }
                    emisorAudio.setBuffer(bucAtacar.getBufferId());
                    emisorAudio.play();

                    try {

                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(QDoomMonster.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    accionIdle.ejecutar();
                }
            };

            // este hilo va a iterar sobre las acciones, deberia existir una clase de
            // comportamientos que defina la ejecucion o no de las acciones
            Thread hiloTest = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // espero unos 15 segundos para empezar
                        Thread.sleep(15000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(QDoomMonster.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    long tInicio = System.currentTimeMillis();

                    // or 2minutos
                    // while (System.currentTimeMillis() - tInicio < 60000 * 2) {
                    while (true) {
                        try {
                            atacar.ejecutar();
                            Thread.sleep(10000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(QDoomMonster.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            hiloTest.start();

            return monstruo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
