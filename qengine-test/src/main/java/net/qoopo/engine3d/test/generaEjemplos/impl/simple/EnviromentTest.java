/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.environment.EnvProbe;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.material.Material;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.test.generaEjemplos.MakeTestScene;

/**
 * esferas con diferentes valores de
 * rugosidad y metalico , y un mapa de reflexiones
 * 
 * @author alberto
 */
public class EnviromentTest extends MakeTestScene {

    public EnviromentTest() {

    }

    public void make(Scene escena) {
        this.scene = escena;

        // la entidad reflexion se encargara de renderizar el mapa de reflejos
        Entity reflexion = new Entity("cubemap");
        EnvProbe envProbe = new EnvProbe();
        envProbe.setGenerarIrradiacion(true);
        reflexion.addComponent(envProbe);
        envProbe.aplicar(EnvProbe.FORMATO_MAPA_CUBO);

        for (Entity entidad : scene.getEntities()) {
            if (!(entidad instanceof Camera)) {
                entidad.getComponents(Mesh.class).forEach(c -> {
                    for (Primitive p : ((Mesh) c).primitiveList) {
                        if (p.material instanceof Material) {
                            ((Material) p.material).setEnvMapType(EnvProbe.FORMATO_MAPA_CUBO);
                            ((Material) p.material).setEnvMap(envProbe.getEnvMap());
                            ((Material) p.material).setHdrMap(envProbe.getHdrMap());
                            ((Material) p.material).setIor(1.45f);
                            ((Material) p.material).setRefraccion(true);
                            ((Material) p.material).setReflexion(true);
                        }
                    }
                });
            }
        }

        escena.addEntity(reflexion);

    }

    @Override
    public void accion(int numAccion, RenderEngine render) {
    }

}
