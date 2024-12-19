/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.core.asset.model.qengine;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine3d.core.util.SerializarUtil;

/**
 *
 * @author alberto
 */
public class LoadModelQEngine implements ModelLoader {

    public LoadModelQEngine() {
    }

    @Override
    public Entity loadModel(File file) {
        try {

            List<Entity> lista = new ArrayList<>();
            // Entity entity = (Entity)
            // SerializarUtil.leerObjeto(archivo.getAbsolutePath());

            int tamanio = SerializarUtil.leerObjetos(file.getAbsolutePath()).size();
            for (int i = 0; i < tamanio; i++) {
                lista.add((Entity) SerializarUtil.leerObjeto(file.getAbsolutePath(), i, true));

            }

            if (lista != null && !lista.isEmpty()) {
                if (lista.size() == 1) {
                    return lista.get(0);
                } else {
                    Entity entity = new Entity("output");
                    lista.forEach(child -> entity.addChild(child));
                    return entity;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    @Override
    public Entity loadModel(InputStream stream) {
        return null;
    }

}
