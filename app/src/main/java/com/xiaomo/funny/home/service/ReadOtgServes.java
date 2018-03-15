package com.xiaomo.funny.home.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.xiaomo.funny.home.MyApp;
import com.xiaomo.funny.home.model.OtgDataModel;
import com.xiaomo.funny.home.model.InitOgtModel;
import com.xiaomo.funny.home.weex.bll.common.module.XBusinessLauncherModule;


import cn.wch.ch34xuartdriver.CH34xUARTDriver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 *
 * @author xiaomochn
 * @date 22/12/2017
 */

public class ReadOtgServes extends Service {
    private boolean isOpen;
    private static final String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";

    public static void startService(Context context) {
        if (!AppUtils.isServiceRunning(context, ReadOtgServes.class.getName())) {
            context.startService(new Intent(context, ReadOtgServes.class));
        } else {
            MyApp.getBus().post(new InitOgtModel("init"));
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
        registerMyReceiver();
    }

    /**
     * Register usb receiver.
     */
    public void registerMyReceiver() {
        // Register receiver.
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        registerReceiver(mUsbReceiver, filter);
    }

    /**
     * Unregister usb receiver.
     */
    public void unregisterMyReceiver() {
        unregisterReceiver(mUsbReceiver);
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(intent.getAction())) {
                initConnection();

            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(intent.getAction())) {
                isOpen = false;
                Toast.makeText(ReadOtgServes.this,"设备已拔出",Toast.LENGTH_SHORT).show();
            }
        }
    };


    void initConnection() {
        MyApp.driver = new CH34xUARTDriver(
                (UsbManager) getSystemService(Context.USB_SERVICE), this,
                ACTION_USB_PERMISSION);
        // 判断系统是否支持USB HOST
        if (!MyApp.driver.UsbFeatureSupported()) {
//            Dialog dialog = new AlertDialog.Builder(ReadOtgServes.this)
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
                    Toast.makeText(ReadOtgServes.this, "打开设备失败!",
                            Toast.LENGTH_SHORT).show();
                    MyApp.driver.CloseDevice();
                } else if (retval == 0) {
                    //对串口设备进行初始化操作
                    if (!MyApp.driver.UartInit()) {
                        Toast.makeText(ReadOtgServes.this, "设备初始化失败!",
                                Toast.LENGTH_SHORT).show();
                        Toast.makeText(ReadOtgServes.this, "打开" +
                                        "设备失败!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ReadOtgServes.this, "打开设备成功!",
                            Toast.LENGTH_SHORT).show();
                    //配置串口波特率，函数说明可参照编程手册
                    MyApp.driver.SetConfig(115200, (byte) 8, (byte) 1, (byte) 0,
                            (byte) 0);
                    isOpen = true;
                    new ReadThread().start();//开启读线程读取串口接收的数据
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ReadOtgServes.this);
                    builder.setIcon(R.drawable.icon);
                    builder.setTitle("未授权限");
                    builder.setMessage("确认退出吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
                    builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
        unregisterMyReceiver();
        super.onDestroy();
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD
    )
    public void initConnection(InitOgtModel event) {
        if ("init".equals(event.getCommond())) {
            initConnection();
        }
    }

    private class ReadThread extends Thread {

        @Override
        public void run() {

            byte[] buffer = new byte[4096];

            while (true) {
                if (!isOpen) {
                    try {
                        Thread.sleep(3 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                int length = MyApp.driver.ReadData(buffer, 4096);
                if (length > 0) {
                    final String recv = new String(buffer, 0, length);
                    Logger.d(recv);

                    if (recv.contains("cardnum:") && MyReceiver.haveUser(ReadOtgServes.this, recv.substring(8))) {
                        byte[] toSend = "opendoor\r\n".getBytes();
                        MyApp.driver.WriteData(toSend, toSend.length);
                    }
                    if (recv.contains("statusn:")) {
                        XBusinessLauncherModule.sendMessageToJerryFromTom(ReadOtgServes.this, XBusinessLauncherModule.getDeviceId(ReadOtgServes.this), recv);
                    }
                    Observable.just("")
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    MyApp.getBus().post(new OtgDataModel(recv));
                                }
                            });

                }
            }
        }
    }


}
