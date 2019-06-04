package com.binhnk.sample.openglessample

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle {

    companion object {
        private val vertexShaderCode =
        // This matrix member variable provides a hook to manipulate
                // the coordinates of the objects that use this vertex shader
                "uniform mat4 uMVPMatrix;" +
                        "attribute vec4 vPosition;" +
                        "void main() {" +
                        // the matrix must be included as a modifier of gl_Position
                        // Note that the uMVPMatrix factor *must be first* in order
                        // for the matrix multiplication product to be correct.
                        "  gl_Position = uMVPMatrix * vPosition;" +
                        "}";

        private val fragmentShaderCode =
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}";

        private var vertexBuffer: FloatBuffer? = null
        private var mProgram: Int? = null
        private var mPositionHandle: Int? = null
        private var mColorHandle: Int? = null
        private var mMVPMatrixHandle: Int? = null

        // number of coordinates per vertex in  this array
        val COORDS_PER_VERTEX = 3
        val triangleCoords = floatArrayOf(
                0.0f,  0.622008459f, 0.0f,   // top
                -0.5f, -0.311004243f, 0.0f,   // bottom left
                0.5f, -0.311004243f, 0.0f    // bottom right
        )

        private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX
        private val vertexStride = COORDS_PER_VERTEX * 4

        var color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 0.0f)

    }

    init {
        val bb: ByteBuffer = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.let {
            it.put(triangleCoords)
            it.position(0)
        }
        val vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        mProgram = GLES20.glCreateProgram()
        mProgram?.let {
            GLES20.glAttachShader(it, vertexShader)     // add the vertex shader to program
            GLES20.glAttachShader(it, fragmentShader)   // add the fragment shader to program
            GLES20.glLinkProgram(it)                    // create OpenGL program executables
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(mProgram!!)

        mPositionHandle = GLES20.glGetAttribLocation(mProgram!!, "vPosition")
        GLES20.glEnableVertexAttribArray(mPositionHandle!!)
        mPositionHandle?.let {
            GLES20.glVertexAttribPointer(
                    it, COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT, false,
                    vertexStride, vertexBuffer
            )
        }

        mColorHandle = GLES20.glGetAttribLocation(mProgram!!, "vColor")
        mColorHandle?.let {
            GLES20.glUniform4fv(it, 1, color, 0)
        }

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram!!, "uMVPMatrix")
        MyGLRenderer.checkGlError("glGetUniformLocation")
        mMVPMatrixHandle?.let {
            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(it, 1, false, mvpMatrix, 0)
        }
        MyGLRenderer.checkGlError("glUniformMatrix4fv")
        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle!!)
    }
}