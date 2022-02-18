package com.smarthome.magic.activity.shengming;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shengming.fragment.SmBaogaoFragment;
import com.smarthome.magic.activity.shengming.fragment.SmBaojingFragment;
import com.smarthome.magic.activity.shengming.fragment.SmJianceFragment;
import com.smarthome.magic.activity.shengming.fragment.SmWodeFragment;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SmHomeActivity extends BaseActivity {

    @BindView(R.id.vpg_content)
    NoScrollViewPager vpg_content;
    @BindView(R.id.iv_shengming_baogao)
    ImageView iv_shengming_baogao;
    @BindView(R.id.tv_shengming_baogao)
    TextView tv_shengming_baogao;
    @BindView(R.id.ll_shengming_baogao)
    LinearLayout ll_shengming_baogao;
    @BindView(R.id.iv_shengming_jiankong)
    ImageView iv_shengming_jiankong;
    @BindView(R.id.tv_shengming_jiankong)
    TextView tv_shengming_jiankong;
    @BindView(R.id.ll_shengming_jiankong)
    LinearLayout ll_shengming_jiankong;
    @BindView(R.id.iv_shengming_baojing)
    ImageView iv_shengming_baojing;
    @BindView(R.id.tv_shengming_baojing)
    TextView tv_shengming_baojing;
    @BindView(R.id.ll_shengming_baojing)
    LinearLayout ll_shengming_baojing;
    @BindView(R.id.iv_shengming_wode)
    ImageView iv_shengming_wode;
    @BindView(R.id.tv_shengming_wode)
    TextView tv_shengming_wode;
    @BindView(R.id.ll_shengming_wode)
    LinearLayout ll_shengming_wode;

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @Override
    public int getContentViewResId() {
        return R.layout.shengming_act_home;
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SmHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public boolean showToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initVpg();
        initHuidiao();
    }

    private void initHuidiao() {
        _subscriptions.add(toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Notice>() {
            @Override
            public void call(Notice message) {
                if (message.type == ConstanceValue.MSG_SHENGMIN_HOME_BACK) {
                    select(0);
                }
            }
        }));
    }

    private void initVpg() {
        List<Fragment> fragments = new ArrayList<>(4);
        SmBaogaoFragment baogaoFragment = new SmBaogaoFragment();
        SmJianceFragment jianceFragment = new SmJianceFragment();
        SmBaojingFragment baojingFragment = new SmBaojingFragment();
        SmWodeFragment wodeFragment = new SmWodeFragment();

        fragments.add(baogaoFragment);
//        fragments.add(jianceFragment);
//        fragments.add(baojingFragment);
//        fragments.add(wodeFragment);

        VpAdapter adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        vpg_content.setOffscreenPageLimit(1);
        vpg_content.setScroll(false);
        vpg_content.setAdapter(adapter);
    }

    @OnClick({R.id.ll_shengming_baogao, R.id.ll_shengming_jiankong, R.id.ll_shengming_baojing, R.id.ll_shengming_wode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_shengming_baogao:
                select(0);
                break;
            case R.id.ll_shengming_jiankong:
                select(1);
                break;
            case R.id.ll_shengming_baojing:
                select(2);
                break;
            case R.id.ll_shengming_wode:
                select(3);
                break;
        }
    }

    private void select(int item) {
        vpg_content.setCurrentItem(item);

//        iv_shengming_baogao.setImageResource(R.mipmap.yiyang_main_shouye_nor);
//        iv_shengming_jiankong.setImageResource(R.mipmap.yiyang_main_anfang_nor);
//        iv_shengming_baojing.setImageResource(R.mipmap.yiyang_main_nongye_nor);
//        iv_shengming_wode.setImageResource(R.mipmap.yiyang_main_xiaoxi_nor);

//        tv_shengming_baogao.setTextColor(Y.getColor(R.color.color_3));
//        tv_shengming_jiankong.setTextColor(Y.getColor(R.color.color_3));
//        tv_shengming_baojing.setTextColor(Y.getColor(R.color.color_3));
//        tv_shengming_wode.setTextColor(Y.getColor(R.color.color_3));
//
//        switch (item) {
//            case 0:
//                iv_shengming_baogao.setImageResource(R.mipmap.yiyang_main_shouye_sel);
//                tv_shengming_baogao.setTextColor(Y.getColor(R.color.color_main_yiyang));
//                break;
//            case 1:
//                iv_shengming_jiankong.setImageResource(R.mipmap.yiyang_main_anfang_sel);
//                tv_shengming_jiankong.setTextColor(Y.getColor(R.color.color_main_yiyang));
//                break;
//            case 2:
//                iv_shengming_baojing.setImageResource(R.mipmap.yiyang_main_xiaoxi_sel);
//                tv_shengming_baojing.setTextColor(Y.getColor(R.color.color_main_yiyang));
//                break;
//            case 3:
//                iv_shengming_wode.setImageResource(R.mipmap.yiyang_main_wd_sel);
//                tv_shengming_wode.setTextColor(Y.getColor(R.color.color_main_yiyang));
//                break;
//        }
    }

    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }
}
