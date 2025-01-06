package net.qoopo.engine.water;

import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlanarMesh;
import net.qoopo.engine.core.entity.component.water.Water;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine.core.util.mesh.NormalUtil;
import net.qoopo.engine.water.distortion.SimpleWaveDistortion;
import net.qoopo.engine.water.distortion.WaveDistortion;

public class LowPolyWater extends Water {

    private WaveDistortion waveDistortion;
    private int sections;

    public LowPolyWater(Scene scene, int width, int height, int sections) {
        this.width = width;
        this.height = height;
        this.scene = scene;
        this.sections = sections;
        waveDistortion = new SimpleWaveDistortion(3.0f, 20.0f, 1.0f);
        // waveDistortion = new GerstnerWaveDistortion(new QVector2(1.8f, 0.4f), 3.0f,
        // 20.0f, 1.0f);
        // waveDistortion = new RadialWavesDistortion(QVector3.zero, 3.0f, 20.0f,
        // 1.0f,100f);
        // waveDistortion = new ThinMatrixWaveDistortion(3.0f, 20.0f, 1.0f);

    }

    public LowPolyWater(Scene scene, int width, int height, int sections, WaveDistortion waveDistortion) {
        this.width = width;
        this.height = height;
        this.scene = scene;
        this.sections = sections;
        this.waveDistortion = waveDistortion;

    }

    public void build() {
        // enableReflection = false;
        // enableRefraction = false;
        super.build();
        // Material
        material = new QMaterialBas("water");
        material.setColorBase(new QColor(1, 0, 0, 0.7f));
        material.setSpecularExponent(64);
        // material.setMapaNormal(getTextNormal());
        material.setMapaColor(getOutputTexture());
        mesh = new PlanarMesh(width, height, sections, sections);
        NormalUtil.computeNormals(mesh);
        MaterialUtil.smooth(mesh, false);
        MaterialUtil.applyMaterial(mesh, material);
        entity.addComponent(mesh);
    }

    @Override
    public void update(RenderEngine mainRender, Scene universo) {
        super.update(mainRender, universo);
        // regenera el plano
        // mesh.build();
        mesh.reset(); // reinicia las coordenadas de los vértices para que no se acumulen las
                      // distorciones
        // QMaterialUtil.aplicarMaterial(mesh, material);
        // aplica distorsion
        waveDistortion.apply(mesh);

    }

    public void destruir() {
        super.destruir();
    }
}
