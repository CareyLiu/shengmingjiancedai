package com.smarthome.magic.fragment.znjj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rairmmd.andmqtt.AndMqtt;
import com.rairmmd.andmqtt.MqttPublish;
import com.rairmmd.andmqtt.MqttSubscribe;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.SuiYiTieOneActivity;
import com.smarthome.magic.activity.SuiYiTieThreeActivity;
import com.smarthome.magic.activity.SuiYiTieTwoActivity;
import com.smarthome.magic.activity.ZhiNengChuangLianActivity;
import com.smarthome.magic.activity.ZhiNengDianDengActivity;
import com.smarthome.magic.activity.ZhiNengJiaJuChaZuoActivity;
import com.smarthome.magic.activity.ZhiNengJiajuWeiYuAutoActivity;
import com.smarthome.magic.activity.ZhiNengJiaoHuaAutoActivity;
import com.smarthome.magic.activity.ZhiNengRoomDeviceDetailAutoActivity;
import com.smarthome.magic.activity.ZhiNengZhuJiDetailAutoActivity;
import com.smarthome.magic.activity.shengming.SmDeviceActivity;
import com.smarthome.magic.activity.shengming.shengmingmodel.CreateSession;
import com.smarthome.magic.activity.shengming.utils.UrlUtils;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.tuya_device.add.TuyaDeviceAddActivity;
import com.smarthome.magic.activity.tuya_device.camera.TuyaCameraActivity;
import com.smarthome.magic.activity.tuya_device.device.DeviceChazuoActivity;
import com.smarthome.magic.activity.tuya_device.device.DeviceWangguanActivity;
import com.smarthome.magic.activity.tuya_device.device.DeviceWgCzActivity;
import com.smarthome.magic.activity.tuya_device.utils.TuyaConfig;
import com.smarthome.magic.activity.yaokongqi.KongQiJingHuaKongZhiActivity;
import com.smarthome.magic.activity.yaokongqi.KongQiJingHuaPeiActivity;
import com.smarthome.magic.activity.yaokongqi.WanNengYaoKongQi;
import com.smarthome.magic.activity.yaokongqi.WanNengYaoKongQiPeiDuiZidingyi;
import com.smarthome.magic.activity.yaokongqi.YaokongKT;
import com.smarthome.magic.activity.yaokongqi.ZhenWanNengYaoKongQiKongZhi;
import com.smarthome.magic.activity.yaokongqi.ZhenWanNengYaoKongQiPeiDuiZidingyi;
import com.smarthome.magic.activity.zckt.AirConditionerActivity;
import com.smarthome.magic.activity.zhinengjiaju.KongQiJianCeActvity;
import com.smarthome.magic.activity.zhinengjiaju.KongQiJianCe_NewActvity;
import com.smarthome.magic.activity.zhinengjiaju.RenTiGanYingActivity;
import com.smarthome.magic.activity.zhinengjiaju.WenShiDuChuanGanQiActivity;
import com.smarthome.magic.activity.zhinengjiaju.ZhiNengJiaJuKaiGuanOneActivity;
import com.smarthome.magic.activity.zhinengjiaju.ZhiNengJiaJuKaiGuanThreeActivity;
import com.smarthome.magic.activity.zhinengjiaju.ZhiNengJiaJuKaiGuanTwoActivity;
import com.smarthome.magic.activity.zhinengjiaju.function.LouShuiActivity;
import com.smarthome.magic.activity.zhinengjiaju.function.MenCiActivity;
import com.smarthome.magic.activity.zhinengjiaju.function.MenSuoActivity;
import com.smarthome.magic.activity.zhinengjiaju.function.SosActivity;
import com.smarthome.magic.activity.zhinengjiaju.function.YanGanActivity;
import com.smarthome.magic.app.AppConfig;
import com.smarthome.magic.app.AppManager;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.basicmvp.BaseFragment;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.fragment.znjj.adapter.ZhiNengDeviceListNewAdapter;
import com.smarthome.magic.fragment.znjj.model.ZhiNengModel;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.ZhiNengFamilyEditBean;
import com.smarthome.magic.mqtt_zhiling.ZnjjMqttMingLing;
import com.smarthome.magic.tools.NetworkUtils;
import com.smarthome.magic.util.GridAverageUIDecoration;
import com.tuya.smart.api.MicroContext;
import com.tuya.smart.panelcaller.api.AbsPanelCallerService;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.get_net.Urls.ZHINENGJIAJU;


public class ZhiNengDeviceFragment extends BaseFragment {

    private View viewLayout;
    private LinearLayout ll_content_bg;
    private RecyclerView recyclerView;
    private ZhiNengDeviceListNewAdapter zhiNengDeviceListAdapter;
    private List<ZhiNengModel.DataBean.DeviceBean> mDatas = new ArrayList<>();

    private String member_type = "";
    public ZnjjMqttMingLing mqttMingLing = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewLayout = inflater.inflate(R.layout.fragment_zhineng_device, container, false);
        initView(viewLayout);
        initHuidiao();
        return viewLayout;
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_SHEBEIZHUANGTAI) {
                    List<String> messageList = (List<String>) message.content;
                    String zhuangZhiId = messageList.get(0);
                    String kaiGuanDengZhuangTai = messageList.get(1);
                    for (int i = 0; i < mDatas.size(); i++) {
                        if (mDatas.get(i).getDevice_ccid().equals(zhuangZhiId)) {
                            mDatas.get(i).setWork_state(kaiGuanDengZhuangTai);
                            /**
                             / 00 主机 01.灯 02.插座 03.喂鱼 04.浇花 05门锁 06.空调电视(开关，加风，减风，讯飞语音配置)
                             / 07.车库门  08.开关 09.晾衣架 10.窗磁 11.烟雾报警 12.门磁 13.漏水14.雷达
                             / 15.紧急开关 16.窗帘 17.电视(开关，加减音量，加减亮暗，讯飞语音配置) 18.摄像头
                             / 19.空气检测 20.温湿度检测 21.煤气管道关闭 22.自来水管道关闭 23.宠物喂食 24.宠物喂水
                             / 25.智能手环 26.排风 27背景音乐显示控制 28.电视遥控 29.空气净化 30.体质检测
                             / 31.光敏控制 32.燃气报警 33.风扇 34.雷达
                             */
                            String type = mDatas.get(i).getDevice_type();
                            if (type.equals("01") || type.equals("02")) {
                                if (zhiNengDeviceListAdapter != null) {
                                    zhiNengDeviceListAdapter.notifyItemChanged(i);
                                }
                            }


                        }
                    }


                } else if (message.type == ConstanceValue.MSG_DEVICE_DELETE_TUYA) {
                    String tuyaId = message.devId;
                    for (int i = 0; i < mDatas.size(); i++) {
                        ZhiNengModel.DataBean.DeviceBean deviceBean = mDatas.get(i);
                        String ty_device_ccid = deviceBean.getTy_device_ccid();
                        if (tuyaId.equals(ty_device_ccid)) {
                            String device_type = deviceBean.getDevice_type();
                            if (device_type.equals(TuyaConfig.CATEGORY_WNYKQ)) {
                                deleteDevice(deviceBean.getDevice_id());
                            }
                        }
                    }
                } else if (message.type == ConstanceValue.MSG_ZHINENGJIAJU_ZI_SHUAXIN) {
                    List<ZhiNengModel.DataBean> dataBean = (List<ZhiNengModel.DataBean>) message.content;
                    getData(dataBean);
                }
            }
        }));
    }

    public void getData(List<ZhiNengModel.DataBean> dataBean) {
        PreferenceHelper.getInstance(getActivity()).putString(AppConfig.FAMILY_ID, dataBean.get(0).getFamily_id());
        member_type = dataBean.get(0).getMember_type();
        if (dataBean.get(0).getDevice().size() == 0) {
            PreferenceHelper.getInstance(getActivity()).putString(AppConfig.DEVICECCID, "");
            PreferenceHelper.getInstance(getActivity()).putString(AppConfig.SERVERID, "");
        } else {
            for (int i = 0; i < dataBean.get(0).getDevice().size(); i++) {
                if (dataBean.get(0).getDevice().get(i).getDevice_type().equals("00")) {
                    if (StringUtils.isEmpty(dataBean.get(0).getDevice().get(i).getDevice_ccid())) {
                        PreferenceHelper.getInstance(getActivity()).putString(AppConfig.DEVICECCID, "");
                    } else {
                        PreferenceHelper.getInstance(getActivity()).putString(AppConfig.DEVICECCID, dataBean.get(0).getDevice().get(i).getDevice_ccid());
                    }
                    if (StringUtils.isEmpty(dataBean.get(0).getDevice().get(i).getServer_id())) {
                        PreferenceHelper.getInstance(getActivity()).putString(AppConfig.SERVERID, "");
                    } else {
                        PreferenceHelper.getInstance(getActivity()).putString(AppConfig.SERVERID, dataBean.get(0).getDevice().get(i).getServer_id());
                    }
                }
            }

        }
        mDatas.clear();
        mDatas.addAll(dataBean.get(0).getDevice());

        ZhiNengModel.DataBean.DeviceBean deviceBean = new ZhiNengModel.DataBean.DeviceBean();
        deviceBean.setDevice_name("生命体征带");
        deviceBean.setDevice_type("40");
        deviceBean.setRoom_name("默认房间");
        deviceBean.setDevice_type_pic("http://yjn-znjj.oss-cn-hangzhou.aliyuncs.com/20220208141753000001.png");
        deviceBean.setOnline_state("1");
        deviceBean.setWork_state("1");
        mDatas.add(deviceBean);

        if (zhiNengDeviceListAdapter != null) {
            zhiNengDeviceListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void initLogic() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.addItemDecoration(new GridAverageUIDecoration(14, 10));

        recyclerView.setLayoutManager(layoutManager);
        // 第一种，直接取消动画，解决闪烁问题
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        zhiNengDeviceListAdapter = new ZhiNengDeviceListNewAdapter(R.layout.item_zhineng_device, mDatas);

        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.activity_zhineng_device_none, null);
        TextView tvBangDingZhuJi = view1.findViewById(R.id.tv_bangdingzhuji);
        tvBangDingZhuJi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAddCamera();
            }
        });
        zhiNengDeviceListAdapter.setEmptyView(view1);

        zhiNengDeviceListAdapter.openLoadAnimation();//默认为渐显效果
        recyclerView.setAdapter(zhiNengDeviceListAdapter);

        zhiNengDeviceListAdapter.setNewData(mDatas);
        zhiNengDeviceListAdapter.notifyDataSetChanged();

        zhiNengDeviceListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_switch:
                        ZhiNengModel.DataBean.DeviceBean bean = (ZhiNengModel.DataBean.DeviceBean) adapter.getData().get(position);
                        if (bean.getDevice_type().equals("20")) {
                            clickTongtiao(bean, position);
                        } else {
                            mqttMingLing = new ZnjjMqttMingLing(getActivity(), bean.getDevice_ccid_up(), bean.getServer_id());

                            if (bean.getWork_state().equals("1")) {
                                mqttMingLing.setAction(bean.getDevice_ccid(), "02", new IMqttActionListener() {
                                    @Override
                                    public void onSuccess(IMqttToken asyncActionToken) {
                                        //UIHelper.ToastMessage(mContext, "当前装置开启");

                                        List<String> stringList = new ArrayList<>();
                                        stringList.add(bean.getDevice_ccid());
                                        stringList.add("2");

                                        Notice notice = new Notice();
                                        notice.type = ConstanceValue.MSG_SHEBEIZHUANGTAI;
                                        notice.content = stringList;
                                        Log.i("Rair", notice.content.toString());
                                        RxBus.getDefault().sendRx(notice);
                                    }

                                    @Override
                                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                        UIHelper.ToastMessage(getActivity(), "未发送指令");
                                    }
                                });

                            }

                            if (bean.getWork_state().equals("2")) {

                                mqttMingLing.setAction(bean.getDevice_ccid(), "01", new IMqttActionListener() {
                                    @Override
                                    public void onSuccess(IMqttToken asyncActionToken) {
                                        List<String> stringList = new ArrayList<>();
                                        stringList.add(bean.getDevice_ccid());
                                        stringList.add("1");

                                        Notice notice = new Notice();
                                        notice.type = ConstanceValue.MSG_SHEBEIZHUANGTAI;
                                        notice.content = stringList;
                                        Log.i("Rair", notice.content.toString());
                                        RxBus.getDefault().sendRx(notice);
                                    }

                                    @Override
                                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                        UIHelper.ToastMessage(getActivity(), "未发送指令");
                                    }
                                });
                            }
                            break;
                        }
                    case R.id.ll_content:
                        ZhiNengModel.DataBean.DeviceBean deviceBean = (ZhiNengModel.DataBean.DeviceBean) adapter.getItem(position);
                        /**
                         / 00 主机 01.灯 02.插座 03.喂鱼 04.浇花 05门锁 06.空调电视(开关，加风，减风，讯飞语音配置)
                         / 07.车库门  08.开关 09.晾衣架 10.窗磁 11.烟雾报警 12.门磁 13.漏水14.雷达
                         / 15.紧急开关 16.窗帘 17.电视(开关，加减音量，加减亮暗，讯飞语音配置) 18.摄像头
                         / 19.空气检测 20.温湿度检测 21.煤气管道关闭 22.自来水管道关闭 23.宠物喂食 24.宠物喂水
                         / 25.智能手环 26.排风 27背景音乐显示控制 28.电视遥控 29.空气净化 30.体质检测
                         / 31.光敏控制 32.燃气报警 33.风扇 34.雷达
                         */
                        if (deviceBean.getDevice_type().equals("20")) {//空调
                            String ccid = deviceBean.getDevice_ccid();
                            PreferenceHelper.getInstance(getContext()).putString("ccid", ccid);
                            PreferenceHelper.getInstance(getContext()).putString("share_type", "1");
                            PreferenceHelper.getInstance(getContext()).putString("sim_ccid_save_type", "1");
                            if (NetworkUtils.isConnected(getActivity())) {
                                Activity currentActivity = AppManager.getAppManager().currentActivity();
                                if (currentActivity != null) {
                                    AirConditionerActivity.actionStart(getActivity(), ccid, "智能空调");
                                }
                            } else {
                                UIHelper.ToastMessage(getActivity(), "请连接网络后重新尝试");
                            }
                        } else if (deviceBean.getDevice_type().equals("16")) {//窗帘
                            ZhiNengChuangLianActivity.actionStart(getActivity(), deviceBean.getDevice_id(), "", member_type);
                        } else if (deviceBean.getDevice_type().equals("01")) {//灯
                            ZhiNengDianDengActivity.actionStart(getActivity(), deviceBean.getDevice_id(), deviceBean.getDevice_type(), member_type);
                        } else if (deviceBean.getDevice_type().equals("03")) {//喂鱼
                            ZhiNengJiajuWeiYuAutoActivity.actionStart(getActivity(), deviceBean.getDevice_id(), deviceBean.getDevice_type(), member_type);
                        } else if (deviceBean.getDevice_type().equals("04")) {//浇花
                            ZhiNengJiaoHuaAutoActivity.actionStart(getActivity(), deviceBean.getDevice_id(), deviceBean.getDevice_type(), member_type);
                        } else if (deviceBean.getDevice_type().equals("02")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("device_id", deviceBean.getDevice_id());
                            bundle.putString("device_type", deviceBean.getDevice_type());
                            bundle.putString("member_type", member_type);
                            bundle.putString("work_state", deviceBean.getWork_state());
                            startActivity(new Intent(getActivity(), ZhiNengJiaJuChaZuoActivity.class).putExtras(bundle));
                        } else if (deviceBean.getDevice_type().equals("12")) {
                            MenCiActivity.actionStart(getActivity(), deviceBean.getDevice_id(), member_type);
                        } else if (deviceBean.getDevice_type().equals("11")) {
                            YanGanActivity.actionStart(getActivity(), deviceBean.getDevice_id());
                        } else if (deviceBean.getDevice_type().equals("15")) {
                            SosActivity.actionStart(getActivity(), deviceBean.getDevice_id(), member_type);
                        } else if (deviceBean.getDevice_type().equals("05")) {
                            MenSuoActivity.actionStart(getActivity(), deviceBean.getDevice_id(), member_type);
                        } else if (deviceBean.getDevice_type().equals("13")) {
                            LouShuiActivity.actionStart(getActivity(), deviceBean.getDevice_id(), member_type);
                        } else if (deviceBean.getDevice_type().equals("28")) {
                            WanNengYaoKongQi.actionStart(getActivity(), deviceBean.getDevice_id(), member_type);
                        } else if (deviceBean.getDevice_type().equals("37")) {
                            YaokongKT.actionStart(getActivity(), deviceBean.getDevice_id(), member_type);
                        } else if (deviceBean.getDevice_type().equals("08")) {//随意贴
                            //SuiYiTieActivity.actionStart(getActivity(), deviceBean.getDevice_ccid(), deviceBean.getDevice_ccid_up());
                            String strJiLian = deviceBean.getDevice_ccid().substring(2, 4);
                            if (strJiLian.equals("01")) {
                                SuiYiTieOneActivity.actionStart(getActivity(), deviceBean.getDevice_ccid(), deviceBean.getDevice_ccid_up());
                            } else if (strJiLian.equals("02")) {
                                SuiYiTieTwoActivity.actionStart(getActivity(), deviceBean.getDevice_ccid(), deviceBean.getDevice_ccid_up());
                            } else if (strJiLian.equals("03")) {
                                SuiYiTieThreeActivity.actionStart(getActivity(), deviceBean.getDevice_ccid(), deviceBean.getDevice_ccid_up());
                            }
                        } else if (deviceBean.getDevice_type().equals("36")) {

                            WenShiDuChuanGanQiActivity.actionStart(getActivity(), deviceBean.getDevice_id(), deviceBean.getDevice_ccid(), deviceBean.getDevice_ccid_up());

                        } else if (deviceBean.getDevice_type().equals("34")) {
                            RenTiGanYingActivity.actionStart(getActivity(), deviceBean.getDevice_id(), member_type);
                        } else if (deviceBean.getDevice_type().equals("35")) {
                            String strJiLian = deviceBean.getDevice_ccid().substring(2, 4);
                            String serverId = deviceBean.getDevice_ccid_up().substring(deviceBean.getDevice_ccid_up().length() - 1) + "/";
                            if (strJiLian.equals("01")) {
                                ZhiNengJiaJuKaiGuanOneActivity.actionStart(getActivity(), deviceBean.getDevice_ccid(), deviceBean.getDevice_ccid_up(), serverId, member_type);
                            } else if (strJiLian.equals("02")) {
                                ZhiNengJiaJuKaiGuanTwoActivity.actionStart(getActivity(), deviceBean.getDevice_ccid(), deviceBean.getDevice_ccid_up(), serverId, member_type);
                            } else if (strJiLian.equals("03")) {
                                ZhiNengJiaJuKaiGuanThreeActivity.actionStart(getActivity(), deviceBean.getDevice_ccid(), deviceBean.getDevice_ccid_up(), serverId, member_type);
                            }
                        } else if (deviceBean.getDevice_type().equals("00")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("device_id", deviceBean.getDevice_id());
                            bundle.putString("device_type", deviceBean.getDevice_type());
                            bundle.putString("member_type", member_type);
                            bundle.putString("work_state", deviceBean.getWork_state());
                            startActivity(new Intent(getActivity(), ZhiNengZhuJiDetailAutoActivity.class).putExtras(bundle));
                        } else if (deviceBean.getDevice_type().equals("19")) {
                            //空气检测
                            KongQiJianCe_NewActvity.actionStart(getActivity(), deviceBean.getDevice_id());
                        } else if (deviceBean.getDevice_type().equals("38")) {//空气净化遥控器
                            KongQiJingHuaKongZhiActivity.actionStart(getActivity(), deviceBean.getDevice_id(), member_type);
                        } else if (deviceBean.getDevice_type().equals("39")) {//万能遥控器
                            ZhenWanNengYaoKongQiKongZhi.actionStart(getActivity(), deviceBean.getDevice_id(), member_type);
                        } else if (deviceBean.getDevice_type().equals("40")) {//生命体征检测带
                            enterShengming();
                        } else {
                            String ty_device_ccid = deviceBean.getTy_device_ccid();
                            if (TextUtils.isEmpty(ty_device_ccid)) {
                                Bundle bundle = new Bundle();
                                bundle.putString("device_id", deviceBean.getDevice_id());
                                bundle.putString("device_type", deviceBean.getDevice_type());
                                bundle.putString("member_type", member_type);
                                bundle.putString("work_state", deviceBean.getWork_state());
                                startActivity(new Intent(getActivity(), ZhiNengRoomDeviceDetailAutoActivity.class).putExtras(bundle));
                            } else {
                                Y.e("设备的信息是什么啊  " + "device_category:" + deviceBean.getDevice_type() + "  produco:" + deviceBean.getDevice_category() + "  device_category_code:" + deviceBean.getDevice_category_code());
                                if (deviceBean.getDevice_type().equals(TuyaConfig.CATEGORY_CAMERA)) {//涂鸦摄像机
                                    TuyaCameraActivity.actionStart(getActivity(), member_type, deviceBean.getDevice_id(), ty_device_ccid, deviceBean.getDevice_name(), deviceBean.getRoom_name());
                                } else if (deviceBean.getDevice_type().equals(TuyaConfig.CATEGORY_CHAZUO)) {//涂鸦插座
                                    if (deviceBean.getDevice_category().equals(TuyaConfig.PRODUCTID_CHAZUO_WG)) {
                                        DeviceWgCzActivity.actionStart(getActivity(), member_type, deviceBean.getDevice_id(), ty_device_ccid, deviceBean.getDevice_name(), deviceBean.getRoom_name());
                                    } else {
                                        DeviceChazuoActivity.actionStart(getActivity(), member_type, deviceBean.getDevice_id(), ty_device_ccid, deviceBean.getDevice_name(), deviceBean.getRoom_name());
                                    }
                                } else if (deviceBean.getDevice_type().equals(TuyaConfig.CATEGORY_WANGGUAN)) {//涂鸦网关
                                    DeviceWangguanActivity.actionStart(getActivity(), member_type, deviceBean.getDevice_id(), ty_device_ccid, deviceBean.getDevice_name(), deviceBean.getRoom_name());
                                } else if (deviceBean.getDevice_type().equals(TuyaConfig.CATEGORY_WNYKQ)) {//万能遥控器
                                    AbsPanelCallerService service = MicroContext.getServiceManager().findServiceByInterface(AbsPanelCallerService.class.getName());
                                    service.goPanelWithCheckAndTip(getActivity(), ty_device_ccid);
                                } else {//其他涂鸦设备
                                    AbsPanelCallerService service = MicroContext.getServiceManager().findServiceByInterface(AbsPanelCallerService.class.getName());
                                    service.goPanelWithCheckAndTip(getActivity(), ty_device_ccid);
                                }
                            }
                        }
                        break;
                }
            }
        });
    }

    private void enterShengming() {
        showProgressDialog();
        String timestamp = System.currentTimeMillis() + "";
        String ltoken = UrlUtils.getMainLtoken(timestamp);
        OkGo.<CreateSession>get(UrlUtils.createSession)
                .params("customerCode", UrlUtils.CUSTOMERCODE)
                .params("timestamp", timestamp)
                .params("ltoken", ltoken)
                .tag(this)//
                .execute(new JsonCallback<CreateSession>() {
                    @Override
                    public void onSuccess(Response<CreateSession> response) {
                        String sessionId = response.body().getData();
                        PreferenceHelper.getInstance(getContext()).putString("sm_sessionId", sessionId);
                        SmDeviceActivity.actionStart(getActivity());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }

    private TishiDialog tishiDialog;

    private void clickAddCamera() {
        // TODO: 2021/2/19 判断是不是管理员
        String str = PreferenceHelper.getInstance(getActivity()).getString(AppConfig.ZHINENGJIAJUGUANLIYUAN, "0");
        if (str.equals("0")) {
            tishiDialog = new TishiDialog(getActivity(), 3, new TishiDialog.TishiDialogListener() {
                @Override
                public void onClickCancel(View v, TishiDialog dialog) {

                }

                @Override
                public void onClickConfirm(View v, TishiDialog dialog) {
                    tishiDialog.dismiss();

                }

                @Override
                public void onDismiss(TishiDialog dialog) {

                }
            });
            tishiDialog.setTextContent("非管理员无法添加主机及设备");
            tishiDialog.show();
        } else if (str.equals("1")) {
            PreferenceHelper.getInstance(getActivity()).putString(AppConfig.MC_DEVICE_CCID, "aaaaaaaaaaaaaaaa80140018");
            TuyaDeviceAddActivity.actionStart(getContext());
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_zhineng_device;
    }

    public void initView(View view) {
        ll_content_bg = view.findViewById(R.id.ll_content_bg);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void clickTongtiao(ZhiNengModel.DataBean.DeviceBean bean, int pos) {
        String KT_ccid = bean.getDevice_ccid();
        String KT_Send = "zckt/cbox/hardware/" + KT_ccid;
        //注册向空调发送数据的主题
        AndMqtt.getInstance().subscribe(new MqttSubscribe()
                .setTopic(KT_Send)
                .setQos(2), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });

        String work_state = bean.getWork_state();
        if (work_state.equals("1")) {
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M030.")
                    .setQos(2).setRetained(false)
                    .setTopic(KT_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Y.t("关闭空调");
                    bean.setWork_state("2");
                    mDatas.set(pos, bean);
                    zhiNengDeviceListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } else {
            AndMqtt.getInstance().publish(new MqttPublish()
                    .setMsg("M031.")
                    .setQos(2).setRetained(false)
                    .setTopic(KT_Send), new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Y.t("开启空调");
                    bean.setWork_state("1");
                    mDatas.set(pos, bean);
                    zhiNengDeviceListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        }
    }

    /**
     * 删除设备
     */
    private void deleteDevice(String device_id) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "16034");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(getContext()).getAppToken());
        map.put("family_id", PreferenceHelper.getInstance(getContext()).getString(AppConfig.PEIWANG_FAMILYID, ""));
        map.put("device_id", device_id);
        Gson gson = new Gson();
        OkGo.<AppResponse<ZhiNengFamilyEditBean>>post(ZHINENGJIAJU)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiNengFamilyEditBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ZhiNengFamilyEditBean>> response) {
                        Notice notice = new Notice();
                        notice.type = ConstanceValue.MSG_DEVICE_DELETE;
                        notice.devId = device_id;
                        RxBus.getDefault().sendRx(notice);
                    }

                    @Override
                    public void onError(Response<AppResponse<ZhiNengFamilyEditBean>> response) {

                    }
                });
    }
}
