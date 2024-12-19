package net.qoopo.engine.core.lwjgl.audio.component;

import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.AL_VELOCITY;
import static org.lwjgl.openal.AL10.alListener3f;
import static org.lwjgl.openal.AL10.alListenerfv;
//import static org.lwjgl.openal.AL10.*;

import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.math.QVector3;

public class SoundListenerAL extends EntityComponent {

    public SoundListenerAL() {
        this(QVector3.of(0, 0, 0));
    }

    public SoundListenerAL(QVector3 position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
        alListener3f(AL_VELOCITY, 0, 0, 0);
    }

    public void setSpeed(QVector3 speed) {
        alListener3f(AL_VELOCITY, speed.x, speed.y, speed.z);
    }

    public void setPosition(QVector3 position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
    }

    public void setOrientation(QVector3 at, QVector3 up) {
        float[] data = new float[6];
        data[0] = at.x;
        data[1] = at.y;
        data[2] = at.z;
        data[3] = up.x;
        data[4] = up.y;
        data[5] = up.z;
        alListenerfv(AL_ORIENTATION, data);
    }

    @Override
    public void destruir() {
    }
}
