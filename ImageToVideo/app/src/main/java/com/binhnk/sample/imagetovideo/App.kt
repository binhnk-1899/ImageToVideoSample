package com.binhnk.sample.imagetovideo

import android.app.Application
import com.binhnk.sample.imagetovideo.di.appModule
import com.binhnk.sample.imagetovideo.di.repositoryModule
import com.binhnk.sample.imagetovideo.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule, repositoryModule, viewModelModule)
        }
    }
}