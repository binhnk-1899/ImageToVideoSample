package com.loanxu.photoeditor

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.loanxu.photoeditor.views.GLView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

//        setContentView(R.layout.activity_main)
        val glview = GLView(baseContext)
        setContentView(glview)
    }
}
