package com.smarthome.magic.activity.yaokongqi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.yaokongqi.dialog.YaokongPeiDialog;
import com.smarthome.magic.activity.yaokongqi.dialog.YaokongPeiFirstDialog;
import com.smarthome.magic.activity.yaokongqi.model.YaokongKeyModel;
import com.smarthome.magic.activity.yaokongqi.model.YaokongTagModel;
import com.smarthome.magic.app.AppConfig;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.mqtt_zhiling.ZnjjMqttMingLing;
import com.smarthome.magic.util.SoundPoolUtils;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.app.ConstanceValue.MSG_PEIWANG_SUCCESS;
import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;

public class WanNengYaoKongQiPeiDui extends BaseActivity {

    @BindView(R.id.ll_zidingyi)
    TextView ll_zidingyi;
    @BindView(R.id.iv_dianyuan)
    ImageView iv_dianyuan;
    @BindView(R.id.ll_dianyuan)
    LinearLayout ll_dianyuan;
    @BindView(R.id.iv_jingyin)
    ImageView iv_jingyin;
    @BindView(R.id.ll_jingyin)
    LinearLayout ll_jingyin;
    @BindView(R.id.iv_yinliang_add)
    ImageView iv_yinliang_add;
    @BindView(R.id.ll_yinliang_add)
    LinearLayout ll_yinliang_add;
    @BindView(R.id.iv_yinliang_jian)
    ImageView iv_yinliang_jian;
    @BindView(R.id.ll_yinliang_jian)
    LinearLayout ll_yinliang_jian;
    @BindView(R.id.tv_pindao_add)
    ImageView tv_pindao_add;
    @BindView(R.id.ll_pindao_add)
    LinearLayout ll_pindao_add;
    @BindView(R.id.tv_pindao_jian)
    ImageView tv_pindao_jian;
    @BindView(R.id.ll_pindao_jian)
    LinearLayout ll_pindao_jian;
    @BindView(R.id.iv_caidan)
    ImageView iv_caidan;
    @BindView(R.id.ll_caidan)
    LinearLayout ll_caidan;
    @BindView(R.id.tv_shuchu)
    TextView tv_shuchu;
    @BindView(R.id.ll_shuchu)
    LinearLayout ll_shuchu;
    @BindView(R.id.bt_up)
    TextView bt_up;
    @BindView(R.id.bt_down)
    TextView bt_down;
    @BindView(R.id.bt_left)
    TextView bt_left;
    @BindView(R.id.bt_right)
    TextView bt_right;
    @BindView(R.id.bt_ok)
    TextView bt_ok;

    private String shebeiMa;
    private String keyMa;
    private String keyName;

    private YaokongPeiFirstDialog firstDialog;
    private YaokongPeiDialog peiDialog;
    private TishiDialog tishiDialog;

    private ZnjjMqttMingLing znjjMqttMingLing;
    private String ccid;
    private String serverId;
    private String topic;
    private List<YaokongKeyModel> keyModels;

    @Override
    public int getContentViewResId() {
        return R.layout.layout_wanneng_yaokongqi_peidui_dianshi;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("?????????????????????");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));

        tv_rightTitle.setVisibility(View.VISIBLE);
        tv_rightTitle.setText("??????");
        tv_rightTitle.setTextSize(17);
        tv_rightTitle.setTextColor(Y.getColor(R.color.color_main));
        tv_rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePeidui();
            }
        });


        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, WanNengYaoKongQiPeiDui.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void back() {
        TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_CAOZUO, new TishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TishiDialog dialog) {
                String mingLingMa = "M22" + shebeiMa + ".";
                znjjMqttMingLing.yaoKongQiMingLing(mingLingMa, topic, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });
                finish();
            }

            @Override
            public void onDismiss(TishiDialog dialog) {

            }
        });
        dialog.setTextContent("?????????????????????????????????????????????????????????");
        dialog.setTextConfirm("??????");
        dialog.setTextCancel("??????");
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initMqtt();
        initDialog();
        initHuidiao();
        getnet();
    }

    private void initKeyModels() {
        keyModels = new ArrayList<>();
        keyModels.add(new YaokongKeyModel(shebeiMa + "01", "??????", "0"));
        keyModels.add(new YaokongKeyModel(shebeiMa + "02", "??????", "0"));
        keyModels.add(new YaokongKeyModel(shebeiMa + "03", "?????????", "0"));
        keyModels.add(new YaokongKeyModel(shebeiMa + "04", "?????????", "0"));
        keyModels.add(new YaokongKeyModel(shebeiMa + "05", "??????", "0"));
        keyModels.add(new YaokongKeyModel(shebeiMa + "06", "??????", "0"));
        keyModels.add(new YaokongKeyModel(shebeiMa + "07", "?????????", "0"));
        keyModels.add(new YaokongKeyModel(shebeiMa + "08", "?????????", "0"));
        keyModels.add(new YaokongKeyModel(shebeiMa + "09", "???", "0"));
        keyModels.add(new YaokongKeyModel(shebeiMa + "10", "???", "0"));
        keyModels.add(new YaokongKeyModel(shebeiMa + "11", "???", "0"));
        keyModels.add(new YaokongKeyModel(shebeiMa + "12", "???", "0"));
        keyModels.add(new YaokongKeyModel(shebeiMa + "13", "??????", "0"));
    }

    private void initMqtt() {
        ccid = PreferenceHelper.getInstance(mContext).getString(AppConfig.DEVICECCID, "");
        serverId = PreferenceHelper.getInstance(mContext).getString(AppConfig.SERVERID, "");
        znjjMqttMingLing = new ZnjjMqttMingLing(mContext);
        topic = "zn/hardware/" + serverId + ccid;
        znjjMqttMingLing.yaoKongQiPeiDui(topic,"M1728.", new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i("rair", "?????????????????????" + topic);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
        znjjMqttMingLing.subscribeLastAppShiShiXinXi_WithCanShu(ccid, serverId, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        znjjMqttMingLing.subscribeServerShiShiXinXi_WithCanShu(ccid, serverId, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }

    private void initDialog() {
        firstDialog = new YaokongPeiFirstDialog(mContext);
        firstDialog.show();

        peiDialog = new YaokongPeiDialog(mContext);

        tishiDialog = new TishiDialog(mContext, TishiDialog.TYPE_SUCESS, new TishiDialog.TishiDialogListener() {
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
        tishiDialog.setTextTitle("??????");
        tishiDialog.setTextCancel("");
        tishiDialog.setTextConfirm("??????");
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_WANNENGYAOKONGQI_CODE_PEIDUI) {
                    String msg = message.content.toString();
                    String shebeiCode = msg.substring(3, 7);
                    String keyCode = msg.substring(7, 9);
                    String isOk = msg.substring(9, 10);

                    if (shebeiMa.equals(shebeiCode)) {
                        peiDialog.dismiss();
                        if (isOk.equals("1")) {
                            SoundPoolUtils.soundPool(mContext, R.raw.hongwai_learn_suc);
                            tishiDialog.setTextContent(keyName + "???????????????!");
                            if (keyCode.equals("01")) {
                                iv_jingyin.setImageResource(R.mipmap.yaokong_icon_jingyin_blue);
                                ll_jingyin.setEnabled(false);
                                YaokongKeyModel keyModel = keyModels.get(0);

                                keyModel.setMark_status("1");
                                keyModels.set(0, keyModel);
                                tishiDialog.show();
                            } else if (keyCode.equals("02")) {
                                iv_dianyuan.setImageResource(R.mipmap.yaokong_icon_guanbi_blue);
                                ll_dianyuan.setEnabled(false);

                                YaokongKeyModel keyModel = keyModels.get(1);
                                keyModel.setMark_status("1");
                                keyModels.set(1, keyModel);
                                tishiDialog.show();
                            } else if (keyCode.equals("03")) {
                                iv_yinliang_add.setImageResource(R.mipmap.yaokong_icon_add_blue);
                                ll_yinliang_add.setEnabled(false);

                                YaokongKeyModel keyModel = keyModels.get(2);
                                keyModel.setMark_status("1");
                                keyModels.set(2, keyModel);
                                tishiDialog.show();
                            } else if (keyCode.equals("04")) {
                                iv_yinliang_jian.setImageResource(R.mipmap.yaokong_icon_reduce_blue);
                                ll_yinliang_jian.setEnabled(false);

                                YaokongKeyModel keyModel = keyModels.get(3);
                                keyModel.setMark_status("1");
                                keyModels.set(3, keyModel);
                                tishiDialog.show();
                            } else if (keyCode.equals("05")) {
                                iv_caidan.setImageResource(R.mipmap.yaokong_icon_menu_blue);
                                ll_caidan.setEnabled(false);

                                YaokongKeyModel keyModel = keyModels.get(4);
                                keyModel.setMark_status("1");
                                keyModels.set(4, keyModel);
                                tishiDialog.show();
                            } else if (keyCode.equals("06")) {
                                tv_shuchu.setTextColor(Y.getColor(R.color.color_main));
                                ll_shuchu.setEnabled(false);

                                YaokongKeyModel keyModel = keyModels.get(5);
                                keyModel.setMark_status("1");
                                keyModels.set(5, keyModel);
                                tishiDialog.show();
                            } else if (keyCode.equals("07")) {
                                tv_pindao_add.setImageResource(R.mipmap.yaokong_icon_add_blue);
                                ll_pindao_add.setEnabled(false);

                                YaokongKeyModel keyModel = keyModels.get(6);
                                keyModel.setMark_status("1");
                                keyModels.set(6, keyModel);
                                tishiDialog.show();
                            } else if (keyCode.equals("08")) {
                                tv_pindao_jian.setImageResource(R.mipmap.yaokong_icon_reduce_blue);
                                ll_pindao_jian.setEnabled(false);

                                YaokongKeyModel keyModel = keyModels.get(7);
                                keyModel.setMark_status("1");
                                keyModels.set(7, keyModel);
                                tishiDialog.show();
                            } else if (keyCode.equals("09")) {
                                bt_up.setTextColor(Y.getColor(R.color.color_main));
                                bt_up.setEnabled(false);

                                YaokongKeyModel keyModel = keyModels.get(8);
                                keyModel.setMark_status("1");
                                keyModels.set(8, keyModel);
                                tishiDialog.show();
                            } else if (keyCode.equals("10")) {
                                bt_down.setTextColor(Y.getColor(R.color.color_main));
                                bt_down.setEnabled(false);

                                YaokongKeyModel keyModel = keyModels.get(9);
                                keyModel.setMark_status("1");
                                keyModels.set(9, keyModel);
                                tishiDialog.show();
                            } else if (keyCode.equals("11")) {
                                bt_left.setTextColor(Y.getColor(R.color.color_main));
                                bt_left.setEnabled(false);

                                YaokongKeyModel keyModel = keyModels.get(10);
                                keyModel.setMark_status("1");
                                keyModels.set(10, keyModel);
                                tishiDialog.show();
                            } else if (keyCode.equals("12")) {
                                bt_right.setTextColor(Y.getColor(R.color.color_main));
                                bt_right.setEnabled(false);

                                YaokongKeyModel keyModel = keyModels.get(11);
                                keyModel.setMark_status("1");
                                keyModels.set(11, keyModel);
                                tishiDialog.show();
                            } else if (keyCode.equals("13")) {
                                bt_ok.setTextColor(Y.getColor(R.color.color_main));
                                bt_ok.setEnabled(false);

                                YaokongKeyModel keyModel = keyModels.get(12);
                                keyModel.setMark_status("1");
                                keyModels.set(12, keyModel);
                                tishiDialog.show();
                            }
                        } else {
                            tishiDialog.setTextContent(keyName + "?????????????????????????????????!");
                            tishiDialog.show();
                        }
                    }
                } else if (message.type == ConstanceValue.MSG_WANNENGYAOKONGQI_CODE_PEIDUI_ZIDINGYI) {
                    YaokongKeyModel keyModel = (YaokongKeyModel) message.content;
                    keyModels.add(keyModel);
                }
            }
        }));
    }

    @OnClick({R.id.ll_zidingyi, R.id.ll_dianyuan, R.id.ll_jingyin, R.id.ll_yinliang_add, R.id.ll_yinliang_jian, R.id.ll_pindao_add, R.id.ll_pindao_jian, R.id.ll_caidan, R.id.ll_shuchu, R.id.bt_up, R.id.bt_down, R.id.bt_left, R.id.bt_right, R.id.bt_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_zidingyi:
                WanNengYaoKongQiPeiDuiZidingyi.actionStart(mContext, shebeiMa, keyModels);
                break;
            case R.id.ll_dianyuan:
                clickKey("?????????", "02");
                break;
            case R.id.ll_jingyin:
                clickKey("?????????", "01");
                break;
            case R.id.ll_yinliang_add:
                clickKey("????????????", "03");
                break;
            case R.id.ll_yinliang_jian:
                clickKey("????????????", "04");
                break;
            case R.id.ll_pindao_add:
                clickKey("????????????", "07");
                break;
            case R.id.ll_pindao_jian:
                clickKey("????????????", "08");
                break;
            case R.id.ll_caidan:
                clickKey("?????????", "05");
                break;
            case R.id.ll_shuchu:
                clickKey("?????????", "06");
                break;
            case R.id.bt_up:
                clickKey("??????", "09");
                break;
            case R.id.bt_down:
                clickKey("??????", "10");
                break;
            case R.id.bt_left:
                clickKey("??????", "11");
                break;
            case R.id.bt_right:
                clickKey("??????", "12");
                break;
            case R.id.bt_ok:
                clickKey("?????????", "13");
                break;
        }
    }

    private void clickKey(String key, String minglingma) {
        keyName = key;
        keyMa = minglingma;

        peiDialog.setKey(key);
        peiDialog.show();

        sendMsg(minglingma);

        SoundPoolUtils.soundPool(mContext, R.raw.hongwai_learn_voice);
    }


    private void sendMsg(String code) {
        String mingLingMa = "M19" + shebeiMa + code + ".";
        znjjMqttMingLing.yaoKongQiMingLing(mingLingMa, topic, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }

    @Override
    public void onBackPressedSupport() {
        back();
    }

    private void getnet() {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "16071");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("device_type", "28");
        map.put("ccid", ccid);
        Gson gson = new Gson();
        OkGo.<YaokongTagModel>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<YaokongTagModel>() {
                    @Override
                    public void onSuccess(Response<YaokongTagModel> response) {
                        shebeiMa = response.body().getLabel_header();
                        initKeyModels();
                    }

                    @Override
                    public void onError(Response<YaokongTagModel> response) {
                        UIHelper.ToastMessage(mContext, response.getException().getMessage());
                        shebeiMa = "";
                    }

                    @Override
                    public void onStart(Request<YaokongTagModel, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }


    private void savePeidui() {
        boolean isOK = false;
        for (int i = 0; i < keyModels.size(); i++) {
            YaokongKeyModel keyModel = keyModels.get(i);
            String mark_status = keyModel.getMark_status();
            if (mark_status.equals("1")) {
                isOK = true;
            }
        }

        if (!isOK) {
            Y.t("????????????");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "16072");
        jsonObject.put("key", Urls.key);
        jsonObject.put("token", UserManager.getManager(mContext).getAppToken());
        jsonObject.put("label_header", shebeiMa);
        jsonObject.put("ccid", ccid);
        jsonObject.put("pro", keyModels);
        OkGo.<YaokongTagModel>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(jsonObject.toJSONString())
                .execute(new JsonCallback<YaokongTagModel>() {
                    @Override
                    public void onSuccess(Response<YaokongTagModel> response) {
                        if (response.body().getMsg_code().equals("0000")) {
                            TishiDialog dialog = new TishiDialog(mContext, TishiDialog.TYPE_SUCESS, new TishiDialog.TishiDialogListener() {
                                @Override
                                public void onClickCancel(View v, TishiDialog dialog) {

                                }

                                @Override
                                public void onClickConfirm(View v, TishiDialog dialog) {

                                }

                                @Override
                                public void onDismiss(TishiDialog dialog) {
                                    Notice notice = new Notice();
                                    notice.type = MSG_PEIWANG_SUCCESS;
                                    RxBus.getDefault().sendRx(notice);
                                    finish();
                                }
                            });
                            dialog.setTextContent("?????????????????????");
                            dialog.show();
                        } else {
                            Y.t(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<YaokongTagModel> response) {
                        Y.tError(response);
                    }

                    @Override
                    public void onStart(Request<YaokongTagModel, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }
}
