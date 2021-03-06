package com.smarthome.magic.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.DefaultX5WebViewActivity;
import com.smarthome.magic.activity.dingdan.AccessActivity;
import com.smarthome.magic.activity.dingdan.DaiFuKuanDingDanActivity;
import com.smarthome.magic.activity.dingdan.DingDanShenQingTuikuanActivity;
import com.smarthome.magic.activity.dingdan.OrderTuiKuanDetailsActivity;
import com.smarthome.magic.activity.dingdan.ShenQingTuiKuanActivity;
import com.smarthome.magic.activity.dingdan.TuanGouDingDanDetails;
import com.smarthome.magic.activity.zijian_shangcheng.ZiJianShopMallDetailsActivity;
import com.smarthome.magic.adapter.dingdan.OrderListAdapter;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.basicmvp.BaseFragment;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;

import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.dialog.newdia.TishiDialog;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.AddressModel;
import com.smarthome.magic.model.DataIn;
import com.smarthome.magic.model.OrderListModel;
import com.smarthome.magic.model.SmartDevice_car_0364;
import com.smarthome.magic.model.YuZhiFuModel;
import com.smarthome.magic.model.YuZhiFuModel_AliPay;
import com.smarthome.magic.pay_about.alipay.PayResult;
import com.smarthome.magic.util.PaySuccessUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.smarthome.magic.app.App.DINGDANZHIFU;
import static com.smarthome.magic.app.ConstanceValue.MSG_DINGDAN_PAY;

public class OrderListFragment extends BaseFragment {

    private RecyclerView recyclerView;
    List<OrderListModel.DataBean> mDatas = new ArrayList<>();
    private OrderListAdapter orderListAdapter;
    OrderListModel.DataBean dataBean = new OrderListModel.DataBean();
    String user_pay_check = "0";
    String str;
    Response<AppResponse<OrderListModel.DataBean>> response;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        progressDialog = new ProgressDialog(getActivity());
        str = bundle.getString("title");

        /**
         *         tagList.add("??????");
         *         tagList.add("?????????");
         *         tagList.add("?????????");
         *         tagList.add("?????????");
         *         tagList.add("?????????");
         *         tagList.add("??????");
         *         tagList.add("?????????");
         *         tagList.add("?????????");
         *         tagList.add("??????/??????");
         *         tagList.add("????????????");
         *
         *         ???????????????0.??????1.????????? 3.?????????
         * 4.?????????5.????????????6.?????????7.????????? 8.9.10.??????/??????
         */
        if (str.equals("??????")) {
            user_pay_check = "0";
        } else if (str.equals("?????????")) {
            user_pay_check = "1";
        } else if (str.equals("?????????")) {
            user_pay_check = "3";
        } else if (str.equals("?????????")) {
            user_pay_check = "4";
        } else if (str.equals("??????")) {
            user_pay_check = "5";
        } else if (str.equals("?????????")) {
            user_pay_check = "6";
        } else if (str.equals("?????????")) {
            user_pay_check = "2";
        } else if (str.equals("??????/??????")) {
            user_pay_check = "10";
        } else if (str.equals("????????????")) {
            user_pay_check = "11";
        } else if (str.equals("?????????")) {
            user_pay_check = "7";
        }

        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == MSG_DINGDAN_PAY) {
                    pageNumber = 0;
                    getNet();//??????????????????????????????
                }
            }
        }));
    }

    public int pageNumber = 0;

    public void getNet() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04161");
        map.put("key", Urls.key);
        map.put("token", PreferenceHelper.getInstance(getActivity()).getString("app_token", "0"));
        map.put("user_pay_check", user_pay_check);//??????
        map.put("page_number", String.valueOf(pageNumber));

        // if (NetworkUtils.isNetAvailable(DaLiBaoZhiFuActivity.this)) {
        Gson gson = new Gson();
        OkGo.<AppResponse<OrderListModel.DataBean>>post(Urls.HOME_PICTURE_HOME)
                .tag(getActivity())//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<OrderListModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<OrderListModel.DataBean>> response) {
                        smartRefreshLayout.finishLoadMore();
                        smartRefreshLayout.finishRefresh();
                        OrderListFragment.this.response = response;
                        if (pageNumber == 0) {
                            mDatas.clear();
                            mDatas.addAll(response.body().data);
                            orderListAdapter.setNewData(mDatas);
                            orderListAdapter.notifyDataSetChanged();
                        } else {
                            mDatas.addAll(response.body().data);
                            orderListAdapter.notifyDataSetChanged();
                        }

                        if (mDatas.size() == 0) {
                            View view = View.inflate(getActivity(), R.layout.layout_orderllist_empty, null);
                            orderListAdapter.setEmptyView(view);
                            orderListAdapter.notifyDataSetChanged();
                        }

                        if (response.body().next.equals("0")) {//????????????
                            smartRefreshLayout.setEnableLoadMore(false);
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<OrderListModel.DataBean>> response) {
                        super.onError(response);
                    }
                });

    }

    String strTitle;
    ImageView ivNone;

    @Override
    protected void initLogic() {
        Bundle args = getArguments();
        strTitle = args.getString("title");
        getNet();
    }

    RelativeLayout rlMain;
    View view;

    @Override
    protected void immersionInit(ImmersionBar mImmersionBar) {
        mImmersionBar
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected boolean immersionEnabled() {
        return true;
    }

    private SmartRefreshLayout smartRefreshLayout;

    @Override
    protected int getLayoutRes() {
        return R.layout.order_list;
    }

    @Override
    protected void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        rlMain = rootView.findViewById(R.id.rl_main);
        ivNone = rootView.findViewById(R.id.iv_none);
        view = rootView.findViewById(R.id.view);
        smartRefreshLayout = rootView.findViewById(R.id.srL_smart);
        initSmartRefresh();
        initAdapter();
    }

    public void initSmartRefresh() {
        smartRefreshLayout.setEnableAutoLoadMore(true);
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++pageNumber;
                getNet();

            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNumber = 0;
                getNet();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderListAdapter = new OrderListAdapter(R.layout.item_order_list, mDatas);
        orderListAdapter.openLoadAnimation();//?????????????????????
        recyclerView.setAdapter(orderListAdapter);

        orderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderListModel.DataBean dataBean = orderListAdapter.getData().get(position);
                switch (view.getId()) {

                    case R.id.constrain:
                        /**
                         * ???????????????1.??????2.?????? 3.???????????? 4.????????????????????????
                         */
                        if (dataBean.getWares_type().equals("1")) {

                            if (dataBean.getUser_pay_check().equals("8") || dataBean.getUser_pay_check().equals("9") || dataBean.getUser_pay_check().equals("10")) {

                                OrderTuiKuanDetailsActivity.actionStart(getActivity(), dataBean.getShop_form_id());

                            } else {
                                DaiFuKuanDingDanActivity.actionStart(getActivity(), dataBean);
                            }

                        } else if (dataBean.getWares_type().equals("3")) {
                            if (dataBean.getUser_pay_check().equals("8") || dataBean.getUser_pay_check().equals("9") || dataBean.getUser_pay_check().equals("10")) {

                                OrderTuiKuanDetailsActivity.actionStart(getActivity(), dataBean.getShop_form_id());

                            } else if (dataBean.getUser_pay_check().equals("11") || dataBean.getUser_pay_check().equals("7")) {
                                DaiFuKuanDingDanActivity.actionStart(getActivity(), dataBean);

                            } else {

                                DaiFuKuanDingDanActivity.actionStart(getActivity(), dataBean);
                            }

                        }

                        break;
                    case R.id.tv_caozuo:
                        doCaoZuo(dataBean, position);
                        break;
                    case R.id.tv_caozuo1:
                        doCaoZuo1(dataBean, position);
                        break;
                    case R.id.tv_caozuo2:
                        doCaoZuo2(dataBean, position);
                        break;
                }
            }
        });

    }

    /**
     * user_pay_check	????????????:1.????????? 2.?????????(???)3.????????? 4.????????? 5.????????????6.????????? 7.?????? 8.???????????? 9.????????? 10.??????/?????? 11 ????????????
     */

    public void doCaoZuo(OrderListModel.DataBean dataBean, int position) {
        /**
         * user_pay_check	????????????:1.????????? 2.?????????(???)3.????????? 4.????????? 5.????????????6.????????? 7.?????? 8.???????????? 9.????????? 10.??????/?????? 11 ????????????
         */
        switch (dataBean.getUser_pay_check()) {
            case "1":
                // helper.setText(R.id.tv_caozuo, "?????????");

//                if (dataBean.getUser_pay_check())
                showSingSelect(dataBean);

                break;
            case "2":
                // helper.setText(R.id.tv_caozuo, "????????????");
                break;
            case "3":
                ShenQingTuiKuanActivity.actionStart(getActivity(), dataBean.getShop_form_id(), dataBean.getTotal_money(), dataBean.getUser_pay_check());
                break;
            case "4":
                // helper.setText(R.id.tv_caozuo, "????????????");
                // wares_go_type	???????????????2.??????3.??????
                if (dataBean.getWares_go_type().equals("2")) {
                    showDngDanCaoZuo(dataBean, position, "??????????????????", "04164");
                } else if (dataBean.getWares_go_type().equals("3")) {
                    showDngDanCaoZuo(dataBean, position, "?????????????????????", "04164");
                }

                break;
            case "5":
                //helper.setText(R.id.tv_caozuo, "????????????");
                UIHelper.ToastMessage(getActivity(), "????????????");

                Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
                String targetId = response.body().data.get(0).getInst_accid();
                String instName = response.body().data.get(0).getInst_name();
                Bundle bundle = new Bundle();
                bundle.putString("dianpuming", instName);
                bundle.putString("inst_accid", response.body().data.get(0).getInst_accid());
                bundle.putString("shoptype","2");
                RongIM.getInstance().startConversation(getActivity(), conversationType, targetId, instName, bundle);

                break;
            case "6":
                showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");

                // helper.setText(R.id.tv_caozuo, "????????????");
                // helper.setVisible(R.id.tv_caozuo, false);
                break;
            case "7":

                // helper.setText(R.id.tv_caozuo, "????????????");

                break;
            case "8":
                // helper.setText(R.id.tv_yipingjia, "????????????");

                //  helper.setText(R.id.tv_caozuo, "????????????");

                break;
            case "9":
                //  helper.setText(R.id.tv_yipingjia, "?????????");
                //  helper.setText(R.id.tv_caozuo, "????????????");

                break;
            case "10":
                //  helper.setText(R.id.tv_yipingjia, "??????/??????");
                //   helper.setText(R.id.tv_caozuo, "????????????");
                showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");

                break;
            case "11":
                //  helper.setText(R.id.tv_yipingjia, "????????????");
                // helper.setText(R.id.tv_caozuo, "????????????");
                // showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");

                break;

        }

    }

    private void getNetQueRenShouHuo() {

    }

    public void doCaoZuo1(OrderListModel.DataBean dataBean, int position) {
        /**
         * user_pay_check	????????????:1.????????? 2.?????????(???)3.????????? 4.????????? 5.????????????6.????????? 7.?????? 8.???????????? 9.????????? 10.??????/?????? 11 ????????????
         */
        switch (dataBean.getUser_pay_check()) {
            case "1":

                // helper.setText(R.id.tv_caozuo1, "????????????");
                //getNet_QUXIAO(dataBean.getShop_form_id());
                showDngDanCaoZuo(dataBean, position, "??????????????????", "04156");

                break;
            case "2":

                //helper.setText(R.id.tv_caozuo1, "????????????");
                showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");

                break;
            case "3":

                //helper.setText(R.id.tv_caozuo1, "?????????");
                //getNet_CaoZuo(dataBean.getShop_form_id(), "04167", position);
                //?????????
                getNetCuiFaHuo(dataBean, position);
                break;
            case "4":
                DefaultX5WebViewActivity.actionStart(getActivity(), dataBean.getExpress_url());
                //helper.setText(R.id.tv_caozuo1, "????????????");
                break;
            case "5":
//                helper.setText(R.id.tv_caozuo1, "????????????");
//
//                break;
                DingDanShenQingTuikuanActivity.actionStart(getActivity(), "????????????(????????????)", dataBean.getShop_form_id(), dataBean.getPay_money());
                break;
            case "6":

                // helper.setText(R.id.tv_caozuo1, "????????????");
                //  DefaultX5WebViewActivity.actionStart(getActivity(), dataBean.getExpress_url());
                // showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                //?????????
                AccessActivity.actionStart(getActivity(), dataBean.getIndex_photo_url(), dataBean.getShop_form_id());
                break;
            case "7":
                showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                //helper.setText(R.id.tv_caozuo1, "????????????");
                break;
            case "8":
            case "10":
            case "9":
                getActivity().finish();
                /**
                 * ???????????????1.??????2.?????? 3.???????????? 4.????????????????????????
                 */
                if (orderListAdapter.getData().get(position).getWares_type().equals("1")) {
                    ZiJianShopMallDetailsActivity.actionStart(getActivity(), dataBean.getShop_product_id(), dataBean.getWares_id());
                } else if (orderListAdapter.getData().get(position).getWares_type().equals("3")) {

                }


                // helper.setText(R.id.tv_caozuo1, "????????????");
                break;

            // helper.setText(R.id.tv_caozuo1, "????????????");

            //  helper.setText(R.id.tv_caozuo1, "????????????");
            //  helper.setVisible(R.id.tv_caozuo1, false);
            case "11":

                showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                break;


        }
    }

    public void doCaoZuo2(OrderListModel.DataBean dataBean, int position) {
        /**
         * user_pay_check	????????????:1.????????? 2.?????????(???)3.????????? 4.????????? 5.????????????6.????????? 7.?????? 8.???????????? 9.????????? 10.??????/?????? 11 ????????????
         */
        switch (dataBean.getUser_pay_check()) {
            case "1":


                break;
            case "2":


                break;
            case "3":


                break;
            case "4":
                ShenQingTuiKuanActivity.actionStart(getActivity(), dataBean.getShop_form_id(), dataBean.getTotal_money(), dataBean.getUser_pay_check());
                break;

            case "6":

                // helper.setText(R.id.tv_caozuo1, "????????????");
                //  DefaultX5WebViewActivity.actionStart(getActivity(), dataBean.getExpress_url());
                // showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                //?????????
                //AccessActivity.actionStart(getActivity(), dataBean.getInst_img_url(), dataBean.getShop_form_id());
                break;
            case "7":
                // showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                //helper.setText(R.id.tv_caozuo1, "????????????");
                break;
            case "8":
            case "10":
            case "9":
                // getActivity().finish();
                // ZiJianShopMallDetailsActivity.actionStart(getActivity(), dataBean.getShop_product_id(), dataBean.getWares_id());
                // helper.setText(R.id.tv_caozuo1, "????????????");
                break;

            // helper.setText(R.id.tv_caozuo1, "????????????");

            //  helper.setText(R.id.tv_caozuo1, "????????????");
            //  helper.setVisible(R.id.tv_caozuo1, false);
            case "11":

                //  showDngDanCaoZuo(dataBean, position, "??????????????????", "04157");
                break;


        }
    }

    private void getNetCuiFaHuo(OrderListModel.DataBean dataBean, int position) {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04167");
        map.put("key", Urls.key);
        map.put("token", PreferenceHelper.getInstance(getActivity()).getString("app_token", "0"));
        map.put("shop_form_id", dataBean.getShop_form_id());

        // if (NetworkUtils.isNetAvailable(DaLiBaoZhiFuActivity.this)) {
        Gson gson = new Gson();
        OkGo.<AppResponse<Object>>post(Urls.HOME_PICTURE_HOME)
                .tag(getActivity())//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Object>> response) {
                        UIHelper.ToastMessage(getActivity(), response.body().msg);
                    }

                    @Override
                    public void onError(Response<AppResponse<Object>> response) {
                        super.onError(response);
                        //  UIHelper.ToastMessage(getActivity(), response.body().msg);
                        String str = response.getException().getMessage();
                        //    Log.i("cuifahuo", str);

                        String[] str1 = str.split("???");

                        if (str1.length == 3) {
                            UIHelper.ToastMessage(getActivity(), str1[2]);
                        }
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

//    public void doCaoZuo2(OrderListModel.DataBean dataBean) {
//
//        /**
//         * user_pay_check	????????????:1.????????? 2.?????????(???)3.????????? 4.????????? 5.????????????6.????????? 7.?????? 8.???????????? 9.????????? 10.??????/?????? 11 ????????????
//         */
//        switch (dataBean.getUser_pay_check()) {
//
//            case "1":
//
//                helper.setText(R.id.tv_caozuo2, "????????????");
//
//                break;
//            case "2":
//
//                helper.setText(R.id.tv_caozuo2, "????????????");
//                helper.setVisible(R.id.tv_caozuo2, false);
//                break;
//            case "3":
//
//                helper.setText(R.id.tv_caozuo2, "????????????");
//                break;
//            case "4":
//
//                helper.setText(R.id.tv_caozuo2, "????????????");
//                helper.setVisible(R.id.tv_caozuo2, false);
//                break;
//            case "5":
//
//                //helper.setText(R.id.tv_caozuo2, "????????????");
//                helper.setVisible(R.id.tv_caozuo2, false);
//                break;
//            case "6":
//
//                helper.setText(R.id.tv_caozuo2, "????????????");
//                helper.setVisible(R.id.tv_caozuo, false);
//                break;
//            case "7":
//
//                helper.setText(R.id.tv_caozuo2, "????????????");
//                helper.setVisible(R.id.tv_caozuo2, false);
//                break;
//            case "8":
//
//                helper.setText(R.id.tv_caozuo2, "????????????");
//                helper.setVisible(R.id.tv_caozuo2, false);
//                break;
//            case "9":
//
//                helper.setText(R.id.tv_caozuo2, "????????????");
//                helper.setVisible(R.id.tv_caozuo2, false);
//                break;
//            case "10":
//
//                helper.setText(R.id.tv_caozuo2, "????????????");
//                break;
//            case "11":
//                helper.setVisible(R.id.tv_caozuo2, false);
//                break;
//
//        }
//
//    }


    public void enterDetails(String type) {

        switch (type) {
            case "1":

                break;
        }
    }

    String payType = "4";//1 ????????? 4 ??????
    private String appId;//??????id ????????????
    ProgressDialog progressDialog;
    private IWXAPI api;
    private String form_id;//??????id
    List<ProductDetails> productDetailsForJava = new ArrayList<>();

    public class ProductDetails {
        private String form_product_id;
        private String shop_product_id;
        private String pay_count;
        private String shop_form_text;
        private String wares_go_type;
        private String installation_type_id;
    }

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
        productDetails.form_product_id = dataBean.getInstallation_type_id();
        productDetailsForJava.add(productDetails);

        //OrderListModel.DataBean dataBean = orderListAdapter.getData().get(position);
        if (pay_id.equals("1")) {//1?????????
            Map<String, Object> map = new HashMap<>();
            map.put("key", Urls.key);
            map.put("token", UserManager.getManager(getActivity()).getAppToken());
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
                    .tag(getActivity())//
                    .upJson(myHeaderInfo)
                    .execute(new JsonCallback<AppResponse<YuZhiFuModel_AliPay.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<YuZhiFuModel_AliPay.DataBean>> response) {

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
            map.put("token", UserManager.getManager(getActivity()).getAppToken());
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
                    .tag(getActivity())//
                    .upJson(myHeaderInfo)
                    .execute(new JsonCallback<AppResponse<YuZhiFuModel.DataBean>>() {
                        @Override
                        public void onSuccess(Response<AppResponse<YuZhiFuModel.DataBean>> response) {
                            api = WXAPIFactory.createWXAPI(getActivity(), response.body().data.get(0).getPay().getAppid());
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
        api = WXAPIFactory.createWXAPI(getActivity(), out.getPay().getAppid());
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


    /**
     * ?????????????????????
     */
    public void payV2(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(getActivity());
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
                        PaySuccessUtils.getNet(getActivity(), form_id);
                        UIHelper.ToastMessage(getActivity(), "????????????", Toast.LENGTH_SHORT);
                        //  getActivity().finish();
                        pageNumber = 0;
                        getNet();//??????????????????????????????

                    } else {
                        // ???????????????????????????????????????????????????????????????????????????
                        Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
                        PaySuccessUtils.getNetFail(getActivity(), form_id);

                    }
                    break;
                }
                default:
                    break;
            }
        }

    };


    private int choice;
    private AlertDialog.Builder builder;

    /**
     * ?????? dialog
     */
    private void showSingSelect(OrderListModel.DataBean dataBean) {

        //?????????????????????
        final String[] items = {"??????", "?????????"};
        choice = -1;
        builder = new AlertDialog.Builder(getActivity()).setIcon(R.mipmap.logi_icon).setTitle("????????????")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        choice = i;
                    }
                }).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (choice == -1) {
                            Toast.makeText(getActivity(), "????????????????????????????????????", Toast.LENGTH_LONG).show();
                        } else if (items[choice].equals("??????")) {
                            //??????
                            String pay_id = "2";
                            PreferenceHelper.getInstance(getActivity()).putString(DINGDANZHIFU, DINGDANZHIFU);
                            getWeiXinOrZhiFuBao(pay_id, dataBean);
                            dialogInterface.dismiss();
                        } else {

                            String pay_id = "1";
                            getWeiXinOrZhiFuBao(pay_id, dataBean);
                            dialogInterface.dismiss();
                        }
                    }
                });
        builder.create().show();
    }
    TishiDialog tishiDialog;

    public void getNet_CaoZuo(String form_id, String code, int position) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("key", Urls.key);
        map.put("token", PreferenceHelper.getInstance(getActivity()).getString("app_token", "0"));
        map.put("shop_form_id", form_id);//??????

        Gson gson = new Gson();
        OkGo.<AppResponse<Object>>post(Urls.HOME_PICTURE_HOME)
                .tag(getActivity())//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Object>> response) {
                        //orderListAdapter.remove(position);
                        UIHelper.ToastMessage(getActivity(), "????????????");
                        //pageNumber = 0;
                        //getNet();
                        smartRefreshLayout.autoRefresh();
                    }

                    @Override
                    public void onError(Response<AppResponse<Object>> response) {
                        super.onError(response);
                        String str = response.getException().getMessage();
                        tishiDialog = new TishiDialog(getActivity(), 3, new TishiDialog.TishiDialogListener() {
                            @Override
                            public void onClickCancel(View v, TishiDialog dialog) {
                                tishiDialog.dismiss();
                            }

                            @Override
                            public void onClickConfirm(View v, TishiDialog dialog) {

                                getActivity().finish();
                            }

                            @Override
                            public void onDismiss(TishiDialog dialog) {

                            }
                        });
                        tishiDialog.setTextContent(str);
                        tishiDialog.show();
                    }
                });

    }

    /**
     * ??????????????? dialog
     */
    private void showDngDanCaoZuo(OrderListModel.DataBean dataBean, int position, String quXiaoDingDanHuaShu, String code) {

        builder = new AlertDialog.Builder(getActivity()).setIcon(R.mipmap.logi_icon).setTitle("????????????")
                .setMessage(quXiaoDingDanHuaShu).setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(getActivity(), "????????????", Toast.LENGTH_LONG).show();
                        getNet_CaoZuo(dataBean.getShop_form_id(), code, position);


                    }
                }).setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

}

