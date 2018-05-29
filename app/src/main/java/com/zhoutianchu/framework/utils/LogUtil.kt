package com.zhoutianchu.framework.utils

import android.util.Log

import com.zhoutianchu.framework.BuildConfig

/**
 * Created by zhoutianchu on 2017/4/12.
 */

object LogUtil {
    private val LOG_TAG = "framework"

    fun debug(msg: String?) {
        if (BuildConfig.DEBUG) {
            if (msg != null) {
                Log.v(LOG_TAG, msg)
            } else {
                Log.v(LOG_TAG, "null")
            }
        }
    }
}
