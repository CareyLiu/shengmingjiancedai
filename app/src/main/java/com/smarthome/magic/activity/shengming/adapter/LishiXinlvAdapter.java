package com.smarthome.magic.activity.shengming.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smarthome.magic.R;
import com.smarthome.magic.activity.shengming.shengmingmodel.HistoryHrRrData;
import com.smarthome.magic.activity.shuinuan.Y;

import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;

public class LishiXinlvAdapter extends BaseQuickAdapter<HistoryHrRrData.DataBean.HrDataBean, BaseViewHolder> {
    public LishiXinlvAdapter(int layoutResId, @Nullable List<HistoryHrRrData.DataBean.HrDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryHrRrData.DataBean.HrDataBean item) {
        helper.setText(R.id.tv_cishu, item.getValue() + "次/分");
        Date time = Y.parseServerTime(item.getTime(), "yyyyMMddHHmmss");
        helper.setText(R.id.tv_time,Y.getTime(time));

        ImageView iv_icon = helper.getView(R.id.iv_icon);
        iv_icon.setImageResource(R.mipmap.shengming_xinlv_icon);
    }
}
