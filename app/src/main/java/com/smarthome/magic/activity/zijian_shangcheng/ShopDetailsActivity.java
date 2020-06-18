package com.smarthome.magic.activity.zijian_shangcheng;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.recyclerview.AppBarStateChangeListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smarthome.magic.R;
import com.smarthome.magic.adapter.ShopDetailsAdapter;
import com.smarthome.magic.adapter.ZhiNengDeviceListAdapter;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.UIHelper;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.DianPuXiangQingModel;
import com.smarthome.magic.util.GlideShowImageUtils;
import com.smarthome.magic.util.GridAverageUIDecoration;
import com.smarthome.magic.view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.smarthome.magic.get_net.Urls.HOME_PICTURE;

public class ShopDetailsActivity extends BaseActivity {

    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.titleView)
    RelativeLayout titleView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbarLayout)
    AppBarLayout appbarLayout;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    @BindView(R.id.cl_zonghe)
    ConstraintLayout clZonghe;
    @BindView(R.id.cl_xiaoliang)
    ConstraintLayout clXiaoliang;
    @BindView(R.id.tv_jiage)
    TextView tvJiage;
    @BindView(R.id.cl_jiage)
    ConstraintLayout clJiage;
    @BindView(R.id.rlv_list)
    RecyclerView rlvList;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.cl_1)
    ConstraintLayout cl1;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_shoucang_renshu)
    TextView tvShoucangRenshu;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_zonghe)
    TextView tvZonghe;
    @BindView(R.id.tv_xiaoliang)
    TextView tvXiaoliang;
    @BindView(R.id.iv_jiage_img)
    ImageView ivJiageImg;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private String inst_id;
    private String order_type = "";
    private int page_num;

    private String guanZhu = "0";//0 未关注 1已关注
    private ShopDetailsAdapter shopDetailsAdapter;
    private List<DianPuXiangQingModel.DataBean.WaresListBean> mDatas = new ArrayList<>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    titleText.setAlpha((float) 0);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    titleText.setAlpha((float) 1.0);
                } else {
                    //中间状态
                    titleText.setAlpha((float) 0.5);
                }
            }
        });
        inst_id = getIntent().getStringExtra("inst_id");
        getNet();

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.ToastMessage(mContext, "该功能暂未开放");
            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        setListAdapter();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        clZonghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvZonghe.setTextColor(mContext.getResources().getColor(R.color.orange_fa7e00));
                tvXiaoliang.setTextColor(mContext.getResources().getColor(R.color.black_000000));
                tvJiage.setTextColor(mContext.getResources().getColor(R.color.black_000000));

                getNet();
            }
        });
        clXiaoliang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvZonghe.setTextColor(mContext.getResources().getColor(R.color.black_000000));
                tvXiaoliang.setTextColor(mContext.getResources().getColor(R.color.orange_fa7e00));
                tvJiage.setTextColor(mContext.getResources().getColor(R.color.black_000000));
                order_type = "1";
                getNet();
            }
        });
        clJiage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvZonghe.setTextColor(mContext.getResources().getColor(R.color.black_000000));
                tvXiaoliang.setTextColor(mContext.getResources().getColor(R.color.black_000000));
                tvJiage.setTextColor(mContext.getResources().getColor(R.color.orange_fa7e00));
                if (order_type.equals("2")) {
                    order_type = "3";
                    ivJiageImg.setBackgroundResource(R.mipmap.dianpu_jiage_jiangxu);
                } else if (order_type.equals("3")) {
                    order_type = "2";
                    ivJiageImg.setBackgroundResource(R.mipmap.dianpu_jiage_shengxu);
                } else {
                    order_type = "2";
                    ivJiageImg.setBackgroundResource(R.mipmap.dianpu_jiage_shengxu);
                }

                getNet();


            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page_num = 0;
                getNet();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                page_num++;
                getNet();
            }
        });

        ivJiageImg.setBackgroundResource(R.mipmap.dianpu_jiage_jiangxu);
    }

    private void setListAdapter() {

        // recyclerView.addItemDecoration(new RecycleItemSpance(20, 2));
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 2);
        rlvList.addItemDecoration(new GridAverageUIDecoration(14, 10));

        rlvList.setLayoutManager(layoutManager);
        shopDetailsAdapter = new ShopDetailsAdapter(R.layout.item_shopdetails_shangpin, mDatas);
        shopDetailsAdapter.openLoadAnimation();//默认为渐显效果
        rlvList.setAdapter(shopDetailsAdapter);
    }

    /**
     * 排序类型：1.销量降序2.价格升序
     * 3.价格降序
     */
    private void getNet() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "04135");
        map.put("key", Urls.key);
        map.put("inst_id", inst_id);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("wares_type", "1");
        map.put("order_type", order_type);
        map.put("page_num", String.valueOf(page_num));
        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<DianPuXiangQingModel.DataBean>>post(HOME_PICTURE)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<DianPuXiangQingModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<DianPuXiangQingModel.DataBean>> response) {
                        DianPuXiangQingModel.DataBean dataBean = response.body().data.get(0);
                        Glide.with(mContext).applyDefaultRequestOptions(GlideShowImageUtils.showZhengFangXing()).load(dataBean.getInst_photo_url()).into(ivImage);
                        tvShopName.setText(dataBean.getInst_name());
                        tvShoucangRenshu.setText(dataBean.getIsCollection() + "人收藏");
                        titleText.setText(dataBean.getInst_name());
                        /**
                         * 是否已关注：
                         * 1.已关注0.未关注
                         */
                        if (dataBean.getIsCollection().equals("0")) {
                            iv2.setBackgroundResource(R.mipmap.shop_icon_weiguanzhu);
                        } else if (dataBean.getIsCollection().equals("1")) {
                            iv2.setBackgroundResource(R.mipmap.shop_icon_yiguanzhu);
                        }

                        if (page_num == 0) {
                            mDatas.clear();
                            mDatas.addAll(dataBean.getWaresList());

                           // shopDetailsAdapter.setNewData(mDatas);
                        } else {
                            mDatas.addAll(dataBean.getWaresList());
                        }

                        shopDetailsAdapter.notifyDataSetChanged();
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();

                        if (response.body().next.equals("0")) {
                            refreshLayout.setEnableLoadMore(false);
                        } else {
                            refreshLayout.setEnableLoadMore(true);
                        }
                    }

                    @Override
                    public void onError(Response<AppResponse<DianPuXiangQingModel.DataBean>> response) {
                        super.onError(response);
                    }
                });
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_shop_details;
    }

    /**
     * 用于其他Activty跳转到该Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String inst_id) {
        Intent intent = new Intent(context, ShopDetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("inst_id", inst_id);
        context.startActivity(intent);
    }
}
