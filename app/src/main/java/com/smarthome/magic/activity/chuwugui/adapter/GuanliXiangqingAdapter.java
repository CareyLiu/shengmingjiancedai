package com.smarthome.magic.activity.chuwugui.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.chuwugui_two.model.GuanliyuanGuiziModel;

import java.util.List;

import androidx.annotation.Nullable;

public class GuanliXiangqingAdapter extends BaseQuickAdapter<GuanliyuanGuiziModel.DataBean.BoxListBean, BaseViewHolder> {
    public GuanliXiangqingAdapter(int layoutResId, @Nullable List<GuanliyuanGuiziModel.DataBean.BoxListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GuanliyuanGuiziModel.DataBean.BoxListBean item) {
        TextView tv_guizi_name = helper.getView(R.id.tv_guizi_name);
        tv_guizi_name.setText(item.getDevice_box_name());
        String box_state = item.getBox_state();
        if (box_state.equals("1")) {
            tv_guizi_name.setBackgroundResource(R.drawable.chuwugui_guizi_kongxianzhong);
        } else if (box_state.equals("2")) {
            tv_guizi_name.setBackgroundResource(R.drawable.chuwugui_guizi_shiyongzhong);
        } else {
            tv_guizi_name.setBackgroundResource(R.drawable.chuwugui_guizi_jinyong);
        }
    }
}
