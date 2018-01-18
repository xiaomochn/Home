package com.lakala.platform.weex.extend.module;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.lakala.foundation.util.ImageUtil;
import com.lakala.foundation.util.LogUtil;
import com.lakala.platform.common.DialogController;
import com.lakala.platform.common.photo.PhotoUtil;
import com.lakala.platform.sns.ShareActivity;
import com.lakala.ui.dialog.ActionSheetDialog;
import com.lakala.ui.dialog.AlertDialog;
import com.lakala.ui.dialog.SinglePickerDialog;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * Created by zangrui on 2017/2/17.
 */

public class DialogModule extends WXModule {

    //表示通过拍照获取图片
    private final int TAKE_PICTURE_FROM_CAMERA = 10000;
    //表示通过从相册中获取图片
    private final int TAKE_PICTURE_FROM_ALBUM = 20000;

    public static final int REQUEST_PREMISSION = 1;

    private JSCallback callback;

    private ActionSheetDialog actionSheetDialog;
    FragmentActivity _this;
    final JSONObject jsonObject = new JSONObject();

    @JSMethod
    public void getPicture(JSCallback callback) throws JSONException {

        this.callback = callback;
        String buttons = new StringBuilder().append("拍照上传").append("|").append("从手机相册选择").toString();

        final String buttonArr[] = buttons.split("\\|");

        jsonObject.put("thumbnail", "600x600");

        _this = (FragmentActivity) mWXSDKInstance.getContext();

        DialogController.getInstance().showActionSheetDialog(_this, buttonArr, new ActionSheetDialog.ActionSheetDialogClickCallback() {
            /**
             * button 点击
             *
             * @param buttonLable button's label
             * @param view        被点击的v对象
             */
            @Override
            public void click(String buttonLable, View view, ActionSheetDialog actionSheetDialog) {
                DialogModule.this.actionSheetDialog = actionSheetDialog;
                switch (buttonLable) {
                    case "拍照上传":
                        requestPermission(_this, jsonObject, actionSheetDialog, TAKE_PICTURE_FROM_CAMERA);

                        break;
                    case "从手机相册选择":

                        requestPermission(_this, jsonObject, actionSheetDialog, TAKE_PICTURE_FROM_ALBUM);

                        break;
                }
            }
        });
    }

    /**
     * 检测是否授权
     */
    private void requestPermission(FragmentActivity _this, JSONObject jsonObject, ActionSheetDialog actionSheetDialog, int type) {
        List<String> permissionsNeeded = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(_this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(_this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(_this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            permissionsNeeded.add(Manifest.permission.CAMERA);

        }
        if (permissionsNeeded.size() > 0) {

            if (type == TAKE_PICTURE_FROM_CAMERA) {
                //所有权限都同意
                ActivityCompat.requestPermissions(_this, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), TAKE_PICTURE_FROM_CAMERA);

            } else if (type == TAKE_PICTURE_FROM_ALBUM) {
                //所有权限都同意
                ActivityCompat.requestPermissions(_this, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), TAKE_PICTURE_FROM_ALBUM);
            }


        } else {
            if (type == TAKE_PICTURE_FROM_CAMERA) {
                //所有权限都同意
                PhotoUtil.getPictureFromCamera(_this, TAKE_PICTURE_FROM_CAMERA, jsonObject);
                this.actionSheetDialog.dismiss();
            } else if (type == TAKE_PICTURE_FROM_ALBUM) {
                //所有权限都同意
                PhotoUtil.getPictureFromAlbum(_this, TAKE_PICTURE_FROM_ALBUM, jsonObject);
                this.actionSheetDialog.dismiss();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        boolean allowed = true;
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //所有权限都同意

                } else {
                    allowed = false;
                    break;
                }
            }
        }

        if (allowed) {
            if (requestCode == TAKE_PICTURE_FROM_CAMERA) {
                PhotoUtil.getPictureFromCamera(_this, TAKE_PICTURE_FROM_CAMERA, jsonObject);
            }else if(requestCode == TAKE_PICTURE_FROM_ALBUM){
                PhotoUtil.getPictureFromAlbum(_this, TAKE_PICTURE_FROM_ALBUM, jsonObject);
            }
        }
    }


    @JSMethod
    public void showTwoBtnAlertDialog(String title, String message, String leftTxt, String rightTxt, final JSCallback callback) {
        final FragmentActivity _this = (FragmentActivity) mWXSDKInstance.getContext();
        DialogController.getInstance().showAlertDialog(_this, title, message, leftTxt, rightTxt, new AlertDialog.Builder.AlertDialogClickListener() {
            @Override
            public void clickCallBack(AlertDialog.Builder.ButtonTypeEnum typeEnum, AlertDialog alertDialog) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                if (typeEnum == AlertDialog.Builder.ButtonTypeEnum.LEFT_BUTTON) {
                    params.put("res", "left");
                    callback.invoke(params);

                    alertDialog.dismiss();
                } else if (typeEnum == AlertDialog.Builder.ButtonTypeEnum.RIGHT_BUTTON) {
                    params.put("res", "right");
                    callback.invoke(params);

                    alertDialog.dismiss();
                }
            }
        });
    }

    @JSMethod
    public void showOneBtnAlertDialog(String title, String message, String btnTxt, final JSCallback callback) {
        final FragmentActivity _this = (FragmentActivity) mWXSDKInstance.getContext();
        DialogController.getInstance().showAlertDialog(_this, title, message, btnTxt, new AlertDialog.Builder.AlertDialogClickListener() {
            @Override
            public void clickCallBack(AlertDialog.Builder.ButtonTypeEnum typeEnum, AlertDialog alertDialog) {
                callback.invoke(null);

                alertDialog.dismiss();
            }
        });
    }


    @JSMethod
    public void showOneBtnAlertDialogWithJupm(String title, String message, String btnTxt, final JSCallback callback, int second, final JSCallback selfCallback) {
        final boolean[] isNeedAutoJump = {true};
        final FragmentActivity _this = (FragmentActivity) mWXSDKInstance.getContext();
        DialogController.getInstance().showOneBtnAlertDialogWithJupm(_this, title, message, btnTxt, new AlertDialog.Builder.AlertDialogClickListener() {
            @Override
            public void clickCallBack(AlertDialog.Builder.ButtonTypeEnum typeEnum, AlertDialog alertDialog) {

                isNeedAutoJump[0] = false;

                if (callback != null)
                    callback.invoke(null);
                alertDialog.dismiss();
            }
        }, second + 1, new AlertDialog.Builder.AlertDialogTimerListener() {
            @Override
            public void onCallBack(AlertDialog alertDialog) {

                if(isNeedAutoJump[0]){
                    if (selfCallback != null)
                        selfCallback.invoke(null);
                }
            }
        });
    }

    @JSMethod
    public void showTwoBtnAlertDialogWithJupm(String title, String message, String leftTxt, String rightTxt, final JSCallback leftCallback, final JSCallback rightCallback, int second, final JSCallback autoJumpCallback) {
        final boolean[] isNeedAutoJump = {true};
        final FragmentActivity _this = (FragmentActivity) mWXSDKInstance.getContext();
        DialogController.getInstance().showTwoBtnAlertDialogWithJupm(_this, title, message, leftTxt, rightTxt, new AlertDialog.Builder.AlertDialogClickListener() {
            @Override
            public void clickCallBack(AlertDialog.Builder.ButtonTypeEnum typeEnum, AlertDialog alertDialog) {
                if (typeEnum == AlertDialog.Builder.ButtonTypeEnum.LEFT_BUTTON) {
                    //不需要自动跳转
                    isNeedAutoJump[0] = false;

                    if (leftCallback != null)
                        leftCallback.invoke(null);
                } else if (typeEnum == AlertDialog.Builder.ButtonTypeEnum.RIGHT_BUTTON) {
                    //不需要自动跳转
                    isNeedAutoJump[0] = false;

                    if (rightCallback != null)
                        rightCallback.invoke(null);
                }
                alertDialog.dismiss();
            }
        }, second + 1, new AlertDialog.Builder.AlertDialogTimerListener() {
            @Override
            public void onCallBack(AlertDialog alertDialog) {
                if(isNeedAutoJump[0]){
                    if (autoJumpCallback != null)
                        autoJumpCallback.invoke(null);
                }
            }
        });
    }

    @JSMethod
    public void share(String data, JSCallback callback) {
        final FragmentActivity _this = (FragmentActivity) mWXSDKInstance.getContext();

        Bitmap bitmap = new ImageUtil(_this).printScreen();
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        String fileName = "/sharePic.jpg";
        ImageUtil.saveBitmap2file(bitmap, fileName);


        //把点击事件传递给 ShareActivity
        ShareActivity.callBackWeex = callback;

        Intent intent = new Intent(_this, ShareActivity.class);
        intent.putExtra(ShareActivity.SNS_PLATFORM, data);
        if (bitmap != null) {
            intent.putExtra(ShareActivity.SNS_VIEW, fileName);
        }
        _this.startActivity(intent);

    }

    @JSMethod
    public void pick(String title, List data, final String key, final JSCallback callback) {
        this.callback = callback;
        final FragmentActivity _this = (FragmentActivity) mWXSDKInstance.getContext();

        List<String> params = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            com.alibaba.fastjson.JSONObject obj = (com.alibaba.fastjson.JSONObject) data.get(i);
            params.add(obj.getString(key));
        }

        DialogController.getInstance().showSinglePickerDialog(_this, params, title, new SinglePickerDialog.SinglePickerDialogClickCallback() {
            @Override
            public void click(String selectedTxt, int selectedIndex, SinglePickerDialog singlePickerDialog) {
                Map<String, Object> backData = new HashMap<>();
                backData.put(key, selectedTxt);
                callback.invoke(backData);
            }
        });

    }

    /**
     * 用于保存照相机处理后返回的图像数据
     */
    public static String picJsonStr = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case TAKE_PICTURE_FROM_ALBUM:
                LogUtil.print("Picture_Album:" + picJsonStr);
                callback.invoke(configBackData());
                break;
            case TAKE_PICTURE_FROM_CAMERA:
                LogUtil.print("Picture_Camera:" + picJsonStr);
                callback.invoke(configBackData());
                break;
        }
    }

    private Map<String, Object> configBackData() {
        String picBase64 = "";
        String picPath = "";
        try {
            JSONObject obj = new JSONObject(picJsonStr);
            JSONObject thumbnail = obj.optJSONObject("thumbnail");
            if (thumbnail != null) {
                picBase64 = thumbnail.optString("base64");
                picPath = thumbnail.optString("path");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, Object> backData = new HashMap<>();
        backData.put("data", picBase64);
        backData.put("path", picPath);
        backData.put("result", "OK");

        return backData;
    }

}


