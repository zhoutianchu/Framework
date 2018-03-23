package com.zhoutianchu.framework.activity;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhoutianchu.framework.R;
import com.zhoutianchu.framework.utils.ImageLoaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;

public class MainActivity extends BaseActivity {


    @BindView(R.id.btn_test)
    Button btn_test;

    @BindView(R.id.btn_test1)
    Button btn_test1;

    @BindView(R.id.btn_test2)
    Button btn_test2;

    @BindView(R.id.txt_random)
    TextView txt_random;

    @BindView(R.id.btn_random)
    Button btn_random;

    @BindView(R.id.img_head)
    ImageView img_head;

    private Random random = new Random(12345);

    List<String> list = new ArrayList<>();

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void addAction() {
        throttleFirst(btn_test).subscribe(v -> GestureLockActivity.startActivity(this, 0));
        throttleFirst(btn_test1).subscribe(v -> GestureLockActivity.startActivity(this, 1));
        throttleFirst(btn_test2).subscribe(v -> GestureLockActivity.startActivity(this, 2));
        throttleFirst(btn_random).subscribe(v->generateRandom());
    }

    @Override
    protected void initData() {
        ImageLoaders.loadCircleImage(this,img_head,"http://static.cucatech.com/ddxl/images/zb.jpg");
    }

    private void generateRandom() {
        list.clear();
        while (list.size() < 10) {
            list.add(String.valueOf(random.nextInt()));
        }
        Observable.fromIterable(list).compose(transformer_to_main()).subscribe(txt_random::setText);
    }
}
