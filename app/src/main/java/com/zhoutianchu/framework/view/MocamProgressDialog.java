package com.zhoutianchu.framework.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;

import com.zhoutianchu.framework.R;


public class MocamProgressDialog extends Dialog {
    public MocamProgressDialog(Context context, String msg) {
        super(context, R.style.ipos_uniprogressdialog);
    }
    public MocamProgressDialog(Context context) {
        super(context, R.style.ipos_uniprogressdialog);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipos_dialog_uniprocess);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        ImageView imageView = (ImageView) findViewById(R.id.ipos_infoProgressAnim);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                .getDrawable();
        if (animationDrawable != null)
            animationDrawable.start();
    }
}
