package net.qoopo.engine.core.lwjgl.asset;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.texture.QTextura;

public class TextureCache {

    private static TextureCache INSTANCE;

    private Map<String, QTextura> texturesMap;

    private TextureCache() {
        texturesMap = new HashMap<>();
    }

    public static synchronized TextureCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TextureCache();
        }
        return INSTANCE;
    }

    public QTextura getTexture(String path) throws Exception {
        QTextura texture = texturesMap.get(path);
        if (texture == null) {
//            texture = new QTextura(path);
            texture = AssetManager.get().loadTexture(path, new File(path));
            if (texture != null) {
                texturesMap.put(path, texture);
            }
        }
        return texture;
    }
}
