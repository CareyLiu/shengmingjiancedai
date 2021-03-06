package com.smarthome.magic.activity.tuya_device.device.tongyong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.ZhiNengRoomManageActivity;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.tuya_device.TuyaBaseDeviceActivity;
import com.smarthome.magic.activity.tuya_device.dialog.TuyaTishiDialog;
import com.smarthome.magic.activity.tuya_device.utils.TuyaDialogUtils;
import com.smarthome.magic.activity.tuya_device.utils.manager.TuyaDeviceManager;
import com.smarthome.magic.app.AppConfig;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.MyCarCaoZuoDialog_CaoZuoTIshi;
import com.smarthome.magic.dialog.MyCarCaoZuoDialog_Success;
import com.smarthome.magic.dialog.ZhiNengFamilyAddDIalog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.ZhiNengFamilyEditBean;
import com.tuya.smart.sdk.api.IResultCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;

public class DeviceSetActivity extends TuyaBaseDeviceActivity {


    @BindView(R.id.ll_device_info)
    LinearLayout ll_device_info;
    @BindView(R.id.tv_room_name)
    TextView tv_room_name;
    @BindView(R.id.ll_device_weizhi)
    LinearLayout ll_device_weizhi;
    @BindView(R.id.tv_device_name)
    TextView tv_device_name;
    @BindView(R.id.ll_device_name)
    LinearLayout ll_device_name;
    @BindView(R.id.iv_switch)
    ImageView iv_switch;
    @BindView(R.id.ll_device_wenti)
    LinearLayout ll_device_wenti;
    @BindView(R.id.ll_device_addzhu)
    LinearLayout ll_device_addzhu;
    @BindView(R.id.ll_device_wangluo)
    LinearLayout ll_device_wangluo;
    @BindView(R.id.bt_delete)
    TextView bt_delete;

    private String family_id;
    private String device_id;
    private String old_name;
    private String room_name;
    private String member_type;
    private String tuyaDevId;


    /**
     * ????????????Activty????????????Activity
     */
    public static void actionStart(Context context, String member_type, String device_id, String old_name, String room_name) {
        Intent intent = new Intent(context, DeviceSetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("member_type", member_type);
        intent.putExtra("device_id", device_id);
        intent.putExtra("old_name", old_name);
        intent.putExtra("room_name", room_name);
        context.startActivity(intent);
    }

    @Override
    public int getContentViewResId() {
        return R.layout.act_device_set;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("??????");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
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
        initHuidiao();
    }

    private void init() {
        family_id = PreferenceHelper.getInstance(mContext).getString(AppConfig.PEIWANG_FAMILYID, "");
        device_id = getIntent().getStringExtra("device_id");
        old_name = getIntent().getStringExtra("old_name");
        room_name = getIntent().getStringExtra("room_name");
        member_type = getIntent().getStringExtra("member_type");

        if (TuyaDeviceManager.getDeviceManager().getDeviceBeen() != null) {
            tuyaDevId = TuyaDeviceManager.getDeviceManager().getDevId();
        } else {
            tuyaDevId = "";
        }

        tv_room_name.setText(room_name);
        tv_device_name.setText(old_name);
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_DEVICE_DATA) {

                } else if (message.type == ConstanceValue.MSG_DEVICE_ROOM_NAME_CHANGE) {
                    if (device_id.equals(message.devId)){
                        tv_room_name.setText(message.content.toString());
                    }
                } else if (message.type == ConstanceValue.MSG_ROOM_DEVICE_CHANGENAME) {
                    changeDevice(message.content.toString());
                }
            }
        }));
    }

    @OnClick({R.id.bt_delete, R.id.ll_device_info, R.id.ll_device_weizhi, R.id.ll_device_name, R.id.iv_switch, R.id.ll_device_wenti, R.id.ll_device_addzhu, R.id.ll_device_wangluo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_device_info:
                break;
            case R.id.ll_device_weizhi:
                clickRoom();
                break;
            case R.id.ll_device_name:
                clickName();
                break;
            case R.id.iv_switch:
                break;
            case R.id.ll_device_wenti:
                break;
            case R.id.ll_device_addzhu:
                break;
            case R.id.ll_device_wangluo:
                break;
            case R.id.bt_delete:
                clickDelete();
                break;
        }
    }

    private void clickRoom() {
        Bundle bundle = new Bundle();
        bundle.putString("device_id", device_id);
        bundle.putString("family_id", family_id);
        bundle.putString("member_type", member_type);
        startActivity(new Intent(mContext, ZhiNengRoomManageActivity.class).putExtras(bundle));
    }

    private void clickName() {
        ZhiNengFamilyAddDIalog zhiNengFamilyAddDIalog = new ZhiNengFamilyAddDIalog(mContext, ConstanceValue.MSG_ROOM_DEVICE_CHANGENAME);
        zhiNengFamilyAddDIalog.show();
    }

    /**
     * ??????????????????
     */
    private void changeDevice(String deviceName) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "16033");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("family_id", family_id);
        map.put("device_id", device_id);
        map.put("old_name", old_name);
        map.put("device_name", deviceName);
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<ZhiNengFamilyEditBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiNengFamilyEditBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        if (response.body().msg_code.equals("0000")) {
                            tv_device_name.setText(deviceName);
                            MyCarCaoZuoDialog_Success myCarCaoZuoDialog_success = new MyCarCaoZuoDialog_Success(DeviceSetActivity.this,
                                    "??????", "?????????????????????", "??????", new MyCarCaoZuoDialog_Success.OnDialogItemClickListener() {
                                @Override
                                public void clickLeft() {

                                }

                                @Override
                                public void clickRight() {

                                }
                            });
                            myCarCaoZuoDialog_success.show();
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        Y.tError(response);
                    }
                });
    }


    private void clickDelete() {
        TuyaTishiDialog dialog = new TuyaTishiDialog(mContext, new TuyaTishiDialog.TishiDialogListener() {
            @Override
            public void onClickCancel(View v, TuyaTishiDialog dialog) {

            }

            @Override
            public void onClickConfirm(View v, TuyaTishiDialog dialog) {
                deleteDevice();
            }

            @Override
            public void onDismiss(TuyaTishiDialog dialog) {

            }
        });
        dialog.setTextCont("??????????????????");
        dialog.setTextConfirm("??????");
        dialog.show();
    }

    /**
     * ????????????
     */
    private void deleteDevice() {
        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("code", "16034");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("family_id", family_id);
        map.put("device_id", device_id);
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<ZhiNengFamilyEditBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiNengFamilyEditBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        dismissProgressDialog();
                        TuyaTishiDialog dialog = new TuyaTishiDialog(mContext, new TuyaTishiDialog.TishiDialogListener() {
                            @Override
                            public void onClickCancel(View v, TuyaTishiDialog dialog) {

                            }

                            @Override
                            public void onClickConfirm(View v, TuyaTishiDialog dialog) {

                            }

                            @Override
                            public void onDismiss(TuyaTishiDialog dialog) {
                                Notice notice = new Notice();
                                notice.type = ConstanceValue.MSG_DEVICE_DELETE;
                                notice.devId = device_id;
                                RxBus.getDefault().sendRx(notice);
                                finish();
                            }
                        });
                        dialog.setTextCont("??????????????????");
                        dialog.setTextCancel("");
                        dialog.show();

                        if (TuyaDeviceManager.getDeviceManager().getDevice() != null) {
                            TuyaDeviceManager.getDeviceManager().getDevice().removeDevice(new IResultCallback() {
                                @Override
                                public void onError(String errorCode, String errorMsg) {
                                    Y.e("??????????????????:" + errorMsg);
                                }

                                @Override
                                public void onSuccess() {
                                    Y.e("??????????????????");
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        dismissProgressDialog();
                        Y.tError(response);
                    }
                });
    }
}
