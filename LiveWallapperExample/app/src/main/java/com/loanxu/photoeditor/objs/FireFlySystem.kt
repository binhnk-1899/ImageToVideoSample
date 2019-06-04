package com.loanxu.photoeditor.objs

import android.graphics.Color
import android.opengl.GLES20
import android.util.Log
import com.loanxu.photoeditor.datas.VertexArray
import com.loanxu.photoeditor.programs.FireFlyProgram
import com.loanxu.photoeditor.programs.ShaderProgram
import com.loanxu.photoeditor.utils.Constants
import com.loanxu.photoeditor.utils.Geometry
import com.loanxu.photoeditor.utils.Utils
import kotlin.random.Random

class FireFlySystem(maxFirefly: Int) : GLObject() {
    private var maxFireFly = 10
    private var currentFireFly = 0
    private var nextFireFly = 0
    private var vertexData: FloatArray
    private var vertexArray: VertexArray
    private var vertexDataSize: FloatArray
    private var vertexDataTmp: BooleanArray

    init {
        this.maxFireFly = maxFirefly
        vertexData = FloatArray(maxFireFly * TOTAL_COMPONENT_COUNT)
        vertexArray = VertexArray(vertexData)
        vertexDataSize = FloatArray(maxFirefly)
        vertexDataTmp = BooleanArray(maxFirefly)
    }

    override fun draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, currentFireFly)
    }

    override fun bindData(program: ShaderProgram) {
        val p = program as FireFlyProgram
        var dataOffset = 0
        //set position
        vertexArray.setVertexAttribPointer(
            dataOffset,
            p.aPositionHandle,
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += POSITION_COMPONENT_COUNT

        //set color
        vertexArray.setVertexAttribPointer(
            dataOffset,
            p.aColorHandle,
            COLOR_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += COLOR_COMPONENT_COUNT

//        //set direction
        vertexArray.setVertexAttribPointer(
            dataOffset,
            p.aDirectionHandle,
            DIRECTION_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += DIRECTION_COMPONENT_COUNT

        //set time
        vertexArray.setVertexAttribPointer(
            dataOffset,
            p.aTimeHandle,
            TIME_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += TIME_COMPONENT_COUNT

        //set phi
        vertexArray.setVertexAttribPointer(
            dataOffset,
            p.aPhiHandle,
            PHI_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += PHI_COMPONENT_COUNT

        //set type curve
        vertexArray.setVertexAttribPointer(
            dataOffset,
            p.aSpeedHandle,
            SPEED_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += SPEED_COMPONENT_COUNT

        //set type curve
        vertexArray.setVertexAttribPointer(
            dataOffset,
            p.aTypeHandle,
            ADD_SUB_COMPONENT_COUNT,
            STRIDE
        )
        dataOffset += ADD_SUB_COMPONENT_COUNT

        //set size
        vertexArray.setVertexAttribPointer(
            dataOffset,
            p.aSizeHandle,
            SIZE_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun addFireFlies(
        color: Int,
        directionVector: Geometry.Vector,
        size: Float,
        currentTime: Float
    ) {
        val point = Utils.createPoint()

        val fireFlyOffset = nextFireFly * TOTAL_COMPONENT_COUNT
        var currentOffset = fireFlyOffset
        vertexDataSize[nextFireFly] = size
        vertexDataTmp[nextFireFly] = Random.nextBoolean()
        nextFireFly++
        if (currentFireFly < maxFireFly) {
            currentFireFly++
        }
        if (nextFireFly == maxFireFly) {
            nextFireFly = 0
        }
        // position
        vertexData[currentOffset++] = point.x
        vertexData[currentOffset++] = point.y
        vertexData[currentOffset++] = point.z
        // color
        vertexData[currentOffset++] = Color.red(color) / 255f
        vertexData[currentOffset++] = Color.green(color) / 255f
        vertexData[currentOffset++] = Color.blue(color) / 255f
        // direction
        vertexData[currentOffset++] = directionVector.x
        vertexData[currentOffset++] = directionVector.y
        vertexData[currentOffset++] = directionVector.z
        //a_time
        vertexData[currentOffset++] = currentTime
        Log.i("TIME_","${vertexData[currentOffset-1]}")
        //a_phi
        vertexData[currentOffset++] = Random.nextInt(360).toFloat()

        //a_speedFirefly
        vertexData[currentOffset++] = Random.nextFloat()/3+0.1f
        //a_Type
        vertexData[currentOffset++] = Random.nextInt(0,2).toFloat()
        //a_size
        vertexData[currentOffset++] = Random.nextInt(1, 23).toFloat()

        vertexArray.updateBuffer(vertexData, fireFlyOffset, TOTAL_COMPONENT_COUNT)

    }

    companion object {
        private const val POSITION_COMPONENT_COUNT = 3
        private const val COLOR_COMPONENT_COUNT = 3
        private const val DIRECTION_COMPONENT_COUNT = 3
        private const val TIME_COMPONENT_COUNT = 1
        private const val PHI_COMPONENT_COUNT = 1
        private const val ADD_SUB_COMPONENT_COUNT = 1
        private const val SPEED_COMPONENT_COUNT = 1
        private const val SIZE_COMPONENT_COUNT = 1

        private const val TOTAL_COMPONENT_COUNT = (
                POSITION_COMPONENT_COUNT
                        + COLOR_COMPONENT_COUNT
                        + DIRECTION_COMPONENT_COUNT
                        + TIME_COMPONENT_COUNT
                        + PHI_COMPONENT_COUNT
                        + ADD_SUB_COMPONENT_COUNT
                        + SPEED_COMPONENT_COUNT
                        + SIZE_COMPONENT_COUNT)

        private val STRIDE = TOTAL_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT
    }

}