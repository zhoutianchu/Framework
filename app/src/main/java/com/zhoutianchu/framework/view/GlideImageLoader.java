package com.zhoutianchu.framework.view;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.imagepicker.loader.ImageLoader;
import com.zhoutianchu.framework.utils.GlideUtil;

/**
 * Created by zhout on 2018/3/27.
 */

public class GlideImageLoader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)
                .load(path)
                .apply(new RequestOptions().override(width,height))
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)
                .load(path)
                .apply(new RequestOptions().override(width,height))
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
