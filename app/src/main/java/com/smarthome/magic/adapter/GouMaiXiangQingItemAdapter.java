package com.smarthome.magic.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;

import java.util.List;

public class GouMaiXiangQingItemAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public GouMaiXiangQingItemAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }



    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }

}
