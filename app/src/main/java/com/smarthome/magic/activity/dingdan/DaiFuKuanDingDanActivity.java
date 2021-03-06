package com.smarthome.magic.activity.dingdan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.DingDanDetailsModel;
import com.smarthome.magic.model.OrderListModel;
import com.smarthome.magic.model.YuZhiFuModel;
import com.smarthome.magic.model.YuZhiFuModel_AliPay;
import com.smarthome.magic.pay_about.alipay.PayResult;
import com.smarthome.magic.util.PaySuccessUtils;
import com.smarthome.magic.util.Tools;
import com.smarthome.magic.util.phoneview.sample.ImageShowActivity;
import com.smarthome.magic.util.phoneview.sample.ImageShow_OnePictureActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.smarthome.magic.get_net.Urls.HOME_PICTURE_HOME;

//????????????
public class DaiFuKuanDingDanActivity extends BaseActivity {


    @BindView(R.id.tv_dingdan_zhuangtai)
    TextView tvDingdanZhuangtai;
    @BindView(R.id.conlayout_1)
    ConstraintLayout conlayout1;
    @BindView(R.id.iv_address)
    ImageView ivAddress;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.constrain2)
    ConstraintLayout constrain2;
    @BindView(R.id.view_1)
    View view1;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_shop)
    TextView tvShop;
    @BindView(R.id.constrain3)
    ConstraintLayout constrain3;
    @BindView(R.id.iv_product)
    ImageView ivProduct;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_kuanshi)
    TextView tvKuanshi;
    @BindView(R.id.tv_danjia)
    TextView tvDanjia;
    @BindView(R.id.tv_paycount)
    TextView tvPaycount;
    @BindView(R.id.constrain4)
    ConstraintLayout constrain4;
    @BindView(R.id.tv_shifujine)
    TextView tvShifujine;
    @BindView(R.id.constrain5)
    ConstraintLayout constrain5;
    @BindView(R.id.view_2)
    View view2;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.tv_go_pay)
    TextView tvGoPay;
    @BindView(R.id.tv_quxiaodingdan)
    TextView tvQuxiaodingdan;
    @BindView(R.id.cl_daifukuan)
    ConstraintLayout clDaifukuan;
    @BindView(R.id.tv_daifahuo_shenqingtuikuan)
    TextView tvDaifahuoShenqingtuikuan;
    @BindView(R.id.tv_daifahuo_cuifahuo)
    TextView tvDaifahuoCuifahuo;
    @BindView(R.id.cl_daifahuo)
    ConstraintLayout clDaifahuo;
    @BindView(R.id.tvdaipingjia)
    TextView tvdaipingjia;
    @BindView(R.id.tv_shangchudingdan)
    TextView tvShangchudingdan;
    @BindView(R.id.cl_daiingjia)
    ConstraintLayout clDaiingjia;
    @BindView(R.id.tv_yanzhengma)
    TextView tvYanzhengma;
    @BindView(R.id.iv_yanzhengma)
    ImageView ivYanzhengma;
    @BindView(R.id.cl_erweima)
    ConstraintLayout clErweima;
    @BindView(R.id.view_mengban)
    View viewMengban;
    @BindView(R.id.iv_yiwancheng)
    ImageView ivYiwancheng;
    private Context cnt;
    private OrderListModel.DataBean dataBean;
    private IWXAPI api;
    private String form_id;//??????id

    /**
     * ArrayList<String> list = new ArrayList<>();
     * list.add(response.body().data.get(0).getBannerList().get(position).getImg_url());
     * ImageShowActivity.actionStart(ZiJianShopMallDetailsActivity.this, list);
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cnt = DaiFuKuanDingDanActivity.this;

        dataBean = (OrderListModel.DataBean) getIntent().getSerializableExtra("dataBean");
        progressDialog = new ProgressDialog(cnt);
        //code	?????????(04162)	6	???
        // key	????????????	10	???
        //token	token		???
        //shop_form_id	??????id		???
        //user_pay_check	????????????		???
        //wares_go_type	???????????????2.??????3.??????		???
        //wares_type	???????????????1.??????2.?????? 3.??????		???
        clDaifukuan.setVisibility(View.GONE);
        clDaifahuo.setVisibility(View.GONE);
        if (dataBean.getUser_pay_check() != null) {
            switch (dataBean.getUser_pay_check()) {
                case "1":
                    // tv_title.setText("?????????");
                    break;
                case "2":
                    tv_title.setText("?????????");
                    break;
                case "3":
                    //  tv_title.setText("?????????");
                    clDaifahuo.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    //  tv_title.setText("?????????");
                    break;
                case "5":

                    //  tv_title.setText("????????????");
                    clErweima.setVisibility(View.VISIBLE);

                    break;
                case "6":
                    //  tv_title.setText("?????????");
                    clDaiingjia.setVisibility(View.VISIBLE);
                    break;
                case "7":
                    // tv_title.setText("??????");
                    break;
                case "8":
                    // tv_title.setText("????????????");
                    break;
                case "9":
                    //  tv_title.setText("?????????");
                    break;
                case "10":
                    //   tv_title.setText("??????/?????????");
                    break;
                case "11":
                    //   tv_title.setText("????????????");
                    break;
            }
        }


        getNet(dataBean.getShop_form_id(), dataBean.getUser_pay_check(), dataBean.getWares_go_type(), dataBean.getWares_type());
        tvGoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (dataBean.getUser_pay_check()) {

                    case "1":
//                        conlayout1.setBackgroundResource(R.mipmap.qianbaobeijing);
//                        tvDingdanZhuangtai.setText("????????????");
                        showSingSelect(dataBean);
                        break;

                    case "11":
                        //????????????
//                        tvDingdanZhuangtai.setText("????????????");
//                        conlayout1.setBackgroundResource(R.mipmap.qianbaobeijing);
//                        tvQuxiaodingdan.setVisibility(View.VISIBLE);
//                        tvQuxiaodingdan.setText("????????????");

                        showDngDanCaoZuo(dataBean, "??????????????????", "04157");
                        break;

                    case "6":

                        break;


                }

            }
        });


        tvQuxiaodingdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (dataBean.getUser_pay_check()) {

                    case "1":
//                        conlayout1.setBackgroundResource(R.mipmap.qianbaobeijing);
//                        tvDingdanZhuangtai.setText("????????????");
                        showDngDanCaoZuo(dataBean, "??????????????????", "04156");
                        break;

                    case "11":
                        //????????????
//                        tvDingdanZhuangtai.setText("????????????");
//                        conlayout1.setBackgroundResource(R.mipmap.qianbaobeijing);
//                        tvQuxiaodingdan.setVisibility(View.VISIBLE);
//                        tvQuxiaodingdan.setText("????????????");

                        showDngDanCaoZuo(dataBean, "??????????????????", "04157");
                        break;


                }

            }
        });


        //?????????
        tvDaifahuoCuifahuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNetCuiFaHuo(dataBean);
            }
        });

        tvDaifahuoShenqingtuikuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShenQingTuiKuanActivity.actionStart(DaiFuKuanDingDanActivity.this, dataBean.getShop_form_id(), dataBean.getTotal_money(), dataBean.getUser_pay_check());
            }
        });

        tvdaipingjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                AccessActivity.actionStart(DaiFuKuanDingDanActivity.this, dataBean.getIndex_photo_url(), dataBean.getShop_form_id());
            }
        });

        tvShangchudingdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDngDanCaoZuo(dataBean, "??????????????????", "04157");
            }
        });
    }
    TishiDialog tishiDialog;
    private void getNetCuiFaHuo(OrderListModel.DataBean dataBean) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04167");
        map.put("key", Urls.key);
        map.put("token", PreferenceHelper.getInstance(DaiFuKuanDingDanActivity.this).getString("app_token", "0"));
        map.put("shop_form_id", dataBean.getShop_form_id());

        // if (NetworkUtils.isNetAvailable(DaLiBaoZhiFuActivity.this)) {
        Gson gson = new Gson();
        OkGo.<AppResponse<Object>>post(HOME_PICTURE_HOME)
                .tag(DaiFuKuanDingDanActivity.this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Object>> response) {
                        UIHelper.ToastMessage(DaiFuKuanDingDanActivity.this, response.body().msg);
                    }

                    @Override
                    public void onError(Response<AppResponse<Object>> response) {
                        super.onError(response);
                        //   UIHelper.ToastMessage(getActivity(), response.body().msg);

                        String str = response.getException().getMessage();
                        tishiDialog = new TishiDialog(mContext, 3, new TishiDialog.TishiDialogListener() {
                            @Override
                            public void onClickCancel(View v, TishiDialog dialog) {
                                tishiDialog.dismiss();
                            }

                            @Override
                            public void onClickConfirm(View v, TishiDialog dialog) {

                                finish();
                            }

                            @Override
                            public void onDismiss(TishiDialog dialog) {

                            }
                        });
                        tishiDialog.setTextContent(str);
                        tishiDialog.show();

                    }

                    @Override
                    public void onCacheSuccess(Response<AppResponse<Object>> response) {
                        super.onCacheSuccess(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });

    }


    @Override
    public int getContentViewResId() {
        return R.layout.activity_dai_fu_kuan_ding_dan;
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, OrderListModel.DataBean dataBean) {
        Intent intent = new Intent(context, DaiFuKuanDingDanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("dataBean", dataBean);
        context.startActivity(intent);
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String dingdanId) {
        Intent intent = new Intent(context, DaiFuKuanDingDanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("dingdanId", dingdanId);
        context.startActivity(intent);
    }

    private int choice;

    /**
     * ?????? dialog
     */
    private void showSingSelect(OrderListModel.DataBean dataBean) {

        //?????????????????????
        final String[] items = {"??????", "?????????"};
        choice = -1;
        AlertDialog.Builder builder = new AlertDialog.Builder(DaiFuKuanDingDanActivity.this).setIcon(R.mipmap.logi_icon).setTitle("????????????")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        choice = i;
                    }
                }).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (choice == -1) {
                            Toast.makeText(DaiFuKuanDingDanActivity.this, "????????????????????????????????????", Toast.LENGTH_LONG).show();
                        } else if (items[choice].equals("??????")) {
                            progressDialog.setMessage("??????????????????????????????...");
                            progressDialog.show();
                            //??????
                            String pay_id = "2";
                            getWeiXinOrZhiFuBao(pay_id, dataBean);
                            dialogInterface.dismiss();
                        } else {
                            progressDialog.setMessage("??????????????????????????????...");
                            progressDialog.show();
                            String pay_id = "1";
                            getWeiXinOrZhiFuBao(pay_id, dataBean);
                            dialogInterface.dismiss();
                        }
                    }
                });
        builder.create().show();
    }

    ProgressDialog progressDialog;
    private String appId;//??????id ????????????

    private void getWeiXinOrZhiFuBao(String pay_id, OrderListModel.DataBean dataBean) {
        //   productDetailsForJava.get(0).shop_form_text = etLiuYan.getText().toString();
//        form_product_id 	???????????????id
//        shop_product_id	????????????id
//        pay_count	????????????
//        shop_form_text	????????????(????????????)
//         wares_go_type	???????????????2.?????? 3.??????
//
        ProductDetails productDetails = new ProductDetails();
        productDetails.shop_product_id = dataBean.getShop_product_id();
        productDetails.pay_count = dataBean.getPay_count();
        productDetails.shop_form_text = dataBean.getShop_form_text();
        productDetails.wares_go_type = dataBean.getWares_go_type();
        productDetailsForJava.add(productDetails);

        //OrderListModel.DataBean dataBean = orderListAdapter.getData().get(position);
        if (pay_id.equals("1")) {//1?????????
            Map<String, Object> map = new HashMap<>();
            map.put("key", Urls.key);
            map.put("token", UserManager.getManager(cnt).getAppToken());
            map.put("operate_type", dataBean.getOperate_type());
            map.put("pay_id", pay_id);
            map.put("pay_type", "1");
            //  map.put("users_addr_id", users_addr_id);
            //   map.put("pro", productDetailsForJava);
            // map.put("deduction_type", userHongBao);
            map.put("shop_form_id", dataBean.getShop_form_id());
            //shop_form_id
            String myHeaderLog = new Gson().toJson(map);
            String myHeaderInfo = StringEscapeUtils.unescapeJava(myHeaderLog);
            Log.i("request_log", myHeaderInfo);
            Gson gson = new Gson();
            OkGo.<AppResponse<YuZhiFuModel_AliPay.DataBean>>post(Urls.DALIBAO_PAY)
                    .tag(cnt)//
                    .upJson(myHeaderInfo)
                    .execute(new JsonCallback<AppResponse<YuZhiFuModel_AliPay.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<YuZhiFuModel_AliPay.DataBean>> response) {
                            progressDialog.dismiss();
                            appId = response.body().data.get(0).getPay();
                            form_id = response.body().data.get(0).getOut_trade_no();
                            payV2(appId);//???????????????????????????????????????
                            //progressDialog.dismiss();
                        }

                        @Override
                        public void onError(Response<AppResponse<YuZhiFuModel_AliPay.DataBean>> response) {
                            super.onError(response);
                            //progressDialog.dismiss();
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

            Map<String, Object> map = new HashMap<>();
            map.put("key", Urls.key);
            map.put("token", UserManager.getManager(cnt).getAppToken());
            map.put("operate_type", dataBean.getOperate_type());
            map.put("pay_id", pay_id);
            map.put("pay_type", "4");

            //   map.put("pay_type", payType);

            // map.put("users_addr_id", users_addr_id);
            // map.put("pro", productDetailsForJava);

            // map.put("deduction_type", userHongBao);
            map.put("shop_form_id", dataBean.getShop_form_id());

            String myHeaderLog = new Gson().toJson(map);
            String myHeaderInfo = StringEscapeUtils.unescapeJava(myHeaderLog);
            Log.i("request_log", myHeaderInfo);

            // if (NetworkUtils.isNetAvailable(DaLiBaoZhiFuActivity.this)) {
            Gson gson = new Gson();
            OkGo.<AppResponse<YuZhiFuModel.DataBean>>post(Urls.DALIBAO_PAY)
                    .tag(cnt)//
                    .upJson(myHeaderInfo)
                    .execute(new JsonCallback<AppResponse<YuZhiFuModel.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                            api = WXAPIFactory.createWXAPI(cnt, response.body().data.get(0).getPay().getAppid());
                            form_id = response.body().data.get(0).getPay().getOut_trade_no();
                            //     finish();
                            goToWeChatPay(response.body().data.get(0));
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                            super.onError(response);
                            progressDialog.dismiss();
                        }
                    });
        }

    }

    /**
     * ????????????
     *
     * @param out
     */
    private void goToWeChatPay(YuZhiFuModel.DataBean out) {
        api = WXAPIFactory.createWXAPI(cnt, out.getPay().getAppid());
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

    List<ProductDetails> productDetailsForJava = new ArrayList<>();

    private class ProductDetails {
        private String form_product_id;
        private String shop_product_id;
        private String pay_count;
        private String shop_form_text;
        private String wares_go_type;
    }

    private static final int SDK_PAY_FLAG = 1;


    /**
     * ?????????????????????
     */
    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(DaiFuKuanDingDanActivity.this);
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
                        PaySuccessUtils.getNet(cnt, form_id);
                        UIHelper.ToastMessage(cnt, "????????????", Toast.LENGTH_SHORT);
                        //  cnt.finish();

                    } else {
                        // ???????????????????????????????????????????????????????????????????????????
                        Toast.makeText(cnt, "????????????", Toast.LENGTH_SHORT).show();
                        PaySuccessUtils.getNetFail(cnt, form_id);

                    }
                    break;
                }
                default:
                    break;
            }
        }

    };


    //  private String
    public void getNet(String dingDanId, String dingDanZhuangTai, String xiaoFeiFangShi, String dingDanLeixing) {
        //code	?????????(04162)	6	???
        // key	????????????	10	???
        //token	token		???
        //shop_form_id	??????id		???
        //user_pay_check	????????????		???
        //wares_go_type	???????????????2.??????3.??????		???
        //wares_type	???????????????1.??????2.?????? 3.??????		???

        Map<String, String> map = new HashMap<>();
        map.put("code", "04162");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("shop_form_id", dingDanId);
        map.put("user_pay_check", dingDanZhuangTai);
        map.put("wares_go_type", xiaoFeiFangShi);
        map.put("wares_type", dingDanLeixing);
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<DingDanDetailsModel.DataBean>>post(HOME_PICTURE_HOME)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<DingDanDetailsModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<DingDanDetailsModel.DataBean>> response) {
                        /**
                         * msg_code	?????????
                         * msg	????????????
                         * data	????????????
                         * shop_form_id	??????id
                         * inst_id	????????????id
                         * inst_img_url	????????????
                         * inst_name	????????????
                         * inst_accid	??????accid
                         * wares_type	???????????????1.??????2.?????? 3.??????
                         * user_pay_check	????????????
                         * user_pay_check_name	??????????????????
                         * pay_check_name	????????????
                         * pay_money	?????????
                         * shop_product_id	??????????????????id
                         * index_photo_url	????????????????????????url
                         * shop_product_title	????????????
                         * product_title	????????????????????????
                         * form_product_money	????????????
                         * pay_count	????????????
                         * form_money_go	??????
                         * inst_x	????????????x????????????????????????
                         * inst_y	????????????y????????????????????????
                         * inst_addr_all	??????????????????????????????????????????
                         * pay_code	?????????????????????????????????????????????
                         * pay_code_state	??????????????????1.?????????2.?????????
                         * user_name	????????????????????????????????????
                         * user_phone	??????????????????????????????????????????
                         * user_addr_all	??????????????????????????????????????????
                         * express_id	????????????id????????????????????????
                         * express_no	????????????
                         * express_name	??????????????????
                         * express_url	????????????url
                         * order_info_arr	??????????????????
                         */
                        DingDanDetailsModel.DataBean dataBean = response.body().data.get(0);
                        DaiFuKuanDingDanActivity.this.dataBean.setUser_pay_check(dataBean.getUser_pay_check());


                        if (dataBean.getOperate_type().equals("26")) {
                            tvAddress.setText(response.body().data.get(0).getInst_addr_all());
                        } else {
                            tvAddress.setText(response.body().data.get(0).getUser_addr_all());
                        }

                        tvShop.setText(response.body().data.get(0).getInst_name());
                        Glide.with(DaiFuKuanDingDanActivity.this).load(dataBean.getInst_img_url()).into(ivImage);
                        Glide.with(DaiFuKuanDingDanActivity.this).load(dataBean.getIndex_photo_url()).into(ivProduct);//?????????
                        tvTitle.setText(dataBean.getShop_product_title());
                        tvKuanshi.setText(dataBean.getProduct_title());
                        tvDanjia.setText(dataBean.getForm_product_money());
                        //???????????????2.??????3.??????4.????????????
                        if (dingDanLeixing != null) {
                            if (dingDanLeixing.equals("1")) {
                                tvShifujine.setText("???????????" + dataBean.getPay_money() + "?????????:" + dataBean.getForm_money_go() + ")");
                            } else if (dingDanLeixing.equals("3")) {
                                tvShifujine.setText("???????????" + dataBean.getPay_money() + "(??????)");


                            } else if (dingDanLeixing.equals("4")) {
                                tvShifujine.setText("???????????" + dataBean.getPay_money());
                            }
                        }

                        if (dataBean.getOrder_info_arr() != null) {


                            for (int i = 0; i < dataBean.getOrder_info_arr().size(); i++) {
                                View view = View.inflate(DaiFuKuanDingDanActivity.this, R.layout.layout_view_info, null);
                                TextView tv = view.findViewById(R.id.tv_text);
                                tv.setText(dataBean.getOrder_info_arr().get(i));
                                llInfo.addView(view);

                            }
                        }
                        tvPaycount.setText("x" + dataBean.getPay_count());

//                        if (dataBean.getUser_pay_check().equals(""))
//                        user_pay_check
                        /**
                         * ????????????:1.????????? 2.?????????(???)3.????????? 4.????????? 5.????????????6.????????? 7.?????? 8.???????????? 9.????????? 10.??????/?????? 11????????????
                         */

                        tvQuxiaodingdan.setVisibility(View.GONE);
                        tvGoPay.setVisibility(View.GONE);
                        //conlayout1.setBackgroundResource(0);
                        switch (dataBean.getUser_pay_check()) {

                            case "1":
                                tvQuxiaodingdan.setVisibility(View.VISIBLE);
                                tvGoPay.setVisibility(View.VISIBLE);
                                tvGoPay.setText("?????????");
                                tvQuxiaodingdan.setText("????????????");

                                conlayout1.setBackgroundResource(R.mipmap.qianbaobeijing);
                                tvDingdanZhuangtai.setText("????????????");

                                if (dataBean.getOperate_type().equals("29")) {
                                    constrain2.setVisibility(View.GONE);
                                    view1.setVisibility(View.GONE);
                                }

                                break;
                            case "2":
                                tvDingdanZhuangtai.setText("??????????????????");
                                break;
                            case "3":
                                tvDingdanZhuangtai.setText("??????????????????");
                                tvQuxiaodingdan.setVisibility(View.VISIBLE);
                                tvGoPay.setVisibility(View.VISIBLE);
                                tvGoPay.setText("?????????");
                                tvQuxiaodingdan.setText("????????????");
                                conlayout1.setBackgroundResource(R.mipmap.wodedingdan_daifahuo);
                                break;
                            case "4":
                                tvDingdanZhuangtai.setText("?????????");

                                conlayout1.setBackgroundResource(R.mipmap.order_yifahuo);
                                break;
                            case "5":

                                conlayout1.setBackgroundResource(R.mipmap.daishiyong);
                                tvDingdanZhuangtai.setText("????????????");
                                break;
                            case "6":
                                constrain2.setVisibility(View.GONE);
                                conlayout1.setBackgroundResource(R.mipmap.jiaoyichenggong);
                                tvDingdanZhuangtai.setText("?????????");
                                if (dataBean.getWares_type().equals("3")) {
                                    clErweima.setVisibility(View.VISIBLE);
                                    constrain2.setVisibility(View.VISIBLE);
                                    conlayout1.setBackgroundResource(R.mipmap.yishiyong);
                                }
                                break;
                            case "7":
                                conlayout1.setBackgroundResource(R.mipmap.jiaoyichenggong);
                                tvDingdanZhuangtai.setText("???????????????");
                                break;
                            case "8":
                                tvDingdanZhuangtai.setText("???????????????");
                                break;
                            case "9":
                                tvDingdanZhuangtai.setText("?????????");
                                break;
                            case "10":
                                tvDingdanZhuangtai.setText("??????/?????????");
                                break;
                            case "11":
                                //????????????
                                constrain2.setVisibility(View.GONE);
                                tvDingdanZhuangtai.setText("????????????");
                                conlayout1.setBackgroundResource(R.mipmap.dingdan_details_x);
                                tvQuxiaodingdan.setVisibility(View.VISIBLE);
                                tvQuxiaodingdan.setText("????????????");
                                break;

                            case "99":
                                constrain2.setVisibility(View.GONE);
                                tvDingdanZhuangtai.setText("????????????");
                                conlayout1.setBackgroundResource(R.mipmap.dingdan_details_x);
                                tvQuxiaodingdan.setVisibility(View.VISIBLE);
                                tvQuxiaodingdan.setText("????????????");

                                break;
                        }


                        setDaoDian(dataBean);

                    }
                });
    }

    private void setDaoDian(DingDanDetailsModel.DataBean dataBean) {
        tvYanzhengma.setText("????????????" + dataBean.getPay_code());
        Bitmap b = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher);
        Bitmap bitmap = Tools.createQRImage(this, dataBean.getPay_code(), b);
        ivYanzhengma.setImageBitmap(bitmap);
//pay_code_state	??????????????????1.?????????2.?????????
        if (dataBean.getPay_code_state().equals("1")) {

            viewMengban.setVisibility(View.GONE);
            ivYiwancheng.setVisibility(View.GONE);

        } else if (dataBean.getPay_code_state().equals("2")) {
            viewMengban.setVisibility(View.VISIBLE);
            ivYiwancheng.setVisibility(View.VISIBLE);
        }

        ivYanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String picture = Tools.converIconToString(bitmap);
                ImageShow_OnePictureActivity.actionStart(mContext, picture);
            }
        });

    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        /**
         * user_pay_check	????????????:1.????????? 2.?????????(???)3.????????? 4.????????? 5.????????????6.????????? 7.?????? 8.???????????? 9.????????? 10.??????/??????11????????????
         */

        tv_title.setTextSize(17);
        tv_title.setText("????????????");
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

    private AlertDialog.Builder builder;

    private void showDngDanCaoZuo(OrderListModel.DataBean dataBean, String quXiaoDingDanHuaShu, String code) {

        builder = new AlertDialog.Builder(mContext).setIcon(R.mipmap.logi_icon).setTitle("????????????")
                .setMessage(quXiaoDingDanHuaShu).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Toast.makeText(getActivity(), "????????????", Toast.LENGTH_LONG).show();
                        //    getNet(dataBean.getShop_form_id(), dataBean.getUser_pay_check(), dataBean.getWares_go_type(), dataBean.getWares_type());
                        getNet_CaoZuo(dataBean.getShop_form_id(), code);
                    }
                }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    public void getNet_CaoZuo(String form_id, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("key", Urls.key);
        map.put("token", PreferenceHelper.getInstance(mContext).getString("app_token", "0"));
        map.put("shop_form_id", form_id);//??????

        Gson gson = new Gson();
        OkGo.<AppResponse<Object>>post(HOME_PICTURE_HOME)
                .tag(mContext)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Object>> response) {
                        //orderListAdapter.remove(position);
                        //  getNet(dataBean.getShop_form_id(), dataBean.getUser_pay_check(), dataBean.getWares_go_type(), dataBean.getWares_type());
                        // getNet(dataBean.getShop_form_id(), "", "", "");

                        if (code.equals("04157")) {//????????????

                            UIHelper.ToastMessage(cnt, "????????????");
                            finish();
                        } else if (code.equals("04156")) {
                            UIHelper.ToastMessage(cnt, "????????????");
                            getNet(dataBean.getShop_form_id(), "", "", "");
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<Object>> response) {
                        super.onError(response);
                    }
                });

    }


}


