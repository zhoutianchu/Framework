package com.zhoutianchu.framework.bean.base;

/**
 * Created by zhoutianchu on 2017/4/13.
 */

public class Message<T> {

    public static enum MsgId {EXIT}

    private MsgId msg_id;

    private T data;

    public MsgId getMsg_id() {
        return msg_id;
    }

    public Message setMsg_id(MsgId msg_id) {
        this.msg_id = msg_id;
        return this;
    }

    public T getData() {
        return data;
    }

    public Message setData(T data) {
        this.data = data;
        return this;
    }
}
