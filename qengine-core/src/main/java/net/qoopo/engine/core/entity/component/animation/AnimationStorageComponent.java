/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.animation;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;

/**
 * Esta clase es un almacen de animaciones que tendra una entity Se puede
 * tener varias animaciones por ejemplo una animacion con el nombre "caminar"
 *
 * @author alberto
 */
public class AnimationStorageComponent implements EntityComponent {

    @Getter
    @Setter
    private Entity entity;

    private final Map<String, AnimationComponent> animaciones = new HashMap<>();

    public void add(String identificador, AnimationComponent animacion) {
        animaciones.put(identificador, animacion);
    }

    public void remove(String identificador) {
        animaciones.remove(identificador);
    }

    public AnimationComponent get(String identificador) {
        return animaciones.get(identificador);
    }

    public Map<String, AnimationComponent> getAnimaciones() {
        return animaciones;
    }

    @Override
    public void destruir() {
        animaciones.clear();
    }

}
