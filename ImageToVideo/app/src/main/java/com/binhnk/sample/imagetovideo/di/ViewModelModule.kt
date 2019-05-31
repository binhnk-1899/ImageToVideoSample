package com.binhnk.sample.imagetovideo.di

import com.binhnk.sample.imagetovideo.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}