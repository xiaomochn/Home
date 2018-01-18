package com.lakala.peopleSearch.activity.common.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.lakala.foundation.util.LogUtil;
import com.lakala.peopleSearch.bll.common.BusinessLauncher;
import com.lakala.platform.activity.LoginActivity;
import com.lakala.platform.bean.User;
import com.lakala.platform.common.ApplicationEx;
import com.lakala.platform.common.LakalaConstant;
import com.lakala.platform.weex.WeexActivity;
import com.lakala.platform.weex.extend.AbstractWeexActivity;
import com.lakala.platform.weex.extend.util.DataFormatUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zangrui on 2016/12/23.
 */

public class LKLBusinessLauncherModule extends WXModule {
    /**
     * 得到result返回的数据key
     */
    public static final String RESULT_DATA_KEY = "result_bundle";

    /**
     * 请求码
     */
    private int REQUEST_CODE = 90;
    private JSCallback callbackId;

    private Activity getCurrentActivity(){
        Activity activity = null;
        Context context = mWXSDKInstance.getContext();
        if (context instanceof AbstractWeexActivity){
            activity = (Activity)context;
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
////                temp.putExtra(LakalaConstant.BUSINESS_TYPE_KEY, action);
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
//                authIntent.putExtra(LakalaConstant.BUSINESS_TYPE_KEY,action);
//                _this.startActivity(authIntent);
//
//            }else {
//
//                Intent intent = new Intent(_this, WeexActivity.class);
//                String action = "weex:" + url;
//                intent.putExtra(LakalaConstant.BUSINESS_TYPE_KEY,action);
//
//                _this.startActivity(intent);
//
//            }
//        }
//    }

    @JSMethod
    public void openURL(String url,String param, String isChangeNavImage, String isRequireLogin, String isRequireRealName){
        Activity _this = getCurrentActivity();

        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(param)){
            try{
                param = URLDecoder.decode(param,"utf-8");
                JSONObject object = new JSONObject(param);
                if (object != null){
                    bundle.putString("data",object.toString());
                }
            }catch (Exception ex){
                LogUtil.print(ex.getMessage());
            }
        }

        if (!TextUtils.isEmpty(url)){

            //未登录，需要登录
            if ("true".equals(isRequireLogin) && !ApplicationEx.getInstance().getSession().isUserLogin()){

//                Intent temp = new Intent(_this, WeexActivity.class);
//                String action = "weex:login.login";
//                temp.putExtra(LakalaConstant.BUSINESS_TYPE_KEY, action);
//                _this.startActivity(temp);
                Intent temp = new Intent(_this, LoginActivity.class);
                temp.putExtra(LakalaConstant.BUSINESS_NAV_BAR, true);
                _this.startActivity(temp);

            }else if("true".equals(isRequireRealName) && null != ApplicationEx.getInstance().getSession().getUser().getUserFlag() &&
                    !"".equals(ApplicationEx.getInstance().getSession().getUser().getUserFlag()) &&
                    !User.AUTH_PASS_USER_FLAG.equals(ApplicationEx.getInstance().getSession().getUser().getUserFlag())){
                //未实名通过

                Intent authIntent = new Intent(_this, WeexActivity.class);
                String action = "weex:faceRecognition.idAuthorization";
                authIntent.putExtra(LakalaConstant.BUSINESS_TYPE_KEY,action);
                _this.startActivity(authIntent);

            }else {

                Intent intent = new Intent(_this, WeexActivity.class);
                String action = "weex:" + url;
                intent.putExtra(LakalaConstant.BUSINESS_TYPE_KEY, action);
                intent.putExtra(LakalaConstant.BUSINESS_BUNDLE_KEY, bundle);

                /**
                 *  false 为蓝色  true为白色
                 */
                if("true".equals(isChangeNavImage)){
                    intent.putExtra(LakalaConstant.BUSINESS_NAV_BAR, true);
                }else{
                    intent.putExtra(LakalaConstant.BUSINESS_NAV_BAR, false);
                }
                _this.startActivity(intent);

            }
        }
    }

    @JSMethod
    public void startPage(String param){

        if (!TextUtils.isEmpty(param)){
            try {
                param = URLDecoder.decode(param,"utf-8");
                JSONObject object = new JSONObject(param);
                String id = object.optString("tag");
                String data = object.optString("param");
                boolean isFinish = object.optBoolean("isFinishSelf");

                Bundle bundle = new Bundle();
                if (!TextUtils.isEmpty(data)){
                    bundle.putString("data",data);
                }
                BusinessLauncher.launch(mWXSDKInstance.getContext(),id,bundle);
                if (isFinish){
                    if (getCurrentActivity() != null){
                        getCurrentActivity().finish();
                    }
                }
            } catch (Exception e) {
                LogUtil.print(e.getMessage());
            }

        }
    }

    @JSMethod
    public void startPageForResult(String param, JSCallback callback){

         if (!TextUtils.isEmpty(param)){
            try{
                this.callbackId = callback;

                param = URLDecoder.decode(param,"utf-8");
                JSONObject object = new JSONObject(param);
                String id = object.optString("tag");
                String data = object.optString("param");

                Bundle bundle = new Bundle();
                if (!TextUtils.isEmpty(data)){
                    bundle.putString("data",data);
                }
                BusinessLauncher.launchForResult(getCurrentActivity(),id,bundle,REQUEST_CODE);

            } catch (Exception ex){
                LogUtil.print(ex.getMessage());
            }
        }
    }

    @JSMethod
    public void setResultAndFinishSelf(String param){

        if (!TextUtils.isEmpty(param)){
            try {
                Intent resIntent = new Intent();
                resIntent.putExtra(RESULT_DATA_KEY, param);
                ((Activity)mWXSDKInstance.getContext()).setResult(Activity.RESULT_OK, resIntent);
                ((Activity)mWXSDKInstance.getContext()).finish();

            } catch (Exception ex) {
                LogUtil.print(ex.getMessage());
            }
        }
    }

    @JSMethod
    public void finishSelf(){
        ((Activity)mWXSDKInstance.getContext()).finish();
    }

    @JSMethod
    public void getParams(JSCallback callback){
        this.callbackId = callback;
        Map<String, Object> retData = new HashMap();
        String data = "{}";

        AbstractWeexActivity activity = ((AbstractWeexActivity)mWXSDKInstance.getContext());

        //增加非空判断
        if(activity != null){
            Intent intent = activity.getIntent();
            Bundle bundle = intent.getBundleExtra(LakalaConstant.BUSINESS_BUNDLE_KEY);
            if (bundle != null){
                data = bundle.getString("data");
            }
        }

        try {
            JSONObject json = new JSONObject(data);
            Iterator it = json.keys();
            while(it.hasNext()){
                String key = (String)it.next();
                String value = json.optString(key);
                retData.put(key, value);
                this.callbackId.invoke(retData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            callback.invoke(data);
        }
    }

    @JSMethod
    public void returnToHome(){
        ApplicationEx.getInstance().returnToHome();
    }

    @JSMethod
    public void goBackTo(int level){
        ApplicationEx.getInstance().goBackTo(level);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE){
            String dataString = data == null ? "{}" : data.getStringExtra(RESULT_DATA_KEY);
            try {
                JSONObject json = new JSONObject(dataString);
                Map<String, Object> res = DataFormatUtil.jsonToMap(json);
                this.callbackId.invoke(res);

            } catch (JSONException e) {
                e.printStackTrace();
                this.callbackId.invoke(dataString);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
