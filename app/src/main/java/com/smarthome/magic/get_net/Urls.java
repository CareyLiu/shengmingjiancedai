package com.smarthome.magic.get_net;

//网络接口地址
public class Urls {
    public static String key = "20180305124455yu";//全局请求key
    public static String SERVER_URL = "https://shop.hljsdkj.com/";//基本地址
//    public static String SERVER_URL = "https://mqrn.hljsdkj.com/";//储物柜
//    public static String SERVER_URL = "http://192.168.1.127:8080/";//大个本地
//    public static String SERVER_URL = "http://192.168.1.110:8080/";//杨哲本地

    public static String MESSAGE_URL = SERVER_URL + "wit/app/user";
    public static String HOME_PICTURE = SERVER_URL + "shop_new/app";
    public static String LIBAOLIST = SERVER_URL + "xc/app/us";//获取大礼包接口
    public static String TAOKELIST = SERVER_URL + "shop_taobaoke/app/user ";
    public static String MSG = SERVER_URL + "msg";
    public static String DALIBAO_PAY = SERVER_URL + "msg/pay/create";//大礼包支付
    public static String ZIJIANHOME = SERVER_URL + "shop_new/app";//自建商城
    public static String ZIYINGFENLEI = SERVER_URL + "shop_new/app";//自建分类
    public static String JDWEB = "https://jmall.ixiaocong.net/#/index/Q6HUHA";//京东的连接
//    public static String PDDWEB = "https://u.jd.com/SgYbff";//京东的连接
    public static String PDDWEB = "https://youhui.pinduoduo.com";//拼多多的链接
    public static String HOME_PICTURE_HOME = SERVER_URL + "shop_new/app/user";

    public static String WIT_APP = SERVER_URL + "wit/app";
    public static String ZHINENGJIAJU = SERVER_URL + "znjj/app/user";//智能家居
    public static String ZHINENGJIJU_ZNJJ = SERVER_URL + "/znjj/app";

    public static String BAZIAPP = SERVER_URL + "bz/app";//八紫APP接口x
    public static String BAZIAPPUSER = SERVER_URL + "bz/app/user";//八紫APP接口
    public static String ARGEMENTS = SERVER_URL + "shop_new/user_agreement";// ARGEMENTS
    public static String PAYSUCCESS = SERVER_URL + "msg/pay/create/ok";//支付成功
    public static String DINGSHI = SERVER_URL + "fn/common";//定时
    public static String TONGCHENG = SERVER_URL + "ir/app/user";//同城相关


    public static String TONG_CHENG = SERVER_URL + "ir/app/user";//同城接口
    public static String TONG_CHENG_UPLOAD = SERVER_URL + "ir/app/upload";//同城上传图片

    public static String GONGJIANGXUZHI = SERVER_URL + "ir/gjIssueNotice";//工匠发布须知
    public static String BIANMINXUZHI = SERVER_URL + "ir/bmIssueNotice";//便民发布须知

}
