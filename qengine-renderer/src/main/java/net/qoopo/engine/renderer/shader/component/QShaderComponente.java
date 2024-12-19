/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shader.component;

import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.renderer.shader.pixelshader.QShader;

/**
 * Componente que tiene un shader personalizado, si una entity tiene este
 * shaderm se usara en lugar del default
 *
 * @author alberto
 */
public class QShaderComponente extends EntityComponent {

    private QShader shader;

    public QShaderComponente() {
    }

    public QShaderComponente(QShader shader) {
        this.shader = shader;
    }

    public QShader getShader() {
        return shader;
    }

    public void setShader(QShader shader) {
        this.shader = shader;
    }

    @Override
    public void destruir() {
        shader = null;
    }

}
