package com.xiaomo.funny.home.weex.extend

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.taobao.weex.IWXRenderListener
import com.taobao.weex.WXSDKInstance
import com.taobao.weex.common.WXRenderStrategy
import com.xiaomo.funny.home.R
import com.xiaomo.funny.home.application.MyApp
import com.xiaomo.funny.home.model.ContionPModel
import com.xiaomo.funny.home.model.UserModel
import com.xiaomo.funny.home.weex.extend.util.ScreenUtil
import java.util.*

class WXActivity : AppCompatActivity(), IWXRenderListener {
    internal var mWXSDKInstance: WXSDKInstance? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wx)
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
            val host = "http://10.5.7.186:8081/"
//            val url = host + "dist/index.js"
            if (path == null) {
                path = "index"
            } else {
//                Thread(Runnable {
//                    var i = 0;
//                    while (true) {
//                        val params = HashMap<String, Any>()
//                        params.put("userNickname", i++)
//                        params.put("userId", "id" + i)
//                        mWXSDKInstance?.fireGlobalEventCallback("onnewuser", params)
//                        Thread.sleep(1000)
//                    }
//
//                }).start()
            }
            val url = host + "dist/" + path + ".js"
//            val url = "file://assets/dist/" + path + ".js"
            renderPageByURL(url, null)
        }
//        mWXSDKInstance!!.render("WXSample", WXFileUtils.loadFileContent("hello.js", this), null, null, -1, -1, WXRenderStrategy.APPEND_ASYNC)
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
}