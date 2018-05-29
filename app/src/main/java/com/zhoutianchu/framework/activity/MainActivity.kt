package com.zhoutianchu.framework.activity

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.zhoutianchu.framework.R
import com.zhoutianchu.framework.adapter.TestAdapter
import com.zhoutianchu.framework.intf.PermissionListener
import com.zhoutianchu.framework.utils.GlideUtil
import com.zhoutianchu.framework.utils.LogUtil
import com.zhoutianchu.framework.utils.SecurityUtil
import com.zhoutianchu.framework.view.GlideImageLoader
import com.zhoutianchu.framework.view.SwipeItemLayout
import com.zhoutianchu.framework.view.banner.Banner
import com.zhoutianchu.framework.view.banner.loader.ImageLoader
import com.zhoutianchu.framework.view.spanner.NiceSpinner

import org.greenrobot.eventbus.EventBus

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.ArrayList
import java.util.Arrays
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun setLayout() {
        setContentView(R.layout.activity_main)
    }

    override fun addAction() {
        throttleFirst(btn_test).subscribe { v -> GestureLockActivity.startActivity(this, 0) }
        throttleFirst(btn_test1).subscribe { v -> GestureLockActivity.startActivity(this, 1) }
        throttleFirst(btn_test2).subscribe { v -> GestureLockActivity.startActivity(this, 2) }
        throttleFirst(btn_random).subscribe { v -> generateRandom() }
        throttleFirst(btn_3).subscribe { v -> gotoZxingActivity() }
        throttleFirst(btn_koltin).subscribe{v->startActivity(Intent(this,KoltinActivity::class.java))}
        throttleFirst(btn_choose).subscribe { v ->
            val intent = Intent(this, ImageGridActivity::class.java)
            startActivityForResult(intent, BaseActivity.IMAGE_PICKER)
        }
        throttleFirst(btn_lottie).subscribe { v -> startActivity(Intent(this, LottieActivity::class.java)) }
    }

    override fun initData() {
        val images = ArrayList<String>()
        images.add("https://goss4.vcg.com/creative/vcg/800/version23/VCG41536246680.jpg")
        images.add("https://goss4.vcg.com/creative/vcg/800/version23/VCG41536246680.jpg")
        images.add("https://goss4.vcg.com/creative/vcg/800/version23/VCG41536246680.jpg")
        banner.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                GlideUtil.loadImage(context as Activity, imageView, path as String)
            }
        })
        banner.setImages(images)
        banner.start()
        GlideUtil.loadCircleImage(this, img_head, "http://static.cucatech.com/ddxl/images/zb.jpg")

        val strings = Arrays.asList("选项1", "选项2", "选项3", "选项4")
        nice_spanner.attachDataSource(strings)

        val adapter = TestAdapter(this, strings)
        list.setLayoutManager(LinearLayoutManager(this))
        list.setAdapter(adapter)
        list.addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(this))
        init()

        val path = ""
        Observable.just(path).observeOn(Schedulers.io()).map<File>({ File(it) }).observeOn(Schedulers.io()).map { file ->
            val reader = BufferedReader(FileReader(file))
            val result = reader.readLine()
            reader.close()
            result
        }.observeOn(AndroidSchedulers.mainThread()).subscribe({ result -> txt_random.setText(result) }) { err -> txt_random.setText("err:" + err.message) }
    }

    private fun generateRandom() {
        val data = "测试 中文+\n+加密"
        sendNotification()
        try {
            val results = SecurityUtil.encode(data, "3JC0m0ezJzQ=")
            LogUtil.debug("数据加密结果:" + results[0])
            LogUtil.debug("随机数加密结果:" + results[1])
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun gotoZxingActivity() {
        val permissions = arrayOf(Manifest.permission.CAMERA)
        requestPermissions(permissions, object : PermissionListener {
            override fun onGranted() {
                startActivity(Intent(this@MainActivity, ZxingActivity::class.java))
            }

            override fun onDenied(deniedPermissions: List<String>) {
                showToast("请打开手机的相机权限")
            }
        })
    }

    private fun sendNotification() {
        val intent = Intent(this, ZxingActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = Notification.Builder(this)
        builder.setContentTitle("测试标题")//设置通知栏标题
                .setContentText("测试内容") //设置通知栏显示内容
                .setContentIntent(pIntent) //设置通知栏点击意图
                //  .setNumber(number) //设置通知集合的数量
                .setTicker("测试通知来啦") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.arrow)//设置通知小ICON
        manager.notify(1, builder.build())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == BaseActivity.IMAGE_PICKER) {
                val images = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>
                val buffer = StringBuffer()
                for (item in images) {
                    LogUtil.debug(item.path)
                    buffer.append(item.path)
                    buffer.append("\n")
                }
                showToast(buffer.toString())
            } else {
                showToast("未选择")
            }
        }
    }

    private fun init() {
        val imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader = GlideImageLoader()   //设置图片加载器
        imagePicker.isShowCamera = true  //显示拍照按钮
        imagePicker.isCrop = true        //允许裁剪（单选才有效）
        imagePicker.isSaveRectangle = true //是否按矩形区域保存
        imagePicker.selectLimit = 9    //选中数量限制
        imagePicker.style = CropImageView.Style.CIRCLE  //裁剪框的形状
        imagePicker.focusWidth = 800   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.focusHeight = 800  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.outPutX = 1000//保存文件的宽度。单位像素
        imagePicker.outPutY = 1000//保存文件的高度。单位像素
        imagePicker.isMultiMode = true
    }
}
