package com.binhnk.sample.imagetovideo.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDialog<ViewBinding : ViewDataBinding>(context: Context)
    : Dialog(context) {

    lateinit var viewBinding: ViewBinding

    @get:LayoutRes
    abstract val layoutId: Int

    abstract fun initSystem()
    abstract fun afterCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSystem()
        viewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                layoutId, null, false
        )
        setContentView(viewBinding.root)

        afterCreate()
    }
}