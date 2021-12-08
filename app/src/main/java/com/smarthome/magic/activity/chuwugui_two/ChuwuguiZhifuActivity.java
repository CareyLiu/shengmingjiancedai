package com.smarthome.magic.activity.chuwugui_two;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui.config.ChuwuguiValue;
import com.smarthome.magic.activity.chuwugui_two.adapter.ZhifuxuanzeAdapter;
import com.smarthome.magic.activity.chuwugui_two.model.SaomaModl;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.YuZhiFuModel;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ChuwuguiZhifuActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.ll_cunchu)
    LinearLayout ll_cunchu;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.rv_content)
    RecyclerView rv_content;
    @BindView(R.id.tv_daizhifu)
    TextView tv_daizhifu;
    @BindView(R.id.bt_zhifu_kaixiang)
    TextView bt_zhifu_kaixiang;

    private SaomaModl.DataBean dataBean;
    private List<SaomaModl.DataBean.SubStrategyListBean> sub_strategy_list;
    private ZhifuxuanzeAdapter zhifuxuanzeAdapter;
    private SaomaModl.DataBean.SubStrategyListBean selectModel;
    private IWXAPI api;
    private String form_id;
    private String charging_method;

    @Override
    public int getContentViewResId() {
        return R.layout.chuwuguitwo_act_zhifuxuanze;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true);
        mImmersionBar.init();
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context, SaomaModl.DataBean dataBean) {
        Intent intent = new Intent(context, ChuwuguiZhifuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("model", dataBean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initData();
        initAdapter();
        initHuidiao();
    }

    private void initData() {
        dataBean = (SaomaModl.DataBean) getIntent().getSerializableExtra("model");
        tv_name.setText(dataBean.getDevice_name());
        tv_address.setText(dataBean.getDevice_addr());
        tv_time.setText(dataBean.getPre_storage_duration() + "小时");
        sub_strategy_list = dataBean.getSub_strategy_list();
        charging_method = dataBean.getCharging_method();

        if (sub_strategy_list.size() > 0) {
            bt_zhifu_kaixiang.setEnabled(true);
            selectModel = sub_strategy_list.get(0);
            selectModel.setSelect(true);
            sub_strategy_list.set(0, selectModel);
            setSelectModel();
        } else {
            bt_zhifu_kaixiang.setEnabled(false);
        }
    }

    private void setSelectModel() {
        if (charging_method.equals("1")) {
            tv_daizhifu.setText("待支付￥0.00");
        } else {
            double lcb_unit_price = Y.getDouble(selectModel.getLcb_unit_price());
            int time = Y.getInt(dataBean.getPre_storage_duration());
            double money = lcb_unit_price * time;
            tv_daizhifu.setText("待支付￥" + Y.getMoney(money));
        }
    }

    private void initAdapter() {
        zhifuxuanzeAdapter = new ZhifuxuanzeAdapter(R.layout.chuwuguitwo_item_zhifuxuanze, sub_strategy_list, charging_method);
        rv_content.setLayoutManager(new LinearLayoutManager(mContext));
        rv_content.setAdapter(zhifuxuanzeAdapter);
        zhifuxuanzeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                selectModel = sub_strategy_list.get(position);
                for (int i = 0; i < sub_strategy_list.size(); i++) {
                    SaomaModl.DataBean.SubStrategyListBean listBean = sub_strategy_list.get(i);
                    listBean.setSelect(false);
                    sub_strategy_list.set(i, listBean);
                }

                selectModel.setSelect(true);
                sub_strategy_list.set(position, selectModel);
                zhifuxuanzeAdapter.notifyDataSetChanged();
                setSelectModel();
            }
        });

    }

    @OnClick({R.id.rl_back, R.id.bt_zhifu_kaixiang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt_zhifu_kaixiang:
                pay();
                break;
        }
    }

    private void pay() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("operate_type", "54");
        if (charging_method.equals("1")) {
            map.put("pay_id", "101");
            map.put("pay_type", "101");
        } else {
            map.put("pay_id", "2");
            map.put("pay_type", "4");
        }

        map.put("ccid", dataBean.getDevice_ccid());
        map.put("billing_rules", dataBean.getCharging_method());
        map.put("device_box_type", selectModel.getLcb_specification_id());
        map.put("device_pay_type", "1");
        map.put("deposit_time", dataBean.getPre_storage_duration());

        String myHeaderLog = new Gson().toJson(map);
        String myHeaderInfo = StringEscapeUtils.unescapeJava(myHeaderLog);

        OkGo.<AppResponse<YuZhiFuModel.DataBean>>post(Urls.DALIBAO_PAY)
                .tag(this)//
                .upJson(myHeaderInfo)
                .execute(new JsonCallback<AppResponse<YuZhiFuModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                        if (charging_method.equals("1")) {
                            ChuwuguiCunbaoMainActivity.actionStart(mContext, "1", "");
                            finish();
                        } else {
                            api = WXAPIFactory.createWXAPI(mContext, response.body().data.get(0).getPay().getAppid());
                            form_id = response.body().data.get(0).getPay().getOut_trade_no();
                            goToWeChatPay(response.body().data.get(0));
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                        super.onError(response);
                        Y.tError(response);
                    }
                });
    }

    /**
     * 微信支付
     */
    private void goToWeChatPay(YuZhiFuModel.DataBean out) {
        PreferenceHelper.getInstance(mContext).putString(App.CHUWUGUI_PAY, "CHUWUGUI_PAY");
        api = WXAPIFactory.createWXAPI(mContext, out.getPay().getAppid());
        api.registerApp(out.getPay().getAppid());
        PayReq req = new PayReq();
        req.appId = out.getPay().getAppid();
        req.partnerId = out.getPay().getPartnerid();
        req.prepayId = out.getPay().getPrepayid();
        req.timeStamp = out.getPay().getTimestamp();
        req.nonceStr = out.getPay().getNoncestr();
        req.sign = out.getPay().getSign();
        req.packageValue = out.getPay().getPackageX();
        api.sendReq(req);
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ChuwuguiValue.MSG_CWG_PAY_SUCCESS) {
                    Y.tLong("支付成功");
                    ChuwuguiCunbaoMainActivity.actionStart(mContext, "1", "");
                    finish();
                } else if (message.type == ChuwuguiValue.MSG_CWG_PAY_FAIL) {
                    Y.tLong("支付失败");
                }
            }
        }));
    }
}
