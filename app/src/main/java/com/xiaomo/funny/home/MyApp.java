package com.xiaomo.funny.home;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.avos.avoscloud.AVOSCloud;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.hwangjr.rxbus.Bus;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.xiaomo.funny.home.weex.bll.common.module.DialogModule;
import com.xiaomo.funny.home.weex.bll.common.module.XBusinessLauncherModule;
import com.xiaomo.funny.home.weex.extend.adapter.FrescoImageAdapter;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

public class MyApp extends Application {

    private static MyApp context;

    public static MyApp getInstance() {
        return context;
    }
    // 需要将CH34x的驱动类写在APP类下面，使得帮助类的生命周期与整个应用程序的生命周期是相同的

    public static CH34xUARTDriver driver;

    private static Bus sBus;

    public static synchronized Bus getBus() {
        if (sBus == null) {
            sBus = new Bus();
        }
        return sBus;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        MultiDex.install(this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        InitConfig config = new InitConfig.Builder().setImgAdapter(new FrescoImageAdapter()).build();
        WXSDKEngine.initialize(this, config);
        Fresco.initialize(this);
        try {
            WXSDKEngine.registerModule("businessLauncher", XBusinessLauncherModule.class);
            WXSDKEngine.registerModule("dialog", DialogModule.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
        AVOSCloud.initialize(this, "NPPHU9xCMP2IRkkACQNOjt7f-gzGzoHsz", "0PC4OzV7L9vD7SnMbCxnNqSL");
        AVOSCloud.setDebugLogEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
