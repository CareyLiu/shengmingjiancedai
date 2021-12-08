package com.smarthome.magic.activity.chuwugui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui_two.model.GuanliyuanListModel;

import java.util.List;

import androidx.annotation.Nullable;

public class GuanliLiebiaoAdapter extends BaseQuickAdapter<GuanliyuanListModel.DataBean, BaseViewHolder> {
    public GuanliLiebiaoAdapter(int layoutResId, @Nullable List<GuanliyuanListModel.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GuanliyuanListModel.DataBean item) {
        helper.setText(R.id.tv_name,item.getDevice_name());
        helper.setText(R.id.tv_dizhi,item.getDevice_addr());

        String online_state = item.getOnline_state();
        if (online_state.equals("1")){
            helper.setText(R.id.tv_zhuangtai,"在线");
        }else {
            helper.setText(R.id.tv_zhuangtai,"离线");
        }
    }
}
