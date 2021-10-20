package com.smarthome.magic.get_net;


import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.AppConfig;

import java.util.Map;
public class ApiClient {

    public static final String UTF_8 = "UTF-8";
    public static final String DESC = "descend";
    public static final String ASC = "ascend";
    public static final String appType = "2";
    private final static int TIMEOUT_CONNECTION = 30000;
    private final static int TIMEOUT_SOCKET = 30000;
    private final static int RETRY_TIME = 3;
    private static String appUserAgent;

    public static String ConsultSources = null;//咨询页面 0 文章 1 美容师/美容顾问 2 店铺
    public static String ConsultArticleUrl = "";//咨询页面文章链接

    public static String getAppUserAgent() {
        if (appUserAgent == null || appUserAgent == "") {
            appUserAgent = getUserAgent(App.getInstance());
        }
        return appUserAgent;
    }

    public static String getToken() {
        return App.getInstance().getProperty(AppConfig.CONF_ACCESSTOKEN);
    }

    private static String getUserAgent(App appContext) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imsi = mTelephonyMgr.getSubscriberId();
        @SuppressLint("MissingPermission") String imei = mTelephonyMgr.getDeviceId();
        if (appUserAgent == null || appUserAgent == "") {
            StringBuilder ua = new StringBuilder("SHENGDENGKEJI");
            ua.append(',' + android.os.Build.MODEL); //手机型号
            ua.append(',' + "1");//手机系统平台
            ua.append(',' + String.valueOf(appContext.getPackageInfo().versionName));//App版本
            ua.append(',' + String.valueOf(appContext.getPackageInfo().versionCode));
            ua.append(',' + String.valueOf(imei));
            ua.append(',' + String.valueOf(imsi));//SIM卡唯一标识
            ua.append(',' + App.DeviceId);//客户端唯一标识
            appUserAgent = ua.toString();
        }
        return appUserAgent;
    }


    private static String _MakeURL(String p_url, Map<String, Object> params) {
        StringBuilder url = new StringBuilder(p_url);
        if (url.indexOf("?") < 0)
            url.append('?');

        for (String name : params.keySet()) {
            url.append('&');
            url.append(name);
            url.append('=');
            url.append(String.valueOf(params.get(name)));
            //不做URLEncoder处理
            //url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
        }

        return url.toString().replace("?&", "?");
    }
}
