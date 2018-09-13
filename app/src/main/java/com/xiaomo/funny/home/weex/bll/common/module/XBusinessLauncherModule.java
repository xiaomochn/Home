package com.xiaomo.funny.home.weex.bll.common.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.code19.library.DeviceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.xiaomo.funny.home.MyApp;
import com.xiaomo.funny.home.XConstant;
import com.xiaomo.funny.home.model.UserModel;
import com.xiaomo.funny.home.activity.WXActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 业务跟名字对不上,提供给js调用本地方法
 *
 * @author qiao
 */
public class XBusinessLauncherModule extends WXModule {

    private Activity getCurrentActivity() {
        Activity activity = null;
        Context context = mWXSDKInstance.getContext();
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        return activity;
    }


    @JSMethod
    public void openURL(String url, String param) {
        Activity _this = getCurrentActivity();
        Intent intent = new Intent(_this, WXActivity.class);
        String action = "weex:" + url;
        intent.putExtra("url", url);
        intent.putExtra("param", param);
        intent.putExtra(XConstant.BUSINESS_TYPE_KEY, action);
        _this.startActivity(intent);

    }

    /**
     * 获取字符串
     */
    // TODO: 24/02/2018  暂时用共享参数,之后得改成数据库或者本地文件存储
    @JSMethod
    public void getString(JSCallback callbackId, String k, String fileName) {
        if (fileName == null) {
            fileName = "default";
        }
        if (callbackId != null) {
            callbackId.invoke(getString(mWXSDKInstance.getContext(), k, fileName));
        }

    }

    /**
     * 存储字符串
     */
    @JSMethod
    public void WriteStr2Port(String commond) {
        byte[] to_send = commond.getBytes();
        if (MyApp.driver != null) {
            MyApp.driver.WriteData(to_send, to_send.length);
        }
    }


    /**
     * 弹出当前acitvity
     */
    @JSMethod
    public void onBackClick() {
        getCurrentActivity().finish();
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
        callbackId.invoke(getDeviceId(mWXSDKInstance.getContext()));

    }

    public static String getDeviceId(Context context) {
        return DeviceUtils.getAndroidID(context);
    }

    @JSMethod
    public void getDeviceName(JSCallback callbackId) {
        callbackId.invoke(DeviceUtils.getDevice());

    }

    @JSMethod
    public void sendMessageToid(String uid, String message) {
        sendMessageToJerryFromTom(mWXSDKInstance.getContext(), uid, message);
    }

    /**
     * 给微信客户端发消息,当前设备用设备id做名字
     */

    public static void sendMessageToJerryFromTom(Context context, final String uid, final String message) {
        // Tom 用自己的名字作为clientId，获取AVIMClient对象实例
        AVIMClient tom = AVIMClient.getInstance("devices" + getDeviceId(context));
        // 与服务器连接
        tom.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    // 创建与Jerry之间的对话
                    client.createConversation(Arrays.asList(uid), uid, null,
                            new AVIMConversationCreatedCallback() {

                                @Override
                                public void done(AVIMConversation conversation, AVIMException e) {
                                    if (e == null) {
                                        AVIMTextMessage msg = new AVIMTextMessage();
                                        msg.setText(message);
                                        // 发送消息
                                        conversation.sendMessage(msg, new AVIMConversationCallback() {

                                            @Override
                                            public void done(AVIMException e) {
                                                if (e == null) {
                                                    Log.d("Tom & Jerry", "发送成功！");
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });
    }


}
