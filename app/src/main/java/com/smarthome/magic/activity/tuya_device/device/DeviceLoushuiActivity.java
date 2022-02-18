package com.smarthome.magic.activity.tuya_device.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.tuya_device.TuyaBaseDeviceActivity;
import com.smarthome.magic.activity.tuya_device.device.adapter.TuyaDeviceLogAdapter;
import com.smarthome.magic.activity.tuya_device.device.model.DpModel;
import com.smarthome.magic.activity.tuya_device.device.tongyong.DeviceSetActivity;
import com.smarthome.magic.activity.tuya_device.utils.TuyaDialogUtils;
import com.smarthome.magic.activity.tuya_device.utils.manager.TuyaDeviceManager;
import com.smarthome.magic.activity.tuya_device.utils.manager.TuyaHomeManager;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.ITuyaDataCallback;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class DeviceLoushuiActivity extends TuyaBaseDeviceActivity {


    @BindView(R.id.iv_dianchi_state)
    ImageView iv_dianchi_state;
    @BindView(R.id.tv_dianchi_state)
    TextView tv_dianchi_state;
    @BindView(R.id.view_device_state)
    View view_device_state;
    @BindView(R.id.tv_device_state)
    TextView tv_device_state;
    @BindView(R.id.iv_menci_left)
    ImageView iv_menci_left;
    @BindView(R.id.switch_gaojing_fangchai)
    ImageView switch_gaojing_fangchai;
    @BindView(R.id.switch_gaojing_loushui)
    ImageView switch_gaojing_loushui;
    @BindView(R.id.switch_dianliang)
    ImageView switch_dianliang;
    @BindView(R.id.rv_content)
    RecyclerView rv_content;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private String ty_device_ccid;
    private String old_name;
    private String room_name;
    private ITuyaDevice mDevice;
    private DeviceBean mDeviceBeen;
    private Boolean isOnline;
    private String productId;
    private String dpsKaiguanId;

    private List<DpModel.DpsBean> dps = new ArrayList<>();
    private TuyaDeviceLogAdapter adapter;
    private int offset;

    private int dianliang;
    private boolean isFangchai;
    private boolean isShuijin;
    private boolean isDianliao;

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context, String member_type, String device_id, String ty_device_ccid, String old_name, String room_name) {
        Intent intent = new Intent(context, DeviceLoushuiActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("member_type", member_type);
        intent.putExtra("device_id", device_id);
        intent.putExtra("ty_device_ccid", ty_device_ccid);
        intent.putExtra("old_name", old_name);
        intent.putExtra("room_name", room_name);
        context.startActivity(intent);
    }

    @Override
    public int getContentViewResId() {
        return R.layout.act_device_loushui;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("水浸传感器");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        iv_rightTitle.setVisibility(View.VISIBLE);
        iv_rightTitle.setImageResource(R.mipmap.mine_shezhi);
        iv_rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set();
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
        initHuidiao();
        initAdapter();
        initSM();
        getRefresh();
    }

    private void init() {
        ty_device_ccid = getIntent().getStringExtra("ty_device_ccid");
        room_name = getIntent().getStringExtra("room_name");
        old_name = getIntent().getStringExtra("old_name");

        DeviceBean haveDevice = TuyaHomeManager.getHomeManager().isHaveDevice(ty_device_ccid);
        if (haveDevice != null) {
            TuyaDeviceManager.getDeviceManager().initDevice(haveDevice);
            initLoushui();
        } else {
            TuyaDialogUtils.t(mContext, "设备已失效!");
        }
    }

    private void initLoushui() {
        mDeviceBeen = TuyaDeviceManager.getDeviceManager().getDeviceBeen();
        mDevice = TuyaDeviceManager.getDeviceManager().getDevice();
        productId = mDeviceBeen.getProductId();
        isOnline = mDeviceBeen.getIsOnline();

        setIsOnline();
        initDeviceListener();

        initDpId();

        isFangchai = false;
        isShuijin = false;
        isDianliao = false;
    }

    private void initDpId() {
        Map<String, Object> dps = mDeviceBeen.getDps();
        dpsKaiguanId = "1";
        String s = dps.get(dpsKaiguanId).toString();
        if (s.equals("alarm")) {
            iv_menci_left.setImageResource(R.mipmap.shuijin_pic_menci_sel);
        } else {
            iv_menci_left.setImageResource(R.mipmap.shuijin_pic_menci_normol);
        }
    }

    private void initDeviceListener() {//初始化监听
        mDevice.registerDevListener(new IDevListener() {
            @Override
            public void onDpUpdate(String devId, String dpStr) {
                Y.e("Dp点数据更新啦漏水:" + devId + " | " + dpStr);
                Notice notice = new Notice();
                notice.type = ConstanceValue.MSG_DEVICE_DATA;
                notice.content = dpStr;
                notice.devId = devId;
                RxBus.getDefault().sendRx(notice);
            }

            @Override
            public void onRemoved(String devId) {
                Y.e("设备被移除了" + devId);
                Notice notice = new Notice();
                notice.type = ConstanceValue.MSG_DEVICE_REMOVED;
                notice.devId = devId;
                RxBus.getDefault().sendRx(notice);
            }

            @Override
            public void onStatusChanged(String devId, boolean online) {
                Y.e("设备上下线回调  " + devId + "   " + online);
                Notice notice = new Notice();
                notice.type = ConstanceValue.MSG_DEVICE_STATUSCHANGED;
                notice.devId = devId;
                notice.content = online;
                RxBus.getDefault().sendRx(notice);
            }

            @Override
            public void onNetworkStatusChanged(String devId, boolean status) {
                Y.e("网络状态发生变动时的回调  " + devId + "    " + status);
            }

            @Override
            public void onDevInfoUpdate(String devId) {
                Y.e("设备信息更新回调  " + devId);
            }
        });
    }

    private void setIsOnline() {
        if (isOnline) {
            view_device_state.setBackgroundResource(R.drawable.bg_zhineng_device_online);
            tv_device_state.setText("在线");
        } else {
            view_device_state.setBackgroundResource(R.drawable.bg_zhineng_device_offline);
            tv_device_state.setText("离线");
        }
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_DEVICE_DATA) {
                    if (message.devId.equals(ty_device_ccid)) {
                        getData(message.content.toString());
                    }
                } else if (message.type == ConstanceValue.MSG_DEVICE_STATUSCHANGED) {
                    if (message.devId.equals(ty_device_ccid)) {
                        isOnline = (boolean) message.content;
                    }
                } else if (message.type == ConstanceValue.MSG_DEVICE_REMOVED) {
                    if (message.devId.equals(ty_device_ccid)) {
                        TuyaDeviceManager.getDeviceManager().device_removed(DeviceLoushuiActivity.this);
                    }
                } else if (message.type == ConstanceValue.MSG_DEVICE_DELETE) {
                    String device_id = getIntent().getStringExtra("device_id");
                    if (device_id.equals(message.devId)) {
                        finish();
                    }
                }
            }
        }));
    }

    public void getData(String dpStr) {
        JSONObject jsonObject = JSON.parseObject(dpStr);
        Set<String> strings = jsonObject.keySet();
        Iterator<String> it = strings.iterator();
        while (it.hasNext()) {
            // 获得key
            String key = it.next();
            Object value = jsonObject.get(key);
            jieData(key, value);
        }
    }

    private void jieData(String key, Object value) {
        setDps(key, value);

        if (key.equals(dpsKaiguanId)) {
            DpModel.DpsBean dpsBean = new DpModel.DpsBean();
            String data = Y.getDateS(new Date());
            dpsBean.setTimeStr(data);
            dpsBean.setValue(value.toString());
            dps.add(0, dpsBean);
            offset++;
            adapter.notifyDataSetChanged();

            if (value.equals("alarm")) {
                iv_menci_left.setImageResource(R.mipmap.shuijin_pic_menci_sel);
            } else {
                iv_menci_left.setImageResource(R.mipmap.shuijin_pic_menci_normol);
            }
        }
    }

    private void setDps(String key, Object value) {
        Y.e("解析出的数据:  " + "key = " + key + "  |  value = " + value);
        Map<String, Object> dps = mDeviceBeen.getDps();
        dps.put(key, value);
        mDeviceBeen.setDps(dps);
    }

    private void initAdapter() {
        adapter = new TuyaDeviceLogAdapter(R.layout.item_tuya_device_log, dps, productId);
        rv_content.setLayoutManager(new LinearLayoutManager(mContext));
        rv_content.setAdapter(adapter);
    }

    private void initSM() {
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getRefresh();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                lordMore();
            }
        });
    }

    private void lordMore() {
        Map<String, Object> map = new HashMap<>();
        map.put("devId", ty_device_ccid);
        map.put("dpIds", dpsKaiguanId);
        map.put("offset", offset);
        map.put("limit", 10);
        TuyaHomeSdk.getRequestInstance().requestWithApiName("tuya.m.smart.operate.all.log", "1.0", map, DpModel.class, new ITuyaDataCallback<DpModel>() {
            @Override
            public void onSuccess(DpModel dpModel) {
                List<DpModel.DpsBean> dpsNext = dpModel.getDps();
                dps.addAll(dpsNext);
                offset = offset + dpsNext.size();
                adapter.setNewData(dps);
                adapter.notifyDataSetChanged();
                smartRefreshLayout.finishLoadMore();
            }

            @Override
            public void onError(String s, String s1) {
                smartRefreshLayout.finishLoadMore();
            }
        });
    }

    private void getRefresh() {
        offset = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("devId", ty_device_ccid);
        map.put("dpIds", dpsKaiguanId);
        map.put("offset", offset);
        map.put("limit", 10);
        TuyaHomeSdk.getRequestInstance().requestWithApiName("tuya.m.smart.operate.all.log", "1.0", map, DpModel.class, new ITuyaDataCallback<DpModel>() {
            @Override
            public void onSuccess(DpModel dpModel) {
                dps = dpModel.getDps();
                offset = offset + dps.size();
                adapter.setNewData(dps);
                adapter.notifyDataSetChanged();
                smartRefreshLayout.finishRefresh();
            }

            @Override
            public void onError(String s, String s1) {
                smartRefreshLayout.finishRefresh();
            }
        });
    }


    @OnClick({R.id.switch_gaojing_fangchai, R.id.switch_gaojing_loushui, R.id.switch_dianliang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.switch_gaojing_loushui:
                clickLoushui();
                break;
            case R.id.switch_gaojing_fangchai:
                clickFangchai();
                break;
            case R.id.switch_dianliang:
                clickDianliang();
                break;
        }
    }

    private void clickLoushui() {
        isShuijin = !isShuijin;
        if (isShuijin) {
            switch_gaojing_loushui.setImageResource(R.mipmap.switch_open);
        } else {
            switch_gaojing_loushui.setImageResource(R.mipmap.switch_close);
        }
    }

    private void clickFangchai() {
        isFangchai = !isFangchai;
        if (isFangchai) {
            switch_gaojing_fangchai.setImageResource(R.mipmap.switch_open);
        } else {
            switch_gaojing_fangchai.setImageResource(R.mipmap.switch_close);
        }
    }

    private void clickDianliang() {
          isDianliao = !isDianliao;
        if (isDianliao) {
            switch_dianliang.setImageResource(R.mipmap.switch_open);
        } else {
            switch_dianliang.setImageResource(R.mipmap.switch_close);
        }
    }

    private void set() {
        DeviceSetActivity.actionStart(mContext,
                getIntent().getStringExtra("member_type"),
                getIntent().getStringExtra("device_id"),
                getIntent().getStringExtra("old_name"),
                getIntent().getStringExtra("room_name")
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDeviceBeen != null) {
            TuyaDeviceManager.getDeviceManager().initDevice(mDeviceBeen);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDevice != null) {
            mDevice.unRegisterDevListener();
            mDevice.onDestroy();
        }
    }
}
