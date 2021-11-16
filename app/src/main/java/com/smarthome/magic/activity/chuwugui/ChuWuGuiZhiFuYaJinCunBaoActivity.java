package com.smarthome.magic.activity.chuwugui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.smarthome.magic.R;
import com.smarthome.magic.app.BaseActivity;

import butterknife.BindView;

public class ChuWuGuiZhiFuYaJinCunBaoActivity extends BaseActivity {
    @BindView(R.id.tv_dizhi)
    TextView tvDizhi;
    @BindView(R.id.iv_shijian_icon)
    ImageView ivShijianIcon;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_xiangzi_guige_)
    TextView tvXiangziGuige;
    @BindView(R.id.tv_time_item)
    TextView tvTimeItem;
    @BindView(R.id.tv_daizhifu)
    TextView tvDaizhifu;
    @BindView(R.id.tv_zhifu_kaixiang)
    TextView tvZhifuKaixiang;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_zhifuyajincunbao;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        tv_title.setText("支付押金存包");
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
}
