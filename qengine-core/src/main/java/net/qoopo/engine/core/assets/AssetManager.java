/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.assets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.assets.audio.AudioBuffer;
import net.qoopo.engine.core.assets.audio.AudioLoader;
import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.renderer.RendererFactory;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.util.image.ImgReader;

/**
 * Gestiona los recursos utilizados por el engine
 *
 * @author alberto
 */
@Getter
@Setter
public class AssetManager implements Serializable {

    private static AssetManager INSTANCE;
    private static Logger logger = Logger.getLogger("asset-manager");
    public static Map<String, Object> mapa = new HashMap<>();
    public ModelLoader modelLoader = null;
    private AudioLoader audioLoader;

    private RendererFactory rendererFactory;

    private Object defaultShader;
    private Object envProbeShader;

    public static AssetManager get() {
        if (INSTANCE == null) {
            INSTANCE = new AssetManager();
        }
        return INSTANCE;
    }

    public void free() {
        logger.info("Liberando recursos");
        try {
            for (Object objeto : mapa.values()) {
                if (objeto instanceof AudioBuffer) {
                    logger.info(" Liberando recurso de audio..");
                    ((AudioBuffer) objeto).cleanup();
                } else if (objeto instanceof Texture) {
                    logger.info(" Liberando recurso de textura..");
                    ((Texture) objeto).destroy();
                } else if (objeto instanceof Entity) {
                    logger.info(" Liberando recurso de textura..");
                    ((Entity) objeto).destruir();
                }
            }
            mapa.clear();
            logger.info("Recursos liberados");
        } catch (Exception e) {

        }
    }

    public void addResource(String clave, Object valor) {
        mapa.put(clave, valor);
    }

    public Object getResource(String clave) {
        return mapa.get(clave);
    }

    public Texture loadTexture(String clave, String file) {
        return loadTexture(clave, new File(file));
    }

    public Entity getModel(String clave) {
        return (Entity) getResource(clave);
    }

    public Texture getTextura(String clave) {
        return (Texture) getResource(clave);
    }

    public AudioBuffer getAudio(String clave) {
        return (AudioBuffer) getResource(clave);
    }

    public AudioBuffer loadAudio(String file, String clave) {
        return loadAudio(new File(file), clave);
    }

    public AudioBuffer loadAudio(File file, String clave) {
        AudioBuffer bufferAudio = null;
        try {
            if (!mapa.containsKey(clave)) {
                logger.info(" Cargando audio " + file.getAbsolutePath());
                bufferAudio = audioLoader.load(file);
                addResource(clave, bufferAudio);
            } else {
                return getAudio(clave);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bufferAudio;
    }

    public Texture loadTexture(File file) {
        try {
            return loadTexture(file.getAbsolutePath(), file);
        } catch (Exception ex) {
            System.out.println("Error al cargar la textura " + file.getAbsolutePath());
            ex.printStackTrace();
        }
        return null;
    }

    public Texture loadTexture(String file) {
        return loadTexture(new File(file));
    }

    public Texture loadTexture(String clave, File file) {
        try {
            if (!mapa.containsKey(clave)) {
                Texture text = new Texture();
                logger.info("  Cargando textura " + file.getName());
                text.loadTexture(ImgReader.read(file));
                addResource(clave, text);
                return text;
            } else {
                return getTextura(clave);
            }
        } catch (Exception ex) {
            System.out.println("Error al cargar la textura " + file.getAbsolutePath());
            ex.printStackTrace();
        }
        return null;
    }

    public Entity loadModel(File file) throws FileNotFoundException {
        if (!mapa.containsKey(file.getAbsolutePath())) {
            Entity entity = modelLoader.loadModel(file);
            addResource(file.getAbsolutePath(), entity);
            return entity;
        } else {
            return getModel(file.getAbsolutePath());
        }
    }

    public Entity loadModel(String file) throws FileNotFoundException {
        if (!mapa.containsKey(file)) {
            Entity entity = modelLoader.loadModel(new File(file));
            addResource(file, entity);
            return entity;
        } else {
            return getModel(file);
        }
    }

}
