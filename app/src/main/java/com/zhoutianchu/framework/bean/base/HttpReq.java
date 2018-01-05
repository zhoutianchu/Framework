package com.zhoutianchu.framework.bean.base;


import com.zhoutianchu.framework.bean.req.BaseReq;

/**
 * Created by zhout on 2017/8/1.
 */

public class HttpReq<T extends BaseReq> {

    private T body;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public HttpReq(T body) {
        this.body = body;
    }

    public HttpReq(){

    }

}
