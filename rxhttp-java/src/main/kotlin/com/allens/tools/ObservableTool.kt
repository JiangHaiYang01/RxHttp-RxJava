package com.allens.tools

import com.allens.impl.ApiService
import com.allens.manager.HttpManager
import com.allens.upload.ProgressRequestBody
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.util.*
import kotlin.collections.HashMap

/**
 *
 * @Description:
 * @Author:         Allens
 * @CreateDate:     2019-11-22 16:07
 * @Version:        1.0
 */
object ObservableTool {

    fun getObservableGet(
        parameter: String,
        heard: HashMap<String, String>,
        map: HashMap<String, Any>
    ): Observable<ResponseBody> {
        val baseUrl = HttpManager.retrofit.baseUrl().toUrl().toString()
        var getUrl: String = baseUrl + parameter
        if (map.size > 0) {
            val param: String = UrlTool.prepareParam(map)
            if (param.trim().isNotEmpty()) {
                getUrl += "?$param"
            }
        }
        return HttpManager.getService(ApiService::class.java)
            .doGet(heard, getUrl)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    fun getObservablePost(
        parameter: String,
        heard: HashMap<String, String>,
        map: HashMap<String, Any>
    ): Observable<ResponseBody> {
        return HttpManager.getService(ApiService::class.java)
            .doPost(parameter, heard, map)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    fun getObservableBody(
        parameter: String,
        heard: HashMap<String, String>,
        map: HashMap<String, Any>
    ): Observable<ResponseBody> {
        val toJson = HttpManager.gson.toJson(map)
        val requestBody =
            toJson.toRequestBody("application/json".toMediaTypeOrNull())

        return HttpManager.getService(ApiService::class.java)
            .doBody(parameter, heard, requestBody)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    fun getObservablePut(
        parameter: String,
        heard: HashMap<String, String>,
        map: HashMap<String, Any>
    ): Observable<ResponseBody> {
        return HttpManager.getService(ApiService::class.java)
            .doPut(parameter, heard, map)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getObservableDelete(
        parameter: String,
        heard: HashMap<String, String>,
        map: HashMap<String, Any>
    ): Observable<ResponseBody> {
        return HttpManager.getService(ApiService::class.java)
            .doDelete(parameter, heard, map)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getObservableDownLoad(
        range: String,
        url: String
    ): Observable<ResponseBody> {
        return HttpManager.getServiceFromDownLoadOrUpload(ApiService::class.java)
            .downloadFile(range, url)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())

    }


    fun getObservableUpload(
        url: String,
        heard: HashMap<String, String>,
        map: HashMap<String, Any>,
        bodyMap:HashMap<String, ProgressRequestBody>
    ): Observable<ResponseBody> {
        return HttpManager.getServiceFromDownLoadOrUpload(ApiService::class.java)
            .upFileList(url, heard, map, bodyMap)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())

    }


}