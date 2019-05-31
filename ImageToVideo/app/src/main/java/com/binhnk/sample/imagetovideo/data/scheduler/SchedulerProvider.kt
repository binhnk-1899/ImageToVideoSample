package com.binhnk.sample.imagetovideo.data.scheduler

import io.reactivex.Scheduler

interface SchedulerProvider {

    val ui: Scheduler

    val computation: Scheduler

    val io: Scheduler

    val newThread: Scheduler

}