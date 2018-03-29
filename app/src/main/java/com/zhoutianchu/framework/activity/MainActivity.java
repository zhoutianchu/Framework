package com.zhoutianchu.framework.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.zhoutianchu.framework.R;
import com.zhoutianchu.framework.intf.PermissionListener;
import com.zhoutianchu.framework.utils.GlideUtil;
import com.zhoutianchu.framework.view.GlideImageLoader;
import com.zhoutianchu.framework.view.banner.Banner;
import com.zhoutianchu.framework.view.banner.loader.ImageLoader;
import com.zhoutianchu.framework.view.spanner.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
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

    @BindView(R.id.nice_spanner)
    NiceSpinner nice_spanner;

    @BindView(R.id.btn_choose)
    Button btn_choose;

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
        throttleFirst(btn_3).subscribe(v -> gotoZxingActivity());
        throttleFirst(btn_choose).subscribe(v -> {
            Intent intent = new Intent(this, ImageGridActivity.class);
            startActivityForResult(intent, IMAGE_PICKER);
        });
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

        List<String> strings = Arrays.asList("选项1", "选项2", "选项3", "选项4");
        nice_spanner.attachDataSource(strings);
        init();
    }

    private void generateRandom() {
        list.clear();
        while (list.size() < 10) {
            list.add(String.valueOf(random.nextInt()));
        }
        Observable.fromIterable(list).compose(transformer_to_main()).subscribe(txt_random::setText);
    }

    private void gotoZxingActivity() {
        String permissions[] = {Manifest.permission.CAMERA};
        requestPermissions(permissions, new PermissionListener() {
            @Override
            public void onGranted() {
                startActivity(new Intent(MainActivity.this, ZxingActivity.class));
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                showToast("请打开手机的相机权限");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                StringBuffer buffer = new StringBuffer();
                for (ImageItem item : images) {
                    buffer.append(item.path);
                    buffer.append("\n");
                }
                showToast(buffer.toString());
            } else {
                showToast("未选择");
            }
        }
    }

    private void init(){
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
        imagePicker.setMultiMode(true);
    }
}
