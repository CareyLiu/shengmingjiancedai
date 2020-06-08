package com.smarthome.magic.adapter.xin_tuanyou;

import androidx.annotation.Nullable;

import com.smarthome.magic.R;
import com.smarthome.magic.activity.xin_tuanyou.TuanYouList;
import com.smarthome.magic.activity.xin_tuanyou.YouZhanDetailsActivity;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.smarthome.magic.model.YouZhanDetailsModel;

import java.util.List;

//多少千米
public class DetailsQiangHaoAdapter extends BaseQuickAdapter<YouZhanDetailsModel.DataBean.OilPriceListBean.OilNosBean.GunNosBean, BaseViewHolder> {


    public DetailsQiangHaoAdapter(int layoutResId, @Nullable List<YouZhanDetailsModel.DataBean.OilPriceListBean.OilNosBean.GunNosBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, YouZhanDetailsModel.DataBean.OilPriceListBean.OilNosBean.GunNosBean item) {
        if (item.getIsSelect().equals("0")) {
            helper.setBackgroundRes(R.id.tv_text, R.drawable.item_youzhan_details_back);

        } else if (item.getIsSelect().equals("1")) {
            helper.setBackgroundRes(R.id.tv_text, R.drawable.item_youzhan_details_pink);
        }

        helper.addOnClickListener(R.id.constrain);
        helper.setText(R.id.tv_text,item.getGunNo());
    }
}
