package com.allens.subscriber

import io.reactivex.disposables.Disposable

class Task(private val disposable: Disposable) {


    fun isDisposed(): Boolean {
        return disposable.isDisposed
    }

    fun dispose() {
        disposable.dispose()
    }


    fun cancel() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}