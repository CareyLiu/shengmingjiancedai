package com.smarthome.magic.activity.chuwugui_two.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui_two.model.CunbaoModel;
import com.smarthome.magic.activity.shuinuan.Y;

import java.util.List;

import androidx.annotation.Nullable;

public class QubaoAdapter extends BaseQuickAdapter<CunbaoModel.DataBean, BaseViewHolder> {
    public QubaoAdapter(int layoutResId, @Nullable List<CunbaoModel.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CunbaoModel.DataBean item) {
        helper.setText(R.id.tv_xiangzi_name, item.getDevice_name());
        helper.setText(R.id.tv_dizhi, item.getDeposit_addr());
        helper.setText(R.id.tv_xiangzi_num, item.getDevice_box_name());
        helper.setText(R.id.tv_cunbao_timw, item.getDeposit_begin_time());
        helper.setText(R.id.tv_yucun_time, item.getDeposit_time());
        helper.setText(R.id.tv_shiyong_time, item.getUse_duration());

        String lc_billing_rules = item.getLc_billing_rules();
        String deposit_unit_money = item.getDeposit_unit_money();
        if (lc_billing_rules.equals("2")) {
            helper.setText(R.id.tv_biaozhun, deposit_unit_money + "元/每次");
            helper.setText(R.id.tv_yifu_money, item.getDeposit_pay_money() + "元");
        } else if (lc_billing_rules.equals("3")) {
            helper.setText(R.id.tv_biaozhun, deposit_unit_money + "元/小时");
            helper.setText(R.id.tv_yifu_money, item.getDeposit_pay_money() + "元");
        } else {
            helper.setText(R.id.tv_biaozhun, "免费");
            helper.setText(R.id.tv_yifu_money, "无");
        }

        String wait_pay_amount = item.getWait_pay_amount();
        View ll_daifu_money = helper.getView(R.id.ll_daifu_money);

        if (Y.getDouble(wait_pay_amount) > 0) {
            ll_daifu_money.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_daifu_money, item.getWait_pay_amount() + "元");
        } else {
            ll_daifu_money.setVisibility(View.GONE);
        }

        helper.addOnClickListener(R.id.bt_kaixiang);
        helper.addOnClickListener(R.id.bt_finish);
    }
}
