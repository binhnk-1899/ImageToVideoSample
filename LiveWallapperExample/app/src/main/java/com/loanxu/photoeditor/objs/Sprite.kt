package com.loanxu.photoeditor.objs

import android.graphics.Color
import android.opengl.GLES20
import com.loanxu.photoeditor.datas.VertexArray
import com.loanxu.photoeditor.programs.ShaderProgram
import com.loanxu.photoeditor.programs.SpriteProgram
import com.loanxu.photoeditor.utils.Constants
import com.loanxu.photoeditor.utils.Geometry

open class Sprite(color: Int,maxSprite: Int) : GLObject() {
    private var vertexData: FloatArray
    private var vertexArray: VertexArray
    private var color: Int
    private var nextSprite=0
    private var maxSprite:Int
    private var currentSprite=0

    init {
        this.color = color
        this.maxSprite=maxSprite
        vertexData = FloatArray(maxSprite*TOTAL_COMPONENT_COUNT)
        vertexArray = VertexArray(vertexData)


    }

    override fun draw() {
        // Draw the point.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, currentSprite)
    }

    override fun bindData(program: ShaderProgram) {
        var p = program as SpriteProgram
        var dataOffset = 0
        //set buffer data position xyz
        vertexArray.setVertexAttribPointer(
            dataOffset,
            p.aPositionHandle,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += POSITION_COMPONENT_COUNT

        //set buffer data color
        vertexArray.setVertexAttribPointer(
            dataOffset,
            p.aColorHandle,
            COLOR_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += COLOR_COMPONENT_COUNT

        vertexArray.setVertexAttribPointer(
            dataOffset,
            p.aTimeHandle,
            TIME_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun addSprite(point: Geometry.Point, timeadd: Float, reverse:Float){
        var spriteOffset=nextSprite* TOTAL_COMPONENT_COUNT
        var currentOffset=spriteOffset
        nextSprite++
        if (currentSprite<maxSprite){
            currentSprite++
        }
        if (nextSprite==maxSprite){
            nextSprite=0
        }
        vertexData[currentOffset++] = point.x
        vertexData[currentOffset++] = point.y
        vertexData[currentOffset++] = point.z

        vertexData[currentOffset++] = Color.red(color) / 255f
        vertexData[currentOffset++] = Color.green(color) / 255f
        vertexData[currentOffset++] = Color.blue(color) / 255f

        vertexData[currentOffset++] = timeadd

        vertexData[currentOffset++] = reverse

        vertexArray.updateBuffer(vertexData, spriteOffset, TOTAL_COMPONENT_COUNT)
    }

//    fun updatePositionSun(type: Int) {
//        var currentOffset = 0
//        //set position
//        vertexData[currentOffset++] = point.x
//        vertexData[currentOffset++] = point.y
//        vertexData[currentOffset++] = point.z
//
//        vertexData[currentOffset++] = Color.red(color) / 255f
//        vertexData[currentOffset++] = Color.green(color) / 255f
//        vertexData[currentOffset++] = Color.blue(color) / 255f
//
//        vertexData[currentOffset++] = type.toFloat()
//
//        vertexArray.updateBuffer(vertexData, 0, TOTAL_COMPONENT_COUNT)
//    }
//
//    fun getColor(timeSunRiseSet: Long): Int {
//        var time = 0.0
//        if (timeSunRiseSet >= Constants.timeDay / 2) {
//            time = Constants.timeDay.toDouble() - timeSunRiseSet
//        } else {
//            time = timeSunRiseSet.toDouble()
//        }
//        return Color.rgb(
//            255, (time * 255 / Constants.timeDay).toInt() + 110
//            , 0
//        )
//    }

    companion object {
        private val POSITION_COMPONENT_COUNT = 3
        private val COLOR_COMPONENT_COUNT = 3
        private val TIME_COMPONENT_COUNT = 1
        private val REVERSE_COMPONENT_COUNT = 1

        private val TOTAL_COMPONENT_COUNT = (
                POSITION_COMPONENT_COUNT +
                        COLOR_COMPONENT_COUNT +
                        REVERSE_COMPONENT_COUNT +
                        TIME_COMPONENT_COUNT)

        private val STRIDE = TOTAL_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT
    }
}