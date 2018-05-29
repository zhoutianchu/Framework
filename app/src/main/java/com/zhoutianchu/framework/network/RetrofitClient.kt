package com.zhoutianchu.framework.network

import android.content.Context

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.zhoutianchu.framework.R
import com.zhoutianchu.framework.activity.BaseActivity

import java.io.InputStream
import java.nio.charset.Charset
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit

import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by zhoutianchu on 2017/4/11.
 */

class RetrofitClient private constructor(private var context: Context?) {
    val apiService: ApiService

    init {
        val init = MyInit()
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).authenticator { route, response -> null }.retryOnConnectionFailure(true).addInterceptor { chain ->
                    val req = chain.request()
                    var param = ""
                    if ("GET" == req.method() || "DELETE" == req.method()) {
                        param = req.url().query()
                    } else {
                        val buffer = Buffer()
                        req.body().writeTo(buffer)
                        param = buffer.clone().readString(Charset.forName("UTF-8"))
                    }
                    chain.proceed(chain.request().newBuilder()
                            .addHeader("x-lemon-secure", "ITGGRgBYJhjpN7Ft")
                            .build())
                }.sslSocketFactory(init.sslSocketFactory, init.manager).addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
        retrofit = Retrofit.Builder().client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())).baseUrl(baseUrl).build()
        apiService = retrofit!!.create(ApiService::class.java)
    }

    internal inner class MyInit {
        var manager: X509TrustManager?=null
        var sslSocketFactory: SSLSocketFactory?=null

        private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager? {
            for (trustManager in trustManagers) {
                if (trustManager is X509TrustManager) {
                    return trustManager
                }
            }
            return null
        }

        init {
            val sslContext: SSLContext
            try {
                sslContext = SSLContext.getInstance("TLS")
                val certificateFactory = CertificateFactory.getInstance("X.509")
                //Create a KeyStore containing our trusted CAs
                val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                keyStore.load(null, null)

                //证书文件
                val certificates = intArrayOf(R.raw.key)

                for (i in certificates.indices) {
                    //读取本地证书
                    val `is` = context!!.resources.openRawResource(certificates[i])
                    keyStore.setCertificateEntry(i.toString(), certificateFactory.generateCertificate(`is`))
                    `is`?.close()
                }
                //Create a TrustManager that trusts the CAs in our keyStore
                val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(keyStore)
                val trustManagers = trustManagerFactory.trustManagers
                val trustManager = chooseTrustManager(trustManagers)
                if (trustManager != null) {
                    manager = MyTrustManager(chooseTrustManager(trustManagers))
                } else {

                }
                sslContext.init(null, arrayOf<TrustManager>(this!!.manager!!), null)
                sslSocketFactory = sslContext.socketFactory
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun showProgressDialog(): RetrofitClient {
        if (context != null) {
            if (context is BaseActivity) {
                (context as BaseActivity).showProgress()
            }
        }
        return this
    }

    fun setContext(context: Context): RetrofitClient {
        this.context = context
        return this
    }

    companion object {
        // private static final String baseUrl = "http://219.135.153.39:9001/";
        private val baseUrl = "http://219.135.153.39:9001/"
        private var retrofit: Retrofit?=null
        private var instance: RetrofitClient? = null

        fun getInstance(context: Context): RetrofitClient {
            if (instance == null) {
                instance = RetrofitClient(context)
            }
            return RetrofitClient.instance!!.setContext(context)
        }
    }
}
