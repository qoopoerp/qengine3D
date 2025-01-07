package net.qoopo.engine.core.entity.component.water;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Plane;
import net.qoopo.engine.core.entity.component.water.texture.WaterTextureProcessor;
import net.qoopo.engine.core.material.basico.Material;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.Texture;
import net.qoopo.engine.core.texture.util.MaterialUtil;

@Getter
@Setter
public class WaterDuDv extends Water {

    protected final static int MUESTRAS_TEXTURAS = 2;
    protected Texture textNormal = null;
    protected Texture dudvMaps = null;

    public WaterDuDv(Scene scene, int width, int height) {
        this.width = width;
        this.height = height;
        this.scene = scene;
    }

    public void build() {
        super.build();
        try {
            textNormal = AssetManager.get().loadTexture("textNormal",
                    "assets/textures/water/waterNormal.png");
            textNormal.setMuestrasU(MUESTRAS_TEXTURAS);
            textNormal.setMuestrasV(MUESTRAS_TEXTURAS);
        } catch (Exception e) {
        }
        try {
            dudvMaps = AssetManager.get().loadTexture("dudvMaps", "assets/textures/water/waterDUDV.png");
            dudvMaps.setMuestrasU(MUESTRAS_TEXTURAS);
            dudvMaps.setMuestrasV(MUESTRAS_TEXTURAS);
        } catch (Exception e) {
        }

        // sobrecarga la textura de salida con el mapa de distorsion
        outputTexture = new WaterTextureProcessor(textReflexion, textRefraccion, dudvMaps);

        // Material
        material = new Material("water");
        material.setColor(new QColor(1, 0, 0, 0.7f));
        material.setSpecularExponent(64);
        material.setMapaNormal(getTextNormal());
        material.setMapaColor(getOutputTexture());

        mesh = new Plane(width, height);

        entity.addComponent(MaterialUtil.applyMaterial(mesh, material));
    }

    @Override
    public void update(RenderEngine mainRender, Scene universo) {
        // evita la ejecucion si no esta activo los materiales

        if (!mainRender.opciones.isMaterial()) {
            return;
        }
        if (mainRender.getFrameBuffer() == null) {
            return;
        }

        super.update(mainRender, universo);

        if (textNormal != null) {
            textNormal.setOffsetX(factorX);
            textNormal.setOffsetY(factorY);
        }

    }

    public void destruir() {
        super.destruir();
        if (textNormal != null) {
            textNormal.destruir();
            textNormal = null;
        }
    }

}
