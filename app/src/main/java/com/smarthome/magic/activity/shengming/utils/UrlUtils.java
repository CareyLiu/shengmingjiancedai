package com.smarthome.magic.activity.shengming.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UrlUtils {
    public final static String CUSTOMERCODE = "RSD1389948105";
    public final static String YTOKEN = "fca76f960fe5ecd7447edbcc4ea799b2";
    public final static String MAC = "149C7656FFB1";
    public final static String URL = "http://iot.52soft.top/api/";

    public final static String createSession = URL + "createSession";//创建会话(认证)

    public final static String setAlarm = URL + "setAlarm";//设备预警参数配置
    public final static String getMacSleepReport = URL + "getMacSleepReport";//获取设备日体征统计报告
    public final static String registerPushMultiAddress = URL + "registerPushMultiAddress";//注册实时数据推送地址
    public final static String registerAlarmPushAddress = URL + "registerAlarmPushAddress";//注册实时预警数据推送地址
    public final static String getHistoryHrRrData = URL + "getHistoryHrRrData";//设备历史心率&呼吸
    public final static String getRealHrRrData = URL + "getRealHrRrData";//设备实时心率&呼吸
    public final static String getDevice = URL + "getDevice";//获取设备信息接口

    public static String getMd5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMainLtoken(String timestamp) {
        String jiami = CUSTOMERCODE + timestamp + YTOKEN;
        String ltoken = getMd5(jiami).toLowerCase();
        return ltoken;
    }


    public static String getLtoken(String sessionId, String timestamp) {
        String jiami = sessionId + CUSTOMERCODE + "_" + timestamp + YTOKEN;
        String ltoken = getMd5(jiami).toLowerCase();
        return ltoken;
    }
}
