package com.smarthome.magic.activity.chuwugui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.smarthome.magic.R;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.UIHelper;

import butterknife.BindView;

public class ChuWuGuiCunBaoJiFeiActivity extends BaseActivity {
    @BindView(R.id.iv_chuwugui_beijingtu)
    ImageView ivChuwuguiBeijingtu;
    @BindView(R.id.tv_baoyuejifei)
    TextView tvBaoyuejifei;
    @BindView(R.id.tv_linshicunbao)
    TextView tvLinshicunbao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvBaoyuejifei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.ToastMessage(mContext, "包月计费");



            }
        });
        tvLinshicunbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.ToastMessage(mContext, "临时存包");
            }
        });
    }

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_layout_cunbaojifeiye;
    }
}
