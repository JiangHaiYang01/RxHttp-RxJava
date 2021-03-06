package com.allens.config

import androidx.lifecycle.Lifecycle
import com.allens.impl.OnBuildClientListener
import com.allens.impl.OnCookieInterceptor
import com.allens.interceptor.OnCookieListener
import com.allens.model_http.impl.OnLogFilterListener
import com.allens.model_http.impl.OnLogListener
import okhttp3.logging.HttpLoggingInterceptor

/**
 *
 * @Description:
 * @Author:         Allens
 * @CreateDate:     2019-11-22 12:01
 * @Version:        1.0
 */

open class HttpConfig {
    companion object {
        var connectTime: Long = 10
        var readTime: Long = 10
        var writeTime: Long = 10
        var logListener: OnLogListener? = null
        var retryOnConnectionFailure: Boolean = false
        var isLog: Boolean = true
        var level: HttpLevel = HttpLevel.BODY
        var onBuildClientListener: OnBuildClientListener? = null
        var baseUrl: String = ""
        var logFilterListener: OnLogFilterListener? = null
        var cookieListener: OnCookieListener? = null
        var onCookieInterceptor: OnCookieInterceptor? = null
        var heardMap: Map<String, String>? = null


        //有网时候的缓存策略 默认无限时请求有网请求好的数据
        var cacheNetWorkType = HttpNetWorkType.NOCACHE

        //无网时候的缓存策略 默认加载30天的
        var cacheNoNewWorkType = HttpCacheType.HAS_TIMEOUT

        //有网时:特定时间之后请求数据；（比如：特定时间为20s） 默认20
        var cacheNetworkTimeOut = 20

        //无网时:特定时间之前请求有网请求好的数据；（（比如：特定时间为30天） 默认30 天  单位（秒）
        var cacheNoNetworkTimeOut = 30 * 24 * 60 * 60

        //缓存大小  10M
        var cacheSize = 10 * 1024 * 1024

        //缓存位置
        var cachePath = ""
    }
}

enum class HttpLevel {
    NONE,
    BASIC,
    HEADERS,
    BODY;

    companion object {
        fun conversion(level: HttpLevel): HttpLoggingInterceptor.Level {
            return when (level) {
                BODY -> HttpLoggingInterceptor.Level.BODY
                NONE -> HttpLoggingInterceptor.Level.NONE
                BASIC -> HttpLoggingInterceptor.Level.BASIC
                HEADERS -> HttpLoggingInterceptor.Level.HEADERS
                else -> HttpLoggingInterceptor.Level.BODY
            }
        }
    }
}


enum class CacheType {
    //不加入缓存的逻辑
    NONE,

    //有网时:每次都请求实时数据； 无网时:无限时请求有网请求好的数据；
    HAS_NETWORK_NOCACHE_AND_NO_NETWORK_NO_TIME,

    //有网时:特定时间之后请求数据； 无网时:无限时请求有网请求好的数据；
    HAS_NETWORK_CACHE_TIME_AND_NO_NETWORK_NO_TIME,

    //有网时:每次都请求实时数据； 无网时:特定时间之前请求有网请求好的数据；
    HAS_NETWORK_NOCACHE_AND_NO_NETWORK_HAS_TIME,

    //有网时:特定时间之后请求数据； 无网时:特定时间之前请求有网请求好的数据；
    HAS_NETWORK_CACHE_TIME_AND_NO_NETWORK_HAS_TIME,
}

enum class HttpNetWorkType {
    //不加入缓存的逻辑
    NONE,

    //有网络时候 实时加载
    NOCACHE,

    //特定时间之后请求数据；（比如：特定时间为20s）
    CACHE_TIME,
}


enum class HttpCacheType {
    //不加入缓存的逻辑
    NONE,

    //无限时请求有网请求好的数据；
    NO_TIMEOUT,

    //特定时间之前请求有网请求好的数据；（（比如：特定时间为20s））
    HAS_TIMEOUT,
}


enum class LifecycleCancel {
    ON_CREATE,
    ON_START,
    ON_RESUME,
    ON_PAUSE,
    ON_STOP,
    ON_DESTROY

}