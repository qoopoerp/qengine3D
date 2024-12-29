/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.fragment;

import net.qoopo.engine.core.entity.component.EntityComponent;

/**
 * Componente que tiene un shader personalizado, si una entity tiene este
 * shaderm se usara en lugar del default
 *
 * @author alberto
 */
public class FragmentShaderComponent extends EntityComponent {

    private FragmentShader shader;

    public FragmentShaderComponent() {
    }

    public FragmentShaderComponent(FragmentShader shader) {
        this.shader = shader;
    }

    public FragmentShader getShader() {
        return shader;
    }

    public void setShader(FragmentShader shader) {
        this.shader = shader;
    }

    @Override
    public void destruir() {
        shader = null;
    }

}
