/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.load.collada.thinmatrix.loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.qoopo.engine.core.load.collada.thinmatrix.xmlParser.XmlNode;
import net.qoopo.engine.core.util.image.ImgReader;

/**
 *
 * @author alberto
 */
public class TexturasLoader {

    private final XmlNode nodo;
    private File ruta;
    private final Map<String, BufferedImage> textures = new HashMap<>();

    public TexturasLoader(XmlNode nodo, File ruta) {

//        this.meshData = nodo.getChild("image");
        this.nodo = nodo;
        this.ruta = ruta;
    }

    public Map<String, BufferedImage> extractModelData() {
        leer();
        return textures;
    }

    private void leer() {
        textures.clear();
        List<XmlNode> items = nodo.getChildren("image");
        String nombreArchivo = "";
        File archivo;
        FileInputStream is = null;
        for (XmlNode item : items) {
            try {
                nombreArchivo = item.getChild("init_from").getData();
                archivo = new File(ruta, nombreArchivo);
                is = new FileInputStream(archivo);
                BufferedImage biProv = ImgReader.read(is);
//                textures.put(item.getAttribute("id"), biProv);
                textures.put(item.getName(), biProv);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (Exception e) {

                }
            }

        }
    }

}
