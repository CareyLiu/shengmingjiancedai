package com.smarthome.magic.activity.zhinengjiaju.peinet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.flyco.roundview.RoundLinearLayout;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.zhinengjiaju.peinet.v1.EspTouchActivity;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.tools.NetworkUtils;

import butterknife.BindView;

public class PeiWangYinDaoPageActivity extends BaseActivity {

    @BindView(R.id.tv_lianjiewifi)
    TextView tvLianjiewifi;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.cl_none_wifi)
    ConstraintLayout clNoneWifi;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_changan_peiwang_huashu)
    TextView tvChanganPeiwangHuashu;
    @BindView(R.id.tv_queren_yishang)
    TextView tvQuerenYishang;
    @BindView(R.id.rll_kaishilianjie)
    RoundLinearLayout rllKaishilianjie;
    @BindView(R.id.cl_peiwang)
    ConstraintLayout clPeiwang;
    @BindView(R.id.nsl_scollview)
    NestedScrollView nslScollview;
    @BindView(R.id.tv_peiwang_help)
    TextView tvPeiwangHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rllKaishilianjie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ZhiNengJiaJuPeiWangActivity.actionStart(mContext);

            }
        });
        tvPeiwangHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeiWangHelpActivity.actionStart(mContext);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isWifi(mContext)) {
            clNoneWifi.setVisibility(View.VISIBLE);
            nslScollview.setVisibility(View.GONE);
        } else {
            clNoneWifi.setVisibility(View.GONE);
            nslScollview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getContentViewResId() {
        return R.layout.activity_pei_wang_yin_dao_page;
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


    /**
     * ????????????Activty????????????Activity
     *
     * @param context
     */
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PeiWangYinDaoPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
