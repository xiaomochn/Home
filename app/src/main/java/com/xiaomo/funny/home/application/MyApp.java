package com.xiaomo.funny.home.application;

import android.app.Application;
import android.content.Context;

import com.hwangjr.rxbus.Bus;

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

	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
	}
}
