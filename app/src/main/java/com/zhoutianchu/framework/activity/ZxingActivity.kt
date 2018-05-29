package com.zhoutianchu.framework.activity

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.uuzuche.lib_zxing.activity.CaptureFragment
import com.uuzuche.lib_zxing.activity.CodeUtils
import com.zhoutianchu.framework.R
import kotlinx.android.synthetic.main.activity_zxing.*
import kotlinx.android.synthetic.main.title_common.*


/**
 * Created by zhout on 2017/7/17.
 */

class ZxingActivity : BaseActivity() {

    private var is_open_lamp = false
    val REQUEST_IMAGE=1000

    /**
     * 二维码解析回调函数
     */
    internal var analyzeCallback: CodeUtils.AnalyzeCallback = object : CodeUtils.AnalyzeCallback {
        override fun onAnalyzeSuccess(mBitmap: Bitmap, result: String) {
            showToast("解析结果:$result")
            val resultIntent = Intent()
            val bundle = Bundle()
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS)
            bundle.putString(CodeUtils.RESULT_STRING, result)
            resultIntent.putExtras(bundle)
            this@ZxingActivity.setResult(Activity.RESULT_OK, resultIntent)
            this@ZxingActivity.finish()
        }

        override fun onAnalyzeFailed() {
            val resultIntent = Intent()
            val bundle = Bundle()
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED)
            bundle.putString(CodeUtils.RESULT_STRING, "")
            resultIntent.putExtras(bundle)
            this@ZxingActivity.setResult(Activity.RESULT_OK, resultIntent)
            this@ZxingActivity.finish()
        }
    }

    override fun setLayout() {
        setContentView(R.layout.activity_zxing)
    }

    override fun addAction() {
        throttleFirst(btn_back).subscribe { v -> finish() }
        throttleFirst(complete_btn).subscribe { v -> gotoDICM() }
        throttleFirst(open_lamp).subscribe { v ->
            if (is_open_lamp) {
                CodeUtils.isLightEnable(false)
                is_open_lamp = false
                open_lamp.setText(R.string.scan_qr_code_tips2)
            } else {
                CodeUtils.isLightEnable(true)
                is_open_lamp = true
                open_lamp.setText(R.string.scan_qr_code_tips3)
            }
        }
    }

    private fun gotoDICM() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                val uri = data.data
                try {
                    CodeUtils.analyzeBitmap(getPath(uri), analyzeCallback)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private fun getPath(uri: Uri?): String {
        var picturePath: String? = ""
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, filePathColumn, null, null, null)// 从系统表中查询指定Uri对应的照片
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            picturePath = cursor.getString(columnIndex) // 获取照片路径
            cursor.close()
        } else {
            picturePath = uri.path
        }
        if (picturePath != null && (picturePath.endsWith(".jpg")
                        || picturePath.endsWith(".JPG")
                        || picturePath.endsWith(".png")
                        || picturePath.endsWith(".PNG")
                        || picturePath.endsWith(".JPEG") || picturePath
                        .endsWith(".jpeg"))) {
        } else {
            showToast(R.string.choose_err_tips)
            return ""
        }
        return picturePath
    }

    override fun initData() {
        text_title.setText(R.string.scan_qr_code_title)
        complete_btn.setText(R.string.scan_qr_code_tips)
        complete_btn.setVisibility(View.GONE)
        /**
         * 执行扫面Fragment的初始化操作
         */
        val captureFragment = CaptureFragment()
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.fragment_zxing)
        captureFragment.analyzeCallback = analyzeCallback
        /**
         * 替换我们的扫描控件
         */
        supportFragmentManager.beginTransaction().replace(R.id.fl_my_container, captureFragment).commit()
    }
}
