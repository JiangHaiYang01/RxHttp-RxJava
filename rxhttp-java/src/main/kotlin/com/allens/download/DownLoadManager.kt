package com.allens.download

import com.allens.RxHttp
import com.allens.download.utils.DownLoadPool
import com.allens.download.utils.FileTool
import com.allens.download.utils.ShareDownLoadUtil
import com.allens.impl.OnDownLoadListener
import com.allens.tools.ObservableTool
import com.allens.subscriber.DownLoadObserver
import com.allens.tools.RxHttpLogTool
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File


object DownLoadManager {

    private const val TAG = RxHttp.TAG


    fun downLoad(
        key: String,
        url: String,
        savePath: String,
        saveName: String,
        loadListener: OnDownLoadListener
    ) {
        //判断是否已经在队列中
        val disposable = DownLoadPool.getDisposableFromKey(key)
        if (disposable != null && !disposable.isDisposed) {
            RxHttpLogTool.i(TAG, "key $key 已经在队列中")
            return
        }

        RxHttpLogTool.i(
            TAG,
            "startDownLoad key: $key  url:$url  savePath: $savePath  saveName:$saveName"
        )


        val file = File("$savePath/$saveName")
        val currentLength = if (!file.exists()) {
            0L
        } else {
            ShareDownLoadUtil.getLong(key, 0)
        }
        RxHttpLogTool.i(TAG, "startDownLoad current $currentLength")


        loadListener.onDownLoadPrepare(key = key)
        val observable = ObservableTool.getObservableDownLoad("bytes=$currentLength-", url)
        observable
            .map {
                DownLoadPool.add(key, loadListener)
                FileTool.downToFile(key, url, savePath, saveName, loadListener, currentLength, it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(DownLoadObserver(key, loadListener))

    }


    fun cancel(key: String) {
        val listener = DownLoadPool.getListenerFromKey(key)
        listener?.onDownLoadCancel(key)
        val path = DownLoadPool.getPathFromKey(key)
        if (path != null) {
            val file = File(path)
            if (file.exists()) {
                file.delete()
            }
        }
        DownLoadPool.remove(key)
    }

    fun pause(key: String) {
        val listener = DownLoadPool.getListenerFromKey(key)
        listener?.onDownLoadPause(key)
        DownLoadPool.pause(key)

    }

    fun doDownLoadCancelAll() {
        DownLoadPool.getListenerMap().forEach {
            cancel(it.key)
        }
    }

    fun doDownLoadPauseAll() {
        DownLoadPool.getListenerMap().forEach {
            pause(it.key)
        }
    }
}