package com.xiaomo.funny.home.bll.common.module;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;


/**
 * Created by zangrui on 2017/2/17.
 */

public class DialogModule extends WXModule {


    @JSMethod
    public void showTwoBtnAlertDialog(String title, String message, String leftTxt, String rightTxt, final JSCallback callback) {
        final FragmentActivity _this = (FragmentActivity) mWXSDKInstance.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(mWXSDKInstance.getContext());
        builder.setNegativeButton(leftTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("res", "left");
                callback.invoke(params);
            }
        }).setPositiveButton(rightTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("res", "right");
                callback.invoke(params);
            }
        }).setTitle(title).create().show();
    }


}


