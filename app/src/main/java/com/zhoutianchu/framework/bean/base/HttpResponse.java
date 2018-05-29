package com.zhoutianchu.framework.bean.base;

import android.text.TextUtils;

/**
 * Created by zhoutianchu on 2017/4/24.
 */

public class HttpResponse<T> {

    private String digest;
    private String msgCd;
    private String msgInfo;

    private T body;

    public static final String NOT_FOUND = "404";

    public static final String SUCCESS_CODE = "00000";

    public static final String LOGIN_FAIL = "401";

    public HttpResponse(String msgCd, String msgInfo) {
        this.msgCd = msgCd;
        this.msgInfo = msgInfo;
    }

    public HttpResponse() {
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getMsgCd() {
        return msgCd;
    }

    public void setMsgCd(String msgCd) {
        this.msgCd = msgCd;
    }

    public String getMsgInfo() {
        return msgInfo;
    }

    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public boolean isSuccess() {
        if (TextUtils.isEmpty(msgCd))
            return false;
        return msgCd.endsWith(SUCCESS_CODE);
    }

    public String getId() {
        return body.getClass().toString();
    }
}
