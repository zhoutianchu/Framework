package com.zhoutianchu.framework.activity;

import android.widget.Button;

import com.zhoutianchu.framework.R;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.btn_test)
    Button btn_test;

    @BindView(R.id.btn_test1)
    Button btn_test1;

    @BindView(R.id.btn_test2)
    Button btn_test2;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void addAction() {
        throttleFirst(btn_test).subscribe(v -> GestureLockActivity.startActivity(this, 0));
        throttleFirst(btn_test1).subscribe(v -> GestureLockActivity.startActivity(this, 1));
        throttleFirst(btn_test2).subscribe(v -> GestureLockActivity.startActivity(this, 2));
    }

    @Override
    protected void initData() {

    }
}
