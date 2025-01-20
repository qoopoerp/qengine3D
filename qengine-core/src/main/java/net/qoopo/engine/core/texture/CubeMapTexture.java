package net.qoopo.engine.core.texture;

import java.awt.image.BufferedImage;

import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.Vector3;

/**
 * Una textura que tiene la forma de CubeMap
 */
public class CubeMapTexture extends Texture {

    public CubeMapTexture() {

    }

    public CubeMapTexture(BufferedImage image) {
        super(image);
    }

    public QColor getColor(Vector3 vector) {
        try {

            float absX = QMath.abs(vector.x);
            float absY = QMath.abs(vector.y);
            float absZ = QMath.abs(vector.z);

            boolean isXPositive = vector.x > 0;
            boolean isYPositive = vector.y > 0;
            boolean isZPositive = vector.z > 0;

            float maxAxis = 0, uc = 0, vc = 0;
            float u, v;

            float factorU = 1.0f / 4.0f;
            float factorV = 1.0f / 3.0f;
            float uOffset = 0;
            float vOffset = 0;

            // POSITIVE Y (Arriba)
            if (isYPositive && absY >= absX && absY >= absZ) {
                // u (0 to 1) goes from -x to +x
                // v (0 to 1) goes from +z to -z
                maxAxis = absY;
                uc = vector.x;
                vc = -vector.z;
                uOffset = factorU;
                vOffset = factorV * 2;
            }

            // NEGATIVE X (izquierda)
            if (!isXPositive && absX >= absY && absX >= absZ) {
                // u (0 to 1) goes from -z to +z
                // v (0 to 1) goes from -y to +y
                maxAxis = absX;
                uc = vector.z;
                vc = vector.y;
                uOffset = 0;
                vOffset = factorV;
            }

            // POSITIVE Z (adelante)
            if (isZPositive && absZ >= absX && absZ >= absY) {
                // u (0 to 1) goes from -x to +x
                // v (0 to 1) goes from -y to +y
                maxAxis = absZ;
                uc = vector.x;
                vc = vector.y;
                uOffset = factorU;
                vOffset = factorV;
            }
            // POSITIVE X (derecha)
            if (isXPositive && absX >= absY && absX >= absZ) {
                // u (0 to 1) goes from +z to -z
                // v (0 to 1) goes from -y to +y
                maxAxis = absX;
                uc = -vector.z;
                vc = vector.y;
                uOffset = 2 * factorU;
                vOffset = factorV;
            }

            // NEGATIVE Z (atras)
            if (!isZPositive && absZ >= absX && absZ >= absY) {
                // u (0 to 1) goes from +x to -x
                // v (0 to 1) goes from -y to +y
                maxAxis = absZ;
                uc = -vector.x;
                vc = vector.y;
                uOffset = 3 * factorU;
                vOffset = factorV;
            }

            // NEGATIVE Y (abajo)
            if (!isYPositive && absY >= absX && absY >= absZ) {
                // u (0 to 1) goes from -x to +x
                // v (0 to 1) goes from -z to +z
                maxAxis = absY;
                uc = vector.x;
                vc = vector.z;
                uOffset = factorU;
                vOffset = 0;
            }

            // Convert range from -1 to 1 to 0 to 1
            u = 0.5f * (uc / maxAxis + 1.0f);
            v = 0.5f * (vc / maxAxis + 1.0f);

            // convierte la coordenada de la subtextura a la textura global
            u = u * factorU + uOffset;
            v = v * factorV + vOffset;
            return getQColor(u, v);
        } catch (Exception e) {
            // e.printStackTrace();
            return QColor.BLACK;
        }
    }

}
