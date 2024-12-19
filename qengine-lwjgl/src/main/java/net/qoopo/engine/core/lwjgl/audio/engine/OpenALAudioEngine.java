/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.lwjgl.audio.engine;

import org.lwjgl.openal.AL11;

import net.qoopo.engine.core.audio.AudioEngine;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.lwjgl.audio.component.SoundEmissorAL;
import net.qoopo.engine.core.lwjgl.audio.component.SoundListenerAL;
import net.qoopo.engine.core.scene.Scene;

/**
 * Motor de audio usando OpenAL
 * 
 * @author alberto
 */
public class OpenALAudioEngine extends AudioEngine {

    private SoundManager manager;

    public OpenALAudioEngine(Scene escena) {
        super(escena);
        manager = new SoundManager();
    }

    @Override
    public void stop() {
        super.stop(); // To change body of generated methods, choose Tools | Templates.
        manager.cleanup();
    }

    @Override
    public void start() {
        try {
            manager.init();
            manager.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // cargarSonidos();
    }

    @Override
    public long update() {
        try {
            long t = System.currentTimeMillis();
            // verifico si exiten nuevos componentes que deba agregar/eliminar/modificar
            for (Entity entity : scene.getEntities()) {
                if (entity.isToRender()) {
                    for (EntityComponent componente : entity.getComponents()) {
                        if (componente instanceof SoundEmissorAL) {
                            if (!manager.contieneSource(componente.entity.getName())) {
                                manager.addSoundSource(componente.entity.getName(), (SoundEmissorAL) componente);
                                if (((SoundEmissorAL) componente).isReproducirAlInicio()) {
                                    ((SoundEmissorAL) componente).play();
                                }
                            }
                            // se actualiza la coordenada de sonido
                            ((SoundEmissorAL) componente)
                                    .setPosition(entity.getMatrizTransformacion(t).toTranslationVector());
                        }

                        // se comporta como componente aunq solo debe haber un listener en todo el
                        // escena
                        if (componente instanceof SoundListenerAL) {
                            if (manager.getListener() == null) {
                                manager.setListener((SoundListenerAL) componente);
                            } else {// actualiza la ubicacion del listener
                                manager.getListener().setPosition(entity.getTransformacion().getTraslacion());
                                manager.getListener().setOrientation(entity.getDirection(), entity.getUp());
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        tiempoPrevio = System.currentTimeMillis();
        return tiempoPrevio;
    }

    // private void cargarSonidos() {
    // try {
    // QBufferSonido buffBack = new QBufferSonido(Global.RECURSOS +
    // "audio/background.ogg");
    // manager.addSoundBuffer(buffBack);
    // QEmisorAudio sourceBack = new QEmisorAudio(true, true);
    // sourceBack.setBuffer(buffBack.getBufferId());
    // manager.addSoundSource("background", sourceBack);
    //
    // QBufferSonido buffBeep = new QBufferSonido(Global.RECURSOS +
    // "audio/beep.ogg");
    // manager.addSoundBuffer(buffBeep);
    // QEmisorAudio sourceBeep = new QEmisorAudio(false, true);
    // sourceBeep.setBuffer(buffBeep.getBufferId());
    // manager.addSoundSource("beep", sourceBeep);
    //
    // QBufferSonido buffFire = new QBufferSonido(Global.RECURSOS +
    // "/audio/fire.ogg");
    // manager.addSoundBuffer(buffFire);
    // QEmisorAudio sourceFire = new QEmisorAudio(true, false);
    //// Vector3f pos = particleEmitter.getBaseParticle().getPosition();
    // QVector3 pos = QVector3.of(-5, 0, 2);
    // sourceFire.setPosition(pos);
    // sourceFire.setBuffer(buffFire.getBufferId());
    // manager.addSoundSource("fuego", sourceFire);
    // sourceFire.play();
    //
    //// manager.setListener(new QSoundListener(QVector3.of(0, 0, 0)));
    //// sourceBack.play();
    // } catch (Exception ex) {
    // Logger.getLogger(QOpenAL.class.getName()).log(Level.SEVERE, null, ex);
    // ex.printStackTrace();
    // }
    // }
}
