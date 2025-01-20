package net.qoopo.engine.core.math;

import java.io.Serializable;

import net.qoopo.engine.core.util.QGlobal;

public class Vector4 implements Serializable {

    public static Vector4 zero = new Vector4(0, 0, 0, 0);

    public static Vector4 unitario_x = new Vector4(1, 0, 0, 0);
    public static Vector4 unitario_y = new Vector4(0, 1, 0, 0);
    public static Vector4 unitario_z = new Vector4(0, 0, 1, 0);
    public static Vector4 unitario_w = new Vector4(0, 0, 0, 1);
    public static Vector4 unitario_xyzw = new Vector4(1, 1, 1, 1);

    public float x, y, z, w;

    public Vector4() {
        x = 0;
        y = 0;
        z = 0;
        w = 0;
    }

    public Vector4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4(Vector3 vector, float w) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        this.w = w;
    }

    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void set(Vector4 vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        this.w = vector.w;
    }

    public void set(Vector3 vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        this.w = 1;
    }

    public void set(Vector3 vector, float w) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        this.w = w;
    }

    public void normalize() {
        if (QGlobal.OPT_USAR_FAST_INVSQRT) {
            float inversaRaizCuadrada = QMath.fastInvSqrt(x * x + y * y + z * z + w * w);
            x *= inversaRaizCuadrada;
            y *= inversaRaizCuadrada;
            z *= inversaRaizCuadrada;
            w *= inversaRaizCuadrada;
        } else {
            float length = length();
            x /= length;
            y /= length;
            z /= length;
            w /= length;
        }
    }

    public Vector4 invert() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Vector4 add(float value) {
        x += value;
        y += value;
        z += value;
        // w += value;
        return this;
    }

    public Vector4 subtract(float value) {
        x -= value;
        y -= value;
        z -= value;
        // w -= value;
        return this;
    }

    public Vector4 add(Vector4... others) {
        for (Vector4 other : others) {
            x += other.x;
            y += other.y;
            z += other.z;
            // w += other.w;
        }
        return this;
    }

    public void flip() {
        x = -x;
        y = -y;
        z = -z;
    }

    public void copyXYZ(Vector4 vector) {
        set(vector.x, vector.y, vector.z, vector.w);
    }

    public Vector4 multiply(float alpha) {
        x *= alpha;
        y *= alpha;
        z *= alpha;
        // w *= alpha;
        return this;
    }

    public float length() {
        if (QGlobal.OPT_USAR_FAST_INVSQRT) {
            return 1.0f / (float) QMath.fastInvSqrt(x * x + y * y + z * z + w * w);
        } else {
            return (float) Math.sqrt(x * x + y * y + z * z + w * w);
        }
    }

    public float dot(Vector4 other) {
        return (this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w);
    }

    public Vector4 rotateX(float angle) {
        float cosAngle = (float) QMath.cos(angle);
        float sinAngle = (float) QMath.sin(angle);
        float tempY = y * cosAngle - z * sinAngle;
        z = y * sinAngle + z * cosAngle;
        y = tempY;
        return this;
    }

    public Vector4 rotateY(float angle) {
        float cosAngle = (float) QMath.cos(angle);
        float sinAngle = (float) QMath.sin(angle);
        float tempZ = z * cosAngle - x * sinAngle;
        x = z * sinAngle + x * cosAngle;
        z = tempZ;
        return this;
    }

    public Vector4 rotateZ(float angle) {
        float cosAngle = (float) QMath.cos(angle);
        float sinAngle = (float) QMath.sin(angle);
        float tempX = x * cosAngle - y * sinAngle;
        y = x * sinAngle + y * cosAngle;
        x = tempX;
        return this;
    }

    @Override
    public Vector4 clone() {
        return new Vector4(x, y, z, w);
    }

    public static Vector4 addNewVector(Vector4 v1, Vector4... others) {
        Vector4 result = v1.clone();
        for (Vector4 other : others) {
            result.x += other.x;
            result.y += other.y;
            result.z += other.z;
            result.w += other.w;
        }
        return result;
    }

    public static float dotProduct(Vector4 v1, Vector4 v2) {
        return (v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
    }

    public static Vector4 crossProduct(Vector4 v1, Vector4 v2) {
        return new Vector4(
                v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x,
                v1.w * v2.w);
    }

    public static Vector4 multiply(float alpha, Vector4 vector) {
        return new Vector4(alpha * vector.x,
                alpha * vector.y, alpha * vector.z, alpha * vector.w);
    }

    public String toString() {
        return x + ", " + y + ", " + z + "," + w;
    }

    public float get(int index) {
        switch (index) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
            case 3:
                return w;
        }
        throw new IllegalArgumentException("index must be either 0, 1 or 2");
    }

    public Vector3 getVector3() {
        return Vector3.of(x, y, z);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Float.floatToIntBits(this.x);
        hash = 47 * hash + Float.floatToIntBits(this.y);
        hash = 47 * hash + Float.floatToIntBits(this.z);
        hash = 47 * hash + Float.floatToIntBits(this.w);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vector4 other = (Vector4) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        if (Float.floatToIntBits(this.z) != Float.floatToIntBits(other.z)) {
            return false;
        }
        if (Float.floatToIntBits(this.w) != Float.floatToIntBits(other.w)) {
            return false;
        }
        return true;
    }

}
