package com.smarthome.magic.activity.zijian_shangcheng;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.AccessListActivity;
import com.smarthome.magic.activity.ChooseTaoCanActivity;
import com.smarthome.magic.activity.gouwuche.GouWuCheActivity;
import com.smarthome.magic.adapter.ZiJianPingLunAdapter;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.common.StringUtils;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.GlideImageLoader;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.GoodsDetails_f;
import com.smarthome.magic.project_A.zijian_interface.ZijianDetailsInterface;
import com.smarthome.magic.util.AlertUtil;
import com.smarthome.magic.util.phoneview.sample.ImageShowActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.smarthome.magic.get_net.Urls.HOME_PICTURE_HOME;

public class ZiJianShopMallDetailsActivity extends BaseActivity implements ZijianDetailsInterface {

    @BindView(R.id.rlv_list)
    RecyclerView rlvList;

    View headerView;//header ???
    View footerView;//footer ???

    ZiJianPingLunAdapter ziJianPingLunAdapter;

    List<GoodsDetails_f.DataBean.AssListBean> assListBeans = new ArrayList<>();
    @BindView(R.id.iv_dianpu_iamge)
    ImageView ivDianpuIamge;
    @BindView(R.id.tv_dianpu)
    TextView tvDianpu;
    @BindView(R.id.iv_kefu_image)
    ImageView ivKefuImage;
    @BindView(R.id.iv_shoucang_iamge)
    ImageView ivShoucangIamge;
    @BindView(R.id.ll_jiaru_gouwuche)
    LinearLayout llJiaruGouwuche;
    @BindView(R.id.ll_liji_goumai)
    LinearLayout llLijiGoumai;
    @BindView(R.id.tv_shoucang)
    TextView tvShoucang;
    @BindView(R.id.tv_kefu)
    TextView tvKefu;
    private Response<AppResponse<GoodsDetails_f.DataBean>> response;
    GoodsDetails_f.DataBean dataBean;
    private String productId;
    private String warseId;
    private String title;
    private String shopName;//?????????

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_zi_jian_shop_mall_details);
        productId = this.getIntent().getStringExtra("productId");
        warseId = this.getIntent().getStringExtra("wareId");
        ButterKnife.bind(this);
        getNet();

        PreferenceHelper.getInstance(ZiJianShopMallDetailsActivity.this).putString(App.PRODUCTID, warseId);
        //setClick();


    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("??????");
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
        iv_rightTitle.setVisibility(View.VISIBLE);
        iv_rightTitle.setBackgroundResource(R.mipmap.shop_xiangqing_gouwuche_slip);
        iv_rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //UIHelper.ToastMessage(ZiJianShopMallDetailsActivity.this, "??????????????????");
                // UIHelper.ToastMessage(ZiJianShopMallDetailsActivity.this, "???????????????");
                GouWuCheActivity.actionStart(ZiJianShopMallDetailsActivity.this);
            }
        });

    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_zi_jian_shop_mall_details;
    }


    private String isCollection = "0";//0 ???????????? 1 ?????????

    @Override
    public void getNet() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04133");
        map.put("key", Urls.key);
        map.put("token", PreferenceHelper.getInstance(mContext).getString("app_token", "0"));
        map.put("shop_product_id", productId);
        map.put("wares_id", warseId);
        Gson gson = new Gson();
        OkGo.<AppResponse<GoodsDetails_f.DataBean>>post(Urls.SERVER_URL + "shop_new/app")
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<GoodsDetails_f.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<GoodsDetails_f.DataBean>> response) {
                        ZiJianShopMallDetailsActivity.this.response = response;
                        assListBeans.addAll(response.body().data.get(0).getAssList());

                        if (assListBeans.size() == 0) {
                            //      View view = View.inflate(ZiJianShopMallDetailsActivity.this, R.layout.layout_none_ass, null);
                            //    ziJianPingLunAdapter.setEmptyView(view);
                            GoodsDetails_f.DataBean.AssListBean assListBean = new GoodsDetails_f.DataBean.AssListBean();
                            assListBean.setNonDataShow("1");
                            assListBeans.add(assListBean);

                        }
                        loadList();
                        //ziJianPingLunAdapter.notifyDataSetChanged();
                        setHeader();
                        //ziJianPingLunAdapter.notifyDataSetChanged();
                        setFooter();
                        setClick();
                        //??????????????????????????????:
                        //1.?????????0.?????????
                        if (response.body().data.get(0).getIsCollection().equals("0")) {
                            ivShoucangIamge.setBackgroundResource(R.mipmap.shopdetails_weishoucang);
                        } else if (response.body().data.get(0).getIsCollection().equals("1")) {
                            ivShoucangIamge.setBackgroundResource(R.mipmap.shop_icon_weishoucang);
                        }

                        isCollection = response.body().data.get(0).getIsCollection();

                        ivShoucangIamge.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isCollection.equals("0")) {
                                    setShouCang("04126");
                                } else if (isCollection.equals("1")) {
                                    setShouCang("04127");
                                }
                            }
                        });

                        tvShoucang.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isCollection.equals("0")) {
                                    setShouCang("04126");
                                } else if (isCollection.equals("1")) {
                                    setShouCang("04127");
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Response<AppResponse<GoodsDetails_f.DataBean>> response) {
                        AlertUtil.t(ZiJianShopMallDetailsActivity.this, response.getException().getMessage());
                    }
                });

    }

    private void setShouCang(String code) {
        //????????????
        /**
         * code	?????????(04126)	6	???
         * key	????????????	10	???
         * token	token	18	???
         * collection_type	???????????? 1.?????? 2.??????		???
         * wares_id	??????id(???????????????1??????)		???
         * shop_product_id	??????id(???????????????1??????)		???
         * inst_id	??????id(???????????????2??????)		???
         */

        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("key", Urls.key);
        map.put("token", PreferenceHelper.getInstance(mContext).getString("app_token", "0"));
        map.put("collection_type", "1");
        map.put("wares_id", warseId);
        map.put("shop_product_id", productId);

        Gson gson = new Gson();
        OkGo.<AppResponse<Object>>post(HOME_PICTURE_HOME)
                .tag(mContext)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<Object>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<Object>> response) {
                        if (code.equals("04126")) {
                            UIHelper.ToastMessage(ZiJianShopMallDetailsActivity.this, "????????????");
                            ivShoucangIamge.setBackgroundResource(R.mipmap.shop_icon_weishoucang);
                            isCollection = "1";
                        } else {
                            UIHelper.ToastMessage(ZiJianShopMallDetailsActivity.this, "??????????????????");
                            ivShoucangIamge.setBackgroundResource(R.mipmap.shopdetails_weishoucang);
                            isCollection = "0";
                        }

                    }

                    @Override
                    public void onError(Response<AppResponse<Object>> response) {
                        super.onError(response);
                        String str = response.getException().getMessage();
                        String[] str1 = str.split("???");

                        if (str1.length == 3) {
                            UIHelper.ToastMessage(ZiJianShopMallDetailsActivity.this, str1[2]);
                        }
                    }
                });
    }

    @Override
    public void loadList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rlvList.setLayoutManager(linearLayoutManager);
        ziJianPingLunAdapter = new ZiJianPingLunAdapter(assListBeans);
        // ziJianPingLunAdapter.notifyDataSetChanged();
        rlvList.setAdapter(ziJianPingLunAdapter);
    }

    Banner banner;
    RelativeLayout rlTaoCan;
    RelativeLayout rlCanshu;

    @Override
    public void setHeader() {
        headerView = View.inflate(this, R.layout.zijian_shopmall_header, null);
        banner = headerView.findViewById(R.id.banner);
        GoodsDetails_f.DataBean dataBean = response.body().data.get(0);
        TextView tvSeeMore = headerView.findViewById(R.id.tv_see_more);
        ImageView ivSeeMore = headerView.findViewById(R.id.iv_see_more_back);
        TextView tvPrice = headerView.findViewById(R.id.tv_price);
        TextView tvTitle = headerView.findViewById(R.id.tv_title);
        TextView tvKuaidi = headerView.findViewById(R.id.tv_kuaidi);
        TextView tvYueXiao = headerView.findViewById(R.id.tv_yuexiao);
        TextView tvDiZhi = headerView.findViewById(R.id.tv_dizhi);
        rlTaoCan = headerView.findViewById(R.id.rl_taocan);
        rlCanshu = headerView.findViewById(R.id.rl_canshu);
        List<String> items = new ArrayList<>();
        if (response.body().data != null) {
            for (int i = 0; i < response.body().data.get(0).getBannerList().size(); i++) {
                items.add(response.body().data.get(0).getBannerList().get(i).getImg_url());
            }
        }



        tvPrice.setText("??" + response.body().data.get(0).getMoney_begin() + "-" + response.body().data.get(0).getMoney_end());
        tvTitle.setText(response.body().data.get(0).getShop_product_title());
        String kuaidi = response.body().data.get(0).getWares_go_type();
        //     ???????????????1.??????/??????
        //     2.?????? 3.??????

        if (kuaidi.equals("1")) {
            tvKuaidi.setText("??????/??????");
        } else if (kuaidi.equals("2")) {
            tvKuaidi.setText("??????");
        } else if (kuaidi.equals("3")) {
            tvKuaidi.setText("??????");
        }

        PreferenceHelper.getInstance(ZiJianShopMallDetailsActivity.this).putString(App.KUAIDITYPE, kuaidi);

        PreferenceHelper.getInstance(ZiJianShopMallDetailsActivity.this).putString(App.KUAIDIFEI, dataBean.getWares_money_go());

        tvYueXiao.setText(dataBean.getWares_sales_volume());
        tvDiZhi.setText(dataBean.getAddr());
        //?????????????????????
        banner.setImageLoader(new GlideImageLoader());
        if (banner != null) {
            //??????????????????
            banner.setImages(items);
            //banner?????????????????????????????????????????????
            banner.start();
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    //startActivity(new Intent(ZiJianShopMallDetailsActivity.this, WebViewActivity.class).putExtra("url", response.body().data.get(0).getBannerList().get(position).getImg_url()));
                    ArrayList<String> list = new ArrayList<>();
                    list.add(response.body().data.get(0).getBannerList().get(position).getImg_url());
                    ImageShowActivity.actionStart(ZiJianShopMallDetailsActivity.this, list);

                }
            });
        }


        if (response.body().data.get(0).getAssList().size() == 0) {
            ivSeeMore.setVisibility(View.GONE);
            tvSeeMore.setVisibility(View.GONE);
        }

        tvSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessListActivity.actionStart(mContext, warseId);
            }
        });

        ziJianPingLunAdapter.addHeaderView(headerView);
    }


    @Override
    public void setFooter() {
        footerView = View.inflate(this, R.layout.zijian_shopmall_footer, null);
        LinearLayout linearLayout = footerView.findViewById(R.id.ll_footer);
        List<GoodsDetails_f.DataBean.DetailImgListBean> productListBeans = response.body().data.get(0).getDetailImgList();
        for (int i = 0; i < productListBeans.size(); i++) {
            //  ImageView iv = new ImageView(ZiJianShopMallDetailsActivity.this);

            View view = View.inflate(ZiJianShopMallDetailsActivity.this, R.layout.item_big_image, null);
            ImageView iv = view.findViewById(R.id.iv_img);
//
//            int screenWidth = getScreenWidth(this); // ??????????????????
//            ViewGroup.LayoutParams para;
//            para = iv.getLayoutParams();
//
//            Log.d("zijianshopmalldetail", "layout height0: " + para.height);
//            Log.d("zijianshopmalldetail", "layout width0: " + para.width);
//
//            para.height = Integer.parseInt(productListBeans.get(i).getImg_height());
//            para.width = screenWidth;
//            iv.setLayoutParams(para);

            if (!StringUtils.isEmpty(productListBeans.get(i).getImg_height())) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.override(Integer.valueOf(productListBeans.get(i).getImg_width()), Integer.valueOf(productListBeans.get(i).getImg_height()));
                Glide.with(ZiJianShopMallDetailsActivity.this).load(productListBeans.get(i).getImg_url()).apply(requestOptions).into(iv);
            }
            linearLayout.addView(view);
        }
        ziJianPingLunAdapter.addFooterView(footerView);
        ziJianPingLunAdapter.notifyDataSetChanged();
    }

    @Override
    public void setClick() {
        ivDianpuIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????
                ShopDetailsActivity.actionStart(mContext, response.body().data.get(0).getInst_id());
            }
        });
        tvDianpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopDetailsActivity.actionStart(mContext, response.body().data.get(0).getInst_id());
            }
        });
        ivKefuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????
                //Demo_rongyun.actionStart(mContext,response.body().data.get(0).getInst_accid());
                Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
                String targetId = response.body().data.get(0).getInst_accid();
                String instName = response.body().data.get(0).getInst_name();
                Bundle bundle = new Bundle();
                bundle.putString("dianpuming", instName);
                bundle.putString("inst_accid", response.body().data.get(0).getInst_accid());
                bundle.putString("shoptype","1");
                RongIM.getInstance().startConversation(mContext, conversationType, targetId, instName, bundle);
            }
        });
        tvKefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????
                //Demo_rongyun.actionStart(mContext,response.body().data.get(0).getInst_accid());
                Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
                String targetId = response.body().data.get(0).getInst_accid();
                String instName = response.body().data.get(0).getInst_name();
                Bundle bundle = new Bundle();
                bundle.putString("dianpuming", instName);
                bundle.putString("inst_accid", response.body().data.get(0).getInst_accid());
                bundle.putString("shoptype","1");
                RongIM.getInstance().startConversation(mContext, conversationType, targetId, instName, bundle);
            }
        });

        llJiaruGouwuche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //???????????????
                dataBean = response.body().data.get(0);
                ChooseTaoCanActivity.actionStart(ZiJianShopMallDetailsActivity.this, dataBean, "1", dataBean.getMoney_begin(), dataBean.getMoney_end(), response.body().data.get(0).getShop_product_title(), dataBean.getInst_name(), dataBean.getInst_logo_url(), response.body().data.get(0).getWares_go_type(), "0");
            }
        });
        llLijiGoumai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????
                dataBean = response.body().data.get(0);
                ChooseTaoCanActivity.actionStart(ZiJianShopMallDetailsActivity.this, dataBean, "1", dataBean.getMoney_begin(), dataBean.getMoney_end(), response.body().data.get(0).getShop_product_title(), dataBean.getInst_name(), dataBean.getInst_logo_url(), response.body().data.get(0).getWares_go_type(), "1");
            }
        });
        rlTaoCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBean = response.body().data.get(0);
                ChooseTaoCanActivity.actionStart(ZiJianShopMallDetailsActivity.this, dataBean, "1", dataBean.getMoney_begin(), dataBean.getMoney_end(), response.body().data.get(0).getShop_product_title(), dataBean.getInst_name(), dataBean.getInst_logo_url(), response.body().data.get(0).getWares_go_type(), "0");

            }
        });
        rlCanshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBean = response.body().data.get(0);
                ChooseTaoCanActivity.actionStart(ZiJianShopMallDetailsActivity.this, dataBean, "2", dataBean.getMoney_begin(), dataBean.getMoney_end(), response.body().data.get(0).getShop_product_title(), dataBean.getInst_name(), dataBean.getInst_logo_url(), response.body().data.get(0).getWares_go_type(), "0");
            }
        });
    }

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String productId, String wareId) {
        Intent intent = new Intent(context, ZiJianShopMallDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("productId", productId);
        intent.putExtra("wareId", wareId);
        context.startActivity(intent);
    }

}
