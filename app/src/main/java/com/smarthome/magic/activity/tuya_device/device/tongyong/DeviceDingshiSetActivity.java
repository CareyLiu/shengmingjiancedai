package com.smarthome.magic.activity.tuya_device.device.tongyong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.tuya_device.TuyaBaseDeviceActivity;
import com.smarthome.magic.activity.tuya_device.device.model.DpsTimeModel;
import com.smarthome.magic.activity.tuya_device.dialog.TuyaInputDialog;
import com.smarthome.magic.activity.tuya_device.utils.TuyaConfig;
import com.smarthome.magic.activity.tuya_device.utils.manager.TuyaDeviceManager;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.tuya.smart.android.device.builder.TuyaTimerBuilder;
import com.tuya.smart.android.device.enums.TimerDeviceTypeEnum;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.constant.TimerUpdateEnum;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class DeviceDingshiSetActivity extends TuyaBaseDeviceActivity {


    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_chongfu)
    TextView tv_chongfu;
    @BindView(R.id.ll_chongfu)
    LinearLayout ll_chongfu;
    @BindView(R.id.tv_beizhu)
    TextView tv_beizhu;
    @BindView(R.id.ll_beizhu)
    LinearLayout ll_beizhu;
    @BindView(R.id.tv_dps_key)
    TextView tv_dps_key;
    @BindView(R.id.tv_dps_value)
    TextView tv_dps_value;
    @BindView(R.id.ll_dps_kongzhi)
    LinearLayout ll_dps_kongzhi;


    private TimePickerView timePicker;

    private String devId;
    private String productId;
    private String category;

    private String dps;
    private String dpsId;
    private Object value;
    private String time;
    private String loops;

    /**
     * ????????????Activty????????????Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DeviceDingshiSetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getContentViewResId() {
        return R.layout.act_device_dingshi_set;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("????????????");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));

        tv_rightTitle.setVisibility(View.VISIBLE);
        tv_rightTitle.setTextSize(16);
        tv_rightTitle.setTextColor(Y.getColor(R.color.color_main));
        tv_rightTitle.setText("??????");
        tv_rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDingshi();
            }
        });

        mToolbar.setNavigationIcon(R.mipmap.back_black);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
        initPeizhi();
        initHuidiao();
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_DEVICE_DINGSHI_CHONGFU) {
                    loops = message.content.toString();
                    Y.e("?????????????????????" + loops);

                    if (loops.equals("0000000")) {
                        tv_chongfu.setText("????????????");
                    } else if (loops.equals("1111111")) {
                        tv_chongfu.setText("??????");
                    } else {
                        String week_0 = loops.substring(0, 1);
                        String week_1 = loops.substring(1, 2);
                        String week_2 = loops.substring(2, 3);
                        String week_3 = loops.substring(3, 4);
                        String week_4 = loops.substring(4, 5);
                        String week_5 = loops.substring(5, 6);
                        String week_6 = loops.substring(6);

                        String chongfuText = "";
                        if (week_0.equals("1")) {
                            chongfuText = chongfuText + "?????? ";
                        }
                        if (week_1.equals("1")) {
                            chongfuText = chongfuText + "?????? ";
                        }
                        if (week_2.equals("1")) {
                            chongfuText = chongfuText + "?????? ";
                        }
                        if (week_3.equals("1")) {
                            chongfuText = chongfuText + "?????? ";
                        }
                        if (week_4.equals("1")) {
                            chongfuText = chongfuText + "?????? ";
                        }
                        if (week_5.equals("1")) {
                            chongfuText = chongfuText + "?????? ";
                        }
                        if (week_6.equals("1")) {
                            chongfuText = chongfuText + "?????? ";
                        }
                        tv_chongfu.setText(chongfuText);
                    }
                }
            }
        }));
    }


    private void init() {
        DeviceBean deviceBeen = TuyaDeviceManager.getDeviceManager().getDeviceBeen();
        devId = deviceBeen.getDevId();
        productId = deviceBeen.getProductId();
        category = deviceBeen.getProductBean().getCategory();

        time = "00:00";
        loops = "0000000";
    }

    private void initPeizhi() {
        switch (productId) {
            case TuyaConfig.PRODUCTID_CAMERA_A:
            case TuyaConfig.PRODUCTID_CAMERA_B:
                dpsId = "119";
                value = true;
                break;
            case TuyaConfig.PRODUCTID_CHAZUO_A:
            case TuyaConfig.PRODUCTID_CHAZUO_B:
            case TuyaConfig.PRODUCTID_CHAZUO_WG:
                setChazuo("??????");
                break;
            case TuyaConfig.PRODUCTID_SWITCH_THREE:
                setSwitch("??????", "??????");
                break;

        }
    }

    private void setSwitch(String kongzhiName, String kongzhi) {
        if (kongzhiName.equals("??????")) {
            dpsId = "1";
        } else if (kongzhiName.equals("??????")) {
            dpsId = "2";
        } else if (kongzhiName.equals("??????")) {
            dpsId = "3";
        }

        if (kongzhi.equals("??????")) {
            value = true;
        } else {
            value = false;
        }

        tv_dps_key.setText("??????");
        tv_dps_value.setText(kongzhiName + ":" + kongzhi);
    }


    private void setChazuo(String kongzhi) {
        dpsId = "1";
        if (kongzhi.equals("??????")) {
            value = true;
        } else {
            value = false;
        }

        tv_dps_key.setText("??????");
        tv_dps_value.setText(kongzhi);
    }

    private void addDingshi() {
        showProgressDialog();
        Map<String, Object> map = new HashMap<>();
        map.put(dpsId, value);
        DpsTimeModel model = new DpsTimeModel(map, time);
        dps = new Gson().toJson(model);
        String beizhu = tv_beizhu.getText().toString();
        Y.e("?????????dps?????????   " + dps + "    " + loops + "  " + beizhu);
        TuyaTimerBuilder builder = new TuyaTimerBuilder.Builder()
                .taskName("")
                .devId(devId)
                .deviceType(TimerDeviceTypeEnum.DEVICE)
                .actions(dps)
                .loops(loops)
                .aliasName(beizhu)
                .status(1)
                .appPush(false)
                .build();
        TuyaHomeSdk.getTimerInstance().addTimer(builder, new IResultCallback() {
            @Override
            public void onSuccess() {
                dismissProgressDialog();
                Y.e("??????????????????");
                Notice notice = new Notice();
                notice.type = ConstanceValue.MSG_DEVICE_DINGSHI;
                RxBus.getDefault().sendRx(notice);
                finish();
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                dismissProgressDialog();
                Y.t("??????????????????:" + errorMsg);
            }
        });
    }

    @OnClick({R.id.tv_time, R.id.ll_chongfu, R.id.ll_beizhu, R.id.ll_dps_kongzhi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_time:
                clickTime();
                break;
            case R.id.ll_chongfu:
                DeviceDingshiChongfuActivity.actionStart(mContext, loops);
                break;
            case R.id.ll_beizhu:
                clickBeizhu();
                break;
            case R.id.ll_dps_kongzhi:
                clickKongzhi();
                break;
        }
    }

    private void clickKongzhi() {
        Y.e("???????????????  " + productId);
        switch (productId) {
            case TuyaConfig.PRODUCTID_CAMERA_A:
            case TuyaConfig.PRODUCTID_CAMERA_B:

                break;
            case TuyaConfig.PRODUCTID_CHAZUO_A:
            case TuyaConfig.PRODUCTID_CHAZUO_B:
            case TuyaConfig.PRODUCTID_CHAZUO_WG:
                kongzhiChazuo();
                break;
            case TuyaConfig.PRODUCTID_SWITCH_THREE:
                clickSwitchSan();
                break;
        }
    }

    private void clickSwitchSan() {
        List<String> kongzhiName = new ArrayList<>();
        kongzhiName.add("??????");
        kongzhiName.add("??????");
        kongzhiName.add("??????");

        List<String> kongzhis = new ArrayList<>();
        kongzhis.add("??????");
        kongzhis.add("??????");
        OptionsPickerView<String> kongZhiPicker = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String kongzhiMing = kongzhiName.get(options1);
                String kongzhi = kongzhis.get(option2);
                setSwitch(kongzhiMing, kongzhi);
            }
        }).setTitleText("??????").build();
        kongZhiPicker.setNPicker(kongzhiName, kongzhis, null);
        kongZhiPicker.show();
    }

    private void kongzhiChazuo() {
        List<String> kongzhis = new ArrayList<>();
        kongzhis.add("??????");
        kongzhis.add("??????");
        OptionsPickerView<String> kongZhiPicker = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String kongzhi = kongzhis.get(options1);
                setChazuo(kongzhi);
            }
        }).setTitleText("??????").build();
        kongZhiPicker.setPicker(kongzhis);
        kongZhiPicker.show();
    }

    private void clickBeizhu() {
        TuyaInputDialog dialog = new TuyaInputDialog(mContext, new TuyaInputDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TuyaInputDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TuyaInputDialog dialog) {
                tv_beizhu.setText(dialog.getTextContent());
            }

            @Override
            public void onDismiss(TuyaInputDialog dialog) {

            }
        });
        dialog.setDismissAfterClick(true);
        dialog.setTextTitle("???????????????");
        dialog.setTextInput(InputType.TYPE_CLASS_TEXT);
        dialog.setTextContent(tv_beizhu.getText().toString());
        dialog.show();
    }

    private void clickTime() {
        boolean[] select = {false, false, false, true, true, false};
        timePicker = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//??????????????????
                int hours = date.getHours();
                int minutes = date.getMinutes();
                if (hours < 10) {
                    if (minutes < 10) {
                        time = "0" + hours + ":" + "0" + minutes;
                    } else {
                        time = "0" + hours + ":" + minutes;
                    }
                } else {
                    if (minutes < 10) {
                        time = hours + ":" + "0" + minutes;
                    } else {
                        time = hours + ":" + minutes;
                    }
                }
                tv_time.setText(time);
            }
        })
                .setType(select)// ??????????????????
                .build();
        timePicker.show();
    }
 }

