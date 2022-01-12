package com.smarthome.magic.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;
import com.rairmmd.andmqtt.MqttUnSubscribe;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.AppManager;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConfigValue;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.config.MyApplication;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.dialog.LordingDialog;
import com.smarthome.magic.dialog.MyCarCaoZuoDialog_Notify;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.util.DoMqttValue;
import com.smarthome.magic.util.SoundPoolUtils;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.math.BigDecimal;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.app.ConfigValue.STARTSHELVES;
import static com.smarthome.magic.app.ConstanceValue.MSG_MQTT_CONNECTARRIVE;
import static com.smarthome.magic.app.ConstanceValue.MSG_MQTT_CONNECTCOMPLETE;
import static com.smarthome.magic.app.ConstanceValue.MSG_MQTT_CONNECTLOST;
import static com.smarthome.magic.app.ConstanceValue.MSG_MQTT_CONNECT_CHONGLIAN_ONFAILE;
import static com.smarthome.magic.app.ConstanceValue.MSG_MQTT_CONNECT_CHONGLIAN_ONSUCCESS;
import static com.smarthome.magic.config.MyApplication.CARBOX_GETNOW;
import static com.smarthome.magic.config.MyApplication.CAR_CTROL;
import static com.smarthome.magic.config.MyApplication.CAR_NOTIFY;
import static com.smarthome.magic.config.MyApplication.getAppContext;


public class FengNuanActivity extends BaseActivity implements View.OnLongClickListener {
    ProgressDialog waitDialog;
    @BindView(R.id.iv_kaiji)
    ImageView ivKaiji;
    @BindView(R.id.iv_guanji)
    ImageView ivGuanji;
    @BindView(R.id.ll_click_shebeima)
    LinearLayout llClickShebeima;
    @BindView(R.id.ll_dingshi)
    LinearLayout llDingshi;
    @BindView(R.id.ll_xinhao)
    LinearLayout llXinhao;
    @BindView(R.id.ll_fuwu_tianshu)
    LinearLayout llFuwuTianshu;
    @BindView(R.id.iv_dangweimoshi)
    ImageView ivDangweimoshi;
    @BindView(R.id.iv_kongtiaomoshi)
    ImageView ivKongtiaomoshi;
    @BindView(R.id.ll_gongneng)
    LinearLayout llGongneng;
    @BindView(R.id.ll_wendu)
    LinearLayout llWendu;
    @BindView(R.id.seekBar1)
    SeekBar seekBar1;
    @BindView(R.id.tv_zuidiwendu)
    TextView tvZuidiwendu;
    @BindView(R.id.tv_zuigaowendu)
    TextView tvZuigaowendu;
    @BindView(R.id.tv_dangqian_wendu_or_dangwei)
    TextView tvDangqianWenduOrDangwei;
    @BindView(R.id.tv_sheding_wendu_or_dangwei)
    TextView tvShedingWenduOrDangwei;
    @BindView(R.id.tv_shuibeng)
    TextView tvShuibeng;
    @BindView(R.id.tv_zidongbengyou)
    TextView tvZidongbengyou;
    @BindView(R.id.tv_yutongfeng)
    TextView tvYutongfeng;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_xinhao)
    ImageView ivXinhao;
    @BindView(R.id.tv_zaixian)
    TextView tvZaixian;
    @BindView(R.id.tv_shebeizhuangtai)
    TextView tvShebeizhuangtai;
    @BindView(R.id.seekBar_kongtiao)
    SeekBar seekBarKongtiao;
    @BindView(R.id.tv_dianya)
    TextView tvDianya;
    @BindView(R.id.tv_rufengkouwendu)
    TextView tvRufengkouwendu;
    @BindView(R.id.tv_chufengkouwendu)
    TextView tvChufengkouwendu;
    @BindView(R.id.tv_haibagaodu)
    TextView tvHaibagaodu;
    @BindView(R.id.tv_hanyangliang)
    TextView tvHanyangliang;
    @BindView(R.id.tv_daqiya)
    TextView tvDaqiya;
    @BindView(R.id.iv_shezhi)
    ImageView ivShezhi;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.tv_gongzuo_shichang)
    TextView tvGongzuoShichang;
    private LordingDialog lordingDialog;
    public String car_server_id;
    public String ccid;
    public String of_user_id;
    MyCarCaoZuoDialog_Notify myCarCaoZuoDialog_notify;
    String version;
    private String dangWeiMoShiValue = "0";//0 关 1 开
    private String kongTiaoMoshiValue = "0";//0关 1 开
    private String bengYouValue = "0";//0 关 1 开
    private String tongFengValue = "0";
    private String shuiBengValue = "0";//0 关 1开 a 无

    private String shuiBengZhongXianFlag = "1";
    private String firstSetDangWei = "0";
    private String firstSetKongTiao = "0";
    TishiDialog tishiDialog;


    //你想要的操作命令   1.档位模式开机 2.档位模式关机 3.空调模式开机 4.空调模式关机 5开启预通风模式 6关闭预通风 7开启泵油 8关闭泵油 9开启水泵 10 关闭水泵,11判断是否在线
    private String whatUWant = "aaa";


    private final String DANGWEIKAIJI = "1";
    private final String DANGWEIGUANJI = "2";
    private final String KONGTIAOKAIJI = "3";
    private final String KONGTIAOGUANJI = "4";
    private final String YUTONGFENGKAIJI = "5";
    private final String YUTONGFENGGUANJI = "6";
    private final String BENGYOUKAIJI = "7";
    private final String BENGYOUGUANJI = "8";
    private final String SHUIBENGKAIJI = "9";
    private final String SHUIBENGGUANJI = "10";
    private final String PANDUANZAIXIANZHUAGNTAI = "11";//判断是否在线
    N9Thread n9Thread = null;
    DangWeiGuanJiThread dangWeiGuanJiThread = null;//档位关机
    DangWeiKaiJiThread dangWeiKaiJiThread = null;//档位开机
    KongTiaoKaiJiThread kongTiaoKaiJiThread = null;//空调开机
    KongTiaoGuanJiThread kongTiaoGuanJiThread = null;//空调关机
    YuTongFengKaiJiThread yuTongFengKaiJiThread = null;//预通风开机
    YuTongFengGuanJiThread yuTongFengGuanJiThread = null;//预通风关机
    BengYouKaiJiThread bengYouKaiJiThread = null;// 泵油开机
    BengYouGuanJiThread bengYouGuanJiThread = null;//泵油关机
    XunHuanN xunHuanN = null;
    String dangQianDangWei = "3";//默认3档

    String simKaIdFlag = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_JIEBANG) {
                    finish();
                } else if (message.type == ConstanceValue.MSG_NETWORK_CHANGE) {
//                    if (!n9Thread.isAlive()){
//                        n9Thread.start();
//                    }
                }
            }
        }));

        simKaIdFlag = getIntent().getStringExtra("simKaIdFlag");

        ivShezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SheBeiSetActivity.actionStart(mContext, SheBeiSetActivity.TYPE_FENGNUAN);
                RatioActivity.actionStart(mContext);
            }
        });
        llDingshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FengnuandishiActivity.actionStart(mContext);
            }
        });
        llClickShebeima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TishiDialog tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
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
                String ccid = PreferenceHelper.getInstance(mContext).getString("ccid", "");
                tishiDialog.setTextTitle("设备码");
                tishiDialog.setTextCancel("");
                tishiDialog.setTextContent(ccid);
                tishiDialog.show();
            }
        });
        PreferenceHelper.getInstance(mContext).putString(App.CHOOSE_KONGZHI_XIANGMU, DoMqttValue.FENGNUAN);
        waitDialog = ProgressDialog.show(mContext, null, "网络状态不稳定,连接中···", true, true);
        lordingDialog = new LordingDialog(mContext);

        car_server_id = PreferenceHelper.getInstance(mContext).getString("car_server_id", "");
        ccid = PreferenceHelper.getInstance(mContext).getString("ccid", "");
        of_user_id = PreferenceHelper.getInstance(mContext).getString("of_user_id", "");
        setMqttZhiLing();

        Glide.with(mContext).asGif().load(R.drawable.fengnuan_kaiji).into(ivKaiji);

        ivKaiji.setVisibility(View.GONE);
        ivGuanji.setVisibility(View.VISIBLE);

        myCarCaoZuoDialog_notify = new MyCarCaoZuoDialog_Notify(getAppContext(), new MyCarCaoZuoDialog_Notify.OnDialogItemClickListener() {
            @Override
            public void clickLeft() {
                // player.stop();

            }

            @Override
            public void clickRight() {
                DiagnosisActivity.actionStart(mContext);
                //SoundPoolUtils.soundPool.release();
                myCarCaoZuoDialog_notify.dismiss();
                if (SoundPoolUtils.soundPool != null) {
                    SoundPoolUtils.soundPool.release();
                }

            }
        }
        );

        myCarCaoZuoDialog_notify.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);

        getTongZhi();

        String value = PreferenceHelper.getInstance(mContext).getString(STARTSHELVES, "1");
//        if (value.equals("1")) {//档位
//            ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_sel);
//            ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_nor);
//
//        } else if (value.equals("2")) {//空调
//            ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_sel);
//            ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_nor);
//        }


        ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_nor);
        ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_nor);

        ivDangweimoshi.setOnLongClickListener(this);
        ivKongtiaomoshi.setOnLongClickListener(this);


        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (dangWeiMoShiValue.equals("1")) {

                    //   UIHelper.ToastMessage(mContext, seekBar.getProgress() + "");
                    if (seekBar.getProgress() >= 0 && seekBar.getProgress() < 20) {
                        seekBar1.setProgress(20);
                        qieHuanDangWei(1);

                    } else if (seekBar.getProgress() > 20 && seekBar.getProgress() < 40) {

                        if (seekBar.getProgress() > 20 && seekBar.getProgress() < 30) {
                            seekBar1.setProgress(20);
                            qieHuanDangWei(1);
                        } else {
                            seekBar1.setProgress(40);
                            qieHuanDangWei(2);
                        }

                    } else if (seekBar.getProgress() > 40 && seekBar.getProgress() < 60) {
                        if (seekBar.getProgress() > 40 && seekBar.getProgress() < 50) {
                            seekBar1.setProgress(40);
                            qieHuanDangWei(2);
                        } else {
                            seekBar1.setProgress(60);
                            qieHuanDangWei(3);
                        }

                    } else if (seekBar.getProgress() > 60 && seekBar.getProgress() < 80) {
                        if (seekBar.getProgress() > 60 && seekBar.getProgress() < 70) {
                            seekBar1.setProgress(60);
                            qieHuanDangWei(3);
                        } else {
                            seekBar1.setProgress(80);
                            qieHuanDangWei(4);
                        }

                    } else if (seekBar.getProgress() > 80 && seekBar.getProgress() < 100) {
                        if (seekBar.getProgress() > 80 && seekBar.getProgress() < 90) {
                            seekBar1.setProgress(80);
                            qieHuanDangWei(4);
                        } else {
                            seekBar1.setProgress(100);
                            qieHuanDangWei(5);
                        }
                    }

                }

            }
        });

        seekBarKongtiao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                qieHuanWenDu(seekBar.getProgress());

            }
        });
        lordingDialog.setTextMsg("设备连接中...");
        lordingDialog.show();

        tvShuibeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (run==true){
//                    run = false;
//                    handler.removeCallbacks(myRunnable);
//                }

                if (shuiBengValue.equals("a")) {
                    UIHelper.ToastMessage(mContext, "无水泵功能");
                    return;
                }

                if (shuiBengValue.equals("0")) {
                    whatUWant = SHUIBENGKAIJI;
                    lordingDialog.setTextMsg("正在开启水泵循环，请稍后");
                    lordingDialog.show();
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M711.")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                            //UIHelper.ToastMessage(WindHeaterActivity.this, "指令发送成功,等待服务器响应", Toast.LENGTH_SHORT);
                            //PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k71" + "1" + "1.");


                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                            UIHelper.ToastMessage(mContext, "指令发送失败", Toast.LENGTH_SHORT);
                        }
                    });
                } else {
                    whatUWant = SHUIBENGGUANJI;
                    lordingDialog.setTextMsg("正在关闭水泵循环，请稍后");
                    lordingDialog.show();
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M712.")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                            //UIHelper.ToastMessage(WindHeaterActivity.this, "指令发送成功,等待服务器响应", Toast.LENGTH_SHORT);
                            PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k71" + "2" + "1.");

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                            UIHelper.ToastMessage(mContext, "指令发送失败", Toast.LENGTH_SHORT);
                        }
                    });
                }


            }
        });
        tvYutongfeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (run==true){
//                    run = false;
//                    handler.removeCallbacks(myRunnable);
//                }
                if (kongTiaoMoshiValue.equals("1") || dangWeiMoShiValue.equals("1") || bengYouValue.equals("1")) {
                    UIHelper.ToastMessage(mContext, "请关机后再执行预通风操作");
                    return;
                }

                if (tongFengValue.equals("0")) {

//                    lordingDialog.setTextMsg("正在开启预通风模式...");
//                    lordingDialog.show();

                    whatUWant = YUTONGFENGKAIJI;
                    yuTongFengKaiJiThread = new YuTongFengKaiJiThread();
                    yuTongFengKaiJiThread.start();

                } else if (tongFengValue.equals("1")) {
                    //关闭预通风
//                    lordingDialog.setTextMsg("正在关机请稍后...");
//                    lordingDialog.show();
                    whatUWant = YUTONGFENGGUANJI;
                    yuTongFengGuanJiThread = new YuTongFengGuanJiThread();
                    yuTongFengGuanJiThread.start();


                }
            }
        });

        tvZidongbengyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (run==true){
//                    run = false;
//                    handler.removeCallbacks(myRunnable);
//                }
                if (kongTiaoMoshiValue.equals("1") || dangWeiMoShiValue.equals("1") || tongFengValue.equals("1")) {
                    UIHelper.ToastMessage(mContext, "请关机后再执行泵油操作");
                    return;
                }

                if (bengYouValue.equals("0")) {
//                    lordingDialog.setTextMsg("正在开启预泵油，请稍后");
//                    lordingDialog.show();
                    whatUWant = BENGYOUKAIJI;
                    bengYouKaiJiThread = new BengYouKaiJiThread();
                    bengYouKaiJiThread.start();

                } else if (bengYouValue.equals("1")) {
                    //关闭泵油
//                    lordingDialog.setTextMsg("正在关机请稍后...");
//                    lordingDialog.show();
                    whatUWant = BENGYOUGUANJI;
                    bengYouGuanJiThread = new BengYouGuanJiThread();
                    bengYouGuanJiThread.start();
                }
            }
        });
        tishiDialog = new TishiDialog(getAppContext(), 1, new TishiDialog.TishiDialogListener() {
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
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (!AndMqtt.getInstance().isConnect()) {
                    UIHelper.ToastMessage(mContext, "请检查网络后重新尝试");
                    return;
                }
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("N.")
                        .setQos(2)
                        .setTopic(CAR_CTROL)
                        .setRetained(false), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        //Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功" + " N9 我是在类里面订阅的");
                        Log.i("xunhuancishu", "循环发送第" + xunHuanCiShu + "次");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        //Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                    }
                });
            }
        });
    }

    boolean flag = true;

    private void getTongZhi() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                Log.i("message.type", String.valueOf(message.type));
                if (message.type == ConstanceValue.MSG_CAR_J_G) {

                    //接收到信息
                    Log.i("msg_car_j_", message.content.toString());
                    String messageData = message.content.toString().substring(1, message.content.toString().length() - 1);
                    if (0 <= messageData.substring(0, 6).indexOf("a")) {
                        // 纬度x 045.666666=045666666 9
                        BigDecimal dec = new BigDecimal(messageData.substring(0, 3) + "." + messageData.substring(3, 9));
                        String gps_x = dec.toString();
                        // 经度y 126.666666=126666666 9
                        dec = new BigDecimal(messageData.substring(9, 12) + "." + messageData.substring(12, 18));
                        String gps_y = dec.toString();
                        // 车头朝向方向:1东2南3西4北5西北 6西南7东南 8东北
                        String car_orientations = messageData.substring(18, 19);
                        if (!"a".equals(car_orientations)) {

                            if (car_orientations.equals("1")) {

                            } else if (car_orientations.equals("2")) {

                            } else if (car_orientations.equals("3")) {

                            } else if (car_orientations.equals("4")) {

                            } else if (car_orientations.equals("5")) {

                            } else if (car_orientations.equals("6")) {

                            } else if (car_orientations.equals("7")) {

                            } else if (car_orientations.equals("8")) {

                            }
                        }
                    }
                } else if (message.type == ConstanceValue.MSG_CAR_J_M) {
                    if (smartRefreshLayout != null) {
                        smartRefreshLayout.finishRefresh();
                    }

                    if (lordingDialog != null) {
                        if (lordingDialog.isShowing()) {
                            lordingDialog.dismiss();
                        }
                    }
                    if (flag) {

                        if (simKaIdFlag.equals("2")) {
                            AndMqtt.getInstance().publish(new MqttPublish()
                                    .setMsg("X.")
                                    .setQos(2)
                                    .setTopic(CAR_CTROL)
                                    .setRetained(false), new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    Log.i("Rair", "发送指令P.等待应答");

                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                    Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                                }
                            });
                        }


                        //获得定位地址
                        AndMqtt.getInstance().publish(new MqttPublish()
                                .setMsg("Y.")
                                .setQos(2)
                                .setTopic(CAR_CTROL)
                                .setRetained(false), new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i("Rair", "发送指令P.等待应答");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                            }
                        });
                        flag = false;
                    }


                    lordingDialog.dismiss();
                    tvZaixian.setText("在线");
                    if (whatUWant.equals(PANDUANZAIXIANZHUAGNTAI)) {
                        whatUWant = "";

                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_N9_LIANJIE;
                        RxBus.getDefault().sendRx(n);
                    }
                    //接收到信息
                    Log.i("msg_car_j_m", message.content.toString());

                    String messageData = message.content.toString().substring(1);
                    Log.i("msg_car_j_m_data", messageData);
                    // 驻车加热器:当前档位1至5档	1	是
                    String oper_dang = messageData.substring(0, 1);

                    dangQianDangWei = oper_dang;
                    if (0 <= oper_dang.indexOf("a")) {
                        oper_dang = "";
                    } else {
                        if (oper_dang.equals("1")) {
                            seekBar1.setProgress(20);
                        } else if (oper_dang.equals("2")) {
                            seekBar1.setProgress(40);
                        } else if (oper_dang.equals("3")) {
                            seekBar1.setProgress(60);
                        } else if (oper_dang.equals("4")) {
                            seekBar1.setProgress(80);
                        } else if (oper_dang.equals("5")) {
                            seekBar1.setProgress(100);
                        }

                    }
                    // 预设温度1至32度	2	是
                    String oper_wendu_now = messageData.substring(1, 3);
                    oper_wendu_now = 0 <= oper_wendu_now.indexOf("a") ? "" : new BigDecimal(oper_wendu_now).toString();

                    // 驻车预热器:1.档位开机2.空调开机3.风暖关机4.水暖:开机5.水暖:关机	1	是
                    String oper_open_close = messageData.substring(3, 4);
                    /**
                     * 驻车预热器:1.档位开机2.空调开机3.关机 4.水泵开机9.关机中
                     * 6.预泵油7.预通风
                     */


                    switch (oper_open_close) {
                        case "1":

//                            if (run!=true){
//                                run = true;
//                                handler.post(myRunnable);
//                            }

                            tvShebeizhuangtai.setText("设备状态：档位模式");
                            tvDangqianWenduOrDangwei.setText("当前档位：" + oper_dang + "档");


                            //   UIHelper.ToastMessage(mContext, seekBar.getProgress() + "");
                            if (seekBar1.getProgress() >= 0 && seekBar1.getProgress() < 20) {

                                tvShedingWenduOrDangwei.setText("当前档位：" + "1" + "档");
                            } else if (seekBar1.getProgress() >= 20 && seekBar1.getProgress() < 40) {

                                if (seekBar1.getProgress() >= 20 && seekBar1.getProgress() < 30) {
                                    tvShedingWenduOrDangwei.setText("当前档位：" + "1" + "档");
                                } else {

                                    tvShedingWenduOrDangwei.setText("当前档位：" + "2" + "档");
                                }

                            } else if (seekBar1.getProgress() >= 40 && seekBar1.getProgress() < 60) {
                                if (seekBar1.getProgress() >= 40 && seekBar1.getProgress() < 50) {
                                    tvShedingWenduOrDangwei.setText("当前档位：" + "2" + "档");
                                } else {
                                    tvShedingWenduOrDangwei.setText("当前档位：" + "3" + "档");
                                }

                            } else if (seekBar1.getProgress() >= 60 && seekBar1.getProgress() < 80) {
                                if (seekBar1.getProgress() >= 60 && seekBar1.getProgress() < 70) {
                                    tvShedingWenduOrDangwei.setText("当前档位：" + "3" + "档");
                                } else {
                                    tvShedingWenduOrDangwei.setText("当前档位：" + "4" + "档");
                                }

                            } else if (seekBar1.getProgress() >= 80 && seekBar1.getProgress() < 100) {
                                if (seekBar1.getProgress() >= 80 && seekBar1.getProgress() < 90) {
                                    tvShedingWenduOrDangwei.setText("当前档位：" + "4" + "档");
                                } else {
                                    tvShedingWenduOrDangwei.setText("当前档位：" + "5" + "档");
                                }
                            }


                            if (firstSetDangWei.equals("0")) {
                                tvShedingWenduOrDangwei.setText("当前档位：" + oper_dang + "档");
                                firstSetDangWei = "1";
                            }
                            PreferenceHelper.getInstance(mContext).putString(STARTSHELVES, "1");
                            //openMode(Integer.parseInt(oper_dang));
                            //switchModel(rbHeaterGearMode, 1);
                            // button = rbHeaterGearMode;

                            // if (oper_dang != null) {
                            //    openMode(Integer.parseInt(oper_dang));
                            //  }
                            ivKaiji.setVisibility(View.VISIBLE);
                            ivGuanji.setVisibility(View.GONE);

                            ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_sel);
                            ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_nor);

                            tvZuidiwendu.setText("1档");
                            tvZuigaowendu.setText("5档");

                            dangWeiMoShiValue = "1";


                            seekBar1.setVisibility(View.VISIBLE);
                            seekBarKongtiao.setVisibility(View.GONE);

                            if (whatUWant.equals(DANGWEIKAIJI)) {
                                lordingDialog.dismiss();
                                SoundPoolUtils.soundPool(mContext, R.raw.yikaiji);
                                whatUWant = "";
                                dangWeiKaiJiThread.interrupt();
                            }

                            if (banBenHao != null) {
                                if (banBenHao.equals("2020")) {
                                    if (xunHuanN == null) {
                                        Log.i("xunhuancishu", "开启循环线程");
                                        //  UIHelper.ToastMessage(mContext, "xunhuan xunhuan");
                                        xunHuanNFlag = true;
                                        xunHuanN = new XunHuanN();
                                        xunHuanN.start();
                                    }
                                }

                            }

                            break;
                        case "2":
//                            if (run!=true){
//                                run = true;
//                                handler.post(myRunnable);
//                            }

                            if (firstSetKongTiao.equals("0")) {
                                tvShedingWenduOrDangwei.setText("设定温度：" + oper_wendu_now + "℃");
                                firstSetKongTiao = "1";
                            }
                            seekBarKongtiao.setProgress(Integer.parseInt(oper_wendu_now));
                            tvShebeizhuangtai.setText("设备状态：空调模式");
                            ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_nor);
                            ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_sel);
                            if (oper_wendu_now != null) {
                                tvDangqianWenduOrDangwei.setText("当前温度：" + oper_wendu_now + "℃");
                            }
                            tvShedingWenduOrDangwei.setText("设定温度：" + seekBarKongtiao.getProgress() + "℃");
                            tvZuidiwendu.setText("0℃");
                            tvZuigaowendu.setText("32℃");
                            kongTiaoMoshiValue = "1";
                            PreferenceHelper.getInstance(mContext).putString(STARTSHELVES, "2");
//                            if (null != oper_wendu_now) {
//                                openKongTiaoMode(Integer.parseInt(oper_wendu_now));
//                            } else {
//                                openMode(0);
//                            }
//
//                            switchModel(rbHeaterAirMode, 2);
//                            button = rbHeaterAirMode;
                            ivKaiji.setVisibility(View.VISIBLE);
                            ivGuanji.setVisibility(View.GONE);

                            seekBar1.setVisibility(View.GONE);
                            seekBarKongtiao.setVisibility(View.VISIBLE);

                            if (whatUWant.equals(KONGTIAOKAIJI)) {
                                lordingDialog.dismiss();
                                SoundPoolUtils.soundPool(mContext, R.raw.yikaiji);
                                whatUWant = "";
                                if (kongTiaoKaiJiThread != null) {
                                    kongTiaoKaiJiThread.interrupt();
                                }

                                if (tishiDialog != null) {
                                    tishiDialog.dismiss();
                                }
                            }
                            if (banBenHao != null) {
                                if (banBenHao.equals("2020")) {
                                    if (xunHuanN == null) {
                                        Log.i("xunhuancishu", "开启循环线程");
                                        xunHuanN = new XunHuanN();
                                        xunHuanNFlag = true;
                                        xunHuanN.start();
                                    }
                                }

                            }
                            break;
                        case "3":
//                            if (run==true){
//                                run = false;
//                                handler.removeCallbacks(myRunnable);
//                            }

//                            if (xunHuanN != null) {
//                                xunHuanNFlag = false;
//                                xunHuanN.interrupt();
//                                xunHuanN = null;
//                            }


                            tvShebeizhuangtai.setText("设备状态：关机");
                            ivKaiji.setVisibility(View.GONE);
                            ivGuanji.setVisibility(View.VISIBLE);

                            ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_nor);
                            ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_nor);

                            dangWeiMoShiValue = "0";
                            kongTiaoMoshiValue = "0";
                            shuiBengValue = "0";
                            tongFengValue = "0";
                            bengYouValue = "0";

                            tvYutongfeng.setTextColor(mContext.getResources().getColor(R.color.gray999999));
                            tvZidongbengyou.setTextColor(mContext.getResources().getColor(R.color.gray999999));

                            //   closeMode(0);
                            //  AlertUtil.t(WindHeaterActivity.this, "当前设备已关机");
                            //tvYuShe_WenDu.setText("长按选择开机模式");

                            tvDangqianWenduOrDangwei.setText("当前温度/档位: - -");
                            tvShedingWenduOrDangwei.setText("设定温度/档位: - -");
                            tvZuidiwendu.setText("0℃/1档");
                            tvZuigaowendu.setText("32℃/5档");
                            if (whatUWant.equals(DANGWEIGUANJI)) {
                                lordingDialog.dismiss();
                                SoundPoolUtils.soundPool(mContext, R.raw.yiguanji);
                                whatUWant = "";
                                if (dangWeiGuanJiThread != null) {
                                    dangWeiGuanJiThread.interrupt();
                                }
                            } else if (whatUWant.equals(KONGTIAOGUANJI)) {
                                lordingDialog.dismiss();
                                SoundPoolUtils.soundPool(mContext, R.raw.yiguanji);
                                whatUWant = "";
                                if (kongTiaoGuanJiThread != null) {
                                    kongTiaoGuanJiThread.interrupt();
                                }
                            } else if (whatUWant.equals(BENGYOUGUANJI)) {
                                tishiDialog.dismiss();
                                whatUWant = "";
                            } else if (whatUWant.equals(YUTONGFENGGUANJI)) {
                                whatUWant = "";
                                if (yuTongFengGuanJiThread != null) {
                                    yuTongFengGuanJiThread.interrupt();
                                }
                                tishiDialog.dismiss();
                            } else if (whatUWant.equals(SHUIBENGGUANJI)) {
                                whatUWant = "";
                                lordingDialog.dismiss();
                            } else if (whatUWant.equals(BENGYOUGUANJI)) {
                                whatUWant = "";
                                tishiDialog.dismiss();
                            }

                            break;
                        case "4"://水泵开机
//                            if (run!=true){
//                                run = true;
//                                handler.post(myRunnable);
//                            }

                            //  tvShebeizhuangtai.setText("设备状态：水泵循环");
//                            tvShuibeng.setTextColor(mContext.getResources().getColor(R.color.blue00fff));
//                            tvShuibeng.setText("水泵已开机");
                            if (lordingDialog != null) {
                                if (lordingDialog.isShowing()) {
                                    lordingDialog.dismiss();
                                }
                            }

                            break;
                        case "6":
//                            if (run!=true){
//                                run = true;
//                                handler.post(myRunnable);
//                            }

                            tvShebeizhuangtai.setText("设备状态：预泵油");
                            tvZidongbengyou.setTextColor(mContext.getResources().getColor(R.color.blue00fff));
                            bengYouValue = "1";

                            if (whatUWant.equals(BENGYOUKAIJI)) {

                                whatUWant = "";

                                if (bengYouKaiJiThread != null) {
                                    bengYouKaiJiThread.interrupt();
                                }
                            }
                            break;
                        case "7":
//                            if (run!=true){
//                                run = true;
//                                handler.post(myRunnable);
//                            }

                            tvShebeizhuangtai.setText("设备状态：预通风");
                            tvYutongfeng.setTextColor(mContext.getResources().getColor(R.color.blue00fff));
                            tongFengValue = "1";
//                            tvYuShe_WenDu.setText("当前的档位为：" + oper_dang + "档");
//                            button = rbHeaterYtfMode;
//                            PreferenceHelper.getInstance(mContext).putString(STARTSHELVES, "7");
//
//                            switchModel(rbHeaterYtfMode, 7);
//                            //档位开机
//                            if (oper_dang != null) {
//                                openMode(Integer.parseInt(oper_dang));
//                            }

                            if (whatUWant.equals(YUTONGFENGKAIJI)) {


                                whatUWant = "";
                                yuTongFengKaiJiThread.interrupt();
                            }

                            break;
                        case "9":
//                            if (run==true){
//                                run = false;
//                                handler.removeCallbacks(myRunnable);
//                            }
                            tvShebeizhuangtai.setText("设备状态：关机中");
                            // mTvWd.setText("00");
                            // showLoadSuccess();
                            //  AlertUtil.t(WindHeaterActivity.this, "正在关机，请稍候");
                            break;
                    }
                    // 驻车加热器:当前温度	3	是
                    String oper_wendu = messageData.substring(4, 6);
                    oper_wendu += "0".equals(messageData.substring(6, 7)) ? "" : "." + messageData.substring(6, 7);

                    // 水暖加热器:尾气温度 例如:-03	3	是
                    String xinhaoQiangDu = messageData.substring(7, 9);
                    String jiGeXinHao = "没有接到信号";
                    if (!StringUtils.isEmpty(xinhaoQiangDu)) {

                        int xinhao = Y.getInt(xinhaoQiangDu);
                        if (xinhao >= 0 && xinhao <= 14) {
                            jiGeXinHao = "一格信号";
                            ivXinhao.setBackgroundResource(R.mipmap.fengnuan_icon_signal1);
                        } else if (xinhao >= 15 && xinhao <= 19) {
                            jiGeXinHao = "两格信号";
                            ivXinhao.setBackgroundResource(R.mipmap.fengnuan_icon_signal2);
                        } else if (xinhao >= 20 && xinhao <= 25) {
                            jiGeXinHao = "三格信号";
                            ivXinhao.setBackgroundResource(R.mipmap.fengnuan_icon_signal3);
                        } else if (xinhao >= 26 && xinhao <= 35) {
                            jiGeXinHao = "三格信号";
                            ivXinhao.setBackgroundResource(R.mipmap.fengnuan_icon_signal4);
                        }

                        //  UIHelper.ToastMessage(mContext, jiGeXinHao+xinhao);
                    }


                    Log.i("rair", "我的当前信号: " + xinhaoQiangDu);
                    // 驻车加热器:电压->0253 = 25.3	4	是
                    String machine_voltage = Y.getInt(messageData.substring(10, 13)) + "." + messageData.substring(13, 14);
                    tvDianya.setText(machine_voltage + "V");
                    // 驻车加热器:风机转速->13245	5	是
                    String revolution = messageData.substring(14, 19);
                    // 驻车加热器:加热塞功率->0264=26.4	4	是
                    String power = messageData.substring(19, 23);
                    // 驻车加热器:油泵频率->0265=26.5	4	是
                    String frequency = messageData.substring(23, 27);
                    frequency = frequency.substring(0, 3) + "." + frequency.substring(3);
                    // 驻车加热器:入风口温度->例如:-026	4	是

                    String in_temperature = messageData.substring(27, 31);
                    tvRufengkouwendu.setText(Y.getInt(in_temperature) + "℃");
                    // 驻车加热器:出风口温度->0128	4	是
                    String out_temperature = messageData.substring(31, 35);
                    tvChufengkouwendu.setText(Y.getInt(out_temperature) + "℃");

                    String wendu = out_temperature.substring(2, 4);
                    if (kongTiaoMoshiValue.equals("1")) {
                        tvDangqianWenduOrDangwei.setText("当前温度：" + oper_wendu_now + "℃");
                    }

                    // 驻车加热器故障码->01至18	2	 标准故障码
                    String zhu_car_stoppage_no = messageData.substring(35, 37);
                    if (!StringUtils.isEmpty(zhu_car_stoppage_no)) {
                        zhu_car_stoppage_no = 0 <= zhu_car_stoppage_no.indexOf("a") ? "" : String.valueOf(Integer.parseInt(zhu_car_stoppage_no));
                    }


                    if (!StringUtils.isEmpty(zhu_car_stoppage_no)) {
                        Activity currentActivity = AppManager.getAppManager().currentActivity();
                        if (currentActivity != null) {
                            if (!currentActivity.getClass().getSimpleName().equals(DiagnosisActivity.class.getSimpleName())) {

                                if (!myCarCaoZuoDialog_notify.isShowing()) {
                                    myCarCaoZuoDialog_notify.show();

//                                    AndMqtt.getInstance().publish(new MqttPublish()
//                                            .setMsg("M692.")
//                                            .setQos(2).setRetained(false)
//                                            .setTopic(CAR_CTROL), new IMqttActionListener() {
//                                        @Override
//                                        public void onSuccess(IMqttToken asyncActionToken) {
//                                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
//                                            //  UIHelper.ToastMessage(WindHeaterActivity.this, "指令发送成功,等待服务器响应", Toast.LENGTH_SHORT);
//
//                                        }
//
//                                        @Override
//                                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
//                                            UIHelper.ToastMessage(mContext, "指令发送失败，请检查是否连接网络", Toast.LENGTH_SHORT);
//                                        }
//                                    });
                                }

                            }
                        }

                    }
                    if (messageData.length() >= 38) {
                        // 水泵状态   0 关  1 开 a 无水泵
                        String shuiBengZhuangTai = messageData.substring(37, 38);

                        switch (shuiBengZhuangTai) {
                            case "0"://0关
//                                rbHeaterPumpMode.setChecked(false);
//                                rbHeaterPumpMode.setVisibility(View.VISIBLE);
                                tvShuibeng.setTextColor(mContext.getResources().getColor(R.color.white));
                                shuiBengValue = "0";
                                if (whatUWant.equals(SHUIBENGGUANJI)) {
                                    whatUWant = "";
                                    lordingDialog.dismiss();
                                }

                                break;
                            case "1"://1开
//                                rbHeaterPumpMode.setChecked(true);
//                                rbHeaterPumpMode.setVisibility(View.VISIBLE);
                                tvShuibeng.setTextColor(mContext.getResources().getColor(R.color.blue00fff));
                                shuiBengValue = "1";
                                if (kongTiaoMoshiValue.equals("0") && dangWeiMoShiValue.equals("0") && tongFengValue.equals("0") && bengYouValue.equals("0")) {
                                    tvShebeizhuangtai.setText("设备状态：" + "水泵模式");
                                }

                                if (whatUWant.equals(SHUIBENGKAIJI)) {
                                    whatUWant = "";
                                    lordingDialog.dismiss();
                                }


                                break;
                            case "a"://无
//                                flag = false;
//                                rbHeaterPumpMode.setVisibility(View.GONE);
                                shuiBengValue = "a";
                                tvShuibeng.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);


                                break;
                        }
                    }


                    String worktime = "1";

                    if (messageData.length() >= 44) {
                        worktime = messageData.substring(38, 44);        // 风暖加热器:工作时长(小时)
                      //  worktime = worktime.replaceFirst("0", "");
                        tvGongzuoShichang.setText(worktime + "天");
                    }


                    if (messageData.length() >= 46) {
                        // 驻车加热器故障码->01至18	2	其它公司用
                        String zhu_car_stoppage_no_o = messageData.substring(44, 46);
                        zhu_car_stoppage_no_o = 0 <= zhu_car_stoppage_no_o.indexOf("a") ? "" : String.valueOf(Integer.parseInt(zhu_car_stoppage_no_o));


                    }

                    if (messageData.length() > 46) {
                        String daqiya = messageData.substring(46, 49);
                        tvDaqiya.setText(Y.getInt(daqiya) + "kpa");

                        String haiBaGaoDu = messageData.substring(49, 53);
                        tvHaibagaodu.setText(Y.getInt(haiBaGaoDu) + "m");

                        String hanYangLiang = messageData.substring(53);
                        tvHanyangliang.setText(Y.getInt(hanYangLiang) + "g/m3");
                    }


                } else if (message.type == ConstanceValue.MSG_CAR_K) {

                } else if (message.type == ConstanceValue.MSG_CAR_I) {

                    //接收到信息
                    Log.i("msg_car_i", message.content.toString());
                    String message1 = message.content.toString();
                    version = message1.substring(message1.indexOf("i") + 7, message1.indexOf("i") + 11);


                    /**
                     *     int MSG_MQTT_CONNECTCOMPLETE = 0x10045;//连接完成
                     *     int MSG_MQTT_CONNECTLOST = 0x10046;//连接丢失
                     *     int MSG_MQTT_CONNECTARRIVE = 0x10047;//收到连接
                     *     int MSG_MQTT_CONNECT_CHONGLIAN_ONSUCCESS = 0x10048;//重连成功
                     *     int MSG_MQTT_CONNECT_CHONGLIAN_ONFAILE = 0x10049;//重连失败
                     */
                } else if (message.type == MSG_MQTT_CONNECTCOMPLETE) {

                    if (waitDialog.isShowing()) {
                        waitDialog.dismiss();
                        //  setMqttZhiLing();
                    }
                    Log.i("rair", "complete");

                } else if (message.type == MSG_MQTT_CONNECTLOST) {

                    if (!waitDialog.isShowing()) {
                        waitDialog.show();
                    }
                    Log.i("rair", "complete_lost");
                } else if (message.type == MSG_MQTT_CONNECTARRIVE) {

                    if (waitDialog.isShowing()) {
                        waitDialog.dismiss();
                    }
                    Log.i("rair", "arrive");
                } else if (message.type == MSG_MQTT_CONNECT_CHONGLIAN_ONSUCCESS) {

                    if (waitDialog.isShowing()) {
                        waitDialog.dismiss();
                    }

                    Log.i("rair", "chonglian_success");

                } else if (message.type == MSG_MQTT_CONNECT_CHONGLIAN_ONFAILE) {

                    if (!waitDialog.isShowing()) {
                        waitDialog.show();
                    }
                    Log.i("rair", "chonglian_failer");
                } else if (message.type == ConstanceValue.MSG_K6111) {
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    }, ConfigValue.YANCHI);//3秒后执行Runnable中的run方法

                } else if (message.type == ConstanceValue.MSG_K6141) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SoundPoolUtils.soundPool(mContext, R.raw.yikaiji);
                            lordingDialog.dismiss();
                        }
                    }, ConfigValue.YANCHI);//3秒后执行Runnable中的run方法
                } else if (message.type == ConstanceValue.MSG_K6161) {


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SoundPoolUtils.soundPool(mContext, R.raw.yikaiji);

                        }
                    }, ConfigValue.YANCHI);//3秒后执行Runnable中的run方法
                } else if (message.type == ConstanceValue.MSG_ZHILINGMA) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lordingDialog.dismiss();
                        }
                    }, ConfigValue.YANCHI);//3秒后执行Runnable中的run方法

                } else if (message.type == ConstanceValue.MSG_N9_WEILIANJIE) {


                    TishiDialog tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                        @Override
                        public void onClickCancel(View v, TishiDialog dialog) {
                            whatUWant = "";
                            lordingDialog.dismiss();
                        }

                        @Override
                        public void onClickConfirm(View v, TishiDialog dialog) {
                            whatUWant = PANDUANZAIXIANZHUAGNTAI;

                            if (n9Thread != null) {
                                whatUWant = PANDUANZAIXIANZHUAGNTAI;
                                n9Thread.interrupt();
                                n9Thread = new N9Thread();
                                n9Thread.start();
                            }

                        }

                        @Override
                        public void onDismiss(TishiDialog dialog) {

                        }
                    });

                    tishiDialog.setTextTitle("提示：网络信号异常");
                    tishiDialog.setTextContent("请检查设备情况。1：设备是否接通电源 2：设备信号等是否闪烁 3：设备是否有损坏 4：手机时候开启网络，如已确认以上问题，请重新尝试");
                    tishiDialog.setTextConfirm("重连");
                    tishiDialog.setTextCancel("取消");
                    tishiDialog.show();

                    tvZaixian.setText("离线");

                    whatUWant = "";
                    n9Thread.interrupt();

                } else if (message.type == ConstanceValue.MSG_N9_LIANJIE) {

                } else if (message.type == ConstanceValue.MSG_DANGWEIGUANJI) {
                    lordingDialog.dismiss();
                    tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                        @Override
                        public void onClickCancel(View v, TishiDialog dialog) {
                            tishiDialog.dismiss();
                        }

                        @Override
                        public void onClickConfirm(View v, TishiDialog dialog) {

                            finish();
                        }

                        @Override
                        public void onDismiss(TishiDialog dialog) {

                        }
                    });

                    tishiDialog.setTextTitle("离线");
                    tishiDialog.setTextContent("检查您的网络是否畅通或加热器是否在线，并退出当前页重新尝试");
                    tishiDialog.setCancelable(false);
                    FengNuanActivity.this.tishiDialog.setTextConfirm("退出重试");
                    tishiDialog.setTextCancel("取消");

                    tishiDialog.show();

                    tvZaixian.setText("离线");


                    whatUWant = "";
                    if (dangWeiGuanJiThread != null) {
                        dangWeiGuanJiThread.interrupt();
                    }

                } else if (message.type == ConstanceValue.MSG_DANGWEIKAIJI) {
                    lordingDialog.dismiss();

                    tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                        @Override
                        public void onClickCancel(View v, TishiDialog dialog) {
                            tishiDialog.dismiss();
                        }

                        @Override
                        public void onClickConfirm(View v, TishiDialog dialog) {
                            finish();

                        }

                        @Override
                        public void onDismiss(TishiDialog dialog) {

                        }
                    });

                    tishiDialog.setTextTitle("离线");
                    tishiDialog.setTextContent("检查您的网络是否畅通或加热器是否在线，并退出当前页重新尝试");
                    tishiDialog.setCancelable(false);
                    FengNuanActivity.this.tishiDialog.setTextConfirm("退出重试");
                    tishiDialog.setTextCancel("取消");

                    tishiDialog.show();

                    tvZaixian.setText("离线");


                    whatUWant = "";
                    if (dangWeiKaiJiThread != null) {
                        dangWeiKaiJiThread.interrupt();
                    }
                } else if (message.type == ConstanceValue.MSG_KONGTIAOKAIJI) {

                    lordingDialog.dismiss();

                    tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                        @Override
                        public void onClickCancel(View v, TishiDialog dialog) {
                            tishiDialog.dismiss();
                        }

                        @Override
                        public void onClickConfirm(View v, TishiDialog dialog) {
                            finish();

                        }

                        @Override
                        public void onDismiss(TishiDialog dialog) {

                        }
                    });

                    tishiDialog.setTextTitle("离线");
                    tishiDialog.setTextContent("检查您的网络是否畅通或加热器是否在线，并退出当前页重新尝试");
                    tishiDialog.setCancelable(false);
                    FengNuanActivity.this.tishiDialog.setTextConfirm("退出重试");
                    tishiDialog.setTextCancel("取消");

                    tishiDialog.show();

                    tvZaixian.setText("离线");


                    whatUWant = "";
                    if (dangWeiKaiJiThread != null) {
                        dangWeiKaiJiThread.interrupt();
                    }
                } else if (message.type == ConstanceValue.MSG_KONGTIAOGUANJI) {
                    lordingDialog.dismiss();

                    tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                        @Override
                        public void onClickCancel(View v, TishiDialog dialog) {
                            tishiDialog.dismiss();
                        }

                        @Override
                        public void onClickConfirm(View v, TishiDialog dialog) {
                            finish();

                        }

                        @Override
                        public void onDismiss(TishiDialog dialog) {

                        }
                    });

                    tishiDialog.setTextTitle("提示");
                    tishiDialog.setTextContent("检查您的网络是否畅通或加热器是否在线，并退出当前页重新尝试");
                    tishiDialog.setCancelable(false);
                    FengNuanActivity.this.tishiDialog.setTextConfirm("退出重试");
                    tishiDialog.setTextCancel("取消");

                    tishiDialog.show();

                    tvZaixian.setText("离线");


                    whatUWant = "";
                    if (kongTiaoGuanJiThread != null) {
                        kongTiaoGuanJiThread.interrupt();
                    }
                } else if (message.type == ConstanceValue.MSG_C_P) {

                    UIHelper.ToastMessage(mContext, message.content.toString());
                } else if (message.type == ConstanceValue.MSG_YUTONGFENGKAIJI) {

                    lordingDialog.dismiss();

                    tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                        @Override
                        public void onClickCancel(View v, TishiDialog dialog) {
                            tishiDialog.dismiss();
                        }

                        @Override
                        public void onClickConfirm(View v, TishiDialog dialog) {
                            finish();

                        }

                        @Override
                        public void onDismiss(TishiDialog dialog) {

                        }
                    });

                    tishiDialog.setTextTitle("提示");
                    tishiDialog.setTextContent("检查您的网络是否畅通或加热器是否通电，并退出当前页重新尝试");
                    tishiDialog.setCancelable(false);
                    FengNuanActivity.this.tishiDialog.setTextConfirm("退出重试");
                    tishiDialog.setTextCancel("取消");

                    tishiDialog.show();

                    tvZaixian.setText("离线");
                    whatUWant = "";
                    if (yuTongFengKaiJiThread != null) {
                        yuTongFengKaiJiThread.interrupt();
                    }
                } else if (message.type == ConstanceValue.MSG_YUTONGFENGGUANJI) {

                    if (tishiDialog != null) {
                        if (tishiDialog.isShowing()) {
                            tishiDialog.dismiss();
                        }
                    }

                    tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                        @Override
                        public void onClickCancel(View v, TishiDialog dialog) {
                            tishiDialog.dismiss();
                        }

                        @Override
                        public void onClickConfirm(View v, TishiDialog dialog) {
                            finish();

                        }

                        @Override
                        public void onDismiss(TishiDialog dialog) {

                        }
                    });

                    tishiDialog.setTextTitle("提示");
                    tishiDialog.setTextContent("检查您的网络是否畅通或加热器是否在线，并退出当前页重新尝试");
                    tishiDialog.setCancelable(false);
                    FengNuanActivity.this.tishiDialog.setTextConfirm("退出重试");
                    tishiDialog.setTextCancel("取消");

                    tishiDialog.show();

                    tvZaixian.setText("离线");


                    whatUWant = "";
                    if (kongTiaoGuanJiThread != null) {
                        kongTiaoGuanJiThread.interrupt();
                    }

                } else if (message.type == ConstanceValue.MSG_BEGNYOUKAIJI) {
                    if (lordingDialog != null) {
                        if (lordingDialog.isShowing()) {
                            lordingDialog.dismiss();
                        }
                    }
                    if (tishiDialog != null) {
                        if (tishiDialog.isShowing()) {
                            tishiDialog.dismiss();
                        }
                    }

                    tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                        @Override
                        public void onClickCancel(View v, TishiDialog dialog) {
                            tishiDialog.dismiss();
                        }

                        @Override
                        public void onClickConfirm(View v, TishiDialog dialog) {
                            finish();

                        }

                        @Override
                        public void onDismiss(TishiDialog dialog) {

                        }
                    });

                    tishiDialog.setTextTitle("提示");
                    tishiDialog.setTextContent("检查您的网络是否畅通或加热器是否在线，并退出当前页重新尝试");
                    tishiDialog.setCancelable(false);
                    FengNuanActivity.this.tishiDialog.setTextConfirm("退出重试");
                    tishiDialog.setTextCancel("取消");

                    tishiDialog.show();

                    tvZaixian.setText("离线");


                    whatUWant = "";
                    if (bengYouKaiJiThread != null) {
                        bengYouKaiJiThread.interrupt();
                    }
                } else if (message.type == ConstanceValue.MSG_BENGYOUGUANJI) {

                    if (lordingDialog != null) {
                        if (lordingDialog.isShowing()) {
                            lordingDialog.dismiss();
                        }
                    }
                    if (tishiDialog != null) {
                        if (tishiDialog.isShowing()) {
                            tishiDialog.dismiss();
                        }
                    }

                    tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                        @Override
                        public void onClickCancel(View v, TishiDialog dialog) {
                            tishiDialog.dismiss();
                        }

                        @Override
                        public void onClickConfirm(View v, TishiDialog dialog) {
                            finish();

                        }

                        @Override
                        public void onDismiss(TishiDialog dialog) {

                        }
                    });

                    tishiDialog.setTextTitle("提示");
                    tishiDialog.setTextContent("检查您的网络是否畅通或加热器是否在线，并退出当前页重新尝试");
                    tishiDialog.setCancelable(false);
                    FengNuanActivity.this.tishiDialog.setTextConfirm("退出重试");
                    tishiDialog.setTextCancel("取消");

                    tishiDialog.show();

                    tvZaixian.setText("离线");


                    whatUWant = "";
                    if (bengYouGuanJiThread != null) {
                        bengYouGuanJiThread.interrupt();
                    }
                } else if (message.type == ConstanceValue.MSG_CAR_Z) {
                    //Z2201802012


                    if (StringUtils.isEmpty(banBenHao)) {
                        String str = String.valueOf(message.content);

                        if (str != null) {
                            if (str.length() > 3) {
                                banBenHao = str.substring(2, 6);
                                Log.i("banbenhao", banBenHao);

                                if (banBenHao != null) {
                                    if (banBenHao.equals("2020")) {
                                        if (xunHuanN == null) {
                                            Log.i("xunhuancishu", "开启循环线程");
                                            //  UIHelper.ToastMessage(mContext, "xunhuan xunhuan");
                                            xunHuanNFlag = true;
                                            xunHuanN = new XunHuanN();
                                            xunHuanN.start();
                                        }
                                    }

                                }
                            }
                        }

                    }


                }
            }
        }));
    }

    String banBenHao = null;

    @Override
    public int getContentViewResId() {
        return R.layout.layout_fengnuan;
    }

    /**
     * 用于其他Activty跳转到该Activity
     *
     * @param context simKaIdFlag 1有 2没有
     */
    public static void actionStart(Context context, String simKaIdFlag) {
        Intent intent = new Intent(context, FengNuanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("simKaIdFlag", simKaIdFlag);
        context.startActivity(intent);
    }


    public void setMqttZhiLing() {

        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(CAR_NOTIFY)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "notify:  " + CAR_NOTIFY + "CAR_NOTIFY 我是在类里面订阅的");


                MyApplication.mqttDingyue.add(CAR_NOTIFY);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("CAR_NOTIFY", "(MainActivity.java:68)-onFailure:-&gt;订阅失败");
            }
        });


//        AndMqtt.getInstance().publish(new MqttPublish()
//                .setMsg("P.")
//                .setQos(2).setRetained(false)
//                .setTopic(CAR_CTROL), new IMqttActionListener() {
//            @Override
//            public void onSuccess(IMqttToken asyncActionToken) {
//                Log.i("Rair", "(CAR_NOTIFY.java:79)-onSuccess:-&gt;发布成功" + "k001 我是在类里面订阅的");
//
//            }
//
//            @Override
//            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
//            }
//        });

        //获得车辆的实时数据和基本信息
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(CARBOX_GETNOW)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "订阅成功 carbox_getnow:  " + CARBOX_GETNOW + " CARBOX_GETNOW 我是在类里面订阅的");
                MyApplication.mqttDingyue.add(CARBOX_GETNOW);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:68)-onFailure:-&gt;订阅失败");
            }
        });


        //控制车辆
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(CAR_CTROL)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", " Rair用户订阅成功" + "wit/app/" + of_user_id + " CAR_CTROL 我是在类里面订阅的");
                MyApplication.mqttDingyue.add(CAR_CTROL);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:68)-onFailure:-&gt;订阅失败");
            }


        });


        whatUWant = PANDUANZAIXIANZHUAGNTAI;
        n9Thread = new N9Thread();
        n9Thread.start();


//        XunHuanN xunHuanN = new XunHuanN();
//        xunHuanN.start();
        //查询车辆详情数据
        // requestData();

        // HeaterMqttService.mqttService.publish("M512.", HeaterMqttService.TOPIC_SERVER_ORDER, 2, false);
//        AndMqtt.getInstance().publish(new MqttPublish()
//                .setMsg("M512.")
//                .setQos(2).setRetained(false)
//                .setTopic(CAR_CTROL), new IMqttActionListener() {
//            @Override
//            public void onSuccess(IMqttToken asyncActionToken) {
//                Log.i("Rair", "(CAR_NOTIFY.java:79)-onSuccess:-&gt;发布成功" + "M512 我是在类里面订阅的");
//            }
//
//            @Override
//            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        whatUWant = "";
        if (n9Thread != null) {
            n9Thread.interrupt();
        }
        if (dangWeiGuanJiThread != null) {
            dangWeiGuanJiThread.interrupt();
        }

        if (dangWeiKaiJiThread != null) {
            dangWeiKaiJiThread.interrupt();
        }

        if (kongTiaoKaiJiThread != null) {
            kongTiaoKaiJiThread.interrupt();
        }
        if (kongTiaoGuanJiThread != null) {
            kongTiaoGuanJiThread.interrupt();
        }

        if (yuTongFengGuanJiThread != null) {
            yuTongFengGuanJiThread.interrupt();
        }
        if (yuTongFengKaiJiThread != null) {
            yuTongFengKaiJiThread.interrupt();
        }

        if (bengYouKaiJiThread != null) {
            bengYouKaiJiThread.interrupt();
        }

        if (bengYouGuanJiThread != null) {
            bengYouGuanJiThread.interrupt();
        }
        xunHuanNFlag = false;
        if (xunHuanN != null) {
            xunHuanN.interrupt();
        }
        PreferenceHelper.getInstance(mContext).removeKey(App.CHOOSE_KONGZHI_XIANGMU);
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

        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CARBOX_GETNOW), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(MainActivity.java:93)-onSuccess:-&gt;取消订阅成功");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:98)-onFailure:-&gt;取消订阅失败");
            }
        });

        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(CAR_CTROL), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(MainActivity.java:93)-onSuccess:-&gt;取消订阅成功");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.i("Rair", "(MainActivity.java:98)-onFailure:-&gt;取消订阅失败");
            }
        });


        for (int i = 0; i < MyApplication.mqttDingyue.size(); i++) {
            if (MyApplication.mqttDingyue.get(i).equals(CAR_NOTIFY)) {
                MyApplication.mqttDingyue.remove(i);
            }
        }

        for (int i = 0; i < MyApplication.mqttDingyue.size(); i++) {
            if (MyApplication.mqttDingyue.get(i).equals(CARBOX_GETNOW)) {
                MyApplication.mqttDingyue.remove(i);
            }
        }

        for (int i = 0; i < MyApplication.mqttDingyue.size(); i++) {
            if (MyApplication.mqttDingyue.get(i).equals(CAR_CTROL)) {
                MyApplication.mqttDingyue.remove(i);
            }
        }
//        handler.removeCallbacks(myRunnable);
//        run = false;
    }

    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {
            case R.id.iv_dangweimoshi:
//                if (run==true){
//                    run = false;
//                    handler.removeCallbacks(myRunnable);
//                }

                if (kongTiaoMoshiValue.equals("1") || bengYouValue.equals("1") || tongFengValue.equals("1")) {
                    UIHelper.ToastMessage(mContext, "请关机后重新以档位模式启动");
                    return false;
                }
                if (dangWeiMoShiValue.equals("0")) {
                    whatUWant = DANGWEIKAIJI;
                    dangWeiKaiJiThread = new DangWeiKaiJiThread();
                    dangWeiKaiJiThread.start();

                } else if (dangWeiMoShiValue.equals("1")) {
                    whatUWant = DANGWEIGUANJI;

                    dangWeiGuanJiThread = new DangWeiGuanJiThread();
                    dangWeiGuanJiThread.start();

                }
                break;
            case R.id.iv_kongtiaomoshi:
//                if (run==true){
//                    run = false;
//                    handler.removeCallbacks(myRunnable);
//                }
                if (dangWeiMoShiValue.equals("1") || bengYouValue.equals("1") || tongFengValue.equals("1")) {
                    UIHelper.ToastMessage(mContext, "请关机后重新以空调模式启动");
                    return false;
                }
                if (kongTiaoMoshiValue.equals("0")) {
                    whatUWant = KONGTIAOKAIJI;
                    kongTiaoKaiJiThread = new KongTiaoKaiJiThread();
                    kongTiaoKaiJiThread.start();

                } else if (kongTiaoMoshiValue.equals("1")) {

                    whatUWant = KONGTIAOGUANJI;
                    kongTiaoGuanJiThread = new KongTiaoGuanJiThread();
                    kongTiaoGuanJiThread.start();


                }

                break;
        }


        return true;
    }

    private void qieHuanDangWei(int dangValue) {

        if (dangQianDangWei.equals(String.valueOf(dangValue))) {
            return;
        }
        tvShedingWenduOrDangwei.setText("设定档位：" + String.valueOf(dangValue) + "档");

        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("M62" + dangValue + ".")
                .setQos(2)
                .setTopic(CAR_CTROL).setRetained(false), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                lordingDialog.setTextMsg("档位切换中,请稍后...");
                lordingDialog.show();
                PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k62" + dangValue + "1.");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                UIHelper.ToastMessage(mContext, "切换档位失败:" + exception.getMessage(), Toast.LENGTH_SHORT);

            }
        });

    }

    private void qieHuanWenDu(int myValue) {
        tvShedingWenduOrDangwei.setText("设定温度：" + myValue + "℃");
        if (myValue > 0 && myValue <= 9) {
            String value = String.valueOf(myValue);
            Log.i("myValue", value);
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M65" + value + ".")
                    .setQos(2)
                    .setTopic(CAR_CTROL).setRetained(false), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                    PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k66" + value + "1.");
                    lordingDialog.setTextMsg("温度调节中,请稍后");
                    lordingDialog.show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败" + exception.getMessage());
                }
            });
        } else if (myValue > 9 && myValue < 20) {
            String value = String.valueOf(myValue).substring(1, 2);
            Log.i("myValue", value);
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M66" + value + ".")
                    .setQos(2)
                    .setTopic(CAR_CTROL).setRetained(false), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                    PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k66" + value + "1.");
                    lordingDialog.setTextMsg("温度调节中,请稍后");
                    lordingDialog.show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败" + exception.getMessage());
                }
            });

        } else if (myValue > 20 && myValue < 30) {
            String value = String.valueOf(myValue).substring(1, 2);
            Log.i("myValue", value);
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M67" + value + ".")
                    .setQos(2)
                    .setTopic(CAR_CTROL).setRetained(false), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                    PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k67" + value + "1.");
                    lordingDialog.setTextMsg("温度调节中,请稍后");
                    lordingDialog.show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败" + exception.getMessage());
                }
            });
        } else if (myValue > 20 && myValue <= 32) {
            String value = String.valueOf(myValue).substring(1, 2);
            Log.i("myValue", value);
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M68" + value + ".")
                    .setQos(2)
                    .setTopic(CAR_CTROL).setRetained(false), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                    PreferenceHelper.getInstance(mContext).putString(ConfigValue.ZHILINGMA, "k68" + value + "1.");
                    lordingDialog.setTextMsg("温度调节中,请稍后");
                    lordingDialog.show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败" + exception.getMessage());
                }
            });
        }
    }


    private class N9Thread extends Thread {
        private int i = 0;

        public void run() {
            while (whatUWant.equals(PANDUANZAIXIANZHUAGNTAI)) {

                try {
                    if (i == 6) {

                        whatUWant = "";
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_N9_WEILIANJIE;
                        RxBus.getDefault().sendRx(n);

                    }
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("N.")
                            .setQos(2)
                            .setTopic(CAR_CTROL)
                            .setRetained(false), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功" + " N9 我是在类里面订阅的");


                            //  UIHelper.ToastMessage(mContext, "第" + String.valueOf(i) + "次发送");
                            i = i + 1;


                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                        }
                    });

                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;

                }
            }
        }
    }

    private class DangWeiGuanJiThread extends Thread {
        private int i = 0;

        public void run() {
            while (whatUWant.equals(DANGWEIGUANJI)) {

                try {
                    if (i == 3) {

                        whatUWant = "";
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_DANGWEIGUANJI;
                        RxBus.getDefault().sendRx(n);

                    }

                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M613.")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                            //  UIHelper.ToastMessage(WindHeaterActivity.this, "指令发送成功,等待服务器响应", Toast.LENGTH_SHORT);


                            if (i == 0) {
                                //   lordingDialog.setTextMsg("正在关机请稍后...");
                                //   lordingDialog.show();
                                SoundPoolUtils.soundPool(mContext, R.raw.guanjizhong);

                                ivKaiji.setVisibility(View.GONE);
                                ivGuanji.setVisibility(View.VISIBLE);

                                ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_nor);
                                ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_nor);

                                tvYutongfeng.setTextColor(mContext.getResources().getColor(R.color.gray999999));
                                tvZidongbengyou.setTextColor(mContext.getResources().getColor(R.color.gray999999));

                            }
                            //UIHelper.ToastMessage(mContext, "第" + String.valueOf(i) + "次发送");
                            i = i + 1;

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                            UIHelper.ToastMessage(mContext, "指令发送失败，请检查是否连接网络", Toast.LENGTH_SHORT);
                        }
                    });


                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;

                }
            }
        }
    }


    private class DangWeiKaiJiThread extends Thread {
        private int i = 0;

        public void run() {
            while (whatUWant.equals(DANGWEIKAIJI)) {

                try {
                    if (i == 3) {

                        whatUWant = "";
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_DANGWEIKAIJI;
                        RxBus.getDefault().sendRx(n);

                    }
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M61" + "1" + ".")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                            //UIHelper.ToastMessage(WindHeaterActivity.this, "指令发送成功,等待服务器响应", Toast.LENGTH_SHORT);

                            if (i == 0) {
                                UIHelper.ToastMessage(mContext, "正在以档位模式");
//                                lordingDialog.setTextMsg("正在以" + "档位模式" + "开机请稍后...");
//                                lordingDialog.show();
                                SoundPoolUtils.soundPool(mContext, R.raw.dangwei);

                                ivKaiji.setVisibility(View.VISIBLE);
                                ivGuanji.setVisibility(View.GONE);

                                ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_sel);
                                ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_nor);

                                tvZuidiwendu.setText("1档");
                                tvZuigaowendu.setText("5档");

                                dangWeiMoShiValue = "1";


                                seekBar1.setVisibility(View.VISIBLE);
                                seekBarKongtiao.setVisibility(View.GONE);
                            }

                            //UIHelper.ToastMessage(mContext, "第" + String.valueOf(i) + "次发送");
                            i = i + 1;


                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                            UIHelper.ToastMessage(mContext, "指令发送失败", Toast.LENGTH_SHORT);
                        }
                    });
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;

                }
            }
        }
    }


    private class KongTiaoKaiJiThread extends Thread {
        private int i = 0;

        public void run() {
            while (whatUWant.equals(KONGTIAOKAIJI)) {

                try {
                    if (i == 3) {

                        whatUWant = "";
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_KONGTIAOKAIJI;
                        RxBus.getDefault().sendRx(n);

                    }
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M61" + "2" + ".")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                            //UIHelper.ToastMessage(WindHeaterActivity.this, "指令发送成功,等待服务器响应", Toast.LENGTH_SHORT);

                            if (i == 0) {
                                //  lordingDialog.setTextMsg("正在以" + "空调模式" + "开机请稍后...");
                                //  lordingDialog.show();
                                SoundPoolUtils.soundPool(mContext, R.raw.kongtiao);

                                ivKaiji.setVisibility(View.VISIBLE);
                                ivGuanji.setVisibility(View.GONE);

                                seekBar1.setVisibility(View.GONE);
                                seekBarKongtiao.setVisibility(View.VISIBLE);
                                tvZuidiwendu.setText("0℃");
                                tvZuigaowendu.setText("32℃");
                                tvShebeizhuangtai.setText("设备状态：空调模式");
                                ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_nor);
                                ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_sel);
                            }

                            //UIHelper.ToastMessage(mContext, "第" + String.valueOf(i) + "次发送");
                            i = i + 1;


                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                            UIHelper.ToastMessage(mContext, "指令发送失败", Toast.LENGTH_SHORT);
                        }
                    });
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;

                }
            }
        }
    }


    private class KongTiaoGuanJiThread extends Thread {
        private int i = 0;

        public void run() {
            while (whatUWant.equals(KONGTIAOGUANJI)) {

                try {
                    if (i == 3) {

                        whatUWant = "";
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_KONGTIAOGUANJI;
                        RxBus.getDefault().sendRx(n);

                    }
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M613.")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                            //  UIHelper.ToastMessage(WindHeaterActivity.this, "指令发送成功,等待服务器响应", Toast.LENGTH_SHORT);


                            if (i == 0) {
                                //  lordingDialog.setTextMsg("正在关机请稍后...");
                                //  lordingDialog.show();
                                whatUWant = KONGTIAOGUANJI;
                                SoundPoolUtils.soundPool(mContext, R.raw.guanjizhong);

                                ivKaiji.setVisibility(View.GONE);
                                ivGuanji.setVisibility(View.VISIBLE);

                                ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_nor);
                                ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_nor);
                                tvYutongfeng.setTextColor(mContext.getResources().getColor(R.color.gray999999));
                                tvZidongbengyou.setTextColor(mContext.getResources().getColor(R.color.gray999999));

                            }

                            i = i + 1;
                            //UIHelper.ToastMessage(mContext, "第" + String.valueOf(i) + "次发送");
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                            UIHelper.ToastMessage(mContext, "指令发送失败，请检查是否连接网络", Toast.LENGTH_SHORT);
                        }
                    });
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;

                }
            }
        }
    }


    private class YuTongFengKaiJiThread extends Thread {
        private int i = 0;

        public void run() {
            while (whatUWant.equals(YUTONGFENGKAIJI)) {

                try {
                    if (i == 3) {

                        whatUWant = "";
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_YUTONGFENGKAIJI;
                        RxBus.getDefault().sendRx(n);

                    }
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M61" + "7" + ".")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                            //UIHelper.ToastMessage(WindHeaterActivity.this, "指令发送成功,等待服务器响应", Toast.LENGTH_SHORT);
//                            lordingDialog.setTextMsg("正在开启预通风模式...");
//                            lordingDialog.show();
//                            SoundPoolUtils.soundPool(mContext, R.raw.yutongfeng);
//                            whatUWant = YUTONGFENGKAIJI;

                            if (i == 0) {

                                SoundPoolUtils.soundPool(mContext, R.raw.yutongfeng);
                                whatUWant = YUTONGFENGKAIJI;

                                ivKaiji.setVisibility(View.GONE);
                                ivGuanji.setVisibility(View.VISIBLE);

                                ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_nor);
                                ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_nor);

                                tvYutongfeng.setTextColor(mContext.getResources().getColor(R.color.gray999999));
                                tvZidongbengyou.setTextColor(mContext.getResources().getColor(R.color.gray999999));

                                SoundPoolUtils.soundPool(mContext, R.raw.yikaiji);
                                //  SoundPoolUtils.soundPool(mContext, R.raw.yikaiji);
                                //开启预通风
                                tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                                    @Override
                                    public void onClickCancel(View v, TishiDialog dialog) {

                                    }

                                    @Override
                                    public void onClickConfirm(View v, TishiDialog dialog) {
                                        if (tongFengValue.equals("1")) {
                                            //关闭预通风
                                            tishiDialog.setTextConfirm("正在停止...");
                                            whatUWant = YUTONGFENGGUANJI;
                                            yuTongFengGuanJiThread = new YuTongFengGuanJiThread();
                                            yuTongFengGuanJiThread.start();

                                        }

                                    }

                                    @Override
                                    public void onDismiss(TishiDialog dialog) {

                                    }
                                });
                                tishiDialog.setTextTitle("预通风模式运行中");

                                tishiDialog.setTextCancel("");
                                tishiDialog.setDismissAfterClick(false);
                                tishiDialog.setTextContent("正在通风");
                                tishiDialog.setTextConfirm("开启中");
                                tishiDialog.setCancelable(true);
                                tishiDialog.show();

                            }
                            i = i + 1;
                            //UIHelper.ToastMessage(mContext, "第" + String.valueOf(i) + "次发送");

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                            UIHelper.ToastMessage(mContext, "指令发送失败", Toast.LENGTH_SHORT);
                        }
                    });
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;

                }
            }
        }
    }

    private class YuTongFengGuanJiThread extends Thread {
        private int i = 0;

        public void run() {
            while (whatUWant.equals(YUTONGFENGGUANJI)) {

                try {
                    if (i == 3) {
                        whatUWant = "";
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_YUTONGFENGGUANJI;
                        RxBus.getDefault().sendRx(n);
                    }
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M613.")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");

                            if (i == 0) {
                                ivKaiji.setVisibility(View.GONE);
                                ivGuanji.setVisibility(View.VISIBLE);

                                ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_nor);
                                ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_nor);

                                tvYutongfeng.setTextColor(mContext.getResources().getColor(R.color.gray999999));
                                tvZidongbengyou.setTextColor(mContext.getResources().getColor(R.color.gray999999));


                            }
                            i = i + 1;
                            //UIHelper.ToastMessage(mContext, "第" + String.valueOf(i) + "次发送");


                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                            UIHelper.ToastMessage(mContext, "指令发送失败，请检查是否连接网络", Toast.LENGTH_SHORT);
                        }
                    });
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;

                }
            }
        }
    }

    private class BengYouKaiJiThread extends Thread {
        private int i = 0;

        public void run() {
            while (whatUWant.equals(BENGYOUKAIJI)) {

                try {
                    if (i == 3) {
                        whatUWant = "";
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_BEGNYOUKAIJI;
                        RxBus.getDefault().sendRx(n);
                    }
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M61" + "6" + ".")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                            //UIHelper.ToastMessage(WindHeaterActivity.this, "指令发送成功,等待服务器响应", Toast.LENGTH_SHORT);
                            if (i == 0) {
                                SoundPoolUtils.soundPool(mContext, R.raw.yubengyou);
                                whatUWant = BENGYOUKAIJI;

                                ivKaiji.setVisibility(View.GONE);
                                ivGuanji.setVisibility(View.VISIBLE);

                                ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_nor);
                                ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_nor);

                                tvYutongfeng.setTextColor(mContext.getResources().getColor(R.color.gray999999));
                                tvZidongbengyou.setTextColor(mContext.getResources().getColor(R.color.gray999999));


                                //  SoundPoolUtils.soundPool(mContext, R.raw.yikaiji);
                                //开启泵油
                                tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                                    @Override
                                    public void onClickCancel(View v, TishiDialog dialog) {

                                    }

                                    @Override
                                    public void onClickConfirm(View v, TishiDialog dialog) {
                                        if (bengYouValue.equals("1")) {
                                            //关闭泵油
                                            tishiDialog.setTextConfirm("正在停止...");
                                            whatUWant = BENGYOUGUANJI;
                                            bengYouGuanJiThread = new BengYouGuanJiThread();
                                            bengYouGuanJiThread.start();
                                        }

                                    }

                                    @Override
                                    public void onDismiss(TishiDialog dialog) {

                                    }
                                });
                                tishiDialog.setTextTitle("预泵油模式运行中");
                                tishiDialog.setTextCancel("");
                                tishiDialog.setDismissAfterClick(false);
                                tishiDialog.setTextContent("正在泵油");
                                tishiDialog.setTextConfirm("停止泵油");
                                tishiDialog.setCancelable(true);
                                tishiDialog.show();
                            }
                            i = i + 1;
                            //UIHelper.ToastMessage(mContext, "第" + String.valueOf(i) + "次发送");
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                            UIHelper.ToastMessage(mContext, "指令发送失败", Toast.LENGTH_SHORT);
                        }
                    });
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;

                }
            }
        }
    }

    private class BengYouGuanJiThread extends Thread {
        private int i = 0;

        public void run() {
            while (whatUWant.equals(BENGYOUGUANJI)) {

                try {
                    if (i == 3) {
                        whatUWant = "";
                        Notice n = new Notice();
                        n.type = ConstanceValue.MSG_BENGYOUGUANJI;
                        RxBus.getDefault().sendRx(n);

                    }
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("M613.")
                            .setQos(2).setRetained(false)
                            .setTopic(CAR_CTROL), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功");
                            //  UIHelper.ToastMessage(WindHeaterActivity.this, "指令发送成功,等待服务器响应", Toast.LENGTH_SHORT);

                            if (i == 0) {
                                ivKaiji.setVisibility(View.GONE);
                                ivGuanji.setVisibility(View.VISIBLE);

                                ivDangweimoshi.setBackgroundResource(R.mipmap.fengnuan_button_dangwei_nor);
                                ivKongtiaomoshi.setBackgroundResource(R.mipmap.fengnuan_button_kongtiao_nor);

                                tvYutongfeng.setTextColor(mContext.getResources().getColor(R.color.gray999999));
                                tvZidongbengyou.setTextColor(mContext.getResources().getColor(R.color.gray999999));
                            }

                            i = i + 1;
                            // UIHelper.ToastMessage(mContext, "第" + String.valueOf(i) + "次发送");
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                            UIHelper.ToastMessage(mContext, "指令发送失败，请检查是否连接网络", Toast.LENGTH_SHORT);
                        }
                    });
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;

                }
            }
        }
    }


    private int xunHuanCiShu = 0;
    private boolean xunHuanNFlag = true;

    private class XunHuanN extends Thread {

        public void run() {
            while (xunHuanNFlag) {
//                UIHelper.ToastMessage(mContext,"正在循环执行");
                //  Log.i("xunhuancishu", "循环发送第" + xunHuanCiShu + "次");
                try {
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("N.")
                            .setQos(2)
                            .setTopic(CAR_CTROL)
                            .setRetained(false), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            //Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功" + " N9 我是在类里面订阅的");
                            xunHuanCiShu = xunHuanCiShu + 1;
                            Log.i("xunhuancishu", "循环发送第" + xunHuanCiShu + "次");
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            //Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
                        }
                    });
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }


    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }
    //    private boolean run = true;
//    private int count = 0;
//
//    private Handler handler = new Handler();

//    private Runnable myRunnable = new Runnable() {
//        public void run() {
//
//            if (run) {
//                handler.postDelayed(this, 1000);
//                count++;
//
//            }
//            if (count % 5 == 0 || count == 1) {
//                AndMqtt.getInstance().publish(new MqttPublish()
//                        .setMsg("N.")
//                        .setQos(2)
//                        .setTopic(CAR_CTROL)
//                        .setRetained(false), new IMqttActionListener() {
//                    @Override
//                    public void onSuccess(IMqttToken asyncActionToken) {
//                        //Log.i("Rair", "(MainActivity.java:79)-onSuccess:-&gt;发布成功" + " N9 我是在类里面订阅的");
//                        xunHuanCiShu = xunHuanCiShu + 1;
//                        // Log.i("xunhuancishu", "循环发送第" + xunHuanCiShu + "次");
//                    }
//
//                    @Override
//                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                        //Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;发布失败");
//                    }
//                });
//            }
//
//
//           // UIHelper.ToastMessage(mContext, "第" + count + "次执行,余数：" + count % 10);
//
//
//        }
//    };


}
