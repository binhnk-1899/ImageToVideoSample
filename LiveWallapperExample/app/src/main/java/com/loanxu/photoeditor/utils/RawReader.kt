package com.loanxu.photoeditor.utils

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object RawReader {
    fun readTFfromRes(context: Context, resId: Int): String {
        val body = StringBuilder()
        val inputStream = context.resources.openRawResource(resId)
        val isReader = InputStreamReader(inputStream)
        val bReader = BufferedReader(isReader)
        var nextLine:String?=bReader.readLine()
        try {
            while (nextLine != null) {
                body.append(nextLine)
                body.append('\n')
                nextLine=bReader.readLine()
            }
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return body.toString()
    }
}