/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.util;

import java.util.ArrayList;
import java.util.List;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;

/**
 *
 * @author alberto
 */
public class ComponentUtil {

    public static Mesh getMesh(Entity entity) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof Mesh) {
                return (Mesh) componente;
            }
        }
        return null;
    }

    public static EntityComponent getComponent(Entity entity, Class<? extends EntityComponent> className) {
        for (EntityComponent componente : entity.getComponents()) {
            if (className.isAssignableFrom(componente.getClass())) {
                return componente;
            }
        }
        return null;
    }

    public static List<? extends EntityComponent> getComponents(Entity entity,
            Class<? extends EntityComponent> className) {
        List<EntityComponent> list = new ArrayList<>();
        for (EntityComponent componente : entity.getComponents()) {
            if (className.isAssignableFrom(componente.getClass())) {
                list.add(componente);
            }
        }
        return list;
    }

    public static void removeComponents(Entity entity, Class<? extends EntityComponent> className) {
        List<EntityComponent> tmp = new ArrayList<>();
        for (EntityComponent componente : entity.getComponents()) {
            if (className.isAssignableFrom(componente.getClass())) {
                componente.destruir();
                tmp.add(componente);
            }
        }
        entity.getComponents().removeAll(tmp);
    }

}
