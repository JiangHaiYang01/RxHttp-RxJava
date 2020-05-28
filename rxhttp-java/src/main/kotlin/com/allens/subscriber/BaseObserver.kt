package com.allens.subscriber

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class BaseObserver<T> : Observer<T> {

     lateinit var disposable: Disposable

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onNext(value: T) {
    }

    override fun onError(e: Throwable) {
    }


    open fun isDisposed(): Boolean {
        return disposable.isDisposed
    }

    open fun dispose() {
        disposable.dispose()
    }

}