package com.smarthome.magic.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.smarthome.magic.R;
import com.smarthome.magic.model.CarBrand;
import com.smarthome.magic.model.SmartDevices;

/**
 * Created by Sl on 2019/6/12.
 *
 */

public class CarBrandAdapter extends ListBaseAdapter<CarBrand.DataBean.CcBean.ClBean> {

    public CarBrandAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_child_brand;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ImageView iv_pic = holder.getView(R.id.iv_pic);
        TextView tv_brand = holder.getView(R.id.tv_brand);

        Glide.with(mContext).load(getDataList().get(position).getPic_url()).into(iv_pic);
        tv_brand.setText(getDataList().get(position).getBrand_name());
    }
}
