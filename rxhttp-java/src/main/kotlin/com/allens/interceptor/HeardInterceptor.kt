package com.allens.interceptor

import com.allens.RxHttp
import com.allens.tools.RxHttpLogTool
import okhttp3.Interceptor
import okhttp3.Request

//请求头
object HeardInterceptor {
    fun register(map: Map<String, String>): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            val builder: Request.Builder = request.newBuilder()
            for ((key, value) in map.entries) {
                RxHttpLogTool.i(RxHttp.TAG, "http----> add heard [key]:$key [value]:$value ")
                builder.addHeader(key, value)
            }
            chain.proceed(builder.build())
        }
    }
}

