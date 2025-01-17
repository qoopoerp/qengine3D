/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene.simple;

import java.io.File;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.environment.EnvProbe;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.renderer.post.filters.blur.BlurFilter;
import net.qoopo.engine.core.renderer.post.filters.color.BloomFilter;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.procesador.MipmapTexture;
import net.qoopo.engine3d.test.scene.MakeTestScene;

/**
 *
 * @author alberto
 */
public class HdriTest extends MakeTestScene {

    public HdriTest() {

    }

    public void make(Scene mundo) {
        this.scene = mundo;

        Texture texture = AssetManager.get().loadTexture("entornoDifuso",
                new File("assets/hdri/qwantani_dusk_2_2k.png"));
        Texture envMap = new MipmapTexture(texture, 5, MipmapTexture.TIPO_BLUR);
        Texture hdrMap = new Texture();
        // texture.getWidth() / 2, texture.getHeight() / 2
        BloomFilter bloom = new BloomFilter();// ,0.6f);
        BlurFilter blur = new BlurFilter(20);
        hdrMap.loadTexture(blur.apply(bloom.apply(texture)).getImagen());

        // a todas las entidades les agrega en el material el mapa de entorno el hdri

        for (Entity entidad : mundo.getEntities()) {
            if (!(entidad instanceof Camera)) {
                entidad.getComponents(Mesh.class).forEach(c -> {
                    for (Primitive p : ((Mesh) c).primitiveList) {
                        if (p.material instanceof Material) {
                            ((Material) p.material).setEnvMapType(EnvProbe.FORMATO_MAPA_HDRI);
                            ((Material) p.material).setEnvMap(envMap);
                            ((Material) p.material).setHdrMap(hdrMap);
                            // ((Material) p.material).setIor(1.45f);
                            // ((Material) p.material).setRefraccion(true);
                            // ((Material) p.material).setReflexion(true);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
