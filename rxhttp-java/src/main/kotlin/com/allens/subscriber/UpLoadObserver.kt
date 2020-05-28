package com.allens.subscriber

import android.os.Handler
import com.allens.RxHttp
import com.allens.impl.OnUpLoadListener
import com.allens.manager.HttpManager
import com.allens.tools.RxHttpLogTool
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

    private var success = false

    override fun onNext(value: ResponseBody) {
        super.onNext(value)
        RxHttpLogTool.i(RxHttp.TAG, "upload onNext")
        val json: String = value.string()
        try {
            val t = HttpManager.gson.fromJson(json, tClass)
            handler?.post {
                listener.onUpLoadSuccess(tag, t)
            }
            success = true

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
        RxHttpLogTool.i(RxHttp.TAG, "upload error")
        handler?.post {
            listener.onUpLoadFailed(tag, e)
        }
    }

    override fun onComplete() {
        super.onComplete()
        RxHttpLogTool.i(RxHttp.TAG, "upload complete")
        if (!success) {
            handler?.post {
                listener.onUpLoadFailed(tag, Throwable("cancel upload"))
                UpLoadPool.remove(tag)
            }
        }
    }
}