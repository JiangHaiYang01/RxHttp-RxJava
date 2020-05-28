package com.allens.subscriber

import com.allens.RxHttp
import com.allens.download.data.DownLoadBean
import com.allens.download.utils.DownLoadPool
import com.allens.impl.OnDownLoadListener
import com.allens.config.HttpConfig
import com.allens.tools.RxHttpLogTool
import io.reactivex.disposables.Disposable

class DownLoadObserver(private val key: String, private val loadListener: OnDownLoadListener) :
    BaseObserver<DownLoadBean>() {

    private val TAG = RxHttp.TAG

    private lateinit var downLoadBean: DownLoadBean

    override fun onSubscribe(d: Disposable) {
        super.onSubscribe(d)
        DownLoadPool.add(key, d)
    }

    override fun onNext(value: DownLoadBean) {
        super.onNext(value)
        downLoadBean = value
    }

    override fun onError(e: Throwable) {
        super.onError(e)
        loadListener.onDownLoadError(key, e)
    }

    override fun onComplete() {
        super.onComplete()
        if (HttpConfig.isLog) {
            RxHttpLogTool.i(TAG, "download complete $downLoadBean")
        }

        if (downLoadBean.isSuccess) {
            DownLoadPool.remove(key)
            loadListener.onDownLoadSuccess(key, downLoadBean.path)
        } else {
            loadListener.onDownLoadError(key, downLoadBean.throwable)
        }
    }
}
