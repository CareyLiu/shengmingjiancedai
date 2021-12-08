package com.smarthome.magic.activity.chuwugui_two;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui.config.ChuwuguiValue;
import com.smarthome.magic.activity.chuwugui_two.adapter.QubaoAdapter;
import com.smarthome.magic.activity.chuwugui_two.model.CunbaoModel;
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

import java.util.ArrayList;
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

public class QubaoliebiaoActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.rv_content)
    RecyclerView rv_content;
    private List<CunbaoModel.DataBean> data = new ArrayList<>();
    private QubaoAdapter qubaoAdapter;
    private IWXAPI api;
    private String form_id;
    private CunbaoModel.DataBean qubaoModel;

    @Override
    public int getContentViewResId() {
        return R.layout.chuwuguitwo_act_qubaoliebiao;
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
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, QubaoliebiaoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initAdapter();
        initHuidiao();
        showProgressDialog();
        getData();
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "120003");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        Gson gson = new Gson();
        OkGo.<AppResponse<CunbaoModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<CunbaoModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<CunbaoModel.DataBean>> response) {
                        data = response.body().data;
                        qubaoAdapter.setNewData(data);
                        qubaoAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }

    private void initAdapter() {
        qubaoAdapter = new QubaoAdapter(R.layout.chuwuguitwo_item_qubaoliebiao, data);
        rv_content.setLayoutManager(new LinearLayoutManager(mContext));
        rv_content.setAdapter(qubaoAdapter);
        qubaoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        qubaoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.bt_kaixiang:
                        kaixiang(position);
                        break;
                    case R.id.bt_finish:
                        jieshucunbao(position);
                        break;
                }
            }
        });

        View view = View.inflate(mContext, R.layout.chuwugui_empty_view, null);
        qubaoAdapter.setEmptyView(view);
    }

    private void jieshucunbao(int position) {
        showProgressDialog();
        qubaoModel = data.get(position);
        if (Y.getDouble(qubaoModel.getWait_pay_amount()) > 0) {
            qubaoPay();
        } else {
            qubao();
        }

    }

    private void qubao() {
        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120006");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("lc_use_id", qubaoModel.getLc_use_id());
        Gson gson = new Gson();
        OkGo.<AppResponse<CunbaoModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<CunbaoModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<CunbaoModel.DataBean>> response) {
                        Y.t("开箱成功");
                        ChuwuguiCunbaoMainActivity.actionStart(mContext, "2", qubaoModel.getLc_use_id());
                    }

                    @Override
                    public void onError(Response<AppResponse<CunbaoModel.DataBean>> response) {
                        super.onError(response);
                        Y.tError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }


    private void qubaoPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("operate_type", "55");
        map.put("pay_id", "2");
        map.put("pay_type", "4");
        map.put("ccid", qubaoModel.getDevice_ccid());
        map.put("billing_rules", qubaoModel.getLc_billing_rules());
        map.put("device_box_type", qubaoModel.getDevice_box_type());
        map.put("lc_use_id", qubaoModel.getLc_use_id());
        map.put("device_pay_type", "1");

        String myHeaderLog = new Gson().toJson(map);
        String myHeaderInfo = StringEscapeUtils.unescapeJava(myHeaderLog);

        OkGo.<AppResponse<YuZhiFuModel.DataBean>>post(Urls.DALIBAO_PAY)
                .tag(this)//
                .upJson(myHeaderInfo)
                .execute(new JsonCallback<AppResponse<YuZhiFuModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                        api = WXAPIFactory.createWXAPI(mContext, response.body().data.get(0).getPay().getAppid());
                        form_id = response.body().data.get(0).getPay().getOut_trade_no();
                        goToWeChatPay(response.body().data.get(0));
                    }

                    @Override
                    public void onError(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                        super.onError(response);
                        Y.tError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
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
                    ChuwuguiCunbaoMainActivity.actionStart(mContext, "2", qubaoModel.getLc_use_id());
                } else if (message.type == ChuwuguiValue.MSG_CWG_PAY_FAIL) {
                    Y.tLong("支付失败");
                }
            }
        }));
    }

    private void kaixiang(int position) {
        showProgressDialog();
        String device_box_id = data.get(position).getDevice_box_id();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120022");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("type", "3");
        map.put("device_box_id", device_box_id);
        Gson gson = new Gson();
        OkGo.<AppResponse<CunbaoModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<CunbaoModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<CunbaoModel.DataBean>> response) {
                        Y.t("开箱成功");
                    }

                    @Override
                    public void onError(Response<AppResponse<CunbaoModel.DataBean>> response) {
                        super.onError(response);
                        Y.tError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        dismissProgressDialog();
                    }
                });
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
