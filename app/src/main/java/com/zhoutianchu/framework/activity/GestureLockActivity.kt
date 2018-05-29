package com.zhoutianchu.framework.activity

import android.content.Context
import android.content.Intent

import com.zhoutianchu.framework.R
import com.zhoutianchu.framework.utils.ArraysUtil
import com.zhoutianchu.framework.utils.Constants
import com.zhoutianchu.framework.utils.SaveObjectUtils
import com.zhoutianchu.framework.view.GestureLock
import com.zhoutianchu.framework.view.GestureLock.OnGestureEventListener
import com.zhoutianchu.framework.view.GestureLockView
import com.zhoutianchu.framework.view.NexusStyleLockView

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_gesture_lock.*
import kotlinx.android.synthetic.main.title_common.*

/**
 * Created by zhout on 2018/3/13.
 * 手势解锁实现类
 */

class GestureLockActivity : BaseActivity() {

    private var mode = 0// 0设置手势密码 1校验手势密码 2修改手势密码

    private var state = 0

    private val errDelay: Long = 1

    private var psw: ArrayList<Int>? = null

    override fun setLayout() {
        setContentView(R.layout.activity_gesture_lock)
    }

    override fun addAction() {
        throttleFirst(btn_back).subscribe { v -> back() }
    }

    override fun initData() {
        mode = intent.getIntExtra(Constants.PARAMS_1, 0)
        initGesture()
    }

    private fun initGesture() {
        if (mode == 0) {
            gesture_lock.setMode(GestureLock.MODE_EDIT)
            psw = ArrayList()
        } else {
            psw = SaveObjectUtils.getInstance(this).getObject<ArrayList<Int>>(
                    Constants.KEY_GESTURE_PWD,
                    ArrayList<Int>().javaClass
            )
            if (psw == null) {
                mode = 0
                state = 0
                gesture_lock.setMode(GestureLock.MODE_EDIT)
                psw = ArrayList()
            } else if (mode == 1) {
                gesture_lock.setMode(GestureLock.MODE_NORMAL)
            } else if (mode == 2) {
                gesture_lock.setMode(GestureLock.MODE_NORMAL)
            }
        }
        refreshTitle()
        gesture_lock.setAdapter(object : GestureLock.GestureLockAdapter {

            override fun getDepth(): Int {
                return 3
            }

            override fun getCorrectGestures(): IntArray {
                return if (mode == 0) {
                    if (state == 0) {
                        intArrayOf(1, 2, 3, 4)
                    } else {
                        ArraysUtil.list_to_int_arrays(psw)
                    }
                } else {
                    ArraysUtil.list_to_int_arrays(psw)
                }
            }

            override fun getUnmatchedBoundary(): Int {
                return 5
            }

            override fun getBlockGapSize(): Int {
                return 30
            }

            override fun getGestureLockViewInstance(context: Context, position: Int): GestureLockView {
                return NexusStyleLockView(context)
            }
        })
        gesture_lock.setOnGestureEventListener(object : OnGestureEventListener {
            override fun onBlockSelected(position: Int) {

            }

            override fun onGestureEvent(matched: Boolean) {
                setVibrator(100)
                if (mode == 0) {
                    if (state == 0) {
                        state = 1
                        gesture_lock.setMode(GestureLock.MODE_NORMAL)
                        refreshTitle()
                        gesture_lock.notifyDataChanged()
                        gesture_lock.clear()
                    } else if (state == 1) {
                        if (matched) {
                            SaveObjectUtils.getInstance(this@GestureLockActivity).setObject(Constants.KEY_GESTURE_PWD, psw)
                            showToast("设置成功")
                            finish()
                        } else {
                            showToast("输入错误,请重新输入")
                            gesture_lock.setTouchable(false)
                            delayToClear()
                        }
                    }
                } else if (mode == 1) {
                    if (matched) {
                        showToast("校验成功")
                        gesture_lock.clear()
                        finish()
                    } else {
                        delayToClear()
                        showToast("校验失败")
                    }
                } else if (mode == 2) {
                    if (matched) {
                        mode = 0
                        state = 0
                        gesture_lock.setMode(GestureLock.MODE_EDIT)
                        refreshTitle()
                        gesture_lock.clear()
                    } else {
                        delayToClear()
                        showToast("校验失败")
                    }
                }
            }

            override fun onUnmatchedExceedBoundary() {

            }

            override fun getGestureResult(result: IntArray) {
                if (mode == 0 && state == 0) {
                    psw!!.clear()
                    for (i in result) {
                        if (i == -1)
                            break
                        psw!!.add(i)
                    }
                }
            }

        })
    }

    /**
     * 刷新title显示
     */
    private fun refreshTitle() {
        if (mode == 0) {
            if (state == 0) {
                text_title.text= "设置手势密码"
            } else if (state == 1) {
                text_title.text = "再次输入手势密码"
            }
        } else if (mode == 1) {
            text_title.text="校验手势密码"
        } else if (mode == 2) {
            text_title.text="校验手势密码"
        }
    }

    private fun back() {
        if (mode == 0) {
            if (state == 0) {
                finish()
            } else {
                gesture_lock.setMode(GestureLock.MODE_EDIT)
                state = 0
                refreshTitle()
            }
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        back()
    }

    /**
     * 延时清除手势密码痕迹
     */
    private fun delayToClear() {
        gesture_lock.setTouchable(false)
        Observable.timer(errDelay, TimeUnit.SECONDS).compose(transformer_to_main()).subscribe { l ->
            gesture_lock.clear()
            gesture_lock.setTouchable(true)
        }
    }

    companion object {

        fun startActivity(context: Context, mode: Int) {
            val t = Intent(context, GestureLockActivity::class.java)
            t.putExtra(Constants.PARAMS_1, mode)
            context.startActivity(t)
        }
    }
}
