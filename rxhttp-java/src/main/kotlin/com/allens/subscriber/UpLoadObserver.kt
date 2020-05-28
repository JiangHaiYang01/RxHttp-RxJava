package com.allens.subscriber

import android.os.Handler
import com.allens.impl.OnUpLoadListener
import com.allens.manager.HttpManager
import com.allens.upload.UpLoadPool
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody

class UpLoadObserver<T>(
    private val tag: String,
    private val handler: Handler?,
    private val tClass: Class<T>,
    private val listener: OnUpLoadListener<T>
) :
    BaseObserver<ResponseBody>() {

    override fun onNext(value: ResponseBody) {
        super.onNext(value)
        val json: String = value.string()
        try {
            val t = HttpManager.gson.fromJson(json, tClass)
            handler?.post {
                listener.onUpLoadSuccess(tag, t)
            }

        } catch (throwable: JsonSyntaxException) {
            handler?.post {
                listener.onUpLoadFailed(tag, Throwable(throwable.message))
            }

        } finally {
            UpLoadPool.remove(tag)
        }

    }

    override fun onError(e: Throwable) {
        super.onError(e)
        handler?.post {
            listener.onUpLoadFailed(tag, e)
        }
    }
}