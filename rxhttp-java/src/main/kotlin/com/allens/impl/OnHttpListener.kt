package com.allens.impl

/**
 *
 * @Description:
 * @Author:         Allens
 * @CreateDate:     2019-11-22 11:49
 * @Version:        1.0
 */


//interface OnHttpListenerImpl<T> {
//    //请求头
//    fun onHeard(map: HashMap<String, String>)
//
//    //请求体
//    fun onMap(map: HashMap<String, Any>)
//
//
//}


abstract class OnHttpListener<T> {
//    override fun onHeard(map: HashMap<String, String>) {
//    }
//
//    override fun onMap(map: HashMap<String, Any>) {
//    }

    //成功
    abstract fun onSuccess(t: T)

    //失败
    abstract fun onError(e: Throwable)

}


interface OnBaseHttpListener<T> {
    //成功
    fun onSuccess(t: T)

    //失败
    fun onError(e: Throwable)
}