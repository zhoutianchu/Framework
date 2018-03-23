package com.zhoutianchu.framework.activity;

import android.support.v7.widget.AppCompatTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhoutianchu.framework.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by zhout on 2018/3/14.
 */

public abstract class BaseTitleActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.back)
    ImageView back;

    @Override
    protected void addAction() {
        throttleFirst(back).subscribe(v -> finish());
    }

    @Override
    protected void initData() {

    }
}
