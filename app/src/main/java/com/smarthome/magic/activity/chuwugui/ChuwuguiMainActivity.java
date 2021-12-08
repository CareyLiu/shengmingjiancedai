package com.smarthome.magic.activity.chuwugui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui.config.ChuwuguiValue;
import com.smarthome.magic.activity.chuwugui.frag.CShouyeFragment;
import com.smarthome.magic.activity.chuwugui.frag.CWodeFragment;
import com.smarthome.magic.activity.shuinuan.Y;
import com.smarthome.magic.activity.yaokongqi.model.YaokongKeyModel;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.ConstanceValue;
import com.smarthome.magic.app.Notice;
import com.smarthome.magic.util.SoundPoolUtils;
import com.smarthome.magic.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class ChuwuguiMainActivity extends BaseActivity {

    @BindView(R.id.vp)
    NoScrollViewPager mVp;
    @BindView(R.id.iv_shouye)
    ImageView iv_shouye;
    @BindView(R.id.tv_shouye)
    TextView tv_shouye;
    @BindView(R.id.ll_shouye)
    LinearLayout ll_shouye;
    @BindView(R.id.iv_wode)
    ImageView iv_wode;
    @BindView(R.id.tv_wode)
    TextView tv_wode;
    @BindView(R.id.ll_wode)
    LinearLayout ll_wode;

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_act_main;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ChuwuguiMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
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
                if (message.type == ChuwuguiValue.MSG_CWG_BACK_SHOUYE) {
                    finish();
                } else if (message.type == ChuwuguiValue.MSG_CWG_BACK_WODE) {
                    clickShouye();
                }
            }
        }));
    }

    private void initVpg() {
        List<Fragment> fragments = new ArrayList<>(2);

        CShouyeFragment shouyeFragment = new CShouyeFragment();
        CWodeFragment wodeFragment = new CWodeFragment();

        fragments.add(shouyeFragment);
        fragments.add(wodeFragment);

        VpAdapter adapter = new VpAdapter(getSupportFragmentManager(), fragments);
        //禁用懒加载，不然每次切换页面都会重新获取数据
        mVp.setOffscreenPageLimit(2);
        //viewPage禁止滑动
        mVp.setScroll(false);
        mVp.setAdapter(adapter);

        clickShouye();
    }

    @OnClick({R.id.ll_shouye, R.id.ll_wode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_shouye:
                clickShouye();
                break;
            case R.id.ll_wode:
                clickWode();
                break;
        }
    }

    private void clickShouye() {
        mVp.setCurrentItem(0);

        tv_shouye.setTextColor(Color.parseColor("#4F94F1"));
        tv_wode.setTextColor(Y.getColor(R.color.color_9));

        iv_shouye.setImageResource(R.mipmap.chuwugui_main_shouye_s);
        iv_wode.setImageResource(R.mipmap.chuwugui_main_wd_n);
    }

    private void clickWode() {
        mVp.setCurrentItem(1);

        tv_wode.setTextColor(Color.parseColor("#4F94F1"));
        tv_shouye.setTextColor(Y.getColor(R.color.color_9));

//        iv_wode.setImageResource();
//        iv_shouye.setImageResource();
    }

    /**
     * view pager adapter
     */
    private static class VpAdapter extends FragmentStatePagerAdapter {
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

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
