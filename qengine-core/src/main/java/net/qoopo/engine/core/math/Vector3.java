package net.qoopo.engine.core.math;

import java.io.Serializable;

import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.util.QGlobal;

public class Vector3 implements Serializable {

    public static final Vector3 zero = new Vector3(0.0f, 0.0f, 0.0f);
    public static final Vector3 gravedad = new Vector3(0.0f, -9.81f, 0.0f);

    public static final Vector3 unitario_x = new Vector3(1.0f, 0.0f, 0.0f);
    public static final Vector3 unitario_y = new Vector3(0.0f, 1.0f, 0.0f);
    public static final Vector3 unitario_z = new Vector3(0.0f, 0.0f, 1.0f);
    public static final Vector3 unitario_xyz = new Vector3(1.0f, 1.0f, 1.0f);

    public float x, y, z;

    public Vector3() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    public Vector3(float value) {
        this.x = value;
        this.y = value;
        this.z = value;
    }

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vertex v1) {
        this(v1.location.x, v1.location.y, v1.location.z);
    }

    public Vector3(Vertex v1, Vertex v2) {
        this(v2.location.getVector3(), v1.location.getVector3());
    }

    public Vector3(Vector3 v1, Vector3 v2) {
        this(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
    }

    public void set(Vector3 v1, Vector3 v2) {
        set(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
    }

    public void set(Vertex v1, Vertex v2) {
        set(v2.location.getVector3(), v1.location.getVector3());
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3 vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public Vector3 invert() {
        x *= -1;
        y *= -1;
        z *= -1;
        return this;
    }

    /**
     * Meotod optimizado para calcular el vector normal usando el metodo de
     * calculo rapido de la inversa de la raiz cuadrada
     *
     * @return
     */
    public Vector3 normalize() {
        if (QGlobal.OPT_USAR_FAST_INVSQRT) {
            float inversaRaizCuadrada = QMath.fastInvSqrt(x * x + y * y + z * z);
            x *= inversaRaizCuadrada;
            y *= inversaRaizCuadrada;
            z *= inversaRaizCuadrada;
        } else {
            float length = length();
            x /= length;
            y /= length;
            z /= length;
            return this;
        }
        return this;
    }

    public Vector3 add(float value) {
        x += value;
        y += value;
        z += value;
        return this;
    }

    public Vector3 subtract(float value) {
        x -= value;
        y -= value;
        z -= value;
        return this;
    }

    public Vector3 add(Vector3... others) {
        for (Vector3 other : others) {
            x += other.x;
            y += other.y;
            z += other.z;
        }
        return this;
    }

    public Vector3 subtract(Vector3... others) {
        for (Vector3 other : others) {
            x -= other.x;
            y -= other.y;
            z -= other.z;
        }
        return this;
    }

    public Vector3 add(float delta, Vector3... others) {
        for (Vector3 other : others) {
            x += other.x * delta;
            y += other.y * delta;
            z += other.z * delta;
        }
        return this;
    }

    public void flip() {
        x = -x;
        y = -y;
        z = -z;
    }

    public Vector3 multiply(float alpha) {
        x *= alpha;
        y *= alpha;
        z *= alpha;
        return this;
    }

    public Vector3 multiply(Vector3 vector) {
        x *= vector.x;
        y *= vector.y;
        z *= vector.z;
        return this;
    }

    public Vector3 divide(Vector3 vector) {
        x /= vector.x;
        y /= vector.y;
        z /= vector.z;
        return this;
    }

    public Vector3 cross(Vector3 other) {
        float tempX = this.y * other.z - this.z * other.y;
        float tempY = this.z * other.x - this.x * other.z;
        z = this.x * other.y - this.y * other.x;
        x = tempX;
        y = tempY;
        return this;
    }

    public float length() {
        if (QGlobal.OPT_USAR_FAST_INVSQRT) {
            return 1.0f / (float) QMath.fastInvSqrt(x * x + y * y + z * z);
        } else {
            return (float) QMath.sqrt(x * x + y * y + z * z);
        }
    }

    public float dot(Vector3 other) {
        return (this.x * other.x + this.y * other.y + this.z * other.z);
    }

    public Vector3 rotateX(float angle) {
        float cosAngle = (float) QMath.cos(angle);
        float sinAngle = (float) QMath.sin(angle);
        float tempY = y * cosAngle - z * sinAngle;
        z = y * sinAngle + z * cosAngle;
        y = tempY;
        return this;
    }

    public Vector3 rotateY(float angle) {
        float cosAngle = (float) QMath.cos(angle);
        float sinAngle = (float) QMath.sin(angle);
        float tempZ = z * cosAngle - x * sinAngle;
        x = z * sinAngle + x * cosAngle;
        z = tempZ;
        return this;
    }

    public Vector3 rotateZ(float angle) {
        float cosAngle = (float) QMath.cos(angle);
        float sinAngle = (float) QMath.sin(angle);
        float tempX = x * cosAngle - y * sinAngle;
        y = x * sinAngle + y * cosAngle;
        x = tempX;
        return this;
    }

    public Vector3 clone() {
        return new Vector3(x, y, z);
    }

    public static Vector3 addNewVector(Vector3 v1, Vector3... others) {
        Vector3 result = v1.clone();
        for (Vector3 other : others) {
            result.x += other.x;
            result.y += other.y;
            result.z += other.z;
        }
        return result;
    }

    public static float dotProduct(Vector3 v1, Vector3 v2) {
        return (v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
    }

    public static Vector3 crossProduct(Vector3 v1, Vector3 v2) {
        return new Vector3(
                v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x);
    }

    public static Vector3 multiply(float alpha, Vector3 vector) {
        return new Vector3(alpha * vector.x, alpha * vector.y, alpha * vector.z);
    }

    public float anguloX(Vector3 otro) {
        float angulo = 0;
        if (otro.y != 0 || otro.z != 0) {
            float cosenoAng = (float) ((y * otro.y + z * otro.z)
                    / ((Math.sqrt(y * y + z * z)) * (Math.sqrt(otro.y * otro.y + otro.z * otro.z))));
            angulo = (float) Math.acos(cosenoAng);
        } else {
            float dif = otro.z - z;
            if (dif > 0) {
                return 0;
            } else {
                return (float) Math.PI;// 180
            }

        }
        if (Float.isNaN(angulo)) {
            angulo = 0;
        }
        return angulo;
    }

    public float anguloY(Vector3 otro) {
        float angulo = 0;
        if (otro.x != 0 || otro.z != 0) {
            float cosenoAng = (float) ((x * otro.x + z * otro.z)
                    / ((Math.sqrt(x * x + z * z)) * (Math.sqrt(otro.x * otro.x + otro.z * otro.z))));
            angulo = (float) Math.acos(cosenoAng);
        } else {
            float dif = otro.x - x;
            if (dif > 0) {
                return 0;
            } else {
                return (float) Math.PI;// 180
            }

        }
        if (Float.isNaN(angulo)) {
            angulo = 0;
        }
        return angulo;
    }

    public float anguloZ(Vector3 otro) {
        float angulo = 0;
        if (otro.x != 0 || otro.y != 0) {
            float cosenoAng = (float) ((y * otro.y + x * otro.x)
                    / ((Math.sqrt(y * y + x * x)) * (Math.sqrt(otro.y * otro.y + otro.x * otro.x))));
            angulo = (float) Math.acos(cosenoAng);
        } else {
            float dif = otro.x - x;
            if (dif > 0) {
                return 0;
            } else {
                return (float) Math.PI;// 180
            }
        }
        if (Float.isNaN(angulo)) {
            angulo = 0;
        }
        return angulo;
    }

    public float angulo(Vector3 otro) {
        float cosenoAng = (float) ((x * otro.x + y * otro.y + z * otro.z) / ((Math.sqrt(x * x + y * y + z * z))
                * (Math.sqrt(otro.x * otro.x + otro.y * otro.y + otro.z * otro.z))));
        float angulo = (float) Math.acos(cosenoAng);
        if (Float.isNaN(angulo)) {
            angulo = 0;
        }
        return angulo;
    }

    public float get(int index) {
        switch (index) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
        }
        throw new IllegalArgumentException("index must be either 0, 1 or 2");
    }

    public Vertex toVertice() {
        return new Vertex(x, y, z, 1);
    }

    public Vector2 xy() {
        return new Vector2(x, y);
    }

    public Vector2 yx() {
        return new Vector2(y, x);
    }

    public Vector2 getXZ() {
        return new Vector2(x, z);
    }

    public Vector2 getYZ() {
        return new Vector2(y, z);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Float.floatToIntBits(this.x);
        hash = 97 * hash + Float.floatToIntBits(this.y);
        hash = 97 * hash + Float.floatToIntBits(this.z);
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
        final Vector3 other = (Vector3) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        if (Float.floatToIntBits(this.z) != Float.floatToIntBits(other.z)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return x + ", " + y + ", " + z;
    }

    public static Vector3 empty() {
        return new Vector3();
    }

    public static Vector3 of(float x, float y, float z) {
        return new Vector3(x, y, z);
    }

    public static Vector3 of(float value) {
        return new Vector3(value);
    }

    public static Vector3 of(Vertex vertice) {
        return new Vector3(vertice);
    }

    public static Vector3 of(Vertex v1, Vertex v2) {
        return new Vector3(v2.location.getVector3(), v1.location.getVector3());
    }

    public static Vector3 of(Vector3 v1, Vector3 v2) {
        return new Vector3(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
    }

    public static Vector3 of(Vector4 v1, Vector4 v2) {
        return new Vector3(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
    }
}
