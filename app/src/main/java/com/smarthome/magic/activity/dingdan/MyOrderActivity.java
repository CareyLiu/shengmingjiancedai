package com.smarthome.magic.activity.dingdan;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.View;

import com.smarthome.magic.R;
import com.smarthome.magic.adapter.NewsFragmentPagerAdapter;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.fragment.OrderListFragment;
import com.smarthome.magic.model.MessageListBean;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MyOrderActivity extends BaseActivity {


    private static final String TAG = "MyOrderActivity";
    MagicIndicator magicIndicator;

    List<String> tagList;
    CustomViewPager viewPager;
    ArrayList<Fragment> messageListFragments = new ArrayList<>();
    List<MessageListBean> listBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        chooseXiangMu = getIntent().getStringExtra("chooseXiangMu");
        tagList = new ArrayList<>();
        tagList.add("??????");
        tagList.add("?????????");
        tagList.add("?????????");
        tagList.add("?????????");
        tagList.add("?????????");
        tagList.add("??????");
        tagList.add("?????????");
        tagList.add("?????????");
        tagList.add("??????/??????");
        tagList.add("????????????");
        magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator4);
        viewPager = findViewById(R.id.view_pager);
        setThisAdapter();
        initMagicIndicator1(tagList);

        if (chooseXiangMu.equals("??????")) {
            viewPager.setCurrentItem(0);
        } else if (chooseXiangMu.equals("?????????")) {
            viewPager.setCurrentItem(1);
        } else if (chooseXiangMu.equals("?????????")) {
            viewPager.setCurrentItem(3);
        } else if (chooseXiangMu.equals("?????????")) {
            viewPager.setCurrentItem(4);
        } else if (chooseXiangMu.equals("??????")) {
            viewPager.setCurrentItem(5);
        } else if (chooseXiangMu.equals("?????????")) {
            viewPager.setCurrentItem(6);
        }


    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_my_order);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initMagicIndicator1(final List<String> list) {
        MagicIndicator magicIndicator = findViewById(R.id.magic_indicator4);
        CommonNavigator commonNavigator = new CommonNavigator(MyOrderActivity.this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list == null ? 0 : list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(MyOrderActivity.this.getResources().getColor(R.color.black_666666));
                simplePagerTitleView.setSelectedColor(MyOrderActivity.this.getResources().getColor(R.color.orange_fa7e00));
                simplePagerTitleView.setText(list.get(index));
                //   App.scaleScreenHelper.loadViewSize(simplePagerTitleView, 35);
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
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(MyOrderActivity.this.getResources().getColor(R.color.orange_fa7e00));
                return linePagerIndicator;
            }
        });
        //commonNavigator.setAdjustMode(true);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

    }


    private void setThisAdapter() {
        // messageListFragments.clear();//??????
        int count = tagList.size();
        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
//            if (tagList.get(i).id != null) {
//                data("id", tagList.get(i) + "");
//            }
            //  data.putInt("type", list.get(i).type);
            data.putString("title", tagList.get(i));
            OrderListFragment newfragment = new OrderListFragment();
            newfragment.setArguments(data);
            messageListFragments.add(newfragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), messageListFragments);
        viewPager.setAdapter(mAdapetr);

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

    private String chooseXiangMu;

    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context, String chooseXiangMu) {
        Intent intent = new Intent(context, MyOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("chooseXiangMu", chooseXiangMu);
        context.startActivity(intent);
    }
}
