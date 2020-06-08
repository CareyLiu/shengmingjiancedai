package com.smarthome.magic;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.smarthome.magic.model.DaLiBaoModel;

import java.util.List;

public class DaLiBaoAdapter extends BaseQuickAdapter<DaLiBaoModel.DataBean.WaresListBean, BaseViewHolder> {
    public DaLiBaoAdapter(int layoutResId, @Nullable List<DaLiBaoModel.DataBean.WaresListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DaLiBaoModel.DataBean.WaresListBean item) {
        Glide.with(mContext).load(item.getWares_photo_url()).into((ImageView) helper.getView(R.id.iv_image));
        helper.setText(R.id.tv_title, item.getShop_product_title());
        helper.setText(R.id.tv_jiage, item.getMoney_range());
    }
}
