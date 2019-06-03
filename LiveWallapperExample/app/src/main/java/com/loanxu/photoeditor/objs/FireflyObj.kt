package com.loanxu.photoeditor.objs

import android.opengl.Matrix.*
import com.loanxu.photoeditor.utils.Geometry
import kotlin.random.Random

class FireflyObj(
    direction: Geometry.Vector,
    private var color: Int,
    private var speed: Float,
    private var angle: Float,
    private var maxSize: Float
) {

    private var directionVector = FloatArray(4)

    init {
        directionVector[0] = direction.x
        directionVector[1] = direction.y
        directionVector[2] = direction.z
    }

    fun addFirefly(fireFlySystem: FireFlySystem, currentTime: Float) {
        fireFlySystem.addFireFlies(color,
            getDirection(directionVector,angle,speed),
            maxSize, currentTime)
    }
    companion object {

        fun getDirection(direction: FloatArray,angle: Float,speed: Float): Geometry.Vector{
            val rotationMatrix = FloatArray(16)
            val resultVector = FloatArray(4)
            val random = Random
            setRotateEulerM(
                rotationMatrix, 0,
                (random.nextFloat() - 0.5f) * angle,
                (random.nextFloat() - 0.5f) * angle,
                (random.nextFloat() - 0.5f) * angle
            )

			// nhan ma tran xoay
            multiplyMV(
                resultVector, 0,
                rotationMatrix, 0,
                direction, 0
            )

            val speedAdjustment = .1f + random.nextFloat() * speed

            return Geometry.Vector(
                resultVector[0] * speedAdjustment,
                resultVector[1] * speedAdjustment,
                resultVector[2] * speedAdjustment
            )
        }
    }

}