package com.loanxu.photoeditor.renders

import android.content.Context
import android.graphics.Color
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.loanxu.photoeditor.R
import com.loanxu.photoeditor.objs.ImageGL
import com.loanxu.photoeditor.objs.Sprite
import com.loanxu.photoeditor.programs.FireFlyProgram
import com.loanxu.photoeditor.programs.ImageProgram
import com.loanxu.photoeditor.programs.SpriteProgram
import com.loanxu.photoeditor.utils.Geometry
import com.loanxu.photoeditor.utils.TextureLoader
import com.loanxu.photoeditor.utils.Utils
import com.loanxu.photoeditor.objs.FireFlySystem
import com.loanxu.photoeditor.objs.FireflyObj
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.random.Random

class GLRender(private val context: Context) : GLSurfaceView.Renderer {
    private var eyeZ = -1f
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    private var imageGL: ImageGL? = null
    private var imageProgram: ImageProgram? = null
    private var textureIdImage: Int = 0
    private var coordX = 0f

    private var spriteProgram: SpriteProgram? = null
    private var sprite: Sprite? = null
    private var spriteTexture: IntArray? = null
    var indexSprite = 0


    private var fireFlyProgram: FireFlyProgram? = null
    private var fireflyObj: FireflyObj? = null
    private var fireFlySystem: FireFlySystem? = null
    private var textureIdFireFly: Int = 0
    private var timeStart: Long = 0
    private var timeStartAdd: Long = 0
    private var angle: Float = 0.0f
    private var isIncrease = true
    private var current = 0
    private var max = 100
    private var index = 0
    private var scale = 3 * max / 100f

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        Matrix.setLookAtM(
            viewMatrix, 0,
            0f, 0f, eyeZ,//View visible
            0f, 0f, 0f,
            0f, 1.0f, 0.0f
        )

        drawBackground()
        drawFireFlys()
//        drawSprite()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        val aspect: Float = width.toFloat() / height.toFloat()
        Matrix.perspectiveM(projectionMatrix, 0, fovy, aspect, 1f, 100f)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        loadBackground()
        loadFireFly()
//        loadSprite()
    }

    fun loadBackground() {
        imageGL = ImageGL()
        imageProgram = ImageProgram(context, R.raw.vertex_image, R.raw.fragment_image)
        textureIdImage = TextureLoader.loadTexture(context, R.drawable.background1)
    }

    fun drawBackground() {
        // Move model to given position
        val modelMatrix = FloatArray(16)
        Matrix.setIdentityM(modelMatrix, 0)
        // Scale to some reasonable size and flip upright
        val scaleX = -MAXCOORY * 16f / 9f
        val scaleY = MAXCOORY
        Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, 0f)
        Matrix.translateM(modelMatrix, 0, coordX, 0f, 0f)
        // Calculate final MVP matrix
        val matrix = FloatArray(16)
        Matrix.multiplyMM(matrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(matrix, 0, projectionMatrix, 0, matrix, 0)
        imageProgram?.useProgram()
        imageProgram?.setUniforms(matrix, textureIdImage)
        imageGL?.bindData(imageProgram!!)
        imageGL?.draw()

    }

    fun loadFireFly() {
        fireflyObj = FireflyObj(
            Geometry.Vector(1f, 1f, 1.0f),
            Color.rgb(225, 255, 255),
            1f,
            3f,
            25f
        )
        fireFlyProgram = FireFlyProgram(context, R.raw.firefly_vertex_shader, R.raw.firefly_fragment_shader)
        fireFlySystem = FireFlySystem(max)
        textureIdFireFly = TextureLoader.loadTexture(context, R.drawable.firefly)


    }

    fun drawFireFlys() {
        var time = (System.nanoTime() - timeStart) / 1000000000f

        if (current < max) {
            if (current % 100 == 0) {
                timeStartAdd = System.nanoTime()
            }

            val timeAdd = (System.nanoTime() - timeStartAdd) / 1000000000f
            fireflyObj?.addFirefly(fireFlySystem!!, timeAdd)
            current++
        }
        angle += (Math.PI * 2).toFloat() / 600

        index = (time / scale).toInt()
        time -= index * scale
        isIncrease = index % 2 == 0

        time = if (!isIncrease) {
            (scale - time) / (max / 100f)
        } else {
            time / (max / 100f)
        }
        val modelMatrix = FloatArray(16)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, coordX * -2f, 0f, 0f)
        val matrix = FloatArray(16)
        Matrix.multiplyMM(matrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(matrix, 0, projectionMatrix, 0, matrix, 0)
        // set blend point
        glEnable(GL_BLEND)
        glBlendFunc(GL_ONE, GL_ONE)
        //use program
        fireFlyProgram?.useProgram()
        fireFlyProgram?.setUniforms(
            matrix,
            angle,
            time,
            isIncrease,
            textureIdFireFly
        )
        fireFlySystem?.bindData(fireFlyProgram!!)
        fireFlySystem?.draw()
        glDisable(GL_BLEND)
    }

    private fun loadSprite() {
        spriteProgram = SpriteProgram(context, R.raw.sun_vertex_shader, R.raw.sun_fragment_shader)
        sprite = Sprite(Color.rgb(255, 255, 255), maxSprite / 100)
        spriteTexture = TextureLoader.loadTextures(
            context,
            intArrayOf(
                R.drawable.i1,
                R.drawable.i2,
                R.drawable.i3,
                R.drawable.i4,
                R.drawable.i5,
                R.drawable.i6,
                R.drawable.i7,
                R.drawable.i8,
                R.drawable.i9,
                R.drawable.i10,
                R.drawable.i11,
                R.drawable.i12,
                R.drawable.i13,
                R.drawable.i14,
                R.drawable.i15,
                R.drawable.i16,
                R.drawable.i17,
                R.drawable.i18
            )
        )

    }

    private var revereFly = -1f
    private var indexSprite_ = 0
    private var countSprite = 0
    private var maxSprite = 300
    private var timeStartSprite: Long = 0
    private fun drawSprite() {
        if (countSprite == 0) {
            timeStartSprite = System.nanoTime()
        }
        val time = (System.nanoTime() - timeStartSprite) / 1000000000f
        if (countSprite < maxSprite) {
            if (countSprite % 100 == 0) {
                val isLeft = Random.nextBoolean()
                val left: Float = if (isLeft) {
                    1f
                } else {
                    -1f
                }
                sprite?.addSprite(Utils.creatPointSprite(isLeft), time, left)
            }
            countSprite++
        }

        indexSprite_ = (angle / 10).toInt()
        val angle_ = angle - indexSprite_ * 10f
        // set matrix point
        val modelMatrix = FloatArray(16)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, coordX * -2f, 0f, 0f)
        val matrix = FloatArray(16)
        Matrix.multiplyMM(matrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(matrix, 0, projectionMatrix, 0, matrix, 0)
        // set blend point
        glEnable(GL_BLEND)
        glBlendFunc(GL_ONE, GL_ONE)
        //use program
        spriteProgram?.useProgram()
        spriteProgram?.setUniforms(
            matrix,
            angle_ * 0.25f,
            spriteTexture!![indexSprite / 4],
            revereFly
        )
        sprite?.bindData(spriteProgram!!)
        sprite?.draw()
        glDisable(GL_BLEND)
        indexSprite++
        if (indexSprite > MAX_SPRITE) {
            indexSprite = 0
        }

    }


    fun handleTouchDrag(deltaX: Float, deltaY: Float) {
        coordX += deltaX * (MAXCOORX / 1000f)
        if (coordX < -COORX) {
            coordX = -COORX
        }
        if (coordX > COORX) {
            coordX = COORX
        }
    }

    companion object {
        const val MAX_SPRITE = 13 * 4
        var fovy = 120f
        val MAXCOORY = Math.tan((fovy / 2) * Math.PI / 180).toFloat()
        val MAXCOORX = MAXCOORY * 9 / 16f
        val COORX = 175 / 256f * MAXCOORX
    }
}