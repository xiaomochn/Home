package com.xiaomo.funny.home.activity


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.data.JPushLocalNotification
import com.code19.library.DeviceUtils
import com.iflytek.autoupdate.IFlytekUpdate
import com.iflytek.autoupdate.UpdateConstants
import com.iflytek.autoupdate.UpdateErrorCode
import com.iflytek.autoupdate.UpdateType
import com.xiaomo.funny.home.MyApp
import com.xiaomo.funny.home.R
import com.xiaomo.funny.home.service.ReadOtgServes
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JPushInterface.init(application)
        val registrationID = JPushInterface.getRegistrationID(application)
        val appId = DeviceUtils.getAndroidID(application)
        JPushInterface.setAlias(application, Date().time.toInt(), appId)
        text.text = appId + " regis: " + registrationID
        text.setOnClickListener({ fffff() })


        topPanel.setOnClickListener({ startActivity(Intent(this, X4Activity::class.java)) })
        topPanel3.setOnClickListener({ startActivity(Intent(this, WXActivity::class.java)) })
        topPanel2.setOnClickListener({ checkUpdate() })
        isdebug.setOnClickListener({
            MyApp.getInstance().isDebug = !MyApp.getInstance().isDebug

            isdebug.setText("isDebug" + MyApp.getInstance().isDebug)

        })
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


    fun checkUpdate() {
        var updManager = IFlytekUpdate.getInstance(this)
        updManager.setParameter(UpdateConstants.EXTRA_WIFIONLY, "true")
        // 设置通知栏icon，默认使用SDK默认
        updManager.setParameter(UpdateConstants.EXTRA_NOTI_ICON, "false")
        updManager.setParameter(UpdateConstants.EXTRA_STYLE, UpdateConstants.UPDATE_UI_DIALOG)

        updManager.autoUpdate(this@MainActivity, { errorcode, result ->
            if (errorcode == UpdateErrorCode.OK && result != null) {
                if (result!!.getUpdateType() === UpdateType.NoNeed) {
                    showTip("已经是最新版本！")

                }
                updManager.showUpdateInfo(this@MainActivity, result)
            } else {
                showTip("请求更新失败！\n更新错误码：$errorcode")
            }
        })


    }

    private fun showTip(str: String) {
        runOnUiThread {
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ReadOtgServes.startService(applicationContext)
    }

}
