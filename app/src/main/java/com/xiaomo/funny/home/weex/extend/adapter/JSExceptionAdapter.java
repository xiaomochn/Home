package com.xiaomo.funny.home.weex.extend.adapter;

import com.taobao.weex.adapter.IWXJSExceptionAdapter;
import com.taobao.weex.common.WXJSExceptionInfo;
import com.taobao.weex.utils.WXLogUtils;

/**
 * Created by xiaofeng on 17/12/4.
 */

public class JSExceptionAdapter implements IWXJSExceptionAdapter {
    @Override
    public void onJSException(WXJSExceptionInfo exception) {
        if (exception != null) {
            WXLogUtils.d(exception.toString());
        }
    }
}
