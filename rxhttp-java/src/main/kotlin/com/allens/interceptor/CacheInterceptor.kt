package com.allens.interceptor

import android.content.Context
import android.net.ConnectivityManager
import com.allens.config.HttpCacheType
import com.allens.config.HttpConfig
import com.allens.tools.RxHttpLogTool
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit


internal class CacheInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val resp: Response
        val req: Request = if (isNetworkAvailable(context)) {
            chain.request()
                .newBuilder()
                .build()
        } else {
            //无网络,检查*天内的缓存,即使是过期的缓存
            val time = when (HttpConfig.cacheNoNewWorkType) {
                HttpCacheType.NO_TIMEOUT -> {
                    Integer.MAX_VALUE
                }
                HttpCacheType.HAS_TIMEOUT -> {
                    HttpConfig.cacheNoNetworkTimeOut
                }
                else -> {
                    0
                }
            }
            RxHttpLogTool.i(
                "http---->  无网络 离线缓存 %s",
                if (time != HttpConfig.cacheNoNetworkTimeOut) {
                    "无限时请求有网请求好的数据"
                } else {
                    "$time 秒请求有网请求好的数据"
                }
            )
            chain.request().newBuilder()
                .cacheControl(
                    CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(time, TimeUnit.SECONDS)
                        .build()
                )
                .build()
        }
        resp = chain.proceed(req)
        return resp.newBuilder().build()
    }
}


private fun isNetworkAvailable(context: Context): Boolean {
    val manger: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = manger.activeNetworkInfo
    return info != null && info.isAvailable
}