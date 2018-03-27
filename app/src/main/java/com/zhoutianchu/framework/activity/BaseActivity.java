package com.zhoutianchu.framework.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.zhoutianchu.framework.bean.base.HttpResponse;
import com.zhoutianchu.framework.bean.base.Message;
import com.zhoutianchu.framework.intf.PermissionListener;
import com.zhoutianchu.framework.view.MocamProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by zhout on 2018/1/5.
 */

public abstract class BaseActivity extends SwipeBackActivity {
    protected static int FirstValue = 10001;

    private MocamProgressDialog progressDialog;

    private SwipeBackLayout mSwipeBackLayout;

    private static Toast toast;

    private static PermissionListener mPermissionListener;
    private static final int CODE_REQUEST_PERMISSION = 1;


    /**
     * rxjava 切换至主线程
     *
     * @param <T>
     * @return
     */
    protected <T extends Object> ObservableTransformer<T, T> transformer_to_main() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).filter(resp -> !isFinishing()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 网络请求返回后的前置过滤器
     *
     * @param <T>
     * @return
     */
    protected <T extends HttpResponse> ObservableTransformer<T, T> resp_filter() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io()).onErrorResumeNext(
                        err -> {
                            return Observable.just((T) new HttpResponse<>(HttpResponse.NOT_FOUND, err.getMessage()));
                        }
                ).filter(resp -> !isFinishing()).observeOn(AndroidSchedulers.mainThread()).filter(resp -> {
                    hideProgress();
                    if (!resp.isSuccess()) {
                        showToast(resp.getMsgInfo());
                        return false;
                    }
                    return true;
                }).observeOn(AndroidSchedulers.mainThread());
    }

    protected <T extends HttpResponse> ObservableTransformer<T, T> resp_filter_without_tips() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io()).onErrorResumeNext(
                        err -> {
                            return Observable.just((T) new HttpResponse<>(HttpResponse.NOT_FOUND, err.getMessage()));
                        }
                ).filter(resp -> !isFinishing()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(true);//设置允许活动退出
        mSwipeBackLayout = getSwipeBackLayout();
        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        // 滑动退出的效果只能从边界滑动才有效果，如果要扩大touch的范围，可以调用这个方法
        //mSwipeBackLayout.setEdgeSize(200);
        setLayout();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        addAction();
        initData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_REQUEST_PERMISSION:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int result = grantResults[i];
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        mPermissionListener.onGranted();
                    } else {
                        mPermissionListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }

    protected abstract void setLayout();

    protected abstract void addAction();

    protected abstract void initData();

    /**
     * 申请权限
     *
     * @param permissions 需要申请的权限(数组)
     * @param listener    权限回调接口
     */
    protected void requestPermissions(String[] permissions, PermissionListener listener) {
        Activity activity =this;
        if (null == activity) {
            return;
        }

        mPermissionListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            //权限没有授权
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]), CODE_REQUEST_PERMISSION);
        } else {
            mPermissionListener.onGranted();
        }
    }

    public void showProgress() {
        Observable.just("").compose(transformer_to_main()).subscribe(s -> {
            progressDialog = new MocamProgressDialog(BaseActivity.this);
            progressDialog.show();
        });
    }

    public void hideProgress() {
        Observable.just("").compose(transformer_to_main()).subscribe(s -> {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        });
    }

    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg))
            msg = "未知错误";
        if (!msg.contains("401"))
            Observable.just(msg).compose(transformer_to_main()).subscribe(s -> {
                if (toast == null)
                    toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
                else
                    toast.setText(s);
                toast.show();
            });
    }

    public void showToast(int id) {
        String msg = getResources().getString(id);
        if (TextUtils.isEmpty(msg))
            msg = "未知错误";
        Observable.just(msg).compose(transformer_to_main()).subscribe(s -> {
            if (toast == null)
                toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
            else
                toast.setText(s);
            toast.show();
        });
    }


    /**
     * 按钮防重复点击方法
     *
     * @param view
     * @return
     */
    protected Observable throttleFirst(View view) {
        return RxView.clicks(view).throttleFirst(1000, TimeUnit.MILLISECONDS);
    }

    protected Observable throttleFirst(long delay, View view) {
        return RxView.clicks(view).throttleFirst(delay, TimeUnit.MILLISECONDS);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(Message msg) {
        if (Message.MsgId.EXIT.equals(msg.getMsg_id())) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected <T> T getSerExtra(String id) {
        return (T) getIntent().getSerializableExtra(id);
    }

    /**
     * 震动
     * @param time
     */
    protected void setVibrator(long time){
        Vibrator vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }

}
