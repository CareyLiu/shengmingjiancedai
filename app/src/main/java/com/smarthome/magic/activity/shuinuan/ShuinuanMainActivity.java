package com.smarthome.magic.activity.shuinuan;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;
import com.rairmmd.andmqtt.MqttUnSubscribe;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.FengnuandishiActivity;
import com.smarthome.magic.activity.SheBeiSetActivity;
import com.smarthome.magic.activity.shuinuan.dialog.GuzhangDialog;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.config.MyApplication;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.util.DoMqttValue;
import com.smarthome.magic.util.SoundPoolUtils;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ShuinuanMainActivity extends ShuinuanBaseNewActivity implements View.OnLongClickListener {

    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.rl_set)
    RelativeLayout rl_set;
    @BindView(R.id.iv_shebeima)
    ImageView iv_shebeima;
    @BindView(R.id.tv_dingshi)
    TextView tv_dingshi;
    @BindView(R.id.iv_xinhao)
    ImageView iv_xinhao;
    @BindView(R.id.tv_zaixian)
    TextView tv_zaixian;
    @BindView(R.id.tv_fuwutianshushengyu)
    TextView tv_fuwutianshushengyu;
    @BindView(R.id.tv_shebei_state)
    TextView tv_shebei_state;
    @BindView(R.id.tv_shebei_youxiaoqi)
    TextView tv_shebei_youxiaoqi;
    @BindView(R.id.iv_shuinuan_kaijie)
    ImageView iv_shuinuan_kaijie;
    @BindView(R.id.tv_shuinuan_kaiji)
    TextView tv_shuinuan_kaiji;
    @BindView(R.id.rv_shuinuan_kaiji)
    RelativeLayout rv_shuinuan_kaiji;
    @BindView(R.id.iv_shuinuan_guanji)
    ImageView iv_shuinuan_guanji;
    @BindView(R.id.tv_shuinuan_guanji)
    TextView tv_shuinuan_guanji;
    @BindView(R.id.rv_shuinuan_guanji)
    RelativeLayout rv_shuinuan_guanji;
    @BindView(R.id.tv_shuinuan_shuibeng)
    TextView tv_shuinuan_shuibeng;
    @BindView(R.id.tv_shuinuan_youbeng)
    TextView tv_shuinuan_youbeng;
    @BindView(R.id.tv_wendu_yushe)
    TextView tv_wendu_yushe;
    @BindView(R.id.tv_wendu_dangqian)
    TextView tv_wendu_dangqian;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.tv_dianya)
    TextView tv_dianya;
    @BindView(R.id.tv_jinshuikou_wendu)
    TextView tv_jinshuikou_wendu;
    @BindView(R.id.tv_chushuikou_wendu)
    TextView tv_chushuikou_wendu;
    @BindView(R.id.tv_haibagaodu)
    TextView tv_haibagaodu;
    @BindView(R.id.tv_hanyangliang)
    TextView tv_hanyangliang;
    @BindView(R.id.tv_daqiya)
    TextView tv_daqiya;
    @BindView(R.id.iv_heater_host)
    ImageView iv_heater_host;
    @BindView(R.id.ll_dingshi)
    LinearLayout llDingshi;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private AnimationDrawable animationDrawable;
    private GuzhangDialog guzhangDialog;

    private String sn_state;     //????????????
    private String shuibeng_state;
    private String youbeng_state;
    private String yushewendu;      //????????????
    private String sim_ccid_save_type;
    private String xinhaoStr;

    private boolean isFirst;
    private boolean isKaiji;
    private boolean isCanGetNs;//?????????????????????   false ????????????true ?????????
    private int typeZaixian;//1 ?????????2 ?????????3 ?????????
    private int typeMingling;//1 ?????????2 ?????????3 ????????????4 ????????????5 ????????????6?????????
    private boolean iskaijiDianhou;
    private boolean isOnActivity;

    private boolean youbengIson;
    private boolean youbengIsdianhou;
    private boolean shubengIson;
    private boolean shoubengisdianhou;


    @Override
    public int getContentViewResId() {
        return R.layout.activity_shuinuan_main;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true);
        mImmersionBar.init();
    }

    /**
     * ????????????Activty????????????Activity
     */
    public static void actionStart(Context context, String ccid, String car_server_id, String time) {
        Intent intent = new Intent(context, ShuinuanMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("ccid", ccid);
        intent.putExtra("car_server_id", car_server_id);
        intent.putExtra("time", time);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        init();
        initHuidiao();
        registerKtMqtt();
        initSM();
        initHandlerNS();
    }

    private void initSM() {
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                typeZaixian = 3;
                registerKtMqtt();
                smartRefreshLayout.finishRefresh();
            }
        });
    }

    private void init() {
        rv_shuinuan_guanji.setSelected(true);
        isKaiji = false;

        rv_shuinuan_kaiji.setOnLongClickListener(this);
        rv_shuinuan_guanji.setOnLongClickListener(this);
        tv_shuinuan_youbeng.setOnLongClickListener(this);
        tv_shuinuan_shuibeng.setOnLongClickListener(this);

        PreferenceHelper.getInstance(mContext).putString(App.CHOOSE_KONGZHI_XIANGMU, DoMqttValue.SHUINUAN);

        initCcid();
    }

    /**
     * ?????????Ccid
     */
    private void initCcid() {
        String time = getIntent().getStringExtra("time");
        tv_shebei_youxiaoqi.setText("????????????:" + time);
        String car_server_id = getIntent().getStringExtra("car_server_id");
        ccid = getIntent().getStringExtra("ccid");
        SN_Send = "wh/hardware/" + car_server_id + ccid;
        SN_Accept = "wh/app/" + car_server_id + ccid;

        MyApplication.mqttDingyue.add(SN_Send);
        MyApplication.mqttDingyue.add(SN_Accept);

        sim_ccid_save_type = PreferenceHelper.getInstance(mContext).getString("sim_ccid_save_type", "0");
        isFirst = true;
        isCanGetNs = true;
        typeZaixian = 3;
    }

    /**
     * ?????????????????????
     */
    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_SN_DATA) {
                    String msg = message.content.toString();
                    getData(msg);
                } else if (message.type == ConstanceValue.MSG_JIEBANG) {
                    finish();
                } else if (message.type == ConstanceValue.MSG_NETWORK_CHANGE) {
                    getNs();
                }
            }
        }));
    }

    private void getData(String msg) {
        Log.e("?????????????????????????????????", msg);
        if (msg.contains("j_s")) {
            tv_zaixian.setText("??????");
            sn_state = msg.substring(3, 4);//????????????
            String syscTime = msg.substring(4, 7);//??????????????????
            //????????????  1.?????????2.?????????
            shuibeng_state = msg.substring(7, 8);
            //????????????  1.?????????2.?????????
            youbeng_state = msg.substring(8, 9);
            String fengji_state = msg.substring(9, 10);//????????????  1.?????????2.?????????
            String dianyan = (Y.getInt(msg.substring(10, 14)) / 10.0f) + "";//??????  0253 = 25.3
            String fengjizhuansu = msg.substring(14, 19);//????????????   13245
            String jairesaigonglv = (Y.getInt(msg.substring(19, 23)) / 10.0f) + "";// ???????????????  0264=26.4
            String youbenggonglv = (Y.getInt(msg.substring(23, 27)) / 10.0f) + "";// ????????????  0264=26.4

            int rushukowenduInt = (Y.getInt(msg.substring(27, 30)) - 50);
            if (rushukowenduInt <= 0) {
                rushukowenduInt = 0;
            }
            String rushukowendu = rushukowenduInt + "";// ????????????????????????  -50???150???000 = -50???100 = 50???

            int chushuikowenduInt = (Y.getInt(msg.substring(30, 33)) - 50);
            if (chushuikowenduInt <= 0) {
                chushuikowenduInt = 0;
            }
            String chushuikowendu = chushuikowenduInt + "";// ????????????????????????  -50???150???000 = -50???100 = 50???

            int weiqiwenduInt = (Y.getInt(msg.substring(33, 37)) - 100);
            if (weiqiwenduInt <= 0) {
                weiqiwenduInt = 0;
            }
            String weiqiwendu = weiqiwenduInt + "";// ?????????????????????  -50???2000???000 = -50???100 = 50???

            String danqiandangwei = msg.substring(37, 38);// 1.??????2.??????????????????*?????????
            yushewendu = msg.substring(38, 40);//????????????????????? ?????????????????????
            String zongTime = msg.substring(40, 45);//????????? ????????????
            String daqiya = msg.substring(45, 48);//?????????
            String haibagaodu = msg.substring(48, 52);//????????????
            String hanyangliang = msg.substring(52, 55);//?????????

            firstCaozuo(msg);


            if (sn_state.equals("0") || sn_state.equals("3")) {
                if (shuibeng_state.equals("1") && youbeng_state.equals("2")) {
                    shubengIson = true;
                    shoubengisdianhou = true;
                    tv_shebei_state.setText("??????????????????????????????");
                } else {
                    shubengIson = false;
                    shoubengisdianhou = false;
                }

                if (youbeng_state.equals("1") && shuibeng_state.equals("2")) {
                    youbengIson = true;
                    youbengIsdianhou = true;
                    tv_shebei_state.setText("??????????????????????????????");
                } else {
                    youbengIson = false;
                    youbengIsdianhou = false;
                }
            }

            xinhaoStr = msg.substring(55, 57);
            if (xinhaoStr.equals("aa")) {
                iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal2);
            } else {
                int xinhao = Y.getInt(xinhaoStr);//????????????
                if (xinhao >= 15 && xinhao <= 19) {
                    iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal2);
                } else if (xinhao >= 20 && xinhao <= 25) {
                    iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal3);
                } else if (xinhao >= 26 && xinhao <= 35) {
                    iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal4);
                } else {
                    iv_xinhao.setImageResource(R.mipmap.fengnuan_icon_signal1);
                }
            }

            String num = "????????????" + sn_state + "  ??????????????????" + syscTime + "  ????????????" + shuibeng_state + "  ????????????" + youbeng_state
                    + "  ????????????" + fengji_state
                    + "  ??????" + dianyan
                    + "  ????????????" + fengjizhuansu
                    + "  ???????????????" + jairesaigonglv
                    + "  ????????????" + youbenggonglv
                    + "    ???????????????" + rushukowendu
                    + "    ???????????????" + chushuikowendu
                    + "    ????????????" + weiqiwendu
                    + "    ????????????" + weiqiwendu
                    + "  ????????????" + danqiandangwei
                    + "  ?????????" + zongTime + "   ?????????" + daqiya + "    ????????????" + haibagaodu + "  ?????????" + hanyangliang
                    + "  ????????????" + xinhaoStr;
            Y.e(num);
            tv_dianya.setText(dianyan + "v");
            tv_jinshuikou_wendu.setText(rushukowendu + "???");
            tv_chushuikou_wendu.setText(chushuikowendu + "???");
            tv_haibagaodu.setText(Y.getInt(haibagaodu) + "m");
            tv_hanyangliang.setText(Y.getInt(hanyangliang) + "kg/cm3");
            tv_daqiya.setText(Y.getInt(daqiya) + "kpa");
            tv_wendu_yushe.setText("????????????:" + yushewendu + "???");
            tv_wendu_dangqian.setText("????????????:" + chushuikowendu + "???");
            typeZaixian = 1;

            switch (sn_state) {
                case "1"://?????????
                case "2"://?????????
                case "4"://?????????
                    isKaiji = true;
                    setUiKaiji();
                    break;
                case "0"://?????????
                case "3"://?????????
                    isKaiji = false;
                    setUiGuanji();
                    break;
            }

            switch (shuibeng_state) {
                case "1":
                    tv_shuinuan_shuibeng.setText("???????????????");
                    tv_shuinuan_shuibeng.setTextColor(Y.getColor(R.color.text_color_blue));
                    break;
                case "2":
                    tv_shuinuan_shuibeng.setText("???????????????");
                    tv_shuinuan_shuibeng.setTextColor(Y.getColor(R.color.text_color_9));
                    break;
            }

            switch (youbeng_state) {
                case "1":
                    tv_shuinuan_youbeng.setText("???????????????");
                    tv_shuinuan_youbeng.setTextColor(Y.getColor(R.color.text_color_blue));
                    break;
                case "2":
                    tv_shuinuan_youbeng.setText("???????????????");
                    tv_shuinuan_youbeng.setTextColor(Y.getColor(R.color.text_color_9));
                    break;
            }
        } else if (msg.contains("k_s")) {
            String code = msg.substring(3, 5);
            if (code.equals("01")) {
                String zhuantai = msg.substring(5, 6);
                if (zhuantai.equals("1")) {
                    Log.i("????????????", "");
                } else {
                    Log.i("????????????", "");
                }
            } else {

            }
        } else if (msg.contains("M_s")) {
            String code = msg.substring(3, 5);
            if (code.equals("01")) {

            } else if (code.equals("02")) {

            }
        } else if (msg.contains("g_s.")) {
            typeZaixian = 1;
        } else if (msg.contains("r_s")) {
            String dianya = msg.substring(3, 4);//??????	0.??????1.??????2.??????3.??????
            String youbeng = msg.substring(4, 5);//??????	0.??????1.??????2.??????3.??????
            String shuibeng = msg.substring(5, 6);//??????	0.??????1.??????2.??????3.??????4.??????5.??????
            String chushuiko = msg.substring(6, 7);//?????????	0.??????1.??????2.??????3.??????4.??????
            String rushuiko = msg.substring(7, 8);//?????????	0.??????1.??????2.??????3.??????4.??????
            String wensheng = msg.substring(8, 9);//??????	0.??????1.??????
            String fengji = msg.substring(9, 10);//??????	0.??????1.??????2.??????3.??????4.??????5.??????
            String chufengko = msg.substring(10, 11);//?????????	0.??????1.??????2.??????3.??????4.??????
            String dianhuosai = msg.substring(11, 12);//?????????	0.??????1.??????2.??????3.??????
            String houyan = msg.substring(12, 13);//??????	0.??????1.??????
            String dianhuo = msg.substring(13, 14);//??????	0.??????1.??????

            List<String> guzhangs = new ArrayList<>();

            if (dianya.equals("1")) {
                guzhangs.add("????????????");
            } else if (dianya.equals("2")) {
                guzhangs.add("????????????");
            } else if (dianya.equals("3")) {
                guzhangs.add("????????????");
            }

            if (youbeng.equals("1")) {
                guzhangs.add("????????????");
            } else if (youbeng.equals("2")) {
                guzhangs.add("????????????");
            } else if (youbeng.equals("3")) {
                guzhangs.add("????????????");
            }

            if (shuibeng.equals("1")) {
                guzhangs.add("????????????");
            } else if (shuibeng.equals("2")) {
                guzhangs.add("????????????");
            } else if (shuibeng.equals("3")) {
                guzhangs.add("????????????");
            } else if (shuibeng.equals("4")) {
                guzhangs.add("????????????");
            } else if (shuibeng.equals("5")) {
                guzhangs.add("????????????");
            }

            if (chushuiko.equals("1")) {
                guzhangs.add("???????????????");
            } else if (chushuiko.equals("2")) {
                guzhangs.add("???????????????");
            } else if (chushuiko.equals("3")) {
                guzhangs.add("???????????????");
            } else if (chushuiko.equals("4")) {
                guzhangs.add("???????????????");
            }

            if (rushuiko.equals("1")) {
                guzhangs.add("???????????????");
            } else if (rushuiko.equals("2")) {
                guzhangs.add("???????????????");
            } else if (rushuiko.equals("3")) {
                guzhangs.add("???????????????");
            } else if (rushuiko.equals("4")) {
                guzhangs.add("???????????????");
            }

            if (wensheng.equals("1")) {
                guzhangs.add("????????????");
            }

            if (fengji.equals("1")) {
                guzhangs.add("????????????");
            } else if (fengji.equals("2")) {
                guzhangs.add("????????????");
            } else if (fengji.equals("3")) {
                guzhangs.add("????????????");
            } else if (fengji.equals("4")) {
                guzhangs.add("????????????");
            } else if (fengji.equals("5")) {
                guzhangs.add("????????????");
            }

            if (chufengko.equals("1")) {
                guzhangs.add("???????????????");
            } else if (chufengko.equals("2")) {
                guzhangs.add("???????????????");
            } else if (chufengko.equals("3")) {
                guzhangs.add("???????????????");
            } else if (chufengko.equals("4")) {
                guzhangs.add("???????????????");
            }

            if (dianhuosai.equals("1")) {
                guzhangs.add("???????????????");
            } else if (dianhuosai.equals("2")) {
                guzhangs.add("???????????????");
            } else if (dianhuosai.equals("3")) {
                guzhangs.add("???????????????");
            }

            if (houyan.equals("1")) {
                guzhangs.add("????????????");
            }

            if (dianhuo.equals("1")) {
                guzhangs.add("????????????  ???????????????");
            }

            if (guzhangs.size() > 0) {
                showguzhangla(guzhangs);
            } else {
                if (guzhangDialog != null) {
                    guzhangDialog.dismiss();
                }
            }
        }
    }

    private void firstCaozuo(String msg) {
        if (isFirst) {
            //??????????????????????????????????????????
            if (!sim_ccid_save_type.equals("1")) {
                AndMqtt.getInstance().publish(new MqttPublish()
                        .setMsg("X_s.")
                        .setQos(2).setRetained(false)
                        .setTopic(SN_Send), new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Y.i("??????????????????");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });
            }

            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("Y_s.")
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Y.i("?????????????????????");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });

            isFirst = false;
        }
    }


    private void showguzhangla(List<String> strings) {
        if (guzhangDialog == null) {
            guzhangDialog = new GuzhangDialog(mContext, new GuzhangDialog.Guzhang() {
                @Override
                public void onClickConfirm(View v, GuzhangDialog dialog) {
                    dealGuzhang();
                }

                @Override
                public void onDismiss(GuzhangDialog dialog) {

                }

                @Override
                public void onHulue(View view, GuzhangDialog dialog) {
                    finish();
                }
            });
        }
        guzhangDialog.showDD(strings);
    }

    private void dealGuzhang() {
        String data = "M_s071.";
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg(data)
                .setQos(2).setRetained(false)
                .setTopic(SN_Send), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        Message message = handlerGuanzhang.obtainMessage(1);
        handlerGuanzhang.sendMessageDelayed(message, 700);
    }

    Handler handlerGuanzhang = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    AndMqtt.getInstance().publish(new MqttPublish()
                            .setMsg("Z_s.")
                            .setQos(2).setRetained(false)
                            .setTopic(SN_Send), new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Y.i("??????????????????");
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                        }
                    });
                    break;
            }
            return false;
        }
    });

    /**
     * ????????????Mqtt
     */
    private void registerKtMqtt() {
        initHandlerStart();
        getNs();
    }

    private void getNs() {
//        //???????????????????????????
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(SN_Send)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        //???????????????????????????app
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(SN_Accept)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
        getNsData();
    }

    private void getNsData() {
        //??????????????????????????????????????????
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg("N_s.")
                .setQos(2).setRetained(false)
                .setTopic(SN_Send), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("app???????????????????????????????????????", "");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }

    private int time = 0;

    private void initHandlerStart() {
        Message message = handlerStart.obtainMessage(1);
        handlerStart.sendMessageDelayed(message, 1000);
    }

    private void initHandlerClick() {
        Message message = handlerStart.obtainMessage(2);
        handlerStart.sendMessageDelayed(message, 1000);
    }

    private void initHandlerShuibeng() {
        Message message = handlerStart.obtainMessage(3);
        handlerStart.sendMessageDelayed(message, 1000);
    }

    private void initHandlerYoubeng() {
        Message message = handlerStart.obtainMessage(4);
        handlerStart.sendMessageDelayed(message, 1000);
    }

    private Handler handlerStart = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            time++;
            switch (msg.what) {
                case 1:
                    if (typeZaixian != 1) {
                        if (time >= 30) {
                            showTishiDialog();
                        } else {
                            if (time == 5 || time == 10 || time == 15 || time == 20 || time == 25) {
                                isCanGetNs = false;
                                getNs();
                            }
                            initHandlerStart();
                        }
                    } else {
                        isCanGetNs = true;
                        time = 0;
                    }
                    break;
                case 2:
                    if (iskaijiDianhou != isKaiji) {
                        if (time >= 30) {
                            isCanGetNs = true;
                            iskaijiDianhou = !iskaijiDianhou;
                            if (iskaijiDianhou) {
                                setUiKaiji();
                            } else {
                                setUiGuanji();
                            }

                            shubengIson = false;
                            youbengIson = false;
                            time = 0;
                        } else {
                            if (time == 5 || time == 10 || time == 15 || time == 20 || time == 25) {
                                sendMingling();
                            }
                            initHandlerClick();
                        }
                    } else {
                        isCanGetNs = true;
                        time = 0;
                    }
                    break;
                case 3:
                    if (shoubengisdianhou != shubengIson) {
                        if (time >= 30) {
                            isCanGetNs = true;
                            shoubengisdianhou = !shoubengisdianhou;
                            if (shoubengisdianhou) {
                                setShuibengUiKai();
                            } else {
                                setShuibengUiGuan();
                            }
                            time = 0;
                        } else {
                            if (time == 5 || time == 10 || time == 15 || time == 20 || time == 25) {
                                sendMingling();
                            }
                            initHandlerShuibeng();
                        }
                    } else {
                        isCanGetNs = true;
                        time = 0;
                    }
                    break;
                case 4:
                    if (youbengIsdianhou != youbengIson) {
                        if (time >= 30) {
                            isCanGetNs = true;
                            youbengIsdianhou = !youbengIsdianhou;
                            if (youbengIsdianhou) {
                                youbengUiKai();
                            } else {
                                youbengUiGuan();
                            }
                            time = 0;
                        } else {
                            if (time == 5 || time == 10 || time == 15 || time == 20 || time == 25) {
                                sendMingling();
                            }
                            initHandlerYoubeng();
                        }
                    } else {
                        isCanGetNs = true;
                        time = 0;
                    }
                    break;
            }
            Y.e(msg.what + "  ?????????????????????  " + time);
            return false;
        }
    });

    private void showTishiDialog() {
        isCanGetNs = true;
        typeZaixian = 2;
        time = 0;
        TishiDialog tishiDialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {
                finish();
            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                registerKtMqtt();
            }

            @Override
            public void onDismiss(TishiDialog dialog) {

            }
        });
        tishiDialog.setTextTitle("???????????????????????????");
        tishiDialog.setTextContent("????????????????????????1:???????????????????????? 2:??????????????????????????? 3:????????????????????? 4:????????????????????????????????????????????????????????????????????????");
        tishiDialog.setTextConfirm("??????");
        tishiDialog.setTextCancel("??????");
        tishiDialog.show();
    }


    @OnClick({R.id.rl_back, R.id.rl_set, R.id.iv_shebeima, R.id.ll_dingshi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_set:
                SheBeiSetActivity.actionStart(mContext, SheBeiSetActivity.TYPE_SHUINUAN);
                break;
            case R.id.iv_shebeima:
                showShebeima();
                break;
            case R.id.ll_dingshi:
                FengnuandishiActivity.actionStart(mContext);
                break;
        }
    }

    private void showShebeima() {
        TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
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
        dialog.setTextTitle("?????????");
        dialog.setTextContent(ccid);
        dialog.setTextCancel("");
        dialog.show();
    }

    @Override
    public boolean onLongClick(View v) {
        if (typeZaixian == 1) {
            if (typeMingling == 1 || typeMingling == 2) {
                handlerStart.removeMessages(2);
            } else if (typeMingling == 3 || typeMingling == 4) {
                handlerStart.removeMessages(4);
            } else if (typeMingling == 5 || typeMingling == 6) {
                handlerStart.removeMessages(3);
            } else {
                handlerStart.removeMessages(1);
            }

            time = 0;

            switch (v.getId()) {
                case R.id.rv_shuinuan_kaiji:
                    kaiji();
                    break;
                case R.id.rv_shuinuan_guanji:
                    guanji();
                    break;
                case R.id.tv_shuinuan_shuibeng:
                    shuibeng();
                    break;
                case R.id.tv_shuinuan_youbeng:
                    youbeng();
                    break;
            }
        } else if (typeZaixian == 3) {
            Y.t("??????????????????????????????...");
        } else if (typeZaixian == 2) {
            chonglian();
        }
        return false;
    }

    private void youbeng() {
        if (iskaijiDianhou) {
            Y.t("??????????????????????????????");
            return;
        }

        if (shoubengisdianhou) {
            Y.t("?????????????????????????????????");
            return;
        }

        initHandlerYoubeng();
        if (youbengIsdianhou) {
            typeMingling = 4;
            SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_youbeng_off);
            sendMingling();
            youbengUiGuan();
        } else {
            typeMingling = 3;
            SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_youbeng_on);
            sendMingling();
            youbengUiKai();
        }
    }

    private void youbengUiGuan() {
        youbengIsdianhou = false;
        tv_shuinuan_youbeng.setText("???????????????");
        tv_shebei_state.setText("????????????????????????");
        tv_shuinuan_youbeng.setTextColor(Y.getColor(R.color.text_color_9));
    }

    private void youbengUiKai() {
        youbengIsdianhou = true;
        tv_shuinuan_youbeng.setText("???????????????");
        tv_shebei_state.setText("??????????????????????????????");
        tv_shuinuan_youbeng.setTextColor(Y.getColor(R.color.text_color_blue));
    }

    private void shuibeng() {
        if (iskaijiDianhou) {
            Y.t("??????????????????????????????");
            return;
        }

        if (youbengIsdianhou) {
            Y.t("?????????????????????????????????");
            return;
        }

        initHandlerShuibeng();
        if (shoubengisdianhou) {
            typeMingling = 6;
            SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_shuibeng_off);
            sendMingling();
            setShuibengUiGuan();
        } else {
            typeMingling = 5;
            SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_shuibeng_on);
            sendMingling();
            setShuibengUiKai();
        }
    }

    private void setShuibengUiGuan() {
        shoubengisdianhou = false;
        tv_shuinuan_shuibeng.setText("???????????????");
        tv_shebei_state.setText("????????????????????????");
        tv_shuinuan_shuibeng.setTextColor(Y.getColor(R.color.text_color_9));
    }

    private void setShuibengUiKai() {
        shoubengisdianhou = true;
        tv_shuinuan_shuibeng.setText("???????????????");
        tv_shebei_state.setText("??????????????????????????????");
        tv_shuinuan_shuibeng.setTextColor(Y.getColor(R.color.text_color_blue));
    }

    private void guanji() {
        if (!iskaijiDianhou) {
            return;
        }

        initHandlerClick();
        SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_start_off);
        typeMingling = 2;
        sendMingling();
        setUiGuanji();
    }

    private void setUiGuanji() {
        iskaijiDianhou = false;
        iv_shuinuan_kaijie.setVisibility(View.GONE);
        iv_shuinuan_guanji.setVisibility(View.VISIBLE);
        tv_shuinuan_kaiji.setTextColor(Y.getColor(R.color.white));
        tv_shuinuan_guanji.setTextColor(Y.getColor(R.color.text_color_blue));
        rv_shuinuan_kaiji.setSelected(false);
        rv_shuinuan_guanji.setSelected(true);
        iv_heater_host.setBackgroundResource(R.drawable.shuinuan_guanji);
        if (!youbengIson && !shubengIson) {
            tv_shebei_state.setText("????????????????????????");
        }
    }

    private void kaiji() {
        if (iskaijiDianhou) {
            return;
        }

        if (youbengIsdianhou) {
            Y.t("?????????????????????????????????");
            return;
        }

        if (shoubengisdianhou) {
            Y.t("?????????????????????????????????");
            return;
        }

        initHandlerClick();
        SoundPoolUtils.soundPool(mContext, R.raw.shuinuan_start_on);
        typeMingling = 1;
        sendMingling();
        setUiKaiji();
    }

    private void setUiKaiji() {
        iskaijiDianhou = true;
        iv_shuinuan_kaijie.setVisibility(View.VISIBLE);
        iv_shuinuan_guanji.setVisibility(View.GONE);
        tv_shuinuan_kaiji.setTextColor(Y.getColor(R.color.text_color_blue));
        tv_shuinuan_guanji.setTextColor(Y.getColor(R.color.white));
        rv_shuinuan_kaiji.setSelected(true);
        rv_shuinuan_guanji.setSelected(false);
        tv_shebei_state.setText("????????????????????????");
        iv_heater_host.setBackgroundResource(R.drawable.shuinuan_kaiji);
        animationDrawable = (AnimationDrawable) iv_heater_host.getBackground();
        animationDrawable.start();
    }

    private void sendMingling() {
        isCanGetNs = false;
        if (typeMingling == 1) {//??????
            String data = "M_s011000080.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 2) {//??????
            String data = "M_s012.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 3) {//????????????
            String data = "M_s051.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 4) {//????????????
            String data = "M_s052.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 5) {//????????????
            String data = "M_s021.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else if (typeMingling == 6) {//????????????
            String data = "M_s022.";
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg(data)
                    .setQos(2).setRetained(false)
                    .setTopic(SN_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        }
    }

    private void chonglian() {
        if (handlerStart != null) {
            handlerStart.removeMessages(1);
        }
        showTishiDialog();
    }

    private Handler handlerTime10 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    if (isOnActivity && isCanGetNs && isKaiji) {
                        getNsData();
                        Y.e("???????????????????????????????????????");
                    } else {
                        Y.e("??????????????????");
                    }
                    initHandlerNS();
                    break;
            }
            return false;
        }
    });

    private void initHandlerNS() {
        Message message = handlerTime10.obtainMessage(1);
        handlerTime10.sendMessageDelayed(message, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOnActivity = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnActivity = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerStart.removeMessages(1);
        handlerStart.removeMessages(2);
        handlerStart.removeMessages(3);
        handlerStart.removeMessages(4);
        handlerTime10.removeMessages(1);

        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(SN_Send), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(SN_Accept), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        for (int i = 0; i < MyApplication.mqttDingyue.size(); i++) {
            if (MyApplication.mqttDingyue.get(i).equals(SN_Send)) {
                MyApplication.mqttDingyue.remove(i);
            }
        }

        for (int i = 0; i < MyApplication.mqttDingyue.size(); i++) {
            if (MyApplication.mqttDingyue.get(i).equals(SN_Accept)) {
                MyApplication.mqttDingyue.remove(i);
            }
        }
    }
}