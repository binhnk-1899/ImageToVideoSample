package com.binhnk.sample.openglessample

import android.app.ActivityManager
import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager

class MainActivity : AppCompatActivity() {

    private val mContext: Context by lazy { this@MainActivity }

    private var glSurfaceView: GLSurfaceView? = null
    private var rendererSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        glSurfaceView = MyGLSurfaceView(mContext)

        glSurfaceView?.let {
            setContentView(it)
        }
    }

    override fun onPause() {
        super.onPause()
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        glSurfaceView!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        glSurfaceView!!.onResume()
    }
}
