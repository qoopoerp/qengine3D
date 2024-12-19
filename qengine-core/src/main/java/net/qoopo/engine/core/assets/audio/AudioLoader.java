package net.qoopo.engine.core.assets.audio;

import java.io.File;

public interface AudioLoader {

    public AudioBuffer load(File file);
}
