package com.smarthome.magic.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.smarthome.magic.R;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.smarthome.magic.model.ZhiNengHomeBean;

import java.util.ArrayList;
import java.util.List;

public class ZhiNengDeviceListAdapter extends BaseQuickAdapter<ZhiNengHomeBean.DataBean.DeviceBean, BaseViewHolder> {

    public ZhiNengDeviceListAdapter(int layoutResId, @Nullable List<ZhiNengHomeBean.DataBean.DeviceBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZhiNengHomeBean.DataBean.DeviceBean item) {
        Glide.with(mContext).load(item.getDevice_type_pic()).into((ImageView) helper.getView(R.id.iv_device));
        helper.setText(R.id.tv_device_name, item.getDevice_name());
        helper.setText(R.id.tv_room_name, item.getRoom_name());
        if (item.getOnline_state().equals("1")) {
            //设备在线
            helper.setBackgroundRes(R.id.iv_state, R.drawable.bg_zhineng_device_online);
            helper.setText(R.id.tv_state, "在线");
        } else {
            //设备离线
            helper.setBackgroundRes(R.id.iv_state, R.drawable.bg_zhineng_device_offline);
            helper.setText(R.id.tv_state, "离线");
        }

        if (item.getDevice_type().equals("11") || item.getDevice_type().equals("12") || item.getDevice_type().equals("13")
                || item.getDevice_type().equals("14") || item.getDevice_type().equals("15")) {
            helper.getView(R.id.iv_switch).setVisibility(View.INVISIBLE);
        } else {
            helper.getView(R.id.iv_switch).setVisibility(View.VISIBLE);
        }

        if (item.getWork_state().equals("1")) {
            helper.setBackgroundRes(R.id.iv_switch, R.mipmap.img_device_switch_open);
        } else if (item.getWork_state().equals("2")) {
            helper.setBackgroundRes(R.id.iv_switch, R.mipmap.img_device_switch_close);
        }
        helper.addOnClickListener(R.id.ll_content);
        helper.addOnClickListener(R.id.iv_switch);
    }
}
