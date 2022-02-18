package com.smarthome.magic.activity.tuya_device.device;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.tuya_device.TuyaBaseDeviceActivity;
import com.smarthome.magic.activity.tuya_device.device.adapter.GaojingAdapter;
import com.smarthome.magic.activity.tuya_device.device.adapter.TuyaDeviceLogAdapter;
import com.smarthome.magic.activity.tuya_device.device.model.DeviceGaojing;
import com.smarthome.magic.activity.tuya_device.device.model.DpModel;
import com.smarthome.magic.activity.tuya_device.device.tongyong.DeviceSetActivity;
import com.smarthome.magic.activity.tuya_device.utils.TuyaConfig;
import com.smarthome.magic.activity.tuya_device.utils.TuyaDialogUtils;
import com.smarthome.magic.activity.tuya_device.utils.manager.TuyaDeviceManager;
import com.smarthome.magic.activity.tuya_device.utils.manager.TuyaHomeManager;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.RxBus;
import com.smarthome.magic.util.SoundPoolUtils;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class DeviceMenciActivity extends TuyaBaseDeviceActivity {

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
    @BindView(R.id.iv_menci_right)
    ImageView iv_menci_right;
    @BindView(R.id.rv_gaojing)
    RecyclerView rv_gaojing;
    @BindView(R.id.rv_content)
    RecyclerView rv_content;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.view_zhongjian)
    View view_zhongjian;

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

    private List<DeviceGaojing> gaojings = new ArrayList<>();
    private GaojingAdapter gaojingAdapter;

    private int dianliang;
    private boolean isGaojing;
    private boolean isDianling;

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context, String member_type, String device_id, String ty_device_ccid, String old_name, String room_name) {
        Intent intent = new Intent(context, DeviceMenciActivity.class);
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
        return R.layout.act_device_menci;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("门磁传感器");
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

    private void initGaojing() {
        Map<String, Object> map = new HashMap<>();
        map.put("devId", ty_device_ccid);
        TuyaHomeSdk.getRequestInstance().requestWithApiName("tuya.m.linkage.rule.product.query", "1.0", map, String.class, new ITuyaDataCallback<String>() {
            @Override
            public void onSuccess(String dpModel) {
                gaojings = JSON.parseArray(dpModel, DeviceGaojing.class);
                gaojingAdapter.setNewData(gaojings);
                gaojingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String s, String s1) {
                smartRefreshLayout.finishLoadMore();
            }
        });

    }

    private void init() {
        ty_device_ccid = getIntent().getStringExtra("ty_device_ccid");
        room_name = getIntent().getStringExtra("room_name");
        old_name = getIntent().getStringExtra("old_name");

        DeviceBean haveDevice = TuyaHomeManager.getHomeManager().isHaveDevice(ty_device_ccid);
        if (haveDevice != null) {
            TuyaDeviceManager.getDeviceManager().initDevice(haveDevice);
            initMenci();
        } else {
            TuyaDialogUtils.t(mContext, "设备已失效!");
        }
    }

    private void initMenci() {
        mDeviceBeen = TuyaDeviceManager.getDeviceManager().getDeviceBeen();
        mDevice = TuyaDeviceManager.getDeviceManager().getDevice();
        productId = mDeviceBeen.getProductId();
        isOnline = mDeviceBeen.getIsOnline();

        isGaojing = false;
        isDianling = false;


        setIsOnline();
        initDeviceListener();

        initDpId();
    }

    private void initDpId() {
        Map<String, Object> dps = mDeviceBeen.getDps();
        if (productId.equals(TuyaConfig.PRODUCTID_MENCISUO_SIG)) {
            dpsKaiguanId = "1";

            String s = dps.get(dpsKaiguanId).toString();
            if (s.equals("true")) {
                view_zhongjian.setVisibility(View.GONE);
            } else {
                view_zhongjian.setVisibility(View.VISIBLE);
            }

        } else if (productId.equals(TuyaConfig.PRODUCTID_MENCI)) {
            dpsKaiguanId = "101";
            String s = dps.get(dpsKaiguanId).toString();

            if (s.equals("true")) {
                view_zhongjian.setVisibility(View.VISIBLE);
            } else {
                view_zhongjian.setVisibility(View.GONE);
            }
        }
    }

    private void initDeviceListener() {//初始化监听
        mDevice.registerDevListener(new IDevListener() {
            @Override
            public void onDpUpdate(String devId, String dpStr) {
                Y.e("Dp点数据更新啦:" + devId + " | " + dpStr);
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
                        TuyaDeviceManager.getDeviceManager().device_removed(DeviceMenciActivity.this);
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
            String value = jsonObject.getString(key);
            jieData(key, value, jsonObject);
        }
    }

    public void jieData(String key, String value, JSONObject jsonObject) {
        Y.e("解析出的数据:  " + "key = " + key + "  |  value = " + value);
        if (key.equals(dpsKaiguanId)) {
            DpModel.DpsBean dpsBean = new DpModel.DpsBean();
            String data = Y.getDateS(new Date());
            dpsBean.setTimeStr(data);
            dpsBean.setValue(value);
            dps.add(0, dpsBean);
            offset++;
            adapter.notifyDataSetChanged();

            if (productId.equals(TuyaConfig.PRODUCTID_MENCISUO_SIG)) {
                if (value.equals("true")) {
                    view_zhongjian.setVisibility(View.GONE);
                } else {
                    view_zhongjian.setVisibility(View.VISIBLE);
                    SoundPoolUtils.soundPool(mContext, R.raw.baojingyin3);
                }
            } else if (productId.equals(TuyaConfig.PRODUCTID_MENCI)) {
                if (value.equals("true")) {
                    view_zhongjian.setVisibility(View.VISIBLE);
                    SoundPoolUtils.soundPool(mContext, R.raw.baojingyin3);
                } else {
                    view_zhongjian.setVisibility(View.GONE);
                }
            }
        } else if (key.equals("103")) {

        }
    }

    private void initAdapter() {
        adapter = new TuyaDeviceLogAdapter(R.layout.item_tuya_device_log, dps, productId);
        rv_content.setLayoutManager(new LinearLayoutManager(mContext));
        rv_content.setAdapter(adapter);

        gaojingAdapter = new GaojingAdapter(R.layout.item_tuya_gaojing, gaojings);
        rv_gaojing.setLayoutManager(new LinearLayoutManager(mContext));
        rv_gaojing.setAdapter(gaojingAdapter);
        gaojingAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                setGaojing(position);
            }
        });
    }

    private void setGaojing(int position) {
        String ruleIds = "";
        for (int i = 0; i < gaojings.size(); i++) {
            DeviceGaojing gaojing = gaojings.get(i);
            boolean enabled = gaojing.isEnabled();
            String id = gaojing.getId();
            if (i == position) {
                if (enabled) {
                    ruleIds = ruleIds + id + ",";
                }
            } else {
                if (!enabled) {
                    ruleIds = ruleIds + id + ",";
                }
            }
        }
        boolean disabled = false;
        if (!TextUtils.isEmpty(ruleIds)) {
            ruleIds = ruleIds.substring(0, ruleIds.length() - 1);
            disabled = true;
        } else {
            disabled = false;
        }

        showProgressDialog();
        Map<String, Object> map = new HashMap<>();
        map.put("devId", ty_device_ccid);
        map.put("ruleIds", ruleIds);
        map.put("disabled", disabled);
        TuyaHomeSdk.getRequestInstance().requestWithApiName("tuya.m.linkage.dev.warn.set", "1.0", map, Boolean.class, new ITuyaDataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean dpModel) {
                dismissProgressDialog();
                DeviceGaojing gaojing = gaojings.get(position);
                gaojing.setEnabled(!gaojing.isEnabled());
                gaojings.set(position, gaojing);
                gaojingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String s, String s1) {
                dismissProgressDialog();
            }
        });
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
        initGaojing();
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
