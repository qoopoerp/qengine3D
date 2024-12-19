package net.qoopo.engine.core.lwjgl.audio.engine;

import static org.lwjgl.openal.AL10.alDistanceModel;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.openal.AL;
//import static org.lwjgl.openal.AL10.*;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.lwjgl.audio.component.AudioBufferAL;
import net.qoopo.engine.core.lwjgl.audio.component.SoundEmissorAL;
import net.qoopo.engine.core.lwjgl.audio.component.SoundListenerAL;

public class SoundManager {

    private long device;
    private long context;
    private SoundListenerAL listener;
    private final List<AudioBufferAL> soundBufferList;
    private final Map<String, SoundEmissorAL> soundSourceMap;
    private final Matrix4f cameraMatrix;

    public SoundManager() {
        soundBufferList = new ArrayList<>();
        soundSourceMap = new HashMap<>();
        cameraMatrix = new Matrix4f();
    }

    public void init() throws Exception {
        this.device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        this.context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        }
        alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
    }

    public void addSoundSource(String name, SoundEmissorAL soundSource) {
        this.soundSourceMap.put(name, soundSource);
    }

    public boolean contieneSource(String name) {
        return this.soundSourceMap.containsKey(name);
    }

    public SoundEmissorAL getSoundSource(String name) {
        return this.soundSourceMap.get(name);
    }

    public void playSoundSource(String name) {
        SoundEmissorAL soundSource = this.soundSourceMap.get(name);
        if (soundSource != null && !soundSource.isPlaying()) {
            soundSource.play();
        }
    }

    public void removeSoundSource(String name) {
        this.soundSourceMap.remove(name);
    }

    public void addSoundBuffer(AudioBufferAL soundBuffer) {
        this.soundBufferList.add(soundBuffer);
    }

    public SoundListenerAL getListener() {
        return this.listener;
    }

    public void setListener(SoundListenerAL listener) {
        this.listener = listener;
    }

    public void updateListenerPosition(Entity entity) {
        // Update camera matrix with camera data
//        Transformation.updateGenericViewMatrix(camera.getPosition(), camera.getRotation(), cameraMatrix);        
        listener.setPosition(entity.getTransformacion().getTraslacion());
        listener.setOrientation(entity.getDirection(), entity.getUp());
    }

    public void setAttenuationModel(int model) {
        alDistanceModel(model);
    }

    public void cleanup() {
        for (SoundEmissorAL soundSource : soundSourceMap.values()) {
            soundSource.cleanup();
        }
        soundSourceMap.clear();
        for (AudioBufferAL soundBuffer : soundBufferList) {
            soundBuffer.cleanup();
        }
        soundBufferList.clear();
        if (context != NULL) {
            alcDestroyContext(context);
        }
        if (device != NULL) {
            alcCloseDevice(device);
        }
    }
}
