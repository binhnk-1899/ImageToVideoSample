package com.loanxu.photoeditor.utils

import android.opengl.GLES20.*

object ShaderHelper {

    fun loadShader(type: Int, shaderCode: String): Int {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        return glCreateShader(type).also { shader ->
            // add the source code to the shader and compile it
            glShaderSource(shader, shaderCode)
            glCompileShader(shader)
        }
    }

    fun creatProgram(vertexShaderSoure:String ,fragmentShaderSoure:String ): Int {
        var program: Int=0
        program = glCreateProgram().also {

            glAttachShader(it,
                loadShader(
                    GL_VERTEX_SHADER,
                    vertexShaderSoure
                )
            )
            glAttachShader(it,
                loadShader(
                    GL_FRAGMENT_SHADER,
                    fragmentShaderSoure
                )
            )
            glLinkProgram(it)
        }
        return program

    }
}