package com.allens.download.data


data class DownLoadBean(
    var throwable: Throwable,
    val isSuccess: Boolean,
    val path: String
)