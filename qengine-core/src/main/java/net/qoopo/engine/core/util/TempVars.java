package net.qoopo.engine.core.util;

import net.qoopo.engine.core.entity.component.mesh.primitive.VertexBuffer;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.math.Cuaternion;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.Matrix3;
import net.qoopo.engine.core.math.Matrix4;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.math.Vector4;

/**
 * Temporary variables assigned to each thread. Engine classes may access these
 * temp variables with TempVars.get(), all retrieved TempVars instances must be
 * returned via TempVars.release(). This returns an available instance of the
 * TempVar class ensuring this particular instance is never used elsewhere in
 * the mean time.
 */
public class TempVars {

    /**
     * Allow X instances of TempVars in a single thread.
     */
    private static final int STACK_SIZE = 50;

    /**
     * ThreadLocal to store a TempVarsStack for each thread. This ensures each
     * thread has a single TempVarsStack that is used only in method calls in
     * that thread.
     */
    private static final ThreadLocal<TempVarsStack> varsLocal = new ThreadLocal<TempVarsStack>() {

        @Override
        public TempVarsStack initialValue() {
            return new TempVarsStack();
        }
    };

    /**
     * Acquire an instance of the TempVar class. You have to release the
     * instance after use by calling the release() method. If more than
     * STACK_SIZE (currently 5) instances are requested in a single thread then
     * an ArrayIndexOutOfBoundsException will be thrown.
     *
     * @return A TempVar instance
     */
    public static TempVars get() {
        TempVarsStack stack = varsLocal.get();
        TempVars instance = stack.tempVars[stack.index];
        if (instance == null) {
            // Create new
            instance = new TempVars();
            // Put it in there
            stack.tempVars[stack.index] = instance;
        }
        stack.index++;
        instance.isUsed = true;
        return instance;
    }

    /**
     * This instance of TempVars has been retrieved but not released yet.
     */
    private boolean isUsed = false;

    /**
     * Color
     */
    public final QColor color = new QColor();
    /**
     * Vectores de 2 dimensiones
     */
    public final Vector2 vector2f1 = new Vector2();
    public final Vector2 vector2f2 = new Vector2();
    public final Vector2 vector2f3 = new Vector2();
    public final Vector2 vector2f4 = new Vector2();
    public final Vector2 vector2f5 = new Vector2();
    /**
     * Vectores de 3 dimensiones
     */
    public final Vector3 vector3f1 = Vector3.empty();
    public final Vector3 vector3f2 = Vector3.empty();
    public final Vector3 vector3f3 = Vector3.empty();
    public final Vector3 vector3f4 = Vector3.empty();
    public final Vector3 vector3f5 = Vector3.empty();
    public final Vector3 vector3f6 = Vector3.empty();
    public final Vector3 vector3f7 = Vector3.empty();
    public final Vector3 vector3f8 = Vector3.empty();
    public final Vector3 vector3f9 = Vector3.empty();
    public final Vector3 vector3f10 = Vector3.empty();
    /**
     * Vectores de 4 dimensiones
     */
    public final Vector4 vector4f1 = new Vector4();
    public final Vector4 vector4f2 = new Vector4();
    public final Vector4 vector4f3 = new Vector4();
    public final Vector4 vector4f4 = new Vector4();

    /**
     * Buffer de vertices
     */
    public final VertexBuffer bufferVertices1 = new VertexBuffer();
    public final VertexBuffer bufferVertices2 = new VertexBuffer();
    public final VertexBuffer bufferVertices3 = new VertexBuffer();
    /**
     * Vertices
     */
    public final Vertex vertex1 = new Vertex();
    public final Vertex vertex2 = new Vertex();
    public final Vertex vertex3 = new Vertex();
    public final Vertex vertex4 = new Vertex();
    public final Vertex vertex5 = new Vertex();
    public final Vertex vertex6 = new Vertex();
    public final Vertex vertex7 = new Vertex();
    public final Vertex vertex8 = new Vertex();

    /**
     * General matrices.
     */
    public final Matrix3 tempMat3 = new Matrix3();
    public final Matrix4 tempMat4 = new Matrix4();
    /**
     * General quaternions.
     */
    public final Cuaternion quat1 = new Cuaternion();
    public final Cuaternion quat2 = new Cuaternion();

    private TempVars() {
    }

    /**
     * Releases this instance of TempVars. Once released, the contents of the
     * TempVars are undefined. The TempVars must be released in the opposite
     * order that they are retrieved, e.g. Acquiring vars1, then acquiring
     * vars2, vars2 MUST be released first otherwise an exception will be
     * thrown.
     */
    public void release() {
        if (!isUsed) {
            throw new IllegalStateException("This instance of TempVars was already released!");
        }

        isUsed = false;

        TempVarsStack stack = varsLocal.get();

        // Return it to the stack
        stack.index--;

        // Check if it is actually there
        if (stack.tempVars[stack.index] != this) {
            throw new IllegalStateException("An instance of TempVars has not been released in a called method!");
        }
    }

    /**
     * <code>TempVarsStack</code> contains a stack of TempVars. Every time
     * TempVars.get() is called, a new entry is added to the stack, and the
     * index incremented. When TempVars.release() is called, the entry is
     * checked against the current instance and then the index is decremented.
     */
    private static class TempVarsStack {

        int index = 0;
        TempVars[] tempVars = new TempVars[STACK_SIZE];
    }

}
