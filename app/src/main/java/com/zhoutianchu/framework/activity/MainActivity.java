package com.zhoutianchu.framework.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhoutianchu.framework.R;
import com.zhoutianchu.framework.intf.PermissionListener;
import com.zhoutianchu.framework.utils.GlideUtil;
import com.zhoutianchu.framework.utils.LogUtil;
import com.zhoutianchu.framework.view.banner.Banner;
import com.zhoutianchu.framework.view.banner.loader.ImageLoader;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @BindView(R.id.banner)
    Banner banner;

    @BindView(R.id.btn_3)
    Button btn_3;

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
        throttleFirst(btn_random).subscribe(v -> generateRandom());
        throttleFirst(btn_3).subscribe(v->gotoZxingActivity());
    }

    @Override
    protected void initData() {
        List<String> images = new ArrayList<>();
        images.add("https://goss4.vcg.com/creative/vcg/800/version23/VCG41536246680.jpg");
        images.add("https://goss4.vcg.com/creative/vcg/800/version23/VCG41536246680.jpg");
        images.add("https://goss4.vcg.com/creative/vcg/800/version23/VCG41536246680.jpg");
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                GlideUtil.loadImage((Activity) context, imageView, (String) path);
            }
        });
        banner.setImages(images);
        banner.start();
        GlideUtil.loadCircleImage(this, img_head, "http://static.cucatech.com/ddxl/images/zb.jpg");
    }

    private void generateRandom() {
        list.clear();
        while (list.size() < 10) {
            list.add(String.valueOf(random.nextInt()));
        }
        Observable.fromIterable(list).compose(transformer_to_main()).subscribe(txt_random::setText);
    }

    private void gotoZxingActivity(){
        String permissions[]={Manifest.permission.CAMERA};
        requestPermissions(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                startActivity(new Intent(MainActivity.this,ZxingActivity.class));
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                showToast("请打开手机的相机权限");
            }
        });
    }
}
