/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post;

import net.qoopo.engine.core.texture.Texture;

/**
 *
 * @author alberto
 */
public interface FilterTexture {

    public Texture apply(Texture... buffer);
}
