package com.smarthome.magic.aakefudan.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarthome.magic.R;
import com.smarthome.magic.aakefudan.model.ZixunModel;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.smarthome.magic.model.ConsultModel;

import java.util.List;

import androidx.annotation.Nullable;

public class XiuliAdapter extends BaseQuickAdapter<ZixunModel.DataBean.ListBean, BaseViewHolder> {

    public XiuliAdapter(int layoutResId, @Nullable List<ZixunModel.DataBean.ListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZixunModel.DataBean.ListBean item) {
        helper.setText(R.id.tv_name, item.getInst_name());
        helper.setText(R.id.tv_juli, item.getMeter() + "KM");
        Glide.with(mContext).load(item.getUrl())
                .error(R.mipmap.logi_icon)
                .into((ImageView) helper.getView(R.id.iv_icon));
    }
}
