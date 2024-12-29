/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.mirros;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlanoBillboard;
import net.qoopo.engine.core.entity.component.mesh.util.QUnidadMedida;
import net.qoopo.engine.core.entity.component.reflections.PlanarReflection;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.util.MaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;
import net.qoopo.engine3d.test.generaEjemplos.impl.textura.TexturedCubeTest;

/**
 *
 * @author alberto
 */
public class Espejos extends MakeTestScene {

    public void make(Scene mundo) {
        this.scene = mundo;

        int anchoEspejos = 200;
        int altoEspejos = 200;
        // agrego los espejos
        Entity espejo1 = new Entity("espejo1");
        QTextura texEspejo1 = new QTextura();
        espejo1.addComponent(MaterialUtil.applyMaterial(new QPlanoBillboard(2, 2), new QMaterialBas(texEspejo1)));
        espejo1.addComponent(new PlanarReflection(texEspejo1, mundo,
                anchoEspejos,
                altoEspejos));
        espejo1.move(-mundo.UM.convertirPixel(2, QUnidadMedida.METRO), 1,
                -mundo.UM.convertirPixel(2, QUnidadMedida.METRO));

        espejo1.rotate(0, (float) Math.toRadians(45), 0);

        mundo.addEntity(espejo1);

        Entity espejo2 = new Entity("espejo2");
        QTextura textEspejo2 = new QTextura();
        espejo2.addComponent(MaterialUtil.applyMaterial(new QPlanoBillboard(2, 2), new QMaterialBas(textEspejo2)));
        espejo2.addComponent(new PlanarReflection(textEspejo2, mundo,
                anchoEspejos,
                altoEspejos));
        espejo2.move(mundo.UM.convertirPixel(2, QUnidadMedida.METRO), 1,
                mundo.UM.convertirPixel(2, QUnidadMedida.METRO));

        espejo2.rotate(0, (float) Math.toRadians(135), 0);

        mundo.addEntity(espejo2);

        Entity espejo3 = new Entity("espejo3");
        QTextura textEspejo3 = new QTextura();
        espejo3.addComponent(MaterialUtil.applyMaterial(new QPlanoBillboard(2, 2), new QMaterialBas(textEspejo3)));
        espejo3.addComponent(new PlanarReflection(textEspejo3, mundo,
                anchoEspejos,
                altoEspejos));
        espejo3.move(0, 3, -3);

        espejo3.rotate((float) Math.toRadians(35), 0, 0);
        mundo.addEntity(espejo3);

        Entity espejoCentral = new Entity("espejoEsfera");
        QTextura textEspejoEsfera = new QTextura();
        QMaterialBas mat = new QMaterialBas();
        mat.setMapaColor(new QProcesadorSimple(textEspejoEsfera));
        espejoCentral.addComponent(MaterialUtil.applyMaterial(new Sphere(0.5f), mat));
        espejoCentral.addComponent(new PlanarReflection(textEspejoEsfera, mundo,
                anchoEspejos,
                altoEspejos));
        espejoCentral.move(0, 0.5f, 0);

        espejoCentral.rotate(0, (float) Math.toRadians(180), 0);

        mundo.addEntity(espejoCentral);

        // lugo realizo agrego otromundo
        // QLuz luz = new QLuz(QLuz.TYPE_POINT, 8.5f, 255, 0, 128, 0, 2f, 0, true);
        MakeTestScene ejemplo;
        ejemplo = new TexturedCubeTest();
        // ejemplo = new EsferaAnimada();
        ejemplo.make(mundo);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
