/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.util;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;

import net.qoopo.engine.core.math.Matrix3;
import net.qoopo.engine.core.math.Matrix4;
import net.qoopo.engine.core.math.Vector3;

//import javax.vecmath.Point3d;
//import javax.vecmath.Point3f;
//import javax.vecmath.Quat4f;
//import net.qoopo.engine3d.core.math.QMatriz3;
//import net.qoopo.engine3d.core.math.QVector3;
//import org.joml.Matrix3f;
//import org.joml.Vector3d;
//import org.joml.Vector3f;
/**
 *
 * @author alberto
 */
public class QJOMLUtil {

    // public static Color3f convertirColor3f(QColor color) {
    // return new Color3f(color.r, color.g, color.b);
    // }
    public static Vector3 convertirQVector3(Vector3f vector) {
        return Vector3.of(vector.x, vector.y, vector.z);
    }

    public static Vector3f convertirVector3f(Vector3 vector) {
        return new Vector3f(vector.x, vector.y, vector.z);
    }

    public static Vector3d convertirVector3d(Vector3 vector) {
        return new Vector3d(vector.x, vector.y, vector.z);
    }

    // public static Point3f convertirPoint3f(QVector3 vector) {
    // return new Point3f(vector.x, vector.y, vector.z);
    // }
    // public static Point3d convertirPoint3d(QVector3 vector) {
    // return new Point3d(vector.x, vector.y, vector.z);
    // }
    public static Matrix3f convertirMatriz3f(Matrix3 matriz) {
        Matrix3f mat = new Matrix3f(
                matriz.get(0, 0),
                matriz.get(0, 1),
                matriz.get(0, 2),
                matriz.get(1, 0),
                matriz.get(1, 1),
                matriz.get(1, 2),
                matriz.get(2, 0),
                matriz.get(2, 1),
                matriz.get(2, 2));
        return mat;
    }

    public static Matrix4 convertirQMatriz4(Matrix4f matriz) {
        Matrix4 mat4 = new Matrix4(
                matriz.m00(), matriz.m01(), matriz.m02(), matriz.m03(),
                matriz.m10(), matriz.m11(), matriz.m12(), matriz.m13(),
                matriz.m20(), matriz.m21(), matriz.m22(), matriz.m23(),
                matriz.m30(), matriz.m31(), matriz.m32(), matriz.m33());
        return mat4;
    }

}
