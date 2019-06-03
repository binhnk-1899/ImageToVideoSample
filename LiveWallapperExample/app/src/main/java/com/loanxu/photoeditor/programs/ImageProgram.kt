package com.loanxu.photoeditor.programs

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.*

class ImageProgram(context: Context, resVertex: Int, resFragment: Int) :
    ShaderProgram(context, resVertex, resFragment) {
    var uMatrixHandle: Int = glGetUniformLocation(program, U_MATRIX)
    var uTextureHandle: Int = glGetUniformLocation(program, U_TEXTURE)
    var aPositionHandle: Int = glGetAttribLocation(program, A_POSITION)
    var aVertexHandle: Int = glGetAttribLocation(program, A_TEXCOORD)


    fun setUniforms(matrix: FloatArray, textureId: Int) {
        glUniformMatrix4fv(uMatrixHandle, 1, false, matrix, 0)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glUniform1i(uTextureHandle, 0)
    }
}