package com.xiaomo.funny.home.bll.common;


/**
 * 定义一些常量字段
 * Created by andy_lv on 14-2-24.
 */
public class LakalaConstant {

    /**
     * 太平洋保险基地址
     */
    public static final String TAIBAO_URL_HOST = "http://www.cpic.com.cn";
    //"http://www.cpic.com.cn";
    // 测试地址 // "http://192.168.1.101:8080";
    /**
     * 调整太平洋保险页面的intent flag
     */
    public static final int TAIPINGYANG_INSURANCE_CODE = 20000;

    //------------------- BusinessManager 用到的常量 BEGIN---------------------
    /**
     * native
     */
    public static final String BUSINESS_NATIVE = "Native";
    /**
     * webapp
     */
    public static final String BUSINESS_WEBAPP = "WebApp";
    /**
     * 收银台
     */
    public static final String BUSINESS_WEBAPP_CASHIER = "Cashier";
    /**
     * webview
     */
    public static final String BUSINESS_WEBVIEW = "WebView";

    /**
     * F7_WebView
     */
    public static final String BUSINESS_F7_WEBVIEW = "F7_WebView";

    /**
     * Weex Activity
     */
    public static final String BUSINESS_WEEX_ACTIVITY = "weex";

    /**
     * F7传递公共参数Key
     */
    public static final String BUSINESS_F7_PARAMETER = "BUSINESS_F7_PARAMETER";

    /**
     * 跳转业务时，通过intent传递数据的key
     */
    public static final String BUSINESS_BUNDLE_KEY = "BUSINESS_BUNDLE_KEY";

    /**
     * 业务类型key
     */
    public static final String BUSINESS_TYPE_KEY = "BUSINESS_TYPE_KEY";

    /**
     * 业务title
     */
    public static final String BUSINESS_TITLE_KEY = "BUSINESS_TITLE_KEY";

    /**
     * NavitaionBar 样式
     */
    public static final String BUSINESS_NAV_BAR = "BUSINESS_NAV_BAR";

    /**
     * 业务requestCode
     */
    public static final String BUSINESS_REQUEST_CODE_KEY = "BUSINESS_REQUEST_CODE_KEY";

    /**
     * 业务跳转的action（动态获取的）
     */
    public static final String BUSINESS_ACTION = "action";

    /**
     * 业务加载的Url
     */
    public static final String BUSINESS_URL = "url";

    /**
     * 第三方业务传递url的key
     */
    public static final String THIRD_PARTY_URL = "url";

    /**
     * 第三方业务传递html content的key
     */
    public static final String THIRD_PARTY_CONTENT = "content";

    /**
     * 第三方业务传导航栏title 的key
     */
    public static final String THIRD_PARTY_TITLE = "title";

    /**
     * 第三方法业务加载的baseurl
     */
    public static final String THIRD_PARTY_BASE_URL = "base_url";

    /**
     * 协议title
     */
    public static final String PROTOCAL_TITLE = "title";

    /**
     * 协议URL
     */
    public static final String PROTOCAL_URL = "url";



    //------------------- BusinessManager 用到的常量 END ---------------------


    // 调用拉卡拉客户端action
    public static final String LAKALA_MAIN_ACTION = "com.koalaCredit.action.main";

}