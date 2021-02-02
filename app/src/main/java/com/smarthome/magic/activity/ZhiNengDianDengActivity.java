package com.smarthome.magic.activity;import android.content.Context;import android.content.Intent;import android.os.Bundle;import android.util.Log;import android.view.View;import android.widget.CompoundButton;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.RelativeLayout;import android.widget.Switch;import android.widget.TextView;import android.widget.Toast;import androidx.annotation.NonNull;import com.bumptech.glide.Glide;import com.google.gson.Gson;import com.jaeger.library.StatusBarUtil;import com.lzy.okgo.OkGo;import com.lzy.okgo.model.Response;import com.lzy.okgo.request.base.Request;import com.rairmmd.andmqtt.AndMqtt;import com.rairmmd.andmqtt.MqttSubscribe;import com.rairmmd.andmqtt.MqttUnSubscribe;import com.scwang.smartrefresh.layout.SmartRefreshLayout;import com.scwang.smartrefresh.layout.api.RefreshLayout;import com.scwang.smartrefresh.layout.listener.OnRefreshListener;import com.smarthome.magic.R;import com.smarthome.magic.app.App;import com.smarthome.magic.app.BaseActivity;import com.smarthome.magic.app.ConstanceValue;import com.smarthome.magic.app.Notice;import com.smarthome.magic.app.UIHelper;import com.smarthome.magic.callback.JsonCallback;import com.smarthome.magic.common.StringUtils;import com.smarthome.magic.config.AppResponse;import com.smarthome.magic.config.PreferenceHelper;import com.smarthome.magic.config.UserManager;import com.smarthome.magic.dialog.LordingDialog;import com.smarthome.magic.dialog.MyCarCaoZuoDialog_CaoZuoTIshi;import com.smarthome.magic.dialog.MyCarCaoZuoDialog_Success;import com.smarthome.magic.dialog.ZhiNengFamilyAddDIalog;import com.smarthome.magic.dialog.newdia.TishiDialog;import com.smarthome.magic.get_net.Urls;import com.smarthome.magic.model.SuiYiTieModel;import com.smarthome.magic.model.ZhiNengFamilyEditBean;import com.smarthome.magic.model.ZhiiNengRoomDeviceRoomBean;import com.smarthome.magic.mqtt_zhiling.ZnjjMqttMingLing;import com.smarthome.magic.util.DoMqttValue;import com.smarthome.magic.util.SoundPoolUtils;import com.suke.widget.SwitchButton;import org.eclipse.paho.client.mqttv3.IMqttActionListener;import org.eclipse.paho.client.mqttv3.IMqttToken;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;import butterknife.BindView;import rx.android.schedulers.AndroidSchedulers;import rx.functions.Action1;import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;public class ZhiNengDianDengActivity extends BaseActivity implements View.OnClickListener {    @BindView(R.id.iv_head)    ImageView ivHead;    @BindView(R.id.ll_online_state)    LinearLayout llOnlineState;    @BindView(R.id.iv_auto)    ImageView ivAuto;    @BindView(R.id.tv_family_title)    TextView tvFamilyTitle;    @BindView(R.id.tv_family)    TextView tvFamily;    @BindView(R.id.tv_room_title)    TextView tvRoomTitle;    @BindView(R.id.tv_room)    TextView tvRoom;    @BindView(R.id.img_room_into)    ImageView imgRoomInto;    @BindView(R.id.rl_room)    RelativeLayout rlRoom;    @BindView(R.id.tv_device_name_title)    TextView tvDeviceNameTitle;    @BindView(R.id.tv_device_name)    TextView tvDeviceName;    @BindView(R.id.img_device_into)    ImageView imgDeviceInto;    @BindView(R.id.rl_device_name)    RelativeLayout rlDeviceName;    @BindView(R.id.tv_device_type_title)    TextView tvDeviceTypeTitle;    @BindView(R.id.tv_device_type)    TextView tvDeviceType;    @BindView(R.id.tv_device_state_title)    TextView tvDeviceStateTitle;    @BindView(R.id.tv_device_state)    TextView tvDeviceState;    @BindView(R.id.rl_device_state)    RelativeLayout rlDeviceState;    @BindView(R.id.tv_switch_title)    TextView tvSwitchTitle;    @BindView(R.id.switch_button)    Switch switchButton;    @BindView(R.id.rl_switch)    RelativeLayout rlSwitch;    @BindView(R.id.tv_auto_switch_title)    TextView tvAutoSwitchTitle;    @BindView(R.id.auto_switch_button)    SwitchButton autoSwitchButton;    @BindView(R.id.rl_auto_switch)    RelativeLayout rlAutoSwitch;    @BindView(R.id.iv_tishi)    ImageView ivTishi;    @BindView(R.id.tv_noti)    TextView tvNoti;    @BindView(R.id.tv_room_delete)    TextView tvRoomDelete;    @BindView(R.id.srL_smart)    SmartRefreshLayout srLSmart;    @BindView(R.id.tv_caozuotishiyin)    TextView tvCaozuotishiyin;    @BindView(R.id.sbtn_caozuotishiyin)    Switch sbtnCaozuotishiyin;    @BindView(R.id.rl_caozuotishiyin)    RelativeLayout rlCaozuotishiyin;    private Context context = ZhiNengDianDengActivity.this;    private String device_id = "";    private String device_type = "";    private String member_type = "";    private String device_ccid = "";//ccid    private String workState = "";//工作状态是否开启    private ZhiiNengRoomDeviceRoomBean.DataBean dataBean;    private boolean autoState = false;    private boolean switchState = false;    private boolean ertongSwitchState = false;    public ZnjjMqttMingLing mqttMingLing = null;    private String erTongMoShi = "0";// 儿童模式默认关闭    private String swbtnfunction_show = "show";//function 可以执行功能 _ show 只做页面展示    private String whatUWant = "";//控制灯的效果    private String GUANDENG = "guandeng";//关灯    private String KAIDENG = "kaideng";//开灯    private LordingDialog lordingDialog;    private String isVoice = "1";    @Override    public int getContentViewResId() {        return R.layout.activity_zhineng_diandeng_detail;    }    @Override    public void initImmersion() {        mImmersionBar.with(this).statusBarColor(R.color.white).init();    }    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        StatusBarUtil.setLightMode(this);        PreferenceHelper.getInstance(mContext).putString(App.CHOOSE_KONGZHI_XIANGMU, DoMqttValue.ZHINENGJIAJU);        initToolbar();        initView();        getnet();        lordingDialog = new LordingDialog(mContext);        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {            @Override            public void call(Notice message) {                if (message.type == ConstanceValue.MSG_SHEBEIZHUANGTAI) {                    List<String> messageList = (List<String>) message.content;                    String zhuangZhiId = messageList.get(0);                    String kaiGuanDengZhuangTai = messageList.get(1);                    if (kaiGuanDengZhuangTai.equals("1")) {//开灯                        if (whatUWant.equals(KAIDENG)) {                            swbtnfunction_show = "show";                            switchButton.setChecked(true);                            whatUWant = "";                            if (lordingDialog != null) {                                if (lordingDialog.isShowing()) {                                    lordingDialog.dismiss();                                }                            }                        }                        Glide.with(mContext).load(R.mipmap.yikaideng).into(ivHead);                    } else if (kaiGuanDengZhuangTai.equals("2")) {//关灯                        if (whatUWant.equals(GUANDENG)) {                            swbtnfunction_show = "show";                            switchButton.setChecked(false);                            whatUWant = "";                            if (lordingDialog != null) {                                if (lordingDialog.isShowing()) {                                    lordingDialog.dismiss();                                }                            }                        }                        Glide.with(mContext).load(R.mipmap.weikaideng).into(ivHead);                    }                    swbtnfunction_show = "function";                }            }        }));    }    private void initView() {        device_id = getIntent().getStringExtra("device_id");        if (device_id == null) {            device_id = "";        }        device_type = getIntent().getStringExtra("device_type");        if (device_type == null) {            device_type = "";        }        member_type = getIntent().getStringExtra("member_type");        if (member_type == null) {            member_type = "";        }        srLSmart.setOnRefreshListener(new OnRefreshListener() {            @Override            public void onRefresh(@NonNull RefreshLayout refreshLayout) {                getnet();            }        });        srLSmart.setEnableLoadMore(false);        ivAuto.setOnClickListener(this);        rlRoom.setOnClickListener(this);        rlDeviceName.setOnClickListener(this);        tvRoomDelete.setOnClickListener(this);        sbtnCaozuotishiyin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {            @Override            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {                if (!buttonView.isPressed()) {                    return;                }                if (isChecked) {                    sheZhiTiShiYin("1");                } else {                    sheZhiTiShiYin("2");                }            }        });        autoSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {            @Override            public void onCheckedChanged(SwitchButton view, boolean isChecked) {                if (isChecked) {                    if (!switchState) {                        String kaiqitishi = "";                        if (device_type.equals("03")) {                            kaiqitishi = "开启自动喂鱼？默认每天8点自动喂鱼，您可修改时间哦";                        } else {                            kaiqitishi = "开启自动浇花？默认每天自动浇花30秒，您可修改时间哦";                        }                        MyCarCaoZuoDialog_CaoZuoTIshi myCarCaoZuoDialog_caoZuoTIshi = new MyCarCaoZuoDialog_CaoZuoTIshi(ZhiNengDianDengActivity.this,                                "提示", kaiqitishi, "取消", "确定", new MyCarCaoZuoDialog_CaoZuoTIshi.OnDialogItemClickListener() {                            @Override                            public void clickLeft() {                                autoSwitchButton.setChecked(false);                                switchState = false;                            }                            @Override                            public void clickRight() {                                autoSwitchButton.setChecked(true);                                switchState = true;                            }                        });                        myCarCaoZuoDialog_caoZuoTIshi.show();                    }                } else {                    if (switchState) {                        String guanbitishi = "";                        if (device_type.equals("03")) {                            guanbitishi = "您确定要关闭自动喂鱼吗？";                        } else {                            guanbitishi = "您确定要关闭自动浇花吗？";                        }                        MyCarCaoZuoDialog_CaoZuoTIshi myCarCaoZuoDialog_caoZuoTIshi = new MyCarCaoZuoDialog_CaoZuoTIshi(ZhiNengDianDengActivity.this,                                "提示", guanbitishi, "取消", "确定", new MyCarCaoZuoDialog_CaoZuoTIshi.OnDialogItemClickListener() {                            @Override                            public void clickLeft() {                                autoSwitchButton.setChecked(true);                                switchState = true;                            }                            @Override                            public void clickRight() {                                autoSwitchButton.setChecked(false);                                switchState = false;                            }                        });                        myCarCaoZuoDialog_caoZuoTIshi.show();                    }                }            }        });        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {            @Override            public void call(Notice message) {                if (message.type == ConstanceValue.MSG_ROOM_DEVICE_CHANGENAME) {                    changeDevice(message.content.toString());                }            }        }));    }    @Override    public boolean showToolBar() {        return true;    }    private void sheZhiTiShiYin(String isVoice) {        //访问网络获取数据 下面的列表数据        Map<String, String> map = new HashMap<>();        map.put("code", "16053");        map.put("key", Urls.key);        map.put("token", UserManager.getManager(mContext).getAppToken());        map.put("device_id", device_id);        map.put("is_voice", isVoice);        Gson gson = new Gson();        String a = gson.toJson(map);        Log.e("map_data", gson.toJson(map));        OkGo.<AppResponse<SuiYiTieModel.DataBean>>post(ZHINENGJIAJU)                .tag(this)//                .upJson(gson.toJson(map))                .execute(new JsonCallback<AppResponse<SuiYiTieModel.DataBean>>() {                    @Override                    public void onSuccess(Response<AppResponse<SuiYiTieModel.DataBean>> response) {                        if (isVoice.equals("1")) {                            UIHelper.ToastMessage(mContext, "开启提示音设置成功");                            ZhiNengDianDengActivity.this.isVoice = "1";                        } else if (isVoice.equals("2")) {                            UIHelper.ToastMessage(mContext, "关闭提示音设置成功");                            ZhiNengDianDengActivity.this.isVoice = "2";                        }                        showLoadSuccess();                    }                    @Override                    public void onError(Response<AppResponse<SuiYiTieModel.DataBean>> response) {                        String str = response.getException().getMessage();                        UIHelper.ToastMessage(mContext, response.getException().getMessage());                    }                    @Override                    public void onStart(Request<AppResponse<SuiYiTieModel.DataBean>, ? extends Request> request) {                        super.onStart(request);                        showLoading();                    }                    @Override                    public void onFinish() {                        super.onFinish();                    }                });    }    @Override    protected void initToolbar() {        super.initToolbar();        tv_title.setText("设备详情");        tv_title.setTextSize(17);        tv_title.setTextColor(getResources().getColor(R.color.black));        mToolbar.setNavigationIcon(R.mipmap.backbutton);        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                finish();            }        });    }    @Override    public void onClick(View v) {        switch (v.getId()) {            case R.id.iv_auto:                break;            case R.id.rl_room:                Bundle bundle = new Bundle();                bundle.putString("device_id", dataBean.getDevice_id());                bundle.putString("family_id", dataBean.getFamily_id());                startActivity(new Intent(context, ZhiNengRoomManageActivity.class).putExtras(bundle));                break;            case R.id.rl_device_name:                ZhiNengFamilyAddDIalog zhiNengFamilyAddDIalog = new ZhiNengFamilyAddDIalog(context, ConstanceValue.MSG_ROOM_DEVICE_CHANGENAME);                zhiNengFamilyAddDIalog.show();                break;            case R.id.tv_room_delete:                MyCarCaoZuoDialog_CaoZuoTIshi myCarCaoZuoDialog_caoZuoTIshi = new MyCarCaoZuoDialog_CaoZuoTIshi(context,                        "提示", "确定要删除设备吗？", "取消", "确定", new MyCarCaoZuoDialog_CaoZuoTIshi.OnDialogItemClickListener() {                    @Override                    public void clickLeft() {                    }                    @Override                    public void clickRight() {                        if (member_type.equals("1")) {                            if (dataBean.getDevice_type().equals("0")) {                                //删除主控设备                                deleteMainDevice();                            } else {                                //删除设备                                deleteDevice();                            }                        } else {                            Toast.makeText(context, "操作失败，需要管理员身份", Toast.LENGTH_SHORT).show();                        }                    }                });                myCarCaoZuoDialog_caoZuoTIshi.show();                break;        }    }    String nowData;    String hardwareData;    String deviceState;    private String onlineState;    private void getnet() {        //访问网络获取数据 下面的列表数据        Map<String, String> map = new HashMap<>();        map.put("code", "16035");        map.put("key", Urls.key);        map.put("token", UserManager.getManager(context).getAppToken());        map.put("device_id", device_id);        Gson gson = new Gson();        String a = gson.toJson(map);        Log.e("map_data", gson.toJson(map));        OkGo.<AppResponse<ZhiiNengRoomDeviceRoomBean.DataBean>>post(ZHINENGJIAJU)                .tag(this)//                .upJson(gson.toJson(map))                .execute(new JsonCallback<AppResponse<ZhiiNengRoomDeviceRoomBean.DataBean>>() {                    @Override                    public void onSuccess(Response<AppResponse<ZhiiNengRoomDeviceRoomBean.DataBean>> response) {                        if (srLSmart != null) {                            srLSmart.setEnableRefresh(true);                            srLSmart.finishRefresh();                            srLSmart.setEnableLoadMore(false);                        }                        if (response.body().msg.equals("ok")) {                            dataBean = response.body().data.get(0);                            tvFamily.setText(dataBean.getFamily_name());                            tvRoom.setText(dataBean.getRoom_name());                            tvDeviceName.setText(dataBean.getDevice_name());                            tvDeviceType.setText(dataBean.getDevice_name());                            device_ccid = dataBean.getDevice_ccid();                            onlineState = dataBean.getOnline_state();                            rlDeviceState.setVisibility(View.VISIBLE);                            if (onlineState.equals("1")) {                                tvDeviceState.setText("正常");                            } else {                                tvDeviceState.setText("离线");                            }                            if (dataBean.getWork_state() != null) {                                if (dataBean.getWork_state().equals("1")) {                                    switchButton.setChecked(true);                                    Glide.with(mContext).load(R.mipmap.yikaideng).into(ivHead);                                } else if (dataBean.getWork_state().equals("2")) {                                    switchButton.setChecked(false);                                    Glide.with(mContext).load(R.mipmap.weikaideng).into(ivHead);                                }                            } else {                                Glide.with(mContext).load(R.mipmap.weikaideng).into(ivHead);                            }//                            switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {//                                @Override//                                public void onCheckedChanged(SwitchButton view, boolean isChecked) {//                                    if(!view.isPressed()) {//                                        return;//                                    }//                                    if (!StringUtils.isEmpty(whatUWant)) {//                                        UIHelper.ToastMessage(mContext, "等待上一个接收响应，请稍后尝试");//                                        return;//                                    }////                                    if (isChecked) {//                                        whatUWant = KAIDENG;//                                        mqttMingLing.setAction(device_ccid, "01", new IMqttActionListener() {//                                            @Override//                                            public void onSuccess(IMqttToken asyncActionToken) {//                                                UIHelper.ToastMessage(mContext, "当前装置开启");////                                                List<String> stringList = new ArrayList<>();//                                                stringList.add(device_ccid);//                                                stringList.add("1");////                                            }////                                            @Override//                                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {//                                                UIHelper.ToastMessage(mContext, "未发送指令");//                                            }//                                        });//                                    } else {//                                        whatUWant = GUANDENG;//                                        mqttMingLing.setAction(device_ccid, "02", new IMqttActionListener() {//                                            @Override//                                            public void onSuccess(IMqttToken asyncActionToken) {//                                                UIHelper.ToastMessage(mContext, "当前装置关闭");////                                                List<String> stringList = new ArrayList<>();//                                                stringList.add(device_ccid);//                                                stringList.add("2");//////                                                Notice notice = new Notice();////                                                notice.type = ConstanceValue.MSG_SHEBEIZHUANGTAI;////                                                notice.content = stringList;////                                                Log.i("Rair", notice.content.toString());////                                                RxBus.getDefault().sendRx(notice);//                                            }////                                            @Override//                                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {//                                                UIHelper.ToastMessage(mContext, "未发送指令");//                                            }//                                        });//                                    }//                                }//                            });                            switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {                                @Override                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {                                    if (!buttonView.isPressed()) {                                        return;                                    }                                    if (isChecked) {                                        if (isVoice.equals("1"))                                            SoundPoolUtils.soundPool(mContext, R.raw.kaideng);                                        whatUWant = KAIDENG;                                        lordingDialog.setTextMsg("正在开灯，请稍后...");                                        lordingDialog.show();                                        mqttMingLing.setAction(device_ccid, "01", new IMqttActionListener() {                                            @Override                                            public void onSuccess(IMqttToken asyncActionToken) {                                                // UIHelper.ToastMessage(mContext, "当前装置开启");                                                List<String> stringList = new ArrayList<>();                                                stringList.add(device_ccid);                                                stringList.add("1");                                            }                                            @Override                                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {                                                UIHelper.ToastMessage(mContext, "未发送指令");                                            }                                        });                                    } else {                                        if (isVoice.equals("1"))                                            SoundPoolUtils.soundPool(mContext, R.raw.guandeng);                                        lordingDialog.setTextMsg("正在关灯，请稍后...");                                        lordingDialog.show();                                        whatUWant = GUANDENG;                                        mqttMingLing.setAction(device_ccid, "02", new IMqttActionListener() {                                            @Override                                            public void onSuccess(IMqttToken asyncActionToken) {                                                //   UIHelper.ToastMessage(mContext, "当前装置关闭");                                                List<String> stringList = new ArrayList<>();                                                stringList.add(device_ccid);                                                stringList.add("2");//                                                Notice notice = new Notice();//                                                notice.type = ConstanceValue.MSG_SHEBEIZHUANGTAI;//                                                notice.content = stringList;//                                                Log.i("Rair", notice.content.toString());//                                                RxBus.getDefault().sendRx(notice);                                            }                                            @Override                                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {                                                UIHelper.ToastMessage(mContext, "未发送指令");                                            }                                        });                                    }                                }                            });                            mqttMingLing = new ZnjjMqttMingLing(mContext, dataBean.getDevice_ccid_up(), dataBean.getServer_id());                            nowData = "zn/app/" + dataBean.getServer_id() + dataBean.getDevice_ccid_up();                            hardwareData = "zn/hardware/" + dataBean.getServer_id() + dataBean.getDevice_ccid_up();                            AndMqtt.getInstance().subscribe(new MqttSubscribe()                                    .setTopic(nowData)                                    .setQos(2), new IMqttActionListener() {                                @Override                                public void onSuccess(IMqttToken asyncActionToken) {                                    Log.i("Rair", "订阅的地址:  " + nowData);                                    //  UIHelper.ToastMessage(mContext, "增加订阅次数");                                }                                @Override                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {                                    Log.i("CAR_NOTIFY", "(MainActivity.java:68)-onFailure:-&gt;订阅失败");                                }                            });                            mqttMingLing.subscribeShiShiXinXi(new IMqttActionListener() {                                @Override                                public void onSuccess(IMqttToken asyncActionToken) {                                    Log.i("Rair", "请求实时数据");                                }                                @Override                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {                                }                            });                        }                        if (StringUtils.isEmpty(response.body().data.get(0).is_voice)) {                            isVoice = "1";                            sbtnCaozuotishiyin.setChecked(true);                        } else {                            isVoice = response.body().data.get(0).is_voice;                            if (isVoice.equals("1")) {                                sbtnCaozuotishiyin.setChecked(true);                            } else if (isVoice.equals("2")) {                                sbtnCaozuotishiyin.setChecked(false);                            }                        }                        showLoadSuccess();                    }                    @Override                    public void onError(Response<AppResponse<ZhiiNengRoomDeviceRoomBean.DataBean>> response) {                        String str = response.getException().getMessage();                        UIHelper.ToastMessage(mContext, response.getException().getMessage());                    }                    @Override                    public void onStart(Request<AppResponse<ZhiiNengRoomDeviceRoomBean.DataBean>, ? extends Request> request) {                        super.onStart(request);                        showLoading();                    }                    @Override                    public void onFinish() {                        super.onFinish();                    }                });    }    TishiDialog tishiDialog;    /**     * 修改设备名字     */    private void changeDevice(String deviceName) {        Map<String, String> map = new HashMap<>();        map.put("code", "16033");        map.put("key", Urls.key);        map.put("token", UserManager.getManager(context).getAppToken());        map.put("family_id", dataBean.getFamily_id());        map.put("device_id", dataBean.getDevice_id());        map.put("old_name", dataBean.getDevice_name());        map.put("device_name", deviceName);        Gson gson = new Gson();        Log.e("map_data", gson.toJson(map));        OkGo.<AppResponse<ZhiNengFamilyEditBean>>post(ZHINENGJIAJU)                .tag(this)//                .upJson(gson.toJson(map))                .execute(new JsonCallback<AppResponse<ZhiNengFamilyEditBean>>() {                    @Override                    public void onSuccess(Response<AppResponse<ZhiNengFamilyEditBean>> response) {                        if (response.body().msg_code.equals("0000")) {                            tvDeviceName.setText(deviceName);                            MyCarCaoZuoDialog_Success myCarCaoZuoDialog_success = new MyCarCaoZuoDialog_Success(ZhiNengDianDengActivity.this,                                    "成功", "名字修改成功咯", "好的", new MyCarCaoZuoDialog_Success.OnDialogItemClickListener() {                                @Override                                public void clickLeft() {                                }                                @Override                                public void clickRight() {                                }                            });                            myCarCaoZuoDialog_success.show();                        }                    }                    @Override                    public void onError(Response<AppResponse<ZhiNengFamilyEditBean>> response) {                        String str = response.getException().getMessage();                        tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {                            @Override                            public void onClickCancel(View v, TishiDialog dialog) {                                tishiDialog.dismiss();                            }                            @Override                            public void onClickConfirm(View v, TishiDialog dialog) {                                finish();                            }                            @Override                            public void onDismiss(TishiDialog dialog) {                            }                        });                        tishiDialog.setTextContent(str);                        tishiDialog.show();                    }                });    }    /**     * 删除主控设备     */    private void deleteMainDevice() {        Map<String, String> map = new HashMap<>();        map.put("code", "16037");        map.put("key", Urls.key);        map.put("token", UserManager.getManager(context).getAppToken());        map.put("device_ccid", dataBean.getDevice_ccid());        Gson gson = new Gson();        Log.e("map_data", gson.toJson(map));        OkGo.<AppResponse<ZhiNengFamilyEditBean>>post(ZHINENGJIAJU)                .tag(this)//                .upJson(gson.toJson(map))                .execute(new JsonCallback<AppResponse<ZhiNengFamilyEditBean>>() {                    @Override                    public void onSuccess(Response<AppResponse<ZhiNengFamilyEditBean>> response) {                        if (response.body().msg_code.equals("0000")) {                            MyCarCaoZuoDialog_Success myCarCaoZuoDialog_success = new MyCarCaoZuoDialog_Success(ZhiNengDianDengActivity.this,                                    "成功", "设备删除成功", "好的", new MyCarCaoZuoDialog_Success.OnDialogItemClickListener() {                                @Override                                public void clickLeft() {                                }                                @Override                                public void clickRight() {                                    finish();                                }                            });                            myCarCaoZuoDialog_success.show();                        }                    }                    @Override                    public void onError(Response<AppResponse<ZhiNengFamilyEditBean>> response) {                        String str = response.getException().getMessage();                        tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {                            @Override                            public void onClickCancel(View v, TishiDialog dialog) {                                tishiDialog.dismiss();                            }                            @Override                            public void onClickConfirm(View v, TishiDialog dialog) {                                finish();                            }                            @Override                            public void onDismiss(TishiDialog dialog) {                            }                        });                        tishiDialog.setTextContent(str);                        tishiDialog.show();                    }                });    }    /**     * 删除设备     */    private void deleteDevice() {        Map<String, String> map = new HashMap<>();        map.put("code", "16034");        map.put("key", Urls.key);        map.put("token", UserManager.getManager(context).getAppToken());        map.put("family_id", dataBean.getFamily_id());        map.put("device_id", dataBean.getDevice_id());        Gson gson = new Gson();        Log.e("map_data", gson.toJson(map));        OkGo.<AppResponse<ZhiNengFamilyEditBean>>post(ZHINENGJIAJU)                .tag(this)//                .upJson(gson.toJson(map))                .execute(new JsonCallback<AppResponse<ZhiNengFamilyEditBean>>() {                    @Override                    public void onSuccess(Response<AppResponse<ZhiNengFamilyEditBean>> response) {                        if (response.body().msg_code.equals("0000")) {                            MyCarCaoZuoDialog_Success myCarCaoZuoDialog_success = new MyCarCaoZuoDialog_Success(ZhiNengDianDengActivity.this,                                    "成功", "设备删除成功", "好的", new MyCarCaoZuoDialog_Success.OnDialogItemClickListener() {                                @Override                                public void clickLeft() {                                }                                @Override                                public void clickRight() {                                    finish();                                }                            });                            myCarCaoZuoDialog_success.show();                        }                    }                    @Override                    public void onError(Response<AppResponse<ZhiNengFamilyEditBean>> response) {                        String str = response.getException().getMessage();                        tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {                            @Override                            public void onClickCancel(View v, TishiDialog dialog) {                                tishiDialog.dismiss();                            }                            @Override                            public void onClickConfirm(View v, TishiDialog dialog) {                                finish();                            }                            @Override                            public void onDismiss(TishiDialog dialog) {                            }                        });                        tishiDialog.setTextContent(str);                        tishiDialog.show();                    }                });    }    /**     * 用于其他Activty跳转到该Activity     *     * @param context     */    public static void actionStart(Context context, String device_id, String device_type) {        Intent intent = new Intent(context, ZhiNengDianDengActivity.class);        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);        intent.putExtra("device_id", device_id);        intent.putExtra("device_type", device_type);        context.startActivity(intent);    }    @Override    protected void onDestroy() {        super.onDestroy();        PreferenceHelper.getInstance(mContext).removeKey(App.CHOOSE_KONGZHI_XIANGMU);        if (!AndMqtt.getInstance().isConnect()) {            UIHelper.ToastMessage(context, "未连接主机,请重新尝试");            return;        }        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(hardwareData), new IMqttActionListener() {            @Override            public void onSuccess(IMqttToken asyncActionToken) {                Log.i("Rair", "(MainActivity.java:93)-onSuccess:-&gt;取消订阅成功");            }            @Override            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {                Log.i("Rair", "(MainActivity.java:98)-onFailure:-&gt;取消订阅失败");            }        });        AndMqtt.getInstance().unSubscribe(new MqttUnSubscribe().setTopic(nowData), new IMqttActionListener() {            @Override            public void onSuccess(IMqttToken asyncActionToken) {                Log.i("Rair", "(MainActivity.java:93)-onSuccess:-&gt;取消订阅成功");            }            @Override            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {                Log.i("Rair", "(MainActivity.java:98)-onFailure:-&gt;取消订阅失败");            }        });    }}