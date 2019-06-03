package com.loanxu.photoeditor.programs

import android.content.Context
import android.opengl.GLES20
import com.loanxu.photoeditor.utils.Constants

class SpriteProgram(context: Context, resVertex: Int, resFragment: Int) :
    ShaderProgram(context, resVertex, resFragment) {
    private val uMatrixHandle: Int
    val uTextureHandle: Int
    val aPositionHandle: Int
    val aColorHandle: Int
    val aTimeHandle: Int
    val aReverseHandle: Int
    val uSizeHandle: Int
    val uAngleHandle: Int
    val uReverseHandle: Int

    init {
        uMatrixHandle = GLES20.glGetUniformLocation(program, U_MATRIX)
        uSizeHandle = GLES20.glGetUniformLocation(program, U_SIZE)
        uAngleHandle = GLES20.glGetUniformLocation(program, U_ANGLE)
        uReverseHandle = GLES20.glGetUniformLocation(program, U_REVERSEFLY)
        uTextureHandle = GLES20.glGetAttribLocation(program, U_TEXTURE)
        aPositionHandle = GLES20.glGetAttribLocation(program, A_POSITION)
        aColorHandle = GLES20.glGetAttribLocation(program, A_COLOR)
        aTimeHandle = GLES20.glGetAttribLocation(program, A_TIME)
        aReverseHandle = GLES20.glGetAttribLocation(program, A_REVERSE)
    }


    fun setUniforms(matrix: FloatArray, timeSun: Long, textureId: Int, isSun: Boolean) {
        GLES20.glUniformMatrix4fv(uMatrixHandle, 1, false, matrix, 0)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(uTextureHandle, 0)
        if (isSun) {
            GLES20.glUniform1f(uSizeHandle, getSizeSun(timeSun))
        } else {
            GLES20.glUniform1f(uSizeHandle, Constants.SIZE_MOON)
        }
    }

    fun setUniforms(matrix: FloatArray, timeSun: Float, textureId: Int, reverse:Float) {
        GLES20.glUniformMatrix4fv(uMatrixHandle, 1, false, matrix, 0)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(uTextureHandle, 0)
        GLES20.glUniform1f(uSizeHandle, Constants.SIZE_SPRITE)
        GLES20.glUniform1f(uReverseHandle, reverse)
        GLES20.glUniform1f(uAngleHandle, timeSun)

    }

    fun getSizeSun(timeSun: Long): Float {
        var size = ((Constants.timeDay - timeSun) * 100 / Constants.timeDay) + 225f
        if (size > Constants.MAX_SIZE_SUN) return Constants.MAX_SIZE_SUN
        else return size
    }

}