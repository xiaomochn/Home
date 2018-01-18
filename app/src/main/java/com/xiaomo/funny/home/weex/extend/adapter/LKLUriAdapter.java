package com.xiaomo.funny.home.weex.extend.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.adapter.URIAdapter;

/**
 * Created by zangrui on 2017/1/16.
 */

public class LKLUriAdapter implements URIAdapter {

    @NonNull
    @Override
    public Uri rewrite(WXSDKInstance instance, String type, Uri uri) {

        //远程连接直接返回
        if (uri.toString().startsWith("http")){
            return uri;
        }

        //base64
        if (uri.toString().contains("data:image")){
            return uri;
        }

        //处理本地连接
        String bundleUrl = instance.getBundleUrl();
        if (bundleUrl.startsWith("file://")){
            Uri assetsUri = Uri.parse("file://assets/weex/" + uri.toString());

            return assetsUri;
        }
        else if (bundleUrl.startsWith("/data/")){
            String hostPath = bundleUrl.substring(0,bundleUrl.indexOf("module"));
            Uri fileUri = Uri.parse("file://" + hostPath + uri.toString());

            return fileUri;
        }
        else if (bundleUrl.startsWith("http")){
            String hostPath = bundleUrl.substring(0,bundleUrl.indexOf("dist") + 5);
            Uri httpUri = Uri.parse(hostPath + uri.toString());

            return httpUri;
        }

        return null;
    }
}
