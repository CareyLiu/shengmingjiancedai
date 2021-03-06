package com.smarthome.magic.activity.taokeshagncheng;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.smarthome.magic.R;
import com.smarthome.magic.adapter.NewsFragmentPagerAdapter;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.callback.JsonCallback;
import com.smarthome.magic.config.AppResponse;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.fragment.taoke_shangcheng.TaoKeMallListFragment;
import com.smarthome.magic.get_net.Urls;
import com.smarthome.magic.model.MessageListBean;
import com.smarthome.magic.model.TaoKeTitleListModel;
import com.smarthome.magic.project_A.zijian_interface.TaoKeHomeInterface;
import com.smarthome.magic.view.CustomViewPager;
import com.smarthome.magic.view.magicindicator.MagicIndicator;
import com.smarthome.magic.view.magicindicator.ViewPagerHelper;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.smarthome.magic.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.smarthome.magic.get_net.Urls.MSG;

public class TaoKeHomeActivity extends BaseActivity implements TaoKeHomeInterface {
    private static final String TAG = "MessageFragment";
    MagicIndicator magicIndicator;

    List<String> tagList;
    CustomViewPager viewPager;
    ArrayList<Fragment> fragments = new ArrayList<>();
    List<MessageListBean> listBeans = new ArrayList<>();
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.magic_indicator4)
    MagicIndicator magicIndicator4;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        tagList = new ArrayList<>();
//        tagList.add("??????");
//        tagList.add("??????");
//        tagList.add("??????");
//        tagList.add("??????");
//        tagList.add("??????");
//
//        tagList.add("??????");
//        tagList.add("??????");
//        tagList.add("??????");
//        tagList.add("??????");
//        tagList.add("??????");

        // magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator4);
        viewPager = findViewById(R.id.view_pager);
        // view.setClickable(true);// ??????????????????????????????fragment??????????????????????????????

        //  initMagicIndicator1(tagList);
        getNet();
        banner();
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Custom5SearchActivity.actionStart(TaoKeHomeActivity.this);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initMagicIndicator1(final List<TaoKeTitleListModel.DataBean> list) {
        MagicIndicator magicIndicator = findViewById(R.id.magic_indicator4);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.black_222222));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.pinkff002f));
                simplePagerTitleView.setText(list.get(index).getItem_name());
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
                linePagerIndicator.setColors(getResources().getColor(R.color.pinkff002f));
                return linePagerIndicator;
            }
        });
        // commonNavigator.setAdjustMode(true);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
        commonNavigator.notifyDataSetChanged();

    }


    private void setThisAdapter() {
        // messageListFragments.clear();//??????
        int count = dataBeanList.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
//            if (tagList.get(i).id != null) {
//                data("id", tagList.get(i) + "");
//            }
            //  data.putInt("type", list.get(i).type);

            data.putString("title", dataBeanList.get(i).getItem_name());
            data.putSerializable("beanList", (Serializable) dataBeanList.get(i).getChild());
            TaoKeMallListFragment taoKeMallListFragment = new TaoKeMallListFragment();
            taoKeMallListFragment.setArguments(data);
            fragments.add(taoKeMallListFragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mAdapetr);

    }

    @Override
    public int getContentViewResId() {
        return R.layout.layout_taoke_home;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("?????????????????????");
        tv_title.setTextSize(17);
        tv_title.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
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
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, TaoKeHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    List<TaoKeTitleListModel.DataBean> dataBeanList = new ArrayList<>();
    List<TaoKeTitleListModel.DataBean.ChildBean> childBeanList = new ArrayList<>();

    public void getNet() {

        //???????????????????????? ?????????????????????

        Map<String, String> map = new HashMap<>();
        map.put("code", "00005");
        map.put("key", Urls.key);
        map.put("token", UserManager.getManager(this).getAppToken());
        map.put("type_id", "tbk_items");
//        NetUtils<TaoKeDetailList.DataBean> netUtils = new NetUtils<>();
//        netUtils.requestData(map, TAOKELIST, getActivity(), new JsonCallback<AppResponse<TaoKeDetailList.DataBean>>() {
//            @Override
//            public void onSuccess(Response<AppResponse<TaoKeDetailList.DataBean>> response) {
//               // Log.i("response_data", new Gson().toJson(response.body()));
//                dataBeanList.addAll(response.body().data);
//                taoKeListAdapter.setNewData(dataBeanList);
//                taoKeListAdapter.notifyDataSetChanged();
//
//
//            }
//        });

        Gson gson = new Gson();
        Log.e("map_data", gson.toJson(map));
        OkGo.<AppResponse<TaoKeTitleListModel.DataBean>>post(MSG)
                .tag(this)//
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<AppResponse<TaoKeTitleListModel.DataBean>>() {
                    @Override
                    public void onSuccess(Response<AppResponse<TaoKeTitleListModel.DataBean>> response) {
                        Log.i("response_data", new Gson().toJson(response.body().data));
                        dataBeanList.addAll(response.body().data);
                        setThisAdapter();
                        initMagicIndicator1(dataBeanList);
                    }
                });
    }


    @Override
    public void banner() {
        MyImageLoader mMyImageLoader = new MyImageLoader();
        banner.setImageLoader(mMyImageLoader);
        //  initMagicIndicator1(tagList);
        List imagePath = new ArrayList<>();
        imagePath.add(R.mipmap.midbanner_1);
        imagePath.add(R.mipmap.midbanner_2);
        imagePath.add(R.mipmap.midbanner_3);
        imagePath.add(R.mipmap.midbanner_4);
        imagePath.add(R.mipmap.midbanner_5);
        final List<String> imageString = new ArrayList<>();
        imageString.add("????????????30");
        imageString.add("????????????");
        imageString.add("????????????");
        imageString.add("iphone XS");
        imageString.add("??????play 4Tpro");
        banner.setBannerTitles(imageString);
        banner.setImages(imagePath);


        //????????????????????????
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                // startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("url", response.body().data.get(0).getBannerList().get(position).getHtml_url()));
                TaokeListActivity.actionStart(TaoKeHomeActivity.this, imageString.get(position));
            }
        });
    }

    /**
     * ???????????????
     */
    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }

}
