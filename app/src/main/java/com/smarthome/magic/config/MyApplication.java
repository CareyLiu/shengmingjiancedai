package com.smarthome.magic.config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.multidex.MultiDexApplication;

import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;

import com.billy.android.loading.Gloading;
import com.bulong.rudeness.RudenessScreenHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttConnect;
import com.rairmmd.andmqtt.MqttSubscribe;
import com.rairmmd.andmqtt.MqttUnSubscribe;
import com.smarthome.magic.R;
import com.smarthome.magic.aakefudan.chat.MyMessage;
import com.smarthome.magic.aakefudan.chat.MyMessageItemProvider;
import com.smarthome.magic.activity.LoginActivity;
import com.smarthome.magic.activity.WelcomeActivity;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.tuya_device.utils.BizBundleFamilyServiceImpl;
import com.smarthome.magic.adapter.view.GlobalAdapter;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.AppConfig;
import com.smarthome.magic.app.AppManager;
import com.smarthome.magic.app.CodeClass;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.HardWareValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.app.RxUtils;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.PeiWangErrorModel;
import com.smarthome.magic.model.PeiwangOtherModel;
import com.smarthome.magic.model.ZhiNengJiaJuNotifyJson;
import com.smarthome.magic.model.ZhiNengJiaJu_0007Model;
import com.smarthome.magic.model.ZhiNengJiaJu_0009Model;
import com.smarthome.magic.util.DoMqttValue;
import com.smarthome.magic.util.JinChengUtils;
import com.smarthome.magic.util.SerializeUtil;
import com.smarthome.magic.util.ShangChuanDongTaiShiTiTool;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tuya.smart.api.router.UrlBuilder;
import com.tuya.smart.api.service.RouteEventListener;
import com.tuya.smart.api.service.ServiceEventListener;
import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.optimus.sdk.TuyaOptimusSdk;
import com.tuya.smart.wrapper.api.TuyaWrapper;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.vivo.push.PushClient;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jaaksi.pickerview.picker.BasePicker;
import org.jaaksi.pickerview.util.Util;
import org.jaaksi.pickerview.widget.DefaultCenterDecoration;
import org.jaaksi.pickerview.widget.PickerView;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import okhttp3.OkHttpClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.smarthome.magic.get_net.Urls.MESSAGE_URL;

public class MyApplication extends MultiDexApplication {
    protected static final String TAG = "MyApplication";
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    //获取屏幕宽高
    public static int windowHeight;
    public static int windowWidth;

    public static Activity activity_main;


    //String mqttUrl = "tcp://192.168.1.127";//大个本地
    String mqttUrl = "tcp://mqtt.hljsdkj.com";//正式
    //String mqttUrl = "tcp://ggw.hljsdkj.com";//ggw

    public String getMqttUrl() {
        if (Urls.SERVER_URL.equals("https://shop.hljsdkj.com/")) {
            return "tcp://mqtt.hljsdkj.com";
        } else {
            return "tcp://ggw.hljsdkj.com";
//            return "tcp://mqtt.hljsdkj.com";
        }
    }

    public static List<String> mqttDingyue = new ArrayList<>();

    public static String getServer_id() {
        car_server_id = PreferenceHelper.getInstance(context).getString("car_server_id", "");
        return car_server_id;
    }

    public static String getCcid() {
        ccid = PreferenceHelper.getInstance(context).getString("ccid", "");
        return ccid;
    }

    public static String getUser_id() {
        user_id = PreferenceHelper.getInstance(context).getString("of_user_id", "");
        return user_id;
    }


    public static String car_server_id;
    public static String user_id;
    public static String ccid;
    public static MyApplication application;
    /**
     * 向客户订阅地址发送车辆时时数据 汽车盒子刚刚启动->接入互联网->订阅本身地址->自己给自己订阅的地址发送心跳,发送，自己接收到了g.说明自己和MQTT连接在线
     */
    public static String CARBOX_GETNOW;//获取实时数据

    /**
     * wit/app/用户id 警报
     */

    public static String CARBOX_JINGBAO;//获得警报数据


    /**
     * 1.app通知后台APP用户全局主题上线
     */

    public static String CAR_NOTIFY;//通知全局上线


    public static String CAR_CTROL;//2.3.2	app发送控制命令

    public static String CAR_MAIN;//主参数订阅

    /**
     * 获取Application
     */
    public static MyApplication getApp() {
        return application;
    }


    private MutableLiveData<String> mBroadcastData;

    private Map<String, Object> mCacheMap;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }

            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    mBroadcastData.setValue(action);
                    break;
            }
        }
    };

    DoMqttValue doMqttValue;
    String doMqtt = "0";//0 不执行mqtt相关功能 1执行mqtt相关功能

    public void onCreate() {


        PreferenceHelper.getInstance(this).removeKey(App.CHOOSE_KONGZHI_XIANGMU);
        StringBuffer param = new StringBuffer();
        param.append("appid=" + getString(R.string.app_id));
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(MyApplication.this, param.toString());
        super.onCreate();
        //设计图标注的宽度
        int designWidth = 720;
        new RudenessScreenHelper(this, designWidth).activate();

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
        MiPushClient.getRegId(getApplicationContext());
        doMqttValue = new DoMqttValue();
        context = getApplicationContext();
        initRongYun();
        initLifecycle();
        initWindow();
        initDefaultPicker();
        initOkgo();
        initTuya();//涂鸦智能家居
        initYoumeng();//友盟推送

        // 获取当前包名
        String packageName = context.getPackageName();
// 获取当前进程名
        String processName = JinChengUtils.getProcessName();
// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
// 初始化Bugly


        Bugly.init(getApplicationContext(), "9aef7d7467", false);
        AndMqtt.getInstance().init(MyApplication.this);

        CompositeSubscription _subscriptions = new CompositeSubscription();
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);

        _subscriptions.add(RxBus.getDefault().toObservable(Notice.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_UNSUB_MQTT) {//取消订阅 application 里面所有的关于mqtt 服务的基本信息
                    // AndMqtt.getInstance().init(MyApplication.this);
                    if (AndMqtt.getInstance().isConnect()) {

                        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CARBOX_JINGBAO), new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i("Rair", "(MainActivity.java:93)-onSuccess:-&gt;取消订阅成功");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i("Rair", "(MainActivity.java:98)-onFailure:-&gt;取消订阅失败");
                                Log.i("Rair", "(MainActivity.java)");
                            }
                        });

                        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CAR_NOTIFY), new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i("Rair", "(MainActivity.java:93)-onSuccess:-&gt;取消订阅成功");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i("Rair", "(MainActivity.java:98)-onFailure:-&gt;取消订阅失败");
                            }
                        });


                    }
                } else if (message.type == ConstanceValue.MSG_CONNET_MQTT) {

                    setMqttConnect();

                } else if (message.type == ConstanceValue.MSG_RONGYUN_CHONGZHI) {
                    initRongYun();
                }
            }
        }));

        setMqttConnect();

        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                // 初始化成功，设置相关的全局配置参数
                // 设置是否使用同步淘客打点
                AlibcTradeSDK.setSyncForTaoke(true);


                // 设置全局淘客参数，方便开发者用同一个淘客参数，不需要在show接口重复传入
                //   AlibcTradeSDK.setTaokeParams(taokeParams);
                Log.i("AlibcTradeSDK", "success");
            }

            @Override
            public void onFailure(int code, String msg) {
                //初始化失败，可以根据code和msg判断失败原因，详情参见错误说明

                Log.i("AlibcTradeSDK", "fail" + "code:" + code + "msg:" + msg);
            }
        });

        application = this;

        mCacheMap = new HashMap<>();

        mBroadcastData = new MutableLiveData<>();
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
//        }
        registerReceiver(mReceiver, filter);

        //view
        Gloading.initDefault(new GlobalAdapter());
        Logger.addLogAdapter(new AndroidLogAdapter());

//        List<String> roomList = new ArrayList<>();
//        roomList.add("茶杯");
//        roomList.add("缸子");
//        List<String> sheBeiList = new ArrayList<>();
//        sheBeiList.add("英雄");
//        sheBeiList.add("大熊猫");

        //  new ShangChuanDongTaiShiTiTool(context, roomList, sheBeiList);
    }

    private void initYoumeng() {
        UMConfigure.init(this, "5fdff473842ba953b890dd98", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "a51f307db82a88eaa46cf1cef4009316");
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String token) {

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler(getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                    }
                });
            }

//            @Override
//            public Notification getNotification(Context context, UMessage msg) {
//                Notification.Builder builder = new Notification.Builder(context).setContentTitle(msg.title).setContentText(msg.text).setDefaults(Notification.DEFAULT_ALL);
//                return builder.build();
//            }
        };

        mPushAgent.setMessageHandler(messageHandler);
    }

    private void initTuya() {//涂鸦智能家居
//        TuyaHomeSdk.init(this);
//        TuyaHomeSdk.setDebugMode(true);
        Fresco.initialize(this);
        TuyaHomeSdk.init(this);
        TuyaWrapper.init(this, new RouteEventListener() {
            @Override
            public void onFaild(int errorCode, UrlBuilder urlBuilder) {
                // urlBuilder.target is a router address, urlBuilder.params is a router params
                // urlBuilder.target 目标路由， urlBuilder.params 路由参数
                Log.e("router not implement", errorCode + "  帆帆帆帆  " + urlBuilder.target + "  嘎嘎嘎  " + urlBuilder.params.toString());
            }
        }, new ServiceEventListener() {
            @Override
            public void onFaild(String serviceName) {
                Log.e("service not implement", serviceName);
            }
        });
        TuyaOptimusSdk.init(this);

        // register family service，mall bizbundle don't have to implement it.
        // 注册家庭服务，商城业务包可以不注册此服务
        TuyaWrapper.registerService(AbsBizBundleFamilyService.class, new BizBundleFamilyServiceImpl());
    }

    private void initRongYun() {
        // 初始化. 建议在 Application 中进行初始化.
        String appKey = "cpj2xarlct6en";
        RongIM.init(context, appKey);
        RongIM.registerMessageType(MyMessage.class);
        RongIM.getInstance().registerMessageTemplate(new MyMessageItemProvider());

        String rongYunToken = UserManager.getManager(context).getRongYun();
        if (!StringUtils.isEmpty(rongYunToken)) {
            connectRongYun(rongYunToken);

            RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageWrapperListener() {
                /**
                 * 接收实时或者离线消息。
                 * 注意:
                 * 1. 针对接收离线消息时，服务端会将 200 条消息打成一个包发到客户端，客户端对这包数据进行解析。
                 * 2. hasPackage 标识是否还有剩余的消息包，left 标识这包消息解析完逐条抛送给 App 层后，剩余多少条。
                 * 如何判断离线消息收完：
                 * 1. hasPackage 和 left 都为 0；
                 * 2. hasPackage 为 0 标识当前正在接收最后一包（200条）消息，left 为 0 标识最后一包的最后一条消息也已接收完毕。
                 *
                 * @param message    接收到的消息对象
                 * @param left       每个数据包数据逐条上抛后，还剩余的条数
                 * @param hasPackage 是否在服务端还存在未下发的消息包
                 * @param offline    消息是否离线消息
                 * @return 是否处理消息。 如果 App 处理了此消息，返回 true; 否则返回 false 由 SDK 处理。
                 */
                @Override
                public boolean onReceived(final Message message, final int left, boolean hasPackage, boolean offline) {
                    Notice notice = new Notice();
                    notice.type = ConstanceValue.MSG_RONGYUN_REVICE;
                    // notice.content = status.
                    //* 用户被开发者后台封禁
                    notice.content = message;
                    RxBus.getDefault().sendRx(notice);
                    return false;
                }
            });

            RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
                /**
                 * 连接状态返回回调
                 * @param status 状态值
                 * CONN_USER_BLOCKED
                 * 用户被开发者后台封禁
                 * CONNECTED
                 * 连接成功。
                 * CONNECTING
                 * 连接中。
                 * DISCONNECTED
                 * 断开连接。
                 * KICKED_OFFLINE_BY_OTHER_CLIENT
                 * 用户账户在其他设备登录，本机会被踢掉线。
                 * NETWORK_UNAVAILABLE
                 * 网络不可用。
                 * SERVER_INVALID
                 * 服务器异常或无法连接。
                 * TOKEN_INCORRECT
                 * Token 不正确。
                 */
                @Override
                public void onChanged(ConnectionStatus status) {

                    Log.i("rongyun", status.getMessage());
                    if (status.getValue() == 3) {

                        Intent intent = new Intent(getAppContext(), LoginActivity.class);
                        intent.putExtra("token_guoqi", "token_guoqi");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }

                    Notice notice = new Notice();
                    notice.type = ConstanceValue.MSG_RONGYUN_STATE;
                    // notice.content = status.
                    //* 用户被开发者后台封禁
                    notice.content = status;
                    RxBus.getDefault().sendRx(notice);


                }
            });
        }

    }

    public void connectRongYun(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallbackEx() {
            /**
             * 数据库回调.
             * @param code 数据库打开状态. DATABASE_OPEN_SUCCESS 数据库打开成功; DATABASE_OPEN_ERROR 数据库打开失败
             */
            @Override
            public void OnDatabaseOpened(RongIMClient.DatabaseOpenStatus code) {
                Log.i("rongYun", "数据库打开失败");
            }

            /**
             * token 无效
             */
            @Override
            public void onTokenIncorrect() {
                Log.i("rongYun", "token 无效");
            }

            /**
             * 成功回调
             * @param userId 当前用户 ID
             */
            @Override
            public void onSuccess(String userId) {
                //UIHelper.ToastMessage(mContext, "融云连接成功");
                Log.i("rongYun", "融云连接成功");
                PreferenceHelper.getInstance(getApplicationContext()).putString(AppConfig.RONGYUN_TOKEN, token);


            }

            /**
             * 错误回调
             * @param errorCode 错误码
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.i("rongYun", "融云连接失败");
            }
        });

    }


    public static MyApplication getInstance() {
        return application;
    }

    public void observeBroadcast(LifecycleOwner owner, Observer<String> observer) {
        mBroadcastData.observe(owner, observer);
    }

    public void observeBroadcastForever(Observer<String> observer) {
        mBroadcastData.observeForever(observer);
    }

    public void removeBroadcastObserver(Observer<String> observer) {
        mBroadcastData.removeObserver(observer);
    }

    public void putCache(String key, Object value) {
        mCacheMap.put(key, value);
    }

    public Object takeCache(String key) {
        return mCacheMap.remove(key);
    }

    public void setMqttConnect() {
        /**
         * 向客户订阅地址发送车辆时时数据 汽车盒子刚刚启动->接入互联网->订阅本身地址->自己给自己订阅的地址发送心跳,发送，自己接收到了g.说明自己和MQTT连接在线
         */

        CARBOX_JINGBAO = "wit/app/" + getUser_id();
        Log.i("getInformation", "CARBOX_JINGBAO   " + CARBOX_JINGBAO);

        CARBOX_GETNOW = "wit/cbox/app/" + getServer_id() + getCcid();
        Log.i("getInformation", "CARBOX_GETNOW   " + CARBOX_GETNOW);

        CAR_NOTIFY = "wit/server/" + "01/" + getUser_id();
        Log.i("getInformation", "CAR_NOTIFY     " + CAR_NOTIFY);

        CAR_CTROL = "wit/cbox/hardware/" + getServer_id() + getCcid();

        if (!StringUtils.isEmpty(getUser_id())) {  //登录再订阅mqtt  如果获取不到 userId 证明没有登录 所有需要优化一下
            if (isAppProcess()) {
                AndMqtt.getInstance().init(this);
                MqttConnect builder = new MqttConnect();
                builder.setClientId(HardWareValue.CLIENT_ID + getUser_id() + System.currentTimeMillis())//连接服务器
                        .setPort(9096)
//                        .setPort(9093)
                        .setAutoReconnect(true)
                        .setCleanSession(true)
                        .setKeepAlive(60)
                        .setCleanSession(true)
                        .setLastWill("K.", "wit/server/01/" + getUser_id(), 2, true)
                        .setUserName("witandroid")
                        .setUserPassword("aG678om34buysdi")
                        .setServer(getMqttUrl()).setTimeout(1);

                builder.setMessageListener(new MqttCallbackExtended() {
                    @Override
                    public void connectComplete(boolean reconnect, String serverURI) {
                        Log.i("Rair", "(MainActivity.java:29)-connectComplete:-&gt;连接完成");
                        sendRx(ConstanceValue.MSG_MQTT_CONNECTCOMPLETE, "");
                        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                                .setTopic(CARBOX_JINGBAO)
                                .setQos(2), new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i("Rair", "自动连接 成功" + CARBOX_JINGBAO);
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i("Rair", "(MainActivity.java:68)-onFailure:-&gt;订阅失败");
                            }
                        });


                        Log.i("rair", "重新订阅数组个数" + mqttDingyue.size() + "");
                        for (int i = 0; i < mqttDingyue.size(); i++) {
                            Log.i("rair", "重新订阅主题：" + mqttDingyue.get(i));
                            AndMqtt.getInstance().subscribe(new MqttSubscribe()
                                    .setTopic(mqttDingyue.get(i))
                                    .setQos(2), new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {

                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                                }
                            });
                        }

                        Notice notice = new Notice();
                        notice.type = ConstanceValue.MSG_NETWORK_CHANGE;
                        RxBus.getDefault().sendRx(notice);
                    }

                    @Override
                    public void connectionLost(Throwable cause) {
                        Log.i("Rair", "(MainActivity.java:34)-connectionLost:-&gt;连接丢失" + cause.getMessage().toString());
                        //UIHelper.ToastMessage(context, "网络不稳定持续连接中", Toast.LENGTH_SHORT);
                        sendRx(ConstanceValue.MSG_MQTT_CONNECTLOST, "");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        if (doMqtt.equals("0")) {
                            return;
                        }
                        System.out.println("Rair 订阅的主题：" + topic + "  收到的数据信息：  " + message.toString());
                        if (message.toString().contains("{")) {
                            //解析对象 code
                            Gson gson = new Gson();
                            CodeClass codeClass = gson.fromJson(message.toString(), CodeClass.class);
                            if (codeClass.code.equals("q")) {
                                //q 是接受消息
                            } else if (codeClass.code.equals("o")) {

                                //警报
                                Notice n = new Notice();
                                n.type = ConstanceValue.MSG_GUZHANG_SHOUYE;
                                n.content = message.toString();
                                RxBus.getDefault().sendRx(n);

                            } else if (codeClass.code.equals("jyj_0006")) {
                                Notice n = new Notice();
                                n.type = ConstanceValue.MSG_ZHINENGJIAJU_MENCI;
                                ZhiNengJiaJuNotifyJson zhiNengJiaJuNotifyJson = new Gson().fromJson(message.toString(), ZhiNengJiaJuNotifyJson.class);
                                n.content = zhiNengJiaJuNotifyJson;
                                RxBus.getDefault().sendRx(n);
                            } else if (codeClass.code.equals("jyj_0007")) {
                                Notice n = new Notice();
                                n.type = ConstanceValue.MSG_TIANJIASHEBEI;
                                ZhiNengJiaJu_0007Model zhiNengJiaJuNotifyJson = new Gson().fromJson(message.toString(), ZhiNengJiaJu_0007Model.class);
                                n.content = zhiNengJiaJuNotifyJson;
                                RxBus.getDefault().sendRx(n);
                            } else if (codeClass.code.equals("jyj_0009")) {
                                Notice n = new Notice();
                                n.type = ConstanceValue.MSG_TIANJIAZHUJI;
                                ZhiNengJiaJu_0009Model zhiNengJiaJuNotifyJson = new Gson().fromJson(message.toString(), ZhiNengJiaJu_0009Model.class);
                                n.content = zhiNengJiaJuNotifyJson;
                                RxBus.getDefault().sendRx(n);
                                //
                            } else if (codeClass.code.equals("jyj_0008")) {
                                Notice n = new Notice();
                                n.type = ConstanceValue.MSG_PEIWANG_ERROR;
                                PeiWangErrorModel peiWangErrorModel = new Gson().fromJson(message.toString(), PeiWangErrorModel.class);
                                n.content = peiWangErrorModel.notify_text;
                                RxBus.getDefault().sendRx(n);
                            } else if (codeClass.code.equals("jyj_0010")) {
                                // 主机被重置时候刷新设备列表接口
                                Notice n = new Notice();
                                n.type = ConstanceValue.MSG_ZHINENGJIAJU_SHOUYE_SHUAXIN;
                                RxBus.getDefault().sendRx(n);
                                PeiWangErrorModel peiWangErrorModel = new Gson().fromJson(message.toString(), PeiWangErrorModel.class);

                                TishiDialog tishiDialog = new TishiDialog(AppManager.getAppManager().currentActivity(), 3, new TishiDialog.TishiDialogListener() {
                                    @Override
                                    public void onClickCancel(View v, TishiDialog dialog) {

                                    }

                                    @Override
                                    public void onClickConfirm(View v, TishiDialog dialog) {

                                    }

                                    @Override
                                    public void onDismiss(TishiDialog dialog) {

                                    }
                                });
                                tishiDialog.setTextContent(peiWangErrorModel.notify_text);
                                tishiDialog.setTextCancel("");
                                tishiDialog.setTextConfirm("确定");
                                tishiDialog.show();
                            } else if (codeClass.code.equals("jyj_0011")) {// 別人绑定主机
                                PeiwangOtherModel peiwangOtherModel = new Gson().fromJson(message.toString(), PeiwangOtherModel.class);
                                Notice n = new Notice();
                                n.type = ConstanceValue.MSG_ZHUJIBANG_OTHER;
                                n.content = peiwangOtherModel;
                                RxBus.getDefault().sendRx(n);
                            }

                        } else if (message.toString().contains("p.")) {
                            Notice n = new Notice();
                            n.type = ConstanceValue.MSG_P;
                            RxBus.getDefault().sendRx(n);
                        } else {
                            doMqttValue.doValue(context, topic, message.toString());
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        try {
                            Log.i("Rair", "消息送达完成： " + token.getMessage().toString());
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        sendRx(ConstanceValue.MSG_MQTT_CONNECTCOMPLETE, "");
                    }
                });

                AndMqtt.getInstance().connect(builder, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.i("Rair", "(MainActivity.java:51)-onSuccess:-&gt;连接成功");

                        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                                .setTopic(CARBOX_JINGBAO)
                                .setQos(2), new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i("Rair", "自动连接 成功" + CARBOX_JINGBAO);
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i("Rair", "(MainActivity.java:68)-onFailure:-&gt;订阅失败");
                            }
                        });

                        sendRx(ConstanceValue.MSG_MQTT_CONNECT_CHONGLIAN_ONSUCCESS, "");

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.i("Rair", "(MainActivity.java:56)-onFailure:-&gt;连接失败" + exception.getMessage() + "..." + exception.getLocalizedMessage());
                        //System.out.println("exception.getMessage" + exception.getMessage());
                        sendRx(ConstanceValue.MSG_MQTT_CONNECT_CHONGLIAN_ONFAILE, "");
                    }
                });
            }
        }
    }


    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    Activity nowActivity;

    private void initLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity.getParent() != null) {//如果这个视图是嵌入的子视图
                    context = activity.getParent();

                } else {
                    context = activity;
                }
                activity_main = activity;
                Log.i(TAG, "ONACTIVITYCREATED  activityName:" + activity_main.getClass().getSimpleName());
                if (activity_main.getClass().getSimpleName().equals("LoginActivity")) {
                    doMqtt = "0";
                } else {
                    doMqtt = "1";
                }

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (activity.getParent() != null) {
                    context = activity.getParent();
                } else {
                    context = activity;
                }
                activity_main = activity;
                Log.i(TAG, "onActivityStarted  activityName:" + activity_main.getClass().getSimpleName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (activity.getParent() != null) {
                    context = activity.getParent();
                } else {
                    context = activity;
                }
                activity_main = activity;
                Log.i(TAG, "onActivityResumed  activityName:" + activity_main.getClass().getSimpleName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (activity.getParent() != null) {
                    context = activity.getParent();
                } else {
                    context = activity;
                }
                Log.i(TAG, "onActivityPaused  activityName:" + activity_main.getClass().getSimpleName());

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (activity.getParent() != null) {
                    context = activity.getParent();
                } else {
                    context = activity;
                }
                Log.i(TAG, "onActivityStopped  activityName:" + activity_main.getClass().getSimpleName());

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                if (activity.getParent() != null) {
                    context = activity.getParent();
                } else {
                    context = activity;
                }
                Log.i(TAG, "onActivitySaveInstanceState  activityName:" + activity_main.getClass().getSimpleName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activity.getParent() != null) {
                    context = activity.getParent();
                } else {
                    context = activity;
                }
                Log.i(TAG, "onActivityDestroyed  activityName:" + activity_main.getClass().getSimpleName());
            }
            //.....
        });

    }


    private void initDefaultPicker() {
        // 利用修改静态默认属性值，快速定制一套满足自己app样式需求的Picker.
        // BasePickerView
        PickerView.sDefaultVisibleItemCount = 3;
        PickerView.sDefaultItemSize = 50;
        PickerView.sDefaultIsCirculation = true;

        // PickerView
        PickerView.sOutTextSize = 18;
        PickerView.sCenterTextSize = 18;
        PickerView.sCenterColor = getResources().getColor(R.color.blue_light);
        PickerView.sOutColor = Color.GRAY;

        // BasePicker
        int padding = Util.dip2px(this, 20);
        BasePicker.sDefaultPaddingRect = new Rect(padding, padding, padding, padding);
        BasePicker.sDefaultPickerBackgroundColor = Color.WHITE;

        // DefaultCenterDecoration
        DefaultCenterDecoration.sDefaultLineWidth = 1;
        DefaultCenterDecoration.sDefaultLineColor = Color.RED;
        //DefaultCenterDecoration.sDefaultDrawable = new ColorDrawable(Color.WHITE);
        int leftMargin = Util.dip2px(this, 10);
        int topMargin = Util.dip2px(this, 2);
        DefaultCenterDecoration.sDefaultMarginRect =
                new Rect(leftMargin, -topMargin, leftMargin, -topMargin);
    }

    private  String appUserAgent;

    public  String getAppUserAgent() {
        if (appUserAgent == null || appUserAgent == "") {
            appUserAgent = getUserAgent();
        }
        return appUserAgent;
    }

    private  String getUserAgent() {
        TelephonyManager mTelephonyMgr = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imsi = mTelephonyMgr.getSubscriberId();
        @SuppressLint("MissingPermission") String imei = mTelephonyMgr.getDeviceId();
        if (appUserAgent == null || appUserAgent == "") {
            StringBuilder ua = new StringBuilder("SHENGDENGKEJI");
            ua.append(',' + android.os.Build.MODEL); //手机型号
            ua.append(',' + "1");//手机系统平台
            ua.append(',' + String.valueOf(application.getPackageInfo().versionName));//App版本
            ua.append(',' + String.valueOf(application.getPackageInfo().versionCode));
            ua.append(',' + String.valueOf(imei));
            ua.append(',' + String.valueOf(imsi));//SIM卡唯一标识
            ua.append(',' + App.DeviceId);//客户端唯一标识
            appUserAgent = ua.toString();
        }
        return appUserAgent;
    }

    HttpHeaders httpParams;

    private void initOkgo() {
//        httpParams = new HttpHeaders();
//        httpParams.put("header", getAppUserAgent());

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);
              //  .addCommonHeaders(httpParams);
        ;                         //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0


    }

    /*
        屏幕宽高
     */
    private void initWindow() {
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        windowWidth = mDisplayMetrics.widthPixels;
        windowHeight = mDisplayMetrics.heightPixels;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    public static Context getAppContext() {
        return context;
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 判断该进程是否是app进程
     *
     * @return
     */


    public boolean isAppProcess() {
        String processName = JinChengUtils.getProcessName();
        if (processName == null || !processName.equalsIgnoreCase(this.getPackageName())) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * * 反序列化byte[]为object或者String。</br>
     * * 注意：如果是Object序列化后的byte[],仅适用jdk自带的序列化</br>
     * * 使用本方法能避免直接反序列化，通过异常判断是String还是Object（只有巧合的走异常流程）。
     */
    private Object unSerializeObjOrString(byte[] bytes) {
        // 不足6位，直接认为是字符串，,经测试单个字符序列化后的byte[]也有8位
        if (bytes.length < 6) {
            return SerializeUtil.Byte2String(bytes);
        }

        String protocol = Integer.toHexString(bytes[0] & 0x000000ff) + Integer.toHexString(bytes[1] & 0x000000ff);

        // 如果是jdk序列化后的
        if ("ACED".equals(protocol.toUpperCase())) {
            Object obj = SerializeUtil.unserializeObj(bytes);

            if (obj != null) {
                return obj;
            }

            // 如果是巧合，则返回的是null
            else {
                return SerializeUtil.Byte2String(bytes);
            }
        }

        // 如果是字符串的byte[]字节形式
        return SerializeUtil.Byte2String(bytes);

    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d(TAG, "onTerminate");
        super.onTerminate();
        if (AndMqtt.getInstance().getMqttClient().isConnected()) {
            try {
                AndMqtt.getInstance().getMqttClient().disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        unregisterReceiver(mReceiver);

    }

    public void setHuLvJingBao(List<CarCode> code) {
        //访问网络获取数据 下面的列表数据
        Map<String, Object> map = new HashMap<>();
        map.put("code", "03221");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(getApp()).getAppToken());
        map.put("where", code);

        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse>post(MESSAGE_URL)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse>() {
                    @Override
                    public void onSuccess(Response<AppResponse> response) {

                    }
                });
    }

    public class CarCode {
        String car_code;
    }

    public void sendRx(int strValue, String strContent) {
        Notice n = new Notice();
        n.type = strValue;
//                            n.content = message.toString();
        n.content = strContent;
        RxBus.getDefault().sendRx(n);
    }


    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) info = new PackageInfo();
        return info;
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

}


