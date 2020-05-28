package com.allens.impl

interface  OnCookieInterceptor {
    //是否拦截所有方法的cookie
    fun isInterceptorAllRequest(): Boolean

    //拦截哪一个方法
    fun isInterceptorRequest(url: String): Boolean
}