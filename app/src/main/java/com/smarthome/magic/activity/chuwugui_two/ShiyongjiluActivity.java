package com.smarthome.magic.activity.chuwugui_two;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui.config.ChuwuguiValue;
import com.smarthome.magic.activity.chuwugui_two.adapter.ShiyongjiluAdapter;
import com.smarthome.magic.activity.chuwugui_two.model.ShiyongjiluModel;
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

public class ShiyongjiluActivity extends BaseActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.tv_cunbaozhong)
    TextView tv_cunbaozhong;
    @BindView(R.id.line_cunbaozhong)
    View line_cunbaozhong;
    @BindView(R.id.ll_cunbaozhong)
    RelativeLayout ll_cunbaozhong;
    @BindView(R.id.tv_yijieshu)
    TextView tv_yijieshu;
    @BindView(R.id.line_yijieshu)
    View line_yijieshu;
    @BindView(R.id.ll_yijieshu)
    RelativeLayout ll_yijieshu;
    @BindView(R.id.tv_qiankuan)
    TextView tv_qiankuan;
    @BindView(R.id.line_qiankuan)
    View line_qiankuan;
    @BindView(R.id.ll_qiankuan)
    RelativeLayout ll_qiankuan;
    @BindView(R.id.rv_content)
    RecyclerView rv_content;
    private int page_number;
    private List<ShiyongjiluModel.DataBean> data = new ArrayList<>();
    private ShiyongjiluAdapter jiluAdaptger;
    private IWXAPI api;
    private String form_id;
    private ShiyongjiluModel.DataBean qubaoModel;

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_act_shiyongjilu;
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
        Intent intent = new Intent(context, ShiyongjiluActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        page_number = 0;
        initAdapter();
        select(0);
        initHuidiao();
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ChuwuguiValue.MSG_CWG_PAY_SUCCESS) {
                    getData("3");
                } else if (message.type == ChuwuguiValue.MSG_CWG_PAY_FAIL) {
                    Y.tLong("支付失败");
                }
            }
        }));
    }

    private void initAdapter() {
        jiluAdaptger = new ShiyongjiluAdapter(R.layout.chuwugui_item_shiyongliebiao, data);
        rv_content.setLayoutManager(new LinearLayoutManager(mContext));
        rv_content.setAdapter(jiluAdaptger);
        jiluAdaptger.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                qubaoModel = data.get(position);
                qubaoPay();
            }
        });

        View view = View.inflate(mContext, R.layout.chuwugui_empty_view, null);
        jiluAdaptger.setEmptyView(view);
    }

    private void qubaoPay() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("operate_type", "56");
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

    private void getData(String lc_deposit_state) {
        showProgressDialog();
        Map<String, String> map = new HashMap<>();
        map.put("code", "120005");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(mContext).getAppToken());
        map.put("subsystem_id", "tlc");
        map.put("page_number", page_number + "");
        map.put("lc_deposit_state", lc_deposit_state);
        Gson gson = new Gson();
        OkGo.<AppResponse<ShiyongjiluModel.DataBean>>post(Urls.SERVER_URL + "lc/app/inst/tlc_user")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ShiyongjiluModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<ShiyongjiluModel.DataBean>> response) {
                        data = response.body().data;
                        jiluAdaptger.setNewData(data);
                        jiluAdaptger.setLc_deposit_state(lc_deposit_state);
                        jiluAdaptger.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<AppResponse<ShiyongjiluModel.DataBean>> response) {
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

    @OnClick({R.id.rl_back, R.id.ll_cunbaozhong, R.id.ll_yijieshu, R.id.ll_qiankuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.ll_cunbaozhong:
                select(0);
                break;
            case R.id.ll_yijieshu:
                select(1);
                break;
            case R.id.ll_qiankuan:
                select(2);
                break;
        }
    }

    private void select(int pos) {
        tv_cunbaozhong.setTextColor(Y.getColor(R.color.color_9));
        tv_yijieshu.setTextColor(Y.getColor(R.color.color_9));
        tv_qiankuan.setTextColor(Y.getColor(R.color.color_9));

        line_cunbaozhong.setVisibility(View.GONE);
        line_yijieshu.setVisibility(View.GONE);
        line_qiankuan.setVisibility(View.GONE);
        String lc_deposit_state = "0";

        switch (pos) {
            case 0:
                tv_cunbaozhong.setTextColor(Color.parseColor("#4DB6FB"));
                line_cunbaozhong.setVisibility(View.VISIBLE);
                lc_deposit_state = "1";
                break;
            case 1:
                tv_yijieshu.setTextColor(Color.parseColor("#4DB6FB"));
                line_yijieshu.setVisibility(View.VISIBLE);
                lc_deposit_state = "2";
                break;
            case 2:
                tv_qiankuan.setTextColor(Color.parseColor("#4DB6FB"));
                line_qiankuan.setVisibility(View.VISIBLE);
                lc_deposit_state = "3";
                break;
        }
        getData(lc_deposit_state);
    }
}
