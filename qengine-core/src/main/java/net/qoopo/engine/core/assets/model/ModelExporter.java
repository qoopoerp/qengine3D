package net.qoopo.engine.core.assets.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import net.qoopo.engine.core.entity.Entity;

public interface ModelExporter {

    public default void exportModel(File file, Entity entity) {
        try {
            file.createNewFile();
            exportModel(new FileOutputStream(file), entity);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void exportModel(OutputStream output, Entity entity);
}
