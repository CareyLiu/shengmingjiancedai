package com.smarthome.magic.activity.chuwugui_two.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui_two.model.ShiyongjiluModel;
import com.smarthome.magic.activity.shuinuan.Y;

import java.util.List;

import androidx.annotation.Nullable;

public class ShiyongjiluAdapter extends BaseQuickAdapter<ShiyongjiluModel.DataBean, BaseViewHolder> {

    private String lc_deposit_state = "1";

    public void setLc_deposit_state(String lc_deposit_state) {
        this.lc_deposit_state = lc_deposit_state;
    }

    public ShiyongjiluAdapter(int layoutResId, @Nullable List<ShiyongjiluModel.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShiyongjiluModel.DataBean item) {
        TextView bt_jiekuan = helper.getView(R.id.bt_jiekuan);
        LinearLayout ll_qiankuan = helper.getView(R.id.ll_qiankuan);
        LinearLayout ll_yucun_time = helper.getView(R.id.ll_yucun_time);
        if (lc_deposit_state.equals("3")) {
            bt_jiekuan.setVisibility(View.VISIBLE);
            ll_qiankuan.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_money_qian, item.getTake_box_wait_pay_money() + "元");
        } else {
            bt_jiekuan.setVisibility(View.GONE);
            ll_qiankuan.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_time, item.getBegin_time());
        helper.setText(R.id.tv_device_name, item.getDevice_name());
        helper.setText(R.id.tv_box_name, item.getDevice_box_name() + "号");
        helper.setText(R.id.tv_guige, item.getDevice_box_type_name());

        String lc_billing_rules = item.getLc_billing_rules();
        if (lc_billing_rules.equals("3")) {
            ll_yucun_time.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_yucun_time, item.getDeposit_time());
        } else if (lc_billing_rules.equals("2")) {
            ll_yucun_time.setVisibility(View.GONE);
        } else {
            ll_yucun_time.setVisibility(View.GONE);
            helper.setText(R.id.tv_biaozhun, "无");
        }

        helper.setText(R.id.tv_money_zong, item.getMoney() + "元");
        helper.setText(R.id.tv_address, item.getDeposit_addr());

        String lc_day = item.getLc_day();
        String lc_hour = item.getLc_hour();
        String lc_minute = item.getLc_minute();
        String lc_second = item.getLc_second();
        String shiyongTime = lc_day + lc_hour + lc_minute + lc_second;
        helper.setText(R.id.tv_shiyong_time, shiyongTime);

        helper.addOnClickListener(R.id.bt_jiekuan);
    }
}
