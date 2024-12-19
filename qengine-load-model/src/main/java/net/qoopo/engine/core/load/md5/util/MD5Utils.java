package net.qoopo.engine.core.load.md5.util;

import org.joml.Vector3f;

import net.qoopo.engine.core.math.Cuaternion;

public class MD5Utils {

    public static final String FLOAT_REGEXP = "[+-]?\\d*\\.?\\d*";

    public static final String VECTOR3_REGEXP = "\\(\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*(" + FLOAT_REGEXP + ")\\s*\\)";

    private MD5Utils() {
    }

    public static Cuaternion calculateQuaternion(Vector3f vec) {
        Cuaternion orientation = new Cuaternion(vec.x, vec.y, vec.z, 0);
        float temp = 1.0f - (orientation.x * orientation.x) - (orientation.y * orientation.y) - (orientation.z * orientation.z);
        if (temp < 0.0f) {
            orientation.w = 0.0f;
        } else {
            orientation.w = -(float) (Math.sqrt(temp));
        }
        return orientation;
    }
}
