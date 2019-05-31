package com.binhnk.sample.imagetovideo

import android.content.Context
import android.os.Bundle
import com.binhnk.sample.imagetovideo.base.BaseActivity
import com.binhnk.sample.imagetovideo.ui.demo.DemoRedererFragment
import com.binhnk.sample.imagetovideo.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.viewModel

class MainActivity : BaseActivity<com.binhnk.sample.imagetovideo.databinding.ActivityMainBinding, MainViewModel>() {

    private val mContext: Context by lazy {this@MainActivity}

    override val viewModel: MainViewModel by viewModel()
    override val layoutId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, DemoRedererFragment.getInstance())
            .commitAllowingStateLoss()

    }

}
