package com.smarthome.magic.activity.chuwugui.adapter;

import androidx.annotation.Nullable;

import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;

import java.util.List;

public class GuiZiLieBiaoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public GuiZiLieBiaoAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
