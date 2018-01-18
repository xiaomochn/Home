package com.xiaomo.funny.home.application;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hwangjr.rxbus.Bus;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.xiaomo.funny.home.bll.common.module.DialogModule;
import com.xiaomo.funny.home.bll.common.module.LKLBusinessLauncherModule;
import com.xiaomo.funny.home.service.Xserves;
import com.xiaomo.funny.home.weex.extend.adapter.FrescoImageAdapter;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

public class MyApp extends Application {

	private static MyApp context;
	public static MyApp getInstance() {
		return context;
	}

	public static CH34xUARTDriver driver;// 需要将CH34x的驱动类写在APP类下面，使得帮助类的生命周期与整个应用程序的生命周期是相同的

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
//		Xserves.startService(this);
		Logger.addLogAdapter(new AndroidLogAdapter());
		InitConfig config=new InitConfig.Builder().setImgAdapter(new FrescoImageAdapter()).build();
		WXSDKEngine.initialize(this,config);
		Fresco.initialize(this);
        try {
            WXSDKEngine.registerModule("businessLauncher", LKLBusinessLauncherModule.class);
            WXSDKEngine.registerModule("dialog", DialogModule.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}
}
