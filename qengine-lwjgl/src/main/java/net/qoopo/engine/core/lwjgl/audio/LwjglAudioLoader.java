package net.qoopo.engine.core.lwjgl.audio;

import java.io.File;

import net.qoopo.engine.core.assets.audio.AudioBuffer;
import net.qoopo.engine.core.assets.audio.AudioLoader;
import net.qoopo.engine.core.lwjgl.audio.component.AudioBufferAL;

public class LwjglAudioLoader implements AudioLoader {

    @Override
    public AudioBuffer load(File file) {
        AudioBufferAL bufferAudio = null;
        try {
            bufferAudio = new AudioBufferAL(file.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bufferAudio;
    }

}
