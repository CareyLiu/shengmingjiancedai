package com.smarthome.magic.activity.tuangou;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.smarthome.magic.R;
import com.smarthome.magic.adapter.TuanGouZhiFuAdapter;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.LordingDialog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.YuZhiFuModel;
import com.smarthome.magic.model.YuZhiFuModel_AliPay;
import com.smarthome.magic.model.ZhiFuTypeModel;
import com.smarthome.magic.pay_about.alipay.PayResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.get_net.Urls.HOME_PICTURE_HOME;

public class TuanGouZhiFuActivity extends BaseActivity {


    String pay_id = "2";//????????????-- 1 ????????? 2 ??????
    String payType = "4";//1 ????????? 4 ??????
    @BindView(R.id.view_weixin)
    View viewWeixin;
    @BindView(R.id.view_zhifubao)
    View viewZhifubao;
    @BindView(R.id.tv_choose_zhifufangshi)
    TextView tvChooseZhifufangshi;
    @BindView(R.id.iv_icon_1)
    ImageView ivIcon1;
    @BindView(R.id.iv_weixin_choose)
    ImageView ivWeixinChoose;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.iv_icon_2)
    ImageView ivIcon2;
    @BindView(R.id.iv_zhifubao_choose)
    ImageView ivZhifubaoChoose;
    @BindView(R.id.frtv_pay)
    RoundTextView frtvPay;
    @BindView(R.id.rlv_list)
    RecyclerView rlvList;

    private String appId;//??????id ????????????
    private IWXAPI api;

    private String war_id;
    TuanGouZhiFuAdapter tuanGouZhiFuAdapter;
    List<ZhiFuTypeModel.DataBean> list;

    LordingDialog lordingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        lordingDialog = new LordingDialog(mContext);
        id = getIntent().getStringExtra("id");
        userHongBao = getIntent().getStringExtra("userHongBao");
        number = getIntent().getStringExtra("count");
        war_id = getIntent().getStringExtra("war_id");
        list = new ArrayList<>();
        //   } else {
        //      UIHelper.ToastMessage(DaLiBaoZhiFuActivity.this, "????????????????????????");
        //  }


        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_TUANGOUPAY) {
                    // MyCarCaoZuoDialog_Success myCarCaoZuoDialog_success = new MyCarCaoZuoDialog_Success(DaLiBaoZhiFuActivity.this, "????????????", "?????????????????????");
                    // myCarCaoZuoDialog_success.show();
                    finish();
                    UIHelper.ToastMessage(TuanGouZhiFuActivity.this, "????????????");
                }
            }
        }));


        frtvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceHelper.getInstance(TuanGouZhiFuActivity.this).putString(App.TUANGOU_PAY, "tuangoupay");
                if (pay_id.equals("2")) {
                    //     finish();

                    getNet();


                } else if (pay_id.equals("1")) {
                    getNet();

                }
            }
        });

//        viewWeixin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                pay_id = "2";//????????????-- 1 ????????? 2 ??????
//                payType = "4";//1 ????????? 4 ??????
//
//                //  ivIcon1.setBackgroundResource(R.mipmap.dingdan_icon_duihao);
//                ivZhifubaoChoose.setVisibility(View.INVISIBLE);
//                ivWeixinChoose.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        viewZhifubao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                pay_id = "1";//????????????-- 1 ????????? 2 ??????
//                payType = "1";//1 ????????? 4 ??????
//
//                ivZhifubaoChoose.setVisibility(View.VISIBLE);
//                ivWeixinChoose.setVisibility(View.INVISIBLE);
//                //ivWeixinChoose.setBackgroundResource(R.mipmap.dingdan_icon_duihao);
//
//            }
//        });
        getZhiFuNet();

        tuanGouZhiFuAdapter = new TuanGouZhiFuAdapter(R.layout.item_zhifutype, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlvList.setLayoutManager(linearLayoutManager);
        rlvList.setAdapter(tuanGouZhiFuAdapter);

        tuanGouZhiFuAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.cl_main:
                        pay_id = list.get(position).pay_id;
                        payType = list.get(position).pay_type;



                        for (int i = 0; i < list.size(); i++) {
                            if (i==position){
                                list.get(position).choose = "1";
                            }else {
                                list.get(i).choose = "0";
                            }
                        }

                        tuanGouZhiFuAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });


    }


    @Override
    public int getContentViewResId() {
        return R.layout.tuangou_choose_pay;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("????????????");
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imm.hideSoftInputFromWindow(findViewById(R.id.cl_layout).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finish();
            }
        });
    }

    //???????????????

    /**
     * ?????????????????????
     */
    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(TuanGouZhiFuActivity.this);
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

    /**
     * ????????????
     *
     * @param out
     */
    private void goToWeChatPay(YuZhiFuModel.DataBean out) {
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


    private static final int SDK_PAY_FLAG = 1;
    private String orderId;

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
//                        Notice n = new Notice();
//                        n.type = ConstanceValue.MSG_DALIBAO_SUCCESS;
//                        //  n.content = message.toString();
//                        RxBus.getDefault().sendRx(n);
//                        finish();
                        // ??????????????????????????????????????????????????????????????????????????????
                        //   Toast.makeText(DaLiBaoZhiFuActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                        //   finish();
                        // ????????????????????????????????????????????????  ?????????????????? ?????????  ?????????????????????

                        // MyCarCaoZuoDialog_Success dialog_success = new MyCarCaoZuoDialog_Success(DaLiBaoZhiFuActivity.this, "????????????", "?????????????????????");
                        // dialog_success.show();
                        UIHelper.ToastMessage(TuanGouZhiFuActivity.this, "????????????", Toast.LENGTH_SHORT);
                        finish();

                    } else {
                        // ???????????????????????????????????????????????????????????????????????????
                        Toast.makeText(TuanGouZhiFuActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    //????????????

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String id, String count, String userHongBao, String war_id) {
        Intent intent = new Intent(context, TuanGouZhiFuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id", id);
        intent.putExtra("count", count);
        intent.putExtra("userHongBao", userHongBao);
        intent.putExtra("war_id", war_id);
        context.startActivity(intent);
    }

    YuZhiFuModel.DataBean dataBean;

    private String number;
    private String id;
    private String userHongBao;//1 ?????? 2??????

    private void getNet() {

        if (pay_id.equals("1")) {//1?????????

            //???????????????????????????\

            /**
             * {
             *   "key":"20180305124455yu",
             *  "token":"1234353453453456",
             *  "operate_id":"12",
             *  "operate_type":"1",
             *  "pay_id":"1"
             * }
             */

            Map<String, String> map = new HashMap<>();
            map.put("key", Urls.key);
            map.put("token", UserManager.getManager(TuanGouZhiFuActivity.this).getAppToken());
            map.put("operate_id", "1");
            map.put("operate_type", "26");
            map.put("pay_id", pay_id);
            map.put("pay_type", payType);
            map.put("shop_count", number);
            map.put("wares_id", war_id);
            map.put("deduction_type", userHongBao);
//        NetUtils<Object> netUtils = new NetUtils<>();
//        netUtils.requestData(map, Urls.DALIBAO_PAY, DaLiBaoZhiFuActivity.this, new JsonCallback<AppResponse<Object>>() {
//            @Override
//            public void onSuccess(Response<AppResponse<Object>> response) {
//
//            }
//
//            @Override
//            public void onError(Response<AppResponse<Object>> response) {
//                super.onError(response);
//                //UIHelper.ToastMessage(DaLiBaoZhiFuActivity.this, response.message());
//            }
//        });


            // if (NetworkUtils.isNetAvailable(DaLiBaoZhiFuActivity.this)) {
            Gson gson = new Gson();
            OkGo.<AppResponse<YuZhiFuModel_AliPay.DataBean>>post(Urls.DALIBAO_PAY)
                    .tag(TuanGouZhiFuActivity.this)//
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<AppResponse<YuZhiFuModel_AliPay.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<YuZhiFuModel_AliPay.DataBean>> response) {

                            appId = response.body().data.get(0).getPay();
                            payV2(appId);
                        }

                        @Override
                        public void onError(Response<AppResponse<YuZhiFuModel_AliPay.DataBean>> response) {
                            super.onError(response);
                        }
                    });

        } else {//2??????

            //???????????????????????????\

            /**
             * {
             *   "key":"20180305124455yu",
             *  "token":"1234353453453456",
             *  "operate_id":"12",
             *  "operate_type":"1",
             *  "pay_id":"1"
             * }
             */

            Map<String, String> map = new HashMap<>();
            map.put("key", Urls.key);
            map.put("token", UserManager.getManager(TuanGouZhiFuActivity.this).getAppToken());
            map.put("operate_id", "1");
            map.put("operate_type", "26");
            map.put("pay_id", pay_id);
            map.put("pay_type", payType);

            map.put("shop_count", number);
            map.put("wares_id", war_id);
            map.put("deduction_type", userHongBao);
//        NetUtils<Object> netUtils = new NetUtils<>();
//        netUtils.requestData(map, Urls.DALIBAO_PAY, DaLiBaoZhiFuActivity.this, new JsonCallback<AppResponse<Object>>() {
//            @Override
//            public void onSuccess(Response<AppResponse<Object>> response) {
//
//            }
//
//            @Override
//            public void onError(Response<AppResponse<Object>> response) {
//                super.onError(response);
//                //UIHelper.ToastMessage(DaLiBaoZhiFuActivity.this, response.message());
//            }
//        });


            // if (NetworkUtils.isNetAvailable(DaLiBaoZhiFuActivity.this)) {
            Gson gson = new Gson();
            OkGo.<AppResponse<YuZhiFuModel.DataBean>>post(Urls.DALIBAO_PAY)
                    .tag(TuanGouZhiFuActivity.this)//
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<AppResponse<YuZhiFuModel.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<YuZhiFuModel.DataBean>> response) {

                            //   appId = response.body().data.get(0).getPay().getAppid();
                            dataBean = response.body().data.get(0);
                            api = WXAPIFactory.createWXAPI(TuanGouZhiFuActivity.this, dataBean.getPay().getAppid());

                            goToWeChatPay(dataBean);
                        }

                        @Override
                        public void onError(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                            super.onError(response);
                        }
                    });

        }

    }

    public void getZhiFuNet() {
        //???????????????????????? ?????????????????????
        Map<String, String> map = new HashMap<>();
        map.put("code", "04266");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(TuanGouZhiFuActivity.this).getAppToken());
        map.put("inst_id", id);
        Gson gson = new Gson();
        OkGo.<AppResponse<ZhiFuTypeModel.DataBean>>post(HOME_PICTURE_HOME)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<ZhiFuTypeModel.DataBean>>() {
                    @Override
                    public void onStart(Request<AppResponse<ZhiFuTypeModel.DataBean>, ? extends Request> request) {
                        super.onStart(request);
                        lordingDialog.setTextMsg("???????????????????????????");
                        lordingDialog.show();
                    }

                    @Override
                    public void onSuccess(Response<AppResponse<ZhiFuTypeModel.DataBean>> response) {
                        list.clear();
                        list.addAll(response.body().data);

                        for (int i = 0; i < list.size(); i++) {
                            if (i == 0) {
                                list.get(i).choose = "1";
                            } else {
                                list.get(i).choose = "0";
                            }
                        }
                        tuanGouZhiFuAdapter.setNewData(list);

                        pay_id = list.get(0).pay_id;
                        payType = list.get(0).pay_type;
                        lordingDialog.dismiss();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        lordingDialog.dismiss();
                    }
                });
    }
}
