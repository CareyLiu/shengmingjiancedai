package com.smarthome.magic.activity.shengxian;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.smarthome.magic.R;
import com.smarthome.magic.app.BaseActivity;

import androidx.annotation.Nullable;

public class ShengxianWaitActivity extends BaseActivity {

    @Override
    public int getContentViewResId() {
        return R.layout.shengxian_act_wait;
    }

    @Override
    public boolean showToolBar() {
        return true;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("生鲜柜");
        tv_title.setTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.mipmap.back_white);
        mToolbar.setBackgroundColor(Color.parseColor("#F77C28"));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ShengxianWaitActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initImmersion() {
        super.initImmersion();
        mImmersionBar.with(this).statusBarColor(R.color.shengxian).init();
    }

    @Override
    public boolean isImmersive() {
        return true;
    }
}
