package net.qoopo.engine.core.lwjgl.asset;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.texture.Texture;

public class TextureCache {

    private static TextureCache INSTANCE;

    private Map<String, Texture> texturesMap;

    private TextureCache() {
        texturesMap = new HashMap<>();
    }

    public static synchronized TextureCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TextureCache();
        }
        return INSTANCE;
    }

    public Texture getTexture(String path) throws Exception {
        Texture texture = texturesMap.get(path);
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
