package com.zhoutianchu.framework.network;



import com.zhoutianchu.framework.bean.base.HttpReq;
import com.zhoutianchu.framework.bean.base.HttpResponse;
import com.zhoutianchu.framework.bean.req.BaseReq;
import com.zhoutianchu.framework.bean.req.PersonReq;
import com.zhoutianchu.framework.bean.resp.BaseResp;
import com.zhoutianchu.framework.bean.resp.PersonResp;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by zhoutianchu on 2017/4/11.
 */

public interface ApiService {
    /**
     * 测试api
     * @param req
     * @return
     */
    @POST("app/test")
    Observable<HttpResponse<BaseResp>> test(@Body HttpReq<BaseReq> req);

    /**
     * 下载文件,@Streaming 文件比较大时可以添加
     * @param url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    /**
     * 上传文件
     *
     * @param params
     * @return
     */
    @Multipart
    @POST
    Observable<HttpResponse<BaseResp>> upload(@Url String url,@PartMap Map<String, RequestBody> params);

    @POST("app/person")
    Observable<HttpResponse<PersonResp>> getPerson(@Body HttpReq<PersonReq> req);
}

