package com.allens.upload
import com.allens.impl.UpLoadCancelListener
import com.allens.subscriber.Task
import java.util.concurrent.ConcurrentHashMap

object UpLoadPool {


    private val scopeMap: ConcurrentHashMap<String, Task> = ConcurrentHashMap()
    private val listenerMap: ConcurrentHashMap<String, UpLoadCancelListener> = ConcurrentHashMap()


    fun add(
        key: String,
        listener: UpLoadCancelListener,
        job: Task
    ) {
        scopeMap[key] = job
        listenerMap[key] = listener
    }


    fun remove(key: String) {
        val scope = scopeMap[key]
        scope?.cancel()

        scopeMap.remove(key)
        listenerMap.remove(key)
    }

    fun getListener(tag: String): UpLoadCancelListener? {
        return listenerMap[tag]
    }


}