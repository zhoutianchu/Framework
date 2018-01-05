package com.zhoutianchu.framework.network;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zhoutianchu.framework.R;
import com.zhoutianchu.framework.activity.BaseActivity;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhoutianchu on 2017/4/11.
 */

public class RetrofitClient {
    // private static final String baseUrl = "http://219.135.153.39:9001/";
    private static final String baseUrl = "http://219.135.153.39:9001/";
    private static Retrofit retrofit;
    private static RetrofitClient instance;
    private ApiService apiService;
    private Context context;

    public static RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context);
        }
        return RetrofitClient.instance.setContext(context);
    }

    private RetrofitClient(Context context) {
        this.context = context;
        MyInit init = new MyInit();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).authenticator((route, response) -> {
                    /*
                     *session失效返回401时会回调此方法
                     */
                    return null;
                }).addInterceptor(chain -> {
                    Request req = chain.request();
                    String param = "";
                    if ("GET".equals(req.method()) || "DELETE".equals(req.method())) {
                        param = req.url().query();
                    } else {
                        Buffer buffer = new Buffer();
                        req.body().writeTo(buffer);
                        param = buffer.clone().readString(Charset.forName("UTF-8"));
                    }
                    return chain.proceed(chain.request().newBuilder()
                            .addHeader("x-lemon-secure", "ITGGRgBYJhjpN7Ft")
                            .build());
                }).sslSocketFactory(init.sslSocketFactory, init.manager).addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();
        retrofit = new Retrofit.Builder().client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())).
                baseUrl(baseUrl).build();
        apiService = retrofit.create(ApiService.class);
    }

    class MyInit {
        X509TrustManager manager;
        SSLSocketFactory sslSocketFactory;

        private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
            for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                    return (X509TrustManager) trustManager;
                }
            }
            return null;
        }

        public MyInit() {
            SSLContext sslContext;
            try {
                sslContext = SSLContext.getInstance("TLS");
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                //Create a KeyStore containing our trusted CAs
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null);
                int[] certificates = {R.raw.key};
                for (int i = 0; i < certificates.length; i++) {
                    //读取本地证书
                    InputStream is = context.getResources().openRawResource(certificates[i]);
                    keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(is));
                    if (is != null) {
                        is.close();
                    }
                }
                //Create a TrustManager that trusts the CAs in our keyStore
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                X509TrustManager trustManager = chooseTrustManager(trustManagers);
                if (trustManager != null) {
                    manager = new MyTrustManager(chooseTrustManager(trustManagers));
                } else {

                }
                sslContext.init(null, new TrustManager[]{manager}, null);
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public ApiService getApiService() {
        return apiService;
    }

    public RetrofitClient showProgressDialog() {
        if (context != null) {
            if (context instanceof BaseActivity) {
                ((BaseActivity) context).showProgress();
            }
        }
        return this;
    }

    public RetrofitClient setContext(Context context) {
        this.context = context;
        return this;
    }
}
