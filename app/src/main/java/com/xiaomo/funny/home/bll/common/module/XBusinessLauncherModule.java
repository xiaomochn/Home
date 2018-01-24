package com.xiaomo.funny.home.bll.common.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.code19.library.DeviceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.xiaomo.funny.home.bll.common.XConstant;
import com.xiaomo.funny.home.model.UserModel;
import com.xiaomo.funny.home.weex.extend.WXActivity;

import java.util.ArrayList;

/**
 * Created by zangrui on 2016/12/23.
 */

public class XBusinessLauncherModule extends WXModule {
    /**
     * 得到result返回的数据key
     */
    public static final String RESULT_DATA_KEY = "result_bundle";

    /**
     * 请求码
     */
    private int REQUEST_CODE = 90;
    private JSCallback callbackId;

    private Activity getCurrentActivity() {
        Activity activity = null;
        Context context = mWXSDKInstance.getContext();
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        return activity;
    }

//    @JSMethod
//    public void openURL(String url, String isRequireLogin, String isRequireRealName){
//        Activity _this = getCurrentActivity();
//
//        if (!TextUtils.isEmpty(url)){
//
//            //未登录，需要登录
//            if ("true".equals(isRequireLogin) && !ApplicationEx.getInstance().getSession().isUserLogin()){
//
////                Intent temp = new Intent(_this, WeexActivity.class);
////                String action = "weex:login.login";
////                temp.putExtra(XConstant.BUSINESS_TYPE_KEY, action);
////                _this.startActivity(temp);
//
//                Intent temp = new Intent(_this, LoginActivity.class);
//                _this.startActivity(temp);
//
//            }else if("true".equals(isRequireRealName) && null != ApplicationEx.getInstance().getSession().getUser().getUserFlag() &&
//                    !"".equals(ApplicationEx.getInstance().getSession().getUser().getUserFlag()) &&
//                    !User.AUTH_PASS_USER_FLAG.equals(ApplicationEx.getInstance().getSession().getUser().getUserFlag())){
//                //未实名通过
//
//                Intent authIntent = new Intent(_this, WeexActivity.class);
//                String action = "weex:faceRecognition.idAuthorization";
//                authIntent.putExtra(XConstant.BUSINESS_TYPE_KEY,action);
//                _this.startActivity(authIntent);
//
//            }else {
//
//                Intent intent = new Intent(_this, WeexActivity.class);
//                String action = "weex:" + url;
//                intent.putExtra(XConstant.BUSINESS_TYPE_KEY,action);
//
//                _this.startActivity(intent);
//
//            }
//        }
//    }

    @JSMethod
    public void openURL(String url, String param, String isChangeNavImage, String isRequireLogin, String isRequireRealName) {
        Activity _this = getCurrentActivity();
        Intent intent = new Intent(_this, WXActivity.class);
        String action = "weex:" + url;
        intent.putExtra("url", url);
        intent.putExtra(XConstant.BUSINESS_TYPE_KEY, action);
//        intent.putExtra(XConstant.BUSINESS_BUNDLE_KEY, bundle);

//        /**
//         *  false 为蓝色  true为白色
//         */
//        if("true".equals(isChangeNavImage)){
//            intent.putExtra(XConstant.BUSINESS_NAV_BAR, true);
//        }else{
//            intent.putExtra(XConstant.BUSINESS_NAV_BAR, false);
//        }
        _this.startActivity(intent);

    }


    @JSMethod
    public void getString(JSCallback callbackId, String k, String fileName) {
        if (fileName == null) fileName = "default";
        if (callbackId != null) {
            callbackId.invoke(getString(mWXSDKInstance.getContext(), k, fileName));
        }

    }


    public static String getString(Context context, String k, String fileName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(k, "");
    }

    @JSMethod
    public void setString(String k, String v) {
        setString(k, "default");
    }

    @JSMethod
    public void setString(String k, String v, String fileName) {
        setString(getCurrentActivity().getApplicationContext(), k, v, fileName);
    }

    public static void setString(Context context, String k, String v, String fileName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(k, v).commit();
        return;
    }

    @JSMethod
    public void addUser(String userId, String userNickname, int userWeight) {
        Log.d("addUser", "addUser: " + userNickname);
        if (userId == null) {
            return;
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mWXSDKInstance.getContext());
        ArrayList<UserModel> list = null;
        try {
            list = new Gson().fromJson(sp.getString("userlist", ""), new TypeToken<ArrayList<UserModel>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        UserModel newUser = new UserModel(userId, userNickname, userWeight);

        boolean haveUser = false;
        for (UserModel userModel : list) {
            if (userId.equals(userModel.getUserId())) {
                haveUser = true;
                break;
            }
        }
        if (!haveUser) {
            list.add(newUser);
        }
        sp.edit().putString("userlist", new Gson().toJson(list)).commit();
    }

    @JSMethod
    public void deleteUser(String userId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mWXSDKInstance.getContext());
        ArrayList<UserModel> list = null;
        try {
            list = new Gson().fromJson(sp.getString("userlist", ""), new TypeToken<ArrayList<UserModel>>() {
            }.getType());
            for (int i = 0; i < list.size(); i++) {
                if (userId.equals(list.get(i).getUserId())) {
                    list.remove(i);
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if (list != null)
            sp.edit().putString("userlist", new Gson().toJson(list)).commit();
    }

    @JSMethod
    public void getUserList(JSCallback callbackId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mWXSDKInstance.getContext());
        callbackId.invoke(sp.getString("userlist", ""));
    }

    @JSMethod
    public void getDeviceId(JSCallback callbackId) {
        callbackId.invoke(DeviceUtils.getAndroidID(mWXSDKInstance.getContext()));

    }

    @JSMethod
    public void getDeviceName(JSCallback callbackId) {
        callbackId.invoke(DeviceUtils.getDevice());

    }


}
