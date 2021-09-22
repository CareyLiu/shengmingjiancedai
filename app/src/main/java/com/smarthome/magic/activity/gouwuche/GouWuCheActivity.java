package com.smarthome.magic.activity.gouwuche;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.zhinengjiaju.peinet.NetUtils;
import com.smarthome.magic.activity.zijian_shangcheng.ZiJianShopMallDetailsActivity;
import com.smarthome.magic.adapter.GouWuCheAdapter;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.GouWuCheDataModel;
import com.smarthome.magic.model.GouWuCheZhengHeModel;
import com.tuya.android.mist.core.html.SpanMark;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.smarthome.magic.get_net.Urls.HOME_PICTURE_HOME;


public class GouWuCheActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.srL_smart)
    SmartRefreshLayout srLSmart;
    @BindView(R.id.iv_choose_all)
    ImageView ivChooseAll;
    @BindView(R.id.ll_jiesuan)
    LinearLayout llJiesuan;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.ll_shanchu)
    LinearLayout llShanchu;
    @BindView(R.id.cl_jiesuan)
    ConstraintLayout clJiesuan;
    @BindView(R.id.tv_jiesuan_jine)
    TextView tvJiesuanJine;
    @BindView(R.id.tv_quanxuan)
    TextView tvQuanxuan;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.empty_view)
    ConstraintLayout emptyView;
    private String guanliOrBianji = "1";//1管理 2编辑
    private GouWuCheAdapter gouWuCheAdapter;
    List<GouWuCheZhengHeModel> mDatas = new ArrayList<>();
    private int pageNumber = 0;


    private String quanxuan = "0";//0否1是
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setList();

        initToolbar();
        tv_rightTitle.setVisibility(View.VISIBLE);
        tv_rightTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        if (guanliOrBianji.equals("1")) {
            tv_rightTitle.setText("       管理 ");
        } else {
            tv_rightTitle.setText("       完成 ");
        }
        tv_rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guanliOrBianji.equals("1")) {
                    tv_rightTitle.setText("       管理 ");
                    guanliOrBianji = "2";
                    llShanchu.setVisibility(View.GONE);
                    clJiesuan.setVisibility(View.VISIBLE);
                } else {
                    tv_rightTitle.setText("       完成 ");
                    guanliOrBianji = "1";
                    llShanchu.setVisibility(View.VISIBLE);
                    clJiesuan.setVisibility(View.GONE);
                }
            }
        });

        getNet();

        ivChooseAll.setBackgroundResource(R.mipmap.quxiaodingdan_button_nor);
        ivChooseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quanxuan.equals("0")) {
                    for (int i = 0; i < mDatas.size(); i++) {
                        mDatas.get(i).setIsSelect("1");
                    }
                    quanxuan = "1";
                    gouWuCheAdapter.notifyDataSetChanged();

                    ivChooseAll.setBackgroundResource(R.mipmap.quxiaodingdan_button_sel);
                } else {
                    for (int i = 0; i < mDatas.size(); i++) {
                        mDatas.get(i).setIsSelect("0");
                    }
                    gouWuCheAdapter.notifyDataSetChanged();
                    quanxuan = "0";
                    ivChooseAll.setBackgroundResource(R.mipmap.quxiaodingdan_button_nor);
                }
                tvPrice.setText("(¥" + getJieSuanJinE().toString() + ")");
                tvJiesuanJine.setText("结算(" + String.valueOf(chooseCount) + ")");
            }
        });
        tvQuanxuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quanxuan.equals("0")) {
                    for (int i = 0; i < mDatas.size(); i++) {
                        mDatas.get(i).setIsSelect("1");
                    }
                    quanxuan = "1";
                    gouWuCheAdapter.notifyDataSetChanged();

                    ivChooseAll.setBackgroundResource(R.mipmap.quxiaodingdan_button_sel);
                } else {
                    for (int i = 0; i < mDatas.size(); i++) {
                        mDatas.get(i).setIsSelect("0");
                    }
                    gouWuCheAdapter.notifyDataSetChanged();
                    quanxuan = "0";
                    ivChooseAll.setBackgroundResource(R.mipmap.quxiaodingdan_button_nor);
                }
                tvPrice.setText("(¥" + getJieSuanJinE().toString() + ")");
                tvJiesuanJine.setText("结算(" + String.valueOf(chooseCount) + ")");

            }
        });

        llShanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDngDanDeleteCaoZuo();
            }
        });

        srLSmart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNumber = 0;
                getNet();
            }
        });

        llJiesuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseCount == 0) {
                    UIHelper.ToastMessage(GouWuCheActivity.this, "请选择您要购买的商品");
                    return;
                }
                List<GouWuCheZhengHeModel> gouWuCheZhengHeModels = getJieSuanList();
                if (gouWuCheZhengHeModels.size() == 0) {
                    UIHelper.ToastMessage(GouWuCheActivity.this, "请选择您要购买的商品");
                    return;
                }
                GouWuCheQueRenDingDanActivity.actionStart(GouWuCheActivity.this, gouWuCheZhengHeModels);
            }
        });

//        srLSmart.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                pageNumber++;
//                getNet();
//            }
//        });
        //srLSmart.setEnableLoadMore(true);

    }


    public void getNet() {

        if (!NetUtils.isNetworkConnected(GouWuCheActivity.this)) {
            UIHelper.ToastMessage(GouWuCheActivity.this, "当前无网络,请联网后重新尝试");
        }

        //  ProgressDialog progressDialog = ProgressDialog.show(GouWuCheActivity.this, null, "正在加载，请稍候···", true, false);

        Map<String, String> map = new HashMap<>();
        map.put("code", "04152");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("page_number", String.valueOf(pageNumber));


        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<GouWuCheDataModel.DataBean>>post(HOME_PICTURE_HOME)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<GouWuCheDataModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<GouWuCheDataModel.DataBean>> response) {
                        srLSmart.finishRefresh();
                        srLSmart.finishLoadMore();
                        if (pageNumber == 0) {
                            mDatas.clear();
                            zhengHeList(response);
                            gouWuCheAdapter.setNewData(mDatas);
                        } else {
                            zhengHeList(response);
                        }

                        gouWuCheAdapter.notifyDataSetChanged();

                        if (mDatas.size() > 0) {
                            emptyView.setVisibility(View.GONE);
                        }
                        // progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Response<AppResponse<GouWuCheDataModel.DataBean>> response) {
                        super.onError(response);

                        String str = response.getException().getMessage();
                        UIHelper.ToastMessage(mContext, str);

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        // progressDialog.dismiss();
                    }
                });
    }


    private String biaoshi = "0x11";

    private void zhengHeList(Response<AppResponse<GouWuCheDataModel.DataBean>> response) {

        for (int i = 0; i < response.body().data.size(); i++) {
            GouWuCheZhengHeModel gouWuCheZhengHeModel = new GouWuCheZhengHeModel(true, response.body().data.get(i).getInst_name());
            /**
             *     private String shopcart_id;
             *     private String inst_id;
             *     private String inst_name;
             *     private String inst_logo_url;
             *     private String subsystem_id;
             */
            gouWuCheZhengHeModel.setShopcart_id(response.body().data.get(i).getShopcart_id());
            gouWuCheZhengHeModel.setInst_id(response.body().data.get(i).getInst_id());
            gouWuCheZhengHeModel.setInst_name(response.body().data.get(i).getInst_name());
            gouWuCheZhengHeModel.setInst_logo_url(response.body().data.get(i).getInst_logo_url());
            gouWuCheZhengHeModel.setSubsystem_id(response.body().data.get(i).getSubsystem_id());
            gouWuCheZhengHeModel.setBiaoshi(biaoshi + i);
            mDatas.add(gouWuCheZhengHeModel);

            for (int j = 0; j < response.body().data.get(i).getProList().size(); j++) {
                GouWuCheDataModel.DataBean.ProListBean proListBean = response.body().data.get(i).getProList().get(j);
                GouWuCheZhengHeModel gouWuCheZhengHeModel_list = new GouWuCheZhengHeModel(false, "");
                /**
                 *     private String shop_product_title;
                 *     private String shop_product_id;
                 *     private String wares_id;
                 *     private String form_product_id;
                 *     private String disabled_cause;
                 *     private String wares_money_go;
                 *     private String product_title;
                 *     private String index_photo_url;
                 *     private String form_product_money;
                 *     private String pay_count;
                 *     private String form_product_state;
                 *     private String wares_go_type;
                 */

                gouWuCheZhengHeModel_list.setShop_product_title(proListBean.getShop_product_title());
                gouWuCheZhengHeModel_list.setShop_product_id(proListBean.getShop_product_id());
                gouWuCheZhengHeModel_list.setWares_id(proListBean.getWares_id());
                gouWuCheZhengHeModel_list.setForm_product_id(proListBean.getForm_product_id());
                gouWuCheZhengHeModel_list.setDisabled_cause(proListBean.getDisabled_cause());
                gouWuCheZhengHeModel_list.setWares_money_go(proListBean.getWares_money_go());
                gouWuCheZhengHeModel_list.setProduct_title(proListBean.getProduct_title());
                gouWuCheZhengHeModel_list.setIndex_photo_url(proListBean.getIndex_photo_url());
                gouWuCheZhengHeModel_list.setForm_product_money(proListBean.getForm_product_money());
                gouWuCheZhengHeModel_list.setPay_count(proListBean.getPay_count());
                gouWuCheZhengHeModel_list.setForm_product_state(proListBean.getForm_product_state());
                gouWuCheZhengHeModel_list.setWares_go_type(proListBean.getWares_go_type());
                gouWuCheZhengHeModel_list.setInstallation_type_id(proListBean.getInstallation_type_id());
                gouWuCheZhengHeModel_list.setBiaoshi(biaoshi + i);
                if (j == response.body().data.get(i).getProList().size() - 1) {
                    //最后一个
                    gouWuCheZhengHeModel_list.setBottomYuanJiao("1");

                } else {
                    gouWuCheZhengHeModel_list.setBottomYuanJiao("0");
                }
                mDatas.add(gouWuCheZhengHeModel_list);
            }
        }


    }

    private void setList() {
        gouWuCheAdapter = new GouWuCheAdapter(R.layout.item_gouwuche_tiaomu, R.layout.item_gouwuche_header, mDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GouWuCheActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(gouWuCheAdapter);
        srLSmart.setEnableLoadMore(false);
        gouWuCheAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {

                    case R.id.iv_select_item:
                        if (mDatas.get(position).getForm_product_state().equals("2")) {
                            return;
                        }
                        if (mDatas.get(position).getIsSelect().equals("0")) {
                            mDatas.get(position).setIsSelect("1");
                        } else {
                            mDatas.get(position).setIsSelect("0");
                        }

                        if (isSelectAllDianPu(position).equals("1")) {

                        } else {

                        }
                        gouWuCheAdapter.notifyDataSetChanged();
                        tvPrice.setText("(¥" + getJieSuanJinE().toString() + ")");
                        tvJiesuanJine.setText("结算(" + String.valueOf(chooseCount) + ")");


                        if (isSelectAll()) {
                            ivChooseAll.setBackgroundResource(R.mipmap.quxiaodingdan_button_sel);
                        } else {
                            ivChooseAll.setBackgroundResource(R.mipmap.quxiaodingdan_button_nor);
                        }
                        break;

                    case R.id.dianpu_select:
                        String strBiaoZhi = mDatas.get(position).getBiaoshi();

                        if (mDatas.get(position).getIsSelect().equals("0")) {
                            for (int i = 0; i < mDatas.size(); i++) {
                                if (strBiaoZhi.equals(mDatas.get(i).getBiaoshi())) {
                                    mDatas.get(i).setIsSelect("1");
                                }
                            }
                        } else {
                            for (int i = 0; i < mDatas.size(); i++) {
                                if (strBiaoZhi.equals(mDatas.get(i).getBiaoshi())) {
                                    mDatas.get(i).setIsSelect("0");
                                }
                            }
                        }

                        tvPrice.setText("(¥" + getJieSuanJinE().toString() + ")");
                        tvJiesuanJine.setText("结算(" + String.valueOf(chooseCount) + ")");
                        gouWuCheAdapter.notifyDataSetChanged();
                        if (isSelectAll()) {
                            ivChooseAll.setBackgroundResource(R.mipmap.quxiaodingdan_button_sel);
                        } else {
                            ivChooseAll.setBackgroundResource(R.mipmap.quxiaodingdan_button_nor);

                        }
                        break;

                    case R.id.tv_jiahao:
                        int payCountJIa = Integer.parseInt(mDatas.get(position).getPay_count()) + 1;
                        mDatas.get(position).setPay_count(payCountJIa + "");
                        tvPrice.setText("(¥" + getJieSuanJinE().toString() + ")");
                        tvJiesuanJine.setText("结算(" + String.valueOf(chooseCount) + ")");
                        gouWuCheAdapter.notifyDataSetChanged();
                        break;

                    case R.id.tv_jianhao:
                        if (StringUtils.isEmpty(mDatas.get(position).getPay_count())) {
                            mDatas.get(position).setPay_count("0");
                        }
                        BigDecimal bigDecimal_num = new BigDecimal(mDatas.get(position).getPay_count());
                        BigDecimal bigDecimal_num_1 = new BigDecimal("1");
                        if (bigDecimal_num.compareTo(bigDecimal_num_1) == 0) {
                            UIHelper.ToastMessage(GouWuCheActivity.this, "您购买的数量不能小于1");
                            return;
                        }


                        int payCountJian = Integer.parseInt(mDatas.get(position).getPay_count()) - 1;
                        mDatas.get(position).setPay_count(payCountJian + "");
                        tvPrice.setText("(¥" + getJieSuanJinE().toString() + ")");
                        tvJiesuanJine.setText("结算(" + String.valueOf(chooseCount) + ")");
                        gouWuCheAdapter.notifyDataSetChanged();
                        break;
                    case R.id.cl_item:
                        ZiJianShopMallDetailsActivity.actionStart(mContext, mDatas.get(position).getShop_product_id(), mDatas.get(position).getWares_id());
                        break;
                }


            }
        });

        gouWuCheAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (mDatas.get(position).getForm_product_state().equals("2")) {

                    showDngDanDeleteDanTiaoCaoZuo(position);

                }
                return false;
            }
        });
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_gou_wu_che;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("购物车");
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

    /**
     * 用于其他Activty跳转到该Activity
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, GouWuCheActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private BigDecimal zongJiaBigDecimal;
    private int chooseCount;

    private BigDecimal getJieSuanJinE() {
        zongJiaBigDecimal = new BigDecimal("0.00");
        chooseCount = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getIsSelect().equals("1")) {//选中
                if (!mDatas.get(i).isHeader) {
                    if (mDatas.get(i).getForm_product_state().equals("1")) {
                        BigDecimal djBigDecimal = new BigDecimal(mDatas.get(i).getForm_product_money());
                        BigDecimal countBigDecimal = new BigDecimal(mDatas.get(i).getPay_count());
                        BigDecimal danShangPinBigDecimal = djBigDecimal.multiply(countBigDecimal);
                        zongJiaBigDecimal = danShangPinBigDecimal.add(zongJiaBigDecimal);
                        zongJiaBigDecimal = zongJiaBigDecimal.add(new BigDecimal(mDatas.get(i).getWares_money_go()));
                        chooseCount = chooseCount + Integer.valueOf(mDatas.get(i).getPay_count());
                    }

                }
            }
        }

        return zongJiaBigDecimal;
    }


    private boolean isSelectAll() {

        boolean flag = true;
        for (int i = 0; i < mDatas.size(); i++) {

            if (mDatas.get(i).getIsSelect().equals("0")) {
                flag = false;
                break;
            }

        }
        return flag;
    }


    List<FormProductId> formProductIdList = new ArrayList<>();

    private class FormProductId {

        public String getForm_product_id() {
            return form_product_id;
        }

        public void setForm_product_id(String form_product_id) {
            this.form_product_id = form_product_id;
        }

        private String form_product_id;
    }


    public void getDeleteNet(String type) {
        // List<FormProductId> formProductIds = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("code", "04153");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("type", type);
        if (formProductIdList.size() == 0) {
            UIHelper.ToastMessage(GouWuCheActivity.this, "请选择商品后再删除");
            return;
        }
        map.put("where", formProductIdList);

        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<Object>>post(HOME_PICTURE_HOME)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Object>> response) {
                        pageNumber = 0;
                        getNet();
                    }
                });
    }

    /**
     * 两个按钮的 dialog
     */
    private void showDngDanDeleteCaoZuo() {

        builder = new AlertDialog.Builder(GouWuCheActivity.this).setIcon(R.mipmap.logi_icon).setTitle("订单操作")
                .setMessage("是否确定删除订单").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {

                        //Toast.makeText(getActivity(), "确定按钮", Toast.LENGTH_LONG).show();
                        String type = "2";   //type	全部删除：1.是2.否
                        formProductIdList = new ArrayList<>();
                        for (int i = 0; i < mDatas.size(); i++) {
                            if (!mDatas.get(i).isHeader) {
                                if (mDatas.get(i).getIsSelect().equals("1")) {
                                    FormProductId formProductId = new FormProductId();
                                    formProductId.form_product_id = mDatas.get(i).getForm_product_id();
                                    formProductIdList.add(formProductId);
                                }
                            }
                        }

                        if (isSelectAll()) {
                            type = "1";
                        } else {
                            type = "2";
                        }
                        getDeleteNet(type);

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }


    /**
     * 两个按钮的 dialog
     */
    private void showDngDanDeleteDanTiaoCaoZuo(int position) {

        builder = new AlertDialog.Builder(GouWuCheActivity.this).setIcon(R.mipmap.logi_icon).setTitle("订单操作")
                .setMessage("是否确定删除订单").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        //Toast.makeText(getActivity(), "确定按钮", Toast.LENGTH_LONG).show();
                        String type = "2";   //type	全部删除：1.是2.否
                        formProductIdList = new ArrayList<>();
                        FormProductId formProductId = new FormProductId();
                        formProductId.form_product_id = mDatas.get(position).getForm_product_id();
                        formProductIdList.add(formProductId);

                        getDeleteNet(type);

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private List<GouWuCheZhengHeModel> getJieSuanList() {
        List<GouWuCheZhengHeModel> gouWuCheZhengHeModels = new ArrayList<>();

        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getIsSelect().equals("1")) {
                if (mDatas.get(i).getForm_product_state() == null) {
                    gouWuCheZhengHeModels.add(mDatas.get(i));
                } else if (mDatas.get(i).getForm_product_state().equals("1")) {
                    gouWuCheZhengHeModels.add(mDatas.get(i));
                }
            }
        }
        return gouWuCheZhengHeModels;
    }

    String selectDianPuAll;//0 没有全选店铺 1 全选了店铺


    private String isSelectAllDianPu(int position) {
        selectDianPuAll = "1";
        String strFlag = "0";//0 只选择了店铺头部  1 选择了商品了已经 选多少不知道
        int dianpuSelectPosition = -1;
        String biaoshi = mDatas.get(position).getBiaoshi();
        for (int i = 0; i < mDatas.size(); i++) {

            if (mDatas.get(i).getBiaoshi().equals(biaoshi)) {
//同一标志下的所有数据
                if (mDatas.get(i).isHeader) {
                    dianpuSelectPosition = i;
                    mDatas.get(dianpuSelectPosition).setIsSelect("1");
                }
                if (!mDatas.get(i).isHeader) {
                    if (mDatas.get(i).getIsSelect().equals("0")) {//没有选中
                        selectDianPuAll = "0";


                    } else {

                        strFlag = "1";
                        return "0";//没全选 但是选了
                    }
                }
            }
        }

        if (strFlag.equals("0")) {
            mDatas.get(dianpuSelectPosition).setIsSelect("0");
        }

        return selectDianPuAll;
    }
}
