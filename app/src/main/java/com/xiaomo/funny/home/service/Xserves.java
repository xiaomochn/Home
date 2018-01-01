package com.xiaomo.funny.home.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.xiaomo.funny.home.application.MyApp;
import com.xiaomo.funny.home.model.Event;
import com.xiaomo.funny.home.model.UserDao;

/**
 * Created by xiaomochn on 22/12/2017.
 */

public class Xserves extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyApp.getBus().register(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        MyApp.getBus().unregister(this);
        super.onDestroy();
    }

    @Subscribe(
            thread = EventThread.IO
    )
    public void eatMore(Event event) {

    }
}
