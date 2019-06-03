package com.loanxu.photoeditor.programs

import android.content.Context
import android.opengl.GLES20
import kotlin.Int as Int1

class FireFlyProgram(context: Context, resVertex: Int1, resFragment: Int1) :
    ShaderProgram(context, resVertex, resFragment) {
    private val uMatrixHandle: Int1 = GLES20.glGetUniformLocation(program, U_MATRIX)
    private val uTextureHandle: Int1 = GLES20.glGetUniformLocation(program, U_TEXTURE)
    private val uTimeHandle: Int1 = GLES20.glGetUniformLocation(program, U_TIME)
    private val uAngleHandle: Int1 = GLES20.glGetUniformLocation(program, U_ANGLE)
    private val uIncreaseHandle: Int1 = GLES20.glGetUniformLocation(program, U_INCREASE)
    val aPositionHandle: Int1 = GLES20.glGetAttribLocation(program, A_POSITION)
    val aColorHandle: Int1 = GLES20.glGetAttribLocation(program, A_COLOR)
    val aSizeHandle: Int1 = GLES20.glGetAttribLocation(program, A_SIZE)
    val aPhiHandle: Int1 = GLES20.glGetAttribLocation(program, A_PHI)
    val aSpeedHandle: Int1 = GLES20.glGetAttribLocation(program, A_SPEED)
    val aTypeHandle: Int1 = GLES20.glGetAttribLocation(program, A_TYPE)
    val aTimeHandle: Int1 = GLES20.glGetAttribLocation(program, A_TIME)
    val aDirectionHandle: Int1 = GLES20.glGetAttribLocation(program, A_DIRECTION)

    fun setUniforms(matrix: FloatArray, angle: Float, time: Float, isIncrease: Boolean, textureId: Int1) {
        GLES20.glUniformMatrix4fv(uMatrixHandle, 1, false, matrix, 0)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(uTextureHandle, 0)
        GLES20.glUniform1f(uTimeHandle, time)
        GLES20.glUniform1f(uAngleHandle, angle)
        val increase = if (isIncrease) 1f
        else 0f
        GLES20.glUniform1f(uIncreaseHandle, increase)
    }
}