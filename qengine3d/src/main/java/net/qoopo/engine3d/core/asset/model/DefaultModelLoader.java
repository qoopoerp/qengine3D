package net.qoopo.engine3d.core.asset.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.assets.model.waveobject.LoadModelObj;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.load.ascii.LoadAsciiModel;
import net.qoopo.engine.core.load.collada.thinmatrix.LoadModelDae;
import net.qoopo.engine.core.load.md5.LoadModelMd5;
import net.qoopo.engine.core.lwjgl.asset.AssimpModelLoader;
import net.qoopo.engine3d.core.asset.model.qengine.LoadModelQEngine;
import net.qoopo.engine3d.core.asset.model.studiomax.LoadModel3DSMax;

public class DefaultModelLoader implements ModelLoader {

    @Override
    public Entity loadModel(InputStream stream) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadModel'");
    }

    public Entity loadModel(File file) throws FileNotFoundException {

        ModelLoader loader;

        if (file.getName().toLowerCase().endsWith("txt")) {
            loader = new LoadAsciiModel();
        } else if (file.getName().toLowerCase().endsWith("3ds")) {
            loader = new LoadModel3DSMax();
        } else if (file.getName().toLowerCase().endsWith("qengine")) {
            loader = new LoadModelQEngine();
        } else if (file.getName().toLowerCase().endsWith("dae")) {
            loader = new LoadModelDae();
        } else if (file.getName().toLowerCase().endsWith("md5mesh")) {
            loader = new LoadModelMd5();
        } else if (file.getName().toLowerCase().endsWith("obj")) {
            loader = new LoadModelObj();
        } else {
            loader = new AssimpModelLoader();
        }

        return loader.loadModel(file);

    }

}
