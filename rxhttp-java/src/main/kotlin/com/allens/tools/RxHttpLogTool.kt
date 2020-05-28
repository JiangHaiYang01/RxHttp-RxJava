package com.allens.tools

import android.util.Log
import com.allens.config.HttpConfig

object RxHttpLogTool {

    fun i(tag: String, info: String) {
        if (HttpConfig.isLog) {
            Log.i(tag, info)
            HttpConfig.logListener?.onRxHttpLog(info)
        }
    }
}