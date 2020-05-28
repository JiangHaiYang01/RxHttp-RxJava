package com.allens.subscriber

import com.allens.impl.OnHttpListener
import com.allens.manager.HttpManager
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody

class BeanObserver<T>(private val tClass: Class<T>, private val listener: OnHttpListener<T>) :
    BaseObserver<ResponseBody>() {

    override fun onNext(value: ResponseBody) {
        super.onNext(value)
        val json: String = value.string()
        try {
            val t = HttpManager.gson.fromJson(json, tClass)
            listener.onSuccess(t)
        } catch (throwable: JsonSyntaxException) {
            listener.onError(Throwable(throwable.message))
        }

    }

    override fun onError(e: Throwable) {
        super.onError(e)
        listener.onError(e)
    }
}