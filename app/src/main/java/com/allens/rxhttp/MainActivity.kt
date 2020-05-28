package com.allens.rxhttp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.allens.RxHttp
import com.allens.config.HttpLevel
import com.allens.config.LifecycleCancel
import com.allens.impl.OnBuildClientListener
import com.allens.impl.OnHttpListener
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "Main"
    }

    private lateinit var rxHttp: RxHttp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rxHttp = RxHttp.Builder()
            .baseUrl("https://www.wanandroid.com")
            .isLog(true)
            .level(HttpLevel.BODY)
            .writeTimeout(10)
            .readTimeout(10)
            .connectTimeout(10)
            .addBuilderClientListener(object : OnBuildClientListener {
                override fun addBuildClient(): MutableSet<Any> {
                    return mutableSetOf(RxJava2CallAdapterFactory.create())
                }
            })
            .build(this)

        btn_get.setOnClickListener {
            getRequest()
        }
        btn_post.setOnClickListener {
            postRequest()
        }

        btn_download.setOnClickListener {
            startActivity(Intent(this, DownLoadAct::class.java))
        }
        btn_upload.setOnClickListener {
            startActivity(Intent(this, UploadAct::class.java))
        }
    }

    private fun postRequest() {
        val data = rxHttp
            .create()
            .addParameter("title", "123456")
            .addParameter("author", "123456")
            .addParameter("link", "123456")
            .bindEvent(this,LifecycleCancel.ON_STOP)
            .doPost("lg/collect/add/json", TestBean::class.java, object : OnHttpListener<TestBean>() {
                override fun onSuccess(t: TestBean) {
                    log.text = t.toString()
                }

                override fun onError(e: Throwable) {
                    log.text = e.toString()
                }
            })
    }


    private fun getRequest() {
        Log.i(TAG, "get 方法启动 线程 ${Thread.currentThread().name}")
        val data = rxHttp
            .create()
            .addParameter("k", "java")
            .doGet(
                parameter = "wxarticle/chapters/json", tClass = TestBean::class.java,
                listener = object : OnHttpListener<TestBean>() {
                    override fun onSuccess(t: TestBean) {
                        log.text = t.toString()
                    }

                    override fun onError(e: Throwable) {
                        log.text = e.toString()
                    }
                }
            )

        Log.i(TAG, "收到响应 $data thread ${Thread.currentThread().name}")

    }
}
