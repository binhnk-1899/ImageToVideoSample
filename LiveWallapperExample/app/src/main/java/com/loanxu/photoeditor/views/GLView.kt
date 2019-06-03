package com.loanxu.photoeditor.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.loanxu.photoeditor.renders.GLRender

class GLView (context: Context): GLSurfaceView(context) {
    private var renderer: GLRender
    private var previousX: Float = 0.toFloat()
    private var previousY: Float = 0.toFloat()
    init {
        setEGLContextClientVersion(2)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        holder.setFormat(PixelFormat.RGBA_8888)
        setZOrderOnTop(false)
        renderer = GLRender(context)
        setRenderer(renderer)
//        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
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
                renderer.handleTouchDrag(deltaX, deltaY)
            }
            true
        } else {
            false
        }
    }
}