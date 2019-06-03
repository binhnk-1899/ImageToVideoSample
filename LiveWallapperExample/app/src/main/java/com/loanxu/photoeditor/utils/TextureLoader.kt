package com.loanxu.photoeditor.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.GLUtils.texImage2D

object TextureLoader {

    fun loadCubeMap(context: Context, cubeResources: IntArray, cubeResourcesBlend: IntArray): IntArray {
        val textureObjectIds = IntArray(2)
        glGenTextures(2, textureObjectIds, 0)

        if (textureObjectIds[0] == 0) {
            return textureObjectIds
        }
        val options = BitmapFactory.Options()
        options.inScaled = false
        val cubeBitmaps = arrayOfNulls<Bitmap>(6)

        for (i in 0..5) {
            cubeBitmaps[i] = BitmapFactory.decodeResource(
                context.resources,
                cubeResources[i], options
            )

            if (cubeBitmaps[i] == null) {
                glDeleteTextures(1, textureObjectIds, 0)
                return textureObjectIds
            }
        }

        glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIds[0])

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        //set image for cube right -> left, down -> up, front -> back
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0)
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0)

        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0)
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0)

        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0)
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0)

        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0])

        // load blend
        val cubeBitmapsBlend = arrayOfNulls<Bitmap>(6)

        for (i in 0..5) {
            cubeBitmapsBlend[i] = BitmapFactory.decodeResource(
                context.resources,
                cubeResourcesBlend[i], options
            )

            if (cubeBitmaps[i] == null) {
                glDeleteTextures(1, textureObjectIds, 0)
                return textureObjectIds
            }
        }
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIds[1])

        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        //set image for cube right -> left, down -> up, front -> back
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmapsBlend[0], 0)
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmapsBlend[1], 0)

        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmapsBlend[2], 0)
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmapsBlend[3], 0)

        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmapsBlend[4], 0)
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmapsBlend[5], 0)

        glBindTexture(GL_TEXTURE_2D, textureObjectIds[1])

        for (bitmap in cubeBitmaps) {
            bitmap?.recycle()
        }
        for (bitmap in cubeBitmapsBlend) {
            bitmap?.recycle()
        }
        return textureObjectIds
    }

    fun loadTexture(context: Context, resourceId: Int): Int {
        val textureObjectIds = IntArray(1)
        glGenTextures(1, textureObjectIds, 0)

        if (textureObjectIds[0] == 0) {

            return 0
        }
        val options = BitmapFactory.Options()
        options.inScaled = false

        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)

        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0])
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()
        glGenerateMipmap(GL_TEXTURE_2D)
        glBindTexture(GL_TEXTURE_2D, 0)

        return textureObjectIds[0]
    }

    fun loadTextures(context: Context, resourceId: IntArray): IntArray {
        val textureObjectIds = IntArray(resourceId.size)
        glGenTextures(resourceId.size, textureObjectIds, 0)

        if (textureObjectIds[0] == 0) {

            return textureObjectIds
        }
        val options = BitmapFactory.Options()
        options.inScaled = false
        for (i in 0 until resourceId.size) {
            val bitmap = BitmapFactory.decodeResource(context.resources, resourceId[i], options)
            glBindTexture(GL_TEXTURE_2D, textureObjectIds[i])
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
            glGenerateMipmap(GL_TEXTURE_2D)
            glBindTexture(GL_TEXTURE_2D, textureObjectIds[i])
        }
        return textureObjectIds
    }
}