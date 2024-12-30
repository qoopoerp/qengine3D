/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.assets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import net.qoopo.engine.core.texture.QTextura;
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
                } else if (objeto instanceof QTextura) {
                    logger.info(" Liberando recurso de textura..");
                    ((QTextura) objeto).destruir();
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

    public void agregarRecurso(String clave, Object valor) {
        mapa.put(clave, valor);
    }

    public Object getRecurso(String clave) {
        return mapa.get(clave);
    }

    public QTextura loadTexture(String clave, String file) {
        return loadTexture(clave, new File(file));
    }

    public QTextura getTextura(String clave) {
        return (QTextura) getRecurso(clave);
    }

    public AudioBuffer getAudio(String clave) {
        return (AudioBuffer) getRecurso(clave);
    }

    public AudioBuffer loadAudio(String file, String clave) {
        return loadAudio(new File(file), clave);
    }

    public AudioBuffer loadAudio(File file, String clave) {
        AudioBuffer bufferAudio = null;
        try {
            logger.info(" Cargando audio " + file.getAbsolutePath());
            bufferAudio = audioLoader.load(file);
            agregarRecurso(clave, bufferAudio);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bufferAudio;
    }

    public QTextura loadTexture(String clave, File file) {
        try {
            QTextura text = new QTextura();
            logger.info("  Cargando textura " + file.getName());
            text.loadTexture(ImgReader.read(file));
            agregarRecurso(clave, text);
            return text;
        } catch (IOException ex) {
            System.out.println("Error al cargar la textura " + file.getAbsolutePath());
            ex.printStackTrace();
        }
        return null;
    }

    public Entity loadModel(File file) throws FileNotFoundException {
        Entity entity = modelLoader.loadModel(file);
        agregarRecurso(entity.getName(), entity);
        return entity;
    }

    public Entity loadModel(String file) throws FileNotFoundException {
        Entity entity = modelLoader.loadModel(new File(file));
        agregarRecurso(entity.getName(), entity);
        return entity;
    }

}
