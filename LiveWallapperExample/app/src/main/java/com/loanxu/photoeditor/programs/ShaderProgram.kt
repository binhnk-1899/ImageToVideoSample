package com.loanxu.photoeditor.programs

import android.content.Context
import android.opengl.GLES20
import com.loanxu.photoeditor.utils.RawReader
import com.loanxu.photoeditor.utils.ShaderHelper

open class ShaderProgram(context: Context, resVertex: Int, resFragment: Int) {
    //uniform
    protected val U_MATRIX = "u_Matrix"
    protected val U_TEXTURE = "u_texture"
    protected val U_TIME = "u_Time"
    protected val U_ANGLE = "u_Angle"
    protected val U_INCREASE = "u_Increase"
    protected val U_SIZE = "u_size"
    protected val U_REVERSEFLY = "u_reverse"

    //Attribute constants
    protected val A_POSITION = "a_Position"
    protected val A_TEXCOORD = "a_texCoord"
    protected val A_REVERSE = "a_Reverse"
    protected val A_COLOR = "a_Color"
    protected val A_SIZE = "a_size"
    protected val A_TIME = "a_Time"
    protected val A_PHI = "a_Phi"
    protected val A_SPEED = "a_Speed"
    protected val A_TYPE = "a_Type"
    protected val A_DIRECTION = "a_DirectionVector"


    protected var program: Int = ShaderHelper.creatProgram(
        RawReader.readTFfromRes(context, resVertex),
        RawReader.readTFfromRes(context, resFragment)
    )

    fun useProgram() {
        GLES20.glUseProgram(program)
    }
}