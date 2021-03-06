package com.smarthome.magic.activity.zhinengjiaju.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.zhinengjiaju.GengDuoJingBaoActivity;
import com.smarthome.magic.adapter.SosListAdapter;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.AppConfig;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.LordingDialog;
import com.smarthome.magic.dialog.MyCarCaoZuoDialog_CaoZuoTIshi;
import com.smarthome.magic.dialog.MyCarCaoZuoDialog_Success;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.AlarmListBean;
import com.smarthome.magic.model.MenCiListModel;
import com.smarthome.magic.model.ZhiNengFamilyEditBean;
import com.smarthome.magic.mqtt_zhiling.ZnjjMqttMingLing;
import com.smarthome.magic.util.DoMqttValue;
import com.smarthome.magic.util.SoundPoolUtils;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.config.MyApplication.CAR_CTROL;
import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;

//???????????? ??????
public class SosActivity extends BaseActivity {
    @BindView(R.id.rlv_list)
    RecyclerView rlvList;
    @BindView(R.id.srL_smart)
    SmartRefreshLayout srLSmart;
    @BindView(R.id.tv_room_delete)
    TextView tvRoomDelete;

    private SosListAdapter menCiListAdapter;
    private List<AlarmListBean> mDatas;

    private TextView tvJiaTingName;
    private TextView tvRoomName;
    private TextView tvMingChengName;
    private TextView tvLeiXingName;
    private Switch switch1;
    private View viewZhongJian;
    View headerView;
    private LinearLayout ll_caozuo_jilu;
    ZnjjMqttMingLing znjjMqttMingLing;
    private String deviceCCid = "";
    LordingDialog lordingDialog;
    private ImageView ivSos;
    SosThread sosThread;
    boolean sosZhuangTai;
    private ImageView ivShebeiZaixianzhuangtaiImg;//???????????? ??????
    private TextView zaiXianLiXian;//????????????

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        lordingDialog = new LordingDialog(mContext);
        PreferenceHelper.getInstance(mContext).putString(App.CHOOSE_KONGZHI_XIANGMU, DoMqttValue.ZHINENGJIAJU);
        srLSmart.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNumber = pageNumber + 1;
                getNet();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNumber = 0;
                getNet();
            }
        });
        device_id = getIntent().getStringExtra("device_id");
        memberType = getIntent().getStringExtra("memberType");
        sosZhuangTai = getIntent().getBooleanExtra("sosZhuangTai", false);


        if (memberType == null) {
            tvRoomDelete.setVisibility(View.GONE);
        }

        mDatas = new ArrayList<>();
        //header ?????????????????????
//        if (onlineState.equals("1")) {
//            tvShebeiZaixianHuashu.setText("????????????");
//            ivShebeiZaixianzhuangtaiImg.setBackgroundResource(R.drawable.bg_zhineng_device_online);
//
//        } else if (onlineState.equals("2")) {
//            tvShebeiZaixianHuashu.setText("????????????");
//            ivShebeiZaixianzhuangtaiImg.setBackgroundResource(R.drawable.bg_zhineng_device_offline);
//        }

        menCiListAdapter = new SosListAdapter(R.layout.item_sos_list, R.layout.item_menci_header, mDatas);
        rlvList.setLayoutManager(new LinearLayoutManager(mContext));
        rlvList.setAdapter(menCiListAdapter);

        menCiListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.rrl_gengduo:
                        //UIHelper.ToastMessage(mContext, mDatas.get(position).sel_alarm_date + "??????");
                        GengDuoJingBaoActivity.actionStart(mContext, device_id, menCiListAdapter.getData().get(position).sel_alarm_date);
                        break;
                }
            }
        });

        headerView = View.inflate(mContext, R.layout.sos_header, null);


        tvJiaTingName = headerView.findViewById(R.id.tv_jiating_name);
        tvLeiXingName = headerView.findViewById(R.id.tv_leixing_name);
        tvMingChengName = headerView.findViewById(R.id.tv_mingcheng_name);
        tvRoomName = headerView.findViewById(R.id.tv_room_name);
        switch1 = headerView.findViewById(R.id.btn_gaojing);
        viewZhongJian = headerView.findViewById(R.id.view_zhongjian);
        ll_caozuo_jilu = headerView.findViewById(R.id.ll_caozuo_jilu);
        ivSos = headerView.findViewById(R.id.iv_sos);
        ivSos.setBackgroundResource(R.mipmap.tuya_sos_pic_normal);
        zaiXianLiXian = headerView.findViewById(R.id.tv_shebei_zaixian_huashu);
        ivShebeiZaixianzhuangtaiImg = headerView.findViewById(R.id.iv_shebei_zaixianzhuangtai_img);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if (isChecked) {
                    getGaoJingTiShiNet("1");
                } else {
                    getGaoJingTiShiNet("2");
                }
            }
        });

        Switch switchBaoJingTishiYin = headerView.findViewById(R.id.btn_baojing_tishiyin);
        String strBaoJing = PreferenceHelper.getInstance(mContext).getString(AppConfig.BOJING_SOS, "2");
        if (strBaoJing.equals("0")) {
            switchBaoJingTishiYin.setChecked(false);
        } else {
            switchBaoJingTishiYin.setChecked(true);
        }

        switchBaoJingTishiYin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if (isChecked) {
                    PreferenceHelper.getInstance(mContext).putString(AppConfig.BOJING_SOS, "1");
                } else {
                    PreferenceHelper.getInstance(mContext).putString(AppConfig.BOJING_SOS, "0");
                }
            }
        });


        menCiListAdapter.addHeaderView(headerView);
        menCiListAdapter.setNewData(mDatas);
        srLSmart.setEnableLoadMore(false);
        getNet();

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice notice) {
                if (notice.type == ConstanceValue.MSG_ZHINENGJIAJU_MENCIPAGE) {
                    getNet();
                } else if (notice.type == ConstanceValue.MSG_SHEBEIZHUANGTAI) {
                    List<String> messageList = (List<String>) notice.content;
                    String zhuangZhiId = messageList.get(0);
                    String kaiGuanDengZhuangTai = messageList.get(1);

                    if (zhuangZhiId.equals(deviceCCid)) {
                        if (kaiGuanDengZhuangTai.equals("2")) {//?????????

                            sosThread = new SosThread();
                            sosThread.start();

                        }
                    }
                    getNet();

                }
            }
        }));

        tvRoomDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCarCaoZuoDialog_CaoZuoTIshi myCarCaoZuoDialog_caoZuoTIshi = new MyCarCaoZuoDialog_CaoZuoTIshi(mContext,
                        "??????", "???????????????????????????", "??????", "??????", new MyCarCaoZuoDialog_CaoZuoTIshi.OnDialogItemClickListener() {
                    @Override
                    public void clickLeft() {
                    }

                    @Override
                    public void clickRight() {
                        if (memberType.equals("1")) {

                            //????????????
                            deleteDevice();

                        } else {
                            Toast.makeText(mContext, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                myCarCaoZuoDialog_caoZuoTIshi.show();
            }
        });


    }

    public int BAOJING = 0X11;
    public int BUBAOJING = 0X12;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == BAOJING) {
                ivSos.setBackgroundResource(R.mipmap.tuya_sos_pic_no);
            } else if (msg.what == BUBAOJING) {
                ivSos.setBackgroundResource(R.mipmap.tuya_sos_pic_normal);
            }
        }
    };


    private class N9Thread extends Thread {
        boolean flag = true;

        public void run() {
            while (true) {
                try {

                    if (flag) {
                        AndMqtt.getInstance().publish(new MqttPublish()
                                .setMsg("i12010101197.")
                                .setQos(2)
                                .setTopic("zn/app/1/aaaaaaaaaaaaaaaa90140018")
                                .setRetained(false), new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i("Rair", "???????????????????????? ???");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
                            }
                        });


                    } else {
                        AndMqtt.getInstance().publish(new MqttPublish()
                                .setMsg("i12010101297.")
                                .setQos(2)
                                .setTopic("zn/app/1/aaaaaaaaaaaaaaaa90140018")
                                .setRetained(false), new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.i("Rair", "???????????????????????? ???");

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.i("Rair", "(MainActivity.java:84)-onFailure:-&gt;????????????");
                            }
                        });

                    }
                    flag = !flag;
                    Thread.sleep(5000);

                } catch (Exception e) {
                    e.printStackTrace();

                }


            }

        }
    }


    @Override
    public int getContentViewResId() {
        return R.layout.layout_sos;
    }


    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String device_id, String memberType) {
        Intent intent = new Intent(context, SosActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("device_id", device_id);
        intent.putExtra("memberType", memberType);
        context.startActivity(intent);
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    private String str = "0";

    public static void actionStart(Context context, String device_id, boolean sosZhuangTai) {
        Intent intent = new Intent(context, SosActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("device_id", device_id);
        intent.putExtra("sosZhuangTai", sosZhuangTai);
        context.startActivity(intent);
    }

    private class SosThread extends Thread {
        private int i = 0;

        public void run() {
            while (i != 4) {
                try {
                    if (i == 0) {
                        Message message = new Message();
                        message.what = BAOJING;
                        handler.sendMessage(message);

                    } else if (i == 1) {
                        Message message = new Message();
                        message.what = BUBAOJING;
                        handler.sendMessage(message);

                    } else if (i == 2) {
                        Message message = new Message();
                        message.what = BAOJING;
                        handler.sendMessage(message);

                    } else if (i == 3) {
                        Message message = new Message();
                        message.what = BUBAOJING;
                        handler.sendMessage(message);
                    }
                    i = i + 1;
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;

                }
            }
        }
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("????????????");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    private String device_id;
    private String memberType;
    MenCiListModel.DataBean dataBean;

    private boolean firstEnter = true;
    private int pageNumber = 0;
    public void getNet() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "16043");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("device_id", device_id);
        map.put("page_num", String.valueOf(pageNumber));

        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<MenCiListModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<MenCiListModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<MenCiListModel.DataBean>> response) {
                        srLSmart.finishRefresh();
                        dataBean = response.body().data.get(0);
                        deviceCCid = dataBean.getDevice_ccid();
                        mDatas.clear();
                        if (firstEnter) {
                            znjjMqttMingLing = new ZnjjMqttMingLing(mContext, dataBean.getDevice_ccid_up(), dataBean.getServer_id());
                            znjjMqttMingLing.subscribeAppShiShiXinXi(new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    getNet();
                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                                }
                            });
                            firstEnter = false;
                        }

                        if (dataBean == null) {
                            return;
                        }

                        ivSos.setBackgroundResource(R.mipmap.tuya_sos_pic_normal);

                        tvJiaTingName.setText(dataBean.getFamily_name());
                        tvLeiXingName.setText(dataBean.getDevice_type_name());
                        tvMingChengName.setText(dataBean.getDevice_name());
                        tvRoomName.setText(dataBean.getRoom_name());

                        if (dataBean.getIs_alarm().equals("1")) {//1 ???
                            switch1.setChecked(true);
                        } else if (dataBean.getIs_alarm().equals("2")) { //2 ???
                            switch1.setChecked(false);
                        }


                        if (dataBean.getAlarm_list().size() == 0) {
                            View view = View.inflate(mContext, R.layout.zhinneng_jiaju_empty_view, null);
                            //ll_caozuo_jilu.addView(view);
                            menCiListAdapter.addFooterView(view);
                            menCiListAdapter.notifyDataSetChanged();
                            return;
                        }

                        mDatas.clear();
                        for (int i = 0; i < dataBean.getAlarm_list().size(); i++) {

                            AlarmListBean alarmListBean = new AlarmListBean(true, dataBean.getAlarm_list().get(i).getAlarm_date());
                            alarmListBean.alarm_date = dataBean.getAlarm_list().get(i).getAlarm_date();
                            alarmListBean.is_more = dataBean.getAlarm_list().get(i).is_more;
                            alarmListBean.sel_alarm_date = dataBean.getAlarm_list().get(i).sel_alarm_date;
                            mDatas.add(alarmListBean);

                            for (int j = 0; j < dataBean.getAlarm_list().get(i).getAlerm_time_list().size(); j++) {

                                MenCiListModel.DataBean.AlarmListBean.AlermTimeListBean alermTimeListBean = dataBean.getAlarm_list().get(i).getAlerm_time_list().get(j);
                                AlarmListBean alarmListBean1 = new AlarmListBean(false, dataBean.getAlarm_list().get(i).getAlarm_date());

                                alarmListBean1.alerm_time = alermTimeListBean.getAlerm_time();
                                alarmListBean1.device_state = alermTimeListBean.getDevice_state();
                                alarmListBean1.device_state_name = alermTimeListBean.getDevice_state_name();

                                mDatas.add(alarmListBean1);
                            }

                        }

                        menCiListAdapter.notifyDataSetChanged();
                        if (sosZhuangTai) {
                            Log.i("SosActivity", String.valueOf(sosZhuangTai) + "sosActivity");
                            sosThread = new SosThread();
                            sosThread.start();
                        }


                        String onlineState = dataBean.getOnline_state();

                        if (onlineState.equals("1")) {
                            zaiXianLiXian.setText("????????????");
                            ivShebeiZaixianzhuangtaiImg.setBackgroundResource
                                    (R.drawable.bg_zhineng_device_online);

                        } else if (onlineState.equals("2")) {
                            zaiXianLiXian.setText("????????????");
                            ivShebeiZaixianzhuangtaiImg.setBackgroundResource
                                    (R.drawable.bg_zhineng_device_offline);
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<MenCiListModel.DataBean>> response) {
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());
                    }

                    @Override
                    public void onStart(Request<AppResponse<MenCiListModel.DataBean>, ? extends Request> request) {
                        super.onStart(request);
                        lordingDialog.setTextMsg("???????????????????????????");
                        lordingDialog.show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (lordingDialog.isShowing()) {
                            lordingDialog.dismiss();
                        }
                    }
                });
    }

    private void getGaoJingTiShiNet(String isAlarm) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "16044");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("device_id", device_id);
        map.put("is_alarm", isAlarm);

        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<MenCiListModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<MenCiListModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<MenCiListModel.DataBean>> response) {
                        UIHelper.ToastMessage(mContext, "????????????");
                    }

                    @Override
                    public void onError(Response<AppResponse<MenCiListModel.DataBean>> response) {
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());
                    }
                });

    }

    TishiDialog tishiDialog;

    private void deleteDevice() {

        Map<String, String> map = new HashMap<>();
        map.put("code", "16034");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("family_id", dataBean.getFamily_id());
        map.put("device_id", dataBean.getDevice_id());
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<ZhiNengFamilyEditBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiNengFamilyEditBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        if (response.body().msg_code.equals("0000")) {
                            Notice notice = new Notice();
                            notice.type = ConstanceValue.MSG_ZHINENGJIAJU_SHOUYE_SHUAXIN;
                            sendRx(notice);

                            MyCarCaoZuoDialog_Success myCarCaoZuoDialog_success = new MyCarCaoZuoDialog_Success(SosActivity.this,
                                    "??????", "??????????????????", "??????", new MyCarCaoZuoDialog_Success.OnDialogItemClickListener() {
                                @Override
                                public void clickLeft() {

                                }

                                @Override
                                public void clickRight() {
                                    finish();
                                }
                            });
                            myCarCaoZuoDialog_success.show();
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        String str = response.getException().getMessage();
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
                        tishiDialog.setTextContent(str);
                        tishiDialog.show();
                    }

                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sosThread != null) {
            sosThread.interrupt();
        }
        znjjMqttMingLing.unSubscribeShiShiXinXi(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        znjjMqttMingLing.unSubscribeAppShiShiXinXi(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }


}
