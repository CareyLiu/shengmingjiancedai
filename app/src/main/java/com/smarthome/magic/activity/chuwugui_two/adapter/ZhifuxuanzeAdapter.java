package com.smarthome.magic.activity.chuwugui_two.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui_two.model.SaomaModl;
import com.smarthome.magic.activity.shuinuan.Y;

import java.util.List;

import androidx.annotation.Nullable;

public class ZhifuxuanzeAdapter extends BaseQuickAdapter<SaomaModl.DataBean.SubStrategyListBean, BaseViewHolder> {
    private String charging_method;


    public ZhifuxuanzeAdapter(int layoutResId, @Nullable List<SaomaModl.DataBean.SubStrategyListBean> data, String charging_method) {
        super(layoutResId, data);
        this.charging_method = charging_method;
    }

    @Override
    protected void convert(BaseViewHolder helper, SaomaModl.DataBean.SubStrategyListBean item) {
        helper.setText(R.id.tv_guizi_name, item.getDevice_box_type_name());

        if (charging_method.equals("1")) {
            helper.setText(R.id.tv_money, "免费");
        } else if (charging_method.equals("2")){
            helper.setText(R.id.tv_money, item.getLcb_unit_price() + "元/次");
        }else {
            helper.setText(R.id.tv_money, item.getLcb_unit_price() + "元/小时");
        }


        View rl_main = helper.getView(R.id.rl_main);
        View iv_select = helper.getView(R.id.iv_select);

        if (item.isSelect()) {
            rl_main.setBackgroundResource(R.drawable.chuwuguitwo_guizi_select_sel);
            iv_select.setVisibility(View.VISIBLE);
        } else {
            rl_main.setBackgroundResource(R.drawable.chuwuguitwo_guizi_select_nor);
            iv_select.setVisibility(View.GONE);
        }
    }
}
