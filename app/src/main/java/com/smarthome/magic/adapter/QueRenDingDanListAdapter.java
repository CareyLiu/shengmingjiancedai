package com.smarthome.magic.adapter;

import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.smarthome.magic.R;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseQuickAdapter;
import com.smarthome.magic.baseadapter.baserecyclerviewadapterhelper.BaseViewHolder;
import com.smarthome.magic.model.Home;

import java.util.List;

public class QueRenDingDanListAdapter extends BaseQuickAdapter<Home.DataBean.IconListBean, BaseViewHolder> {
    public QueRenDingDanListAdapter(int layoutResId, @Nullable List<Home.DataBean.IconListBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Home.DataBean.IconListBean item) {


    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
