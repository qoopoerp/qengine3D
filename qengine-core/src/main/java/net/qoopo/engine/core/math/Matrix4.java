package net.qoopo.engine.core.math;

import java.util.logging.Logger;

public final class Matrix4 implements Cloneable, java.io.Serializable {

    static final long serialVersionUID = 1;
    private static final Logger logger = Logger.getLogger(Matrix4.class.getName());
    public static final Matrix4 ZERO = new Matrix4(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    public static final Matrix4 IDENTITY = new Matrix4();

    public float m00, m01, m02, m03;
    public float m10, m11, m12, m13;
    public float m20, m21, m22, m23;
    public float m30, m31, m32, m33;

    /**
     * Constructor instantiates a new <code>Matrix</code> that is set to the
     * identity matrix.
     *
     */
    public Matrix4() {
        loadIdentity();
    }

    /**
     * constructs a matrix with the given values.
     *
     * @param m00
     * @param m01
     * @param m02
     * @param m03
     * @param m10
     * @param m11
     * @param m12
     * @param m13
     * @param m20
     * @param m21
     * @param m22
     * @param m23
     * @param m30
     * @param m31
     * @param m32
     * @param m33
     */
    public Matrix4(float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {

        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }

    /**
     * Create a new Matrix4f, given data in column-major format.
     *
     * @param array An array of 16 floats in column-major format (translation in
     *              elements 12, 13 and 14).
     */
    public Matrix4(float[] array) {
        set(array, false);
    }

    /**
     * Constructor instantiates a new <code>Matrix</code> that is set to the
     * provided matrix. This constructor copies a given Matrix. If the provided
     * matrix is null, the constructor sets the matrix to the identity.
     *
     * @param mat the matrix to copy.
     */
    public Matrix4(Matrix4 mat) {
        copy(mat);
    }

    /**
     * <code>copy</code> transfers the contents of a given matrix to this
     * matrix. If a null matrix is supplied, this matrix is set to the identity
     * matrix.
     *
     * @param matrix the matrix to copy.
     */
    public void copy(Matrix4 matrix) {
        if (null == matrix) {
            loadIdentity();
        } else {
            m00 = matrix.m00;
            m01 = matrix.m01;
            m02 = matrix.m02;
            m03 = matrix.m03;
            m10 = matrix.m10;
            m11 = matrix.m11;
            m12 = matrix.m12;
            m13 = matrix.m13;
            m20 = matrix.m20;
            m21 = matrix.m21;
            m22 = matrix.m22;
            m23 = matrix.m23;
            m30 = matrix.m30;
            m31 = matrix.m31;
            m32 = matrix.m32;
            m33 = matrix.m33;
        }
    }

    public void fromFrame(Vector3 location, Vector3 direction, Vector3 up, Vector3 left) {
        try {
            Vector3 fwdVector = direction.clone();
            Vector3 leftVector = fwdVector.clone().cross(up);
            Vector3 upVector = leftVector.clone().cross(fwdVector);

            m00 = leftVector.x;
            m01 = leftVector.y;
            m02 = leftVector.z;
            m03 = -leftVector.dot(location);

            m10 = upVector.x;
            m11 = upVector.y;
            m12 = upVector.z;
            m13 = -upVector.dot(location);

            m20 = -fwdVector.x;
            m21 = -fwdVector.y;
            m22 = -fwdVector.z;
            m23 = fwdVector.dot(location);

            m30 = 0f;
            m31 = 0f;
            m32 = 0f;
            m33 = 1f;
        } finally {

        }
    }

    /**
     * <code>get</code> retrieves the values of this object into a float array
     * in row-major order.
     *
     * @param matrix the matrix to set the values into.
     */
    public void get(float[] matrix) {
        get(matrix, true);
    }

    /**
     * <code>set</code> retrieves the values of this object into a float array.
     *
     * @param matrix   the matrix to set the values into.
     * @param rowMajor whether the outgoing data is in row or column major
     *                 order.
     */
    public void get(float[] matrix, boolean rowMajor) {
        if (matrix.length != 16) {
            throw new IllegalArgumentException(
                    "Array must be of size 16.");
        }

        if (rowMajor) {
            matrix[0] = m00;
            matrix[1] = m01;
            matrix[2] = m02;
            matrix[3] = m03;
            matrix[4] = m10;
            matrix[5] = m11;
            matrix[6] = m12;
            matrix[7] = m13;
            matrix[8] = m20;
            matrix[9] = m21;
            matrix[10] = m22;
            matrix[11] = m23;
            matrix[12] = m30;
            matrix[13] = m31;
            matrix[14] = m32;
            matrix[15] = m33;
        } else {
            matrix[0] = m00;
            matrix[4] = m01;
            matrix[8] = m02;
            matrix[12] = m03;
            matrix[1] = m10;
            matrix[5] = m11;
            matrix[9] = m12;
            matrix[13] = m13;
            matrix[2] = m20;
            matrix[6] = m21;
            matrix[10] = m22;
            matrix[14] = m23;
            matrix[3] = m30;
            matrix[7] = m31;
            matrix[11] = m32;
            matrix[15] = m33;
        }
    }

    /**
     * <code>get</code> retrieves a value from the matrix at the given position.
     * If the position is invalid a <code>JmeException</code> is thrown.
     *
     * @param i the row index.
     * @param j the colum index.
     * @return the value at (i, j).
     */
    @SuppressWarnings("fallthrough")
    public float get(int i, int j) {
        switch (i) {
            case 0:
                switch (j) {
                    case 0:
                        return m00;
                    case 1:
                        return m01;
                    case 2:
                        return m02;
                    case 3:
                        return m03;
                }
            case 1:
                switch (j) {
                    case 0:
                        return m10;
                    case 1:
                        return m11;
                    case 2:
                        return m12;
                    case 3:
                        return m13;
                }
            case 2:
                switch (j) {
                    case 0:
                        return m20;
                    case 1:
                        return m21;
                    case 2:
                        return m22;
                    case 3:
                        return m23;
                }
            case 3:
                switch (j) {
                    case 0:
                        return m30;
                    case 1:
                        return m31;
                    case 2:
                        return m32;
                    case 3:
                        return m33;
                }
        }

        logger.warning("Invalid matrix index.");
        throw new IllegalArgumentException("Invalid indices into matrix.");
    }

    /**
     * <code>getColumn</code> returns one of three columns specified by the
     * parameter. This column is returned as a float array of length 4.
     *
     * @param i the column to retrieve. Must be between 0 and 3.
     * @return the column specified by the index.
     */
    public float[] getColumn(int i) {
        return getColumn(i, null);
    }

    /**
     * <code>getColumn</code> returns one of three columns specified by the
     * parameter. This column is returned as a float[4].
     *
     * @param i     the column to retrieve. Must be between 0 and 3.
     * @param store the float array to store the result in. if null, a new one
     *              is created.
     * @return the column specified by the index.
     */
    public float[] getColumn(int i, float[] store) {
        if (store == null) {
            store = new float[4];
        }
        switch (i) {
            case 0:
                store[0] = m00;
                store[1] = m10;
                store[2] = m20;
                store[3] = m30;
                break;
            case 1:
                store[0] = m01;
                store[1] = m11;
                store[2] = m21;
                store[3] = m31;
                break;
            case 2:
                store[0] = m02;
                store[1] = m12;
                store[2] = m22;
                store[3] = m32;
                break;
            case 3:
                store[0] = m03;
                store[1] = m13;
                store[2] = m23;
                store[3] = m33;
                break;
            default:
                logger.warning("Invalid column index.");
                throw new IllegalArgumentException("Invalid column index. " + i);
        }
        return store;
    }

    /**
     *
     * <code>setColumn</code> sets a particular column of this matrix to that
     * represented by the provided vector.
     *
     * @param i      the column to set.
     * @param column the data to set.
     */
    public void setColumn(int i, float[] column) {

        if (column == null) {
            logger.warning("Column is null. Ignoring.");
            return;
        }
        switch (i) {
            case 0:
                m00 = column[0];
                m10 = column[1];
                m20 = column[2];
                m30 = column[3];
                break;
            case 1:
                m01 = column[0];
                m11 = column[1];
                m21 = column[2];
                m31 = column[3];
                break;
            case 2:
                m02 = column[0];
                m12 = column[1];
                m22 = column[2];
                m32 = column[3];
                break;
            case 3:
                m03 = column[0];
                m13 = column[1];
                m23 = column[2];
                m33 = column[3];
                break;
            default:
                logger.warning("Invalid column index.");
                throw new IllegalArgumentException("Invalid column index. " + i);
        }
    }

    /**
     * <code>set</code> places a given value into the matrix at the given
     * position. If the position is invalid a <code>JmeException</code> is
     * thrown.
     *
     * @param i     the row index.
     * @param j     the colum index.
     * @param value the value for (i, j).
     */
    @SuppressWarnings("fallthrough")
    public void set(int i, int j, float value) {
        switch (i) {
            case 0:
                switch (j) {
                    case 0:
                        m00 = value;
                        return;
                    case 1:
                        m01 = value;
                        return;
                    case 2:
                        m02 = value;
                        return;
                    case 3:
                        m03 = value;
                        return;
                }
            case 1:
                switch (j) {
                    case 0:
                        m10 = value;
                        return;
                    case 1:
                        m11 = value;
                        return;
                    case 2:
                        m12 = value;
                        return;
                    case 3:
                        m13 = value;
                        return;
                }
            case 2:
                switch (j) {
                    case 0:
                        m20 = value;
                        return;
                    case 1:
                        m21 = value;
                        return;
                    case 2:
                        m22 = value;
                        return;
                    case 3:
                        m23 = value;
                        return;
                }
            case 3:
                switch (j) {
                    case 0:
                        m30 = value;
                        return;
                    case 1:
                        m31 = value;
                        return;
                    case 2:
                        m32 = value;
                        return;
                    case 3:
                        m33 = value;
                        return;
                }
        }

        logger.warning("Invalid matrix index.");
        throw new IllegalArgumentException("Invalid indices into matrix.");
    }

    /**
     * <code>set</code> sets the values of this matrix from an array of values.
     *
     * @param matrix the matrix to set the value to.
     */
    public void set(float[][] matrix) {
        if (matrix.length != 4 || matrix[0].length != 4) {
            throw new IllegalArgumentException(
                    "Array must be of size 16.");
        }

        m00 = matrix[0][0];
        m01 = matrix[0][1];
        m02 = matrix[0][2];
        m03 = matrix[0][3];
        m10 = matrix[1][0];
        m11 = matrix[1][1];
        m12 = matrix[1][2];
        m13 = matrix[1][3];
        m20 = matrix[2][0];
        m21 = matrix[2][1];
        m22 = matrix[2][2];
        m23 = matrix[2][3];
        m30 = matrix[3][0];
        m31 = matrix[3][1];
        m32 = matrix[3][2];
        m33 = matrix[3][3];
    }

    /**
     * Sets the values of this matrix
     */
    public void set(float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {

        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }

    /**
     * <code>set</code> sets the values of this matrix from another matrix.
     *
     * @param matrix the matrix to read the value from.
     */
    public Matrix4 set(Matrix4 matrix) {
        m00 = matrix.m00;
        m01 = matrix.m01;
        m02 = matrix.m02;
        m03 = matrix.m03;
        m10 = matrix.m10;
        m11 = matrix.m11;
        m12 = matrix.m12;
        m13 = matrix.m13;
        m20 = matrix.m20;
        m21 = matrix.m21;
        m22 = matrix.m22;
        m23 = matrix.m23;
        m30 = matrix.m30;
        m31 = matrix.m31;
        m32 = matrix.m32;
        m33 = matrix.m33;
        return this;
    }

    /**
     * <code>set</code> sets the values of this matrix from an array of values
     * assuming that the data is rowMajor order;
     *
     * @param matrix the matrix to set the value to.
     */
    public void set(float[] matrix) {
        set(matrix, true);
    }

    /**
     * <code>set</code> sets the values of this matrix from an array of values;
     *
     * @param matrix   the matrix to set the value to.
     * @param rowMajor whether the incoming data is in row or column major
     *                 order.
     */
    public void set(float[] matrix, boolean rowMajor) {
        if (matrix.length != 16) {
            throw new IllegalArgumentException(
                    "Array must be of size 16.");
        }

        if (rowMajor) {
            m00 = matrix[0];
            m01 = matrix[1];
            m02 = matrix[2];
            m03 = matrix[3];
            m10 = matrix[4];
            m11 = matrix[5];
            m12 = matrix[6];
            m13 = matrix[7];
            m20 = matrix[8];
            m21 = matrix[9];
            m22 = matrix[10];
            m23 = matrix[11];
            m30 = matrix[12];
            m31 = matrix[13];
            m32 = matrix[14];
            m33 = matrix[15];
        } else {
            m00 = matrix[0];
            m01 = matrix[4];
            m02 = matrix[8];
            m03 = matrix[12];
            m10 = matrix[1];
            m11 = matrix[5];
            m12 = matrix[9];
            m13 = matrix[13];
            m20 = matrix[2];
            m21 = matrix[6];
            m22 = matrix[10];
            m23 = matrix[14];
            m30 = matrix[3];
            m31 = matrix[7];
            m32 = matrix[11];
            m33 = matrix[15];
        }
    }

    public Matrix4 transpose() {
        float[] tmp = new float[16];
        get(tmp, true);
        Matrix4 mat = new Matrix4(tmp);
        return mat;
    }

    /**
     * <code>transpose</code> locally transposes this Matrix.
     *
     * @return this object for chaining.
     */
    public Matrix4 transposeLocal() {
        float tmp = m01;
        m01 = m10;
        m10 = tmp;

        tmp = m02;
        m02 = m20;
        m20 = tmp;

        tmp = m03;
        m03 = m30;
        m30 = tmp;

        tmp = m12;
        m12 = m21;
        m21 = tmp;

        tmp = m13;
        m13 = m31;
        m31 = tmp;

        tmp = m23;
        m23 = m32;
        m32 = tmp;

        return this;
    }

    public void fillFloatArray(float[] f, boolean columnMajor) {
        if (columnMajor) {
            f[0] = m00;
            f[1] = m10;
            f[2] = m20;
            f[3] = m30;
            f[4] = m01;
            f[5] = m11;
            f[6] = m21;
            f[7] = m31;
            f[8] = m02;
            f[9] = m12;
            f[10] = m22;
            f[11] = m32;
            f[12] = m03;
            f[13] = m13;
            f[14] = m23;
            f[15] = m33;
        } else {
            f[0] = m00;
            f[1] = m01;
            f[2] = m02;
            f[3] = m03;
            f[4] = m10;
            f[5] = m11;
            f[6] = m12;
            f[7] = m13;
            f[8] = m20;
            f[9] = m21;
            f[10] = m22;
            f[11] = m23;
            f[12] = m30;
            f[13] = m31;
            f[14] = m32;
            f[15] = m33;
        }
    }

    /**
     * <code>loadIdentity</code> sets this matrix to the identity matrix, namely
     * all zeros with ones along the diagonal.
     *
     */
    public void loadIdentity() {
        m01 = m02 = m03 = 0.0f;
        m10 = m12 = m13 = 0.0f;
        m20 = m21 = m23 = 0.0f;
        m30 = m31 = m32 = 0.0f;
        m00 = m11 = m22 = m33 = 1.0f;
    }

    /**
     *
     * @param nearCerca
     * @param farLejos
     * @param leftIzquierda
     * @param rightDerecha
     * @param topArriba
     * @param bottomAbajo
     * @param parallel
     */
    public void fromFrustum(float nearCerca, float farLejos, float leftIzquierda, float rightDerecha, float topArriba,
            float bottomAbajo, boolean parallel) {
        loadIdentity();

        if (parallel) {
            // Ortogonal
            //
            // m00 = 2.0f / (rightDerecha - leftIzquierda);
            // m11 = 2.0f / (topArriba - bottomAbajo);
            // m22 = -2.0f / (farLejos - nearCerca);
            // m30 = -(rightDerecha + leftIzquierda) / (rightDerecha - leftIzquierda);
            // m31 = -(topArriba + bottomAbajo) / (topArriba - bottomAbajo);
            // m32 = -(farLejos + nearCerca) / (farLejos - nearCerca);

            // opcion 2
            // http://www.songho.ca/opengl/gl_transform.html
            // http://www.songho.ca/opengl/gl_projectionmatrix.html
            m00 = 2.0f / (rightDerecha - leftIzquierda);
            m11 = 2.0f / (topArriba - bottomAbajo);
            m22 = -2.0f / (farLejos - nearCerca);
            m03 = -(rightDerecha + leftIzquierda) / (rightDerecha - leftIzquierda);
            m13 = -(topArriba + bottomAbajo) / (topArriba - bottomAbajo);
            m23 = -(farLejos + nearCerca) / (farLejos - nearCerca);
        } else {
            // perspectiva
            //
            // m00 = (2.0f * nearCerca) / (rightDerecha - leftIzquierda);
            // m11 = (2.0f * nearCerca) / (topArriba - bottomAbajo);
            // m23 = -1.0f;
            // m33 = -0.0f;
            // m20 = (rightDerecha + leftIzquierda) / (rightDerecha - leftIzquierda);
            // m21 = (topArriba + bottomAbajo) / (topArriba - bottomAbajo);
            // m22 = -(farLejos + nearCerca) / (farLejos - nearCerca);
            // m32 = -(2.0f * farLejos * nearCerca) / (farLejos - nearCerca);

            // opcion 2
            // http://www.songho.ca/opengl/gl_transform.html
            // http://www.songho.ca/opengl/gl_projectionmatrix.html
            m00 = (2.0f * nearCerca) / (rightDerecha - leftIzquierda);
            m11 = (2.0f * nearCerca) / (topArriba - bottomAbajo);
            m02 = (rightDerecha + leftIzquierda) / (rightDerecha - leftIzquierda);
            m12 = (topArriba + bottomAbajo) / (topArriba - bottomAbajo);
            m22 = -(farLejos + nearCerca) / (farLejos - nearCerca);
            m32 = -1.0f;
            m23 = -(2.0f * farLejos * nearCerca) / (farLejos - nearCerca);
            m33 = -0.0f;
        }
    }

    /**
     * <code>fromAngleAxis</code> sets this matrix4f to the values specified by
     * an angle and an axis of rotation. This method creates an object, so use
     * fromAngleNormalAxis if your axis is already normalized.
     *
     * @param angle the angle to rotate (in radians).
     * @param axis  the axis of rotation.
     */
    public void fromAngleAxis(float angle, Vector3 axis) {
        Vector3 normAxis = axis.clone();
        normAxis.normalize();
        fromAngleNormalAxis(angle, normAxis);
    }

    /**
     * <code>fromAngleNormalAxis</code> sets this matrix4f to the values
     * specified by an angle and a normalized axis of rotation.
     *
     * @param angle the angle to rotate (in radians).
     * @param axis  the axis of rotation (already normalized).
     */
    public void fromAngleNormalAxis(float angle, Vector3 axis) {
        zero();
        m33 = 1;

        float fCos = QMath.cos(angle);
        float fSin = QMath.sin(angle);
        float fOneMinusCos = ((float) 1.0) - fCos;
        float fX2 = axis.x * axis.x;
        float fY2 = axis.y * axis.y;
        float fZ2 = axis.z * axis.z;
        float fXYM = axis.x * axis.y * fOneMinusCos;
        float fXZM = axis.x * axis.z * fOneMinusCos;
        float fYZM = axis.y * axis.z * fOneMinusCos;
        float fXSin = axis.x * fSin;
        float fYSin = axis.y * fSin;
        float fZSin = axis.z * fSin;

        m00 = fX2 * fOneMinusCos + fCos;
        m01 = fXYM - fZSin;
        m02 = fXZM + fYSin;
        m10 = fXYM + fZSin;
        m11 = fY2 * fOneMinusCos + fCos;
        m12 = fYZM - fXSin;
        m20 = fXZM - fYSin;
        m21 = fYZM + fXSin;
        m22 = fZ2 * fOneMinusCos + fCos;
    }

    /**
     * <code>mult</code> multiplies this matrix by a scalar.
     *
     * @param scalar the scalar to multiply this matrix by.
     */
    public void multLocal(float scalar) {
        m00 *= scalar;
        m01 *= scalar;
        m02 *= scalar;
        m03 *= scalar;
        m10 *= scalar;
        m11 *= scalar;
        m12 *= scalar;
        m13 *= scalar;
        m20 *= scalar;
        m21 *= scalar;
        m22 *= scalar;
        m23 *= scalar;
        m30 *= scalar;
        m31 *= scalar;
        m32 *= scalar;
        m33 *= scalar;
    }

    public Matrix4 mult(float scalar) {
        Matrix4 out = new Matrix4();
        out.set(this);
        out.multLocal(scalar);
        return out;
    }

    public Matrix4 mult(float scalar, Matrix4 store) {
        store.set(this);
        store.multLocal(scalar);
        return store;
    }

    /**
     * <code>mult</code> multiplies this matrix with another matrix. The result
     * matrix will then be returned. This matrix will be on the left hand side,
     * while the parameter matrix will be on the right.
     *
     * @param in2 the matrix to multiply this matrix by.
     * @return the resultant matrix
     */
    public Matrix4 mult(Matrix4 in2) {
        if (in2 != null) {
            return mult(in2, null);
        } else {
            return null;
        }
    }

    /**
     * <code>mult</code> multiplies this matrix with another matrix. The result
     * matrix will then be returned. This matrix will be on the left hand side,
     * while the parameter matrix will be on the right.
     *
     * @param in2   the matrix to multiply this matrix by.
     * @param store where to store the result. It is safe for in2 and store to
     *              be the same object.
     * @return the resultant matrix
     */
    public Matrix4 mult(Matrix4 in2, Matrix4 store) {
        if (store == null) {
            store = new Matrix4();
        }

        float temp00, temp01, temp02, temp03;
        float temp10, temp11, temp12, temp13;
        float temp20, temp21, temp22, temp23;
        float temp30, temp31, temp32, temp33;

        temp00 = m00 * in2.m00
                + m01 * in2.m10
                + m02 * in2.m20
                + m03 * in2.m30;
        temp01 = m00 * in2.m01
                + m01 * in2.m11
                + m02 * in2.m21
                + m03 * in2.m31;
        temp02 = m00 * in2.m02
                + m01 * in2.m12
                + m02 * in2.m22
                + m03 * in2.m32;
        temp03 = m00 * in2.m03
                + m01 * in2.m13
                + m02 * in2.m23
                + m03 * in2.m33;

        temp10 = m10 * in2.m00
                + m11 * in2.m10
                + m12 * in2.m20
                + m13 * in2.m30;
        temp11 = m10 * in2.m01
                + m11 * in2.m11
                + m12 * in2.m21
                + m13 * in2.m31;
        temp12 = m10 * in2.m02
                + m11 * in2.m12
                + m12 * in2.m22
                + m13 * in2.m32;
        temp13 = m10 * in2.m03
                + m11 * in2.m13
                + m12 * in2.m23
                + m13 * in2.m33;

        temp20 = m20 * in2.m00
                + m21 * in2.m10
                + m22 * in2.m20
                + m23 * in2.m30;
        temp21 = m20 * in2.m01
                + m21 * in2.m11
                + m22 * in2.m21
                + m23 * in2.m31;
        temp22 = m20 * in2.m02
                + m21 * in2.m12
                + m22 * in2.m22
                + m23 * in2.m32;
        temp23 = m20 * in2.m03
                + m21 * in2.m13
                + m22 * in2.m23
                + m23 * in2.m33;

        temp30 = m30 * in2.m00
                + m31 * in2.m10
                + m32 * in2.m20
                + m33 * in2.m30;
        temp31 = m30 * in2.m01
                + m31 * in2.m11
                + m32 * in2.m21
                + m33 * in2.m31;
        temp32 = m30 * in2.m02
                + m31 * in2.m12
                + m32 * in2.m22
                + m33 * in2.m32;
        temp33 = m30 * in2.m03
                + m31 * in2.m13
                + m32 * in2.m23
                + m33 * in2.m33;

        store.m00 = temp00;
        store.m01 = temp01;
        store.m02 = temp02;
        store.m03 = temp03;
        store.m10 = temp10;
        store.m11 = temp11;
        store.m12 = temp12;
        store.m13 = temp13;
        store.m20 = temp20;
        store.m21 = temp21;
        store.m22 = temp22;
        store.m23 = temp23;
        store.m30 = temp30;
        store.m31 = temp31;
        store.m32 = temp32;
        store.m33 = temp33;

        return store;
    }

    /**
     * <code>mult</code> multiplies this matrix with another matrix. The results
     * are stored internally and a handle to this matrix will then be returned.
     * This matrix will be on the left hand side, while the parameter matrix
     * will be on the right.
     *
     * @param in2 the matrix to multiply this matrix by.
     * @return the resultant matrix
     */
    public Matrix4 multLocal(Matrix4 in2) {
        return mult(in2, this);
    }

    /**
     * <code>mult</code> multiplies a vector about a rotation matrix. The
     * resulting vector is returned as a QVector3.of.
     *
     * @param vec vec to multiply against.
     * @return the rotated vector.
     */
    public Vector3 mult(Vector3 vec) {
        return mult(vec, null);
    }

    /**
     * <code>mult</code> multiplies a vector about a rotation matrix and adds
     * translation. The resulting vector is returned.
     *
     * @param vec   vec to multiply against.
     * @param store a vector to store the result in. Created if null is passed.
     * @return the rotated vector.
     */
    public Vector3 mult(Vector3 vec, Vector3 store) {
        if (store == null) {
            store = Vector3.empty();
        }

        float vx = vec.x, vy = vec.y, vz = vec.z;
        store.x = m00 * vx + m01 * vy + m02 * vz + m03;
        store.y = m10 * vx + m11 * vy + m12 * vz + m13;
        store.z = m20 * vx + m21 * vy + m22 * vz + m23;

        return store;
    }

    /**
     * <code>mult</code> multiplies a <code>QVector4</code> about a rotation
     * matrix. The resulting vector is returned as a new <code>QVector4</code>.
     *
     * @param vec vec to multiply against.
     * @return the rotated vector.
     */
    public Vector4 mult(Vector4 vec) {
        return mult(vec, null);
    }

    /**
     * <code>mult</code> multiplies a <code>QVector4</code> about a rotation
     * matrix. The resulting vector is returned.
     *
     * @param vec   vec to multiply against.
     * @param store a vector to store the result in. Created if null is passed.
     * @return the rotated vector.
     */
    public Vector4 mult(Vector4 vec, Vector4 store) {
        if (null == vec) {
            logger.warning("Source vector is null, null result returned.");
            return null;
        }
        if (store == null) {
            store = new Vector4();
        }

        float vx = vec.x, vy = vec.y, vz = vec.z, vw = vec.w;
        store.x = m00 * vx + m01 * vy + m02 * vz + m03 * vw;
        store.y = m10 * vx + m11 * vy + m12 * vz + m13 * vw;
        store.z = m20 * vx + m21 * vy + m22 * vz + m23 * vw;
        store.w = m30 * vx + m31 * vy + m32 * vz + m33 * vw;

        return store;
    }

    /**
     * <code>mult</code> multiplies a vector about a rotation matrix. The
     * resulting vector is returned.
     *
     * @param vec vec to multiply against.
     *
     * @return the rotated vector.
     */
    public Vector4 multAcross(Vector4 vec) {
        return multAcross(vec, null);
    }

    /**
     * <code>mult</code> multiplies a vector about a rotation matrix. The
     * resulting vector is returned.
     *
     * @param vec   vec to multiply against.
     * @param store a vector to store the result in. created if null is passed.
     * @return the rotated vector.
     */
    public Vector4 multAcross(Vector4 vec, Vector4 store) {
        if (null == vec) {
            logger.warning("Source vector is null, null result returned.");
            return null;
        }
        if (store == null) {
            store = new Vector4();
        }

        float vx = vec.x, vy = vec.y, vz = vec.z, vw = vec.w;
        store.x = m00 * vx + m10 * vy + m20 * vz + m30 * vw;
        store.y = m01 * vx + m11 * vy + m21 * vz + m31 * vw;
        store.z = m02 * vx + m12 * vy + m22 * vz + m32 * vw;
        store.w = m03 * vx + m13 * vy + m23 * vz + m33 * vw;

        return store;
    }

    /**
     * <code>multNormal</code> multiplies a vector about a rotation matrix, but
     * does not add translation. The resulting vector is returned.
     *
     * @param vec   vec to multiply against.
     * @param store a vector to store the result in. Created if null is passed.
     * @return the rotated vector.
     */
    public Vector3 multNormal(Vector3 vec, Vector3 store) {
        if (store == null) {
            store = Vector3.empty();
        }

        float vx = vec.x, vy = vec.y, vz = vec.z;
        store.x = m00 * vx + m01 * vy + m02 * vz;
        store.y = m10 * vx + m11 * vy + m12 * vz;
        store.z = m20 * vx + m21 * vy + m22 * vz;

        return store;
    }

    /**
     * <code>multNormal</code> multiplies a vector about a rotation matrix, but
     * does not add translation. The resulting vector is returned.
     *
     * @param vec   vec to multiply against.
     * @param store a vector to store the result in. Created if null is passed.
     * @return the rotated vector.
     */
    public Vector3 multNormalAcross(Vector3 vec, Vector3 store) {
        if (store == null) {
            store = Vector3.empty();
        }

        float vx = vec.x, vy = vec.y, vz = vec.z;
        store.x = m00 * vx + m10 * vy + m20 * vz;
        store.y = m01 * vx + m11 * vy + m21 * vz;
        store.z = m02 * vx + m12 * vy + m22 * vz;

        return store;
    }

    /**
     * <code>mult</code> multiplies a vector about a rotation matrix and adds
     * translation. The w value is returned as a result of multiplying the last
     * column of the matrix by 1.0
     *
     * @param vec   vec to multiply against.
     * @param store a vector to store the result in.
     * @return the W value
     */
    public float multProj(Vector3 vec, Vector3 store) {
        float vx = vec.x, vy = vec.y, vz = vec.z;
        store.x = m00 * vx + m01 * vy + m02 * vz + m03;
        store.y = m10 * vx + m11 * vy + m12 * vz + m13;
        store.z = m20 * vx + m21 * vy + m22 * vz + m23;
        return m30 * vx + m31 * vy + m32 * vz + m33;
    }

    /**
     * <code>mult</code> multiplies a vector about a rotation matrix. The
     * resulting vector is returned.
     *
     * @param vec   vec to multiply against.
     * @param store a vector to store the result in. created if null is passed.
     * @return the rotated vector.
     */
    public Vector3 multAcross(Vector3 vec, Vector3 store) {
        if (null == vec) {
            logger.warning("Source vector is null, null result returned.");
            return null;
        }
        if (store == null) {
            store = Vector3.empty();
        }

        float vx = vec.x, vy = vec.y, vz = vec.z;
        store.x = m00 * vx + m10 * vy + m20 * vz + m30 * 1;
        store.y = m01 * vx + m11 * vy + m21 * vz + m31 * 1;
        store.z = m02 * vx + m12 * vy + m22 * vz + m32 * 1;

        return store;
    }

    /**
     * <code>mult</code> multiplies a quaternion about a matrix. The resulting
     * vector is returned.
     *
     * @param vec   vec to multiply against.
     * @param store a quaternion to store the result in. created if null is
     *              passed.
     * @return store = this * vec
     */
    public Cuaternion mult(Cuaternion vec, Cuaternion store) {

        if (null == vec) {
            logger.warning("Source vector is null, null result returned.");
            return null;
        }
        if (store == null) {
            store = new Cuaternion();
        }

        float x = m00 * vec.x + m10 * vec.y + m20 * vec.z + m30 * vec.w;
        float y = m01 * vec.x + m11 * vec.y + m21 * vec.z + m31 * vec.w;
        float z = m02 * vec.x + m12 * vec.y + m22 * vec.z + m32 * vec.w;
        float w = m03 * vec.x + m13 * vec.y + m23 * vec.z + m33 * vec.w;
        store.x = x;
        store.y = y;
        store.z = z;
        store.w = w;

        return store;
    }

    /**
     * <code>mult</code> multiplies an array of 4 floats against this rotation
     * matrix. The results are stored directly in the array. (vec4f x mat4f)
     *
     * @param vec4f float array (size 4) to multiply against the matrix.
     * @return the vec4f for chaining.
     */
    public float[] mult(float[] vec4f) {
        if (null == vec4f || vec4f.length != 4) {
            logger.warning("invalid array given, must be nonnull and length 4");
            return null;
        }

        float x = vec4f[0], y = vec4f[1], z = vec4f[2], w = vec4f[3];

        vec4f[0] = m00 * x + m01 * y + m02 * z + m03 * w;
        vec4f[1] = m10 * x + m11 * y + m12 * z + m13 * w;
        vec4f[2] = m20 * x + m21 * y + m22 * z + m23 * w;
        vec4f[3] = m30 * x + m31 * y + m32 * z + m33 * w;

        return vec4f;
    }

    /**
     * <code>mult</code> multiplies an array of 4 floats against this rotation
     * matrix. The results are stored directly in the array. (vec4f x mat4f)
     *
     * @param vec4f float array (size 4) to multiply against the matrix.
     * @return the vec4f for chaining.
     */
    public float[] multAcross(float[] vec4f) {
        if (null == vec4f || vec4f.length != 4) {
            logger.warning("invalid array given, must be nonnull and length 4");
            return null;
        }

        float x = vec4f[0], y = vec4f[1], z = vec4f[2], w = vec4f[3];

        vec4f[0] = m00 * x + m10 * y + m20 * z + m30 * w;
        vec4f[1] = m01 * x + m11 * y + m21 * z + m31 * w;
        vec4f[2] = m02 * x + m12 * y + m22 * z + m32 * w;
        vec4f[3] = m03 * x + m13 * y + m23 * z + m33 * w;

        return vec4f;
    }

    /**
     * Inverts this matrix as a new Matrix4f.
     *
     * @return The new inverse matrix
     */
    /**
     * Inverts this matrix as a new QMatriz4.
     *
     * @return The new inverse matrix
     */
    public Matrix4 invert() {
        return invert(null);
    }

    /**
     * Inverts this matrix and stores it in the given store.
     *
     * @return The store
     */
    public Matrix4 invert(Matrix4 store) {
        if (store == null) {
            store = new Matrix4();
        }

        float fA0 = m00 * m11 - m01 * m10;
        float fA1 = m00 * m12 - m02 * m10;
        float fA2 = m00 * m13 - m03 * m10;
        float fA3 = m01 * m12 - m02 * m11;
        float fA4 = m01 * m13 - m03 * m11;
        float fA5 = m02 * m13 - m03 * m12;
        float fB0 = m20 * m31 - m21 * m30;
        float fB1 = m20 * m32 - m22 * m30;
        float fB2 = m20 * m33 - m23 * m30;
        float fB3 = m21 * m32 - m22 * m31;
        float fB4 = m21 * m33 - m23 * m31;
        float fB5 = m22 * m33 - m23 * m32;
        float fDet = fA0 * fB5 - fA1 * fB4 + fA2 * fB3 + fA3 * fB2 - fA4 * fB1 + fA5 * fB0;

        if (QMath.abs(fDet) <= 0f) {
            throw new ArithmeticException("This matrix cannot be inverted");
        }

        store.m00 = +m11 * fB5 - m12 * fB4 + m13 * fB3;
        store.m10 = -m10 * fB5 + m12 * fB2 - m13 * fB1;
        store.m20 = +m10 * fB4 - m11 * fB2 + m13 * fB0;
        store.m30 = -m10 * fB3 + m11 * fB1 - m12 * fB0;
        store.m01 = -m01 * fB5 + m02 * fB4 - m03 * fB3;
        store.m11 = +m00 * fB5 - m02 * fB2 + m03 * fB1;
        store.m21 = -m00 * fB4 + m01 * fB2 - m03 * fB0;
        store.m31 = +m00 * fB3 - m01 * fB1 + m02 * fB0;
        store.m02 = +m31 * fA5 - m32 * fA4 + m33 * fA3;
        store.m12 = -m30 * fA5 + m32 * fA2 - m33 * fA1;
        store.m22 = +m30 * fA4 - m31 * fA2 + m33 * fA0;
        store.m32 = -m30 * fA3 + m31 * fA1 - m32 * fA0;
        store.m03 = -m21 * fA5 + m22 * fA4 - m23 * fA3;
        store.m13 = +m20 * fA5 - m22 * fA2 + m23 * fA1;
        store.m23 = -m20 * fA4 + m21 * fA2 - m23 * fA0;
        store.m33 = +m20 * fA3 - m21 * fA1 + m22 * fA0;

        float fInvDet = 1.0f / fDet;
        store.multLocal(fInvDet);

        return store;
    }

    /**
     * Inverts this matrix locally.
     *
     * @return this
     */
    public Matrix4 invertLocal() {

        float fA0 = m00 * m11 - m01 * m10;
        float fA1 = m00 * m12 - m02 * m10;
        float fA2 = m00 * m13 - m03 * m10;
        float fA3 = m01 * m12 - m02 * m11;
        float fA4 = m01 * m13 - m03 * m11;
        float fA5 = m02 * m13 - m03 * m12;
        float fB0 = m20 * m31 - m21 * m30;
        float fB1 = m20 * m32 - m22 * m30;
        float fB2 = m20 * m33 - m23 * m30;
        float fB3 = m21 * m32 - m22 * m31;
        float fB4 = m21 * m33 - m23 * m31;
        float fB5 = m22 * m33 - m23 * m32;
        float fDet = fA0 * fB5 - fA1 * fB4 + fA2 * fB3 + fA3 * fB2 - fA4 * fB1 + fA5 * fB0;

        if (QMath.abs(fDet) <= 0f) {
            return zero();
        }

        float f00 = +m11 * fB5 - m12 * fB4 + m13 * fB3;
        float f10 = -m10 * fB5 + m12 * fB2 - m13 * fB1;
        float f20 = +m10 * fB4 - m11 * fB2 + m13 * fB0;
        float f30 = -m10 * fB3 + m11 * fB1 - m12 * fB0;
        float f01 = -m01 * fB5 + m02 * fB4 - m03 * fB3;
        float f11 = +m00 * fB5 - m02 * fB2 + m03 * fB1;
        float f21 = -m00 * fB4 + m01 * fB2 - m03 * fB0;
        float f31 = +m00 * fB3 - m01 * fB1 + m02 * fB0;
        float f02 = +m31 * fA5 - m32 * fA4 + m33 * fA3;
        float f12 = -m30 * fA5 + m32 * fA2 - m33 * fA1;
        float f22 = +m30 * fA4 - m31 * fA2 + m33 * fA0;
        float f32 = -m30 * fA3 + m31 * fA1 - m32 * fA0;
        float f03 = -m21 * fA5 + m22 * fA4 - m23 * fA3;
        float f13 = +m20 * fA5 - m22 * fA2 + m23 * fA1;
        float f23 = -m20 * fA4 + m21 * fA2 - m23 * fA0;
        float f33 = +m20 * fA3 - m21 * fA1 + m22 * fA0;

        m00 = f00;
        m01 = f01;
        m02 = f02;
        m03 = f03;
        m10 = f10;
        m11 = f11;
        m12 = f12;
        m13 = f13;
        m20 = f20;
        m21 = f21;
        m22 = f22;
        m23 = f23;
        m30 = f30;
        m31 = f31;
        m32 = f32;
        m33 = f33;

        float fInvDet = 1.0f / fDet;
        multLocal(fInvDet);

        return this;
    }

    /**
     * Returns a new matrix representing the adjoint of this matrix.
     *
     * @return The adjoint matrix
     */
    public Matrix4 adjoint() {
        return adjoint(null);
    }

    public void setTransform(Vector3 position, Vector3 scale, Matrix3 rotMat) {
        // Ordering:
        // 1. Scale
        // 2. Rotate
        // 3. Translate

        // Set up final matrix with scale, rotation and translation
        m00 = scale.x * rotMat.m00;
        m01 = scale.y * rotMat.m01;
        m02 = scale.z * rotMat.m02;
        m03 = position.x;
        m10 = scale.x * rotMat.m10;
        m11 = scale.y * rotMat.m11;
        m12 = scale.z * rotMat.m12;
        m13 = position.y;
        m20 = scale.x * rotMat.m20;
        m21 = scale.y * rotMat.m21;
        m22 = scale.z * rotMat.m22;
        m23 = position.z;

        // No projection term
        m30 = 0;
        m31 = 0;
        m32 = 0;
        m33 = 1;
    }

    /**
     * Places the adjoint of this matrix in store (creates store if null.)
     *
     * @param store The matrix to store the result in. If null, a new matrix is
     *              created.
     * @return store
     */
    public Matrix4 adjoint(Matrix4 store) {
        if (store == null) {
            store = new Matrix4();
        }

        float fA0 = m00 * m11 - m01 * m10;
        float fA1 = m00 * m12 - m02 * m10;
        float fA2 = m00 * m13 - m03 * m10;
        float fA3 = m01 * m12 - m02 * m11;
        float fA4 = m01 * m13 - m03 * m11;
        float fA5 = m02 * m13 - m03 * m12;
        float fB0 = m20 * m31 - m21 * m30;
        float fB1 = m20 * m32 - m22 * m30;
        float fB2 = m20 * m33 - m23 * m30;
        float fB3 = m21 * m32 - m22 * m31;
        float fB4 = m21 * m33 - m23 * m31;
        float fB5 = m22 * m33 - m23 * m32;

        store.m00 = +m11 * fB5 - m12 * fB4 + m13 * fB3;
        store.m10 = -m10 * fB5 + m12 * fB2 - m13 * fB1;
        store.m20 = +m10 * fB4 - m11 * fB2 + m13 * fB0;
        store.m30 = -m10 * fB3 + m11 * fB1 - m12 * fB0;
        store.m01 = -m01 * fB5 + m02 * fB4 - m03 * fB3;
        store.m11 = +m00 * fB5 - m02 * fB2 + m03 * fB1;
        store.m21 = -m00 * fB4 + m01 * fB2 - m03 * fB0;
        store.m31 = +m00 * fB3 - m01 * fB1 + m02 * fB0;
        store.m02 = +m31 * fA5 - m32 * fA4 + m33 * fA3;
        store.m12 = -m30 * fA5 + m32 * fA2 - m33 * fA1;
        store.m22 = +m30 * fA4 - m31 * fA2 + m33 * fA0;
        store.m32 = -m30 * fA3 + m31 * fA1 - m32 * fA0;
        store.m03 = -m21 * fA5 + m22 * fA4 - m23 * fA3;
        store.m13 = +m20 * fA5 - m22 * fA2 + m23 * fA1;
        store.m23 = -m20 * fA4 + m21 * fA2 - m23 * fA0;
        store.m33 = +m20 * fA3 - m21 * fA1 + m22 * fA0;

        return store;
    }

    /**
     * <code>determinant</code> generates the determinate of this matrix.
     *
     * @return the determinate
     */
    public float determinant() {
        float fA0 = m00 * m11 - m01 * m10;
        float fA1 = m00 * m12 - m02 * m10;
        float fA2 = m00 * m13 - m03 * m10;
        float fA3 = m01 * m12 - m02 * m11;
        float fA4 = m01 * m13 - m03 * m11;
        float fA5 = m02 * m13 - m03 * m12;
        float fB0 = m20 * m31 - m21 * m30;
        float fB1 = m20 * m32 - m22 * m30;
        float fB2 = m20 * m33 - m23 * m30;
        float fB3 = m21 * m32 - m22 * m31;
        float fB4 = m21 * m33 - m23 * m31;
        float fB5 = m22 * m33 - m23 * m32;
        float fDet = fA0 * fB5 - fA1 * fB4 + fA2 * fB3 + fA3 * fB2 - fA4 * fB1 + fA5 * fB0;
        return fDet;
    }

    /**
     * Sets all of the values in this matrix to zero.
     *
     * @return this matrix
     */
    public Matrix4 zero() {
        m00 = m01 = m02 = m03 = 0.0f;
        m10 = m11 = m12 = m13 = 0.0f;
        m20 = m21 = m22 = m23 = 0.0f;
        m30 = m31 = m32 = m33 = 0.0f;
        return this;
    }

    public Matrix4 add(Matrix4 mat) {
        Matrix4 result = new Matrix4();
        result.m00 = this.m00 + mat.m00;
        result.m01 = this.m01 + mat.m01;
        result.m02 = this.m02 + mat.m02;
        result.m03 = this.m03 + mat.m03;
        result.m10 = this.m10 + mat.m10;
        result.m11 = this.m11 + mat.m11;
        result.m12 = this.m12 + mat.m12;
        result.m13 = this.m13 + mat.m13;
        result.m20 = this.m20 + mat.m20;
        result.m21 = this.m21 + mat.m21;
        result.m22 = this.m22 + mat.m22;
        result.m23 = this.m23 + mat.m23;
        result.m30 = this.m30 + mat.m30;
        result.m31 = this.m31 + mat.m31;
        result.m32 = this.m32 + mat.m32;
        result.m33 = this.m33 + mat.m33;
        return result;
    }

    /**
     * <code>add</code> adds the values of a parameter matrix to this matrix.
     *
     * @param mat the matrix to add to this.
     */
    public void addLocal(Matrix4 mat) {
        m00 += mat.m00;
        m01 += mat.m01;
        m02 += mat.m02;
        m03 += mat.m03;
        m10 += mat.m10;
        m11 += mat.m11;
        m12 += mat.m12;
        m13 += mat.m13;
        m20 += mat.m20;
        m21 += mat.m21;
        m22 += mat.m22;
        m23 += mat.m23;
        m30 += mat.m30;
        m31 += mat.m31;
        m32 += mat.m32;
        m33 += mat.m33;
    }

    public Vector3 toTranslationVector() {
        return Vector3.of(m03, m13, m23);
    }

    public void toTranslationVector(Vector3 vector) {
        vector.set(m03, m13, m23);
    }

    public Cuaternion toRotationQuat() {
        Cuaternion quat = new Cuaternion();
        quat.fromRotationMatrix(toRotationMatrix());
        return quat;
    }

    public void toRotationQuat(Cuaternion q) {
        q.fromRotationMatrix(toRotationMatrix());
    }

    public Matrix3 toRotationMatrix() {
        return new Matrix3(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    public void toRotationMatrix(Matrix3 mat) {
        mat.m00 = m00;
        mat.m01 = m01;
        mat.m02 = m02;
        mat.m10 = m10;
        mat.m11 = m11;
        mat.m12 = m12;
        mat.m20 = m20;
        mat.m21 = m21;
        mat.m22 = m22;
    }

    /**
     * Retreives the scale vector from the matrix.
     *
     * @return the scale vector
     */
    public Vector3 toScaleVector() {
        Vector3 result = Vector3.empty();
        this.toScaleVector(result);
        return result;
    }

    /**
     * Retreives the scale vector from the matrix and stores it into a given
     * vector.
     *
     * @param vector the vector where the scale will be stored
     */
    public void toScaleVector(Vector3 vector) {
        float scaleX = (float) Math.sqrt(m00 * m00 + m10 * m10 + m20 * m20);
        float scaleY = (float) Math.sqrt(m01 * m01 + m11 * m11 + m21 * m21);
        float scaleZ = (float) Math.sqrt(m02 * m02 + m12 * m12 + m22 * m22);
        vector.set(scaleX, scaleY, scaleZ);
    }

    /**
     * Sets the scale.
     *
     * @param x the X scale
     * @param y the Y scale
     * @param z the Z scale
     */
    public void setScale(float x, float y, float z) {

        Vector3 vect1 = Vector3.of(m00, m10, m20);
        vect1.normalize();
        vect1.multiply(x);
        m00 = vect1.x;
        m10 = vect1.y;
        m20 = vect1.z;

        vect1.set(m01, m11, m21);
        vect1.normalize();
        vect1.multiply(y);
        m01 = vect1.x;
        m11 = vect1.y;
        m21 = vect1.z;

        vect1.set(m02, m12, m22);
        vect1.normalize();
        vect1.multiply(z);
        m02 = vect1.x;
        m12 = vect1.y;
        m22 = vect1.z;

        vect1 = null;
    }

    /**
     * Sets the scale.
     *
     * @param scale the scale vector to set
     */
    public void setScale(Vector3 scale) {
        this.setScale(scale.x, scale.y, scale.z);
    }

    /**
     * <code>setTranslation</code> will set the matrix's translation values.
     *
     * @param translation the new values for the translation.
     *
     */
    public void setTranslation(float[] translation) {
        if (translation.length != 3) {
            throw new IllegalArgumentException(
                    "Translation size must be 3.");
        }
        m03 = translation[0];
        m13 = translation[1];
        m23 = translation[2];
    }

    /**
     * <code>setTranslation</code> will set the matrix's translation values.
     *
     * @param x value of the translation on the x axis
     * @param y value of the translation on the y axis
     * @param z value of the translation on the z axis
     */
    public void setTranslation(float x, float y, float z) {
        m03 = x;
        m13 = y;
        m23 = z;
    }

    /**
     * <code>setTranslation</code> will set the matrix's translation values.
     *
     * @param translation the new values for the translation.
     */
    public void setTranslation(Vector3 translation) {
        m03 = translation.x;
        m13 = translation.y;
        m23 = translation.z;
    }

    /**
     * <code>setInverseTranslation</code> will set the matrix's inverse
     * translation values.
     *
     * @param translation the new values for the inverse translation. *
     */
    public void setInverseTranslation(float[] translation) {
        if (translation.length != 3) {
            throw new IllegalArgumentException(
                    "Translation size must be 3.");
        }
        m03 = -translation[0];
        m13 = -translation[1];
        m23 = -translation[2];
    }

    /**
     * <code>angleRotation</code> sets this matrix to that of a rotation about
     * three axes (x, y, z). Where each axis has a specified rotation in
     * degrees. These rotations are expressed in a single <code>QVector3</code>
     * object.
     *
     * @param angles the angles to rotate.
     */
    public void angleRotation(Vector3 angles) {
        float angle;
        float sr, sp, sy, cr, cp, cy;

        angle = (angles.z * QMath.DEG_TO_RAD);
        sy = QMath.sin(angle);
        cy = QMath.cos(angle);
        angle = (angles.y * QMath.DEG_TO_RAD);
        sp = QMath.sin(angle);
        cp = QMath.cos(angle);
        angle = (angles.x * QMath.DEG_TO_RAD);
        sr = QMath.sin(angle);
        cr = QMath.cos(angle);

        // matrix = (Z * Y) * X
        m00 = cp * cy;
        m10 = cp * sy;
        m20 = -sp;
        m01 = sr * sp * cy + cr * -sy;
        m11 = sr * sp * sy + cr * cy;
        m21 = sr * cp;
        m02 = (cr * sp * cy + -sr * -sy);
        m12 = (cr * sp * sy + -sr * cy);
        m22 = cr * cp;
        m03 = 0.0f;
        m13 = 0.0f;
        m23 = 0.0f;
    }

    /**
     * <code>setRotationCuaternion</code> builds a rotation from a
     * <code>Cuaternion</code>.
     *
     * @param quat the quaternion to build the rotation from.
     * @throws NullPointerException if quat is null.
     */
    public void setRotationCuaternion(Cuaternion quat) {
        quat.toRotationMatrix(this);
    }

    /**
     * <code>setInverseRotationRadians</code> builds an inverted rotation from
     * Euler angles that are in radians.
     *
     * @param angles the Euler angles in radians.
     *
     */
    public void setInverseRotationRadians(float[] angles) {
        if (angles.length != 3) {
            throw new IllegalArgumentException(
                    "Angles must be of size 3.");
        }
        double cr = QMath.cos(angles[0]);
        double sr = QMath.sin(angles[0]);
        double cp = QMath.cos(angles[1]);
        double sp = QMath.sin(angles[1]);
        double cy = QMath.cos(angles[2]);
        double sy = QMath.sin(angles[2]);

        m00 = (float) (cp * cy);
        m10 = (float) (cp * sy);
        m20 = (float) (-sp);

        double srsp = sr * sp;
        double crsp = cr * sp;

        m01 = (float) (srsp * cy - cr * sy);
        m11 = (float) (srsp * sy + cr * cy);
        m21 = (float) (sr * cp);

        m02 = (float) (crsp * cy + sr * sy);
        m12 = (float) (crsp * sy - sr * cy);
        m22 = (float) (cr * cp);
    }

    /**
     * <code>setInverseRotationDegrees</code> builds an inverted rotation from
     * Euler angles that are in degrees.
     *
     * @param angles the Euler angles in degrees.
     *
     */
    public void setInverseRotationDegrees(float[] angles) {
        if (angles.length != 3) {
            throw new IllegalArgumentException(
                    "Angles must be of size 3.");
        }
        float vec[] = new float[3];
        vec[0] = (angles[0] * QMath.RAD_TO_DEG);
        vec[1] = (angles[1] * QMath.RAD_TO_DEG);
        vec[2] = (angles[2] * QMath.RAD_TO_DEG);
        setInverseRotationRadians(vec);
    }

    /**
     *
     * <code>inverseTranslateVect</code> translates a given QVector3 by the
     * translation part of this matrix.
     *
     * @param vec the QVector3 data to be translated.
     *
     */
    public void inverseTranslateVect(float[] vec) {
        if (vec.length != 3) {
            throw new IllegalArgumentException(
                    "vec must be of size 3.");
        }

        vec[0] = vec[0] - m03;
        vec[1] = vec[1] - m13;
        vec[2] = vec[2] - m23;
    }

    /**
     *
     * <code>inverseTranslateVect</code> translates a given QVector3 by the
     * translation part of this matrix.
     *
     * @param data the QVector3 to be translated.
     */
    public void inverseTranslateVect(Vector3 data) {
        data.x -= m03;
        data.y -= m13;
        data.z -= m23;
    }

    /**
     *
     * <code>inverseTranslateVect</code> translates a given QVector3 by the
     * translation part of this matrix.
     *
     * @param data the QVector3 to be translated.
     */
    public void translateVect(Vector3 data) {
        data.x += m03;
        data.y += m13;
        data.z += m23;
    }

    /**
     *
     * <code>inverseRotateVect</code> rotates a given QVector3 by the rotation
     * part of this matrix.
     *
     * @param vec the QVector3 to be rotated.
     */
    public void inverseRotateVect(Vector3 vec) {
        float vx = vec.x, vy = vec.y, vz = vec.z;

        vec.x = vx * m00 + vy * m10 + vz * m20;
        vec.y = vx * m01 + vy * m11 + vz * m21;
        vec.z = vx * m02 + vy * m12 + vz * m22;
    }

    public void rotateVect(Vector3 vec) {
        float vx = vec.x, vy = vec.y, vz = vec.z;

        vec.x = vx * m00 + vy * m01 + vz * m02;
        vec.y = vx * m10 + vy * m11 + vz * m12;
        vec.z = vx * m20 + vy * m21 + vz * m22;
    }

    /**
     * <code>toString</code> returns the string representation of this object.
     * It is in a format of a 4x4 matrix. For example, an identity matrix would
     * be represented by the following string. com.jme.math.QMatriz3 <br>
     * [<br>
     * 1.0 0.0 0.0 0.0 <br>
     * 0.0 1.0 0.0 0.0 <br>
     * 0.0 0.0 1.0 0.0 <br>
     * 0.0 0.0 0.0 1.0 <br>
     * ]<br>
     *
     * @return the string representation of this object.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Matrix4f\n[\n");
        result.append(" ");
        result.append(m00);
        result.append("  ");
        result.append(m01);
        result.append("  ");
        result.append(m02);
        result.append("  ");
        result.append(m03);
        result.append(" \n");
        result.append(" ");
        result.append(m10);
        result.append("  ");
        result.append(m11);
        result.append("  ");
        result.append(m12);
        result.append("  ");
        result.append(m13);
        result.append(" \n");
        result.append(" ");
        result.append(m20);
        result.append("  ");
        result.append(m21);
        result.append("  ");
        result.append(m22);
        result.append("  ");
        result.append(m23);
        result.append(" \n");
        result.append(" ");
        result.append(m30);
        result.append("  ");
        result.append(m31);
        result.append("  ");
        result.append(m32);
        result.append("  ");
        result.append(m33);
        result.append(" \n]");
        return result.toString();
    }

    /**
     *
     * <code>hashCode</code> returns the hash code value as an integer and is
     * supported for the benefit of hashing based collection classes such as
     * Hashtable, HashMap, HashSet etc.
     *
     * @return the hashcode for this instance of QMatriz4.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 37;
        hash = 37 * hash + Float.floatToIntBits(m00);
        hash = 37 * hash + Float.floatToIntBits(m01);
        hash = 37 * hash + Float.floatToIntBits(m02);
        hash = 37 * hash + Float.floatToIntBits(m03);

        hash = 37 * hash + Float.floatToIntBits(m10);
        hash = 37 * hash + Float.floatToIntBits(m11);
        hash = 37 * hash + Float.floatToIntBits(m12);
        hash = 37 * hash + Float.floatToIntBits(m13);

        hash = 37 * hash + Float.floatToIntBits(m20);
        hash = 37 * hash + Float.floatToIntBits(m21);
        hash = 37 * hash + Float.floatToIntBits(m22);
        hash = 37 * hash + Float.floatToIntBits(m23);

        hash = 37 * hash + Float.floatToIntBits(m30);
        hash = 37 * hash + Float.floatToIntBits(m31);
        hash = 37 * hash + Float.floatToIntBits(m32);
        hash = 37 * hash + Float.floatToIntBits(m33);

        return hash;
    }

    /**
     * are these two matrices the same? they are is they both have the same mXX
     * values.
     *
     * @param o the object to compare for equality
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Matrix4) || o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        Matrix4 comp = (Matrix4) o;
        if (Float.compare(m00, comp.m00) != 0) {
            return false;
        }
        if (Float.compare(m01, comp.m01) != 0) {
            return false;
        }
        if (Float.compare(m02, comp.m02) != 0) {
            return false;
        }
        if (Float.compare(m03, comp.m03) != 0) {
            return false;
        }

        if (Float.compare(m10, comp.m10) != 0) {
            return false;
        }
        if (Float.compare(m11, comp.m11) != 0) {
            return false;
        }
        if (Float.compare(m12, comp.m12) != 0) {
            return false;
        }
        if (Float.compare(m13, comp.m13) != 0) {
            return false;
        }

        if (Float.compare(m20, comp.m20) != 0) {
            return false;
        }
        if (Float.compare(m21, comp.m21) != 0) {
            return false;
        }
        if (Float.compare(m22, comp.m22) != 0) {
            return false;
        }
        if (Float.compare(m23, comp.m23) != 0) {
            return false;
        }

        if (Float.compare(m30, comp.m30) != 0) {
            return false;
        }
        if (Float.compare(m31, comp.m31) != 0) {
            return false;
        }
        if (Float.compare(m32, comp.m32) != 0) {
            return false;
        }
        if (Float.compare(m33, comp.m33) != 0) {
            return false;
        }

        return true;
    }

    /**
     * @return true if this matrix is identity
     */
    public boolean isIdentity() {
        return (m00 == 1 && m01 == 0 && m02 == 0 && m03 == 0)
                && (m10 == 0 && m11 == 1 && m12 == 0 && m13 == 0)
                && (m20 == 0 && m21 == 0 && m22 == 1 && m23 == 0)
                && (m30 == 0 && m31 == 0 && m32 == 0 && m33 == 1);
    }

    /**
     * Apply a scale to this matrix.
     *
     * @param scale the scale to apply
     */
    public void scale(Vector3 scale) {
        m00 *= scale.x;
        m10 *= scale.x;
        m20 *= scale.x;
        m30 *= scale.x;
        m01 *= scale.y;
        m11 *= scale.y;
        m21 *= scale.y;
        m31 *= scale.y;
        m02 *= scale.z;
        m12 *= scale.z;
        m22 *= scale.z;
        m32 *= scale.z;
    }

    // XXX: This tests more solid than converting the q to a matrix and
    // multiplying... why?
    public void multLocal(Cuaternion rotation) {
        Vector3 axis = Vector3.empty();
        float angle = rotation.toAngleAxis(axis);
        Matrix4 matrix4f = new Matrix4();
        matrix4f.fromAngleAxis(angle, axis);
        multLocal(matrix4f);
    }

    @Override
    public Matrix4 clone() {
        try {
            return (Matrix4) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // can not happen
        }
    }

}
