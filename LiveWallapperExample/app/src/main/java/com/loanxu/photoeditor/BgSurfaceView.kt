package com.loanxu.photoeditor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLSurfaceView
import android.view.MotionEvent

@SuppressLint("ViewConstructor")
class BgSurfaceView(
    context: Context,
    private val mRenderer: BackgroundRenderer
) : GLSurfaceView(context) {

    private var previousX: Float = 0.toFloat()
    private var previousY: Float = 0.toFloat()

    init {
        setEGLContextClientVersion(2)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        holder.setFormat(PixelFormat.RGBA_8888)
        setZOrderOnTop(false)
        setRenderer(mRenderer)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (event != null) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                previousX = event.x
                previousY = event.y
            } else if (event.action == MotionEvent.ACTION_MOVE) {
                val deltaX = event.x - previousX
                val deltaY = event.y - previousY
                previousX = event.x
                previousY = event.y
                mRenderer.handleTouchDrag(deltaX, deltaY)
            }
            true
        } else {
            false
        }
    }
}