package com.zhoutianchu.framework.activity;

import android.content.Context;
import android.content.Intent;

import com.zhoutianchu.framework.R;
import com.zhoutianchu.framework.utils.ArraysUtil;
import com.zhoutianchu.framework.utils.Constants;
import com.zhoutianchu.framework.utils.SaveObjectUtils;
import com.zhoutianchu.framework.view.GestureLock;
import com.zhoutianchu.framework.view.GestureLock.OnGestureEventListener;
import com.zhoutianchu.framework.view.GestureLockView;
import com.zhoutianchu.framework.view.NexusStyleLockView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by zhout on 2018/3/13.
 * 手势解锁实现类
 */

public class GestureLockActivity extends BaseTitleActivity {
    @BindView(R.id.gesture_lock)
    GestureLock gesture_lock;

    private int mode = 0;// 0设置手势密码 1校验手势密码 2修改手势密码

    private int state = 0;

    private long errDelay = 1;

    private ArrayList<Integer> psw;

    public static void startActivity(Context context, int mode) {
        Intent t = new Intent(context, GestureLockActivity.class);
        t.putExtra(Constants.PARAMS_1, mode);
        context.startActivity(t);
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_gesture_lock);
    }

    @Override
    protected void addAction() {
        super.addAction();
        throttleFirst(back).subscribe(v -> back());
    }

    @Override
    protected void initData() {
        super.initData();
        mode = getIntent().getIntExtra(Constants.PARAMS_1, 0);
        initGesture();
    }

    private void initGesture() {
        if (mode == 0) {
            gesture_lock.setMode(GestureLock.MODE_EDIT);
            psw = new ArrayList<>();
        } else {
            psw = SaveObjectUtils.getInstance(this).getObject(Constants.KEY_GESTURE_PWD, ArrayList.class);
            if (psw == null) {
                mode = 0;
                state = 0;
                gesture_lock.setMode(GestureLock.MODE_EDIT);
                psw = new ArrayList<>();
            } else if (mode == 1) {
                gesture_lock.setMode(GestureLock.MODE_NORMAL);
            } else if (mode == 2) {
                gesture_lock.setMode(GestureLock.MODE_NORMAL);
            }
        }
        refreshTitle();
        gesture_lock.setAdapter(new GestureLock.GestureLockAdapter() {

            @Override
            public int getDepth() {
                return 3;
            }

            @Override
            public int[] getCorrectGestures() {
                if (mode == 0) {
                    if (state == 0) {
                        return new int[]{1, 2, 3, 4};
                    } else {
                        return ArraysUtil.list_to_int_arrays(psw);
                    }
                } else {
                    return ArraysUtil.list_to_int_arrays(psw);
                }
            }

            @Override
            public int getUnmatchedBoundary() {
                return 5;
            }

            @Override
            public int getBlockGapSize() {
                return 30;
            }

            @Override
            public GestureLockView getGestureLockViewInstance(Context context, int position) {
                return new NexusStyleLockView(context);
            }
        });
        gesture_lock.setOnGestureEventListener(new OnGestureEventListener() {
            @Override
            public void onBlockSelected(int position) {

            }

            @Override
            public void onGestureEvent(boolean matched) {
                setVibrator(100);
                if (mode == 0) {
                    if (state == 0) {
                        state = 1;
                        gesture_lock.setMode(GestureLock.MODE_NORMAL);
                        refreshTitle();
                        gesture_lock.notifyDataChanged();
                        gesture_lock.clear();
                    } else if (state == 1) {
                        if (matched) {
                            SaveObjectUtils.getInstance(GestureLockActivity.this).setObject(Constants.KEY_GESTURE_PWD, psw);
                            showToast("设置成功");
                            finish();
                        } else {
                            showToast("输入错误,请重新输入");
                            gesture_lock.setTouchable(false);
                            delayToClear();
                        }
                    }
                } else if (mode == 1) {
                    if (matched) {
                        showToast("校验成功");
                        gesture_lock.clear();
                        finish();
                    } else {
                        delayToClear();
                        showToast("校验失败");
                    }
                } else if (mode == 2) {
                    if (matched) {
                        mode = 0;
                        state = 0;
                        gesture_lock.setMode(GestureLock.MODE_EDIT);
                        refreshTitle();
                        gesture_lock.clear();
                    } else {
                        delayToClear();
                        showToast("校验失败");
                    }
                }
            }

            @Override
            public void onUnmatchedExceedBoundary() {

            }

            @Override
            public void getGestureResult(int[] result) {
                if (mode == 0 && state == 0) {
                    psw.clear();
                    for (int i : result) {
                        if (i == -1)
                            break;
                        psw.add(i);
                    }
                }
            }

        });
    }

    /**
     * 刷新title显示
     */
    private void refreshTitle() {
        if (mode == 0) {
            if (state == 0) {
                title.setText("设置手势密码");
            } else if (state == 1) {
                title.setText("再次输入手势密码");
            }
        } else if (mode == 1) {
            title.setText("校验手势密码");
        } else if (mode == 2) {
            title.setText("校验手势密码");
        }
    }

    private void back() {
        if (mode == 0) {
            if (state == 0) {
                finish();
            } else {
                gesture_lock.setMode(GestureLock.MODE_EDIT);
                state = 0;
                refreshTitle();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    /**
     * 延时清除手势密码痕迹
     */
    private void delayToClear() {
        gesture_lock.setTouchable(false);
        Observable.timer(errDelay, TimeUnit.SECONDS).compose(transformer_to_main()).subscribe(l -> {
            gesture_lock.clear();
            gesture_lock.setTouchable(true);
        });
    }
}
