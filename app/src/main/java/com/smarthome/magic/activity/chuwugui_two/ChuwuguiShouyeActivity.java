package com.smarthome.magic.activity.chuwugui_two;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.LoginActivity;
import com.smarthome.magic.activity.chuwugui.GuanliLiebiaoActivity;
import com.smarthome.magic.app.App;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.config.PreferenceHelper;
import com.smarthome.magic.config.UserManager;
import com.smarthome.magic.fragment.MineFragment;
import com.smarthome.magic.get_net.Urls;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChuwuguiShouyeActivity extends BaseActivity {


    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.iv_head)
    ImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.bt_cunbao)
    ImageView bt_cunbao;
    @BindView(R.id.bt_qubao)
    ImageView bt_qubao;
    @BindView(R.id.bt_jilu)
    ImageView bt_jilu;
    @BindView(R.id.tv_kefu)
    TextView tv_kefu;
    @BindView(R.id.bt_guanliyuan)
    TextView bt_guanliyuan;

    @Override
    public int getContentViewResId() {
        return R.layout.chuwuguitwo_act_shouye;
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
        Intent intent = new Intent(context, ChuwuguiShouyeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        String isManage = UserManager.getManager(mContext).getIsManage();
        if (isManage.equals("1")) {
            bt_guanliyuan.setVisibility(View.VISIBLE);
        } else {
            bt_guanliyuan.setVisibility(View.GONE);
        }

        String userName = UserManager.getManager(mContext).getUserName();
        String user_phone = PreferenceHelper.getInstance(mContext).getString("user_phone", "");
        tv_name.setText(userName);
        tv_phone.setText(user_phone);

        String user_img_url = PreferenceHelper.getInstance(mContext).getString("user_img_url", "");
        Glide.with(mContext).load(user_img_url).into(iv_head);
    }

    @OnClick({R.id.rl_back, R.id.bt_cunbao, R.id.bt_qubao, R.id.bt_jilu, R.id.bt_guanliyuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.bt_cunbao:
                SaomacunbaoActivity.actionStart(mContext);
                break;
            case R.id.bt_qubao:
                QubaoliebiaoActivity.actionStart(mContext);
                break;
            case R.id.bt_jilu:
                ShiyongjiluActivity.actionStart(mContext);
                break;
            case R.id.bt_guanliyuan:
                GuanliLiebiaoActivity.actionStart(mContext);
                break;
        }
    }
}
