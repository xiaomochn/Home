package com.xiaomo.funny.home.service;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.code19.library.AppUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.thread.EventThread;
import com.orhanobut.logger.Logger;
import com.xiaomo.funny.home.R;
import com.xiaomo.funny.home.application.MyApp;
import com.xiaomo.funny.home.model.ContionPModel;
import com.xiaomo.funny.home.model.XservesModel;


import cn.wch.ch34xuartdriver.CH34xUARTDriver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by xiaomochn on 22/12/2017.
 */

public class Xserves extends Service {
    private boolean isOpen;
    private static final String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";

    public static void startService(Context context) {
        if (!AppUtils.isServiceRunning(context, Xserves.class.getName())) {
            context.startService(new Intent(context, Xserves.class));
        }else {
            MyApp.getBus().post(new XservesModel("init"));
        }


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            MyApp.getBus().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initConnection();

    }

    void initConnection() {
        MyApp.driver = new CH34xUARTDriver(
                (UsbManager) getSystemService(Context.USB_SERVICE), this,
                ACTION_USB_PERMISSION);
        Logger.d("creac");

        MyApp.getBus().post(new ContionPModel("onCreate"));
        if (!MyApp.driver.UsbFeatureSupported())// 判断系统是否支持USB HOST
        {
//            Dialog dialog = new AlertDialog.Builder(Xserves.this)
//                    .setTitle("提示")
//                    .setMessage("您的手机不支持USB HOST，请更换其他手机再试！")
//                    .setPositiveButton("确认",
//                            new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface arg0,
//                                                    int arg1) {
//                                    System.exit(0);
//                                }
//                            }).create();
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
        } else {

            if (!isOpen) {
                int retval = MyApp.driver.ResumeUsbList();
                if (retval == -1)// ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
                {
                    Toast.makeText(Xserves.this, "打开设备失败!",
                            Toast.LENGTH_SHORT).show();
                    MyApp.driver.CloseDevice();
                    try {
                        stopService(new Intent(this,Xserves.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (retval == 0) {
                    if (!MyApp.driver.UartInit()) {//对串口设备进行初始化操作
                        Toast.makeText(Xserves.this, "设备初始化失败!",
                                Toast.LENGTH_SHORT).show();
                        Toast.makeText(Xserves.this, "打开" +
                                        "设备失败!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(Xserves.this, "打开设备成功!",
                            Toast.LENGTH_SHORT).show();
                    isOpen = true;
                    new readThread().start();//开启读线程读取串口接收的数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {

                                byte[] to_send = "111".getBytes();
                                int retval = MyApp.driver.WriteData(to_send, to_send.length);

                                Observable.just("")
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<String>() {
                                            @Override
                                            public void accept(String s) throws Exception {

                                                MyApp.getBus().post(new ContionPModel("write"));

                                            }
                                        });
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Xserves.this);
                    builder.setIcon(R.drawable.icon);
                    builder.setTitle("未授权限");
                    builder.setMessage("确认退出吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
//								MainFragmentActivity.this.finish();
                            System.exit(0);
                        }
                    });
                    builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    });
                    builder.show();

                }
            } else {
                MyApp.driver.CloseDevice();

                isOpen = false;
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        MyApp.getBus().unregister(this);
        isOpen = false;
        super.onDestroy();
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD
    )
    public void eatMore(XservesModel event) {
        if ("init".equals(event.getCommond())) initConnection();
    }

    private class readThread extends Thread {

        public void run() {

            byte[] buffer = new byte[4096];

            while (true) {
                if (!isOpen) {
                    break;
                }
                int length = MyApp.driver.ReadData(buffer, 4096);
                if (length > 0) {
                    String recv = new String(buffer, 0, length);
                    Logger.d(recv);
                    io.reactivex.Observable.just("")
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    MyApp.getBus().post(new ContionPModel("read"));
                                }
                            });

                }
            }
        }
    }


}
