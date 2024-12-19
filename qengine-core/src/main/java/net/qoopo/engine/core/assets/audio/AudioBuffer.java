package net.qoopo.engine.core.assets.audio;

import java.io.Serializable;

public interface AudioBuffer extends Serializable {
    
    public int getBufferId();
    public void cleanup();
}
