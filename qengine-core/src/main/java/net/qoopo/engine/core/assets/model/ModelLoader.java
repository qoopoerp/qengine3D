package net.qoopo.engine.core.assets.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.qoopo.engine.core.entity.Entity;

public interface ModelLoader {

    public default Entity loadModel(File file) throws FileNotFoundException {
        return loadModel(new FileInputStream(file));
    }

    public Entity loadModel(InputStream stream);
}
