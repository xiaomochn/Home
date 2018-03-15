package com.xiaomo.funny.home.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.taobao.weex.IWXRenderListener
import com.taobao.weex.WXSDKInstance
import com.taobao.weex.common.WXRenderStrategy
import com.xiaomo.funny.home.R
import com.xiaomo.funny.home.MyApp
import com.xiaomo.funny.home.model.OtgDataModel
import com.xiaomo.funny.home.model.EventModel
import com.xiaomo.funny.home.model.UserModel
import com.xiaomo.funny.home.util.ScreenUtil
import java.util.*

/**
 * weex页面
 * */
class WXActivity : AppCompatActivity(), IWXRenderListener {
    internal var mWXSDKInstance: WXSDKInstance? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wx)
//        setStatusBar()
        mWXSDKInstance = WXSDKInstance(this)
        mWXSDKInstance!!.registerRenderListener(this)
        /**
         * WXSample 可以替换成自定义的字符串，针对埋点有效。
         * template 是.we transform 后的 js文件。
         * option 可以为空，或者通过option传入 js需要的参数。例如bundle js的地址等。
         * jsonInitData 可以为空。
         * width 为-1 默认全屏，可以自己定制。
         * height =-1 默认全屏，可以自己定制。
         */
        val isDebut = true
        if (!isDebut) {
            //本地文件路径，读取代码片段
//            var bundleUrl = ""
//            if (PropertiesUtil.isLoadAssetsWebapp()) {
//                //Load Assets
//                val assetsPath = WEEX_INDEX_PATH + anchor
//                bundleUrl = "file://assets/$assetsPath.js"
//                if (assetsPath != null) {
//                    sourceTemplate = WXFileUtils.loadAsset(assetsPath!! + ".js", this)
//                }
//            } else {
//                //Load Files Data
//                try {
//                    val absPath = filesDir.absolutePath + LOCAL_FILE_PATH + anchor + ".js"
//                    bundleUrl = absPath
//                    sourceTemplate = FileUtil.readFile(absPath)
//                } catch (ex: IOException) {
//                    LogUtil.print("zrzrzr", ex.message)
//                }
//
//            }
//
//            if (sourceTemplate.length > 0) {
//                renderPage(sourceTemplate, bundleUrl, data)
//                getInstance().setBundleUrl(bundleUrl)
//            }
        } else {
            //远程路径
            var path = intent?.extras?.getString("url")
            var host = "http://10.5.6.245:8081/"
//            val host = "http://192.168.1.8:8081/"

//            val host = "http://oqgi5s4fg.bkt.clouddn.com/homevue/"\
            if (MyApp.getInstance().isDebug) {
                host = "http://10.5.6.245:8081/"
            } else {
                host = "http://oqgi5s4fg.bkt.clouddn.com/homevue/"
            }
//            val url = host + "dist/index.js"
            if (path == null) {
                path = "module/home"
            }
            val url = host + "dist/" + path + ".js"

//            val url = "file://assets/dist/" + path + ".js"
            renderPageByURL(url, null)
        }
    }

    protected fun renderPageByURL(url: String, jsonInitData: String?) {

        val options = HashMap<String, Any>()
        options.put(WXSDKInstance.BUNDLE_URL, url)
        mWXSDKInstance?.renderByUrl(
                "WXSample",
                url,
                options,
                jsonInitData,
                ScreenUtil.getDisplayWidth(this),
                ScreenUtil.getDisplayHeight(this),
                WXRenderStrategy.APPEND_ASYNC)
    }

    override fun onViewCreated(instance: WXSDKInstance, view: View) {
        setContentView(view)
    }

    override fun onRenderSuccess(instance: WXSDKInstance, width: Int, height: Int) {}
    override fun onRefreshSuccess(instance: WXSDKInstance, width: Int, height: Int) {}
    override fun onException(instance: WXSDKInstance, errCode: String, msg: String) {}
    override fun onResume() {
        super.onResume()
        if (mWXSDKInstance != null) {
            mWXSDKInstance!!.onActivityResume()
        }
        MyApp.getBus().register(this)
    }

    override fun onPause() {
        super.onPause()
        if (mWXSDKInstance != null) {
            mWXSDKInstance!!.onActivityPause()
        }
        MyApp.getBus().unregister(this)
    }

    override fun onStop() {
        super.onStop()
        if (mWXSDKInstance != null) {
            mWXSDKInstance!!.onActivityStop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mWXSDKInstance != null) {
            mWXSDKInstance!!.onActivityDestroy()
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun onNewUserEvent(userModel: UserModel) {
        val params = HashMap<String, Any>()
        params.put("userNickname", userModel.userNickname)
        params.put("userId", userModel.userId)
        mWXSDKInstance?.fireGlobalEventCallback("onnewuser", params)

    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun onReadPortEvent(userModel: OtgDataModel) {
        userModel.commond?.let {
            val params = HashMap<String, Any>()
            params.put("commond", it)
            mWXSDKInstance?.fireGlobalEventCallback("onReadPortEvent", params)
        }

    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun ongetJpush(userModel: EventModel) {
        val params = HashMap<String, Any>()
        params.put("c", userModel.c)
        params.put("d", userModel.d)
        params.put("e", userModel.e)
        params.put("f", userModel.f)
        mWXSDKInstance?.fireGlobalEventCallback("onJpushPortEvent", params)

    }
//
//    protected var useThemestatusBarColor = false//是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
//    protected var useStatusBarColor = true//是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
//
//    protected fun setStatusBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
//            val decorView = window.decorView
//            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            decorView.systemUiVisibility = option
//            //根据上面设置是否对状态栏单独设置颜色
//            if (useThemestatusBarColor) {
//                window.statusBarColor = resources.getColor(R.color.textColorPrimary)
//            } else {
//                window.statusBarColor = Color.TRANSPARENT
//            }
//
////            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
//            val localLayoutParams = window.attributes
//            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
//        }
//        if ( useStatusBarColor) {//android6.0以后可以对状态栏文字颜色和图标进行修改
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        }
//    }

}