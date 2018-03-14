package com.zhoutianchu.framework.utils;

import android.util.Log;

import com.zhoutianchu.framework.BuildConfig;

/**
 * Created by zhoutianchu on 2017/4/12.
 */

public class LogUtil {
    private static final String LOG_TAG = "framework";

    private LogUtil() {

    }

    public static void debug(String msg) {
        if (BuildConfig.DEBUG) {
            if (msg != null) {
                Log.v(LOG_TAG, msg);
            } else {
                Log.v(LOG_TAG, "null");
            }
        }
    }
}
