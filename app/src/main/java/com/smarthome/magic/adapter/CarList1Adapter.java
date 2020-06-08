package com.smarthome.magic.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarthome.magic.R;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.smarthome.magic.model.SmartDevice_car_0364;

import java.util.List;

/**
 * Created by Sl on 2019/6/12.
 *
 */

public class CarList1Adapter extends BaseQuickAdapter<SmartDevice_car_0364.DataBean, BaseViewHolder> {

    public CarList1Adapter( List<SmartDevice_car_0364.DataBean> datas) {
        super(R.layout.item_bind_car,datas);

    }

    @Override
    protected void convert(BaseViewHolder helper, SmartDevice_car_0364.DataBean item) {
        ImageView iv_icon = helper.getView(R.id.iv_icon);
        TextView tv_brand = helper.getView(R.id.tv_brand);
        TextView tv_state = helper.getView(R.id.tv_state);


        Glide.with(mContext).load(item.getCar_brand_url()).into(iv_icon);
        tv_brand.setText(item.getCar_brand_name());
        tv_state.setText(item.getPlate_number());

    }
}
