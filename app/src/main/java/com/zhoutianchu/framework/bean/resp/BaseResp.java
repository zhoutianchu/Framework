package com.zhoutianchu.framework.bean.resp;

import java.io.Serializable;

/**
 * Created by zhout on 2017/7/13.
 */

public class BaseResp implements Serializable {
    private String page;
    private String total;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

