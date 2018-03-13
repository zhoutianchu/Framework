package com.zhoutianchu.framework.activity;

import android.content.Context;

import com.sevenheaven.gesturelock.GestureLock;
import com.sevenheaven.gesturelock.GestureLockView;
import com.zhoutianchu.framework.R;
import com.zhoutianchu.framework.view.NexusStyleLockView;

import butterknife.BindView;

/**
 * Created by zhout on 2018/3/13.
 * 手势解锁实现类
 */

public class GestureLockActivity extends BaseActivity {
    @BindView(R.id.gesture_lock)
    GestureLock gesture_lock;
    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_gesture_lock);
    }

    @Override
    protected void addAction() {

    }

    @Override
    protected void initData() {
        gesture_lock.setAdapter(new GestureLock.GestureLockAdapter() {

            @Override
            public int getDepth() {
                return 3;
            }

            @Override
            public int[] getCorrectGestures() {
                return new int[]{1, 2, 3, 4};
            }

            @Override
            public int getUnmatchedBoundary() {
                return 5;
            }

            @Override
            public int getBlockGapSize(){
                return 30;
            }

            @Override
            public GestureLockView getGestureLockViewInstance(Context context, int position) {
                return new NexusStyleLockView(context);
            }
        });
        gesture_lock.setOnGestureEventListener(new GestureLock.OnGestureEventListener() {
            @Override
            public void onBlockSelected(int position) {
                showToast("选中了："+position);
            }

            @Override
            public void onGestureEvent(boolean matched) {
                showToast("手势密码匹配结果:"+matched);
            }

            @Override
            public void onUnmatchedExceedBoundary() {

            }
        });
    }
}
