package com.binhnk.sample.imagetovideo.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.binhnk.sample.imagetovideo.BR
import com.binhnk.sample.imagetovideo.base.BaseViewModel

abstract class BaseActivity<ViewBinding : ViewDataBinding, ViewModel : BaseViewModel> :
        AppCompatActivity() {

    lateinit var viewBinding: ViewBinding

    abstract val viewModel: ViewModel

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, layoutId)
        viewBinding.setVariable(BR.viewModel, viewModel)
    }

}