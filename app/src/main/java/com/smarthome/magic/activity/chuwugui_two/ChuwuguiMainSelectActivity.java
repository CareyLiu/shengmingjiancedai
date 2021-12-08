package com.smarthome.magic.activity.chuwugui_two;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.smarthome.magic.R;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.config.PreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChuwuguiMainSelectActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.bt_youwangcunbao)
    TextView btYouwangcunbao;
    @BindView(R.id.bt_wuwangcunbao)
    TextView btWuwangcunbao;

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_act_main_select;
    }

    @Override
    public void initImmersion() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.statusBarDarkFont(true);
        mImmersionBar.init();
    }

    /**
     * 用于其他Activty跳转到该Activity
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ChuwuguiMainSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_back, R.id.bt_youwangcunbao, R.id.bt_wuwangcunbao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt_youwangcunbao:
                PreferenceHelper.getInstance(mContext).putString(App.CHUWUGUI_TYPE, "1");
                ChuwuguiShouyeActivity.actionStart(mContext);
                break;
            case R.id.bt_wuwangcunbao:
                PreferenceHelper.getInstance(mContext).putString(App.CHUWUGUI_TYPE, "2");
                ChuwuguiShouyeActivity.actionStart(mContext);
                break;
        }
    }
}
