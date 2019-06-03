package com.loanxu.photoeditor.objs

import com.loanxu.photoeditor.programs.ShaderProgram

abstract class GLObject {
    abstract fun draw()
    abstract fun bindData(program:ShaderProgram)
}