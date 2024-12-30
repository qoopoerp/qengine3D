/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.util.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 *
 * @author alberto
 */
public class ImgReader {

    private static Logger log = Logger.getLogger("img-reader");

    public static BufferedImage read(File archivo) throws IOException {

        try {
            if (archivo.getAbsolutePath().toLowerCase().endsWith(".tga")) {
                return TargaReader.getImage(archivo.getAbsolutePath());
            } else {
                return ImageIO.read(archivo);
            }
        } catch (Exception e) {
            // si el archivo no existe apuntamos a la textura uv
            log.severe("[x] Error al cargar " + archivo.getAbsolutePath() + " --> " + e.getLocalizedMessage());
            return ImageIO.read(ImgReader.class.getResourceAsStream("textures/uv.png"));
        }
    }

    public static BufferedImage read(InputStream is) throws IOException {
        // if (archivo.getAbsolutePath().toLowerCase().endsWith(".tga")) {
        // return TargaReader.getImage(archivo.getAbsolutePath());
        // } else {
        return ImageIO.read(is);
        // }
    }
}
