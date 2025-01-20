package net.qoopo.engine.core.load.collada.loader2;

import net.qoopo.engine.core.math.Vector4;

/**
 * Triangle class.
 * 
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class Triangle {

    private final Vector4[] points = new Vector4[3];

    public Triangle(Vector4 a, Vector4 b, Vector4 c) {
        points[0] = a;
        points[1] = b;
        points[2] = c;
    }

    public Vector4[] getPoints() {
        return points;
    }
    
}
