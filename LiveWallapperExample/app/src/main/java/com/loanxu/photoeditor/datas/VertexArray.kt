package com.loanxu.photoeditor.datas

import android.opengl.GLES20.*
import android.opengl.GLES20.glEnableVertexAttribArray
import com.loanxu.photoeditor.utils.Constants.BYTES_PER_FLOAT
import java.nio.*

open class VertexArray() {
    lateinit var vertexBuffer: FloatBuffer

    constructor(vertexData: FloatArray):this(){
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)
    }

    /**
     * du lieu cuc bo dk lien ket vs shader
     */
    fun setVertexAttribPointer(
        dataOffset: Int, attributeLocation: Int,
        componentCount: Int, stride: Int
    ) {
        vertexBuffer.position(dataOffset)
        glVertexAttribPointer(
            attributeLocation,
            componentCount,
            GL_FLOAT,
            false,
            stride,
            vertexBuffer
        )
        glEnableVertexAttribArray(attributeLocation)
        vertexBuffer.position(0)
    }

    fun updateBuffer(vertexData: FloatArray, start: Int, count: Int) {
        vertexBuffer.position(start)
        vertexBuffer.put(vertexData, start, count)
        vertexBuffer.position(0)
    }


}