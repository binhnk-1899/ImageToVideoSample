package com.loanxu.photoeditor.objs

import android.opengl.GLES20.*
import com.loanxu.photoeditor.datas.VertexArray
import com.loanxu.photoeditor.programs.ImageProgram
import com.loanxu.photoeditor.programs.ShaderProgram
import com.loanxu.photoeditor.utils.Constants
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer

class ImageGL : GLObject() {
    private val indicesData = shortArrayOf(0, 1, 2, 0, 2, 3)

    // fixed coordinator
    private var verticesData = floatArrayOf(
        9 / 16f, 1f, 0.0f, // Position 0
        0.0f, 0.0f, // TexCoord 0
        9 / 16f, -1f, 0.0f, // Position 1
        0.0f, 1.0f, // TexCoord 1
        -9 / 16f, -1f, 0.0f, // Position 2
        1.0f, 1.0f, // TexCoord 2
        -9 / 16f, 1f, 0.0f, // Position 3
        1.0f, 0.0f // TexCoord 3
    )
    private var vertexArray: VertexArray

    private var indexArray: ShortBuffer

    init {
        vertexArray = VertexArray(verticesData)
        indexArray = ByteBuffer.allocateDirect(indicesData.size * 2)
            .order(ByteOrder.nativeOrder()).asShortBuffer()
        indexArray.put(indicesData).position(0)
    }

    override fun draw() {
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, indexArray);
    }

    override fun bindData(program: ShaderProgram) {
        var p = program as ImageProgram
        var currentOffset = 0

        vertexArray.setVertexAttribPointer(
            currentOffset,
            p.aPositionHandle,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        currentOffset += POSITION_COMPONENT_COUNT

        vertexArray.setVertexAttribPointer(
            currentOffset,
            p.aVertexHandle,
            TEXCOORD_COMPONENT_COUNT,
            STRIDE
        )
    }

    companion object {
        const val POSITION_COMPONENT_COUNT = 3
        const val TEXCOORD_COMPONENT_COUNT = 2
        private const val TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT +
                TEXCOORD_COMPONENT_COUNT
        val STRIDE = TOTAL_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT
    }
}