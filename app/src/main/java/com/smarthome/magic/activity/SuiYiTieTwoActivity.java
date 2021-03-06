package com.smarthome.magic.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundRelativeLayout;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.zhinengjiaju.RenTiGanYingActivity;
import com.smarthome.magic.app.AppConfig;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.SuiYiTieModel;
import com.smarthome.magic.mqtt_zhiling.ZnjjMqttMingLing;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;

public class SuiYiTieTwoActivity extends BaseActivity {
    @BindView(R.id.iv_1)
    ImageButton iv1;
    @BindView(R.id.ll_1)
    RoundLinearLayout ll1;
    @BindView(R.id.iv_2)
    ImageButton iv2;

    @BindView(R.id.iv_3)
    ImageButton iv3;
    @BindView(R.id.ll_3)
    RoundLinearLayout ll3;
    @BindView(R.id.tv_left)
    TextView tvLeft;

    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_add1)
    ImageView ivAdd1;
    @BindView(R.id.rl_left)
    RoundRelativeLayout rlLeft;
    @BindView(R.id.iv_add2)
    ImageView ivAdd2;

    @BindView(R.id.iv_add3)
    ImageView ivAdd3;
    @BindView(R.id.rl_right)
    RoundRelativeLayout rlRight;

    @BindView(R.id.tv_add1_name)
    TextView tvAdd1Name;
    @BindView(R.id.tv_add2_name)
    TextView tvAdd2Name;
    @BindView(R.id.tv_add3_name)
    TextView tvAdd3Name;
    @BindView(R.id.tvsuiyitie)
    TextView tvsuiyitie;


    private String device_ccid = "";
    private String device_ccidup = "";
    private String paiwei = "1";
    private List<SuiYiTieModel.DataBean.DeviceListBean> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device_ccid = getIntent().getStringExtra("device_ccid");
        device_ccidup = getIntent().getStringExtra("device_ccid_up");
        kongJianClick();
        getnet();

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_KAIGUAN_DELETE) {
                    finish();
                }
            }
        }));
    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_suiyitie_two;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("???????????????");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        iv_rightTitle.setVisibility(View.VISIBLE);
        iv_rightTitle.setImageResource(R.mipmap.mine_shezhi);
        iv_rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuiYiTieSetting.actionStart(mContext, device_ccid, device_ccidup);
            }
        });
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void kongJianClick() {
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv1.isSelected()) {
                    zhiXingMqtt(0, "02");
                    iv1.setSelected(false);
                } else {
                    zhiXingMqtt(0, "01");
                    iv1.setSelected(true);
                }
            }
        });

        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iv3.isSelected()) {
                    zhiXingMqtt(1, "02");
                    iv3.setSelected(false);
                } else {
                    zhiXingMqtt(1, "01");
                    iv3.setSelected(true);
                }


            }
        });
        rlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tanChuang("0", bangDingLeft);
            }
        });

        rlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tanChuang("1", bangDingRight);

            }
        });
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String device_ccid, String device_ccidup) {
        Intent intent = new Intent(context, SuiYiTieTwoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("device_ccid", device_ccid);
        intent.putExtra("device_ccid_up", device_ccidup);
        context.startActivity(intent);
    }


    String bangDingLeft;//0??? 1 ???

    String bangDingRight;//0??? 1???


    public void tanChuang(String paiwei, String bangDingType) {
        if (bangDingType != null) {
            if (bangDingType.equals("1")) {
                String[] items = {"???????????????", "????????????"};
                final ActionSheetDialog dialog = new ActionSheetDialog(this, items, null);
                dialog.isTitleShow(false).show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        Uri imageUri = Uri.fromFile(file);
                        switch (position) {
                            case 0:
                                String deviceCcidSt = "";
                                //UIHelper.ToastMessage(mContext, "????????????");
                                if (mDatas.get(Integer.valueOf(paiwei)) != null) {
                                    deviceCcidSt = mDatas.get(Integer.valueOf(paiwei)).getDevice_ccid();
                                }
                                SuiYiTieTianJiaSheBeiActivity.actionStart(mContext, paiwei, device_ccid, device_ccidup, "2", deviceCcidSt);
                                break;
                            case 1:
                                yiChuSheBei(paiwei);
                                break;
                        }
                        dialog.dismiss();
                    }
                });
            }else {
                String deviceCcidSt = "";
                //UIHelper.ToastMessage(mContext, "????????????");
                if (mDatas.get(Integer.valueOf(paiwei)) != null) {
                    deviceCcidSt = mDatas.get(Integer.valueOf(paiwei)).getDevice_ccid();
                }
                SuiYiTieTianJiaSheBeiActivity.actionStart(mContext, paiwei, device_ccid, device_ccidup, "2", deviceCcidSt);
            }
        } else {
            String deviceCcidSt = "";
            //UIHelper.ToastMessage(mContext, "????????????");
            if (mDatas.get(Integer.valueOf(paiwei)) != null) {
                deviceCcidSt = mDatas.get(Integer.valueOf(paiwei)).getDevice_ccid();
            }
            SuiYiTieTianJiaSheBeiActivity.actionStart(mContext, paiwei, device_ccid, device_ccidup, "2", deviceCcidSt);
        }
    }


    private void getnet() {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "16052");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("device_ccid", device_ccid);
        map.put("device_ccid_up", device_ccidup);
        Gson gson = new Gson();
        String a = gson.toJson(map);
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<SuiYiTieModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<SuiYiTieModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<SuiYiTieModel.DataBean>> response) {
                        showLoadSuccess();
                        flag = true;//?????????????????????onresume

                        ll1.setVisibility(View.VISIBLE);
                        ll3.setVisibility(View.VISIBLE);
                        rlLeft.setVisibility(View.VISIBLE);
                        rlRight.setVisibility(View.VISIBLE);

                        ivAdd1.setVisibility(View.VISIBLE);
                        ivAdd2.setVisibility(View.GONE);
                        ivAdd3.setVisibility(View.VISIBLE);

                        tvAdd2Name.setVisibility(View.GONE);
                        tvAdd1Name.setVisibility(View.VISIBLE);
                        tvAdd3Name.setVisibility(View.VISIBLE);

                        tvLeft.setVisibility(View.VISIBLE);
                        tvRight.setVisibility(View.VISIBLE);

                        Glide.with(mContext).load(R.mipmap.tuya_nav_icon_add).into(ivAdd1);
                        Glide.with(mContext).load(R.mipmap.tuya_nav_icon_add).into(ivAdd2);
                        Glide.with(mContext).load(R.mipmap.tuya_nav_icon_add).into(ivAdd3);

                        mDatas.clear();
                        mDatas.addAll(response.body().data.get(0).getDevice_list());

                        showAddIcon(response);
                    }

                    @Override
                    public void onError(Response<AppResponse<SuiYiTieModel.DataBean>> response) {
                        String str = response.getException().getMessage();
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());
                    }

                    @Override
                    public void onStart(Request<AppResponse<SuiYiTieModel.DataBean>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void showAddIcon(Response<AppResponse<SuiYiTieModel.DataBean>> response) {

        if (response.body().data.get(0).getBinding_type() == null) {
            if (response.body().data.get(0).getBinding_type().equals("1")) {
                rlLeft.setVisibility(View.GONE);
                rlRight.setVisibility(View.GONE);

                tvsuiyitie.setVisibility(View.VISIBLE);
                String str1 = response.body().data.get(0).getDevice_list().get(0).getBinding_device_name();
                String str = "??????????????????" + str1 + "??????????????????????????????????????????";
                tvsuiyitie.setText(str);

            } else {
                showSuiYiTie(response);
            }
        } else {
            showSuiYiTie(response);
        }


        if (StringUtils.isEmpty(response.body().data.get(0).getBinding_type())) {
            tvsuiyitie.setVisibility(View.GONE);
            tvAdd1Name.setText("??????");
            tvAdd3Name.setText("??????");
        }
    }

    public void showSuiYiTie(Response<AppResponse<SuiYiTieModel.DataBean>> response) {

            tvsuiyitie.setVisibility(View.GONE);

            if (StringUtils.isEmpty(response.body().data.get(0).getDevice_list().get(0).getBinding_device_type_pic())) {
                Glide.with(mContext).load(R.mipmap.tuya_nav_icon_add).into(ivAdd1);

                bangDingLeft = "0";
            } else {
                bangDingLeft = "1";
                SuiYiTieModel.DataBean dataBean0 = response.body().data.get(0);
                Glide.with(mContext).load(dataBean0.getDevice_list().get(0).getBinding_device_type_pic()).into(ivAdd1);
                tvAdd1Name.setText(dataBean0.getDevice_list().get(0).getBinding_device_name());

                if (dataBean0.getDevice_list().get(0).getWork_state().equals("1")) {
                    iv1.setSelected(true);
                } else if (dataBean0.getDevice_list().get(0).getWork_state().equals("2")) {
                    iv1.setSelected(false);
                }

            }

            if (StringUtils.isEmpty(response.body().data.get(0).getDevice_list().get(1).getBinding_device_type_pic())) {
                Glide.with(mContext).load(R.mipmap.tuya_nav_icon_add).into(ivAdd3);
                bangDingRight = "0";
            } else {
                bangDingRight = "1";
                SuiYiTieModel.DataBean dataBean1 = response.body().data.get(0);
                Glide.with(mContext).load(dataBean1.getDevice_list().get(1).getBinding_device_type_pic()).into(ivAdd3);
                tvAdd3Name.setText(dataBean1.getDevice_list().get(1).getBinding_device_name());

                if (dataBean1.getDevice_list().get(1).getWork_state().equals("1")) {
                    iv3.setSelected(true);
                } else if (dataBean1.getDevice_list().get(1).getWork_state().equals("2")) {
                    iv3.setSelected(false);
                }
            }



    }

    private boolean flag = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (flag) {
            getnet();
        }

    }

    public ZnjjMqttMingLing mqttMingLing = null;
    String nowData;
    String hardwareData;

    private String device_ccid_up = null;
    private String serverId = null;

    public void zhiXingMqtt(int position, String caoZuoFangShi) {
        if (mqttMingLing == null) {
            device_ccid_up = mDatas.get(position).getDevice_ccid_up();
            serverId = mDatas.get(position).getServer_id();

            mqttMingLing = new ZnjjMqttMingLing(mContext, mDatas.get(position).getDevice_ccid_up(), mDatas.get(position).getServer_id());
            nowData = "zn/app/" + mDatas.get(position).getServer_id() + mDatas.get(position).getDevice_ccid_up();
            //     hardwareData = "zn/hardware/" + mDatas.get(position).getServer_id() + mDatas.get(position).getDevice_ccid_up();
            AndMqtt.getInstance().subscribe(new MqttSubscribe()
                    .setTopic(nowData)
                    .setQos(2), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("Rair", "???????????????:  " + nowData);
                    //  UIHelper.ToastMessage(mContext, "??????????????????");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i("CAR_NOTIFY", "(MainActivity.java:68)-onFailure:-&gt;????????????");
                }
            });

        }

        String zhuangZhiId = mDatas.get(position).getDevice_ccid();
        String zhiLing = "i" + zhuangZhiId + "1" + "01" + "003" + ".";
        Log.i("Rair", zhiLing);
        AndMqtt.getInstance().publish(new MqttPublish()
                .setMsg(zhiLing)
                .setQos(2).setRetained(false)
                .setTopic(nowData), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }


    public void yiChuSheBei(String paiwei) {


        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "16051");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        String kaiGuanType = mDatas.get(Integer.valueOf(paiwei)).getDevice_type();
        if (kaiGuanType.equals("35")) {
            map.put("unbinding_type", "1");
        } else {
            map.put("unbinding_type", "2");
        }

        map.put("device_ccid", mDatas.get(Integer.valueOf(paiwei)).getDevice_ccid());
        map.put("device_ccid_up", device_ccidup);
        String familyId = PreferenceHelper.getInstance(mContext).getString(AppConfig.FAMILY_ID, "0");
        map.put("family_id", familyId);
        Gson gson = new Gson();
        String a = gson.toJson(map);
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<SuiYiTieModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<SuiYiTieModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<SuiYiTieModel.DataBean>> response) {
                        UIHelper.ToastMessage(mContext, "??????????????????");
                        getnet();
                        showLoadSuccess();
                    }

                    @Override
                    public void onError(Response<AppResponse<SuiYiTieModel.DataBean>> response) {
                        String str = response.getException().getMessage();
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());
                        showLoadSuccess();
                    }

                    @Override
                    public void onStart(Request<AppResponse<SuiYiTieModel.DataBean>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mqttMingLing != null) {
            mqttMingLing.unSubscribeShiShiXinXi_App(device_ccid_up, serverId, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("Rair", "??????app????????????????????????");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });

            mqttMingLing.unSubscribeHardware_WithCanShu(device_ccid_up, serverId, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i("Rair", "????????????????????????????????????");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        }
    }
}

