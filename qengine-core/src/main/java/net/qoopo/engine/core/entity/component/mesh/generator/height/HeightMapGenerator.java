package net.qoopo.engine.core.entity.component.mesh.generator.height;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.util.image.ImgReader;

@Getter
@Setter
public class HeightMapGenerator implements HeightsGenerator {

    private static final int MAX_COLOR = 255 * 255 * 255;

    protected BufferedImage imagen;
    private float minHeight = 0;
    private float maxHeight = 1;

    public HeightMapGenerator(BufferedImage imagen, float minHeight, float maxHeight) {
        this.imagen = imagen;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public HeightMapGenerator(File heigthMap, float minHeight, float maxHeight) {
        try {
            imagen = ImgReader.leerImagen(heigthMap);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    @Override
    public float getHeight(float x, float z) {
        int rgb = imagen.getRGB((int) (x * imagen.getWidth()), (int) (z * imagen.getHeight()));
        return minHeight + Math.abs(maxHeight - minHeight) * ((float) rgb / (float) MAX_COLOR);
    }

}
