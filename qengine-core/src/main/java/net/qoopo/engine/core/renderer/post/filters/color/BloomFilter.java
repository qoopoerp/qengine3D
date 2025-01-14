/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer.post.filters.color;

import net.qoopo.engine.core.renderer.post.FilterTexture;
import net.qoopo.engine.core.renderer.post.filters.blur.BlurFilter;
import net.qoopo.engine.core.texture.Texture;

/**
 * Realiza una modificación del color final de la imagen realzando el contraste
 *
 * @author alberto
 */
public class BloomFilter implements FilterTexture {

    private BlurFilter blur = null;
    private BrigthFilter brillo = null;
    private MixFilter combina = null;
    private HightContrastFilter contraste = null;

    public BloomFilter() {

        this.blur = new BlurFilter(10);
        this.brillo = new BrigthFilter(0.85f);
        this.combina = new MixFilter();
        this.contraste = new HightContrastFilter(0.3f);
    }

    public BloomFilter(float brillo) {

        this.blur = new BlurFilter(10);
        this.brillo = new BrigthFilter(brillo);
        this.combina = new MixFilter();
        this.contraste = new HightContrastFilter(0.3f);
    }

    @Override
    public Texture apply(Texture... buffer) {
        Texture textura = buffer[0];
        Texture output = new Texture(textura.getWidth(), textura.getHeight());
        try {
            output.loadTexture(contraste.apply(combina.apply(textura, blur.apply(brillo.apply(textura)))).getImagen());
        } catch (Exception e) {

        }
        return output;

    }

}
