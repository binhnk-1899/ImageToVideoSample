package com.binhnk.sample.imagetovideo.ui.demo

import android.os.Bundle
import android.view.View
import com.binhnk.sample.imagetovideo.R
import com.binhnk.sample.imagetovideo.base.BaseFragment
import com.binhnk.sample.imagetovideo.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.sharedViewModel

class DemoRedererFragment: BaseFragment<com.binhnk.sample.imagetovideo.databinding.FragmentRendererBinding, MainViewModel>() {
    override val viewModel: MainViewModel by sharedViewModel()
    override val layoutId: Int
        get() = R.layout.fragment_renderer

    companion object {
        fun getInstance() = DemoRedererFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}