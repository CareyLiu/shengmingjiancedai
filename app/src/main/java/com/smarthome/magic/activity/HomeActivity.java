package com.smarthome.magic.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.heytap.msp.push.HeytapPushManager;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.jaeger.library.StatusBarUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.gaiban.HomeFragment_New;
import com.smarthome.magic.activity.zhinengjiaju.RenTiGanYingActivity;
import com.smarthome.magic.activity.zhinengjiaju.function.LouShuiActivity;
import com.smarthome.magic.activity.zhinengjiaju.function.MenCiActivity;
import com.smarthome.magic.activity.zhinengjiaju.function.MenSuoActivity;
import com.smarthome.magic.activity.zhinengjiaju.function.SosActivity;
import com.smarthome.magic.activity.zhinengjiaju.function.YanGanActivity;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.AppConfig;
import com.smarthome.magic.app.AppManager;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.AudioFocusManager;
import com.smarthome.magic.config.MyApplication;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.MyCarCaoZuoDialog_Notify;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.fragment.MessagerFragment;
import com.smarthome.magic.fragment.MineFragment;
import com.smarthome.magic.fragment.OnlineFragment;
import com.smarthome.magic.fragment.znjj.ZhiNengJiaJuFragment;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.inter.YuYinInter;
import com.smarthome.magic.model.AccessListModel;
import com.smarthome.magic.model.AlarmClass;
import com.smarthome.magic.model.DongTaiShiTiZhuangTaiModel;
import com.smarthome.magic.model.ZhiNengJiaJuNotifyJson;
import com.smarthome.magic.util.AlertUtil;
import com.smarthome.magic.util.AppToast;
import com.smarthome.magic.util.DoMqttValue;
import com.smarthome.magic.util.ShangChuanDongTaiShiTiTool;
import com.smarthome.magic.util.SoundPoolUtils;
import com.smarthome.magic.util.YuYinChuLiTool;
import com.smarthome.magic.view.NoScrollViewPager;
import com.tuya.smart.wrapper.api.TuyaWrapper;
import com.vivo.push.PushClient;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.app.ConstanceValue.MSG_PEIWANG_SUCCESS;
import static com.smarthome.magic.config.MyApplication.CAR_NOTIFY;
import static com.smarthome.magic.config.MyApplication.context;
import static com.smarthome.magic.config.MyApplication.getAppContext;
import static com.smarthome.magic.config.MyApplication.getUser_id;
import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;


public class HomeActivity extends BaseActivity {

    @BindView(R.id.vp)
    NoScrollViewPager mVp;
    @BindView(R.id.activity_with_view_pager)
    RelativeLayout activityWithViewPager;
    @BindView(R.id.bnve)
    BottomNavigationViewEx mBnve;
    @BindView(R.id.tv_yuyin_image)
    ImageView tvYuyinImage;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.cl_top)
    ConstraintLayout clTop;
    @BindView(R.id.tv_shishishuo)
    TextView tvShishishuo;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.rrl_yuyin_mianban)
    RelativeLayout rrlYuyinMianban;
    @BindView(R.id.tv_shezhi)
    TextView tvShezhi;
    @BindView(R.id.tv_chaxun_dabao_zhuangtai)
    TextView tvChaxunDabaoZhuangtai;
    @BindView(R.id.layout_bg)
    RelativeLayout layoutBg;
    @BindView(R.id.tv_shangchuan)
    TextView tvShangchuan;
    private boolean isExit;
    private SparseIntArray items;
    AlarmClass alarmClass;
    private int i = 0;
    TishiDialog tishiDialog;
    YuYinChuLiTool yuYinChuLiTool;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initImmersion() {
        mImmersionBar.with(this).statusBarColor(R.color.white).init();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean vPush = PushClient.getInstance(context).isSupport();
        Log.i("vPush", "" + vPush);

        boolean OPush = HeytapPushManager.isSupportPush();
        Log.i("OPush", "" + OPush);
        //  getZhuJiNet();
        StatusBarUtil.setLightMode(this);
        ButterKnife.bind(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        TuyaWrapper.onLogin();

        initView();
        initData();
        initEvent();

        AppManager.getAppManager().addActivity(this);
        yuYinChuLiTool = new YuYinChuLiTool(HomeActivity.this, new YuYinInter() {
            @Override
            public void showMianBan() {
                Log.i("????????????", "showMianBan");
                rrlYuyinMianban.setVisibility(View.VISIBLE);
                SoundPoolUtils.soundPool(mContext, R.raw.huanxing_mianban);
            }

            @Override
            public void dismissMianBan() {
                rrlYuyinMianban.setVisibility(View.GONE);
            }

            @Override
            public void yuYinResult(String result) {
                tvResult.setText(result);
            }
        });

        String yuYinZhuShouEnable = PreferenceHelper.getInstance(HomeActivity.this).getString(AppConfig.YUYINZHUSHOU, "0");
        Log.i("yuYinZhuShou", "yuYinZhuShou: " + yuYinZhuShouEnable);
        if (yuYinZhuShouEnable.equals("0")) {

        } else {
            wakeUpClick();
        }
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice notice) {
                if (notice.type == ConstanceValue.MSG_GUZHANG_SHOUYE) {
                    tuiSongTanChuang(notice);
                } else if (notice.type == ConstanceValue.MSG_GOTOXIAOXI) {
                    mVp.setCurrentItem(3, false);
                } else if (notice.type == ConstanceValue.MSG_P) {
                    handler.removeCallbacks(runnable);
                } else if (notice.type == ConstanceValue.MSG_ZHINENGJIAJU) {
                    mVp.setCurrentItem(1, false);
                } else if (notice.type == ConstanceValue.MSG_ZHINENGJIAJU_MENCI) {
                    zhiNengJiaJuCaoZuo(notice);
                } else if (notice.type == ConstanceValue.MSG_YUYINHUANXING) {//????????????
                    if (rrlYuyinMianban.getVisibility() == View.VISIBLE) {

                    } else {
                        rrlYuyinMianban.setVisibility(View.VISIBLE);
                        yuYinChuLiTool.beginYuYIn();
                    }

                } else if (notice.type == ConstanceValue.MSG_YUYINKAIQITONGZHI) {
                    wakeUpClick();
                } else if (notice.type == ConstanceValue.MSG_YUYINGUANBITONGZHI) {
                    yuYinChuLiTool.stopHuanXing();//??????????????????????????????
                } else if (notice.type == ConstanceValue.MSG_YUYINXIAOSHI) {
                    yuYinChuLiTool.closeMianBan();
                    rrlYuyinMianban.setVisibility(View.GONE);
                } else if (notice.type == ConstanceValue.MSG_CAOZUODONGTAISHITI) {
                    dognTaiShiTiUrl();
                } else if (notice.type == ConstanceValue.MSG_XIUGAIDONGTAISHITIFINISH) {
                    xiuGaiDongTaiShiTiFinish();
                }
            }
        }));

        rrlYuyinMianban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    //???????????????
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("O.")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_NOTIFY), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "??????O.??????");

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 5000);
            }
        };

        handler.postDelayed(runnable, 5000);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuYinChuLiTool.closeMianBan();
                rrlYuyinMianban.setVisibility(View.GONE);
            }
        });

        tvShezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YuYinSheZhiActivity.actionStart(HomeActivity.this);
            }
        });

        tvChaxunDabaoZhuangtai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yuYinChuLiTool.chaXunDaBaoZhuangTai();
            }
        });
        tvShangchuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                List<String> roomList = new ArrayList<>();
//                roomList.add("??????");
//                roomList.add("??????");
//                List<String> sheBeiList = new ArrayList<>();
//                sheBeiList.add("??????");
//                sheBeiList.add("?????????");
//
//                ShangChuanDongTaiShiTiTool shangChuanDongTaiShiTiTool = new ShangChuanDongTaiShiTiTool(context, roomList, sheBeiList);
//                shangChuanDongTaiShiTiTool.syncContactsRoom(roomList);
//                shangChuanDongTaiShiTiTool.syncContactsSheBei(sheBeiList);
//
//
//                yuYinChuLiTool.syncContactsSheBei();
//                yuYinChuLiTool.syncContactsRoom();
            }
        });
        dognTaiShiTiUrl();

        if (AndMqtt.getInstance().isConnect()) {
            AndMqtt.getInstance().subscribe(new MqttSubscribe()
                    .setTopic("wit/server/01/" + getUser_id())
                    .setQos(2), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        }


    }

    private List<String> roomList = new ArrayList<>();
    private List<String> deviceList = new ArrayList<>();


    private void dognTaiShiTiUrl() {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "16069");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        String str = PreferenceHelper.getInstance(mContext).getString(AppConfig.FAMILY_ID, "");
        if (!StringUtils.isEmpty(str)) {
            map.put("family_id", PreferenceHelper.getInstance(mContext).getString(AppConfig.FAMILY_ID, ""));
        } else {
            return;
        }
        Gson gson = new Gson();
        String a = gson.toJson(map);
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<DongTaiShiTiZhuangTaiModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<DongTaiShiTiZhuangTaiModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<DongTaiShiTiZhuangTaiModel.DataBean>> response) {
                        roomList.clear();
                        deviceList.clear();

                        if (response.body().data.get(0).getRoom_list().size() > 0) {
                            for (int i = 0; i < response.body().data.get(0).getRoom_list().size(); i++) {
                                roomList.add(response.body().data.get(0).getRoom_list().get(i).getName());
                            }
                        }

                        if (response.body().data.get(0).getDevice_list().size() > 0) {
                            for (int i = 0; i < response.body().data.get(0).getDevice_list().size(); i++) {
                                deviceList.add(response.body().data.get(0).getDevice_list().get(i).getName());
                            }

                        }

                        String firstInstallDongTaiShiTi = PreferenceHelper.getInstance(mContext).getString(AppConfig.FIRSTINSTALLDONGTAISHITI, "1");


                        if (firstInstallDongTaiShiTi.equals("0")) {
                            //?????????
                            if (response.body().change_state.equals("1")) {//1.??????????????? 2.?????????

                            } else {
                                new YuYinChuLiTool(context, roomList, deviceList);
                            }

                        } else if (firstInstallDongTaiShiTi.equals("1")) {
                            //??????
                            new YuYinChuLiTool(context, roomList, deviceList);

                        }

                        PreferenceHelper.getInstance(mContext).putString(AppConfig.FIRSTINSTALLDONGTAISHITI, "0");
                    }

                    @Override
                    public void onError(Response<AppResponse<DongTaiShiTiZhuangTaiModel.DataBean>> response) {
                        String str = response.getException().getMessage();
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void xiuGaiDongTaiShiTiFinish() {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "16070");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("family_id", PreferenceHelper.getInstance(mContext).getString(AppConfig.FAMILY_ID, ""));
        Gson gson = new Gson();
        String a = gson.toJson(map);
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<DongTaiShiTiZhuangTaiModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<DongTaiShiTiZhuangTaiModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<DongTaiShiTiZhuangTaiModel.DataBean>> response) {
                    }

                    @Override
                    public void onError(Response<AppResponse<DongTaiShiTiZhuangTaiModel.DataBean>> response) {
                        String str = response.getException().getMessage();
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


    private void zhiNengJiaJuCaoZuo(Notice notice) {
        if (tishiDialog != null && tishiDialog.isShowing()) {
            return;
        }
        /**
         / 00 ?????? 01.??? 02.?????? 03.?????? 04.?????? 05?????? 06.????????????(?????????????????????????????????????????????)
         / 07.?????????  08.?????? 09.????????? 10.?????? 11.???????????? 12.?????? 13.??????14.??????
         / 15.???????????? 16.?????? 17.??????(?????????????????????????????????????????????????????????) 18.?????????
         / 19.???????????? 20.??????????????? 21.?????????????????? 22.????????????????????? 23.???????????? 24.????????????
         / 25.???????????? 26.?????? 27???????????????????????? 28.???????????? 29.???????????? 30.????????????
         / 31.???????????? 32.???????????? 33.?????? 34.??????
         */
        ZhiNengJiaJuNotifyJson zhiNengJiaJuNotifyJson = (ZhiNengJiaJuNotifyJson) notice.content;
        ZhiNengJiaJuNotifyJson finalZhiNengJiaJuNotifyJson1 = zhiNengJiaJuNotifyJson;

        tishiDialog = new TishiDialog(mContext, 1, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {
            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                if (finalZhiNengJiaJuNotifyJson1.getDevice_type().equals("12")) {
                    MenCiActivity.actionStart(mContext, finalZhiNengJiaJuNotifyJson1.getDevice_id());
                } else if (finalZhiNengJiaJuNotifyJson1.getDevice_type().equals("11")) {
                    YanGanActivity.actionStart(mContext, finalZhiNengJiaJuNotifyJson1.getDevice_id());
                } else if (finalZhiNengJiaJuNotifyJson1.getDevice_type().equals("15")) {
                    SosActivity.actionStart(mContext, finalZhiNengJiaJuNotifyJson1.getDevice_id(), true);
                } else if (finalZhiNengJiaJuNotifyJson1.getDevice_type().equals("05")) {
                    MenSuoActivity.actionStart(mContext, finalZhiNengJiaJuNotifyJson1.getDevice_id());
                } else if (finalZhiNengJiaJuNotifyJson1.getDevice_type().equals("13")) {
                    LouShuiActivity.actionStart(mContext, finalZhiNengJiaJuNotifyJson1.getDevice_id());
                } else if (finalZhiNengJiaJuNotifyJson1.getDevice_type().equals("34")) {
                    RenTiGanYingActivity.actionStart(mContext, finalZhiNengJiaJuNotifyJson1.getDevice_id());
                }
                tishiDialog.dismiss();
            }

            @Override
            public void onDismiss(TishiDialog dialog) {
            }
        });


        if (finalZhiNengJiaJuNotifyJson1.getDevice_type().equals("12")) {
            tishiDialog.setTextContent("??????????????????????????????????????????????????????????");
            //MenCiActivity.actionStart(mContext, finalZhiNengJiaJuNotifyJson1.getDevice_id());
        } else if (finalZhiNengJiaJuNotifyJson1.getDevice_type().equals("11")) {
            tishiDialog.setTextContent("???????????????????????????????????????????????????????????????????");
            //YanGanActivity.actionStart(mContext, finalZhiNengJiaJuNotifyJson1.getDevice_id());
        } else if (finalZhiNengJiaJuNotifyJson1.getDevice_type().equals("15")) {
            tishiDialog.setTextContent("???????????????sos?????????????????????????????????????????????????");
            //SosActivity.actionStart(mContext, finalZhiNengJiaJuNotifyJson1.getDevice_id(), true);
        } else if (finalZhiNengJiaJuNotifyJson1.getDevice_type().equals("05")) {
            tishiDialog.setTextContent("??????????????????????????????????????????????????????????");
            //MenSuoActivity.actionStart(mContext, finalZhiNengJiaJuNotifyJson1.getDevice_id());
        } else if (finalZhiNengJiaJuNotifyJson1.getDevice_type().equals("13")) {
            tishiDialog.setTextContent("??????????????????????????????????????????????????????????");
            //LouShuiActivity.actionStart(mContext, finalZhiNengJiaJuNotifyJson1.getDevice_id());
        } else if (finalZhiNengJiaJuNotifyJson1.getDevice_type().equals("34")) {
            tishiDialog.setTextContent("????????????????????????????????????????????????????????????????");
            //RenTiGanYingActivity.actionStart(mContext, finalZhiNengJiaJuNotifyJson1.getDevice_id());
        } else {
            tishiDialog.setTextContent("?????????????????????????????????????????????????");
        }
        String simpleName = MyApplication.getApp().activity_main.getClass().getSimpleName();
        boolean menciFlag = !simpleName.equals(MenCiActivity.class.getSimpleName());
        boolean yanganFlag = !simpleName.equals(YanGanActivity.class.getSimpleName());
        boolean sosFlag = !simpleName.equals(SosActivity.class.getSimpleName());
        boolean loushuiFalg = !simpleName.equals(LouShuiActivity.class.getSimpleName());

        if (menciFlag && yanganFlag && sosFlag && loushuiFalg) {
            if (tishiDialog != null && !tishiDialog.isShowing()) {
                tishiDialog.show();
                String strBaoJingYin = PreferenceHelper.getInstance(mContext).getString(AppConfig.BAOJING_YANGAN, "2");
                if (strBaoJingYin.equals("0")) {

                } else {
                    SoundPoolUtils.soundPool(mContext, R.raw.baojingyin3);
                }
            }
        }


    }

    private void tuiSongTanChuang(Notice notice) {

        String message = (String) notice.content;
        Gson gson = new Gson();
        alarmClass = gson.fromJson(message.toString(), AlarmClass.class);
        Log.i("alarmClass", alarmClass.changjia_name + alarmClass.sell_phone);

        if (MyApplication.activity_main.getClass().getSimpleName().equals(FengNuanActivity.class.getSimpleName())) {
            return;
        } else if (MyApplication.activity_main.getClass().getSimpleName().equals(DiagnosisActivity.class.getSimpleName())) {
            return;
        }

        Log.i("HomeActivity", "11111");
        if (alarmClass.type.equals("1")) {
            switch (alarmClass.sound) {

                case "chSound1.mp3":
                    // SoundPoolUtils.soundPool(mContext,R.raw.ch_sound1);
                    playMusic(R.raw.ch_sound1);
                    break;
                case "chSound2.mp3":
                    playMusic(R.raw.ch_sound2);
                    break;
                case "chSound3.mp3":
                    playMusic(R.raw.ch_sound3);
                    break;
                case "chSound4.mp3":
                    playMusic(R.raw.ch_sound4);
                    break;
                case "chSound5.mp3":
                    playMusic(R.raw.ch_sound5);
                    break;
                case "chSound6.mp3":
                    playMusic(R.raw.ch_sound6);
                    break;
                case "chSound8.mp3":
                    playMusic(R.raw.ch_sound8);
                    break;
                case "chSound9.mp3":
                    playMusic(R.raw.ch_sound9);
                    break;
                case "chSound10.mp3":
                    playMusic(R.raw.ch_sound10);
                    break;
                case "chSound11.mp3":
                    playMusic(R.raw.ch_sound11);
                    break;
                case "chSound18.mp3":
                    playMusic(R.raw.ch_sound18);
                    break;
            }
        } else if (alarmClass.type.equals("5")) {
            if (alarmClass.code.equals("jyj_0006")) {

                tishiDialog = new TishiDialog(mContext, 1, new TishiDialog.TishiDialogListener() {
                    @Override
                    public void onClickCancel(View v, TishiDialog dialog) {
                    }

                    @Override
                    public void onClickConfirm(View v, TishiDialog dialog) {

                        if (alarmClass.device_type.equals("12")) {
                            MenCiActivity.actionStart(mContext, alarmClass.device_id);
                        } else if (alarmClass.device_type.equals("11")) {
                            YanGanActivity.actionStart(mContext, alarmClass.device_id);
                        } else if (alarmClass.device_type.equals("15")) {
                            SosActivity.actionStart(mContext, alarmClass.device_id, true);
                            SoundPoolUtils.soundPool(mContext, R.raw.baojingyin_1);
                        } else if (alarmClass.device_type.equals("05")) {
                            MenSuoActivity.actionStart(mContext, alarmClass.device_id);
                        } else if (alarmClass.device_type.equals("13")) {
                            LouShuiActivity.actionStart(mContext, alarmClass.device_id);
                        }
                    }

                    @Override
                    public void onDismiss(TishiDialog dialog) {

                    }
                });
                tishiDialog.setTextContent("?????????????????????????????????????????????????");


            } else if (alarmClass.code.equals("jyj_0007")) {

            }
        }
    }

    public MediaPlayer player;
    public AudioFocusManager audioFocusManage;
    public int position;
    Runnable runnable;

    public void playMusic(int res) {
        boolean flag = false;

        Activity currentActivity = MyApplication.activity_main;
        boolean flag1 = !currentActivity.getClass().getSimpleName().equals(DiagnosisActivity.class.getSimpleName());
        boolean flag2 = !currentActivity.getClass().getSimpleName().equals(FengNuanActivity.class.getSimpleName());
        if (currentActivity != null) {
            if (flag1 && flag2) {
                TishiDialog myCarCaoZuoDialog_notify = new TishiDialog(HomeActivity.this, 1, new TishiDialog.TishiDialogListener() {

                    @Override
                    public void onClickCancel(View v, TishiDialog dialog) {
                        if (SoundPoolUtils.soundPool != null) {
                            SoundPoolUtils.soundPool.release();
                        }
                    }

                    @Override
                    public void onClickConfirm(View v, TishiDialog dialog) {
                        DiagnosisActivity.actionStart(HomeActivity.this, alarmClass);
                        //SoundPoolUtils.soundPool.release();
                        if (SoundPoolUtils.soundPool != null) {
                            SoundPoolUtils.soundPool.release();
                        }
                    }

                    @Override
                    public void onDismiss(TishiDialog dialog) {

                    }
                }

                );

                // myCarCaoZuoDialog_notify.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);
                myCarCaoZuoDialog_notify.show();

                myCarCaoZuoDialog_notify.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (SoundPoolUtils.soundPool != null) {
                            SoundPoolUtils.soundPool.release();
                        }
                    }
                });

            } else {
                flag = true;
            }
        }

        if (flag) {
            return;
        }

        SoundPoolUtils.soundPool(mContext, res);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("HomeActivity_xxx", "onRestart");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i("HomeActivity_xxx", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("HomeActivity_xxx", "onDestroy");

//        if (AndMqtt.getInstance().getMqttClient().isConnected()) {
//            try {
//                //???????????????
//                AndMqtt.getInstance().publish(new MqttPublish()
//                        .setMsg("K.")
//                        .setQos(2).setRetained(false)
//                        .setTopic("wit/server/01/" + getUser_id()), new IMqttActionListener() {
//                    @Override
//                    public void onSuccess(IMqttToken asyncActionToken) {
//                        Log.i("Rair", "??????K.??????");
//
//                    }
//
//                    @Override
//                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                        Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//
//        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                AppToast.makeShortToast(this, "???????????????????????????");
                isExit = true;
                new Thread() {
                    public void run() {
                        SystemClock.sleep(3000);
                        isExit = false;
                    }

                }.start();
                return true;
            }
//            AppManager.AppExit();
            AppManager.getAppManager().finishAllActivity();
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void finish() {
        super.finish();
    }


    /**
     * change BottomNavigationViewEx style
     */
    private void initView() {
        mBnve.enableAnimation(false);
        mBnve.enableShiftingMode(false);
        mBnve.enableItemShiftingMode(false);
    }

    /**
     * create fragments
     */
    private void initData() {
        List<Fragment> fragments = new ArrayList<>(5);
        items = new SparseIntArray(5);

        HomeFragment_New homeFragment = new HomeFragment_New();
        ZhiNengJiaJuFragment zhiNengJiaJuFragment = new ZhiNengJiaJuFragment();
        OnlineFragment onlineFragment = new OnlineFragment();
        MessagerFragment messagerFragment = new MessagerFragment();
        MineFragment mineFragment = new MineFragment();
        fragments.add(homeFragment);
        //   fragments.add(playerFragment);
        fragments.add(zhiNengJiaJuFragment);
        fragments.add(onlineFragment);
        fragments.add(messagerFragment);
        fragments.add(mineFragment);
//
        items.put(R.id.i_home, 0);
        items.put(R.id.i_zhinengjiaju, 1);
        items.put(R.id.i_car_online, 2);
        items.put(R.id.i_message, 3);
        items.put(R.id.i_mine, 4);

        // set adapter
        VpAdapter adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        //??????????????????????????????????????????????????????????????????
        mVp.setOffscreenPageLimit(5);
        //viewPage????????????
        mVp.setScroll(false);
        mVp.setAdapter(adapter);
    }

    /**
     * set listeners
     */
    private void initEvent() {
        // set listener to change the current item of view pager when click bottom nav item
        mBnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            private int previousPosition = -1;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = items.get(item.getItemId());
                if (previousPosition != position) {
                    previousPosition = position;
                    mVp.setCurrentItem(position, false);
                }
                return true;
            }
        });

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBnve.setCurrentItem(position);
                if (position == 1) {
                    PreferenceHelper.getInstance(mContext).putString(App.CHOOSE_KONGZHI_XIANGMU, DoMqttValue.ZHINENGJIAJU);
                } else {
                    PreferenceHelper.getInstance(mContext).removeKey(App.CHOOSE_KONGZHI_XIANGMU);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private boolean flag = true;
    Handler handler;

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

    }

    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }


    public static HomeActivity getInstance() {
        return new HomeActivity();
    }

    private void wakeUpClick() {
        yuYinChuLiTool.beginWakeUp();
    }


    @Override
    protected void onResume() {
        super.onResume();
        String font_settings = PreferenceHelper.getInstance(mContext).getString(AppConfig.FONT_SETTINGS, "");
        if (TextUtils.isEmpty(font_settings)) {
            setFortXiao();
        } else {
            if (font_settings.equals(AppConfig.FONT_DA)) {
                setFortDa();
            } else {
                setFortXiao();
            }
        }
    }

    private void setFortXiao() {
        mBnve.setTextSize(12);
    }

    private void setFortDa() {
        mBnve.setTextSize(15);
    }
}
