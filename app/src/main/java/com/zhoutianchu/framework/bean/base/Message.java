package com.zhoutianchu.framework.bean.base;

/**
 * Created by zhoutianchu on 2017/4/13.
 */

public class Message<T> {

    public static enum MsgId {EXIT,GOTO_CASHIER,GOTO_OWNER,SET_NO_MENU,SET_MENU,PAY_SUCCESS,SETTLEMENT_PWD_CONFIRM_SUCCESS,ORDER_REFUND_RESULT,ORDER_SEARCH_RESULT,PUSH_DATA,LOGIN_FAIL,PUSH_ORDER_DATA,REFRESH_SETTLEMENT_INFO,LANGUAGE_CHANGE}

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
