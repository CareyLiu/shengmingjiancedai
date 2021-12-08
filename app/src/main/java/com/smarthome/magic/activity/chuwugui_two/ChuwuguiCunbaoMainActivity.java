package com.smarthome.magic.activity.chuwugui_two;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui_two.model.CunbaoModel;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChuwuguiCunbaoMainActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.tv_title_name)
    TextView tv_title_name;
    @BindView(R.id.tv_xiangzi_name)
    RoundTextView tv_xiangzi_name;
    @BindView(R.id.tv_xiangzi_guige)
    TextView tv_xiangzi_guige;
    @BindView(R.id.tv_jifeiguize)
    TextView tv_jifeiguize;
    @BindView(R.id.tv_cunbao_time)
    TextView tv_cunbao_time;
    @BindView(R.id.tv_qubao_time)
    TextView tv_qubao_time;
    @BindView(R.id.rl_qubao_time)
    RelativeLayout rl_qubao_time;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_use_time)
    TextView tv_use_time;
    @BindView(R.id.rl_use_time)
    RelativeLayout rl_use_time;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.bt_chongxinkaixiang)
    RelativeLayout bt_chongxinkaixiang;
    @BindView(R.id.bt_finish)
    RelativeLayout bt_finish;
    private CunbaoModel.DataBean dataBeanCunbao;
    private String type;
    private String lc_use_id;

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_act_cunbaoye;
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
    public static void actionStart(Context context, String type, String lc_use_id) {
        Intent intent = new Intent(context, ChuwuguiCunbaoMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type", type);
        intent.putExtra("lc_use_id", lc_use_id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initData();
    }


    private void initData() {
        type = getIntent().getStringExtra("type");
        lc_use_id = getIntent().getStringExtra("lc_use_id");
        if (type.equals("1")) {//存包页面
            rl_use_time.setVisibility(View.INVISIBLE);
            rl_qubao_time.setVisibility(View.GONE);
            getCunbao();
        } else {
            getQubao();
        }
    }

    private void getQubao() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "120004");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("lc_use_id", lc_use_id);
        Gson gson = new Gson();
        OkGo.<AppResponse<CunbaoModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<CunbaoModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<CunbaoModel.DataBean>> response) {
                        dataBeanCunbao = response.body().data.get(0);

                        String device_name = dataBeanCunbao.getDevice_name();
                        String device_box_name = dataBeanCunbao.getDevice_box_name();
                        tv_xiangzi_name.setText(device_name + "-" + device_box_name + "号箱");
                        tv_xiangzi_guige.setText(dataBeanCunbao.getDevice_box_type_name());

                        String lc_billing_rules = dataBeanCunbao.getLc_billing_rules();
                        String deposit_unit_money = dataBeanCunbao.getDeposit_unit_money();
                        if (lc_billing_rules.equals("2")) {
                            tv_jifeiguize.setText(deposit_unit_money + "元/次");
                        } else if (lc_billing_rules.equals("3")) {
                            tv_jifeiguize.setText(deposit_unit_money + "元/小时");
                        } else {
                            tv_jifeiguize.setText("免费");
                        }

                        tv_cunbao_time.setText(dataBeanCunbao.getDeposit_begin_time());
                        tv_address.setText(dataBeanCunbao.getDeposit_addr());
                        tv_money.setText(dataBeanCunbao.getPaid_amount() + "元");
                        tv_qubao_time.setText(dataBeanCunbao.getEnd_time());
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


    private void getCunbao() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "120002");
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
                        dataBeanCunbao = response.body().data.get(0);

                        rl_use_time.setVisibility(View.INVISIBLE);
                        rl_qubao_time.setVisibility(View.GONE);

                        String device_name = dataBeanCunbao.getDevice_name();
                        String device_box_name = dataBeanCunbao.getDevice_box_name();
                        tv_xiangzi_name.setText(device_name + "-" + device_box_name + "号箱");
                        tv_xiangzi_guige.setText(dataBeanCunbao.getDevice_box_type_name());

                        String lc_billing_rules = dataBeanCunbao.getLc_billing_rules();
                        String deposit_unit_money = dataBeanCunbao.getDeposit_unit_money();
                        if (lc_billing_rules.equals("2")) {
                            tv_jifeiguize.setText(deposit_unit_money + "元/次");
                        } else if (lc_billing_rules.equals("3")) {
                            tv_jifeiguize.setText(deposit_unit_money + "元/小时");
                        } else {
                            tv_jifeiguize.setText("免费");
                        }

                        tv_cunbao_time.setText(dataBeanCunbao.getDeposit_begin_time());
                        tv_address.setText(dataBeanCunbao.getDeposit_addr());
                        tv_money.setText(dataBeanCunbao.getDeposit_pay_money() + "元");
                    }

                    @Override
                    public void onError(Response<AppResponse<CunbaoModel.DataBean>> response) {
                        super.onError(response);
                        Y.tError(response);
                    }
                });

    }

    @OnClick({R.id.rl_back, R.id.bt_chongxinkaixiang, R.id.bt_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_chongxinkaixiang:
                kaixiang();
                break;
            case R.id.bt_finish:
            case R.id.rl_back:
                finish();
                break;
        }
    }


    private void kaixiang() {
        if (type.equals("1")) {
            cunbaokaixiang();
        }else {
            qubaokaixiang();
        }
    }

    private void qubaokaixiang() {
        showProgressDialog();
        String device_box_id = dataBeanCunbao.getDevice_box_id();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120022");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("type", "2");
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

    private void cunbaokaixiang() {
        showProgressDialog();
        String device_box_id = dataBeanCunbao.getDevice_box_id();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120022");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("type", "1");
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
}
