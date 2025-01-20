package net.qoopo.engine.core.texture;

import java.awt.image.BufferedImage;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.Vector3;

/**
 * Textura HDRI
 */
public class HDRITexture extends Texture {

    public HDRITexture() {

    }

    public HDRITexture(BufferedImage image) {
        super(image);
    }

    public QColor getColor(Vector3 vector) {
        try {
            BufferedImage img = getImagen();
            int hdriWidth = img.getWidth();
            int hdriHeight = img.getHeight();
            int newX = (int) (hdriWidth * ((Math.PI - Math.atan2(vector.x, vector.z)) / (2 * Math.PI)));
            int newY = (int) (hdriHeight * (Math.acos(vector.y / vector.length()) / Math.PI));
            newX = QMath.rotateNumber(newX, hdriWidth);
            newY = QMath.rotateNumber(newY, hdriHeight);
            return new QColor(img.getRGB(newX, newY));
        } catch (Exception e) {
            return QColor.BLACK;
        }
    }
}
