package com.binhnk.sample.imagetovideo.di

import com.binhnk.sample.imagetovideo.data.scheduler.AppSchedulerProvider
import com.binhnk.sample.imagetovideo.data.scheduler.SchedulerProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single { androidApplication().resources }
    single<SchedulerProvider> { AppSchedulerProvider() }
}