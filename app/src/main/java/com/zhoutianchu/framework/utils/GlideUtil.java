package com.zhoutianchu.framework.utils;

import android.app.Activity;
import android.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zhoutianchu.framework.R;
import com.zhoutianchu.framework.view.CircleImageTransform;

/**
 * Created by zhout on 2018/3/23.
 */

public class GlideUtil {
    private GlideUtil() {

    }

    private static RequestOptions circleOptions;

    private static RequestOptions options;

    static {
        circleOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.head_default)//占位图
                .error(R.drawable.head_default)//加载错误时占位图
                .priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleImageTransform());

        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.head_default)//占位图
                .error(R.drawable.head_default)//加载错误时占位图
                .priority(Priority.HIGH).diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    public static void loadCircleImage(Activity activity, ImageView imageView, String url) {
        Glide.with(activity)
                .load(url)
                .apply(circleOptions)
                .into(imageView);
    }

    public static void loadCircleImage(Fragment fragment, ImageView imageView, String url) {
        Glide.with(fragment)
                .load(url)
                .apply(circleOptions)
                .into(imageView);
    }

    public static void loadImage(Activity activity, ImageView imageView, String url) {
        Glide.with(activity)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void loadImage(Fragment fragment, ImageView imageView, String url) {
        Glide.with(fragment)
                .load(url)
                .apply(options)
                .into(imageView);
    }
}
