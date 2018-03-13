package com.zhoutianchu.framework.activity;

import android.content.Intent;
import android.widget.Button;

import com.zhoutianchu.framework.R;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.btn_test)
    Button btn_test;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void addAction() {
        throttleFirst(btn_test).subscribe(v -> startActivity(new Intent(this, GestureLockActivity.class)));
    }

    @Override
    protected void initData() {

    }
}
