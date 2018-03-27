package com.zhoutianchu.framework.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhoutianchu.framework.R;

import butterknife.BindView;

/**
 * Created by zhout on 2017/7/17.
 */

public class ZxingActivity extends BaseTitleActivity {



    @BindView(R.id.complete_btn)
    TextView complete_btn;

    private static final int REQUEST_IMAGE = FirstValue++;

    @BindView(R.id.open_lamp)
    TextView open_lamp;

    private boolean is_open_lamp = false;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_zxing);
    }

    @Override
    protected void addAction() {
        throttleFirst(back).subscribe(v -> finish());
        throttleFirst(complete_btn).subscribe(v -> gotoDICM());
        throttleFirst(open_lamp).subscribe(v -> {
            if (is_open_lamp) {
                CodeUtils.isLightEnable(false);
                is_open_lamp = false;
                open_lamp.setText(R.string.scan_qr_code_tips2);
            } else {
                CodeUtils.isLightEnable(true);
                is_open_lamp = true;
                open_lamp.setText(R.string.scan_qr_code_tips3);
            }
        });
    }

    private void gotoDICM() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(getPath(uri), analyzeCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getPath(Uri uri) {
        String picturePath = "";
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, filePathColumn, null,
                null, null);// 从系统表中查询指定Uri对应的照片
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex); // 获取照片路径
            cursor.close();
        } else {
            picturePath = uri.getPath();
        }
        if (picturePath != null
                && (picturePath.endsWith(".jpg")
                || picturePath.endsWith(".JPG")
                || picturePath.endsWith(".png")
                || picturePath.endsWith(".PNG")
                || picturePath.endsWith(".JPEG") || picturePath
                .endsWith(".jpeg"))) {
        } else {
            showToast(R.string.choose_err_tips);
            return "";
        }
        return picturePath;
    }

    @Override
    protected void initData() {
        title.setText(R.string.scan_qr_code_title);
        complete_btn.setText(R.string.scan_qr_code_tips);
        complete_btn.setVisibility(View.GONE);
        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.fragment_zxing);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            showToast("解析结果:"+result);
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            ZxingActivity.this.setResult(RESULT_OK, resultIntent);
            ZxingActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            ZxingActivity.this.setResult(RESULT_OK, resultIntent);
            ZxingActivity.this.finish();
        }
    };
}
