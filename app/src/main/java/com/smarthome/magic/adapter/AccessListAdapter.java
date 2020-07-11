package com.smarthome.magic.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.smarthome.magic.R;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.smarthome.magic.model.AccessListModel;

import java.util.List;


public class AccessListAdapter extends BaseQuickAdapter<AccessListModel.DataBean, BaseViewHolder> {

    public AccessListAdapter(int layoutResId, @Nullable List<AccessListModel.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AccessListModel.DataBean item) {
            helper.setVisible(R.id.constrain, true);
            helper.setVisible(R.id.tv_access, false);
            Glide.with(mContext).load(item.getUser_img_url()).into((ImageView) helper.getView(R.id.clv_image));
            helper.setText(R.id.tv_title, item.getUser_name());
            helper.setText(R.id.tv_pinglun_content, item.getUser_to_text());

    }
}