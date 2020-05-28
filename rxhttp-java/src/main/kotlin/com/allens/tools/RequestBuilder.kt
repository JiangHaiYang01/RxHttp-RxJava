package com.allens.tools

import android.annotation.SuppressLint
import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.allens.config.HttpConfig
import com.allens.config.LifecycleCancel
import com.allens.download.DownLoadManager
import com.allens.impl.OnDownLoadListener
import com.allens.impl.OnHttpListener
import com.allens.impl.OnUpLoadListener
import com.allens.subscriber.BeanObserver
import com.allens.subscriber.Task
import com.allens.subscriber.UpLoadObserver
import com.allens.upload.ProgressRequestBody
import com.allens.upload.UpLoadPool
import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle
import io.reactivex.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.File
import java.util.*


class RequestBuilder {

    private val heard = HashMap<String, String>()
    private val map = HashMap<String, Any>()
    private val bodyMap = HashMap<String, ProgressRequestBody>()


    private var handler: Handler? = null
    fun addHeard(key: String, value: String): RequestBuilder {
        heard[key] = value
        return this
    }

    fun addParameter(key: String, value: Any): RequestBuilder {
        map[key] = value
        return this
    }


    fun addFile(key: String, file: File): RequestBuilder {
        handler = Handler()
        val fileBody: RequestBody =
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        bodyMap[key] = ProgressRequestBody(null, "", fileBody, handler)
        return this
    }


    private var owner: LifecycleOwner? = null

    private var event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY

    fun bindEvent(owner: LifecycleOwner, type: LifecycleCancel) {
        when (type) {
            LifecycleCancel.ON_CREATE -> {
                event = Lifecycle.Event.ON_CREATE
            }
            LifecycleCancel.ON_START -> {
                event = Lifecycle.Event.ON_START
            }
            LifecycleCancel.ON_RESUME -> {
                event = Lifecycle.Event.ON_RESUME
            }
            LifecycleCancel.ON_PAUSE -> {
                event = Lifecycle.Event.ON_PAUSE
            }
            LifecycleCancel.ON_STOP -> {
                event = Lifecycle.Event.ON_STOP
            }
            LifecycleCancel.ON_DESTROY -> {
                event = Lifecycle.Event.ON_DESTROY
            }
        }
    }

    fun <T> doGet(
        tClass: Class<T>,
        parameter: String,
        listener: OnHttpListener<T>
    ): Task {
        val observable = ObservableTool.getObservableGet(parameter, heard, map)
        val observer = BeanObserver(tClass, listener)
        bindLifeCycle(observable)
            .subscribe(observer)
        return Task(observer.disposable)
    }

    @SuppressLint("CheckResult")
    private fun bindLifeCycle(observer: Observable<ResponseBody>): Observable<ResponseBody> {
        return if (owner == null) {
            observer
        } else {
            observer.compose(
                AndroidLifecycle.createLifecycleProvider(owner)
                    .bindUntilEvent(event)
            )
        }
    }



    fun <T> doPost(
        parameter: String,
        tClass: Class<T>,
        listener: OnHttpListener<T>
    ): Task {
        val beanObserver = BeanObserver(tClass, listener)
        val observable = ObservableTool.getObservablePost(parameter, heard, map)
        bindLifeCycle(observable)
            .subscribe(beanObserver)
        return Task(beanObserver.disposable)
    }


    fun <T> doBody(
        tClass: Class<T>,
        parameter: String,
        listener: OnHttpListener<T>
    ): Task {
        val observable = ObservableTool.getObservableBody(parameter, heard, map)
        val beanObserver = BeanObserver(tClass, listener)
        bindLifeCycle(observable)
            .subscribe(beanObserver)
        return Task(beanObserver.disposable)
    }

    fun <T> doPut(
        tClass: Class<T>,
        parameter: String,
        listener: OnHttpListener<T>
    ): Task {
        val observable = ObservableTool.getObservablePut(parameter, heard, map)
        val beanObserver = BeanObserver(tClass, listener)
        bindLifeCycle(observable)
            .subscribe(beanObserver)
        return Task(beanObserver.disposable)
    }

    fun <T> doDelete(
        tClass: Class<T>,
        parameter: String,
        listener: OnHttpListener<T>
    ): Task {
        val observable = ObservableTool.getObservableDelete(parameter, heard, map)
        val beanObserver = BeanObserver(tClass, listener)
        bindLifeCycle(observable).subscribe(beanObserver)
        return Task(beanObserver.disposable)
    }


    //获取缓存大小
    fun getCacheSize(): Long {
        val file = File(HttpConfig.cachePath)
        return file.length()
    }


    //下载
    fun doDownLoad(
        key: String,
        url: String,
        savePath: String,
        saveName: String,
        loadListener: OnDownLoadListener
    ) {
        DownLoadManager.downLoad(key, url, savePath, saveName, loadListener)
    }

    //下载 cancel
    fun doDownLoadCancel(key: String) {
        DownLoadManager.cancel(key)
    }

    //暂停下载
    fun doDownLoadPause(key: String) {
        DownLoadManager.pause(key)
    }

    //暂停所有下载
    fun doDownLoadPauseAll() {
        DownLoadManager.doDownLoadPauseAll()
    }

    //取消所有下载
    fun doDownLoadCancelAll() {
        DownLoadManager.doDownLoadCancelAll()
    }


    //上传
    fun <T : Any> doUpload(
        tag: String,
        url: String,
        tClass: Class<T>,
        listener: OnUpLoadListener<T>
    ): Task {
        listener.opUploadPrepare(tag)
        for ((key, value) in bodyMap) {
            bodyMap[key] = ProgressRequestBody(listener, tag, value.getRequestBody(), handler)
        }
        val observable = ObservableTool.getObservableUpload(url, heard, map, bodyMap)
        val beanObserver = UpLoadObserver(tag, handler,tClass, listener)
        bindLifeCycle(observable).subscribe(beanObserver)
        val task = Task(beanObserver.disposable)
        UpLoadPool.add(tag, listener, task)
        return task
    }


    fun doUpLoadCancel(tag: String) {
        UpLoadPool.getListener(tag)?.onUploadCancel(tag)
        UpLoadPool.remove(tag)
    }


}