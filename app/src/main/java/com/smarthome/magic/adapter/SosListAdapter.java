package com.smarthome.magic.adapter;

import com.smarthome.magic.R;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseSectionQuickAdapter;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.smarthome.magic.model.AlarmListBean;

import java.util.List;

public class SosListAdapter extends BaseSectionQuickAdapter<AlarmListBean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public SosListAdapter(int layoutResId, int sectionHeadResId, List<AlarmListBean> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, AlarmListBean item) {

        helper.setText(R.id.tv_riqi, item.alarm_date);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlarmListBean item) {
        helper.setText(R.id.tv_shijianduan, item.device_state_name);
        helper.setText(R.id.tv_shijian, item.alerm_time);
        helper.setText(R.id.tv_menchuangzhuangtai, item.device_state_name);
    }
}
