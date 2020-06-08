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
import com.smarthome.magic.model.TaoKeTitleListModel;

import java.util.List;

public class TaoKeHomeSecondViewAdapter extends BaseQuickAdapter<TaoKeTitleListModel.DataBean.ChildBean, BaseViewHolder> {
    public TaoKeHomeSecondViewAdapter(int layoutResId, @Nullable List<TaoKeTitleListModel.DataBean.ChildBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, TaoKeTitleListModel.DataBean.ChildBean item) {
        //    helper.setText(R.id.tv_text, item.getName());
        //   Glide.with(mContext).load(item.getImg_url()).into((ImageView) helper.getView(R.id.iv_image));

//        if (helper.getAdapterPosition() == 0) {
//            setMargins(helper.getView(R.id.constrain), DensityUtil.dp2px(13), DensityUtil.dp2px(10), 0, 0);
//        }
        setMargins(helper.getView(R.id.constrain), DensityUtil.dp2px(10), DensityUtil.dp2px(10), 0, 0);
        helper.addOnClickListener(R.id.constrain);

        helper.setText(R.id.tv_text, item.getItem_name());
        Glide.with(mContext).load(item.getItem_pic()).into((ImageView) helper.getView(R.id.iv_image));


    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}
