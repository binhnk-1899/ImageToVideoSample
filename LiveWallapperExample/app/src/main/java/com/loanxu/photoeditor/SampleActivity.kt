package com.loanxu.photoeditor

import android.app.ActivityManager
import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import com.loanxu.photoeditor.objs.ImageGL
import com.loanxu.photoeditor.programs.ImageProgram
import com.loanxu.photoeditor.renders.GLRender
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

    companion object {
        private const val eyeZ = -1f
        private const val fovy = 90f
        private val projectionMatrix = FloatArray(16)
        private val viewMatrix = FloatArray(16)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        loadBackground()
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
