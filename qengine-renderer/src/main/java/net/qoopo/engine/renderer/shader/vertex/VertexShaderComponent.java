/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.vertex;

import net.qoopo.engine.core.entity.component.EntityComponent;

/**
 * Componente que tiene un shader personalizado, si una entity tiene este
 * shader se usara en lugar del default
 *
 * @author alberto
 */
public class VertexShaderComponent extends EntityComponent {

    private VertexShader shader;

    public VertexShaderComponent() {
    }

    public VertexShaderComponent(VertexShader shader) {
        this.shader = shader;
    }

    public VertexShader getShader() {
        return shader;
    }

    public void setShader(VertexShader shader) {
        this.shader = shader;
    }

    @Override
    public void destruir() {
        shader = null;
    }

}
