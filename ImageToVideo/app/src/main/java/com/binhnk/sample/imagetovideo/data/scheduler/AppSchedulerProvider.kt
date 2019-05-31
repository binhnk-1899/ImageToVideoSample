package com.binhnk.sample.imagetovideo.data.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppSchedulerProvider : SchedulerProvider {

    override val ui: Scheduler
        get() = AndroidSchedulers.mainThread()

    override val computation: Scheduler
        get() = Schedulers.computation()

    override val io: Scheduler
        get() = Schedulers.io()

    override val newThread: Scheduler
        get() = Schedulers.newThread()

}