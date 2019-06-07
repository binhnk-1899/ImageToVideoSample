package com.loanxu.photoeditor

import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.loanxu.photoeditor.objs.FireFlySystem
import com.loanxu.photoeditor.objs.FireflyObj
import com.loanxu.photoeditor.objs.ImageGL
import com.loanxu.photoeditor.programs.FireFlyProgram
import com.loanxu.photoeditor.programs.ImageProgram
import com.loanxu.photoeditor.renders.GLRender
import com.loanxu.photoeditor.utils.Geometry
import com.loanxu.photoeditor.utils.TextureLoader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SampleActivity : AppCompatActivity() {
    private var glSurfaceView: BgSurfaceView? = null
    private var rendererSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo =
            activityManager.deviceConfigurationInfo
        val supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
                || (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                        && (Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86"))
                )

        Log.e("Ahihi", "GLES support: $supportsEs2")

        if (supportsEs2) {
            glSurfaceView = BgSurfaceView(this@SampleActivity, BackgroundRenderer(this@SampleActivity))
            glSurfaceView?.let {

                rendererSet = true
                setContentView(it)
            }
        } else {
            setContentView(R.layout.activity_sample)
        }
    }

    override fun onPause() {
        super.onPause()
        if (rendererSet) {
            glSurfaceView?.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (rendererSet) {
            glSurfaceView?.onResume()
        }
    }
}

class BackgroundRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private var imageGL: ImageGL? = null
    private var imageProgram: ImageProgram? = null
    private var textureIdImage: Int = 0
    private var coordX = 0f

    // firefly object
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
    private var scale = 2 * max / 100f

    companion object {
        private const val eyeZ = -1f
        private const val fovy = 90f
        private val projectionMatrix = FloatArray(16)
        private val viewMatrix = FloatArray(16)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        loadBackground()
        loadFireFly()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        val aspect: Float = width.toFloat() / height.toFloat()
        Matrix.perspectiveM(projectionMatrix, 0, fovy, aspect, 1f, 100f)
    }

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
    }

    private fun loadBackground() {
        imageGL = ImageGL()
        imageProgram = ImageProgram(context, R.raw.vertex_image, R.raw.fragment_image)
        textureIdImage = TextureLoader.loadTexture(context, R.drawable.background4)
    }

    private fun drawBackground() {
        val modelMatrix = FloatArray(16)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(
            modelMatrix,
            0,
            coordX,
            0f,
            0f
        )

        val matrix = FloatArray(16)
        Matrix.multiplyMM(
            matrix,
            0,
            viewMatrix,
            0,
            modelMatrix,
            0
        )
        Matrix.multiplyMM(
            matrix,
            0,
            projectionMatrix,
            0,
            matrix,
            0
        )
        imageProgram?.let {
            it.useProgram()
            it.setUniforms(matrix, textureIdImage)
            imageGL?.let { imageGL ->
                run {
                    imageGL.bindData(it)
                    imageGL.draw()
                }
            }
        }

    }

    private fun loadFireFly() {
        fireflyObj = FireflyObj(
            Geometry.Vector(1f, 1f, 1.0f),
            Color.rgb(225, 255, 255),
            1f,
            3f,
            20f
        )
        fireFlyProgram = FireFlyProgram(context, R.raw.snow_vertex_shader, R.raw.firefly_fragment_shader)
        fireFlySystem = FireFlySystem(max)
        textureIdFireFly = TextureLoader.loadTexture(context, R.drawable.ic_snow)

    }

    private fun drawFireFlys() {
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

    fun handleTouchDrag(deltaX: Float, deltaY: Float) {
        coordX -= deltaX * (GLRender.MAXCOORX / 1000f)
        if (coordX < -GLRender.COORX) {
            coordX = -GLRender.COORX
        }
        if (coordX > GLRender.COORX) {
            coordX = GLRender.COORX
        }
    }

}

class FireFlyRenderer(private val context: Context) : GLSurfaceView.Renderer {
    override fun onDrawFrame(gl: GL10?) {

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

    }

}
