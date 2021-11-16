package com.smarthome.magic.activity.chuwugui;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.flyco.roundview.RoundTextView;
import com.smarthome.magic.R;
import com.smarthome.magic.app.BaseActivity;
import com.smarthome.magic.app.UIHelper;

import butterknife.BindView;

public class ChuWuGuiCunBaoActivity extends BaseActivity {

    @BindView(R.id.tv_jihaoxiangzi)
    RoundTextView tvJihaoxiangzi;
    @BindView(R.id.rl_dingbu_lanse)
    RelativeLayout rlDingbuLanse;
    @BindView(R.id.tv_xiangzi_bianhao)
    TextView tvXiangziBianhao;
    @BindView(R.id.rl_1)
    RelativeLayout rl1;
    @BindView(R.id.tv_jifeiguize)
    TextView tvJifeiguize;
    @BindView(R.id.rl_2)
    RelativeLayout rl2;
    @BindView(R.id.tv_cunfangshijian)
    TextView tvCunfangshijian;
    @BindView(R.id.rl_3)
    RelativeLayout rl3;
    @BindView(R.id.tv_cunfangweizhi)
    TextView tvCunfangweizhi;
    @BindView(R.id.rl_4)
    RelativeLayout rl4;
    @BindView(R.id.ll_dingbu_layout)
    LinearLayout llDingbuLayout;
    @BindView(R.id.rl_chongxinkaixiang)
    RelativeLayout rlChongxinkaixiang;
    @BindView(R.id.rl_wancheng)
    RelativeLayout rlWancheng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvXiangziBianhao.setText("填写编号");
        tvJifeiguize.setText("填写计费规则");
        tvCunfangweizhi.setText("填写存放位置");
        tvCunfangshijian.setText("填写存放时间");
        tvJihaoxiangzi.setText("填写几号箱子");

        rlChongxinkaixiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.ToastMessage(mContext, "重新开箱");
            }
        });

        rlWancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.ToastMessage(mContext, "完成");
            }
        });

    }

    @Override
    public int getContentViewResId() {
        return R.layout.chuwugui_layout_cunbao;
    }
}
