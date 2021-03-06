package com.smarthome.magic.activity.wode_page.bazinew;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.DefaultX5WebView_HaveNameActivity;
import com.smarthome.magic.activity.homepage.DaLiBaoZhiFuActivity;
import com.smarthome.magic.activity.taokeshagncheng.QueRenDingDanActivity;
import com.smarthome.magic.activity.wode_page.bazinew.base.BaziBaseActivity;
import com.smarthome.magic.activity.wode_page.bazinew.utils.TimeUtils;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.YuZhiFuModel;
import com.smarthome.magic.model.YuZhiFuModel_AliPay;
import com.smarthome.magic.pay_about.alipay.PayResult;
import com.smarthome.magic.util.PaySuccessUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class BaziPayActivity extends BaziBaseActivity {


    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.iv_select_wx)
    ImageView iv_select_wx;
    @BindView(R.id.ll_select_wx)
    LinearLayout ll_select_wx;
    @BindView(R.id.iv_select_zfb)
    ImageView iv_select_zfb;
    @BindView(R.id.ll_select_zfb)
    LinearLayout ll_select_zfb;
    @BindView(R.id.bt_paipan)
    Button bt_paipan;
    private int payType;
    private String mingpan_id;
    private YuZhiFuModel.DataBean dataBean;
    private IWXAPI api;
    private String pay_id;//1????????? 2??????
    private String pay_type;//1????????? 4??????
    private String operate_id;
    private String time;
    private String form_id;
    private String appId;

    @Override
    public int getContentViewResId() {
        return R.layout.bazi_activity_pay;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("????????????");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        payType = getIntent().getIntExtra("payType", 0);
        mingpan_id = getIntent().getStringExtra("mingpan_id");

        time = getIntent().getStringExtra("time");

        if (payType == 1) {
            operate_id = "4";
            tv_type.setText("????????????????????????");
            tv_money.setText("???1???");
        } else if (payType == 101) {
            operate_id = "4";
            tv_type.setText("????????????????????????");
            tv_money.setText("???1???");
        }else if (payType == 102) {
            operate_id = "0";
            tv_type.setText("????????????????????????");
            tv_money.setText("???100???");
        } else {
            operate_id = "0";
            tv_type.setText("???????????????????????????");
            tv_money.setText("???100???");
        }

        selectWx();


        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_SAOMASUCCESS) {
                    PaySuccessUtils.getNet(BaziPayActivity.this, form_id);
                    t("????????????");
                    setResult(100);
                    finish();
                } else if (message.type == ConstanceValue.MSG_SAOMAFAILE) {
                    PaySuccessUtils.getNet(BaziPayActivity.this, form_id);
                    t("????????????");
                }
            }
        }));
    }

    @OnClick({R.id.ll_select_wx, R.id.ll_select_zfb, R.id.bt_paipan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select_wx:
                selectWx();
                break;
            case R.id.ll_select_zfb:
                selectZfb();
                break;
            case R.id.bt_paipan:
                pay();
                break;
        }
    }

    private void selectWx() {
        iv_select_wx.setImageResource(R.mipmap.zhifu_icon_select_on);
        iv_select_zfb.setImageResource(R.mipmap.zhifu_icon_select_off);
        pay_id = "2";
        pay_type = "4";
    }

    private void selectZfb() {
        iv_select_wx.setImageResource(R.mipmap.zhifu_icon_select_off);
        iv_select_zfb.setImageResource(R.mipmap.zhifu_icon_select_on);
        pay_id = "1";
        pay_type = "1";
    }

    private void pay() {
        if (pay_id.equals("1")) {
            zfbPay();
        } else {
            wxPay();
        }
    }

    private void zfbPay() {
        Map<String, String> map = new HashMap<>();
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("operate_type", "27");
        map.put("project_type", "bz");
        map.put("pay_id", pay_id);
        map.put("pay_type", pay_type);
        map.put("operate_id", operate_id);
        map.put("mingpan_id", mingpan_id);
        map.put("time", time);

        Gson gson = new Gson();
        OkGo.<AppResponse<YuZhiFuModel_AliPay.DataBean>>post(Urls.DALIBAO_PAY)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<YuZhiFuModel_AliPay.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<YuZhiFuModel_AliPay.DataBean>> response) {
                        appId = response.body().data.get(0).getPay();
                        form_id = response.body().data.get(0).getOut_trade_no();
                        payV2(appId);//???????????????????????????????????????
                    }
                });
    }

    private void wxPay() {
        Map<String, String> map = new HashMap<>();
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("operate_type", "27");
        map.put("project_type", "bz");
        map.put("pay_id", pay_id);
        map.put("pay_type", pay_type);
        map.put("operate_id", operate_id);
        map.put("mingpan_id", mingpan_id);
        map.put("time", time);

        Gson gson = new Gson();
        OkGo.<AppResponse<YuZhiFuModel.DataBean>>post(Urls.DALIBAO_PAY)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<YuZhiFuModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                        PreferenceHelper.getInstance(BaziPayActivity.this).putString(App.BAZI_PAY, App.BAZI_PAY);
                        dataBean = response.body().data.get(0);
                        form_id = response.body().data.get(0).getPay().getOut_trade_no();
                        api = WXAPIFactory.createWXAPI(BaziPayActivity.this, dataBean.getPay().getAppid());
                        api.registerApp(dataBean.getPay().getAppid());
                        PayReq req = new PayReq();
                        req.appId = dataBean.getPay().getAppid();
                        req.partnerId = dataBean.getPay().getPartnerid();
                        req.prepayId = dataBean.getPay().getPrepayid();
                        req.timeStamp = dataBean.getPay().getTimestamp();
                        req.nonceStr = dataBean.getPay().getNoncestr();
                        req.sign = dataBean.getPay().getSign();
                        req.packageValue = dataBean.getPay().getPackageX();
                        api.sendReq(req);
                    }
                });
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                     */
                    String resultInfo = payResult.getResult();// ?????????????????????????????????
                    String resultStatus = payResult.getResultStatus();
                    // ??????resultStatus ???9000?????????????????????
                    if (TextUtils.equals(resultStatus, "9000")) {
                        PaySuccessUtils.getNet(BaziPayActivity.this, form_id);
                        t("????????????");
                        setResult(100);
                        finish();
                    } else {
                        // ???????????????????????????????????????????????????????????????????????????
                        PaySuccessUtils.getNetFail(BaziPayActivity.this, form_id);
                        t("????????????");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };


    /**
     * ?????????????????????
     */
    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(BaziPayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private static final int SDK_PAY_FLAG = 1;
}
