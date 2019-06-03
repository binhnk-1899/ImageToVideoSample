package com.loanxu.photoeditor.utils

import android.text.format.DateFormat
import com.loanxu.photoeditor.renders.GLRender
import java.util.*
import kotlin.random.Random

object Utils {
    fun getTimeCurrent(): Long {
        var isDay = true
        val cal = Calendar.getInstance()
        val date = DateFormat.format("hh:mm:ss:a", cal).toString()
        val times = date.split(":")
        var hh = times[0].toInt()
        val mm = times[1].toInt()
        val ss = times[2].toInt()
        isDay = if (hh == 12) return (hh * 3600 + mm * 60 + ss).toLong()
        else times[3].equals("AM")
        if (!isDay) {
            hh += 12
        }
        return (hh * 3600 + mm * 60 + ss).toLong()
    }

    fun getTimeSunSetRise(): Long {
        var isDay = true
        val cal = Calendar.getInstance()
        val date = DateFormat.format("hh:mm:ss:a", cal).toString()
        val times = date.split(":")
        isDay = times[3].equals("AM")
        var hh: Int
        val mm: Int
        val ss: Int
        hh = times[0].toInt()
        if (!isDay) {
            hh += 12
        }
        mm = times[1].toInt()
        ss = times[2].toInt()
        return (hh * 3600 + mm * 60 + ss).toLong() - 6 * 3600
    }

    fun checkSunRiseSet(): Boolean {
        val cal = Calendar.getInstance()
        val date = DateFormat.format("hh:a", cal).toString()
        val time = date.split(":")
        return if (time[1].equals("AM")) {
            time[0].toInt() >= 6
        } else {
            time[0].toInt() <= 6
        }
    }

    fun createPoint(): Geometry.Point {
        var rx = (Random.nextFloat()) * 2f
        rx = if (rx < 1f) {
            -1f * rx * GLRender.MAXCOORX
        } else {
            (rx - 1f) * GLRender.MAXCOORX

        }
        var ry = (Random.nextFloat()) * 2f
        ry = if (ry < 1.5f) {
            -1f * ry / 1.5f * GLRender.MAXCOORY
        } else {
            (ry - 1.5f) / 0.5f * GLRender.MAXCOORY

        }
        return Geometry.Point(rx, ry, 0f)
    }

    fun creatPointSprite(isLeft: Boolean): Geometry.Point {
        var rx: Float
        if (isLeft) {
            rx = GLRender.MAXCOORX
        } else {
            rx = -1 * GLRender.MAXCOORY
        }
        var ry = (Random.nextFloat())
        ry = -1 * ry * (2 * GLRender.MAXCOORY / 3)
        return Geometry.Point(rx, ry, 0f)
    }
}