package com.smarthome.magic.activity.shengxian;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.smarthome.magic.R;
import com.smarthome.magic.activity.GouMaiXiangQingActivity;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.config.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

public class ShengxianMainActivity extends BaseActivity {


    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    @Override
    public int getContentViewResId() {
        return R.layout.shengxian_act_main;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("生鲜柜");
        iv_rightTitle.setVisibility(View.VISIBLE);
        iv_rightTitle.setImageResource(R.mipmap.jij_shouye_saoyisao);
        tv_title.setTextSize(17);
        tv_title.setTextColor(getResources().getColor(R.color.black));
        mToolbar.setNavigationIcon(R.mipmap.backbutton);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_rightTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GouMaiXiangQingActivity.actionStart(mContext);
                saoyisao();
            }
        });
    }

    private void saoyisao() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            ShengxianSaoActivity.actionStart(mContext);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.xjqx), 0, Manifest.permission.CAMERA);
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShengxianMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBanner();
    }

    private void initBanner() {
        List<Integer> items = new ArrayList<>();
        items.add(R.mipmap.shengxian_banner1);
        items.add(R.mipmap.shengxian_banner2);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(items);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }
}
