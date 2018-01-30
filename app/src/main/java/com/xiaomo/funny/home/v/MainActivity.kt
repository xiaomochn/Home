package com.xiaomo.funny.home.v

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.data.JPushLocalNotification
import com.code19.library.DeviceUtils
import com.xiaomo.funny.home.Logger
import com.xiaomo.funny.home.R
import com.xiaomo.funny.home.p.ContionPres
import com.xiaomo.funny.home.service.Xserves
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity(), IContionView {
    override fun getStatusTextView(): TextView {
        return message
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var mContionPres: ContionPres? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JPushInterface.setDebugMode(true)    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(application)
        val registrationID = JPushInterface.getRegistrationID(application)
        val appId = DeviceUtils.getAndroidID(application)
        JPushInterface.setAlias(application, Date().time.toInt(), appId)
        Logger.d("mess", appId + " regis: " + registrationID)

        text.text = appId + " regis: " + registrationID
        text.setOnClickListener({ fffff() })
        Xserves.startService(applicationContext)
        mContionPres = ContionPres(this)
        topPanel.setOnClickListener({ startActivity(Intent(this, X4Activity::class.java)) })
        topPanel3.setOnClickListener({ startActivity(Intent(this, WXActivity::class.java)) })

    }

    fun fffff() {
        val ln = JPushLocalNotification()
        ln.builderId = 0
        ln.content = "hhh"
        ln.title = "ln"
        ln.notificationId = 11111111
        ln.broadcastTime = System.currentTimeMillis() + 100

        val map = HashMap<String, Any>()
        map.put("name", "jpush")
        map.put("test", "111")
        val json = JSONObject(map)
        ln.extras = json.toString()
        JPushInterface.addLocalNotification(applicationContext, ln)
    }

    override fun onDestroy() {
        super.onDestroy()
        mContionPres?.onDestory()
    }
}
