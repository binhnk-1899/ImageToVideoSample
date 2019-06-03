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
import com.loanxu.photoeditor.objs.ImageGL
import com.loanxu.photoeditor.programs.ImageProgram
import com.loanxu.photoeditor.renders.GLRender
import com.loanxu.photoeditor.utils.TextureLoader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SampleActivity : AppCompatActivity() {
    private var glSurfaceView: GLSurfaceView? = null
    private var rendererSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            glSurfaceView = GLSurfaceView(this@SampleActivity)
            glSurfaceView?.let {
                // request an openGL ES 2.0 compatible context
                it.setEGLContextClientVersion(2)

                // assign our renderer
                it.setEGLConfigChooser(8, 8, 8, 8, 16, 0)
                it.setRenderer(CustomRenderer(this@SampleActivity))

                it.holder.setFormat(PixelFormat.RGBA_8888)
                it.holder.setFormat(PixelFormat.TRANSLUCENT)
                it.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

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

class CustomRenderer(private val context: Context) : GLSurfaceView.Renderer {
    private var imageGL: ImageGL? = null
    private var imageProgram: ImageProgram? = null
    private var textureIdImage: Int = 0
    private var coordX = 0f

    companion object {
        private const val eyeZ = -1f
        const val MAX_SPRITE = 13 * 4
        var fovy = 120f
        private val MAXCOORY = Math.tan((fovy / 2) * Math.PI / 180).toFloat()
        private val MAXCOORX = MAXCOORY * 9 / 16f
        private val COORX = 175 / 256f * MAXCOORX
        private val projectionMatrix = FloatArray(16)
        private val viewMatrix = FloatArray(16)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
//        glClearColor(0.5f, 0f, 0.2f, 1f)

        loadBackground()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
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
        textureIdImage = TextureLoader.loadTexture(context, R.drawable.bg)
    }

    private fun drawBackground() {
        val modelMatrix = FloatArray(16)
        Matrix.setIdentityM(modelMatrix, 0)
        // Scale to some reasonable size and flip upright
        val scaleX = -MAXCOORY * 16f / 9f
        val scaleY = MAXCOORY
        Matrix.scaleM(modelMatrix, 0, scaleX, scaleY, 0f)
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

}
