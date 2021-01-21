package com.smarthome.magic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthome.magic.R;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.MyCarCaoZuoDialog_CaoZuoTIshi;
import com.smarthome.magic.dialog.MyCarCaoZuoDialog_Success;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.SuiYiTieModel;
import com.smarthome.magic.model.ZhiNengFamilyEditBean;
import com.smarthome.magic.model.ZhiiNengRoomDeviceRoomBean;
import com.smarthome.magic.mqtt_zhiling.ZnjjMqttMingLing;
import com.smarthome.magic.util.SoundPoolUtils;
import com.suke.widget.SwitchButton;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;

public class ZhiNengChuangLianActivity extends BaseActivity {

    //    @BindView(R.id.iv_head)
//    ImageView ivHead;
    @BindView(R.id.ll_online_state)
    LinearLayout llOnlineState;
    @BindView(R.id.tv_family_title)
    TextView tvFamilyTitle;
    @BindView(R.id.tv_family)
    TextView tvFamily;
    @BindView(R.id.tv_room_title)
    TextView tvRoomTitle;
    @BindView(R.id.tv_room)
    TextView tvRoom;
    @BindView(R.id.img_room_into)
    ImageView imgRoomInto;
    @BindView(R.id.rl_room)
    RelativeLayout rlRoom;
    @BindView(R.id.tv_device_name_title)
    TextView tvDeviceNameTitle;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.img_device_into)
    ImageView imgDeviceInto;
    @BindView(R.id.rl_device_name)
    RelativeLayout rlDeviceName;
    @BindView(R.id.tv_device_type_title)
    TextView tvDeviceTypeTitle;
    @BindView(R.id.tv_device_type)
    TextView tvDeviceType;
    @BindView(R.id.tv_device_state_title)
    TextView tvDeviceStateTitle;
    @BindView(R.id.tv_device_state)
    TextView tvDeviceState;
    @BindView(R.id.rl_device_state)
    RelativeLayout rlDeviceState;
    @BindView(R.id.tv_auto_switch_title)
    TextView tvAutoSwitchTitle;
    @BindView(R.id.auto_switch_button)
    SwitchButton autoSwitchButton;
    @BindView(R.id.rl_auto_switch)
    RelativeLayout rlAutoSwitch;
    @BindView(R.id.iv_tishi)
    ImageView ivTishi;
    @BindView(R.id.tv_noti)
    TextView tvNoti;
    @BindView(R.id.tv_room_delete)
    TextView tvRoomDelete;
    @BindView(R.id.srL_smart)
    SmartRefreshLayout srLSmart;
    @BindView(R.id.iv_guanchuang)
    ImageView ivGuanchuang;
    @BindView(R.id.rl_guanchuang)
    RelativeLayout rlGuanchuang;
    @BindView(R.id.iv_kaichuang)
    ImageView ivKaichuang;
    @BindView(R.id.rl_kaichuang)
    RelativeLayout rlKaichuang;
    @BindView(R.id.iv_zanting)
    ImageView ivZanting;
    @BindView(R.id.rl_zanting)
    RelativeLayout rlZanting;
    @BindView(R.id.rrl_guanchuang)
    RelativeLayout rrlGuanchuang;
    @BindView(R.id.rrl_kaichuang)
    RelativeLayout rrlKaichuang;
    @BindView(R.id.rrl_zanting)
    RelativeLayout rrlZanting;
    @BindView(R.id.tv_guanchuanglian)
    TextView tvGuanchuanglian;
    @BindView(R.id.tv_kaichuanglian)
    TextView tvKaichuanglian;
    @BindView(R.id.tv_zanting)
    TextView tvZanting;
    @BindView(R.id.animation_view)
    LottieAnimationView animationView;
    @BindView(R.id.iv_zanting_tupian)
    ImageView ivZantingTupian;

    @BindView(R.id.tv_caozuotishiyin)
    TextView tvCaozuotishiyin;
    @BindView(R.id.sbtn_caozuotishiyin)
    Switch sbtnCaozuotishiyin;
    @BindView(R.id.rl_caozuotishiyin)
    RelativeLayout rlCaozuotishiyin;


    private String device_id;
    ZnjjMqttMingLing mqttMingLing;
    private String device_ccid;
    private String member_type = "";
    private String isVoice = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null) {
            device_id = getIntent().getStringExtra("device_id");
            member_type = getIntent().getStringExtra("member_type");
        }
        getnet();

        tvRoomDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCarCaoZuoDialog_CaoZuoTIshi myCarCaoZuoDialog_caoZuoTIshi = new MyCarCaoZuoDialog_CaoZuoTIshi(mContext,
                        "提示", "确定要删除设备吗？", "取消", "确定", new MyCarCaoZuoDialog_CaoZuoTIshi.OnDialogItemClickListener() {
                    @Override
                    public void clickLeft() {

                    }

                    @Override
                    public void clickRight() {

                        //删除设备
                        deleteDevice();

                    }
                });
                myCarCaoZuoDialog_caoZuoTIshi.show();
            }
        });

        srLSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getnet();
                srLSmart.finishRefresh();
            }
        });

        ivGuanchuang.setBackgroundResource(R.mipmap.chuanglian_button_guan_nor);
        ivKaichuang.setBackgroundResource(R.mipmap.chuanglian_button_kai_nor);
        ivZanting.setBackgroundResource(R.mipmap.chuanglian_button_stop_nor);

        animationView.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.chuanglian_pic_chuanglian_open));

        rlKaichuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UIHelper.ToastMessage(mContext, "点击了开窗");
                if (isVoice.equals("1"))
                    SoundPoolUtils.soundPool(mContext, R.raw.kaiqizhinengchuanglian);
                rrlGuanchuang.setBackground(mContext.getResources().getDrawable(R.drawable.chuanglian_no_sel));
                tvGuanchuanglian.setTextColor(mContext.getResources().getColor(R.color.black_333333));
                ivGuanchuang.setBackgroundResource(R.mipmap.chuanglian_button_guan_nor);

                rrlZanting.setBackground(mContext.getResources().getDrawable(R.drawable.chuanglian_no_sel));
                tvZanting.setTextColor(mContext.getResources().getColor(R.color.black_333333));
                ivZanting.setBackgroundResource(R.mipmap.chuanglian_button_stop_nor);

                rrlKaichuang.setBackground(mContext.getResources().getDrawable(R.drawable.chuagnlian_sel));
                tvKaichuanglian.setTextColor(mContext.getResources().getColor(R.color.white));
                ivKaichuang.setBackgroundResource(R.mipmap.chuanglian_button_kai_sel);

                mqttMingLing.setAction(device_ccid, "01", new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // UIHelper.ToastMessage(mContext, "执行成功");

                        animationView.setAnimation("cloth_open_data.json");
                        animationView.setImageAssetsFolder("open_images/");
                        animationView.loop(false);
                        animationView.playAnimation();

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });


            }
        });
        rlGuanchuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // UIHelper.ToastMessage(mContext, "点击了关窗");
                if (isVoice.equals("1"))
                    SoundPoolUtils.soundPool(mContext, R.raw.guanbizhinengchuanglian);
                rrlGuanchuang.setBackground(mContext.getResources().getDrawable(R.drawable.chuagnlian_sel));
                tvGuanchuanglian.setTextColor(mContext.getResources().getColor(R.color.white));
                ivGuanchuang.setBackgroundResource(R.mipmap.chuanglian_button_guan_sel);

                rrlZanting.setBackground(mContext.getResources().getDrawable(R.drawable.chuanglian_no_sel));
                tvZanting.setTextColor(mContext.getResources().getColor(R.color.black_333333));
                ivZanting.setBackgroundResource(R.mipmap.chuanglian_button_stop_nor);

                rrlKaichuang.setBackground(mContext.getResources().getDrawable(R.drawable.chuanglian_no_sel));
                tvKaichuanglian.setTextColor(mContext.getResources().getColor(R.color.black_333333));
                ivKaichuang.setBackgroundResource(R.mipmap.chuanglian_button_kai_nor);

                mqttMingLing.setAction(device_ccid, "02", new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        //UIHelper.ToastMessage(mContext, "执行成功");
                        animationView.setAnimation("cloth_close_data.json");
                        animationView.setImageAssetsFolder("close_images/");
                        animationView.loop(false);
                        animationView.playAnimation();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });
            }
        });
        rlZanting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // UIHelper.ToastMessage(mContext, "点击了暂停");

                rrlGuanchuang.setBackground(mContext.getResources().getDrawable(R.drawable.chuanglian_no_sel));
                tvGuanchuanglian.setTextColor(mContext.getResources().getColor(R.color.black_333333));
                ivGuanchuang.setBackgroundResource(R.mipmap.chuanglian_button_guan_nor);

                rrlZanting.setBackground(mContext.getResources().getDrawable(R.drawable.chuagnlian_sel));
                tvZanting.setTextColor(mContext.getResources().getColor(R.color.white));
                ivZanting.setBackgroundResource(R.mipmap.chuanglian_button_stop_sel);

                rrlKaichuang.setBackground(mContext.getResources().getDrawable(R.drawable.chuanglian_no_sel));
                tvKaichuanglian.setTextColor(mContext.getResources().getColor(R.color.black_333333));
                ivKaichuang.setBackgroundResource(R.mipmap.chuanglian_button_kai_nor);

                mqttMingLing.setAction(device_ccid, "03", new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        //   UIHelper.ToastMessage(mContext, "执行成功");
                        //  animationView.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.chuanglian_pic_chuanglian_close));
                        animationView.cancelAnimation();
                        animationView.setProgress(0f);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });
            }
        });
        sbtnCaozuotishiyin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!buttonView.isPressed()) {
                    return;
                }
                if (isChecked) {
                    sheZhiTiShiYin("1");
                } else {
                    sheZhiTiShiYin("2");
                }

            }
        });

    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_zhinengchuanglian_activity;
    }

    private ZhiiNengRoomDeviceRoomBean.DataBean dataBean;


    private void getnet() {
        //访问网络获取数据 下面的列表数据
        Map<String, String> map = new HashMap<>();
        map.put("code", "16035");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("device_id", device_id);
        Gson gson = new Gson();
        String a = gson.toJson(map);
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<ZhiiNengRoomDeviceRoomBean.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiiNengRoomDeviceRoomBean.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZhiiNengRoomDeviceRoomBean.DataBean>> response) {
                        if (srLSmart != null) {
                            srLSmart.setEnableRefresh(true);
                            srLSmart.finishRefresh();
                            srLSmart.setEnableLoadMore(false);
                        }
                        if (response.body().msg.equals("ok")) {
                            dataBean = response.body().data.get(0);
                            // Glide.with(mContext).load(dataBean.getDevice_type_pic()).into(ivHead);
                            tvFamily.setText(dataBean.getFamily_name());
                            tvRoom.setText(dataBean.getRoom_name());
                            tvDeviceName.setText(dataBean.getDevice_name());
                            tvDeviceType.setText(dataBean.getDevice_name());
                            device_ccid = dataBean.getDevice_ccid();


//                            if (dataBean.getWork_state() != null) {
//                                if (dataBean.getWork_state().equals("1")) {
//                                    animationView.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.chuanglian_pic_chuanglian_open));
//                                } else if (dataBean.getWork_state().equals("2")) {
//                                    animationView.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.chuanglian_pic_chuanglian_close1));
//                                }
//                            }

                            mqttMingLing = new ZnjjMqttMingLing(mContext, dataBean.getDevice_ccid_up(), dataBean.getServer_id());
                            //       nowData = "zn/app/" + dataBean.getServer_id() + dataBean.getDevice_ccid_up();
//                            AndMqtt.getInstance().subscribe(new MqttSubscribe()
//                                    .setTopic(nowData)
//                                    .setQos(2), new IMqttActionListener() {
//                                @Override
//                                public void onSuccess(IMqttToken asyncActionToken) {
//                                    Log.i("Rair", "订阅的地址:  " + nowData);
//
//
//                                }
//
//                                @Override
//                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                                    Log.i("CAR_NOTIFY", "(MainActivity.java:68)-onFailure:-&gt;订阅失败");
//                                }
//                            });
//                            mqttMingLing.subscribeShiShiXinXi(new IMqttActionListener() {
//                                @Override
//                                public void onSuccess(IMqttToken asyncActionToken) {
//                                    Log.i("Rair", "请求实时数据");
//                                }
//
//                                @Override
//                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//
//                                }
//                            });
                        }

                        if (StringUtils.isEmpty(response.body().data.get(0).is_voice)) {
                            isVoice = "1";
                            sbtnCaozuotishiyin.setChecked(true);
                        } else {
                            isVoice = response.body().data.get(0).is_voice;

                            if (isVoice.equals("1")) {
                                sbtnCaozuotishiyin.setChecked(true);
                            } else if (isVoice.equals("2")) {
                                sbtnCaozuotishiyin.setChecked(false);
                            }
                        }
                        showLoadSuccess();
                    }

                    @Override
                    public void onError(Response<AppResponse<ZhiiNengRoomDeviceRoomBean.DataBean>> response) {
                        String str = response.getException().getMessage();
                        Log.i("cuifahuo", str);
                        String[] str1 = str.split("：");
//                        if (str1.length == 3) {
//                            if (srLSmart != null) {
//                                srLSmart.setEnableRefresh(true);
//                                srLSmart.finishRefresh();
//                                srLSmart.setEnableLoadMore(false);
//                            }
//                            Toast.makeText(context, str1[2], Toast.LENGTH_SHORT).show();
//                        }
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());

                    }

                    @Override
                    public void onStart(Request<AppResponse<ZhiiNengRoomDeviceRoomBean.DataBean>, ? extends Request> request) {
                        super.onStart(request);
                        showLoading();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }


    /**
     * 用于其他Activty跳转到该Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String device_id) {
        Intent intent = new Intent(context, ZhiNengChuangLianActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("device_id", device_id);
        context.startActivity(intent);
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("设备详情");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imm.hideSoftInputFromWindow(findViewById(R.id.cl_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });
    }

    /**
     * 删除设备
     */
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
                            MyCarCaoZuoDialog_Success myCarCaoZuoDialog_success = new MyCarCaoZuoDialog_Success(ZhiNengChuangLianActivity.this,
                                    "成功", "设备删除成功", "好的", new MyCarCaoZuoDialog_Success.OnDialogItemClickListener() {
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
                        Log.i("cuifahuo", str);
                        String[] str1 = str.split("：");
                        if (str1.length == 3) {
                            MyCarCaoZuoDialog_CaoZuoTIshi myCarCaoZuoDialog_caoZuoTIshi = new MyCarCaoZuoDialog_CaoZuoTIshi(mContext,
                                    "提示", str1[2], "知道了", new MyCarCaoZuoDialog_CaoZuoTIshi.OnDialogItemClickListener() {
                                @Override
                                public void clickLeft() {

                                }

                                @Override
                                public void clickRight() {

                                }
                            });
                            myCarCaoZuoDialog_caoZuoTIshi.show();
                        }
                    }
                });
    }

    private void sheZhiTiShiYin(String isVoice) {
        //访问网络获取数据 下面的列表数据
        Map<String, String> map = new HashMap<>();
        map.put("code", "16053");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("device_id", device_id);
        map.put("is_voice", isVoice);
        Gson gson = new Gson();
        String a = gson.toJson(map);
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<SuiYiTieModel.DataBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<SuiYiTieModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<SuiYiTieModel.DataBean>> response) {

                        if (isVoice.equals("1")) {
                            UIHelper.ToastMessage(mContext, "开启提示音设置成功");
                            ZhiNengChuangLianActivity.this.isVoice = "1";
                        } else if (isVoice.equals("2")) {
                            UIHelper.ToastMessage(mContext, "关闭提示音设置成功");
                            ZhiNengChuangLianActivity.this.isVoice = "2";
                        }
                        showLoadSuccess();
                    }

                    @Override
                    public void onError(Response<AppResponse<SuiYiTieModel.DataBean>> response) {
                        String str = response.getException().getMessage();
                        Log.i("cuifahuo", str);
                        String[] str1 = str.split("：");
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
}
