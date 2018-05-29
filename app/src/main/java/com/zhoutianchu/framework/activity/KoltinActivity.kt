package com.zhoutianchu.framework.activity

import com.zhoutianchu.framework.R
import com.zhoutianchu.framework.bean.base.HttpReq
import com.zhoutianchu.framework.bean.base.Message
import com.zhoutianchu.framework.bean.req.PersonReq
import com.zhoutianchu.framework.network.RetrofitClient
import com.zhoutianchu.framework.utils.LogUtil
import kotlinx.android.synthetic.main.activity_koltin.*


/**
 * Created by zhout on 2018/5/28.
 */

class KoltinActivity : BaseActivity() {

    override fun setLayout() {
        setContentView(R.layout.activity_koltin)
    }

    override fun addAction() {
        throttleFirst(btn_confirm).subscribe{
            RetrofitClient.getInstance(this).showProgressDialog().apiService.getPerson(HttpReq(PersonReq("age"))).compose(resp_filter()).subscribe({
                LogUtil.debug("code is:${it.msgCd};msgInfo is:${it.msgInfo}");
            })
        }

        throttleFirst(btn_exit).subscribe{
            sendEvent(Message<String>().setData("退出事件").setMsg_id(Message.MsgId.EXIT));
        }
    }

    override fun initData() {
        showToast("this is koltin language")
        var list = method.invoke()
    }

    override fun onResume() {
        super.onResume()
    }

    val method: () -> List<String> = {
        ->
        var list:MutableList<String> =ArrayList<String>()
        for(i in 1..10){
            list.add(i.toString())
        }
        list
    }

}
