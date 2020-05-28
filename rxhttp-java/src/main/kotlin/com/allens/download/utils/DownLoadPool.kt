package com.allens.download.utils

import com.allens.impl.OnDownLoadListener
import io.reactivex.disposables.Disposable
import java.util.concurrent.ConcurrentHashMap


object DownLoadPool {

    //任务
    private val hashMap: ConcurrentHashMap<String, Disposable> = ConcurrentHashMap()
    //监听
    private val listenerHashMap: ConcurrentHashMap<String, OnDownLoadListener> = ConcurrentHashMap()
    //下载位置
    private val pathMap: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    //任务
    fun add(key: String, disposable: Disposable) {
        hashMap[key] = disposable
    }

    //监听
    fun add(key: String, loadListener: OnDownLoadListener) {
        listenerHashMap[key] = loadListener
    }

    //下载位置
    fun add(key: String, path: String) {
        pathMap[key] = path
    }


    fun remove(key: String) {
        val disposable = hashMap[key]
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
        hashMap.remove(key)
        listenerHashMap.remove(key)
        pathMap.remove(key)
        ShareDownLoadUtil.remove(key)
    }


    fun pause(key: String) {
        val disposable = hashMap[key]
        if (disposable?.isDisposed == false) {
            disposable.dispose()
        }
    }

    fun getDisposableFromKey(key: String): Disposable? {
        return hashMap[key]
    }

    fun getListenerFromKey(key: String): OnDownLoadListener? {
        return listenerHashMap[key]
    }

    fun getPathFromKey(key: String): String? {
        return pathMap[key]
    }

    fun getListenerMap(): ConcurrentHashMap<String, OnDownLoadListener> {
        return listenerHashMap
    }
}